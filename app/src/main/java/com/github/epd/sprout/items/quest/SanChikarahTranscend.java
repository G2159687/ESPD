
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.SanChikarahDeath;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class SanChikarahTranscend extends Item {

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
	public boolean doPickUp(Hero hero) {
		if (super.doPickUp(hero)) {

			if (Dungeon.level != null && Dungeon.depth == 33) {
				Dungeon.sanchikarahtranscend = true;
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void doDrop(Hero hero) {

		if (Dungeon.level != null && Dungeon.depth == 33) {
			Dungeon.sanchikarahtranscend = false;
		}

		super.doDrop(hero);
	}

	@Override
	public String info() {
		return Messages.get(SanChikarahDeath.class, "desc");
	}
}
