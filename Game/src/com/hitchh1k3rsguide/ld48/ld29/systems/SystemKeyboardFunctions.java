package com.hitchh1k3rsguide.ld48.ld29.systems;

import java.awt.event.KeyEvent;
import java.util.Collection;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentWindow;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.systems.ISystem;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentAnyKey;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentNumberKeys;

public class SystemKeyboardFunctions implements ISystem
{

    @Override
    public void primaryUpdate(GameEngine ecs)
    {
        Collection<AbstractEntity> windowEntities = ecs.getAll(ComponentWindow.class).values();
        Collection<AbstractEntity> anykeyEntities = ecs.getAll(ComponentAnyKey.class).values();
        Collection<AbstractEntity> numberEntities = ecs.getAll(ComponentNumberKeys.class).values();
        for (AbstractEntity window : windowEntities)
        {
            if (((ComponentWindow) window).isAnyKeyPressed())
            {
                for (AbstractEntity entity : anykeyEntities)
                {
                    ((ComponentAnyKey) entity).pressedAnyKey();
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_0))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(0);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_1))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(1);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_2))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(2);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_3))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(3);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_4))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(4);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_5))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(5);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_6))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(6);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_7))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(7);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_8))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(8);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_9))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(9);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD0))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(0);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD1))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(1);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD2))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(2);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD3))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(3);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD4))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(4);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD5))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(5);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD6))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(6);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD7))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(7);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD8))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(8);
                }
            }
            if (((ComponentWindow) window).isKeyPressed(KeyEvent.VK_NUMPAD9))
            {
                for (AbstractEntity entity : numberEntities)
                {
                    ((ComponentNumberKeys) entity).pressNumberKey(9);
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
