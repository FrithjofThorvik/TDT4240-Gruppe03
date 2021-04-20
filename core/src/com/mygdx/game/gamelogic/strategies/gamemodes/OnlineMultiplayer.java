package com.mygdx.game.gamelogic.strategies.gamemodes;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Application;
import com.mygdx.game.ECS.components.FontComponent;
import com.mygdx.game.ECS.components.HealthComponent;
import com.mygdx.game.ECS.components.flags.HealthDisplayerComponent;
import com.mygdx.game.ECS.components.flags.MovementControlComponent;
import com.mygdx.game.ECS.components.flags.PlayerComponent;
import com.mygdx.game.ECS.components.PositionComponent;
import com.mygdx.game.ECS.components.projectiles.ProjectileComponent;
import com.mygdx.game.ECS.components.flags.isAimingComponent;
import com.mygdx.game.ECS.components.flags.isShootingComponent;
import com.mygdx.game.ECS.EntityUtils.EntityTemplateMapper;
import com.mygdx.game.ECS.systems.AimingSystem;
import com.mygdx.game.ECS.systems.MovementSystem;
import com.mygdx.game.ECS.systems.ShootingSystem;
import com.mygdx.game.gamelogic.states.GameStateManager;
import com.mygdx.game.gamelogic.states.ScreenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.mygdx.game.utils.GameController.CM;
import static com.mygdx.game.ECS.managers.ECSManager.ECSManager;
import static com.mygdx.game.gamelogic.states.GameStateManager.GSM;
import static com.mygdx.game.gamelogic.states.ScreenManager.SM;
import static com.mygdx.game.utils.B2DConstants.PPM;
import static com.mygdx.game.utils.GameConstants.ROUND_TIME;
import static com.mygdx.game.utils.GameConstants.START_GAME_TIME;
import static com.mygdx.game.utils.GameConstants.TIME_BETWEEN_ROUNDS;

public class OnlineMultiplayer implements GameMode {
    private final DecimalFormat df = new DecimalFormat("0.0"); // Format timer that displays on the time on the screen
    public Socket socket;

    String mapFile = "scifi.tmx";
    String mapTexture = "mapsprite.png";

    boolean stopTimer = false; // If the timer should increment with time
    float timer = 0; // The timer
    float switchTime = 0; // The time used to switch between rounds -> is updated in some states

    float airBorneTime = 0; // How long has projectiles been airborne
    float timeoutTime = 10; // If projectiles are airborne longer than this -> switch round

    public ImmutableArray<Entity> players; // List of players
    public ImmutableArray<Entity> healthDisplayers; // List of players
    private ImmutableArray<Entity> projectiles; // List of projectiles

    public HashMap<String, Entity> onlinePlayers = new HashMap<String, Entity>();
    public String playerId;
    Entity yourPlayer;
    Entity currentPlayer; // The player whose turn it is
    boolean gameStart = false;

    public OnlineMultiplayer() {
        // Connect to server
        this.connectSocket();
        this.socketConfigs();
    }

    @Override
    public void update(float dt) {
        if(players.size()>=2)
            gameStart = true;

        updateServer(); // Update server related things
        if (gameStart && yourPlayer==currentPlayer) { // Wait until another player joins
            // Check if aim button is pressed
            if (CM.aimPressed)
                GSM.setGameState(GameStateManager.STATE.PLAYER_AIMING); // Change state to player aiming if button is pressed

            this.players = ECSManager.engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
            this.healthDisplayers = ECSManager.engine.getEntitiesFor(Family.one(HealthDisplayerComponent.class).get());
            this.projectiles = ECSManager.engine.getEntitiesFor(Family.one(ProjectileComponent.class).get());

            if (players.size() > 0) {
                checkHealth(); // Check if any health components have reached 0, and terminate those players
                updateUI(); // Update UI elements
                //checkForEndGame(); // Ends the game if certain conditions are met
            }

            if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE))
                checkProjectileTimeOut(dt); // Check if the projectiles have been airborne for two long

