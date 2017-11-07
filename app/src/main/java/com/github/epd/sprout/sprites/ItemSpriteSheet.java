
package com.github.epd.sprout.sprites;

public class ItemSpriteSheet {

	private static final int WIDTH = 16;

	private static int xy(int x, int y){
		x -= 1; y -= 1;
		return x + WIDTH*y;
	}

	private static final int PLACEHOLDERS   =                           xy(1, 1);
	private static final int UNCOLLECTABLE  =                           xy(7, 1);
	private static final int KEYS           =                           xy(14, 1);

	public static final int NULLWARN                = PLACEHOLDERS + 0;
	public static final int WEAPON                  = PLACEHOLDERS + 1;
	public static final int ARMOR                   = PLACEHOLDERS + 2;
	public static final int RING                    = PLACEHOLDERS + 3;
	public static final int SMTH                    = PLACEHOLDERS + 4;
	public static final int DEWDROP                 = UNCOLLECTABLE + 0;
	public static final int YELLOWDEWDROP           = UNCOLLECTABLE + 1;
	public static final int REDDEWDROP              = UNCOLLECTABLE + 2;
	public static final int VIOLETDEWDROP           = UNCOLLECTABLE + 3;
	public static final int PETAL                   = UNCOLLECTABLE + 4;
	public static final int SANDBAG                 = UNCOLLECTABLE + 5;
	public static final int GOLD                    = UNCOLLECTABLE + 6;
	public static final int IRON_KEY                = KEYS + 0;
	public static final int GOLDEN_KEY              = KEYS + 1;
	public static final int SKELETON_KEY            = KEYS + 2;

	private static final int HEAPS          =                           xy(1, 2);

	public static final int BONES                   = HEAPS + 0;
	public static final int REMAINS                 = HEAPS + 1;
	public static final int TOMB                    = HEAPS + 2;
	public static final int GRAVE                   = HEAPS + 3;
	public static final int CHEST                   = HEAPS + 4;
	public static final int LOCKED_CHEST            = HEAPS + 5;
	public static final int CRYSTAL_CHEST           = HEAPS + 6;

	private static final int WEAPONS        =                           xy(1, 3);

	public static final int KNUCKLEDUSTER           = WEAPONS + 0;
	public static final int DAGGER                  = WEAPONS + 1;
	public static final int SHORT_SWORD             = WEAPONS + 2;
	public static final int QUARTERSTAFF            = WEAPONS + 3;
	public static final int SPEAR                   = WEAPONS + 4;
	public static final int MACE                    = WEAPONS + 5;
	public static final int SWORD                   = WEAPONS + 6;
	public static final int BATTLE_AXE              = WEAPONS + 7;
	public static final int LONG_SWORD              = WEAPONS + 8;
	public static final int WAR_HAMMER              = WEAPONS + 9;
	public static final int GLAIVE                  = WEAPONS + 10;

	private static final int MISSILES       =                           xy(1, 4);

	public static final int DART                    = MISSILES + 0;
	public static final int BOOMERANG               = MISSILES + 1;
	public static final int INCENDIARY_DART         = MISSILES + 2;
	public static final int SHURIKEN                = MISSILES + 3;
	public static final int CURARE_DART             = MISSILES + 4;
	public static final int JAVELIN                 = MISSILES + 5;
	public static final int TOMAHAWK                = MISSILES + 6;
	public static final int WAVE                    = MISSILES + 7;
	public static final int SKULLWEP                = MISSILES + 8;
	public static final int RICEBALL                = MISSILES + 9;

	private static final int ARMORS         =                           xy(1, 5);

	public static final int ARMOR_CLOTH             = ARMORS + 0;
	public static final int ARMOR_LEATHER           = ARMORS + 1;
	public static final int ARMOR_MAIL              = ARMORS + 2;
	public static final int ARMOR_SCALE             = ARMORS + 3;
	public static final int ARMOR_PLATE             = ARMORS + 4;
	public static final int ARMOR_WARRIOR           = ARMORS + 5;
	public static final int ARMOR_MAGE              = ARMORS + 6;
	public static final int ARMOR_ROGUE             = ARMORS + 7;
	public static final int ARMOR_HUNTRESS          = ARMORS + 8;

