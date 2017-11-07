
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ConfusionGas;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.particles.SparkParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.RedDewdrop;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.LitTowerSprite;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class LitTower extends Mob implements Callback {

	private static final float TIME_TO_ZAP = 2f;

	private static final String TXT_LIGHTNING_KILLED = Messages.get(LitTower.class, "kill");

	{
		name = Messages.get(this, "name");
		spriteClass = LitTowerSprite.class;

		HP = HT = 600;
		defenseSkill = 1000;

		EXP = 25;

		hostile = false;
		state = PASSIVE;

		loot = new RedDewdrop();
		lootChance = 1f;

		properties.add(Property.IMMOVABLE);
	}

	@Override
	public void beckon(int cell) {
		// Do nothing
	}

	@Override
	public int damageRoll() {
		return 0;
	}


	@Override
	public int attackSkill(Char target) {
		return 100;
	}

	@Override
	public int dr() {
		return 1000;
	}


	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	protected boolean act() {
		if (Dungeon.level.distance(pos, Dungeon.hero.pos) < 5 && Dungeon.hero.isAlive() && checkOtiluke()) {
			zapAll(Dungeon.hero.pos);
		}
		throwItem();
		return super.act();
	}

	protected void throwItem() {
		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			int n;
			do {
				n = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Level.passable[n] && !Level.avoid[n]);
			Dungeon.level.drop(heap.pickUp(), n).sprite.drop(pos);
		}
	}

	@Override
	public void call() {
		next();
	}

	protected boolean checkOtiluke() {
		boolean check = false;

		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof Otiluke) {
				check = true;
			}
		}
		return check;
	}


	protected boolean heroNear() {
		boolean check = false;
		for (int i : PathFinder.NEIGHBOURS9DIST2) {
			int cell = pos + i;
			if (Actor.findChar(cell) != null
					&& (Actor.findChar(cell) instanceof Hero)
					) {
				check = true;
			}
		}
		return check;
	}


	@Override
	protected boolean canAttack(Char enemy) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {
		return false;
	}


	public synchronized void zapAll(int loc) {

		yell(Messages.get(this, "zap"));

		Char hero = Dungeon.hero;

		int mobDmg = Random.Int(300, 600);


		boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[loc];


		if (visible) {
			sprite.zap(loc);
		}


		hero.damage(mobDmg, LightningTrap.LIGHTNING);

		hero.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
		hero.sprite.flash();

		Camera.main.shake(2, 0.3f);
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	@Override
	public void add(Buff buff) {
	}


	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(Death.class);
		RESISTANCES.add(ScrollOfPsionicBlast.class);
		RESISTANCES.add(LightningTrap.Electricity.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(ConfusionGas.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}


}
