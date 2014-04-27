package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMessageable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMovable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite.Property;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.Vec2d;
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;

public class EntityCloud extends AbstractEntity implements ComponentMovable, ComponentRenderable,
        ComponentMessageable
{

    public Vec2d position, velocity;
    public static Sprite sprite = null;
    public double scale;

    public EntityCloud(long index, double x)
    {
        super(index);
        if (sprite == null)
        {
            sprite = new Sprite("Cloud.png");
        }
        position = new Vec2d(x, Math.random() * 350);
        velocity = new Vec2d((Math.random() * -0.5) - 0.5, 0);
        scale = (Math.random() * 0.8) + 0.25;
    }

    @Override
    public void draw(Graphics2D g)
    {
        AffineTransform af = g.getTransform();
        g.setTransform(new AffineTransform());
        sprite.draw(g, Property.Pos2i, (int) position.x, (int) position.y, Property.Scale2d, scale,
                scale);
        g.setTransform(af);
    }

    @Override
    public int getZIndex()
    {
        return 1;
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
        if (position.x < -sprite.width)
        {
            position.set(Game.GAME_WIDTH + sprite.width + (Math.random() * 350),
                    Math.random() * 200);
            velocity.set((Math.random() * -0.5) - 0.5, 0);
            scale = Math.random() + 0.25;
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

    @Override
    public boolean shouldRender(double translateX)
    {
        return true;
    }

}
