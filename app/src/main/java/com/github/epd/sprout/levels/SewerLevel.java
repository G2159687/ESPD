
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.actors.mobs.npcs.Ghost;
import com.github.epd.sprout.actors.mobs.npcs.Ghost.GnollArcher;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.items.food.Blackberry;
import com.github.epd.sprout.items.food.Blueberry;
import com.github.epd.sprout.items.food.Cloudberry;
import com.github.epd.sprout.items.food.Moonberry;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.painters.SewerPainter;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class SewerLevel extends RegularLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	@Override
	protected int standardRooms() {
		//5 to 7, average 5.57
		return 5+Random.chances(new float[]{4, 2, 1});
	}

	@Override
	protected int specialRooms() {
		//1 to 3, average 1.67
		return 1+Random.chances(new float[]{4, 4, 2});
	}

	@Override
	protected Painter painter() {
		return new SewerPainter()
				.setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 5)
				.setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 4);
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}


	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}

	@Override
	protected void setPar() {
		Dungeon.pars[Dungeon.depth] = 500 + (Dungeon.depth * 50) + (secretDoors * 50);
	}

	@Override
	protected void createItems() {
		if (Dungeon.depth == 1) {
			addItemToSpawn(new Moonberry());
			addItemToSpawn(new Blueberry());
			addItemToSpawn(new Cloudberry());
			addItemToSpawn(new Blackberry());
		}

		Ghost.Quest.spawn(this);
		spawnGnoll(this);

		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Random.Int(3) == 0) {
			addItemToSpawn(new Bomb());
		}
		super.createItems();
	}

	public static void spawnGnoll(SewerLevel level) {
		if (Dungeon.depth == 4) {

			GnollArcher gnoll = new Ghost.GnollArcher();
			do {
				gnoll.pos = level.randomRespawnCell();
			} while (gnoll.pos == -1);
			level.mobs.add(gnoll);
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
				scene.add(new Sink(i));
			}
		}
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(SewerLevel.class, "water_name");
			default:
				return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(SewerLevel.class, "empty_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(SewerLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc(tile);
		}
	}

	private static class Sink extends Emitter {

		private int pos;
		private float rippleDelay = 0;

		private static final Emitter.Factory factory = new Factory() {

			@Override
			public void emit(Emitter emitter, int index, float x, float y) {
				WaterParticle p = (WaterParticle) emitter
						.recycle(WaterParticle.class);
				p.reset(x, y);
			}
		};

		public Sink(int pos) {
			super();

			this.pos = pos;

			PointF p = DungeonTilemap.tileCenterToWorld(pos);
			pos(p.x - 2, p.y + 1, 4, 0);

			pour(factory, 0.1f);
		}

		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {

				super.update();

				if ((rippleDelay -= Game.elapsed) <= 0) {
					GameScene.ripple(pos + Dungeon.level.getWidth()).y -= DungeonTilemap.SIZE / 2;
					rippleDelay = Random.Float(0.4f, 0.6f);
				}
			}
		}
	}

	public static final class WaterParticle extends PixelParticle {

		public WaterParticle() {
			super();

			acc.y = 50;
			am = 0.5f;

			color(ColorMath.random(0xb6ccc2, 0x3b6653));
			size(2);
		}

		public void reset(float x, float y) {
			revive();

			this.x = x;
			this.y = y;

			speed.set(Random.Float(-2, +2), 0);

			left = lifespan = 0.5f;
		}
	}
}
