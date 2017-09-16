package com.github.epd.sprout.windows.help;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.Window;
import com.watabou.noosa.Game;

public class HelpIndex extends Window {

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP = 2;
	private int pos;

	public HelpIndex() {

		super();

		addButton(new NewRedButton(Messages.get(HelpIndex.class, "intro")) {
			@Override
			protected void onClick() {
				hide();
				Game.scene().add(new WndHlpCatIntro());
			}
		});

		addButton(new NewRedButton(Messages.get(HelpIndex.class, "faq")) {
			@Override
			protected void onClick() {
				hide();
				Game.scene().add(new WndHlpCatMec());
			}
		});

		addButton(new NewRedButton(Messages.get(HelpIndex.class, "items")) {
			@Override
			protected void onClick() {
				hide();
				Game.scene().add(new WndHlpCatItem());
			}
		});

		addButton(new NewRedButton(Messages.get(HelpIndex.class, "actors")) {
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
