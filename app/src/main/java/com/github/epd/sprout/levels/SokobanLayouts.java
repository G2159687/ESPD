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
package com.github.epd.sprout.levels;

public class SokobanLayouts {

	//32X32
	private static final int W = Terrain.WALL;
	private static final int T = Terrain.SHRUB;
	private static final int Z = Terrain.HIGH_GRASS;
	private static final int D = Terrain.DOOR;
	private static final int L = Terrain.LOCKED_DOOR;
	private static final int M = Terrain.EMPTY; //for readability

	//private static final int T = Terrain.INACTIVE_TRAP;

	private static final int E = Terrain.EMPTY;
	//private static final int X = Terrain.EXIT;

	//private static final int M = Terrain.WALL_DECO;
	//private static final int P = Terrain.PEDESTAL;

	private static final int A = Terrain.SOKOBAN_SHEEP;
	private static final int X = Terrain.CORNER_SOKOBAN_SHEEP;
	private static final int C = Terrain.SWITCH_SOKOBAN_SHEEP;
	private static final int B = Terrain.BLACK_SOKOBAN_SHEEP;
	private static final int H = Terrain.SOKOBAN_HEAP;
	private static final int I = Terrain.SOKOBAN_ITEM_REVEAL;
	private static final int F = Terrain.FLEECING_TRAP;
	private static final int U = Terrain.STATUE;
	private static final int G = Terrain.CHANGE_SHEEP_TRAP;
	private static final int S = Terrain.SECRET_DOOR;
	private static final int R = Terrain.PORT_WELL;
	private static final int V = Terrain.SOKOBAN_PORT_SWITCH;

	public static final int[] SOKOBAN_INTRO_LEVEL = {
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, E, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, M, U, A, U, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, S, W, W, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, M, U, F, U, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, C, M, W, M, C, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, M, U, U, M, D, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, G, M, M, M, G, M, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, M, M, W, W, W, W, W, W, U, U, A, U, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, M, M, S, M, M, M, M, D, F, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, H, W, W, U, U, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, U, F, U, M, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, U, F, A, M, W, M, M, M, M, W, M, M, M, M, M, W, W, W, W, W, W, W, W, W, W, M, U, F, U, M, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, U, U, X, U, W, M, M, M, M, W, M, C, M, M, U, W, W, W, W, W, W, W, W, W, W, M, U, F, U, M, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, H, U, U, F, D, M, M, C, M, W, M, G, M, M, F, D, M, M, M, M, W, W, W, W, W, G, U, F, U, G, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, S, W, W, W, W, M, M, M, M, W, M, M, M, M, U, W, C, G, G, M, W, W, W, W, W, C, U, H, U, C, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, F, W, W, W, W, M, M, M, M, D, M, M, M, M, M, W, M, M, M, M, W, W, W, W, W, W, W, S, W, W, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, U, W, W, W, W, W, W, W, M, W, W, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, S, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, U, F, W, W, W, W, W, W, W, M, W, W, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, M, B, M, M, M, D, F, M, M, M, D, M, M, M, M, W, W, W, W, W, W, D, W, W, W, W, W, W, W, M, W, W, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, M, M, M, M, M, W, U, U, U, M, W, M, M, M, M, W, W, W, W, W, W, M, W, W, W, W, W, W, M, M, M, W, W, W, W, M, W, W, W, W, W, W, W,
			W, W, W, W, W, M, M, R, M, M, W, B, B, M, M, W, M, G, M, M, W, M, M, M, M, W, M, H, W, W, W, W, W, M, M, B, W, M, F, M, A, M, W, W, W, W, W, W,
			W, W, W, W, W, M, M, M, M, M, W, U, U, U, M, W, M, G, M, M, W, M, M, M, M, D, M, W, W, W, W, W, W, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, M, M, M, M, M, W, H, F, M, M, W, M, G, M, M, W, M, I, A, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, D, W, W, W, W, W, W, W, W, W, W, M, C, M, M, W, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, M, M, M, M, M, W, W, W, W, W, W, M, M, M, M, L, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, M, M, X, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, M, M, I, V, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, M, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W

	};

