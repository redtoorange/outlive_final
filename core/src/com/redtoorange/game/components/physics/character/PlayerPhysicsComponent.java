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

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.Player;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * PlayerPhysicsComponent.java - A physics component that is specialized to be attached to a player.  This simplifies
 * collision detection between different physics bodies.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class PlayerPhysicsComponent extends CharacterPhysicsComponent {
    /**
     * @param physicsSystem The world's physicsSystem.
     * @param player        The player that this component is attached to.
     */
    public PlayerPhysicsComponent( PhysicsSystem physicsSystem, Player player ) {
        super( physicsSystem, player, 5f, 10f, 10f,
                5f );
    }

    /** @param owner The owning GameObject. */
    @Override
    public void start( GameObject owner ) {
        super.start( owner );

        Filter f = body.getFixtureList().first().getFilterData();
        f.categoryBits = Global.PLAYER;
        f.maskBits = Global.WALL | Global.ENEMY | Global.AMMO;
        body.getFixtureList().first().setFilterData( f );
    }

    /** @return The player's body. */
    public Body getBody() {
        return body;
    }
}