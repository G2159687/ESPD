
package com.github.epd.sprout.items;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class KindOfWeapon extends EquipableItem {

	private static final String TXT_EQUIP_CURSED = Messages.get(KindOfWeapon.class, "cursed");

	protected static final float TIME_TO_EQUIP = 1f;

	public int MIN = 0;
	public int MAX = 1;

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
		return actions;
	}

	@Override
	public boolean isEquipped(Hero hero) {
		return hero.belongings.weapon == this;
	}

	@Override
	public boolean doEquip(Hero hero) {

		detachAll(hero.belongings.backpack);

		if (hero.belongings.weapon == null
				|| hero.belongings.weapon.doUnequip(hero, true)) {

			hero.belongings.weapon = this;
			activate(hero);

			updateQuickslot();

			cursedKnown = true;
			if (cursed) {
				equipCursed(hero);
				GLog.n(TXT_EQUIP_CURSED);
			}

			hero.spendAndNext(TIME_TO_EQUIP);
			return true;

		} else {

			collect(hero.belongings.backpack);
			return false;
		}
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)) {

			hero.belongings.weapon = null;
			return true;

		} else {

			return false;

		}
	}

	public void activate(Hero hero) {
	}

	public int damageRoll(Hero owner) {
		return Random.NormalIntRange(MIN, MAX);
	}

	public float acuracyFactor(Hero hero) {
		return 1f;
	}

	public float speedFactor(Hero hero) {
		return 1f;
	}

	public int reachFactor(Hero hero) {
		return 1;
	}

	public void proc(Char attacker, Char defender, int damage) {
	}

}
