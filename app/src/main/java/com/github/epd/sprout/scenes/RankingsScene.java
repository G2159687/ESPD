
package com.github.epd.sprout.scenes;

import com.github.epd.sprout.Rankings;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.effects.Flare;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.Archs;
import com.github.epd.sprout.ui.ExitButton;
import com.github.epd.sprout.ui.Icons;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.windows.WndError;
import com.github.epd.sprout.windows.WndRanking;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.GameMath;

public class RankingsScene extends PixelScene {

	private static final String TXT_TITLE = Messages.get(RankingsScene.class, "title");
	private static final String TXT_TOTAL = Messages.get(RankingsScene.class, "total");
	private static final String TXT_NO_GAMES = Messages.get(RankingsScene.class, "no_games");

	private static final String TXT_NO_INFO = Messages.get(RankingsScene.class, "no_info");

	private static final float ROW_HEIGHT_MAX = 20;
	private static final float ROW_HEIGHT_MIN = 12;

	private static final float MAX_ROW_WIDTH = 160;

	private static final float GAP = 4;

	private Archs archs;

	@Override
	public void create() {

		super.create();

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		archs = new Archs();
		archs.setSize(w, h);
		add(archs);

		Rankings.INSTANCE.load();

		RenderedText title = PixelScene.renderText(TXT_TITLE, 9);
		title.hardlight(Window.SHPX_COLOR);
		title.x = align((w - title.width()) / 2);
		title.y = align(GAP);
		add(title);

		if (Rankings.INSTANCE.records.size() > 0) {

			// attempts to give each record as much space as possible, ideally
			// as much space as portrait mode
			float rowHeight = GameMath.gate(ROW_HEIGHT_MIN,
					(uiCamera.height - 26) / Rankings.INSTANCE.records.size(),
					ROW_HEIGHT_MAX);

			float left = (w - Math.min(MAX_ROW_WIDTH, w)) / 2 + GAP;
			float top = align((h - rowHeight * Rankings.INSTANCE.records.size()) / 2);

			int pos = 0;

			for (Rankings.Record rec : Rankings.INSTANCE.records) {
				Record row = new Record(pos,
						pos == Rankings.INSTANCE.lastRecord, rec);
				float offset = rowHeight <= 14 ? pos % 2 == 1 ? 5 : -5 : 0;
				row.setRect(left + offset, top + pos * rowHeight, w - left * 2,
						rowHeight);
				add(row);

				pos++;
			}

			if (Rankings.INSTANCE.totalNumber >= Rankings.TABLE_SIZE) {
				RenderedText label = PixelScene.renderText(TXT_TOTAL, 8);
				label.hardlight(0xCCCCCC);
				add(label);

				RenderedText won = PixelScene.renderText(
						Integer.toString(Rankings.INSTANCE.wonNumber), 8);
				won.hardlight(Window.SHPX_COLOR);
				add(won);

				RenderedText total = PixelScene.renderText("/"
						+ Rankings.INSTANCE.totalNumber, 8);
				total.hardlight(0xCCCCCC);
				total.x = align((w - total.width()) / 2);
				total.y = align(top + pos * rowHeight + GAP);
				add(total);

				float tw = label.width() + won.width() + total.width();
				label.x = align((w - tw) / 2);
				won.x = label.x + label.width();
				total.x = won.x + won.width();
				label.y = won.y = total.y = align(h - label.height() - GAP);

			}

		} else {

			RenderedText noRec = PixelScene.renderText(TXT_NO_GAMES, 8);
			noRec.hardlight(0xCCCCCC);
			noRec.x = align((w - noRec.width()) / 2);
			noRec.y = align((h - noRec.height()) / 2);
			add(noRec);

		}

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(Camera.main.width - btnExit.width(), 0);
		add(btnExit);

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}

	public static class Record extends Button {

		private static final float GAP = 4;

