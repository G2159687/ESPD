
package com.github.epd.sprout.items.weapon.missiles;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class CurareDart extends MissileWeapon {

	public static final float DURATION = 3f;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.CURARE_DART;

		STR = 14;

		MIN = 1;
		MAX = 3;
	}

	public CurareDart() {
		this(1);
	}

	public CurareDart(int number) {
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
		return Messages.get(this, "desc");
	}

	@Override
	public Item random() {
		quantity = Random.Int(2, 5);
		return this;
	}

	@Override
	public int price() {
		return 8 * quantity;
	}
}
