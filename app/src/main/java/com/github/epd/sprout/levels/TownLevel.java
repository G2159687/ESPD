
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Badges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Alchemy;
import com.github.epd.sprout.actors.blobs.Alter;
import com.github.epd.sprout.actors.blobs.WellWater;
import com.github.epd.sprout.actors.mobs.BlueCat;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.npcs.OtilukeNPC;
import com.github.epd.sprout.actors.mobs.npcs.Shopkeeper;
import com.github.epd.sprout.actors.mobs.npcs.Tinkerer4;
import com.github.epd.sprout.actors.mobs.npcs.Tinkerer5;
import com.github.epd.sprout.items.bombs.ActiveMrDestructo;
import com.github.epd.sprout.items.bombs.ActiveMrDestructo2;
import com.github.epd.sprout.items.Egg;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.bombs.SeekingClusterBombItem;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.items.potions.PotionOfOverHealing;
import com.github.epd.sprout.items.quest.DarkGold;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.levels.features.Chasm;
import com.github.epd.sprout.levels.features.Door;
import com.github.epd.sprout.levels.features.HighGrass;
import com.github.epd.sprout.levels.traps.AlarmTrap;
import com.github.epd.sprout.levels.traps.FireTrap;
import com.github.epd.sprout.levels.traps.GrippingTrap;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.levels.traps.ParalyticTrap;
import com.github.epd.sprout.levels.traps.PoisonTrap;
import com.github.epd.sprout.levels.traps.SummoningTrap;
import com.github.epd.sprout.levels.traps.ToxicTrap;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Phaseshift;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.plants.Starflower;
import com.github.epd.sprout.plants.Sungrass;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class TownLevel extends Level {


	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
		special = false;
	}


	public int mineDepth = 0;

	public int[] scrollspots;
	public int[] storespots;
	//public int[] potionspots;


	private static final String MINEDEPTH = "mineDepth";
	private static final String SCROLLSPOTS = "scrollspots";
	private static final String STORESPOTS = "storespots";
	//private static final String POTIONSPOTS = "potionspots";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(MINEDEPTH, mineDepth);
		bundle.put(SCROLLSPOTS, scrollspots);
		bundle.put(STORESPOTS, storespots);
		//bundle.put(POTIONSPOTS, potionspots);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		mineDepth = bundle.getInt(MINEDEPTH);
		scrollspots = bundle.getIntArray(SCROLLSPOTS);
		storespots = bundle.getIntArray(STORESPOTS);
		//if (Dungeon.version>134){
		//   potionspots = bundle.getIntArray(POTIONSPOTS);
		// }
		// storeStock();
	}

	private boolean checkOtiluke() {
		boolean check = false;
		for (Mob mob : mobs) {
			if (mob instanceof OtilukeNPC) {
				check = true;
			}
		}
		return check;
	}

	public void storeStock() {


		if (Actor.findChar(13 + getWidth() * 10) == null) {
			Mob shopkeeper = new Shopkeeper();
			shopkeeper.pos = 13 + getWidth() * 10;
			mobs.add(shopkeeper);
		}
		if (Actor.findChar(8 + getWidth() * 23) == null) {
			Mob shopkeeper2 = new Shopkeeper();
			shopkeeper2.pos = 8 + getWidth() * 23;
			mobs.add(shopkeeper2);

		}

		if (Badges.checkOtilukeRescued() && !checkOtiluke()) {
			if (Actor.findChar(32 + getWidth() * 15) == null) {
				Mob otiluke = new OtilukeNPC();
				otiluke.pos = 32 + getWidth() * 15;
				mobs.add(otiluke);
			}

			map[exit] = Terrain.STATUE;
		}

		if (storeRefresh()) {


			for (int i : scrollspots) {
				Heap heap = heaps.get(i);
				if (heap == null) {
					drop(new ScrollOfUpgrade(), i).type = Heap.Type.FOR_SALE;
				}
			}


			for (int i : storespots) {
				Heap heap = heaps.get(i);
				if (heap == null) {
					Item storeitem = storeItem();
					drop(storeitem, i).type = Heap.Type.FOR_SALE;
				}
			}
		}
	}

	public Item storeItem() {
		Item prize;
		switch (Random.Int(10)) {
			case 0:
				prize = new PotionOfHealing();
				break;
			case 1:
				prize = Generator.random(Generator.Category.SEEDRICH);
				break;
			case 2:
				prize = new PotionOfOverHealing();
				break;
			case 3:
				prize = new PotionOfOverHealing();
				break;
			case 5:
				prize = new ActiveMrDestructo2();
				break;
			case 6:
				prize = new SeekingClusterBombItem();
				break;
			case 7:
				prize = new DarkGold().quantity(50);
				break;
			case 8:
				prize = new DarkGold().quantity(50);
				break;
			case 9:
				prize = new Sungrass.Seed().quantity(5);
				break;
			default:
				prize = new PotionOfHealing();
				break;
		}

		return prize;

	}

	public boolean storeRefresh() {
		boolean check = false;
		if (Statistics.realdeepestFloor > mineDepth) {
			mineDepth = Statistics.realdeepestFloor;
			check = true;
		}
		return check;
	}


	@Override
	protected void createItems() {

		/* 6 + getWidth() * 25	to    10 + getWidth() *25  Magic
		 * 4 + getWidth() * 27   to    4 + getWidth() * 22
		 * 
		 * 7 + getWidth() * 10	to    7 + getWidth() * 7		Normal
		 * 12 + getWidth() * 13	to    12 + getWidth() * 8	
		 */
		//drop(new PotionOfLiquidFlame(), 9 + getWidth() * 24).type = Heap.Type.FOR_SALE;

		Mob shopkeeper = new Shopkeeper();
		shopkeeper.pos = 13 + getWidth() * 10;
		mobs.add(shopkeeper);

		Mob shopkeeper2 = new Shopkeeper();
		shopkeeper2.pos = 8 + getWidth() * 23;
		mobs.add(shopkeeper2);
	  /*
      Mob shopkeeper3 =  new Shopkeeper();
      shopkeeper3.pos = 22 + getWidth() * 8;
      mobs.add(shopkeeper3);
      */

		Mob bluecat = new BlueCat();
		bluecat.pos = 35 + getWidth() * 5;
		mobs.add(bluecat);


		Mob tinkerer1 = new Tinkerer4();
		tinkerer1.pos = 31 + getWidth() * 20;
		mobs.add(tinkerer1);

		Mob tinkerer2 = new Tinkerer5();
		tinkerer2.pos = 14 + getWidth() * 33;
		mobs.add(tinkerer2);


		scrollspots = new int[11];
		scrollspots[0] = 4 + getWidth() * 27;
		scrollspots[1] = 4 + getWidth() * 26;
		scrollspots[2] = 4 + getWidth() * 25;
		scrollspots[3] = 4 + getWidth() * 24;
		scrollspots[4] = 4 + getWidth() * 23;
		scrollspots[5] = 4 + getWidth() * 22;

		scrollspots[6] = 6 + getWidth() * 25;
		scrollspots[7] = 7 + getWidth() * 25;
		scrollspots[8] = 8 + getWidth() * 25;
		scrollspots[9] = 9 + getWidth() * 25;
		scrollspots[10] = 10 + getWidth() * 25;

		storespots = new int[9];
		storespots[0] = 7 + getWidth() * 10;
		storespots[1] = 7 + getWidth() * 9;
		storespots[2] = 7 + getWidth() * 8;
		storespots[3] = 7 + getWidth() * 7;

		storespots[4] = 12 + getWidth() * 13;
		storespots[5] = 12 + getWidth() * 12;
		storespots[6] = 12 + getWidth() * 11;
		storespots[7] = 12 + getWidth() * 10;
		storespots[8] = 12 + getWidth() * 9;

     /*
      potionspots = new int[10];
      potionspots[0] =  20 + getWidth() * 6;
      potionspots[1] =  20 + getWidth() * 7;
      potionspots[2] =  20 + getWidth() * 8;
      potionspots[3] =  20 + getWidth() * 9;

      potionspots[4] =  20 + getWidth() * 10;
      potionspots[5] =  20 + getWidth() * 11;
      potionspots[6] =  21 + getWidth() * 6;
      potionspots[7] =  22 + getWidth() * 6;
      potionspots[8] =  23 + getWidth() * 6;
      potionspots[9] =  24 + getWidth() * 6;
        */

		storeStock();

		Alter alter = new Alter();
		alter.seed(this, 33 + getWidth() * 32, 1);
		blobs.put(Alter.class, alter);

		addChest(39 + getWidth() * 4);
		addChest(40 + getWidth() * 4);
		addChest(41 + getWidth() * 4);
		addChest(42 + getWidth() * 4);

		addChest(41 + getWidth() * 18);
		addChest(42 + getWidth() * 18);

		addChest(7 + getWidth() * 5);
		addChest(8 + getWidth() * 5);
		addChest(9 + getWidth() * 5);

		addChest(27 + getWidth() * 11);
		addChest(27 + getWidth() * 12);

		//Activate Bunny Rancher Mode
      /*
      for (int i = 0; i < LENGTH; i++) {
			if (Random.Int(10)<2 && map[i]==Terrain.EMPTY){
				Bunny pet = new Bunny();
				mobs.add(pet); pet.pos = i;
				Actor.occupyCell(pet);
			}
		}
		*/

	}

	private void addChest(int pos) {

		Item prize;
		switch (Random.Int(10)) {
			case 0:
				prize = new Egg();
				break;
			case 1:
				prize = new Phaseshift.Seed();
				break;
			case 2:
				prize = Generator.random(Generator.Category.BERRY);
				break;
			case 3:
				prize = new Starflower.Seed();
				break;
			case 5:
				prize = new ActiveMrDestructo();
				break;
			case 6:
				prize = new SeekingClusterBombItem();
				break;
			default:
				prize = new Gold(Random.IntRange(1, 5));
				break;
		}

		drop(prize, pos).type = Heap.Type.CHEST;
	}


	@Override
	public void press(int cell, Char ch) {

		if (!special) {
			storeStock();
			special = true;
		}

		if (pit[cell] && ch == Dungeon.hero) {
			Chasm.heroFall(cell);
			return;
		}

		TimekeepersHourglass.timeFreeze timeFreeze = null;

		if (ch != null)
			timeFreeze = ch.buff(TimekeepersHourglass.timeFreeze.class);

		boolean trap = false;
		boolean interrupt = false;

		switch (map[cell]) {


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

			case Terrain.PEDESTAL:
				if (ch == null) {
					Alter.transmute(cell);
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

		if (interrupt) {

			Dungeon.hero.interrupt();
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

		if (trap) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.INACTIVE_TRAP);
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
		return Assets.TILES_TOWN;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

	@Override
	protected boolean build() {

		setSize(48, 48);

		map = TownLayouts.TOWN_LAYOUT.clone();

		buildFlagMaps();
		cleanWalls();

		entrance = 25 + getWidth() * 21;
		exit = 5 + getWidth() * 40;


		return true;
	}

	@Override
	protected void createMobs() {

	}


	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(TownLevel.class, "empty_desc");
			default:
				return super.tileDesc(tile);
		}
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(CityLevel.class, "water_desc");
			case Terrain.HIGH_GRASS:
				return Messages.get(CityLevel.class, "high_grass_desc");
			default:
				return super.tileName(tile);
		}
	}


	@Override
	public int randomRespawnCell() {
		return -1;
	}


}
