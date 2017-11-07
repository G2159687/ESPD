
package com.github.epd.sprout.items.bombs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.BlastParticle;
import com.github.epd.sprout.effects.particles.SmokeParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
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

public class Bomb extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.BOMB;
		defaultAction = AC_LIGHTTHROW;
		usesTargeting = true;
		stackable = true;
	}

	public Fuse fuse;


	private static boolean lightingFuse = false;

	private static final String AC_LIGHTTHROW = Messages.get(Bomb.class, "ac_lightthrow");

	public static final String AC_DIZZYBOMB = Messages.get(Bomb.class, "dizzy");
	public static final String AC_SMARTBOMB = Messages.get(Bomb.class, "smart");
	public static final String AC_SEEKINGBOMB = Messages.get(Bomb.class, "seeking");
	public static final String AC_CLUSTERBOMB = Messages.get(Bomb.class, "cluster");
	public static final String AC_SEEKINGCLUSTERBOMB = Messages.get(Bomb.class, "sc");

	public static final float TIME_TO_COOK_BOMB = 4;

	@Override
	public boolean isSimilar(Item item) {
		return item instanceof Bomb && this.fuse == ((Bomb) item).fuse;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_LIGHTTHROW);
		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Dungeon.hero.lvl > 4) {
			actions.add(AC_DIZZYBOMB);
		}
		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Dungeon.hero.lvl > 9) {
			actions.add(AC_SMARTBOMB);
		}
		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Dungeon.hero.lvl > 14) {
			actions.add(AC_SEEKINGBOMB);
		}
		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Dungeon.hero.lvl > 19) {
			actions.add(AC_CLUSTERBOMB);
		}
		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Dungeon.hero.lvl > 29) {
			actions.add(AC_SEEKINGCLUSTERBOMB);
		}

		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_LIGHTTHROW)) {
			lightingFuse = true;
			action = AC_THROW;
		} else {
			lightingFuse = false;
		}

		if (action.equals(AC_DIZZYBOMB)) {

			hero.spend(TIME_TO_COOK_BOMB);
			hero.busy();

			hero.sprite.operate(hero.pos);

			DizzyBomb dbomb = new DizzyBomb();
			if (dbomb.doPickUp(Dungeon.hero)) {
				GLog.i(Messages.get(Hero.class, "have"), dbomb.name());
			} else {
				Dungeon.level.drop(dbomb, Dungeon.hero.pos).sprite.drop();
			}
			detach(Dungeon.hero.belongings.backpack);
		}

		if (action.equals(AC_SMARTBOMB)) {

			hero.spend(TIME_TO_COOK_BOMB);
			hero.busy();

			hero.sprite.operate(hero.pos);

			SmartBomb smbomb = new SmartBomb();
			if (smbomb.doPickUp(Dungeon.hero)) {
				GLog.i(Messages.get(Hero.class, "have"), smbomb.name());
			} else {
				Dungeon.level.drop(smbomb, Dungeon.hero.pos).sprite.drop();
			}
			detach(Dungeon.hero.belongings.backpack);
		}

		if (action.equals(AC_SEEKINGBOMB)) {

			hero.spend(TIME_TO_COOK_BOMB);
			hero.busy();

			hero.sprite.operate(hero.pos);

			SeekingBombItem sbomb = new SeekingBombItem();
			if (sbomb.doPickUp(Dungeon.hero)) {
				GLog.i(Messages.get(Hero.class, "have"), sbomb.name());
			} else {
				Dungeon.level.drop(sbomb, Dungeon.hero.pos).sprite.drop();
			}
			detach(Dungeon.hero.belongings.backpack);
		}

		if (action.equals(AC_CLUSTERBOMB)) {

			hero.spend(TIME_TO_COOK_BOMB);
			hero.busy();

			hero.sprite.operate(hero.pos);

			ClusterBomb cbomb = new ClusterBomb();
			if (cbomb.doPickUp(Dungeon.hero)) {
				GLog.i(Messages.get(Hero.class, "have"), cbomb.name());
			} else {
				Dungeon.level.drop(cbomb, Dungeon.hero.pos).sprite.drop();
			}
			detach(Dungeon.hero.belongings.backpack);
		}

		if (action.equals(AC_SEEKINGCLUSTERBOMB)) {

			hero.spend(TIME_TO_COOK_BOMB);
			hero.busy();

			hero.sprite.operate(hero.pos);

			SeekingClusterBombItem scbomb = new SeekingClusterBombItem();
			if (scbomb.doPickUp(Dungeon.hero)) {
				GLog.i(Messages.get(Hero.class, "have"), scbomb.name());
			} else {
				Dungeon.level.drop(scbomb, Dungeon.hero.pos).sprite.drop();
			}
			detach(Dungeon.hero.belongings.backpack);
		}

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
			GLog.w(Messages.get(this, "snuff_fuse"));
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
			if (c >= 0 && c < Dungeon.level.getLength()) {
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
					int minDamage = c == cell ? Dungeon.depth * 6 : Dungeon.depth * 3;
					int maxDamage = 5 + Dungeon.depth * 15;

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

	public void genBomb() {
		if (Dungeon.hero.heroClass == HeroClass.ROGUE && Random.Int(1) == 0) {
			Dungeon.level.drop(new Bomb(), Dungeon.level.randomDestination()).sprite.drop();
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
		return (fuse != null ? Messages.get(this, "desc_burning")
				: Messages.get(this, "desc"));
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

		private Bomb bomb;

		public Fuse ignite(Bomb bomb) {
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
					bomb.genBomb();

					Actor.remove(this);
					return true;
				}
			}

			bomb.fuse = null;
			Actor.remove(this);
			return true;
		}
	}

	public static class DoubleBomb extends Bomb {

		{
			name = Messages.get(this, "name");
			image = ItemSpriteSheet.DBL_BOMB;
			stackable = false;
		}

		@Override
		public String info() {
			return Messages.get(this, "desc");
		}

		@Override
		public boolean doPickUp(Hero hero) {
			Bomb bomb = new Bomb();
			bomb.quantity(2);
			if (bomb.doPickUp(hero)) {
				// isaaaaac....
				hero.sprite.showStatus(CharSprite.NEUTRAL, Messages.get(Bomb.class, "free"));
				return true;
			}
			return false;
		}
	}
}
