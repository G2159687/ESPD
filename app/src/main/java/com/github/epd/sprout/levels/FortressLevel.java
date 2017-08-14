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
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.mobs.npcs.Tinkerer3;
import com.github.epd.sprout.items.Mushroom;
import com.github.epd.sprout.items.Rice;
import com.github.epd.sprout.items.SanChikarahLife;
import com.github.epd.sprout.levels.Room.Type;
import com.github.epd.sprout.messages.Messages;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class FortressLevel extends RegularLevel {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
		cleared=true;
	}

	protected static final int REGROW_TIMER = 4;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}

	@Override
	protected boolean[] water() {
		return Patch.generate(feeling == Feeling.WATER ? 0.65f : 0.45f, 4);
	}

	@Override
	protected boolean[] grass() {
		return Patch.generate(feeling == Feeling.GRASS ? 0.60f : 0.40f, 3);
	}

	
	@Override
	protected boolean assignRoomType() {
		
		specialsf = new ArrayList<Room.Type>(Room.SPECIALSFORT);

		  
		  int specialRooms = 0;

			for (Room r : rooms) {
				if (r.type == Type.NULL && r.connected.size() == 1) {

					if (specialsf.size() > 0 && r.width() > 3 && r.height() > 3
							//&& Random.Int(specialRooms * specialRooms + 2) == 0
							) {

						
						int n = specialsf.size();
						r.type = specialsf.get(Math.min(Random.Int(n),Random.Int(n)));
						
						Room.useType(r.type);
						//specialsf.remove(r.type);
						specialRooms++;

					} else if (Random.Int(2) == 0) {

						HashSet<Room> neigbours = new HashSet<Room>();
						for (Room n : r.neigbours) {
							if (!r.connected.containsKey(n)
									&& !Room.SPECIALSFORT.contains(n.type)
									&& n.type != Type.PIT) {

								neigbours.add(n);
							}
						}
						if (neigbours.size() > 1) {
							r.connect(Random.element(neigbours));
						}
					}
				}
			}

		
		int count = 0;
		for (Room r : rooms) {
			if (r.type == Type.NULL) {
				int connections = r.connected.size();
				if (connections == 0) {

				} else if (Random.Int(connections * connections) == 0) {
					r.type = Type.STANDARD;
					count++;
				} else {
					r.type = Type.STANDARD;
				}
			}
		}

		while (count < 4) {
			Room r = randomRoom(Type.TUNNEL, 1);
			if (r != null) {
				r.type = Type.STANDARD;
				count++;
			}
		}

		for (Room r : rooms) {
			if (r.type == Type.TUNNEL) {
				r.type = Type.PASSAGE;
			}
		}

		return true;
	}

	@Override
	protected void decorate() {

		for (int i = 0; i < getLength(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int(10) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			} else if (map[i] == Terrain.WALL && Random.Int(8) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}

		while (true) {
			int pos = roomEntrance.random();
			if (pos != entrance) {
				break;
			}
		}
		
				
   int length = Level.getLength();
		
		for (int i = 0; i < length; i++) {
			
					
			if (map[i]==Terrain.ENTRANCE){map[i] = Terrain.PEDESTAL;}
			if (map[i]==Terrain.EXIT){map[i] = Terrain.PEDESTAL;  if (!Dungeon.sanchikarahlife){drop(new SanChikarahLife(), i);}}
			if (map[i]==Terrain.CHASM){map[i] = Terrain.EMPTY;}
			

		}
	}

	@Override
		protected void createItems() {
		
		addItemToSpawn(new Mushroom());
		
		Tinkerer3 npc = new Tinkerer3();
		do {
			npc.pos = randomRespawnCell();
		} while (npc.pos == -1 || heaps.get(npc.pos) != null);
		mobs.add(npc);
		Actor.occupyCell(npc);

		
			super.createItems();

			spawn(this, roomEntrance);
			
	
			
		}

		public static void spawn(FortressLevel level, Room room) {
		int pos;
			do {pos = room.random();}
			while (level.heaps.get(pos) != null);
			level.drop(new Rice(), pos);
		}
		

	@Override
	public String tileName(int tile) {
		switch (tile) {
		case Terrain.WATER:
			return Messages.get(CityLevel.class,"water_name");
		case Terrain.HIGH_GRASS:
			return Messages.get(CityLevel.class,"high_grass_name");
		default:
			return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.ENTRANCE:
			return Messages.get(CityLevel.class,"entrance_desc");
		case Terrain.EXIT:
			return Messages.get(CityLevel.class,"exit_desc");
		case Terrain.WALL_DECO:
		case Terrain.EMPTY_DECO:
			return Messages.get(CityLevel.class,"deco_desc");
		case Terrain.EMPTY_SP:
			return Messages.get(CityLevel.class,"sp_desc");
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return Messages.get(CityLevel.class,"statue_desc");
		case Terrain.BOOKSHELF:
			return Messages.get(CityLevel.class,"bookshelf_desc");
		default:
			return super.tileDesc(tile);
		}
	}


}