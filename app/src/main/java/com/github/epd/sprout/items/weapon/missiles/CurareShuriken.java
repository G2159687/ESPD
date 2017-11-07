
package com.github.epd.sprout.items.weapon.missiles;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class CurareShuriken extends MissileWeapon {

	public static final float DURATION = 3f;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SHURIKEN;

		STR = 13;

		MIN = 2;
		MAX = 6;

		DLY = 0.5f;
	}

	public CurareShuriken() {
		this(1);
	}

	public CurareShuriken(int number) {
		super();
		quantity = number;
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		Buff.prolong(defender, Paralysis.class, DURATION);
		super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		return Messages.get(Shuriken.class, "desc");
	}

	@Override
	public Item random() {
		quantity = Random.Int(5, 15);
		return this;
	}

	@Override
	public int price() {
		return 6 * quantity;
	}
}
