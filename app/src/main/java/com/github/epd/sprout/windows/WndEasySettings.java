
package com.github.epd.sprout.windows;

import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.CheckBox;
import com.watabou.noosa.Group;

/* Easy Settings */

public class WndEasySettings extends WndTabbed {

	private static final int WIDTH = 112;
	private static final int WIDTH_L = 226;
	private static final int BTN_HEIGHT = 20;
	private static final int HEIGHT = 170;
	private static final int HEIGHT_L = 120;
	private static final int SLIDER_HEIGHT = 25;
	private static final int GAP_SML = 2;
	private static final int GAP_LRG = 5;

	private HeroTab herotab;
	private ItemTab itemtab;
	private ActorTab actortab;

	private static int last_index = 0;

	public WndEasySettings() {
		super();

		itemtab = new ItemTab();
		add(itemtab);

		herotab = new HeroTab();
		add(herotab);

		actortab = new ActorTab();
		add(actortab);


		add(new LabeledTab(Messages.get(this, "herotab")) {
			@Override
			protected void select(boolean value) {
				super.select(value);
				herotab.visible = herotab.active = value;
				if (value) last_index = 0;
			}
		});

		add(new LabeledTab(Messages.get(this,"itemtab")) {
			@Override
			protected void select(boolean value) {
				super.select(value);
				itemtab.visible = itemtab.active = value;
				if (value) last_index = 1;
			}
		});

		add(new LabeledTab(Messages.get(this,"actortab")) {
			@Override
			protected void select(boolean value) {
				super.select(value);
				actortab.visible = actortab.active = value;
				if (value) last_index = 2;
			}
		});

		if (!ShatteredPixelDungeon.landscape()) {
			resize(WIDTH, HEIGHT);
		} else {
			resize(WIDTH_L, HEIGHT_L);
		}

		layoutTabs();

		select(last_index);

	}

	private class HeroTab extends Group {

		public HeroTab() {

			CheckBox moreherohp = new CheckBox(Messages.get(WndEasySettings.class, "moreherohp")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.moreHeroHP(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				moreherohp.setRect(0, 0, WIDTH, BTN_HEIGHT);
			} else {
				moreherohp.setRect(0, 0, WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			moreherohp.checked(ShatteredPixelDungeon.moreHeroHP());
			add(moreherohp);

			CheckBox moreheroatk = new CheckBox(Messages.get(WndEasySettings.class, "moreheroatk")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.moreHeroAtk(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				moreheroatk.setRect(0, moreherohp.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			} else {
				moreheroatk.setRect(WIDTH_L / 2 + 1, 0, WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			moreheroatk.checked(ShatteredPixelDungeon.moreHeroAtk());
			add(moreheroatk);

			CheckBox moreherodef = new CheckBox(Messages.get(WndEasySettings.class, "moreherodef")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.moreHeroDef(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				moreherodef.setRect(0, moreheroatk.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			} else {
				moreherodef.setRect(0, moreheroatk.bottom() + GAP_SML, WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			moreherodef.checked(ShatteredPixelDungeon.moreHeroDef());
			add(moreherodef);

			CheckBox moreherostr = new CheckBox(Messages.get(WndEasySettings.class, "moreherostr")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.moreHeroStr(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				moreherostr.setRect(0, moreherodef.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			} else {
				moreherostr.setRect(WIDTH_L / 2 + 1, moreheroatk.bottom() + GAP_SML, WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			moreherostr.checked(ShatteredPixelDungeon.moreHeroStr());
			add(moreherostr);

		}

	}

	private class ItemTab extends Group {
		public ItemTab() {
			super();

			CheckBox moreshops = new CheckBox(Messages.get(WndEasySettings.class, "moreshops")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.moreShops(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				moreshops.setRect(0, 0, WIDTH, BTN_HEIGHT);
			} else {
				moreshops.setRect(0, 0, WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			moreshops.checked(ShatteredPixelDungeon.moreShops());
			add(moreshops);

			CheckBox shopcheaper = new CheckBox(Messages.get(WndEasySettings.class, "shopcheaper")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.ShopCheaper(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				shopcheaper.setRect(0, moreshops.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			} else {
				shopcheaper.setRect(WIDTH_L / 2 + 1, 0, WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			shopcheaper.checked(ShatteredPixelDungeon.ShopCheaper());
			add(shopcheaper);

			CheckBox vialunlimit = new CheckBox(Messages.get(WndEasySettings.class, "vialunlimit")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.VialUnLimit(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				vialunlimit.setRect(0, shopcheaper.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			} else {
				vialunlimit.setRect(0, shopcheaper.bottom(), WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			vialunlimit.checked(ShatteredPixelDungeon.VialUnLimit());
			add(vialunlimit);

			CheckBox superdew = new CheckBox(Messages.get(WndEasySettings.class, "superdew")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.SuperDew(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				superdew.setRect(0, vialunlimit.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			} else {
				superdew.setRect(WIDTH_L / 2 + 1, shopcheaper.bottom(), WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			superdew.checked(ShatteredPixelDungeon.SuperDew());
			add(superdew);

			CheckBox upgradetweaks = new CheckBox(Messages.get(WndEasySettings.class, "upgradetweaks")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.UpgradeTweaks(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				upgradetweaks.setRect(0, superdew.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			} else {
				upgradetweaks.setRect(0, superdew.bottom(), WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			upgradetweaks.checked(ShatteredPixelDungeon.UpgradeTweaks());
			add(upgradetweaks);
		}
	}

	private class ActorTab extends Group {
		public ActorTab() {
			super();

			CheckBox moreloots = new CheckBox(Messages.get(WndEasySettings.class, "moreloots")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.MoreLoots(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				moreloots.setRect(0, 0, WIDTH, BTN_HEIGHT);
			} else {
				moreloots.setRect(0, 0, WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			moreloots.checked(ShatteredPixelDungeon.MoreLoots());
			add(moreloots);

			CheckBox questtweaks = new CheckBox(Messages.get(WndEasySettings.class, "questtweaks")) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.QuestTweaks(checked());
				}
			};
			if (!ShatteredPixelDungeon.landscape()) {
				questtweaks.setRect(0, moreloots.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			} else {
				questtweaks.setRect(WIDTH_L / 2 + 1, 0, WIDTH_L / 2 - 1, BTN_HEIGHT);
			}
			questtweaks.checked(ShatteredPixelDungeon.QuestTweaks());
			add(questtweaks);
		}
	}
}
/*OptionSlider slots = new OptionSlider("", "0", "4", 0, 4) {
				@Override
				protected void onChange() {
					//ShatteredPixelDungeon.quickSlots(getSelectedValue());
				}
			};
			slots.setSelectedValue(ShatteredPixelDungeon.quickSlots());
			if (!ShatteredPixelDungeon.landscape()) {
				slots.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
			} else {
				slots.setRect(0, 0, WIDTH_L / 2 - 1, SLIDER_HEIGHT);
			}
			add(slots);*/