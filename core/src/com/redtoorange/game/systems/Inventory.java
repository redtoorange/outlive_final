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

import java.io.Serializable;

/**
 * Inventory.java - A glorified wrapper for an Array of ints.  This is simplified by the use of a GunType enum to determine
 * both array index and GunType as the same time.  The inventory stores indexes equal to the number of different guns,
 * each index contains the number of bullets in the player's inventory of the given type.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class Inventory implements Serializable {
    private int[] ammoAmounts = new int[GunType.TOTAL_GUNS.getValue()];

    /** Create an Inventory with slots equal to the number of different GunTypes there are. */
    public Inventory() {
        for ( int i = 0; i < ammoAmounts.length; i++ ) {
            ammoAmounts[i] = 0;
        }
    }

    /**
     * Ammo was used, remove it from the inventory.
     *
     * @param type   The GunType of the ammo.
     * @param amount The amount to remove from the inventory.
     */
    public void consume( GunType type, int amount ) {
        ammoAmounts[type.getValue()] -= amount;
    }

    /**
     * Ammo was picked up, add it to the inventory.
     *
     * @param type   The GunType of the ammo.
     * @param amount The amount to add from the inventory.
     */
    public void pickup( GunType type, int amount ) {
        ammoAmounts[type.getValue()] += amount;
    }

    /**
     * Query to see how much ammo of a type remains.
     *
     * @param type The GunType of the ammo.
     * @return How much ammo of type is left in the inventory.
     */
    public int remaining( GunType type ) {
        return ammoAmounts[type.getValue()];
    }

    /**
     * Set the inventory's ammo count of a given type.
     *
     * @param type   The GunType of the ammo.
     * @param amount The amount to set the inventory to have.
     */
    public void setAmount( GunType type, int amount ) {
        ammoAmounts[type.getValue()] = amount;
    }
}