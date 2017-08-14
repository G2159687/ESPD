/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.github.epd.sprout.levels.features;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.levels.DeadEndLevel;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndMessage;
import com.watabou.noosa.audio.Sample;

public class Sign {

	private static final String TXT_DEAD_END = Messages.get(Sign.class,"dead_end");

	private static final String[] TIPS = {
			Messages.get(Sign.class,"tip_1"),
			Messages.get(Sign.class,"tip_2"),
			Messages.get(Sign.class,"tip_3"),
			Messages.get(Sign.class,"tip_4"),

			Messages.get(Sign.class,"tip_5"),

			Messages.get(Sign.class,"tip_6"),
			Messages.get(Sign.class,"tip_7"),
			Messages.get(Sign.class,"tip_8"),
			Messages.get(Sign.class,"tip_9"),

			Messages.get(Sign.class,"tip_10"),

			Messages.get(Sign.class,"tip_11"),
			Messages.get(Sign.class,"tip_12"),
			Messages.get(Sign.class,"tip_13"),
			Messages.get(Sign.class,"tip_14"),

			Messages.get(Sign.class,"tip_15"),

			Messages.get(Sign.class,"tip_16"),
			Messages.get(Sign.class,"tip_17"),
			Messages.get(Sign.class,"tip_18"),
			Messages.get(Sign.class,"tip_19"),

			Messages.get(Sign.class,"tip_20"),

			Messages.get(Sign.class,"tip_21"),

			// hmm.. I wonder what this is?
			"standOfF roW", "fraCtion doWnpOur", "gaffe MaSts" };
	
	
	private static final String PIT = Messages.get(Sign.class,"pit");
	//private static final String BOOKLVL = "Note to self: Always leave a teleport scroll in the vault.";


	private static final String TXT_BURN = Messages.get(Sign.class,"burn");

	public static void read(int pos) {

		if (Dungeon.level instanceof DeadEndLevel) {

			GameScene.show(new WndMessage(TXT_DEAD_END));

		} else {

			int index = Dungeon.depth - 1;

			if (index < TIPS.length) {
				GameScene.show(new WndMessage(TIPS[index]));

				if (index >= 21) {

					Level.set(pos, Terrain.EMBERS);
					GameScene.updateMap(pos);
					GameScene.discoverTile(pos, Terrain.SIGN);

					GLog.w(TXT_BURN);

					CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
					Sample.INSTANCE.play(Assets.SND_BURNING);
				}

			}
		}
	}
	
	public static void readPit(int pos) {
				GameScene.show(new WndMessage(PIT));			
			}
			
	
}
