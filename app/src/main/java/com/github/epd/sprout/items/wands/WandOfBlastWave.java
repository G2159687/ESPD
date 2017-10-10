package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.effects.Effects;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.effects.Pushing;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfBlastWave extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_BLASTWAVE;

		collisionProperties = Ballistica.PROJECTILE;
	}

	@Override
	protected void onZap(Ballistica bolt) {
		Sample.INSTANCE.play(Assets.SND_BLAST);
		BlastWave.blast(bolt.collisionPos);

		int damage = Random.NormalIntRange(level() / 2, 6 + level() * 4);

		//presses all tiles in the AOE first
		for (int i : PathFinder.NEIGHBOURS9) {
			Dungeon.level.press(bolt.collisionPos + i, Actor.findChar(bolt.collisionPos + i));
		}

		//throws other chars around the center.
		for (int i : PathFinder.NEIGHBOURS8) {
			Char ch = Actor.findChar(bolt.collisionPos + i);

			if (ch != null) {
				ch.damage(damage, this);

				if (ch.isAlive()) {
					Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
					int strength = 1 + ((level() + 1) / 3);
					throwChar(ch, trajectory, strength);
				}
			}
		}

		//throws the char at the center of the blast
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null) {
			ch.damage(damage, this);

			if (ch.isAlive() && bolt.path.size() > bolt.dist) {
				Ballistica trajectory = new Ballistica(ch.pos, bolt.path.get(bolt.dist + 1), Ballistica.MAGIC_BOLT);
				int strength = level() + 3;
				throwChar(ch, trajectory, strength);
			}
		}

		if (!curUser.isAlive()) {
			Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
			GLog.n(Messages.get(this, "ondeath"));
		}
	}

	private void throwChar(Char ch, Ballistica trajectory, int power) {
		int dist = Math.min(trajectory.dist, power);

		if (dist == 0 || ch.properties().contains(Char.Property.IMMOVABLE)) return;

		int newPos = trajectory.path.get(dist);

		if (Actor.findChar(newPos) != null) {
			newPos = trajectory.path.get(--dist);
		}

		if (newPos == ch.pos) return;


		Actor.addDelayed(new Pushing(ch, ch.pos, newPos), -1);
		ch.pos = newPos;
		Dungeon.level.press(newPos, ch);

		if (ch.pos == trajectory.collisionPos) {
			ch.damage(Random.NormalIntRange((dist + 1) / 2, dist), this);
			Paralysis.prolong(ch, Paralysis.class, Random.NormalIntRange((dist + 1) / 2, dist));
		}

	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.slowness(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	public static class BlastWave extends Image {

		private static final float TIME_TO_FADE = 0.2f;

		private float time;

		public BlastWave() {
			super(Effects.get(Effects.Type.RIPPLE));
			origin.set(width / 2, height / 2);
		}

		public void reset(int pos) {
			revive();

			x = (pos % Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
			y = (pos / Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

			time = TIME_TO_FADE;
		}

		@Override
		public void update() {
			super.update();

			if ((time -= Game.elapsed) <= 0) {
				kill();
			} else {
				float p = time / TIME_TO_FADE;
				alpha(p);
				scale.y = scale.x = (1 - p) * 3;
			}
		}

		public static void blast(int pos) {
			Group parent = Dungeon.hero.sprite.parent;
			BlastWave b = (BlastWave) parent.recycle(BlastWave.class);
			parent.bringToFront(b);
			b.reset(pos);
		}

	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 1, 6 + (int) (level() * level() / 4f));
	}
}
