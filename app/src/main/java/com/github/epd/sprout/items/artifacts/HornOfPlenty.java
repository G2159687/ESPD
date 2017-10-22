package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.SpellSprite;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.Blandfruit;
import com.github.epd.sprout.items.food.Food;
import com.github.epd.sprout.items.scrolls.ScrollOfRecharging;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HornOfPlenty extends Artifact {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ARTIFACT_HORN1;

		level = 0;
		levelCap = 10;

		charge = 0;
		partialCharge = 0;
		chargeCap = 10;
		reinforced = true;

		defaultAction = AC_EAT;
	}

	private static final float TIME_TO_EAT = 3f;

	public static final String AC_EAT = Messages.get(HornOfPlenty.class, "ac_eat");
	public static final String AC_STORE = Messages.get(HornOfPlenty.class, "ac_store");

	protected String inventoryTitle = Messages.get(this, "prompt");
	protected WndBag.Mode mode = WndBag.Mode.FOOD;

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge > 0)
			actions.add(AC_EAT);
		if (isEquipped(hero) && level < 10 && !cursed)
			actions.add(AC_STORE);
		return actions;
	}

	@Override
	public boolean isUpgradable() {
		return true;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			if (!isEquipped(hero))
				GLog.i(Messages.get(this, "equip"));
			else if (charge == 0)
				GLog.i(Messages.get(this, "no_food"));
			else {
				hero.buff(Hunger.class).satisfy(70f * charge);

				// if you get at least 140 food energy from the horn
				if (charge >= 2) {
					switch (hero.heroClass) {
						case WARRIOR:
							if (hero.HP < hero.HT) {
								hero.HP = Math.min(hero.HP + 5, hero.HT);
								hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
							}
							break;
						case MAGE:
							hero.belongings.charge(false);
							ScrollOfRecharging.charge(hero);
							break;
						case ROGUE:
						case HUNTRESS:
							break;
					}

					if (charge >= 8) {
						if (level > 15) {
							if (hero.HP < hero.HT) {
								hero.HP = hero.HP + Random.IntRange(1, hero.HT - hero.HP);
								hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
								GLog.p(Messages.get(HornOfPlenty.class,"hp"));
							}
						}
						if (level > 47) {
							if (Random.Float() < 0.7f) {
								hero.HT += Random.IntRange(5, 10);
								hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 2);
								GLog.p(Messages.get(HornOfPlenty.class,"ht"));
							}
						}
					}
				}
				charge = 0;

				hero.sprite.operate(hero.pos);
				hero.busy();
				SpellSprite.show(hero, SpellSprite.FOOD);
				Sample.INSTANCE.play(Assets.SND_EAT);
				GLog.i(Messages.get(this, "eat"));

				hero.spend(TIME_TO_EAT);
				image = ItemSpriteSheet.ARTIFACT_HORN1;

				updateQuickslot();
			}

		} else if (action.equals(AC_STORE)) {
			GameScene.selectItem(itemSelector, mode, inventoryTitle);
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new hornRecharge();
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc");

		if (charge == 0)
			desc += Messages.get(this, "1");
		else if (charge < 3)
			desc += Messages.get(this, "2");
		else if (charge < 7)
			desc += Messages.get(this, "3");
		else if (charge < 10)
			desc += Messages.get(this, "4");
		else
			desc += Messages.get(this, "5");

		if (isEquipped(Dungeon.hero)) {
			if (!cursed) {
				desc += Messages.get(this, "6");

				if (level < 15)
					desc += Messages.get(this, "desc_hint");
			} else {
				desc += Messages.get(this, "desc_cursed");
			}
		}

		return desc;
	}

	public class hornRecharge extends ArtifactBuff {

		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed) {

				// generates 0.25 food value every round, +0.015 value per level
				// to a max of 0.70 food value per round (0.25+0.5, at level 30)
				partialCharge += 0.25f + (0.05f * level);

				// charge is in increments of 36 food value.
				if (partialCharge >= 50) {
					charge++;
					partialCharge -= 50;

					if (charge == chargeCap)
						image = ItemSpriteSheet.ARTIFACT_HORN4;
					else if (charge >= 7)
						image = ItemSpriteSheet.ARTIFACT_HORN3;
					else if (charge >= 3)
						image = ItemSpriteSheet.ARTIFACT_HORN2;
					else
						image = ItemSpriteSheet.ARTIFACT_HORN1;

					if (charge == chargeCap) {
						GLog.p(Messages.get(HornOfPlenty.class, "full"));
						partialCharge = 0;
					}

					updateQuickslot();
				}
			} else
				partialCharge = 0;

			spend(TICK);

			return true;
		}

	}

	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null && item instanceof Food) {
				if (item instanceof Blandfruit
						&& ((Blandfruit) item).potionAttrib == null) {
					GLog.w(Messages.get(HornOfPlenty.class, "reject"));
				} else {
					Hero hero = Dungeon.hero;
					hero.sprite.operate(hero.pos);
					hero.busy();
					hero.spend(TIME_TO_EAT);

					curItem.upgrade();
					if (curItem.level >= 10) {
						curItem.level = 10;
						GLog.p(Messages.get(HornOfPlenty.class, "maxlevel"));
					} else
						GLog.p(Messages.get(HornOfPlenty.class, "levelup"));
					item.detach(hero.belongings.backpack);
				}

			}
		}
	};

}
