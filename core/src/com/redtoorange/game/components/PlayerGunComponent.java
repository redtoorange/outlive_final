package com.redtoorange.game.components;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.redtoorange.game.Global;
import com.redtoorange.game.GunType;
import com.redtoorange.game.Inventory;
import com.redtoorange.game.components.input.PlayerInputComponent;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.Bullet;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.screens.PlayScreen;
import com.redtoorange.game.systems.PhysicsSystem;
import com.redtoorange.game.systems.sound.GunSoundManager;
import com.redtoorange.game.systems.sound.SoundEffect;

/**
 * PlayerGunComponent.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 14/Jan/2017
 */
public class PlayerGunComponent extends Component {
	private final GunType type = GunType.REVOLVER;
	private final int MAX_BULLETS = 6;
	private final Player player;
	private Inventory playerInventory;
	private Texture bulletTexture;
	private Array<Bullet> bulletController = new Array<Bullet>( );
	private int bulletIndex = 0;
	private float timeTillFire = 0.0f;
	private float coolDown = .25f;
	private boolean fireBullet = false;
	private float speed = 5f;
	private int maxBulletsInGun = 6;
	private int bulletsInGun = maxBulletsInGun;
	private TextureRegion[] bulletTextures = new TextureRegion[ maxBulletsInGun + 1 ];
	private boolean needsReload = false;
	private PlayScreen playScreen;

	private SpriteComponent sc;
	private PlayerInputComponent in;
	private PhysicsSystem physicsSystem;

	private GunSoundManager gsm = new GunSoundManager( );
	private boolean reloading = false;

	private PointLight muzzelFlash;
	private float muzzleFlashTimer = 0.0f;
	private float muzzelFlashDwell = 0.05f;

	public PlayerGunComponent( PhysicsSystem physicsSystem, Player player, PlayScreen playScreen ) {
		this.playScreen = playScreen;
		this.player = player;
		this.physicsSystem = physicsSystem;
	}

	@Override
	public void start( GameObject parent ) {
		super.start( parent );

		initBullets( physicsSystem );
		playerInventory = player.getInventoy( );

		for ( int i = 0; i <= maxBulletsInGun; i++ )
			bulletTextures[ i ] = new TextureRegion( new Texture( "weapons/revolver/revolver_" + i + ".png" ) );

		playScreen.getGunUI( ).swapCurrentImage( bulletTextures[ maxBulletsInGun ] );
		sc = player.getComponent( SpriteComponent.class );
		in= player.getComponent( PlayerInputComponent.class );

		muzzelFlash = new PointLight( physicsSystem.getRayHandler(), 10 );
		muzzelFlash.setActive( false );
	}

	public void update( float deltaTime ) {
		processInput( );
		updateBullets( deltaTime );
	}

	public void draw( SpriteBatch batch ) {
		drawBullets( batch );
	}

	private void processInput( ) {
		if ( Gdx.input.isButtonPressed( Input.Buttons.LEFT ) && timeTillFire <= 0f ) {
			fireBullet = true;
		}

		if ( Gdx.input.isKeyJustPressed( Input.Keys.R ) && !reloading ) {
			reload( );
		}
	}

	private void updateBullets( float deltaTime ) {
		gsm.update( deltaTime );

		if ( fireBullet && !needsReload && !reloading ) {
			gsm.playSound( "gunshot" );
			bulletsInGun--;
			playScreen.getGunUI( ).swapCurrentImage( bulletTextures[ bulletsInGun ] );

			if ( bulletsInGun <= 0 ) {
				needsReload = true;
			}

			fireBullet( );
		} else if ( fireBullet && needsReload && !reloading ) {
			SoundEffect se = gsm.getSoundEffect( "nobullets" );

			if ( !se.isPlaying( ) )
				gsm.playSound( "nobullets" );

			fireBullet = false;
		} else {
			fireBullet = false;
		}


		for ( Bullet b : bulletController ) {
			b.update( deltaTime );
		}

		if ( timeTillFire >= 0f ) {
			timeTillFire -= deltaTime;
		}

		if ( reloading ) {
			if ( !gsm.getSoundEffect( "reloaded" ).isPlaying( ) )
				reloading = false;
		}


		if(muzzleFlashTimer > 0){
			muzzleFlashTimer -= deltaTime;
			if(muzzleFlashTimer <= 0){
				muzzelFlash.setActive( false );
			}
		}
	}

	private void reload( ) {
		if(playerInventory.remaining( type ) > 0){
			if ( bulletsInGun > 0 ) {
				playerInventory.pickup( type, bulletsInGun );
			}
			int bullets = Math.min( maxBulletsInGun, playerInventory.remaining( type ) );
			playerInventory.consume( type, bullets );
			bulletsInGun = bullets;

			playScreen.getGunUI( ).swapCurrentImage( bulletTextures[ bulletsInGun ] );

			if ( bulletsInGun > 0 ) {
				SoundEffect se = gsm.getSoundEffect( "reloaded" );

				if ( !se.isPlaying( ) )
					gsm.playSound( "reloaded" );

				needsReload = false;
				reloading = true;
			}

			playScreen.getGunUI().setAmmoCount( playerInventory.remaining( type ) );
		}
	}

	private void drawBullets( SpriteBatch batch ) {
		for ( Bullet b : bulletController ) {
			b.draw( batch );
		}
	}

	private void fireBullet( ) {
		fireBullet = false;
		timeTillFire += coolDown;

		Bullet b = bulletController.get( bulletIndex );
		bulletIndex++;
		if ( bulletIndex == MAX_BULLETS )
			bulletIndex = 0;

		Vector2 bulletPosition = sc.getCenter( );
		bulletPosition.add( new Vector2( 0.35f, -0.3f ).rotate( player.getRotation( ) ) );

		Vector2 velocity = new Vector2( in.getMousePosition( ).x - bulletPosition.x,
				in.getMousePosition( ).y - bulletPosition.y ).nor( );
		velocity.scl( speed );

		b.fire( bulletPosition, velocity, player.getRotation( ) );

		muzzelFlash.setPosition( bulletPosition );
		muzzelFlash.setColor( Color.FIREBRICK );
		muzzelFlash.setActive( true );
		muzzleFlashTimer = muzzelFlashDwell;
	}

	private void initBullets( PhysicsSystem physicsSystem ) {
		bulletTexture = new Texture( "bullet.png" );
		Sprite bulletSprite = new Sprite( bulletTexture );
		bulletSprite.setSize( 1f, 1f );
		bulletSprite.setOriginCenter( );

		for ( int i = 0; i < MAX_BULLETS; i++ ) {
			bulletController.add( new Bullet( player, new Sprite( bulletSprite ), physicsSystem, new Vector2( -1000, -1000 ), speed ) );
		}
	}

	@Override
	public void dispose( ) {
		for ( TextureRegion t : bulletTextures ) {
			t.getTexture( ).dispose( );
		}

		bulletTexture.dispose( );

		if( Global.DEBUG)
			System.out.println( this.getClass().getSimpleName() + " disposed" );
	}
}
