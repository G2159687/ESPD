
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.ParalyticGas;
import com.github.epd.sprout.scenes.GameScene;

public class ParalyticTrap {

	// 0xCCCC55

	public static void trigger(int pos, Char ch) {

		GameScene.add(Blob
				.seed(pos, 80 + 5 * Dungeon.depth, ParalyticGas.class));

	}
}
