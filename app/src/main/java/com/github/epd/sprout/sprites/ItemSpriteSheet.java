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
package com.github.epd.sprout.sprites;

public class ItemSpriteSheet {

	// Row definers
	private static final int ROW1 = 0 * 16;
	private static final int ROW2 = 1 * 16;
	private static final int ROW3 = 2 * 16;
	private static final int ROW4 = 3 * 16;
	private static final int ROW5 = 4 * 16;
	private static final int ROW6 = 5 * 16;
	private static final int ROW7 = 6 * 16;
	private static final int ROW8 = 7 * 16;
	private static final int ROW9 = 8 * 16;
	private static final int ROW10 = 9 * 16;
	private static final int ROW11 = 10 * 16;
	private static final int ROW12 = 11 * 16;
	private static final int ROW13 = 12 * 16;
	private static final int ROW14 = 13 * 16;
	private static final int ROW15 = 14 * 16;
	private static final int ROW16 = 15 * 16;
	private static final int ROW17 = 16 * 16;

	// Row One: Items which can't be obtained
	// null warning occupies space 0, should only show up if there's a bug.
	public static final int NULLWARN = ROW1 + 0;
	public static final int DEWDROP = ROW1 + 1;
	public static final int PETAL = ROW1 + 2;
	public static final int SANDBAG = ROW1 + 3;
	public static final int REDDEWDROP = ROW8 + 8;
	public static final int YELLOWDEWDROP = ROW8 + 14;
	public static final int VIOLETDEWDROP =  ROW14 + 13;
	public static final int COIN =  ROW14 + 14;
	public static final int ARTIFACT =  ROW14 + 15;
	// Heaps (containers)
	public static final int BONES = ROW1 + 4;
	public static final int REMAINS = ROW1 + 5;
	public static final int TOMB = ROW1 + 6;
	public static final int GRAVE = ROW1 + 7;
	public static final int CHEST = ROW1 + 8;
	public static final int LOCKED_CHEST = ROW1 + 9;
	public static final int CRYSTAL_CHEST = ROW1 + 10;
	// Placeholders
	public static final int WEAPON = ROW1 + 11;
	public static final int ARMOR = ROW1 + 12;
	public static final int RING = ROW1 + 13;
	public static final int SMTH = ROW1 + 14;

	// Row Two: Miscellaneous single use items
	public static final int GOLD = ROW2 + 0;
	public static final int TORCH = ROW2 + 1;
	public static final int STYLUS = ROW2 + 2;
	public static final int ANKH = ROW2 + 3;
	// Keys
	public static final int IRON_KEY = ROW2 + 4;
	public static final int GOLDEN_KEY = ROW2 + 5;
	public static final int SKELETON_KEY = ROW2 + 6;
	// Boss Rewards
	public static final int BEACON = ROW2 + 7;
	public static final int MASTERY = ROW2 + 8;
	public static final int KIT = ROW2 + 9;
	public static final int AMULET = ROW2 + 10;
	public static final int WEIGHT = ROW2 + 11;
	public static final int BOMB = ROW2 + 12;
	public static final int DBL_BOMB = ROW2 + 13;
	public static final int HONEYPOT = ROW2 + 14;
	public static final int SHATTPOT = ROW2 + 15;
	public static final int RICEBOMB = ROW13 + 10;
	public static final int HOLY_HAND_GRENADE = ROW13 + 13;
	public static final int CLUSTER_BOMB = ROW15 + 11;
	public static final int SEEKING_BOMB = ROW15 + 12;
	public static final int DIZZY_BOMB = ROW15 + 13;
	public static final int SMART_BOMB = ROW15 + 14;
	public static final int SEEKING_CLUSTER_BOMB = ROW15 + 15;
	public static final int SHELL = ROW5 + 10;
	public static final int BONE = ROW5 + 11;
	

	// Row Three: Melee weapons
	public static final int KNUCKLEDUSTER = ROW3 + 0;
	public static final int DAGGER = ROW3 + 1;
	public static final int SHORT_SWORD = ROW3 + 2;
	public static final int QUARTERSTAFF = ROW3 + 3;
	public static final int SPEAR = ROW3 + 4;
	public static final int MACE = ROW3 + 5;
	public static final int SWORD = ROW3 + 6;
	public static final int BATTLE_AXE = ROW3 + 7;
	public static final int LONG_SWORD = ROW3 + 8;
	public static final int WAR_HAMMER = ROW3 + 9;
	public static final int GLAIVE = ROW3 + 10;
	public static final int ADAMANT_WEAPON = ROW3 + 11;
	public static final int CHAINSAW = ROW3 + 12;

