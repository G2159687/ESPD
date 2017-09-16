package com.github.epd.sprout.windows.help;

import com.github.epd.sprout.items.help.item.HelpAdamant;
import com.github.epd.sprout.items.help.item.HelpMrdestructo;
import com.github.epd.sprout.items.help.item.HelpPortalItem;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.Window;
import com.watabou.noosa.Game;

public class WndHlpCatItemMisc extends Window {
	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP = 2;
	private int pos;

	public WndHlpCatItemMisc() {

		super();

		addButton(new NewRedButton(Messages.get(WndHlpCatItemMisc.class, "mrdestructo")) {
			@Override
			protected void onClick() {
				hide();
				Game.scene().add(new WndHelp(new HelpMrdestructo()));
			}
		});

		addButton(new NewRedButton(Messages.get(WndHlpCatItemMisc.class, "adamant")) {
			@Override
			protected void onClick() {
				hide();
				Game.scene().add(new WndHelp(new HelpAdamant()));
			}
		});

		addButton(new NewRedButton(Messages.get(WndHlpCatItemMisc.class, "portalitem")) {
			@Override
			protected void onClick() {
				hide();
				Game.scene().add(new WndHelp(new HelpPortalItem()));
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
