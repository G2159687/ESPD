package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;

public class PinCushion extends Buff {

	private ArrayList<MissileWeapon> items = new ArrayList<>();

	public void stick(MissileWeapon projectile){
		for (Item item : items){
			if (item.isSimilar(projectile)){
				item.quantity(item.quantity() + projectile.quantity());
				return;
			}
		}
		items.add(projectile);
	}

	@Override
	public void detach() {
		for (Item item : items)
			Dungeon.level.drop(item, target.pos).sprite.drop();
		super.detach();
	}

	private static final String ITEMS = "items";

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(ITEMS, items);
		super.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		items = new ArrayList<>(
				(Collection<MissileWeapon>) ((Collection<?>) bundle
						.getCollection(ITEMS)));
		super.restoreFromBundle(bundle);
	}

}
