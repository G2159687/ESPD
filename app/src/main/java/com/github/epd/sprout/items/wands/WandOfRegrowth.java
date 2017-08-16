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
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.Regrowth;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfRegrowth extends Wand {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.WAND_REGROWTH;
	}

	@Override
	protected void onZap(Ballistica bolt) {

		for (int i : bolt.subPath(1, bolt.dist)) {
			int c = Dungeon.level.map[i];
			if (c == Terrain.EMPTY || c == Terrain.EMBERS
					|| c == Terrain.EMPTY_DECO) {

				Level.set(i, Terrain.GRASS);

			}
		}

		int c = Dungeon.level.map[bolt.collisionPos];
		if (c == Terrain.EMPTY || c == Terrain.EMBERS
				|| c == Terrain.EMPTY_DECO || c == Terrain.GRASS
				|| c == Terrain.HIGH_GRASS) {

			GameScene.add(Blob.seed(bolt.collisionPos, (level() + 2) * 20, Regrowth.class));

		} else {

			GLog.i(Messages.get(WandOfPoison.class,"nothing"));

		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.foliage(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this,"desc");
	}
}
