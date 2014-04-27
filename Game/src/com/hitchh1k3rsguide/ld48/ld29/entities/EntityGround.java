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
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;
import com.hitchh1k3rsguide.ld48.ld29.utilities.LevelHelper.Level;

public class EntityGround extends AbstractEntity implements ComponentCollision,
        ComponentRenderable, ComponentMessageable
{

    public AxisAlignedBoundingBox bounds;
    public static Sprite[] sprites = null;
    public Sprite sprite;
    double xPos;

    public EntityGround(long index, double x, double height, Level level)
    {
        super(index);
        if (sprites == null)
        {
            sprites = new Sprite[] { new Sprite("Ground.png", 0, 100),
                    new Sprite("CaveGround.png", 0, 60), new Sprite("LabFloor.png", 0, 60),
                    new Sprite("MainFloor.png", 0, 60) };
        }
        switch (level)
        {
            case CAVES:
                sprite = sprites[1];
                break;
            case LABRATORY:
                sprite = sprites[2];
                break;
            case MAINFRAME:
                sprite = sprites[3];
                break;
            case SURFACE:
                sprite = sprites[0];
                break;
            default:
                break;
        }
        xPos = x;
        bounds = new AxisAlignedBoundingBox(x + (sprite.width / 2), Game.GAME_HEIGHT - height, 744,
                height);
    }

    @Override
    public void draw(Graphics2D g)
    {
        sprite.draw(g, Property.Pos2i, (int) bounds.getMinX(), (int) bounds.getMinY());
    }

    @Override
    public int getZIndex()
    {
        return 2;
    }

    @Override
    public AbstractBoundingVolume getBounds()
    {
        return bounds;
    }

    @Override
    public void collidesWith(ComponentCollision other)
    {

    }

    @Override
    public void getMessage(GameEngine ecs, IMessage message)
    {
        if (message instanceof MessageDeleteWorld)
        {
            dispose();
        }
    }

    @Override
    public boolean shouldRender(double translateX)
    {
        return (xPos > translateX - sprite.width && xPos < translateX + Game.GAME_WIDTH);
    }

}