		private static final int[] TEXT_WIN = {0xFFFF88, 0xB2B25F};
		private static final int[] TEXT_LOSE = {0xDDDDDD, 0x888888};
		private static final int FLARE_WIN = 0x888866;
		private static final int FLARE_LOSE = 0x666666;

		private Rankings.Record rec;

		protected ItemSprite shield;
		private Flare flare;
		private BitmapText position;
		private RenderedTextMultiline desc;
		private Image steps;
		private BitmapText depth;
		private Image classIcon;
		private BitmapText level;

		public Record(int pos, boolean latest, Rankings.Record rec) {
			super();

			this.rec = rec;

			if (latest) {
				flare = new Flare(6, 24);
				flare.angularSpeed = 90;
				flare.color(rec.win ? FLARE_WIN : FLARE_LOSE);
				addToBack(flare);
			}

			if (pos != Rankings.TABLE_SIZE - 1) {
				position.text(Integer.toString(pos + 1));
			} else
				position.text(" ");
			position.measure();

			desc.text(rec.info);


			int odd = pos % 2;

			if (rec.win) {
				shield.view(ItemSpriteSheet.AMULET, null);
				position.hardlight(TEXT_WIN[odd]);
				desc.hardlight(TEXT_WIN[odd]);
				depth.hardlight(TEXT_WIN[odd]);
				level.hardlight(TEXT_WIN[odd]);
			} else {
				position.hardlight(TEXT_LOSE[odd]);
				desc.hardlight(TEXT_LOSE[odd]);
				depth.hardlight(TEXT_LOSE[odd]);
				level.hardlight(TEXT_LOSE[odd]);

				if (rec.depth != 0) {
					depth.text(Integer.toString(rec.depth));
					depth.measure();
					steps.copy(Icons.DEPTH_LG.get());

					add(steps);
					add(depth);
				}

			}

			if (rec.herolevel != 0) {
				level.text(Integer.toString(rec.herolevel));
				level.measure();
				add(level);
			}

			classIcon.copy(Icons.get(rec.heroClass));
		}

		@Override
		protected void createChildren() {

			super.createChildren();

			shield = new ItemSprite(ItemSpriteSheet.TOMB, null);
			add(shield);

			position = new BitmapText(PixelScene.font1x);
			position.alpha(0.8f);
			add(position);

			desc = renderMultiline(7);
			add(desc);

			depth = new BitmapText(PixelScene.font1x);
			depth.alpha(0.8f);

			steps = new Image();

			classIcon = new Image();
			add(classIcon);

			level = new BitmapText(PixelScene.font1x);
			level.alpha(0.8f);
		}

		@Override
		protected void layout() {

			super.layout();

			shield.x = x;
			shield.y = y + (height - shield.height) / 2;

			position.x = align(shield.x + (shield.width - position.width()) / 2);
			position.y = align(shield.y + (shield.height - position.height())
					/ 2 + 1);

			if (flare != null) {
				flare.point(shield.center());
			}

			classIcon.x = x + width - classIcon.width;
			classIcon.y = shield.y;

			level.x = classIcon.x + (classIcon.width - level.width()) / 2f;
			level.y = classIcon.y + (classIcon.height - level.height()) / 2f + 1;
			align(level);

			steps.x = x + width - steps.width - classIcon.width;
			steps.y = shield.y;

			depth.x = steps.x + (steps.width - depth.width()) / 2f;
			depth.y = steps.y + (steps.height - depth.height()) / 2f + 1;
			align(depth);

			desc.maxWidth((int) (steps.x - (shield.x + shield.width + GAP)));
			desc.setPos(shield.x + shield.width + GAP, shield.y + (shield.height - desc.height()) / 2f + 1);
			align(desc);
		}

		@Override
		protected void onClick() {
			if (rec.gameFile.length() > 0) {
				parent.add(new WndRanking(rec.gameFile));
			} else {
				parent.add(new WndError(TXT_NO_INFO));
			}
		}
	}
}
