package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Graphics2D;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMessageable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentUpkeep;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.messages.MessageRequest;
import com.hitchh1k3rsguide.gameEngine.messages.MessageResponse;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite.Property;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.Vec2d;
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;
import com.hitchh1k3rsguide.ld48.ld29.utilities.SoundHelper;

public class EntityLaserTurret extends AbstractEntity implements ComponentUpkeep,
        ComponentRenderable, ComponentMessageable
{

    Vec2d position;
    public static Sprite sprite = null;
    int laserCooldown;
    int laserInterval;
    int shootValue;

    public EntityLaserTurret(long index, double x, double y, int interval)
    {
        super(index);
        position = new Vec2d(x, y);
        if (sprite == null)
        {
            sprite = new Sprite("LaserTurret.png");
        }
        laserInterval = interval;
        laserCooldown = laserInterval;
        shootValue = -1;
    }

    @Override
    public void draw(Graphics2D g)
    {
        sprite.draw(g, Property.Pos2i, (int) position.x, (int) position.y);
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
    public void primaryUpdate(GameEngine ecs)
    {
        update(ecs);
    }

    @Override
    public void update(GameEngine ecs)
    {
        if (--laserCooldown <= 0)
        {
            laserCooldown = laserInterval;
            MessageRequest message = new MessageRequest(EntityPlayer.REQUEST_getPlayerPosition);
            shootValue = message.index;
            ecs.sendMessage(message);
        }
    }

    @Override
    public void getMessage(GameEngine ecs, IMessage message)
    {
        if (message instanceof MessageDeleteWorld)
        {
            dispose();
        }
        else if (message instanceof MessageResponse)
        {
            if (((MessageResponse) message).index == shootValue
                    && ((MessageResponse) message).requestName
                            .equals(EntityPlayer.REQUEST_getPlayerPosition))
            {
                Vec2d playerPos = (Vec2d) ((MessageResponse) message).payload;
                if (Math.abs(playerPos.x - position.x + (Game.GAME_WIDTH / 2)) < Game.GAME_WIDTH)
                {
                    SoundHelper.playSound("EvilLaser");
                    ecs.addEntity(new EntityEvilLaser(ecs.getUniqueID(), position.x, position.y,
                            Math.PI / 2, 7.5, 25));
                }
            }
        }
    }

}
