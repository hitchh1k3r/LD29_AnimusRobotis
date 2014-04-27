package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentUpkeep;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.MessageStopMusic;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentAnyKey;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentNumberKeys;
import com.hitchh1k3rsguide.ld48.ld29.utilities.LevelHelper;
import com.hitchh1k3rsguide.ld48.ld29.utilities.LevelHelper.Level;
import com.hitchh1k3rsguide.ld48.ld29.utilities.SoundHelper;

public class EntityTerminal extends AbstractEntity implements ComponentRenderable, ComponentAnyKey,
        ComponentUpkeep, ComponentNumberKeys
{

    public Sprite sprite = null;
    int type;
    GameEngine ecs;
    int age;
    float fadeScreen;
    int endingTimer;

    public EntityTerminal(long index, int type, GameEngine ecs)
    {
        super(index);
        if (sprite == null)
        {
            sprite = new Sprite("scenes/Terminal_" + type + ".png", 0, 0);
        }
        this.type = type;
        this.ecs = ecs;
        age = 0;
        fadeScreen = -1;
        endingTimer = 0;
        if (type == 5)
        {
            fadeScreen = 1;
        }
    }

    @Override
    public void draw(Graphics2D g)
    {
        sprite.draw(g);
        if (fadeScreen >= 0)
        {
            g.setColor(new Color(0, 0, 0, fadeScreen));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        }
    }

    @Override
    public int getZIndex()
    {
        return 10;
    }

    @Override
    public void pressedAnyKey()
    {
        if (type < 4 && age >= 60)
        {
            SoundHelper.playSound("Select");
            switch (type)
            {
                case 0:
                    LevelHelper.buildLevel(Level.SURFACE, ecs);
                    break;
                case 1:
                    LevelHelper.buildLevel(Level.CAVES, ecs);
                    break;
                case 2:
                    LevelHelper.buildLevel(Level.LABRATORY, ecs);
                    break;
                case 3:
                    LevelHelper.buildLevel(Level.MAINFRAME, ecs);
                    break;
            }
            dispose();
        }
    }

    @Override
    public boolean shouldRender(double translateX)
    {
        return true;
    }

    @Override
    public void primaryUpdate(GameEngine ecs)
    {
        update(ecs);
        if (endingTimer >= 120)
        {
            LevelHelper.buildLevel(Level.ENDING, ecs);
            dispose();
        }
    }

    @Override
    public void update(GameEngine ecs)
    {
        if (age < 60)
        {
            ++age;
        }
        if (fadeScreen > 0 && fadeScreen <= 1 && type == 5)
        {
            fadeScreen -= 0.01;
            if (fadeScreen < 0)
            {
                fadeScreen = 0;
            }
        }
        if (fadeScreen >= 0 && fadeScreen < 1 && type == 4)
        {
            fadeScreen += 0.01;
            if (fadeScreen > 1)
            {
                fadeScreen = 1;
            }
        }
        if (fadeScreen == 1)
        {
            ++endingTimer;
        }
    }

    @Override
    public void pressNumberKey(int number)
    {
        if (type == 4 && fadeScreen < 0 && number >= 1 && number <= 2)
        {
            ecs.sendMessage(new MessageStopMusic());
            SoundHelper.playSound("Nuke");
            fadeScreen = 0;
        }
    }

}
