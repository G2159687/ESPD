
package com.github.epd.sprout.levels;

import com.watabou.utils.Random;

public class Patch {

	/*
	 * fill is the initial seeded fill rate when creating a random boolean array.
	 *
	 * clustering is the number of clustering passes done on then array, to create patches.
	 * each clustering pass is basically a 3x3 mask filter but with rounding to true or false
	 * high clustering values will produce more concentrated patches,
	 * but any amount of clustering will rapidly push fill rates towards 1.0f or 0.0f
	 * The closer the fill rate is to 0.5f the weaker this pushing will be.
	 *
	 * forceFillRate adjusts the algorithm to force fill rate to be consistent despite clustering.
	 * this is achived by firstly pulling the initial fill value towards 0.5f
	 * and then by manually filling in or emptying cells after clustering, until the fill rate is
	 * achieved. This is tracked with the fillDiff variable.
	*/
	public static boolean[] generate( int w, int h, float fill, int clustering, boolean forceFillRate ) {

		int length = w * h;

		boolean[] cur = new boolean[length];
		boolean[] off = new boolean[length];

		int fillDiff = -Math.round(length * fill);

		if (forceFillRate && clustering > 0) {
			fill += (0.5f - fill) * 0.5f;
		}

		for (int i=0; i < length; i++) {
			off[i] = Random.Float() < fill;
			if (off[i]) fillDiff++;
		}

		for (int i=0; i < clustering; i++) {

			for (int y=0; y < h; y++) {
				for (int x=0; x < w; x++) {

					int pos = x + y * w;
					int count = 0;
					int neighbours = 0;

					if (y > 0) {
						if (x > 0){
							if (off[pos - w - 1]) count++;
							neighbours++;
						}
						if (off[pos - w]) count++;
						neighbours++;
						if (x < (w - 1)){
							if (off[pos - w + 1]) count++;
							neighbours++;
						}
					}

					if (x > 0){
						if (off[pos - 1]) count++;
						neighbours++;
					}
					if (off[pos]) count++;
					neighbours++;
					if (x < (w-1)){
						if (off[pos + 1]) count++;
						neighbours++;
					}

					if (y < (h-1)) {
						if (x > 0){
							if (off[pos + w - 1]) count++;
							neighbours++;
						}
						if (off[pos + w]) count++;
						neighbours++;
						if (x < (w-1)){
							if (off[pos + w + 1]) count++;
							neighbours++;
						}
					}

					cur[pos] = 2*count >= neighbours;
					if (cur[pos] != off[pos]) fillDiff += cur[pos] ? +1 : -1;

				}
			}

			boolean[] tmp = cur;
			cur = off;
			off = tmp;
		}

		//even if force fill rate is on, only do this if we have some kind of border
		if (forceFillRate && Math.min(w, h) > 2) {
			int[] neighbours = new int[]{-w - 1, -w, -w + 1, -1, 0, +1, +w - 1, +w, +w + 1};
			boolean growing = fillDiff < 0;

			while (fillDiff != 0) {
				int cell;
				int tries = 0;

				//random cell, not in the map's borders
				// try length/10 times to find a cell we can grow from, and not start a new patch/hole
				do {
					cell = Random.Int(1, w - 1) + Random.Int(1, h - 1) * w;
					tries++;
				} while (off[cell] != growing && tries * 10 < length);

				for (int i : neighbours) {
					if (fillDiff != 0 && off[cell + i] != growing) {
						off[cell + i] = growing;
						fillDiff += growing ? +1 : -1;
					}
				}
			}
		}

		return off;
	}
}
