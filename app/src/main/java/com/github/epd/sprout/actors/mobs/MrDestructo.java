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
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.PurpleParticle;
import com.github.epd.sprout.items.InactiveMrDestructo;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.items.weapon.enchantments.Leech;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.MrDestructoSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.utils.Random;

import java.util.HashSet;

public class MrDestructo extends Mob {

	private static final String TXT_DEATHGAZE_KILLED = Messages.get(MrDestructo.class,"kill");

	{
		name = Messages.get(this,"name");
		spriteClass = MrDestructoSprite.class;
		hostile = false;
		state = HUNTING;
		HP = HT= 100;
		defenseSkill = 3;	
	}

	
	private static final float SPAWN_DELAY = 0.1f;
	
	@Override
	public int dr() {
		return 5;
	}

	
	@Override
	protected boolean act() {
		boolean result = super.act();
		return result;
	}
	
	@Override
	public void move(int step) {		
	}
		
	@Override
	protected Char chooseEnemy() {

		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.hostile && Level.fieldOfView[mob.pos]) {
					enemies.add(mob);
				}
			}

			enemy = enemies.size() > 0 ? Random.element(enemies) : null;
		}

		return enemy;
	}

	
	
    public static MrDestructo spawnAt(int pos) {
		
    	MrDestructo b = new MrDestructo();  
    	
			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;
     
     }

	private Ballistica beam;

	@Override
	protected boolean canAttack( Char enemy ) {

		beam = new Ballistica( pos, enemy.pos, Ballistica.STOP_TERRAIN);

		return beam.subPath(1, beam.dist).contains(enemy.pos);
	}

	@Override
	public int attackSkill(Char target) {
		return 20+(Dungeon.depth);
	}

	@Override
	protected float attackDelay() {
		return 0.5f;
	}
	
	@Override
	protected boolean doAttack(Char enemy) {

		spend(attackDelay());

		boolean rayVisible = false;

		for (int i : beam.subPath(0, beam.dist)) {
			if (Dungeon.visible[i]) {
				rayVisible = true;
			}
		}

		if (rayVisible) {
			sprite.attack(beam.collisionPos);
			return false;
		} else {
			attack(enemy);
			return true;
		}
	}

	@Override
	public boolean attack(Char enemy) {

		for (int pos : beam.subPath(1, beam.dist)) {

			Char ch = Actor.findChar(pos);
			if (ch == null) {
				continue;
			}

			if (hit(this, ch, true)) {
				ch.damage(Random.NormalIntRange(Dungeon.depth, Dungeon.depth+12), this);
				yell(Messages.get(this,"atk"));
				damage(Random.NormalIntRange(5, 10), this);

				if (Dungeon.visible[pos]) {
					ch.sprite.flash();
					CellEmitter.center(pos).burst(PurpleParticle.BURST,
							Random.IntRange(1, 2));
				}

				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail(Utils.format(ResultDescriptions.MOB,
							Utils.indefinite(name)));
					GLog.n(TXT_DEATHGAZE_KILLED, name);
				}
			} else {
				ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
			}
		}

		return true;
	}

	@Override
	public void beckon(int cell) {
	}
	
	@Override
	public String description() {
		return Messages.get(this,"desc");
	}

	
	@Override
	public void die(Object cause) {

		yell(Messages.get(this,"die"));
		Dungeon.level.drop(new InactiveMrDestructo(), pos);
		super.die(cause);

		
	
	}
			
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(Death.class);
		RESISTANCES.add(Leech.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(ToxicGas.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
