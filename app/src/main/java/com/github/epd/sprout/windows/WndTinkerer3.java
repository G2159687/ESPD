
package com.github.epd.sprout.windows;

import com.github.epd.sprout.actors.mobs.npcs.Tinkerer3;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;

public class WndTinkerer3 extends Window {

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndTinkerer3(final Tinkerer3 tinkerer, final Item item) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item.image(), null));
		titlebar.label(Utils.capitalize(item.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline message = PixelScene
				.renderMultiline("", 6);
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add(message);

		NewRedButton btnUpgrade = new NewRedButton("") {
			@Override
			protected void onClick() {
				selectUpgrade(tinkerer);
			}
		};
		btnUpgrade.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnUpgrade);


		resize(WIDTH, (int) btnUpgrade.bottom());
	}

	private void selectUpgrade(Tinkerer3 tinkerer) {
		hide();
		tinkerer.destroy();
		tinkerer.sprite.die();
	}
}
