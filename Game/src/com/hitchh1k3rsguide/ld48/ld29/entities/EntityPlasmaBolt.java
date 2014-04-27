package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Graphics2D;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentCollision;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMovable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentUpkeep;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite.Property;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.AbstractBoundingVolume;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.OrientedBoundingBox;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.Vec2d;
import com.hitchh1k3rsguide.ld48.ld29.Game;

public class EntityPlasmaBolt extends AbstractEntity implements ComponentCollision,
        ComponentMovable, ComponentRenderable, ComponentUpkeep
{

    Vec2d position, velocity;
    double angle;
    OrientedBoundingBox bounds;
    public static Sprite sprite = null;
    int age;

    public EntityPlasmaBolt(long index, double x, double y, double angle, double speed,
            double offset)
    {
        super(index);
        if (sprite == null)
        {
            sprite = new Sprite("PlasmaBolt.png");
        }
        this.angle = angle;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        velocity = new Vec2d(cos * speed, sin * speed);
        position = new Vec2d(x + (cos * offset), y + (sin * offset));
        bounds = new OrientedBoundingBox(0, 0, 101, 43, angle);
        age = 0;
    }

    @Override
    public void draw(Graphics2D g)
    {
        if (age % 2 == 0)
        {
            sprite.draw(g, Property.Pos2i, (int) position.x, (int) position.y, Property.Rotate1d,
                    angle);
        }
        else
        {
            sprite.draw(g, Property.Pos2i, (int) position.x, (int) position.y, Property.Rotate1d,
                    angle, Property.FlipVert);
        }
    }

    @Override
    public int getZIndex()
    {
        return 4;
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
        bounds.setPosition(position.x, position.y);
    }

    @Override
    public AbstractBoundingVolume getBounds()
    {
        return bounds;
    }

    @Override
    public void collidesWith(ComponentCollision other)
    {
        if (other instanceof EntityGround || other instanceof EntityEvilLaser)
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
        ++age;
        if (age > 50)
        {
            dispose();
        }
    }

    @Override
    public boolean shouldRender(double translateX)
    {
        return (position.x > translateX - sprite.width && position.x < translateX + Game.GAME_WIDTH
                + sprite.width);
    }

}
