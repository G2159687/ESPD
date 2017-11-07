
package com.github.epd.sprout.scenes;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.windows.WndStory;
import com.watabou.noosa.Game;

public class IntroScene extends PixelScene {

	private static final String TEXT = Messages.get(IntroScene.class, "text");

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
