
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.actors.mobs.GoldThief;
import com.github.epd.sprout.actors.mobs.npcs.Imp;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.levels.painters.CityPainter;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.messages.Messages;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class CityLevel extends RegularLevel {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}

	@Override
	protected int standardRooms() {
		switch (Dungeon.mapSize){
			case 1:
				return 7 + Random.chances(new float[]{4, 3, 2, 1});
			case 2:
				return 16 + Random.chances(new float[]{4, 3, 2, 1});
			case 3:
				return 20 + Random.chances(new float[]{4, 3, 2, 1});
			default:
				return 5 + Random.chances(new float[]{4, 2, 1});
		}
	}

	@Override
	protected int specialRooms() {
		switch (Dungeon.mapSize){
			case 1:
				return 2 + Random.chances(new float[]{2, 1});
			case 2:
				return 3 + Random.chances(new float[]{2, 1});
			case 3:
				return 4 + Random.chances(new float[]{2, 1});
			default:
				return 1 + Random.chances(new float[]{4, 2, 1});
		}
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}

	@Override
	protected Painter painter() {
		return new CityPainter()
				.setWater(feeling == Feeling.WATER ? 0.90f : 0.30f, 4)
				.setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 3);
	}

	@Override
	protected void setPar() {
		Dungeon.pars[Dungeon.depth] = (150 + (Dungeon.depth * 50) + (secretDoors * 20)) * Math.round(0.5f + 0.5f * Dungeon.mapSize);
	}

	@Override
	protected void createItems() {
		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Random.Int(1) == 0) {
			addItemToSpawn(new Bomb());
		}
		super.createItems();

		Imp.Quest.spawn(this);
		spawnGoldThief(this);
	}

	public static void spawnGoldThief(CityLevel level) {
		if (Dungeon.depth == 19) {

			GoldThief thief = new GoldThief();
			do {
				thief.pos = level.randomRespawnCell();
			} while (thief.pos == -1);
			level.mobs.add(thief);
		}
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(CityLevel.class, "water_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CityLevel.class, "high_grass_name");
			default:
				return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CityLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CityLevel.class, "exit_desc");
			case Terrain.WALL_DECO:
			case Terrain.EMPTY_DECO:
				return Messages.get(CityLevel.class, "deco_desc");
			case Terrain.EMPTY_SP:
				return Messages.get(CityLevel.class, "sp_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(CityLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CityLevel.class, "bookshelf_desc");
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
				scene.add(new Smoke(i));
			}
		}
	}

	private static class Smoke extends Emitter {

		private int pos;

		private static final Emitter.Factory factory = new Factory() {

			@Override
			public void emit(Emitter emitter, int index, float x, float y) {
				SmokeParticle p = (SmokeParticle) emitter
						.recycle(SmokeParticle.class);
				p.reset(x, y);
			}
		};

		public Smoke(int pos) {
			super();

			this.pos = pos;

			PointF p = DungeonTilemap.tileCenterToWorld(pos);
			pos(p.x - 4, p.y - 2, 4, 0);

			pour(factory, 0.2f);
		}

		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				super.update();
			}
		}
	}

	public static final class SmokeParticle extends PixelParticle {

		public SmokeParticle() {
			super();

			color(0x000000);
			speed.set(Random.Float(8), -Random.Float(8));
		}

		public void reset(float x, float y) {
			revive();

			this.x = x;
			this.y = y;

			left = lifespan = 2f;
		}

		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.8f ? 1 - p : p * 0.25f;
			size(8 - p * 4);
		}
	}
}