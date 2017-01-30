package com.redtoorange.game;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 23/Jan/2017
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
