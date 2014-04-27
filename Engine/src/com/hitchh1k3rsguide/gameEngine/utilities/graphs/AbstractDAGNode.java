package com.hitchh1k3rsguide.gameEngine.utilities.graphs;

import java.util.ArrayList;

public abstract class AbstractDAGNode
{

    ArrayList<AbstractDAGNode> children;
    AbstractDAGNode parent;

    public AbstractDAGNode(AbstractDAGNode parent, ArrayList<AbstractDAGNode> children)
    {
        this.parent = parent;
        this.children = children;
        if (children == null)
        {
            this.children = new ArrayList<AbstractDAGNode>();
        }
    }

    public void setChildren(ArrayList<AbstractDAGNode> children)
    {
        this.children = children;
    }

    public ArrayList<AbstractDAGNode> getChildren()
    {
        return children;
    }

}
