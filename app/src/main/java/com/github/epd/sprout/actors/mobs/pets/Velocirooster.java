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
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.VelociroosterSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class Velocirooster extends PET {

    {
        name = Messages.get(this, "name");
        spriteClass = VelociroosterSprite.class;
        flying = false;
        state = HUNTING;
        level = 1;
        type = 3;
        cooldown = 1000;

    }

    protected int regen = 1;
    protected float regenChance = 0.05f;


    @Override
    public void adjustStats(int level) {
        this.level = level;
        HT = (2 + level) * 5;
        defenseSkill = 1 + level;
    }


    @Override
    public int dr() {
        return level * 3;
    }

    @Override
    public int attackSkill(Char target) {
        return defenseSkill;
    }

    @Override
    public int damageRoll() {

        int dmg = 0;
        if (cooldown == 0) {
            dmg = Random.NormalIntRange(HT / 2, HT);
            GLog.p(Messages.get(this, "ready"));
            cooldown = 1000;
        } else {
            dmg = Random.NormalIntRange(HT / 5, HT / 2);
        }
        return dmg;

    }

    @Override
    protected boolean act() {

        if (cooldown > 0) {
            cooldown = Math.max(cooldown - (level * level), 0);
            if (cooldown == 0) {
                yell(Messages.get(this, "atk"));
            }
        }

        if (Random.Float() < regenChance && HP < HT) {
            HP += regen;
        }

        return super.act();
    }


    @Override
    public String description() {
        return Messages.get(this, "desc");
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


}