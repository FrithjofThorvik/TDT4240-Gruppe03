# Projectile Wars

## Overview
Projectile Wars is a simple game inspired by the "Worms" series that was made as a part of the course TDT4240 - Software Architecture at NTNU. The game is made with a focus on architecture, rather than features. This has resulted in an easily modifiable game, where the groundwork for further development has been laid.
<br></br>
![Skjermbilde 2021-04-22 kl  13 19 32](https://user-images.githubusercontent.com/43807082/115705934-6bc88c00-a36d-11eb-833a-8badd51edeca.png)

<br></br>
## Table of contents
- [Overview](Overview)
- [Installation and set-up](Installation-and-set-up)
- [Project Structure](Project-Structure)
- [Developers](Developers)

<br></br>
## Installation and set-up
(This guide assumes Android Studio, LibGDX and an Android SDK is already installed)
1. Clone the repository:
```zsh
$ git clone https://github.com/FrithjofThorvik/TDT4240-Gruppe03.git
```
2. Open the project with Android Studio

3. Build the project

4. Run the project with Android Studio's built in Android emulator


<br></br>
## Project Structure
```bash
 core
  │
 src
  │
 game
  ├── ECS
  │   ├── EntityUtils
  │   │   ├── EntityTemplateMapper.java
  │   │   ├── strategies
  │   │   │   ├── PowerUp
  │   │   │   │   ├── DefaultPowerUp.java
  │   │   │   │   ├── HealthUP.java
  │   │   │   │   ├── PowerUpEffect.java
  │   │   │   │   └── SpeedUp.java
  │   │   │   └── Projectile
  │   │   │       ├── BouncyType.java
  │   │   │       ├── ProjectileType.java
  │   │   │       ├── SplitterType.java
  │   │   │       └── StandardType.java
  │   │   └── templates
  │   │       ├── AbstractEntity.java
  │   │       ├── Fonts
  │   │       │   ├── AbstractFont.java
  │   │       │   ├── HealthFont.java
  │   │       │   └── TextFont.java
  │   │       ├── Players
  │   │       │   ├── AbstractPlayer.java
  │   │       │   ├── DefaultPlayer.java
  │   │       │   └── SpeedyPlayer.java
  │   │       └── Projectiles
  │   │           ├── AbstractProjectile.java
  │   │           ├── BouncerProjectile.java
  │   │           ├── DefaultProjectile.java
  │   │           ├── SpeedyProjectile.java
  │   │           └── SplitterProjectile.java
  │   ├── components
  │   │   ├── Box2DComponent.java
  │   │   ├── CollisionComponent.java
  │   │   ├── FontComponent.java
  │   │   ├── HealthComponent.java
  │   │   ├── ParentComponent.java
  │   │   ├── PositionComponent.java
  │   │   ├── ShootingComponent.java
  │   │   ├── SpriteComponent.java
  │   │   ├── VelocityComponent.java
  │   │   ├── flags
  │   │   │   ├── HealthDisplayerComponent.java
  │   │   │   ├── MovementControlComponent.java
  │   │   │   ├── PlayerComponent.java
  │   │   │   ├── PowerUpComponent.java
  │   │   │   ├── RenderComponent.java
  │   │   │   ├── isAimingComponent.java
  │   │   │   └── isShootingComponent.java
  │   │   └── projectiles
  │   │       ├── BouncyComponent.java
  │   │       ├── ProjectileComponent.java
  │   │       └── SplitterComponent.java
  │   ├── managers
  │   │   ├── ECSManager.java
  │   │   ├── GameEntitiesManager.java
  │   │   ├── MapManager.java
  │   │   └── UIManager.java
  │   └── systems
  │       ├── AimingSystem.java
  │       ├── MovementSystem.java
  │       ├── PhysicsSystem.java
  │       ├── PowerUpSystem.java
  │       ├── ProjectileSystem.java
  │       ├── RenderingSystem.java
  │       └── ShootingSystem.java
  ├── firebase
  │   ├── CoreInterFaceClass.java
  │   └── FirebaseInterface.java
  ├── gamelogic
  │   ├── states
  │   │   ├── GameStateManager.java
  │   │   ├── ScreenManager.java
  │   │   ├── game
  │   │   │   ├── AbstractGameState.java
  │   │   │   ├── EndGame.java
  │   │   │   ├── PlayerAiming.java
  │   │   │   ├── PlayerShooting.java
  │   │   │   ├── ProjectileAirborne.java
  │   │   │   ├── StartGame.java
  │   │   │   ├── StartRound.java
  │   │   │   └── SwitchRound.java
  │   │   └── screens
  │   │       ├── AbstractScreen.java
  │   │       ├── EndScreen.java
  │   │       ├── GameScreen.java
  │   │       ├── LeaderboardScreen.java
  │   │       └── MainMenuScreen.java
  │   └── strategies
  │       └── gamemodes
  │           ├── GameMode.java
  │           ├── LocalMultiplayer.java
  │           ├── OnlineMultiplayer.java
  │           └── Training.java
  └── utils
      ├── B2DConstants.java
      ├── CollisionHandler.java
      ├── GameConstants.java
      └── GameController.java
```

## Developers

- Steven Francis
- Frithjof Thorvik
- Espen Iversen
- Håkon Frøland
- Enok Liland