	private static final int RINGS          =                           xy(1, 7);

	public static final int RING_GARNET             = RINGS + 0;
	public static final int RING_RUBY               = RINGS + 1;
	public static final int RING_TOPAZ              = RINGS + 2;
	public static final int RING_EMERALD            = RINGS + 3;
	public static final int RING_ONYX               = RINGS + 4;
	public static final int RING_OPAL               = RINGS + 5;
	public static final int RING_TOURMALINE         = RINGS + 6;
	public static final int RING_SAPPHIRE           = RINGS + 7;
	public static final int RING_AMETHYST           = RINGS + 8;
	public static final int RING_QUARTZ             = RINGS + 9;
	public static final int RING_AGATE              = RINGS + 10;
	public static final int RING_DIAMOND            = RINGS + 11;

	private static final int WANDS          =                           xy(1, 8);

	public static final int WAND_MAGIC_MISSILE      = WANDS + 0;
	public static final int WAND_AVALANCHE          = WANDS + 1;
	public static final int WAND_TELEPORTATION      = WANDS + 2;
	public static final int WAND_FIREBOLT           = WANDS + 3;
	public static final int WAND_TELEKINESIS        = WANDS + 4;
	public static final int WAND_REGROWTH           = WANDS + 5;
	public static final int WAND_AMOK               = WANDS + 6;
	public static final int WAND_VENOM              = WANDS + 7;
	public static final int WAND_BLINK              = WANDS + 8;
	public static final int WAND_SLOWNESS           = WANDS + 9;
	public static final int WAND_LIGHTNING          = WANDS + 10;
	public static final int WAND_DISINTEGRATION     = WANDS + 11;
	public static final int WAND_FLOCK              = WANDS + 12;
	public static final int WAND_PRISMATIC          = WANDS + 13;
	public static final int WAND_FROST              = WANDS + 14;
	public static final int WAND_BLASTWAVE          = WANDS + 15;

	private static final int WANDS2         =                           xy(1, 9);

	public static final int WAND_POISON             = WANDS2 + 0;
	public static final int WAND_TRANSFUTION        = WANDS2 + 1;

	private static final int ARTIFACTS_S    =                           xy(1, 10);

	public static final int ARTIFACT_CLOAK          = ARTIFACTS_S + 0;
	public static final int ARTIFACT_ARMBAND        = ARTIFACTS_S + 1;
	public static final int ARTIFACT_CAPE           = ARTIFACTS_S + 2;
	public static final int ARTIFACT_TALISMAN       = ARTIFACTS_S + 3;
	public static final int ARTIFACT_HOURGLASS      = ARTIFACTS_S + 4;
	public static final int ARTIFACT_TOOLKIT        = ARTIFACTS_S + 5;
	public static final int ARTIFACT_SPELLBOOK      = ARTIFACTS_S + 6;
	public static final int ARTIFACT_BEACON         = ARTIFACTS_S + 7;
	public static final int TOWEL                   = ARTIFACTS_S + 8;
	public static final int OTILUKES_SPECS          = ARTIFACTS_S + 9;
	public static final int AUTOPOTION              = ARTIFACTS_S + 10;
	public static final int WHISTLE                 = ARTIFACTS_S + 11;
	public static final int RING_DISINTEGRATION     = ARTIFACTS_S + 12;
	public static final int RING_FROST              = ARTIFACTS_S + 13;

	private static final int WEAPONS_SP     =                           xy(1, 11);

	public static final int TRIDENT                 = WEAPONS_SP + 0;
	public static final int ARESSWORD               = WEAPONS_SP + 1;
	public static final int JUPITERSWRAITH          = WEAPONS_SP + 2;
	public static final int CROMAXE                 = WEAPONS_SP + 3;
	public static final int LOKISFLAIL              = WEAPONS_SP + 4;
	public static final int CHAINSAW                = WEAPONS_SP + 5;
	public static final int SPORK                   = WEAPONS_SP + 6;
	public static final int ROYALSPORK              = WEAPONS_SP + 7;


