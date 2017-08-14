package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.messages.Messages;

public class RingOfTenacity extends Ring {

	{
		name = Messages.get(this,"name");
	}

	@Override
	protected RingBuff buff() {
		return new Tenacity();
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this,"desc")
				: super.desc();
	}

	public class Tenacity extends RingBuff {
	}
}
