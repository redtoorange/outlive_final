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

import com.badlogic.gdx.physics.box2d.Filter;
import com.redtoorange.game.gameobject.GameObject;
import com.redtoorange.game.gameobject.characters.enemies.Enemy;
import com.redtoorange.game.systems.Global;
import com.redtoorange.game.systems.PhysicsSystem;

/**
 * EnemyPhysicsComponent.java - A specialized physics component for enemies.  This handles most of the same work as the
 * other physics components, but it has special filters to allow collision with bullets and the player.
 *
 * @author Andrew McGuiness
 * @version 20/Apr/2017
 */
public class EnemyPhysicsComponent extends CharacterPhysicsComponent {
    /**
     * @param physicsSystem The world's physics system.
     * @param enemy         The gameObject this is attached to.
     */
    public EnemyPhysicsComponent( PhysicsSystem physicsSystem, Enemy enemy ) {
        super( physicsSystem, enemy, 1f, 10f, 10f, 5f );
    }

    /** @param owner The owning gameObject. */
    @Override
    public void start( GameObject owner ) {
        super.start( owner );

        Filter f = body.getFixtureList().first().getFilterData();
        f.categoryBits = Global.ENEMY;
        f.maskBits = Global.WALL | Global.PLAYER | Global.BULLET_LIVE | Global.LIGHT | Global.ENEMY;
        body.getFixtureList().first().setFilterData( f );
    }
}