	private static final int EXPLOSIVES     =                           xy(1, 12);

	public static final int ACTIVEMRD               = EXPLOSIVES + 0;
	public static final int INACTIVEMRD             = EXPLOSIVES + 1;
	public static final int ACTIVEMRD2              = EXPLOSIVES + 2;
	public static final int INACTIVEMRD2            = EXPLOSIVES + 3;
	public static final int ORBOFZOT                = EXPLOSIVES + 4;
	public static final int CLUSTER_BOMB            = EXPLOSIVES + 5;
	public static final int SEEKING_BOMB            = EXPLOSIVES + 6;
	public static final int DIZZY_BOMB              = EXPLOSIVES + 7;
	public static final int SMART_BOMB              = EXPLOSIVES + 8;
	public static final int SEEKING_CLUSTER_BOMB    = EXPLOSIVES + 9;
	public static final int HOLY_HAND_GRENADE       = EXPLOSIVES + 10;
	public static final int RICEBOMB                = EXPLOSIVES + 11;
	public static final int BOMB                    = EXPLOSIVES + 12;
	public static final int DBL_BOMB                = EXPLOSIVES + 13;

	private static final int ONETIME        =                           xy(1, 13);

	public static final int MASTERY                 = ONETIME + 0;
	public static final int KIT                     = ONETIME + 1;
	public static final int AMULET                  = ONETIME + 2;
	public static final int WEIGHT                  = ONETIME + 3;
	public static final int HONEYPOT                = ONETIME + 4;
	public static final int SHATTPOT                = ONETIME + 5;
	public static final int STL_HONEYPOT            = ONETIME + 6;
	public static final int STL_SHATTPOT            = ONETIME + 7;
	public static final int UPGRADEGOO_YELLOW       = ONETIME + 8;
	public static final int UPGRADEGOO_RED          = ONETIME + 9;
	public static final int UPGRADEGOO_VIOLET       = ONETIME + 10;
	public static final int DWARFHAMMER             = ONETIME + 11;
	public static final int TORCH                   = ONETIME + 12;
	public static final int STYLUS                  = ONETIME + 13;
	public static final int ANKH                    = ONETIME + 14;

	private static final int ONETIME2       =                           xy(1, 14);

	public static final int EGG                     = ONETIME2 + 0;
	public static final int EASTEREGG               = ONETIME2 + 1;
	public static final int STONE                   = ONETIME2 + 2;
	public static final int NORNGREEN               = ONETIME2 + 3;
	public static final int NORNBLUE                = ONETIME2 + 4;
	public static final int NORNORANGE              = ONETIME2 + 5;
	public static final int NORNPURPLE              = ONETIME2 + 6;
	public static final int NORNYELLOW              = ONETIME2 + 7;
	public static final int BEACON                  = ONETIME2 + 8;
	public static final int ADAMANT_WEAPON          = ONETIME2 + 9;
	public static final int ARMOR_ADAMANT           = ONETIME2 + 10;
	public static final int RING_ADAMANT            = ONETIME2 + 11;
	public static final int WAND_ADAMANT            = ONETIME2 + 12;

	private static final int CONTAINERS     =                           xy(1, 15);

	public static final int VIAL                    = CONTAINERS + 0;
	public static final int POUCH                   = CONTAINERS + 1;
	public static final int HOLDER                  = CONTAINERS + 2;
	public static final int BANDOLIER               = CONTAINERS + 3;
	public static final int HOLSTER                 = CONTAINERS + 4;
	public static final int CHAIN                   = CONTAINERS + 5;
	public static final int KEYRING                 = CONTAINERS + 6;

	private static final int QUEST          =                           xy(1, 16);

	public static final int DUST                    = QUEST + 0;
	public static final int PICKAXE                 = QUEST + 1;
	public static final int ORE                     = QUEST + 2;
	public static final int TOKEN                   = QUEST + 3;
	public static final int ANVIL                   = QUEST + 4;

	private static final int FOOD           =                           xy(1, 18);  //16 slots

