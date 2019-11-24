package com.asparagus.usclassifieds;

import com.google.firebase.database.DataSnapshot;

public interface OnGetDataListener {
    void onSuccess(DataSnapshot dataSnapshot);

    void onStart();

    void onFailure();
}
