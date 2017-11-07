
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.buffs.Levitation;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.misc.Towel;
import com.github.epd.sprout.items.misc.Whistle;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.food.Food;
import com.github.epd.sprout.items.misc.AutoPotion;
import com.github.epd.sprout.items.misc.Spectacles;
import com.github.epd.sprout.items.teleporter.OtilukesJournal;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Game;

public class WndOtiluke extends Window {

	public static final float TIME_TO_USE = 1;

	private static final int PAGES = 20;
	private static final int WIDTH = 60;
	private static final int WIDTH2 = 120;
	private static final int BTN_HEIGHT = 18;
	private static final float GAP = 2;

	public WndOtiluke(final boolean[] rooms, final OtilukesJournal item) {

		super();

		String[] roomNames = new String[PAGES];
		roomNames[0] = Messages.get(WndOtiluke.class, "s0");
		roomNames[1] = Messages.get(WndOtiluke.class, "s1");
		roomNames[2] = Messages.get(WndOtiluke.class, "s2");
		roomNames[3] = Messages.get(WndOtiluke.class, "s3");
		roomNames[4] = Messages.get(WndOtiluke.class, "s4");
		roomNames[5] = Messages.get(WndOtiluke.class, "s5");
		roomNames[6] = Messages.get(WndOtiluke.class, "s6");
		roomNames[7] = Messages.get(WndOtiluke.class, "s7");
		roomNames[8] = Messages.get(WndOtiluke.class, "s8");
		roomNames[9] = Messages.get(WndOtiluke.class, "s9");
		roomNames[10] = Messages.get(WndOtiluke.class, "s10");
		roomNames[11] = Messages.get(WndOtiluke.class, "s11");
		roomNames[12] = Messages.get(WndOtiluke.class, "s12");
		roomNames[13] = Messages.get(WndOtiluke.class, "s13");
		roomNames[14] = Messages.get(WndOtiluke.class, "s14");
		roomNames[15] = Messages.get(WndOtiluke.class, "s15");
		roomNames[16] = Messages.get(WndOtiluke.class, "s16");

		RenderedTextMultiline message = PixelScene.renderMultiline(Messages.get(WndOtiluke.class, "where"), 6);
		message.maxWidth(WIDTH2);
		message.setPos(0, 0);
		add(message);

		NewRedButton btn1 = new NewRedButton(roomNames[0]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(0, item.firsts[0]);
				if (!Dungeon.playtest) {
					item.firsts[0] = false;
				}
			}
		};
		btn1.setRect(0, message.top() + message.height() + GAP, WIDTH - 1, BTN_HEIGHT);
		if (rooms[0]) {
			add(btn1);
		}

		NewRedButton btn2 = new NewRedButton(roomNames[1]) {
			@Override
			protected void onClick() {
				if (item.firsts[1]){
					getPrize(1, item);
				} else {
					item.returnDepth = Dungeon.depth;
					item.returnPos = Dungeon.hero.pos;
					port(1, item.firsts[1]);
					if (!Dungeon.playtest) {
						item.firsts[1] = false;
					}
				}
			}
		};
		btn2.setRect(WIDTH + 1, message.top() + message.height() + GAP, WIDTH - 1, BTN_HEIGHT);
		if (rooms[1]) {
			add(btn2);
		}

		NewRedButton btn3 = new NewRedButton(roomNames[2]) {
			@Override
			protected void onClick() {
				if (item.firsts[2]){
					getPrize(2, item);
				} else {
					item.returnDepth = Dungeon.depth;
					item.returnPos = Dungeon.hero.pos;
					port(2, item.firsts[2]);
					if (!Dungeon.playtest) {
						item.firsts[2] = false;
					}
				}
			}
		};
		btn3.setRect(0, message.top() + message.height() + GAP * 2 + BTN_HEIGHT, WIDTH - 1, BTN_HEIGHT);
		if (rooms[2]) {
			add(btn3);
		}

		NewRedButton btn4 = new NewRedButton(roomNames[3]) {
			@Override
			protected void onClick() {
				if (item.firsts[3]){
					getPrize(3, item);
				} else {
					item.returnDepth = Dungeon.depth;
					item.returnPos = Dungeon.hero.pos;
					port(3, item.firsts[3]);
					if (!Dungeon.playtest) {
						item.firsts[3] = false;
					}
				}
			}
		};
		btn4.setRect(WIDTH + 1, message.top() + message.height() + GAP * 2 + BTN_HEIGHT, WIDTH - 1, BTN_HEIGHT);
		if (rooms[3]) {
			add(btn4);
		}

