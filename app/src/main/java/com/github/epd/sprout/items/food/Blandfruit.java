package com.github.epd.sprout.items.food;


import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.buffs.Barkskin;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.EarthImbue;
import com.github.epd.sprout.actors.buffs.FireImbue;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.ToxicImbue;
import com.github.epd.sprout.actors.buffs.Weakness;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.npcs.Wandmaker;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.SpellSprite;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.potions.PotionOfExperience;
import com.github.epd.sprout.items.potions.PotionOfFrost;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.items.potions.PotionOfInvisibility;
import com.github.epd.sprout.items.potions.PotionOfLevitation;
import com.github.epd.sprout.items.potions.PotionOfLiquidFlame;
import com.github.epd.sprout.items.potions.PotionOfMight;
import com.github.epd.sprout.items.potions.PotionOfMindVision;
import com.github.epd.sprout.items.potions.PotionOfOverHealing;
import com.github.epd.sprout.items.potions.PotionOfParalyticGas;
import com.github.epd.sprout.items.potions.PotionOfPurity;
import com.github.epd.sprout.items.potions.PotionOfStrength;
import com.github.epd.sprout.items.potions.PotionOfToxicGas;
import com.github.epd.sprout.items.scrolls.ScrollOfRecharging;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Blindweed;
import com.github.epd.sprout.plants.Dreamfoil;
import com.github.epd.sprout.plants.Earthroot;
import com.github.epd.sprout.plants.Fadeleaf;
import com.github.epd.sprout.plants.Firebloom;
import com.github.epd.sprout.plants.Flytrap;
import com.github.epd.sprout.plants.Icecap;
import com.github.epd.sprout.plants.Phaseshift;
import com.github.epd.sprout.plants.Plant.Seed;
import com.github.epd.sprout.plants.Sorrowmoss;
import com.github.epd.sprout.plants.Stormvine;
import com.github.epd.sprout.plants.Sungrass;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Blandfruit extends Food {

	public String message = Messages.get(Blandfruit.class, "raw");
	public String info = Messages.get(Blandfruit.class, "desc");

	public Potion potionAttrib = null;
	public ItemSprite.Glowing potionGlow = null;

	{
		name = Messages.get(this, "name");
		stackable = true;
		image = ItemSpriteSheet.BLANDFRUIT;
		energy = (Hunger.STARVING - Hunger.HUNGRY) / 2;
		hornValue = 6; // only applies when blandfruit is cooked

		bones = true;
	}

	@Override
	public boolean isSimilar(Item item) {
		if (item instanceof Blandfruit) {
			if (potionAttrib == null) {
				if (((Blandfruit) item).potionAttrib == null)
					return true;
			} else if (((Blandfruit) item).potionAttrib != null) {
				if (((Blandfruit) item).potionAttrib.getClass() == potionAttrib
						.getClass())
					return true;
			}
		}
		return false;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_EAT)) {

			if (potionAttrib == null) {

				detach(hero.belongings.backpack);

				hero.buff(Hunger.class).satisfy(energy);
				GLog.i(message);

				hero.sprite.operate(hero.pos);
				hero.busy();
				SpellSprite.show(hero, SpellSprite.FOOD);
				Sample.INSTANCE.play(Assets.SND_EAT);

				hero.spend(1f);

			} else {

				hero.buff(Hunger.class).satisfy(Hunger.HUNGRY);

				detach(hero.belongings.backpack);

				hero.spend(1f);
				hero.busy();

				if (potionAttrib instanceof PotionOfFrost) {
					GLog.i(Messages.get(Blandfruit.class, "ice_msg"));
					switch (Random.Int(5)) {
						case 0:
							GLog.i(Messages.get(PotionOfInvisibility.class, "invisible"));
							Buff.affect(hero, Invisibility.class,
									Invisibility.DURATION);
							break;
						case 1:
							GLog.i(Messages.get(FrozenCarpaccio.class, "hard"));
							Buff.affect(hero, Barkskin.class).level(hero.HT / 4);
							break;
						case 2:
							GLog.i(Messages.get(FrozenCarpaccio.class, "refresh"));
							Buff.detach(hero, Poison.class);
							Buff.detach(hero, Cripple.class);
							Buff.detach(hero, Weakness.class);
							Buff.detach(hero, Bleeding.class);
							break;
						case 3:
							GLog.i(Messages.get(FrozenCarpaccio.class, "better"));
							if (hero.HP < hero.HT) {
								hero.HP = Math.min(hero.HP + hero.HT / 4, hero.HT);
								hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
							}
							break;
					}
				} else if (potionAttrib instanceof PotionOfLiquidFlame) {
					GLog.i(Messages.get(Blandfruit.class, "fire_msg"));
					Buff.affect(hero, FireImbue.class).set(FireImbue.DURATION);
				} else if (potionAttrib instanceof PotionOfToxicGas) {
					GLog.i(Messages.get(Blandfruit.class, "toxic_msg"));
					Buff.affect(hero, ToxicImbue.class)
							.set(ToxicImbue.DURATION);
				} else if (potionAttrib instanceof PotionOfParalyticGas) {
					GLog.i(Messages.get(Blandfruit.class, "para_msg"));
					Buff.affect(hero, EarthImbue.class, EarthImbue.DURATION);
				} else
					potionAttrib.apply(hero);

				Sample.INSTANCE.play(Assets.SND_EAT);
				SpellSprite.show(hero, SpellSprite.FOOD);
				hero.sprite.operate(hero.pos);

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
			}
		} else {
			super.execute(hero, action);
		}
	}

	@Override
	public String info() {
		return info;
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

	public Item cook(Seed seed) {

		try {
			return imbuePotion((Potion) seed.alchemyClass.newInstance());
		} catch (Exception e) {
			return null;
		}

	}

	public Item imbuePotion(Potion potion) {

		potionAttrib = potion;
		potionAttrib.ownedByFruit = true;

		potionAttrib.image = ItemSpriteSheet.BLANDFRUIT;

		info = Messages.get(this, "desc_cooked");

		if (potionAttrib instanceof PotionOfHealing) {

			name = Messages.get(this, "sunfruit");
			potionGlow = new ItemSprite.Glowing(0x2EE62E);

		} else if (potionAttrib instanceof PotionOfStrength) {

			name = Messages.get(this, "powerfruit");
			potionGlow = new ItemSprite.Glowing(0xCC0022);

		} else if (potionAttrib instanceof PotionOfMight) {

			name = Messages.get(this, "mightyfruit");
			potionGlow = new ItemSprite.Glowing(0xFF3300);

		} else if (potionAttrib instanceof PotionOfParalyticGas) {

			name = Messages.get(this, "earthfruit");
			potionGlow = new ItemSprite.Glowing(0x67583D);

		} else if (potionAttrib instanceof PotionOfInvisibility) {

			name = Messages.get(this, "blindfruit");
			potionGlow = new ItemSprite.Glowing(0xE5D273);

		} else if (potionAttrib instanceof PotionOfLiquidFlame) {

			name = Messages.get(this, "firefruit");
			potionGlow = new ItemSprite.Glowing(0xFF7F00);

		} else if (potionAttrib instanceof PotionOfFrost) {

			name = Messages.get(this, "icefruit");
			potionGlow = new ItemSprite.Glowing(0x66B3FF);

		} else if (potionAttrib instanceof PotionOfMindVision) {

			name = Messages.get(this, "fadefruit");
			potionGlow = new ItemSprite.Glowing(0xB8E6CF);

		} else if (potionAttrib instanceof PotionOfToxicGas) {

			name = Messages.get(this, "sorrowfruit");
			potionGlow = new ItemSprite.Glowing(0xA15CE5);

		} else if (potionAttrib instanceof PotionOfLevitation) {

			name = Messages.get(this, "stormfruit");
			potionGlow = new ItemSprite.Glowing(0x1C3A57);

		} else if (potionAttrib instanceof PotionOfPurity) {

			name = Messages.get(this, "dreamfruit");
			potionGlow = new ItemSprite.Glowing(0x8E2975);

		} else if (potionAttrib instanceof PotionOfExperience) {

			name = Messages.get(this, "starfruit");
			potionGlow = new ItemSprite.Glowing(0xA79400);

		} else if (potionAttrib instanceof PotionOfOverHealing) {

			name = Messages.get(this, "heartfruit");
			potionGlow = new ItemSprite.Glowing(0xB20000);

		}

		return this;
	}

	public static final String POTIONATTRIB = "potionattrib";

	@Override
	public void cast(final Hero user, int dst) {
		if (potionAttrib instanceof PotionOfLiquidFlame
				|| potionAttrib instanceof PotionOfToxicGas
				|| potionAttrib instanceof PotionOfParalyticGas
				|| potionAttrib instanceof PotionOfFrost
				|| potionAttrib instanceof PotionOfLevitation
				|| potionAttrib instanceof PotionOfPurity) {
			potionAttrib.cast(user, dst);
			detach(user.belongings.backpack);
		} else {
			super.cast(user, dst);
		}

	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(POTIONATTRIB, potionAttrib);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(POTIONATTRIB)) {
			imbuePotion((Potion) bundle.get(POTIONATTRIB));


		} else if (bundle.contains("name")) {
			name = bundle.getString("name");

			if (name.equals("Healthfruit"))
				cook(new Sungrass.Seed());
			else if (name.equals("Powerfruit"))
				cook(new Wandmaker.Rotberry.Seed());
			else if (name.equals("Paralyzefruit"))
				cook(new Earthroot.Seed());
			else if (name.equals("Invisifruit"))
				cook(new Blindweed.Seed());
			else if (name.equals("Flamefruit"))
				cook(new Firebloom.Seed());
			else if (name.equals("Frostfruit"))
				cook(new Icecap.Seed());
			else if (name.equals("Visionfruit"))
				cook(new Fadeleaf.Seed());
			else if (name.equals("Toxicfruit"))
				cook(new Sorrowmoss.Seed());
			else if (name.equals("Floatfruit"))
				cook(new Stormvine.Seed());
			else if (name.equals("Purefruit"))
				cook(new Dreamfoil.Seed());
			else if (name.equals("Mightyfruit"))
				cook(new Phaseshift.Seed());
			else if (name.equals("Heartfruit"))
				cook(new Flytrap.Seed());
		}

	}

	@Override
	public ItemSprite.Glowing glowing() {
		return potionGlow;
	}

}
