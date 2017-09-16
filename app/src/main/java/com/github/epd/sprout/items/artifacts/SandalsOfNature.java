package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.Water;
import com.github.epd.sprout.actors.buffs.Barkskin;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.EarthParticle;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;

public class SandalsOfNature extends Artifact {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ARTIFACT_SANDALS;

		level = 0;
		levelCap = 3;

		charge = 0;

		defaultAction = AC_ROOT;
		reinforced = true;
	}

	public static final String[] NAMES = {Messages.get(SandalsOfNature.class, "name"),
			Messages.get(SandalsOfNature.class, "name_1"), Messages.get(SandalsOfNature.class, "name_2"), Messages.get(SandalsOfNature.class, "name_3")};
	public static final String AC_FEED = Messages.get(SandalsOfNature.class, "ac_feed");
	public static final String AC_ROOT = Messages.get(SandalsOfNature.class, "ac_root");
	public static final String AC_GROW = Messages.get(SandalsOfNature.class, "ac_grow");

	protected String inventoryTitle = Messages.get(SandalsOfNature.class, "prompt");
	protected WndBag.Mode mode = WndBag.Mode.SEED;

	public ArrayList<String> seeds = new ArrayList<>();

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && level < 3 && !cursed)
			actions.add(AC_FEED);
		if (isEquipped(hero) && charge > 0)
			actions.add(AC_ROOT);
		if (isEquipped(hero) && charge > 50)
			actions.add(AC_GROW);
		return actions;
	}

	@Override
	public boolean isUpgradable() {
		return true;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_FEED)) {
			GameScene.selectItem(itemSelector, mode, inventoryTitle);
		} else if (action.equals(AC_ROOT) && level > 0) {

			if (!isEquipped(hero))
				GLog.i(Messages.get(SandalsOfNature.class, "equip"));
			else if (charge == 0)
				GLog.i(Messages.get(SandalsOfNature.class, "no_charge"));
			else {
				Buff.affect(hero, Barkskin.class).level(charge / 2);
				CellEmitter.bottom(hero.pos).start(EarthParticle.FACTORY,
						0.05f, 8);
				charge = 0;
				updateQuickslot();
			}
		} else if (action.equals(AC_GROW) && level > 0) {

			if (!isEquipped(hero))
				GLog.i(Messages.get(SandalsOfNature.class, "equip"));
			else if (charge < 50)
				GLog.i(Messages.get(SandalsOfNature.class, "no_charge"));
			else {
				growPlant();
				charge = charge - 50;
				updateQuickslot();
			}
		}
	}

	protected void growPlant() {
		Dungeon.hero.sprite.operate(Dungeon.hero.pos);
		int length = Level.getLength();
		for (int i = 0; i < length; i++) {
			GameScene.add(Blob.seed(i, (2) * 20, Water.class));
		}
		GLog.p(Messages.get(SandalsOfNature.class, "grow"));
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Naturalism();
	}

	@Override
	public String desc() {
		String desc = "";
		if (level == 0)
			desc += Messages.get(SandalsOfNature.class, "desc_1");
		else if (level == 1)
			desc += Messages.get(SandalsOfNature.class, "desc_2");
		else if (level == 2)
			desc += Messages.get(SandalsOfNature.class, "desc_3");
		else
			desc += Messages.get(SandalsOfNature.class, "desc_4");

		if (isEquipped(Dungeon.hero)) {
			desc += "\n\n";
			if (level == 0) {
				if (!cursed)
					desc += Messages.get(SandalsOfNature.class, "desc_5");
				else
					desc += Messages.get(SandalsOfNature.class, "desc_6");
			} else if (level == 1)
				desc += Messages.get(SandalsOfNature.class, "desc_7");
			else if (level == 2)
				desc += Messages.get(SandalsOfNature.class, "desc_8");
			else
				desc += Messages.get(SandalsOfNature.class, "desc_9");

			if (!cursed)
				desc += Messages.get(SandalsOfNature.class, "desc_hint");
			else
				desc += Messages.get(SandalsOfNature.class, "desc_cursed");

			if (level > 0)
				desc += "\n\n" + Messages.get(SandalsOfNature.class, "desc_ability");
		}

		if (!seeds.isEmpty()) {
			desc += "\n\n" + Messages.get(SandalsOfNature.class, "desc_10", seeds.size());
		}

		return desc;
	}

	@Override
	public Item upgrade() {
		if (level < 3) {
			if (level < 0)
				image = ItemSpriteSheet.ARTIFACT_SANDALS;
			else if (level == 0)
				image = ItemSpriteSheet.ARTIFACT_SHOES;
			else if (level == 1)
				image = ItemSpriteSheet.ARTIFACT_BOOTS;
			else if (level >= 2)
				image = ItemSpriteSheet.ARTIFACT_GREAVES;
			name = NAMES[level + 1];
		}
		return super.upgrade();
	}

	private static final String SEEDS = "seeds";
	private static final String NAME = "name";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(NAME, name);
		bundle.put(SEEDS, seeds.toArray(new String[seeds.size()]));
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		name = bundle.getString(NAME);
		if (bundle.contains(SEEDS))
			Collections.addAll(seeds, bundle.getStringArray(SEEDS));
	}

	public class Naturalism extends ArtifactBuff {
		public void charge() {
			if (charge < 5 * level) {
				charge++;
				updateQuickslot();
			}
		}
	}

	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null && item instanceof Plant.Seed) {
				if (seeds.contains(item.name())) {
					GLog.w(Messages.get(SandalsOfNature.class, "already_fed"));
				} else {
					seeds.add(item.name());

					Hero hero = Dungeon.hero;
					hero.sprite.operate(hero.pos);
					Sample.INSTANCE.play(Assets.SND_PLANT);
					hero.busy();
					hero.spend(2f);
					if (seeds.size() >= 5 + (level * 2)) {
						seeds.clear();
						upgrade();
						if (level >= 1 && level <= 3) {
							GLog.p(Messages.get(SandalsOfNature.class, "levelup"));
						}

					} else {
						GLog.i(Messages.get(SandalsOfNature.class, "absorb_seed"));
					}
					item.detach(hero.belongings.backpack);
				}
			}
		}
	};

}
