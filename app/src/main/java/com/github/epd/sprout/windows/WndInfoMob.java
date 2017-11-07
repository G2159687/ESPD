
package com.github.epd.sprout.windows;

import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.ui.HealthBar;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

public class WndInfoMob extends WndTitledMessage {

	public WndInfoMob(Mob mob) {

		super(new MobTitle(mob), desc(mob));

	}

	private static String desc(Mob mob) {

		StringBuilder builder = new StringBuilder(mob.description());

		builder.append("\n\n" + mob.state.status());

		return builder.toString();
	}

	private static class MobTitle extends Component {

		private static final int GAP = 2;

		private CharSprite image;
		private RenderedText name;
		private HealthBar health;
		private BuffIndicator buffs;

		public MobTitle(Mob mob) {

			name = PixelScene.renderText(Messages.titleCase(mob.name), 9);
			name.hardlight(TITLE_COLOR);
			add(name);

			image = mob.sprite();
			add(image);

			health = new HealthBar();
			health.level(mob);
			add(health);

			buffs = new BuffIndicator(mob);
			add(buffs);
		}

		@Override
		protected void layout() {

			image.x = 0;
			image.y = Math.max(0, name.height() + GAP + health.height() - image.height);

			name.x = image.width + GAP;
			name.y = image.height - health.height() - GAP - name.baseLine();

			float w = width - image.width - GAP;

			health.setRect(image.width + GAP, image.height - health.height(), w, health.height());

			buffs.setPos(
					name.x + name.width() + GAP - 1,
					name.y + name.baseLine() - BuffIndicator.SIZE - 2);

			height = health.bottom();
		}
	}
}