	// Row Four: Missile weapons and sporks
	public static final int DART = ROW4 + 0;
	public static final int BOOMERANG = ROW4 + 1;
	public static final int INCENDIARY_DART = ROW4 + 2;
	public static final int SHURIKEN = ROW4 + 3;
	public static final int CURARE_DART = ROW4 + 4;
	public static final int JAVELIN = ROW4 + 5;
	public static final int TOMAHAWK = ROW4 + 6;
	public static final int SPORK = ROW4 + 7;
	public static final int ROYALSPORK = ROW4 + 8;
	public static final int WAVE = ROW4 + 9;
	public static final int STL_HONEYPOT = ROW4 + 11;
	public static final int STL_SHATTPOT = ROW4 + 12;
	public static final int PALANTIR = ROW4 + 13;
	public static final int SKULLWEP = ROW4 + 14;
	public static final int STONE = ROW4 + 15;
	
	
	// Row Five: Armors
	public static final int ARMOR_CLOTH = ROW5 + 0;
	public static final int ARMOR_LEATHER = ROW5 + 1;
	public static final int ARMOR_MAIL = ROW5 + 2;
	public static final int ARMOR_SCALE = ROW5 + 3;
	public static final int ARMOR_PLATE = ROW5 + 4;
	public static final int ARMOR_WARRIOR = ROW5 + 5;
	public static final int ARMOR_MAGE = ROW5 + 6;
	public static final int ARMOR_ROGUE = ROW5 + 7;
	public static final int ARMOR_HUNTRESS = ROW5 + 8;
	public static final int ARMOR_ADAMANT = ROW5 + 9;

	// Row Six: Wands
	public static final int WAND_MAGIC_MISSILE 	= ROW6 + 0;
	public static final int WAND_AVALANCHE		= ROW6+1;
	public static final int WAND_TELEPORTATION	= ROW6+2;
	public static final int WAND_FIREBOLT		= ROW6+3;
	public static final int WAND_TELEKINESIS	= ROW6+4;
	public static final int WAND_REGROWTH		= ROW6+5;
	public static final int WAND_AMOK 			= ROW6 + 6;
	public static final int WAND_POISON 		= ROW6 + 7;
	public static final int WAND_BLINK			= ROW6 + 8;
	public static final int WAND_SLOWNESS		= ROW6 + 9;
	public static final int WAND_LIGHTNING 		= ROW6 + 10;
	public static final int WAND_DISINTEGRATION	= ROW6 + 11;
	public static final int WAND_FLOCK			= ROW6 + 12;
	public static final int WAND_ADAMANT 		= ROW6 + 13;
	public static final int WAND_PRISMATIC 		= ROW6 + 14;
	public static final int WAND_FROST   		= ROW6 + 15;

	// Row Seven: Rings
	public static final int RING_GARNET = ROW7 + 0;
	public static final int RING_RUBY = ROW7 + 1;
	public static final int RING_TOPAZ = ROW7 + 2;
	public static final int RING_EMERALD = ROW7 + 3;
	public static final int RING_ONYX = ROW7 + 4;
	public static final int RING_OPAL = ROW7 + 5;
	public static final int RING_TOURMALINE = ROW7 + 6;
	public static final int RING_SAPPHIRE = ROW7 + 7;
	public static final int RING_AMETHYST = ROW7 + 8;
	public static final int RING_QUARTZ = ROW7 + 9;
	public static final int RING_AGATE = ROW7 + 10;
	public static final int RING_DIAMOND = ROW7 + 11;
	public static final int RING_ADAMANT = ROW7 + 12;

	// Row Eight: Artifacts with Static Images
	public static final int ARTIFACT_CLOAK = ROW8 + 0;
	public static final int ARTIFACT_ARMBAND = ROW8 + 1;
	public static final int ARTIFACT_CAPE = ROW8 + 2;
	public static final int ARTIFACT_TALISMAN = ROW8 + 3;
	public static final int ARTIFACT_HOURGLASS = ROW8 + 4;
	public static final int ARTIFACT_TOOLKIT = ROW8 + 5;
	public static final int ARTIFACT_SPELLBOOK = ROW8 + 6;
	public static final int BOOKOFDEAD = ROW8 + 9;
	public static final int BOOKOFLIFE = ROW8 + 10;
	public static final int BOOKOFTRANSCENDENCE = ROW8 + 11;
	public static final int DWARFHAMMER = ROW8 + 12;
	public static final int ANCIENTKEY = ROW8 + 13;
	public static final int OTILUKES_JOURNAL = ROW16 + 3;
	public static final int RING_DISINTEGRATION = ROW7 + 13;
	public static final int RING_FROST = ROW7 + 15;

