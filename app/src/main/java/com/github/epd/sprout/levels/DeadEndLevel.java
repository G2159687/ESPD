
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.messages.Messages;

import java.util.Arrays;

public class DeadEndLevel extends Level {

	private static final int SIZE = 5;

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	@Override
	protected boolean build() {

		setSize(7, 7);

		Arrays.fill(map, Terrain.WALL);

		for (int i = 2; i < SIZE; i++) {
			for (int j = 2; j < SIZE; j++) {
				map[i * getWidth() + j] = Terrain.EMPTY;
			}
		}

		for (int i = 1; i <= SIZE; i++) {
			map[getWidth() + i] = map[getWidth() * SIZE + i] = map[getWidth() * i + 1] = map[getWidth()
					* i + SIZE] = Terrain.WATER;
		}

		entrance = SIZE * getWidth() + SIZE / 2 + 1;
		map[entrance] = Terrain.ENTRANCE;

		exit = 0;

		return true;
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
	protected void createMobs() {
	}

	@Override
	protected void createItems() {
	}

	@Override
	public int randomRespawnCell() {
		return entrance - getWidth();
	}

}
