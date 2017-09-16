package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.messages.Messages;

public class RingOfSharpshooting extends Ring {

	{
		name = Messages.get(this, "name");
	}

	@Override
	protected RingBuff buff() {
		return new Aim();
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this, "desc")
				: super.desc();
	}

	public class Aim extends RingBuff {
	}
}
