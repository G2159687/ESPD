
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class AlarmTrap {

	// 0xDD3333

	public static void trigger(int pos, Char ch) {

		for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
			if (mob != ch) {
				mob.beckon(pos);
			}
		}

		if (Dungeon.visible[pos]) {
			GLog.w(Messages.get(AlarmTrap.class, "alarm"));
			CellEmitter.center(pos).start(Speck.factory(Speck.SCREAM), 0.3f, 3);
		}

		Sample.INSTANCE.play(Assets.SND_ALERT);
	}
}
