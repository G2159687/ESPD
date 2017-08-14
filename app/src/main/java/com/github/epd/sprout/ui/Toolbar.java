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
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.scenes.CellSelector;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.windows.WndBag;
import com.github.epd.sprout.windows.WndCatalogus;
import com.github.epd.sprout.windows.WndHero;
import com.github.epd.sprout.windows.WndInfoCell;
import com.github.epd.sprout.windows.WndInfoItem;
import com.github.epd.sprout.windows.WndInfoMob;
import com.github.epd.sprout.windows.WndInfoPlant;
import com.github.epd.sprout.windows.WndMessage;
import com.github.epd.sprout.windows.WndOptions;
import com.github.epd.sprout.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Locale;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnSearch;
	//private Tool btnInfo;
	//private Tool btnResume;
	private Tool btnInventory;
	private Tool btnQuick;
	private Tool btnQuick2;
	private Tool btnQuick3;
	private Tool btnQuick4;
	public boolean examining = false;

	public static int QuickSlots;
	private PickedUpItem pickedUp;
	private boolean lastEnabled = true;

	public Toolbar() {
		super();

		QuickSlots = ShatteredPixelDungeon.quickSlots();

		height = btnInventory.height();
	}
	
	

	@Override
	protected void createChildren() {

		add(btnWait = new Tool(0, 7, 20, 24) {
			@Override
			protected void onClick() {
				examining = false;
				Dungeon.hero.rest(false);
			}

			protected boolean onLongClick() {
				examining = false;
				Dungeon.hero.rest(true);
				return true;
			}
		});
		
		add(btnSearch = new Tool(20, 7, 20, 24) {
			@Override
			protected void onClick() {
				if (!examining) {
					GameScene.selectCell(informer);
					examining = true;
				} else {
					informer.onSelect(null);
					Dungeon.hero.search(true);
				}
			}
		});

	//	add(btnInfo = new Tool(40, 7, 21, 24) {
	//		@Override
	//		protected void onClick() {
	//			GameScene.selectCell(informer);
	//		}

	//		@Override
	//		protected boolean onLongClick() {
	//			Dungeon.hero.rest(true);
	//			return true;
	//		}

	//	});

		/*
		 * add( btnResume = new Tool( 61, 7, 21, 24 ) {
		 * 
		 * @Override protected void onClick() { Dungeon.hero.resume(); } } );
		 */

		add(btnInventory = new Tool(82, 7, 23, 24) {
			private GoldIndicator gold;

			@Override
			protected void onClick() {
				GameScene.show(new WndBag(Dungeon.hero.belongings.backpack,
						null, WndBag.Mode.ALL, null));
			}

			@Override
			protected boolean onLongClick() {
				GameScene.show(new WndCatalogus());
				return true;
			}

			@Override
			protected void createChildren() {
				super.createChildren();
				gold = new GoldIndicator();
				add(gold);
			}

			@Override
			protected void layout() {
				super.layout();
				gold.fill(this);
			}
		});

		add(btnQuick = new QuickslotTool(105, 7, 22, 24, 0));

		btnQuick2 = new QuickslotTool(105, 7, 22, 24, 1);

		btnQuick3 = new QuickslotTool(105, 7, 22, 24, 2);

		btnQuick4 = new QuickslotTool(105, 7, 22, 24, 3);

		add(pickedUp = new PickedUpItem());
		

	}

	@Override
	protected void layout() {
		btnWait.setPos(x, y);
		btnSearch.setPos(btnWait.right(), y);
		//btnInfo.setPos(btnSearch.right(), y);
		// btnResume.setPos( btnInfo.right(), y );
		btnQuick.setPos(width - btnQuick.width(), y);
		btnQuick2.setPos(btnQuick.left() - btnQuick2.width(), y);
		btnQuick3.setPos(btnQuick2.left() - btnQuick3.width(), y);
		btnQuick4.setPos(btnQuick3.left() - btnQuick4.width(), y);
		if (QuickSlots == 4) {
			add(btnQuick2);
			btnQuick2.visible = btnQuick2.active = true;
			add (btnQuick3);
			btnQuick3.visible = btnQuick3.active = true;
			add (btnQuick4);
			btnQuick4.visible = btnQuick4.active = true;
			btnInventory.setPos(btnQuick4.left() - btnInventory.width(), y);
		}
		else if (QuickSlots == 3) {
			remove(btnQuick4);
			add(btnQuick2);
			btnQuick2.visible = btnQuick2.active = true;
			add (btnQuick3);
			btnQuick3.visible = btnQuick3.active = true;
			btnQuick4.visible = btnQuick4.active = false;
			btnInventory.setPos(btnQuick3.left() - btnInventory.width(), y);
		}
		else if (QuickSlots == 2) {
			add(btnQuick2);
			remove(btnQuick3);
			remove(btnQuick4);
			btnQuick2.visible = btnQuick2.active = true;
			btnQuick3.visible = btnQuick3.active = false;
			btnQuick4.visible = btnQuick4.active = false;
			btnInventory.setPos(btnQuick2.left() - btnInventory.width(), y);
		} else {
			remove(btnQuick2);
			remove(btnQuick3);
			remove(btnQuick4);
			btnQuick2.visible = btnQuick2.active = false;
			btnQuick3.visible = btnQuick3.active = false;
			btnQuick4.visible = btnQuick4.active = false;
			btnInventory.setPos(btnQuick.left() - btnInventory.width(), y);
		}
	}

	@Override
	public void update() {
		super.update();

		if (lastEnabled != Dungeon.hero.ready) {
			lastEnabled = Dungeon.hero.ready;

			for (Gizmo tool : members) {
				if (tool instanceof Tool) {
					((Tool) tool).enable(lastEnabled);
				}
			}
		}

		// btnResume.visible = Dungeon.hero.lastAction != null;

		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable(true);
		}

		// If we have 2 slots, and 2nd one isn't visible, or we have 1, and 2nd
		// one is visible...
		if ((QuickSlots == 1) == btnQuick2.visible) {
			layout();
		}
	}

	public void pickup(Item item) {
		pickedUp.reset(item, btnInventory.centerX(), btnInventory.centerY());
	}

	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer cell) {

			if (cell == null) {
				return;
			}

			if (cell < 0
					|| cell > Level.getLength()
					|| (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
				GameScene.show(new WndMessage(Messages.get(Toolbar.class,"dont_know")));
				return;
			}

			if (!Dungeon.visible[cell]) {
				GameScene.show(new WndInfoCell(cell));
				return;
			}
			ArrayList<String> names = new ArrayList<>();
			final ArrayList<Object> objects = new ArrayList<>();

			if (cell == Dungeon.hero.pos) {
				objects.add(Dungeon.hero);
				names.add(Dungeon.hero.className().toUpperCase(Locale.ENGLISH));
			} else {
				if (Dungeon.visible[cell]) {
					Mob mob = (Mob) Actor.findChar(cell);
					if (mob != null) {
						objects.add(mob);
						names.add(Messages.titleCase(mob.name));
					}
				}
			}

			Heap heap = Dungeon.level.heaps.get(cell);
			if (heap != null) {
				objects.add(heap);
				names.add(Messages.titleCase( heap.toString()));
			}

			Plant plant = Dungeon.level.plants.get(cell);
			if (plant != null) {
				objects.add(plant);
				names.add(Messages.titleCase( plant.plantName ));
			}

			if (objects.isEmpty()) {
				GameScene.show(new WndInfoCell(cell));
			} else if (objects.size() == 1){
				examineObject(objects.get(0));
			} else {
				GameScene.show(new WndOptions(Messages.get(GameScene.class, "choose_examine"),
						Messages.get(GameScene.class, "multiple_examine"), names.toArray(new String[names.size()])){
					@Override
					protected void onSelect(int index) {
						examineObject(objects.get(index));
					}
				});
			}
		}

		public void examineObject(Object o){
			if (o == Dungeon.hero){
				GameScene.show( new WndHero() );
			} else if ( o instanceof Mob ){
				GameScene.show(new WndInfoMob((Mob) o));
			} else if ( o instanceof Heap ){
				Heap heap = (Heap)o;
				if (heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0) {
					GameScene.show(new WndTradeItem(heap, false));
				} else {
					GameScene.show(new WndInfoItem(heap));
				}
			} else if ( o instanceof Plant ){
				GameScene.show( new WndInfoPlant((Plant) o) );
			} else {
				GameScene.show( new WndMessage( Messages.get(GameScene.class, "dont_know") ) ) ;
			}
		}

		@Override
		public String prompt() {
			return Messages.get(Toolbar.class,"examine_prompt");
		}
	};

	private static class Tool extends Button {

		private static final int BGCOLOR = 0x7B8073;

		private Image base;

		public Tool(int x, int y, int width, int height) {
			super();

			base.frame(x, y, width, height);

			this.width = width;
			this.height = height;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			base = new Image(Assets.TOOLBAR);
			add(base);
		}

		@Override
		protected void layout() {
			super.layout();

			base.x = x;
			base.y = y;
		}

		@Override
		protected void onTouchDown() {
			base.brightness(1.4f);
		}

		@Override
		protected void onTouchUp() {
			if (active) {
				base.resetColor();
			} else {
				base.tint(BGCOLOR, 0.7f);
			}
		}

		public void enable(boolean value) {
			if (value != active) {
				if (value) {
					base.resetColor();
				} else {
					base.tint(BGCOLOR, 0.7f);
				}
				active = value;
			}
		}
	}

	private static class QuickslotTool extends Tool {

		private QuickSlotButton slot;

		public QuickslotTool(int x, int y, int width, int height, int slotNum) {
			super(x, y, width, height);

			slot = new QuickSlotButton(slotNum);
			add(slot);
		}

		@Override
		protected void layout() {
			super.layout();
			slot.setRect(x + 1, y + 2, width - 2, height - 2);
		}

		@Override
		public void enable(boolean value) {
			super.enable(value);
			slot.enable(value);
		}
	}

	private static class PickedUpItem extends ItemSprite {

		private static final float DISTANCE = DungeonTilemap.SIZE;
		private static final float DURATION = 0.2f;

		private float dstX;
		private float dstY;
		private float left;

		public PickedUpItem() {
			super();

			originToCenter();

			active = visible = false;
		}

		public void reset(Item item, float dstX, float dstY) {
			view(item);

			active = visible = true;

			this.dstX = dstX - ItemSprite.SIZE / 2;
			this.dstY = dstY - ItemSprite.SIZE / 2;
			left = DURATION;

			x = this.dstX - DISTANCE;
			y = this.dstY - DISTANCE;
			alpha(1);
		}

		@Override
		public void update() {
			super.update();

			if ((left -= Game.elapsed) <= 0) {

				visible = active = false;

			} else {
				float p = left / DURATION;
				scale.set((float) Math.sqrt(p));
				float offset = DISTANCE * p;
				x = dstX - offset;
				y = dstY - offset;
			}
		}
	}
}
