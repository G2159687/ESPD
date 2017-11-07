
package com.github.epd.sprout.items.potions;

import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.utils.GLog;

public class PotionOfMight extends Potion {

	{
		initials = 6;
		name = Messages.get(this, "name");

		bones = true;
	}

	@Override
	public void apply(Hero hero) {
		setKnown();

		hero.STR++;
		hero.HT += 5;
		hero.HP += 5;
		hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "msg_1"));
		GLog.p(Messages.get(this, "msg_2"));
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return isKnown() ? 200 * quantity : super.price();
	}
}
