package com.redtoorange.game.systems;

/**
 * GunType.java -
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public enum GunType {
    REVOLVER( 0 ), TOTAL_GUNS( 1 );

    private final int value;

    GunType( int value ) {
        this.value = value;
    }

    public int getValue( ) {
        return value;
    }
}
