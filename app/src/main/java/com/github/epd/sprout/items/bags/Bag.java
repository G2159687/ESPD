
package com.github.epd.sprout.items.bags;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Iterator;

public class Bag extends Item implements Iterable<Item> {

	public static final String AC_OPEN = Messages.get(Bag.class, "ac_open");

	{
		image = 11;
		unique = true;
		defaultAction = AC_OPEN;
	}

	public Char owner;

	public ArrayList<Item> items = new ArrayList<Item>();

	public int size = 1;

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_OPEN)) {

			GameScene.show(new WndBag(this, null, WndBag.Mode.ALL, null));

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public boolean collect(Bag container) {
		if (super.collect(container)) {

			owner = container.owner;

			for (Item item : container.items.toArray(new Item[0])) {
				if (grab(item)) {
					item.detachAll(container);
					item.collect(this);
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDetach() {
		this.owner = null;
		for (Item item : items)
			Dungeon.quickslot.clearItem(item);
		updateQuickslot();
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	public void clear() {
		items.clear();
	}

	private static final String ITEMS = "inventory";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ITEMS, items);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		for (Bundlable item : bundle.getCollection(ITEMS)) {
			if (item != null)
				((Item) item).collect(this);
		}
	}

	public boolean contains(Item item) {
		for (Item i : items) {
			if (i == item) {
				return true;
			} else if (i instanceof Bag && ((Bag) i).contains(item)) {
				return true;
			}
		}
		return false;
	}

	public boolean grab(Item item) {
		return false;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}

	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		private Iterator<Item> nested = null;

		@Override
		public boolean hasNext() {
			if (nested != null) {
				return nested.hasNext() || index < items.size();
			} else {
				return index < items.size();
			}
		}

		@Override
		public Item next() {
			if (nested != null && nested.hasNext()) {

				return nested.next();

			} else {

				nested = null;

				Item item = items.get(index++);
				if (item instanceof Bag) {
					nested = ((Bag) item).iterator();
				}

				return item;
			}
		}

		@Override
		public void remove() {
			if (nested != null) {
				nested.remove();
			} else {
				items.remove(index);
			}
		}
	}
}
