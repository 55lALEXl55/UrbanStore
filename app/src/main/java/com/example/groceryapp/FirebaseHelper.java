package com.example.groceryapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {

    private DatabaseReference databaseReference;

    public FirebaseHelper(String ref) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference(ref);
    }

    public void writeData(String key, Object value) {
        databaseReference.child(key).setValue(value);
    }

    public void readData(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }
}
