
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.effects.particles.PoisonParticle;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfPoison extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_POISON;
	}

	@Override
	protected void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null) {

			int poisonbase = 5;

			if (Dungeon.hero.buff(Strength.class) != null) {
				poisonbase *= (int) 4f;
				Buff.detach(Dungeon.hero, Strength.class);
			}

			Buff.affect(ch, Poison.class).set(
					Poison.durationFactor(ch) * (poisonbase + level() * 2));

		} else {

			GLog.i(Messages.get(this, "nothing"));

		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.poison(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 3 + level());
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		if (level > Random.IntRange(0, 50)){
			Buff.affect(defender, Poison.class).set(Poison.durationFactor(defender) * Math.min(level, 20));
			CellEmitter.center(defender.pos).burst(PoisonParticle.SPLASH, 5);
		}
	}
}
