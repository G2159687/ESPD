package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Light;
import com.github.epd.sprout.actors.mobs.Acidic;
import com.github.epd.sprout.actors.mobs.Bandit;
import com.github.epd.sprout.actors.mobs.BlueWraith;
import com.github.epd.sprout.actors.mobs.Eye;
import com.github.epd.sprout.actors.mobs.FossilSkeleton;
import com.github.epd.sprout.actors.mobs.Goo;
import com.github.epd.sprout.actors.mobs.Gullin;
import com.github.epd.sprout.actors.mobs.King;
import com.github.epd.sprout.actors.mobs.Kupua;
import com.github.epd.sprout.actors.mobs.Mimic;
import com.github.epd.sprout.actors.mobs.Monk;
import com.github.epd.sprout.actors.mobs.RedWraith;
import com.github.epd.sprout.actors.mobs.Scorpio;
import com.github.epd.sprout.actors.mobs.Senior;
import com.github.epd.sprout.actors.mobs.Skeleton;
import com.github.epd.sprout.actors.mobs.Succubus;
import com.github.epd.sprout.actors.mobs.Thief;
import com.github.epd.sprout.actors.mobs.Warlock;
import com.github.epd.sprout.actors.mobs.Wraith;
import com.github.epd.sprout.actors.mobs.Yog;
import com.github.epd.sprout.actors.mobs.Zot;
import com.github.epd.sprout.actors.mobs.ZotPhase;
import com.github.epd.sprout.actors.mobs.npcs.Ghost;
import com.github.epd.sprout.effects.Beam;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.RainbowParticle;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicMapping;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.HashSet;

public class WandOfPrismaticLight extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_PRISMATIC;

		collisionProperties = Ballistica.MAGIC_BOLT;
	}

	@Override
	protected void onZap(Ballistica beam) {
		Char ch = Actor.findChar(beam.collisionPos);
		if (ch != null) {
			affectTarget(ch);
		}
		affectMap(beam);

		if (curUser.viewDistance < 4)
			Buff.prolong(curUser, Light.class, 10f + level() * 5);
	}

	private void affectTarget(Char ch) {

		int level = level();

		int dmg = Random.NormalIntRange(level, 8 + level * 4);

		//5 in (5+lvl) chance of failing
		if (Random.Int(5 + level) >= 5) {
			Buff.prolong(ch, Blindness.class, 2f + (level * 0.5f));
			ch.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 6);
		}

		if (ch.properties().contains(Char.Property.UNDEAD)){
			ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + level());
			Sample.INSTANCE.play(Assets.SND_BURNING);

			ch.damage((int) (dmg * 1.5), this);
		} else {
			ch.sprite.centerEmitter().burst(RainbowParticle.BURST, 10 + level());

			ch.damage(dmg, this);
		}

	}

	private void affectMap(Ballistica beam) {
		boolean noticed = false;
		for (int c : beam.subPath(0, beam.dist)) {
			for (int n : PathFinder.NEIGHBOURS9DIST2) {
				int cell = c + n;
				if (!Dungeon.level.insideMap(cell))
					continue;

				if (Level.discoverable[cell])
					Dungeon.level.mapped[cell] = true;

				int terr = Dungeon.level.map[cell];
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

					Level.set(cell, Terrain.discover(terr));
					GameScene.updateMap(cell);

					GameScene.discoverTile(cell, terr);
					ScrollOfMagicMapping.discover(cell);

					noticed = true;
				}
			}

			CellEmitter.center(c).burst(RainbowParticle.BURST, Random.IntRange(1, 2));
		}
		if (noticed)
			Sample.INSTANCE.play(Assets.SND_SECRET);

		Dungeon.observe();
	}

	@Override
	protected void fx(Ballistica beam, Callback callback) {
		curUser.sprite.parent.add(
				new Beam.LightRay(curUser.sprite.center(), DungeonTilemap.tileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", level(), (int) (8 + (level() * (level() / 5f))));
	}
}
