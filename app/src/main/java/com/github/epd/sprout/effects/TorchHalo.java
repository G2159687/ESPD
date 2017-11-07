
package com.github.epd.sprout.effects;

import android.opengl.GLES20;

import com.github.epd.sprout.sprites.CharSprite;
import com.watabou.noosa.Game;

import javax.microedition.khronos.opengles.GL10;

public class TorchHalo extends Halo {

	private CharSprite target;

	private float phase = 0;

	public TorchHalo(CharSprite sprite) {
		super(24, 0xFFDDCC, 0.15f);
		target = sprite;
		am = 0;
	}

	@Override
	public void update() {
		super.update();

		if (phase < 0) {
			if ((phase += Game.elapsed) >= 0) {
				killAndErase();
			} else {
				scale.set((2 + phase) * radius / RADIUS);
				am = -phase * brightness;
			}
		} else if (phase < 1) {
			if ((phase += Game.elapsed) >= 1) {
				phase = 1;
			}
			scale.set(phase * radius / RADIUS);
			am = phase * brightness;
		}

		point(target.x + target.width / 2, target.y + target.height / 2);
	}

	@Override
	public void draw() {
		GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		super.draw();
		GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void putOut() {
		phase = -1;
	}
}
