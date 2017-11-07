
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.EquipableItem;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.ItemSlot;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;

public class WndItem extends Window {

	private static final float BUTTON_WIDTH = 36;
	private static final float BUTTON_HEIGHT = 16;

	private static final float GAP = 2;

	private static final int WIDTH = 120;

	public WndItem(final WndBag owner, final Item item) {
		this(owner, item, owner != null);
	}

	public WndItem(final WndBag owner, final Item item, final boolean options) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item));
		titlebar.label(Utils.capitalize(item.toString()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		if (item.levelKnown && item.level > 0) {
			titlebar.color(ItemSlot.UPGRADED);
		} else if (item.levelKnown && item.level < 0) {
			titlebar.color(ItemSlot.DEGRADED);
		}

		RenderedTextMultiline info = PixelScene.renderMultiline(item.info(), 6);
		info.maxWidth(WIDTH);
		info.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add(info);

		float y = info.top() + info.height() + GAP;
		float x = 0;

		if (Dungeon.hero.isAlive() && options) {
			for (final String action : item.actions(Dungeon.hero)) {

				NewRedButton btn = new NewRedButton(action) {
					@Override
					protected void onClick() {
						item.execute(Dungeon.hero, action);
						hide();
						if (owner != null) owner.hide();
						if (action.equals(EquipableItem.AC_UNEQUIP)) {
							GameScene.show(new WndBag(Dungeon.hero.belongings.backpack, null, WndBag.Mode.ALL, null));
						}
					}
				};
				btn.setSize(Math.max(BUTTON_WIDTH, btn.reqWidth()),
						BUTTON_HEIGHT);
				if (x + btn.width() > WIDTH) {
					x = 0;
					y += BUTTON_HEIGHT + GAP;
				}
				btn.setPos(x, y);
				add(btn);

				if (action == item.defaultAction) {
					btn.textColor(TITLE_COLOR);
				}

				x += btn.width() + GAP;
			}
		}

		resize(WIDTH, (int) (y + (x > 0 ? BUTTON_HEIGHT : 0)));
	}
}
