/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.windows.help;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.windows.WndMessage;
import com.watabou.gltextures.TextureCache;
import com.watabou.utils.FileOperations;

import java.io.IOException;

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
				} catch (IOException e) {
					parent.add(new WndMessage(Messages.get(WndRecoverSave.class, "fail")));
					return;
				}
				parent.add(new WndMessage(Messages.get(WndRecoverSave.class, "success")));
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
