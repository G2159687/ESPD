
package com.github.epd.sprout.items.misc;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class AutoPotion extends MiscEquippable {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.AUTOPOTION;
	}

	@Override
	protected MiscBuff buff() {
		return new AutoHealPotion();
	}

	public class AutoHealPotion extends MiscBuff {
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
