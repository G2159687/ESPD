package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class StudyRoom extends StandardRoom {

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
		return new float[]{2, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.BOOKSHELF );
		Painter.fill( level, this, 2 , Terrain.EMPTY_SP );

		for (Door door : connected.values()) {
			Painter.drawInside(level, this, door, 2, Terrain.EMPTY_SP);
			door.set( Door.Type.REGULAR );
		}

		if (sizeCat == SizeCategory.LARGE){
			int pillarW = (width()-7)/2;
			int pillarH = (height()-7)/2;

			Painter.fill(level, left+3, top+3, pillarW, 1, Terrain.BOOKSHELF);
			Painter.fill(level, left+3, top+3, 1, pillarH, Terrain.BOOKSHELF);

			Painter.fill(level, left+3, bottom-2-1, pillarW, 1, Terrain.BOOKSHELF);
			Painter.fill(level, left+3, bottom-2-pillarH, 1, pillarH, Terrain.BOOKSHELF);

			Painter.fill(level, right-2-pillarW, top+3, pillarW, 1, Terrain.BOOKSHELF);
			Painter.fill(level, right-2-1, top+3, 1, pillarH, Terrain.BOOKSHELF);

			Painter.fill(level, right-2-pillarW, bottom-2-1, pillarW, 1, Terrain.BOOKSHELF);
			Painter.fill(level, right-2-1, bottom-2-pillarH, 1, pillarH, Terrain.BOOKSHELF);
		}

		Point center = center();
		Painter.set( level, center, Terrain.PEDESTAL );

		Item prize = level.findPrizeItem();

		if (prize != null) {
			level.drop(prize, (center.x + center.y * level.getWidth()));
		} else {
			level.drop(Generator.random( Random.oneOf(
					Generator.Category.POTION,
					Generator.Category.SCROLL)), (center.x + center.y * level.getWidth()));
		}
	}
}
