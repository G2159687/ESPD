package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class PlatformRoom extends StandardRoom {

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
		return new float[]{6, 3, 1};
	}

	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );

		Painter.fill( level, this, 1, Terrain.CHASM );

		ArrayList<Rect> platforms = new ArrayList<>();
		splitPlatforms( new Rect(left+2, top+2, right-2, bottom-2), platforms);

		for (Rect platform : platforms){
			Painter.fill( level, platform.left, platform.top, platform.width()+1, platform.height()+1, Terrain.EMPTY_SP);
		}

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			Painter.drawInside(level, this, door, 2, Terrain.EMPTY_SP);
		}

	}

	private void splitPlatforms( Rect curPlatform, ArrayList<Rect> allPlatforms ){
		int curArea = (curPlatform.width()+1) * (curPlatform.height()+1);

		//chance to split scales between 0% and 100% between areas of 25 and 36
		if (Random.Float() < (curArea-25)/11f){
			if (curPlatform.width() > curPlatform.height() ||
					(curPlatform.width() == curPlatform.height() && Random.Int(2) == 0)){

				//split the platform
				int splitX = Random.IntRange( curPlatform.left+2, curPlatform.right-2);
				splitPlatforms( new Rect( curPlatform.left, curPlatform.top, splitX-1, curPlatform.bottom) , allPlatforms);
				splitPlatforms( new Rect( splitX+1, curPlatform.top, curPlatform.right, curPlatform.bottom) , allPlatforms);

				//add a bridge between
				int bridgeY = Random.NormalIntRange(curPlatform.top, curPlatform.bottom);
				allPlatforms.add( new Rect( splitX - 1, bridgeY, splitX + 1, bridgeY));

			} else {

				//split the platform
				int splitY = Random.IntRange( curPlatform.top+2, curPlatform.bottom-2);
				splitPlatforms( new Rect( curPlatform.left, curPlatform.top, curPlatform.right, splitY-1) , allPlatforms);
				splitPlatforms( new Rect( curPlatform.left, splitY+1, curPlatform.right, curPlatform.bottom) , allPlatforms);

				//add a bridge between
				int bridgeX = Random.NormalIntRange(curPlatform.left, curPlatform.right);
				allPlatforms.add( new Rect( bridgeX, splitY-1, bridgeX, splitY+1));

			}
		} else {
			allPlatforms.add(curPlatform);
		}
	}

}
