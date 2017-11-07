
package com.github.epd.sprout.items.weapon.melee.relic;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Gullin;
import com.github.epd.sprout.actors.mobs.Kupua;
import com.github.epd.sprout.actors.mobs.MineSentinel;
import com.github.epd.sprout.actors.mobs.Otiluke;
import com.github.epd.sprout.actors.mobs.Zot;
import com.github.epd.sprout.actors.mobs.ZotPhase;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class RelicMeleeWeapon extends Weapon {

	private int tier;

	private static final float TIME_TO_EQUIP = 1f;

	public Buff passiveBuff;
	protected Buff activeBuff;

	// level is used internally to track upgrades to artifacts, size/logic
	// varies per artifact.
	// already inherited from item superclass
	// exp is used to count progress towards levels for some artifacts
	protected int exp = 0;
	// levelCap is the artifact's maximum level
	protected int levelCap = 0;

	// the current artifact charge
	public int charge = 0;

	// the maximum charge, varies per artifact, not all artifacts use this.
	public int chargeCap = 0;

	// used by some artifacts to keep track of duration of effects or cooldowns
	// to use.
	protected int cooldown = 0;

	public RelicMeleeWeapon(int tier, float acu, float dly) {
		super();

		this.tier = tier;

		ACU = acu;
		DLY = dly;

		STR = typicalSTR();

		MIN = min();
		MAX = max();
		reinforced = true;
	}

	private int min() {
		return tier;
	}

	private int max() {
		return (int) ((tier * tier - tier + 10) / ACU * DLY);
	}

	@Override
	public boolean doEquip(Hero hero) {

		activate(hero);

		return super.doEquip(hero);

	}


	@Override
	public void activate(Hero hero) {
		passiveBuff = passiveBuff();
		passiveBuff.attachTo(hero);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {

		if (super.doUnequip(hero, collect, single)) {

			if (passiveBuff != null) {
				passiveBuff.detach();
				passiveBuff = null;
			}

			hero.belongings.weapon = null;
			return true;

		} else {

			return false;

		}
	}


	protected WeaponBuff passiveBuff() {
		return null;
	}

	public class WeaponBuff extends Buff {

		public int level() {
			return level;
		}

		public boolean isCursed() {
			return cursed;
		}

	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {

		if (defender instanceof Gullin
				|| defender instanceof Kupua
				|| defender instanceof MineSentinel
				|| defender instanceof Otiluke
				|| defender instanceof Zot
				|| defender instanceof ZotPhase) {

			//damage*=2;

			defender.damage(Random.Int(damage, damage * 2), this);
		}


		if (enchantment != null) {
			enchantment.proc(this, attacker, defender, damage);
		}
	}


	@Override
	public Item upgrade() {
		return upgrade(false);
	}

	@Override
	public Item upgrade(boolean enchant) {
		STR--;
		MIN++;
		MAX += tier;

		if (enchant) {
			GLog.i(Messages.get(RelicMeleeWeapon.class, "refuse"));
		}
		return super.upgrade(false);

	}

	public Item safeUpgrade() {
		return upgrade(enchantment != null);
	}


	@Override
	public Item degrade() {
		STR++;
		MIN--;
		MAX -= tier;
		return super.degrade();
	}

	public int typicalSTR() {
		return 8 + tier * 2;
	}

	@Override
	public String info() {

		final String p = "\n\n";

		StringBuilder info = new StringBuilder(desc());

		String quality = levelKnown && level != 0 ? (level > 0 ? Messages.get(RelicMeleeWeapon.class, "upgraded")
				: Messages.get(RelicMeleeWeapon.class, "degraded")) : "";
		info.append(p);
		info.append(Messages.get(RelicMeleeWeapon.class, "this", name, Utils.indefinite(quality)));
		info.append(Messages.get(RelicMeleeWeapon.class, "this2", tier));

		if (levelKnown) {
			info.append(Messages.get(RelicMeleeWeapon.class, "avgdmg",
					Math.round((MIN) * (imbue == Imbue.LIGHT ? 0.75f : (imbue == Imbue.HEAVY ? 1.5f : 1))), Math.round((MAX) * (imbue == Imbue.LIGHT ? 0.75f : (imbue == Imbue.HEAVY ? 1.5f : 1)))));
		} else {
			info.append(Messages.get(RelicMeleeWeapon.class, "typicaldmg", min(), max(), typicalSTR()));
			if (typicalSTR() > Dungeon.hero.STR()) {
				info.append(Messages.get(RelicMeleeWeapon.class, "pheavy"));
			}
		}

		if (DLY != 1f) {
			info.append(Messages.get(RelicMeleeWeapon.class, "rather") + (DLY < 1f ? Messages.get(RelicMeleeWeapon.class, "fast") : Messages.get(RelicMeleeWeapon.class, "slow")));
			if (ACU != 1f) {
				if ((ACU > 1f) == (DLY < 1f)) {
					info.append(Messages.get(RelicMeleeWeapon.class, "and"));
				} else {
					info.append(Messages.get(RelicMeleeWeapon.class, "but"));
				}
				info.append(ACU > 1f ? Messages.get(RelicMeleeWeapon.class, "ac") : Messages.get(RelicMeleeWeapon.class, "inac"));
			}
			info.append(Messages.get(RelicMeleeWeapon.class, "weapon"));
		} else if (ACU != 1f) {
			info.append(Messages.get(RelicMeleeWeapon.class, "rather")
					+ (ACU > 1f ? Messages.get(RelicMeleeWeapon.class, "ac") : Messages.get(RelicMeleeWeapon.class, "inac")) + Messages.get(RelicMeleeWeapon.class, "weapon"));
		}
		switch (imbue) {
			case LIGHT:
				info.append(Messages.get(RelicMeleeWeapon.class, "lighter"));
				break;
			case HEAVY:
				info.append(Messages.get(RelicMeleeWeapon.class, "heavier"));
				break;
			case NONE:
		}

		if (enchantment != null) {
			info.append(Messages.get(RelicMeleeWeapon.class, "enchanted"));
		}

		if (reinforced) {
			info.append(Messages.get(RelicMeleeWeapon.class, "reinforced"));
		}

		if (charge >= chargeCap) {
			info.append(Messages.get(RelicMeleeWeapon.class, "fullcharge"));
		} else {
			info.append(Messages.get(RelicMeleeWeapon.class, "charge", charge, chargeCap));
		}
		if (levelKnown
			//&& Dungeon.hero.belongings.backpack.items.contains(this)
				) {
			if (STR > Dungeon.hero.STR()) {
				info.append(p);
				info.append(Messages.get(RelicMeleeWeapon.class, "decreased", name));
			}
			if (STR < Dungeon.hero.STR()) {
				info.append(p);
				info.append(Messages.get(RelicMeleeWeapon.class, "increased", name));
			}
		}

		if (isEquipped(Dungeon.hero)) {
			info.append(p);
			info.append(Messages.get(RelicMeleeWeapon.class, "atready", name)
					+ (cursed ? Messages.get(RelicMeleeWeapon.class, "equipcursed") : ""));
		} else {
			if (cursedKnown && cursed) {
				info.append(p);
				info.append(Messages.get(RelicMeleeWeapon.class, "knowncursed", name));
			}
		}

		return info.toString();
	}

	@Override
	public int price() {
		int price = 20 * (1 << (tier - 1));
		if (enchantment != null) {
			price *= 1.5;
		}
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level > 0) {
				price *= (level + 1);
			} else if (level < 0) {
				price /= (1 - level);
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	@Override
	public Item random() {
		super.random();

		if (Random.Int(10 + level) == 0) {
			enchant();
		}

		return this;
	}

	private static final String CHARGE = "charge";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHARGE, charge);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		charge = bundle.getInt(CHARGE);
	}
}


