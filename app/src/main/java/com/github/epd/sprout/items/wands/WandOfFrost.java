package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Fire;
import com.github.epd.sprout.actors.blobs.Freezing;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Chill;
import com.github.epd.sprout.actors.buffs.Frost;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfFrost extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_FROST;
	}

	@Override
	protected void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null) {

			int level = level();

			int damage = Random.NormalIntRange(level, 8 + level * 4);

			if (ch.buff(Frost.class) != null) {
				return; //do nothing, can't affect a frozen target
			}
			if (ch.buff(Chill.class) != null) {
				damage = Math.round(damage * ch.buff(Chill.class).speedFactor());
			} else {
				ch.sprite.burst(0xFF99CCFF, level < 7 ? level / 2 + 2 : 5);
			}

			ch.damage(damage, this);

			if (ch.isAlive()) {
				if (Level.water[ch.pos]) {
					//20+(10*level)% chance
					if (Random.Int(10) >= 8 - level)
						Buff.affect(ch, Frost.class, Frost.duration(ch) * Random.Float(2f, 4f));
					else
						Buff.prolong(ch, Chill.class, 6 + level);
				} else {
					Buff.prolong(ch, Chill.class, 4 + level);
				}
			}
			Heap heap = Dungeon.level.heaps.get(bolt.collisionPos);
			if (heap != null) {
				heap.freeze();
			}
		} else {
			Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
			Freezing.affect(bolt.collisionPos, fire);
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.blueLight(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 5 + level(), 10 + (level() * level() / 3));
	}
}
