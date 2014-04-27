package com.hitchh1k3rsguide.gameEngine.utilities.graphs;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import com.hitchh1k3rsguide.gameEngine.utilities.graphics.LayeredImage;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite;
import com.hitchh1k3rsguide.gameEngine.utilities.graphics.Sprite.Property;
import com.hitchh1k3rsguide.gameEngine.utilities.graphs.components.NodeComponentGraphics;
import com.hitchh1k3rsguide.gameEngine.utilities.physics.Vec2d;

public class GraphicsNode extends AbstractDAGNode implements NodeComponentGraphics
{

    AffineTransform matrix;
    public Sprite graphic;
    int zIndex;
    public Vec2d origin;
    public Vec2d translate;
    public double rotation;
    public boolean visible;
    public boolean flip;

    public GraphicsNode(Sprite graphic, AbstractDAGNode parent,
            ArrayList<AbstractDAGNode> children, int zIndex)
    {
        super(parent, children);
        matrix = new AffineTransform(); // Identity Matrix
        this.graphic = graphic;
        this.zIndex = zIndex;
        this.origin = new Vec2d(0, 0);
        this.translate = new Vec2d(0, 0);
        this.rotation = 0;
        this.visible = true;
    }

    @Override
    public AffineTransform getTransform()
    {
        matrix = AffineTransform.getTranslateInstance((int) (origin.x + translate.x),
                (int) (origin.y + translate.y));
        matrix.rotate(rotation);
        if (flip)
        {
            matrix.scale(-1, 1);
        }
        return matrix;
    }

    @Override
    public void draw(Graphics2D g, LayeredImage zBuffer)
    {
        if (visible)
        {
            graphic.draw(g, Property.ZBuffer1o, zBuffer, Property.ZIndex1i, zIndex);
        }
    }

}
