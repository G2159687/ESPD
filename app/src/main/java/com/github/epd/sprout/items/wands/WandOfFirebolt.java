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
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.Fire;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfFirebolt extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_FIREBOLT;
	}

	@Override
	protected void onZap(Ballistica bolt) {

		int level = level();

		for (int c : bolt.subPath(0, bolt.dist)) {
			if (Level.flamable[c]) {
				GameScene.add(Blob.seed(c, 1, Fire.class));
			}
		}

		int cell = bolt.collisionPos;

		GameScene.add(Blob.seed(cell, 1, Fire.class));

		Char ch = Actor.findChar(cell);
		if (ch != null) {

			int damage = Random.Int(1, 8 + level * level);
			if (Dungeon.hero.buff(Strength.class) != null) {
				damage *= (int) 4f;
				Buff.detach(Dungeon.hero, Strength.class);
			}
			ch.damage(damage, this);

			Buff.affect(ch, Burning.class).reignite(ch);

			ch.sprite.emitter().burst(FlameParticle.FACTORY, 5);

			if (ch == curUser && !ch.isAlive()) {
				Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
				GLog.n(Messages.get(this, "kill"));
			}
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.fire(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 1, 8 + level * level);
	}
}
