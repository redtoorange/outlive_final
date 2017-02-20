//package com.redtoorange.game.systems;
//
////TODO: Stub class.  Need to implement the entire System.
////TODO: Z-Ordering Code.
//
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.utils.Array;
//import com.redtoorange.game.Global;
//import com.redtoorange.game.components.Component;
//import com.redtoorange.game.engine.Drawable;
//import com.redtoorange.game.engine.PostLightingDraw;
//import com.redtoorange.game.gameobject.GameObject;
//
///**
// * RenderingSystem.java - A system that will manage and render all Render Components registered with the engine.
// * Handles Z-Ordering of RenderComponents.
// *
// * @author - Andrew M.
// * @version - 20/Jan/2017
// */
//public class RenderingSystem extends System {
//	private Array<Drawable> drawables;
//	private Array<PostLightingDraw> postLightingDraws;
//
//	public RenderingSystem(){
//		drawables = new Array<Drawable>(  );
//		postLightingDraws = new Array<PostLightingDraw>(  );
//	}
//
//	@Override
//	public void entityAdded( GameObject e ) {
//		for( Component c : e.getComponents()) {
//			if ( c instanceof Drawable )
//				drawables.add( ( Drawable ) c );
//			if ( c instanceof PostLightingDraw )
//				postLightingDraws.add( ( PostLightingDraw ) c );
//		}
//	}
//
//	@Override
//	public void entityRemoved( GameObject e ) {
//		for( Component c : e.getComponents()) {
//			if ( c instanceof Drawable )
//				drawables.removeValue( ( Drawable ) c, true );
//			if ( c instanceof PostLightingDraw )
//				postLightingDraws.removeValue( ( PostLightingDraw ) c, true );
//		}
//	}
//
//	public void render( SpriteBatch batch ){
//		for(Drawable d : drawables)
//			d.preLighting( batch );
//	}
//
//	public void postLightingRender( SpriteBatch batch ){
//		for(PostLightingDraw d : postLightingDraws)
//			d.postLightingDraw( batch );
//	}
//
//	public void dispose(){
//		drawables.clear();
//		postLightingDraws.clear();
//
//		if( Global.DEBUG)
//			java.lang.System.out.println( "Rendering System Disposed" );
//	}
//}