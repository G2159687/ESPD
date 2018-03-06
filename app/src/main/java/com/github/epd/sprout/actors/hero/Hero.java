
package com.github.epd.sprout.actors.hero;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Bones;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.GamesInProgress;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Awareness;
import com.github.epd.sprout.actors.buffs.Barkskin;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Charm;
import com.github.epd.sprout.actors.buffs.Combo;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Drowsy;
import com.github.epd.sprout.actors.buffs.Fury;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.buffs.LichenDrop;
import com.github.epd.sprout.actors.buffs.Light;
import com.github.epd.sprout.actors.buffs.MindVision;
import com.github.epd.sprout.actors.buffs.Ooze;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Regeneration;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.actors.buffs.SnipersMark;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.actors.buffs.Weakness;
import com.github.epd.sprout.actors.mobs.Lichen;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.npcs.NPC;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.CheckedCell;
import com.github.epd.sprout.effects.Flare;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.misc.Spectacles;
import com.github.epd.sprout.items.quest.Amulet;
import com.github.epd.sprout.items.consumables.Ankh;
import com.github.epd.sprout.items.Dewdrop;
import com.github.epd.sprout.items.EasterEgg;
import com.github.epd.sprout.items.Egg;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Heap.Type;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.KindOfWeapon;
import com.github.epd.sprout.items.ShadowDragonEgg;
import com.github.epd.sprout.items.armor.glyphs.Viscosity;
import com.github.epd.sprout.items.artifacts.CapeOfThorns;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.HornOfPlenty;
import com.github.epd.sprout.items.artifacts.TalismanOfForesight;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.keys.GoldenKey;
import com.github.epd.sprout.items.keys.GoldenSkeletonKey;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.items.keys.Key;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.items.misc.AutoPotion.AutoHealPotion;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.potions.PotionOfExperience;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.items.potions.PotionOfMight;
import com.github.epd.sprout.items.potions.PotionOfOverHealing;
import com.github.epd.sprout.items.potions.PotionOfStrength;
import com.github.epd.sprout.items.rings.RingOfElements;
import com.github.epd.sprout.items.rings.RingOfEvasion;
import com.github.epd.sprout.items.rings.RingOfForce;
import com.github.epd.sprout.items.rings.RingOfFuror;
import com.github.epd.sprout.items.rings.RingOfHaste;
import com.github.epd.sprout.items.rings.RingOfMight;
import com.github.epd.sprout.items.rings.RingOfTenacity;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicMapping;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicalInfusion;
import com.github.epd.sprout.items.scrolls.ScrollOfRecharging;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.items.teleporter.DragonCave;
import com.github.epd.sprout.items.teleporter.Vault;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.melee.MeleeWeapon;
import com.github.epd.sprout.items.weapon.missiles.MissileWeapon;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.features.AlchemyPot;
import com.github.epd.sprout.levels.features.Chasm;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Earthroot;
import com.github.epd.sprout.plants.Sungrass;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.scenes.SurfaceScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.HeroSprite;
import com.github.epd.sprout.ui.AttackIndicator;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.ui.QuickSlotButton;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndAscend;
import com.github.epd.sprout.windows.WndDescend;
import com.github.epd.sprout.windows.WndMessage;
import com.github.epd.sprout.windows.WndResurrect;
import com.github.epd.sprout.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Hero extends Char {

	{
		actPriority = 0; //acts at priority 0, baseline for the rest of behaviour.
	}

	public static final int STARTING_STR = 10;

	private static final float TIME_TO_REST = 1f;
	private static final float TIME_TO_SEARCH = 2f;

	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;

	public int attackSkill = 10;
	public int defenseSkill = 5;

	public boolean ready = false;

	public boolean haspet = false;
	public boolean petfollow = false;
	public int petType = 0;
	public int petLevel = 0;
	public int petKills = 0;
	public int petHP = 0;
	public int petExperience = 0;
	public int petCooldown = 0;

	public int petCount = 0;

	private boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	private Char enemy;

	private Item theKey;
	private Item theSkeletonKey;

	public boolean resting = false;

	public MissileWeapon rangedWeapon = null;
	public Belongings belongings;

	public int STR;
	public boolean weakened = false;

	public float awareness;

	public int lvl = 1;
	public int exp = 0;

	private ArrayList<Mob> visibleEnemies;

	public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();

	public Hero() {
		super();
		name = Messages.get(this, "name");

		HP = HT = 20;
		STR = STARTING_STR;
		awareness = 0.1f;

		belongings = new Belongings(this);

		visibleEnemies = new ArrayList<Mob>();
	}

	public int STR() {
		int STR = this.STR;

		for (Buff buff : buffs(RingOfMight.Might.class)) {
			STR += ((RingOfMight.Might) buff).level;
		}

		return weakened ? STR - 2 : STR;
	}

	private static final String ATTACK = "attackSkill";
	private static final String DEFENSE = "defenseSkill";
	private static final String STRENGTH = "STR";
	private static final String LEVEL = "lvl";
	private static final String EXPERIENCE = "exp";
	private static final String HASPET = "haspet";
	private static final String PETFOLLOW = "petfollow";
	private static final String PETTYPE = "petType";
	private static final String PETLEVEL = "petLevel";
	private static final String PETKILLS = "petKills";
	private static final String PETHP = "petHP";
	private static final String PETEXP = "petExperience";
	private static final String PETCOOLDOWN = "petCooldown";
	private static final String PETCOUNT = "petCount";

	@Override
	public void storeInBundle(Bundle bundle) {

		super.storeInBundle(bundle);

		heroClass.storeInBundle(bundle);
		subClass.storeInBundle(bundle);

		bundle.put(ATTACK, attackSkill);
		bundle.put(DEFENSE, defenseSkill);

		bundle.put(STRENGTH, STR);

		bundle.put(LEVEL, lvl);
		bundle.put(EXPERIENCE, exp);
		bundle.put(HASPET, haspet);
		bundle.put(PETFOLLOW, petfollow);
		bundle.put(PETTYPE, petType);
		bundle.put(PETLEVEL, petLevel);
		bundle.put(PETKILLS, petKills);
		bundle.put(PETHP, petHP);
		bundle.put(PETEXP, petExperience);
		bundle.put(PETCOOLDOWN, petCooldown);
		bundle.put(PETCOUNT, petCount);

		belongings.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		heroClass = HeroClass.restoreInBundle(bundle);
		subClass = HeroSubClass.restoreInBundle(bundle);

		attackSkill = bundle.getInt(ATTACK);
		defenseSkill = bundle.getInt(DEFENSE);

		STR = bundle.getInt(STRENGTH);
		updateAwareness();

		lvl = bundle.getInt(LEVEL);
		exp = bundle.getInt(EXPERIENCE);
		haspet = bundle.getBoolean(HASPET);
		petfollow = bundle.getBoolean(PETFOLLOW);
		petType = bundle.getInt(PETTYPE);
		petLevel = bundle.getInt(PETLEVEL);
		petKills = bundle.getInt(PETKILLS);
		petHP = bundle.getInt(PETHP);
		petExperience = bundle.getInt(PETEXP);
		petCooldown = bundle.getInt(PETCOOLDOWN);
		petCount = bundle.getInt(PETCOUNT);

		belongings.restoreFromBundle(bundle);
	}

	public static void preview(GamesInProgress.Info info, Bundle bundle) {
		info.level = bundle.getInt(LEVEL);
	}

	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass
				.title() : subClass.title();
	}

	public String givenName() {
		return name.equals(Messages.get(this, "name")) ? className() : name;
	}

	public void live() {
		Buff.affect(this, Regeneration.class);
		Buff.affect(this, Hunger.class);
	}

	public int tier() {
		return belongings.armor == null ? 0 : belongings.armor.tier;
	}

	public boolean shoot(Char enemy, MissileWeapon wep) {

		rangedWeapon = wep;
		boolean result = attack(enemy);
		Invisibility.dispel();
		rangedWeapon = null;

		return result;
	}

	@Override
	public int attackSkill(Char target) {
		float accuracy = 1;
		if (rangedWeapon != null && Dungeon.level.distance(pos, target.pos) == 1) {
			accuracy *= 0.5f;
		}

		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon
				: belongings.weapon;
		if (wep != null) {
			return (int) (attackSkill * accuracy * wep.acuracyFactor(this));
		} else {
			return (int) (attackSkill * accuracy);
		}
	}

	@Override
	public int defenseSkill(Char enemy) {

		int bonus = 0;
		for (Buff buff : buffs(RingOfEvasion.Evasion.class)) {
			bonus += ((RingOfEvasion.Evasion) buff).effectiveLevel;
		}

		float evasion = (float) Math.pow(1.15, bonus);
		if (paralysed > 0) {
			evasion /= 2;
		}

		int aEnc = belongings.armor != null ? belongings.armor.STR - STR()
				: 9 - STR();

		if (aEnc > 0) {
			return (int) (defenseSkill * evasion / Math.pow(1.5, aEnc));
		} else {

			if (heroClass == HeroClass.ROGUE) {
				return (int) ((defenseSkill - aEnc) * evasion);
			} else {
				return (int) (defenseSkill * evasion);
			}
		}
	}

	@Override
	public int dr() {
		int dr = belongings.armor != null ? Math.max(belongings.armor.DR, 0)
				: 0;
		Barkskin barkskin = buff(Barkskin.class);
		if (barkskin != null) {
			dr += barkskin.level();
		}
		return dr;
	}

	@Override
	public int damageRoll() {
		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon
				: belongings.weapon;
		int dmg;
		int bonus = 0;
		for (Buff buff : buffs(RingOfForce.Force.class)) {
			bonus += ((RingOfForce.Force) buff).level;
		}

		if (wep != null) {
			dmg = wep.damageRoll(this) + bonus;
		} else {
			int str = STR() - 8;
			dmg = bonus == 0 ? str > 1 ? Random.NormalIntRange(1, str) : 1
					: bonus > 0 ? str > 0 ? Random.NormalIntRange(str / 2
					+ bonus, (int) (str * 0.5f * bonus) + str * 2) : 1
					: 0;
		}
		if (dmg < 0)
			dmg = 0;

		if (buff(Fury.class) != null) {
			dmg *= 1.5f;
		}

		if (buff(Strength.class) != null) {
			dmg *= 4f;
			Buff.detach(this, Strength.class);
		}

		return dmg;

	}


	@Override
	public float speed() {

		float speed = super.speed();

		int hasteLevel = 0;
		for (Buff buff : buffs(RingOfHaste.Haste.class)) {
			hasteLevel += ((RingOfHaste.Haste) buff).level;
		}

		if (haspet) {
			int pethaste = Dungeon.petHasteLevel;
			PET heropet = checkpet();

			if (pethaste > 0 && hasteLevel > 10 && heropet != null) {
				hasteLevel = 10;
			}

		}


		if (hasteLevel != 0)
			speed *= Math.pow(1.2, hasteLevel);

		int aEnc = belongings.armor != null ? belongings.armor.STR - STR() : 0;
		if (aEnc > 0) {

			return (float) (speed * Math.pow(1.3, -aEnc));

		} else {

			return ((HeroSprite) sprite)
					.sprint(subClass == HeroSubClass.FREERUNNER
							&& !isStarving()) ? invisible > 0 ? 4f * speed
					: 1.5f * speed : speed;

		}
	}

	public float attackDelay() {
		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon
				: belongings.weapon;
		if (wep != null) {

			return wep.speedFactor(this);

		} else {
			// Normally putting furor speed on unarmed attacks would be
			// unnecessary
			// But there's going to be that one guy who gets a furor+force ring
			// combo
			// This is for that one guy, you shall get your fists of fury!
			int bonus = 0;
			for (Buff buff : buffs(RingOfFuror.Furor.class)) {
				bonus += ((RingOfFuror.Furor) buff).level;
			}
			return (float) (0.25 + (1 - 0.25) * Math.pow(0.8, bonus));
		}
	}

	@Override
	public void spend(float time) {
		TimekeepersHourglass.timeFreeze buff = buff(TimekeepersHourglass.timeFreeze.class);
		if (!(buff != null && buff.processTime(time)))
			super.spend(time);
	}

	public void spendAndNext(float time) {
		busy();
		spend(time);
		next();
	}

	@Override
	public boolean act() {

		super.act();

		Statistics.moves++;

		Dungeon.level.currentmoves++;

		if (paralysed > 0) {

			curAction = null;

			spendAndNext(TICK);
			return false;
		}

		Egg egg = belongings.getItem(Egg.class);
		if (egg != null) {
			egg.moves++;
		}

		EasterEgg egg2 = belongings.getItem(EasterEgg.class);
		if (egg2 != null) {
			egg2.moves++;
		}

		ShadowDragonEgg egg3 = belongings.getItem(ShadowDragonEgg.class);
		if (egg3 != null) {
			egg3.moves++;
		}

		checkVisibleMobs();
		if (!resting || buff(MindVision.class) != null || buff(Awareness.class) != null) {
			Dungeon.observe();
		}

		if (curAction == null) {

			if (resting) {
				spend(TIME_TO_REST);
				next();
				return false;
			}

			ready();
			return false;

		} else {

			resting = false;

			ready = false;

			if (curAction instanceof HeroAction.Move) {

				return actMove((HeroAction.Move) curAction);

			} else if (curAction instanceof HeroAction.Interact) {

				return actInteract((HeroAction.Interact) curAction);

			} else if (curAction instanceof HeroAction.InteractPet) {

				return actInteractPet((HeroAction.InteractPet) curAction);

			} else if (curAction instanceof HeroAction.Buy) {

				return actBuy((HeroAction.Buy) curAction);

			} else if (curAction instanceof HeroAction.PickUp) {

				return actPickUp((HeroAction.PickUp) curAction);

			} else if (curAction instanceof HeroAction.OpenChest) {

				return actOpenChest((HeroAction.OpenChest) curAction);

			} else if (curAction instanceof HeroAction.Unlock) {

				return actUnlock((HeroAction.Unlock) curAction);

			} else if (curAction instanceof HeroAction.Descend) {

				return actDescend((HeroAction.Descend) curAction);

			} else if (curAction instanceof HeroAction.Ascend) {

				return actAscend((HeroAction.Ascend) curAction);

			} else if (curAction instanceof HeroAction.Attack) {

				return actAttack((HeroAction.Attack) curAction);

			} else if (curAction instanceof HeroAction.Cook) {

				return actCook((HeroAction.Cook) curAction);

			} else if (curAction instanceof HeroAction.Collect) {

				return actCollect((HeroAction.Collect) curAction);

			}
		}

		return false;
	}

	public void busy() {
		ready = false;
	}

	private void ready() {
		sprite.idle();
		curAction = null;
		damageInterrupt = true;
		ready = true;

		AttackIndicator.updateState();

		GameScene.ready();
	}

	public void interrupt() {
		if (isAlive() && curAction != null &&
				((curAction instanceof HeroAction.Move && curAction.dst != pos) ||
						(curAction instanceof HeroAction.Ascend || curAction instanceof HeroAction.Descend))) {
			lastAction = curAction;
		}
		curAction = null;
	}

	public void resume() {
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		next();
	}


	private boolean actMove(HeroAction.Move action) {

		if (getCloser(action.dst)) {
			return true;
		} else {
			ready();
			return false;
		}
	}

	private boolean actInteract(HeroAction.Interact action) {

		NPC npc = action.npc;

		if (Dungeon.level.adjacent(pos, npc.pos)) {

			ready();
			sprite.turnTo(pos, npc.pos);
			return npc.interact();

		} else {

			if (Level.fieldOfView[npc.pos] && getCloser(npc.pos)) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}

	private boolean actInteractPet(HeroAction.InteractPet action) {

		PET pet = action.pet;

		if (Dungeon.level.adjacent(pos, pet.pos)) {

			ready();
			sprite.turnTo(pos, pet.pos);
			return pet.interact();

		} else {

			if (Level.fieldOfView[pet.pos] && getCloser(pet.pos)) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}

	private boolean actBuy(HeroAction.Buy action) {
		int dst = action.dst;
		if (pos == dst || Dungeon.level.adjacent(pos, dst)) {

			ready();

			Heap heap = Dungeon.level.heaps.get(dst);
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				GameScene.show(new WndTradeItem(heap, true));
			}

			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actCook(HeroAction.Cook action) {
		int dst = action.dst;
		if (Dungeon.visible[dst]) {

			ready();
			AlchemyPot.operate(this, dst);
			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actCollect(HeroAction.Collect action) {
		int dst = action.dst;
		if (Dungeon.level.adjacent(pos, dst)) {
			int shrub = Dungeon.level.map[dst];
			if (shrub == Terrain.SHRUB) {
				spend(2f);
				sprite.operate(dst);
				Sample.INSTANCE.play(Assets.SND_EAT);
				if (Random.Int(10) == 0){
					Item.autocollect(Generator.random(Generator.Category.BERRY), pos);
					Level.set(dst, Terrain.BARRICADE);
					GameScene.updateMap(dst);
				} else if (Random.Int(3) == 0){
					Item.autocollect(Generator.random(Generator.Category.SEED), pos);
					Level.set(dst, Terrain.BARRICADE);
					GameScene.updateMap(dst);
				} else {
					GLog.i(Messages.get(Hero.class,"find_nothing"));
					Level.set(dst, Terrain.BARRICADE);
					GameScene.updateMap(dst);
				}
			} else if (shrub == Terrain.BOOKSHELF){
				spend(2f);
				sprite.operate(dst);
				if (Random.Int(10) == 0){
					Item.autocollect(Generator.random(Generator.Category.SCROLL), pos);
					Level.set(dst, Terrain.BARRICADE);
					GameScene.updateMap(dst);
				} else {
					GLog.i(Messages.get(Hero.class,"find_nothing"));
					Level.set(dst, Terrain.BARRICADE);
					GameScene.updateMap(dst);
				}
				if (Random.Int(25) == 0){
					if (Dungeon.hero.buff(Spectacles.MagicSight.class) != null && !Dungeon.limitedDrops.vaultpage.dropped()) {
						Item.autocollect(new Vault(), pos);
						Dungeon.limitedDrops.vaultpage.drop();
					}
				}
				if (Random.Int(25) == 0){
					if (Dungeon.hero.buff(Spectacles.MagicSight.class) != null && !Dungeon.limitedDrops.dragoncave.dropped()) {
						Item.autocollect(new DragonCave(), pos);
						Dungeon.limitedDrops.dragoncave.drop();
					}
				}
			}
			return false;
		} else if (getCloser(dst)) {
			return true;
		} else {
			ready();
			return false;
		}
	}

	private boolean actPickUp(HeroAction.PickUp action) {
		int dst = action.dst;
		if (pos == dst) {

			Heap heap = Dungeon.level.heaps.get(pos);
			if (heap != null) {
				Item item = heap.pickUp();
				if (item.doPickUp(this)) {

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal) {
						// Do Nothing
					} else {

						boolean important = ((item instanceof ScrollOfUpgrade || item instanceof ScrollOfMagicalInfusion) && ((Scroll) item)
								.isKnown())
								|| ((item instanceof PotionOfStrength || item instanceof PotionOfMight || item instanceof PotionOfHealing || item instanceof PotionOfExperience
								|| item instanceof PotionOfOverHealing) && ((Potion) item)
								.isKnown());
						if (important) {
							GLog.p(Messages.get(this, "have"), item.name());
						} else {
							GLog.i(Messages.get(this, "have"), item.name());
						}
					}
					curAction = null;
				} else {
					Dungeon.level.drop(item, pos).sprite.drop();
					ready();
				}
			} else {
				ready();
			}

			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actOpenChest(HeroAction.OpenChest action) {
		int dst = action.dst;
		if (Dungeon.level.adjacent(pos, dst) || pos == dst) {

			Heap heap = Dungeon.level.heaps.get(dst);
			if (heap != null
					&& (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) {

				theKey = null;
				theSkeletonKey = null;

				if (heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST) {

					theKey = belongings.getKey(GoldenKey.class, Dungeon.depth);
					theSkeletonKey = belongings.getKey(GoldenSkeletonKey.class, 0);

					if (theKey == null && theSkeletonKey == null) {
						GLog.w(Messages.get(this, "chestlock"));
						ready();
						return false;
					}
				}

				if (heap.type == Type.HARD_TOMB) {
					GLog.w(Messages.get(Hero.class, "hardtomb"));
					ready();
				}

				switch (heap.type) {
					case TOMB:
						Sample.INSTANCE.play(Assets.SND_TOMB);
						Camera.main.shake(1, 0.5f);
						break;
					case SKELETON:
					case REMAINS:
						break;
					default:
						Sample.INSTANCE.play(Assets.SND_UNLOCK);
				}

				spend(Key.TIME_TO_UNLOCK);
				sprite.operate(dst);

			} else {
				ready();
			}

			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actUnlock(HeroAction.Unlock action) {
		int doorCell = action.dst;
		if (Dungeon.level.adjacent(pos, doorCell)) {

			theKey = null;
			int door = Dungeon.level.map[doorCell];

			if (door == Terrain.LOCKED_DOOR) {

				theKey = belongings.getKey(IronKey.class, Dungeon.depth);

			} else if (door == Terrain.LOCKED_EXIT) {

				theKey = belongings.getKey(SkeletonKey.class, Dungeon.depth);

			}

			if (theKey != null) {

				spend(Key.TIME_TO_UNLOCK);
				sprite.operate(doorCell);

				Sample.INSTANCE.play(Assets.SND_UNLOCK);

			} else {
				GLog.w(Messages.get(this, "doorlock"));
				ready();
			}

			return false;

		} else if (getCloser(doorCell)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	public PET checkpet() {
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof PET) {
				return (PET) mob;
			}
		}
		return null;
	}

	private boolean checkpetNear() {
		for (int n : PathFinder.NEIGHBOURS8) {
			int c = pos + n;
			if (Actor.findChar(c) instanceof PET) {
				return true;
			}
		}
		return false;
	}

	private boolean actDescend(HeroAction.Descend action) {
		int stairs = action.dst;

		if (!Dungeon.level.forcedone && !Dungeon.level.cleared
				&& !Dungeon.notClearableLevel(Dungeon.depth)) {

			GameScene.show(new WndDescend());
			ready();
			return false;
		}


		if (pos == stairs && pos == Dungeon.level.exit && !Dungeon.level.sealedlevel) {

			curAction = null;

			if (!Dungeon.level.cleared && !Dungeon.notClearableLevel(Dungeon.depth)) {
				Dungeon.level.cleared = true;
				Statistics.prevfloormoves = 0;
			}

			PET pet = checkpet();
			if (pet != null && checkpetNear()) {
				Dungeon.hero.petType = pet.type;
				Dungeon.hero.petLevel = pet.level;
				Dungeon.hero.petKills = pet.kills;
				Dungeon.hero.petHP = pet.HP;
				Dungeon.hero.petExperience = pet.experience;
				Dungeon.hero.petCooldown = pet.cooldown;
				pet.destroy();
				petfollow = true;
			} else petfollow = Dungeon.hero.haspet && Dungeon.hero.petfollow;

			Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
			if (buff != null) buff.detach();

			Buff buffinv = Dungeon.hero.buff(Invisibility.class);
			if (buffinv != null)
				buffinv.detach();
			Invisibility.dispel();
			Dungeon.hero.invisible = 0;

			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
				if (mob instanceof DriedRose.GhostHero)
					mob.destroy();

			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene(InterlevelScene.class);

			return false;

		} else if (getCloser(stairs)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAscend(HeroAction.Ascend action) {
		int stairs = action.dst;
		if (pos == stairs && pos == Dungeon.level.entrance) {

			if (Dungeon.depth == 1) {

				if (belongings.getItem(Amulet.class) == null) {
					GameScene.show(new WndMessage(Messages.get(this, "leave")));
					ready();

				} else if (Dungeon.level.forcedone) {
					Dungeon.win(ResultDescriptions.WIN);
					Dungeon.deleteGame(Dungeon.hero.heroClass, true);
					Game.switchScene(SurfaceScene.class);
				} else {
					GameScene.show(new WndAscend());
					ready();
				}

			} else if (Dungeon.depth == 34) {
				curAction = null;

				Hunger hunger = buff(Hunger.class);
				if (hunger != null && !hunger.isStarving()) {
					hunger.satisfy(-Hunger.STARVING / 10);
				}

				PET pet = checkpet();
				if (pet != null && checkpetNear()) {
					Dungeon.hero.petType = pet.type;
					Dungeon.hero.petLevel = pet.level;
					Dungeon.hero.petKills = pet.kills;
					Dungeon.hero.petHP = pet.HP;
					Dungeon.hero.petExperience = pet.experience;
					Dungeon.hero.petCooldown = pet.cooldown;
					pet.destroy();
					petfollow = true;
				} else petfollow = Dungeon.hero.haspet && Dungeon.hero.petfollow;

				Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null)
					buff.detach();

				Buff buffinv = Dungeon.hero.buff(Invisibility.class);
				if (buffinv != null)
					buffinv.detach();
				Invisibility.dispel();
				Dungeon.hero.invisible = 0;

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
					if (mob instanceof DriedRose.GhostHero)
						mob.destroy();

				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);


			} else if (Dungeon.depth == 41) {
				curAction = null;

				Hunger hunger = buff(Hunger.class);
				if (hunger != null && !hunger.isStarving()) {
					hunger.satisfy(-Hunger.STARVING / 10);
				}

				PET pet = checkpet();
				if (pet != null && checkpetNear()) {
					Dungeon.hero.petType = pet.type;
					Dungeon.hero.petLevel = pet.level;
					Dungeon.hero.petKills = pet.kills;
					Dungeon.hero.petHP = pet.HP;
					Dungeon.hero.petExperience = pet.experience;
					Dungeon.hero.petCooldown = pet.cooldown;
					pet.destroy();
					petfollow = true;
				} else petfollow = Dungeon.hero.haspet && Dungeon.hero.petfollow;

				Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null)
					buff.detach();

				Buff buffinv = Dungeon.hero.buff(Invisibility.class);
				if (buffinv != null)
					buffinv.detach();
				Invisibility.dispel();
				Dungeon.hero.invisible = 0;

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
					if (mob instanceof DriedRose.GhostHero)
						mob.destroy();

				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);

			} else if (Dungeon.depth > 26 && !Dungeon.townCheck(Dungeon.depth)) {
				ready();
			} else if (Dungeon.depth == 25 || Dungeon.depth == 55 || Dungeon.depth == 99) {
				ready();
			} else if (Dungeon.depth > 55 && Dungeon.level.locked) {
				ready();
			} else {

				curAction = null;

				Hunger hunger = buff(Hunger.class);
				if (hunger != null && !hunger.isStarving()) {
					hunger.satisfy(-Hunger.STARVING / 10);
				}

				PET pet = checkpet();
				if (pet != null && checkpetNear()) {
					Dungeon.hero.petType = pet.type;
					Dungeon.hero.petLevel = pet.level;
					Dungeon.hero.petKills = pet.kills;
					Dungeon.hero.petHP = pet.HP;
					Dungeon.hero.petExperience = pet.experience;
					Dungeon.hero.petCooldown = pet.cooldown;
					pet.destroy();
					petfollow = true;
				} else petfollow = Dungeon.hero.haspet && Dungeon.hero.petfollow;

				Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null)
					buff.detach();

				Buff buffinv = Dungeon.hero.buff(Invisibility.class);
				if (buffinv != null)
					buffinv.detach();
				Invisibility.dispel();
				Dungeon.hero.invisible = 0;

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
					if (mob instanceof DriedRose.GhostHero)
						mob.destroy();

				try {
					Dungeon.saveAll();
				} catch (IOException e) {
				}
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}

			return false;

		} else if (getCloser(stairs)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAttack(HeroAction.Attack action) {

		enemy = action.target;

		boolean inRange = belongings.weapon != null ?
				Dungeon.level.distance(pos, enemy.pos) <= belongings.weapon.reachFactor(this)
				: Dungeon.level.adjacent(pos, enemy.pos);

		if (inRange && enemy.isAlive() && !isCharmedBy(enemy)) {

			spend(attackDelay());
			sprite.attack(enemy.pos);

			return false;

		} else {

			if (Level.fieldOfView[enemy.pos] && getCloser(enemy.pos)) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}

	public void rest(boolean fullRest) {
		//search(true);
		spendAndNext(TIME_TO_REST);
		if (!fullRest) {
			sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "wait"));
		}
		resting = fullRest;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon
				: belongings.weapon;

		if (wep != null && !(wep instanceof Wand))
			wep.proc(this, enemy, damage);

		switch (subClass) {
			case GLADIATOR:
				if (wep instanceof MeleeWeapon || wep == null) {
					damage += Buff.affect(this, Combo.class).hit(enemy, damage);
				}
				break;
			case BATTLEMAGE:
				if (wep instanceof Wand) {
					Wand wand = (Wand) wep;
					wand.proc(this, enemy, damage);

					if (wand.curCharges < wand.maxCharges && damage > 0) {

						wand.curCharges++;
						if (Dungeon.quickslot.contains(wand)) {
							QuickSlotButton.refresh();
						}

						ScrollOfRecharging.charge(this);
					}
					damage += wand.curCharges;
				}
				break;
			case SNIPER:
				if (rangedWeapon != null) {
					Buff.prolong(this, SnipersMark.class, attackDelay() * 1.1f).object = enemy.id();
				}
				break;
			default:
		}

		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage) {

		Earthroot.Armor armor = buff(Earthroot.Armor.class);
		if (armor != null) {
			damage = armor.absorb(damage);
		}

		Sungrass.Health health = buff(Sungrass.Health.class);
		if (health != null) {
			health.absorb(damage);
		}

		if (belongings.armor != null) {
			damage = belongings.armor.proc(enemy, this, damage);
		}

		return damage;
	}

	@Override
	public void damage(int dmg, Object src) {
		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt) {
			interrupt();
			resting = false;
		}
		if (this.buff(Drowsy.class) != null) {
			Buff.detach(this, Drowsy.class);
			GLog.w(Messages.get(this, "nosleep"));
		}

		CapeOfThorns.Thorns thorns = buff(CapeOfThorns.Thorns.class);
		if (thorns != null) {
			dmg = thorns.proc(dmg, (src instanceof Char ? (Char) src : null), this);
		}

		int tenacity = 0;
		for (Buff buff : buffs(RingOfTenacity.Tenacity.class)) {
			tenacity += ((RingOfTenacity.Tenacity) buff).level;
		}
		if (tenacity != 0) // (HT - HP)/HT = heroes current % missing health.
			dmg = (int) Math.ceil(dmg
					* Math.pow(0.9, tenacity * ((float) (HT - HP) / HT)));

		super.damage(dmg, src);

		if (subClass == HeroSubClass.BERSERKER && 0 < HP
				&& HP <= HT * Fury.LEVEL) {
			Buff.affect(this, Fury.class);
		}

		if (this.buff(AutoHealPotion.class) != null && ((float) HP / HT) < .1) {
			PotionOfHealing pot = Dungeon.hero.belongings.getItem(PotionOfHealing.class);
			if (pot != null) {
				pot.detach(Dungeon.hero.belongings.backpack, 1);
				GLog.w(Messages.get(this, "autopotion"));
				pot.apply(this);
			} else if (pot == null) {
				GLog.w(Messages.get(this, "nohealing"));
			}

		}

	}

	private void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;
		Mob target = null;

		for (Mob m : Dungeon.level.mobs) {
			if (Level.fieldOfView[m.pos] && m.hostile) {
				visible.add(m);
				if (!visibleEnemies.contains(m)) {
					newMob = true;
				}
				if (!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1) {
					if (target == null) {
						target = m;
					} else if (distance(target) > distance(m)) {
						target = m;
					}
				}
			}
		}

		if (target != null && (QuickSlotButton.lastTarget == null ||
				!QuickSlotButton.lastTarget.isAlive() ||
				!Dungeon.visible[QuickSlotButton.lastTarget.pos])) {
			QuickSlotButton.target(target);
		}

		if (newMob) {
			interrupt();
			resting = false;
		}

		visibleEnemies = visible;
	}

	public int visibleEnemies() {
		return visibleEnemies.size();
	}

	public Mob visibleEnemy(int index) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}

	private boolean getCloser(final int target) {

		if (target == pos)
			return false;

		if (rooted) {
			Camera.main.shake(1, 1f);
			return false;
		}

		int step = -1;

		if (Dungeon.level.adjacent(pos, target)) {

			path = null;

			if (Actor.findChar(target) == null) {
				if (Level.pit[target] && !flying && !Level.solid[target]) {
					if (!Chasm.jumpConfirmed) {
						Chasm.heroJump(this);
						interrupt();
					} else {
						Chasm.heroFall(target);
					}
					return false;
				}
				if (Level.passable[target] || Level.avoid[target]) {
					step = target;
				}
			}

		} else {

			boolean newPath = false;
			if (path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
				newPath = true;
			else if (path.getLast() != target)
				newPath = true;
			else {
				//looks ahead for path validity, up to length-1 or 2.
				//Note that this is shorter than for mobs, so that mobs usually yield to the hero
				int lookAhead = (int) GameMath.gate(0, path.size() - 1, 2);
				for (int i = 0; i < lookAhead; i++) {
					int cell = path.get(i);
					if (!Level.passable[cell] || (Dungeon.visible[cell] && Actor.findChar(cell) != null)) {
						newPath = true;
						break;
					}
				}
			}

			if (newPath) {

				int len = Dungeon.level.getLength();
				boolean[] p = Level.passable;
				boolean[] v = Dungeon.level.visited;
				boolean[] m = Dungeon.level.mapped;
				boolean[] passable = new boolean[len];
				for (int i = 0; i < len; i++) {
					passable[i] = p[i] && (v[i] || m[i]);
				}

				path = Dungeon.findPath(this, pos, target, passable, Level.fieldOfView);
			}

			if (path == null) return false;
			step = path.removeFirst();

		}

		if (step != -1) {

			sprite.move(pos, step);
			move(step);
			spend(1 / speed());

			search(false);

			return true;

		} else {

			return false;

		}

	}

	public boolean handle(int cell) {

		if (cell == -1) {
			return false;
		}

		Char ch;
		Heap heap;

		if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {

			curAction = new HeroAction.Cook(cell);

		} else if (Level.fieldOfView[cell]
				&& (ch = Actor.findChar(cell)) instanceof Mob) {

			if (ch instanceof NPC) {
				curAction = new HeroAction.Interact((NPC) ch);
			} else if (ch instanceof PET) {
				curAction = new HeroAction.InteractPet((PET) ch);
			} else {
				curAction = new HeroAction.Attack(ch);
			}

		} else if ((heap = Dungeon.level.heaps.get(cell)) != null
				//moving to an item doesn't auto-pickup when enemies are near...
				&& (visibleEnemies.size() == 0 || cell == pos ||
				//...but only for standard heaps, chests and similar open as normal.
				(heap.type != Type.HEAP && heap.type != Type.FOR_SALE))) {

			switch (heap.type) {
				case HEAP:
					curAction = new HeroAction.PickUp(cell);
					break;
				case FOR_SALE:
					curAction = heap.size() == 1 && heap.peek().price() > 0 ? new HeroAction.Buy(
							cell) : new HeroAction.PickUp(cell);
					break;
				default:
					curAction = new HeroAction.OpenChest(cell);
			}

		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR
				|| Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {

			curAction = new HeroAction.Unlock(cell);

		} else if (cell == Dungeon.level.exit && (Dungeon.depth < 26 || Dungeon.townCheck(Dungeon.depth))) {

			curAction = new HeroAction.Descend(cell);

		} else if (cell == Dungeon.level.entrance) {

			curAction = new HeroAction.Ascend(cell);

		} else if ((Dungeon.level.map[cell] == Terrain.SHRUB && Dungeon.depth == 27)
				|| Dungeon.level.map[cell] == Terrain.BOOKSHELF){

			curAction = new HeroAction.Collect(cell);

		} else {

			curAction = new HeroAction.Move(cell);
			lastAction = null;

		}

		return true;
	}

	public void earnExp(int exp) {

		this.exp += exp;

		boolean levelUp = false;
		while (this.exp >= maxExp()) {
			this.exp -= maxExp();
			lvl++;

			HT += 5;
			HP += 5;
			attackSkill++;
			defenseSkill++;

			if (lvl < 10) {
				updateAwareness();
			}

			levelUp = true;
		}

		if (levelUp) {

			GLog.p(Messages.get(this, "newlevel"), lvl);
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "levelup"));
			Sample.INSTANCE.play(Assets.SND_LEVELUP);

			int value = HT - HP;
			if (value > 0) {
				HP += value;
				sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}

			if (subClass == HeroSubClass.WARLOCK) {

				int value2 = lvl;
				if (value2 > 0) {
					if (HP < HT + value2) {
						HP = HT + value2;
						sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
						GLog.p(Messages.get(this, "overfill"), lvl);
					}
				}
			}
		}

		if (subClass == HeroSubClass.WARLOCK) {

			int value = Math.min(HT - HP, 1 + (Dungeon.depth - 1) / 5);
			if (value > 0) {
				HP += value;
				sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}
			buff(Hunger.class).satisfy(100);
		}

		if (buff(HornOfPlenty.hornRecharge.class) != null) {
			if (buff(HornOfPlenty.hornRecharge.class).level() > 30) {
				buff(Hunger.class).satisfy(exp * 5);
			}
		}
	}

	public int maxExp() {
		return 5 + lvl * 5;
	}

	void updateAwareness() {
		awareness = (float) (1 - Math.pow((heroClass == HeroClass.ROGUE ? 0.85
				: 0.90), (1 + Math.min(lvl, 9)) * 0.5));
	}

	public boolean isStarving() {
		return buff(Hunger.class) != null
				&& buff(Hunger.class).isStarving();
	}

	@Override
	public void add(Buff buff) {

		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		super.add(buff);

		if (sprite != null) {
			if (buff instanceof Burning) {
				GLog.w(Messages.get(this, "fire"));
				interrupt();
			} else if (buff instanceof Paralysis) {
				GLog.w(Messages.get(this, "paralysis"));
				interrupt();
			} else if (buff instanceof Poison) {
				GLog.w(Messages.get(this, "poison"));
				interrupt();
			} else if (buff instanceof Ooze) {
				GLog.w(Messages.get(this, "ooze"));
			} else if (buff instanceof Roots) {
				GLog.w(Messages.get(this, "roots"));
			} else if (buff instanceof Weakness) {
				GLog.w(Messages.get(this, "weakness"));
			} else if (buff instanceof Blindness) {
				GLog.w(Messages.get(this, "blindness"));
			} else if (buff instanceof Fury) {
				GLog.w(Messages.get(this, "fury"));
				sprite.showStatus(CharSprite.POSITIVE, Messages.get(Fury.class, "name"));
			} else if (buff instanceof Charm) {
				GLog.w(Messages.get(this, "charm"));
			} else if (buff instanceof Cripple) {
				GLog.w(Messages.get(this, "cripple"));
			} else if (buff instanceof Bleeding) {
				GLog.w(Messages.get(this, "bleeding"));
			} else if (buff instanceof RingOfMight.Might) {
				if (((RingOfMight.Might) buff).level > 0) {
					HT += ((RingOfMight.Might) buff).level * 5;
				}
			} else if (buff instanceof Vertigo) {
				GLog.w(Messages.get(this, "vertigo"));
				interrupt();
			} else if (buff instanceof Light) {
				sprite.add(CharSprite.State.ILLUMINATED);
			}
		}

		BuffIndicator.refreshHero();
	}

	@Override
	public void remove(Buff buff) {
		super.remove(buff);

		if (buff instanceof Light) {
			sprite.remove(CharSprite.State.ILLUMINATED);
		} else if (buff instanceof RingOfMight.Might) {
			if (((RingOfMight.Might) buff).level > 0) {
				HT -= ((RingOfMight.Might) buff).level * 5;
				HP = Math.min(HT, HP);
			}
		}

		BuffIndicator.refreshHero();
	}

	@Override
	public int stealth() {
		int stealth = super.stealth();
		for (Buff buff : buffs(RingOfEvasion.Evasion.class)) {
			stealth += ((RingOfEvasion.Evasion) buff).effectiveLevel;
		}
		return stealth;
	}

	@Override
	public void die(Object cause) {

		curAction = null;

		Ankh ankh = null;

		// look for ankhs in player inventory, prioritize ones which are
		// blessed.
		for (Item item : belongings) {
			if (item instanceof Ankh) {
				if (ankh == null || ((Ankh) item).isBlessed()) {
					ankh = (Ankh) item;
				}
			}
		}

		if (ankh != null && ankh.isBlessed()) {
			this.HP = HT;

			Buff.detach(this, Paralysis.class);
			spend(-cooldown());

			new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
			CellEmitter.get(this.pos)
					.start(Speck.factory(Speck.LIGHT), 0.2f, 3);

			ankh.detach(belongings.backpack);

			Sample.INSTANCE.play(Assets.SND_TELEPORT);
			GLog.w(Ankh.TXT_REVIVE);
			Statistics.ankhsUsed++;

			return;
		}

		Actor.fixTime();
		super.die(cause);

		if (ankh == null) {

			reallyDie(cause);

		} else {

			ankh.detach(belongings.backpack);
			Dungeon.deleteGame(Dungeon.hero.heroClass, false);
			GameScene.show(new WndResurrect(ankh, cause));

		}
	}

	public static void reallyDie(Object cause) {

		int length = Dungeon.level.getLength();
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Level.discoverable;

		for (int i = 0; i < length; i++) {

			int terr = map[i];

			if (discoverable[i]) {

				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					Level.set(i, Terrain.discover(terr));
					GameScene.updateMap(i);
				}
			}
		}

		Bones.leave();

		Dungeon.observe();
		GameScene.updateFog();

		Dungeon.hero.belongings.identify();

		int pos = Dungeon.hero.pos;

		ArrayList<Integer> passable = new ArrayList<Integer>();
		for (Integer ofs : PathFinder.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((Level.passable[cell] || Level.avoid[cell])
					&& Dungeon.level.heaps.get(cell) == null) {
				passable.add(cell);
			}
		}
		Collections.shuffle(passable);

		ArrayList<Item> items = new ArrayList<Item>(
				Dungeon.hero.belongings.backpack.items);
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}

			Item item = Random.element(items);
			Dungeon.level.drop(item, cell).sprite.drop(pos);
			items.remove(item);
		}

		GameScene.gameOver();

		if (cause instanceof Hero.Doom) {
			((Hero.Doom) cause).onDeath();
		}

		Dungeon.deleteGame(Dungeon.hero.heroClass, true);
	}

	@Override
	public void move(int step) {
		super.move(step);

		if (!flying) {

			if (Level.water[pos]) {
				Sample.INSTANCE.play(Assets.SND_WATER, 1, 1,
						Random.Float(0.8f, 1.25f));
			} else {
				Sample.INSTANCE.play(Assets.SND_STEP);
			}
			Dungeon.level.press(pos, this);
		}

		if (buff(LichenDrop.class) != null) {
			Lichen.spawnAroundChance(pos);
		}
	}

	@Override
	public void onAttackComplete() {

		AttackIndicator.target(enemy);

		attack(enemy);
		curAction = null;

		Invisibility.dispel();

		super.onAttackComplete();
	}

	@Override
	public void onOperateComplete() {

		if (curAction instanceof HeroAction.Unlock) {

			if (theKey != null) {
				theKey.detach(belongings.backpack);
				theKey = null;
			}

			int doorCell = ((HeroAction.Unlock) curAction).dst;
			int door = Dungeon.level.map[doorCell];

			Level.set(doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR
					: Terrain.UNLOCKED_EXIT);
			GameScene.updateMap(doorCell);

		} else if (curAction instanceof HeroAction.OpenChest) {

			if (theKey != null) {
				theKey.detach(belongings.backpack);
				theKey = null;
			} else if (theKey == null && theSkeletonKey != null) {
				theSkeletonKey.detach(belongings.backpack);
				theSkeletonKey = null;
			}

			Heap heap = Dungeon.level.heaps
					.get(((HeroAction.OpenChest) curAction).dst);
			if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
				Sample.INSTANCE.play(Assets.SND_BONES);
			}
			heap.open(this);
		}
		curAction = null;

		super.onOperateComplete();
	}

	public boolean search(boolean intentional) {

		boolean smthFound = false;

		int positive = 0;
		int negative = 0;

		int distance = 1 + positive + negative;

		float level = intentional ? (2 * awareness - awareness * awareness)
				: awareness;
		if (distance <= 0) {
			level /= 2 - distance;
			distance = 1;
		}

		int cx = pos % Dungeon.level.getWidth();
		int cy = pos / Dungeon.level.getWidth();
		int ax = cx - distance;
		if (ax < 0) {
			ax = 0;
		}
		int bx = cx + distance;
		if (bx >= Dungeon.level.getWidth()) {
			bx = Dungeon.level.getWidth() - 1;
		}
		int ay = cy - distance;
		if (ay < 0) {
			ay = 0;
		}
		int by = cy + distance;
		if (by >= Dungeon.level.getHeight()) {
			by = Dungeon.level.getHeight() - 1;
		}

		TalismanOfForesight.Foresight foresight = buff(TalismanOfForesight.Foresight.class);

		// cursed talisman of foresight makes unintentionally finding things
		// impossible.
		if (foresight != null && foresight.isCursed()) {
			level = -1;
		}

		for (int y = ay; y <= by; y++) {
			for (int x = ax, p = ax + y * Dungeon.level.getWidth(); x <= bx; x++, p++) {

				if (Dungeon.visible[p] && p != pos) {

					if (intentional) {
						sprite.parent.addToBack(new CheckedCell(p));
					}

					if (Level.secret[p]
							&& (intentional || Random.Float() < level)) {

						int oldValue = Dungeon.level.map[p];

						GameScene.discoverTile(p, oldValue);

						Level.set(p, Terrain.discover(oldValue));

						GameScene.updateMap(p);

						ScrollOfMagicMapping.discover(p);

						smthFound = true;

						if (foresight != null && !foresight.isCursed())
							foresight.charge();
					}
				}
			}
		}

		if (intentional) {
			sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "search"));
			sprite.operate(pos);
			if (foresight != null && foresight.isCursed()) {
				GLog.n(Messages.get(this, "nocon"));
				spendAndNext(TIME_TO_SEARCH * 3);
			} else {
				spendAndNext(TIME_TO_SEARCH);
			}

		}

		if (smthFound) {
			GLog.w(Messages.get(this, "notice"));
			Sample.INSTANCE.play(Assets.SND_SECRET);
			interrupt();
		}

		return smthFound;
	}

	public void resurrect(int resetLevel) {

		HP = HT;
		Dungeon.gold = 0;
		exp = 0;

		belongings.resurrect(resetLevel);

		live();
	}

	@Override
	public HashSet<Class<?>> resistances() {
		RingOfElements.Resistance r = buff(RingOfElements.Resistance.class);
		return r == null ? super.resistances() : r.resistances();
	}

	@Override
	public HashSet<Class<?>> immunities() {
		HashSet<Class<?>> immunities = new HashSet<Class<?>>();
		for (Buff buff : buffs()) {
			for (Class<?> immunity : buff.immunities)
				immunities.add(immunity);
		}
		return immunities;
	}

	@Override
	public void next() {
		super.next();
	}

	public interface Doom {
		void onDeath();
	}
}
