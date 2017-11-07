
package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.ItemStatusHandler;
import com.github.epd.sprout.items.KindofMisc;
import com.github.epd.sprout.items.artifacts.Artifact;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Ring extends KindofMisc {

	private static final int TICKS_TO_KNOW = 200;

	private static final String TXT_UNEQUIP_TITLE = Messages.get(Artifact.class, "unequip_title");
	private static final String TXT_UNEQUIP_MESSAGE = Messages.get(Artifact.class, "unequip_msg");

	private static final String TXT_IDENTIFY = Messages.get(Ring.class, "identify");

	protected Buff buff;

	private static final Class<?>[] rings = {RingOfAccuracy.class,
			RingOfEvasion.class, RingOfElements.class, RingOfForce.class,
			RingOfFuror.class, RingOfHaste.class, RingOfMagic.class,
			RingOfMight.class, RingOfSharpshooting.class, RingOfTenacity.class,
			RingOfWealth.class,};
	private static final String[] gems = {
			Messages.get(Ring.class, "diamond"),
			Messages.get(Ring.class, "opal"),
			Messages.get(Ring.class, "garnet"),
			Messages.get(Ring.class, "ruby"),
			Messages.get(Ring.class, "amethyst"),
			Messages.get(Ring.class, "topaz"),
			Messages.get(Ring.class, "onyx"),
			Messages.get(Ring.class, "tourmaline"),
			Messages.get(Ring.class, "emerald"),
			Messages.get(Ring.class, "sapphire"),
			Messages.get(Ring.class, "quartz"),
			Messages.get(Ring.class, "agate")};
	private static final Integer[] images = {ItemSpriteSheet.RING_DIAMOND,
			ItemSpriteSheet.RING_OPAL, ItemSpriteSheet.RING_GARNET,
			ItemSpriteSheet.RING_RUBY, ItemSpriteSheet.RING_AMETHYST,
			ItemSpriteSheet.RING_TOPAZ, ItemSpriteSheet.RING_ONYX,
			ItemSpriteSheet.RING_TOURMALINE, ItemSpriteSheet.RING_EMERALD,
			ItemSpriteSheet.RING_SAPPHIRE, ItemSpriteSheet.RING_QUARTZ,
			ItemSpriteSheet.RING_AGATE};

	private static ItemStatusHandler<Ring> handler;

	private String gem;

	private int ticksToKnow = TICKS_TO_KNOW;

	@SuppressWarnings("unchecked")
	public static void initGems() {
		handler = new ItemStatusHandler<Ring>((Class<? extends Ring>[]) rings,
				gems, images);
	}

	public static void save(Bundle bundle) {
		handler.save(bundle);
	}

	@SuppressWarnings("unchecked")
	public static void restore(Bundle bundle) {
		handler = new ItemStatusHandler<Ring>((Class<? extends Ring>[]) rings,
				gems, images, bundle);
	}

	public Ring() {
		super();
		syncVisuals();
	}

	@Override
	public void syncVisuals() {
		image = handler.image(this);
		gem = handler.label(this);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
		return actions;
	}

	@Override
	public void activate(Char ch) {
		buff = buff();
		buff.attachTo(ch);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)) {

			hero.remove(buff);
			buff = null;

			return true;

		} else {

			return false;

		}
	}

	@Override
	public Item upgrade() {

		super.upgrade();

		if (buff != null) {

			Char owner = buff.target;
			buff.detach();
			if ((buff = buff()) != null) {
				buff.attachTo(owner);
			}
		}

		return this;
	}

	public boolean isKnown() {
		return handler.isKnown(this);
	}

	protected void setKnown() {
		if (!isKnown()) {
			handler.know(this);
		}
	}

	@Override
	public String name() {
		return isKnown() ? super.name() : Messages.get(this, "unknown_name", gem);
	}

	@Override
	public String desc() {
		return Messages.get(this, "unknown_desc", gem);
	}

	@Override
	public String info() {
		if (isEquipped(Dungeon.hero)) {

			return desc()
					+ "\n\n" + Messages.get(this, "on_finger", name())
					+ (cursed ? Messages.get(this, "cursed_worn") : "")
					+ (reinforced ? Messages.get(this, "reinforced") : "");

		} else if (cursed && cursedKnown) {

			return desc()
					+ "\n\n" + Messages.get(this, "curse_known", name());

		} else {

			return desc() + (reinforced ? Messages.get(this, "reinforced") : "");

		}
	}

	@Override
	public boolean isIdentified() {
		return super.isIdentified() && isKnown();
	}

	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}

	@Override
	public Item random() {
		int n = 1;
		if (Random.Int(3) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}

		if (Random.Float() < 0.3f) {
			level = -n;
			cursed = true;
		} else
			level = n;
		return this;
	}

	public static boolean allKnown() {
		return handler.known().size() == rings.length - 2;
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

	protected RingBuff buff() {
		return null;
	}

	private static final String UNFAMILIRIARITY = "unfamiliarity";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(UNFAMILIRIARITY, ticksToKnow);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if ((ticksToKnow = bundle.getInt(UNFAMILIRIARITY)) == 0) {
			ticksToKnow = TICKS_TO_KNOW;
		}
	}

	public class RingBuff extends Buff {

		private final String TXT_KNOWN = Messages.get(Ring.class, "known");

		public int level;

		public RingBuff() {
			level = Ring.this.level;
		}

		@Override
		public boolean attachTo(Char target) {

			if (target instanceof Hero
					&& ((Hero) target).heroClass == HeroClass.ROGUE
					&& !isKnown()) {
				setKnown();
				GLog.i(TXT_KNOWN, name());
			}

			return super.attachTo(target);
		}

		@Override
		public boolean act() {

			if (!isIdentified() && --ticksToKnow <= 0) {
				String gemName = name();
				identify();
				GLog.w(TXT_IDENTIFY, gemName, Ring.this.toString());
			}

			spend(TICK);

			return true;
		}
	}
}
