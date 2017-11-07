
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.CheckBox;
import com.github.epd.sprout.ui.Window;
import com.watabou.noosa.RenderedText;

import java.util.ArrayList;

public class WndChallenges extends Window {

	private static final int WIDTH = 108;
	private static final int TTL_HEIGHT = 12;
	private static final int BTN_HEIGHT = 18;
	private static final int GAP = 1;

	private boolean editable;
	private ArrayList<CheckBox> boxes;

	public WndChallenges(int checked, boolean editable) {

		super();

		this.editable = editable;

		RenderedText title = PixelScene.renderText(Messages.get(this, "title"), 9);
		title.hardlight(TITLE_COLOR);
		title.x = PixelScene.align(camera, (WIDTH - title.width()) / 2);
		title.y = PixelScene.align(camera, (TTL_HEIGHT - title.height()) / 2);
		PixelScene.align(title);
		add(title);

		boxes = new ArrayList<CheckBox>();

		float pos = TTL_HEIGHT;
		for (int i = 0; i < Challenges.NAMES.length; i++) {

			CheckBox cb = new CheckBox(Challenges.NAMES[i]);
			cb.checked((checked & Challenges.MASKS[i]) != 0);
			cb.active = editable;

			if (i > 0) {
				pos += GAP;
			}
			cb.setRect(0, pos, WIDTH, BTN_HEIGHT);
			pos = cb.bottom();

			add(cb);
			boxes.add(cb);
		}

		resize(WIDTH, (int) pos);
	}

	@Override
	public void onBackPressed() {

		if (editable) {
			int value = 0;
			for (int i = 0; i < boxes.size(); i++) {
				if (boxes.get(i).checked()) {
					value |= Challenges.MASKS[i];
				}
			}
			ShatteredPixelDungeon.challenges(value);
		}

		super.onBackPressed();
	}
}