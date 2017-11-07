
package com.github.epd.sprout.levels;

public class Layouts {

	//32X32
	private static final int W = Terrain.WALL;
	private static final int Z = Terrain.HIGH_GRASS;
	private static final int D = Terrain.DOOR;
	private static final int L = Terrain.LOCKED_DOOR;
	private static final int P = Terrain.EMPTY;

	private static final int E = Terrain.EMPTY;

	private static final int A = Terrain.SOKOBAN_SHEEP;
	private static final int X = Terrain.CORNER_SOKOBAN_SHEEP;
	private static final int U = Terrain.STATUE;
	private static final int Q = Terrain.LOCKED_EXIT;
	private static final int T = Terrain.INACTIVE_TRAP;
	private static final int N = Terrain.ENTRANCE;

	public static final int[] SAFE_ROOM_DEFAULT = {

			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, P, P, P, P, P, W, W, W, W, W,
			W, Z, Z, Z, W, P, Z, Z, Z, P, W, Z, Z, Z, W,
			W, Z, Z, Z, D, P, Z, E, Z, P, D, Z, Z, Z, W,
			W, Z, Z, Z, W, P, Z, Z, Z, P, W, Z, Z, Z, W,
			W, W, W, W, W, P, P, P, P, P, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W

	};

	public static final int[] PRISON_BOSS = {

			W, W, W, W, W, W, W, W,
			W, W, W, Q, W, W, W, W,
			W, E, E, E, E, E, E, W,
			W, E, T, T, T, T, E, W,
			W, E, T, T, T, T, E, W,
			W, E, T, T, T, T, E, W,
			W, E, T, T, T, T, E, W,
			W, E, E, E, E, E, E, W,
			W, W, W, L, W, W, W, W,
			W, E, E, E, E, E, E, W,
			W, E, E, E, E, E, E, W,
			W, E, E, N, E, E, E, W,
			W, W, W, W, W, W, W, W

	};

	public static final int[] MINE_BOSS = {
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, P, P, P, P, P, P, P, P, X, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, X, P, P, P, P, P, P, P, X, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, A, A, A, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, A, A, A, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, A, A, A, A, A, P, P, P, P, P, P, P, P, P, P, P, P, P, P, A, A, A, A, A, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, X, P, P, A, A, P, A, A, P, P, X, P, P, P, P, P, P, P, P, P, P, P, A, A, P, A, A, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, A, A, A, A, A, P, P, P, P, P, P, P, P, P, P, P, P, P, P, A, A, A, A, A, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, A, A, A, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, A, A, A, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, X, P, P, P, P, P, P, P, X, P, P, P, P, U, U, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, X, P, P, P, P, P, P, P, P, U, U, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, U, U, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, U, U, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, U, P, P, U, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W,
			W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W,
			W, W, W, W, W, W, W, P, P, P, P, P, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, P, P, P, P, P, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, P, P, P, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, P, P, P, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, P, P, P, P, P, P, P, P, P, P, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, W, P, W, P, W, P, P, W, P, W, P, W, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, W, P, W, P, W, P, P, W, P, W, P, W, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, W, P, W, P, P, P, P, P, P, W, P, W, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, W, P, P, P, P, P, P, P, P, P, P, W, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, W, P, W, P, W, P, P, W, P, W, P, W, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, P, W, P, W, P, W, P, P, W, P, W, P, W, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, E, P, P, W, P, W, P, P, W, P, W, P, P, P, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
	};

}
