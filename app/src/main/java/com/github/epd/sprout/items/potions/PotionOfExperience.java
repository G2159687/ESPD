
package com.github.epd.sprout.items.potions;

import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;

public class PotionOfExperience extends Potion {

	{
		initials = 0;

		name = Messages.get(this, "name");

		bones = true;
	}

	@Override
	public void apply(Hero hero) {
		setKnown();
		hero.earnExp(hero.maxExp() - hero.exp);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return isKnown() ? 80 * quantity : super.price();
	}
}
