
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.effects.Pushing;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.items.potions.PotionOfMending;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.features.Door;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.SwarmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Swarm extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = SwarmSprite.class;

		HP = HT = 100;
		defenseSkill = 8;

		maxLvl = 10;

		flying = true;

		if (!Dungeon.moreLoots) {
			loot = new PotionOfMending();
		} else {
			loot = new PotionOfHealing();
		}
		//loot = new PotionOfMending(); potential nerf
		lootChance = 0.2f; // by default, see die()
	}

	private static final float SPLIT_DELAY = 1f;

	int generation = 0;

	private static final String GENERATION = "generation";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(GENERATION, generation);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		generation = bundle.getInt(GENERATION);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(3, 5);
	}

	@Override
	public int defenseProc(Char enemy, int damage) {

		if (HP >= damage + 2) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			boolean[] passable = Level.passable;

			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.getWidth(),
					pos - Dungeon.level.getWidth()};
			for (int n : neighbours) {
				if (passable[n] && Actor.findChar(n) == null) {
					candidates.add(n);
				}
			}

			if (candidates.size() > 0) {

				Swarm clone = split();
				clone.HP = (HP - damage) / 2;
				clone.pos = Random.element(candidates);
				clone.state = clone.HUNTING;

				if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
					Door.enter(clone.pos);
				}

				GameScene.add(clone, SPLIT_DELAY);
				Actor.addDelayed(new Pushing(clone, pos, clone.pos), -1);

				HP -= clone.HP;
			}
		}

		return damage;
	}

	@Override
	public int attackSkill(Char target) {
		return 12;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(this, "def");
	}

	private Swarm split() {
		Swarm clone = new Swarm();
		clone.generation = generation + 1;
		if (buff(Burning.class) != null) {
			Buff.affect(clone, Burning.class).reignite(clone);
		}
		if (buff(Poison.class) != null) {
			Buff.affect(clone, Poison.class).set(2);
		}
		return clone;
	}

	@Override
	public void die(Object cause) {
		// sets drop chance
		if (Dungeon.moreLoots) {
			lootChance = 1f;
		} else {
			lootChance = 0.5f / (5 / (generation + 1));
		}
		super.die(cause);
	}

	@Override
	protected Item createLoot() {
		return super.createLoot();
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}
}
