
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ShopkeeperSprite;
import com.github.epd.sprout.windows.WndBag;
import com.github.epd.sprout.windows.WndTradeItem;

public class Shopkeeper extends NPC {

	public static final String TXT_THIEF = Messages.get(Shopkeeper.class, "thief");

	{
		name = Messages.get(Shopkeeper.class, "name");
		spriteClass = ShopkeeperSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	@Override
	protected boolean act() {

		throwItem();

		sprite.turnTo(pos, Dungeon.hero.pos);
		spend(TICK);
		return true;
	}

	@Override
	public void damage(int dmg, Object src) {
		//flee();
	}

	@Override
	public void add(Buff buff) {
		//flee();
	}

	public void flee() {
		for (Heap heap : Dungeon.level.heaps.values()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
				heap.destroy();
			}
		}

		destroy();

		sprite.killAndErase();
		CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public String description() {
		return Messages.get(Shopkeeper.class, "desc");
	}

	public static WndBag sell() {
		return GameScene.selectItem(itemSelector, WndBag.Mode.FOR_SALE,
				Messages.get(Shopkeeper.class, "sell"));
	}

	private static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show(new WndTradeItem(item, parentWnd));
			}
		}
	};

	@Override
	public boolean interact() {
		sell();
		return false;
	}
}
