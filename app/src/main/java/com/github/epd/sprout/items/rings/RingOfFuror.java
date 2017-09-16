package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.messages.Messages;

public class RingOfFuror extends Ring {

	{
		name = Messages.get(this, "name");
	}

	@Override
	protected RingBuff buff() {
		return new Furor();
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this, "desc") : super.desc();
	}

	public class Furor extends RingBuff {
	}
}
