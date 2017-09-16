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
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.npcs.NPC;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokoban;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanBlack;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanCorner;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanStop;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanSwitch;
import com.github.epd.sprout.actors.mobs.npcs.Shopkeeper;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.effects.Pushing;
import com.github.epd.sprout.items.Dewdrop;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Heap.Type;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.potions.PotionOfStrength;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfTelekinesis extends Wand {

	private static final String TXT_YOU_NOW_HAVE = Messages.get(WandOfTelekinesis.class, "have");

	{
		name = Messages.get(this, "name");
		collisionProperties = Ballistica.STOP_CHARS | Ballistica.STOP_SOLID;
		image = ItemSpriteSheet.WAND_TELEKINESIS;
	}

	private static final String TXT_PREVENTING = Messages.get(WandOfTelekinesis.class, "prevent");

	@Override
	protected void onZap(Ballistica bolt) {

		boolean mapUpdated = false;

		int maxDistance = level() + 4;
		bolt.dist = Math.min(bolt.dist, maxDistance);

		Char ch;
		Heap heap = null;

		for (int c : bolt.subPath(1, bolt.dist)) {
			int before = Dungeon.level.map[c];

			if ((ch = Actor.findChar(c)) != null) {

				int next = bolt.path.get(bolt.dist + 1);
				if ((Level.passable[next] || Level.avoid[next]) && Actor.findChar(next) == null) {

					//Sokoban
					if ((ch instanceof SheepSokoban || ch instanceof SheepSokobanCorner || ch instanceof SheepSokobanStop || ch instanceof SheepSokobanSwitch || ch instanceof SheepSokobanBlack)
							&& (Dungeon.level.map[next] == Terrain.FLEECING_TRAP || Dungeon.level.map[next] == Terrain.CHANGE_SHEEP_TRAP)) {
					} else {
						Actor.addDelayed(new Pushing(ch, ch.pos, next), -1f);
					}

					ch.pos = next;
					Actor.freeCell(next);

					if (ch instanceof Shopkeeper) {
					}

					if (ch instanceof SheepSokoban ||
							ch instanceof SheepSokobanCorner ||
							ch instanceof SheepSokobanStop ||
							ch instanceof SheepSokobanSwitch ||
							ch instanceof SheepSokobanBlack) {
						Dungeon.level.mobPress((NPC) ch);
					} else if (ch instanceof Mob) {
						Dungeon.level.mobPress((Mob) ch);

					} else {
						Dungeon.level.press(ch.pos, ch);
					}

				} else {
					ch.damage(maxDistance - 1 - bolt.dist, this);

				}
			}

			if (heap == null && (heap = Dungeon.level.heaps.get(c)) != null) {
				switch (heap.type) {
					case HEAP:
						transport(heap);
						break;
					case CHEST:
						open(heap);
						break;
					default:
				}
			}

			Dungeon.level.press(c, null);
			if (before == Terrain.OPEN_DOOR && Actor.findChar(c) == null) {

				Level.set(c, Terrain.DOOR);
				GameScene.updateMap(c);

			} else if (Level.water[c]) {

				GameScene.ripple(c);

			}

			if (!mapUpdated && Dungeon.level.map[c] != before) {
				mapUpdated = true;
			}
		}

		if (mapUpdated) {
			Dungeon.observe();
			GameScene.updateFog();
		}
	}

	private void transport(Heap heap) {

		if (Dungeon.depth > 50) {
			GLog.w(TXT_PREVENTING);
			Invisibility.dispel();
			return;
		}

		Item item = heap.pickUp();
		if (item.doPickUp(curUser)) {

			if (item instanceof Dewdrop) {

			} else {

				if ((item instanceof ScrollOfUpgrade && ((ScrollOfUpgrade) item)
						.isKnown())
						|| (item instanceof PotionOfStrength && ((PotionOfStrength) item)
						.isKnown())) {
					GLog.p(TXT_YOU_NOW_HAVE, item.name());
				} else {
					GLog.i(TXT_YOU_NOW_HAVE, item.name());
				}
			}

		} else {
			Dungeon.level.drop(item, curUser.pos).sprite.drop();
		}
	}

	private void open(Heap heap) {
		heap.type = Type.HEAP;
		heap.sprite.link();
		heap.sprite.drop();
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.force(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
