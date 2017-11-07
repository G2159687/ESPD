
package com.github.epd.sprout.windows.help;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.windows.WndMessage;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.utils.FileOperations;

public class WndRecoverSave extends Window {

	private static final String TXT_MESSAGE = Messages.get(WndRecoverSave.class, "msg");

	private static final String TXT_YES = Messages.get(WndRecoverSave.class, "yes");
	private static final String TXT_NO = Messages.get(WndRecoverSave.class, "no");

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndRecoverSave() {

		super();

		RenderedTextMultiline message = PixelScene
				.renderMultiline(TXT_MESSAGE, 6);
		message.maxWidth(WIDTH);
		message.setPos(0, GAP);
		add(message);

		NewRedButton btnYes = new NewRedButton(TXT_YES) {
			@Override
			protected void onClick() {
				FileOperations.delete(TextureCache.context.getFilesDir());
				try {
					FileOperations.copyDirectiory(TextureCache.context.getExternalFilesDir(null).getAbsolutePath(), TextureCache.context.getFilesDir().getAbsolutePath());
				} catch (Exception e) {
					parent.add(new WndMessage(Messages.get(WndRecoverSave.class, "fail")));
					return;
				}
				Game.instance.finish();
			}
		};
		btnYes.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnYes);

		NewRedButton btnNo = new NewRedButton(TXT_NO) {
			@Override
			protected void onClick() {
				hide();
			}
		};

		btnNo.setRect(0, btnYes.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnNo);


		resize(WIDTH, (int) btnNo.bottom());
	}

}
