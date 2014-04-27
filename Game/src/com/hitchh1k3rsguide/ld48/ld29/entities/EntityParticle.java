package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Graphics2D;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentGravity;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMovable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentUpkeep;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite.Property;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.Vec2d;

public class EntityParticle extends AbstractEntity implements ComponentGravity, ComponentMovable,
        ComponentRenderable, ComponentUpkeep
{

    Sprite sprite;
    Vec2d position;
    Vec2d velocity;
    double rotation;
    double rotationVel;
    int age;

    public EntityParticle(long index, Sprite sprite, double x, double y)
    {
        super(index);
        this.sprite = sprite;
        position = new Vec2d(x, y);
        velocity = new Vec2d((Math.random() * 16) - 8, -10 - Math.random() * 10);
        rotationVel = Math.random() * 0.1 - 0.05;
    }

    @Override
    public void primaryUpdate(GameEngine ecs)
    {
        update(ecs);
    }

    @Override
    public void update(GameEngine ecs)
    {
        if (++age >= 100)
        {
            dispose();
        }
    }

    @Override
    public void draw(Graphics2D g)
    {
        sprite.draw(g, Property.Pos2i, (int) position.x, (int) position.y, Property.Rotate1d,
                rotation, Property.Scale2d, 0.65d, 0.65d);
    }

    @Override
    public int getZIndex()
    {
        return 4;
    }

    @Override
    public boolean shouldRender(double translateX)
    {
        return true;
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
        rotation += rotationVel;
    }

    @Override
    public double getFallingSpeed()
    {
        return velocity.y;
    }

    @Override
    public double getMaxFallSpeed(double gravity)
    {
        return 10;
    }

    @Override
    public double getGravityMultiplier()
    {
        return 0.5;
    }

    @Override
    public void setFallingSpeed(double speed)
    {
        velocity.y = speed;
    }

}
