
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.mobs.Sentinel;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.quest.SanChikarahTranscend;
import com.github.epd.sprout.items.food.PotionOfConstitution;
import com.github.epd.sprout.levels.builders.Builder;
import com.github.epd.sprout.levels.builders.LineBuilder;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.painters.PrisonPainter;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.special.MagicWellRoom;
import com.github.epd.sprout.levels.rooms.special.StatueRoom;
import com.github.epd.sprout.levels.rooms.standard.EmptyRoom;
import com.github.epd.sprout.levels.rooms.standard.EntranceRoom;
import com.github.epd.sprout.levels.rooms.standard.ExitRoom;
import com.github.epd.sprout.levels.rooms.standard.StudyRoom;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Flytrap;
import com.github.epd.sprout.plants.Phaseshift;
import com.watabou.noosa.Scene;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ChasmLevel extends RegularLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
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
	protected int standardRooms() {
		return 9;
	}

	@Override
	protected int specialRooms() {
		return 7;
	}

	@Override
	protected boolean build() {
		feeling = Feeling.CHASM;
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
		initRooms.add(roomEntrance = new EntranceRoom());
		initRooms.add(roomExit = new ExitRoom());

		int standards = standardRooms();
		for (int i = 0; i < standards; i++) {
			if (Random.Float() < 0.3f)
				initRooms.add(new StudyRoom());
			else initRooms.add(new EmptyRoom());
		}

		int specials = specialRooms();
		for (int i = 0; i < specials; i++) {
			if (Random.Int(2) == 0) {
				initRooms.add(new MagicWellRoom());
			} else {
				initRooms.add(new StatueRoom());
			}
		}

		return initRooms;
	}

	@Override
	protected Builder builder() {
		return new LineBuilder();
	}

	@Override
	protected Painter painter() {
		return new PrisonPainter()
				.setWater(0.10f, 4)
				.setGrass(0.10f, 3);
	}

	@Override
	protected void createItems() {
		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.ENTRANCE) {
				map[i] = Terrain.PEDESTAL;
			}
			if (map[i] == Terrain.EXIT) {
				map[i] = Terrain.PEDESTAL;
				drop(new SanChikarahTranscend(), i);
			}
		}

		addItemToSpawn(new Phaseshift.Seed());
		addItemToSpawn(new Phaseshift.Seed());
		addItemToSpawn(new Phaseshift.Seed());
		addItemToSpawn(new Flytrap.Seed());
		addItemToSpawn(new Flytrap.Seed());
		addItemToSpawn(new Flytrap.Seed());
		addItemToSpawn(new PotionOfConstitution());
		addItemToSpawn(Generator.random(Generator.Category.BERRY));
		addItemToSpawn(Generator.random(Generator.Category.BERRY));
		addItemToSpawn(Generator.random(Generator.Category.BERRY));

		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.EMPTY_SP && heaps.get(i) == null && Random.Float() < .05) {
				Sentinel sentinel = new Sentinel();
				sentinel.pos = i;
				mobs.add(sentinel);
			}
		}

		super.createItems();
	}

	public int nMobs() {
		return 10;
	}

	@Override
	protected void createMobs() {
		super.createMobs();

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
		PrisonLevel.addVisuals(this, scene);
	}
}
