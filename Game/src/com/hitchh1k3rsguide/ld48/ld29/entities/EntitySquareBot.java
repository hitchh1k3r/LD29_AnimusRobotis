package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentCollision;
import com.hitchh1k3rsguide.gameEngine.components.ComponentGravity;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMessageable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMovable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentUpkeep;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.messages.MessageRequest;
import com.hitchh1k3rsguide.gameEngine.messages.MessageResponse;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.LayeredImage;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphs.GraphicsNode;
import com.hitchh1k3rsguide.gameEngine.utilities.graphs.NodeTraversal;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.AbstractBoundingVolume;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.AxisAlignedBoundingBox;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.Vec2d;
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;
import com.hitchh1k3rsguide.ld48.ld29.utilities.SoundHelper;

public class EntitySquareBot extends AbstractEntity implements ComponentCollision,
        ComponentGravity, ComponentMovable, ComponentRenderable, ComponentUpkeep,
        ComponentMessageable
{

    public static Sprite[] sprites = null;
    public final GraphicsNode body, armLUp, armLDown, armR, wheel, shoulder, shadow;
    public Vec2d position, velocity;
    public AxisAlignedBoundingBox bounds;
    private int animationTimer;
    boolean onGround, onGroundCalc;
    int shootValue;
    int shootCooldown, jumpCooldown;
    GameEngine ecs;

    public EntitySquareBot(long index, double x, double y, GameEngine ecs)
    {
        super(index);
        if (sprites == null)
        {
            sprites = new Sprite[] { new Sprite("squareBot/Body.png", 60, 190),
                    new Sprite("squareBot/Arm.L.up.png", 86, 12),
                    new Sprite("squareBot/Arm.L.down.png", 91, 24),
                    new Sprite("squareBot/Arm.R.png", 100, 14),
                    new Sprite("squareBot/Wheel.png", 61, 8), new Sprite("squareBot/Shoulder.png"),
                    new Sprite("squareBot/Shadow.png") };
        }
        body = new GraphicsNode(sprites[0], null, null, 2);
        body.translate.set(0, 0);

        armLUp = new GraphicsNode(sprites[1], body, null, 3);
        armLUp.translate.set(-11, -33);

        armLDown = new GraphicsNode(sprites[2], armLUp, null, 2);
        armLDown.translate.set(-74, 0);

        armR = new GraphicsNode(sprites[3], body, null, 0);
        armR.translate.set(-53, -45);

        wheel = new GraphicsNode(sprites[4], body, null, 1);
        wheel.translate.set(0, 60);

        shoulder = new GraphicsNode(sprites[5], body, null, 4);
        shoulder.translate.set(-11, -33);

        shadow = new GraphicsNode(sprites[6], body, null, 0);
        shadow.translate.set(0, 185);

        body.getChildren().add(armLUp);
        body.getChildren().add(armR);
        body.getChildren().add(shoulder);
        body.getChildren().add(wheel);
        body.getChildren().add(shadow);

        armLUp.getChildren().add(armLDown);

        position = new Vec2d(x, y);
        velocity = new Vec2d(0, 0);

        bounds = new AxisAlignedBoundingBox(0, 0, 150, 263);
        bounds.setCenter(position);

        animationTimer = 0;
        onGround = false;
        onGroundCalc = false;
        shootValue = -1;
        jumpCooldown = (int) ((Math.random() * 120) + 240);
        shootCooldown = (int) ((Math.random() * 120) + 120);
        this.ecs = ecs;
    }

    @Override
    public void draw(Graphics2D g)
    {
        rotateParts();
        AffineTransform af = g.getTransform();
        g.translate(position.x, position.y);
        g.scale(0.75, 0.75);
        LayeredImage layers = new LayeredImage(Game.GAME_WIDTH, Game.GAME_HEIGHT);
        NodeTraversal.drawGraph(g, body, layers);
        layers.draw(g);
        g.setTransform(af);
    }

    private void rotateParts()
    {

        double sin = Math.sin(0.02 * animationTimer * Math.PI * 2) / 2;
        armLUp.rotation = sin;
        armLDown.rotation = -sin;
        armR.rotation = -sin * 0.75;
        wheel.rotation = sin * 0.25;
        shadow.visible = onGround;
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
        velocity.y *= 0.9;
    }

    @Override
    public double getFallingSpeed()
    {
        return velocity.y;
    }

    @Override
    public double getMaxFallSpeed(double gravity)
    {
        return 20;
    }

    @Override
    public double getGravityMultiplier()
    {
        return 0.75;
    }

    @Override
    public void setFallingSpeed(double speed)
    {
        velocity.y = speed;
    }

    @Override
    public AbstractBoundingVolume getBounds()
    {
        return bounds;
    }

    @Override
    public void collidesWith(ComponentCollision other)
    {
        if (other instanceof EntityGround)
        {
            velocity.y = 0;
            position.y = ((EntityGround) other).bounds.getMinY() - bounds.radius.y;
            onGroundCalc = true;
        }
        else if (other instanceof EntityPlasmaBolt)
        {
            SoundHelper.playSound("Explode");
            for (int i = 0; i < sprites.length - 1; ++i)
            {
                ecs.addEntity(new EntityParticle(ecs.getUniqueID(), sprites[i], position.x,
                        position.y));
            }
            dispose();
            ((EntityPlasmaBolt) other).dispose();
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
        ++animationTimer;
        if (animationTimer > 50)
            animationTimer = 0;
        onGround = onGroundCalc;
        onGroundCalc = false;
        if (onGround)
        {
            velocity.x = -3;
            if (--jumpCooldown <= 0)
            {
                jumpCooldown = (int) ((Math.random() * 120) + 240);
                velocity.y = -20;
            }
        }
        else
        {
            velocity.x = -1.5;
        }
        if (--shootCooldown <= 0)
        {
            shootCooldown = (int) ((Math.random() * 120) + 120);
            MessageRequest message = new MessageRequest(EntityPlayer.REQUEST_getPlayerPosition);
            shootValue = message.index;
            ecs.sendMessage(message);
        }
        if (position.y > Game.GAME_HEIGHT)
        {
            position.y = Game.GAME_HEIGHT + 1;
            velocity.x = -3;
        }
        if (position.x < -200)
        {
            dispose();
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
                            Math.PI, 7.5, 180));
                }
            }
        }
    }

    @Override
    public boolean shouldRender(double translateX)
    {
        return (position.x > translateX - 150 && position.x < translateX + Game.GAME_WIDTH + 150);
    }

}