            if (!stopTimer)
                timer += dt; // Increment the timer if not paused

            // Start a new round when the timer is greater than the switchTime variable
            if (timer > switchTime) {
                timer = 0;
                if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_GAME) || GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
                    GSM.setGameState(GameStateManager.STATE.START_ROUND);
                } else
                    GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND);
            }
        }
    }

    @Override
    public void startGame() { // Is called when the game starts
        // Initialize entity arrays
        this.players = ECSManager.engine.getEntitiesFor(Family.one(PlayerComponent.class).get());
        this.projectiles = ECSManager.engine.getEntitiesFor(Family.one(ProjectileComponent.class).get());
        this.healthDisplayers = ECSManager.engine.getEntitiesFor(Family.one(HealthDisplayerComponent.class).get());

        CM.setVisible(false); // Make all controller not visible
        setPlayerSpawn(); // Choose location for where players spawn
        switchTime = START_GAME_TIME; // The timer now countsdown from START_GAME_TIME to 0

        // Start or stop systems (if they should be processed or not)
        ECSManager.engine.getSystem(ShootingSystem.class).setProcessing(false);
        ECSManager.engine.getSystem(MovementSystem.class).setProcessing(false);
        ECSManager.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void endGame() { // Is called when the game ends
        // Remove entites
        ECSManager.removeAllEntities();

        SM.setScreen(ScreenManager.STATE.END_SCREEN); // Display the end screen

        // Disconnect server socket
        this.disconnectSocket();
    }

    @Override
    public void startRound() {
        // Is called when a new round starts
        CM.setVisible(true); // Make all controller visible visible

        timer = 0; // Reset the timer
        switchTime = ROUND_TIME; // Set the timer to the length of a round

        // Update moving player
        if (players.size() > 0) {
            if (onlinePlayers.get(playerId) == currentPlayer) {
                CM.startMoving(); // Enable moving buttons
                currentPlayer.add(new MovementControlComponent()); // Add movement to the local player
            }
        }

        // Start or stop systems (if they should be processed or not)
        ECSManager.engine.getSystem(MovementSystem.class).setProcessing(true);
    }

    @Override
    public void playerAim() {// Is called when the player should start aiming
        CM.startShooting(); // Enable shooting button

        // Remove or add components to entities
        yourPlayer.remove(MovementControlComponent.class); // The player should loose ability to move whilst aiming
        yourPlayer.add(new isAimingComponent());

        ECSManager.addShootingRender();

        // Start or stop systems (if they should be processed or not)
        ECSManager.engine.getSystem(AimingSystem.class).setProcessing(true);
        ECSManager.engine.getSystem(MovementSystem.class).setProcessing(false);
    }

    @Override
    public void playerShooting() {// Is called when the player should start shooting
        stopTimer = true; // Stop the timer from counting down

        // Remove or add components to entities
        yourPlayer.remove(isAimingComponent.class);
        yourPlayer.add(new isShootingComponent());

        // Start or stop systems (if they should be processed or not)
        ECSManager.engine.getSystem(ShootingSystem.class).setProcessing(true);
        ECSManager.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void projectileAirborne() {// Is called when a projectile has been fired and is currently airborne
        CM.idle(); // Make all controller buttons idle
        CM.setVisible(false); // Make all controller not visible

        // Remove or add components to entities
        yourPlayer.remove(isShootingComponent.class);
        ECSManager.removeShootingRender();

        // Start or stop systems (if they should be processed or not)
        ECSManager.engine.getSystem(ShootingSystem.class).setProcessing(false);
        ECSManager.engine.getSystem(AimingSystem.class).setProcessing(false);
    }

    @Override
    public void switchRound() {// Is called when the game is switching between round
        // Remove or add components to entities
        yourPlayer.remove(isShootingComponent.class);
        yourPlayer.remove(isAimingComponent.class);
        ECSManager.removeShootingRender();
        // Start or stop systems (if they should be processed or not)
        ECSManager.engine.getSystem(ShootingSystem.class).setProcessing(false);
        ECSManager.engine.getSystem(AimingSystem.class).setProcessing(false);

        timer = 0; // Reset the timer
        stopTimer = false; // Start the timer again
        switchTime = TIME_BETWEEN_ROUNDS; // Set the switchTime to how long the round switch should take
        socket.emit("switchRound");
    }

    @Override
    public void updateUI() {
        // Is called every update
        this.printTimer(); // Print information about how much time is left in a round, etc...

        ECSManager.updatePowerBar(yourPlayer); // Makes the powerbar display correctly

        // Update health displays
        for (int i = 0; i < healthDisplayers.size(); i++) {
            Entity entity = healthDisplayers.get(i);
            Entity parent = ECSManager.parentMapper.get(entity).parent;
            ECSManager.positionMapper.get(entity).position.x = ECSManager.positionMapper.get(parent).position.x;
            ECSManager.positionMapper.get(entity).position.y = ECSManager.positionMapper.get(parent).position.y + ECSManager.spriteMapper.get(parent).size.y;
            ECSManager.fontMapper.get(entity).text = ECSManager.healthMapper.get(parent).hp + " hp";
        }
    }

    @Override
    public void initEntities() {
        ECSManager.createMap(mapFile, mapTexture);
    }

    // Print information about how much time is left in a round, etc...
    private void printTimer() {
        // SWITCH_ROUND
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.SWITCH_ROUND)) {
            FontComponent timerFont = ECSManager.fontMapper.get(ECSManager.timer);
            timerFont.text = "Switching players in: " + this.df.format(TIME_BETWEEN_ROUNDS - timer) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }

        // START_GAME
        else if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.START_GAME)) {
            FontComponent timerFont = ECSManager.fontMapper.get(ECSManager.timer);
            timerFont.text = "\n\n\n\n" + ((int) START_GAME_TIME - (int) timer);
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }

        // OTHER
        else {
            FontComponent timerFont = ECSManager.fontMapper.get(ECSManager.timer);
            timerFont.text = "Timer: " + this.df.format(ROUND_TIME - timer) + "s";
            timerFont.layout = new GlyphLayout(timerFont.font, timerFont.text);
        }
    }

    // Check if any player healths are under 0
    private void checkHealth() {
        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i);
            HealthComponent playerHealth = ECSManager.healthMapper.get(player);

            // If player health < 0 -> delete the player and its associate health displayer
            if (playerHealth.hp <= 0) {
                for (int j = 0; j < healthDisplayers.size(); j++) {
                    Entity healthDisplayer = healthDisplayers.get(j);
                    if (ECSManager.parentMapper.get(healthDisplayer).parent == player)
                        healthDisplayer.removeAll();
                }
                player.removeAll();
            }
        }
        this.players = ECSManager.engine.getEntitiesFor(Family.one(PlayerComponent.class).get()); // Update the player array
    }

    // Set the spawnpoint of players
    private void setPlayerSpawn() {
        for (int i = 0; i < players.size(); i++) {
            Entity player = players.get(i);
            PositionComponent pos = ECSManager.positionMapper.get(player);

            // Spawn players with equal distance between them
            ECSManager.b2dMapper.get(player).body.setTransform((50f + (Application.camera.viewportWidth / players.size()) * i) / PPM, pos.position.y / PPM, 0);
        }
    }

    private void updateServer() {
        if (players.size() > 0) {

            // Update moving player
            if (yourPlayer == currentPlayer) {
                JSONObject data = new JSONObject();

                try {
                    if (CM.leftPressed || CM.rightPressed) {
                        Vector2 position = ECSManager.positionMapper.get(currentPlayer).position;
                        data.put("x", position.x);
                        data.put("y", position.y);
                        socket.emit("playerMoved", data);
                    }
                } catch (JSONException e) {
                    System.out.println("[Server] Error: " + e.getMessage());
                }
            }
        }
    }

    // Connect to server socket
    private void connectSocket() {
        try {
            System.out.println("[Server] Server connected");
            socket = IO.socket("http://localhost:8080");  //showing where the server is located
            socket.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Disconnect socket from server
    private void disconnectSocket() {
        socket.disconnect();
    }

    // Initialize event handlers for socket
    private void socketConfigs() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("[Server] Server connected");
            }
        }).on("socketID", new Emitter.Listener() { // Sets playerId for the local player
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    playerId = data.getString("id"); // Store the local player's id
                    System.out.println("[Server] Player connected [" + playerId + "]");

                    onlinePlayers.put(playerId, ECSManager.entityTemplateMapper.getPlayerClass(EntityTemplateMapper.PLAYERS.SPEEDY).createEntity()); // Add player to array of online players
                    yourPlayer = onlinePlayers.get(playerId);

                } catch (JSONException e) {
                    System.out.println("[Server] Error getting playerId");
                }
            }
        }).on("newPlayer", new Emitter.Listener() { // Adds new players to the array of onlinePlayers
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    System.out.println("[Server] New player connected [" + id + "]");

                    onlinePlayers.put(id, ECSManager.entityTemplateMapper.getPlayerClass(EntityTemplateMapper.PLAYERS.DEFAULT).createEntity());
                } catch (JSONException e) {
                    System.out.println("[Server] Error getting new playerId");
                }
            }
        }).on("playerMoved", new Emitter.Listener() { // Adds new players to the array of onlinePlayers
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Vector2 position = new Vector2((float) data.getDouble("x"), (float) data.getDouble("y"));

                    // Update moving players position
                    if (onlinePlayers.get(id) != null) {
                        ECSManager.b2dMapper.get(onlinePlayers.get(id)).body.setTransform(position.x/PPM, position.y/PPM, 0); // Update Box2D position
                    }
                } catch (JSONException e) {
                    System.out.println("[Server] Error getting new playerId");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("[Server] Getting all online players");
                JSONArray objects = (JSONArray) args[0];
                try {
                    for (int i = 0; i < objects.length(); i++) {
                        String id = objects.getJSONObject(i).getString("id");

                        if (!id.equals(playerId)) {
                            // Create coop player
                            Entity coopPlayer = ECSManager.entityTemplateMapper.getPlayerClass(EntityTemplateMapper.PLAYERS.DEFAULT).createEntity();

                            // Update position component
                            Vector2 position = new Vector2();
                            position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                            position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                            ECSManager.positionMapper.get(coopPlayer).position = position;

                            // Add coop player to array of online players
                            onlinePlayers.put(id, coopPlayer);
                        }
                    }
                } catch (JSONException e) {
                    System.out.println("[Server] Error: " + e.getMessage());
                }
            }
        }).on("getCurrentPlayerId", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("[Server] Getting current player");
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("currentPlayerId");
                    System.out.println("[Server] Current player: [" + id + "]");

                    currentPlayer = onlinePlayers.get(id);


                } catch (JSONException e) {
                    System.out.println("[Server] Error getting current playerId");
                }
            }
        });
    }

    // Checks if the game state should switch from ProjectileAirborne to a new round
    private void checkProjectileTimeOut(float dt) {
        if (GSM.gameState == GSM.getGameState(GameStateManager.STATE.PROJECTILE_AIRBORNE)) {
            airBorneTime += dt;

            // Change gamestate if the projectile has been airborne too long
            if (airBorneTime > timeoutTime) {
                airBorneTime = 0;
                GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND);
            }

            // Check if there are no projectiles -> move on to SWITCH_ROUND state
            if ((projectiles.size() <= 0))
                GSM.setGameState(GameStateManager.STATE.SWITCH_ROUND);
        }
    }

    // Check if the game should end -> and end the game
    private void checkForEndGame() {
        if (players.size() == 1) // End the game when there is only one player left
            GSM.setGameState(GameStateManager.STATE.END_GAME);
    }
}
