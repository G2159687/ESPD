
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class WndInfoBuff extends Window {

	private static final float GAP = 2;

	private static final int WIDTH = 120;

	private SmartTexture icons;
	private TextureFilm film;

	public WndInfoBuff(Buff buff) {
		super();

		IconTitle titlebar = new IconTitle();

		icons = TextureCache.get(Assets.BUFFS_LARGE);
		film = new TextureFilm(icons, 16, 16);

		Image buffIcon = new Image(icons);
		buffIcon.frame(film.get(buff.icon()));

		titlebar.icon(buffIcon);
		titlebar.label(Messages.titleCase(buff.toString()), Window.TITLE_COLOR);
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline txtInfo = PixelScene.renderMultiline(buff.desc(), 6);
		txtInfo.maxWidth(WIDTH);
		txtInfo.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add(txtInfo);

		resize(WIDTH, (int) (txtInfo.top() + txtInfo.height()));
	}
}
