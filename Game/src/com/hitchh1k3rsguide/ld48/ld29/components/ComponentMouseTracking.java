package com.hitchh1k3rsguide.ld48.ld29.components;

import com.hitchh1k3rsguide.gameEngine.components.IComponent;

public interface ComponentMouseTracking extends IComponent
{

    public void updateMouse(int x, int y);

    public void click();

}
