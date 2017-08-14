package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.items.scrolls.ScrollOfIdentify;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicMapping;
import com.github.epd.sprout.items.scrolls.ScrollOfRemoveCurse;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class UnstableSpellbook extends Artifact {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.ARTIFACT_SPELLBOOK;

		reinforced = true;

		level = 0;
		levelCap = 10;
		levelCapNew = 150;

		charge = (level / 2) + 3;
		partialCharge = 0;
		chargeCap = (level / 2) + 3;

		defaultAction = AC_READ;
	}

	public static final String AC_READ = Messages.get(UnstableSpellbook.class,"ac_read");
	public static final String AC_ADD = Messages.get(UnstableSpellbook.class,"ac_add");

	private final ArrayList<Class> scrolls = new ArrayList<>();

	protected String inventoryTitle = Messages.get(UnstableSpellbook.class,"prompt");
	protected WndBag.Mode mode = WndBag.Mode.SCROLL;

	public UnstableSpellbook() {
		super();

		Class<?>[] scrollClasses = Generator.Category.SCROLL.classes;
		float[] probs = Generator.Category.SCROLL.probs.clone();
		int i = Random.chances(probs);

		while (i != -1) {
			scrolls.add(scrollClasses[i]);
			probs[i] = 0;

			i = Random.chances(probs);
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge > 0 && !cursed)
			actions.add(AC_READ);
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
		if (action.equals(AC_READ)) {

			if (hero.buff(Blindness.class) != null)
				GLog.w(Messages.get(this,"blinded"));
			else if (!isEquipped(hero))
				GLog.i(Messages.get(this,"equip"));
			else if (charge == 0)
				GLog.i(Messages.get(this,"no_charge"));
			else if (cursed)
				GLog.i(Messages.get(this,"cursed"));
			else {
				charge--;

				Scroll scroll;
				if (level < 50){
					do {
						scroll = (Scroll) Generator
								.random(Generator.Category.SCROLL);
					} while (scroll == null ||
							// gotta reduce the rate on these scrolls or that'll be all the
							// item does.
							((scroll instanceof ScrollOfIdentify || scroll instanceof ScrollOfMagicMapping) && Random.Int(4) == 0) ||
							(scroll instanceof ScrollOfRemoveCurse) && Random.Int(5) == 0);

					scroll.ownedByBook = true;
					scroll.execute(hero, AC_READ);
				} else {
					do {
						scroll = (Scroll) Generator
								.random(Generator.Category.SCROLL2);
					} while (scroll == null ||
							// gotta reduce the rate on these scrolls or that'll be all the
							// item does.
							((scroll instanceof ScrollOfIdentify || scroll instanceof ScrollOfMagicMapping) && Random.Int(4) == 0) ||
							(scroll instanceof ScrollOfRemoveCurse) && Random.Int(5) == 0);

					scroll.ownedByBook = true;
					scroll.execute(hero, AC_READ);
				}
			}

		} else if (action.equals(AC_ADD)) {
			GameScene.selectItem(itemSelector, mode, inventoryTitle);
		} else
			super.execute(hero, action);
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new bookRecharge();
	}

	@Override
	public Item upgrade() {
		if (level < 34)
		chargeCap = (((level + 1) / 2) + 3);

		// for artifact transmutation.
		while (scrolls.size() > (levelCap - 1 - level) && scrolls.size() > 0)
			scrolls.remove(0);

		return super.upgrade();
	}

	@Override
	public String desc() {
		String desc = Messages.get(this,"desc");

		desc += "\n\n";

		if (isEquipped(Dungeon.hero)) {

			if (!cursed)
				desc += Messages.get(this,"desc2");
			else
				desc += Messages.get(this,"desc_cursed");

			desc += "\n\n";

		}

		if (level < levelCap)
			if (scrolls.size() > 1)
				desc += Messages.get(this, "desc_index")
						+ Messages.get(this, "desc3")
						+ Messages.get(scrolls.get(0), "name")
						+ Messages.get(this, "and")
						+ Messages.get(scrolls.get(1), "name")
						+ Messages.get(this, "desc4");
			else if (scrolls.size() == 1)
				desc += Messages.get(this, "desc5", Messages.get(scrolls.get(0), "name"));
			else desc += Messages.get(this,"desc6");
		else
			desc += Messages.get(this,"desc6");

		return desc;
	}

	private static final String SCROLLS = "scrolls";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( SCROLLS, scrolls.toArray(new Class[scrolls.size()]) );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		scrolls.clear();
		Collections.addAll(scrolls, bundle.getClassArray(SCROLLS));
	}

	public class bookRecharge extends ArtifactBuff {
		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed) {

				partialCharge += level<10 ? (1 / (150f - level * 15f)) : 1 / 15f;

				if (partialCharge >= 1) {
					partialCharge--;
					charge++;

					if (charge == chargeCap) {
						partialCharge = 0;
					}
				}
			}

			updateQuickslot();

			spend(TICK);

			return true;
		}
	}

	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null && item instanceof Scroll && item.isIdentified()) {
				Hero hero = Dungeon.hero;
				for (int i = 0; (i <= 1 && i < scrolls.size()); i++) {
					if (scrolls.get(i).equals(item.getClass())) {
						hero.sprite.operate(hero.pos);
						hero.busy();
						hero.spend(2f);
						Sample.INSTANCE.play(Assets.SND_BURNING);
						hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);

						scrolls.remove(i);
						item.detach(hero.belongings.backpack);

						upgrade();
						GLog.i(Messages.get(UnstableSpellbook.class,"infuse_scroll"));
						return;
					}
				}
				if (item != null)
					GLog.w(Messages.get(UnstableSpellbook.class,"unable_scroll"));
			} else if (item instanceof Scroll && !item.isIdentified())
				GLog.w(Messages.get(UnstableSpellbook.class,"unknown_scroll"));
		}
	};
}
