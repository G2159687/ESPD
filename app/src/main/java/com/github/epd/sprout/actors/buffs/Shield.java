
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.messages.Messages;

public class Shield extends Buff {

	public static float LEVEL = 0.4f;

	private int hits = (Math.max(2, Math.round(Statistics.deepestFloor / 5) + 3));

	//private int hits = Math.max(2, Math.round(Statistics.deepestFloor/5)+3);
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public void detach() {
		hits--;
		if (hits == 0) {
			super.detach();
		}
	}

}
