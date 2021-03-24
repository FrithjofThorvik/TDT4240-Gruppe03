package com.mygdx.game.ECS.systems;

import java.text.DecimalFormat;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.game.ECS.States.Context;
import com.mygdx.game.ECS.States.RoundState;
import com.mygdx.game.ECS.States.RoundSwitch;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.MovementControlComponent;
import com.mygdx.game.ECS.components.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.PowerbarComponent;
import com.mygdx.game.ECS.components.RenderableComponent;
import com.mygdx.game.ECS.components.TakeAimComponent;

//This system controls which player has the turn
//This system is also responsible how long the rounds are, and how much time between each round
public class GameplaySystem extends EntitySystem {
    //Create a new RoundState, the Roundstate controls logic for changing between players
    public RoundState roundState = new RoundSwitch();

    //The context is used by the roundState (the context contains variables)
    public Context context = new Context();

    //Arrays of enitities that this system act on
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> timers;

    public GameplaySystem() {
    }

    // Will be called automatically by the engine -> fetches entities
    public void addedToEngine(Engine e) {
        players = e.getEntitiesFor(Family.all(PlayerComponent.class).get()); // Get all entities that are players
        timers = e.getEntitiesFor(Family.all(FontComponent.class).get()); // Get all entities that are fonts
    }

    // Will be called by the engine automatically -> updates similarly to libgdx update function
    public void update(float deltaTime) {
        //Get the timer entity
        Entity timer = timers.get(0);

        //Update the roundState
        roundState=context.getState();

        //Execute the roundState logic (which is dependent on which state the game is in)
        roundState.doSomething(context, deltaTime, players, timer);
    }
}
