
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Journal;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.EquipableItem;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.teleporter.SanChikarah;
import com.github.epd.sprout.items.quest.SanChikarahDeath;
import com.github.epd.sprout.items.quest.SanChikarahLife;
import com.github.epd.sprout.items.quest.SanChikarahTranscend;
import com.github.epd.sprout.items.artifacts.Artifact;
import com.github.epd.sprout.items.quest.Anvil;
import com.github.epd.sprout.items.quest.DarkGold;
import com.github.epd.sprout.items.quest.Pickaxe;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.standard.BlacksmithRoom;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.BlacksmithSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBlacksmith;
import com.github.epd.sprout.windows.WndQuest;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Blacksmith extends NPC {

	{
		name = Messages.get(this, "name");
		spriteClass = BlacksmithSprite.class;

		properties.add(Property.IMMOVABLE);
	}


	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public boolean interact() {

		sprite.turnTo(pos, Dungeon.hero.pos);

		if (!Quest.given) {

			GameScene.show(new WndQuest(this, Quest.alternative ? Messages.get(Blacksmith.class, "bloodone")
					: Messages.get(Blacksmith.class, "goldone")) {

				@Override
				public void onBackPressed() {
					super.onBackPressed();

					Quest.given = true;
					Quest.completed = false;

					if (Quest.alternative) {
						Quest.placeItem();
					}

					Item.autocollect(new Pickaxe(), Dungeon.hero.pos);

				}
			});

			Journal.add(Journal.Feature.TROLL);

		} else if (!Quest.completed) {

			if (Quest.alternative) {

				Anvil dust = Dungeon.hero.belongings.getItem(Anvil.class);
				if (dust == null || dust.cursed) {
					tell(Messages.get(Blacksmith.class, "four"));
				} else {
					tell(Messages.get(this, "completed"));
					dust.detach(Dungeon.hero.belongings.backpack);

					Quest.completed = true;
					Quest.reforged = false;
				}

			} else {
				DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
				if (gold == null || gold.quantity() < 30) {
					tell(Messages.get(Blacksmith.class, "three"));
				} else {
					tell(Messages.get(Blacksmith.class, "completed"));
					if (gold.quantity != 30)
						gold.detach(Dungeon.hero.belongings.backpack, 30);
					else gold.detachAll(Dungeon.hero.belongings.backpack);

					Quest.completed = true;
					Quest.reforged = false;
				}
			}
		} else if (checksan()) {
			tell(Messages.get(Blacksmith.class, "collected"));
			Item.autocollect(new SanChikarah(), Dungeon.hero.pos);
		} else if (!Quest.reforged) {
			GameScene.show(new WndBlacksmith(this, Dungeon.hero));
		} else if (Dungeon.isChallenged(Challenges.NO_SCROLLS)) {
			GameScene.show(new WndBlacksmith(this, Dungeon.hero));
		} else tell(Messages.get(Blacksmith.class, "lost"));

		return false;
	}

	private void tell(String text) {
		GameScene.show(new WndQuest(this, text));
	}

	public static String verify(Item item1, Item item2) {

		if (item1 == item2) {
			return Messages.get(Blacksmith.class, "sametwice");
		}

		if (!item1.isIdentified() || !item2.isIdentified()) {
			return Messages.get(Blacksmith.class, "identify");
		}

		if (item1.cursed || item2.cursed) {
			return Messages.get(Blacksmith.class, "cursed");
		}

		if (item1.level < 0 || item2.level < 1) {
			return Messages.get(Blacksmith.class, "junk");
		}

		if ((item1.level + item2.level > 15) && !item1.isReinforced()) {
			return Messages.get(Blacksmith.class, "reinforce");
		}

		if (!item1.isUpgradable() || !item2.isUpgradable()
				|| item1 instanceof Artifact || item2 instanceof Artifact) {
			return Messages.get(Blacksmith.class, "cant");
		}

		return null;
	}

	private static float upgradeChance = 0.5f;

	public static void upgrade(Item item1, Item item2) {

		Item first, second;

		first = item1;
		second = item2;


		Sample.INSTANCE.play(Assets.SND_EVOKE);
		ScrollOfUpgrade.upgrade(Dungeon.hero);
		Item.evoke(Dungeon.hero);

		if (first.isEquipped(Dungeon.hero)) {
			((EquipableItem) first).doUnequip(Dungeon.hero, true);
		}

		DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
		if (gold != null) {
			upgradeChance = (upgradeChance + (gold.quantity() * 0.05f));
		}
		if (first != null) {
			for (int i = 0; i < second.level; i++) {
				if (i < 2) {
					Sample.INSTANCE.play(Assets.SND_EVOKE);
					first.upgrade();
				} else if (Random.Float() < upgradeChance) {
					first.upgrade();
					upgradeChance = Math.max(0.5f, upgradeChance - 0.1f);
				}
			}
		}

		Dungeon.hero.spendAndNext(2f);

		if (second.isEquipped(Dungeon.hero)) {
			((EquipableItem) second).doUnequip(Dungeon.hero, false);
		}
		second.detachAll(Dungeon.hero.belongings.backpack);
		if (gold != null) {
			gold.detachAll(Dungeon.hero.belongings.backpack);
		}
		Quest.reforged = true;

		if (Dungeon.isChallenged(Challenges.NO_SCROLLS)) {
			GLog.h(Messages.get(Blacksmith.class, "challenge"));
		}

		Journal.remove(Journal.Feature.TROLL);
	}

	public static boolean checksan() {
		SanChikarahDeath san1 = Dungeon.hero.belongings.getItem(SanChikarahDeath.class);
		SanChikarahLife san2 = Dungeon.hero.belongings.getItem(SanChikarahLife.class);
		SanChikarahTranscend san3 = Dungeon.hero.belongings.getItem(SanChikarahTranscend.class);

		if (san1 != null && san2 != null && san3 != null) {
			san1.detachAll(Dungeon.hero.belongings.backpack);
			san2.detachAll(Dungeon.hero.belongings.backpack);
			san3.detachAll(Dungeon.hero.belongings.backpack);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	public static class Quest {

		private static boolean spawned;

		private static boolean alternative;
		private static boolean given;
		private static boolean completed;
		private static boolean reforged;

		public static void reset() {
			spawned = false;
			given = false;
			completed = false;
			reforged = false;
		}

		private static final String NODE = "blacksmith";

		private static final String SPAWNED = "spawned";
		private static final String ALTERNATIVE = "alternative";
		private static final String GIVEN = "given";
		private static final String COMPLETED = "completed";
		private static final String REFORGED = "reforged";

		public static void storeInBundle(Bundle bundle) {

			Bundle node = new Bundle();

			node.put(SPAWNED, spawned);

			if (spawned) {
				node.put(ALTERNATIVE, alternative);
				node.put(GIVEN, given);
				node.put(COMPLETED, completed);
				node.put(REFORGED, reforged);
			}

			bundle.put(NODE, node);
		}

		public static void restoreFromBundle(Bundle bundle) {

			Bundle node = bundle.getBundle(NODE);

			if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
				alternative = node.getBoolean(ALTERNATIVE);
				given = node.getBoolean(GIVEN);
				completed = node.getBoolean(COMPLETED);
				reforged = node.getBoolean(REFORGED);
			} else {
				reset();
			}
		}

		public static ArrayList<Room> spawn( ArrayList<Room> rooms ) {
			if (!spawned && Dungeon.depth == 11) {

				rooms.add(new BlacksmithRoom());
				spawned = true;
				alternative = Random.Int( 2 ) == 0;

				given = false;

			}
			return rooms;
		}

		public static void placeItem() {

			int pos = Dungeon.level.randomRespawnCell();
			while (Dungeon.level.heaps.get(pos) != null) {
				pos = Dungeon.level.randomRespawnCell();
			}

			if (pos != -1) {
				Heap heap = Dungeon.level.drop(new Anvil(), pos);
				heap.type = Heap.Type.HARD_TOMB;
				heap.sprite.link();
				given = true;
			}

		}
	}
}
