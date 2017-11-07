
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.Fire;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfFirebolt extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_FIREBOLT;
	}

	@Override
	protected void onZap(Ballistica bolt) {

		for (int c : bolt.subPath(0, bolt.dist)) {
			if (Level.flamable[c]) {
				GameScene.add(Blob.seed(c, 1, Fire.class));
			}
		}

		int cell = bolt.collisionPos;

		GameScene.add(Blob.seed(cell, 1, Fire.class));

		Char ch = Actor.findChar(cell);
		if (ch != null) {

			int damage = Random.Int(1, 8 + level() * 4);
			if (Dungeon.hero.buff(Strength.class) != null) {
				damage *= (int) 4f;
				Buff.detach(Dungeon.hero, Strength.class);
			}
			ch.damage(damage, this);

			Buff.affect(ch, Burning.class).reignite(ch);

			ch.sprite.emitter().burst(FlameParticle.FACTORY, 5);

			if (ch == curUser && !ch.isAlive()) {
				Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
				GLog.n(Messages.get(this, "kill"));
			}
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.fire(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 1, 8 + level() * level());
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		if (defender.isAlive())
			if (level > Random.IntRange(0, 50)) {
				Buff.affect(defender, Burning.class).reignite(defender);
				defender.damage(Random.Int(1, level + 2), this);
				defender.sprite.emitter().burst(FlameParticle.FACTORY, level < 10 ? level + 1 : 10);
			}
	}
}
