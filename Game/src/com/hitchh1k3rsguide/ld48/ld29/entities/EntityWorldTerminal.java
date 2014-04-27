package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Graphics2D;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentCollision;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMessageable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite.Property;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.AbstractBoundingVolume;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.AxisAlignedBoundingBox;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.Vec2d;
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;
import com.hitchh1k3rsguide.ld48.ld29.utilities.LevelHelper;
import com.hitchh1k3rsguide.ld48.ld29.utilities.LevelHelper.Level;

public class EntityWorldTerminal extends AbstractEntity implements ComponentCollision,
        ComponentRenderable, ComponentMessageable
{

    Vec2d position;
    static Sprite[] sprites = null;
    Sprite sprite;
    AxisAlignedBoundingBox bounds;
    Level level;
    GameEngine ecs;

    public EntityWorldTerminal(long index, double x, double y, Level level, GameEngine ecs)
    {
        super(index);
        position = new Vec2d(x, y);
        if (sprites == null)
        {
            sprites = new Sprite[] { new Sprite("SurfaceTerminal.png"),
                    new Sprite("CaveTerminal.png"), new Sprite("LabTerminal.png"),
                    new Sprite("NukeTerminal.png") };
            for (int i = 0; i < sprites.length; ++i)
            {
                sprites[i].setOrigin(sprites[i].width / 2, sprites[i].height);
            }
        }
        switch (level)
        {
            case CAVES:
                sprite = sprites[1];
                bounds = new AxisAlignedBoundingBox(x - 150, y - 300, 140, 600);
                break;
            case LABRATORY:
                sprite = sprites[2];
                bounds = new AxisAlignedBoundingBox(x, y - 300, 140, 600);
                break;
            case MAINFRAME:
                sprite = sprites[3];
                bounds = new AxisAlignedBoundingBox(x, y - 300, 140, 600);
                break;
            case SURFACE:
                sprite = sprites[0];
                bounds = new AxisAlignedBoundingBox(x - 150, y - 300, 140, 600);
                break;
            default:
                break;
        }
        this.level = level;
        this.ecs = ecs;
    }

    @Override
    public void draw(Graphics2D g)
    {
        sprite.draw(g, Property.Pos2i, (int) position.x, (int) position.y);
    }

    @Override
    public int getZIndex()
    {
        return 3;
    }

    @Override
    public boolean shouldRender(double translateX)
    {
        return (position.x > translateX - sprite.width && position.x < translateX + sprite.width
                + Game.GAME_WIDTH);
    }

    @Override
    public AbstractBoundingVolume getBounds()
    {
        return bounds;
    }

    @Override
    public void collidesWith(ComponentCollision other)
    {
        if (other instanceof EntityPlayer)
        {
            switch (level)
            {
                case CAVES:
                    LevelHelper.buildLevel(Level.CAVES_LOG, ecs);
                    break;
                case LABRATORY:
                    LevelHelper.buildLevel(Level.LAB_LOG, ecs);
                    break;
                case MAINFRAME:
                    LevelHelper.buildLevel(Level.MAINFRAME_LOG, ecs);
                    break;
                case SURFACE:
                    LevelHelper.buildLevel(Level.SURFACE_LOG, ecs);
                    break;
                default:
                    break;

            }
        }
    }

    @Override
    public void getMessage(GameEngine ecs, IMessage message)
    {
        if (message instanceof MessageDeleteWorld)
        {
            dispose();
        }
    }
}