	public static final int[] SOKOBAN_CASTLE = {
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, I, T, I, T, I, T, I, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W,
			W, T, T, W, S, W, W, W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W, W, W, W, W, T, T, W,
			W, T, T, W, R, U, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, F, H, W, T, T, W,
			W, T, T, W, U, U, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, F, F, W, T, T, W,
			W, T, T, W, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, W, T, T, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, H, W, H, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, T, T, W,
			W, T, T, W, W, M, W, W, W, W, G, M, W, F, G, C, F, M, M, F, M, B, W, F, W, F, W, F, F, H, F, H, F, F, F, F, W, M, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, H, M, S, F, F, X, F, M, C, U, M, M, W, M, W, M, W, F, F, F, F, F, F, F, F, F, S, M, X, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, M, M, W, F, F, X, F, G, C, U, M, M, W, M, W, M, W, F, F, F, F, F, F, F, F, F, W, M, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, F, F, W, H, F, M, F, G, H, U, M, M, W, M, W, M, W, M, X, X, X, X, X, M, M, M, W, F, F, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, M, B, M, W, W, W, W, D, W, W, W, W, S, W, W, S, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, W, W, M, F, F, F, M, W, W, M, W, M, W, W, H, F, U, F, H, W, W, W, W, W, M, M, M, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, W, W, U, F, U, F, U, W, W, M, W, M, W, W, U, U, M, U, U, W, W, W, W, W, U, M, H, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, W, W, M, X, G, X, M, W, W, M, W, M, W, W, M, G, M, G, M, W, W, W, W, W, S, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, W, W, M, C, G, C, M, W, W, M, W, M, W, W, C, G, M, G, C, W, W, H, H, M, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, D, W, W, W, W, M, W, M, W, W, W, W, S, W, W, W, W, M, M, X, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, U, U, U, C, L, M, W, W, W, F, W, W, W, W, M, W, M, W, W, W, W, F, W, W, W, W, U, M, U, U, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, H, F, G, M, W, M, W, W, W, F, W, W, W, W, M, W, M, W, W, W, W, F, W, W, W, W, M, M, M, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, U, U, U, B, W, M, W, W, W, G, G, I, W, W, M, W, M, W, W, M, G, F, W, W, W, W, M, M, M, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, M, W, W, W, X, M, C, W, W, G, W, G, W, W, C, A, G, W, W, W, W, M, M, M, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, M, U, U, F, D, M, W, W, W, M, M, C, M, G, X, C, X, G, M, M, C, M, W, W, W, W, M, M, M, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, I, C, M, F, W, M, W, W, W, W, W, W, W, W, C, E, C, W, W, W, W, W, W, M, W, W, W, W, M, W, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, G, X, M, M, W, X, M, M, M, M, M, M, M, G, X, C, X, G, M, M, M, M, M, X, M, W, W, W, L, W, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, M, U, U, M, W, M, W, W, W, W, W, W, W, W, G, W, G, W, W, W, W, W, W, W, M, W, M, M, M, M, M, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, M, W, F, F, F, F, F, U, W, M, W, M, W, W, W, W, W, W, W, M, W, U, U, M, U, U, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, U, U, M, M, W, M, W, F, F, F, F, F, U, W, M, W, M, W, W, W, W, W, W, W, M, W, M, G, X, G, I, S, S, M, W, T, T, W, W,
			W, M, T, W, W, M, W, R, L, F, F, X, M, W, M, W, F, F, H, F, F, U, W, M, W, M, W, W, W, W, W, M, M, M, W, U, F, M, F, U, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, H, U, G, G, D, M, W, F, F, F, F, F, U, W, M, W, M, W, W, W, W, W, M, M, M, M, M, M, X, M, M, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, F, U, M, C, W, M, W, F, F, F, F, F, U, W, M, W, M, W, W, W, W, W, M, M, W, W, U, M, X, M, U, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, S, W, W, W, W, M, W, M, X, X, M, M, M, W, M, W, M, W, W, W, W, W, M, W, W, W, M, M, M, M, M, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, A, W, W, W, W, D, W, W, W, W, L, W, W, W, M, W, M, W, W, H, S, I, M, M, W, W, W, W, W, W, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, G, W, W, W, W, M, M, M, M, M, M, M, G, W, M, W, M, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, M, M, M, W, W, W, M, U, M, U, M, F, X, U, W, M, W, M, W, W, W, W, U, M, U, S, M, W, W, W, W, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, U, F, U, W, W, W, M, M, M, M, M, M, X, G, W, M, W, M, W, W, W, W, M, X, M, W, M, M, M, M, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, I, H, C, W, W, W, M, U, M, U, M, U, M, U, W, M, W, M, W, W, W, W, M, M, M, W, W, W, M, R, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, M, U, M, U, M, U, M, U, W, M, B, M, W, W, W, W, X, X, X, W, W, W, M, M, M, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, M, M, M, W, W, W, M, M, X, M, M, M, M, B, W, M, W, M, W, W, W, W, M, M, M, W, W, W, W, W, W, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, M, M, M, W, W, W, M, U, M, U, M, U, F, U, W, M, W, M, W, W, W, W, M, X, M, W, W, W, W, W, W, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, X, X, X, W, W, W, M, U, M, U, F, U, F, U, W, M, W, M, W, W, W, W, M, M, M, W, W, W, W, W, W, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, I, H, I, W, W, W, M, M, C, G, F, F, H, I, W, F, W, F, W, W, W, W, M, H, V, W, W, W, W, W, W, W, W, W, M, W, T, T, W, W,
			W, T, T, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, H, W, H, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, T, W, W,
			W, T, T, W, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, W, T, W, W,
			W, T, M, W, F, F, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, F, F, W, T, W, W,
			W, T, T, W, H, F, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, F, H, S, T, W, W,
			W, T, T, W, W, W, W, W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W, W, W, W, W, T, W, W,
			W, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W

	};

