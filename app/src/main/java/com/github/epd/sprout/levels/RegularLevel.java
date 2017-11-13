
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Bones;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.levels.builders.Builder;
import com.github.epd.sprout.levels.builders.LoopBuilder;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.special.PitRoom;
import com.github.epd.sprout.levels.rooms.special.ShopRoom;
import com.github.epd.sprout.levels.rooms.special.SpecialRoom;
import com.github.epd.sprout.levels.rooms.special.WeakFloorRoom;
import com.github.epd.sprout.levels.rooms.standard.EntranceRoom;
import com.github.epd.sprout.levels.rooms.standard.ExitRoom;
import com.github.epd.sprout.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public abstract class RegularLevel extends Level {

	protected ArrayList<Room> rooms;

	protected Builder builder;

	protected Room roomEntrance;
	protected Room roomExit;

	public int secretDoors;

	@Override
	protected boolean build() {

		builder = builder();


		ArrayList<Room> initRooms = initRooms();
		Random.shuffle(initRooms);

		do {
			for (Room r : initRooms) {
				r.neigbours.clear();
				r.connected.clear();
			}
			rooms = builder.build((ArrayList<Room>) initRooms.clone());
		} while (rooms == null);

		setPar();

		if (painter().paint(this, rooms)){
			return true;
		} else {
			return false;
		}

	}

	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		initRooms.add(roomEntrance = new EntranceRoom());
		initRooms.add(roomExit = new ExitRoom());

		int standards = standardRooms();
		for (int i = 0; i < standards; i++) {
			StandardRoom s;
			do {
				s = StandardRoom.createRoom();
			} while (!s.setSizeCat( standards-i ));
			i += s.sizeCat.roomValue-1;
			initRooms.add(s);
		}

		if (Dungeon.shopOnLevel())
			initRooms.add(new ShopRoom());

		int specials = specialRooms();
		SpecialRoom.initForFloor();
		for (int i = 0; i < specials; i++)
			initRooms.add(SpecialRoom.createRoom());

		return initRooms;
	}

	protected int standardRooms() {
		return 0;
	}

	protected int specialRooms() {
		return 0;
	}

	protected Builder builder(){
		return new LoopBuilder()
				.setLoopShape( 2 ,
						Random.Float(0.55f, 0.85f),
						Random.Float(0f, 0.5f));
	}

	protected abstract Painter painter();

	protected void placeTraps() {

		int nTraps = nTraps();
		float[] trapChances = trapChances();

		for (int i = 0; i < nTraps; i++) {

			int trapPos = Random.Int(getLength());

			if (map[trapPos] == Terrain.EMPTY || map[trapPos] == Terrain.WATER) {
				switch (Random.chances(trapChances)) {
					case 0:
						map[trapPos] = Terrain.SECRET_TOXIC_TRAP;
						break;
					case 1:
						map[trapPos] = Terrain.SECRET_FIRE_TRAP;
						break;
					case 2:
						map[trapPos] = Terrain.SECRET_PARALYTIC_TRAP;
						break;
					case 3:
						map[trapPos] = Terrain.SECRET_POISON_TRAP;
						break;
					case 4:
						map[trapPos] = Terrain.SECRET_ALARM_TRAP;
						break;
					case 5:
						map[trapPos] = Terrain.SECRET_LIGHTNING_TRAP;
						break;
					case 6:
						map[trapPos] = Terrain.SECRET_GRIPPING_TRAP;
						break;
					case 7:
						map[trapPos] = Terrain.SECRET_SUMMONING_TRAP;
						break;
				}
			} else {
				i--;
			}
		}
		buildFlagMaps();
	}

	protected int nTraps() {
		return Random.Int(1, (rooms.size() + Dungeon.depth) / 3);
	}

	protected float[] trapChances() {
		return new float[]{1, 1, 1, 1, 1, 1, 1, 1};
	}

	protected void setPar() {
		Dungeon.pars[Dungeon.depth] = 600;
	}

	@Override
	public int nMobs() {
		if (Dungeon.depth < 5 && !Statistics.amuletObtained) {
			return (10 + Dungeon.depth + Random.Int(3)) * Math.round(0.5f + 0.5f * Dungeon.mapSize);
		} else if (!Statistics.amuletObtained) {
			return (5 + Dungeon.depth % 5 + Random.Int(3)) * Math.round(0.5f + 0.5f * Dungeon.mapSize);
		} else {
			return (10 + (5 - Dungeon.depth % 5) + Random.Int(3)) * Math.round(0.5f + 0.5f * Dungeon.mapSize);
		}
	}

	@Override
	protected void createMobs() {
		//on floor 1, 10 rats are created so the player can get level 2.
		int mobsToSpawn = Dungeon.depth == 1 ? 10 : nMobs();

		ArrayList<Room> stdRooms = new ArrayList<>();
		for (Room room : rooms) {
			if (room instanceof StandardRoom && room != roomEntrance) {
				for (int i = 0; i < ((StandardRoom) room).sizeCat.roomValue; i++) {
					stdRooms.add(room);
				}
				//pre-0.6.0 save compatibility
			} else if (room.legacyType.equals("STANDARD")){
				stdRooms.add(room);
			}
		}
		Random.shuffle(stdRooms);
		Iterator<Room> stdRoomIter = stdRooms.iterator();


		while (mobsToSpawn > 0) {
			if (!stdRoomIter.hasNext())
				stdRoomIter = stdRooms.iterator();
			Room roomToSpawn = stdRoomIter.next();

			Mob mob = Bestiary.mob(Dungeon.depth);
			mob.pos = pointToCell(roomToSpawn.random());

			if (findMob(mob.pos) == null && Level.passable[mob.pos]) {
				mobsToSpawn--;
				mobs.add(mob);
				mob.originalgen = true;

				if (mobsToSpawn > 0 && Random.Int(4) == 0) {
					mob = Bestiary.mob(Dungeon.depth);
					mob.pos = pointToCell(roomToSpawn.random());

					if (findMob(mob.pos) == null && Level.passable[mob.pos]) {
						mobsToSpawn--;
						mobs.add(mob);
					}
				}
			}
		}

		for (Mob m : mobs) {
			if (map[m.pos] == Terrain.HIGH_GRASS) {
				map[m.pos] = Terrain.GRASS;
				losBlocking[m.pos] = false;
			}

		}

	}

	@Override
	public int randomRespawnCell() {
		int count = 0;
		int cell = -1;

		while (true) {

			if (++count > 30) {
				return -1;
			}

			Room room = randomRoom(StandardRoom.class);
			if (room == null || room == roomEntrance) {
				continue;
			}

			cell = pointToCell(room.random());
			if (!Dungeon.visible[cell]
					&& Actor.findChar(cell) == null
					&& Level.passable[cell]
					&& cell != exit) {
				return cell;
			}

		}
	}

	@Override
	public int randomDestination() {
		int cell = -1;
		while (true) {
			Room room = Random.element(rooms);
			if (room == null) {
				continue;
			}
			cell = pointToCell(room.random());
			if (Level.passable[cell]) {
				return cell;
			}
		}
	}

	@Override
	protected void createItems() {

		int nItems = 3;
		int bonus = 0;
		for (Buff buff : Dungeon.hero.buffs(MasterThievesArmband.Thievery.class)) {
			bonus += ((MasterThievesArmband.Thievery) buff).level();
		}
		while (Random.Float() < (0.4f + bonus * 0.01f)) {
			nItems++;
		}

		if (Dungeon.mapSize == 2){
			nItems = Math.round(1.7f * nItems);
		} else if (Dungeon.mapSize == 3){
			nItems = Math.round(2.5f * nItems);
		}

		for (int i = 0; i < nItems; i++) {
			Heap.Type type = null;
			switch (Random.Int(15)) {
				case 0:
					type = Heap.Type.SKELETON;
					break;
				case 1:
				case 2:
				case 3:
				case 4:
					type = Heap.Type.CHEST;
					break;
				case 5:
					type = Dungeon.depth > 1 ? Heap.Type.MIMIC : Heap.Type.CHEST;
					break;
				default:
					type = Heap.Type.HEAP;
			}
			drop(Generator.random(), randomDropCell()).type = type;
		}

		for (Item item : itemsToSpawn) {
			int cell = randomDropCell();
			if (item instanceof Scroll) {
				while (map[cell] == Terrain.FIRE_TRAP
						|| map[cell] == Terrain.SECRET_FIRE_TRAP) {
					cell = randomDropCell();
				}
			}
			drop(item, cell).type = Heap.Type.HEAP;
		}

		Item item = Bones.get();
		if (item != null) {
			drop(item, randomDropCell()).type = Heap.Type.REMAINS;
		}

		placeTraps();
	}

	protected Room randomRoom(Class<? extends Room> type) {
		Collections.shuffle(rooms);
		for (Room r : rooms) {
			if (type.isInstance(r)
					//compatibility with pre-0.6.0 saves
					|| (type == StandardRoom.class && r.legacyType.equals("STANDARD"))) {
				return r;
			}
		}
		return null;
	}

	public Room room(int pos) {
		for (Room room : rooms) {
			if (room.inside(cellToPoint(pos))) {
				return room;
			}
		}

		return null;
	}

	protected int randomDropCell() {
		while (true) {
			Room room = randomRoom(StandardRoom.class);
			if (room != null && room != roomEntrance) {
				int pos = pointToCell(room.random());
				if (passable[pos]) {
					return pos;
				}
			}
		}
	}

	@Override
	public int pitCell() {
		for (Room room : rooms) {
			if (room instanceof PitRoom || room.legacyType.equals("PIT")) {
				return pointToCell(room.random());
			}
		}

		return super.pitCell();
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (rooms != null) {
			bundle.put("rooms", rooms);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		if (bundle.contains("rooms")) {
			rooms = new ArrayList<>(
					(Collection<Room>) ((Collection<?>) bundle
							.getCollection("rooms")));
			for (Room r : rooms) {
				r.onLevelLoad(this);
				if (r instanceof WeakFloorRoom || r.legacyType.equals("WEAK_FLOOR")) {
					weakFloorCreated = true;
					break;
				}
			}
		}
	}

}
