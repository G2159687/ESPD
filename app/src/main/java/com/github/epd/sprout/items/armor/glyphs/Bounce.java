
package com.github.epd.sprout.items.armor.glyphs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.Pushing;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.Armor.Glyph;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Bounce extends Glyph {

	private static final String TXT_BOUNCE = Messages.get(Bounce.class, "name");

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.level);

		if (Dungeon.level.adjacent(attacker.pos, defender.pos)
				&& !defender.properties().contains(Char.Property.IMMOVABLE)
				&& Random.Int(level + 5) >= 4) {

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int ofs = PathFinder.NEIGHBOURS8[i];
				if (attacker.pos - defender.pos == ofs) {
					int newPos = attacker.pos + ofs;
					if ((Level.passable[newPos] || Level.avoid[newPos])
							&& Actor.findChar(newPos) == null) {

						Actor.addDelayed(new Pushing(attacker, attacker.pos,
								newPos), -1);

						attacker.pos = newPos;

						if (attacker instanceof Mob) {
							Dungeon.level.mobPress((Mob) attacker);
						} else {
							Dungeon.level.press(newPos, attacker);
						}

					}
					break;
				}
			}

		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_BOUNCE, weaponName);
	}

}
