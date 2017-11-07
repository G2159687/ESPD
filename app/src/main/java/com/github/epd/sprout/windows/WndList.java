
package com.github.epd.sprout.windows;

import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;

public class WndList extends Window {

	private static final int WIDTH = 120;
	private static final int MARGIN = 4;
	private static final int GAP = 4;

	private static final String DOT = "\u007F";

	public WndList(String[] items) {

		super();

		float pos = MARGIN;
		float dotWidth = 0;
		float maxWidth = 0;

		for (int i = 0; i < items.length; i++) {

			if (i > 0) {
				pos += GAP;
			}

			BitmapText dot = PixelScene.createText(DOT, 6);
			dot.x = MARGIN;
			dot.y = pos;
			if (dotWidth == 0) {
				dot.measure();
				dotWidth = dot.width();
			}
			add(dot);

			BitmapTextMultiline item = PixelScene.createMultiline(items[i], 6);
			item.x = dot.x + dotWidth;
			item.y = pos;
			item.maxWidth = (int) (WIDTH - MARGIN * 2 - dotWidth);
			item.measure();
			add(item);

			pos += item.height();
			float w = item.width();
			if (w > maxWidth) {
				maxWidth = w;
			}
		}

		resize((int) (maxWidth + dotWidth + MARGIN * 2), (int) (pos + MARGIN));
	}
}
