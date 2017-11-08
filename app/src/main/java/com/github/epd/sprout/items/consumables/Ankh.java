
package com.github.epd.sprout.items.consumables;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.DewVial;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Ankh extends Item {

	public static final String AC_BLESS = Messages.get(Ankh.class, "ac_bless");

	public static final String TXT_DESC_NOBLESS = Messages.get(Ankh.class, "desc");
	public static final String TXT_DESC_BLESSED = Messages.get(Ankh.class, "desc_blessed");

	public static final String TXT_BLESS = Messages.get(Ankh.class, "bless");
	public static final String TXT_REVIVE = Messages.get(Ankh.class, "revive");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ANKH;

		// You tell the ankh no, don't revive me, and then it comes back to
		// revive you again in another run.
		// I'm not sure if that's enthusiasm or passive-aggression.
		bones = true;
		stackable = true;
	}

	private Boolean blessed = true;

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		DewVial vial = hero.belongings.getItem(DewVial.class);
		if (vial != null && vial.isFullBless() && !blessed)
			actions.add(AC_BLESS);
		return actions;
	}

	@Override
	public void execute(final Hero hero, String action) {
		if (action.equals(AC_BLESS)) {

			DewVial vial = hero.belongings.getItem(DewVial.class);
			if (vial != null) {
				blessed = true;
				vial.empty();
				GLog.p(TXT_BLESS);
				hero.spend(1f);
				hero.busy();

				Sample.INSTANCE.play(Assets.SND_DRINK);
				CellEmitter.get(hero.pos).start(Speck.factory(Speck.LIGHT),
						0.2f, 3);
				hero.sprite.operate(hero.pos);
			}
		} else {

			super.execute(hero, action);

		}

	}

	@Override
	public String info() {
		if (blessed)
			return TXT_DESC_BLESSED;
		else
			return TXT_DESC_NOBLESS;
	}

	public Boolean isBlessed() {
		return blessed;
	}

	private static final Glowing WHITE = new Glowing(0xFFFFCC);

	@Override
	public Glowing glowing() {
		return isBlessed() ? WHITE : null;
	}

	private static final String BLESSED = "blessed";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(BLESSED, blessed);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		blessed = bundle.getBoolean(BLESSED);
	}

	@Override
	public int price() {
		return 50 * quantity;
	}
}
