
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;

public class Slashing extends Weapon.Enchantment {

	private static final String TXT_SLASHING = Messages.get(Slashing.class, "name");

	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x00FF00);

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	private int[] points = new int[2];
	private int nPoints;


	public static final int[] NEIGHBOURS8 = {+1,
			-1,
			+Dungeon.level.getWidth(),
			-Dungeon.level.getWidth(),

			+1 + Dungeon.level.getWidth(),
			+1 - Dungeon.level.getWidth(),
			-1 + Dungeon.level.getWidth(),
			-1 - Dungeon.level.getWidth()};

	/*  -W-1 -W  -W+1
	 *  -1    P  +1
	 *  W-1   W  W+1
	 * 
	 */

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		int level = Math.max(0, weapon.level);

		if (defender.pos - attacker.pos == 1) {

			//		points[0] = [attacker.pos-Dungeon.level.W]

		} else if (defender.pos - attacker.pos == -1) {


		} else if (defender.pos - attacker.pos == Dungeon.level.getWidth()) {


		} else if (defender.pos - attacker.pos == -Dungeon.level.getWidth()) {


		} else if (defender.pos - attacker.pos == Dungeon.level.getWidth() + 1) {


		} else if (defender.pos - attacker.pos == Dungeon.level.getWidth() - 1) {


		} else if (defender.pos - attacker.pos == -Dungeon.level.getWidth() - 1) {


		} else if (defender.pos - attacker.pos == -Dungeon.level.getWidth() + 1) {


		} else {


		}


		int dmg = damage;
		for (int i = 1; i <= level + 1; i++) {
			dmg = Math.max(dmg, attacker.damageRoll() - i);
		}

		if (dmg > damage) {
			defender.damage(dmg - damage, this);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_SLASHING, weaponName);
	}

	@Override
	public Glowing glowing() {
		return GREEN;
	}
}
