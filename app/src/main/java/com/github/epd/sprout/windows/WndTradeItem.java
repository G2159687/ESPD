
package com.github.epd.sprout.windows;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.npcs.Shopkeeper;
import com.github.epd.sprout.items.EquipableItem;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.ItemSlot;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;

public class WndTradeItem extends Window {

	private static final float GAP = 2;
	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 16;

	private static final String TXT_SALE = Messages.get(WndTradeItem.class, "sale");
	private static final String TXT_BUY = Messages.get(WndTradeItem.class, "buy");
	private static final String TXT_STEAL = Messages.get(WndTradeItem.class, "steal");
	private static final String TXT_SELL = Messages.get(WndTradeItem.class, "sell");
	private static final String TXT_SELL_1 = Messages.get(WndTradeItem.class, "sell1");
	private static final String TXT_SELL_ALL = Messages.get(WndTradeItem.class, "sellall");
	private static final String TXT_CANCEL = Messages.get(WndTradeItem.class, "cancel");

	private static final String TXT_SOLD = Messages.get(WndTradeItem.class, "sold");
	private static final String TXT_BOUGHT = Messages.get(WndTradeItem.class, "bought");
	private static final String TXT_STOLE = Messages.get(WndTradeItem.class, "stole");

	private WndBag owner;

	public WndTradeItem(final Item item, WndBag owner) {

		super();

		this.owner = owner;

		float pos = createDescription(item, false);

		if (item.quantity() == 1) {

			NewRedButton btnSell = new NewRedButton(Utils.format(TXT_SELL,
					item.price())) {
				@Override
				protected void onClick() {
					sell(item);
					hide();
				}
			};
			btnSell.setRect(0, pos + GAP, WIDTH, BTN_HEIGHT);
			add(btnSell);

			pos = btnSell.bottom();

		} else {

			int priceAll = item.price();
			NewRedButton btnSell1 = new NewRedButton(Utils.format(TXT_SELL_1,
					priceAll / item.quantity())) {
				@Override
				protected void onClick() {
					sellOne(item);
					hide();
				}
			};
			btnSell1.setRect(0, pos + GAP, WIDTH, BTN_HEIGHT);
			add(btnSell1);
			NewRedButton btnSellAll = new NewRedButton(Utils.format(TXT_SELL_ALL,
					priceAll)) {
				@Override
				protected void onClick() {
					sell(item);
					hide();
				}
			};
			btnSellAll.setRect(0, btnSell1.bottom() + GAP, WIDTH, BTN_HEIGHT);
			add(btnSellAll);

			pos = btnSellAll.bottom();

		}

		NewRedButton btnCancel = new NewRedButton(TXT_CANCEL) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect(0, pos + GAP, WIDTH, BTN_HEIGHT);
		add(btnCancel);

		resize(WIDTH, (int) btnCancel.bottom());
	}

	public WndTradeItem(final Heap heap, boolean canBuy) {

		super();

		Item item = heap.peek();

		float pos = createDescription(item, true);

		final int price = price(item);

		if (canBuy) {

			NewRedButton btnBuy = new NewRedButton(Utils.format(TXT_BUY, price)) {
				@Override
				protected void onClick() {
					hide();
					buy(heap);
				}
			};
			btnBuy.setRect(0, pos + GAP, WIDTH, BTN_HEIGHT);
			btnBuy.enable(price <= Dungeon.gold);
			add(btnBuy);

			NewRedButton btnCancel = new NewRedButton(TXT_CANCEL) {
				@Override
				protected void onClick() {
					hide();
				}
			};

			final MasterThievesArmband.Thievery thievery = Dungeon.hero.buff(MasterThievesArmband.Thievery.class);
			if (thievery != null) {
				final float chance = thievery.stealChance(price);
				NewRedButton btnSteal = new NewRedButton(Utils.format(TXT_STEAL,
						Math.min(100, (int) (chance * 100)))) {
					@Override
					protected void onClick() {
						if (thievery.steal(price)) {
							Hero hero = Dungeon.hero;
							Item item = heap.pickUp();
							GLog.i(TXT_STOLE, item.name());
							hide();

							if (!item.doPickUp(hero)) {
								Dungeon.level.drop(item, heap.pos).sprite
										.drop();
							}
						} else {
							for (Mob mob : Dungeon.level.mobs) {
								if (mob instanceof Shopkeeper) {
									mob.yell(Shopkeeper.TXT_THIEF);
									((Shopkeeper) mob).flee();
									break;
								}
							}
							hide();
						}
					}
				};
				btnSteal.setRect(0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT);
				add(btnSteal);

				btnCancel
						.setRect(0, btnSteal.bottom() + GAP, WIDTH, BTN_HEIGHT);
			} else
				btnCancel.setRect(0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT);

			add(btnCancel);

			resize(WIDTH, (int) btnCancel.bottom());

		} else {

			resize(WIDTH, (int) pos);

		}
	}

	@Override
	public void hide() {

		super.hide();

		if (owner != null) {
			owner.hide();
			Shopkeeper.sell();
		}
	}

	private float createDescription(Item item, boolean forSale) {

		// Title
		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item));
		titlebar.label(forSale ? Utils.format(TXT_SALE, item.toString(),
				price(item)) : Utils.capitalize(item.toString()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		// Upgraded / degraded
		if (item.levelKnown && item.level > 0) {
			titlebar.color(ItemSlot.UPGRADED);
		} else if (item.levelKnown && item.level < 0) {
			titlebar.color(ItemSlot.DEGRADED);
		}

		// Description
		RenderedTextMultiline info = PixelScene.renderMultiline(item.info(), 6);
		info.maxWidth(WIDTH);
		info.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add(info);

		return info.bottom();

	}

	private void sell(Item item) {

		Hero hero = Dungeon.hero;

		if (item.isEquipped(hero)
				&& !((EquipableItem) item).doUnequip(hero, false)) {
			return;
		}
		item.detachAll(hero.belongings.backpack);

		int price = item.price();

		new Gold(price).doPickUp(hero);
		GLog.i(TXT_SOLD, item.name(), price);
	}

	private void sellOne(Item item) {

		if (item.quantity() <= 1) {
			sell(item);
		} else {

			Hero hero = Dungeon.hero;

			item = item.detach(hero.belongings.backpack);
			int price = item.price();

			new Gold(price).doPickUp(hero);
			GLog.i(TXT_SOLD, item.name(), price);
		}
	}

	private int price(Item item) {
		return Dungeon.isChallenged(Challenges.NO_ARMOR) ? 1 : item.price() * 5 * (Dungeon.depth / 5 + 1);
	}

	private void buy(Heap heap) {

		Hero hero = Dungeon.hero;
		Item item = heap.pickUp();

		int price = price(item);
		Dungeon.gold -= price;

		GLog.i(TXT_BOUGHT, item.name(), price);

		if (!item.doPickUp(hero)) {
			Dungeon.level.drop(item, heap.pos).sprite.drop();
		}
	}
}
