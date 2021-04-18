package com.mygdx.game.ECS.entities;

import com.mygdx.game.ECS.entities.Fonts.HealthFont;
import com.mygdx.game.ECS.entities.Fonts.TextFont;
import com.mygdx.game.ECS.entities.Players.AbstractPlayer;
import com.mygdx.game.ECS.entities.Players.DefaultPlayer;
import com.mygdx.game.ECS.entities.Players.SpeedyPlayer;
import com.mygdx.game.ECS.entities.Projectiles.AbstractProjectile;
import com.mygdx.game.ECS.entities.Projectiles.BouncerProjectile;
import com.mygdx.game.ECS.entities.Projectiles.DefaultProjectile;
import com.mygdx.game.ECS.entities.Projectiles.SpeedyProjectile;
import com.mygdx.game.ECS.entities.Projectiles.SplitterProjectile;
import com.mygdx.game.managers.ScreenManager;
import com.mygdx.game.states.screens.AbstractScreen;

import java.util.HashMap;


/**
 * This class is responsible for keeping a HashMap of available default construction of certain entities
 * Use this class in order when creating these entities -> so you don't need to remember all the different variants
 **/
public class EntityCreator {

    // Create enums for different classes of entities
    public enum PLAYERS {
        DEFAULT,
        SPEEDY
    }

    public enum PROJECTILES {
        DEFAULT,
        SPLITTER,
        BOUNCER,
        SPEEDY
    }

    // Create HashMaps
    private HashMap<PLAYERS, AbstractPlayer> players;
    private HashMap<PROJECTILES, AbstractProjectile> projectiles;

    public EntityCreator() {
        this.initClasses();
    }

    private void initClasses() {
        this.players = new HashMap<PLAYERS, AbstractPlayer>();
        this.projectiles = new HashMap<PROJECTILES, AbstractProjectile>();

        this.players.put(PLAYERS.DEFAULT, new DefaultPlayer());
        this.players.put(PLAYERS.SPEEDY, new SpeedyPlayer());
        this.projectiles.put(PROJECTILES.DEFAULT, new DefaultProjectile());
        this.projectiles.put(PROJECTILES.BOUNCER, new BouncerProjectile());
        this.projectiles.put(PROJECTILES.SPLITTER, new SplitterProjectile());
        this.projectiles.put(PROJECTILES.SPEEDY, new SpeedyProjectile());
    }

    public AbstractPlayer getPlayerClass(PLAYERS playerClass) {
        return this.players.get(playerClass);
    }

    public AbstractProjectile getProjectileClass(PROJECTILES projectileClass) {
        return this.projectiles.get(projectileClass);
    }

    public AbstractEntity getHealthFont() {
        return new HealthFont();
    }

    public AbstractEntity getTextFont() {
        return new TextFont();
    }

}
