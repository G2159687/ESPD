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
import com.github.epd.sprout.actors.mobs.pets.Bunny;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.HealthBar;
import com.watabou.noosa.TextureFilm;

public class BunnySprite extends MobSprite {

	private HealthBar hpBar;
	
	//Frames 0,2 are idle, 0,1,2 are moving, 0,3,4,1 are attack and 5,6,7 are for death 
	

	public BunnySprite() {
		super();

		texture(Assets.BUNNY);

		TextureFilm frames = new TextureFilm(texture, 14, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 1, 0, 0);

		run = new Animation(4, true);
		run.frames(frames, 0, 0, 0, 2);

		attack = new Animation(8, false);
		attack.frames(frames, 0, 2, 3, 4);

		zap = attack.clone();
		
		die = new Animation(8, false);
		die.frames(frames, 5, 6, 7, 8);

		play(idle);
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (ch instanceof Bunny){
			final Char finalCH = ch;
			hpBar = new HealthBar(){
				@Override
				public synchronized void update() {
					super.update();
					hpBar.setRect(finalCH.sprite.x, finalCH.sprite.y-3, finalCH.sprite.width, hpBar.height());
					hpBar.level( finalCH );
					visible = finalCH.sprite.visible;
				}
			};
			((GameScene) ShatteredPixelDungeon.scene()).ghostHP.add(hpBar);
		}
	}
		
	@Override
	public int blood() {
		return 0xFFcdcdb7;
	}
}
