
package com.github.epd.sprout.windows;

import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class WndTitledMessage extends Window {

	protected static final int WIDTH_P = 120;
	protected static final int WIDTH_L = 144;
	protected static final int GAP = 2;

	public WndTitledMessage(Image icon, String title, String message) {

		this(new IconTitle(icon, title), message);

	}

	public WndTitledMessage(Component titlebar, String message) {

		super();

		int width = ShatteredPixelDungeon.landscape() ? WIDTH_L : WIDTH_P;

		titlebar.setRect(0, 0, width, 0);
		add(titlebar);

		RenderedTextMultiline text = PixelScene.renderMultiline(6);
		text.text(message, width);
		text.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add(text);

		resize(width, (int) text.bottom());
	}
}
