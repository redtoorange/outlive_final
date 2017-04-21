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

import com.badlogic.gdx.utils.ArrayMap;

/**
 * SoundManager.java - This is basically a wrapper for a collection of SoundEffects that simplifies some of the more
 * annoying tasks like playing and updating the sounds.  A SoundEffect can be added to this manager at any point and
 * the manager will immediately begin to manage it's updating.  SoundEffects can be accessed and play based on a String
 * key.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class SoundManager {
    /** A collection of all the SoundEffects managed here. */
    protected ArrayMap< String, SoundEffect > sounds = new ArrayMap< String, SoundEffect >();


    /**
     * Play a specific SoundEffect.  It will continue to be automatically updated.
     *
     * @param name The key of the SoundEffect.
     */
    public void playSound( String name ) {
        if ( sounds.containsKey( name ) )
            sounds.get( name ).play();
    }

    /**
     * Continue playing all the sounds and call their updates.
     *
     * @param deltaTime The time since the last update.
     */
    public void update( float deltaTime ) {
        for ( SoundEffect se : sounds.values() ) {
            se.update( deltaTime );
        }
    }

    /**
     * Add a new SoundEffect to this manager.
     *
     * @param name        The key for the SoundEffect.
     * @param soundEffect The actual SoundEffect.
     */
    public void addSound( String name, SoundEffect soundEffect ) {
        sounds.put( name, soundEffect );
    }

    /**
     * Get a reference to a specific SoundEffect stored here.
     *
     * @param name The key of the sound effect.
     * @return A reference to the SoundEffect.
     */
    public SoundEffect getSoundEffect( String name ) {
        return sounds.get( name );
    }
}