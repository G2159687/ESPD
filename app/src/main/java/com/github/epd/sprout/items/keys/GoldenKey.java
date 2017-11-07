
package com.github.epd.sprout.items.keys;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class GoldenKey extends Key {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.GOLDEN_KEY;
	}

	public GoldenKey() {
		this(0);
	}

	public GoldenKey(int depth) {
		super();
		this.depth = depth;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
