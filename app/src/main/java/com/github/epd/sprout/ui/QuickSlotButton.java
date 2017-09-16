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
package com.github.epd.sprout.ui;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.keys.Key;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.items.weapon.missiles.JupitersWraith;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.PathFinder;

public class QuickSlotButton extends Button implements WndBag.Listener {

	private static final String TXT_SELECT_ITEM = Messages.get(QuickSlotButton.class, "select_item");
	public static final String TXT_CHARGE = "%d%%";

	private static QuickSlotButton[] instance = new QuickSlotButton[4];
	private int slotNum;

	private ItemSlot slot;

	private static Image crossB;
	private static Image crossM;

	private static boolean targeting = false;
	public static Char lastTarget = null;

	public QuickSlotButton(int slotNum) {
		super();
		this.slotNum = slotNum;
		item(select(slotNum));

		instance[slotNum] = this;
	}

	@Override
	public void destroy() {
		super.destroy();

		reset();
	}

	public static void reset() {
		instance = new QuickSlotButton[4];

		lastTarget = null;
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		slot = new ItemSlot() {
			@Override
			protected void onClick() {
				if (targeting) {
					int cell = autoAim(lastTarget);
					if (cell != -1) {
						GameScene.handleCell(cell);
					} else {
						GameScene.handleCell(lastTarget.pos);
					}

				} else {
					Item item = select(slotNum);
					if (item.usesTargeting)
						useTargeting();
					item.execute(Dungeon.hero);
				}
			}

			@Override
			protected boolean onLongClick() {
				return QuickSlotButton.this.onLongClick();
			}

			@Override
			protected void onTouchDown() {
				icon.lightness(0.7f);
			}

			@Override
			protected void onTouchUp() {
				icon.resetColor();
			}

			@Override
			public void item(Item item) {
				if (item == null) {

					active = false;
					icon.visible = topLeft.visible = topRight.visible = bottomRight.visible = false;
					if (bottomRightIcon != null) remove(bottomRightIcon);

				} else {

					active = true;
					icon.visible = topLeft.visible = topRight.visible = bottomRight.visible = true;

					icon.view(item.image(), item.glowing());

					topLeft.text(item.status());

					if (item instanceof RelicMeleeWeapon) {
						topLeft.text(Utils.format(TXT_CHARGE, Math.round(100 * ((RelicMeleeWeapon) item).charge / ((RelicMeleeWeapon) item).chargeCap)));
						topLeft.measure();
					} else if (item instanceof JupitersWraith) {
						topLeft.text(Utils.format(TXT_CHARGE, Math.round(100 * ((JupitersWraith) item).charge / ((JupitersWraith) item).chargeCap)));
						topLeft.measure();
					} else if (item instanceof Key && !(item instanceof SkeletonKey)) {
						topRight.text(Utils.format(TXT_KEY_DEPTH, ((Key) item).depth));
						topRight.measure();
					} else {

						topRight.text(null);

					}

					int level = item.visiblyUpgraded();

					if (level != 0) {
						bottomRight.text(item.levelKnown ? Utils.format(TXT_LEVEL,
								level) : TXT_CURSED);
						bottomRight.measure();
						bottomRight.hardlight(level > 0 ? UPGRADED : DEGRADED);
					} else {
						bottomRight.text(null);
					}

					Integer iconInt;
					if (bottomRightIcon != null) remove(bottomRightIcon);
					if (item instanceof Scroll) {
						iconInt = ((Scroll) item).initials();
					} else if (item instanceof Potion) {
						iconInt = ((Potion) item).initials();
					} else {
						iconInt = null;
					}
					if (iconInt != null && iconVisible) {
						bottomRightIcon = new Image(Assets.CONS_ICONS);
						int left = iconInt * 7;
						int top = item instanceof Potion ? 0 : 8;
						bottomRightIcon.frame(left, top, 7, 8);
						add(bottomRightIcon);
					}

					layout();
				}
			}

		};
		slot.showParams(true, false, true);
		add(slot);

		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add(crossB);

		crossM = new Image();
		crossM.copy(crossB);
	}

	@Override
	protected void layout() {
		super.layout();

		slot.fill(this);

		crossB.x = PixelScene.align(x + (width - crossB.width) / 2);
		crossB.y = PixelScene.align(y + (height - crossB.height) / 2);
	}

	@Override
	protected void onClick() {
		GameScene.selectItem(this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM);
	}

	@Override
	protected boolean onLongClick() {
		GameScene.selectItem(this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM);
		return true;
	}

	private static Item select(int slotNum) {
		return Dungeon.quickslot.getItem(slotNum);
	}

	@Override
	public void onSelect(Item item) {
		if (item != null) {
			Dungeon.quickslot.setSlot(slotNum, item);
			refresh();
		}
	}

	public void item(Item item) {
		slot.item(item);
		enableSlot();
	}

	public void enable(boolean value) {
		active = value;
		if (value) {
			enableSlot();
		} else {
			slot.enable(false);
		}
	}

	private void enableSlot() {
		slot.enable(Dungeon.quickslot.isNonePlaceholder(slotNum));
	}

	private void useTargeting() {

		targeting = lastTarget != null && lastTarget.isAlive()
				&& Dungeon.visible[lastTarget.pos];

		if (targeting) {
			if (Actor.all().contains(lastTarget)) {
				lastTarget.sprite.parent.add(crossM);
				crossM.point(DungeonTilemap.tileToWorld(lastTarget.pos));
				crossB.x = PixelScene.align(x + (width - crossB.width) / 2);
				crossB.y = PixelScene.align(y + (height - crossB.height) / 2);
				crossB.visible = true;
			} else {
				lastTarget = null;
			}
		}
	}

	public static int autoAim(Char target) {
		if (new Ballistica(Dungeon.hero.pos, target.pos, Ballistica.PROJECTILE).collisionPos == target.pos) {
			return target.pos;
		}
		for (int i : PathFinder.NEIGHBOURS9DIST2) {
			if (new Ballistica(Dungeon.hero.pos, target.pos + i, Ballistica.PROJECTILE).collisionPos == target.pos) {
				return target.pos + i;
			}
		}
		return -1;
	}

	public static void refresh() {
		for (int i = 0; i < instance.length; i++) {
			if (instance[i] != null) {
				instance[i].item(select(i));
			}
		}
	}

	public static void target(Char target) {
		if (target != Dungeon.hero) {
			lastTarget = target;

			HealthIndicator.instance.target(target);
		}
	}

	public static void cancel() {
		if (targeting) {
			crossB.visible = false;
			crossM.remove();
			targeting = false;
		}
	}
}
