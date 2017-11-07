
package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.messages.Messages;

public class RingOfAccuracy extends Ring {

	{
		name = Messages.get(this, "name");
	}

	@Override
	protected RingBuff buff() {
		return new Accuracy();
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this, "desc") : super.desc();
	}

	public class Accuracy extends RingBuff {
	}
}
