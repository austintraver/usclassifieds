// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

// let Promise = require('promise');
const algoliasearch = require('algoliasearch');

var serviceAccount = require("./serviceKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://usclassifieds-c83d5.firebaseio.com"
});


exports.syncListingToAlgolia = functions.database.ref('/listings/{userid}/{listingid}')
  .onWrite(async (change, context) => {
    console.log('add listing from database');
    const listingid = context.params.listingid;
    const userid = context.params.userid;
    
    if (change.after.val()) {
          const getListingParamsPromise = admin.database()
        .ref(`/listings/${userid}/${listingid}`).once('value');

      const results = await Promise.all([getListingParamsPromise]);

      listingSnapshot = results[0];
      const data = {
        objectID: listingid,
        description: listingSnapshot.child("description").val(), 
        latitude: listingSnapshot.child("latitude").val(),
        longitude: listingSnapshot.child("longitude").val(),
        ownerEmail: listingSnapshot.child("ownerEmail").val(),
        ownerID: listingSnapshot.child("ownerID").val(),
        ownerName: listingSnapshot.child("ownerName").val(),
        price: listingSnapshot.child("price").val(),
        sold: listingSnapshot.child("sold").val(),
        storageReference: listingSnapshot.child("storageReference").val(),
        title: listingSnapshot.child("title").val(),
        uuid: listingSnapshot.child("uuid").val()
  };
return addToAlgolia(data, 'listings')
 .then(res => console.log('SUCCESS ALGOLIA listing ADD', res))
 .catch(err => console.log('ERROR ALGOLIA listing ADD', err));
    }
    else if (!change.after.val()) {
      return removeFromAlgolia(listingid, 'listings')
    .then(res => console.log('SUCCESS ALGOLIA listing remove', res))
    .catch(err => console.log('ERROR ALGOLIA listing remove', err));
    }
    return (console.log('cant decide if add or remove'))
});

// helper functions for create, edit and delete in Firestore to replicate this in Algolia
function addToAlgolia(object, indexName) {
 console.log('GETS IN addToAlgolia')
 console.log('object', object)
 console.log('indexName', indexName)
 const ALGOLIA_ID = 'VTODAQDVW5';
 const ALGOLIA_ADMIN_KEY = 'a06f0b0003ffe4d67f6fe6d89fa05f9a';
 const client = algoliasearch(ALGOLIA_ID, ALGOLIA_ADMIN_KEY);
 const index = client.initIndex(indexName);
return new Promise((resolve, reject) => {
  index.addObject(object)
  .then(res => {  resolve(res); return console.log('res GOOD', res);})
  .catch(err => {  reject(err); return console.log('err BAD', err);});
 });
}

function removeFromAlgolia(objectID, indexName) {
 const ALGOLIA_ID = 'VTODAQDVW5';
 const ALGOLIA_ADMIN_KEY = 'a06f0b0003ffe4d67f6fe6d89fa05f9a';
 const client = algoliasearch(ALGOLIA_ID, ALGOLIA_ADMIN_KEY);
 const index = client.initIndex(indexName);
return new Promise((resolve, reject) => {
  index.deleteObject(objectID)
  .then(res => {  resolve(res); return console.log('res GOOD', res); })
  .catch(err => {  reject(err); return console.log('err BAD', err);});
 });
}





/**
 * Requested whenever we handle user changes in the database. This will store our user in the backend and is used
 * for convenience when we need to manage users. 
 * */ 


exports.createUser = functions.database.ref('/users/{userid}')
  .onWrite(async (change, context) => {
    
    const userid = context.params.userid;
    if (change.after.val()) {
      console.log('in create user');
      

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
    else {
      console.log('in delete user');
      if (!change.after.val()) {
        admin.auth().deleteUser(userid)
        .then(() => {
          // See the UserRecord reference doc for the contents of userRecord.
          return console.log('Successfully deleted user:', userid);
        })
        .catch(error => {
          return console.log('Error deleting user:', error);
        });
      }
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
        
        // remove user request list
        requestingRef = admin.database().ref(`/users/${requesting}/outgoingFriendRequests/${requested}`);
        requestingRef.remove();

        // remove user request list
        requestingRef = admin.database().ref(`/users/${requested}/incomingFriendRequests/${requesting}`);
        requestingRef.remove();
        
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
        console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
      console.log('Fetched requester profile', requester);

      let messageBody;
      if (friendRequestType === 'request') {
        messageBody = `${requester.displayName} has sent you a friend request.`;
        
        // adds outgoing friend request
        requestingRef = admin.database().ref(`/users/${requesting}/outgoingFriendRequests`);
        requestingRef.update({
            [requested]: "true"
        });

        console.log('got this far');

        // adds incoming friend request
        requestedRef = admin.database().ref(`/users/${requested}/incomingFriendRequests`);
        requestedRef.update({
            [requesting]: "true"
        });
      }
      else if (friendRequestType === 'accept') {
        messageBody = `${requester.displayName} has accepted your friend request.`;

        // adds friend to outgoing
        requestingRef = admin.database().ref(`/users/${requesting}/friends`);
        requestingRef.update({
            [requested]: "true"
        }); 

        // remove user request list
        requestingRef = admin.database().ref(`/users/${requested}/outgoingFriendRequests/${requesting}`);
        requestingRef.remove();

        
        // adds incoming friend request
        requestedRef = admin.database().ref(`/users/${requested}/friends`);
        requestedRef.update({
            [requesting]: "true"
        });

        // remove user request list
        requestingRef = admin.database().ref(`/users/${requesting}/incomingFriendRequests/${requested}`);
        requestingRef.remove();



        //removes actual friend requests
        requestingRef = admin.database().ref(`/friendrequests/${requested}`);
        requestingRef.remove();

        requestingRef = admin.database().ref(`/friendrequests/${requesting}`);
        requestingRef.remove();
            
      }

      else if (friendRequestType === 'reject') {


        // remove user request list
        requestingRef = admin.database().ref(`/users/${requested}/outgoingFriendRequests/${requesting}`);
        requestingRef.remove();


        // remove user request list
        requestingRef = admin.database().ref(`/users/${requesting}/incomingFriendRequests/${requested}`);
        requestingRef.remove();



        //removes actual friend requests
        requestingRef = admin.database().ref(`/friendrequests/${requested}`);
        requestingRef.remove();

        requestingRef = admin.database().ref(`/friendrequests/${requesting}`);
        requestingRef.remove();
        return console.log('finished reject function');
      }
      
        console.log('body ', messageBody);

      // Notification details.
      const payload = {
        notification: {
          title: 'You have a new friend request!',
          body: messageBody,
        }
      };

      // Listing all tokens as an array.
      tokens = Object.values(tokensSnapshot.val());
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