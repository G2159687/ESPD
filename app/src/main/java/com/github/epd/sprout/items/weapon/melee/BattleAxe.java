
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class BattleAxe extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.BATTLE_AXE;
	}

	public BattleAxe() {
		super(4, 1.2f, 1f);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
