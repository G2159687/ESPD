
package com.github.epd.sprout.effects;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.Image;

public class BannerSprites {

	public enum Type {
		PIXEL_DUNGEON, BOSS_SLAIN, GAME_OVER, SELECT_YOUR_HERO, PIXEL_DUNGEON_SIGNS, CLEARED
	}

	public static Image get(Type type) {
		Image icon = new Image(Assets.BANNERS);
		switch (type) {
			case PIXEL_DUNGEON:
				icon.frame(icon.texture.uvRect(0, 0, 128, 70));
				break;
			case BOSS_SLAIN:
				icon.frame(icon.texture.uvRect(0, 70, 128, 105));
				break;
			case GAME_OVER:
				icon.frame(icon.texture.uvRect(0, 105, 128, 140));
				break;
			case SELECT_YOUR_HERO:
				icon.frame(icon.texture.uvRect(0, 140, 128, 161));
				break;
			case PIXEL_DUNGEON_SIGNS:
				icon.frame(icon.texture.uvRect(0, 161, 128, 218));
				break;
			case CLEARED:
				icon.frame(icon.texture.uvRect(0, 230, 128, 250));
				break;
		}
		return icon;
	}
}
