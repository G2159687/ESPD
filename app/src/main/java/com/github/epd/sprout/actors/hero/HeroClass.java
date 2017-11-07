
package com.github.epd.sprout.actors.hero;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Badges;
import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.items.DewVial;
import com.github.epd.sprout.items.TomeOfMastery;
import com.github.epd.sprout.items.armor.ClothArmor;
import com.github.epd.sprout.items.artifacts.CloakOfShadows;
import com.github.epd.sprout.items.bags.AnkhChain;
import com.github.epd.sprout.items.bags.KeyRing;
import com.github.epd.sprout.items.bags.PotionBandolier;
import com.github.epd.sprout.items.bags.ScrollHolder;
import com.github.epd.sprout.items.bags.SeedPouch;
import com.github.epd.sprout.items.bags.WandHolster;
import com.github.epd.sprout.items.food.Food;
import com.github.epd.sprout.items.potions.PotionOfMindVision;
import com.github.epd.sprout.items.potions.PotionOfStrength;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.items.scrolls.ScrollOfIdentify;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicMapping;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicalInfusion;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.scrolls.ScrollOfRemoveCurse;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.items.teleporter.AncientCoin;
import com.github.epd.sprout.items.teleporter.Bone;
import com.github.epd.sprout.items.teleporter.CavesKey;
import com.github.epd.sprout.items.teleporter.CityKey;
import com.github.epd.sprout.items.teleporter.ConchShell;
import com.github.epd.sprout.items.teleporter.HallsKey;
import com.github.epd.sprout.items.teleporter.OtilukesJournal;
import com.github.epd.sprout.items.teleporter.PrisonKey;
import com.github.epd.sprout.items.teleporter.SewersKey;
import com.github.epd.sprout.items.teleporter.TenguKey;
import com.github.epd.sprout.items.teleporter.Town;
import com.github.epd.sprout.items.wands.WandOfMagicMissile;
import com.github.epd.sprout.items.weapon.melee.Dagger;
import com.github.epd.sprout.items.weapon.melee.Glaive;
import com.github.epd.sprout.items.weapon.melee.Knuckles;
import com.github.epd.sprout.items.weapon.melee.ShortSword;
import com.github.epd.sprout.items.weapon.missiles.Boomerang;
import com.github.epd.sprout.items.weapon.missiles.Dart;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR("warrior"), MAGE("mage"), ROGUE("rogue"), HUNTRESS("huntress");

	private String title;

	HeroClass(String title) {
		this.title = title;
	}

	// TODO: Remove these things
	private static void initDebug() {
		new Glaive().reinforce().upgrade(300).collect();
		new Town().collect();
		new SewersKey().collect();
		new PrisonKey().collect();
		new CavesKey().collect();
		new CityKey().collect();
		new HallsKey().collect();
		new AncientCoin().collect();
		new Bone().collect();
		new ConchShell().collect();
		new TenguKey().collect();
	}

	public void initHero(Hero hero) {

		hero.heroClass = this;

		initCommon(hero);

		switch (this) {
			case WARRIOR:
				initWarrior(hero);
				break;

			case MAGE:
				initMage(hero);
				break;

			case ROGUE:
				initRogue(hero);
				break;

			case HUNTRESS:
				initHuntress(hero);
				break;
		}

		if (Badges.isUnlocked(masteryBadge())) {
			new TomeOfMastery().collect();
		}

		hero.updateAwareness();
	}

	private static void initCommon(Hero hero) {
		(hero.belongings.armor = new ClothArmor()).identify();
		new Food().identify().collect();
		new SeedPouch().identify().collect();
		new ScrollHolder().identify().collect();
		new PotionBandolier().identify().collect();
		new DewVial().identify().collect();
		new WandHolster().identify().collect();
		new AnkhChain().identify().collect();
		new OtilukesJournal().identify().collect();

		initDebug();
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior(Hero hero) {
		if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
			hero.STR = hero.STR + 10;
		} else {
			hero.STR = hero.STR + 1;
		}
		if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
			hero.HT = hero.HT + 1000;
			hero.HP = hero.HT;
		}
		if (Dungeon.isChallenged(Challenges.NO_HEALING)) {
			hero.attackSkill = hero.attackSkill + 1000;
			hero.defenseSkill = hero.defenseSkill + 1000;
		}


		(hero.belongings.weapon = new ShortSword()).identify();
		Dart darts = new Dart(8);
		darts.identify().collect();

		Dungeon.quickslot.setSlot(0, darts);

		KeyRing keyring = new KeyRing();
		keyring.collect();

		new PotionOfStrength().setKnown();

		//playtest(hero);
	}

	private static void initMage(Hero hero) {

		if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
			hero.STR = hero.STR + 10;
		}
		if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
			hero.HT = hero.HT + 100;
			hero.HP = hero.HT;
		}
		if (Dungeon.isChallenged(Challenges.NO_HEALING)) {
			hero.attackSkill = hero.attackSkill + 100;
			hero.defenseSkill = hero.defenseSkill + 100;
		}

		(hero.belongings.weapon = new Knuckles()).identify();

		WandOfMagicMissile wand = new WandOfMagicMissile();
		wand.identify().collect();

		KeyRing keyring = new KeyRing();
		keyring.collect();

		Dungeon.quickslot.setSlot(0, wand);

		new ScrollOfIdentify().setKnown();

		//playtest(hero);
	}

	private static void initRogue(Hero hero) {

		if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
			hero.STR = hero.STR + 10;
		}
		if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
			hero.HT = hero.HT + 100;
			hero.HP = hero.HT;
		}
		if (Dungeon.isChallenged(Challenges.NO_HEALING)) {
			hero.attackSkill = hero.attackSkill + 100;
			hero.defenseSkill = hero.defenseSkill + 100;
		}

		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.misc1 = cloak).identify();
		hero.belongings.misc1.activate(hero);

		Dart darts = new Dart(10);
		darts.identify().collect();

		KeyRing keyring = new KeyRing();
		keyring.collect();

		Dungeon.quickslot.setSlot(0, cloak);
		if (ShatteredPixelDungeon.quickSlots() > 1)
			Dungeon.quickslot.setSlot(1, darts);

		Bomb bomb = new Bomb();
		bomb.collect();
		new ScrollOfMagicMapping().setKnown();
	}

	private static void initHuntress(Hero hero) {

		if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
			hero.STR = hero.STR + 10;
		}
		if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
			hero.HT = hero.HT + 100;
			hero.HP = hero.HT;
		}
		if (Dungeon.isChallenged(Challenges.NO_HEALING)) {
			hero.attackSkill = hero.attackSkill + 100;
			hero.defenseSkill = hero.defenseSkill + 100;
		}


		//hero.HP = (hero.HT -= 5);

		(hero.belongings.weapon = new Dagger()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();

		KeyRing keyring = new KeyRing();
		keyring.collect();

		Dungeon.quickslot.setSlot(0, boomerang);

		new PotionOfMindVision().setKnown();
	}

	public void playtest(Hero hero) {
		if (!Dungeon.playtest) {
			//Playtest
			//TomeOfMastery tome = new TomeOfMastery(); tome.collect();
			hero.HT = hero.HP = 999;
			hero.STR = hero.STR + 20;
			Dungeon.playtest = true;
			GLog.i("Playtest Activated");

			for (int i = 0; i < 199; i++) {
				Scroll scroll = new ScrollOfMagicalInfusion();
				scroll.identify().collect();
				Scroll scroll2 = new ScrollOfUpgrade();
				scroll2.identify().collect();

				Scroll scroll3 = new ScrollOfIdentify();
				scroll3.identify().collect();
				Scroll scroll4 = new ScrollOfRemoveCurse();
				scroll4.identify().collect();
				Scroll scroll5 = new ScrollOfPsionicBlast();
				scroll5.identify().collect();

				hero.earnExp(hero.maxExp() - hero.exp);
			}
		}
	}

	public String title() {
		return Messages.get(HeroClass.class, title);
	}

	public String spritesheet() {

		switch (this) {
			case WARRIOR:
				return Assets.WARRIOR;
			case MAGE:
				return Assets.MAGE;
			case ROGUE:
				return Assets.ROGUE;
			case HUNTRESS:
				return Assets.HUNTRESS;
		}

		return null;
	}

	public String[] perks() {

		switch (this) {
			case WARRIOR:
				return new String[]{
						Messages.get(HeroClass.class, "warrior_perk1"),
						Messages.get(HeroClass.class, "warrior_perk2"),
						Messages.get(HeroClass.class, "warrior_perk3"),
						Messages.get(HeroClass.class, "warrior_perk4"),
						Messages.get(HeroClass.class, "warrior_perk5"),
				};
			case MAGE:
				return new String[]{
						Messages.get(HeroClass.class, "mage_perk1"),
						Messages.get(HeroClass.class, "mage_perk2"),
						Messages.get(HeroClass.class, "mage_perk3"),
						Messages.get(HeroClass.class, "mage_perk4"),
						Messages.get(HeroClass.class, "mage_perk5"),
						Messages.get(HeroClass.class, "mage_perk6"),
				};
			case ROGUE:
				return new String[]{
						Messages.get(HeroClass.class, "rogue_perk1"),
						Messages.get(HeroClass.class, "rogue_perk2"),
						Messages.get(HeroClass.class, "rogue_perk3"),
						Messages.get(HeroClass.class, "rogue_perk4"),
						Messages.get(HeroClass.class, "rogue_perk5"),
						Messages.get(HeroClass.class, "rogue_perk6"),
				};
			case HUNTRESS:
				return new String[]{
						Messages.get(HeroClass.class, "huntress_perk2"),
						Messages.get(HeroClass.class, "huntress_perk3"),
						Messages.get(HeroClass.class, "huntress_perk4"),
						Messages.get(HeroClass.class, "huntress_perk5"),
						Messages.get(HeroClass.class, "huntress_perk6"),
						Messages.get(HeroClass.class, "huntress_perk7"),
				};
		}

		return null;
	}

	private static final String CLASS = "class";

	public void storeInBundle(Bundle bundle) {
		bundle.put(CLASS, toString());
	}

	public static HeroClass restoreInBundle(Bundle bundle) {
		String value = bundle.getString(CLASS);
		return value.length() > 0 ? valueOf(value) : ROGUE;
	}
}
