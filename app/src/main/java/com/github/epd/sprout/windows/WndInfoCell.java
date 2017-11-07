
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.watabou.noosa.Image;

public class WndInfoCell extends Window {

	private static final float GAP = 2;

	private static final int WIDTH = 120;

	private static final String TXT_NOTHING = Messages.get(WndInfoCell.class, "nothing");

	public WndInfoCell(int cell) {

		super();

		int tile = Dungeon.level.map[cell];
		if (Level.water[cell]) {
			tile = Terrain.WATER;
		} else if (Level.pit[cell]) {
			tile = Terrain.CHASM;
		}

		IconTitle titlebar = new IconTitle();
		if (tile == Terrain.WATER) {
			Image water = new Image(Dungeon.level.waterTex());
			water.frame(0, 0, DungeonTilemap.SIZE, DungeonTilemap.SIZE);
			titlebar.icon(water);
		} else {
			titlebar.icon(DungeonTilemap.tile(tile));
		}
		titlebar.label(Dungeon.level.tileName(tile));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline info = PixelScene.renderMultiline(6);
		add(info);

		StringBuilder desc = new StringBuilder(Dungeon.level.tileDesc(tile));

		for (Blob blob : Dungeon.level.blobs.values()) {
			if (blob.volume > 0 && blob.cur[cell] > 0 && blob.tileDesc() != null) {
				if (desc.length() > 0) {
					desc.append("\n\n");
				}
				desc.append(blob.tileDesc());
			}
		}

		info.text(desc.length() > 0 ? desc.toString() : TXT_NOTHING);
		info.maxWidth(WIDTH);
		info.setPos(titlebar.left(), titlebar.bottom() + GAP);

		resize(WIDTH, (int) (info.top() + info.height()));
	}
}
