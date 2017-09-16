package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.messages.Messages;

public class RingOfWealth extends Ring {
	{
		name = Messages.get(this, "name");
	}

	@Override
	protected RingBuff buff() {
		return new Wealth();
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this, "desc")
				: super.desc();
	}

	public class Wealth extends RingBuff {
	}
}
