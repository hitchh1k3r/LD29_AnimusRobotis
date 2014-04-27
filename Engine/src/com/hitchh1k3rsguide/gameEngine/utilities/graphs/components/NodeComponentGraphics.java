package com.hitchh1k3rsguide.gameEngine.utilities.graphs.components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.hitchh1k3rsguide.gameEngine.utilities.graphics.LayeredImage;

public interface NodeComponentGraphics
{

    public AffineTransform getTransform();

    public void draw(Graphics2D g, LayeredImage zBuffer);

}