	public static final int MEAT                    = FOOD + 0;
	public static final int STEAK                   = FOOD + 1;
	public static final int OVERPRICED              = FOOD + 2;
	public static final int CARPACCIO               = FOOD + 3;
	public static final int BLANDFRUIT              = FOOD + 4;
	public static final int RATION                  = FOOD + 5;
	public static final int PASTY                   = FOOD + 6;
	public static final int MYSTERYMEAT             = FOOD + 7;

	private static final int SCROLLS        =                           xy(1, 20);  //16 slots

	public static final int SCROLL_KAUNAN           = SCROLLS + 0;
	public static final int SCROLL_SOWILO           = SCROLLS + 1;
	public static final int SCROLL_LAGUZ            = SCROLLS + 2;
	public static final int SCROLL_YNGVI            = SCROLLS + 3;
	public static final int SCROLL_GYFU             = SCROLLS + 4;
	public static final int SCROLL_RAIDO            = SCROLLS + 5;
	public static final int SCROLL_ISAZ             = SCROLLS + 6;
	public static final int SCROLL_MANNAZ           = SCROLLS + 7;
	public static final int SCROLL_NAUDIZ           = SCROLLS + 8;
	public static final int SCROLL_BERKANAN         = SCROLLS + 9;
	public static final int SCROLL_NCOSRANE         = SCROLLS + 10;
	public static final int SCROLL_TIWAZ            = SCROLLS + 11;
	public static final int SCROLL_NENDIL           = SCROLLS + 12;

	private static final int POTIONS        =                           xy(1, 21);  //16 slots

	public static final int POTION_CRIMSON          = POTIONS + 0;
	public static final int POTION_AMBER            = POTIONS + 1;
	public static final int POTION_GOLDEN           = POTIONS + 2;
	public static final int POTION_JADE             = POTIONS + 3;
	public static final int POTION_TURQUOISE        = POTIONS + 4;
	public static final int POTION_AZURE            = POTIONS + 5;
	public static final int POTION_INDIGO           = POTIONS + 6;
	public static final int POTION_MAGENTA          = POTIONS + 7;
	public static final int POTION_BISTRE           = POTIONS + 8;
	public static final int POTION_CHARCOAL         = POTIONS + 9;
	public static final int POTION_SILVER           = POTIONS + 10;
	public static final int POTION_IVORY            = POTIONS + 11;
	public static final int POTION_AQUA             = POTIONS + 12;
	public static final int POTION_HONEY            = POTIONS + 13;
	public static final int POTION_VIOLET           = POTIONS + 14;

	// Row Twelve: Seeds
	private static final int SEEDS        =                             xy(1, 23);  //16 slots

	public static final int SEED_ROTBERRY           = SEEDS + 0;
	public static final int SEED_FIREBLOOM          = SEEDS + 1;
	public static final int SEED_BLINDWEED          = SEEDS + 2;
	public static final int SEED_SUNGRASS           = SEEDS + 3;
	public static final int SEED_ICECAP             = SEEDS + 4;
	public static final int SEED_STORMVINE          = SEEDS + 5;
	public static final int SEED_SORROWMOSS         = SEEDS + 6;
	public static final int SEED_DREAMFOIL          = SEEDS + 7;
	public static final int SEED_EARTHROOT          = SEEDS + 8;
	public static final int SEED_FADELEAF           = SEEDS + 9;
	public static final int SEED_BLANDFRUIT         = SEEDS + 10;

	private static final int SEEDS2        =                             xy(1, 24);  //16 slots

	public static final int SEED_FLYTRAP            = SEEDS2 + 0;
	public static final int SEED_DEWCATCHER         = SEEDS2 + 1;
	public static final int SEED_STARFLOWER         = SEEDS2 + 2;
	public static final int SEED_PHASEPITCHER       = SEEDS2 + 3;
	public static final int SEED_DUNGEONNUT         = SEEDS2 + 4;
	public static final int SEED_TOASTEDDUNGEONNUT  = SEEDS2 + 5;
	public static final int SEED_GOLDENDUNGEONNUT   = SEEDS2 + 6;
	public static final int SEED_RICE               = SEEDS2 + 7;

	private static final int BERRY        =                              xy(1, 25);  //16 slots

