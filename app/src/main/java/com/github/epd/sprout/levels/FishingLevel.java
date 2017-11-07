
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.bombs.FishingBomb;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.potions.PotionOfLevitation;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.Scene;
import com.watabou.utils.Random;

public class FishingLevel extends Level {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
		cleared = true;

		viewDistance = 8;
	}

	private static final int ROOM_LEFT = 48 / 2 - 2;
	private static final int ROOM_RIGHT = 48 / 2 + 2;
	private static final int ROOM_TOP = 48 / 2 - 2;
	private static final int ROOM_BOTTOM = 48 / 2 + 2;

	@Override
	public String tilesTex() {
		return Assets.TILES_BEACH;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}


	@Override
	protected boolean build() {

		setSize(48, 48);

		int topMost = Integer.MAX_VALUE;

		for (int i = 0; i < 8; i++) {
			int left, right, top, bottom;
			if (Random.Int(2) == 0) {
				left = Random.Int(1, ROOM_LEFT - 3);
				right = ROOM_RIGHT + 3;
			} else {
				left = ROOM_LEFT - 3;
				right = Random.Int(ROOM_RIGHT + 3, getWidth() - 1);
			}
			if (Random.Int(2) == 0) {
				top = Random.Int(2, ROOM_TOP - 3);
				bottom = ROOM_BOTTOM + 3;
			} else {
				top = ROOM_LEFT - 3;
				bottom = Random.Int(ROOM_TOP + 3, getHeight() - 1);
			}

			Painter.fill(this, left, top, right - left + 1, bottom - top + 1,
					Terrain.EMPTY);

			if (top < topMost) {
				topMost = top;
				exit = Random.Int(left, right) + (top - 1) * getWidth();
			}
		}


		map[exit] = Terrain.WALL;


		Painter.fill(this, ROOM_LEFT, ROOM_TOP + 1, ROOM_RIGHT - ROOM_LEFT + 1,
				ROOM_BOTTOM - ROOM_TOP, Terrain.EMPTY);


		entrance = Random.Int(ROOM_LEFT + 1, ROOM_RIGHT - 1)
				+ Random.Int(ROOM_TOP + 1, ROOM_BOTTOM - 1) * getWidth();

		for (int i = 0; i < getLength(); i++) {

			if (map[i] == Terrain.EMPTY && Random.Float() < .95) {
				map[i] = Terrain.WATER;
			}
		}

		boolean[] patch = Patch.generate( getWidth(), getHeight(), 0.30f, 6, true );
		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.WATER && patch[i]) {
				map[i] = Terrain.EMPTY;
			}
		}
		decorate();

		return true;
	}

	protected void decorate() {

		for (int i = getWidth() + 1; i < getLength() - getWidth(); i++) {
			if (map[i] == Terrain.EMPTY) {
				int n = 0;
				if (map[i + 1] == Terrain.WALL) {
					n++;
				}
				if (map[i - 1] == Terrain.WALL) {
					n++;
				}
				if (map[i + getWidth()] == Terrain.WALL) {
					n++;
				}
				if (map[i - getWidth()] == Terrain.WALL) {
					n++;
				}
				if (Random.Int(8) <= n) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}

		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.WALL && Random.Int(8) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
			if (map[i] == Terrain.ENTRANCE) {
				map[i] = Terrain.EMPTY;
			}
		}

	}

	@Override
	protected void createItems() {

		int pos = entrance + 1;
		drop(new FishingBomb(99), pos).type = Heap.Type.CHEST;
		drop(new PotionOfLevitation(), pos);

	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(PrisonLevel.class, "water_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CrabBossLevel.class, "high_grass_name");
			default:
				return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return "";
			case Terrain.EXIT:
				return "";
			case Terrain.WALL_DECO:
			case Terrain.EMPTY_DECO:
				return Messages.get(CrabBossLevel.class, "deco_desc");
			case Terrain.EMPTY_SP:
				return "";
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(CrabBossLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CrabBossLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc(tile);
		}
	}


	@Override
	public void addVisuals(Scene scene) {
		CavesLevel.addVisuals(this, scene);
	}

	@Override
	public int nMobs() {
		return 30;
	}

	@Override
	protected void createMobs() {
		int nMobs = nMobs();
		for (int i = 0; i < nMobs; i++) {
			Mob mob = Bestiary.mob(Dungeon.depth);
			do {
				mob.pos = randomRespawnCellFishMob();
			} while (mob.pos == -1);
			mobs.add(mob);
		}
	}


	public int randomRespawnCellFishMob() {
		int cell;
		do {
			cell = Random.Int(getLength());
		} while (map[cell] != Terrain.WATER);
		return cell;
	}

	@Override
	public Actor respawner() {
		return new Actor() {

			{
				actPriority = 1; //as if it were a buff.
			}

			@Override
			protected boolean act() {
				if (mobs.size() < nMobs()) {

					Mob mob = Bestiary.mutable(Dungeon.depth);
					mob.state = mob.WANDERING;
					mob.pos = randomRespawnCellFishMob();
					if (Dungeon.hero.isAlive() && mob.pos != -1) {
						GameScene.add(mob);
					}
				}
				spend(Dungeon.level.feeling == Feeling.DARK
						|| Statistics.amuletObtained ? TIME_TO_RESPAWN / 2
						: TIME_TO_RESPAWN);
				return true;
			}
		};
	}
}
