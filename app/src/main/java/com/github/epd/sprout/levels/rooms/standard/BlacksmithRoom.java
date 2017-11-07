
package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.mobs.npcs.Blacksmith;
import com.github.epd.sprout.actors.mobs.npcs.Blacksmith2;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Random;

public class BlacksmithRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 6);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 6);
	}

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FIRE_TRAP);
		Painter.fill(level, this, 2, Terrain.EMPTY_SP);


		for (int i = 0; i < 2; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY_SP);
			level.drop(Generator.random(Random.oneOf(Generator.Category.ARMOR,
					Generator.Category.WEAPON)), pos);
		}


		for (Door door : connected.values()) {
			door.set(Door.Type.UNLOCKED);
			Painter.drawInside(level, this, door, 1, Terrain.EMPTY);
		}

		Blacksmith npc = new Blacksmith();
		do {
			npc.pos = level.pointToCell(random(2));
		} while (level.heaps.get(npc.pos) != null);
		level.mobs.add(npc);


		Blacksmith2 npc2 = new Blacksmith2();
		do {
			npc2.pos = level.pointToCell(random(2));
		} while (level.heaps.get(npc2.pos) != null || Actor.findChar(npc2.pos) != null);
		level.mobs.add(npc2);

	}
}
