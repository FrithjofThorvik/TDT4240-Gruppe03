package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

public interface FirebaseInterface {

    public void SetOnValueChangedListener();

    public void SetValueInDb(String target, String value);

    public void SetHighScore(int value);

    public Array<Integer> GetHighScore();

}
