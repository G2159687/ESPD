
package com.github.epd.sprout.sprites;

import android.graphics.Bitmap;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class ItemSprite extends MovieClip {

	public static final int SIZE = 16;

	private static final float DROP_INTERVAL = 0.4f;

	protected static TextureFilm film;

	public Heap heap;

	private Glowing glowing;
	private float phase;
	private boolean glowUp;

	private float dropInterval;

	public ItemSprite() {
		this(ItemSpriteSheet.SMTH, null);
	}

	public ItemSprite(Item item) {
		super(Assets.ITEMS);
		if (film == null) {
			film = new TextureFilm(texture, SIZE, SIZE);
		}

		view(item);
	}

	public ItemSprite(int image, Glowing glowing) {
		super(Assets.ITEMS);

		if (film == null) {
			film = new TextureFilm(texture, SIZE, SIZE);
		}

		view(image, glowing);
	}

	public void originToCenter() {
		origin.set(SIZE / 2);
	}

	public void link() {
		link(heap);
	}

	public void link(Heap heap) {
		this.heap = heap;
		view(heap.image(), heap.glowing());
		place(heap.pos);
	}

	@Override
	public void revive() {
		super.revive();

		speed.set(0);
		acc.set(0);
		dropInterval = 0;

		heap = null;
	}

	public PointF worldToCamera(int cell) {
		final int csize = DungeonTilemap.SIZE;

		return new PointF(cell % Dungeon.level.getWidth() * csize + (csize - SIZE) * 0.5f,
				cell / Dungeon.level.getWidth() * csize + (csize - SIZE) * 0.5f);
	}

	public void place(int p) {
		if (Dungeon.level != null)
			point(worldToCamera(p));
	}

	public void drop() {

		if (heap.isEmpty()) {
			return;
		}

		dropInterval = DROP_INTERVAL;

		speed.set(0, -100);
		acc.set(0, -speed.y / DROP_INTERVAL * 2);

		if (visible && heap != null && heap.peek() instanceof Gold) {
			CellEmitter.center(heap.pos).burst(Speck.factory(Speck.COIN), 5);
			Sample.INSTANCE.play(Assets.SND_GOLD, 1, 1,
					Random.Float(0.9f, 1.1f));
		}
	}

	public void drop(int from) {

		if (heap.pos == from) {
			drop();
		} else {

			float px = x;
			float py = y;
			drop();

			place(from);

			speed.offset((px - x) / DROP_INTERVAL, (py - y) / DROP_INTERVAL);
		}
	}

	public ItemSprite view(Item item) {
		return view(item.image(), item.glowing());
	}

	public ItemSprite view(int image, Glowing glowing) {
		frame(film.get(image));
		if ((this.glowing = glowing) == null) {
			resetColor();
		}
		return this;
	}

	@Override
	public void update() {
		super.update();

		visible = (heap == null || heap.seen);

		if (dropInterval > 0 && (dropInterval -= Game.elapsed) <= 0) {

			speed.set(0);
			acc.set(0);
			place(heap.pos);

			if (visible) {
				boolean water = Level.water[heap.pos];

				if (water) {
					GameScene.ripple(heap.pos);
				} else {
					int cell = Dungeon.level.map[heap.pos];
					water = (cell == Terrain.WELL || cell == Terrain.ALCHEMY);
				}

				if (!(heap.peek() instanceof Gold)) {
					Sample.INSTANCE.play(water ? Assets.SND_WATER
							: Assets.SND_STEP, 0.8f, 0.8f, 1.2f);
				}
			}
		}

		if (visible && glowing != null) {
			if (glowUp && (phase += Game.elapsed) > glowing.period) {

				glowUp = false;
				phase = glowing.period;

			} else if (!glowUp && (phase -= Game.elapsed) < 0) {

				glowUp = true;
				phase = 0;

			}

			float value = phase / glowing.period * 0.6f;

			rm = gm = bm = 1 - value;
			ra = glowing.red * value;
			ga = glowing.green * value;
			ba = glowing.blue * value;
		}
	}

	public static int pick(int index, int x, int y) {
		Bitmap bmp = TextureCache.get(Assets.ITEMS).bitmap;
		int rows = bmp.getWidth() / SIZE;
		int row = index / rows;
		int col = index % rows;
		return bmp.getPixel(col * SIZE + x, row * SIZE + y);
	}

	public static class Glowing {

		public static final Glowing WHITE = new Glowing(0xFFFFFF, 0.6f);

		public float red;
		public float green;
		public float blue;
		public float period;

		public Glowing(int color) {
			this(color, 1f);
		}

		public Glowing(int color, float period) {
			red = (color >> 16) / 255f;
			green = ((color >> 8) & 0xFF) / 255f;
			blue = (color & 0xFF) / 255f;

			this.period = period;
		}
	}
}
