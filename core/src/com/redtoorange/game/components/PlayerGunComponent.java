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
import com.redtoorange.game.components.input.PlayerInputComponent;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.gameobject.Bullet;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.states.MissionState;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.GunType;
import com.redtoorange.game.systems.Inventory;
import com.redtoorange.game.systems.PhysicsSystem;
import com.redtoorange.game.systems.sound.GunSoundManager;
import com.redtoorange.game.systems.sound.SoundEffect;

/**
 * PlayerGunComponent.java - The player's gun is represented as a component that has a pool of GameObjects, bullets, that
 * it managed.  It handles updating and drawing them on it's own.  When the player is out of ammo, he must reload to fire
 * again.  The sound manager is linked into to play sounds for firing and reloading.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class PlayerGunComponent extends Component {
    private final GunType type = GunType.REVOLVER;
    private final int MAX_BULLETS = 6;
    private Player player;
    private Inventory playerInventory;
    private Texture bulletTexture;
    private Array< Bullet > bulletPool = new Array< Bullet >();
    private int bulletIndex = 0;
    private float timeTillFire = 0.0f;
    private float coolDown = .25f;
    private boolean fireBullet = false;
    private float speed = 5f;
    private int maxBulletsInGun = 6;
    private int bulletsInGun = maxBulletsInGun;
    private TextureRegion[] bulletTextures = new TextureRegion[maxBulletsInGun + 1];
    private boolean needsReload = false;
    private MissionState missionState;
    private SpriteComponent sc;
    private PlayerInputComponent in;
    private PhysicsSystem physicsSystem;
    private GunSoundManager gsm = new GunSoundManager();
    private boolean reloading = false;
    private PointLight muzzelFlash;
    private float muzzleFlashTimer = 0.0f;
    private float muzzelFlashDwell = 0.05f;

    /**
     * @param physicsSystem The world's physicsSystem.
     * @param missionState  The current mission state, used to update the UI need needed.
     */
    public PlayerGunComponent( PhysicsSystem physicsSystem, MissionState missionState ) {
        this.missionState = missionState;
        this.physicsSystem = physicsSystem;

        muzzelFlash = new PointLight( missionState.getLightingSystem().getRayHandler(), 10 );
        muzzelFlash.setActive( false );
    }

    /**
     * Setup the bullet pool.
     *
     * @param parent The owning GameObject.
     */
    @Override
    public void start( GameObject parent ) {
        super.start( parent );

        player = ( Player ) parent;

        initBullets( physicsSystem );
        playerInventory = player.getInventoy();

        for ( int i = 0; i <= maxBulletsInGun; i++ )
            bulletTextures[i] = new TextureRegion( new Texture( "weapons/revolver/revolver_" + i + ".png" ) );

        missionState.getGunUI().swapCurrentImage( bulletTextures[maxBulletsInGun] );
        sc = player.getComponent( SpriteComponent.class );
        in = player.getComponent( PlayerInputComponent.class );


    }

    /** @param deltaTime The time since the last update. */
    public void update( float deltaTime ) {
        processInput();
        updateBullets( deltaTime );
    }

    /** @param batch Batch to draw all the bullets with. */
    public void draw( SpriteBatch batch ) {
        drawBullets( batch );
    }

    /** handle the fire and reload keys. */
    private void processInput() {
        if ( Gdx.input.isButtonPressed( Input.Buttons.LEFT ) && timeTillFire <= 0f )
            fireBullet = true;
        if ( Gdx.input.isKeyJustPressed( Input.Keys.R ) && !reloading )
            reload();
    }

    /**
     * Update the position of all the bullets in the bullet pool and recalculate the timers.
     *
     * @param deltaTime Time since the last update.
     */
    private void updateBullets( float deltaTime ) {
        gsm.update( deltaTime );

        //handle if a bullet was fired
        if ( fireBullet && !needsReload && !reloading ) {
            gsm.playSound( "gunshot" );
            bulletsInGun--;
            missionState.getGunUI().swapCurrentImage( bulletTextures[bulletsInGun] );

            if ( bulletsInGun <= 0 )
                needsReload = true;

            fireBullet();
        } else if ( fireBullet && needsReload && !reloading ) {
            SoundEffect se = gsm.getSoundEffect( "nobullets" );

            if ( !se.isPlaying() )
                gsm.playSound( "nobullets" );

            fireBullet = false;
        } else
            fireBullet = false;

        //update all the bullets
        for ( Bullet b : bulletPool )
            b.update( deltaTime );

        if ( timeTillFire >= 0f )
            timeTillFire -= deltaTime;

        //play the sound effect for reloading
        if ( reloading )
            if ( !gsm.getSoundEffect( "reloaded" ).isPlaying() )
                reloading = false;

        //Hide the flash after the cooldown
        if ( muzzleFlashTimer > 0 ) {
            muzzleFlashTimer -= deltaTime;
            if ( muzzleFlashTimer <= 0 )
                muzzelFlash.setActive( false );
        }
    }

    /** Reload the gun with available bullets from the player's inventory. */
    private void reload() {
        if ( playerInventory.remaining( type ) > 0 ) {
            if ( bulletsInGun > 0 )
                playerInventory.pickup( type, bulletsInGun );
            int bullets = Math.min( maxBulletsInGun, playerInventory.remaining( type ) );
            playerInventory.consume( type, bullets );
            bulletsInGun = bullets;

            missionState.getGunUI().swapCurrentImage( bulletTextures[bulletsInGun] );

            if ( bulletsInGun > 0 ) {
                SoundEffect se = gsm.getSoundEffect( "reloaded" );

                if ( !se.isPlaying() )
                    gsm.playSound( "reloaded" );

                needsReload = false;
                reloading = true;
            }

            missionState.getGunUI().setAmmoCount( playerInventory.remaining( type ) );
        }
    }

    /**
     * Draw all the bullets in the bullet pool.
     *
     * @param batch The batch to use to draw the bullets.
     */
    private void drawBullets( SpriteBatch batch ) {
        for ( Bullet b : bulletPool )
            b.preLighting( batch );
    }

    /** Fire a bullet from the gun and create the muzzle flash. */
    private void fireBullet() {
        //set timers
        fireBullet = false;
        timeTillFire += coolDown;

        //grab the next available bullet
        Bullet b = bulletPool.get( bulletIndex );
        bulletIndex++;
        if ( bulletIndex == MAX_BULLETS )
            bulletIndex = 0;

        //set the bullet's position and velocity.
        Vector2 bulletPosition = sc.getCenter();
        bulletPosition.add( new Vector2( 0.35f, -0.3f ).rotate( player.getRotation() ) );

        Vector2 velocity = new Vector2( in.getMousePosition().x - bulletPosition.x,
                in.getMousePosition().y - bulletPosition.y ).nor();
        velocity.scl( speed );

        b.fire( bulletPosition, velocity, player.getRotation() );

        //Fire off the muzzle flash
        muzzelFlash.setPosition( bulletPosition );
        muzzelFlash.setColor( Color.FIREBRICK );
        muzzelFlash.setActive( true );
        muzzleFlashTimer = muzzelFlashDwell;
    }

    /**
     * Create all the bullets for the bullet pool.
     *
     * @param physicsSystem The physicsSystem that the bullets will be spawned in.
     */
    private void initBullets( PhysicsSystem physicsSystem ) {
        //Pull the texture
        bulletTexture = new Texture( "bullet.png" );
        Sprite bulletSprite = new Sprite( bulletTexture );
        bulletSprite.setSize( 1f, 1f );
        bulletSprite.setOriginCenter();

        //Create the bullets for the pool.
        for ( int i = 0; i < MAX_BULLETS; i++ ) {
            Bullet b = new Bullet( player, new Sprite( bulletSprite ), physicsSystem, new Vector2( -1000, -1000 ), speed );
            b.start( player );
            bulletPool.add( b );
        }
    }

    /** Destroy all the bullets can the textures. */
    @Override
    public void dispose() {
        for ( TextureRegion t : bulletTextures )
            t.getTexture().dispose();

        bulletTexture.dispose();

        if ( Global.DEBUG )
            System.out.println( this.getClass().getSimpleName() + " disposed" );
    }
}
