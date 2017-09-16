package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.messages.Messages;

public class RingOfMight extends Ring {

	{
		name = Messages.get(this, "name");
	}

	@Override
	protected RingBuff buff() {
		return new Might();
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this, "desc")
				: super.desc();
	}

	public class Might extends RingBuff {
	}
}
