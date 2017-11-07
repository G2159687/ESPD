
package com.github.epd.sprout.items.potions;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.Fire;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class PotionOfLiquidFlame extends Potion {

	{
		initials = 5;
		name = Messages.get(this, "name");
		if (isKnown()) {
			defaultAction = AC_THROW;
		}
	}

	@Override
	public void shatter(int cell) {

		if (Dungeon.visible[cell]) {
			setKnown();

			splash(cell);
			Sample.INSTANCE.play(Assets.SND_SHATTER);
		}

		for (int offset : PathFinder.NEIGHBOURS9) {
			if (Level.flamable[cell + offset]
					|| Actor.findChar(cell + offset) != null
					|| Dungeon.level.heaps.get(cell + offset) != null) {

				GameScene.add(Blob.seed(cell + offset, 2, Fire.class));

			} else {

				CellEmitter.get(cell + offset).burst(FlameParticle.FACTORY, 2);

			}
		}
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
}
