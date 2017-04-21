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

package com.redtoorange.game.systems;

/**
 * GunType.java - Represent all of the GunTypes in the game as enumerated types.  This was done so that the Inventory
 * class could be vastly simplified to an array wrapper.  Each GunType has an internal type value, which is used as
 * it's array index inside of the player's inventory.  This also allows for ammo power-ups to be randomly generated with
 * a random GunType.
 *
 * @author Andrew McGuiness
 * @version 21/Apr/2017
 */
public enum GunType {
    REVOLVER( 0 ), TOTAL_GUNS( 1 );

    private final int value;

    /** @param value The array Index of the Gun in an Inventory. */
    GunType( int value ) {
        this.value = value;
    }

    /** @return Get the array Index of the Gun in an Inventory. */
    public int getValue() {
        return value;
    }
}
