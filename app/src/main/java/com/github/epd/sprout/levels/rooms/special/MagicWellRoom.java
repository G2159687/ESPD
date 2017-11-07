package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.blobs.WaterOfAwareness;
import com.github.epd.sprout.actors.blobs.WaterOfHealth;
import com.github.epd.sprout.actors.blobs.WaterOfTransmutation;
import com.github.epd.sprout.actors.blobs.WellWater;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class MagicWellRoom extends SpecialRoom {

	private static final Class<?>[] WATERS =
			{WaterOfAwareness.class, WaterOfHealth.class, WaterOfTransmutation.class};

	public Class<?extends WellWater> overrideWater = null;

	public void paint( Level level ) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Point c = center();
		Painter.set( level, c.x, c.y, Terrain.WELL );

		@SuppressWarnings("unchecked")
		Class<? extends WellWater> waterClass =
				overrideWater != null ?
						overrideWater :
						(Class<? extends WellWater>)Random.element( WATERS );

		if (waterClass == WaterOfTransmutation.class) {
			SpecialRoom.disableGuaranteedWell();
		}

		WellWater water = (WellWater)level.blobs.get( waterClass );
		if (water == null) {
			try {
				water = waterClass.newInstance();
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
				return;
			}
		}

		water.seed(level, c.x + level.getWidth() * c.y, 1);
		level.blobs.put(waterClass, water);

		entrance().set( Door.Type.REGULAR );
	}
}