	// Row Nine: Artifacts with Dynamic Images
	public static final int ARTIFACT_HORN1 = ROW9 + 0;
	public static final int ARTIFACT_HORN2 = ROW9 + 1;
	public static final int ARTIFACT_HORN3 = ROW9 + 2;
	public static final int ARTIFACT_HORN4 = ROW9 + 3;
	public static final int ARTIFACT_CHALICE1 = ROW9 + 4;
	public static final int ARTIFACT_CHALICE2 = ROW9 + 5;
	public static final int ARTIFACT_CHALICE3 = ROW9 + 6;
	public static final int ARTIFACT_SANDALS = ROW9 + 7;
	public static final int ARTIFACT_SHOES = ROW9 + 8;
	public static final int ARTIFACT_BOOTS = ROW9 + 9;
	public static final int ARTIFACT_GREAVES = ROW9 + 10;
	public static final int ARTIFACT_ROSE1 = ROW9 + 11;
	public static final int ARTIFACT_ROSE2 = ROW9 + 12;
	public static final int ARTIFACT_ROSE3 = ROW9 + 13;

	// Row Ten: Scrolls
	public static final int SCROLL_KAUNAN = ROW10 + 0;
	public static final int SCROLL_SOWILO = ROW10 + 1;
	public static final int SCROLL_LAGUZ = ROW10 + 2;
	public static final int SCROLL_YNGVI = ROW10 + 3;
	public static final int SCROLL_GYFU = ROW10 + 4;
	public static final int SCROLL_RAIDO = ROW10 + 5;
	public static final int SCROLL_ISAZ = ROW10 + 6;
	public static final int SCROLL_MANNAZ = ROW10 + 7;
	public static final int SCROLL_NAUDIZ = ROW10 + 8;
	public static final int SCROLL_BERKANAN = ROW10 + 9;
	public static final int SCROLL_NCOSRANE = ROW10 + 10;
	public static final int SCROLL_TIWAZ = ROW10 + 11;
	public static final int SCROLL_NENDIL = ROW10 + 12;
	public static final int JOURNAL_PAGE = ROW10 + 13;

	// Row Eleven: Potions
	public static final int POTION_CRIMSON = ROW11 + 0;
	public static final int POTION_AMBER = ROW11 + 1;
	public static final int POTION_GOLDEN = ROW11 + 2;
	public static final int POTION_JADE = ROW11 + 3;
	public static final int POTION_TURQUOISE = ROW11 + 4;
	public static final int POTION_AZURE = ROW11 + 5;
	public static final int POTION_INDIGO = ROW11 + 6;
	public static final int POTION_MAGENTA = ROW11 + 7;
	public static final int POTION_BISTRE = ROW11 + 8;
	public static final int POTION_CHARCOAL = ROW11 + 9;
	public static final int POTION_SILVER = ROW11 + 10;
	public static final int POTION_IVORY = ROW11 + 11;
	public static final int POTION_AQUA = ROW11 + 12;
	public static final int POTION_HONEY = ROW11 + 13;
	public static final int POTION_VIOLET = ROW11 + 14;

	// Row Twelve: Seeds
	public static final int SEED_ROTBERRY = ROW12 + 0;
	public static final int SEED_FIREBLOOM = ROW12 + 1;
	public static final int SEED_BLINDWEED = ROW12 + 2;
	public static final int SEED_SUNGRASS = ROW12 + 3;
	public static final int SEED_ICECAP = ROW12 + 4;
	public static final int SEED_STORMVINE = ROW12 + 5;
	public static final int SEED_SORROWMOSS = ROW12 + 6;
	public static final int SEED_DREAMFOIL = ROW12 + 7;
	public static final int SEED_EARTHROOT = ROW12 + 8;
	public static final int SEED_FADELEAF = ROW12 + 9;
	public static final int SEED_BLANDFRUIT = ROW12 + 10;
	public static final int SEED_DUNGEONNUT = ROW12 + 11;
	public static final int SEED_TOASTEDDUNGEONNUT = ROW12 + 12;
	public static final int SEED_GOLDENDUNGEONNUT = ROW12 + 13;
	public static final int SEED_BLACKBERRY = ROW12 + 14;
	public static final int SEED_RICE = ROW12 + 15;
	public static final int SEED_CLOUDBERRY = ROW13 + 11;
	public static final int SEED_BLUEBERRY = ROW13 + 12;
	public static final int SEED_MOONBERRY = ROW13 + 14;
	public static final int SEED_FULLMOONBERRY = ROW13 + 15;
	public static final int SEED_STARFLOWER = ROW9 + 15;
	public static final int SEED_PHASEPITCHER = ROW14 + 5;
	public static final int SEED_FLYTRAP = ROW5 + 12;
	public static final int SEED_DEWCATCHER	= ROW5+13;

