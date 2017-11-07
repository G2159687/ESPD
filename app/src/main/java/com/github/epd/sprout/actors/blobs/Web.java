
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.particles.WebParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;

public class Web extends Blob {

	@Override
	protected void evolve() {

		int cell;

		for (int i = area.left; i < area.right; i++) {
			for (int j = area.top; j < area.bottom; j++) {
				cell = i + j * Dungeon.level.getWidth();
				off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

				if (off[cell] > 0) {

					volume += off[cell];

					Char ch = Actor.findChar(cell);
					if (ch != null) {
						Buff.prolong(ch, Roots.class, TICK);
					}
				}
			}
		}
	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);

		emitter.pour(WebParticle.FACTORY, 0.4f);
	}

	public void seed(Level level, int cell, int amount ) {
		if (cur == null) cur = new int[level.getLength()];
		if (off == null) off = new int[cur.length];
		int diff = amount - cur[cell];
		if (diff > 0) {
			cur[cell] = amount;
			volume += diff;
		}
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
