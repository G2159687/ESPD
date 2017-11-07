package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Random;

public class GrassyGraveRoom extends StandardRoom {

	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		Painter.fill( level, this, 1 , Terrain.GRASS );

		int w = width() - 2;
		int h = height() - 2;
		int nGraves = Math.max( w, h ) / 2;

		int index = Random.Int( nGraves );

		int shift = Random.Int( 2 );
		for (int i=0; i < nGraves; i++) {
			int pos = w > h ?
					left + 1 + shift + i * 2 + (top + 2 + Random.Int( h-2 )) * level.getWidth() :
					(left + 2 + Random.Int( w-2 )) + (top + 1 + shift + i * 2) * level.getWidth();
			level.drop( i == index ? Generator.random() : new Gold().random(), pos ).type = Heap.Type.TOMB;
		}
	}
}