	// Row Thirteen: Food
	public static final int MEAT = ROW13 + 0;
	public static final int STEAK = ROW13 + 1;
	public static final int OVERPRICED = ROW13 + 2;
	public static final int CARPACCIO = ROW13 + 3;
	public static final int BLANDFRUIT = ROW13 + 4;
	public static final int RATION = ROW13 + 5;
	public static final int PASTY = ROW13 + 6;
	public static final int MYSTERYMEAT = ROW13 + 7;
	public static final int RICEBALL = ROW13 + 8;

	// Row Fourteen: Quest Items
	public static final int SKULL = ROW14 + 0;
	public static final int DUST = ROW14 + 1;
	public static final int PICKAXE = ROW14 + 2;
	public static final int ORE = ROW14 + 3;
	public static final int TOKEN = ROW14 + 4;
	public static final int SANCHIKARAH = ROW13 + 9;
	public static final int MUSHROOM = ROW9 + 14;
	public static final int MUSHROOM_DEATHCAP = ROW14 + 9;
	public static final int MUSHROOM_LANTERN =  ROW14 + 6;
	public static final int MUSHROOM_LICHEN = ROW14 + 8;
	public static final int MUSHROOM_EARTHSTAR = ROW14 + 7;
	public static final int MUSHROOM_BLUEMILK = ROW14 + 12;
	public static final int MUSHROOM_GOLDENJELLY = ROW14 + 11;
	public static final int MUSHROOM_PIXIEPARASOL= ROW14 + 10;

	// Row Fifteen: Containers/Bags
	public static final int VIAL = ROW15 + 0;
	public static final int POUCH = ROW15 + 1;
	public static final int HOLDER = ROW15 + 2;
	public static final int BANDOLIER = ROW15 + 3;
	public static final int HOLSTER = ROW15 + 4;
	public static final int CHAIN = ROW15 + 5;
	public static final int ACTIVEMRD = ROW15 + 6;
	public static final int INACTIVEMRD = ROW15 + 7;
	public static final int ACTIVEMRD2 = ROW15 + 8;
	public static final int INACTIVEMRD2 = ROW15 + 9;
	public static final int ORBOFZOT = ROW15 + 10;
	public static final int KEYRING = ROW4 + 10;

	// Row Sixteen: Random OP Stuff
	public static final int UPGRADEGOO_YELLOW = ROW16 + 0;
	public static final int UPGRADEGOO_RED = ROW16 + 1;
	public static final int UPGRADEGOO_VIOLET = ROW16 + 2;
	public static final int EGG = ROW16 + 4;
	public static final int TRIDENT = ROW16 + 5;
	public static final int ARESSWORD = ROW16 + 6;
	public static final int JUPITERSWRAITH = ROW16 + 7;
	public static final int CROMAXE = ROW16 + 8;
	public static final int LOKISFLAIL = ROW16 + 9;
	public static final int EASTEREGG = ROW16 + 10;
	public static final int NORNGREEN = ROW16 + 11;
	public static final int NORNBLUE = ROW16 + 12;
	public static final int NORNORANGE = ROW16 + 13;
	public static final int NORNPURPLE = ROW16 + 14;
	public static final int NORNYELLOW = ROW16 + 15;
	
	// Row Seventeen: Random Lame Stuff
	public static final int TOWEL = ROW17 + 0;
	public static final int OTILUKES_SPECS = ROW17 + 1;
	public static final int PUDDING_CUP = ROW17 + 2;
	public static final int WHISTLE = ROW17 + 3;
	
//TODO: Add new placeholders for wands
}
