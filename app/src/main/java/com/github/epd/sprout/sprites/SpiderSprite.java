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
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.pets.Spider;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.HealthBar;
import com.watabou.noosa.TextureFilm;

public class SpiderSprite extends MobSprite {

	public HealthBar hpBar;

	public SpiderSprite() {
		super();

		texture(Assets.SPINNER);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(10, true);
		idle.frames(frames, 16, 16, 16, 16, 16, 17, 16, 17);

		run = new Animation(15, true);
		run.frames(frames, 16, 18, 16, 19);

		attack = new Animation(12, false);
		attack.frames(frames, 16, 20, 21, 16);

		die = new Animation(12, false);
		die.frames(frames, 22, 23, 24, 25);

		play(idle);
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (ch instanceof Spider) {
			final Char finalCH = ch;
			hpBar = new HealthBar() {
				@Override
				public synchronized void update() {
					super.update();
					hpBar.setRect(finalCH.sprite.x, finalCH.sprite.y - 3, finalCH.sprite.width, hpBar.height());
					hpBar.level(finalCH);
					visible = finalCH.sprite.visible;
				}
			};
			((GameScene) ShatteredPixelDungeon.scene()).ghostHP.add(hpBar);
		}
	}

	@Override
	public int blood() {
		return 0xFFBFE5B8;
	}

	@Override
	public void die() {
		super.die();

		if (hpBar != null) {
			hpBar.killAndErase();
		}
	}
}
