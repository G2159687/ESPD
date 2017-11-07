package com.github.epd.sprout.levels.builders;

import com.github.epd.sprout.levels.rooms.Room;
import com.watabou.utils.Random;

import java.util.ArrayList;

//A builder that creates only branches, very simple and very random
public class BranchesBuilder extends RegularBuilder {

	@Override
	public ArrayList<Room> build(ArrayList<Room> rooms) {

		setupRooms( rooms );

		if (entrance == null){
			return null;
		}

		ArrayList<Room> branchable = new ArrayList<>();

		entrance.setSize();
		entrance.setPos(0, 0);
		branchable.add(entrance);

		if (shop != null){
			placeRoom(branchable, entrance, shop, Random.Float(360f));
		}

		ArrayList<Room> roomsToBranch = new ArrayList<>();
		roomsToBranch.addAll(multiConnections);
		if (exit != null) roomsToBranch.add(exit);
		roomsToBranch.addAll(singleConnections);
		createBranches(rooms, branchable, roomsToBranch, branchTunnelChances);

		findNeighbours(rooms);

		for (Room r : rooms){
			for (Room n : r.neigbours){
				if (!n.connected.containsKey(r)
						&& Random.Float() < extraConnectionChance){
					r.connect(n);
				}
			}
		}

		return rooms;
	}
}
