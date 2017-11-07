package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Patch;
import com.github.epd.sprout.utils.BArray;
import com.watabou.utils.PathFinder;

//This room type uses the patch system to fill itself in in some manner
//it's still up to the specific room to implement paint, but utility methods are provided
public abstract class PatchRoom extends StandardRoom {

	protected boolean[] patch;

	protected void setupPatch(Level level, float fill, int clustering, boolean ensurePath){

		if (ensurePath){
			PathFinder.setMapSize(width()-2, height()-2);
			boolean valid;
			do {
				patch = Patch.generate(width()-2, height()-2, fill, clustering, true);
				int startPoint = 0;
				for (Door door : connected.values()) {
					if (door.x == left) {
						startPoint = xyToPatchCoords(door.x + 1, door.y);
						patch[xyToPatchCoords(door.x + 1, door.y)] = false;
						patch[xyToPatchCoords(door.x + 2, door.y)] = false;
					} else if (door.x == right) {
						startPoint = xyToPatchCoords(door.x - 1, door.y);
						patch[xyToPatchCoords(door.x - 1, door.y)] = false;
						patch[xyToPatchCoords(door.x - 2, door.y)] = false;
					} else if (door.y == top) {
						startPoint = xyToPatchCoords(door.x, door.y + 1);
						patch[xyToPatchCoords(door.x, door.y + 1)] = false;
						patch[xyToPatchCoords(door.x, door.y + 2)] = false;
					} else if (door.y == bottom) {
						startPoint = xyToPatchCoords(door.x, door.y - 1);
						patch[xyToPatchCoords(door.x, door.y - 1)] = false;
						patch[xyToPatchCoords(door.x, door.y - 2)] = false;
					}
				}

				PathFinder.buildDistanceMap(startPoint, BArray.not(patch, null));

				valid = true;
				for (int i = 0; i < patch.length; i++){
					if (!patch[i] && PathFinder.distance[i] == Integer.MAX_VALUE){
						valid = false;
						break;
					}
				}
			} while (!valid);
			PathFinder.setMapSize(level.getWidth(), level.getHeight());
		} else {
			patch = Patch.generate(width()-2, height()-2, fill, clustering, true);
		}
	}

	//removes all diagonal-only adjacent filled patch areas, handy for making things look cleaner
	//note that this will reduce the fill rate very slightly
	protected void cleanDiagonalEdges(){
		if (patch == null) return;

		int pWidth = width()-2;

		for (int i = 0; i < patch.length - pWidth; i++){
			if (!patch[i]) continue;

			//we don't need to check above because we are either at the top
			// or have already dealt with those tiles

			//down-left
			if (i % pWidth != 0){
				if (patch[i - 1 + pWidth] && !(patch[i - 1] || patch[i + pWidth])){
					patch[i - 1 + pWidth] = false;
				}
			}

			//down-right
			if ((i + 1) % pWidth != 0){
				if (patch[i + 1 + pWidth] && !(patch[i + 1] || patch[i + pWidth])){
					patch[i + 1 + pWidth] = false;
				}
			}

		}
	}

	protected int xyToPatchCoords(int x, int y){
		return (x-left-1) + ((y-top-1) * (width()-2));
	}
}
