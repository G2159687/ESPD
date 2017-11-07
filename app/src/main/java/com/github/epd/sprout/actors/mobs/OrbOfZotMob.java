
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.PurpleParticle;
import com.github.epd.sprout.items.bombs.OrbOfZot;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.items.weapon.enchantments.Leech;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.OrbOfZotSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.utils.Random;

import java.util.HashSet;

public class OrbOfZotMob extends Mob {

	private static final String TXT_DEATHGAZE_KILLED = Messages.get(OrbOfZotMob.class, "kill");

	{
		name = Messages.get(this, "name");
		spriteClass = OrbOfZotSprite.class;
		hostile = false;
		state = HUNTING;
		HP = HT = 500;
		defenseSkill = 35;

		properties.add(Property.IMMOVABLE);
	}


	private static final float SPAWN_DELAY = 0.1f;


	@Override
	public int dr() {
		return 55;
	}


	@Override
	protected boolean act() {
		return super.act();
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


	public static OrbOfZotMob spawnAt(int pos) {

		OrbOfZotMob b = new OrbOfZotMob();

		b.pos = pos;
		b.state = b.HUNTING;
		GameScene.add(b, SPAWN_DELAY);

		return b;

	}

	private Ballistica beam;

	@Override
	protected boolean canAttack(Char enemy) {

		beam = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN);

		return beam.subPath(1, beam.dist).contains(enemy.pos);
	}

	@Override
	public int attackSkill(Char target) {
		return 70 + (Dungeon.depth);
	}

	@Override
	protected float attackDelay() {
		return 0.25f;
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
				ch.damage(Random.NormalIntRange(100, 300), this);
				yell(Messages.get(this, "atk"));
				damage(Random.NormalIntRange(10, 20), this);
				//Dungeon.hero.earnExp(5);
				//Dungeon.zotDrains++;
				//if(Dungeon.zotDrains>=100){
				//	Dungeon.hero.STR++;
				//	Dungeon.hero.HT += 5;
				//	Dungeon.hero.HP += 5;
				//	Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, "+1 str, +5 ht");
				//	Dungeon.zotDrains=0;
				//}

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
		return Messages.get(this, "desc");
	}


	@Override
	public void die(Object cause) {

		yell(Messages.get(this, "die"));
		Dungeon.level.drop(new OrbOfZot(), pos);
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
