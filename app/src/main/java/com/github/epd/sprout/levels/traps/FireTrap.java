
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.Fire;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.scenes.GameScene;

public class FireTrap {

	// 0xFF7708

	public static void trigger(int pos, Char ch) {

		GameScene.add(Blob.seed(pos, 2, Fire.class));
		CellEmitter.get(pos).burst(FlameParticle.FACTORY, 5);

	}
}
