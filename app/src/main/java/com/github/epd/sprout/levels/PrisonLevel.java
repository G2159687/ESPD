
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.actors.mobs.MossySkeleton;
import com.github.epd.sprout.actors.mobs.npcs.Wandmaker;
import com.github.epd.sprout.effects.Halo;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.painters.PrisonPainter;
import com.github.epd.sprout.messages.Messages;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class PrisonLevel extends RegularLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}

	@Override
	protected int standardRooms() {
		//6 to 8, average 6.66
		return 6+Random.chances(new float[]{4, 2, 2});
	}

	@Override
	protected int specialRooms() {
		//1 to 3, average 1.83
		return 1+Random.chances(new float[]{3, 4, 3});
	}

	@Override
	protected Painter painter() {
		return new PrisonPainter()
				.setWater(feeling == Feeling.WATER ? 0.90f : 0.30f, 4)
				.setGrass(feeling == Feeling.GRASS ? 0.80f : 0.20f, 3);
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

	@Override
	protected void setPar() {
		Dungeon.pars[Dungeon.depth] = 500 + (Dungeon.depth * 50) + (secretDoors * 20);
	}

	@Override
	protected void createItems() {
		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Random.Int(1) == 0) {
			addItemToSpawn(new Bomb());
		}

		super.createItems();

		Wandmaker.Quest.spawn(this, roomEntrance);
		spawnSkeleton(this);
	}

	public static void spawnSkeleton(PrisonLevel level) {
		if (Dungeon.depth == 9) {

			MossySkeleton skeleton = new MossySkeleton();
			do {
				skeleton.pos = level.randomRespawnCell();
			} while (skeleton.pos == -1);
			level.mobs.add(skeleton);
		}
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(PrisonLevel.class, "water_name");
			default:
				return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(PrisonLevel.class, "empty_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(PrisonLevel.class, "bookshelf_desc");
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
				scene.add(new Torch(i));
			}
		}
	}

	private static class Torch extends Emitter {

		private int pos;

		public Torch(int pos) {
			super();

			this.pos = pos;

			PointF p = DungeonTilemap.tileCenterToWorld(pos);
			pos(p.x - 1, p.y + 3, 2, 0);

			pour(FlameParticle.FACTORY, 0.15f);

			add(new Halo(16, 0xFFFFCC, 0.2f).point(p.x, p.y));
		}

		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				super.update();
			}
		}
	}
}