/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Bones;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.Tower;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class CavesBossLevel extends Level {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;

		viewDistance = 6;
	}

	private static final int ROOM_LEFT = getWidth() / 2 - 2;
	private static final int ROOM_RIGHT = getWidth() / 2 + 2;
	private static final int ROOM_TOP = HEIGHT / 2 - 2;
	private static final int ROOM_BOTTOM = HEIGHT / 2 + 2;

	private int arenaDoor;
	private boolean enteredArena = false;
	private boolean keyDropped = false;

	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}

	private static final String DOOR = "door";
	private static final String ENTERED = "entered";
	private static final String DROPPED = "droppped";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DOOR, arenaDoor);
		bundle.put(ENTERED, enteredArena);
		bundle.put(DROPPED, keyDropped);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		arenaDoor = bundle.getInt(DOOR);
		enteredArena = bundle.getBoolean(ENTERED);
		keyDropped = bundle.getBoolean(DROPPED);
	}

	@Override
	protected boolean build() {

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
				bottom = Random.Int(ROOM_TOP + 3, HEIGHT - 1);
			}

			Painter.fill(this, left, top, right - left + 1, bottom - top + 1,
					Terrain.EMPTY);

			if (top < topMost) {
				topMost = top;
				exit = Random.Int(left, right) + (top - 1) * getWidth();
			}
		}

		map[exit] = Terrain.LOCKED_EXIT;

		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int(6) == 0) {
				map[i] = Terrain.INACTIVE_TRAP;
			}
		}

		Painter.fill(this, ROOM_LEFT - 1, ROOM_TOP - 1, ROOM_RIGHT - ROOM_LEFT
				+ 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL);
		Painter.fill(this, ROOM_LEFT, ROOM_TOP + 1, ROOM_RIGHT - ROOM_LEFT + 1,
				ROOM_BOTTOM - ROOM_TOP, Terrain.EMPTY);

		Painter.fill(this, ROOM_LEFT, ROOM_TOP, ROOM_RIGHT - ROOM_LEFT + 1, 1,
				Terrain.TOXIC_TRAP);

		arenaDoor = Random.Int(ROOM_LEFT, ROOM_RIGHT) + (ROOM_BOTTOM + 1)
				* getWidth();
		map[arenaDoor] = Terrain.DOOR;

		entrance = Random.Int(ROOM_LEFT + 1, ROOM_RIGHT - 1)
				+ Random.Int(ROOM_TOP + 1, ROOM_BOTTOM - 1) * getWidth();
		map[entrance] = Terrain.ENTRANCE;

		boolean[] patch = Patch.generate(0.45f, 6);
		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}

		return true;
	}

	@Override
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
		}

		int sign;
		do {
			sign = Random.Int(ROOM_LEFT, ROOM_RIGHT)
					+ Random.Int(ROOM_TOP, ROOM_BOTTOM) * getWidth();
		} while (sign == entrance);
		map[sign] = Terrain.SIGN;
	}

	@Override
	protected void createMobs() {
	}

	@Override
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = Random.IntRange(ROOM_LEFT, ROOM_RIGHT)
						+ Random.IntRange(ROOM_TOP + 1, ROOM_BOTTOM) * getWidth();
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop(item, pos).type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell() {
		return -1;
	}

	@Override
	public void press(int cell, Char hero) {

		super.press(cell, hero);

		if (!enteredArena && outsideEntraceRoom(cell) && hero == Dungeon.hero) {

			enteredArena = true;
			locked = true;

			Mob boss = Bestiary.mob(Dungeon.depth);
			boss.state = boss.WANDERING;
			do {
				boss.pos = Random.Int(getLength());
			} while (!passable[boss.pos] || !outsideEntraceRoom(boss.pos)
					|| Dungeon.visible[boss.pos]);
			GameScene.add(boss);
			
			Tower a = new Tower();  
	    	
			do {
				a.pos = Random.Int(getLength());
			} while (!passable[a.pos] || !outsideEntraceRoom(a.pos)
					|| Dungeon.visible[a.pos]);
			a.state = a.PASSIVE;
			GameScene.add(a);
			
			//for (int n : NEIGHBOURS8) {
			//	if (map[a.pos + n] == Terrain.EMPTY) {
			//		map[a.pos + n] = Terrain.SECRET_ALARM_TRAP;
			//	    }
			//}
			
            Tower b = new Tower();  
	    	
            do {
				b.pos = Random.Int(getLength());
			} while (!passable[b.pos] || !outsideEntraceRoom(b.pos)
					|| Dungeon.visible[b.pos]);
			b.state = b.PASSIVE;
			GameScene.add(b);
			
			//for (int n : NEIGHBOURS8) {
			//	if (map[b.pos + n] == Terrain.EMPTY) {
			//		map[b.pos + n] = Terrain.SECRET_ALARM_TRAP;
			//	    }
			//}

			set(arenaDoor, Terrain.WALL);
			GameScene.updateMap(arenaDoor);

			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (mob instanceof PET) {
					if (!((PET) mob).stay) {
						((PET) mob).pos = arenaDoor;
						mob.sprite.move(Dungeon.hero.pos, mob.pos);
					}
				}
				if (mob instanceof DriedRose.GhostHero) {
					((DriedRose.GhostHero) mob).pos = arenaDoor;
					mob.sprite.move(Dungeon.hero.pos, mob.pos);
				}
			}

			Dungeon.observe();

			CellEmitter.get(arenaDoor).start(Speck.factory(Speck.ROCK), 0.07f,
					10);
			Camera.main.shake(3, 0.7f);
			Sample.INSTANCE.play(Assets.SND_ROCKS);
		}
	}

	@Override
	public Heap drop(Item item, int cell) {

		if (!keyDropped && item instanceof SkeletonKey) {

			keyDropped = true;
			locked = false;

			CellEmitter.get(arenaDoor).start(Speck.factory(Speck.ROCK), 0.07f,
					10);

			set(arenaDoor, Terrain.EMPTY_DECO);
			GameScene.updateMap(arenaDoor);
			Dungeon.observe();
		}

		return super.drop(item, cell);
	}

	private boolean outsideEntraceRoom(int cell) {
		int cx = cell % getWidth();
		int cy = cell / getWidth();
		return cx < ROOM_LEFT - 1 || cx > ROOM_RIGHT + 1 || cy < ROOM_TOP - 1
				|| cy > ROOM_BOTTOM + 1;
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
		case Terrain.GRASS:
			return Messages.get(CavesLevel.class,"grass_name");
		case Terrain.HIGH_GRASS:
			return Messages.get(CavesLevel.class,"high_grass_name");
		case Terrain.WATER:
			return Messages.get(CavesLevel.class,"water_name");
		default:
			return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.ENTRANCE:
			return Messages.get(CavesLevel.class,"entrance_desc");
		case Terrain.EXIT:
			return Messages.get(CavesLevel.class,"exit_desc");
		case Terrain.HIGH_GRASS:
			return Messages.get(CavesLevel.class,"high_grass_desc");
		case Terrain.WALL_DECO:
			return Messages.get(CavesLevel.class,"wall_deco_desc");
		default:
			return super.tileDesc(tile);
		}
	}

	@Override
	public void addVisuals(Scene scene) {
		CavesLevel.addVisuals(this, scene);
	}
}
