package com.hitchh1k3rsguide.ld48.ld29.systems;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Collection;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentWindow;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.systems.ISystem;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentMouseTracking;

public class SystemMouseTracking implements ISystem
{

    Point lastMousePos;

    public SystemMouseTracking()
    {
        lastMousePos = new Point();
    }

    @Override
    public void primaryUpdate(GameEngine ecs)
    {
        Collection<AbstractEntity> windowEntities = ecs.getAll(ComponentWindow.class).values();
        Collection<AbstractEntity> mouseEntities = ecs.getAll(ComponentMouseTracking.class)
                .values();
        for (AbstractEntity window : windowEntities)
        {
            Point pos = ((ComponentWindow) window).getMousePos();
            if (pos != lastMousePos)
            {
                lastMousePos = pos;
                for (AbstractEntity mouse : mouseEntities)
                {
                    ((ComponentMouseTracking) mouse).updateMouse(pos.x, pos.y);
                }
            }
            if (((ComponentWindow) window).isMousePressed(MouseEvent.BUTTON1)
                    || ((ComponentWindow) window).isMouseRepeat(MouseEvent.BUTTON1))
            {
                for (AbstractEntity mouse : mouseEntities)
                {
                    ((ComponentMouseTracking) mouse).click();
                }
            }
        }
    }

    @Override
    public void update(GameEngine ecs)
    {
    }

    @Override
    public void getMessage(GameEngine ecs, IMessage message)
    {
    }

}
