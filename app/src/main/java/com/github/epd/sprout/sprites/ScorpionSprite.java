
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.pets.Scorpion;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.HealthBar;
import com.watabou.noosa.TextureFilm;

public class ScorpionSprite extends MobSprite {

	public HealthBar hpBar;

	public ScorpionSprite() {
		super();

		texture(Assets.SCORPIO);

		TextureFilm frames = new TextureFilm(texture, 18, 17);

		idle = new Animation(12, true);
		idle.frames(frames, 28, 28, 28, 28, 28, 28, 28, 28, 29, 30, 29, 30, 29, 30);

		run = new Animation(8, true);
		run.frames(frames, 33, 33, 34, 34);

		attack = new Animation(15, false);
		attack.frames(frames, 28, 31, 32);

		die = new Animation(12, false);
		die.frames(frames, 28, 35, 36, 37, 38);

		play(idle);
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (ch instanceof Scorpion) {
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
		return 0xFF44FF22;
	}

	@Override
	public void die() {
		super.die();

		if (hpBar != null) {
			hpBar.killAndErase();
		}
	}

	@Override
	public void killAndErase(){

		if (hpBar != null) {
			hpBar.killAndErase();
		}

		super.killAndErase();
	}

}
