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
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.BlueWraith;
import com.github.epd.sprout.actors.mobs.DwarfKingTomb;
import com.github.epd.sprout.actors.mobs.DwarfLich;
import com.github.epd.sprout.actors.mobs.King;
import com.github.epd.sprout.actors.mobs.King.Undead;
import com.github.epd.sprout.actors.mobs.MossySkeleton;
import com.github.epd.sprout.actors.mobs.RedWraith;
import com.github.epd.sprout.actors.mobs.Skeleton;
import com.github.epd.sprout.actors.mobs.SpectralRat;
import com.github.epd.sprout.actors.mobs.Warlock;
import com.github.epd.sprout.actors.mobs.Wraith;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.SmokeParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HolyHandGrenade extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.HOLY_HAND_GRENADE;
		defaultAction = AC_LIGHTTHROW;
		stackable = true;
		usesTargeting = true;
	}

	public Fuse fuse;


	private static boolean lightingFuse = false;

	private static final String AC_LIGHTTHROW = Messages.get(HolyHandGrenade.class, "ac");

	@Override
	public boolean isSimilar(Item item) {
		return item instanceof HolyHandGrenade && this.fuse == ((HolyHandGrenade) item).fuse;
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

			CellEmitter.get(hero.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
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
		if ((Actor.findChar(cell) != null
				&& !(Actor.findChar(cell) instanceof Hero)) ||
				Dungeon.level.heaps.get(cell) != null) {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS9DIST2)
				if (Level.passable[cell + i] && Dungeon.level.heaps.get(cell + i) == null)
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
			GLog.w(Messages.get(HolyHandGrenade.class, "stop"));
			fuse = null;
		}
		return super.doPickUp(hero);
	}

	public void explode(int cell) {
		// We're blowing up, so no need for a fuse anymore.
		this.fuse = null;
		Sample.INSTANCE.play(Assets.SND_BLAST, 2);

		//if (Dungeon.visible[cell]) {
		//	CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
		//}

		for (int n : PathFinder.NEIGHBOURS9DIST2) {
			int c = cell + n;
			if (c >= 0 && c < Level.getLength()) {
				if (Dungeon.visible[c]) {
					CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
				}

				Heap heap = Dungeon.level.heaps.get(c);
				if (heap != null) {
					heap.holyexplode();
				}

				Char ch = Actor.findChar(c);
				if (ch instanceof MossySkeleton || ch instanceof Skeleton || ch instanceof RedWraith
						|| ch instanceof BlueWraith || ch instanceof Wraith || ch instanceof DwarfLich
						|| ch instanceof SpectralRat || ch instanceof RedWraith || ch instanceof DwarfKingTomb
						|| ch instanceof King || ch instanceof Warlock || ch instanceof Undead) {
					if (Random.Int(2) == 0) {
						Buff.affect(ch, Terror.class, Terror.DURATION).object = curUser.id();
						int dmg = Random.NormalIntRange(75, 300 - Random.Int(ch.dr()));
						if (dmg > 0) {
							ch.damage(dmg, this);
						}
					} else {
						Buff.affect(ch, Terror.class, Terror.DURATION).object = curUser.id();
						int dmg = Random.NormalIntRange(75, 300);
						if (dmg > 0) {
							ch.damage(dmg, this);
						}
					}

				}
			}
		}

		Dungeon.observe();
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
	public ItemSprite.Glowing glowing() {
		return fuse != null ? new ItemSprite.Glowing(0xFF0000, 0.6f) : null;
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc1")
				+ (fuse != null ? Messages.get(this, "desc2")
				: Messages.get(this, "desc3"));
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

		private HolyHandGrenade bomb;

		public Fuse ignite(HolyHandGrenade bomb) {
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

					Actor.remove(this);
					return true;
				}

			}
			bomb.fuse = null;
			Actor.remove(this);
			return true;
		}
	}

	public HolyHandGrenade() {
		this(1);
	}

	public HolyHandGrenade(int value) {
		this.quantity = value;
	}

}
