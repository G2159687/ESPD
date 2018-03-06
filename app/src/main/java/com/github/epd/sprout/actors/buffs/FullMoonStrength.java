
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.messages.Messages;
import com.watabou.utils.Bundle;

public class FullMoonStrength extends Buff {

	public static float LEVEL = 0.4f;

	private int hits = (Math.max(2, Math.round(Statistics.deepestFloor / 5) + 3));


	private static final String HITS = "hits";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(HITS, hits);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		hits = bundle.getInt(HITS);
	}

	//private int hits = Math.max(2, Math.round(Statistics.deepestFloor/5)+3);
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", hits);
	}

	@Override
	public void detach() {
		hits--;
		if (hits == 0) {
			super.detach();
		}
	}

}
