
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.KindOfWeapon;
import com.github.epd.sprout.items.bags.Bag;
import com.github.epd.sprout.items.rings.RingOfMagic.Magic;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.CellSelector;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.QuickSlotButton;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Wand extends KindOfWeapon {

	//TODO: Damage balancing

	private static final int USAGES_TO_KNOW = 20;

	public static final String AC_ZAP = Messages.get(Wand.class, "ac_zap");

	private static final String TXT_WOOD = Messages.get(Wand.class, "wood");
	private static final String TXT_DAMAGE = Messages.get(Wand.class, "damage");
	private static final String TXT_WEAPON = Messages.get(Wand.class, "weapon");

	private static final String TXT_FIZZLES = Messages.get(Wand.class, "fizzles");
	private static final String TXT_SELF_TARGET = Messages.get(Wand.class, "self_target");

	private static final String TXT_IDENTIFY = Messages.get(Wand.class, "identify");

	private static final String TXT_REINFORCED = Messages.get(Wand.class, "re");

	private static final float TIME_TO_ZAP = 1f;

	public int maxCharges = initialCharges();
	public int curCharges = maxCharges;
	private float partialCharge = 0f;
	protected Charger charger;

	private boolean curChargeKnown = false;

	private int usagesToKnow = USAGES_TO_KNOW;

	protected int collisionProperties = Ballistica.MAGIC_BOLT;


	{
		defaultAction = AC_ZAP;
		usesTargeting = true;
	}

	public Wand() {
		super();
		calculateDamage();
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (curCharges > 0 || !curChargeKnown) {
			actions.add(AC_ZAP);
		}
		if (hero.heroClass != HeroClass.MAGE) {
			actions.remove(AC_EQUIP);
			actions.remove(AC_UNEQUIP);
		}
		return actions;
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		onDetach();
		return super.doUnequip(hero, collect, single);
	}

	@Override
	public void activate(Hero hero) {
		charge(hero);
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_ZAP)) {

			curUser = hero;
			curItem = this;
			GameScene.selectCell(zapper);

		} else {

			super.execute(hero, action);

		}
	}

	protected abstract void onZap(Ballistica attack);

	@Override
	public boolean collect(Bag container) {
		if (super.collect(container)) {
			if (container.owner != null) {
				charge(container.owner);
			}
			return true;
		} else {
			return false;
		}
	}

	public void charge(Char owner) {
		if (charger == null) charger = new Charger();
		charger.attachTo(owner);
	}

	public void charge(Char owner, float chargeScaleFactor) {
		charge(owner);
		charger.setScaleFactor(chargeScaleFactor);
	}

	@Override
	public void onDetach() {
		stopCharging();
	}

	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}

	public int level() {

		int magicLevel = 0;
		if (charger != null) {
			Magic magic = charger.target.buff(Magic.class);
			if (magic != null) {
				magicLevel = magic.level;
			}
			return magic == null ? level : Math.max(level + magicLevel, 0);
		} else {
			return level;
		}
	}

	@Override
	public Item identify() {
		curChargeKnown = true;
		super.identify();

		updateQuickslot();

		return this;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(super.toString());

		String status = status();
		if (status != null) {
			sb.append(" (" + status + ")");
		}

		return sb.toString();
	}

	@Override
	public String info() {
		StringBuilder info = new StringBuilder(desc());
		if (Dungeon.hero.heroClass == HeroClass.MAGE) {
			info.append("\n\n");
			if (levelKnown) {
				info.append(String.format(TXT_DAMAGE, MIN, MAX));
			} else {
				info.append(String.format(TXT_WEAPON));
			}

		}
		if (reinforced) {
			info.append(String.format(TXT_REINFORCED));
		}
		return info.toString();
	}

	@Override
	public boolean isIdentified() {
		return super.isIdentified() && curChargeKnown;
	}

	@Override
	public String status() {
		if (levelKnown) {
			return (curChargeKnown ? curCharges : "?") + "/" + maxCharges;
		} else {
			return null;
		}
	}

	@Override
	public Item upgrade() {

		super.upgrade();

		updateLevel();
		curCharges = Math.min(curCharges + 1, maxCharges);
		updateQuickslot();

		return this;
	}

	@Override
	public Item degrade() {
		super.degrade();

		updateLevel();
		updateQuickslot();

		return this;
	}

	public void updateLevel() {
		maxCharges = Math.min(initialCharges() + level, 14);
		curCharges = Math.min(curCharges, maxCharges);

		calculateDamage();
	}

	protected int initialCharges() {
		return 2;
	}

	protected int chargesPerCast() {
		return 1;
	}

	private void calculateDamage() {
		int tier = 1 + level / 3;
		MIN = tier;
		MAX = (tier * tier - tier + 10) / 2 + level;
	}

	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.whiteLight(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	protected void wandUsed() {
		usagesToKnow -= chargesPerCast();
		curCharges -= chargesPerCast();
		if (!isIdentified() && usagesToKnow <= 0) {
			identify();
			GLog.w(TXT_IDENTIFY, name());
		} else {
			updateQuickslot();
		}

		curUser.spendAndNext(TIME_TO_ZAP);
	}

	protected void wandEmpty() {
		curCharges = 0;
		updateQuickslot();
	}

	@Override
	public Item random() {
		int n = 0;

		if (Random.Int(3) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}

		upgrade(n);

		return this;
	}

	@Override
	public int price() {
		int price = 75;
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
	public void proc(Char attacker, Char defender, int damage){

	}

	private static final String UNFAMILIRIARITY = "unfamiliarity";
	private static final String MAX_CHARGES = "maxCharges";
	private static final String CUR_CHARGES = "curCharges";
	private static final String CUR_CHARGE_KNOWN = "curChargeKnown";
	private static final String PARTIALCHARGE = "partialCharge";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(UNFAMILIRIARITY, usagesToKnow);
		bundle.put(MAX_CHARGES, maxCharges);
		bundle.put(CUR_CHARGES, curCharges);
		bundle.put(CUR_CHARGE_KNOWN, curChargeKnown);
		bundle.put(PARTIALCHARGE, partialCharge);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if ((usagesToKnow = bundle.getInt(UNFAMILIRIARITY)) == 0) {
			usagesToKnow = USAGES_TO_KNOW;
		}
		maxCharges = bundle.getInt(MAX_CHARGES);
		curCharges = bundle.getInt(CUR_CHARGES);
		curChargeKnown = bundle.getBoolean(CUR_CHARGE_KNOWN);
		partialCharge = bundle.getFloat(PARTIALCHARGE);
	}

	protected static CellSelector.Listener zapper = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer target) {

			if (target != null) {

				if (!(Item.curItem instanceof Wand)){
					return;
				}

				final Wand curWand = (Wand) Item.curItem;

				final Ballistica shot = new Ballistica(curUser.pos, target, curWand.collisionProperties);
				int cell = shot.collisionPos;

				if (target == curUser.pos || cell == curUser.pos) {
					GLog.i(TXT_SELF_TARGET);
					return;
				}

				curUser.sprite.zap(cell);

				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));

				if (curWand.curCharges >= curWand.chargesPerCast()) {

					curUser.busy();

					curWand.fx(shot, new Callback() {
						public void call() {
							curWand.onZap(shot);
							curWand.wandUsed();
						}
					});

					Invisibility.dispel();

				} else {

					curUser.spendAndNext(TIME_TO_ZAP);
					GLog.w(TXT_FIZZLES);

				}

			}
		}

		@Override
		public String prompt() {
			return Messages.get(Wand.class, "prompt");
		}
	};

	protected class Charger extends Buff {

		private static final float BASE_CHARGE_DELAY = 10f;
		private static final float SCALING_CHARGE_ADDITION = 40f;
		private static final float NORMAL_SCALE_FACTOR = 0.85f;
		float scalingFactor = NORMAL_SCALE_FACTOR;

		@Override
		public boolean attachTo(Char target) {
			super.attachTo(target);
			return true;
		}

		@Override
		public boolean act() {

			if (curCharges < maxCharges)
				gainCharge();
			if (partialCharge >= 1 && curCharges < maxCharges) {
				partialCharge--;
				curCharges++;
				updateQuickslot();
			}

			spend(TICK);

			return true;
		}

		private void gainCharge() {
			int missingCharges = maxCharges - curCharges;

			float turnsToCharge = (float) (BASE_CHARGE_DELAY + (SCALING_CHARGE_ADDITION * Math.pow(scalingFactor, missingCharges)));
			partialCharge += 1f / turnsToCharge;
		}

		private void setScaleFactor(float value) {
			this.scalingFactor = value;
		}
	}
}
