
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Dewcharge;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.Blackberry;
import com.github.epd.sprout.items.food.Blueberry;
import com.github.epd.sprout.items.food.ChargrilledMeat;
import com.github.epd.sprout.items.food.Cloudberry;
import com.github.epd.sprout.items.food.FrozenCarpaccio;
import com.github.epd.sprout.items.food.FullMoonberry;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.items.food.Moonberry;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.food.Nut;
import com.github.epd.sprout.items.food.ToastedNut;
import com.github.epd.sprout.items.teleporter.JournalPage;
import com.github.epd.sprout.items.teleporter.OtilukesJournal;
import com.github.epd.sprout.items.teleporter.Sokoban1;
import com.github.epd.sprout.items.teleporter.Sokoban2;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.HeroSprite;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.ui.HealthBar;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.PathFinder;

import java.util.Locale;

public class WndHero extends WndTabbed {

	private static final String TXT_STATS = Messages.get(WndHero.class, "stats");
	private static final String TXT_LEVELSTATS = Messages.get(WndHero.class, "levelstats");
	private static final String TXT_BUFFS = Messages.get(WndHero.class, "buffs");
	private static final String TXT_PET = Messages.get(WndHero.class, "pet");

	private static final String TXT_HEALS = Messages.get(WndHero.class, "heals");

	private static final String TXT_EXP = Messages.get(WndHero.class, "exp");
	private static final String TXT_STR = Messages.get(WndHero.class, "str");
	private static final String TXT_BREATH = Messages.get(WndHero.class, "breath");
	private static final String TXT_SPIN = Messages.get(WndHero.class, "spin");
	private static final String TXT_STING = Messages.get(WndHero.class, "sting");
	private static final String TXT_FEATHERS = Messages.get(WndHero.class, "feathers");
	private static final String TXT_SPARKLE = Messages.get(WndHero.class, "sparkle");
	private static final String TXT_FANGS = Messages.get(WndHero.class, "fangs");
	private static final String TXT_ATTACK = Messages.get(WndHero.class, "attack");
	private static final String TXT_HEALTH = Messages.get(WndHero.class, "health");
	private static final String TXT_MOVES2 = Messages.get(WndHero.class, "moves2");
	private static final String TXT_MOVES3 = Messages.get(WndHero.class, "moves3");
	private static final String TXT_MOVES4 = Messages.get(WndHero.class, "moves4");
	private static final String TXT_HUNGER = Messages.get(WndHero.class, "hunger");
	private static final String TXT_MOVES_DEW = Messages.get(WndHero.class, "moves_dew");
	private static final String TXT_ATTACKSKILL = Messages.get(WndHero.class, "attackskill");

	private static final int WIDTH = 100;

	private StatsTab stats;
	private LevelStatsTab levelstats;
	private PetTab pet;
	private BuffsTab buffs;

	private SmartTexture icons;
	private TextureFilm film;


