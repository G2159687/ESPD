/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.items;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.IconTitle;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Weightstone extends Item {

	private static final String TXT_SELECT_WEAPON = Messages.get(Weightstone.class,"select");
	private static final String TXT_LIGHT = Messages.get(Weightstone.class,"light");
	private static final String TXT_HEAVY = Messages.get(Weightstone.class,"heavy");

	private static final float TIME_TO_APPLY = 2;

	private static final String AC_APPLY = Messages.get(Weightstone.class,"ac_apply");

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.WEIGHT;

		stackable = true;

		bones = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_APPLY);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action == AC_APPLY) {

			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.WEAPON,
					TXT_SELECT_WEAPON);

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

	private void apply(Weapon weapon, boolean forSpeed) {

		detach(curUser.belongings.backpack);

		if (forSpeed) {
			weapon.imbue = Weapon.Imbue.LIGHT;
			GLog.p(TXT_LIGHT, weapon.name());
		} else {
			weapon.imbue = Weapon.Imbue.HEAVY;
			GLog.p(TXT_HEAVY, weapon.name());
		}

		curUser.sprite.operate(curUser.pos);
		Sample.INSTANCE.play(Assets.SND_MISS);

		curUser.spend(TIME_TO_APPLY);
		curUser.busy();
	}

	@Override
	public int price() {
		return 40 * quantity;
	}

	@Override
	public String info() {
		return Messages.get(this,"desc");
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {
				GameScene.show(new WndBalance((Weapon) item));
			}
		}
	};

	public class WndBalance extends Window {

		private final String TXT_CHOICE = Messages.get(Weightstone.class,"choice");

		private final String TXT_LIGHT = Messages.get(Weightstone.class,"lighter");
		private final String TXT_HEAVY = Messages.get(Weightstone.class,"heavier");
		private final String TXT_CANCEL = Messages.get(Weightstone.class,"cancel");

		private static final int WIDTH = 120;
		private static final int MARGIN = 2;
		private static final int BUTTON_WIDTH = WIDTH - MARGIN * 2;
		private static final int BUTTON_HEIGHT = 20;

		public WndBalance(final Weapon weapon) {
			super();

			IconTitle titlebar = new IconTitle(weapon);
			titlebar.setRect(0, 0, WIDTH, 0);
			add(titlebar);

			RenderedTextMultiline tfMesage = PixelScene.renderMultiline( Messages.get(Weightstone.class, "choice"), 8 );
			tfMesage.maxWidth(WIDTH - MARGIN * 2);
			tfMesage.setPos(MARGIN, titlebar.bottom() + MARGIN);
			add( tfMesage );

			float pos = tfMesage.top() + tfMesage.height();

			if (weapon.imbue != Weapon.Imbue.LIGHT) {
				NewRedButton btnSpeed = new NewRedButton(TXT_LIGHT) {
					@Override
					protected void onClick() {
						hide();
						Weightstone.this.apply(weapon, true);
					}
				};
				btnSpeed.setRect(MARGIN, pos + MARGIN, BUTTON_WIDTH,
						BUTTON_HEIGHT);
				add(btnSpeed);

				pos = btnSpeed.bottom();
			}

			if (weapon.imbue != Weapon.Imbue.HEAVY) {
				NewRedButton btnAccuracy = new NewRedButton(TXT_HEAVY) {
					@Override
					protected void onClick() {
						hide();
						Weightstone.this.apply(weapon, false);
					}
				};
				btnAccuracy.setRect(MARGIN, pos + MARGIN, BUTTON_WIDTH,
						BUTTON_HEIGHT);
				add(btnAccuracy);

				pos = btnAccuracy.bottom();
			}

			NewRedButton btnCancel = new NewRedButton(TXT_CANCEL) {
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel
					.setRect(MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
			add(btnCancel);

			resize(WIDTH, (int) btnCancel.bottom() + MARGIN);
		}

		protected void onSelect(int index) {
		}
	}
}