		NewRedButton btn5 = new NewRedButton(roomNames[4]) {
			@Override
			protected void onClick() {
				if (item.firsts[4]){
					getPrize(4, item);
				} else {
					item.returnDepth = Dungeon.depth;
					item.returnPos = Dungeon.hero.pos;
					port(4, item.firsts[4]);
					if (!Dungeon.playtest) {
						item.firsts[4] = false;
					}
				}
			}
		};
		btn5.setRect(0, message.top() + message.height() + GAP * 3 + BTN_HEIGHT * 2, WIDTH - 1, BTN_HEIGHT);
		if (rooms[4]) {
			add(btn5);
		}

		NewRedButton btn6 = new NewRedButton(roomNames[5]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(5, item.firsts[5]);
				if (!Dungeon.playtest) {
					item.firsts[5] = false;
				}
			}
		};
		btn6.setRect(WIDTH + 1, message.top() + message.height() + GAP * 3 + BTN_HEIGHT * 2, WIDTH - 1, BTN_HEIGHT);
		if (rooms[5]) {
			add(btn6);
		}

		NewRedButton btn7 = new NewRedButton(roomNames[6]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(6, item.firsts[6]);
				if (!Dungeon.playtest) {
					item.firsts[6] = false;
				}
			}
		};
		btn7.setRect(0, message.top() + message.height() + GAP * 4 + BTN_HEIGHT * 3, WIDTH - 1, BTN_HEIGHT);
		if (rooms[6]) {
			add(btn7);
		}

		NewRedButton btn8 = new NewRedButton(roomNames[7]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(7, item.firsts[7]);
				if (!Dungeon.playtest) {
					item.firsts[7] = false;
				}
			}
		};
		btn8.setRect(WIDTH + 1, message.top() + message.height() + GAP * 4 + BTN_HEIGHT * 3, WIDTH - 1, BTN_HEIGHT);
		if (rooms[7]) {
			add(btn8);
		}

		NewRedButton btn9 = new NewRedButton(roomNames[8]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(8, item.firsts[8]);
				if (!Dungeon.playtest) {
					item.firsts[8] = false;
				}
			}
		};
		btn9.setRect(0, message.top() + message.height() + GAP * 5 + BTN_HEIGHT * 4, WIDTH - 1, BTN_HEIGHT);
		if (rooms[8]) {
			add(btn9);
		}

		NewRedButton btn10 = new NewRedButton(roomNames[9]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(9, item.firsts[9]);
				if (!Dungeon.playtest) {
					item.firsts[9] = false;
				}
			}
		};
		btn10.setRect(WIDTH + 1, message.top() + message.height() + GAP * 5 + BTN_HEIGHT * 4, WIDTH - 1, BTN_HEIGHT);
		if (rooms[9]) {
			add(btn10);
		}

		NewRedButton btn11 = new NewRedButton(roomNames[10]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(10, item.firsts[10]);
				if (!Dungeon.playtest) {
					item.firsts[10] = false;
				}
			}
		};
		btn11.setRect(0, message.top() + message.height() + GAP * 6 + BTN_HEIGHT * 5, WIDTH - 1, BTN_HEIGHT);
		if (rooms[10]) {
			add(btn11);
		}

		NewRedButton btn12 = new NewRedButton(roomNames[11]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(11, item.firsts[11]);
				if (!Dungeon.playtest) {
					item.firsts[11] = false;
				}
			}
		};
		btn12.setRect(WIDTH + 1, message.top() + message.height() + GAP * 6 + BTN_HEIGHT * 5, WIDTH - 1, BTN_HEIGHT);
		if (rooms[11]) {
			add(btn12);
		}

		NewRedButton btn13 = new NewRedButton(roomNames[12]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(12, item.firsts[12]);
				if (!Dungeon.playtest) {
					item.firsts[12] = false;
				}
			}
		};
		btn13.setRect(0, message.top() + message.height() + GAP * 7 + BTN_HEIGHT * 6, WIDTH - 1, BTN_HEIGHT);
		if (rooms[12]) {
			add(btn13);
		}

		NewRedButton btn14 = new NewRedButton(roomNames[13]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(13, item.firsts[13]);
				if (!Dungeon.playtest) {
					item.firsts[13] = false;
				}
			}
		};
		btn14.setRect(WIDTH + 1, message.top() + message.height() + GAP * 7 + BTN_HEIGHT * 6, WIDTH - 1, BTN_HEIGHT);
		if (rooms[13]) {
			add(btn14);
		}

		NewRedButton btn15 = new NewRedButton(roomNames[14]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(14, item.firsts[14]);
				if (!Dungeon.playtest) {
					item.firsts[14] = false;
				}
			}
		};
		btn15.setRect(0, message.top() + message.height() + GAP * 8 + BTN_HEIGHT * 7, WIDTH - 1, BTN_HEIGHT);
		if (rooms[14]) {
			add(btn15);
		}

		NewRedButton btn16 = new NewRedButton(roomNames[15]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(15, item.firsts[15]);
				if (!Dungeon.playtest) {
					item.firsts[15] = false;
				}
			}
		};
		btn16.setRect(WIDTH + 1, message.top() + message.height() + GAP * 8 + BTN_HEIGHT * 7, WIDTH - 1, BTN_HEIGHT);
		if (rooms[15]) {
			add(btn16);
		}

		NewRedButton btn17 = new NewRedButton(roomNames[16]) {
			@Override
			protected void onClick() {
				item.returnDepth = Dungeon.depth;
				item.returnPos = Dungeon.hero.pos;
				port(16, item.firsts[16]);
				if (!Dungeon.playtest) {
					item.firsts[16] = false;
				}
			}
		};
		btn17.setRect(0, message.top() + message.height() + GAP * 9 + BTN_HEIGHT * 8, WIDTH - 1, BTN_HEIGHT);
		if (rooms[16]) {
			add(btn17);
		}

		resize(WIDTH2, (int)btn17.bottom());
	}


	private void port(int room, boolean first) {

		Hero hero = Dungeon.hero;
		hero.spend(TIME_TO_USE);

		Buff buff = Dungeon.hero
				.buff(TimekeepersHourglass.timeFreeze.class);
		if (buff != null)
			buff.detach();

		Buff buff2 = Dungeon.hero.buff(Levitation.class);
		if (buff2 != null)
			buff2.detach();

		Buff buffinv = Dungeon.hero.buff(Invisibility.class);
		if (buffinv != null)
			buffinv.detach();
		Invisibility.dispel();
		Dungeon.hero.invisible = 0;

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
			if (mob instanceof DriedRose.GhostHero)
				mob.destroy();

		switch (room){
			case 1:case 2:case 3:case 4:case 5:case 6:case 7:
				InterlevelScene.mode = InterlevelScene.Mode.JOURNAL;
				break;
			case 8:
				InterlevelScene.mode = InterlevelScene.Mode.PORTSEWERS;
				break;
			case 9:
				InterlevelScene.mode = InterlevelScene.Mode.PORTPRISON;
				break;
			case 10:
				InterlevelScene.mode = InterlevelScene.Mode.PORTCAVES;
				break;
			case 11:
				InterlevelScene.mode = InterlevelScene.Mode.PORTCITY;
				break;
			case 12:
				InterlevelScene.mode = InterlevelScene.Mode.PORTHALLS;
				break;
			case 13:
				InterlevelScene.mode = InterlevelScene.Mode.PORTCOIN;
				break;
			case 14:
				InterlevelScene.mode = InterlevelScene.Mode.PORTBONE;
				break;
			case 15:
				InterlevelScene.mode = InterlevelScene.Mode.PORTCRAB;
				break;
			case 16:
				InterlevelScene.mode = InterlevelScene.Mode.PORTTENGU;
				break;
		}

		InterlevelScene.returnDepth = Dungeon.depth;
		InterlevelScene.returnPos = Dungeon.hero.pos;
		InterlevelScene.journalpage = room;
		InterlevelScene.first = first;
		Game.switchScene(InterlevelScene.class);

	}

	private void getPrize(final int portnum, final OtilukesJournal item) {
		hide();
		GameScene.show(
				new WndOptions(Messages.get(WndOtiluke.class, "sokoban_title"),
						Messages.get(WndOtiluke.class, "sokoban_msg"),
						Messages.titleCase(Messages.get(WndOtiluke.class, "sokoban_yes")),
						Messages.titleCase(Messages.get(WndOtiluke.class, "sokoban_no"))) {

					@Override
					protected void onSelect(int index) {

						Item prize;
						if (index == 0) {
							switch (portnum) {
								case 1:
									prize = new Towel();
									break;
								case 2:
									prize = new Spectacles().identify();
									break;
								case 3:
									prize = new AutoPotion().identify();
									break;
								case 4:
									prize = new Whistle().identify();
									break;
								default:
									prize = new Food().identify();
									break;
							}
							Dungeon.level.drop(prize, Dungeon.hero.pos).sprite.drop();
							item.firsts[portnum] = false;
						} else {
							item.returnDepth = Dungeon.depth;
							item.returnPos = Dungeon.hero.pos;
							port(portnum, item.firsts[portnum]);
							item.firsts[portnum] = false;
						}
					}
				});
	}
}
