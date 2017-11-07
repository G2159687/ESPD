
package com.github.epd.sprout.items.bombs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.AlbinoPiranha;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.SmokeParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
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

public class FishingBomb extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.RICEBOMB;
		defaultAction = AC_LIGHTTHROW;
		stackable = true;
		usesTargeting = true;
	}

	public Fuse fuse;

	public static final String TXT_NO_TELEPORT = Messages.get(FishingBomb.class, "no_tele");


	private static boolean lightingFuse = false;

	private static final String AC_LIGHTTHROW = Messages.get(FishingBomb.class, "ac");

	@Override
	public boolean isSimilar(Item item) {
		return item instanceof FishingBomb && this.fuse == ((FishingBomb) item).fuse;
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
			GLog.w(Messages.get(FishingBomb.class, "stop"));
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
			if (c >= 0 && c < Dungeon.level.getLength()) {
				if (Dungeon.visible[c]) {
					CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
				}

				Heap heap = Dungeon.level.heaps.get(c);
				if (heap != null)
					heap.dumpexplode();

				Char ch = Actor.findChar(c);
				if (ch instanceof AlbinoPiranha) {

					int count = 20;
					int pos;
					do {
						pos = Dungeon.level.randomRespawnCellFish();
						if (count-- <= 0) {
							break;
						}
					} while (pos == -1);

					if (pos == -1) {

						GLog.w(TXT_NO_TELEPORT);

					} else {

						ch.pos = pos;
						ch.sprite.place(ch.pos);
						ch.sprite.visible = Dungeon.visible[pos];
						GLog.i(Messages.get(FishingBomb.class, "tele", curUser.name, ch.name));

					}

				}
			}
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
	public ItemSprite.Glowing glowing() {
		return fuse != null ? new ItemSprite.Glowing(0xFF0000, 0.6f) : null;
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

	@Override
	public String info() {
		return Messages.get(FishingBomb.class, "desc1") + (fuse != null ? Messages.get(FishingBomb.class, "desc2") : Messages.get(FishingBomb.class, "desc3"));
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

		private FishingBomb bomb;

		public Fuse ignite(FishingBomb bomb) {
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

	public FishingBomb() {
		this(1);
	}

	public FishingBomb(int value) {
		this.quantity = value;
	}
}
