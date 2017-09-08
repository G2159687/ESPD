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
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.mobs.Bee;
import com.github.epd.sprout.effects.particles.SparkParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.GreenDragonSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class GreenDragon extends PET implements Callback {

    {
        name = Messages.get(GreenDragon.class, "name");
        spriteClass = GreenDragonSprite.class;
        flying = true;
        state = HUNTING;
        level = 1;
        type = 5;
        cooldown = 1000;
    }

    private static final float TIME_TO_ZAP = 2f;
    private static final String TXT_LIGHTNING_KILLED = Messages.get(GreenDragon.class, "kill");

    @Override
    protected float attackDelay() {
        return 0.8f;
    }


    @Override
    public int dr() {
        return level * 3;
    }

    //Frames 1-4 are idle, 5-8 are moving, 9-12 are attack and the last are for death

    //flame on!
    //spits fire
    //feed meat

    protected int regen = 1;
    protected float regenChance = 0.2f;


    @Override
    public void adjustStats(int level) {
        this.level = level;
        HT = (3 + level) * 8;
        defenseSkill = 5 + (level * level) / 2;
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
                Messages.get(GreenDragon.class, "ready");
            }
        }

        if (Random.Float() < regenChance && HP < HT) {
            HP += regen;
        }

        return super.act();
    }


    @Override
    protected boolean canAttack(Char enemy) {
        if (cooldown > 0) {
            return Level.adjacent(pos, enemy.pos);
        } else {
            return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
        }
    }


    @Override
    protected boolean doAttack(Char enemy) {

        if (Level.adjacent(pos, enemy.pos)) {

            return super.doAttack(enemy);

        } else {

            boolean visible = Level.fieldOfView[pos]
                    || Level.fieldOfView[enemy.pos];
            if (visible) {
                sprite.zap(enemy.pos);
            }

            spend(TIME_TO_ZAP);
            cooldown = 1000;
            yell(Messages.get(GreenDragon.class, "atk"));

            if (hit(this, enemy, true)) {
                int dmg = damageRoll() * 2;
                if (Level.water[enemy.pos] && !enemy.flying) {
                    dmg *= 1.5f;
                }
                enemy.damage(dmg, LightningTrap.LIGHTNING);

                enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                enemy.sprite.flash();

                if (enemy == Dungeon.hero) {

                    Camera.main.shake(2, 0.3f);

                    if (!enemy.isAlive()) {
                        Dungeon.fail(Utils.format(ResultDescriptions.MOB,
                                Utils.indefinite(name)));
                        GLog.n(TXT_LIGHTNING_KILLED, name);
                    }
                }
            } else {
                enemy.sprite
                        .showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
            }

            return !visible;
        }
    }


    @Override
    public void call() {
        next();
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
            GLog.i(Messages.get(Bee.class, "shake"), name);
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


    @Override
    public String description() {
        return Messages.get(GreenDragon.class, "desc");
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

    static {
        RESISTANCES.add(LightningTrap.Electricity.class);
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

}