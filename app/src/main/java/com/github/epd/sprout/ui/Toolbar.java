
package com.github.epd.sprout.ui;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
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
	private Tool btnInventory;
	private QuickslotTool[] btnQuick;

	private PickedUpItem pickedUp;

	private boolean lastEnabled = true;
	public boolean examining = false;

	private static Toolbar instance;

	public enum Mode {
		SPLIT,
		GROUP,
		CENTER
	}

	public Toolbar() {
		super();

		instance = this;

		height = btnInventory.height();
	}

	@Override
	protected void createChildren() {

		add(btnWait = new Tool(24, 0, 20, 26) {
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

		add(btnSearch = new Tool(44, 0, 20, 26) {
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

		btnQuick = new QuickslotTool[4];

		add(btnQuick[3] = new QuickslotTool(64, 0, 22, 24, 3));

		add(btnQuick[2] = new QuickslotTool(64, 0, 22, 24, 2));

		add(btnQuick[1] = new QuickslotTool(64, 0, 22, 24, 1));

		add(btnQuick[0] = new QuickslotTool(64, 0, 22, 24, 0));

		add(btnInventory = new Tool(0, 0, 24, 26) {
			private GoldIndicator gold;

			@Override
			protected void onClick() {
				GameScene.show(new WndBag(Dungeon.hero.belongings.backpack, null, WndBag.Mode.ALL, null));
			}

			protected boolean onLongClick() {
				GameScene.show(new WndCatalogus());
				return true;
			}

			;

			@Override
			protected void createChildren() {
				super.createChildren();
				gold = new GoldIndicator();
				add(gold);
			}

			;

			@Override
			protected void layout() {
				super.layout();
				gold.fill(this);
			}

			;
		});

		add(pickedUp = new PickedUpItem());
	}

	@Override
	protected void layout() {

		int[] visible = new int[4];
		int slots = ShatteredPixelDungeon.quickSlots();

		for (int i = 0; i <= 3; i++)
			visible[i] = (int) ((slots > i) ? y + 2 : y + 25);

		for (int i = 0; i <= 3; i++) {
			btnQuick[i].visible = btnQuick[i].active = slots > i;
			//decides on quickslot layout, depending on available screen size.
			if (slots == 4 && width < 150) {
				if (width < 139) {
					if ((ShatteredPixelDungeon.flipToolbar() && i == 3) ||
							(!ShatteredPixelDungeon.flipToolbar() && i == 0)) {
						btnQuick[i].border(0, 0);
						btnQuick[i].frame(88, 0, 17, 24);
					} else {
						btnQuick[i].border(0, 1);
						btnQuick[i].frame(88, 0, 18, 24);
					}
				} else {
					if (i == 0 && !ShatteredPixelDungeon.flipToolbar() ||
							i == 3 && ShatteredPixelDungeon.flipToolbar()) {
						btnQuick[i].border(0, 2);
						btnQuick[i].frame(106, 0, 19, 24);
					} else if (i == 0 && ShatteredPixelDungeon.flipToolbar() ||
							i == 3 && !ShatteredPixelDungeon.flipToolbar()) {
						btnQuick[i].border(2, 1);
						btnQuick[i].frame(86, 0, 20, 24);
					} else {
						btnQuick[i].border(0, 1);
						btnQuick[i].frame(88, 0, 18, 24);
					}
				}
			} else {
				btnQuick[i].border(2, 2);
				btnQuick[i].frame(64, 0, 22, 24);
			}

		}

		float right = width;
		switch (Mode.valueOf(ShatteredPixelDungeon.toolbarMode())) {
			case SPLIT:
				btnWait.setPos(x, y);
				btnSearch.setPos(btnWait.right(), y);

				btnInventory.setPos(right - btnInventory.width(), y);

				btnQuick[0].setPos(btnInventory.left() - btnQuick[0].width(), visible[0]);
				btnQuick[1].setPos(btnQuick[0].left() - btnQuick[1].width(), visible[1]);
				btnQuick[2].setPos(btnQuick[1].left() - btnQuick[2].width(), visible[2]);
				btnQuick[3].setPos(btnQuick[2].left() - btnQuick[3].width(), visible[3]);
				break;

			//center = group but.. well.. centered, so all we need to do is pre-emptively set the right side further in.
			case CENTER:
				right = width - (width - btnWait.width() - btnSearch.width() - btnInventory.width() -
						btnQuick[0].width() - btnQuick[1].width() - btnQuick[2].width() - btnQuick[3].width()) / 2;

			case GROUP:
				btnWait.setPos(right - btnWait.width(), y);
				btnSearch.setPos(btnWait.left() - btnSearch.width(), y);
				btnInventory.setPos(btnSearch.left() - btnInventory.width(), y);

				btnQuick[0].setPos(btnInventory.left() - btnQuick[0].width(), visible[0]);
				btnQuick[1].setPos(btnQuick[0].left() - btnQuick[1].width(), visible[1]);
				btnQuick[2].setPos(btnQuick[1].left() - btnQuick[2].width(), visible[2]);
				btnQuick[3].setPos(btnQuick[2].left() - btnQuick[3].width(), visible[3]);
				break;
		}
		right = width;

		if (ShatteredPixelDungeon.flipToolbar()) {

			btnWait.setPos((right - btnWait.right()), y);
			btnSearch.setPos((right - btnSearch.right()), y);
			btnInventory.setPos((right - btnInventory.right()), y);

			for (int i = 0; i <= 3; i++) {
				btnQuick[i].setPos(right - btnQuick[i].right(), visible[i]);
			}

		}

	}

	public static void updateLayout() {
		if (instance != null) instance.layout();
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

		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable(true);
		}
	}

	public void pickup(Item item) {
		pickedUp.reset(item, btnInventory.centerX(), btnInventory.centerY());
	}

	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer cell) {

			instance.examining = false;

			if (cell == null) {
				return;
			}

			if (cell < 0
					|| cell > Dungeon.level.getLength()
					|| (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
				GameScene.show(new WndMessage(Messages.get(Toolbar.class, "dont_know")));
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
				names.add(Messages.titleCase(heap.toString()));
			}

			Plant plant = Dungeon.level.plants.get(cell);
			if (plant != null) {
				objects.add(plant);
				names.add(Messages.titleCase(plant.plantName));
			}

			if (objects.isEmpty()) {
				GameScene.show(new WndInfoCell(cell));
			} else if (objects.size() == 1) {
				examineObject(objects.get(0));
			} else {
				GameScene.show(new WndOptions(Messages.get(GameScene.class, "choose_examine"),
						Messages.get(GameScene.class, "multiple_examine"), names.toArray(new String[names.size()])) {
					@Override
					protected void onSelect(int index) {
						examineObject(objects.get(index));
					}
				});
			}
		}

		public void examineObject(Object o) {
			if (o == Dungeon.hero) {
				GameScene.show(new WndHero());
			} else if (o instanceof Mob) {
				GameScene.show(new WndInfoMob((Mob) o));
			} else if (o instanceof Heap) {
				Heap heap = (Heap) o;
				if (heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0) {
					GameScene.show(new WndTradeItem(heap, false));
				} else {
					GameScene.show(new WndInfoItem(heap));
				}
			} else if (o instanceof Plant) {
				GameScene.show(new WndInfoPlant((Plant) o));
			} else {
				GameScene.show(new WndMessage(Messages.get(GameScene.class, "dont_know")));
			}
		}

		@Override
		public String prompt() {
			return Messages.get(Toolbar.class, "examine_prompt");
		}
	};

	private static class Tool extends Button {

		private static final int BGCOLOR = 0x7B8073;

		private Image base;

		public Tool(int x, int y, int width, int height) {
			super();

			frame(x, y, width, height);
		}

		public void frame(int x, int y, int width, int height) {
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
		private int borderLeft = 2;
		private int borderRight = 2;

		public QuickslotTool(int x, int y, int width, int height, int slotNum) {
			super(x, y, width, height);

			slot = new QuickSlotButton(slotNum);
			add(slot);
		}

		public void border(int left, int right) {
			borderLeft = left;
			borderRight = right;
			layout();
		}

		@Override
		protected void layout() {
			super.layout();
			slot.setRect(x + borderLeft, y + 2, width - borderLeft - borderRight, height - 4);
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
