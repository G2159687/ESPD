
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.particles.LeafParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.scenes.GameScene;

public class Water extends Blob {

	@Override
	protected void evolve() {
		super.evolve();

		if (volume > 0) {

			boolean mapUpdated = false;

			for (int i = 0; i < Dungeon.level.getLength(); i++) {
				if (off[i] > 0) {
					int c = Dungeon.level.map[i];
					if (c == Terrain.EMPTY || c == Terrain.EMBERS
							|| c == Terrain.EMPTY_DECO) {

						Level.set(i, cur[i] > 9 ? Terrain.HIGH_GRASS
								: Terrain.GRASS);
						mapUpdated = true;

					} else if (c == Terrain.GRASS && cur[i] > 9) {

						Level.set(i, Terrain.HIGH_GRASS);
						mapUpdated = true;

					}

				}
			}

			if (mapUpdated) {
				GameScene.updateMap();
				Dungeon.observe();
			}
		}
	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);

		emitter.start(LeafParticle.LEVEL_SPECIFIC, 0.2f, 0);
	}
}
