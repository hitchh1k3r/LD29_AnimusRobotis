package com.hitchh1k3rsguide.ld48.ld29.entities;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMessageable;
import com.hitchh1k3rsguide.gameEngine.components.ComponentRenderable;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite.Property;
import com.hitchh1k3rsguide.ld48.ld29.Game;
import com.hitchh1k3rsguide.ld48.ld29.messages.MessageDeleteWorld;
import com.hitchh1k3rsguide.ld48.ld29.utilities.LevelHelper.Level;

public class EntityBackground extends AbstractEntity implements ComponentRenderable,
        ComponentMessageable
{

    public static Sprite[] sprites = null;
    public Sprite sprite = null;
    int x, y;

    public EntityBackground(long index, Level level)
    {
        super(index);
        if (sprites == null)
        {
            sprites = new Sprite[] { new Sprite("SurfaceBG.png"), new Sprite("CavesBG.png"),
                    new Sprite("LabBG.png"), new Sprite("MainBG.png") };
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
    }

    @Override
    public void draw(Graphics2D g)
    {
        AffineTransform af = g.getTransform();
        g.setTransform(new AffineTransform());
        sprite.draw(g, Property.Pos2i, Game.GAME_WIDTH / 2, Game.GAME_HEIGHT / 2);
        g.setTransform(af);
    }

    @Override
    public int getZIndex()
    {
        return 0;
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
