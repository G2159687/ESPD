
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class PoisonGooSprite extends MobSprite {

	private Animation pump;
	private Animation pumpAttack;

	public PoisonGooSprite() {
		super();

		texture(Assets.POISONGOO);

		TextureFilm frames = new TextureFilm(texture, 20, 14);

		idle = new Animation(10, true);
		idle.frames(frames, 2, 1, 0, 0, 1);

		run = new Animation(15, true);
		run.frames(frames, 3, 2, 1, 2);

		pump = new Animation(20, true);
		pump.frames(frames, 4, 3, 2, 1, 0);

		pumpAttack = new Animation(20, false);
		pumpAttack.frames(frames, 4, 3, 2, 1, 0, 7);

		attack = new Animation(10, false);
		attack.frames(frames, 8, 9, 10);

		die = new Animation(10, false);
		die.frames(frames, 5, 6, 7);

		play(idle);
	}

	public void pumpUp() {
		play(pump);
	}

	public void pumpAttack() {
		play(pumpAttack);
	}

	@Override
	public void play(Animation anim, boolean force) {
		super.play(anim, force);
	}

	@Override
	public int blood() {
		return 0xFF000000;
	}

	public static class GooParticle extends PixelParticle.Shrinking {

		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
			public void emit(Emitter emitter, int index, float x, float y) {
				((GooParticle) emitter.recycle(GooParticle.class)).reset(x, y);
			}
		};

		public GooParticle() {
			super();

			color(0x000000);
			lifespan = 0.3f;

			acc.set(0, +50);
		}

		public void reset(float x, float y) {
			revive();

			this.x = x;
			this.y = y;

			left = lifespan;

			size = 4;
			speed.polar(-Random.Float(PointF.PI), Random.Float(32, 48));
		}

		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.5f ? (1 - p) * 2f : 1;
		}
	}

	@Override
	public void onComplete(Animation anim) {
		super.onComplete(anim);

		if (anim == pumpAttack) {

			idle();
			ch.onAttackComplete();
		}
	}
}
