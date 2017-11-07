
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Bones;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.builders.Builder;
import com.github.epd.sprout.levels.builders.LoopBuilder;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.special.RatKingRoom;
import com.github.epd.sprout.levels.rooms.standard.EmptyRoom;
import com.github.epd.sprout.levels.rooms.standard.SewerBossEntranceRoom;
import com.github.epd.sprout.levels.rooms.standard.StandardRoom;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SewerBossLevel extends SewerLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	private int stairs = 0;

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		initRooms.add ( roomEntrance = roomExit = new SewerBossEntranceRoom());

		int standards = standardRooms();
		for (int i = 0; i < standards; i++) {
			initRooms.add(new EmptyRoom());
		}

		initRooms.add(new RatKingRoom());
		return initRooms;
	}

	@Override
	protected int standardRooms() {
		//2 to 4, average 3
		return 2+Random.chances(new float[]{1, 1, 1});
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
	protected float waterFill() {
		return 0.50f;
	}

	@Override
	protected int waterSmoothing() {
		return 5;
	}

	@Override
	protected float grassFill() {
		return 0.20f;
	}

	@Override
	protected int grassSmoothing() {
		return 4;
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
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = pointToCell(roomEntrance.random());
			} while (pos == entrance || solid[pos]);
			drop( item, pos ).type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell() {
		int pos;
		do {
			pos = pointToCell(roomEntrance.random());
		} while (pos == entrance || solid[pos]);
		return pos;
	}

	public void seal() {
		if (entrance != 0) {

			locked = true;

			set(entrance, Terrain.WATER_TILES);
			GameScene.updateMap(entrance);
			GameScene.ripple(entrance);

			stairs = entrance;
			entrance = 0;
		}
	}

	public void unseal() {
		if (stairs != 0) {

			locked = false;

			entrance = stairs;
			stairs = 0;

			set(entrance, Terrain.ENTRANCE);
			GameScene.updateMap(entrance);

		}
	}

	private static final String STAIRS = "stairs";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STAIRS, stairs);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		stairs = bundle.getInt(STAIRS);
	}
}
