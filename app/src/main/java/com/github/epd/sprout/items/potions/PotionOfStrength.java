
package com.github.epd.sprout.items.potions;

import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.utils.GLog;

public class PotionOfStrength extends Potion {

	{
		initials = 10;
		name = Messages.get(this, "name");

		bones = true;
	}

	@Override
	public void apply(Hero hero) {
		setKnown();

		hero.STR++;
		hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "msg_1"));
		GLog.p(Messages.get(this, "msg_2"));
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return isKnown() ? 100 * quantity : super.price();
	}
}
