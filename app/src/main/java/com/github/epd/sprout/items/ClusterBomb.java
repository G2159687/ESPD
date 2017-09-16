/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.items;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.BlastParticle;
import com.github.epd.sprout.effects.particles.SmokeParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ClusterBomb extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.CLUSTER_BOMB;
		defaultAction = AC_LIGHTTHROW;
		stackable = true;
		usesTargeting = true;
	}

	public Fuse fuse;


	private static boolean lightingFuse = false;

	private static final String AC_LIGHTTHROW = Messages.get(ClusterBomb.class, "ac");

	@Override
	public boolean isSimilar(Item item) {
		return item instanceof ClusterBomb && this.fuse == ((ClusterBomb) item).fuse;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_LIGHTTHROW);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_LIGHTTHROW)) {
			lightingFuse = true;
			action = AC_THROW;
		} else
			lightingFuse = false;

		super.execute(hero, action);
	}

	@Override
	protected void onThrow(int cell) {
		if (!Level.pit[cell] && lightingFuse) {
			Actor.addDelayed(fuse = new Fuse().ignite(this), 2);
		}
		if (Actor.findChar(cell) != null
				&& !(Actor.findChar(cell) instanceof Hero)) {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8)
				if (Level.passable[cell + i])
					candidates.add(cell + i);
			int newCell = candidates.isEmpty() ? cell : Random
					.element(candidates);
			Dungeon.level.drop(this, newCell).sprite.drop(cell);

		} else
			super.onThrow(cell);
	}

	@Override
	public boolean doPickUp(Hero hero) {
		if (fuse != null) {
			GLog.w(Messages.get(this, "sniff"));
			fuse = null;
		}
		return super.doPickUp(hero);
	}

	public void explode(int cell) {
		// We're blowing up, so no need for a fuse anymore.
		this.fuse = null;

		Sample.INSTANCE.play(Assets.SND_BLAST, 2);

		if (Dungeon.visible[cell]) {
			CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
		}

		boolean terrainAffected = false;
		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Level.getLength()) {
				if (Dungeon.visible[c]) {
					CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
				}

				if (Level.flamable[c]) {
					Level.set(c, Terrain.EMBERS);
					GameScene.updateMap(c);
					terrainAffected = true;
				}

				// destroys items / triggers bombs caught in the blast.
				Heap heap = Dungeon.level.heaps.get(c);
				if (heap != null)
					heap.explode();

				Char ch = Actor.findChar(c);
				if (ch != null) {
					// those not at the center of the blast take damage less
					// consistently.
					int minDamage = c == cell ? Dungeon.depth * 9 : Dungeon.depth * 5;
					int maxDamage = 5 + Dungeon.depth * 20;

					int dmg = Random.NormalIntRange(minDamage, maxDamage)
							- Random.Int(ch.dr());
					if (dmg > 0) {
						ch.damage(dmg, this);
					}

					if (ch == Dungeon.hero && !ch.isAlive())
						// constant is used here in the rare instance a player
						// is killed by a double bomb.
						Dungeon.fail(Utils.format(ResultDescriptions.ITEM,
								"bomb"));
				}
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
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
	public Item random() {
		switch (Random.Int(2)) {
			case 0:
			default:
				return this;
			case 1:
				return new DoubleBomb();
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return fuse != null ? new ItemSprite.Glowing(0xFF0000, 0.6f) : null;
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

	@Override
	public String info() {
		return fuse != null ? Messages.get(this, "desc1") : Messages.get(this, "desc2");
	}

	private static final String FUSE = "fuse";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(FUSE, fuse);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(FUSE))
			Actor.add(fuse = ((Fuse) bundle.get(FUSE)).ignite(this));
	}

	public static class Fuse extends Actor {

		{
			actPriority = 3; //as if it were a buff
		}

		private ClusterBomb bomb;

		public Fuse ignite(ClusterBomb bomb) {
			this.bomb = bomb;
			return this;
		}

		@Override
		protected boolean act() {

			// something caused our bomb to explode early, or be defused. Do
			// nothing.
			if (bomb.fuse != this) {
				Actor.remove(this);
				return true;
			}

			// look for our bomb, remove it from its heap, and blow it up.
			for (Heap heap : Dungeon.level.heaps.values()) {
				if (heap.items.contains(bomb)) {
					heap.items.remove(bomb);

					bomb.explode(heap.pos);

					for (int n : PathFinder.NEIGHBOURS8DIST2) {
						int c = heap.pos + n;
						if (Random.Int(3) == 0) {
							bomb.explode(c);
							//spend(2f);
						}
					}

					Actor.remove(this);
					return true;
				}
			}

			bomb.fuse = null;
			Actor.remove(this);
			return true;
		}
	}

	public static class DoubleBomb extends ClusterBomb {

		{
			name = "two bombs";
			image = ItemSpriteSheet.DBL_BOMB;
			stackable = false;
		}

		@Override
		public String info() {
			return "A stack of two hefty cluster bombs, looks like you get one free!";
		}

		@Override
		public boolean doPickUp(Hero hero) {
			ClusterBomb cbomb = new ClusterBomb();
			cbomb.quantity(2);
			if (cbomb.doPickUp(hero)) {
				// isaaaaac....
				hero.sprite.showStatus(CharSprite.NEUTRAL, "1+1 free!");
				return true;
			}
			return false;
		}
	}
}
