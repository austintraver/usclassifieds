// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

var serviceAccount = require("./serviceKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://usclassifieds-c83d5.firebaseio.com"
});


/**
 * Requested whenever we create a user in the database. This will store our user in the backend and is used
 * for convenience when we need to manage users. 
 * */ 


exports.createUser = functions.database.ref('/users/{userid}')
  .onWrite(async (change, context) => {
    console.log('in create user');
    if (change.after.val()) {
      
      const userid = context.params.userid;

      const getUserParamsPromise = admin.database()
          .ref(`/users/${userid}`).once('value');

      const results = await Promise.all([getUserParamsPromise]);

      userSnapshot = results[0];
      
      const requid = context.params.userid
      const reqemail = userSnapshot.child("email").val();
      const reqdisplayName = userSnapshot.child("firstName").val() + ' ' + userSnapshot.child("lastName").val();

      admin.auth().createUser({
        uid: requid,
        email: reqemail,
        displayName: reqdisplayName 
      })
        .then(userRecord => {
          // See the UserRecord reference doc for the contents of userRecord.
          return console.log('Successfully created new user:', userRecord.toJSON());
        })
        .catch(error => {
          return console.log('Error creating new user:', error);
        });
    }
});

/**
 * Triggered whenever a user is deleted from the database.
 * */

exports.deleteUser = functions.database.ref('/users/{userid}')
    .onWrite(async (change, context) => {
  // Grab the text parameter.
  const requid = context.params.userid;
  

console.log('in delete user');
  if (!change.after.val()) {
    admin.auth().deleteUser(requid)
    .then(() => {
      // See the UserRecord reference doc for the contents of userRecord.
      return console.log('Successfully deleted user:', requid);
    })
    .catch(error => {
      return console.log('Error deleting user:', error);
    });
  }
});


/**
 * Triggers when a user gets a new friend request and sends a notification.
 *
 * Followers add a flag to `/friendrequests/{requesting}/{requested}`.
 * Users save their device notification tokens to `/users/{requested}/notificationTokens/{notificationToken}`.
 */
exports.sendFriendRequestNotification = functions.database.ref('/friendrequests/{requesting}/{requested}')
    .onWrite(async (change, context) => {
      const requesting = context.params.requesting;
      const requested = context.params.requested;
      // If un-follow we exit the function.
      if (!change.after.val()) {
        return console.log('Friend Request from ', requesting, 'to the following person has been removed: ', requested);
      }
      console.log('We have a new friend request from:', requesting, 'for user:', requested);

      // Get the list of device notification tokens.
      const getDeviceTokensPromise = admin.database()
          .ref(`/users/${requested}/notificationTokens`).once('value');

      // Get the follower profile.
      const getRequestingProfilePromise = admin.auth().getUser(requesting)
        .then(userRecord => {
          // See the UserRecord reference doc for the contents of userRecord.
          console.log('Successfully fetched user data:', userRecord.toJSON());
          return userRecord;
        })
        .catch(error => {
          return console.log('Error fetching user data:', error);
        });

      const getFriendRequestPromise = admin.database()
        .ref(`friendrequests/${requesting}/${requested}`).once('value');


      // The snapshot to the user's tokens.
      let tokensSnapshot;

      // The array containing all the user's tokens.
      let tokens;

      let friendRequestType;

      const results = await Promise.all([getDeviceTokensPromise, getRequestingProfilePromise, getFriendRequestPromise]);
      tokensSnapshot = results[0];
      const requester = results[1];
      friendRequestType = results[2].val();

      // Check if there are any device tokens.
      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
      console.log('Fetched requester profile', requester);

      let messageBody;
      if (friendRequestType === 'request') {
        messageBody = `${requester.displayName} has sent you a friend request.`;
      }
      else if (friendRequestType === 'accept') {
        messageBody = `${requester.displayName} has accepted your friend request.`;
      }
      console.log('body ', messageBody);

      // Notification details.
      const payload = {
        notification: {
          title: 'You have a new friend request!',
          body: messageBody,
          //icon: follower.photoURL
        }
      };

      // Listing all tokens as an array.
      tokens = Object.keys(tokensSnapshot.val());
      // Send notifications to all tokens.
      const response = await admin.messaging().sendToDevice(tokens, payload);
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        console.log('response received with following message: ',response);
        console.log('success count: ',response.successCount);
        console.log('canonical reg token: ',result.canonicalRegistrationToken);

        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
          // Cleanup the tokens who are not registered anymore.
          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });