
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Bones;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.SanChikarahDeath;
import com.github.epd.sprout.levels.builders.BranchesBuilder;
import com.github.epd.sprout.levels.builders.Builder;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.painters.SewerPainter;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.standard.EntranceRoom;
import com.github.epd.sprout.levels.rooms.standard.ExitRoom;
import com.github.epd.sprout.levels.rooms.standard.GrassyGraveRoom;
import com.github.epd.sprout.messages.Messages;
import com.watabou.noosa.Scene;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CatacombLevel extends RegularLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
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
	protected int standardRooms() {
		//2 to 4, average 3
		return 25 + 3 * Random.chances(new float[]{1, 1, 1});
	}

	@Override
	protected boolean build() {
		feeling = Feeling.NONE;
		if (super.build()) {

			for (int i = 0; i < getLength(); i++) {
				if (map[i] == Terrain.SECRET_DOOR) {
					map[i] = Terrain.DOOR;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		initRooms.add(new EntranceRoom());
		initRooms.add(new ExitRoom());

		int standards = standardRooms();
		for (int i = 0; i < standards; i++) {
			initRooms.add(new GrassyGraveRoom());
		}
		return initRooms;
	}

	@Override
	protected Builder builder() {
		return new BranchesBuilder();
	}

	@Override
	protected Painter painter() {
		return new SewerPainter()
				.setWater(0.10f, 4)
				.setGrass(0.10f, 3);
	}

	public int nMobs() {
		return 10;
	}

	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = pointToCell(roomEntrance.random());
			} while (pos == entrance);
			drop(item, pos).type = Heap.Type.REMAINS;
		}

		for (int i = 0; i < getLength(); i++) {

			if (map[i] == Terrain.ENTRANCE) {
				map[i] = Terrain.PEDESTAL;
			}
			if (map[i] == Terrain.EXIT) {
				map[i] = Terrain.PEDESTAL;
				drop(new SanChikarahDeath(), i);
			}
			if (map[i] == Terrain.CHASM) {
				map[i] = Terrain.EMPTY;
			}

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
			default:
				return super.tileDesc(tile);
		}
	}

	@Override
	public void addVisuals(Scene scene) {
		super.addVisuals(scene);
		SewerLevel.addVisuals(this, scene);
	}
}
