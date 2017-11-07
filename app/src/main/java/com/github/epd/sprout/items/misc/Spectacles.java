
package com.github.epd.sprout.items.misc;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Spectacles extends MiscEquippable {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.OTILUKES_SPECS;
	}

	@Override
	protected MiscBuff buff() {
		return new MagicSight();
	}

	public class MagicSight extends MiscBuff {
	}

	@Override
	public String cursedDesc() {
		return Messages.get(this, "cursed", this);
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}


}
