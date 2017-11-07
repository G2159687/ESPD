
package com.github.epd.sprout.effects;

import com.github.epd.sprout.DungeonTilemap;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class CheckedCell extends Image {

	private float alpha;

	public CheckedCell(int pos) {
		super(TextureCache.createSolid(0xFF55AAFF));

		origin.set(0.5f);

		point(DungeonTilemap.tileToWorld(pos).offset(DungeonTilemap.SIZE / 2,
				DungeonTilemap.SIZE / 2));

		alpha = 0.8f;
	}

	@Override
	public void update() {
		if ((alpha -= Game.elapsed) > 0) {
			alpha(alpha);
			scale.set(DungeonTilemap.SIZE * alpha);
		} else {
			killAndErase();
		}
	}
}
