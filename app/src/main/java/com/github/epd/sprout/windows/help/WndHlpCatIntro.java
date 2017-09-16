package com.github.epd.sprout.windows.help;

import com.github.epd.sprout.items.help.HelpIntro;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.windows.WndMessage;
import com.watabou.noosa.Game;

public class WndHlpCatIntro extends Window {
	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP = 2;
	private int pos;

	public WndHlpCatIntro() {

		super();

		addButton(new NewRedButton(Messages.get(WndHlpCatIntro.class, "1")) {
			@Override
			protected void onClick() {
				hide();
				Game.scene().add(new WndHelp(new HelpIntro()));
			}
		});

		addButton(new NewRedButton(Messages.get(WndHlpCatIntro.class, "3")) {
			@Override
			protected void onClick() {
				Game.scene().add(new WndMessage(Messages.get(WndHlpCatIntro.class, "3_detail")));
				hide();
			}
		});

		addButton(new NewRedButton(Messages.get(WndHlpCatIntro.class, "2")) {
			@Override
			protected void onClick() {
			}
		});

		resize(WIDTH, pos);
	}

	private void addButton(NewRedButton btn) {
		add(btn);
		btn.setRect(0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT);
		pos += BTN_HEIGHT;
	}
}
