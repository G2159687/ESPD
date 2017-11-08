
package com.github.epd.sprout.items.consumables;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.particles.PurpleParticle;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Stylus extends Item {

	{
		defaultAction = AC_INSCRIBE;
	}

	private static final String TXT_SELECT_ARMOR = Messages.get(Stylus.class, "prompt");
	private static final String TXT_INSCRIBED = Messages.get(Stylus.class, "inscribed");

	private static final float TIME_TO_INSCRIBE = 2;

	private static final String AC_INSCRIBE = Messages.get(Stylus.class, "ac_inscribe");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.STYLUS;

		stackable = true;

		bones = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_INSCRIBE);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action == AC_INSCRIBE) {

			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.ARMOR,
					TXT_SELECT_ARMOR);

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	private void inscribe(Armor armor) {

		detach(curUser.belongings.backpack);

		GLog.w(TXT_INSCRIBED, armor.name());

		armor.inscribe();

		curUser.sprite.operate(curUser.pos);
		curUser.sprite.centerEmitter().start(PurpleParticle.BURST, 0.05f, 10);
		Sample.INSTANCE.play(Assets.SND_BURNING);

		curUser.spend(TIME_TO_INSCRIBE);
		curUser.busy();
	}

	@Override
	public int price() {
		return 30 * quantity;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {
				Stylus.this.inscribe((Armor) item);
			}
		}
	};
}
