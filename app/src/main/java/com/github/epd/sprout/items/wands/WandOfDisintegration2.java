
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.effects.Beam;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.PurpleParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfDisintegration2 extends Wand {

	{
		name = Messages.get(this, "name");
		collisionProperties = Ballistica.STOP_TERRAIN;
		image = ItemSpriteSheet.WAND_DISINTEGRATION;
	}

	@Override
	protected void onZap(Ballistica beam) {

		boolean terrainAffected = false;

		int level = level();

		int maxDistance = Math.min(distance(), beam.dist);

		ArrayList<Char> chars = new ArrayList<Char>();

		for (int c : beam.subPath(1, maxDistance)) {

			Char ch;
			if ((ch = Actor.findChar(c)) != null) {
				chars.add(ch);
			}

			int terr = Dungeon.level.map[c];
			if (terr == Terrain.DOOR || terr == Terrain.BARRICADE) {

				Level.set(c, Terrain.EMBERS);
				GameScene.updateMap(c);
				terrainAffected = true;

			} else if (terr == Terrain.HIGH_GRASS) {

				Level.set(c, Terrain.GRASS);
				GameScene.updateMap(c);
				terrainAffected = true;

			}

			CellEmitter.center(c).burst(PurpleParticle.BURST,
					Random.IntRange(1, 2));
		}

		if (terrainAffected) {
			Dungeon.observe();
		}

		int lvl = level + chars.size();
		int dmgMin = lvl;
		int dmgMax = 8 + lvl * lvl / 3;
		if (Dungeon.hero.buff(Strength.class) != null) {
			dmgMin *= (int) 4f;
			dmgMax *= (int) 4f;
			Buff.detach(Dungeon.hero, Strength.class);
		}
		for (Char ch : chars) {
			ch.damage(Random.NormalIntRange(dmgMin, dmgMax), this);
			ch.sprite.centerEmitter().burst(PurpleParticle.BURST,
					Random.IntRange(1, 2));
			ch.sprite.flash();
		}
	}

	private int distance() {
		return level() + 4;
	}

	@Override
	protected void fx(Ballistica beam, Callback callback) {

		int cell = beam.path.get(Math.min(beam.dist, distance()));
		curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(),
				DungeonTilemap.tileCenterToWorld(cell)));
		callback.call();
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
