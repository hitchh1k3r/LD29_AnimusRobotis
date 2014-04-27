package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Graphics2D;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentCollision;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMessageable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMovable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentUpkeep;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite.Property;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.AbstractBoundingVolume;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.AxisAlignedBoundingBox;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.Vec2d;
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;

public class EntityFallingRock extends AbstractEntity implements ComponentCollision,
        ComponentMovable, ComponentRenderable, ComponentUpkeep, ComponentMessageable
{

    Vec2d position, velocity;
    int shakeTimer;
    boolean fallMode;
    static Sprite[] sprites = null;
    Sprite sprite = null;
    AxisAlignedBoundingBox bounds;

    public EntityFallingRock(long index, double x, double y)
    {
        super(index);
        if (sprites == null)
        {
            sprites = new Sprite[] { new Sprite("FallingRock.png"), new Sprite("FallenRock.png") };
        }
        sprite = sprites[0];
        position = new Vec2d(x, y);
        velocity = new Vec2d(0, 0);
        shakeTimer = 0;
        fallMode = false;
        bounds = new AxisAlignedBoundingBox(0, 0, 500, 1400);
        bounds.setCenter(position);
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
    public void primaryUpdate(GameEngine ecs)
    {
        update(ecs);
    }

    @Override
    public void update(GameEngine ecs)
    {
        if (shakeTimer > 0)
        {
            if (--shakeTimer <= 0)
            {
                fallMode = true;
                velocity.y = 20;
            }
        }
    }

    @Override
    public void draw(Graphics2D g)
    {
        if (shakeTimer > 0)
        {
            sprite.draw(g, Property.Pos2i, (int) (position.x + (Math.random() * 12 - 6)),
                    (int) (position.y + (Math.random() * 8 - 4)));
        }
        else
        {
            sprite.draw(g, Property.Pos2i, (int) position.x, (int) position.y);
        }
    }

    @Override
    public int getZIndex()
    {
        return 4;
    }

    @Override
    public boolean shouldRender(double translateX)
    {
        return (position.x > translateX - sprite.width && position.x < translateX + Game.GAME_WIDTH
                + sprite.width);
    }

    @Override
    public Vec2d getPosition()
    {
        return position;
    }

    @Override
    public Vec2d getVelocity()
    {
        return velocity;
    }

    @Override
    public void setPosition(double x, double y)
    {
        position.set(x, y);
    }

    @Override
    public void move()
    {
        if (position.y > Game.GAME_HEIGHT - (sprites[1].height / 2) + 50)
        {
            sprite = sprites[1];
            position.y = Game.GAME_HEIGHT - (sprites[1].height / 2) + 50;
            velocity.y = 0;
        }
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
            if (!fallMode && shakeTimer == 0)
            {
                shakeTimer = (int) ((Math.random() * 30) + 45);
                bounds.radius.set(77 / 2, 288 / 2);
            }
            else if (((EntityPlayer) other).hurtTimer <= 0)
            {
                Vec2d direction = ((EntityPlayer) other).position.copy();
                direction.scale(-1);
                direction.add(position);
                ((EntityPlayer) other).hurt(direction, 3);
            }
        }
    }

}
