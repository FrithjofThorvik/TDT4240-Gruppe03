package com.mygdx.game.ECS.EntityUtils;

import com.mygdx.game.ECS.EntityUtils.templates.AbstractEntity;
import com.mygdx.game.ECS.EntityUtils.templates.Fonts.HealthFont;
import com.mygdx.game.ECS.EntityUtils.templates.Fonts.TextFont;
import com.mygdx.game.ECS.EntityUtils.templates.Players.AbstractPlayer;
import com.mygdx.game.ECS.EntityUtils.templates.Players.DefaultPlayer;
import com.mygdx.game.ECS.EntityUtils.templates.Players.SpeedyPlayer;
import com.mygdx.game.ECS.EntityUtils.templates.Projectiles.AbstractProjectile;
import com.mygdx.game.ECS.EntityUtils.templates.Projectiles.BouncerProjectile;
import com.mygdx.game.ECS.EntityUtils.templates.Projectiles.DefaultProjectile;
import com.mygdx.game.ECS.EntityUtils.templates.Projectiles.SpeedyProjectile;
import com.mygdx.game.ECS.EntityUtils.templates.Projectiles.SplitterProjectile;

import java.util.HashMap;


/**
 * This class is responsible for keeping a HashMap of available default construction of certain entities
 * Use this class in order when creating these entities -> so you don't need to remember all the different variants
 **/
public class EntityTemplateMapper {

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
    private HashMap<PLAYERS, com.mygdx.game.ECS.EntityUtils.templates.Players.AbstractPlayer> players;
    private HashMap<PROJECTILES, com.mygdx.game.ECS.EntityUtils.templates.Projectiles.AbstractProjectile> projectiles;

    public EntityTemplateMapper() {
        this.initClasses();
    }

    private void initClasses() {
        this.players = new HashMap<PLAYERS, com.mygdx.game.ECS.EntityUtils.templates.Players.AbstractPlayer>();
        this.projectiles = new HashMap<PROJECTILES, com.mygdx.game.ECS.EntityUtils.templates.Projectiles.AbstractProjectile>();

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
