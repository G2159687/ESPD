
package com.github.epd.sprout;

import com.watabou.utils.Bundle;

//move target 300 + d3*lvl

public class Statistics {

	public static int deepestFloor;
	public static int realdeepestFloor;
	public static int enemiesSlain;
	public static int archersKilled;
	public static int skeletonsKilled;
	public static int albinoPiranhasKilled;
	public static int goldThievesKilled;
	public static int shadowYogsKilled;
	public static int ankhsUsed;
	public static int ballsCooked;
	public static int waters;

	public static float duration;
	public static int prevfloormoves;
	public static int moves;

	public static boolean amuletObtained = false;
	public static boolean orbObtained = false;


	public static void reset() {

		deepestFloor = 0;
		realdeepestFloor = 0;
		enemiesSlain = 0;

		archersKilled = 0;
		skeletonsKilled = 0;
		albinoPiranhasKilled = 0;
		goldThievesKilled = 0;
		shadowYogsKilled = 0;
		ankhsUsed = 0;
		ballsCooked = 0;
		waters = 0;

		duration = 0;
		moves = 0;
		prevfloormoves = 0;

		amuletObtained = false;
		orbObtained = false;

	}

	private static final String DEEPEST = "maxDepth";
	private static final String REALDEEPEST = "maxDepthReal";
	private static final String SLAIN = "enemiesSlain";
	private static final String WATERS = "waters";

	private static final String ARCHERS = "archers";
	private static final String SKELETONS = "skeletons";
	private static final String APIRANHAS = "apiranhas";
	private static final String THIEVES = "thieves";
	private static final String SYOGS = "syogs";
	private static final String BALLS = "balls";

	private static final String ANKHS = "ankhsUsed";
	private static final String DURATION = "duration";
	private static final String PREVFLOORMOVES = "prevfloormoves";
	private static final String MOVES = "moves";
	private static final String AMULET = "amuletObtained";
	private static final String ORB = "orbObtained";

	public static void storeInBundle(Bundle bundle) {
		bundle.put(DEEPEST, deepestFloor);
		bundle.put(REALDEEPEST, realdeepestFloor);
		bundle.put(SLAIN, enemiesSlain);
		bundle.put(ARCHERS, archersKilled);
		bundle.put(SKELETONS, skeletonsKilled);
		bundle.put(APIRANHAS, albinoPiranhasKilled);
		bundle.put(THIEVES, goldThievesKilled);
		bundle.put(SYOGS, shadowYogsKilled);
		bundle.put(BALLS, ballsCooked);
		bundle.put(ANKHS, ankhsUsed);
		bundle.put(DURATION, duration);
		bundle.put(PREVFLOORMOVES, prevfloormoves);
		bundle.put(MOVES, moves);
		bundle.put(AMULET, amuletObtained);
		bundle.put(ORB, orbObtained);
		bundle.put(WATERS, waters);
	}

	public static void restoreFromBundle(Bundle bundle) {
		deepestFloor = bundle.getInt(DEEPEST);
		realdeepestFloor = bundle.getInt(REALDEEPEST);
		enemiesSlain = bundle.getInt(SLAIN);
		waters = bundle.getInt(WATERS);

		archersKilled = bundle.getInt(ARCHERS);
		skeletonsKilled = bundle.getInt(SKELETONS);
		albinoPiranhasKilled = bundle.getInt(APIRANHAS);
		goldThievesKilled = bundle.getInt(THIEVES);
		shadowYogsKilled = bundle.getInt(SYOGS);
		ballsCooked = bundle.getInt(BALLS);

		ankhsUsed = bundle.getInt(ANKHS);
		duration = bundle.getFloat(DURATION);
		prevfloormoves = bundle.getInt(PREVFLOORMOVES);
		moves = bundle.getInt(MOVES);
		amuletObtained = bundle.getBoolean(AMULET);
		orbObtained = bundle.getBoolean(ORB);

	}

}
