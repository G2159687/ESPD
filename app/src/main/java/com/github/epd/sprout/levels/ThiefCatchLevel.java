
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Bones;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.levels.builders.Builder;
import com.github.epd.sprout.levels.builders.LoopBuilder;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.standard.EmptyRoom;
import com.github.epd.sprout.levels.rooms.standard.SewerBossEntranceRoom;
import com.github.epd.sprout.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ThiefCatchLevel extends CavesLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		initRooms.add ( roomEntrance = roomExit = new SewerBossEntranceRoom());

		int standards = standardRooms();
		for (int i = 0; i < standards; i++) {
			initRooms.add(new EmptyRoom());
		}
		return initRooms;
	}

	@Override
	protected int standardRooms() {
		//2 to 4, average 3
		return 4+Random.chances(new float[]{1, 1, 1});
	}

	protected Builder builder(){
		return new LoopBuilder()
				.setPathLength(1f, new float[]{1})
				.setTunnelLength(new float[]{1}, new float[]{1});
	}

	protected int nTraps() {
		return 0;
	}

	@Override
	protected void createMobs() {
		Mob mob = Bestiary.mob( Dungeon.depth );
		Room room;
		do {
			room = randomRoom(StandardRoom.class);
		} while (room == roomEntrance);
		mob.pos = pointToCell(room.random());
		mobs.add( mob );
	}

	@Override
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		addItemToSpawn(Generator.random(Generator.Category.ARTIFACT));
		addItemToSpawn(Generator.random(Generator.Category.WAND));
		addItemToSpawn(Generator.random(Generator.Category.RING));
		addItemToSpawn(Generator.random(Generator.Category.SEEDRICH));
		if (Random.Int(10) == 0 && !Dungeon.limitedDrops.armband.dropped()){
			addItemToSpawn(new MasterThievesArmband());
			Dungeon.limitedDrops.armband.drop();
		}
		for (int i = 0; i < 10; i++) {
			if (Random.Int(5) == 0) {
				switch (Random.Int(3)) {
					case 0:
						addItemToSpawn(Generator.random(Generator.Category.ARTIFACT));
					case 1:
						addItemToSpawn(Generator.random(Generator.Category.WAND));
					case 2:
						addItemToSpawn(Generator.random(Generator.Category.RING));
					case 3:
						addItemToSpawn(Generator.random(Generator.Category.SEEDRICH));
				}
			}
		}
		super.createItems();

		map[exit] = Terrain.WALL;
	}

	@Override
	public int randomRespawnCell() {
		int pos;
		do {
			pos = pointToCell(roomEntrance.random());
		} while (pos == entrance || solid[pos]);
		return pos;
	}
}
