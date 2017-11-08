
package com.github.epd.sprout.items;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Frost;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mimic;
import com.github.epd.sprout.actors.mobs.RedWraith;
import com.github.epd.sprout.actors.mobs.Wraith;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.Splash;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.github.epd.sprout.items.artifacts.AlchemistsToolkit;
import com.github.epd.sprout.items.artifacts.Artifact;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.bombs.ActiveMrDestructo;
import com.github.epd.sprout.items.bombs.ActiveMrDestructo2;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.items.bombs.ClusterBomb;
import com.github.epd.sprout.items.bombs.DizzyBomb;
import com.github.epd.sprout.items.bombs.DumplingBomb;
import com.github.epd.sprout.items.bombs.HolyHandGrenade;
import com.github.epd.sprout.items.bombs.InactiveMrDestructo;
import com.github.epd.sprout.items.bombs.InactiveMrDestructo2;
import com.github.epd.sprout.items.bombs.SeekingBombItem;
import com.github.epd.sprout.items.bombs.SeekingClusterBombItem;
import com.github.epd.sprout.items.bombs.SmartBomb;
import com.github.epd.sprout.items.food.Blandfruit;
import com.github.epd.sprout.items.food.ChargrilledMeat;
import com.github.epd.sprout.items.food.FrozenCarpaccio;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.food.Nut;
import com.github.epd.sprout.items.food.ToastedNut;
import com.github.epd.sprout.items.quest.NornStone;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.potions.PotionOfExperience;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.AresSword;
import com.github.epd.sprout.items.weapon.melee.relic.CromCruachAxe;
import com.github.epd.sprout.items.weapon.melee.relic.LokisFlail;
import com.github.epd.sprout.items.weapon.melee.relic.NeptunusTrident;
import com.github.epd.sprout.items.weapon.missiles.JupitersWraith;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant.Seed;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Heap implements Bundlable {

	private static final String TXT_MIMIC = Messages.get(Heap.class, "mimic");
	//private static final String TXT_MONSTERBOX = "You've opened a world of hurt!";

	private static final int SEEDS_TO_POTION = 3;

	public enum Type {
		HEAP, FOR_SALE, CHEST, LOCKED_CHEST, CRYSTAL_CHEST, TOMB, SKELETON, REMAINS, MIMIC, HARD_TOMB //,MONSTERBOX
	}

	public Type type = Type.HEAP;

	public int pos = 0;

	public ItemSprite sprite;
	public boolean seen = false;

	public LinkedList<Item> items = new LinkedList<>();

	public int image() {
		switch (type) {
			case HEAP:
			case FOR_SALE:
				return size() > 0 ? items.peek().image() : 0;
			case CHEST:
			case MIMIC:
				return ItemSpriteSheet.CHEST;
			case LOCKED_CHEST:
				return ItemSpriteSheet.LOCKED_CHEST;
			case CRYSTAL_CHEST:
				return ItemSpriteSheet.CRYSTAL_CHEST;
			case TOMB:
				return ItemSpriteSheet.TOMB;
			case SKELETON:
				return ItemSpriteSheet.BONES;
			case REMAINS:
				return ItemSpriteSheet.REMAINS;
			case HARD_TOMB:
				return ItemSpriteSheet.GRAVE;
			//case MONSTERBOX:
			//	return ItemSpriteSheet.LOCKED_CHEST;
			default:
				return 0;
		}
	}


	public boolean chestCheck() {
		switch (type) {
			case HEAP:
			case FOR_SALE:
			case TOMB:
			case SKELETON:
			case REMAINS:
				return false;
			case CRYSTAL_CHEST:
			case LOCKED_CHEST:
			case MIMIC:
			case CHEST:
			case HARD_TOMB:
				return true;
			default:
				return false;
		}
	}


	public ItemSprite.Glowing glowing() {
		return (type == Type.HEAP || type == Type.FOR_SALE) && items.size() > 0 ? items
				.peek().glowing() : null;
	}

	public void open(Hero hero) {
		switch (type) {
			//case MONSTERBOX:
			//	if (MonsterBox.spawnAt(pos, items) != null) {
			//		GLog.n(TXT_MONSTERBOX);
			//		destroy();
			//	} else {
			//		type = Type.CHEST;
			//	}
			case MIMIC:
				if (Mimic.spawnAt(pos, items) != null) {
					GLog.n(TXT_MIMIC);
					destroy();
				} else {
					type = Type.CHEST;
				}
				break;
			case TOMB:
				Wraith.spawnAround(hero.pos);
				break;
			case SKELETON:
			case REMAINS:
				CellEmitter.center(pos).start(Speck.factory(Speck.RATTLE), 0.1f, 3);
				for (Item item : items) {
					if (item.cursed) {
						if (RedWraith.spawnAt(pos) == null) {
							hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
							hero.damage(hero.HP / 2, this);
						}
						Sample.INSTANCE.play(Assets.SND_CURSED);
						break;
					}
				}
				break;
			default:
		}

		//if (type != Type.MIMIC && type != Type.MONSTERBOX) {
		if (type != Type.MIMIC) {
			type = Type.HEAP;
			sprite.link();
			sprite.drop();
		}
	}

	public int size() {
		return items.size();
	}

	public Item pickUp() {

		Item item = items.removeFirst();
		if (items.isEmpty()) {
			destroy();
		} else if (sprite != null) {
			sprite.view(image(), glowing());
		}

		return item;
	}

	public Item peek() {
		return items.peek();
	}

	public void drop(Item item) {

		if (item.stackable && type != Type.FOR_SALE) {

			for (Item i : items) {
				if (i.isSimilar(item)) {
					i.quantity += item.quantity;
					item = i;
					break;
				}
			}
			items.remove(item);

		}

		if ((item instanceof Dewdrop || item instanceof DriedRose.Petal) && type != Type.FOR_SALE) {
			items.add(item);
		} else {
			items.addFirst(item);
		}

		if (sprite != null) {
			sprite.view(image(), glowing());
		}
	}

	public void replace(Item a, Item b) {
		int index = items.indexOf(a);
		if (index != -1) {
			items.remove(index);
			items.add(index, b);
		}
	}

	public void burn() {

		if (type == Type.MIMIC) {
			Mimic m = Mimic.spawnAt(pos, items);
			if (m != null) {
				Buff.affect(m, Burning.class).reignite(m);
				m.sprite.emitter().burst(FlameParticle.FACTORY, 5);
				destroy();
			}
		}

		if (type != Type.HEAP) {
			return;
		}

		boolean burnt = false;
		boolean evaporated = false;

		for (Item item : items.toArray(new Item[0])) {
			if (item instanceof Scroll) {
				items.remove(item);
				burnt = true;
			} else if (item instanceof Dewdrop) {
				items.remove(item);
				evaporated = true;
			} else if (item instanceof Egg) {
				((Egg) item).burns++;
				burnt = true;
			} else if (item instanceof EasterEgg) {
				((EasterEgg) item).burns++;
				burnt = true;
			} else if (item instanceof MysteryMeat) {
				replace(item, ChargrilledMeat.cook((MysteryMeat) item));
				burnt = true;
			} else if (item instanceof Meat) {
				replace(item, ChargrilledMeat.cook((Meat) item));
				burnt = true;
			} else if (item instanceof Nut) {
				replace(item, ToastedNut.cook((Nut) item));
				burnt = true;
			} else if (item instanceof Bomb) {
				items.remove(item);
				((Bomb) item).explode(pos);
				// stop processing the burning, it will be replaced by the
				// explosion.
				return;
			}
		}

		if (burnt || evaporated) {

			if (Dungeon.visible[pos]) {
				if (burnt) {
					burnFX(pos);
				} else {
					evaporateFX(pos);
				}
			}

			if (isEmpty()) {
				destroy();
			} else if (sprite != null) {
				sprite.view(image(), glowing());
			}

		}
	}

	public void removeSeekingBomb() {
		for (Item item : items.toArray(new Item[0])) {
			if (item instanceof SeekingBombItem) {
				items.remove(item);
			}

		}
	}

	// Note: should not be called to initiate an explosion, but rather by an
	// explosion that is happening.
	public void explode() {

		// breaks open most standard containers, mimics die.
		if (type == Type.MIMIC || type == Type.CHEST || type == Type.SKELETON) {
			type = Type.HEAP;
			sprite.link();
			sprite.drop();
			return;
		}

		if (type != Type.HEAP) {

			return;

		} else {

			for (Item item : items.toArray(new Item[0])) {

				if (item instanceof Potion && Random.Float() < 0.10f) {
					items.remove(item);
					((Potion) item).shatter(pos);

				} else if (item instanceof Bomb) {
					items.remove(item);
					((Bomb) item).explode(pos);
					// stop processing current explosion, it will be replaced by
					// the new one.
					return;
				} else if (item instanceof DizzyBomb) {
					items.remove(item);
					((DizzyBomb) item).explode(pos);
					// stop processing current explosion, it will be replaced by
					// the new one.
					return;
				} else if (item instanceof SmartBomb) {
					items.remove(item);
					((SmartBomb) item).explode(pos);
					// stop processing current explosion, it will be replaced by
					// the new one.
					return;
				} else if (item instanceof SeekingBombItem) {
					items.remove(item);
					((Bomb) item).explode(pos);
					// stop processing current explosion, it will be replaced by
					// the new one.
					return;
				} else if (item instanceof SeekingClusterBombItem) {
					items.remove(item);
					((ClusterBomb) item).explode(pos);
					// stop processing current explosion, it will be replaced by
					// the new one.
					return;
				} else if (item instanceof ClusterBomb) {
					items.remove(item);
					((ClusterBomb) item).explode(pos);
					// stop processing current explosion, it will be replaced by
					// the new one.
					return;

					// unique and upgraded items can endure the blast
				} else if (!(item.level > 0 || item.unique) && Random.Float() < 0.10f)
					items.remove(item);

			}

			if (items.isEmpty())
				destroy();
		}
	}

	// Note: should not be called to initiate an explosion, but rather by an
	// explosion that is happening.
	public void holyexplode() {

		for (Item item : items.toArray(new Item[0])) {

			if (item.cursed) {
				item.cursed = false;
				if (item.isUpgradable() && item.level < 0) {
					item.upgrade(-item.level);
				} //upgrade to even
			}

			if (item instanceof HolyHandGrenade) {
				items.remove(item);
				((HolyHandGrenade) item).explode(pos);
				// stop processing current explosion, it will be replaced by
				// the new one.
				return;
				// unique and upgraded items can endure the blast
			}

		}


		if (items.isEmpty())
			destroy();
	}

	// Note: should not be called to initiate an explosion, but rather by an
	// explosion that is happening.
	public void dumpexplode() {


		if (type != Type.HEAP) {

			return;

		} else {

			for (Item item : items.toArray(new Item[0])) {


				if (item instanceof DumplingBomb) {
					items.remove(item);
					((DumplingBomb) item).explode(pos);
					// stop processing current explosion, it will be replaced by
					// the new one.
					return;
					// unique and upgraded items can endure the blast
				}
			}

			if (items.isEmpty())
				destroy();
		}
	}

	public int dewdrops() {

		if (type != Type.HEAP) {
			return 0;
		}

		int drops = 0;

		for (Item item : items.toArray(new Item[0])) {
			if (item instanceof Dewdrop) {
				drops++;
			} else if (item instanceof VioletDewdrop) {
				drops++;
			} else if (item instanceof RedDewdrop) {
				drops++;
			} else if (item instanceof YellowDewdrop) {
				drops++;
			}
		}

		return drops;
	}


	public void lit() {
		if (type != Type.HEAP) {
			return;
		}
		for (Item item : items.toArray(new Item[0])) {
			if (item instanceof Egg) {
				((Egg) item).lits++;
			} else if (item instanceof EasterEgg) {
				((EasterEgg) item).lits++;
			} else if (item instanceof InactiveMrDestructo) {
				items.remove(item);
				Dungeon.level.drop(new ActiveMrDestructo(), pos).sprite.drop();
			} else if (item instanceof InactiveMrDestructo2) {
				items.remove(item);
				Dungeon.level.drop(new ActiveMrDestructo2(), pos).sprite.drop();
			}
		}
	}


	public void summon() {
		if (type != Type.HEAP) {
			return;
		}
		for (Item item : items.toArray(new Item[0])) {
			if (item instanceof Egg) {
				((Egg) item).summons++;
			} else if (item instanceof EasterEgg) {
				((EasterEgg) item).summons++;
			}
		}
	}

	public void poison() {
		if (type != Type.HEAP) {
			return;
		}
		for (Item item : items.toArray(new Item[0])) {
			if (item instanceof Egg) {
				((Egg) item).poisons++;
			} else if (item instanceof EasterEgg) {
				((EasterEgg) item).poisons++;
			}
		}
	}

	public void freeze() {

		if (type == Type.MIMIC) {
			Mimic m = Mimic.spawnAt(pos, items);
			if (m != null) {
				Buff.prolong(m, Frost.class,
						Frost.duration(m) * Random.Float(1.0f, 1.5f));
				destroy();
			}
		}


		if (type != Type.HEAP) {
			return;
		}

		boolean frozen = false;
		for (Item item : items.toArray(new Item[0])) {
			if (item instanceof MysteryMeat) {
				replace(item, FrozenCarpaccio.cook((MysteryMeat) item));
				frozen = true;
			} else if (item instanceof Potion) {
				if (items != null) {
					items.remove(item);
					((Potion) item).shatter(pos);
					frozen = true;
				}
			} else if (item instanceof Egg) {
				((Egg) item).freezes++;
				frozen = true;
			} else if (item instanceof EasterEgg) {
				((EasterEgg) item).freezes++;
			} else if (item instanceof Bomb) {
				((Bomb) item).fuse = null;
				frozen = true;
			} else if (item instanceof Meat) {
				replace(item, FrozenCarpaccio.cook((Meat) item));
				frozen = true;
			}
		}

		if (frozen) {
			if (isEmpty()) {
				destroy();
			} else if (sprite != null) {
				sprite.view(image(), glowing());
			}
		}
	}

	public Item transmute() {

		CellEmitter.get(pos).burst(Speck.factory(Speck.BUBBLE), 3);
		Splash.at(pos, 0xFFFFFF, 3);

		float chances[] = new float[items.size()];
		int count = 0;

		if (items.size() == 2 && items.get(0) instanceof Seed
				&& items.get(1) instanceof Blandfruit) {

			Sample.INSTANCE.play(Assets.SND_PUFF);
			CellEmitter.center(pos).burst(Speck.factory(Speck.EVOKE), 3);

			Blandfruit result = new Blandfruit();
			result.cook((Seed) items.get(0));

			destroy();

			return result;

		}

		int index = 0;
		for (Item item : items) {
			if (item instanceof Seed) {
				count += item.quantity;
				chances[index++] = item.quantity;
			} else {
				count = 0;
				break;
			}
		}

		// alchemists toolkit gives a chance to cook a potion in two or even one
		// seeds
		AlchemistsToolkit.alchemy alchemy = Dungeon.hero
				.buff(AlchemistsToolkit.alchemy.class);
		int bonus = alchemy != null ? alchemy.level() : -1;

		if (bonus != -1 ? alchemy.tryCook(count) : count >= SEEDS_TO_POTION) {

			CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
			Sample.INSTANCE.play(Assets.SND_PUFF);

			Item potion;

			if (Random.Int(count + bonus) == 0) {

				CellEmitter.center(pos).burst(Speck.factory(Speck.EVOKE), 3);

				destroy();

				potion = Generator.random(Generator.Category.POTION);

			} else {

				Seed proto = (Seed) items.get(Random.chances(chances));
				Class<? extends Item> itemClass = proto.alchemyClass;

				destroy();

				if (itemClass == null) {
					potion = Generator.random(Generator.Category.POTION);
				} else {
					try {
						potion = itemClass.newInstance();
					} catch (Exception e) {
						return null;
					}
				}
			}

			// not a buff per-se, meant to cancel out higher potion accuracy
			// when ppl are farming for potions of exp.
			if (bonus > 0)
				if (Random.Int(1000 / bonus) == 0)
					return new PotionOfExperience();

			return potion;

		} else {
			return null;
		}
	}

	public Weapon consecrate() {

		CellEmitter.get(pos).burst(Speck.factory(Speck.FORGE), 3);
		Splash.at(pos, 0xFFFFFF, 3);

		int count = 0;
		int type = 0;

		for (Item item : items) {
			if (item instanceof NornStone) {
				count += item.quantity;
				if (type == 0) {
					type = ((NornStone) item).type;
				} else if (Random.Int(3) < item.quantity) {
					type = ((NornStone) item).type;
				}
			} else {
				count = 0;
				break;
			}
		}

		Weapon weapon;

		if (count >= SEEDS_TO_POTION) {

			CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
			Sample.INSTANCE.play(Assets.SND_PUFF);

			destroy();

			switch (type) {
				case 1:
					weapon = new JupitersWraith();
					weapon.enchantJupiter();
					break;
				case 2:
					weapon = new AresSword();
					weapon.enchantAres();
					break;
				case 3:
					weapon = new CromCruachAxe();
					weapon.enchantLuck();
					break;
				case 4:
					weapon = new LokisFlail();
					weapon.enchantLoki();
					break;
				case 5:
					weapon = new NeptunusTrident();
					weapon.enchantNeptune();
					break;
				default:
					weapon = new AresSword();
					weapon.enchantAres();
					break;
			}

			return weapon;

		} else {
			return null;
		}
	}


	public static void burnFX(int pos) {
		CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
		Sample.INSTANCE.play(Assets.SND_BURNING);
	}

	public static void evaporateFX(int pos) {
		CellEmitter.get(pos).burst(Speck.factory(Speck.STEAM), 5);
	}

	public boolean isEmpty() {
		return items == null || items.size() == 0;
	}

	public void destroy() {
		Dungeon.level.heaps.remove(this.pos);
		if (sprite != null) {
			sprite.kill();
		}
		items.clear();
	}

	@Override
	public String toString() {
		switch (type) {
			case CHEST:
			case MIMIC:
				return Messages.get(this, "chest");
			case LOCKED_CHEST:
				return Messages.get(this, "locked_chest");
			case CRYSTAL_CHEST:
				return Messages.get(this, "crystal_chest");
			case TOMB:
				return Messages.get(this, "tomb");
			case SKELETON:
				return Messages.get(this, "skeleton");
			case REMAINS:
				return Messages.get(this, "remains");
			case HARD_TOMB:
				return Messages.get(this, "hardtomb");
			default:
				return peek().toString();
		}
	}

	public String info() {
		switch (type) {
			case CHEST:
			case MIMIC:
				return Messages.get(this, "chest_desc");
			case LOCKED_CHEST:
				return Messages.get(this, "locked_chest_desc");
			case CRYSTAL_CHEST:
				if (peek() instanceof Artifact)
					return Messages.get(this, "crystal_chest_desc", Messages.get(this, "artifact"));
				else if (peek() instanceof Wand)
					return Messages.get(this, "crystal_chest_desc", Messages.get(this, "wand"));
				else
					return Messages.get(this, "crystal_chest_desc", Messages.get(this, "ring"));
			case TOMB:
				return Messages.get(this, "tomb_desc");
			case SKELETON:
				return Messages.get(this, "skeleton_desc");
			case REMAINS:
				return Messages.get(this, "remains_desc");
			case HARD_TOMB:
				return Messages.get(this, "hardtomb_desc");
			default:
				return peek().info();
		}
	}

	private static final String POS = "pos";
	private static final String SEEN = "seen";
	private static final String TYPE = "type";
	private static final String ITEMS = "items";

	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle(Bundle bundle) {
		pos = bundle.getInt(POS);
		seen = bundle.getBoolean(SEEN);
		type = Type.valueOf(bundle.getString(TYPE));
		items = new LinkedList<Item>(
				(Collection<Item>) ((Collection<?>) bundle.getCollection(ITEMS)));
		items.removeAll(Collections.singleton(null));
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(POS, pos);
		bundle.put(SEEN, seen);
		bundle.put(TYPE, type.toString());
		bundle.put(ITEMS, items);
	}

}
