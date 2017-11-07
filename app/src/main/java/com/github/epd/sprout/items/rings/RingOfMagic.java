
package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.messages.Messages;

public class RingOfMagic extends Ring {

	{
		name = Messages.get(this, "name");
	}

	@Override
	protected RingBuff buff() {
		return new Magic();
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this, "desc") : super.desc();
	}

	public class Magic extends RingBuff {
	}
}
