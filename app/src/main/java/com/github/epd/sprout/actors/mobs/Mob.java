
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Amok;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Dewcharge;
import com.github.epd.sprout.actors.buffs.Sleep;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroSubClass;
import com.github.epd.sprout.actors.mobs.npcs.NPC;
import com.github.epd.sprout.actors.mobs.npcs.Shopkeeper;
import com.github.epd.sprout.effects.Surprise;
import com.github.epd.sprout.effects.Wound;
import com.github.epd.sprout.items.DewVial;
import com.github.epd.sprout.items.Dewdrop;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.RedDewdrop;
import com.github.epd.sprout.items.VioletDewdrop;
import com.github.epd.sprout.items.YellowDewdrop;
import com.github.epd.sprout.items.artifacts.CloakOfShadows;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.rings.RingOfAccuracy;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.features.HighGrass;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.ui.QuickSlotButton;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public abstract class Mob extends Char {

	{
		actPriority = 2; //hero gets priority over mobs.
	}

	private static final String TXT_DIED = Messages.get(Mob.class, "died");

	protected static final String TXT_NOTICE1 = "?!";
	protected static final String TXT_RAGE = Messages.get(Mob.class, "rage");
	protected static final String TXT_EXP = Messages.get(Mob.class, "exp");

	public AiState SLEEPING = new Sleeping();
	public AiState HUNTING = new Hunting();
	public AiState WANDERING = new Wandering();
	public AiState FLEEING = new Fleeing();
	public AiState PASSIVE = new Passive();
	public AiState state = SLEEPING;

	public Class<? extends CharSprite> spriteClass;

	protected int target = -1;

	protected int defenseSkill = 0;

	protected int EXP = 1;
	protected int maxLvl = 30000;
	protected int dewLvl = 1;

	public Char enemy;
	protected boolean enemySeen;
	protected boolean alerted = false;

	protected static final float TIME_TO_WAKE_UP = 1f;

	public boolean hostile = true;
	public boolean ally = false;
	public boolean originalgen = false;

	private static final String STATE = "state";
	private static final String SEEN = "seen";
	private static final String TARGET = "target";
	private static final String ORIGINAL = "originalgen";

	public int getExp() {
		return EXP;
	}

	public boolean isPassive() {
		return state == PASSIVE;
	}

	@Override
	public void storeInBundle(Bundle bundle) {

		super.storeInBundle(bundle);

		if (state == SLEEPING) {
			bundle.put(STATE, Sleeping.TAG);
		} else if (state == WANDERING) {
			bundle.put(STATE, Wandering.TAG);
		} else if (state == HUNTING) {
			bundle.put(STATE, Hunting.TAG);
		} else if (state == FLEEING) {
			bundle.put(STATE, Fleeing.TAG);
		} else if (state == PASSIVE) {
			bundle.put(STATE, Passive.TAG);
		}
		bundle.put(SEEN, enemySeen);
		bundle.put(TARGET, target);
		bundle.put(ORIGINAL, originalgen);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {

		super.restoreFromBundle(bundle);

		String state = bundle.getString(STATE);
		if (state.equals(Sleeping.TAG)) {
			this.state = SLEEPING;
		} else if (state.equals(Wandering.TAG)) {
			this.state = WANDERING;
		} else if (state.equals(Hunting.TAG)) {
			this.state = HUNTING;
		} else if (state.equals(Fleeing.TAG)) {
			this.state = FLEEING;
		} else if (state.equals(Passive.TAG)) {
			this.state = PASSIVE;
		}

		enemySeen = bundle.getBoolean(SEEN);

		target = bundle.getInt(TARGET);

		originalgen = bundle.getBoolean(ORIGINAL);
	}

	public CharSprite sprite() {
		CharSprite sprite = null;
		try {
			sprite = spriteClass.newInstance();
		} catch (Exception e) {
		}
		return sprite;
	}

	@Override
	protected boolean act() {

		super.act();

		boolean justAlerted = alerted;
		alerted = false;

		sprite.hideAlert();

		if (paralysed > 0) {
			enemySeen = false;
			spend(TICK);
			return true;
		}

		enemy = chooseEnemy();

		/*
		boolean invisibleCheck=false;

		if (enemy.invisible > 0 && this.resistances().contains(Invisibility.class)){
			invisibleCheck=false;			
		} else if (enemy.invisible > 0) {
			invisibleCheck=true;
		}
		*/
		boolean enemyInFOV = enemy != null && enemy.isAlive() && Level.fieldOfView[enemy.pos] && enemy.invisible <= 0;

		return state.act(enemyInFOV, justAlerted);
	}

	protected Char chooseEnemy() {

		Terror terror = buff(Terror.class);
		if (terror != null) {
			Char source = (Char) Actor.findById(terror.object);
			if (source != null) {
				return source;
			}
		}

		// resets target if: the target is dead, the target has been lost
		// (wandering)
		// or if the mob is amoked and targeting the hero (will try to target
		// something else)
		if (enemy != null && !enemy.isAlive() || state == WANDERING
				|| (buff(Amok.class) != null && enemy == Dungeon.hero))
			enemy = null;

		// if there is no current target, find a new one.
		if (enemy == null) {

			HashSet<Char> enemies = new HashSet<Char>();

			// if the mob is amoked...
			if (buff(Amok.class) != null) {

				// try to find an enemy mob to attack first.
				for (Mob mob : Dungeon.level.mobs)
					if (mob != this && Level.fieldOfView[mob.pos]
							&& mob.hostile)
						enemies.add(mob);
				if (enemies.size() > 0)
					return Random.element(enemies);

				// try to find ally mobs to attack second.
				for (Mob mob : Dungeon.level.mobs)
					if (mob != this && Level.fieldOfView[mob.pos] && mob.ally)
						enemies.add(mob);
				if (enemies.size() > 0)
					return Random.element(enemies);

				// if there is nothing, go for the hero.
				return Dungeon.hero;

				// if the mob is not amoked...
			} else {

				// try to find ally mobs to attack.
				for (Mob mob : Dungeon.level.mobs)
					if (mob != this && Level.fieldOfView[mob.pos] && mob.ally)
						enemies.add(mob);

				// and add the hero to the list of targets.
				enemies.add(Dungeon.hero);

				//go after the closest enemy, preferring the hero if two are equidistant
				Char closest = null;
				for (Char curr : enemies) {
					if (closest == null
							|| Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)
							|| Dungeon.level.distance(pos, curr.pos) == Dungeon.level.distance(pos, closest.pos) && curr == Dungeon.hero) {
						closest = curr;
					}
				}
				return closest;

			}

		} else
			return enemy;
	}

	protected boolean moveSprite(int from, int to) {

		if (sprite.isVisible()
				&& (Dungeon.visible[from] || Dungeon.visible[to])) {
			sprite.move(from, to);
			return true;
		} else {
			sprite.place(to);
			return true;
		}
	}

	@Override
	public void add(Buff buff) {
		super.add(buff);
		if (buff instanceof Amok) {
			if (sprite != null) {
				sprite.showStatus(CharSprite.NEGATIVE, TXT_RAGE);
			}
			state = HUNTING;
		} else if (buff instanceof Terror) {
			state = FLEEING;
		} else if (buff instanceof Sleep) {
			state = SLEEPING;
			this.sprite().showSleep();
			postpone(Sleep.SWS);
		}
	}

	@Override
	public void remove(Buff buff) {
		super.remove(buff);
		if (buff instanceof Terror) {
			sprite.showStatus(CharSprite.NEGATIVE, TXT_RAGE);
			state = HUNTING;
		}
	}

	protected boolean canAttack(Char enemy) {
		return Dungeon.level.adjacent(pos, enemy.pos);
	}

	protected boolean getCloser(int target) {

		if (rooted || target == pos) {
			return false;
		}

		int step = -1;

		if (Dungeon.level.adjacent(pos, target)) {

			path = null;

			if (Actor.findChar(target) == null && Level.passable[target]) {
				step = target;
			}

		} else {

			boolean newPath = false;
			if (path == null || path.isEmpty()
					|| !Dungeon.level.adjacent(pos, path.getFirst())
					|| path.size() > 2 * Dungeon.level.distance(pos, target))
				newPath = true;
			else if (path.getLast() != target) {
				//if the new target is adjacent to the end of the path, adjust for that
				//rather than scrapping the whole path. Unless the path is very long,
				//in which case re-checking will likely result in a much better path
				if (Dungeon.level.adjacent(target, path.getLast()) && path.size() < Dungeon.level.distance(pos, target)) {
					int last = path.removeLast();

					if (path.isEmpty()) {

						//shorten for a closer one
						if (Dungeon.level.adjacent(target, pos)) {
							path.add(target);
							//extend the path for a further target
						} else {
							path.add(last);
							path.add(target);
						}

					} else if (!path.isEmpty()) {
						//if the new target is simply 1 earlier in the path shorten the path
						if (path.getLast() == target) {

							//if the new target is closer/same, need to modify end of path
						} else if (Dungeon.level.adjacent(target, path.getLast())) {
							path.add(target);

							//if the new target is further away, need to extend the path
						} else {
							path.add(last);
							path.add(target);
						}
					}

				} else {
					newPath = true;
				}

			}


			if (!newPath) {
				//looks ahead for path validity, up to length-1 or 4, but always at least 1.
				int lookAhead = (int) GameMath.gate(1, path.size() - 1, 4);
				for (int i = 0; i < lookAhead; i++) {
					int cell = path.get(i);
					if (!Level.passable[cell] || (Dungeon.visible[cell] && Actor.findChar(cell) != null)) {
						newPath = true;
						break;
					}
				}
			}

			if (newPath) {
				path = Dungeon.findPath(this, pos, target,
						Level.passable,
						Level.fieldOfView);
			}

			if (path == null ||
					(state == HUNTING && path.size() > Math.max(9, 2 * Dungeon.level.distance(pos, target)))) {
				return false;
			}

			step = path.removeFirst();
		}
		if (step != -1) {
			move(step);
			return true;
		} else {
			return false;
		}
	}

	protected boolean getFurther(int target) {
		int step = Dungeon.flee(this, pos, target, Level.passable,
				Level.fieldOfView);
		if (step != -1) {
			move(step);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updateSpriteState() {
		super.updateSpriteState();
		if (Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) != null)
			sprite.add(CharSprite.State.PARALYSED);
	}

	@Override
	public void move(int step) {
		super.move(step);

		if (!flying) {
			Dungeon.level.mobPress(this);
		}
	}

	protected float attackDelay() {
		return 1f;
	}

	protected boolean doAttack(Char enemy) {

		boolean visible = Dungeon.visible[pos];

		if (visible) {
			sprite.attack(enemy.pos);
		} else {
			attack(enemy);
		}

		spend(attackDelay());

		return !visible;
	}

	@Override
	public void onAttackComplete() {
		attack(enemy);
		super.onAttackComplete();
	}

	@Override
	public int defenseSkill(Char enemy) {
		if (enemySeen && paralysed == 0) {
			int defenseSkill = this.defenseSkill;
			int penalty = 0;
			for (Buff buff : enemy.buffs(RingOfAccuracy.Accuracy.class)) {
				penalty += ((RingOfAccuracy.Accuracy) buff).level;
			}
			if (penalty != 0 && enemy == Dungeon.hero)
				defenseSkill *= Math.pow(0.75, penalty);
			return defenseSkill;
		} else {
			return 0;
		}
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		if (!enemySeen && enemy == Dungeon.hero) {
			if (((Hero) enemy).subClass == HeroSubClass.ASSASSIN) {
				damage *= 1.34f;
				Wound.hit(this);
			} else if (enemy.buff(CloakOfShadows.cloakStealth.class) != null) {
				float damagebonus = enemy.buff(CloakOfShadows.cloakStealth.class) == null ? 0
						: (enemy.buff(CloakOfShadows.cloakStealth.class).level() - 10) / 20f;
				damage *= (1f + damagebonus);
				Wound.hit(this);
			} else {
				Surprise.hit(this);
			}
		}

		//if attacked by something else than current target, and that thing is closer, switch targets
		if (this.enemy == null
				|| (enemy != this.enemy && (Dungeon.level.distance(pos, enemy.pos) < Dungeon.level.distance(pos, this.enemy.pos)))) {
			aggro(enemy);
			target = enemy.pos;
		}
		return damage;
	}

	public void aggro(Char ch) {
		enemy = ch;
		if (state != PASSIVE) {
			state = HUNTING;
		}
	}

	public int adj(int type) {

		int adjustment = 1;

		switch (type) {
			case 0:
				adjustment = Dungeon.depth;
			case 1:
				adjustment = Dungeon.depth / 2;
			case 2:
				adjustment = Dungeon.depth / 4;
			case 3:
				adjustment = Dungeon.depth * 2;
			default:
				adjustment = 1;

		}

		return adjustment;
	}

	@Override
	public void damage(int dmg, Object src) {

		Terror.recover(this);

		if (state == SLEEPING) {
			state = WANDERING;
		}
		alerted = true;

		super.damage(dmg, src);
	}

	@Override
	public void destroy() {

		super.destroy();

		Dungeon.level.mobs.remove(this);

		if (Dungeon.hero.isAlive()) {

			if (hostile) {
				Statistics.enemiesSlain++;
			}

			if (EXP > 0) {
				Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, TXT_EXP, EXP);
				Dungeon.hero.earnExp(EXP);

				if (Dungeon.hero.checkpet() != null) {
					Dungeon.hero.checkpet().sprite.showStatus(CharSprite.POSITIVE, TXT_EXP, EXP);
					Dungeon.hero.checkpet().earnExp(EXP);
				}
			}
		}
	}

	public boolean checkOriginalGenMobs() {
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob.originalgen && !(mob instanceof Shopkeeper)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void die(Object cause) {

		super.die(cause);

		int generation = 0;

		if (this instanceof Swarm) {
			Swarm swarm = (Swarm) this;
			generation = swarm.generation;
		}

		if (Dungeon.hero.buff(Dewcharge.class) != null && generation == 0 && !(this instanceof NPC)) {
			explodeDewHigh(pos);
		}

		if (!Dungeon.level.cleared && originalgen && !checkOriginalGenMobs() && Dungeon.depth > 2 && Dungeon.depth < 25 && !Dungeon.bossLevel(Dungeon.depth)) {
			Dungeon.level.cleared = true;
			GameScene.levelCleared();
			if (Dungeon.depth > 0) {
				Statistics.prevfloormoves = Math.max(Dungeon.pars[Dungeon.depth] - Dungeon.level.currentmoves, 0);
				if (Statistics.prevfloormoves > 1) {
					GLog.h(Messages.get(this, "draw1"), Statistics.prevfloormoves);
				} else if (Statistics.prevfloormoves == 1) {
					GLog.h(Messages.get(this, "draw2"));
				} else if (Statistics.prevfloormoves == 0) {
					GLog.h(Messages.get(this, "draw3"));
				}
			}
		}

		float lootChance = this.lootChance;
		float lootChanceOther = this.lootChanceOther;
		int bonus = 0;
		for (Buff buff : Dungeon.hero.buffs(MasterThievesArmband.Thievery.class)) {
			if (((MasterThievesArmband.Thievery) buff).level() > 10)
				bonus += ((MasterThievesArmband.Thievery) buff).level() - 10;
		}

		if (lootChance != 0f) {
			if (lootChanceOther == 0f) {
				lootChance += 0.024f * bonus;
				if (lootChance > 1f) {
					lootChance = 1f;
				}
			} else {
				lootChance += 0.012f * bonus;
				if (lootChance > 0.5f) {
					lootChance = 0.5f;
				}

				lootChanceOther += 0.024f * bonus;
				if (lootChanceOther > 1f) {
					lootChanceOther = 1f;
				}
			}
		}


		if (Random.Float() < lootChance) {
			Item loot = createLoot();
			if (loot != null)
				if (loot instanceof Dewdrop || loot instanceof YellowDewdrop || loot instanceof RedDewdrop || loot instanceof VioletDewdrop) {
					Dungeon.level.drop(loot, pos).sprite.drop();
				}
		} else if (Random.Float() < lootChanceOther) {
			Item lootOther = createLootOther();
			if (lootOther != null)
				if (lootOther instanceof Dewdrop || lootOther instanceof YellowDewdrop || lootOther instanceof RedDewdrop || lootOther instanceof VioletDewdrop) {
					Dungeon.level.drop(lootOther, pos).sprite.drop();
				}
		}

		QuickSlotButton.refresh();

		if (Dungeon.hero.isAlive() && !Dungeon.visible[pos]) {
			GLog.i(TXT_DIED);
		}
	}

	protected Object loot = null;
	protected Object lootOther = null;
	protected float lootChance = 0;
	protected float lootChanceOther = 0;

	@SuppressWarnings("unchecked")
	protected Item createLoot() {
		Item item;
		if (loot instanceof Generator.Category) {

			item = Generator.random((Generator.Category) loot);

		} else if (loot instanceof Class<?>) {

			item = Generator.random((Class<? extends Item>) loot);

		} else {
			item = (Item) loot;
		}

		if (ShatteredPixelDungeon.autocollect() && item != null) {
			if (item instanceof Gold) {
				Dungeon.gold += item.quantity;
				MasterThievesArmband.Thievery thievery = hero.buff(MasterThievesArmband.Thievery.class);
				if (thievery != null) thievery.collect(item.quantity);
			} else {
				if (!(item instanceof Dewdrop) && !(item instanceof YellowDewdrop) && !(item instanceof RedDewdrop) && !(item instanceof VioletDewdrop)) {
					Item.autocollect(item, Dungeon.hero.pos);
				}

			}

		} else {
			Dungeon.level.drop(item, pos).sprite.drop();
		}


		return item;

	}

	Hero hero = Dungeon.hero;

	@SuppressWarnings("unchecked")
	protected Item createLootOther() {
		Item item;
		if (lootOther instanceof Generator.Category) {

			item = Generator.random((Generator.Category) lootOther);

		} else if (lootOther instanceof Class<?>) {

			item = Generator.random((Class<? extends Item>) lootOther);

		} else {

			item = (Item) lootOther;

		}
		if (item != null) {
			if (ShatteredPixelDungeon.autocollect()) {
				if (item instanceof Gold) {
					Dungeon.gold += item.quantity;

					MasterThievesArmband.Thievery thievery = hero.buff(MasterThievesArmband.Thievery.class);
					if (thievery != null)
						thievery.collect(item.quantity);
				} else {
					if (!(item instanceof Dewdrop) && !(item instanceof YellowDewdrop) && !(item instanceof RedDewdrop) && !(item instanceof VioletDewdrop)) {
						Item.autocollect(item, Dungeon.hero.pos);
					}
				}
			} else Dungeon.level.drop(item, pos).sprite.drop();
		}

		return item;
	}

	DewVial vial = Dungeon.hero.belongings.getItem(DewVial.class);
	Level level = Dungeon.level;

	public void explodeDew(int cell) {

		Sample.INSTANCE.play(Assets.SND_BLAST, 2);

		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Dungeon.level.getLength() && Level.passable[c]) {
				if (Random.Int(10) == 1) {
					if (ShatteredPixelDungeon.autocollect() && vial != null) {
						if (!vial.isFull()) {
							vial.volume = vial.volume + (Dungeon.superDew ? 50 : 5);
							GLog.i(Messages.get(HighGrass.class, "red"));
							if (vial.isFull()) {
								vial.volume = DewVial.MAX_VOLUME();
								Messages.get(DewVial.class, "full");
							}
						} else Dungeon.level.drop(new RedDewdrop(), c).sprite.drop();
					} else Dungeon.level.drop(new RedDewdrop(), c).sprite.drop();
				} else if (Random.Int(3) == 1) {
					if (ShatteredPixelDungeon.autocollect() && vial != null) {
						if (!vial.isFull()) {
							vial.volume = vial.volume + (Dungeon.superDew ? 20 : 2);
							GLog.i(Messages.get(HighGrass.class, "yellow"));
							if (vial.isFull()) {
								vial.volume = DewVial.MAX_VOLUME();
								Messages.get(DewVial.class, "full");
							}
						} else Dungeon.level.drop(new YellowDewdrop(), c).sprite.drop();
					} else Dungeon.level.drop(new YellowDewdrop(), c).sprite.drop();
				}
			}
		}
		QuickSlotButton.refresh();

	}

	public void explodeDewHigh(int cell) {

		Sample.INSTANCE.play(Assets.SND_BLAST, 2);

		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Dungeon.level.getLength() && Level.passable[c]) {
				if (Random.Int(8) == 1) {
					if (ShatteredPixelDungeon.autocollect() && vial != null) {
						if (vial.volume <= (DewVial.MAX_VOLUME() - 45)) {
							vial.volume = vial.volume + (Dungeon.superDew ? 200 : 50);
							GLog.i(Messages.get(HighGrass.class, "violet"));
							if (vial.isFull()) {
								vial.volume = DewVial.MAX_VOLUME();
								Messages.get(DewVial.class, "full");
							}
						} else Dungeon.level.drop(new VioletDewdrop(), c).sprite.drop();
					} else Dungeon.level.drop(new VioletDewdrop(), c).sprite.drop();
				} else if (Random.Int(2) == 1) {
					if (ShatteredPixelDungeon.autocollect() && vial != null) {
						if (!vial.isFull()) {
							vial.volume = vial.volume + (Dungeon.superDew ? 50 : 5);
							GLog.i(Messages.get(HighGrass.class, "red"));
							if (vial.isFull()) {
								vial.volume = DewVial.MAX_VOLUME();
								Messages.get(DewVial.class, "full");
							}
						} else Dungeon.level.drop(new RedDewdrop(), c).sprite.drop();
					} else Dungeon.level.drop(new RedDewdrop(), c).sprite.drop();
				}
			}
		}
		QuickSlotButton.refresh();
	}

	public boolean reset() {
		return false;
	}

	public void beckon(int cell) {

		notice();

		if (state != HUNTING) {
			state = WANDERING;
		}
		target = cell;
	}

	public String description() {
		return Messages.get(this, "desc");
	}

	public void notice() {
		sprite.showAlert();
	}

	public void yell(String str) {
		GLog.h("%s: \"%s\" ", name, str);
	}

	// returns true when a mob sees the hero, and is currently targeting them.
	public boolean focusingHero() {
		return enemySeen && (target == Dungeon.hero.pos);
	}

	public interface AiState {
		boolean act(boolean enemyInFOV, boolean justAlerted);

		String status();
	}

	private class Sleeping implements AiState {

		public static final String TAG = "SLEEPING";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (enemyInFOV
					&& Random.Int(distance(enemy) + enemy.stealth()
					+ (enemy.flying ? 2 : 0)) == 0) {

				enemySeen = true;

				notice();
				state = HUNTING;
				target = enemy.pos;


				spend(TIME_TO_WAKE_UP);

			} else {

				enemySeen = false;

				spend(TICK);

			}
			return true;
		}

		@Override
		public String status() {
			return Messages.get(this, "status", name);
		}
	}

	private class Wandering implements AiState {

		public static final String TAG = "WANDERING";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (enemyInFOV
					&& (justAlerted || Random.Int(distance(enemy) / 2
					+ enemy.stealth()) == 0)) {

				enemySeen = true;

				notice();
				state = HUNTING;
				target = enemy.pos;

			} else {

				enemySeen = false;

				int oldPos = pos;
				if (target != -1 && getCloser(target)) {
					spend(1 / speed());
					return moveSprite(oldPos, pos);
				} else {
					target = Dungeon.level.randomDestination();
					spend(TICK);
				}

			}
			return true;
		}

		@Override
		public String status() {
			return Messages.get(this, "status", name);
		}
	}

	private class Hunting implements AiState {

		public static final String TAG = "HUNTING";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy(enemy) && canAttack(enemy)) {

				return doAttack(enemy);

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				}

				int oldPos = pos;
				if (target != -1 && getCloser(target)) {

					spend(1 / speed());
					return moveSprite(oldPos, pos);

				} else {

					spend(TICK);
					state = WANDERING;
					target = Dungeon.level.randomDestination();
					return true;
				}
			}
		}

		@Override
		public String status() {
			return Messages.get(this, "status", name);
		}
	}

	protected class Fleeing implements AiState {

		public static final String TAG = "FLEEING";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;
			if (enemyInFOV) {
				target = enemy.pos;
			}

			int oldPos = pos;
			if (target != -1 && getFurther(target)) {

				spend(1 / speed());
				return moveSprite(oldPos, pos);

			} else {

				spend(TICK);
				nowhereToRun();

				return true;
			}
		}

		protected void nowhereToRun() {
		}

		@Override
		public String status() {
			return Messages.get(this, "status", name);
		}
	}

	private class Passive implements AiState {

		public static final String TAG = "PASSIVE";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = false;
			spend(TICK);
			return true;
		}

		@Override
		public String status() {
			return Messages.get(this, "status", name);
		}
	}


}
