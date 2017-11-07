package com.github.epd.sprout.levels.painters;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Patch;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.standard.EmptyRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public abstract class RegularPainter extends Painter {

	private float waterFill = 0f;
	private int waterSmoothness;

	public RegularPainter setWater(float fill, int smoothness){
		waterFill = fill;
		waterSmoothness = smoothness;
		return this;
	}

	private float grassFill = 0f;
	private int grassSmoothness;

	public RegularPainter setGrass(float fill, int smoothness){
		grassFill = fill;
		grassSmoothness = smoothness;
		return this;
	}

	private int nTraps = 0;
	private float[] trapChances;

	@Override
	public boolean paint(Level level, ArrayList<Room> rooms) {

		//painter can be used without rooms
		if (rooms != null) {
			int leftMost = Integer.MAX_VALUE, topMost = Integer.MAX_VALUE;

			for (Room r : rooms) {
				if (r.left < leftMost) leftMost = r.left;
				if (r.top < topMost) topMost = r.top;
			}

			//subtract 1 for padding
			leftMost--;
			topMost--;

			int rightMost = 0, bottomMost = 0;

			for (Room r : rooms) {
				r.shift(-leftMost, -topMost);
				if (r.right > rightMost) rightMost = r.right;
				if (r.bottom > bottomMost) bottomMost = r.bottom;
			}

			//add 1 for padding
			rightMost++;
			bottomMost++;

			//add 1 to account for 0 values
			level.setSize(rightMost + 1, bottomMost + 1);
		} else {
			//check if the level's size was already initialized by something else
			if (level.getLength() == 0) return false;

			//easier than checking for null everywhere
			rooms = new ArrayList<>();
		}

		for (Room r : rooms) {
			placeDoors( r );
			r.paint( level );
		}

		for (Room r : rooms) {
			paintDoors( level, r );
		}

		if (waterFill > 0f) {
			paintWater( level, rooms );
		}

		if (grassFill > 0f){
			paintGrass( level, rooms );
		}

		if (nTraps > 0){
			paintTraps( level, rooms );
		}

		decorate( level, rooms );

		return true;
	}

	protected abstract void decorate(Level level, ArrayList<Room> rooms);

	private void placeDoors( Room r ) {
		for (Room n : r.connected.keySet()) {
			Room.Door door = r.connected.get( n );
			if (door == null) {

				Rect i = r.intersect( n );
				ArrayList<Point> doorSpots = new ArrayList<>();
				for (Point p : i.getPoints()){
					if (r.canConnect(p) && n.canConnect(p))
						doorSpots.add(p);
				}
				door = new Room.Door(Random.element(doorSpots));

				r.connected.put( n, door );
				n.connected.put( r, door );
			}
		}
	}

	protected void paintDoors( Level l, Room r ) {
		for (Room n : r.connected.keySet()) {

			if (joinRooms( l, r, n )) {
				continue;
			}

			Room.Door d = r.connected.get( n );
			int door = d.x + d.y * l.getWidth();

			switch (d.type) {
				case EMPTY:
					l.map[door] = Terrain.EMPTY;
					break;
				case TUNNEL:
					l.map[door] =  l.tunnelTile();
					break;
				case REGULAR:
					if (Dungeon.depth <= 1) {
						l.map[door] = Terrain.DOOR;
					} else {
						boolean secret = (Dungeon.depth < 6 ? Random.Int( 12 - Dungeon.depth ) : Random.Int( 6 )) == 0;
						l.map[door] = secret ? Terrain.SECRET_DOOR : Terrain.DOOR;
					}
					break;
				case UNLOCKED:
					l.map[door] = Terrain.DOOR;
					break;
				case HIDDEN:
					l.map[door] = Terrain.SECRET_DOOR;
					break;
				case BARRICADE:
					l.map[door] = Terrain.BARRICADE;
					break;
				case LOCKED:
					l.map[door] = Terrain.LOCKED_DOOR;
					break;
			}
		}
	}

	protected boolean joinRooms( Level l, Room r, Room n ) {

		if (!(r instanceof EmptyRoom && n instanceof EmptyRoom)) {
			return false;
		}

		Rect w = r.intersect( n );
		if (w.left == w.right) {

			if (w.bottom - w.top < 3) {
				return false;
			}

			if (w.height()+1 == Math.max( r.height(), n.height() )) {
				return false;
			}

			if (r.width() + n.width() > 10) {
				return false;
			}

			w.top += 1;
			w.bottom -= 0;

			w.right++;

			Painter.fill( l, w.left, w.top, 1, w.height(), Terrain.EMPTY );

		} else {

			if (w.right - w.left < 3) {
				return false;
			}

			if (w.width()+1 == Math.max( r.width(), n.width() )) {
				return false;
			}

			if (r.height() + n.height() > 10) {
				return false;
			}

			w.left += 1;
			w.right -= 0;

			w.bottom++;

			Painter.fill( l, w.left, w.top, w.width(), 1, Terrain.EMPTY );
		}

		return true;
	}

	protected void paintWater( Level l, ArrayList<Room> rooms ){
		boolean[] lake = Patch.generate( l.getWidth(), l.getHeight(), waterFill, waterSmoothness, true );

		if (!rooms.isEmpty()){
			for (Room r : rooms){
				for (Point p : r.terrainModifiablePoints()){
					int i = l.pointToCell(p);
					if (lake[i] && l.map[i] == Terrain.EMPTY){
						l.map[i] = Terrain.WATER;
					}
				}
			}
		} else {
			for (int i = 0; i < l.getLength(); i ++) {
				if (lake[i] && l.map[i] == Terrain.EMPTY){
					l.map[i] = Terrain.WATER;
				}
			}
		}

	}

	protected void paintGrass( Level l, ArrayList<Room> rooms ) {
		boolean[] grass = Patch.generate( l.getWidth(), l.getHeight(), grassFill, grassSmoothness, true );

		ArrayList<Integer> grassCells = new ArrayList<>();

		if (!rooms.isEmpty()){
			for (Room r : rooms){
				for (Point p : r.terrainModifiablePoints()){
					int i = l.pointToCell(p);
					if (grass[i] && l.map[i] == Terrain.EMPTY){
						grassCells.add(i);
					}
				}
			}
		} else {
			for (int i = 0; i < l.getLength(); i ++) {
				if (grass[i] && l.map[i] == Terrain.EMPTY){
					grassCells.add(i);
				}
			}
		}

		//Adds chaos to grass height distribution. Ratio of high grass depends on fill and smoothing
		//Full range is 8.3% to 75%, but most commonly (20% fill with 3 smoothing) is around 60%
		//low smoothing, or very low fill, will begin to push the ratio down, normally to 50-30%
		for (int i : grassCells) {
			int count = 1;
			for (int n : PathFinder.NEIGHBOURS8) {
				if (grass[i + n]) {
					count++;
				}
			}
			l.map[i] = (Random.Float() < count / 12f) ? Terrain.HIGH_GRASS : Terrain.GRASS;
		}
	}

	protected void paintTraps( Level l, ArrayList<Room> rooms ) {
		ArrayList<Integer> validCells = new ArrayList<>();

		if (!rooms.isEmpty()){
			for (Room r : rooms){
				for (Point p : r.trapPlaceablePoints()){
					int i = l.pointToCell(p);
					if (l.map[i] == Terrain.EMPTY){
						validCells.add(i);
					}
				}
			}
		} else {
			for (int i = 0; i < l.getLength(); i ++) {
				if (l.map[i] == Terrain.EMPTY){
					validCells.add(i);
				}
			}
		}

		//no more than one trap every 5 valid tiles.
		nTraps = Math.min(nTraps, validCells.size()/5);

		for (int i = 0; i < nTraps; i++) {

			int trapPos = Random.Int(l.getLength());

			if (Dungeon.level.map[trapPos] == Terrain.EMPTY) {
				switch (Random.chances(trapChances)) {
					case 0:
						Dungeon.level.map[trapPos] = Terrain.SECRET_TOXIC_TRAP;
						break;
					case 1:
						Dungeon.level.map[trapPos] = Terrain.SECRET_FIRE_TRAP;
						break;
					case 2:
						Dungeon.level.map[trapPos] = Terrain.SECRET_PARALYTIC_TRAP;
						break;
					case 3:
						Dungeon.level.map[trapPos] = Terrain.SECRET_POISON_TRAP;
						break;
					case 4:
						Dungeon.level.map[trapPos] = Terrain.SECRET_ALARM_TRAP;
						break;
					case 5:
						Dungeon.level.map[trapPos] = Terrain.SECRET_LIGHTNING_TRAP;
						break;
					case 6:
						Dungeon.level.map[trapPos] = Terrain.SECRET_GRIPPING_TRAP;
						break;
					case 7:
						Dungeon.level.map[trapPos] = Terrain.SECRET_SUMMONING_TRAP;
						break;
				}
			}
		}
	}

}
