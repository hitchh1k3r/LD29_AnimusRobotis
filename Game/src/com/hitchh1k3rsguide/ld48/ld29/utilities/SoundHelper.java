package com.hitchh1k3rsguide.ld48.ld29.utilities;

import java.util.HashMap;
import java.util.Map;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import com.hitchh1k3rsguide.ld48.ld29.Game;

public class SoundHelper
{

    private static Map<String, Sound> sounds = new HashMap<String, Sound>();

    public static void playSound(String sound)
    {
        if (!sounds.containsKey(sound) || sounds.get(sound) == null)
        {
            sounds.put(sound,
                    TinySound.loadSound(Game.class.getResource("/assets/snd/" + sound + ".wav")));
        }
        try
        {
            sounds.get(sound).play();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
