
package com.github.epd.sprout.items.bags;

import com.github.epd.sprout.items.bombs.ActiveMrDestructo;
import com.github.epd.sprout.items.bombs.ActiveMrDestructo2;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.items.bombs.ClusterBomb;
import com.github.epd.sprout.items.bombs.DizzyBomb;
import com.github.epd.sprout.items.bombs.HolyHandGrenade;
import com.github.epd.sprout.items.bombs.InactiveMrDestructo;
import com.github.epd.sprout.items.bombs.InactiveMrDestructo2;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.bombs.OrbOfZot;
import com.github.epd.sprout.items.bombs.SeekingBombItem;
import com.github.epd.sprout.items.bombs.SeekingClusterBombItem;
import com.github.epd.sprout.items.bombs.SmartBomb;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class ScrollHolder extends Bag {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.HOLDER;

		size = 24;
	}

	@Override
	public boolean grab(Item item) {
		return item instanceof Scroll
				|| item instanceof Bomb
				|| item instanceof DizzyBomb
				|| item instanceof SmartBomb
				|| item instanceof SeekingBombItem
				|| item instanceof ClusterBomb
				|| item instanceof SeekingClusterBombItem
				|| item instanceof ActiveMrDestructo
				|| item instanceof ActiveMrDestructo2
				|| item instanceof InactiveMrDestructo
				|| item instanceof InactiveMrDestructo2
				|| item instanceof OrbOfZot
				|| item instanceof HolyHandGrenade;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
