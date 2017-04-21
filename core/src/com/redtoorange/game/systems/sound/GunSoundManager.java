/*
 * Copyright 2017  Andrew James McGuiness
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated  documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the  rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit   persons to whom the Software is furnished to do
 *   so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.redtoorange.game.systems.sound;

/**
 * GunSoundManager.java - Specialized Sound Manager that automatically loads in all the required sun sounds for the game.
 * This stream lines the process of calling a gun sound from anywhere in the game.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class GunSoundManager extends SoundManager {
    /** Create a new GunSoundManager and load in the default sounds. */
    public GunSoundManager() {
        super();

        sounds.put( "gunshot", new SoundEffect( "sounds/gunshot.wav", 0.15f ) );
        sounds.put( "bullethit", new SoundEffect( "sounds/bullethit.wav", 0.15f ) );
        sounds.put( "reloaded", new SoundEffect( "sounds/reloaded.wav", 2f ) );
        sounds.put( "nobullets", new SoundEffect( "sounds/nobullets.wav", 0.25f ) );
    }
}
