
package com.github.epd.sprout.actors.mobs.pets;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.SparkParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.SugarplumFairySprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class SugarplumFairy extends PET implements Callback {

	{
		name = Messages.get(SugarplumFairy.class, "name");
		spriteClass = SugarplumFairySprite.class;
		flying = true;
		state = HUNTING;
		level = 1;
		type = 11;
		cooldown = 1000;
	}

	private static final float TIME_TO_ZAP = 2f;
	private static final String TXT_LIGHTNING_KILLED = Messages.get(SugarplumFairy.class, "kill");

	@Override
	protected float attackDelay() {
		return 0.5f;
	}

	//Frames 0,2 are idle, 0,1,2 are moving, 0,3,4,1 are attack and 5,6,7 are for death


	@Override
	public int dr() {
		return level * 2;
	}

	protected int regen = 1;
	protected float regenChance = 0.2f;


	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (level) * 10;
		defenseSkill = 5 + (level * level);
	}

	@Override
	public void flee() {
		((SugarplumFairySprite) sprite).hpBar.killAndErase();
		super.flee();
	}

	@Override
	public int attackSkill(Char target) {
		return defenseSkill;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(defenseSkill / 2, defenseSkill);
	}

	@Override
	protected boolean act() {

		if (cooldown > 0) {
			cooldown = Math.max(cooldown - (level * level), 0);
			if (level < 50 && cooldown == 0) {
				GLog.p(Messages.get(SugarplumFairy.class, "ready"));
			}
		}

		if (cooldown == 0 && Dungeon.level.adjacent(pos, Dungeon.hero.pos) && Random.Int(1) == 0) {

			int bless = Random.Int(level * level);

			if (Dungeon.hero.HP < Dungeon.hero.HT) {
				Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(bless));
				Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 1);
				Dungeon.hero.HP = Math.min(Dungeon.hero.HT, Dungeon.hero.HP + bless);
			}

			if (Random.Int(20) == 0) {
				Dungeon.hero.earnExp(5);
				Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(SugarplumFairy.class, "exp"));
				cooldown = 100;
			}

			if (Random.Int(50) == 0) {
				Dungeon.hero.HP += 1;
				Dungeon.hero.HT += 1;
				Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(SugarplumFairy.class, "ht"));
				cooldown = 100;
			}
		}

		if (Random.Float() < regenChance && HP < HT) {
			HP += regen;
		}

		return super.act();
	}


	@Override
	protected boolean canAttack(Char enemy) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}


	@Override
	protected boolean doAttack(Char enemy) {

		if (Dungeon.level.adjacent(pos, enemy.pos)) {

			return super.doAttack(enemy);

		} else {

			boolean visible = Level.fieldOfView[pos]
					|| Level.fieldOfView[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			}

			spend(TIME_TO_ZAP);
			cooldown = 1000;
			if (level < 50)
				yell(Messages.get(SugarplumFairy.class, "atk"));

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
			GLog.i(Messages.get(bee.class, "shake"), name);
		}

		int curPos = pos;

		if (Level.passable[pos] && Dungeon.level.map[pos] != Terrain.CHASM) {

			moveSprite(pos, Dungeon.hero.pos);
			move(Dungeon.hero.pos);

			Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
			Dungeon.hero.move(curPos);

			Dungeon.hero.spend(1 / Dungeon.hero.speed());
			Dungeon.hero.busy();
		}

		return true;
	}

	@Override
	public String description() {
		return Messages.get(SugarplumFairy.class, "desc");
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