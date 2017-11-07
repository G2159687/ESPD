
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.items.quest.PuddingCup;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.messages.Messages;
import com.watabou.noosa.Scene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Arrays;

public class LastLevel extends Level {

	private static final int SIZE = 30;

	{
		color1 = 0x801500;
		color2 = 0xa68521;

		viewDistance = 8;
	}

	private int pedestal;

	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	@Override
	public void create() {
		super.create();
		for (int i = 0; i < getLength(); i++) {
			int flags = Terrain.flags[map[i]];
			if ((flags & Terrain.PIT) != 0) {
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
	}

	@Override
	protected boolean build() {

		setSize(48, 48);

		Arrays.fill(map, Terrain.CHASM);

		Painter.fill(this, 7, 31, 19, 1, Terrain.WALL);
		Painter.fill(this, 15, 10, 3, 21, Terrain.EMPTY);
		Painter.fill(this, 13, 30, 7, 1, Terrain.EMPTY);
		Painter.fill(this, 14, 29, 5, 1, Terrain.EMPTY);

		Painter.fill(this, 14, 9, 5, 7, Terrain.EMPTY);
		Painter.fill(this, 13, 10, 7, 5, Terrain.EMPTY);

		// Painter.fill( this, 2, 2, SIZE-2, SIZE-2, Terrain.EMPTY );
		// Painter.fill( this, SIZE/2, SIZE/2, 3, 3, Terrain.EMPTY_SP );

		entrance = SIZE * getWidth() + SIZE / 2 + 1;
		map[entrance] = Terrain.ENTRANCE;

		pedestal = (SIZE / 2 + 1) * (getWidth() + 1) - 4 * getWidth();
		map[pedestal] = Terrain.PEDESTAL;
		map[pedestal - 1 - getWidth()] = map[pedestal + 1 - getWidth()] = map[pedestal
				- 1 + getWidth()] = map[pedestal + 1 + getWidth()] = Terrain.STATUE_SP;

		exit = pedestal;

		int pos = pedestal;

		map[pos - getWidth()] = map[pos - 1] = map[pos + 1] = map[pos - 2] = map[pos + 2] = Terrain.WATER;
		pos += getWidth();
		map[pos] = map[pos - 2] = map[pos + 2] = map[pos - 3] = map[pos + 3] = Terrain.WATER;
		pos += getWidth();
		map[pos - 3] = map[pos - 2] = map[pos - 1] = map[pos] = map[pos + 1] = map[pos + 2] = map[pos + 3] = Terrain.WATER;
		pos += getWidth();
		map[pos - 2] = map[pos + 2] = Terrain.WATER;

		for (int i=0; i < getLength(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			}
		}

		feeling = Feeling.NONE;
		viewDistance = 8;

		return true;
	}

	@Override
	protected void createMobs() {
	}

	@Override
	protected void createItems() {
		drop(new PuddingCup(), pedestal);
	}

	@Override
	public int randomRespawnCell() {
		return -1;
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
		super.addVisuals(scene);
		HallsLevel.addVisuals(this, scene);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		for (int i = 0; i < getLength(); i++) {
			int flags = Terrain.flags[map[i]];
			if ((flags & Terrain.PIT) != 0) {
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
	}
}
