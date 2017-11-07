
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.effects.Lightning;
import com.github.epd.sprout.effects.particles.SparkParticle;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.messages.Messages;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Shock extends Weapon.Enchantment {

	private static final String TXT_SHOCKING = Messages.get(Shock.class, "name");

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		int level = Math.max(0, weapon.level);

		if (Random.Int(level + 4) >= 3) {

			affected.clear();
			affected.add(attacker);

			arcs.clear();
			arcs.add(new Lightning.Arc(attacker.pos, defender.pos));
			hit(defender, Random.Int(1, damage / 2));

			attacker.sprite.parent.add(new Lightning(arcs, null));

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		int level = Math.max(0, weapon.level);

		if (Random.Int(level + 4) >= 3) {

			affected.clear();
			affected.add(attacker);

			arcs.clear();
			arcs.add(new Lightning.Arc(attacker.pos, defender.pos));
			hit(defender, Random.Int(1, damage / 2));

			attacker.sprite.parent.add(new Lightning(arcs, null));

			return true;

		} else {

			return false;

		}
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_SHOCKING, weaponName);
	}

	private ArrayList<Char> affected = new ArrayList<Char>();

	private ArrayList<Lightning.Arc> arcs = new ArrayList<>();

	private void hit(Char ch, int damage) {

		if (damage < 1) {
			return;
		}

		affected.add(ch);
		ch.damage(Level.water[ch.pos] && !ch.flying ? (int) (damage * 2) : damage, LightningTrap.LIGHTNING);

		ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
		ch.sprite.flash();

		HashSet<Char> ns = new HashSet<Char>();
		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char n = Actor.findChar(ch.pos + PathFinder.NEIGHBOURS8[i]);
			if (n != null && !affected.contains(n)) {
				arcs.add(new Lightning.Arc(ch.pos, n.pos));
				hit(n, Random.Int(damage / 2, damage));
			}
		}
	}
}
