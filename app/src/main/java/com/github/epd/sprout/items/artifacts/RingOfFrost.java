package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Frost;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.effects.particles.SnowParticle;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RingOfFrost extends Artifact {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.RING_FROST;

		level = 0;
		exp = 0;
		levelCap = 10;

		charge = 0;
		partialCharge = 0;
		chargeCap = 100;
		reinforced = true;

		defaultAction = AC_BLAST;
	}

	protected String inventoryTitle = Messages.get(RingOfFrost.class, "invtitle");
	protected WndBag.Mode mode = WndBag.Mode.SCROLL;

	public static int consumedpts = 0;

	public static final String AC_BLAST = Messages.get(RingOfFrost.class, "ac_blast");
	public static final String AC_ADD = Messages.get(RingOfFrost.class, "ac_add");


	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge == 100 && !cursed)
			actions.add(AC_BLAST);
		if (isEquipped(hero) && level < levelCap && !cursed)
			actions.add(AC_ADD);
		return actions;
	}

	@Override
	public boolean isUpgradable() {
		return true;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_BLAST)) {

			if (!isEquipped(hero))
				GLog.i(Messages.get(RingOfFrost.class, "equip"));
			else if (charge != chargeCap)
				GLog.i(Messages.get(RingOfFrost.class, "no_charge"));
			else {

				blast(hero.pos);
				charge = 0;
				updateQuickslot();
				CellEmitter.get(hero.pos).start(SnowParticle.FACTORY, 0.2f, 6);

			}

		} else if (action.equals(AC_ADD)) {
			GameScene.selectItem(itemSelector, mode, inventoryTitle);
		}
	}

	private int distance() {
		return (level() * 2) + 1;
	}

	public int level() {
		return level;
	}

	public void blast(int cell) {
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob.hostile) {
				int dist = Level.distance(cell, mob.pos);
				if (dist <= distance()) {
					mob.damage(Random.Int(level(), (level() * level()) / 10 + 1), this);
					Buff.prolong(mob, Frost.class, Frost.duration(mob) * Random.Float(1f * level(), 1.5f * level()));
					CellEmitter.get(mob.pos).start(SnowParticle.FACTORY, 0.2f, 6);
				}
			}
		}
		updateQuickslot();
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new ringRecharge();
	}

	@Override
	public String desc() {
		String desc = Messages.get(RingOfFrost.class, "desc");
		if (isEquipped(Dungeon.hero)) {
			desc += "\n\n";
			if (charge < 100)
				desc += Messages.get(RingOfFrost.class, "desc_2");
			else
				desc += Messages.get(RingOfFrost.class, "desc_3");
		}

		return desc;
	}

	public class ringRecharge extends ArtifactBuff {
		@Override
		public boolean act() {
			if (charge < chargeCap) {
				partialCharge += 1 + (level * level);
				if (partialCharge >= 10) {
					charge++;
					partialCharge = 0;
					if (charge == chargeCap) {
						partialCharge = 0;
					}
				}
			} else partialCharge = 0;

			updateQuickslot();

			spend(TICK);

			return true;
		}

	}

	private static final String PARTIALCHARGE = "partialCharge";
	private static final String CHARGE = "charge";
	private static final String CONSUMED = "consumedpts";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PARTIALCHARGE, partialCharge);
		bundle.put(CHARGE, charge);
		bundle.put(CONSUMED, consumedpts);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		partialCharge = bundle.getInt(PARTIALCHARGE);
		charge = bundle.getInt(CHARGE);
		consumedpts = bundle.getInt(CONSUMED);
	}

	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null && item instanceof Scroll && item.isIdentified()) {
				Hero hero = Dungeon.hero;
				int scrollWorth = item.consumedValue;
				consumedpts += scrollWorth;

				hero.sprite.operate(hero.pos);
				hero.busy();
				hero.spend(2f);
				Sample.INSTANCE.play(Assets.SND_BURNING);
				hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);

				item.detach(hero.belongings.backpack);
				GLog.i(Messages.get(RingOfFrost.class, "points", consumedpts));

				int levelChk = ((level() * level() / 2) + 1) * 5;

				if (consumedpts > levelChk && level() < 10) {
					upgrade();
					GLog.p(Messages.get(RingOfFrost.class, "better"));
				}

			} else if (item instanceof Scroll && !item.isIdentified()) {
				GLog.w(Messages.get(RingOfFrost.class, "notsure"));
			} else if (item != null) {
				GLog.w(Messages.get(RingOfFrost.class, "unable"));
			}
		}
	};
}
