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
import com.github.epd.sprout.actors.mobs.LitTower;
import com.github.epd.sprout.effects.Lightning;
import com.watabou.noosa.TextureFilm;

public class LitTowerSprite extends MobSprite {

	public LitTowerSprite() {
		super();

		texture(Assets.LITTOWER);
		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(10, true);
		idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		run = idle.clone();
		die = idle.clone();
		attack = idle.clone();

		zap = attack.clone();
		
		idle();
	}

	@Override
	public void zap(int pos) {

		parent.add( new Lightning( ch.pos, pos, (LitTower)ch ) );

		turnTo(ch.pos, pos);
		play(zap);
	}
	
}
