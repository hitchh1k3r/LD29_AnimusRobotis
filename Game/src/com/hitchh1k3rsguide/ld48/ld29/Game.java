package com.hitchh1k3rsguide.ld48.ld29;

import kuusisto.tinysound.TinySound;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentArrowKeys;
import com.hitchh1k3rsguide.gameEngine.components.ComponentCollision;
import com.hitchh1k3rsguide.gameEngine.components.ComponentGravity;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMessageable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMovable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentUpkeep;
import com.hitchh1k3rsguide.gameEngine.components.ComponentWindow;
import com.hitchh1k3rsguide.gameEngine.entities.EntityGameWindow;
import com.hitchh1k3rsguide.gameEngine.systems.SystemRendering;
import com.hitchh1k3rsguide.gameEngine.systems.SystemSimplePlatformPhysics;
import com.hitchh1k3rsguide.gameEngine.systems.SystemUpkeep;
import com.hitchh1k3rsguide.gameEngine.systems.SystemWindow;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentAnyKey;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentCameraTracking;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentMouseTracking;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentNumberKeys;
import com.hitchh1k3rsguide.ld48.ld29.systems.SystemKeyboardFunctions;
import com.hitchh1k3rsguide.ld48.ld29.systems.SystemMouseTracking;
import com.hitchh1k3rsguide.ld48.ld29.systems.SystemPlayerCamera;
import com.hitchh1k3rsguide.ld48.ld29.utilities.LevelHelper;
import com.hitchh1k3rsguide.ld48.ld29.utilities.LevelHelper.Level;

public class Game
{

    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;

    public static void main(String args[])
    { // Beneath The Surface
      // Create Entity Component System:
        GameEngine ecs = new GameEngine();
        ecs.setFPS(60);

        // Add Components:
        ecs.addComponent(ComponentWindow.class);
        ecs.addComponent(ComponentRenderable.class);
        ecs.addComponent(ComponentGravity.class);
        ecs.addComponent(ComponentMovable.class);
        ecs.addComponent(ComponentUpkeep.class);
        ecs.addComponent(ComponentMessageable.class);
        ecs.addComponent(ComponentCollision.class);
        ecs.addComponent(ComponentArrowKeys.class);
        ecs.addComponent(ComponentMouseTracking.class);
        ecs.addComponent(ComponentCameraTracking.class);
        ecs.addComponent(ComponentAnyKey.class);
        ecs.addComponent(ComponentNumberKeys.class);

        // Add Systems:
        ecs.systems.add(new SystemWindow());
        ecs.systems.add(new SystemPlayerCamera());
        ecs.systems.add(new SystemRendering());
        ecs.systems.add(new SystemSimplePlatformPhysics(0.9));
        ecs.systems.add(new SystemUpkeep());
        ecs.systems.add(new SystemMouseTracking());
        ecs.systems.add(new SystemKeyboardFunctions());

        // Add Entities:
        ecs.addEntity(new EntityGameWindow(ecs.getUniqueID(), "Animus Robotis (LD29)", GAME_WIDTH,
                GAME_HEIGHT, true));

        TinySound.init();

        LevelHelper.buildLevel(Level.START_LOG, ecs);

        // Run ECS!
        ecs.run();

        System.exit(0);
    }

}
