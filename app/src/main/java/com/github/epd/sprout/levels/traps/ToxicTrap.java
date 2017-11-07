
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.scenes.GameScene;

public class ToxicTrap {

	// 0x40CC55

	public static void trigger(int pos, Char ch) {

		GameScene.add(Blob.seed(pos, 300 + 20 * Dungeon.depth, ToxicGas.class));

	}
}
