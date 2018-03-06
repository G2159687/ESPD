
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Bones;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Yog;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.Scene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class HallsBossLevel extends Level {

	{
		color1 = 0x801500;
		color2 = 0xa68521;

		viewDistance = 3;
	}

	private static final int ROOM_LEFT = 48 / 2 - 1;
	private static final int ROOM_RIGHT = 48 / 2 + 1;
	private static final int ROOM_TOP = 48 / 2 - 1;
	private static final int ROOM_BOTTOM = 48 / 2 + 1;

	private int stairs = -1;
	private boolean enteredArena = false;
	private boolean keyDropped = false;

	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	private static final String STAIRS = "stairs";
	private static final String ENTERED = "entered";
	private static final String DROPPED = "droppped";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STAIRS, stairs);
		bundle.put(ENTERED, enteredArena);
		bundle.put(DROPPED, keyDropped);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		stairs = bundle.getInt(STAIRS);
		enteredArena = bundle.getBoolean(ENTERED);
		keyDropped = bundle.getBoolean(DROPPED);
	}

	@Override
	protected boolean build() {

		setSize(48, 48);

		for (int i = 0; i < 5; i++) {

			int top = Random.IntRange(2, ROOM_TOP - 1);
			int bottom = Random.IntRange(ROOM_BOTTOM + 1, 22);
			Painter.fill(this, 2 + i * 4, top, 4, bottom - top + 1,
					Terrain.EMPTY);

			if (i == 2) {
				exit = (i * 4 + 3) + (top - 1) * getWidth();
			}

			for (int j = 0; j < 4; j++) {
				if (Random.Int(2) == 0) {
					int y = Random.IntRange(top + 1, bottom - 1);
					map[i * 4 + j + y * getWidth()] = Terrain.WALL_DECO;
				}
			}
		}

		map[exit] = Terrain.LOCKED_EXIT;

		Painter.fill(this, ROOM_LEFT - 1, ROOM_TOP - 1, ROOM_RIGHT - ROOM_LEFT
				+ 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.BARRICADE);

		Painter.fill(this, ROOM_LEFT, ROOM_TOP, ROOM_RIGHT - ROOM_LEFT + 1,
				ROOM_BOTTOM - ROOM_TOP + 1, Terrain.EMPTY);

		entrance = Random.Int(ROOM_LEFT + 1, ROOM_RIGHT - 1)
				+ Random.Int(ROOM_TOP + 1, ROOM_BOTTOM - 1) * getWidth();
		map[entrance] = Terrain.PEDESTAL;

		boolean[] patch = Patch.generate( getWidth(), getHeight(), 0.30f, 6, true );
		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}

		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int(10) == 0) {
				map[i] = Terrain.EMPTY_DECO;
				if (map[i] == Terrain.ENTRANCE) {
					map[i] = Terrain.PEDESTAL;
				}
			}
		}

		sealedlevel = false;

		return true;
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
			} while (pos == entrance);
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

		if (!enteredArena && hero == Dungeon.hero && cell != entrance) {

			enteredArena = true;
			locked = true;

			for (int i = ROOM_LEFT - 1; i <= ROOM_RIGHT + 1; i++) {
				doMagic((ROOM_TOP - 1) * getWidth() + i);
				doMagic((ROOM_BOTTOM + 1) * getWidth() + i);
			}
			for (int i = ROOM_TOP; i < ROOM_BOTTOM + 1; i++) {
				doMagic(i * getWidth() + ROOM_LEFT - 1);
				doMagic(i * getWidth() + ROOM_RIGHT + 1);
			}
			doMagic(entrance);
			GameScene.updateMap();

			Dungeon.observe();

			Yog boss = new Yog();
			do {
				boss.pos = Random.Int(getLength());
			} while (!passable[boss.pos] || Dungeon.visible[boss.pos]);
			GameScene.add(boss);
			boss.spawnFists();

			stairs = entrance;
			entrance = -1;
		}
	}

	private void doMagic(int cell) {
		set(cell, Terrain.EMPTY_SP);
		CellEmitter.get(cell).start(FlameParticle.FACTORY, 0.1f, 3);
	}

	@Override
	public Heap drop(Item item, int cell) {

		if (!keyDropped && item instanceof SkeletonKey) {
			keyDropped = true;
			locked = false;

			entrance = stairs;
			set(entrance, Terrain.PEDESTAL);
			GameScene.updateMap(entrance);
		}

		return super.drop(item, cell);
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
			default:
				return super.tileDesc(tile);
		}
	}

	@Override
	public void addVisuals(Scene scene) {
		HallsLevel.addVisuals(this, scene);
	}
}
