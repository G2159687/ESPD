
package com.github.epd.sprout.sprites;

import android.opengl.GLES20;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.ShaftParticle;
import com.watabou.noosa.TextureFilm;

import javax.microedition.khronos.opengles.GL10;

public class GhostSprite extends MobSprite {

	public GhostSprite() {
		super();

		texture(Assets.GHOST);

		TextureFilm frames = new TextureFilm(texture, 14, 15);

		idle = new Animation(5, true);
		idle.frames(frames, 0, 1);

		run = new Animation(10, true);
		run.frames(frames, 0, 1);

		attack = new Animation(10, false);
		attack.frames(frames, 0, 2, 3);

		die = new Animation(8, false);
		die.frames(frames, 0, 4, 5, 6, 7);

		play(idle);
	}

	@Override
	public void draw() {
		GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		super.draw();
		GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void die() {
		super.die();
		emitter().start(ShaftParticle.FACTORY, 0.3f, 4);
		emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
	}

	@Override
	public int blood() {
		return 0xFFFFFF;
	}
}