	public static final int[] SOKOBAN_TELEPORT_LEVEL = {
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, R, M, M, W, W, W, W, W, W, W, W, H, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, F, H, H, F, M, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, M, X, H, W, W, W, W, W, W, W, W, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, F, F, F, F, M, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, B, F, M, W, W, W, W, W, W, W, W, G, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, X, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, C, R, S, M, L, B, W, F, R, W, W, W, W, W, W, W, M, R, W, W, W, W, W, W, W, M, M, M, W, W, W, W, W,
			W, W, I, F, R, W, W, W, W, F, H, F, W, W, W, W, W, W, W, W, W, W, F, F, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, A, X, M, W, W, W, W, W,
			W, W, M, F, F, W, W, W, W, F, F, F, W, W, W, W, W, W, W, W, W, W, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, F, F, F, M, W, W, W, W, W,
			W, W, M, M, M, W, W, W, W, M, X, M, W, W, W, W, W, W, W, W, W, W, M, M, M, R, W, W, W, W, W, W, W, W, W, W, W, W, W, F, F, F, R, W, W, W, W, W,
			W, W, M, M, H, W, W, W, W, M, R, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, H, F, W, W, W, W, W, W, W,
			W, W, R, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, C, G, G, G, G, F, F, F, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, M, M, X, A, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, V, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, M, X, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, F, W, W, M, M, M, M, W, W, M, M, M, I, W, W, W, W,
			W, W, W, W, W, W, W, M, E, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, C, W, M, V, X, M, W, W, M, X, V, M, W, W, W, W,
			W, W, M, R, W, W, W, M, R, X, M, W, W, W, W, W, M, R, M, W, W, W, W, W, W, W, W, W, W, W, W, G, W, W, M, M, M, R, W, W, R, M, M, M, W, W, W, W,
			W, W, M, X, W, W, W, M, M, I, M, W, W, W, W, W, M, X, M, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, C, G, W, W, W, M, M, M, M, W, W, W, W, W, F, F, F, W, W, W, W, H, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, C, G, W, W, W, W, W, W, W, W, W, W, W, W, M, H, M, W, W, W, W, S, W, W, W, W, W, W, W, M, W, W, M, M, M, R, W, W, R, M, M, M, W, W, W, W,
			W, W, C, G, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, R, M, U, F, W, W, W, W, W, W, M, W, W, M, V, X, M, W, W, M, X, V, M, W, W, W, W,
			W, W, U, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, U, A, R, W, W, W, W, W, M, W, W, I, M, M, M, W, W, M, M, M, H, W, W, W, W,
			W, W, U, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, U, F, W, W, W, W, W, W, W, W, W, M, C, R, W, W, W, W, W, M, M, M, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, U, M, M, M, M, M, W, M, M, W, W, M, X, M, W, W, W, W, W, M, M, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, U, M, U, M, M, M, L, M, H, W, W, G, G, G, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, U, I, U, I, M, M, W, M, M, W, W, M, M, I, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, H, M, M, M, M, F, F, F, M, M, M, M, M, M, M, M, M, M, M, R, H, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, I, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, M, U, M, M, M, U, M, U, M, M, U, M, U, M, M, M, M, M, M, U, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, U, M, U, M, M, U, M, U, M, M, M, M, M, M, M, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, M, U, M, U, U, U, M, U, F, F, U, M, U, U, U, M, M, M, M, U, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, M, M, M, F, F, F, F, F, F, F, F, F, F, F, F, M, M, M, M, M, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, M, U, G, U, F, M, M, M, M, M, M, M, M, F, U, G, M, M, M, U, I, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, M, F, F, F, M, W, W, W, W, W, W, W, W, M, M, G, F, M, M, X, M, X, X, M, X, M, M, F, G, M, M, M, M, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, W, W, W, W, W, W, W, W, M, U, G, U, F, V, M, V, M, M, V, M, V, F, U, G, M, M, M, U, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, M, M, H, M, M, W, W, W, W, W, W, W, W, M, M, M, F, F, M, M, M, M, M, M, M, M, F, F, M, M, M, M, M, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, U, M, U, U, U, U, U, U, U, U, U, U, U, U, M, M, M, M, U, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, X, M, M, X, M, M, M, M, M, M, M, M, M, M, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, I, U, M, M, M, M, M, X, M, M, X, M, M, M, M, M, M, M, M, U, I, W, W,
			W, W, W, M, M, H, I, H, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, V, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W,
			W, W, W, F, F, F, F, F, F, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, U, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, U, M, W, W,
			W, W, W, F, F, F, F, F, F, F, W, W, W, W, W, W, W, W, W, H, M, W, W, W, W, U, U, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, W, W,
			W, W, W, M, X, X, R, C, M, M, W, W, W, W, W, W, W, W, W, H, M, M, M, R, S, M, U, M, U, M, U, M, U, M, U, M, U, M, U, M, U, M, U, M, U, M, W, W,
			W, W, W, M, M, G, G, G, I, W, W, W, W, W, W, W, W, W, W, H, M, W, W, W, W, H, U, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, H, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W


	};

