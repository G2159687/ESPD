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
package com.github.epd.sprout.scenes;

import com.github.epd.sprout.Badges;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.Archs;
import com.github.epd.sprout.ui.ExitButton;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.windows.WndChallenges;
import com.github.epd.sprout.windows.help.HelpIndex;
import com.github.epd.sprout.windows.help.WndHlpCatTS;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;

public class BadgesScene extends PixelScene {

	private NewRedButton btn;
	private NewRedButton btn2;
	private NewRedButton btn3;
	private NewRedButton btn4;
	private NewRedButton btn5;
	private NewRedButton btn6;
	private NewRedButton btn7;
	private NewRedButton btn8;

	@Override
	public void create() {

		String TXT1 = Messages.get(BadgesScene.class, "title");

		super.create();

		final float colWidth = Camera.main.width / (ShatteredPixelDungeon.landscape() ? 2 : 1);
		final float colTop = (Camera.main.height / 2) - (ShatteredPixelDungeon.landscape() ? 30 : 90);

		RenderedText title = renderText(TXT1, 8);
		title.hardlight(0xFFFF00);
		add(title);

		title.x = (colWidth - title.width()) / 2;
		title.y = colTop;
		align(title);

		RenderedTextMultiline text = renderMultiline("", 7);
		text.maxWidth((int) Math.min(colWidth, 120));
		//add( text );

		text.setPos((colWidth - text.width()) / 2, title.y + title.height() + 12);
		//	align(text);

		btn = new NewRedButton(Messages.get(BadgesScene.class, "b1")) {
			@Override
			protected void onClick() {
				BadgesScene.this.add(new WndChallenges(ShatteredPixelDungeon
						.challenges(), true));
			}
		};
		btn.setRect(colWidth / 2 - 50, colTop + 30, 100, 18);
		add(btn);

		btn2 = new NewRedButton(Messages.get(BadgesScene.class, "b2")) {
			@Override
			protected void onClick() {
				Game.scene().add(new HelpIndex());
				Badges.validateSupporter2();
			}
		};
		btn2.setRect(colWidth / 2 - 50, colTop + 50, 100, 18);
		add(btn2);

		btn3 = new NewRedButton(Messages.get(BadgesScene.class, "ts")) {
			@Override
			protected void onClick() {
				Game.scene().add(new WndHlpCatTS());
			}
		};
		btn3.setRect(colWidth / 2 - 50, colTop + 70, 100, 18);
		add(btn3);

/*		btn3= new NewRedButton("Game Settings #3 (Unavailable)") {
            @Override
			protected void onClick() {
			}
		};
		btn3.setRect(colWidth / 2 - 50,colTop + 70,100,18);
		add (btn3);

		btn4= new NewRedButton("Game Settings #4 (Unavailable)") {
			@Override
			protected void onClick() {
			}
		};
		btn4.setRect(colWidth / 2 - 50,colTop + 90,100,18);
		add (btn4);

		btn5= new NewRedButton("Game Settings #5 (Unavailable)") {
			@Override
			protected void onClick() {
			}
		};
		btn5.setRect(colWidth / 2 - 50,colTop + 110,100,18);
		add (btn5);

		btn6= new NewRedButton("Game Settings #6 (Unavailable)") {
			@Override
			protected void onClick() {
			}
		};
		btn6.setRect(colWidth / 2 - 50,colTop + 130,100,18);
		add (btn6);

		btn7= new NewRedButton("Game Settings #7 (Unavailable)") {
			@Override
			protected void onClick() {
			}
		};
		btn7.setRect(colWidth / 2 - 50,colTop + 150,100,18);
		add (btn7);

		btn8= new NewRedButton("Game Settings #8 (Unavailable)") {
			@Override
			protected void onClick() {}
		};
		btn8.setRect(colWidth / 2 - 50,colTop + 170,100,18);
		add (btn8);
*/
		Archs archs = new Archs();
		archs.setSize(Camera.main.width, Camera.main.height);
		addToBack(archs);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(Camera.main.width - btnExit.width(), 0);
		add(btnExit);

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}
}