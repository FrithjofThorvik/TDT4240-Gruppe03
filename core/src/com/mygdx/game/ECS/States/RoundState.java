package com.mygdx.game.ECS.States;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import java.text.DecimalFormat;

public interface RoundState {
    public void doSomething(Context context, float deltaTime, ImmutableArray<Entity> players, Entity timer);
}
