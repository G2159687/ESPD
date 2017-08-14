/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.SokobanBlackSheepSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class SheepSokobanBlack extends NPC {

    private static final String[] QUOTES = {Messages.get(SheepSokoban.class, "one"), Messages.get(SheepSokoban.class, "two"), Messages.get(SheepSokoban.class, "three"),
            Messages.get(SheepSokoban.class, "four")};

    {
        name = Messages.get(SheepSokobanBlack.class, "name");
        spriteClass = SokobanBlackSheepSprite.class;
    }


    @Override
    protected boolean act() {
        throwItem();
        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public void add(Buff buff) {
    }

    @Override
    public String description() {
        return Messages.get(SheepSokobanBlack.class, "desc");
    }

    @Override
    public boolean interact() {
        int traps = Dungeon.level.countFleeceTraps(pos, 5);
        int newPos = -1;
        int curPos = pos;
        int count = 100;
        int dist = 6;
        boolean moved = false;

        if (traps > 0) {

            do {
                newPos = Dungeon.level.randomRespawnCellSheep(pos, 5);
                dist = Level.distance(newPos, pos);
                if (count-- <= 0) {
                    break;
                }
            } while (newPos == -1);

        }

        if (newPos == -1) {


            GLog.n(Messages.get(SheepSokoban.class, "sheepname") + Random.element(QUOTES));
            //yell("pos = " + dist);
            destroy();
            sprite.killAndErase();
            sprite.emitter().burst(ShadowParticle.UP, 5);
            moved = true;

        } else {
            yell(Messages.get(SheepSokoban.class, "five"));
            //yell("pos = " + dist);
            Actor.freeCell(pos);
            CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            pos = newPos;
            move(pos);
            moved = true;
        }

        if (moved) {
            Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
            Dungeon.hero.move(curPos);
        }

        Dungeon.hero.spend(1 / Dungeon.hero.speed());
        Dungeon.hero.busy();

        return true;

    }

}
