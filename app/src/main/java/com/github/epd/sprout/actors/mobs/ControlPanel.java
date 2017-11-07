
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.RedDewdrop;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.DwarfKingTombSprite;

public class ControlPanel extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = DwarfKingTombSprite.class;

		HP = HT = 6000;
		defenseSkill = 50;

		EXP = 10;

		hostile = false;
		state = PASSIVE;

		loot = new RedDewdrop();
		lootChance = 0.05f;

		properties.add(Property.IMMOVABLE);

	}

	@Override
	public void beckon(int cell) {
		// Do nothing
	}

	@Override
	public void add(Buff buff) {
	}


	@Override
	public int damageRoll() {
		return 0;
	}

	@Override
	public int attackSkill(Char target) {
		return 0;
	}

	@Override
	public int dr() {
		return 80;

	}


	@Override
	public String description() {
		return Messages.get(this, "desc");
	}


}
