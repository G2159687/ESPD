
package com.github.epd.sprout.items;

import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Light;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

public class Torch extends Item {

	public static final String AC_LIGHT = Messages.get(Torch.class, "ac_light");

	public static final float TIME_TO_LIGHT = 1;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.TORCH;

		stackable = true;

		defaultAction = AC_LIGHT;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_LIGHT);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action.equals(AC_LIGHT)) {

			hero.spend(TIME_TO_LIGHT);
			hero.busy();

			hero.sprite.operate(hero.pos);

			detach(hero.belongings.backpack);
			Buff.affect(hero, Light.class, Light.DURATION);

			Emitter emitter = hero.sprite.centerEmitter();
			emitter.start(FlameParticle.FACTORY, 0.2f, 3);

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public int price() {
		return 10 * quantity;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
