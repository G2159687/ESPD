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
package com.github.epd.sprout.actors.mobs.pets;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.Web;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.SpiderSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Spider extends PET {

    {
        name = Messages.get(Spider.class, "name");
        spriteClass = SpiderSprite.class;
        flying = false;
        state = HUNTING;
        level = 1;
        type = 1;
        cooldown = 1000;

    }

    protected int regen = 1;
    protected float regenChance = 0.1f;


    @Override
    public void adjustStats(int level) {
        this.level = level;
        HT = (2 + level) * 5;
        defenseSkill = 1 + level;
    }


    @Override
    public int attackSkill(Char target) {
        return defenseSkill;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(HT / 5, HT / 2);
    }

    @Override
    protected boolean act() {

        if (cooldown > 0) {
            cooldown = Math.max(cooldown - (level * level), 0);
            if (cooldown == 0) {
                GLog.p(Messages.get(Spider.class, "ready"));
            }
        }

        if (Random.Float() < regenChance && HP < HT) {
            HP += regen;
        }

        return super.act();
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (cooldown > 0 && Random.Int(10) == 0) {
            Buff.affect(enemy, Poison.class).set(Random.Int(7, 9) * Poison.durationFactor(enemy));
            GameScene.add(Blob.seed(enemy.pos, Random.Int(5, 7), Web.class));
        }
        if (cooldown == 0) {
            Buff.affect(enemy, Poison.class).set(Random.Int(10, 25) * Poison.durationFactor(enemy));
            GameScene.add(Blob.seed(enemy.pos, Random.Int(8, 9), Web.class));
            damage += damage;
            cooldown = 1000;
        }

        return damage;
    }

    @Override
    public String description() {
        return Messages.get(Spider.class, "desc");
    }


    @Override
    public boolean interact() {

        if (this.buff(MagicalSleep.class) != null) {
            Buff.detach(this, MagicalSleep.class);
        }

        if (state == SLEEPING) {
            state = HUNTING;
        }
        if (buff(Paralysis.class) != null) {
            Buff.detach(this, Paralysis.class);
            GLog.i(Messages.get(bee.class, "shake"), name);
        }

        int curPos = pos;

        moveSprite(pos, Dungeon.hero.pos);
        move(Dungeon.hero.pos);

        Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
        Dungeon.hero.move(curPos);

        Dungeon.hero.spend(1 / Dungeon.hero.speed());
        Dungeon.hero.busy();

        return true;
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

    static {
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

}