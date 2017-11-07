
package com.github.epd.sprout.items.weapon.melee.relic;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Slow;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class NeptunusTrident extends RelicMeleeWeapon {

	public NeptunusTrident() {
		super(6, 1f, 1f);

	}


	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.TRIDENT;

		level = 0;
		exp = 0;
		levelCap = 15;

		charge = 0;
		chargeCap = 1000;

		cooldown = 0;
		bones = false;

		defaultAction = AC_FLOOD;
	}


	private int distance() {
		return Math.round(level / 5);
	}

	private void flood(int distance, Hero hero) {
		charge = 0;
		ArrayList<Integer> affected = new ArrayList<Integer>();

		int length = Dungeon.level.getLength();
		int width = Dungeon.level.getWidth();
		for (int i = width; i < length - width; i++) {
			int dist = Dungeon.level.distance(hero.pos, i);
			if (dist < distance) {
				//GLog.i("TRI2 %s", dist);
				if (checkFloodable(i)) {
					affected.add(i);
					Dungeon.level.map[i] = Terrain.WATER;
					Level.water[i] = true;
				}
			}

		}
		//GLog.i("TRI1 %s", length);
		for (int n : affected) {
			int t = Terrain.WATER_TILES;
			for (int j = 0; j < PathFinder.NEIGHBOURS4.length; j++) {
				if ((Terrain.flags[Dungeon.level.map[n + PathFinder.NEIGHBOURS4[j]]] & Terrain.UNSTITCHABLE) != 0) {
					t += 1 << j;

				}
			}

			Char ch = Actor.findChar(n);
			if (ch != null && ch != hero) {
				Buff.affect(ch, Slow.class, Slow.duration(ch) / 3 + level);
			}

			Dungeon.level.map[n] = t;
			//Level.water[i] = true;
			GameScene.updateMap(n);
		}
		Dungeon.observe();

	}

	private boolean checkFloodable(int cell) {

		boolean check = false;

		if ((Dungeon.level.map[cell] == Terrain.EMPTY ||
				Dungeon.level.map[cell] == Terrain.GRASS ||
				Dungeon.level.map[cell] == Terrain.HIGH_GRASS ||
				Dungeon.level.map[cell] == Terrain.EMBERS ||
				Dungeon.level.map[cell] == Terrain.EMPTY_DECO ||
				Dungeon.level.map[cell] == Terrain.SHRUB ||
				Dungeon.level.map[cell] == Terrain.STATUE ||
				Dungeon.level.map[cell] == Terrain.SECRET ||
				Dungeon.level.map[cell] == Terrain.AVOID)
				&&
				!(Dungeon.level.map[cell] == Terrain.UNSTITCHABLE || Dungeon.level.map[cell] == Terrain.WELL)
				) {
			check = true;
		}

		if (Level.water[cell]) {
			check = true;
		}

		if (Dungeon.level.map[cell] == Terrain.ENTRANCE || Dungeon.level.map[cell] == Terrain.EXIT) {
			check = false;
		}

		return check;
	}


	public static final String AC_FLOOD = Messages.get(NeptunusTrident.class, "ac_flood");

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge >= chargeCap)
			actions.add(AC_FLOOD);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_FLOOD)) {
			int distance = distance();
			GLog.p(Messages.get(this, "ready"));
			flood(distance, hero);
		} else
			super.execute(hero, action);
	}


	public class Flooding extends WeaponBuff {

		@Override
		public boolean act() {
			if (charge < chargeCap) {
				charge += level;
				if (charge >= chargeCap) {
					charge = chargeCap;
					GLog.p(Messages.get(NeptunusTrident.class, "buffdesc"));
				}
				updateQuickslot();
			}
			spend(TICK);
			return true;
		}


		@Override
		public String toString() {
			return Messages.get(NeptunusTrident.class, "buffname");
		}

		@Override
		public int icon() {
			if (cooldown == 0)
				return BuffIndicator.NONE;
			else
				return BuffIndicator.NONE;
		}

		@Override
		public void detach() {
			cooldown = 0;
			charge = 0;
			super.detach();
		}

	}

	@Override
	protected WeaponBuff passiveBuff() {
		return new Flooding();
	}

}