	public static final int[] SOKOBAN_PUZZLE_LEVEL = {
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, R, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, U, M, M, M, M, U, S, R, W, W, W, M, A, M, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, C, C, U, H, U, C, C, W, W, W, W, W, W, W, W, W, W, W, M, M, H, H, M, M, W, W, W, W, W, U, I, U, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, G, G, U, F, U, G, G, W, W, W, W, W, W, W, W, W, W, W, M, M, H, H, M, M, W, W, W, W, W, U, I, U, W, W, W, W,
			W, W, W, W, W, W, W, W, M, M, S, M, G, G, U, F, U, G, G, V, S, M, M, U, W, W, W, W, W, W, U, M, M, M, M, U, W, W, W, W, W, U, I, U, W, W, W, W,
			W, W, W, W, W, W, W, W, G, U, W, W, C, I, U, F, U, I, C, W, W, W, W, G, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, U, I, U, W, W, W, W,
			W, W, W, W, W, W, W, W, W, V, W, W, G, G, U, F, U, G, G, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, U, I, U, W, W, W, W,
			W, W, W, W, W, W, W, W, W, M, W, W, C, M, U, F, U, M, C, W, W, W, W, G, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, U, I, U, W, W, W, W,
			W, W, W, W, W, W, W, W, W, M, S, R, G, G, U, F, U, G, G, V, S, M, M, U, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, U, I, U, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, M, M, A, E, A, M, I, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, U, I, U, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, U, G, C, U, C, G, U, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, U, H, U, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, H, M, M, M, W, B, M, M, M, B, W, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, F, F, F, F, W, G, G, M, G, G, W, M, M, F, F, W, W, W, W, W, W, W, M, M, R, W, W, W, M, M, U, M, H, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, H, M, M, M, S, M, M, G, M, M, S, M, G, F, H, W, W, W, W, W, W, W, M, H, M, W, W, W, M, M, M, M, H, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, F, F, F, F, W, M, F, F, F, M, W, M, M, F, F, W, W, W, W, W, W, W, M, M, M, W, W, W, M, M, U, M, H, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, H, M, M, M, W, M, F, H, F, M, W, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, R, M, M, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, M, C, G, C, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, X, G, X, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, M, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, M, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, F, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, M, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, C, C, C, C, C, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, M, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, I, M, G, G, G, G, G, M, I, W, W, W, W, W, W, W, W, W, W, W, W, W, F, F, H, H, M, L, M, W, W, W, W, M, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, M, M, U, U, U, U, U, M, M, W, W, W, W, W, W, W, W, W, M, H, M, W, F, U, U, U, U, W, T, W, W, W, W, M, W, W, W,
			W, W, W, W, W, W, W, W, W, W, C, G, U, F, M, M, M, F, U, G, C, W, W, W, W, W, W, W, W, M, M, M, W, A, X, B, I, M, W, T, W, W, W, M, M, W, W, W,
			W, W, W, W, W, W, W, W, W, W, C, G, U, M, F, F, F, M, U, G, C, W, W, W, W, W, W, W, W, M, M, M, W, M, M, M, M, M, W, T, W, W, W, U, A, W, W, W,
			W, W, W, W, W, W, W, W, W, W, C, G, U, M, U, F, U, M, U, G, C, W, W, W, W, W, W, W, W, W, M, W, W, M, M, M, M, M, W, T, W, W, W, T, T, W, W, W,
			W, W, W, W, W, W, W, W, W, W, C, G, U, M, U, F, U, M, U, G, C, W, W, W, W, W, W, W, W, W, M, W, W, L, W, W, W, W, W, T, W, W, W, T, F, W, W, W,
			W, W, W, W, W, W, W, W, W, W, C, G, U, F, U, H, U, F, U, G, C, W, W, W, W, W, W, W, M, M, M, M, A, F, F, M, W, W, W, T, W, W, W, T, F, R, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, F, U, U, U, U, U, F, W, W, W, W, W, W, W, W, W, G, W, W, W, W, W, W, M, W, W, W, T, W, W, W, T, F, W, W, W,
			W, W, W, W, W, W, W, W, W, H, F, S, M, B, M, B, M, B, M, S, F, H, W, W, W, W, W, M, M, W, W, W, W, W, W, M, W, W, W, T, W, W, W, T, T, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, T, W, W, W, T, T, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, G, V, W, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, T, T, T, T, T, T, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, B, G, G, G, G, G, F, F, M, W, W, W, W, W, W, W, M, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, C, C, G, C, C, W, W, W, W, W, W, W, W, W, W, M, M, M, M, M, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, U, U, G, U, U, W, W, W, W, W, M, M, R, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, C, C, M, C, C, W, W, W, W, W, X, M, M, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, U, G, M, G, U, W, W, W, W, W, F, M, M, W, W, I, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, B, C, M, C, M, W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, U, U, M, U, U, W, W, W, W, M, M, M, W, W, W, G, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, B, F, F, F, F, F, F, F, L, M, B, M, F, H, D, C, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, M, M, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
			W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
	};


}
