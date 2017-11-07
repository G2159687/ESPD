
package com.github.epd.sprout.ui;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;

public class LootIndicator extends Tag {

	private ItemSlot slot;

	private Item lastItem = null;
	private int lastQuantity = 0;

	public LootIndicator() {
		super(0x1F75CC);

		setSize(24, 24);

		visible = false;
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		slot = new ItemSlot() {
			@Override
			protected void onClick() {
				if (Dungeon.hero.handle(Dungeon.hero.pos)) {
					Dungeon.hero.next();
				}

			}
		};
		slot.showParams(true, false, false);
		add(slot);
	}

	@Override
	protected void layout() {
		super.layout();

		slot.setRect(x + 2, y + 3, width - 2, height - 6);
	}

	@Override
	public void update() {

		if (Dungeon.hero.ready) {
			Heap heap = Dungeon.level.heaps.get(Dungeon.hero.pos);
			if (heap != null) {

				Item item = heap.type == Heap.Type.CHEST
						|| heap.type == Heap.Type.MIMIC ? ItemSlot.CHEST
						: heap.type == Heap.Type.LOCKED_CHEST ? ItemSlot.LOCKED_CHEST
						: heap.type == Heap.Type.HARD_TOMB ? ItemSlot.GRAVE
						: heap.type == Heap.Type.CRYSTAL_CHEST ? ItemSlot.CRYSTAL_CHEST
						: heap.type == Heap.Type.TOMB ? ItemSlot.TOMB
						: heap.type == Heap.Type.SKELETON ? ItemSlot.SKELETON
						: heap.type == Heap.Type.REMAINS ? ItemSlot.REMAINS
						: heap.peek();
				if (item != lastItem || item.quantity() != lastQuantity) {
					lastItem = item;
					lastQuantity = item.quantity();

					slot.item(item);
					flash();
				}
				visible = true;

			} else {

				lastItem = null;
				visible = false;

			}
		}

		slot.enable(visible && Dungeon.hero.ready);

		super.update();
	}
}
