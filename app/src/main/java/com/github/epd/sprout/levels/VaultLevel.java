
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.Scene;
import com.watabou.utils.Random;

public class VaultLevel extends Level {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
		cleared = true;

	}

	private static final int ROOM_LEFT = 48 / 2 - 2;
	private static final int ROOM_RIGHT = 48 / 2 + 2;
	private static final int ROOM_TOP = 48 / 2 - 2;
	private static final int ROOM_BOTTOM = 48 / 2 + 2;

	protected static final float TIME_TO_RESPAWN = 20;
	protected static final int REGROW_TIMER = 4;

	@Override
	public String tilesTex() {
		return Assets.TILES_VAULT;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}


	@Override
	protected boolean build() {

		setSize(48, 39);

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
		map[entrance] = Terrain.EMPTY;
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
			if (map[i] == Terrain.EMPTY && heaps.get(i) == null && Random.Float() < .15) {
				map[i] = Terrain.STATUE;
			}
			if (map[i] == Terrain.EMPTY && heaps.get(i) == null && Random.Float() < .10) {
				map[i] = Terrain.HIGH_GRASS;
			}
			if (map[i] == Terrain.EMPTY && heaps.get(i) == null && Random.Float() < .10) {
				map[i] = Terrain.GRASS;
			}
		}

	}

	@Override
	protected void createItems() {

		for (int i = 0; i < 50; i++) {
			int pos = randomDestination();
			drop(new Gold(Random.Int(100, 300)), pos);
		}

	}


	//@Override
	//public int randomRespawnCell() {
	//	return -1;
	//}


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
				return Messages.get(ThiefBossLevel.class, "deco_desc");
			case Terrain.EMPTY_SP:
				return Messages.get(CityLevel.class, "sp_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(CityLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return "";
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
		return 16;
	}

	@Override
	protected void createMobs() {
		int nMobs = nMobs();
		for (int i = 0; i < nMobs; i++) {
			Mob mob = Bestiary.mob(Dungeon.depth);
			do {
				mob.pos = randomRespawnCellMob();
			} while (mob.pos == -1);
			mobs.add(mob);
		}
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
					mob.pos = randomRespawnCellMob();
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
