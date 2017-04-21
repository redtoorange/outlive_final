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

package com.redtoorange.game.components.physics.character;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.redtoorange.game.components.input.InputComponent;
import com.redtoorange.game.components.physics.PhysicsComponent;
import com.redtoorange.game.components.rendering.sprite.SpriteComponent;
import com.redtoorange.game.factories.Box2DFactory;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.GameObjectCharacter;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * CharacterPhysicsComponent.java - A PhysicsComponent meant to be applied to characters and subClassed to specialize
 * the usage of the component.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public abstract class CharacterPhysicsComponent extends PhysicsComponent {
    /** The amount to kill speed each frame. */
    protected float linearDampening;
    /** The amount to kill rotation each frame. */
    protected float angularDampening;
    /** The density of the body (in square meters). */
    protected float density;
    /** The character GameObject this component is attached to. */
    protected GameObjectCharacter entityCharacter;
    /** The GameObject's inputComponent is one exists. */
    protected InputComponent inputComponent;


    /**
     * @param physicsSystem    The world's physicsSystem.
     * @param entityCharacter  The character GameObject this component is attached to.
     * @param speed            The speed that the body should have.
     * @param linearDampening  The amount to kill speed each frame.
     * @param angularDampening The amount to kill rotation each frame.
     * @param density          The density of the body (in square meters).
     */
    public CharacterPhysicsComponent( PhysicsSystem physicsSystem, GameObjectCharacter entityCharacter,
                                      float speed, float linearDampening, float angularDampening,
                                      float density ) {
        super( physicsSystem );

        this.speed = speed;
        this.linearDampening = linearDampening;
        this.angularDampening = angularDampening;
        this.density = density;
        this.entityCharacter = entityCharacter;
    }

    /** @param owner The GameObject this component is attached to. */
    @Override
    public void start( GameObject owner ) {
        super.start( owner );

        initPhysics( physicsSystem );
    }

    /**
     * if there is an input component, use it to get delta input.  Update the Transform of the GameObject based on this
     * body.
     *
     * @param deltaTime The time since the last update.
     */
    @Override
    public void update( float deltaTime ) {
        if ( inputComponent == null )
            inputComponent = entityCharacter.getComponent( InputComponent.class );

        if ( Math.abs( entityCharacter.getRotation() - Math.toDegrees( body.getAngle() ) ) > 0.01f )
            body.setTransform( body.getPosition(), ( float ) Math.toRadians( entityCharacter.getRotation() ) );

        body.applyLinearImpulse( inputComponent.getDeltaInput().nor().scl( speed ), body.getWorldCenter(), true );
        entityCharacter.getTransform().setPosition( body.getPosition() );
    }

    /**
     * Initialize the physics body for this component.  Attaches to a SpriteComponent is one can be found.
     *
     * @param physicsSystem The world's physicsSystem.
     */
    protected void initPhysics( PhysicsSystem physicsSystem ) {
        SpriteComponent sc = entityCharacter.getComponent( SpriteComponent.class );

        body = Box2DFactory.createBody( physicsSystem, sc.getBoundingBox(), BodyDef.BodyType.DynamicBody,
                density, 0f, 0f, false, false );

        body.setUserData( entityCharacter );

        body.setFixedRotation( true );
        body.setLinearDamping( linearDampening );
        body.setAngularDamping( angularDampening );
        body.setSleepingAllowed( false );
    }
}