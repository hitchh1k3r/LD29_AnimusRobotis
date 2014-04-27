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
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.LayeredImage;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphs.GraphicsNode;
import com.hitchh1k3rsguide.gameEngine.utilities.graphs.NodeTraversal;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.AbstractBoundingVolume;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.CircularBounding;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.Vec2d;
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;
import com.hitchh1k3rsguide.ld48.ld29.utilities.SoundHelper;

public class EntityRoundBot extends AbstractEntity implements ComponentCollision, ComponentGravity,
        ComponentMovable, ComponentRenderable, ComponentUpkeep, ComponentMessageable
{

    public static Sprite[] sprites = null;
    public final GraphicsNode body, armL, armR, shoulder, shadow;
    public Vec2d position, velocity;
    public CircularBounding bounds;
    private int animationTimer;
    boolean onGround, onGroundCalc;
    int jumpCooldown;
    GameEngine ecs;

    public EntityRoundBot(long index, double x, double y, GameEngine ecs)
    {
        super(index);
        if (sprites == null)
        {
            sprites = new Sprite[] { new Sprite("roundBot/Body.png", 115, 155),
                    new Sprite("roundBot/Arm.L.png", 133, 4),
                    new Sprite("roundBot/Arm.R.png", 108, 10), new Sprite("roundBot/Shoulder.png"),
                    new Sprite("roundBot/Shadow.png") };
        }
        body = new GraphicsNode(sprites[0], null, null, 2);
        body.translate.set(0, 0);

        armL = new GraphicsNode(sprites[1], body, null, 2);
        armL.translate.set(0, 66);

        armR = new GraphicsNode(sprites[2], body, null, 1);
        armR.translate.set(0, 66);

        shoulder = new GraphicsNode(sprites[3], body, null, 2);
        shoulder.translate.set(0, 66);

        shadow = new GraphicsNode(sprites[4], body, null, 0);
        shadow.translate.set(1, 114);

        body.getChildren().add(armL);
        body.getChildren().add(armR);
        body.getChildren().add(shoulder);
        body.getChildren().add(shadow);

        position = new Vec2d(x, y);
        velocity = new Vec2d(0, 0);

        bounds = new CircularBounding(position, 75);
        // bounds = new AxisAlignedBoundingBox(0, 0, 150, 225);
        // bounds.setCenter(position);

        animationTimer = 0;
        onGround = false;
        onGroundCalc = false;

        jumpCooldown = (int) ((Math.random() * 120) + 120);
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
        armL.rotation = sin;
        armR.rotation = -sin;
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
            position.y = ((EntityGround) other).bounds.getMinY() - bounds.radius;
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
                jumpCooldown = (int) ((Math.random() * 120) + 120);
                velocity.y = -15;
            }
        }
        else
        {
            velocity.x = -1.5;
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
    }

    @Override
    public boolean shouldRender(double translateX)
    {
        return (position.x > translateX - 150 && position.x < translateX + Game.GAME_WIDTH + 150);
    }

}
