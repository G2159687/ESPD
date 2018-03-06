
package com.github.epd.sprout;

import com.github.epd.sprout.messages.Messages;

public class Challenges {
	public static final int NO_FOOD = 1;
	public static final int NO_ARMOR = 2;
	public static final int NO_HEALING = 4;
	public static final int NO_HERBALISM = 8;
	public static final int SWARM_INTELLIGENCE = 16;
	public static final int DARKNESS = 32;
	public static final int NO_SCROLLS = 64;

	public static final int MAX_VALUE = 127;

	public static final String[] NAMES = {
			Messages.get(Challenges.class, "c1"),
			Messages.get(Challenges.class, "c2"),
			Messages.get(Challenges.class, "c3"),
			Messages.get(Challenges.class, "c4"),
			Messages.get(Challenges.class, "c5"),
			Messages.get(Challenges.class, "c6"),
			Messages.get(Challenges.class, "c7")};

	public static final int[] MASKS = {NO_FOOD, NO_ARMOR, NO_HEALING,
			NO_HERBALISM, SWARM_INTELLIGENCE, DARKNESS, NO_SCROLLS};

}