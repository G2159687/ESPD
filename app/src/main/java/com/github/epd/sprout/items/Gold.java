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
package com.github.epd.sprout.items;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Gold extends Item {

	private static final String TXT_COLLECT = Messages.get(Gold.class, "collect");
	private static final String TXT_INFO = Messages.get(Gold.class, "info")
			+ TXT_COLLECT;
	private static final String TXT_INFO_1 = Messages.get(Gold.class, "one") + TXT_COLLECT;
	private static final String TXT_VALUE = "%+d";

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.GOLD;
		stackable = true;
	}

	public Gold() {
		this(1);
	}

	public Gold(int value) {
		this.quantity = value;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		return new ArrayList<String>();
	}

	@Override
	public boolean doPickUp(Hero hero) {

		Dungeon.gold += quantity;

		MasterThievesArmband.Thievery thievery = hero.buff(MasterThievesArmband.Thievery.class);
		if (thievery != null)
			thievery.collect(quantity);

		GameScene.pickUp(this);
		hero.sprite.showStatus(CharSprite.NEUTRAL, TXT_VALUE, quantity);
		hero.spendAndNext(TIME_TO_PICK_UP);

		Sample.INSTANCE.play(Assets.SND_GOLD, 1, 1, Random.Float(0.9f, 1.1f));

		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		switch (quantity) {
			case 0:
				return TXT_COLLECT;
			case 1:
				return TXT_INFO_1;
			default:
				return Utils.format(TXT_INFO);
		}
	}

	@Override
	public Item random() {
		quantity = Random.Int(30 + Dungeon.depth * 10, 60 + Dungeon.depth * 20);
		return this;
	}

	private static final String VALUE = "value";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(VALUE, quantity);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		quantity = bundle.getInt(VALUE);
	}
}
