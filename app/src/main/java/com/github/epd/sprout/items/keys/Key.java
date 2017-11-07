
package com.github.epd.sprout.items.keys;

import com.github.epd.sprout.items.Item;
import com.watabou.utils.Bundle;

public class Key extends Item {

	public static final float TIME_TO_UNLOCK = 1f;

	{
		stackable = true;
		unique = true;
	}

	public int depth;

	@Override
	public boolean isSimilar(Item item) {
		return item.getClass() == getClass() && ((Key) item).depth == depth;
	}

	private static final String DEPTH = "depth";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEPTH, depth);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		depth = bundle.getInt(DEPTH);
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

}
