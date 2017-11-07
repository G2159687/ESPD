
package com.github.epd.sprout.levels;

public class SokobanLayouts2 {

	private static final int W = Terrain.WALL;
	private static final int D = Terrain.DOOR;
	private static final int M = Terrain.EMPTY;

	private static final int E = Terrain.EMPTY;

	private static final int H = Terrain.SOKOBAN_HEAP;
	private static final int F = Terrain.FLEECING_TRAP;


	public static final int[] SOKOBAN_VAULT_LEVEL = {
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W, M, M, M, W, W, W, W, W, M, M, M, M, M, M, M, M, M, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, E, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, M, M, M, M, W, W, W, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W, W,
			W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, M, M, M, M, W, W, W, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W,
			W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, M, M, M, M, M, W, W, W, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W,
			W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, M, M, M, M, M, W, W, W, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, W, W, M, M, M, M, M, M, W, W, W, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W,
			W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W,
			W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W, W,
			W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W, W, W,
			W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W, W, W, W, W, W, M, M, W, W,
			W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W, W, W, W, W, M, M, M, W, W,
			W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W, W, W, M, M, M, M, M, W, W,
			W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, M, M, M, M, M, M, W,
			W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, M, M, M, M, M, M, W,
			W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, M, M, M, M, M, M, W, W,
			W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W,
			W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, M, M, M, M, M, M, W, W, W, W, W, M, M, M, M, M, M, M, M, W, W, M, M, M, M, M, M, M, M, M, W, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, M, M, M, M, M, W, W, W, W, W, W, M, M, M, M, M, M, M, W, W, W, M, M, M, M, M, M, M, M, W, W, W, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W,
			W, M, M, M, M, W, W, W, W, W, W, W, M, M, M, M, M, M, W, W, W, W, M, M, M, M, M, M, M, M, W, W, W, W, M, M, M, M, M, M, M, M, M, W, W, W, W, W,
			W, M, M, M, M, W, W, W, W, W, W, W, M, M, M, M, M, M, W, W, W, W, M, M, M, M, M, M, M, M, W, W, W, W, M, M, M, M, M, M, M, W, W, W, W, W, W, W,
			W, M, M, M, M, W, W, W, W, W, W, W, M, M, M, M, M, M, W, W, W, W, W, M, M, M, M, M, W, W, W, W, W, W, M, M, M, M, M, M, M, W, W, W, W, W, W, W,
			W, M, M, M, M, W, W, W, W, W, W, W, W, M, M, M, W, W, W, W, W, W, W, M, M, M, M, M, W, W, W, W, W, W, M, M, M, M, M, M, M, W, W, W, W, W, W, W,
			W, W, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, W, W, W, W, W, W, W, M, M, M, M, M, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, W, W, W, W, W, W, W, W, W, M, M, M, M, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
	};

	public static final int[] DRAGON_CAVE = {
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, M, W, W, M, W, W, W, W, W, W, W, W, W, W, W, F, F, F, F, F, F, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, M, M, M, M, M, W, M, M, W, W, W, W, M, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, H, W, M, M, M, W, W, W, W, W, W, M, H, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, M, M, M, M, W, W, W, W, W, M, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, M, M, M, W, W, W, W, W, M, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, W, H, H, W, W, W, W, W, W,
			W, W, W, W, M, W, W, W, W, M, M, M, M, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, H, H, H, H, W, W, W, W, W,
			W, W, W, W, M, W, W, W, W, M, W, W, M, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, H, H, H, H, W, W, W, W, W,
			W, W, W, W, M, W, W, W, W, M, W, W, M, M, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, M, H, H, M, W, W, W, W, W,
			W, W, W, W, M, M, W, W, M, M, W, W, W, M, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, M, M, W, W, W, W, W, W, W,
			W, W, W, W, W, M, W, W, M, W, W, W, W, M, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, M, W, W, W, W, W, W, W, W,
			W, W, W, W, W, M, M, M, M, W, W, W, W, M, M, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, M, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, M, M, W, W, W, W, W, W, M, M, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, M, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, M, M, W, W, W, W, W, W,
			W, W, W, W, W, M, M, W, W, W, W, W, W, W, W, M, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, M, M, W, W, W, W, W,
			W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, M, W, W, W, W, W,
			W, W, M, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, M, W, W, W, W, W,
			W, W, H, M, W, M, W, W, W, W, W, W, W, W, W, M, M, M, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, M, M, W, W, W, W,
			W, W, M, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, M, W, W, W, W,
			W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, M, W, W, W, W,
			W, W, W, W, W, W, M, W, W, W, W, M, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, M, M, W, W, W, W,
			W, W, W, W, W, W, M, W, W, W, M, M, M, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, M, M, W, W, W, W,
			W, W, W, W, W, W, M, W, W, W, M, H, M, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, M, M, W, W, W, W,
			W, W, W, W, W, M, M, W, W, W, W, M, M, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, M, W, D, W, W, W,
			W, W, W, W, M, M, W, M, M, M, M, M, M, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, M, M, W, W, M, W, W,
			W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, M, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, M, M, W, W, W, M, W, W,
			W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, M, M, W, W, W, W, M, W, W,
			W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, H, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, M, M, M, W, W, W, W, M, M, W, W,
			W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, M, M, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, W, M, W, W, W,
			W, W, W, W, W, M, M, M, M, M, M, W, W, W, W, M, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, W, M, W, W, W,
			W, W, W, W, W, W, W, W, M, W, W, M, W, W, W, M, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, M, M, M, W, W, W,
			W, W, W, W, W, W, W, M, M, W, W, W, M, W, W, M, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, H, M, M, W, W, W, W,
			W, W, W, W, W, W, M, M, H, W, W, W, W, M, M, M, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, H, M, M, M, W, W, W,
			W, W, W, W, W, M, M, H, M, M, W, W, W, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, M, M, M, W, W, W,
			W, W, W, W, W, M, M, M, M, M, W, W, W, W, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, M, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, F, F, F, F, F, F, F, F, F, F, F, F, F, F, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
	};

}
