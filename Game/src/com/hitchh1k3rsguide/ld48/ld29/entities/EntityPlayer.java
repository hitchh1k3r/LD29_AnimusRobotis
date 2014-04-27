package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentArrowKeys;
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
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentCameraTracking;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentMouseTracking;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageSetLevelWidth;
import com.hitchh1k3rsguide.ld48.ld29.utilities.LevelHelper;
import com.hitchh1k3rsguide.ld48.ld29.utilities.SoundHelper;

public class EntityPlayer extends AbstractEntity implements ComponentArrowKeys, ComponentCollision,
        ComponentGravity, ComponentMovable, ComponentRenderable, ComponentUpkeep,
        ComponentMouseTracking, ComponentMessageable, ComponentCameraTracking
{

    public static final String REQUEST_getPlayerPosition = "EntityPlayer_getPlayerPosition";

    public static Sprite[] sprites = null;
    public final GraphicsNode body, armL, armR, legLUp, legLDown, legRUp, legRDown, shoulder,
            shadow;
    public Vec2d position, velocity;
    public AxisAlignedBoundingBox bounds;
    private int animationTimer;
    boolean onGround, onGroundCalc;
    int[] wasd;
    double armAngle, armDisplayAngle;
    Point mousePos;
    GameEngine ecs = null;
    Color hurtColor;
    int hurtTimer;
    Direction hurtDirection;
    int laserCooldown, longLaserCooldown;
    int maxX;
    float HP, Heat;
    int heatSFXCooldown;

    public EntityPlayer(long index, GameEngine ecs)
    {
        super(index);
        if (sprites == null)
        {
            sprites = new Sprite[] { new Sprite("player/Body.png", 66, 232),
                    new Sprite("player/Arm.L.png", 3, 6), new Sprite("player/Arm.R.png", 3, 16),
                    new Sprite("player/Leg.L.up.png", 15, 7),
                    new Sprite("player/Leg.L.down.png", 29, 6),
                    new Sprite("player/Leg.R.up.png", 15, 5),
                    new Sprite("player/Leg.R.down.png", 30, 6), new Sprite("player/Shoulder.png"),
                    new Sprite("player/Shadow.png"), new Sprite("player/Body.Hurt.png", 66, 232),
                    new Sprite("player/Arm.L.Hurt.png", 3, 6),
                    new Sprite("player/Arm.R.Hurt.png", 3, 16),
                    new Sprite("player/Leg.L.up.Hurt.png", 15, 7),
                    new Sprite("player/Leg.L.down.Hurt.png", 29, 6),
                    new Sprite("player/Leg.R.up.Hurt.png", 15, 5),
                    new Sprite("player/Leg.R.down.Hurt.png", 30, 6),
                    new Sprite("player/Shoulder.Hurt.png") };
        }
        body = new GraphicsNode(sprites[0], null, null, 3);
        body.translate.set(0, 0);

        armL = new GraphicsNode(sprites[1], body, null, 2);
        armL.translate.set(-3, -23);

        armR = new GraphicsNode(sprites[2], body, null, 5);
        armR.translate.set(-6, -20);

        legLUp = new GraphicsNode(sprites[3], body, null, 2);
        legLUp.translate.set(9, 71);

        legLDown = new GraphicsNode(sprites[4], legLUp, null, 1);
        legLDown.translate.set(0, 50);

        legRUp = new GraphicsNode(sprites[5], body, null, 5);
        legRUp.translate.set(-16, 73);

        legRDown = new GraphicsNode(sprites[6], legRUp, null, 4);
        legRDown.translate.set(0, 51);

        shoulder = new GraphicsNode(sprites[7], body, null, 5);
        shoulder.translate.set(-6, -23);

        shadow = new GraphicsNode(sprites[8], body, null, 0);
        shadow.translate.set(10, 195);

        body.getChildren().add(armL);
        body.getChildren().add(legLUp);
        body.getChildren().add(legRUp);
        body.getChildren().add(armR);
        body.getChildren().add(shoulder);
        body.getChildren().add(shadow);

        legLUp.getChildren().add(legLDown);
        legRUp.getChildren().add(legRDown);
        position = new Vec2d(300, 300);
        velocity = new Vec2d(0, 0);

        bounds = new AxisAlignedBoundingBox(0, 0, 133, 300);
        bounds.setCenter(position);

        animationTimer = 0;
        onGround = false;
        onGroundCalc = false;
        wasd = new int[] { KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D };
        mousePos = new Point();
        armAngle = 0;
        armDisplayAngle = 0;
        hurtTimer = 0;
        hurtColor = new Color(255, 0, 0, 0);
        laserCooldown = 0;
        maxX = 10;
        HP = 1.0f;
        Heat = 0.0f;
        this.ecs = ecs;
        heatSFXCooldown = 0;
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
        g.setTransform(new AffineTransform());
        drawHUD(g);
        g.setTransform(af);
    }

    private void drawHUD(Graphics2D g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(10, 10, 500, 38);
        g.setColor(new Color(1.0f - HP, HP, 0.0f));
        g.fillRect(12, 12, (int) (496 * HP), 16);
        g.setColor(new Color(1.0f, 1.0f - Heat, 0.0f));
        g.fillRect(12, 30, (int) (496 * Heat), 16);
    }

    private void rotateParts()
    {
        double vel = Math.min(Math.abs(velocity.x) + Math.abs(velocity.y), 20);
        if (vel < 0.1)
        {
            legLUp.rotation = 0;
            legRUp.rotation = 0;
            legLDown.rotation = 0;
            legRDown.rotation = 0;
            armL.rotation = (Math.PI * 0.2);
        }
        else
        {
            double sin = Math.sin(0.02 * animationTimer * Math.PI * 2) * vel / 40;
            legLUp.rotation = sin;
            legRUp.rotation = -sin;
            legLDown.rotation = sin;
            legRDown.rotation = -sin;
            armL.rotation = sin * 2 + (Math.PI * 0.2);
        }
        if (hurtTimer > 0)
        {
            body.flip = (hurtDirection == Direction.LEFT);
        }
        armR.rotation = armDisplayAngle;
        shadow.visible = onGround;
    }

    @Override
    public int getZIndex()
    {
        return 5;
    }

    @Override
    public Vec2d getVelocity()
    {
        return velocity;
    }

    @Override
    public void setPosition(double x, double y)
    {
        position.x = x;
        position.y = y;
    }

    @Override
    public void move()
    {
        if (hurtTimer == 0)
        {
            velocity.x *= 0.9;
            velocity.y *= 0.9;
        }
        if (position.x > maxX - bounds.radius.x)
        {
            position.x = maxX - bounds.radius.x;
        }
        if (position.x < bounds.radius.x)
        {
            position.x = bounds.radius.x;
        }
    }

    @Override
    public Vec2d getPosition()
    {
        return position;
    }

    @Override
    public double getFallingSpeed()
    {
        return velocity.y;
    }

    @Override
    public double getMaxFallSpeed(double gravity)
    {
        return 30;
    }

    @Override
    public double getGravityMultiplier()
    {
        return 1;
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
        else if (hurtTimer == 0 && other instanceof EntityRoundBot)
        {
            Vec2d direction = position.copy();
            direction.scale(-1);
            direction.add(((EntityRoundBot) other).position);
            hurt(direction, 1);
        }
        else if (hurtTimer == 0 && other instanceof EntitySquareBot)
        {
            Vec2d direction = position.copy();
            direction.scale(-1);
            direction.add(((EntitySquareBot) other).position);
            hurt(direction, 3);
        }
        else if (hurtTimer == 0 && other instanceof EntityEvilLaser)
        {
            Vec2d direction = position.copy();
            direction.scale(-1);
            direction.add(((EntityEvilLaser) other).position);
            ((AbstractEntity) other).dispose();
            hurt(direction, 2);
        }
    }

    @Override
    public int[] getKeyAliases()
    {
        return wasd;
    }

    @Override
    public void pressArrow(Direction arrow)
    {
        if (hurtTimer == 0)
        {
            if ((arrow == Direction.UP || arrow == Direction.UP_LEFT || arrow == Direction.UP_RIGHT)
                    && onGround)
            {
                velocity.y = -50;
                SoundHelper.playSound("Jump");
            }
            if (arrow == Direction.LEFT || arrow == Direction.DOWN_LEFT
                    || arrow == Direction.UP_LEFT)
            {
                velocity.x = (onGround ? -12 : -10);
            }
            if (arrow == Direction.RIGHT || arrow == Direction.DOWN_RIGHT
                    || arrow == Direction.UP_RIGHT)
            {
                velocity.x = (onGround ? 12 : 10);
            }
        }
    }

    @Override
    public void primaryUpdate(GameEngine ecs)
    {
        update(ecs);
    }

    public void hurt(Vec2d direction, int power)
    {
        body.graphic = sprites[9];
        armL.graphic = sprites[10];
        armR.graphic = sprites[11];
        legLUp.graphic = sprites[12];
        legLDown.graphic = sprites[13];
        legRUp.graphic = sprites[14];
        legRDown.graphic = sprites[15];
        shoulder.graphic = sprites[16];
        hurtTimer = 20;
        direction.normalize();
        if (direction.y < -0.1)
        {
            if (direction.x < 0)
                velocity.set(-10, 10);
            else
                velocity.set(10, 10);
        }
        else
        {
            velocity.set(direction.x * -10, -20);
        }
        if (onGround)
        {
            position.y -= 10;
        }
        if (direction.x < 0)
        {
            hurtDirection = Direction.LEFT;
        }
        else
        {
            hurtDirection = Direction.RIGHT;
        }
        HP -= (float) power / 15;
        if (HP <= 0.01)
        {
            HP = 0;
            LevelHelper.restartLevel(ecs);
            SoundHelper.playSound("Death");
        }
        else
        {
            SoundHelper.playSound("Hurt");
        }
    }

    @Override
    public void update(GameEngine ecs)
    {
        ++animationTimer;
        if (animationTimer > 50)
        {
            animationTimer = 0;
        }
        if (laserCooldown > 0)
        {
            --laserCooldown;
        }
        if (heatSFXCooldown > 0)
        {
            --heatSFXCooldown;
        }
        if (longLaserCooldown > 0)
        {
            --longLaserCooldown;
        }
        else if (Heat > 0)
        {
            Heat -= 0.01f;
            if (Heat < 0)
            {
                Heat = 0;
            }
        }
        if (HP < 1)
        {
            HP += 0.00001f;
            if (HP > 1)
            {
                HP = 1;
            }
        }
        onGround = onGroundCalc;
        onGroundCalc = false;
        if (Math.abs(velocity.x) + Math.abs(velocity.y) > 0.1)
        {
            updateMouse(mousePos.x, mousePos.y);
        }
        if (hurtTimer > 0)
        {
            --hurtTimer;
            if (hurtTimer <= 0)
            {
                body.graphic = sprites[0];
                armL.graphic = sprites[1];
                armR.graphic = sprites[2];
                legLUp.graphic = sprites[3];
                legLDown.graphic = sprites[4];
                legRUp.graphic = sprites[5];
                legRDown.graphic = sprites[6];
                shoulder.graphic = sprites[7];
            }
        }
    }

    @Override
    public void updateMouse(int x, int y)
    {
        mousePos = new Point(x, y);
        double rawAngle = Math.atan2(x - position.x + getCameraPan(), y - position.y);
        armAngle = (Math.PI * 0.5) - rawAngle;
        armDisplayAngle = (Math.PI * 0.5) - Math.abs(rawAngle);
        body.flip = (rawAngle < 0);
    }

    @Override
    public void click()
    {
        if (hurtTimer == 0 && laserCooldown == 0)
        {
            laserCooldown = 15;
            longLaserCooldown = 60;
            Heat += 0.1;
            if (Heat >= 1)
            {
                Heat = 1;
                laserCooldown = 60 * 3;
                longLaserCooldown = 60 * 3;
            }
            SoundHelper.playSound("Laser");
            ecs.addEntity(new EntityPlasmaBolt(ecs.getUniqueID(), position.x, position.y, armAngle,
                    15, 200));
        }
        if (Heat >= 1 && heatSFXCooldown == 0)
        {
            heatSFXCooldown = 25;
            SoundHelper.playSound("OverHeat");
        }
    }

    @Override
    public void getMessage(GameEngine ecs, IMessage message)
    {
        if (message instanceof MessageDeleteWorld)
        {
            dispose();
        }
        else if (message instanceof MessageSetLevelWidth)
        {
            maxX = ((MessageSetLevelWidth) message).width;
        }
        else if (message instanceof MessageRequest)
        {
            if (((MessageRequest) message).requestName.equals(REQUEST_getPlayerPosition))
            {
                ecs.sendMessage(new MessageResponse(((MessageRequest) message).index,
                        REQUEST_getPlayerPosition, position));
            }
        }
    }

    @Override
    public double getCameraPan()
    {
        return Math.min(Math.max(position.x - (Game.GAME_WIDTH / 4), 0), maxX - Game.GAME_WIDTH);
    }

    @Override
    public boolean shouldRender(double translateX)
    {
        return true;
    }

}
