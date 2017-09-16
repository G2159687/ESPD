package com.github.epd.sprout.items;

import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndOptions;

public abstract class KindofMisc extends EquipableItem {


	private static final float TIME_TO_EQUIP = 1f;

	@Override
	public boolean doEquip(final Hero hero) {

		if (hero.belongings.misc1 != null && hero.belongings.misc2 != null && hero.belongings.misc3 != null && hero.belongings.misc4 != null) {

			final KindofMisc m1 = hero.belongings.misc1;
			final KindofMisc m2 = hero.belongings.misc2;
			final KindofMisc m3 = hero.belongings.misc3;
			final KindofMisc m4 = hero.belongings.misc4;
			final KindofMisc toEquip = this;

			GameScene.show(
					new WndOptions(Messages.get(KindofMisc.class, "unequip_title"),
							Messages.get(KindofMisc.class, "unequip_message"),
							Messages.titleCase(m1.toString()),
							Messages.titleCase(m2.toString()),
							Messages.titleCase(m3.toString()),
							Messages.titleCase(m4.toString())) {

						@Override
						protected void onSelect(int index) {

							KindofMisc equipped;

							if (index == 0) {
								equipped = m1;
							} else if (index == 1) {
								equipped = m2;
							} else if (index == 2) {
								equipped = m3;
							} else {
								equipped = m4;
							}

							if (equipped.doUnequip(hero, true, false)) {
								execute(hero, AC_EQUIP);
							}
						}
					});

			return false;

		} else {

			if (hero.belongings.misc1 == null) {
				hero.belongings.misc1 = this;
			} else if (hero.belongings.misc2 == null) {
				hero.belongings.misc2 = this;
			} else if (hero.belongings.misc3 == null) {
				hero.belongings.misc3 = this;
			} else {
				hero.belongings.misc4 = this;
			}

			detach(hero.belongings.backpack);

			activate(hero);

			cursedKnown = true;
			if (cursed) {
				equipCursed(hero);
				GLog.n(Messages.get(this, "cursed", this));
			}

			hero.spendAndNext(TIME_TO_EQUIP);
			return true;

		}

	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)) {

			if (hero.belongings.misc1 == this) {
				hero.belongings.misc1 = null;
			} else if (hero.belongings.misc2 == this) {
				hero.belongings.misc2 = null;
			} else if (hero.belongings.misc3 == this) {
				hero.belongings.misc3 = null;
			} else {
				hero.belongings.misc4 = null;
			}

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped(Hero hero) {
		return hero.belongings.misc1 == this || hero.belongings.misc2 == this || hero.belongings.misc3 == this || hero.belongings.misc4 == this;
	}

}
