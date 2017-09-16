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

import android.opengl.GLES20;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Badges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.FogOfWar;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.BannerSprites;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.EmoIcon;
import com.github.epd.sprout.effects.Flare;
import com.github.epd.sprout.effects.FloatingText;
import com.github.epd.sprout.effects.Ripple;
import com.github.epd.sprout.effects.SpellSprite;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Honeypot;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.bags.PotionBandolier;
import com.github.epd.sprout.items.bags.ScrollHolder;
import com.github.epd.sprout.items.bags.SeedPouch;
import com.github.epd.sprout.items.bags.WandHolster;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.wands.WandOfBlink;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.RegularLevel;
import com.github.epd.sprout.levels.features.Chasm;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.DiscardedItemSprite;
import com.github.epd.sprout.sprites.HeroSprite;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.PlantSprite;
import com.github.epd.sprout.ui.ActionIndicator;
import com.github.epd.sprout.ui.AttackIndicator;
import com.github.epd.sprout.ui.Banner;
import com.github.epd.sprout.ui.BusyIndicator;
import com.github.epd.sprout.ui.GameLog;
import com.github.epd.sprout.ui.HealthIndicator;
import com.github.epd.sprout.ui.LootIndicator;
import com.github.epd.sprout.ui.QuickSlotButton;
import com.github.epd.sprout.ui.ResumeIndicator;
import com.github.epd.sprout.ui.StatusPane;
import com.github.epd.sprout.ui.Toast;
import com.github.epd.sprout.ui.Toolbar;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.github.epd.sprout.windows.WndBag.Mode;
import com.github.epd.sprout.windows.WndGame;
import com.github.epd.sprout.windows.WndStory;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;

public class GameScene extends PixelScene {

	private static final String TXT_WELCOME = Messages.get(GameScene.class, "welcome");
	private static final String TXT_WELCOME_BACK = Messages.get(GameScene.class, "welcome_back");

	private static final String TXT_CHASM = Messages.get(GameScene.class, "chasm");
	private static final String TXT_WATER = Messages.get(GameScene.class, "water");
	private static final String TXT_GRASS = Messages.get(GameScene.class, "grass");
	private static final String TXT_DARK = Messages.get(GameScene.class, "dark");
	private static final String TXT_SECRETS = Messages.get(GameScene.class, "secrets");

	static GameScene scene;

	private SkinnedBlock water;
	private DungeonTilemap tiles;
	private FogOfWar fog;
	private HeroSprite hero;

	private GameLog log;

	private BusyIndicator busy;

	private static CellSelector cellSelector;

	private Group terrain;
	private Group ripples;
	private Group plants;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;

	private Toolbar toolbar;
	private Toast prompt;

	private AttackIndicator attack;
	private LootIndicator loot;
	private ActionIndicator action;
	private ResumeIndicator resume;

	public Group ghostHP;

