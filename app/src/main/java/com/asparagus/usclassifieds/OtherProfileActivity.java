package com.asparagus.usclassifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OtherProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        Intent intent = getIntent();
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
        TODO
        Normally, the GlobalHelper's getUser() method
        retrieves the logged-in user's information. We
        need to have the GlobalHelper refer to the user
        whose page is the one currently being viewed
         */

        textView = findViewById(R.id.other_first_name);
        textView.setText(GlobalHelper.getUser().getFirstName());

        textView = findViewById(R.id.other_last_name);
        textView.setText(GlobalHelper.getUser().getLastName());

        textView = findViewById(R.id.other_phone_number);
        textView.setText(GlobalHelper.getUser().getPhone());

        textView = findViewById(R.id.other_street_number);
        textView.setText(GlobalHelper.getUser().getStreetNumber());

        textView = findViewById(R.id.other_street_name);
        textView.setText(GlobalHelper.getUser().getStreetName());

        textView = findViewById(R.id.other_city_name);
        textView.setText(GlobalHelper.getUser().getCity());

        textView = findViewById(R.id.other_state_code);
        textView.setText(GlobalHelper.getUser().getState());

        textView = findViewById(R.id.other_zip_code);
        textView.setText(GlobalHelper.getUser().getZipCode());

        textView = findViewById(R.id.other_description);
        textView.setText(GlobalHelper.getUser().getDescription());

        /*
         TODO
         1. If the two users are not friends AND there is NOT an outstanding friend request

            a) The button should display the string "Add Friend"
            b) Clicking on the button should submit a friend request
                i) change the button's string to display "Cancel Friend Request"
                ii) add that user as a friend

         2. If the two users are not friends AND there IS an outstanding friend request

            a) If the friend request was submit by the currently logged-in user,
                i) then button should display the string "Cancel Friend Request"

            b) If the friend request was submit by the user whose profile we are viewing
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
                ii) delete that user as a member of the friends list array
                iii) remove the entry from the database that was storing their friendship

         */
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_button:
                /*
                TODO

                 */
                break;
            case R.id.other_listings_button:
                /*
                 TODO

                 */
                break;
        }
    }
}
