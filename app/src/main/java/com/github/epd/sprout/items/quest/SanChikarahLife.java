
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.SanChikarahDeath;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class SanChikarahLife extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SANCHIKARAH;

		stackable = false;
		unique = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		return Messages.get(SanChikarahDeath.class, "desc");
	}
}
