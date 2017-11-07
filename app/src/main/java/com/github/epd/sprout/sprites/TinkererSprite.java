
package com.github.epd.sprout.sprites;

import android.opengl.GLES20;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.effects.Halo;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.PointF;

import javax.microedition.khronos.opengles.GL10;

public class TinkererSprite extends MobSprite {

	private Shield shield;

	public TinkererSprite() {
		super();

		texture(Assets.TINKERER1);

		TextureFilm frames = new TextureFilm(texture, 12, 14);

		idle = new Animation(10, true);
		idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3,
				3, 3, 3, 3, 3, 2, 1);

		run = new Animation(20, true);
		run.frames(frames, 0);

		die = new Animation(20, false);
		die.frames(frames, 0);

		play(idle);
	}

	@Override
	public void link(Char ch) {
		super.link(ch);

		if (shield == null) {
			parent.add(shield = new Shield());
		}
	}

	@Override
	public void die() {
		super.die();

		if (shield != null) {
			shield.putOut();
		}
		emitter().start(ElmoParticle.FACTORY, 0.03f, 60);
	}

	public class Shield extends Halo {

		private float phase;

		public Shield() {

			super(14, 0xBBAACC, 1f);

			am = -1;
			aa = +1;

			phase = 1;
		}

		@Override
		public void update() {
			super.update();

			if (phase < 1) {
				if ((phase -= Game.elapsed) <= 0) {
					killAndErase();
				} else {
					scale.set((2 - phase) * radius / RADIUS);
					am = phase * (-1);
					aa = phase * (+1);
				}
			}

			if (visible = TinkererSprite.this.visible) {
				PointF p = TinkererSprite.this.center();
				point(p.x, p.y);
			}
		}

		@Override
		public void draw() {
			GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			super.draw();
			GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		}

		public void putOut() {
			phase = 0.999f;
		}
	}

}
