
package com.github.epd.sprout.items.keys;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class GoldenSkeletonKey extends Key {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.GOLDEN_KEY;
	}

	public GoldenSkeletonKey() {
		this(0);
	}

	public GoldenSkeletonKey(int depth) {
		super();
		this.depth = depth;
	}

	private static final Glowing WHITE = new Glowing(0xFFFFCC);

	@Override
	public Glowing glowing() {
		return WHITE;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
