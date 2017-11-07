
package com.github.epd.sprout.effects;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.blobs.Blob;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class BlobEmitter extends Emitter {

	private Blob blob;

	public BlobEmitter(Blob blob) {

		super();

		this.blob = blob;
		blob.use(this);
	}

	@Override
	protected void emit(int index) {

		if (blob.volume <= 0) {
			return;
		}

		if (blob.area.isEmpty())
			blob.setupArea();

		int[] map = blob.cur;
		float size = DungeonTilemap.SIZE;

		int cell;
		for (int i = blob.area.left; i < blob.area.right; i++) {
			for (int j = blob.area.top; j < blob.area.bottom; j++) {
				cell = i + j * Dungeon.level.getWidth();
				if (map[cell] > 0 && Dungeon.visible[cell]) {
					float x = (i + Random.Float()) * size;
					float y = (j + Random.Float()) * size;
					factory.emit(this, index, x, y);
				}
			}
		}
	}
}
