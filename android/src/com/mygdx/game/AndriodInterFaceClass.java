package com.mygdx.game;

import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;

import com.badlogic.gdx.utils.Array;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class AndriodInterFaceClass implements FirebaseInterface {
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference highScoreRef;
    private Array<Integer> highscores;

    public AndriodInterFaceClass() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        highScoreRef = database.getReference().child("Highscore");
        highscores = new Array<Integer>();
    }

    @Override
    // function will notify when a value is changed
    public void SetOnValueChangedListener() {
        myRef.addValueEventListener(new ValueEventListener() {
            // Read from the database

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        highScoreRef.addValueEventListener(new ValueEventListener() {
            // Read from the database

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int i = 0;
                Array<Integer> temp = new Array<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    temp.insert(i, snapshot.getValue(Integer.class));
                    i++;
                }
                highscores = temp;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    //this function will set value in the database
    @Override
    public void SetValueInDb(String target, String value) {
        myRef = database.getReference(target);
        myRef.setValue(value);
    }

    @Override
    public void SetHighScore(int value) {
        highScoreRef.child("score" + highscores.size).setValue(value);
    }

    @Override
    public Array<Integer> GetHighScore() {
        return highscores;
    }


}
