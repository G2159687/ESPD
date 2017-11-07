
package com.github.epd.sprout.windows;

import com.github.epd.sprout.actors.mobs.npcs.Tinkerer1;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;

public class WndTinkerer extends Window {

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndTinkerer(final Tinkerer1 tinkerer, final Item item) {

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

		NewRedButton btnBattle = new NewRedButton("") {
			@Override
			protected void onClick() {
				selectUpgrade(tinkerer, 1);
			}
		};
		btnBattle.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnBattle);

		NewRedButton btnNonBattle = new NewRedButton("") {
			@Override
			protected void onClick() {
				selectUpgrade(tinkerer, 2);
			}
		};

		btnNonBattle.setRect(0, btnBattle.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnNonBattle);

		NewRedButton btnNonBattle2 = new NewRedButton("") {
			@Override
			protected void onClick() {
				GameScene.show(new WndDewDrawInfo(item));
			}
		};
		btnNonBattle2.setRect(0, btnNonBattle.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnNonBattle2);

		resize(WIDTH, (int) btnNonBattle2.bottom());
	}

	private void selectUpgrade(Tinkerer1 tinkerer, int type) {
		hide();
		tinkerer.destroy();
		tinkerer.sprite.die();
	}
}
