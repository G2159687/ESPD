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

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.windows.WndStory;
import com.watabou.noosa.Game;

public class IntroScene extends PixelScene {

	private static final String TEXT = Messages.get(IntroScene.class,"text");

	@Override
	public void create() {
		super.create();

		add(new WndStory(TEXT) {
			@Override
			public void hide() {
				super.hide();
				Game.switchScene(InterlevelScene.class);
			}
		});

		fadeIn();
	}
}
