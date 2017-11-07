
package com.github.epd.sprout.windows;

import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;

public class WndMessage extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;
	private static final int MARGIN = 4;

	public WndMessage(String text) {

		super();

		RenderedTextMultiline info = PixelScene.renderMultiline(text, 6);
		info.maxWidth((ShatteredPixelDungeon.landscape() ? WIDTH_L : WIDTH_P) - MARGIN * 2);
		info.setPos(MARGIN, MARGIN);
		add(info);

		resize(
				(int) info.width() + MARGIN * 2,
				(int) info.height() + MARGIN * 2);
	}
}
