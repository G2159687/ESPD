
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Amok;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfAmok extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_AMOK;
	}

	@Override
	protected void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null) {
			Buff.affect(ch, Amok.class, 3f + level());
		} else {
			GLog.i(Messages.get(this, "effect"));
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.purpleLight(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		if (defender.isAlive()) {
			if (level > Random.IntRange(0, 50))
				Buff.affect(defender, Amok.class, Random.IntRange(level() / 2, 2 * level));
		}
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
