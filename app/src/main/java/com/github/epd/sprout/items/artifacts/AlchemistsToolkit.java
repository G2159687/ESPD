package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Egg;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.potions.PotionOfExperience;
import com.github.epd.sprout.items.potions.PotionOfOverHealing;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class AlchemistsToolkit extends Artifact {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ARTIFACT_TOOLKIT;

		level = 0;
		levelCap = 10;
	}

	public static final String AC_BREW = Messages.get(AlchemistsToolkit.class, "ac_brew");

	// arrays used in containing potion collections for mix logic.
	public final ArrayList<Class> combination = new ArrayList<>();
	public ArrayList<Class> curGuess = new ArrayList<>();
	public ArrayList<Class> bstGuess = new ArrayList<>();

	public int numWrongPlace = 0;
	public int numRight = 0;

	private int seedsToPotion = 0;

	protected String inventoryTitle = Messages.get(AlchemistsToolkit.class, "invtitle");
	protected WndBag.Mode mode = WndBag.Mode.POTION;

	public AlchemistsToolkit() {
		super();

		Generator.Category cat = Generator.Category.POTION;
		for (int i = 1; i <= 3; i++) {
			Class potion;
			do {
				potion = cat.classes[Random.chances(cat.probs)];
				// forcing the player to use experience potions would be
				// completely unfair.
			}
			while (combination.contains(potion) || potion == PotionOfExperience.class || potion == Egg.class || potion == PotionOfOverHealing.class);
			combination.add(potion);
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && level < levelCap && !cursed)
			actions.add(AC_BREW);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_BREW)) {
			GameScene.selectItem(itemSelector, mode, inventoryTitle);
		} else {
			super.execute(hero, action);
		}
	}

	public void guessBrew() {
		if (curGuess.size() != 3)
			return;

		int numWrongPlace = 0;
		int numRight = 0;

		for (Class potion : curGuess) {
			if (combination.contains(potion)) {
				if (curGuess.indexOf(potion) == combination.indexOf(potion)) {
					numRight++;
				} else {
					numWrongPlace++;
				}
			}
		}

		int score = (numRight * 3) + numWrongPlace;

		if (score == 9)
			score++;

		if (score == 0) {

			GLog.i(Messages.get(this, "0"));

		} else if (score > level) {

			level = score;
			seedsToPotion = 0;
			bstGuess = curGuess;
			this.numRight = numRight;
			this.numWrongPlace = numWrongPlace;

			if (level == 10) {
				bstGuess = new ArrayList<>();
				GLog.p(Messages.get(this, "10"));
			} else {
				GLog.w(Messages.get(this, "finish")
						+ brewDesc(numWrongPlace, numRight)
						+ Messages.get(this, "bestbrew"));
			}

		} else {

			GLog.w(Messages.get(this, "finish")
					+ brewDesc(numWrongPlace, numRight)
					+ Messages.get(this, "throw"));
		}
		curGuess = new ArrayList<>();

	}

	private String brewDesc(int numWrongPlace, int numRight) {
		String result = "";
		if (numWrongPlace > 0) {
			result += numWrongPlace + Messages.get(this, "bdorder");
			if (numRight > 0)
				result += Messages.get(this, "and");
		}
		if (numRight > 0) {
			result += numRight + Messages.get(this, "perfect");
		}
		return result;
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new alchemy();
	}

	@Override
	public String desc() {
		String result = Messages.get(this, "desc1");

		if (isEquipped(Dungeon.hero))
			if (cursed)
				result += Messages.get(this, "desc2");
			else
				result += Messages.get(this, "desc3");

		if (level == 0) {
			result += Messages.get(this, "desc4");
		} else if (level == 10) {
			result += Messages.get(this, "desc5");
		} else if (!bstGuess.isEmpty()) {
			result += Messages.get(this, "desc6")
					+ Messages.get(bstGuess.get(0), "name") + ", " + Messages.get(bstGuess.get(1), "name") + ", "
					+ Messages.get(bstGuess.get(2), "name") + Messages.get(this, "desc7");
			result += Messages.get(this, "desc8")
					+ brewDesc(numWrongPlace, numRight) + Messages.get(this, "desc9");

			// would only trigger if an upgraded toolkit was gained through
			// transmutation or bones.
		} else {
			result += Messages.get(this, "desc10");
		}
		return result;
	}

	private static final String COMBINATION = "combination";
	private static final String CURGUESS = "curguess";
	private static final String BSTGUESS = "bstguess";

	private static final String NUMWRONGPLACE = "numwrongplace";
	private static final String NUMRIGHT = "numright";

	private static final String SEEDSTOPOTION = "seedstopotion";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(NUMWRONGPLACE, numWrongPlace);
		bundle.put(NUMRIGHT, numRight);

		bundle.put(SEEDSTOPOTION, seedsToPotion);

		bundle.put(COMBINATION,
				combination.toArray(new Class[combination.size()]));
		bundle.put(CURGUESS, curGuess.toArray(new Class[curGuess.size()]));
		bundle.put(BSTGUESS, bstGuess.toArray(new Class[bstGuess.size()]));
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		numWrongPlace = bundle.getInt(NUMWRONGPLACE);
		numRight = bundle.getInt(NUMRIGHT);

		seedsToPotion = bundle.getInt(SEEDSTOPOTION);

		combination.clear();
		Collections.addAll(combination, bundle.getClassArray(COMBINATION));
		Collections.addAll(curGuess, bundle.getClassArray(CURGUESS));
		Collections.addAll(bstGuess, bundle.getClassArray(BSTGUESS));
	}

	public class alchemy extends ArtifactBuff {

		public boolean tryCook(int count) {

			// this logic is handled inside the class with a variable so that it
			// may be stored.
			// to prevent manipulation where a player could keep throwing in 1-2
			// seeds until they get lucky.
			if (seedsToPotion == 0) {
				if (Random.Int(20) < 10 + level) {
					if (Random.Int(20) < level) {
						seedsToPotion = 1;
					} else
						seedsToPotion = 2;
				} else
					seedsToPotion = 3;
			}

			if (count >= seedsToPotion) {
				seedsToPotion = 0;
				return true;
			} else
				return false;

		}

	}

	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null && item instanceof Potion && item.isIdentified()) {
				if (!curGuess.contains(item.getClass())) {

					Hero hero = Dungeon.hero;
					hero.sprite.operate(hero.pos);
					hero.busy();
					hero.spend(2f);
					Sample.INSTANCE.play(Assets.SND_DRINK);

					item.detach(hero.belongings.backpack);

					curGuess.add(item.getClass());
					if (curGuess.size() == 3) {
						guessBrew();
					} else {
						GLog.i(Messages.get(AlchemistsToolkit.class, "mix1") + item.name()
								+ Messages.get(AlchemistsToolkit.class, "mix2"));
					}
				} else {
					GLog.w(Messages.get(AlchemistsToolkit.class, "mix3"));
				}
			} else if (item != null) {
				GLog.w(Messages.get(AlchemistsToolkit.class, "mix4"));
			}
		}
	};

}
