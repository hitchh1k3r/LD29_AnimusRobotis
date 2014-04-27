package com.hitchh1k3rsguide.ld48.ld29.systems;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collection;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentWindow;
import com.hitchh1k3rsguide.gameEngine.entities.AbstractEntity;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.systems.ISystem;
import com.hitchh1k3rsguide.ld48.ld29.components.ComponentCameraTracking;

public class SystemPlayerCamera implements ISystem
{

    @Override
    public void primaryUpdate(GameEngine ecs)
    {
        Collection<AbstractEntity> windowEntities = ecs.getAll(ComponentWindow.class).values();
        Collection<AbstractEntity> cameraEntities = ecs.getAll(ComponentCameraTracking.class)
                .values();
        for (AbstractEntity window : windowEntities)
        {
            Graphics2D g = ((ComponentWindow) window).getBufferContext();
            g.setTransform(new AffineTransform());
            for (AbstractEntity camera : cameraEntities)
            {
                double camPan = ((ComponentCameraTracking) camera).getCameraPan();
                g.translate(-camPan, 0);
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
