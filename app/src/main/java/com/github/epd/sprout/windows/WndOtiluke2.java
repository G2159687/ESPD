
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.watabou.noosa.Game;

import java.io.IOException;

public class WndOtiluke2 extends Window {

	private static final String TXT_MESSAGE = Messages.get(WndOtiluke2.class, "msg");

	private static final int WIDTH = 130;
	private static final int BTN_HEIGHT = 20;
	private static final int BTN_WIDTH = 20;
	private static final int GAP_WIDTH = 22;
	private static final float GAP = 2;

	public WndOtiluke2() {

		super();

		RenderedTextMultiline message = PixelScene
				.renderMultiline(TXT_MESSAGE, 6);
		message.maxWidth(WIDTH);
		message.setPos(0, 0);
		add(message);

		NewRedButton btn1 = new NewRedButton("1") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 2;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn1.setRect(0, message.top() + message.height() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 1)
			add(btn1);

		NewRedButton btn2 = new NewRedButton("2") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 3;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn2.setRect(GAP_WIDTH, message.top() + message.height() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 2)
			add(btn2);

		NewRedButton btn3 = new NewRedButton("3") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 4;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn3.setRect(GAP_WIDTH * 2, message.top() + message.height() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 3)
			add(btn3);

		NewRedButton btn4 = new NewRedButton("4") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 5;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn4.setRect(GAP_WIDTH * 3, message.top() + message.height() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 4)
			add(btn4);

		NewRedButton btn5 = new NewRedButton("5") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 6;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn5.setRect(GAP_WIDTH * 4, message.top() + message.height() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 5)
			add(btn5);

		NewRedButton btn6 = new NewRedButton("6") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 7;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn6.setRect(GAP_WIDTH * 5, message.top() + message.height() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 6)
			add(btn6);

		NewRedButton btn7 = new NewRedButton("7") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 8;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn7.setRect(0, btn1.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 7)
			add(btn7);

		NewRedButton btn8 = new NewRedButton("8") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 9;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn8.setRect(GAP_WIDTH, btn1.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 8)
			add(btn8);

		NewRedButton btn9 = new NewRedButton("9") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 10;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn9.setRect(GAP_WIDTH * 2, btn1.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 9)
			add(btn9);

		NewRedButton btn10 = new NewRedButton("10") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 11;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn10.setRect(GAP_WIDTH * 3, btn1.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 10)
			add(btn10);

		NewRedButton btn11 = new NewRedButton("11") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 12;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn11.setRect(GAP_WIDTH * 4, btn1.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 11)
			add(btn11);

		NewRedButton btn12 = new NewRedButton("12") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 13;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn12.setRect(GAP_WIDTH * 5, btn1.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 12)
			add(btn12);

		NewRedButton btn13 = new NewRedButton("13") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 14;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn13.setRect(0, btn12.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 13)
			add(btn13);

		NewRedButton btn14 = new NewRedButton("14") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 15;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn14.setRect(GAP_WIDTH, btn12.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 14)
			add(btn14);

		NewRedButton btn15 = new NewRedButton("15") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 16;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn15.setRect(GAP_WIDTH * 2, btn12.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 15)
			add(btn15);

		NewRedButton btn16 = new NewRedButton("16") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 17;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn16.setRect(GAP_WIDTH * 3, btn12.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 16)
			add(btn16);

		NewRedButton btn17 = new NewRedButton("17") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 18;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn17.setRect(GAP_WIDTH * 4, btn12.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 17)
			add(btn17);

		NewRedButton btn18 = new NewRedButton("18") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 19;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn18.setRect(GAP_WIDTH * 5, btn12.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 18)
			add(btn18);

		NewRedButton btn19 = new NewRedButton("19") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 20;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn19.setRect(0, btn18.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 19)
			add(btn19);

		NewRedButton btn20 = new NewRedButton("20") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 21;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn20.setRect(GAP_WIDTH, btn18.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 20)
			add(btn20);

		NewRedButton btn21 = new NewRedButton("21") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 22;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn21.setRect(GAP_WIDTH * 2, btn18.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 21)
			add(btn21);

		NewRedButton btn22 = new NewRedButton("22") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 23;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn22.setRect(GAP_WIDTH * 3, btn18.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 22)
			add(btn22);

		NewRedButton btn23 = new NewRedButton("23") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 24;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn23.setRect(GAP_WIDTH * 4, btn18.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 23)
			add(btn23);

		NewRedButton btn24 = new NewRedButton("24") {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				Dungeon.depth = 25;
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btn24.setRect(GAP_WIDTH * 5, btn18.bottom() + GAP, BTN_WIDTH, BTN_HEIGHT);
		if (Statistics.deepestFloor > 23)
			add(btn24);

		resize(WIDTH, (int) btn24.bottom());

	}

}
