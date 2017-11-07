
package com.github.epd.sprout.items.weapon.missiles;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Tamahawk extends MissileWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.TOMAHAWK;

		STR = 17;

		MIN = 4;
		MAX = 20;
	}

	public Tamahawk() {
		this(1);
	}

	public Tamahawk(int number) {
		super();
		quantity = number;
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		super.proc(attacker, defender, damage);
		Buff.affect(defender, Bleeding.class).set(damage);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public Item random() {
		quantity = Random.Int(5, 12);
		return this;
	}

	@Override
	public int price() {
		return 15 * quantity;
	}
}
