
package com.github.epd.sprout.levels;

import android.opengl.GLES20;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.items.Torch;
import com.github.epd.sprout.levels.painters.HallsPainter;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.messages.Messages;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import javax.microedition.khronos.opengles.GL10;

public class HallsLevel extends RegularLevel {

	{
		minRoomSize = 6;

		viewDistance = Math.max(25 - Dungeon.depth, 1);

		color1 = 0x801500;
		color2 = 0xa68521;
	}

	@Override
	protected int standardRooms() {
		//8 to 10, average 8.67
		return 8+Random.chances(new float[]{3, 2, 1});
	}

	@Override
	protected int specialRooms() {
		//2 to 3, average 2.5
		return 2 + Random.chances(new float[]{1, 1});
	}

	@Override
	public void create() {
		addItemToSpawn(new Torch());
		super.create();
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	@Override
	protected void setPar() {
		Dungeon.pars[Dungeon.depth] = 300 + (Dungeon.depth * 50) + (secretDoors * 20);
	}

	@Override
	protected Painter painter() {
		return new HallsPainter()
				.setWater(feeling == Feeling.WATER ? 0.70f : 0.15f, 6)
				.setGrass(feeling == Feeling.GRASS ? 0.65f : 0.10f, 3);
	}

	@Override
	protected void createItems() {
		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Random.Int(1) == 0) {
			addItemToSpawn(new Bomb());
		}
		super.createItems();
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case Terrain.GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(HallsLevel.class, "bookshelf_desc");
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
			if (level.map[i] == 63) {
				scene.add(new Stream(i));
			}
		}
	}

	private static class Stream extends Group {

		private int pos;

		private float delay;

		public Stream(int pos) {
			super();

			this.pos = pos;

			delay = Random.Float(2);
		}

		@Override
		public void update() {

			if (visible = Dungeon.visible[pos]) {

				super.update();

				if ((delay -= Game.elapsed) <= 0) {

					delay = Random.Float(2);

					PointF p = DungeonTilemap.tileToWorld(pos);
					((FireParticle) recycle(FireParticle.class)).reset(p.x
									+ Random.Float(DungeonTilemap.SIZE),
							p.y + Random.Float(DungeonTilemap.SIZE));
				}
			}
		}

		@Override
		public void draw() {
			GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			super.draw();
			GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		}
	}

	public static class FireParticle extends PixelParticle.Shrinking {

		public FireParticle() {
			super();

			color(0xEE7722);
			lifespan = 1f;

			acc.set(0, +80);
		}

		public void reset(float x, float y) {
			revive();

			this.x = x;
			this.y = y;

			left = lifespan;

			speed.set(0, -40);
			size = 4;
		}

		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.8f ? (1 - p) * 5 : 1;
		}
	}
}
