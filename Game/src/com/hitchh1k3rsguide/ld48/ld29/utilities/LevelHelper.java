package com.hitchh1k3rsguide.ld48.ld29.utilities;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.entities.EntityMusic;
import com.hitchh1k3rsguide.gameEngine.messages.MessageStopMusic;
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntityBackground;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntityCloud;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntityFallingRock;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntityGround;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntityLaserTurret;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntityPlayer;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntityRoundBot;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntitySquareBot;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntityTerminal;
import com.hitchh1k3rsguide.ld48.ld29.entities.EntityWorldTerminal;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageSetLevelWidth;

public class LevelHelper
{

    private static Level lastLevel;

    public static enum Level
    {
        START_LOG, SURFACE_LOG, CAVES_LOG, LAB_LOG, MAINFRAME_LOG, ENDING, SURFACE, CAVES, LABRATORY, MAINFRAME
    }

    public static void buildLevel(Level level, GameEngine ecs)
    {
        lastLevel = level;
        ecs.sendMessage(new MessageStopMusic());
        ecs.sendMessage(new MessageDeleteWorld());
        switch (level)
        {
            case CAVES:
                buildCaves(ecs);
                break;
            case LABRATORY:
                buildLabratory(ecs);
                break;
            case MAINFRAME:
                buildMainFrame(ecs);
                break;
            case SURFACE:
                buildSurface(ecs);
                break;
            case CAVES_LOG:
                buildTerminal(2, ecs);
                break;
            case ENDING:
                buildTerminal(5, ecs);
                break;
            case LAB_LOG:
                buildTerminal(3, ecs);
                break;
            case MAINFRAME_LOG:
                buildTerminal(4, ecs);
                break;
            case START_LOG:
                buildTerminal(0, ecs);
                break;
            case SURFACE_LOG:
                buildTerminal(1, ecs);
                break;
        }
    }

    public static void restartLevel(GameEngine ecs)
    {
        buildLevel(lastLevel, ecs);
    }

    private static void buildTerminal(int i, GameEngine ecs)
    {
        ecs.addEntity(new EntityTerminal(ecs.getUniqueID(), i, ecs));
        if (i != 5)
        {
            ecs.addEntity(new EntityMusic(ecs.getUniqueID(), "music/Terminal.ogg", 84327.62f));
        }
    }

    //////////////////////////////////////////////////////////////////

    private static void buildSurface(GameEngine ecs)
    {
        ecs.addEntity(new EntityMusic(ecs.getUniqueID(), "music/Surface.ogg", 137272.02f));
        ecs.addEntity(new EntityPlayer(ecs.getUniqueID(), ecs));
        ecs.addEntity(new EntityWorldTerminal(ecs.getUniqueID(), 744 * 24 + 100,
                Game.GAME_HEIGHT - 45, Level.SURFACE, ecs));
        for (int i = 0; i < 40; ++i)
        {
            if (i > 10 && i % 10 == 0)
            {
                ecs.addEntity(new EntitySquareBot(ecs.getUniqueID(), 2500 + i * 700, 500, ecs));
            }
            else
            {
                ecs.addEntity(new EntityRoundBot(ecs.getUniqueID(), 2500 + i * 700, 500, ecs));
            }
        }
        for (int i = 0; i < 25; ++i)
        {
            ecs.addEntity(new EntityGround(ecs.getUniqueID(), 744 * i, 151 - (i * 5), Level.SURFACE));
        }
        ecs.addEntity(new EntityCloud(ecs.getUniqueID(), 0));
        ecs.addEntity(new EntityCloud(ecs.getUniqueID(), 500));
        ecs.addEntity(new EntityCloud(ecs.getUniqueID(), 1000));
        ecs.addEntity(new EntityCloud(ecs.getUniqueID(), 1500));
        ecs.addEntity(new EntityCloud(ecs.getUniqueID(), 2000));
        ecs.addEntity(new EntityBackground(ecs.getUniqueID(), Level.SURFACE));
        ecs.indexEntities();
        ecs.sendMessage(new MessageSetLevelWidth(25 * 744));
    }

    //////////////////////////////////////////////////////////////////

