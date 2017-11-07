
package com.github.epd.sprout.effects;

import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.sprites.CharSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;

public class Pushing extends Actor {

	{
		actPriority = Integer.MIN_VALUE; //it's a visual effect, gets priority no matter what
	}

	private CharSprite sprite;
	private int from;
	private int to;

	private Effect effect;

	public Pushing(Char ch, int from, int to) {
		sprite = ch.sprite;
		this.from = from;
		this.to = to;
	}

	@Override
	protected boolean act() {
		if (sprite != null) {

			if (effect == null) {
				new Effect();
			}
		}
		Actor.remove(Pushing.this);

		//so that all pushing effects at the same time go simultaneously
		for (Actor actor : Actor.all()) {
			if (actor instanceof Pushing && ((Pushing) actor).cooldown() == 0)
				return true;
		}
		return false;
	}

	public class Effect extends Visual {

		private static final float DELAY = 0.15f;

		private PointF end;

		private float delay;

		public Effect() {
			super(0, 0, 0, 0);

			point(sprite.worldToCamera(from));
			end = sprite.worldToCamera(to);

			speed.set(2 * (end.x - x) / DELAY, 2 * (end.y - y) / DELAY);
			acc.set(-speed.x / DELAY, -speed.y / DELAY);

			delay = 0;

			if (sprite.parent != null)
				sprite.parent.add(this);
		}

		@Override
		public void update() {
			super.update();

			if ((delay += Game.elapsed) < DELAY) {

				sprite.x = x;
				sprite.y = y;

			} else {

				sprite.point(end);

				killAndErase();
				Actor.remove(Pushing.this);

				next();
			}
		}
	}

}
