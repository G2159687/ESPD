
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.scrolls.ScrollOfTeleportation;
import com.github.epd.sprout.messages.Messages;

public class SokobanPortalTrap {

	private static final String name = Messages.get(SokobanPortalTrap.class, "name");
	public static int portPos = 0;

	// 00x66CCEE

	public static void trigger(int pos, Char ch, int dest) {

		if (ch instanceof Hero) {
			//teleport ch to dest from pos teleport scroll
			ScrollOfTeleportation.teleportHeroLocation((Hero) ch, dest);
			//GLog.i("teleport to,  %s",dest);

		}
	}
}
