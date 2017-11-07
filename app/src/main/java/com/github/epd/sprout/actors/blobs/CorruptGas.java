
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class CorruptGas extends Blob implements Hero.Doom {

	@Override
	protected void evolve() {
		super.evolve();

		int levelDamage = 5 + Dungeon.depth * 5;
		int bleedDamage = 5 + Dungeon.depth * 2;

		Char ch;
		int cell;

		for (int i = area.left; i < area.right; i++) {
			for (int j = area.top; j < area.bottom; j++) {
				cell = i + j * Dungeon.level.getWidth();
				if (cur[cell] > 0 && (ch = Actor.findChar(cell)) != null) {

					if (!ch.immunities().contains(ConfusionGas.class)) {
						Buff.prolong(ch, Vertigo.class, 2);
					}

					if (!ch.immunities().contains(this.getClass())) {
						Buff.affect(ch, Bleeding.class).set(bleedDamage);
						Buff.prolong(ch, Cripple.class, Cripple.DURATION);

						int damage = (ch.HT + levelDamage) / 40;
						if (Random.Int(40) < (ch.HT + levelDamage) % 40) {
							damage++;
						}

						ch.damage(damage, this);
					}
				}
			}
		}
	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);

		emitter.pour(Speck.factory(Speck.CORRUPT), 0.4f);
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

	@Override
	public void onDeath() {
		Dungeon.fail(ResultDescriptions.GAS);
		GLog.n(Messages.get(this, "die"));
	}
}
