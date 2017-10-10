package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ConfusionGas;
import com.github.epd.sprout.actors.blobs.CorruptGas;
import com.github.epd.sprout.actors.blobs.ParalyticGas;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Ooze;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.RedWraith;
import com.github.epd.sprout.actors.mobs.npcs.NPC;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.ShaftParticle;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.GhostSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndQuest;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class DriedRose extends Artifact {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ARTIFACT_ROSE1;

		level = 0;
		levelCap = 10;

		charge = 100;
		chargeCap = 100;

		reinforced = true;

		defaultAction = AC_SUMMON;
	}

	protected static boolean talkedTo = false;
	protected static boolean firstSummon = false;
	protected static boolean spawned = false;

	public int droppedPetals = 0;

	public static final String AC_SUMMON = Messages.get(DriedRose.class, "ac_summon");

	public DriedRose() {
		super();
		talkedTo = firstSummon = spawned = false;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge == chargeCap && !cursed)
			actions.add(AC_SUMMON);
		return actions;
	}

	@Override
	public boolean isUpgradable() {
		return true;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_SUMMON)) {

			if (spawned)
				GLog.n(Messages.get(this, "spawned"));
			else if (!isEquipped(hero))
				GLog.i(Messages.get(this, "equip"));
			else if (charge != chargeCap)
				GLog.i(Messages.get(this, "no_charge"));
			else if (cursed)
				GLog.i(Messages.get(this, "cursed"));
			else {
				ArrayList<Integer> spawnPoints = new ArrayList<Integer>();
				for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
					int p = hero.pos + PathFinder.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null
							&& (Level.passable[p] || Level.avoid[p])) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					GhostHero ghost = new GhostHero(level);
					ghost.pos = Random.element(spawnPoints);

					GameScene.add(ghost, 1f);
					CellEmitter.get(ghost.pos).start(ShaftParticle.FACTORY,
							0.3f, 4);
					CellEmitter.get(ghost.pos).start(
							Speck.factory(Speck.LIGHT), 0.2f, 3);

					hero.spend(1f);
					hero.busy();
					hero.sprite.operate(hero.pos);

					if (!firstSummon) {
						ghost.yell(Messages.get(DriedRose.class, "hello", Dungeon.hero.givenName()));
						Sample.INSTANCE.play(Assets.SND_GHOST);
						firstSummon = true;
					} else
						ghost.saySpawned();

					spawned = true;
					charge = 0;
					updateQuickslot();

				} else
					GLog.i(Messages.get(this, "no_space"));
			}

		} else {
			super.execute(hero, action);
		}
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc");

		if (isEquipped(Dungeon.hero)) {
			if (!cursed) {
				desc += Messages.get(this, "warm");
				desc += Messages.get(this, "desc_hint");
			} else
				desc += Messages.get(this, "desc_cursed");
		}

		return desc;
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new roseRecharge();
	}

	@Override
	public Item upgrade() {
		if (level >= 9)
			image = ItemSpriteSheet.ARTIFACT_ROSE3;
		else if (level >= 4)
			image = ItemSpriteSheet.ARTIFACT_ROSE2;

		// For upgrade transferring via well of transmutation
		droppedPetals = Math.max(level, droppedPetals);

		return super.upgrade();
	}

	private static final String TALKEDTO = "talkedto";
	private static final String FIRSTSUMMON = "firstsummon";
	private static final String SPAWNED = "spawned";
	private static final String PETALS = "petals";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put(TALKEDTO, talkedTo);
		bundle.put(FIRSTSUMMON, firstSummon);
		bundle.put(SPAWNED, spawned);
		bundle.put(PETALS, droppedPetals);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		talkedTo = bundle.getBoolean(TALKEDTO);
		firstSummon = bundle.getBoolean(FIRSTSUMMON);
		spawned = bundle.getBoolean(SPAWNED);
		droppedPetals = bundle.getInt(PETALS);
	}

	public class roseRecharge extends ArtifactBuff {

		@Override
		public boolean act() {

			if (charge < chargeCap && !cursed) {

				partialCharge += 10 / 75f;
				if (partialCharge > 1) {
					charge++;
					partialCharge--;
					if (charge == chargeCap) {
						partialCharge = 0f;
						GLog.p(Messages.get(DriedRose.class, "charged"));
					}
				}
			} else if (cursed && Random.Int(100) == 0) {

				ArrayList<Integer> spawnPoints = new ArrayList<Integer>();

				for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
					int p = target.pos + PathFinder.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null
							&& (Level.passable[p] || Level.avoid[p])) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					RedWraith.spawnAt(Random.element(spawnPoints));
					Sample.INSTANCE.play(Assets.SND_CURSED);
				}

			}

			updateQuickslot();

			spend(TICK);

			return true;
		}
	}

	public static class Petal extends Item {

		{
			name = Messages.get(this, "name");
			stackable = true;
			image = ItemSpriteSheet.PETAL;
		}

		@Override
		public boolean doPickUp(Hero hero) {
			DriedRose rose = hero.belongings.getItem(DriedRose.class);

			if (rose == null) {
				GLog.w(Messages.get(this, "no_rose"));
				return false;
			}
			if (rose.level >= rose.levelCap) {
				GLog.i(Messages.get(this, "no_room"));
				hero.spendAndNext(TIME_TO_PICK_UP);
				return true;
			} else {

				rose.upgrade();
				if (rose.level == rose.levelCap) {
					GLog.p(Messages.get(this, "maxlevel"));
					Sample.INSTANCE.play(Assets.SND_GHOST);
					GLog.n(Messages.get(DriedRose.class, "ghost"));
				} else
					GLog.i(Messages.get(this, "levelup"));

				Sample.INSTANCE.play(Assets.SND_DEWDROP);
				hero.spendAndNext(TIME_TO_PICK_UP);
				return true;

			}
		}

		@Override
		public String info() {
			return Messages.get(this, "desc");
		}

	}

	public static class GhostHero extends NPC {

		{
			name = Messages.get(this, "name");
			spriteClass = GhostSprite.class;

			flying = true;

			state = WANDERING;
			enemy = null;

			ally = true;
		}

		public GhostHero() {
			super();

			// double heroes defence skill
			defenseSkill = (Dungeon.hero.lvl + 4) * 2;
		}

		public GhostHero(int roseLevel) {
			this();
			HP = HT = 10 + roseLevel * 4;
		}

		public void saySpawned() {
			int i = (Dungeon.depth - 1) / 5;
			if (!(Dungeon.depth >= 26)) {
				if (chooseEnemy() == null)
					yell(Random.element(VOICE_AMBIENT[i]));
				else
					yell(Random.element(VOICE_ENEMIES[i][Dungeon.bossLevel() ? 1
							: 0]));
				Sample.INSTANCE.play(Assets.SND_GHOST);
			} else {
				Sample.INSTANCE.play(Assets.SND_GHOST);
			}
		}

		public void sayAnkh() {
			yell(Random.element(VOICE_BLESSEDANKH));
			Sample.INSTANCE.play(Assets.SND_GHOST);
		}

		public void sayDefeated() {
			yell(Random.element(VOICE_DEFEATED[Dungeon.bossLevel() ? 1 : 0]));
			Sample.INSTANCE.play(Assets.SND_GHOST);
		}

		public void sayHeroKilled() {
			yell(Random.element(VOICE_HEROKILLED));
			Sample.INSTANCE.play(Assets.SND_GHOST);
		}

		public void sayBossBeaten() {
			yell(Random.element(VOICE_BOSSBEATEN[Dungeon.depth == 25 ? 1 : 0]));
			Sample.INSTANCE.play(Assets.SND_GHOST);
		}

		@Override
		public String defenseVerb() {
			return Messages.get(this, "def_verb");
		}

		@Override
		protected boolean act() {
			if (Random.Int(20) == 0)
				damage(1, this);
			if (!isAlive())
				return true;
			if (!Dungeon.hero.isAlive()) {
				sayHeroKilled();
				sprite.die();
				destroy();
				return true;
			}
			return super.act();
		}

		@Override
		protected boolean getCloser(int target) {
			if (state == WANDERING
					|| Level.distance(target, Dungeon.hero.pos) > 6)
				this.target = target = Dungeon.hero.pos;
			return super.getCloser(target);
		}

		@Override
		protected Char chooseEnemy() {
			if (enemy == null || !enemy.isAlive() || state == WANDERING) {

				HashSet<Mob> enemies = new HashSet<Mob>();
				for (Mob mob : Dungeon.level.mobs) {
					if (mob.hostile && Level.fieldOfView[mob.pos]
							&& mob.state != mob.PASSIVE) {
						enemies.add(mob);
					}
				}
				enemy = enemies.size() > 0 ? Random.element(enemies) : null;
			}
			return enemy;
		}

		@Override
		public int attackSkill(Char target) {
			// same accuracy as the hero.
			return (defenseSkill / 2) + 5;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(HT - 7, 8 + 5 * (HT - 10));
		}

		@Override
		public int dr() {
			// defence is equal to the level of rose.
			return HT - 10;
		}

		@Override
		public void add(Buff buff) {
			// in other words, can't be directly affected by buffs/debuffs.
		}

		@Override
		public boolean interact() {
			if (!DriedRose.talkedTo) {
				DriedRose.talkedTo = true;
				GameScene.show(new WndQuest(this, VOICE_INTRODUCE));
				return false;
			} else {
				int curPos = pos;

				moveSprite(pos, Dungeon.hero.pos);
				move(Dungeon.hero.pos);

				Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
				Dungeon.hero.move(curPos);

				Dungeon.hero.spend(1 / Dungeon.hero.speed());
				Dungeon.hero.busy();
				return true;
			}
		}

		@Override
		public void die(Object cause) {
			sayDefeated();
			super.die(cause);
		}

		@Override
		public void destroy() {
			DriedRose.spawned = false;
			super.destroy();
		}

		@Override
		public String description() {
			return Messages.get(this, "desc");
		}

		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

		static {
			IMMUNITIES.add(ToxicGas.class);
			IMMUNITIES.add(ScrollOfPsionicBlast.class);
			IMMUNITIES.add(CorruptGas.class);
			IMMUNITIES.add(Ooze.class);
			IMMUNITIES.add(ConfusionGas.class);
			IMMUNITIES.add(ParalyticGas.class);
			IMMUNITIES.add(Roots.class);
		}

		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}

		// ************************************************************************************
		// This is a bunch strings & string arrays, used in all of the sad
		// ghost's voice lines.
		// ************************************************************************************

		private static final String VOICE_INTRODUCE = Messages.get(DriedRose.class, "1");

		// 1st index - depth type, 2nd index - specific line.
		public static final String[][] VOICE_AMBIENT = {
				{Messages.get(DriedRose.class, "2")},
				{Messages.get(DriedRose.class, "3")},
				{Messages.get(DriedRose.class, "4")},
				{Messages.get(DriedRose.class, "5")},
				{Messages.get(DriedRose.class, "6")},
				{Messages.get(DriedRose.class, "7")}};

		// 1st index - depth type, 2nd index - boss or not, 3rd index - specific
		// line.
		public static final String[][][] VOICE_ENEMIES = {
				{{Messages.get(DriedRose.class, "8")}, {Messages.get(DriedRose.class, "9")}},
				{{Messages.get(DriedRose.class, "10")}, {Messages.get(DriedRose.class, "11")}},
				{{Messages.get(DriedRose.class, "12")}, {Messages.get(DriedRose.class, "13")}},
				{{Messages.get(DriedRose.class, "14")}, {Messages.get(DriedRose.class, "15")}},
				{{Messages.get(DriedRose.class, "16")}, {Messages.get(DriedRose.class, "17")}},
				{{Messages.get(DriedRose.class, "18")},
						{"Hello source viewer, I'm writing this here as this line should never trigger. Have a nice day!"}}};

		// 1st index - Yog or not, 2nd index - specific line.
		public static final String[][] VOICE_BOSSBEATEN = {
				{Messages.get(DriedRose.class, "19")},
				{Messages.get(DriedRose.class, "20")}};

		// 1st index - boss or not, 2nd index - specific line.
		public static final String[][] VOICE_DEFEATED = {
				{Messages.get(DriedRose.class, "21"), Messages.get(DriedRose.class, "22"),
						Messages.get(DriedRose.class, "23")},
				{Messages.get(DriedRose.class, "24"), Messages.get(DriedRose.class, "25"),
						Messages.get(DriedRose.class, "26")}};

		public static final String[] VOICE_HEROKILLED = {Messages.get(DriedRose.class, "27"),
				Messages.get(DriedRose.class, "28"), Messages.get(DriedRose.class, "29")};

		public static final String[] VOICE_BLESSEDANKH = {Messages.get(DriedRose.class, "30"),
				Messages.get(DriedRose.class, "31"), Messages.get(DriedRose.class, "32")};
	}
}
