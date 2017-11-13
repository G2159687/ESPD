
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Bones;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.misc.Rice;
import com.github.epd.sprout.items.quest.SanChikarahLife;
import com.github.epd.sprout.levels.builders.BranchesBuilder;
import com.github.epd.sprout.levels.builders.Builder;
import com.github.epd.sprout.levels.painters.CityPainter;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.special.FoliageRoom;
import com.github.epd.sprout.levels.rooms.standard.EntranceRoom;
import com.github.epd.sprout.levels.rooms.standard.ExitRoom;
import com.github.epd.sprout.levels.rooms.standard.GardenRoom;
import com.github.epd.sprout.levels.rooms.standard.StripedRoom;
import com.github.epd.sprout.messages.Messages;
import com.watabou.noosa.Scene;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class FortressLevel extends RegularLevel {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
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
	protected int standardRooms() {
		switch (Dungeon.mapSize){
			case 1:
				return 10 + Random.chances(new float[]{4, 2, 1});
			case 2:
				return 15 + Random.chances(new float[]{4, 2, 1});
			case 3:
				return 20 + Random.chances(new float[]{4, 2, 1});
			default:
				return 5 + Random.chances(new float[]{4, 2, 1});
		}
	}

	@Override
	protected int specialRooms() {
		switch (Dungeon.mapSize){
			case 1:
				return 6 + Random.chances(new float[]{4, 2, 1});
			case 2:
				return 8 + Random.chances(new float[]{4, 2, 1});
			case 3:
				return 10 + Random.chances(new float[]{4, 2, 1});
			default:
				return 5 + Random.chances(new float[]{4, 2, 1});
		}
	}

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		initRooms.add(roomEntrance = new EntranceRoom());
		initRooms.add(roomExit = new ExitRoom());

		int standards = standardRooms();
		for (int i = 0; i < standards; i++) {
			if (Random.Float() < 0.3f)
				initRooms.add(new StripedRoom());
			else initRooms.add(new GardenRoom());
		}

		int specials = specialRooms();
		for (int i = 0; i < specials; i++) {
			initRooms.add(new FoliageRoom());
		}

		return initRooms;
	}

	@Override
	protected Builder builder() {
		return new BranchesBuilder();
	}

	@Override
	protected Painter painter() {
		return new CityPainter()
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

		int ricepos;
		do {
			ricepos = pointToCell(roomEntrance.random());
		} while (ricepos == entrance);
		drop(new Rice(), ricepos);

		for (int i = 0; i < getLength(); i++) {

			if (map[i] == Terrain.ENTRANCE) {
				map[i] = Terrain.PEDESTAL;
			}
			if (map[i] == Terrain.EXIT) {
				map[i] = Terrain.PEDESTAL;
				drop(new SanChikarahLife(), i);
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
		CityLevel.addVisuals(this, scene);
	}
}
