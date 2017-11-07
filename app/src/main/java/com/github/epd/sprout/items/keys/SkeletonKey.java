
package com.github.epd.sprout.items.keys;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class SkeletonKey extends Key {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SKELETON_KEY;
		stackable = false;
	}

	public SkeletonKey() {
		this(0);
	}

	public SkeletonKey(int depth) {
		super();
		this.depth = depth;
	}

	@Override
	public boolean isSimilar(Item item) {
		return false;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
