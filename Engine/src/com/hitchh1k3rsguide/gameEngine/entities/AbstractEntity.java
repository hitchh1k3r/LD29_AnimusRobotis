package com.hitchh1k3rsguide.gameEngine.entities;

public abstract class AbstractEntity
{

    public final long index;
    public boolean deleteMark;

    public AbstractEntity(long index)
    {
        this.index = index;
        deleteMark = false;
    }

    public void dispose()
    {
        deleteMark = true;
    }

}