	@Override
	public void create() {

		Music.INSTANCE.play(Assets.TUNE, true);
		Music.INSTANCE.volume(1f);

		ShatteredPixelDungeon.lastClass(Dungeon.hero.heroClass.ordinal());

		super.create();
		Camera.main.zoom(GameMath.gate(minZoom, defaultZoom + ShatteredPixelDungeon.zoom(), maxZoom));

		scene = this;

		terrain = new Group();
		add(terrain);

		water = new SkinnedBlock(Level.getWidth() * DungeonTilemap.SIZE,
				Level.HEIGHT * DungeonTilemap.SIZE, Dungeon.level.waterTex()) {
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				//water has no alpha component, this improves performance
				GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ZERO);
				super.draw();
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			}
		};
		terrain.add(water);

		tiles = new DungeonTilemap();
		terrain.add(tiles);

		ripples = new Group();
		terrain.add(ripples);

		Dungeon.level.addVisuals(this);

		plants = new Group();
		add(plants);

		int size = Dungeon.level.plants.size();
		for (int i = 0; i < size; i++) {
			addPlantSprite(Dungeon.level.plants.valueAt(i));
		}

		heaps = new Group();
		add(heaps);

		size = Dungeon.level.heaps.size();
		for (int i = 0; i < size; i++) {
			addHeapSprite(Dungeon.level.heaps.valueAt(i));
		}

		emitters = new Group();
		effects = new Group();
		emoicons = new Group();

		mobs = new Group();
		add(mobs);

		ghostHP = new Group();

		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite(mob);
		}

		add(emitters);
		add(effects);

		gases = new Group();
		add(gases);

		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite(blob);
		}

		fog = new FogOfWar(Level.getWidth(), Level.HEIGHT);
		add(fog);

		spells = new Group();
		add(spells);

		statuses = new Group();
		add(statuses);

		add(emoicons);

		hero = new HeroSprite();
		hero.place(Dungeon.hero.pos);
		hero.updateArmor();
		mobs.add(hero);

		add(new HealthIndicator());

		add(ghostHP);

		add(cellSelector = new CellSelector(tiles));

		StatusPane sb = new StatusPane();
		sb.camera = uiCamera;
		sb.setSize(uiCamera.width, 0);
		add(sb);

		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect(0, uiCamera.height - toolbar.height(), uiCamera.width,
				toolbar.height());
		add(toolbar);

		attack = new AttackIndicator();
		attack.camera = uiCamera;
		add(attack);

		loot = new LootIndicator();
		loot.camera = uiCamera;
		add(loot);

		action = new ActionIndicator();
		action.camera = uiCamera;
		add(action);

		resume = new ResumeIndicator();
		resume.camera = uiCamera;
		add(resume);

		log = new GameLog();
		log.camera = uiCamera;
		add(log);

		layoutTags();

		if (Dungeon.depth < Statistics.deepestFloor)
			GLog.i(TXT_WELCOME_BACK, Dungeon.depth);
		else
			GLog.i(TXT_WELCOME, Dungeon.depth);
		Sample.INSTANCE.play(Assets.SND_DESCEND);
		switch (Dungeon.level.feeling) {
			case CHASM:
				GLog.w(TXT_CHASM);
				break;
			case WATER:
				GLog.w(TXT_WATER);
				break;
			case GRASS:
				GLog.w(TXT_GRASS);
				break;
			case DARK:
				GLog.w(TXT_DARK);
				break;
			default:
		}
		if (Dungeon.level instanceof RegularLevel
				&& ((RegularLevel) Dungeon.level).secretDoors > Random
				.IntRange(3, 4)) {
			GLog.w(TXT_SECRETS);
		}

		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = sb.bottom() + 1;
		add(busy);

		switch (InterlevelScene.mode) {
			case RESURRECT:
				WandOfBlink.appear(Dungeon.hero, Dungeon.level.entrance);
				new Flare(8, 32).color(0xFFFF66, true).show(hero, 2f);
				break;
			case RETURN:
				WandOfBlink.appear(Dungeon.hero, Dungeon.hero.pos);
				break;
			case FALL:
				Chasm.heroLand();
				break;
			case PALANTIR:
				WndStory.showChapter(WndStory.ID_ZOT);
				break;
			case DESCEND:
				switch (Dungeon.depth) {
					case 1:
						WndStory.showChapter(WndStory.ID_SEWERS);
						break;
					case 6:
						WndStory.showChapter(WndStory.ID_PRISON);
						break;
					case 11:
						WndStory.showChapter(WndStory.ID_CAVES);
						break;
					case 16:
						WndStory.showChapter(WndStory.ID_METROPOLIS);
						break;
				}

			case JOURNAL:
				switch (Dungeon.depth) {
					case 50:
						WndStory.showChapter(WndStory.ID_SAFELEVEL);
						break;
					case 51:
						WndStory.showChapter(WndStory.ID_SOKOBAN1);
						break;
					case 52:
						WndStory.showChapter(WndStory.ID_SOKOBAN2);
						break;
					case 53:
						WndStory.showChapter(WndStory.ID_SOKOBAN3);
						break;
					case 54:
						WndStory.showChapter(WndStory.ID_SOKOBAN4);
						break;
					case 55:
						WndStory.showChapter(WndStory.ID_TOWN);
						break;
				}
				break;
			default:
		}

		ArrayList<Item> dropped = Dungeon.droppedItems.get(Dungeon.depth);
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell();
				if (item instanceof Potion) {
					((Potion) item).shatter(pos);
				} else if (item instanceof Plant.Seed) {
					Dungeon.level.plant((Plant.Seed) item, pos);
				} else if (item instanceof Honeypot) {
					Dungeon.level.drop(((Honeypot) item).shatter(null, pos),
							pos);
				} else {
					Dungeon.level.drop(item, pos);
				}
			}
			Dungeon.droppedItems.remove(Dungeon.depth);
		}

		Dungeon.hero.next();

		Camera.main.target = hero;
		fadeIn();
	}

	@Override
	public void destroy() {

		//tell the actor thread to finish, then wait for it to complete any actions it may be doing.
		if (actorThread.isAlive()) {
			synchronized (GameScene.class) {
				synchronized (actorThread) {
					actorThread.interrupt();
				}
				try {
					GameScene.class.wait(15000);
				} catch (InterruptedException e) {
					ShatteredPixelDungeon.reportException(e);
				}
				synchronized (actorThread) {
					if (Actor.processing()) {
						Throwable t = new Throwable();
						t.setStackTrace(actorThread.getStackTrace());
						throw new RuntimeException("timeout waiting for actor thread! ", t);
					}
				}
			}
		}

		freezeEmitters = false;

		scene = null;
		Badges.saveGlobal();

		super.destroy();
	}

	@Override
	public synchronized void pause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
		} catch (IOException e) {
			//
		}
	}

	private static final Thread actorThread = new Thread() {
		@Override
		public void run() {
			Actor.process();
		}
	};

	@Override
	public synchronized void update() {
		if (Dungeon.hero == null || scene == null) {
			return;
		}

		super.update();

		if (!freezeEmitters)
			water.offset(0, -5 * Game.elapsed);

		if (!Actor.processing() && Dungeon.hero.isAlive()) {
			if (!actorThread.isAlive()) {
				//if cpu cores are limited, game should prefer drawing the current frame
				if (Runtime.getRuntime().availableProcessors() == 1) {
					actorThread.setPriority(Thread.NORM_PRIORITY - 1);
				}
				actorThread.start();
			} else {
				synchronized (actorThread) {
					actorThread.notify();
				}
			}
		}

		if (Dungeon.hero.ready && Dungeon.hero.paralysed == 0) {
			log.newLine();
		}

		if (tagAttack != attack.active ||
				tagLoot != loot.visible ||
				tagAction != action.visible ||
				tagResume != resume.visible) {

			//we only want to change the layout when new tags pop in, not when existing ones leave.
			boolean tagAppearing = (attack.active && !tagAttack) ||
					(loot.visible && !tagLoot) ||
					(action.visible && !tagAction) ||
					(resume.visible && !tagResume);

			tagAttack = attack.active;
			tagLoot = loot.visible;
			tagAction = action.visible;
			tagResume = resume.visible;

			if (tagAppearing) layoutTags();
		}

		cellSelector.enable(Dungeon.hero.ready);
	}

	private boolean tagAttack = false;
	private boolean tagLoot = false;
	private boolean tagAction = false;
	private boolean tagResume = false;

	public static void layoutTags() {

		float tagLeft = ShatteredPixelDungeon.flipTags() ? 0 : uiCamera.width - scene.attack.width();

		if (ShatteredPixelDungeon.flipTags()) {
			scene.log.setRect(scene.attack.width(), scene.toolbar.top(), uiCamera.width - scene.attack.width(), 0);
		} else {
			scene.log.setRect(0, scene.toolbar.top(), uiCamera.width - scene.attack.width(), 0);
		}

		float pos = scene.toolbar.top();

		if (scene.tagAttack) {
			scene.attack.setPos(tagLeft, pos - scene.attack.height());
			scene.attack.flip(tagLeft == 0);
			pos = scene.attack.top();
		}

		if (scene.tagLoot) {
			scene.loot.setPos(tagLeft, pos - scene.loot.height());
			scene.loot.flip(tagLeft == 0);
			pos = scene.loot.top();
		}

		if (scene.tagResume) {
			scene.resume.setPos(tagLeft, pos - scene.resume.height());
			scene.resume.flip(tagLeft == 0);
		}
	}

	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add(new WndGame());
		}
	}

	@Override
	protected void onMenuPressed() {
		if (Dungeon.hero.ready) {
			selectItem(null, WndBag.Mode.ALL, null);
		}
	}

	private void addHeapSprite(Heap heap) {
		ItemSprite sprite = heap.sprite = (ItemSprite) heaps
				.recycle(ItemSprite.class);
		sprite.revive();
		sprite.link(heap);
		heaps.add(sprite);
	}

	private void addDiscardedSprite(Heap heap) {
		heap.sprite = (DiscardedItemSprite) heaps
				.recycle(DiscardedItemSprite.class);
		heap.sprite.revive();
		heap.sprite.link(heap);
		heaps.add(heap.sprite);
	}

	private void addPlantSprite(Plant plant) {
		(plant.sprite = (PlantSprite) plants.recycle(PlantSprite.class))
				.reset(plant);
	}

	private void addBlobSprite(final Blob gas) {
		if (gas.emitter == null) {
			gases.add(new BlobEmitter(gas));
		}
	}

	private void addMobSprite(Mob mob) {
		CharSprite sprite = mob.sprite();
		sprite.visible = Dungeon.visible[mob.pos];
		mobs.add(sprite);
		sprite.link(mob);
	}

	private synchronized void prompt(String text) {

		if (prompt != null) {
			prompt.killAndErase();
			prompt = null;
		}

		if (text != null) {
			prompt = new Toast(text) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos((uiCamera.width - prompt.width()) / 2,
					uiCamera.height - 60);
			add(prompt);
		}
	}

	private void showBanner(Banner banner) {
		banner.camera = uiCamera;
		banner.x = align(uiCamera, (uiCamera.width - banner.width) / 2);
		banner.y = align(uiCamera, (uiCamera.height - banner.height) / 3);
		add(banner);
	}

	// -------------------------------------------------------

	public static void add(Plant plant) {
		if (scene != null) {
			scene.addPlantSprite(plant);
		}
	}

	public static void add(Blob gas) {
		Actor.add(gas);
		if (scene != null) {
			scene.addBlobSprite(gas);
		}
	}

	public static void add(Heap heap) {
		if (scene != null) {
			scene.addHeapSprite(heap);
		}
	}

	public static void discard(Heap heap) {
		if (scene != null) {
			scene.addDiscardedSprite(heap);
		}
	}

	public static void add(Mob mob) {
		Dungeon.level.mobs.add(mob);
		Actor.add(mob);
		Actor.occupyCell(mob);
		scene.addMobSprite(mob);
	}

	public static void add(Mob mob, float delay) {
		Dungeon.level.mobs.add(mob);
		Actor.addDelayed(mob, delay);
		Actor.occupyCell(mob);
		scene.addMobSprite(mob);
	}

	public static void add(EmoIcon icon) {
		scene.emoicons.add(icon);
	}

	public static void effect(Visual effect) {
		scene.effects.add(effect);
	}

	public static Ripple ripple(int pos) {
		Ripple ripple = (Ripple) scene.ripples.recycle(Ripple.class);
		ripple.reset(pos);
		return ripple;
	}

	public static SpellSprite spellSprite() {
		return (SpellSprite) scene.spells.recycle(SpellSprite.class);
	}

	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter) scene.emitters.recycle(Emitter.class);
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}

	public static FloatingText status() {
		return scene != null ? (FloatingText) scene.statuses
				.recycle(FloatingText.class) : null;
	}

	public static void pickUp(Item item) {
		scene.toolbar.pickup(item);
	}

	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updateMap();
		}
	}

	public static void updateMap(int cell) {
		if (scene != null) {
			scene.tiles.updateMapCell(cell);
		}
	}

	public static void discoverTile(int pos, int oldValue) {
		if (scene != null) {
			scene.tiles.discover(pos, oldValue);
		}
	}

	public static void show(Window wnd) {
		cancelCellSelector();
		scene.add(wnd);
	}

	public static void updateFog() {
		if (scene != null)
			scene.fog.updateFog();
	}

	public static void updateFog(int x, int y, int w, int h) {
		if (scene != null) {
			scene.fog.updateFogArea(x, y, w, h);
		}
	}

	public static void afterObserve() {
		if (scene != null) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.sprite != null)
					mob.sprite.visible = Dungeon.visible[mob.pos];
			}
		}
	}

	public static void flash(int color) {
		scene.fadeIn(0xFF000000 | color, true);
	}

	public static void gameOver() {
		Banner gameOver = new Banner(
				BannerSprites.get(BannerSprites.Type.GAME_OVER));
		gameOver.show(0x000000, 1f);
		scene.showBanner(gameOver);

		Sample.INSTANCE.play(Assets.SND_DEATH);
	}

	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner(
					BannerSprites.get(BannerSprites.Type.BOSS_SLAIN));
			bossSlain.show(0xFFFFFF, 0.3f, 5f);
			scene.showBanner(bossSlain);

			Sample.INSTANCE.play(Assets.SND_BOSS);
		}
	}

	public static void levelCleared() {
		if (Dungeon.hero.isAlive()) {
			Banner levelCleared = new Banner(
					BannerSprites.get(BannerSprites.Type.CLEARED));
			levelCleared.show(0xFFFFFF, 0.3f, 5f);
			scene.showBanner(levelCleared);
		}
	}

	public static void handleCell(int cell) {
		cellSelector.select(cell);
	}

	public static void selectCell(CellSelector.Listener listener) {
		cellSelector.listener = listener;
		if (scene != null)
			scene.prompt(listener.prompt());
	}

	private static boolean cancelCellSelector() {
		if (cellSelector.listener != null
				&& cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}

	public static WndBag selectItem(WndBag.Listener listener, WndBag.Mode mode,
	                                String title) {
		cancelCellSelector();

		WndBag wnd = mode == Mode.SEED ? WndBag.getBag(SeedPouch.class,
				listener, mode, title) : mode == Mode.SCROLL ? WndBag.getBag(
				ScrollHolder.class, listener, mode, title)
				: mode == Mode.POTION ? WndBag.getBag(PotionBandolier.class,
				listener, mode, title) : mode == Mode.WAND ? WndBag
				.getBag(WandHolster.class, listener, mode, title)
				: WndBag.lastBag(listener, mode, title);

		scene.add(wnd);

		return wnd;
	}

	static boolean cancel() {
		if (Dungeon.hero.curAction != null || Dungeon.hero.resting) {

			Dungeon.hero.curAction = null;
			Dungeon.hero.resting = false;
			return true;

		} else {

			return cancelCellSelector();

		}
	}

	public static void ready() {
		selectCell(defaultCellListener);
		QuickSlotButton.cancel();
		if (scene != null && scene.toolbar != null) scene.toolbar.examining = false;
	}

	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer cell) {
			if (Dungeon.hero.handle(cell)) {
				Dungeon.hero.next();
			}
		}

		@Override
		public String prompt() {
			return null;
		}
	};
}
