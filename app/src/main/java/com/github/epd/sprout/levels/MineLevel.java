
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.levels.builders.BranchesBuilder;
import com.github.epd.sprout.levels.builders.Builder;
import com.github.epd.sprout.levels.painters.CavesPainter;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.connection.TunnelRoom;
import com.github.epd.sprout.levels.rooms.standard.StandardRoom;
import com.github.epd.sprout.messages.Messages;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class MineLevel extends RegularLevel {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;

		viewDistance = 6;
	}

	@Override
	protected int standardRooms() {
		switch (Dungeon.mapSize){
			case 1:
				return 8 + Random.chances(new float[]{4, 2, 1});
			case 2:
				return 16 + Random.chances(new float[]{4, 2, 1});
			case 3:
				return 20 + Random.chances(new float[]{4, 2, 1});
			default:
				return 5 + Random.chances(new float[]{4, 2, 1});
		}
	}

	@Override
	protected int specialRooms() {
		switch (Dungeon.mapSize){
			case 1:
				return 2 + Random.chances(new float[]{4, 2, 1});
			case 2:
				return 3 + Random.chances(new float[]{4, 2, 1});
			case 3:
				return 4 + Random.chances(new float[]{4, 2, 1});
			default:
				return 1 + Random.chances(new float[]{4, 2, 1});
		}
	}

	@Override
	protected Painter painter() {
		return new CavesPainter()
				.setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 6)
				.setGrass(feeling == Feeling.GRASS ? 0.65f : 0.15f, 3);
	}

	protected Builder builder(){
		return new BranchesBuilder();
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}

	@Override
	protected void setPar() {
		Dungeon.pars[Dungeon.depth] = 400 + (Dungeon.depth * 50) + (secretDoors * 20);
	}

	@Override
	protected void createItems() {

		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Random.Int(3) == 0) {
			addItemToSpawn(new Bomb());
		}

		decorate();
		super.createItems();
	}

	protected void decorate() {

		for (Room room : rooms) {
			if (!(room instanceof StandardRoom)) {
				continue;
			}

			if (room.width() <= 3 || room.height() <= 3) {
				continue;
			}

			for (Room n : room.connected.keySet()) {
				if ((n instanceof StandardRoom|| n instanceof TunnelRoom)
						&& Random.Int(3) == 0) {
					Painter.set(this, room.connected.get(n), Terrain.EMPTY_DECO);
				}
			}
		}

		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.WALL && Random.Int(3) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}

		setPar();

		if (Dungeon.bossLevel(Dungeon.depth + 1)) {
			return;
		}

		for (Room r : rooms) {
			if (r instanceof StandardRoom) {
				for (Room n : r.neigbours) {
					if (n instanceof StandardRoom && !r.connected.containsKey(n)) {
						Rect w = r.intersect(n);
						if (w.left == w.right && w.bottom - w.top >= 5) {

							w.top += 2;
							w.bottom -= 1;

							w.right++;

							Painter.fill(this, w.left, w.top, 1, w.height(),
									Terrain.BARRICADE);

						} else if (w.top == w.bottom && w.right - w.left >= 5) {

							w.left += 2;
							w.right -= 1;

							w.bottom++;

							Painter.fill(this, w.left, w.top, w.width(), 1,
									Terrain.BARRICADE);
						}
					}
				}
			}
		}

	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.GRASS:
				return Messages.get(CavesLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_name");
			case Terrain.WATER:
				return Messages.get(CavesLevel.class, "water_name");
			default:
				return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CavesLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CavesLevel.class, "exit_desc");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_desc");
			case Terrain.WALL_DECO:
				return Messages.get(CavesLevel.class, "wall_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CavesLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc(tile);
		}
	}

	@Override
	public void addVisuals(Scene scene) {
		super.addVisuals(scene);
		addVisuals(this, scene);
	}

	public static void addVisuals(Level level, Scene scene) {
		for (int i = 0; i < Dungeon.level.getLength(); i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				scene.add(new Vein(i));
			}
		}
	}

	private static class Vein extends Group {

		private int pos;

		private float delay;

		public Vein(int pos) {
			super();

			this.pos = pos;

			delay = Random.Float(2);
		}

		@Override
		public void update() {

			if (visible = Dungeon.visible[pos]) {

				super.update();

				if ((delay -= Game.elapsed) <= 0) {

					delay = Random.Float();

					PointF p = DungeonTilemap.tileToWorld(pos);
					((Sparkle) recycle(Sparkle.class)).reset(
							p.x + Random.Float(DungeonTilemap.SIZE), p.y
									+ Random.Float(DungeonTilemap.SIZE));
				}
			}
		}
	}

	public static final class Sparkle extends PixelParticle {

		public void reset(float x, float y) {
			revive();

			this.x = x;
			this.y = y;

			left = lifespan = 0.5f;
		}

		@Override
		public void update() {
			super.update();

			float p = left / lifespan;
			size((am = p < 0.5f ? p * 2 : (1 - p) * 2) * 2);
		}
	}
}