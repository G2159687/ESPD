
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Journal;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.Icons;
import com.github.epd.sprout.ui.ScrollPane;
import com.github.epd.sprout.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

import java.util.Collections;

public class WndJournal extends Window {

	private static final int WIDTH = 112;
	private static final int HEIGHT_P = 160;
	private static final int HEIGHT_L = 144;

	private static final int ITEM_HEIGHT = 18;

	private static final String TXT_TITLE = Messages.get(WndJournal.class, "journal");

	private RenderedText txtTitle;
	private ScrollPane list;

	public WndJournal() {

		super();
		resize(WIDTH, ShatteredPixelDungeon.landscape() ? HEIGHT_L : HEIGHT_P);

		txtTitle = PixelScene.renderText(TXT_TITLE, 9);
		txtTitle.hardlight(Window.TITLE_COLOR);
		txtTitle.x = PixelScene.align(PixelScene.uiCamera,
				(WIDTH - txtTitle.width()) / 2);
		add(txtTitle);

		Component content = new Component();

		Collections.sort(Journal.records);

		float pos = 0;
		for (Journal.Record rec : Journal.records) {
			ListItem item = new ListItem(rec.feature, rec.depth);
			item.setRect(0, pos, WIDTH, ITEM_HEIGHT);
			content.add(item);

			pos += item.height();
		}

		content.setSize(WIDTH, pos);

		list = new ScrollPane(content);
		add(list);

		list.setRect(0, txtTitle.height(), WIDTH, height - txtTitle.height());
	}

	private static class ListItem extends Component {

		private RenderedText feature;
		private BitmapText depth;

		private Image icon;

		public ListItem(Journal.Feature f, int d) {
			super();

			feature.text(f.desc);

			depth.text(Integer.toString(d));
			depth.measure();

			if (d == Dungeon.depth) {
				feature.hardlight(TITLE_COLOR);
				depth.hardlight(TITLE_COLOR);
			}
		}

		@Override
		protected void createChildren() {
			feature = PixelScene.renderText(9);
			add(feature);

			depth = new BitmapText(PixelScene.font1x);
			add(depth);

			icon = Icons.get(Icons.DEPTH);
			add(icon);
		}

		@Override
		protected void layout() {

			icon.x = width - icon.width;

			depth.x = icon.x - 1 - depth.width();
			depth.y = PixelScene.align(y + (height - depth.height()) / 2);

			icon.y = depth.y - 1;

			feature.y = PixelScene.align(depth.y + depth.baseLine()
					- feature.baseLine());
		}
	}
}
