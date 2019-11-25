package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class OtherProfileActivity extends Activity {

    private static User user = GlobalHelper.user;
    private User otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        Intent intent = getIntent();
        otherUser = (User) intent.getSerializableExtra("other_user");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();

        TextView textView;

        /*
        Normally, the GlobalHelper's getUser() method
        retrieves the logged-in person's information. We
        need to have the GlobalHelper refer to the person
        whose page is the one currently being viewed
         */

        textView = findViewById(R.id.other_first_name);
        textView.setText(otherUser.getFirstName());

        textView = findViewById(R.id.other_last_name);
        textView.setText(otherUser.getLastName());

        textView = findViewById(R.id.other_phone_number);
        textView.setText(otherUser.getPhone());

        textView = findViewById(R.id.other_street_number);
        textView.setText(otherUser.getStreetNumber());

        textView = findViewById(R.id.other_street_name);
        textView.setText(otherUser.getStreetName());

        textView = findViewById(R.id.other_city_name);
        textView.setText(otherUser.getCity());

        textView = findViewById(R.id.other_state_code);
        textView.setText(otherUser.getState());

        textView = findViewById(R.id.other_zip_code);
        textView.setText(otherUser.getZipCode());

        textView = findViewById(R.id.other_description);
        textView.setText(otherUser.getDescription());

        /*
         TODO
         1. If the two users are not friends AND there is NOT an outstanding friend request

            a) The button should display the string "Add Friend"
            b) Clicking on the button should submit a friend request
                i) change the button's string to display "Cancel Friend Request"
                ii) add that person as a friend

         2. If the two users are not friends AND there IS an outstanding friend request

            a) If the friend request was submit by the currently logged-in person,
                i) then button should display the string "Cancel Friend Request"

            b) If the friend request was submit by the person whose profile we are viewing
                i) then the button should display the string "Respond to Friend Request"
                ii) clicking on the button should prompt a dialogue with three choices
                    一) "Accept"
                        *) close the dialogue box
                        *) change the button's string to "Remove Friend"
                        *) store an existing relationship among the two friends in the database
                    二) "Reject" which will... closes the dialogue box and
                        *) close the dialogue box
                        *) change the button's string to "Add Friend"
                        *) delete the outstanding friend request from the database
                    三) "Cancel" which will...
                        *) close the dialogue box
                        *) and do nothing else

         3. If the two users are already friends,
            a) The button should display the string "Remove Friend"
            b) Clicking on the button should
                i) change the button's string to display "Add Friend"
                ii) delete that person as a member of the friends list array
                iii) remove the entry from the database that was storing their friendship

         */
    }

    public void onClick(View v) {
        System.out.println("clicked");
        switch (v.getId()) {
            case R.id.friend_button:
                System.out.println("user id: " + user.userID);
                final Map<String, String> reqType = new HashMap<String, String>() {{
                    put(otherUser.userID, "request");
                }};
                HashMap<String, Object> result = new HashMap<String, Object>() {{
                    put(user.userID, reqType);
                }};
                // If not friends
                System.out.println("user status: " + user.friends.get(otherUser.userID));
                if (Objects.equals(user.friends.get(otherUser.userID), null)) {
                    System.out.println("aren't friends: " + user.userID);
                    FirebaseDatabase.getInstance().getReference("friendrequests").child(user.userID).setValue(result);
                }
                Map<String, Object> userValues = user.toMap();
                FirebaseDatabase.getInstance().getReference("users").child(user.userID).setValue(userValues);
                break;
            /* User clicks the view listings button for the other otherUser */
            case R.id.other_listings_button:
                /*
                TODO
                    The home activitiy is launched
                    The filter is set to users
                    The content of that filter is set to that otherUser's name
                    Hopefully the listings appear
                */
                break;
        }
    }
}
