
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokoban;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanCorner;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanSwitch;
import com.github.epd.sprout.messages.Messages;

public class HeapGenTrap {

	private static final String name = Messages.get(HeapGenTrap.class, "name");
	public static boolean gen = false;

	// 00x66CCEE

	public static void trigger(int pos, Char ch) {

		if (ch instanceof SheepSokoban || ch instanceof SheepSokobanCorner || ch instanceof SheepSokobanSwitch) {
			gen = true;
		}
	}
}
