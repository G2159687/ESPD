
package com.github.epd.sprout.scenes;

import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.Archs;
import com.github.epd.sprout.ui.ExitButton;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.watabou.noosa.Camera;
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

/*		btn = new NewRedButton(Messages.get(BadgesScene.class, "b1")) {
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

		btn3= new NewRedButton("Game Settings #3 (Unavailable)") {
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