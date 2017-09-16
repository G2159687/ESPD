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

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Alchemy;
import com.github.epd.sprout.actors.blobs.WellWater;
import com.github.epd.sprout.actors.mobs.LitTower;
import com.github.epd.sprout.actors.mobs.MineSentinel;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.Otiluke;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokoban;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanBlack;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanCorner;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanSwitch;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Palantir;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.items.wands.WandOfFlock.Sheep;
import com.github.epd.sprout.levels.features.Chasm;
import com.github.epd.sprout.levels.features.Door;
import com.github.epd.sprout.levels.features.HighGrass;
import com.github.epd.sprout.levels.traps.AlarmTrap;
import com.github.epd.sprout.levels.traps.ChangeSheepTrap;
import com.github.epd.sprout.levels.traps.FireTrap;
import com.github.epd.sprout.levels.traps.FleecingTrap;
import com.github.epd.sprout.levels.traps.GrippingTrap;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.levels.traps.ParalyticTrap;
import com.github.epd.sprout.levels.traps.PoisonTrap;
import com.github.epd.sprout.levels.traps.SummoningTrap;
import com.github.epd.sprout.levels.traps.ToxicTrap;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.windows.WndOtilukeMessage;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class MinesBossLevel extends Level {


	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
		WIDTH = 48;
		HEIGHT = 48;
		LENGTH = HEIGHT * WIDTH;
	}


	private boolean entered = false;
	private static final String ENTERED = "entered";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ENTERED, entered);

	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		entered = bundle.getBoolean(ENTERED);

	}


	@Override
	public void press(int cell, Char ch) {


		if (ch == Dungeon.hero && !entered) {

			entered = true;
			locked = true;
			GameScene.show(new WndOtilukeMessage());

		}

		if (pit[cell] && ch == Dungeon.hero) {
			Chasm.heroFall(cell);
			return;
		}

		TimekeepersHourglass.timeFreeze timeFreeze = null;

		if (ch != null)
			timeFreeze = ch.buff(TimekeepersHourglass.timeFreeze.class);

		boolean trap = false;

		switch (map[cell]) {

			case Terrain.FLEECING_TRAP:

				if (ch != null && ch == Dungeon.hero) {
					trap = true;
					FleecingTrap.trigger(cell, ch);
				}
				break;

			case Terrain.CHANGE_SHEEP_TRAP:

				if (ch instanceof SheepSokoban || ch instanceof SheepSokobanSwitch || ch instanceof SheepSokobanCorner || ch instanceof Sheep) {
					trap = true;
					ChangeSheepTrap.trigger(cell, ch);
				}
				break;


			case Terrain.HIGH_GRASS:
				HighGrass.trample(this, cell, ch);
				break;

			case Terrain.WELL:
				WellWater.affectCell(cell);
				break;

			case Terrain.ALCHEMY:
				if (ch == null) {
					Alchemy.transmute(cell);
				}
				break;

			case Terrain.DOOR:
				Door.enter(cell);
				break;
		}

		if (trap) {

			if (Dungeon.visible[cell])
				Sample.INSTANCE.play(Assets.SND_TRAP);

			if (ch == Dungeon.hero)
				Dungeon.hero.interrupt();

			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);
		}

		Plant plant = plants.get(cell);
		if (plant != null) {
			plant.activate(ch);
		}
	}


	@Override
	public void mobPress(Mob mob) {

		int cell = mob.pos;

		if (pit[cell] && !mob.flying) {
			Chasm.mobFall(mob);
			return;
		}

		boolean trap = true;
		boolean fleece = false;
		boolean sheep = false;
		switch (map[cell]) {

			case Terrain.TOXIC_TRAP:
				ToxicTrap.trigger(cell, mob);
				break;

			case Terrain.FIRE_TRAP:
				FireTrap.trigger(cell, mob);
				break;

			case Terrain.PARALYTIC_TRAP:
				ParalyticTrap.trigger(cell, mob);
				break;

			case Terrain.FLEECING_TRAP:
				if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner || mob instanceof SheepSokobanBlack || mob instanceof Sheep) {
					fleece = true;
				}
				FleecingTrap.trigger(cell, mob);
				break;


			case Terrain.POISON_TRAP:
				PoisonTrap.trigger(cell, mob);
				break;

			case Terrain.ALARM_TRAP:
				AlarmTrap.trigger(cell, mob);
				break;

			case Terrain.LIGHTNING_TRAP:
				LightningTrap.trigger(cell, mob);
				break;

			case Terrain.GRIPPING_TRAP:
				GrippingTrap.trigger(cell, mob);
				break;

			case Terrain.SUMMONING_TRAP:
				SummoningTrap.trigger(cell, mob);
				break;

			case Terrain.DOOR:
				Door.enter(cell);

			default:
				trap = false;
		}

		if (trap && !fleece && !sheep) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);
		}

		if (trap && fleece) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.WOOL_RUG);
			GameScene.updateMap(cell);
		}

		if (trap && sheep) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.EMPTY);
			GameScene.updateMap(cell);
		}


		Plant plant = plants.get(cell);
		if (plant != null) {
			plant.activate(mob);
		}

		Dungeon.observe();
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

	@Override
	protected boolean build() {


		map = Layouts.MINE_BOSS.clone();

		decorate();

		buildFlagMaps();
		cleanWalls();

		entrance = 17 + WIDTH * 44;
		exit = 0;


		return true;
	}

	@Override
	protected void decorate() {
		//do nothing, all decorations are hard-coded.
	}

	@Override
	protected void createMobs() {
		for (int i = 0; i < LENGTH; i++) {
			if (map[i] == Terrain.SOKOBAN_SHEEP) {
				MineSentinel npc = new MineSentinel();
				mobs.add(npc);
				npc.pos = i;
				Actor.occupyCell(npc);
			} else if (map[i] == Terrain.CORNER_SOKOBAN_SHEEP) {
				LitTower npc = new LitTower();
				mobs.add(npc);
				npc.pos = i;
				Actor.occupyCell(npc);
			}
		}

		Otiluke mob = new Otiluke();
		mobs.add(mob);
		mob.pos = 33 + WIDTH * 10;
		Actor.occupyCell(mob);
	}


	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(PrisonLevel.class, "empty_deco_desc");
			default:
				return super.tileDesc(tile);
		}
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(CityLevel.class, "water_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CityLevel.class, "high_grass_name");
			default:
				return super.tileName(tile);
		}
	}


	@Override
	protected void createItems() {

		drop(new IronKey(Dungeon.depth), 30 + WIDTH * 44).type = Heap.Type.CHEST;
		drop(new Palantir(), 14 + WIDTH * 10);

	}

	@Override
	public int randomRespawnCell() {
		return -1;
	}


}
