/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokoban;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanCorner;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanStop;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanSwitch;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;

public class ChangeSheepTrap {

	private static final String name = Messages.get(ChangeSheepTrap.class, "name");

	private static final float SPAWN_DELAY = 0.2f;

	// 00x66CCEE

	public static void trigger(int pos, Char ch) {

		if (ch instanceof SheepSokoban) {
			ch.destroy();
			ch.sprite.killAndErase();
			CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
			SheepSokobanCorner s = new SheepSokobanCorner();
			s.pos = pos;
			GameScene.add(s, SPAWN_DELAY);
		} else if (ch instanceof SheepSokobanCorner) {
			ch.destroy();
			ch.sprite.killAndErase();
			CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
			SheepSokobanStop s = new SheepSokobanStop();
			s.pos = pos;
			GameScene.add(s, SPAWN_DELAY);

		} else if (ch instanceof SheepSokobanSwitch) {
			ch.destroy();
			ch.sprite.killAndErase();
			CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
			SheepSokoban s = new SheepSokoban();
			s.pos = pos;
			GameScene.add(s, SPAWN_DELAY);
		}
	}
}
