package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.KindofMisc;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Artifact extends KindofMisc {

	// TODO: Add cursed effects for sprouted's new artifact

	private static final String TXT_TO_STRING = "%s";
	private static final String TXT_TO_STRING_CHARGE = "%s (%d/%d)";
	private static final String TXT_TO_STRING_LVL = "%s%+d";
	private static final String TXT_TO_STRING_LVL_CHARGE = "%s%+d (%d/%d)";

	private static final String TXT_UNEQUIP_TITLE = Messages.get(Artifact.class, "unequip_title");
	private static final String TXT_UNEQUIP_MESSAGE = Messages.get(Artifact.class, "unequip_msg");

	protected Buff passiveBuff;
	protected Buff activeBuff;

	// level is used internally to track upgrades to artifacts, size/logic
	// varies per artifact.
	// already inherited from item superclass
	// exp is used to count progress towards levels for some artifacts
	protected int exp = 0;
	// levelCap is the artifact's maximum level
	public int levelCap = 0;

	// the current artifact charge
	protected int charge = 0;
	// the build towards next charge, usually rolls over at 1.
	// better to keep charge as an int and use a separate float than casting.
	protected float partialCharge = 0;
	// the maximum charge, varies per artifact, not all artifacts use this.
	protected int chargeCap = 0;

	// used by some artifacts to keep track of duration of effects or cooldowns
	// to use.
	protected int cooldown = 0;

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
		return actions;
	}

	@Override
	public boolean doEquip(final Hero hero) {

		if ((hero.belongings.misc1 != null && hero.belongings.misc1.getClass() == this.getClass())
				|| (hero.belongings.misc2 != null && hero.belongings.misc2.getClass() == this.getClass())
				|| (hero.belongings.misc3 != null && hero.belongings.misc3.getClass() == this.getClass())
				|| (hero.belongings.misc4 != null && hero.belongings.misc4.getClass() == this.getClass())) {

			GLog.w(Messages.get(Artifact.class, "2a"));
			return false;

		} else {
			if (super.doEquip(hero)) {
				identify();
				return true;
			} else {
				return false;
			}
		}

	}

	@Override
	public void activate(Char ch) {
		passiveBuff = passiveBuff();
		passiveBuff.attachTo(ch);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)) {

			passiveBuff.detach();
			passiveBuff = null;

			if (activeBuff != null) {
				activeBuff.detach();
				activeBuff = null;
			}

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	// transfers upgrades from another artifact, transfer level will equal the
	// displayed level
	public void transferUpgrade(int transferLvl) {
		upgrade(Math.round((float) (transferLvl * levelCap) / 10));
	}

	@Override
	public String info() {
		if (cursed && cursedKnown && !isEquipped(Dungeon.hero)) {

			return desc()
					+ Messages.get(this, "curse_known");

		} else {

			return desc();

		}
	}

	@Override
	public String toString() {

		if (levelKnown && level / levelCap != 0) {
			if (chargeCap > 0) {
				return Utils.format(TXT_TO_STRING_LVL_CHARGE, name(),
						level, charge, chargeCap);
			} else {
				return Utils.format(TXT_TO_STRING_LVL, name(),
						level);
			}
		} else {
			if (chargeCap > 0) {
				return Utils.format(TXT_TO_STRING_CHARGE, name(), charge,
						chargeCap);
			} else {
				return Utils.format(TXT_TO_STRING, name());
			}
		}
	}

	@Override
	public String status() {

		// display the current cooldown
		if (cooldown != 0)
			return Utils.format("%d", cooldown);

		// display as percent
		if (chargeCap == 100)
			return Utils.format("%d%%", charge);

		// display as #/#
		if (chargeCap > 0)
			return Utils.format("%d/%d", charge, chargeCap);

		// if there's no cap -
		// - but there is charge anyway, display that charge
		if (charge != 0)
			return Utils.format("%d", charge);

		// otherwise, if there's no charge, return null.
		return null;
	}


	@Override
	public Item random() {
		if (Random.Float() < 0.3f) {
			cursed = true;
		}
		return this;
	}

	@Override
	public int price() {
		int price = 100;
		if (level > 0)
			price += 50 * ((level * 10) / levelCap);
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	protected ArtifactBuff passiveBuff() {
		return null;
	}

	protected ArtifactBuff activeBuff() {
		return null;
	}

	public class ArtifactBuff extends Buff {

		public int level() {
			return level;
		}

		public boolean isCursed() {
			return cursed;
		}

	}

	private static final String IMAGE = "image";
	private static final String EXP = "exp";
	private static final String CHARGE = "charge";
	private static final String PARTIALCHARGE = "partialcharge";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(IMAGE, image);
		bundle.put(EXP, exp);
		bundle.put(CHARGE, charge);
		bundle.put(PARTIALCHARGE, partialCharge);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(IMAGE)) image = bundle.getInt(IMAGE);
		exp = bundle.getInt(EXP);
		charge = bundle.getInt(CHARGE);
		partialCharge = bundle.getFloat(PARTIALCHARGE);
	}
}
