
package com.github.epd.sprout.items.scrolls;

import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.ItemStatusHandler;
import com.github.epd.sprout.items.artifacts.UnstableSpellbook;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Scroll extends Item {

	protected int initials;

	private static final String TXT_BLINDED = Messages.get(Scroll.class, "blinded");

	private static final String TXT_CURSED = Messages.get(Scroll.class, "cursed");

	public static final String AC_READ = Messages.get(Scroll.class, "ac_read");

	protected static final float TIME_TO_READ = 1f;

	private static final Class<?>[] scrolls = {ScrollOfIdentify.class,
			ScrollOfMagicMapping.class, ScrollOfRecharging.class,
			ScrollOfRemoveCurse.class, ScrollOfTeleportation.class,
			ScrollOfUpgrade.class, ScrollOfRage.class, ScrollOfTerror.class,
			ScrollOfLullaby.class, ScrollOfMagicalInfusion.class,
			ScrollOfPsionicBlast.class, ScrollOfMirrorImage.class, ScrollOfRegrowth.class};
	private static final String[] runes = {
			Messages.get(Scroll.class, "kaunan"),
			Messages.get(Scroll.class, "sowilo"),
			Messages.get(Scroll.class, "laguz"),
			Messages.get(Scroll.class, "yngvi"),
			Messages.get(Scroll.class, "gyfu"),
			Messages.get(Scroll.class, "raido"),
			Messages.get(Scroll.class, "isaz"),
			Messages.get(Scroll.class, "mannaz"),
			Messages.get(Scroll.class, "naudiz"),
			Messages.get(Scroll.class, "berkanan"),
			Messages.get(Scroll.class, "ncosrane"),
			Messages.get(Scroll.class, "tiwaz"),
			Messages.get(Scroll.class, "nendil")};
	private static final Integer[] images = {ItemSpriteSheet.SCROLL_KAUNAN,
			ItemSpriteSheet.SCROLL_SOWILO, ItemSpriteSheet.SCROLL_LAGUZ,
			ItemSpriteSheet.SCROLL_YNGVI, ItemSpriteSheet.SCROLL_GYFU,
			ItemSpriteSheet.SCROLL_RAIDO, ItemSpriteSheet.SCROLL_ISAZ,
			ItemSpriteSheet.SCROLL_MANNAZ, ItemSpriteSheet.SCROLL_NAUDIZ,
			ItemSpriteSheet.SCROLL_BERKANAN, ItemSpriteSheet.SCROLL_NCOSRANE,
			ItemSpriteSheet.SCROLL_TIWAZ, ItemSpriteSheet.SCROLL_NENDIL};

	private static ItemStatusHandler<Scroll> handler;

	private String rune;

	public boolean ownedByBook = false;

	{
		stackable = true;
		defaultAction = AC_READ;
	}

	@SuppressWarnings("unchecked")
	public static void initLabels() {
		handler = new ItemStatusHandler<Scroll>(
				(Class<? extends Scroll>[]) scrolls, runes, images);
	}

	public static void save(Bundle bundle) {
		handler.save(bundle);
	}

	@SuppressWarnings("unchecked")
	public static void restore(Bundle bundle) {
		handler = new ItemStatusHandler<Scroll>(
				(Class<? extends Scroll>[]) scrolls, runes, images, bundle);
	}

	public Scroll() {
		super();
		syncVisuals();
	}

	@Override
	public void syncVisuals() {
		image = handler.image(this);
		rune = handler.label(this);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_READ);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_READ)) {

			if (hero.buff(Blindness.class) != null) {
				GLog.w(TXT_BLINDED);
			} else if (hero.buff(UnstableSpellbook.bookRecharge.class) != null
					&& hero.buff(UnstableSpellbook.bookRecharge.class)
					.isCursed()
					&& !(this instanceof ScrollOfRemoveCurse)) {
				GLog.n(TXT_CURSED);
			} else {
				curUser = hero;
				curItem = detach(hero.belongings.backpack);
				doRead();
			}

		} else {

			super.execute(hero, action);

		}
	}

	abstract protected void doRead();

	public boolean isKnown() {
		return handler.isKnown(this);
	}

	public void setKnown() {
		if (!isKnown() && !ownedByBook) {
			handler.know(this);
			updateQuickslot();
		}
	}

	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}

	@Override
	public String name() {
		return isKnown() ? name : Messages.get(this, "unknown_name", rune);
	}

	@Override
	public String info() {
		return isKnown() ? desc()
				: Messages.get(this, "unknown_desc", rune);
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return isKnown();
	}

	public static HashSet<Class<? extends Scroll>> getKnown() {
		return handler.known();
	}

	public static HashSet<Class<? extends Scroll>> getUnknown() {
		return handler.unknown();
	}

	public static boolean allKnown() {
		return handler.known().size() == scrolls.length;
	}

	@Override
	public int price() {
		return 15 * quantity;
	}

	public Integer initials() {
		return isKnown() ? initials : null;
	}
}
