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
package com.github.epd.sprout.mechanics;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.levels.Level;

import java.util.ArrayList;
import java.util.List;

public class Ballistica {

	//note that the path is the FULL path of the projectile, including tiles after collision.
	//make sure to generate a subPath for the common case of going source to collision.
	public ArrayList<Integer> path = new ArrayList<>();
	public Integer sourcePos = null;
	public Integer collisionPos = null;
	public Integer dist = 0;

	//parameters to specify the colliding cell
	public static final int STOP_TARGET = 1; //ballistica will stop at the target cell
	public static final int STOP_CHARS = 2; //ballistica will stop on first char hit
	public static final int STOP_TERRAIN = 4; //ballistica will stop on terrain(LOS blocking, impassable, etc.)

	public static final int PROJECTILE =  	STOP_TARGET	| STOP_CHARS	| STOP_TERRAIN;
	public static final int MAGIC_BOLT =    STOP_CHARS  | STOP_TERRAIN;
	public static final int WONT_STOP =     0;
	public static final int STOP_SOLID = 8; //ballistica will stop on walls(impassable)

	public Ballistica( int from, int to, int params ){
		sourcePos = from;
		build(from, to, (params & STOP_TARGET) > 0, (params & STOP_CHARS) > 0, (params & STOP_TERRAIN) > 0, (params & STOP_SOLID) > 0);
		if (collisionPos != null)
			dist = path.indexOf( collisionPos );
		else
			collisionPos = path.get( dist=path.size()-1 );
	}
	private void build( int from, int to, boolean stopTarget, boolean stopChars, boolean stopTerrain, boolean stopSolid ) {
		int w = Level.getWidth();

		int x0 = from % w;
		int x1 = to % w;
		int y0 = from / w;
		int y1 = to / w;

		int dx = x1 - x0;
		int dy = y1 - y0;

		int stepX = dx > 0 ? +1 : -1;
		int stepY = dy > 0 ? +1 : -1;

		dx = Math.abs( dx );
		dy = Math.abs( dy );

		int stepA;
		int stepB;
		int dA;
		int dB;

		if (dx > dy) {

			stepA = stepX;
			stepB = stepY * w;
			dA = dx;
			dB = dy;

		} else {

			stepA = stepY * w;
			stepB = stepX;
			dA = dy;
			dB = dx;

		}

		int cell = from;

		int err = dA / 2;
		while (Dungeon.level.insideMap(cell)) {

			//if we're in a wall, collide with the previous cell along the path.
			if (stopTerrain && cell != sourcePos && !Level.passable[cell] && !Level.avoid[cell]) {
				collide(path.get(path.size() - 1));
			}

			path.add(cell);

			if ((stopTerrain && cell != sourcePos && Level.losBlocking[cell])
					|| (cell != sourcePos && stopChars && Actor.findChar( cell ) != null)
					|| (cell == to && stopTarget)
					|| (stopSolid && cell != sourcePos && Level.solid[cell])){
				collide(cell);
			}

			cell += stepA;

			err += dB;
			if (err >= dA) {
				err = err - dA;
				cell = cell + stepB;
			}
		}
	}

	//we only want to record the first position collision occurs at.
	private void collide(int cell){
		if (collisionPos == null)
			collisionPos = cell;
	}
	//returns a segment of the path from start to end, inclusive.
	//if there is an error, returns an empty arraylist instead.
	public List<Integer> subPath(int start, int end){
		try {
			end = Math.min( end, path.size()-1);
			return path.subList(start, end+1);
		} catch (Exception e){
			return new ArrayList<>();
		}
	}

	public static int[] trace = new int[Math.max(Level.getWidth(), Level.HEIGHT)];
	public static int distance;

	public static int cast(int from, int to, boolean magic, boolean hitChars) {

		int w = Level.getWidth();

		int x0 = from % w;
		int x1 = to % w;
		int y0 = from / w;
		int y1 = to / w;

		int dx = x1 - x0;
		int dy = y1 - y0;

		int stepX = dx > 0 ? +1 : -1;
		int stepY = dy > 0 ? +1 : -1;

		dx = Math.abs(dx);
		dy = Math.abs(dy);

		int stepA;
		int stepB;
		int dA;
		int dB;

		if (dx > dy) {

			stepA = stepX;
			stepB = stepY * w;
			dA = dx;
			dB = dy;

		} else {

			stepA = stepY * w;
			stepB = stepX;
			dA = dy;
			dB = dx;

		}

		distance = 1;
		trace[0] = from;

		int cell = from;

		int err = dA / 2;
		while (cell != to || magic) {

			cell += stepA;

			err += dB;
			if (err >= dA) {
				err = err - dA;
				cell = cell + stepB;
			}

			trace[distance++] = cell;

			if (!Level.passable[cell] && !Level.avoid[cell]) {
				return trace[--distance - 1];
			}

			if (Level.losBlocking[cell]
					|| (hitChars && Actor.findChar(cell) != null)) {
				return cell;
			}
		}

		trace[distance++] = cell;

		return to;
	}
}

