
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.Lightning;
import com.github.epd.sprout.effects.particles.SparkParticle;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.messages.Messages;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class NeptuneShock extends Weapon.Enchantment {

	private static final String TXT_SHOCKING = Messages.get(NeptuneShock.class, "name");
	private int cost = 10;

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {

		return false;
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%


		if (weapon.charge >= cost) {
			weapon.charge -= cost;
		} else {
			return false;
		}

		int level = Math.max(0, weapon.level);
		boolean procced = false;
		int distance = 1 + level * 2;

		for (Mob mob : Dungeon.level.mobs) {

			boolean visible = Level.fieldOfView[mob.pos];

			if (Dungeon.level.distance(attacker.pos, mob.pos) < distance && mob.isAlive() && !mob.isPassive() && Random.Int(10) < 5) {

				// int dmg = 20;
				attacker.sprite.parent.add(new Lightning(mob.pos, attacker.pos, null));

				mob.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				mob.sprite.flash();

				if (Level.water[mob.pos] && !mob.flying) {
					damage *= 2;
				}

				if (mob.isAlive()) {
					if (damage < mob.HP) {
						mob.damage(damage, weapon);
					} else {
						mob.damage(mob.HP - 2, weapon);
						Buff.prolong(mob, com.github.epd.sprout.actors.buffs.Paralysis.class, Random.Float(1, 1.5f + level));
					}
				}

				Camera.main.shake(2, 0.3f);
				procced = true;
			}
		}


		return procced;
	}

	private void hit(Mob ch, int mobdamage) {
		if (mobdamage < 1 || !ch.isAlive()) {
			return;
		}
		ch.damage(Level.water[ch.pos] && !ch.flying ? mobdamage * 2 : mobdamage, LightningTrap.LIGHTNING);
	}


	@Override
	public String name(String weaponName) {
		return String.format(TXT_SHOCKING, weaponName);
	}


}
