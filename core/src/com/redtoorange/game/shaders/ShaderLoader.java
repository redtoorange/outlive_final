package com.redtoorange.game.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Scanner;

/**
 * ShaderLoader.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 21/Feb/2017
 */
public class ShaderLoader {
	/**
	 * Singleton pattern for the ShaderLoader
	 */
	public static ShaderLoader S = new ShaderLoader();

	private ShaderProgram[] shaders = new ShaderProgram[SHADER.TOTAL_SHADERS.getIndex()];


	public ShaderLoader(){
		for(int i = 0; i < SHADER.TOTAL_SHADERS.getIndex(); i++){
			shaders[i] = parseShaderFile( i );
		}
	}

	public ShaderProgram getShader( SHADER shader ){
		ShaderProgram sp = null;

		if(shader.getIndex()  >= 0 && shader.getIndex() < shaders.length ) {
			sp = shaders[ shader.getIndex( ) ];
		}

		return sp;
	}

	private ShaderProgram parseShaderFile( int index ){
		ShaderProgram shader = null;

		try{
			Scanner scanner = new Scanner( SHADER.getShader( index ).getFragment() );
			String fragment = "";
			while( scanner.hasNextLine() ){
				fragment += scanner.nextLine();
				fragment += "\n";
			}
			scanner.close();

			scanner = new Scanner( SHADER.getShader( index ).getVertex() );
			String vertex = "";
			while( scanner.hasNextLine() ){
				vertex += scanner.nextLine();
				vertex += "\n";
			}
			scanner.close();

			shader = new ShaderProgram( vertex, fragment );
		}
		catch( Exception e){
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}

		return shader;
	}
}
