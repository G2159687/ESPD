
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.BArray;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfAvalanche extends Wand {

	{
		name = Messages.get(this, "name");
		collisionProperties = Ballistica.STOP_TERRAIN;
		image = ItemSpriteSheet.WAND_AVALANCHE;
	}

	@Override
	protected void onZap(Ballistica bolt) {

		Sample.INSTANCE.play(Assets.SND_ROCKS);

		int level = level();

		int size = 1 + level / 3;
		PathFinder.buildDistanceMap(bolt.collisionPos, BArray.not(Level.solid, null), size);

		for (int i = 0; i < Dungeon.level.getLength(); i++) {

			int d = PathFinder.distance[i];

			if (d < Integer.MAX_VALUE) {

				Char ch = Actor.findChar(i);
				if (ch != null) {

					ch.sprite.flash();

					int damage = Random.Int(2, 6 + (size - d) * 2);
					if (Dungeon.hero.buff(Strength.class) != null) {
						damage *= (int) 4f;
						Buff.detach(Dungeon.hero, Strength.class);
					}
					ch.damage(damage, this);


					if (ch.isAlive() && Random.Int(1 + d) == 0) {
						Buff.prolong(ch, Paralysis.class, Random.IntRange(2, 6));
					}
				}

				CellEmitter.get(i).start(Speck.factory(Speck.ROCK), 0.07f, 3);
				Camera.main.shake(3, 0.07f * 3);
			}
		}

		if (!curUser.isAlive()) {
			Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
			GLog.n(Messages.get(this, "kill"));
		}
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		if (defender.isAlive())
			if (level > Random.IntRange(0, 50)) {
				CellEmitter.get(defender.pos).start(Speck.factory(Speck.ROCK), 0.07f, 3);
				Buff.prolong(defender, Paralysis.class, Random.IntRange(2, 6));
			}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.earth(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 2, 7 + level() / 3);
	}
}
