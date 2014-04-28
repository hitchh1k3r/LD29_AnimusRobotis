package com.hitchh1k3rsguide.gameEngine.entities;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

import com.hitchh1k3rsguide.gameEngine.GameEngine;
import com.hitchh1k3rsguide.gameEngine.components.ComponentMessageable;
import com.hitchh1k3rsguide.gameEngine.messages.IMessage;
import com.hitchh1k3rsguide.gameEngine.messages.MessageStopMusic;

public class EntityMusic extends AbstractEntity implements ComponentMessageable
{

    Music song;

    public EntityMusic(long index, String songName, float length)
    {
        super(index);
        song = TinySound.loadMusic(getClass().getResource("/assets/snd/" + songName));
        song.play(true);
    }

    @Override
    public void dispose()
    {
        super.dispose();
        song.stop();
        song.unload();
    }

    @Override
    public void getMessage(GameEngine ecs, IMessage message)
    {
        if (message instanceof MessageStopMusic)
        {
            dispose();
        }
    }

}
