
package com.github.epd.sprout.effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.epd.sprout.scenes.PixelScene;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;

public class Halo extends Image {

	private static final Object CACHE_KEY = Halo.class;

	protected static final int RADIUS = 64;

	protected float radius = RADIUS;
	protected float brightness = 1;

	public Halo() {
		super();

		if (!TextureCache.contains(CACHE_KEY)) {
			Bitmap bmp = Bitmap.createBitmap(RADIUS * 2, RADIUS * 2,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			Paint paint = new Paint();
			paint.setColor(0xFFFFFFFF);
			canvas.drawCircle(RADIUS, RADIUS, RADIUS * 0.75f, paint);
			paint.setColor(0x88FFFFFF);
			canvas.drawCircle(RADIUS, RADIUS, RADIUS, paint);
			TextureCache.add(CACHE_KEY, new SmartTexture(bmp));
		}

		texture(CACHE_KEY);
	}

	public Halo(float radius, int color, float brightness) {

		this();

		hardlight(color);
		alpha(this.brightness = brightness);
		radius(radius);
	}

	public Halo point(float x, float y) {
		this.x = x - (width() / 2f);
		this.y = y - (height() / 2f);
		PixelScene.align(this);
		return this;
	}

	public void radius(float value) {
		scale.set((this.radius = value) / RADIUS);
	}
}
