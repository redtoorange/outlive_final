package com.redtoorange.game.shaders;

import java.io.File;

/**
 * SHADER.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 21/Feb/2017
 */
public enum SHADER{
	TEST_SHADER( 0, "TestVertex.glsl",  "TestFragment.glsl"),
	TOTAL_SHADERS( 1, null, null )
	;

	private int index;
	private String vertexShader;
	private String fragmentShader;

	SHADER( int index, String vertexShader, String fragmentShader ){
		this.index = index;
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
	}

	public int getIndex(){
		return index;
	}

	public File getVertex(){
		return new File( vertexShader );
	}

	public File getFragment(){
		return new File( fragmentShader );
	}

	public static SHADER getShader( int i ){
		for(SHADER shader : SHADER.values())
			if( shader.getIndex() == i)
				return shader;
		return null;
	}
}
