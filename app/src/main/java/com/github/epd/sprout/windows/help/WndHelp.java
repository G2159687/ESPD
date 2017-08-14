package com.github.epd.sprout.windows.help;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.IconTitle;

public class WndHelp extends Window{

    private static final float GAP = 2;

    private static final int WIDTH = 120;

    public WndHelp(Item item) {
        super();

        IconTitle titlebar = new IconTitle();
        titlebar.icon(new ItemSprite(item));
        titlebar.label(Utils.capitalize(item.toString()));
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        RenderedTextMultiline info = PixelScene.renderMultiline(item.info(), 6);
        info.maxWidth(WIDTH);
        info.setPos(titlebar.left(), titlebar.bottom() + GAP);
        add(info);

        float y = info.top() + info.height() +GAP;
        float x = 0;

        resize(WIDTH, (int) y);
    }
}
