
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.particles.ShaftParticle;
import com.github.epd.sprout.levels.Level;
import com.watabou.utils.Bundle;

public class Portal extends Blob {

	protected int pos;

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		for (int i = 0; i < cur.length; i++) {
			if (cur[i] > 0) {
				pos = i;
				break;
			}
		}
	}

	@Override
	protected void evolve() {
		volume = off[pos] = cur[pos];
	}

	@Override
	public void seed(Level level, int cell, int amount) {
		cur[pos] = 0;
		pos = cell;
		volume = cur[pos] = amount;
	}


	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.start(ShaftParticle.FACTORY, 0.9f, 0);
	}
}
