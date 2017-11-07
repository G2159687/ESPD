
package com.github.epd.sprout;

import com.github.epd.sprout.levels.Terrain;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class DungeonTilemap extends Tilemap {

	public static final int SIZE = 16;

	private static DungeonTilemap instance;

	public DungeonTilemap() {
		super(Dungeon.level.tilesTex(), new TextureFilm(
				Dungeon.level.tilesTex(), SIZE, SIZE));
		map(Dungeon.level.map, Dungeon.level.getWidth());

		instance = this;
	}

	public int screenToTile(int x, int y) {
		Point p = camera().screenToCamera(x, y).offset(this.point().negate())
				.invScale(SIZE).floor();
		return p.x >= 0 && p.x < Dungeon.level.getWidth() && p.y >= 0 && p.y < Dungeon.level.getHeight() ? p.x
				+ p.y * Dungeon.level.getWidth()
				: -1;
	}

	@Override
	public boolean overlapsPoint(float x, float y) {
		return true;
	}

	public void discover(int pos, int oldValue) {

		final Image tile = tile(oldValue);
		tile.point(tileToWorld(pos));

		// For bright mode
		tile.rm = tile.gm = tile.bm = rm;
		tile.ra = tile.ga = tile.ba = ra;
		parent.add(tile);

		parent.add(new AlphaTweener(tile, 0, 0.6f) {
			@Override
			protected void onComplete() {
				tile.killAndErase();
				killAndErase();
			}
		});
	}

	public static PointF tileToWorld(int pos) {
		return new PointF(pos % Dungeon.level.getWidth(), pos / Dungeon.level.getWidth()).scale(SIZE);
	}

	public static PointF tileCenterToWorld(int pos) {
		return new PointF((pos % Dungeon.level.getWidth() + 0.5f) * SIZE,
				(pos / Dungeon.level.getWidth() + 0.5f) * SIZE);
	}

	public static Image tile(int index) {
		Image img = new Image(instance.texture);
		img.frame(instance.tileset.get(index));
		return img;
	}

	@Override
	public boolean overlapsScreenPoint(int x, int y) {
		return true;
	}

	@Override
	protected boolean needsRender(int pos) {
		return (Dungeon.level.discoverable[pos] || Dungeon.level.map[pos] == Terrain.CHASM) && Dungeon.level.map[pos] != Terrain.WATER;
	}
}
