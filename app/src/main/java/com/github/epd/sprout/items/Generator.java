/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.npcs.Ghost;
import com.github.epd.sprout.actors.mobs.npcs.Wandmaker.Rotberry;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.ClothArmor;
import com.github.epd.sprout.items.armor.LeatherArmor;
import com.github.epd.sprout.items.armor.MailArmor;
import com.github.epd.sprout.items.armor.PlateArmor;
import com.github.epd.sprout.items.armor.ScaleArmor;
import com.github.epd.sprout.items.artifacts.AlchemistsToolkit;
import com.github.epd.sprout.items.artifacts.Artifact;
import com.github.epd.sprout.items.artifacts.CapeOfThorns;
import com.github.epd.sprout.items.artifacts.ChaliceOfBlood;
import com.github.epd.sprout.items.artifacts.CloakOfShadows;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.HornOfPlenty;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.items.artifacts.RingOfDisintegration;
import com.github.epd.sprout.items.artifacts.RingOfFrost;
import com.github.epd.sprout.items.artifacts.SandalsOfNature;
import com.github.epd.sprout.items.artifacts.TalismanOfForesight;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.artifacts.UnstableSpellbook;
import com.github.epd.sprout.items.bags.Bag;
import com.github.epd.sprout.items.food.Blackberry;
import com.github.epd.sprout.items.food.BlueMilk;
import com.github.epd.sprout.items.food.Blueberry;
import com.github.epd.sprout.items.food.Cloudberry;
import com.github.epd.sprout.items.food.DeathCap;
import com.github.epd.sprout.items.food.Earthstar;
import com.github.epd.sprout.items.food.Food;
import com.github.epd.sprout.items.food.GoldenJelly;
import com.github.epd.sprout.items.food.JackOLantern;
import com.github.epd.sprout.items.food.Moonberry;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.food.Nut;
import com.github.epd.sprout.items.food.Pasty;
import com.github.epd.sprout.items.food.PixieParasol;
import com.github.epd.sprout.items.nornstone.BlueNornStone;
import com.github.epd.sprout.items.nornstone.GreenNornStone;
import com.github.epd.sprout.items.nornstone.NornStone;
import com.github.epd.sprout.items.nornstone.OrangeNornStone;
import com.github.epd.sprout.items.nornstone.PurpleNornStone;
import com.github.epd.sprout.items.nornstone.YellowNornStone;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.potions.PotionOfExperience;
import com.github.epd.sprout.items.potions.PotionOfFrost;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.items.potions.PotionOfInvisibility;
import com.github.epd.sprout.items.potions.PotionOfLevitation;
import com.github.epd.sprout.items.potions.PotionOfLiquidFlame;
import com.github.epd.sprout.items.potions.PotionOfMending;
import com.github.epd.sprout.items.potions.PotionOfMight;
import com.github.epd.sprout.items.potions.PotionOfMindVision;
import com.github.epd.sprout.items.potions.PotionOfOverHealing;
import com.github.epd.sprout.items.potions.PotionOfParalyticGas;
import com.github.epd.sprout.items.potions.PotionOfPurity;
import com.github.epd.sprout.items.potions.PotionOfStrength;
import com.github.epd.sprout.items.potions.PotionOfToxicGas;
import com.github.epd.sprout.items.rings.Ring;
import com.github.epd.sprout.items.rings.RingOfAccuracy;
import com.github.epd.sprout.items.rings.RingOfElements;
import com.github.epd.sprout.items.rings.RingOfEvasion;
import com.github.epd.sprout.items.rings.RingOfForce;
import com.github.epd.sprout.items.rings.RingOfFuror;
import com.github.epd.sprout.items.rings.RingOfHaste;
import com.github.epd.sprout.items.rings.RingOfMagic;
import com.github.epd.sprout.items.rings.RingOfMight;
import com.github.epd.sprout.items.rings.RingOfSharpshooting;
import com.github.epd.sprout.items.rings.RingOfTenacity;
import com.github.epd.sprout.items.rings.RingOfWealth;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.items.scrolls.ScrollOfIdentify;
import com.github.epd.sprout.items.scrolls.ScrollOfLullaby;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicMapping;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicalInfusion;
import com.github.epd.sprout.items.scrolls.ScrollOfMirrorImage;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.scrolls.ScrollOfRage;
import com.github.epd.sprout.items.scrolls.ScrollOfRecharging;
import com.github.epd.sprout.items.scrolls.ScrollOfRegrowth;
import com.github.epd.sprout.items.scrolls.ScrollOfRemoveCurse;
import com.github.epd.sprout.items.scrolls.ScrollOfTeleportation;
import com.github.epd.sprout.items.scrolls.ScrollOfTerror;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.wands.WandOfAmok;
import com.github.epd.sprout.items.wands.WandOfAvalanche;
import com.github.epd.sprout.items.wands.WandOfBlink;
import com.github.epd.sprout.items.wands.WandOfDisintegration;
import com.github.epd.sprout.items.wands.WandOfFirebolt;
import com.github.epd.sprout.items.wands.WandOfFlock;
import com.github.epd.sprout.items.wands.WandOfFrost;
import com.github.epd.sprout.items.wands.WandOfLightning;
import com.github.epd.sprout.items.wands.WandOfMagicMissile;
import com.github.epd.sprout.items.wands.WandOfPoison;
import com.github.epd.sprout.items.wands.WandOfPrismaticLight;
import com.github.epd.sprout.items.wands.WandOfRegrowth;
import com.github.epd.sprout.items.wands.WandOfSlowness;
import com.github.epd.sprout.items.wands.WandOfTelekinesis;
import com.github.epd.sprout.items.wands.WandOfTeleportation;
import com.github.epd.sprout.items.wands.WandOfVenom;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.BattleAxe;
import com.github.epd.sprout.items.weapon.melee.Dagger;
import com.github.epd.sprout.items.weapon.melee.Glaive;
import com.github.epd.sprout.items.weapon.melee.Knuckles;
import com.github.epd.sprout.items.weapon.melee.Longsword;
import com.github.epd.sprout.items.weapon.melee.Mace;
import com.github.epd.sprout.items.weapon.melee.Quarterstaff;
import com.github.epd.sprout.items.weapon.melee.ShortSword;
import com.github.epd.sprout.items.weapon.melee.Spear;
import com.github.epd.sprout.items.weapon.melee.Spork;
import com.github.epd.sprout.items.weapon.melee.Sword;
import com.github.epd.sprout.items.weapon.melee.WarHammer;
import com.github.epd.sprout.items.weapon.missiles.Boomerang;
import com.github.epd.sprout.items.weapon.missiles.CurareDart;
import com.github.epd.sprout.items.weapon.missiles.Dart;
import com.github.epd.sprout.items.weapon.missiles.IncendiaryDart;
import com.github.epd.sprout.items.weapon.missiles.Javelin;
import com.github.epd.sprout.items.weapon.missiles.Shuriken;
import com.github.epd.sprout.items.weapon.missiles.Tamahawk;
import com.github.epd.sprout.plants.BlandfruitBush;
import com.github.epd.sprout.plants.Blindweed;
import com.github.epd.sprout.plants.Dewcatcher;
import com.github.epd.sprout.plants.Dreamfoil;
import com.github.epd.sprout.plants.Earthroot;
import com.github.epd.sprout.plants.Fadeleaf;
import com.github.epd.sprout.plants.Firebloom;
import com.github.epd.sprout.plants.Flytrap;
import com.github.epd.sprout.plants.Icecap;
import com.github.epd.sprout.plants.Phaseshift;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.plants.Sorrowmoss;
import com.github.epd.sprout.plants.Starflower;
import com.github.epd.sprout.plants.Stormvine;
import com.github.epd.sprout.plants.Sungrass;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Generator {

	public enum Category {
		WEAPON(150, Weapon.class), ARMOR(100, Armor.class), POTION(500, Potion.class),
		SCROLL(400, Scroll.class), WAND(60, Wand.class), RING(30, Ring.class),
		ARTIFACT(40, Artifact.class), SEED(0, Plant.Seed.class),
		SEED2(0,	Plant.Seed.class), SEEDRICH(0,	Plant.Seed.class),
		FOOD(0, Food.class), GOLD(500, Gold.class), BERRY(50, Food.class), MUSHROOM(0, Food.class),
		NORNSTONE(0,NornStone.class), NORNSTONE2(0,NornStone.class), SCROLL2(0, Scroll.class);

		public Class<?>[] classes;
		public float[] probs;

		public float prob;
		public Class<? extends Item> superClass;

		Category(float prob, Class<? extends Item> superClass) {
			this.prob = prob;
			this.superClass = superClass;
		}

		public static int order(Item item) {
			for (int i = 0; i < values().length; i++) {
				if (values()[i].superClass.isInstance(item)) {
					return i;
				}
			}

			return item instanceof Bag ? Integer.MAX_VALUE
					: Integer.MAX_VALUE - 1;
		}
	}

	private static HashMap<Category, Float> categoryProbs = new HashMap<Generator.Category, Float>();
	
	private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{  0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0};

	static {

		Category.GOLD.classes = new Class<?>[] { Gold.class };
		Category.GOLD.probs = new float[] { 1 };

		Category.SCROLL.classes = new Class<?>[] { ScrollOfIdentify.class,
				ScrollOfTeleportation.class, ScrollOfRemoveCurse.class,
				ScrollOfUpgrade.class, ScrollOfRecharging.class,
				ScrollOfMagicMapping.class, ScrollOfRage.class,
				ScrollOfTerror.class, ScrollOfLullaby.class,
				ScrollOfMagicalInfusion.class, ScrollOfPsionicBlast.class,
				ScrollOfMirrorImage.class, ScrollOfRegrowth.class };
		Category.SCROLL.probs = new float[] { 20,
				10, 8,
				0, 15,
				15, 10,
				8, 8,
				0, 4,
				10, 0 };

		Category.SCROLL2.classes = new Class<?>[] { ScrollOfIdentify.class,
				ScrollOfTeleportation.class, ScrollOfRemoveCurse.class,
				ScrollOfUpgrade.class, ScrollOfRecharging.class,
				ScrollOfMagicMapping.class, ScrollOfRage.class,
				ScrollOfTerror.class, ScrollOfLullaby.class,
				ScrollOfMagicalInfusion.class, ScrollOfPsionicBlast.class,
				ScrollOfMirrorImage.class, ScrollOfRegrowth.class };
		Category.SCROLL2.probs = new float[] { 8,
				10, 4,
				2, 15,
				6, 10,
				8, 8,
				1, 4,
				10, 5 };

		Category.POTION.classes = new Class<?>[] { PotionOfHealing.class,
				PotionOfExperience.class, PotionOfToxicGas.class,
				PotionOfParalyticGas.class, PotionOfLiquidFlame.class,
				PotionOfLevitation.class, PotionOfStrength.class,
				PotionOfMindVision.class, PotionOfPurity.class,
				PotionOfInvisibility.class, PotionOfMight.class,
				PotionOfFrost.class, PotionOfMending.class,
				PotionOfOverHealing.class, Egg.class};
		Category.POTION.probs = new float[] { 10,
				4, 15,
				10, 15,
				10, 0,
				20, 12,
				10, 0,
				10, 45,
				4, 10};

		Category.WAND.classes = new Class<?>[] { WandOfTeleportation.class,
				WandOfSlowness.class, WandOfFirebolt.class,
				WandOfRegrowth.class, WandOfPoison.class, WandOfBlink.class,
				WandOfLightning.class, WandOfAmok.class,
				WandOfTelekinesis.class, WandOfFlock.class, WandOfMagicMissile.class,
				WandOfDisintegration.class, WandOfAvalanche.class,
				WandOfPrismaticLight.class, WandOfVenom.class,
				WandOfFrost.class};
		Category.WAND.probs = new float[] { 10,
				10, 10,
				10, 10, 10,
				10, 10,
				10, 8, 0,
				10, 10,
				10, 10,
				10};

		Category.WEAPON.classes = new Class<?>[] { Dagger.class,
				Knuckles.class, Quarterstaff.class, Spear.class, Mace.class,
				Sword.class, Longsword.class, BattleAxe.class, WarHammer.class,
				Glaive.class, ShortSword.class, Dart.class, Javelin.class,
				IncendiaryDart.class, CurareDart.class, Shuriken.class,
				Boomerang.class, Tamahawk.class, Spork.class };
		Category.WEAPON.probs = new float[] { 1,
				1, 1, 1, 1,
				1, 1, 1, 1,
				1, 0, 0, 1,
				1, 1, 1,
				0, 1, 0 };

		Category.ARMOR.classes = new Class<?>[] { ClothArmor.class,
				LeatherArmor.class, MailArmor.class, ScaleArmor.class,
				PlateArmor.class };
		Category.ARMOR.probs = new float[] { 1,
				1, 1, 1,
				1 };

		Category.FOOD.classes = new Class<?>[] { Food.class, Pasty.class, MysteryMeat.class };
		Category.FOOD.probs = new float[] { 4, 1, 0 };

		Category.RING.classes = new Class<?>[] { RingOfAccuracy.class,
				RingOfEvasion.class, RingOfElements.class, RingOfForce.class,
				RingOfFuror.class, RingOfHaste.class, RingOfMagic.class,
				RingOfMight.class, RingOfSharpshooting.class,
				RingOfTenacity.class, RingOfWealth.class };
		Category.RING.probs = new float[] { 10,
				10, 10, 10,
				10, 10, 2,
				10, 10,
				10, 0 };

		Category.ARTIFACT.classes = new Class<?>[] { CapeOfThorns.class,
				ChaliceOfBlood.class, CloakOfShadows.class, HornOfPlenty.class,
				MasterThievesArmband.class, SandalsOfNature.class,
				TalismanOfForesight.class, TimekeepersHourglass.class,
				UnstableSpellbook.class, AlchemistsToolkit.class, RingOfDisintegration.class,
				RingOfFrost.class,
				DriedRose.class // starts with no chance of spawning, chance is
								// set directly after beating ghost quest.
		};
		Category.ARTIFACT.probs =  INITIAL_ARTIFACT_PROBS.clone();

		Category.SEED.classes = new Class<?>[] { 
				Firebloom.Seed.class, Icecap.Seed.class, Sorrowmoss.Seed.class, Blindweed.Seed.class, Sungrass.Seed.class,
				Earthroot.Seed.class, Fadeleaf.Seed.class, Rotberry.Seed.class, BlandfruitBush.Seed.class, Dreamfoil.Seed.class,
				Stormvine.Seed.class, Nut.class, Blackberry.class, Blueberry.class, Cloudberry.class,
				Moonberry.class, Starflower.Seed.class, Phaseshift.Seed.class, Flytrap.Seed.class, Dewcatcher.Seed.class};
		
		Category.SEED.probs = new float[] { 12, 12, 12, 12, 12,
				                            12, 12, 0, 2, 12,
				                            12, 48, 20, 4, 16,
				                            2, 1, 1, 4, (Dungeon.isChallenged(Challenges.NO_SCROLLS)?12:8)};
		
		
		Category.SEED2.classes = new Class<?>[] { Firebloom.Seed.class,
				Icecap.Seed.class, Sorrowmoss.Seed.class, Blindweed.Seed.class,
				Sungrass.Seed.class, Earthroot.Seed.class, Fadeleaf.Seed.class,
				Rotberry.Seed.class, BlandfruitBush.Seed.class,
				Dreamfoil.Seed.class, Stormvine.Seed.class,
				Starflower.Seed.class, Phaseshift.Seed.class, Flytrap.Seed.class,
				Dewcatcher.Seed.class};
		
		Category.SEED2.probs = new float[] { 12,
				12, 12, 12,
				12, 12, 12,
				0, 4,
				12, 12,
				2, 2, 6,
				8};
		
		Category.SEEDRICH.classes = new Class<?>[] { Firebloom.Seed.class,
				Icecap.Seed.class, Sorrowmoss.Seed.class, Blindweed.Seed.class,
				Sungrass.Seed.class, Earthroot.Seed.class, Fadeleaf.Seed.class,
				Rotberry.Seed.class, BlandfruitBush.Seed.class,
				Dreamfoil.Seed.class, Stormvine.Seed.class,
				Starflower.Seed.class, Phaseshift.Seed.class, Flytrap.Seed.class,
				Dewcatcher.Seed.class};
		
		Category.SEEDRICH.probs = new float[] { 1,
				1, 1, 1,
				2, 1, 1,
				0, 4,
				1, 1,
				4, 4, 8,
				6};
		
		Category.BERRY.classes = new Class<?>[] {Blackberry.class, Blueberry.class, Cloudberry.class, Moonberry.class};
		Category.BERRY.probs = new float[] {8,2,2,1};	
		
		Category.MUSHROOM.classes = new Class<?>[] {BlueMilk.class, DeathCap.class, Earthstar.class, JackOLantern.class, PixieParasol.class, GoldenJelly.class};
		Category.MUSHROOM.probs = new float[] {2,2,2,2,2,2};
		
		Category.NORNSTONE.classes = new Class<?>[] {BlueNornStone.class, GreenNornStone.class, OrangeNornStone.class, PurpleNornStone.class, YellowNornStone.class};
		Category.NORNSTONE.probs = new float[] {2,2,2,2,2};
		
		Category.NORNSTONE2.classes = new Class<?>[] {BlueNornStone.class, GreenNornStone.class, OrangeNornStone.class, PurpleNornStone.class, YellowNornStone.class};
		Category.NORNSTONE2.probs = new float[] {2,0,2,2,2};
		
	}

	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put(cat, cat.prob);
		}
	}

	public static Item random() {
		return random(Random.chances(categoryProbs));
	}

	public static Item random(Category cat) {
		try {

			categoryProbs.put(cat, categoryProbs.get(cat) / 2);

			switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case ARTIFACT:
				Item item = randomArtifact();
				// if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				return ((Item) cat.classes[Random.chances(cat.probs)]
						.newInstance()).random();
			}

		} catch (Exception e) {

			return null;

		}
	}

	public static Item random(Class<? extends Item> cl) {
		try {

			return cl.newInstance().random();

		} catch (Exception e) {

			return null;

		}
	}

	public static Armor randomArmor() {
		int curStr = Hero.STARTING_STR
				+ Dungeon.limitedDrops.strengthPotions.count;

		return randomArmor(curStr);
	}

	public static Armor randomArmor(int targetStr) {

		Category cat = Category.ARMOR;

		try {
			Armor a1 = (Armor) cat.classes[Random.chances(cat.probs)]
					.newInstance();
			Armor a2 = (Armor) cat.classes[Random.chances(cat.probs)]
					.newInstance();

			a1.random();
			a2.random();

			return Math.abs(targetStr - a1.STR) < Math.abs(targetStr - a2.STR) ? a1
					: a2;
		} catch (Exception e) {
			return null;
		}
	}

	public static Weapon randomWeapon() {
		int curStr = Hero.STARTING_STR
				+ Dungeon.limitedDrops.strengthPotions.count;

		return randomWeapon(curStr);
	}

	public static Weapon randomWeapon(int targetStr) {

		try {
			Category cat = Category.WEAPON;

			Weapon w1 = (Weapon) cat.classes[Random.chances(cat.probs)]
					.newInstance();
			Weapon w2 = (Weapon) cat.classes[Random.chances(cat.probs)]
					.newInstance();

			w1.random();
			w2.random();

			return Math.abs(targetStr - w1.STR) < Math.abs(targetStr - w2.STR) ? w1
					: w2;
		} catch (Exception e) {
			return null;
		}
	}

	// enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		try {
			Category cat = Category.ARTIFACT;
			int i = Random.chances(cat.probs);

			// if no artifacts are left, return null
			if (i == -1) {
				return null;
			}

			Artifact artifact = (Artifact) cat.classes[i].newInstance();

			// remove the chance of spawning this artifact.
			cat.probs[i] = 0;
			spawnedArtifacts.add(cat.classes[i].getSimpleName());

			artifact.random();

			return artifact;

		} catch (Exception e) {
			return null;
		}
	}

	public static boolean removeArtifact(Artifact artifact) {
		if (spawnedArtifacts.contains(artifact.getClass().getSimpleName()))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifact.getClass())) {
				if (cat.probs[i] == 1) {
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact.getClass().getSimpleName());
					return true;
				} else
					return false;
			}

		return false;
	}

	// resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();
		
		//checks for dried rose quest completion, adds the rose in accordingly.
		if (Ghost.Quest.completed()) Category.ARTIFACT.probs[12] = 1;
		spawnedArtifacts = new ArrayList<String>();
	}

	private static ArrayList<String> spawnedArtifacts = new ArrayList<String>();

	private static final String ARTIFACTS = "artifacts";

	// used to store information on which artifacts have been spawned.
	public static void storeInBundle(Bundle bundle) {
		bundle.put(ARTIFACTS,
				spawnedArtifacts.toArray(new String[spawnedArtifacts.size()]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		initArtifacts();

		if (bundle.contains(ARTIFACTS)) {
			Collections.addAll(spawnedArtifacts,
					bundle.getStringArray(ARTIFACTS));
			Category cat = Category.ARTIFACT;

			for (String artifact : spawnedArtifacts)
				for (int i = 0; i < cat.classes.length; i++)
					if (cat.classes[i].getSimpleName().equals(artifact))
						cat.probs[i] = 0;
		}
	}
}
