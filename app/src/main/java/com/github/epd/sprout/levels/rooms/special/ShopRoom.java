
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.npcs.Shopkeeper;
import com.github.epd.sprout.items.Ankh;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.items.BookOfDead;
import com.github.epd.sprout.items.BookOfLife;
import com.github.epd.sprout.items.BookOfTranscendence;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Honeypot;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.Stylus;
import com.github.epd.sprout.items.Torch;
import com.github.epd.sprout.items.Weightstone;
import com.github.epd.sprout.items.armor.MailArmor;
import com.github.epd.sprout.items.armor.PlateArmor;
import com.github.epd.sprout.items.armor.ScaleArmor;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.items.potions.PotionOfOverHealing;
import com.github.epd.sprout.items.scrolls.ScrollOfIdentify;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicMapping;
import com.github.epd.sprout.items.scrolls.ScrollOfRegrowth;
import com.github.epd.sprout.items.scrolls.ScrollOfRemoveCurse;
import com.github.epd.sprout.items.weapon.melee.BattleAxe;
import com.github.epd.sprout.items.weapon.melee.Longsword;
import com.github.epd.sprout.items.weapon.melee.Mace;
import com.github.epd.sprout.items.weapon.melee.Quarterstaff;
import com.github.epd.sprout.items.weapon.melee.Spear;
import com.github.epd.sprout.items.weapon.melee.Sword;
import com.github.epd.sprout.items.weapon.missiles.CurareDart;
import com.github.epd.sprout.items.weapon.missiles.IncendiaryDart;
import com.github.epd.sprout.items.weapon.missiles.Javelin;
import com.github.epd.sprout.items.weapon.missiles.Shuriken;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShopRoom extends SpecialRoom {

	private ArrayList<Item> itemsToSpawn;

	@Override
	public int minWidth() {
		if (itemsToSpawn == null) itemsToSpawn = generateItems();
		return Math.max(7, (int)(Math.sqrt(itemsToSpawn.size())+3));
	}

	@Override
	public int minHeight() {
		if (itemsToSpawn == null) itemsToSpawn = generateItems();
		return Math.max(7, (int)(Math.sqrt(itemsToSpawn.size())+3));
	}

	public void paint( Level level ) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );

		placeShopkeeper( level );

		placeItems( level );

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

	}

	protected void placeShopkeeper( Level level ) {

		int pos = level.pointToCell(center());

		Mob shopkeeper = new Shopkeeper();
		shopkeeper.pos = pos;
		level.mobs.add( shopkeeper );

	}

	protected void placeItems( Level level ){

		if (itemsToSpawn == null)
			itemsToSpawn = generateItems();

		Point itemPlacement = new Point(entrance());
		if (itemPlacement.y == top){
			itemPlacement.y++;
		} else if (itemPlacement.y == bottom) {
			itemPlacement.y--;
		} else if (itemPlacement.x == left){
			itemPlacement.x++;
		} else {
			itemPlacement.x--;
		}

		for (Item item : itemsToSpawn) {

			if (itemPlacement.x == left+1 && itemPlacement.y != top+1){
				itemPlacement.y--;
			} else if (itemPlacement.y == top+1 && itemPlacement.x != right-1){
				itemPlacement.x++;
			} else if (itemPlacement.x == right-1 && itemPlacement.y != bottom-1){
				itemPlacement.y++;
			} else {
				itemPlacement.x--;
			}

			int cell = level.pointToCell(itemPlacement);

			if (level.heaps.get( cell ) != null) {
				do {
					cell = level.pointToCell(random());
				} while (level.heaps.get( cell ) != null || level.findMob( cell ) != null);
			}

			level.drop( item, cell ).type = Heap.Type.FOR_SALE;
		}

	}

	protected static ArrayList<Item> generateItems() {

		ArrayList<Item> itemsToSpawn = new ArrayList<>();

		switch (Dungeon.depth) {
			case 6:
				itemsToSpawn.add((Random.Int(2) == 0 ? new Quarterstaff() : new Spear()).identify());
				itemsToSpawn.add(Random.Int(2) == 0 ? new IncendiaryDart().quantity(Random.NormalIntRange(2, 4))
						: new CurareDart().quantity(Random.NormalIntRange(1, 3)));
				itemsToSpawn.add(new MailArmor().identify());
				break;

			case 11:
				itemsToSpawn.add((Random.Int(2) == 0 ? new Sword() : new Mace()).identify());
				itemsToSpawn.add(Random.Int(2) == 0 ? new CurareDart().quantity(Random.NormalIntRange(2, 5))
						: new Shuriken().quantity(Random.NormalIntRange(3, 6)));
				itemsToSpawn.add(new ScaleArmor().identify());
				break;

			case 16:
				itemsToSpawn.add((Random.Int(2) == 0 ? new Longsword() : new BattleAxe()).identify());
				itemsToSpawn.add(Random.Int(2) == 0 ? new Shuriken().quantity(Random.NormalIntRange(4, 7))
						: new Javelin().quantity(Random.NormalIntRange(3, 6)));
				itemsToSpawn.add(new PlateArmor().identify());
				break;

			case 21:
				itemsToSpawn.add(new Torch());
				itemsToSpawn.add(new Torch());
				itemsToSpawn.add(new Torch());
				itemsToSpawn.add(new BookOfDead());
				itemsToSpawn.add(new BookOfLife());
				itemsToSpawn.add(new BookOfTranscendence());
				break;
		}

		itemsToSpawn.add(Random.Int(3) == 0 ? new PotionOfOverHealing() : new PotionOfHealing());
		for (int i = 0; i < 3; i++)
			itemsToSpawn.add(Generator.random(Generator.Category.POTION));

		itemsToSpawn.add(new ScrollOfIdentify());
		itemsToSpawn.add(new ScrollOfRemoveCurse());
		itemsToSpawn.add(Random.Int(3) == 0 ? new ScrollOfRegrowth() : new ScrollOfMagicMapping());
		itemsToSpawn.add(Generator.random(Generator.Category.SCROLL));

		for (int i = 0; i < 2; i++)
			itemsToSpawn.add(Random.Int(2) == 0 ? Generator
					.random(Generator.Category.POTION) : Generator
					.random(Generator.Category.SCROLL));

		itemsToSpawn.add(new Bomb().random());
		switch (Random.Int(5)) {
			case 1:
				itemsToSpawn.add(new Bomb());
				break;
			case 2:
				itemsToSpawn.add(new Bomb().random());
				break;
			case 3:
			case 4:
				itemsToSpawn.add(new Honeypot());
				break;
		}

		if (Dungeon.depth == 6) {
			itemsToSpawn.add(new Ankh());
			itemsToSpawn.add(new Weightstone());
		} else {
			itemsToSpawn.add(Random.Int(2) == 0 ? new Ankh()
					: new Weightstone());
		}

		TimekeepersHourglass hourglass = Dungeon.hero.belongings
				.getItem(TimekeepersHourglass.class);
		if (hourglass != null) {
			int bags = 0;
			// creates the given float percent of the remaining bags to be
			// dropped.
			// this way players who get the hourglass late can still max it,
			// usually.
			switch (Dungeon.depth) {
				case 6:
					bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.20f);
					break;
				case 11:
					bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.25f);
					break;
				case 16:
					bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.50f);
					break;
				case 21:
					bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.80f);
					break;
			}

			for (int i = 1; i <= bags; i++) {
				itemsToSpawn.add(new TimekeepersHourglass.sandBag());
				hourglass.sandBags++;
			}
		}

		Item rare;
		switch (Random.Int(10)) {
			case 0:
				rare = Generator.random(Generator.Category.WAND);
				rare.level = 0;
				break;
			case 1:
				rare = Generator.random(Generator.Category.RING);
				rare.level = 1;
				break;
			case 2:
				rare = Generator.random(Generator.Category.ARTIFACT).identify();
				break;
			case 3:
				rare = Generator.random(Generator.Category.ARTIFACT).identify();
				break;
			default:
				rare = new Stylus();
		}
		rare.cursed = rare.cursedKnown = false;
		itemsToSpawn.add(rare);

		//hard limit is 63 items + 1 shopkeeper, as shops can't be bigger than 8x8=64 internally
		if (itemsToSpawn.size() > 63)
			throw new RuntimeException(
					"Shop attempted to carry more than 63 items!");

		Random.shuffle(itemsToSpawn);
		return itemsToSpawn;
	}

}