    private static void buildCaves(GameEngine ecs)
    {
        ecs.addEntity(new EntityMusic(ecs.getUniqueID(), "music/Caves.ogg", 174914.47f));
        ecs.addEntity(new EntityPlayer(ecs.getUniqueID(), ecs));
        ecs.addEntity(new EntityWorldTerminal(ecs.getUniqueID(), 744 * 34 + 100,
                Game.GAME_HEIGHT - 45, Level.CAVES, ecs));
        for (int i = 0; i < 20; ++i)
        {
            if (i > 0 && i % 2 == 0)
            {
                ecs.addEntity(new EntitySquareBot(ecs.getUniqueID(), 2500 + i * 2100, 500, ecs));
            }
            else
            {
                ecs.addEntity(new EntityRoundBot(ecs.getUniqueID(), 2500 + i * 2100, 500, ecs));
            }
        }
        for (int i = 0; i < 3; ++i)
        {
            for (int q = 0; q <= i * 2; ++q)
            {
                ecs.addEntity(new EntityFallingRock(ecs.getUniqueID(), 2500 + i * 6000 + q * 400,
                        100));
            }
        }
        for (int i = 0; i < 35; ++i)
        {
            ecs.addEntity(new EntityGround(ecs.getUniqueID(), 744 * i, 30, Level.CAVES));
        }
        ecs.addEntity(new EntityBackground(ecs.getUniqueID(), Level.CAVES));
        ecs.indexEntities();
        ecs.sendMessage(new MessageSetLevelWidth(35 * 744));
    }

    //////////////////////////////////////////////////////////////////

    private static void buildLabratory(GameEngine ecs)
    {
        ecs.addEntity(new EntityMusic(ecs.getUniqueID(), "music/Lab.ogg", 174914.47f));
        ecs.addEntity(new EntityPlayer(ecs.getUniqueID(), ecs));
        ecs.addEntity(new EntityWorldTerminal(ecs.getUniqueID(), 744 * 39 + 100,
                Game.GAME_HEIGHT - 45, Level.LABRATORY, ecs));
        for (int i = 0; i < 60; ++i)
        {
            if (i % 2 == 0)
            {
                ecs.addEntity(new EntitySquareBot(ecs.getUniqueID(), 2500 + i * 700, 500, ecs));
            }
        }
        for (int i = 0; i < 40; ++i)
        {
            if (i > 0 && i % 5 == 0)
            {
                ecs.addEntity(new EntityLaserTurret(ecs.getUniqueID(), 744 * i, 20, 120));
                ecs.addEntity(new EntityLaserTurret(ecs.getUniqueID(), 744 * i + 600, 20, 120));
            }
        }
        for (int i = 0; i < 40; ++i)
        {
            ecs.addEntity(new EntityGround(ecs.getUniqueID(), 744 * i, 70, Level.LABRATORY));
        }
        ecs.addEntity(new EntityBackground(ecs.getUniqueID(), Level.LABRATORY));
        ecs.indexEntities();
        ecs.sendMessage(new MessageSetLevelWidth(40 * 744));
    }

    //////////////////////////////////////////////////////////////////

    private static void buildMainFrame(GameEngine ecs)
    {
        ecs.addEntity(new EntityMusic(ecs.getUniqueID(), "music/Mainframe.ogg", 174914.47f));
        ecs.addEntity(new EntityPlayer(ecs.getUniqueID(), ecs));

        int[] timings = new int[] { 100, 200, 100, 200, 100, 0, 100, 80, 100, 80, 100, 80, 200,
                100, 80, 200, 80, 100, 0, 60, 60, 0, 60 };

        for (int i = 0; i < timings.length; ++i)
        {
            if (timings[i] > 0)
            {
                ecs.addEntity(new EntityLaserTurret(ecs.getUniqueID(), 744 + (300 * i), 20,
                        timings[i]));
            }
        }

        for (int i = 0; i < (int) ((timings.length * 300 + 744) / 744) + 1; ++i)
        {
            ecs.addEntity(new EntityGround(ecs.getUniqueID(), 744 * i, 35, Level.MAINFRAME));
        }

        ecs.addEntity(new EntityWorldTerminal(ecs.getUniqueID(),
                744 * ((timings.length * 300 + 744) / 744) + 100, Game.GAME_HEIGHT - 45,
                Level.MAINFRAME, ecs));
        ecs.addEntity(new EntityBackground(ecs.getUniqueID(), Level.MAINFRAME));
        ecs.indexEntities();
        ecs.sendMessage(new MessageSetLevelWidth(
                ((int) ((timings.length * 300 + 744) / 744) + 1) * 744));
    }
}
