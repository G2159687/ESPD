package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Random;

public class PillarsRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 7);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 7);
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{9, 3, 1};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		int minDim = Math.min(width(), height());

		//2 pillars
		if (minDim == 7 || Random.Int(2) == 0){

			int pillarInset = minDim >= 11 ? 2 : 1;
			int pillarSize = ((minDim-3)/2) - pillarInset;

			int pillarX, pillarY;
			if (Random.Int(2) == 0) {
				pillarX = Random.IntRange(left + 1 + pillarInset, right - pillarSize - pillarInset);
				pillarY = top + 1 + pillarInset;
			} else {
				pillarX = left + 1 + pillarInset;
				pillarY = Random.IntRange(top + 1 + pillarInset, bottom - pillarSize - pillarInset);
			}

			//first pillar
			Painter.fill(level, pillarX, pillarY, pillarSize, pillarSize, Terrain.WALL);

			//invert for second pillar
			pillarX = right - (pillarX - left + pillarSize - 1);
			pillarY = bottom - (pillarY - top + pillarSize - 1);
			Painter.fill(level, pillarX, pillarY, pillarSize, pillarSize, Terrain.WALL);

			//4 pillars
		} else {

			int pillarInset = minDim >= 12 ? 2 : 1;
			int pillarSize = (minDim - 6)/(pillarInset + 1);

			float xSpaces = width() - 2*pillarInset - pillarSize - 2;
			float ySpaces = height() - 2*pillarInset - pillarSize - 2;
			float minSpaces = Math.min(xSpaces, ySpaces);

			float percentSkew = Math.round(Random.Float() * minSpaces) / minSpaces;

			//top-left, skews right
			Painter.fill(level, left + 1 + pillarInset + Math.round(percentSkew*xSpaces), top + 1 + pillarInset, pillarSize, pillarSize, Terrain.WALL);

			//top-right, skews down
			Painter.fill(level, right - pillarSize - pillarInset, top + 1 + pillarInset + Math.round(percentSkew*ySpaces), pillarSize, pillarSize, Terrain.WALL);

			//bottom-right, skews left
			Painter.fill(level, right - pillarSize - pillarInset - Math.round(percentSkew*xSpaces), bottom - pillarSize - pillarInset, pillarSize, pillarSize, Terrain.WALL);

			//bottom-left, skews up
			Painter.fill(level, left + 1 + pillarInset, bottom - pillarSize - pillarInset - Math.round(percentSkew*ySpaces), pillarSize, pillarSize, Terrain.WALL);

		}
	}
}
