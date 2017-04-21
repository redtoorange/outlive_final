package com.redtoorange.game.systems;

import java.io.Serializable;

/**
 * Inventory.java -
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class Inventory implements Serializable {
    private int[] ammoAmounts = new int[GunType.TOTAL_GUNS.getValue()];

    public Inventory() {
        for ( int i = 0; i < ammoAmounts.length; i++ ) {
            ammoAmounts[i] = 0;
        }
    }

    public void consume( GunType type, int amount ) {
        ammoAmounts[type.getValue()] -= amount;
    }

    public void pickup( GunType type, int amount ) {
        ammoAmounts[type.getValue()] += amount;
    }

    public int remaining( GunType type ) {
        return ammoAmounts[type.getValue()];
    }

    public void setAmount( GunType type, int amount ) {
        ammoAmounts[type.getValue()] = amount;
    }
}