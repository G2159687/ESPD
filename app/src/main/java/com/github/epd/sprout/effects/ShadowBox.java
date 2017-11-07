
package com.github.epd.sprout.effects;

import com.github.epd.sprout.Assets;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.NinePatch;

public class ShadowBox extends NinePatch {

	public static final float SIZE = 16;

	public ShadowBox() {
		super(Assets.SHADOW, 1);

		if (texture.id == -1)
			texture.filter(Texture.LINEAR, Texture.LINEAR);

		scale.set(SIZE, SIZE);
	}

	@Override
	public void size(float width, float height) {
		super.size(width / SIZE, height / SIZE);
	}

	public void boxRect(float x, float y, float width, float height) {
		this.x = x - SIZE;
		this.y = y - SIZE;
		size(width + SIZE * 2, height + SIZE * 2);
	}
}
