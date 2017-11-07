
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.QuickSlotButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;

import static com.github.epd.sprout.ui.QuickSlotButton.refresh;

public class WndDewVial extends Window {

	//if people don't get it after this, I quit. I just quit.

	private static final String TXT_MESSAGE = Messages.get(WndDewVial.class, "msg");
	private static final String TXT_WINDOW = Messages.get(WndDewVial.class, "window");
	private static final String TXT_DRINK = Messages.get(WndDewVial.class, "drink");
	private static final String TXT_WATER = Messages.get(WndDewVial.class, "water");
	private static final String TXT_SPLASH = Messages.get(WndDewVial.class, "splash");
	private static final String TXT_BLESS = Messages.get(WndDewVial.class, "bless");
	private static final String TXT_OTHER = Messages.get(WndDewVial.class, "other");


	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndDewVial(final Item item, final int slotNum) {

		super();

		final WndBag.Listener itemSelector = new WndBag.Listener() {
			@Override
			public void onSelect(Item item) {
				if (item != null) {
					Dungeon.quickslot.setSlot(slotNum, item);
					refresh();
				}
			}
		};

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item.image(), null));
		titlebar.label(Utils.capitalize(item.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline message = PixelScene
				.renderMultiline(TXT_MESSAGE, 6);
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add(message);

		NewRedButton btnWindow = new NewRedButton(TXT_WINDOW) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.dewmode(0);
				hide();
			}
		};
		btnWindow.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnWindow);

		NewRedButton btnDrink = new NewRedButton(TXT_DRINK) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.dewmode(1);
				hide();
			}
		};
		btnDrink.setRect(0, btnWindow.top() + btnWindow.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnDrink);

		NewRedButton btnWater = new NewRedButton(TXT_WATER) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.dewmode(2);
				hide();
			}
		};
		btnWater.setRect(0, btnDrink.top() + btnDrink.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnWater);

		NewRedButton btnSplash = new NewRedButton(TXT_SPLASH) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.dewmode(3);
				hide();
			}
		};
		btnSplash.setRect(0, btnWater.top() + btnWater.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnSplash);

		NewRedButton btnBless = new NewRedButton(TXT_BLESS) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.dewmode(4);
				hide();
			}
		};
		btnBless.setRect(0, btnSplash.top() + btnSplash.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnBless);

		NewRedButton btnOther = new NewRedButton(TXT_OTHER) {
			@Override
			protected void onClick() {
				hide();
				GameScene.selectItem(itemSelector, WndBag.Mode.QUICKSLOT, QuickSlotButton.TXT_SELECT_ITEM);
			}
		};
		btnOther.setRect(0, btnBless.top() + btnBless.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnOther);

		resize(WIDTH, (int) btnOther.bottom());

	}




}
