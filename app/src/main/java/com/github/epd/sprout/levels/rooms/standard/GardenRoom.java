package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.plants.Plant;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class GardenRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 5);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 5);
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{3, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.GRASS );
		Painter.fill( level, this, 2, Terrain.HIGH_GRASS );

		if (Math.min(width(), height()) >= 7){
			Painter.fill( level, this, 3, Terrain.GRASS );
		}

		Point center = center();

		//place at least 2 plants for rooms with at least 9 in one dimensions
		if (Math.max(width(), height()) >= 9){

			//place 4 plants for very large rooms
			if (Math.min(width(), height()) >= 11) {
				Painter.drawLine(level, new Point(left+2, center.y), new Point(right-2, center.y), Terrain.HIGH_GRASS);
				Painter.drawLine(level, new Point(center.x, top+2), new Point(center.x, bottom-2), Terrain.HIGH_GRASS);
				level.plant( randomSeed(), level.pointToCell(new Point(center.x-1, center.y-1)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x+1, center.y-1)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x-1, center.y+1)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x+1, center.y+1)));

				//place 2 plants otherwise
				//left/right
			} else if (width() > height() || (width() == height() && Random.Int(2) == 0)){
				Painter.drawLine(level, new Point(center.x, top+2), new Point(center.x, bottom-2), Terrain.HIGH_GRASS);
				level.plant( randomSeed(), level.pointToCell(new Point(center.x-1, center.y)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x+1, center.y)));

				//top/bottom
			} else {
				Painter.drawLine(level, new Point(left+2, center.y), new Point(right-2, center.y), Terrain.HIGH_GRASS);
				level.plant( randomSeed(), level.pointToCell(new Point(center.x, center.y-1)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x, center.y+1)));

			}

			//place just one plant for smaller sized rooms
		} else {
			level.plant( randomSeed(), level.pointToCell(center));
		}

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		super.paint(level);
	}

	private static Plant.Seed randomSeed(){
		return (Plant.Seed) Generator.random(Generator.Category.REALSEED);
	}
}
