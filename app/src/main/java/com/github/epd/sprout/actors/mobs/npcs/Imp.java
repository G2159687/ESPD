
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Journal;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.mobs.Golem;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.Monk;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.DwarfToken;
import com.github.epd.sprout.items.rings.Ring;
import com.github.epd.sprout.levels.CityLevel;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ImpSprite;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndImp;
import com.github.epd.sprout.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Imp extends NPC {

	{
		name = Messages.get(Imp.class, "name");
		spriteClass = ImpSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	private static final String TXT_GOLEMS1 = Messages.get(Imp.class, "golemone");

	private static final String TXT_MONKS1 = Messages.get(Imp.class, "monkone");

	private static final String TXT_GOLEMS2 = Messages.get(Imp.class, "golemtwo");

	private static final String TXT_MONKS2 = Messages.get(Imp.class, "monktwo");

	private static final String TXT_CYA = Messages.get(Imp.class, "cya", Dungeon.hero.givenName());
	private static final String TXT_HEY = Messages.get(Imp.class, "hey", Dungeon.hero.givenName());

	private boolean seenBefore = false;

	@Override
	protected boolean act() {

		if (!Quest.given && Dungeon.visible[pos]) {
			if (!seenBefore) {
				yell(Utils.format(TXT_HEY, Dungeon.hero.givenName()));
			}
			seenBefore = true;
		} else {
			seenBefore = false;
		}

		throwItem();

		return super.act();
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(Imp.class, "def");
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
	public boolean interact() {

		sprite.turnTo(pos, Dungeon.hero.pos);
		if (Quest.given) {

			DwarfToken tokens = Dungeon.hero.belongings
					.getItem(DwarfToken.class);
			if (tokens != null
					&& (tokens.quantity() >= 8 || (!Quest.alternative && tokens
					.quantity() >= 6))) {
				GameScene.show(new WndImp(this, tokens));
			} else {
				tell(Quest.alternative ? TXT_MONKS2 : TXT_GOLEMS2,
						Dungeon.hero.givenName());
			}

		} else {
			tell(Quest.alternative ? TXT_MONKS1 : TXT_GOLEMS1);
			Quest.given = true;
			Quest.completed = false;

			Journal.add(Journal.Feature.IMP);
		}
		return false;
	}

	private void tell(String format, Object... args) {
		GameScene.show(new WndQuest(this, Utils.format(format, args)));
	}

	public void flee() {

		yell(Utils.format(TXT_CYA, Dungeon.hero.givenName()));

		destroy();
		sprite.die();
	}

	@Override
	public String description() {
		return Messages.get(Imp.class, "desc");
	}

	public static class Quest {

		private static boolean alternative;

		private static boolean spawned;
		private static boolean given;
		private static boolean completed;

		public static Ring reward;

		public static void reset() {
			spawned = false;

			reward = null;
		}

		private static final String NODE = "demon";

		private static final String ALTERNATIVE = "alternative";
		private static final String SPAWNED = "spawned";
		private static final String GIVEN = "given";
		private static final String COMPLETED = "completed";
		private static final String REWARD = "reward";

		public static void storeInBundle(Bundle bundle) {

			Bundle node = new Bundle();

			node.put(SPAWNED, spawned);

			if (spawned) {
				node.put(ALTERNATIVE, alternative);

				node.put(GIVEN, given);
				node.put(COMPLETED, completed);
				node.put(REWARD, reward);
			}

			bundle.put(NODE, node);
		}

		public static void restoreFromBundle(Bundle bundle) {

			Bundle node = bundle.getBundle(NODE);

			if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
				alternative = node.getBoolean(ALTERNATIVE);

				given = node.getBoolean(GIVEN);
				completed = node.getBoolean(COMPLETED);
				reward = (Ring) node.get(REWARD);
			}
		}

		public static void spawn(CityLevel level) {
			if (!spawned && Dungeon.depth > 16
				//&& Random.Int(20 - Dungeon.depth) == 0
					) {

				Imp npc = new Imp();
				do {
					npc.pos = level.randomRespawnCell();
				} while (npc.pos == -1);
				level.mobs.add(npc);

				spawned = true;
				alternative = Random.Int(2) == 0;

				given = false;

				do {
					reward = (Ring) Generator.random(Generator.Category.RING);
				} while (reward.cursed);
				reward.upgrade(2);
				if (Dungeon.questTweaks) {
					reward.upgrade(30);
				}
				reward.cursed = true;
			}
		}

		public static void process(Mob mob) {
			if (spawned && given && !completed) {
				if ((alternative && mob instanceof Monk)
						|| (!alternative && mob instanceof Golem)) {

					Item.autocollect(new DwarfToken(), mob.pos);
				}
			}
		}

		public static void complete() {
			reward = null;
			completed = true;

			Journal.remove(Journal.Feature.IMP);
		}

		public static boolean isCompleted() {
			return completed;
		}
	}
}
