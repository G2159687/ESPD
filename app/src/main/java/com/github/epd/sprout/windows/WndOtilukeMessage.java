
package com.github.epd.sprout.windows;

import com.github.epd.sprout.items.Palantir;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;

public class WndOtilukeMessage extends Window {

	private static final String TXT_MESSAGE = Messages.get(WndOtilukeMessage.class, "msg");
	private static final String TXT_REWARD = Messages.get(WndOtilukeMessage.class, "ok");

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP = 2;

	public WndOtilukeMessage() {

		super();

		Palantir palantir = new Palantir();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(palantir.image(), null));
		titlebar.label(Utils.capitalize(palantir.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline message = PixelScene
				.renderMultiline(TXT_MESSAGE, 6);
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add(message);

		NewRedButton btnReward = new NewRedButton(TXT_REWARD) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnReward.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnReward);

		resize(WIDTH, (int) btnReward.bottom());
	}


}
