package com.hitchh1k3rsguide.ld48.ld29.messages;

import com.hitchh1k3rsguide.gameEngine.messages.IMessage;

public class MessageSetLevelWidth implements IMessage
{

    public int width;

    public MessageSetLevelWidth(int width)
    {
        this.width = width;
    }

}
