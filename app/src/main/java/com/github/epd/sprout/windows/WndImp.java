
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.npcs.Ghost;
import com.github.epd.sprout.actors.mobs.npcs.Imp;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.DwarfToken;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;

public class WndImp extends Window {

	private static final String TXT_MESSAGE = Messages.get(WndImp.class, "msg");
	private static final String TXT_REWARD = Messages.get(WndImp.class, "ok");

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP = 2;

	public WndImp(final Imp imp, final DwarfToken tokens) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(tokens.image(), null));
		titlebar.label(Utils.capitalize(tokens.name()));
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
				takeReward(imp, tokens, Imp.Quest.reward);
			}
		};
		btnReward.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnReward);

		resize(WIDTH, (int) btnReward.bottom());
	}

	private void takeReward(Imp imp, DwarfToken tokens, Item reward) {

		hide();

		tokens.detachAll(Dungeon.hero.belongings.backpack);

		reward.identify();
		if (reward.doPickUp(Dungeon.hero)) {
			GLog.i(Messages.get(Hero.class, "have"), reward.name());
		} else {
			Dungeon.level.drop(reward, imp.pos).sprite.drop();
		}

		if (Dungeon.questTweaks){
			GLog.h(Messages.get(Ghost.class, "challenge"));
		}

		imp.flee();

		Imp.Quest.complete();
	}
}