	public static final int SEED_CLOUDBERRY         = BERRY + 0;
	public static final int SEED_BLUEBERRY          = BERRY + 1;
	public static final int SEED_MOONBERRY          = BERRY + 2;
	public static final int SEED_FULLMOONBERRY      = BERRY + 3;
	public static final int SEED_BLACKBERRY         = BERRY + 4;

	private static final int MUSHROOMS        =                      xy(1, 26);  //16 slots

	public static final int MUSHROOM                = MUSHROOMS + 0;
	public static final int MUSHROOM_LANTERN        = MUSHROOMS + 1;
	public static final int MUSHROOM_EARTHSTAR      = MUSHROOMS + 2;
	public static final int MUSHROOM_LICHEN         = MUSHROOMS + 3;
	public static final int MUSHROOM_DEATHCAP       = MUSHROOMS + 4;
	public static final int MUSHROOM_BLUEMILK       = MUSHROOMS + 5;
	public static final int MUSHROOM_GOLDENJELLY    = MUSHROOMS + 6;
	public static final int MUSHROOM_PIXIEPARASOL   = MUSHROOMS + 7;

	private static final int TELEPORTER =                               xy(1, 29);

	public static final int BOOKOFDEAD              = TELEPORTER + 0;
	public static final int BOOKOFLIFE              = TELEPORTER + 1;
	public static final int BOOKOFTRANSCENDENCE     = TELEPORTER + 2;
	public static final int PALANTIR                = TELEPORTER + 3;
	public static final int COIN                    = TELEPORTER + 4;
	public static final int BONE                    = TELEPORTER + 5;
	public static final int SHELL                   = TELEPORTER + 6;
	public static final int ANCIENTKEY              = TELEPORTER + 7;
	public static final int OTILUKES_JOURNAL        = TELEPORTER + 8;
	public static final int SANCHIKARAH             = TELEPORTER + 9;
	public static final int PAGE_0                  = TELEPORTER + 10;
	public static final int PAGE_1                  = TELEPORTER + 11;
	public static final int PAGE_2                  = TELEPORTER + 12;
	public static final int PAGE_3                  = TELEPORTER + 13;
	public static final int PAGE_4                  = TELEPORTER + 14;
	public static final int PAGE_5                  = TELEPORTER + 15;
	public static final int PAGE_6                  = TELEPORTER + 16;
	public static final int PAGE_7                  = TELEPORTER + 17;
	public static final int PAGE_8                  = TELEPORTER + 18;
	public static final int PAGE_9                  = TELEPORTER + 19;
	public static final int PAGE_10                 = TELEPORTER + 20;
	public static final int PAGE_11                 = TELEPORTER + 21;


	private static final int ARTIFACT_D        =                        xy(1, 31);  //16 slots

	public static final int ARTIFACT_HORN1          = ARTIFACT_D + 0;
	public static final int ARTIFACT_HORN2          = ARTIFACT_D + 1;
	public static final int ARTIFACT_HORN3          = ARTIFACT_D + 2;
	public static final int ARTIFACT_HORN4          = ARTIFACT_D + 3;
	public static final int ARTIFACT_CHALICE1       = ARTIFACT_D + 4;
	public static final int ARTIFACT_CHALICE2       = ARTIFACT_D + 5;
	public static final int ARTIFACT_CHALICE3       = ARTIFACT_D + 6;
	public static final int ARTIFACT_SANDALS        = ARTIFACT_D + 7;
	public static final int ARTIFACT_SHOES          = ARTIFACT_D + 8;
	public static final int ARTIFACT_BOOTS          = ARTIFACT_D + 9;
	public static final int ARTIFACT_GREAVES        = ARTIFACT_D + 10;
	public static final int ARTIFACT_ROSE1          = ARTIFACT_D + 11;
	public static final int ARTIFACT_ROSE2          = ARTIFACT_D + 12;
	public static final int ARTIFACT_ROSE3          = ARTIFACT_D + 13;

	private static final int UNKNOWN    =                                xy(1, 32);

	public static final int PUDDING_CUP             = UNKNOWN + 0;
	public static final int SKULL                   = UNKNOWN + 4;
	
}
