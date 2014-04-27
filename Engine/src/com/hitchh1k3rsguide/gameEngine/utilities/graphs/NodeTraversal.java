package com.hitchh1k3rsguide.gameEngine.utilities.graphs;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.hitchh1k3rsguide.gameEngine.utilities.graphics.LayeredImage;
import com.hitchh1k3rsguide.gameEngine.utilities.graphs.components.NodeComponentGraphics;

public class NodeTraversal
{

    public static void drawGraph(Graphics2D g, AbstractDAGNode node, LayeredImage layeredImage)
    {
        AffineTransform af = g.getTransform();
        if (node instanceof NodeComponentGraphics)
        {
            NodeComponentGraphics drawNode = (NodeComponentGraphics) node;
            g.transform(drawNode.getTransform());
            drawNode.draw(g, layeredImage);
        }
        for (AbstractDAGNode child : node.children)
        {
            drawGraph(g, child, layeredImage);
        }
        g.setTransform(af);
    }

}
