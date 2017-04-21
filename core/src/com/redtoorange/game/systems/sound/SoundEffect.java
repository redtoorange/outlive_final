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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * SoundEffect.java - A model for a Sound Effect.  It will load itself from the specified file location.  Based on the
 * given length, it will only play for as long as the length is, turning itself off automatically.  It cannot be forced
 * to play multiple times, if play is called while it's playing, it will simply stop the current sound, then begin
 * playing it again.  If multiple overlapping sounds are needed, a sound pool must be used.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public class SoundEffect {
    private Sound sound;
    private float length;
    private float currentTime = 0;
    private boolean playing = false;

    /**
     * @param fileName The file to load in.
     * @param length   The length of the sound effect.
     */
    public SoundEffect( String fileName, float length ) {
        this.length = length;

        sound = Gdx.audio.newSound( Gdx.files.internal( fileName ) );
    }

    /**
     * Continue playing this sound until the timer runs out, then set the playing flag to false.
     *
     * @param deltaTime Time since last update.
     */
    public void update( float deltaTime ) {
        if ( playing ) {
            currentTime += deltaTime;
            if ( currentTime >= length ) {
                playing = false;
                currentTime = 0;
            }
        }
    }

    /** @return Is this sound currently playing? */
    public boolean isPlaying() {
        return playing;
    }

    /** Play this sound. */
    public void play() {
        playing = true;
        sound.play();
    }
}