	private PET checkpet() {
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof PET) {
				return (PET) mob;
			}
		}
		return null;
	}

	public WndHero() {

		super();

		icons = TextureCache.get(Assets.BUFFS_LARGE);
		film = new TextureFilm(icons, 16, 16);

		stats = new StatsTab();
		add(stats);

		levelstats = new LevelStatsTab();
		add(levelstats);

		PET heropet = checkpet();

		if (heropet != null) {
			pet = new PetTab(heropet);
			add(pet);
		}

		buffs = new BuffsTab();
		add(buffs);


		add(new LabeledTab(TXT_STATS) {
			@Override
			protected void select(boolean value) {
				super.select(value);
				stats.visible = stats.active = selected;
			}
		});

		add(new LabeledTab(TXT_LEVELSTATS) {
			@Override
			protected void select(boolean value) {
				super.select(value);
				levelstats.visible = levelstats.active = selected;
			}
		});


		if (heropet != null) {
			add(new LabeledTab(TXT_PET) {
				@Override
				protected void select(boolean value) {
					super.select(value);
					pet.visible = pet.active = selected;
				}
			});
		}

		add(new LabeledTab(TXT_BUFFS) {
			@Override
			protected void select(boolean value) {
				super.select(value);
				buffs.visible = buffs.active = selected;
			}
		});

		resize(WIDTH, (int) Math.max(stats.height(), buffs.height()));

		layoutTabs();

		select(0);
	}

	private class StatsTab extends Group {

		private final String TXT_TITLE = Messages.get(WndHero.class, "title");
		private final String TXT_CATALOGUS = Messages.get(WndHero.class, "catalogus");
		private final String TXT_JOURNAL = Messages.get(WndHero.class, "journal");

		private static final int GAP = 5;

		private float pos;

		public StatsTab() {

			Hero hero = Dungeon.hero;

			IconTitle title = new IconTitle();
			title.icon(HeroSprite.avatar(hero.heroClass, hero.tier()));
			title.label(Utils.format(TXT_TITLE, hero.lvl, hero.className())
					.toUpperCase(Locale.ENGLISH), 9);
			title.color(Dungeon.challenges > 0 ? EASY_COLOR : Window.SHPX_COLOR);
			title.setRect(0, 0, WIDTH, 0);
			add(title);

			NewRedButton btnCatalogus = new NewRedButton(TXT_CATALOGUS) {
				@Override
				protected void onClick() {
					hide();
					GameScene.show(new WndCatalogus());
				}

				@Override
				protected boolean onLongClick() {
					Hero heroToBuff = Dungeon.hero;
					if (Level.water[heroToBuff.pos] && heroToBuff.belongings.armor == null) {
						heroToBuff.heroClass.playtest(heroToBuff);
					}
					return true;
				}
			};
			btnCatalogus.setRect(0, title.height(),
					btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2);
			add(btnCatalogus);

			NewRedButton btnJournal = new NewRedButton(TXT_JOURNAL) {
				@Override
				protected void onClick() {
					hide();
					GameScene.show(new WndJournal());
				}
			};
			btnJournal.setRect(btnCatalogus.right() + 1, btnCatalogus.top(),
					btnJournal.reqWidth() + 2, btnJournal.reqHeight() + 2);
			add(btnJournal);

			pos = btnJournal.bottom() + GAP;

			statSlot(TXT_STR, hero.STR());
			statSlot(TXT_HEALTH, (hero.HP + hero.SHLD) + "/" + hero.HT);
			statSlot(TXT_EXP, hero.exp + "/" + hero.maxExp());
			statSlot(TXT_ATTACKSKILL, hero.attackSkill + "/" + hero.defenseSkill);

			if (Dungeon.hero.buff(Hunger.class) != null) {
				statSlot(TXT_HUNGER,
						(100 - Math.round(((float) Dungeon.hero.buff(Hunger.class).hungerLevel()) / 7f)) + "%");
			}


			pos += GAP;


		}

		private void statSlot(String label, String value) {

			RenderedText txt = PixelScene.renderText(label, 8);
			txt.y = pos;
			add(txt);

			txt = PixelScene.renderText(value, 8);
			txt.x = PixelScene.align(WIDTH * 0.65f);
			txt.y = pos;
			add(txt);

			pos += GAP + txt.baseLine();
		}

		private void statSlot(String label, int value) {
			statSlot(label, Integer.toString(value));
		}

		public float height() {
			return pos;
		}
	}

	private class LevelStatsTab extends Group {

		private final String TXT_TITLE = Messages.get(WndHero.class, "title");
		private final String TXT_CATALOGUS = Messages.get(WndHero.class, "catalogus");
		private final String TXT_JOURNAL = Messages.get(WndHero.class, "journal");

		private static final int GAP = 5;

		private float pos;

		public LevelStatsTab() {

			Hero hero = Dungeon.hero;

			IconTitle title = new IconTitle();
			title.icon(HeroSprite.avatar(hero.heroClass, hero.tier()));
			title.label(Utils.format(TXT_TITLE, hero.lvl, hero.className())
					.toUpperCase(Locale.ENGLISH), 9);
			title.color(Window.SHPX_COLOR);
			title.setRect(0, 0, WIDTH, 0);
			add(title);

			NewRedButton btnCatalogus = new NewRedButton(TXT_CATALOGUS) {
				@Override
				protected void onClick() {
					hide();
					GameScene.show(new WndCatalogus());
				}

				@Override
				protected boolean onLongClick() {
					Hero heroToBuff = Dungeon.hero;
					heroToBuff.heroClass.playtest(heroToBuff);
					GLog.i(Messages.get(WndHero.class, "test"));
					Dungeon.hero.HT = Dungeon.hero.HP = 999;
					Dungeon.hero.STR = Dungeon.hero.STR + 20;
					OtilukesJournal jn = new OtilukesJournal();
					jn.collect();
					JournalPage sk1 = new Sokoban1();
					sk1.collect();
					JournalPage sk2 = new Sokoban2();
					sk2.collect();
					return true;
				}
			};
			btnCatalogus.setRect(0, title.height(),
					btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2);
			add(btnCatalogus);

			NewRedButton btnJournal = new NewRedButton(TXT_JOURNAL) {
				@Override
				protected void onClick() {
					hide();
					GameScene.show(new WndJournal());
				}
			};
			btnJournal.setRect(btnCatalogus.right() + 1, btnCatalogus.top(),
					btnJournal.reqWidth() + 2, btnJournal.reqHeight() + 2);
			add(btnJournal);

			pos = btnCatalogus.bottom() + GAP;


			if (Dungeon.depth < 26) {
				statSlot(TXT_MOVES2, Dungeon.level.currentmoves);
				statSlot(TXT_MOVES3, Dungeon.pars[Dungeon.depth]);
				statSlot(TXT_MOVES4, Statistics.prevfloormoves);
				if (Dungeon.hero.buff(Dewcharge.class) != null) {
					int dewration = Dungeon.hero.buff(Dewcharge.class).dispTurnsInt();
					statSlot(TXT_MOVES_DEW, dewration);
				}
			}


			pos += GAP;
		}

		private void statSlot(String label, String value) {

			RenderedText txt = PixelScene.renderText(label, 8);
			txt.y = pos;
			add(txt);

			txt = PixelScene.renderText(value, 8);
			txt.x = PixelScene.align(WIDTH * 0.65f);
			txt.y = pos;
			add(txt);

			pos += GAP + txt.baseLine();
		}

		private void statSlot(String label, int value) {
			statSlot(label, Integer.toString(value));
		}

		public float height() {
			return pos;
		}
	}


	private class BuffsTab extends Group {

		private static final int GAP = 2;

		private float pos;

		public BuffsTab() {
			for (Buff buff : Dungeon.hero.buffs()) {
				if (buff.icon() != BuffIndicator.NONE) {
					BuffSlot slot = new BuffSlot(buff);
					slot.setRect(0, pos, WIDTH, slot.icon.height());
					add(slot);
					pos += GAP + slot.height();
				}
			}
		}

		public float height() {
			return pos;
		}

		private class BuffSlot extends Button {
			private Buff buff;
			Image icon;
			RenderedText txt;

			public BuffSlot(Buff buff) {
				super();
				this.buff = buff;
				int index = buff.icon();

				icon = new Image(icons);
				icon.frame(film.get(index));
				icon.y = this.y;
				add(icon);

				txt = PixelScene.renderText(buff.toString(), 8);
				txt.x = icon.width + GAP;
				txt.y = this.y + (int) (icon.height - txt.baseLine()) / 2;
				add(txt);
			}

			@Override
			protected void layout() {
				super.layout();
				icon.y = this.y;
				txt.x = icon.width + GAP;
				txt.y = pos + (int) (icon.height - txt.baseLine()) / 2;
			}

			@Override
			protected void onClick() {
				GameScene.show(new WndInfoBuff(buff));
			}
		}


	}

	private class PetTab extends Group {

		private final String TXT_TITLE = Messages.get(WndHero.class, "p_title");
		private final String TXT_FEED = Messages.get(WndHero.class, "p_feed");
		private final String TXT_CALL = Messages.get(WndHero.class, "p_call");
		private final String TXT_STAY = Messages.get(WndHero.class, "p_stay");
		private final String TXT_RELEASE = Messages.get(WndHero.class, "p_release");
		private final String TXT_SELECT = Messages.get(WndHero.class, "p_select");
		private final String TXT_SPEED = Messages.get(WndHero.class, "p_speed");

		private CharSprite image;
		private RenderedText name;
		private HealthBar health;
		private BuffIndicator buffs;

		private static final int GAP = 5;

		private float pos;


		public PetTab(final PET heropet) {

			name = PixelScene.renderText(Utils.capitalize(heropet.name), 9);
			name.hardlight(Dungeon.challenges > 0 ? EASY_COLOR : TITLE_COLOR);
			//add(name);

			image = heropet.sprite();
			add(image);

			health = new HealthBar();
			health.level((float) heropet.HP / heropet.HT);
			add(health);

			buffs = new BuffIndicator(heropet);
			add(buffs);


			IconTitle title = new IconTitle();
			title.icon(image);
			title.label(Utils.format(TXT_TITLE, heropet.level, heropet.name).toUpperCase(Locale.ENGLISH), 9);
			title.color(Window.SHPX_COLOR);
			title.setRect(0, 0, WIDTH, 0);
			add(title);

			NewRedButton btnFeed = new NewRedButton(TXT_FEED) {
				@Override
				protected void onClick() {
					hide();
					GameScene.selectItem(itemSelector, WndBag.Mode.ALL, TXT_SELECT);
				}
			};
			btnFeed.setRect(0, title.height(),
					btnFeed.reqWidth() + 2, btnFeed.reqHeight() + 2);
			add(btnFeed);

			NewRedButton btnCall = new NewRedButton(TXT_CALL) {
				@Override
				protected void onClick() {
					hide();
					heropet.callback = true;
					heropet.stay = false;
					heropet.enemy = null;
				}
			};
			btnCall.setRect(btnFeed.right() + 1, btnFeed.top(),
					btnCall.reqWidth() + 2, btnCall.reqHeight() + 2);
			add(btnCall);

			NewRedButton btnStay = new NewRedButton(heropet.stay ? TXT_RELEASE : TXT_STAY) {
				@Override
				protected void onClick() {
					hide();
					heropet.stay = !heropet.stay;
				}
			};
			btnStay.setRect(btnCall.right() + 1, btnCall.top(),
					btnStay.reqWidth() + 2, btnStay.reqHeight() + 2);

			add(btnStay);


			pos = btnStay.bottom() + GAP;

			statSlot(TXT_ATTACK, heropet.attackSkill(null));
			statSlot(TXT_SPEED, Dungeon.petHasteLevel);
			statSlot(TXT_HEALTH, heropet.HP + "/" + heropet.HT);
			statSlot(TXT_EXP, heropet.level < 100 ? heropet.experience + "/" + (5 + heropet.level * 5) : Messages.get(WndHero.class, "max"));
			if (heropet.type == 4 || heropet.type == 5 || heropet.type == 6 || heropet.type == 7 || heropet.type == 12) {
				statSlot(TXT_BREATH, heropet.cooldown == 0 ? Messages.get(WndHero.class, "p_ready") : (Math.round((1000 - heropet.cooldown) / 10) + "%"));
			} else if (heropet.type == 1) {
				statSlot(TXT_SPIN, heropet.cooldown == 0 ? Messages.get(WndHero.class, "p_ready") : (Math.round((1000 - heropet.cooldown) / 10) + "%"));
			} else if (heropet.type == 3) {
				statSlot(TXT_FEATHERS, heropet.cooldown == 0 ? Messages.get(WndHero.class, "p_ready") : (Math.round((1000 - heropet.cooldown) / 10) + "%"));
			} else if (heropet.type == 8) {
				statSlot(TXT_STING, heropet.cooldown == 0 ? Messages.get(WndHero.class, "p_ready") : (Math.round((1000 - heropet.cooldown) / 10) + "%"));
			} else if (heropet.type == 10 || heropet.type == 11) {
				statSlot(TXT_SPARKLE, heropet.cooldown == 0 ? Messages.get(WndHero.class, "p_ready") : (Math.round((1000 - heropet.cooldown) / 10) + "%"));
			} else if (heropet.type == 9) {
				statSlot(TXT_FANGS, heropet.cooldown == 0 ? Messages.get(WndHero.class, "p_ready") : (Math.round((1000 - heropet.cooldown) / 10) + "%"));
			}

			pos += GAP;


		}

		private void statSlot(String label, String value) {

			RenderedText txt = PixelScene.renderText(label, 8);
			txt.y = pos;
			add(txt);

			txt = PixelScene.renderText(value, 8);
			txt.x = PixelScene.align(WIDTH * 0.65f);
			txt.y = pos;
			add(txt);

			pos += GAP + txt.baseLine();
		}

		private void statSlot(String label, int value) {
			statSlot(label, Integer.toString(value));
		}

		public float height() {
			return pos;
		}
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {
				feed(item);
			}
		}
	};

	private boolean checkpetNear() {
		for (int n : PathFinder.NEIGHBOURS8) {
			int c = Dungeon.hero.pos + n;
			if (Actor.findChar(c) instanceof PET) {
				return true;
			}
		}
		return false;
	}

	private void feed(Item item) {

		PET heropet = checkpet();
		boolean nomnom = false;
		if (heropet != null) {
			nomnom = checkFood(heropet.type, item);
		}
		boolean nearby = checkpetNear();

		if (nomnom && nearby) {
			int effect = heropet.HT - heropet.HP;
			if (effect > 0) {
				heropet.HP = heropet.HT;
				heropet.sprite.emitter().burst(Speck.factory(Speck.HEALING), 2);
				heropet.sprite.showStatus(CharSprite.POSITIVE, TXT_HEALS, effect);
			}
			heropet.cooldown = 1;
			item.detach(Dungeon.hero.belongings.backpack);
			GLog.i(Messages.get(WndHero.class, "p_eat", item.name()));
		} else if (!nearby) {
			GLog.w(Messages.get(WndHero.class, "p_far"));
		} else {
			GLog.w(Messages.get(WndHero.class, "p_reject", item.name()));

		}
	}

	private boolean checkFood(Integer petType, Item item) {
		boolean nomnom = false;

		if (petType == 1) { //Spider
			if (item instanceof Nut) {
				nomnom = true;
			}
		}

		if (petType == 2) { //steel bee
			if (item instanceof Blackberry
					|| item instanceof Blueberry
					|| item instanceof Cloudberry
					|| item instanceof Moonberry
					|| item instanceof FullMoonberry
					) {
				nomnom = true;
			}
		}
		if (petType == 3) {//Velocirooster
			if (item instanceof Plant.Seed
					|| item instanceof Nut
					|| item instanceof ToastedNut
					) {
				nomnom = true;
			}
		}
		if (petType == 4) {//red dragon - fire
			if (item instanceof Meat
					|| item instanceof ChargrilledMeat
					|| item instanceof FrozenCarpaccio
					|| item instanceof MysteryMeat
					) {
				nomnom = true;
			}
		}

		if (petType == 5) {//green dragon - lit
			if (item instanceof Meat
					|| item instanceof ChargrilledMeat
					|| item instanceof Plant.Seed
					|| item instanceof Nut
					|| item instanceof ToastedNut
					|| item instanceof Blackberry
					|| item instanceof Blueberry
					|| item instanceof Cloudberry
					|| item instanceof Moonberry
					|| item instanceof FullMoonberry
					) {
				nomnom = true;
			}
		}

		if (petType == 6) {//violet dragon - poison
			if (item instanceof Meat
					|| item instanceof ChargrilledMeat
					|| item instanceof FrozenCarpaccio
					|| item instanceof MysteryMeat
					|| item instanceof Nut
					|| item instanceof ToastedNut
					) {
				nomnom = true;
			}
		}
		if (petType == 7) {//blue dragon - ice
			if (item instanceof Meat
					|| item instanceof ChargrilledMeat
					|| item instanceof FrozenCarpaccio
					|| item instanceof Plant.Seed
					) {
				nomnom = true;
			}
		}

		if (petType == 8) { //scorpion
			if (item instanceof Meat
					|| item instanceof ChargrilledMeat
					|| item instanceof FrozenCarpaccio
					|| item instanceof MysteryMeat
					) {
				nomnom = true;
			}
		}

		if (petType == 9) {//Vorpal Bunny
			if (item instanceof Meat
					|| item instanceof ChargrilledMeat
					|| item instanceof FrozenCarpaccio
					|| item instanceof MysteryMeat
					) {
				nomnom = true;
			}
		}
		if (petType == 10) {//Fairy
			if (item instanceof Blackberry
					|| item instanceof Blueberry
					|| item instanceof Cloudberry
					|| item instanceof Moonberry
					|| item instanceof FullMoonberry
					) {
				nomnom = true;
			}
		}
		if (petType == 11) {//Sugarplum Fairy
			if (item instanceof Blackberry
					|| item instanceof Blueberry
					|| item instanceof Cloudberry
					|| item instanceof Moonberry
					|| item instanceof FullMoonberry
					) {
				nomnom = true;
			}
		}
		if (petType == 12) {//shadow dragon - non elemental
			if (item instanceof Meat
					|| item instanceof ChargrilledMeat
					|| item instanceof FrozenCarpaccio
					|| item instanceof Plant.Seed
					|| item instanceof Blackberry
					|| item instanceof Blueberry
					|| item instanceof Cloudberry
					|| item instanceof Moonberry
					|| item instanceof FullMoonberry
					|| item instanceof MysteryMeat
					|| item instanceof Nut
					|| item instanceof ToastedNut
					) {
				nomnom = true;
			}
		}
		return nomnom;
	}

}
