
package com.github.epd.sprout.windows;

import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.ScrollPane;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndCatalogus extends WndTabbed {

	private static final int WIDTH_P = 112;
	private static final int HEIGHT_P = 160;

	private static final int WIDTH_L = 128;
	private static final int HEIGHT_L = 128;

	private static final int ITEM_HEIGHT = 18;

	private static final int TAB_WIDTH = 50;

	private static final String TXT_POTIONS = Messages.get(WndCatalogus.class, "potions");
	private static final String TXT_SCROLLS = Messages.get(WndCatalogus.class, "scrolls");
	private static final String TXT_TITLE = Messages.get(WndCatalogus.class, "title");

	private RenderedText txtTitle;
	private ScrollPane list;

	private ArrayList<ListItem> items = new ArrayList<WndCatalogus.ListItem>();

	private static boolean showPotions = true;

	public WndCatalogus() {

		super();

		if (ShatteredPixelDungeon.landscape()) {
			resize(WIDTH_L, HEIGHT_L);
		} else {
			resize(WIDTH_P, HEIGHT_P);
		}

		txtTitle = PixelScene.renderText(TXT_TITLE, 9);
		txtTitle.hardlight(Window.TITLE_COLOR);
		add(txtTitle);

		list = new ScrollPane(new Component()) {
			@Override
			public void onClick(float x, float y) {
				int size = items.size();
				for (int i = 0; i < size; i++) {
					if (items.get(i).onClick(x, y)) {
						break;
					}
				}
			}
		};
		add(list);
		list.setRect(0, txtTitle.height(), width, height - txtTitle.height());

		boolean showPotions = WndCatalogus.showPotions;
		Tab[] tabs = {new LabeledTab(TXT_POTIONS) {
			@Override
			protected void select(boolean value) {
				super.select(value);
				WndCatalogus.showPotions = value;
				updateList();
			}
		}, new LabeledTab(TXT_SCROLLS) {
			@Override
			protected void select(boolean value) {
				super.select(value);
				WndCatalogus.showPotions = !value;
				updateList();
			}
		}};
		for (Tab tab : tabs) {
			add(tab);
		}

		layoutTabs();

		select(showPotions ? 0 : 1);
	}

	private void updateList() {

		txtTitle.text(Utils.format(TXT_TITLE, showPotions ? TXT_POTIONS
				: TXT_SCROLLS));
		txtTitle.x = PixelScene.align(PixelScene.uiCamera,
				(width - txtTitle.width()) / 2);

		items.clear();

		Component content = list.content();
		content.clear();
		list.scrollTo(0, 0);

		float pos = 0;
		for (Class<? extends Item> itemClass : showPotions ? Potion.getKnown()
				: Scroll.getKnown()) {
			ListItem item = new ListItem(itemClass);
			item.setRect(0, pos, width, ITEM_HEIGHT);
			content.add(item);
			items.add(item);

			pos += item.height();
		}

		for (Class<? extends Item> itemClass : showPotions ? Potion
				.getUnknown() : Scroll.getUnknown()) {
			ListItem item = new ListItem(itemClass);
			item.setRect(0, pos, width, ITEM_HEIGHT);
			content.add(item);
			items.add(item);

			pos += item.height();
		}

		content.setSize(width, pos);
	}

	private static class ListItem extends Component {

		private Item item;
		private boolean identified;

		private ItemSprite sprite;
		private RenderedText label;

		public ListItem(Class<? extends Item> cl) {
			super();

			try {
				item = cl.newInstance();
				if (identified = item.isIdentified()) {
					sprite.view(item.image(), null);
					label.text(item.name());
				} else {
					sprite.view(127, null);
					label.text(item.trueName());
					label.hardlight(0xCCCCCC);
				}
			} catch (Exception e) {
				// Do nothing
			}
		}

		@Override
		protected void createChildren() {
			sprite = new ItemSprite();
			add(sprite);

			label = PixelScene.renderText(8);
			add(label);
		}

		@Override
		protected void layout() {
			sprite.y = PixelScene.align(y + (height - sprite.height) / 2);

			label.x = sprite.x + sprite.width;
			label.y = PixelScene.align(y + (height - label.baseLine()) / 2);
		}

		public boolean onClick(float x, float y) {
			if (identified && inside(x, y)) {
				GameScene.show(new WndInfoItem(item));
				return true;
			} else {
				return false;
			}
		}
	}
}
