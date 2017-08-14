package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.GooSprite;

public class GooWarn extends Blob {

	{
		actPriority = 3;
	}

	// cosmetic blob, used to warn noobs that goo's pump up should, infact, be
	// avoided.

	// Thanks to Watabou for the much better particle effect, I was lazy and
	// just re-colored flames initially

	protected int pos;

	@Override
	protected void evolve() {

		int cell;

		for (int i = area.left; i < area.right; i++){
			for (int j = area.top; j < area.bottom; j++){
				cell = i + j*WIDTH;
				off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

				if (off[cell] > 0) {
					volume += off[cell];
				}
			}
		}

	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.pour(GooSprite.GooParticle.FACTORY, 0.03f);
	}

	@Override
	public String tileDesc() {
		return Messages.get(this,"desc");
	}
}
