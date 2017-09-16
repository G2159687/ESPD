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
package com.github.epd.sprout.ui;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.HeroSprite;
import com.github.epd.sprout.windows.WndGame;
import com.github.epd.sprout.windows.WndHero;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.ColorMath;

public class StatusPane extends Component {

	private NinePatch bg;
	private Image avatar;
	private float warning;

	private int lastTier = 0;

	private Image shield;
	private Image hp;
	private Image exp;

	private BossHealthBar bossHP;

	private int lastLvl = -1;
	private int lastKeys = -1;

	private BitmapText level;
	private BitmapText depth;
	private BitmapText keys;

	private DangerIndicator danger;
	private BuffIndicator buffs;
	private Compass compass;

	private MenuButton btnMenu;

	@Override
	protected void createChildren() {

		bg = new NinePatch(Assets.STATUS, 80, 0, 30 + 18, 0);
		add(bg);

		add(new TouchArea(0, 1, 31, 31) {
			@Override
			protected void onClick(Touch touch) {
				Image sprite = Dungeon.hero.sprite;
				if (!sprite.isVisible()) {
					Camera.main.focusOn(sprite);
				}
				GameScene.show(new WndHero());
			}
		});

		btnMenu = new MenuButton();
		add(btnMenu);

		avatar = HeroSprite.avatar(Dungeon.hero.heroClass, lastTier);
		add(avatar);

		compass = new Compass(Dungeon.level.exit);
		add(compass);

		shield = new Image(Assets.SHLD_BAR);
		//	add(shield);

		hp = new Image(Assets.HP_BAR);
		add(hp);

		exp = new Image(Assets.XP_BAR);
		add(exp);

		bossHP = new BossHealthBar();
		add(bossHP);

		level = new BitmapText(PixelScene.font1x);
		level.hardlight(0xFFEBA4);
		add(level);

		depth = new BitmapText(Integer.toString(Dungeon.depth),
				PixelScene.font1x);
		depth.hardlight(0xCACFC2);
		depth.measure();
		add(depth);

		Dungeon.hero.belongings.countIronKeys();
		keys = new BitmapText(PixelScene.font1x);
		keys.hardlight(0xCACFC2);
		add(keys);

		danger = new DangerIndicator();
		add(danger);

		buffs = new BuffIndicator(Dungeon.hero);
		add(buffs);
	}

	@Override
	protected void layout() {

		height = 32;

		bg.size(width, bg.height);

		avatar.x = PixelScene.align(camera(), bg.x + 15 - avatar.width / 2);
		avatar.y = PixelScene
				.align(camera(), bg.y + 16 - avatar.height / 2);

		compass.x = avatar.x + avatar.width / 2 - compass.origin.x;
		compass.y = avatar.y + avatar.height / 2 - compass.origin.y;

		hp.x = shield.x = 30;
		hp.y = shield.y = 3;

		bossHP.setPos(6 + (width - bossHP.width()) / 2, 20);

		depth.x = width - 24 - depth.width() - 18;
		depth.y = 6;

		keys.y = 6;

		danger.setPos(width - danger.width(), 20);

		buffs.setPos(31, 9);

		btnMenu.setPos(width - btnMenu.width(), 1);
	}

	@Override
	public void update() {
		super.update();

		float health = Dungeon.hero.HP;
		float shield = Dungeon.hero.SHLD;
		float max = Math.max(health + shield, Dungeon.hero.HT);

		if (!Dungeon.hero.isAlive()) {
			avatar.tint(0x000000, 0.5f);
		} else if ((health / max) < 0.3f) {
			warning += Game.elapsed * 5f * (0.4f - (health / max));
			warning %= 1f;
			avatar.tint(ColorMath.interpolate(warning, 0x660000, 0xCC0000, 0x660000), 0.5f);
		} else {
			avatar.resetColor();
		}

		hp.scale.x = health / max;
		this.shield.scale.x = (health + shield) / max;

		exp.scale.x = (width / exp.width) * Dungeon.hero.exp
				/ Dungeon.hero.maxExp();

		if (Dungeon.hero.lvl != lastLvl) {

			if (lastLvl != -1) {
				Emitter emitter = (Emitter) recycle(Emitter.class);
				emitter.revive();
				emitter.pos(27, 27);
				emitter.burst(Speck.factory(Speck.STAR), 12);
			}

			lastLvl = Dungeon.hero.lvl;
			level.text(Integer.toString(lastLvl));
			level.measure();
			level.x = PixelScene.align(27.0f - level.width() / 2);
			level.y = PixelScene.align(27.5f - level.baseLine() / 2);
		}

		int k = IronKey.curDepthQuantity;
		if (k != lastKeys) {
			lastKeys = k;
			keys.text(Integer.toString(lastKeys));
			keys.measure();
			keys.x = width - 8 - keys.width() - 18;
		}

		int tier = Dungeon.hero.tier();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy(HeroSprite.avatar(Dungeon.hero.heroClass, tier));
		}
	}

	private static class MenuButton extends Button {

		private Image image;

		public MenuButton() {
			super();

			width = image.width + 4;
			height = image.height + 4;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			image = new Image(Assets.STATUS, 114, 3, 12, 11);
			add(image);
		}

		@Override
		protected void layout() {
			super.layout();

			image.x = x + 2;
			image.y = y + 2;
		}

		@Override
		protected void onTouchDown() {
			image.brightness(1.5f);
			Sample.INSTANCE.play(Assets.SND_CLICK);
		}

		@Override
		protected void onTouchUp() {
			image.resetColor();
		}

		@Override
		protected void onClick() {
			GameScene.show(new WndGame());
		}
	}
}
