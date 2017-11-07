
package com.github.epd.sprout.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Random;

public class SmokeParticle extends PixelParticle {

	public static final Factory FACTORY = new Factory() {
		@Override
		public void emit(Emitter emitter, int index, float x, float y) {
			((SmokeParticle) emitter.recycle(SmokeParticle.class)).reset(x, y);
		}
	};

	public SmokeParticle() {
		super();

		color(0x222222);

		acc.set(0, -40);
	}

	public void reset(float x, float y) {
		revive();

		this.x = x;
		this.y = y;

		left = lifespan = Random.Float(0.6f, 1f);
		speed.set(Random.Float(-4, +4), Random.Float(-8, +8));
	}

	@Override
	public void update() {
		super.update();

		float p = left / lifespan;
		am = p > 0.8f ? 2 - 2 * p : p * 0.5f;
		size(16 - p * 8);
	}
}