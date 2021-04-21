package com.mygdx.game.firebase;

import com.badlogic.gdx.utils.Array;

public interface FirebaseInterface {

    void SetOnValueChangedListener();

    void SetValueInDb(String target, String value);

    void SetHighScore(int value);

    Array<Integer> GetHighScore();

}
