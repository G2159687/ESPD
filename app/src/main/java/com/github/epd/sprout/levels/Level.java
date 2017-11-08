
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Alchemy;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.WellWater;
import com.github.epd.sprout.actors.buffs.Awareness;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MindVision;
import com.github.epd.sprout.actors.buffs.Shadows;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokoban;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanCorner;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanSwitch;
import com.github.epd.sprout.actors.mobs.pets.BlueDragon;
import com.github.epd.sprout.actors.mobs.pets.Bunny;
import com.github.epd.sprout.actors.mobs.pets.Fairy;
import com.github.epd.sprout.actors.mobs.pets.GreenDragon;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.actors.mobs.pets.RedDragon;
import com.github.epd.sprout.actors.mobs.pets.Scorpion;
import com.github.epd.sprout.actors.mobs.pets.ShadowDragon;
import com.github.epd.sprout.actors.mobs.pets.Spider;
import com.github.epd.sprout.actors.mobs.pets.SugarplumFairy;
import com.github.epd.sprout.actors.mobs.pets.Velocirooster;
import com.github.epd.sprout.actors.mobs.pets.VioletDragon;
import com.github.epd.sprout.actors.mobs.pets.bee;
import com.github.epd.sprout.effects.Pushing;
import com.github.epd.sprout.effects.particles.FlowParticle;
import com.github.epd.sprout.effects.particles.WindParticle;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.StoneOre;
import com.github.epd.sprout.items.consumables.Stylus;
import com.github.epd.sprout.items.consumables.Torch;
import com.github.epd.sprout.items.artifacts.AlchemistsToolkit;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.food.Blandfruit;
import com.github.epd.sprout.items.potions.PotionOfMight;
import com.github.epd.sprout.items.potions.PotionOfStrength;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicalInfusion;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.levels.features.Chasm;
import com.github.epd.sprout.levels.features.Door;
import com.github.epd.sprout.levels.features.HighGrass;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.traps.AlarmTrap;
import com.github.epd.sprout.levels.traps.ChangeSheepTrap;
import com.github.epd.sprout.levels.traps.FireTrap;
import com.github.epd.sprout.levels.traps.FleecingTrap;
import com.github.epd.sprout.levels.traps.GrippingTrap;
import com.github.epd.sprout.levels.traps.HeapGenTrap;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.levels.traps.ParalyticTrap;
import com.github.epd.sprout.levels.traps.PoisonTrap;
import com.github.epd.sprout.levels.traps.SummoningTrap;
import com.github.epd.sprout.levels.traps.ToxicTrap;
import com.github.epd.sprout.mechanics.ShadowCaster;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.BlandfruitBush;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.utils.BArray;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Level implements Bundlable {

	public enum Feeling {
		NONE, CHASM, WATER, GRASS, DARK
	}

	/*  -W-1 -W  -W+1
	 *  -1    P  +1
	 *  W-1   W  W+1
	 * 
	 */

	protected int WIDTH;
	protected int HEIGHT;
	protected int LENGTH = WIDTH * HEIGHT;

	protected static final float TIME_TO_RESPAWN = 50;
	protected static final int REGROW_TIMER = 10;
	protected static final int DROP_TIMER = 10;
	protected static final int PET_TICK = 1;

	private static final String TXT_HIDDEN_PLATE_CLICKS = Messages.get(Level.class, "hidden_plate");
	public static boolean first;

	public int[] map;
	public boolean[] visited;
	public boolean[] mapped;

	public int movepar = 0;
	public int currentmoves = 0;
	public boolean genpetnext = false;

	public int viewDistance = 8;

	public static boolean[] fieldOfView;

	public static boolean[] passable;
	public static boolean[] losBlocking;
	public static boolean[] flamable;
	public static boolean[] secret;
	public static boolean[] solid;
	public static boolean[] avoid;
	public static boolean[] water;
	public static boolean[] pit;

	public static boolean[] discoverable;

	public Feeling feeling = Feeling.NONE;


	public int entrance;
	public int exit;

	// when a boss level has become locked.
	public boolean locked = false;
	public boolean special = false;
	public boolean cleared = false;
	public boolean forcedone = false;
	public boolean sealedlevel = false;

	public HashSet<Mob> mobs;
	public SparseArray<Heap> heaps;
	public HashMap<Class<? extends Blob>, Blob> blobs;
	public SparseArray<Plant> plants;

	protected ArrayList<Item> itemsToSpawn = new ArrayList<Item>();

	public int color1 = 0x004400;
	public int color2 = 0x88CC44;

	public static boolean pitRoomNeeded = false;
	public static boolean weakFloorCreated = false;
	public boolean reset = false;

	private static final String MAP = "map";
	private static final String VISITED = "visited";
	private static final String MAPPED = "mapped";
	private static final String ENTRANCE = "entrance";
	private static final String EXIT = "exit";
	private static final String LOCKED = "locked";
	private static final String HEAPS = "heaps";
	private static final String PLANTS = "plants";
	private static final String MOBS = "mobs";
	private static final String BLOBS = "blobs";
	private static final String FEELING = "feeling";
	private static final String PITSIGN = "pitSign";
	private static final String MOVES = "currentmoves";
	private static final String CLEARED = "cleared";
	private static final String RESET = "reset";
	private static final String FORCEDONE = "forcedone";
	private static final String GENPETNEXT = "genpetnext";
	private static final String SEALEDLEVEL = "sealedlevel";
	private static final String VIEW = "viewdistance";


	public void create() {

		if (!Dungeon.bossLevel()) {
			addItemToSpawn(Generator.random(Generator.Category.FOOD));
			addItemToSpawn(Generator.random(Generator.Category.FOOD));
			if (Dungeon.posNeeded()) {
				addItemToSpawn(new PotionOfStrength());
				Dungeon.limitedDrops.strengthPotions.count++;
			}
			if (Dungeon.souNeeded()) {
				addItemToSpawn(new ScrollOfUpgrade());
				Dungeon.limitedDrops.upgradeScrolls.count++;
			}
			if (Dungeon.asNeeded()) {
				addItemToSpawn(new Stylus());
				Dungeon.limitedDrops.arcaneStyli.count++;
			}

			int bonus = 0;
			for (Buff buff : Dungeon.hero.buffs(MasterThievesArmband.Thievery.class)) {
				bonus += ((MasterThievesArmband.Thievery)buff).level();
			}
			if (Random.Float() > Math.pow(0.99, bonus)) {
				if (Random.Int(2) == 0)
					addItemToSpawn(new ScrollOfMagicalInfusion());
				else
					addItemToSpawn(new PotionOfMight());
			}

			DriedRose rose = Dungeon.hero.belongings.getItem(DriedRose.class);
			if (rose != null && !rose.cursed) {
				if (rose.droppedPetals < 11) {
					addItemToSpawn(new DriedRose.Petal());
					rose.droppedPetals++;
				}
			}

			if (Dungeon.depth > 1 && Dungeon.depth < 6) {
				if (Dungeon.depth == 4) {
					feeling = Feeling.GRASS;
				} else {
					switch (Random.Int(10)) {
						case 0:
							if (!Dungeon.bossLevel(Dungeon.depth + 1)) {
								feeling = Feeling.CHASM;
							}
							break;
						case 1:
							feeling = Feeling.WATER;
							break;
						case 2:
						case 3:
						case 4:
							feeling = Feeling.GRASS;
							break;
					}
				}
			} else if (Dungeon.depth > 5 && Dungeon.depth < 22) {
				switch (Random.Int(10)) {
					case 0:
						if (!Dungeon.bossLevel(Dungeon.depth + 1)) {
							feeling = Feeling.CHASM;
						}
						break;
					case 1:
						feeling = Feeling.WATER;
						break;
					case 2:
						feeling = Feeling.GRASS;
						break;
					case 3:
						feeling = Feeling.DARK;
						addItemToSpawn(new Torch());
						addItemToSpawn(new Torch());
						addItemToSpawn(new Torch());
						viewDistance = (int) Math.ceil(viewDistance / 3f);
						break;
				}
			} else if (Dungeon.depth > 21 && Dungeon.depth < 27) {
				switch (Random.Int(10)) {
					case 1:
						feeling = Feeling.WATER;
						break;
					case 2:
						feeling = Feeling.GRASS;
						break;
					case 3:
					case 0:
						feeling = Feeling.DARK;
						addItemToSpawn(new Torch());
						addItemToSpawn(new Torch());
						addItemToSpawn(new Torch());
						viewDistance = (int) Math.ceil(viewDistance / 3f);
						break;
				}
			} else if (Dungeon.depth == 29) {
				feeling = Feeling.WATER;
			} else if (Dungeon.depth == 31) {
				feeling = Feeling.DARK;
				addItemToSpawn(new Torch());
				addItemToSpawn(new Torch());
				addItemToSpawn(new Torch());
				viewDistance = (int) Math.ceil(viewDistance / 3f);
			} else if (Dungeon.depth > 55) {
				addItemToSpawn(new StoneOre());
				addItemToSpawn(new StoneOre());
				addItemToSpawn(new StoneOre());
				addItemToSpawn(new StoneOre());
				addItemToSpawn(new StoneOre());
				addItemToSpawn(new StoneOre());
				addItemToSpawn(new StoneOre());
				addItemToSpawn(new StoneOre());
				addItemToSpawn(new StoneOre());
				viewDistance = (int) Math.ceil(viewDistance / 3f);
			} else if (Dungeon.depth == 32) {
				feeling = Feeling.WATER;
			} else if (Dungeon.depth == 33) {
				feeling = Feeling.CHASM;
			}

		}

		boolean pitNeeded = Dungeon.depth > 1 && weakFloorCreated;

		do {

			pitRoomNeeded = pitNeeded;
			weakFloorCreated = false;

			mobs = new HashSet<>();
			heaps = new SparseArray<>();
			blobs = new HashMap<>();
			plants = new SparseArray<>();

		} while (!build());

		buildFlagMaps();
		cleanWalls();

		createMobs();
		createItems();
	}

	public void setSize(int w, int h){

		WIDTH = w;
		HEIGHT = h;
		LENGTH = w * h;

		map = new int[getLength()];
		Arrays.fill( map, Terrain.WALL );
		Arrays.fill( map, feeling == Level.Feeling.CHASM ? Terrain.CHASM : Terrain.WALL );

		visited = new boolean[getLength()];
		mapped = new boolean[getLength()];
		Dungeon.visible = new boolean[getLength()];

		fieldOfView = new boolean[getLength()];

		passable	= new boolean[getLength()];
		losBlocking	= new boolean[getLength()];
		flamable	= new boolean[getLength()];
		secret		= new boolean[getLength()];
		solid		= new boolean[getLength()];
		avoid		= new boolean[getLength()];
		water		= new boolean[getLength()];
		pit			= new boolean[getLength()];

		PathFinder.setMapSize(w, h);
	}

	public void reset() {

		for (Mob mob : mobs.toArray(new Mob[0])) {
			if (!mob.reset()) {
				mobs.remove(mob);
			}
		}
		createMobs();
		reset = true;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {

		if (bundle.contains("width") && bundle.contains("height")){
			setSize( bundle.getInt("width"), bundle.getInt("height"));
		} else
			setSize( 32, 32);

		mobs = new HashSet<Mob>();
		heaps = new SparseArray<Heap>();
		blobs = new HashMap<Class<? extends Blob>, Blob>();
		plants = new SparseArray<Plant>();

		map = bundle.getIntArray(MAP);
		visited = bundle.getBooleanArray(VISITED);
		mapped = bundle.getBooleanArray(MAPPED);

		entrance = bundle.getInt(ENTRANCE);
		exit = bundle.getInt(EXIT);
		currentmoves = bundle.getInt(MOVES);

		locked = bundle.getBoolean(LOCKED);

		cleared = bundle.getBoolean(CLEARED);
		reset = bundle.getBoolean(RESET);
		forcedone = bundle.getBoolean(FORCEDONE);
		genpetnext = bundle.getBoolean(GENPETNEXT);
		sealedlevel = bundle.getBoolean(SEALEDLEVEL);
		viewDistance = bundle.getInt(VIEW);

		weakFloorCreated = false;

		Collection<Bundlable> collection = bundle.getCollection(HEAPS);
		for (Bundlable h : collection) {
			Heap heap = (Heap) h;
			if (!heap.isEmpty())
				heaps.put(heap.pos, heap);
		}

		collection = bundle.getCollection(PLANTS);
		for (Bundlable p : collection) {
			Plant plant = (Plant) p;
			plants.put(plant.pos, plant);
		}

		collection = bundle.getCollection(MOBS);
		for (Bundlable m : collection) {
			Mob mob = (Mob) m;
			if (mob != null) {
				mobs.add(mob);
			}
		}

		collection = bundle.getCollection(BLOBS);
		for (Bundlable b : collection) {
			Blob blob = (Blob) b;
			blobs.put(blob.getClass(), blob);
		}

		feeling = bundle.getEnum(FEELING, Feeling.class);
		if (feeling == Feeling.DARK)
			viewDistance = (int) Math.ceil(viewDistance / 3f);

		buildFlagMaps();
		cleanWalls();
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put( "width", WIDTH );
		bundle.put( "height", HEIGHT );
		bundle.put(MAP, map);
		bundle.put(VISITED, visited);
		bundle.put(MAPPED, mapped);
		bundle.put(ENTRANCE, entrance);
		bundle.put(EXIT, exit);
		bundle.put(LOCKED, locked);
		bundle.put(HEAPS, heaps.values());
		bundle.put(PLANTS, plants.values());
		bundle.put(MOBS, mobs);
		bundle.put(BLOBS, blobs.values());
		bundle.put(FEELING, feeling);
		bundle.put(MOVES, currentmoves);
		bundle.put(CLEARED, cleared);
		bundle.put(RESET, reset);
		bundle.put(FORCEDONE, forcedone);
		bundle.put(GENPETNEXT, genpetnext);
		bundle.put(SEALEDLEVEL, sealedlevel);
		bundle.put(VIEW, viewDistance);
	}

	public int tunnelTile() {
		return feeling == Feeling.CHASM ? Terrain.EMPTY_SP : Terrain.EMPTY;
	}

	public String tilesTex() {
		return null;
	}

	public String waterTex() {
		return null;
	}

	abstract protected boolean build();

	abstract protected void createMobs();

	abstract protected void createItems();

	public void addVisuals(Scene scene) {
		for (int i = 0; i < getLength(); i++) {
			if (pit[i]) {
				scene.add(new WindParticle.Wind(i));
				if (i >= getWidth() && water[i - getWidth()]) {
					scene.add(new FlowParticle.Flow(i - getWidth()));
				}
			}
		}
	}

	public int nMobs() {
		return 0;
	}

	public Actor respawner() {
		return new Actor() {

			{
				actPriority = 1; //as if it were a buff.
			}

			@Override
			protected boolean act() {
				if ((Dungeon.level.cleared && mobs.size() < nMobs()) ||
						(Dungeon.depth == 1 && mobs.size() < nMobs()) ||
						(Dungeon.depth == 2 && mobs.size() < nMobs())) {

					Mob mob = Bestiary.mutable(Dungeon.depth);
					mob.state = mob.WANDERING;
					mob.pos = randomRespawnCell();
					if (Dungeon.hero.isAlive() && mob.pos != -1 && distance(Dungeon.hero.pos, mob.pos) >= 4) {
						GameScene.add(mob);
					}
				}
				spend(Dungeon.level.feeling == Feeling.DARK
						|| Statistics.amuletObtained ? TIME_TO_RESPAWN / 2
						: TIME_TO_RESPAWN);
				return true;
			}
		};
	}

	public Actor respawnerPet() {
		return new Actor() {

			{
				actPriority = 1; //as if it were a buff.
			}

			@Override
			protected boolean act() {
				//GLog.i("Check Pet");
				int petpos = -1;
				int heropos = Dungeon.hero.pos;
				if (Actor.findChar(heropos) != null && Dungeon.hero.petfollow) {
					//GLog.i("Check Pet 2");
					ArrayList<Integer> candidates = new ArrayList<Integer>();
					boolean[] passable = Level.passable;

					for (int n : PathFinder.NEIGHBOURS8) {
						int c = heropos + n;
						if (passable[c] && Actor.findChar(c) == null) {
							candidates.add(c);
						}
					}

					petpos = candidates.size() > 0 ? Random.element(candidates) : -1;
				}

				if (petpos != -1 && Dungeon.hero.haspet && Dungeon.hero.petfollow) {

					PET petCheck = checkpet();
					if (petCheck != null) {
						petCheck.destroy();
						petCheck.sprite.killAndErase();
					}

					if (Dungeon.hero.petType == 1) {
						Spider pet = new Spider();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 2) {
						bee pet = new bee();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 3) {
						Velocirooster pet = new Velocirooster();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 4) {
						RedDragon pet = new RedDragon();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 5) {
						GreenDragon pet = new GreenDragon();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 6) {
						VioletDragon pet = new VioletDragon();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 7) {
						BlueDragon pet = new BlueDragon();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 8) {
						Scorpion pet = new Scorpion();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 9) {
						Bunny pet = new Bunny();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 10) {
						Fairy pet = new Fairy();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 11) {
						SugarplumFairy pet = new SugarplumFairy();
						spawnPet(pet, petpos, heropos);
					}
					if (Dungeon.hero.petType == 12) {
						ShadowDragon pet = new ShadowDragon();
						spawnPet(pet, petpos, heropos);
					}

				}

				spend(PET_TICK);
				return true;
			}
		};
	}

	private PET checkpet() {
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof PET) {
				return (PET) mob;
			}
		}
		return null;
	}

	public void spawnPet(PET pet, Integer petpos, Integer heropos) {
		pet.spawn(Dungeon.hero.petLevel);
		pet.HP = Dungeon.hero.petHP;
		pet.pos = petpos;
		pet.state = pet.HUNTING;
		pet.kills = Dungeon.hero.petKills;
		pet.experience = Dungeon.hero.petExperience;
		pet.cooldown = Dungeon.hero.petCooldown;

		GameScene.add(pet);
		Actor.addDelayed(new Pushing(pet, heropos, petpos), -1f);
		Dungeon.hero.petfollow = false;
	}

	public boolean checkOriginalGenMobs() {
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob.originalgen) {
				return true;
			}
		}
		return false;
	}

	public Actor regrower() {
		return new Actor() {

			{
				actPriority = 2; //as if it were a buff.
			}

			@Override
			protected boolean act() {

				int growPos = -1;
				for (int i = 0; i < 20; i++) {
					growPos = randomRegrowthCell();

					if (growPos != -1) {
						break;
					}
				}
				if (growPos != -1) {
					if (map[growPos] == Terrain.GRASS) {
						Level.set(growPos, Terrain.HIGH_GRASS);
					}
					GameScene.updateMap();
					Dungeon.observe();
				}


				spend(REGROW_TIMER);
				return true;
			}
		};
	}

	public int randomRegrowthCell() {
		int cell;
		int count = 1;
		do {
			cell = Random.Int(getLength());
			count++;
		} while (map[cell] != Terrain.GRASS && count < 100);
		return cell;
	}

	public Actor floordropper() {
		return new Actor() {

			{
				actPriority = 3; //as if it were a buff.
			}

			@Override
			protected boolean act() {


				int dropPos = -1;
				for (int i = 0; i < 20; i++) {
					dropPos = randomChasmCell();

					if (dropPos != -1) {
						//GLog.i("one ");
						break;
					}
				}
				if (dropPos != -1) {

					//GLog.i("two %s",dropPos);
					if (map[dropPos] == Terrain.EMPTY && Actor.findChar(dropPos) == null) {

						//GLog.i("three ");
						//if the tile above is not chasm then set to chasm floor. If is chasm then set to chasm

						if (map[dropPos - getWidth()] == Terrain.WALL ||
								map[dropPos - getWidth()] == Terrain.WALL_DECO) {

							set(dropPos, Terrain.CHASM_WALL);
							//GLog.i("four ");
						} else if (map[dropPos - getWidth()] != Terrain.CHASM &&
								map[dropPos - getWidth()] != Terrain.CHASM_FLOOR &&
								map[dropPos - getWidth()] != Terrain.CHASM_WALL) {
							set(dropPos, Terrain.CHASM_FLOOR);
						} else {
							set(dropPos, Terrain.CHASM);
							// GLog.i("five ");
						}

						if (map[dropPos + getWidth()] == Terrain.CHASM_FLOOR) {
							set(dropPos + getWidth(), Terrain.CHASM);
							// GLog.i("six ");
						}


					}
					GameScene.updateMap(dropPos);
					GameScene.updateMap(dropPos - getWidth());
					GameScene.updateMap(dropPos + getWidth());
					Dungeon.observe();
				}

				spend(DROP_TIMER);
				return true;
			}
		};
	}

	public int randomChasmCell() {
		int cell;
		int count = 1;
		do {
			cell = Random.Int(getWidth() + 1, getLength() - (getWidth() + 1));
			count++;
		} while (map[cell] != Terrain.EMPTY && count < 100);
		return cell;
	}

	public int randomRespawnCell() {
		int cell;
		do {
			cell = Random.Int(getLength());
		} while (!passable[cell] || Dungeon.visible[cell]
				|| Actor.findChar(cell) != null);
		return cell;
	}

	public int randomRespawnCellMob() {
		int cell;
		do {
			cell = Random.Int(getLength());
		} while (!passable[cell] || Dungeon.visible[cell]
				|| Actor.findChar(cell) != null);
		return cell;
	}

	public int randomRespawnCellSheep(int start, int dist) {
		int cell;
		do {
			cell = Random.Int(getLength());
		} while (!avoid[cell] || Actor.findChar(cell) != null || map[cell] != Terrain.FLEECING_TRAP
				|| distance(start, cell) > dist);
		return cell;
	}

	public int countFleeceTraps(int start, int dist) {
		int count = 0;
		for (int cell = 0; cell < getLength(); cell++) {
			if (avoid[cell] && Actor.findChar(cell) == null && map[cell] == Terrain.FLEECING_TRAP && distance(start, cell) < dist) {
				count++;
			}
		}
		return count;
	}


	public int randomRespawnCellFish() {
		int cell;
		do {
			cell = Random.Int(getLength());
		} while (!passable[cell] || Actor.findChar(cell) != null || map[cell] != Terrain.EMPTY);
		return cell;
	}

	public int randomDestination() {
		int cell;
		do {
			cell = Random.Int(getLength());
		} while (!passable[cell]);
		return cell;
	}

	public void addItemToSpawn(Item item) {
		if (item != null) {
			itemsToSpawn.add(item);
		}
	}

	public Item findPrizeItem() {
		return findPrizeItem(null);
	}

	public Item findPrizeItem(Class<? extends Item> match) {
		if (itemsToSpawn.size() == 0)
			return null;

		if (match == null) {
			Item item = Random.element(itemsToSpawn);
			itemsToSpawn.remove(item);
			return item;
		}

		for (Item item : itemsToSpawn) {
			if (match.isInstance(item)) {
				itemsToSpawn.remove(item);
				return item;
			}
		}

		return null;
	}

	protected void buildFlagMaps() {

		for (int i = 0; i < getLength(); i++) {
			int flags = Terrain.flags[map[i]];
			passable[i] = (flags & Terrain.PASSABLE) != 0;
			losBlocking[i] = (flags & Terrain.LOS_BLOCKING) != 0;
			flamable[i] = (flags & Terrain.FLAMABLE) != 0;
			secret[i] = (flags & Terrain.SECRET) != 0;
			solid[i] = (flags & Terrain.SOLID) != 0;
			avoid[i] = (flags & Terrain.AVOID) != 0;
			water[i] = (flags & Terrain.LIQUID) != 0;
			pit[i] = (flags & Terrain.PIT) != 0;
		}

		int lastRow = getLength() - getWidth();
		for (int i = 0; i < getWidth(); i++) {
			passable[i] = avoid[i] = false;
			passable[lastRow + i] = avoid[lastRow + i] = false;
		}
		for (int i = getWidth(); i < lastRow; i += getWidth()) {
			passable[i] = avoid[i] = false;
			passable[i + getWidth() - 1] = avoid[i + getWidth() - 1] = false;
		}

		for (int i = getWidth(); i < getLength() - getWidth(); i++) {

			if (water[i]) {
				int t = Terrain.WATER_TILES;
				for (int j = 0; j < PathFinder.NEIGHBOURS4.length; j++) {
					if ((Terrain.flags[map[i + PathFinder.NEIGHBOURS4[j]]] & Terrain.UNSTITCHABLE) != 0) {
						t += 1 << j;
					}
				}
				map[i] = t;
			}

			if (pit[i]) {
				if (!pit[i - getWidth()]) {
					int c = map[i - getWidth()];
					if (c == Terrain.EMPTY_SP || c == Terrain.STATUE_SP) {
						map[i] = Terrain.CHASM_FLOOR_SP;
					} else if (water[i - getWidth()]) {
						map[i] = Terrain.CHASM_WATER;
					} else if ((Terrain.flags[c] & Terrain.UNSTITCHABLE) != 0) {
						map[i] = Terrain.CHASM_WALL;
					} else {
						map[i] = Terrain.CHASM_FLOOR;
					}
				}
			}
		}
	}

	private static int[] N4Indicies = new int[]{0, 2, 3, 1};

	protected void cleanWalls() {
		discoverable = new boolean[getLength()];

		for (int i = 0; i < getLength(); i++) {

			boolean d = false;

			for (int j = 0; j < PathFinder.NEIGHBOURS9.length; j++) {
				int n = i + PathFinder.NEIGHBOURS9[j];
				if (n >= 0 && n < getLength() && map[n] != Terrain.WALL
						&& map[n] != Terrain.WALL_DECO) {
					d = true;
					break;
				}
			}

			if (d) {
				d = false;

				for (int j = 0; j < PathFinder.NEIGHBOURS9.length; j++) {
					int n = i + PathFinder.NEIGHBOURS9[j];
					if (n >= 0 && n < getLength() && !pit[n]) {
						d = true;
						break;
					}
				}
			}

			discoverable[i] = d;
		}
	}

	public static void set(int cell, int terrain) {
		Painter.set(Dungeon.level, cell, terrain);

		int flags = Terrain.flags[terrain];
		passable[cell] = (flags & Terrain.PASSABLE) != 0;
		losBlocking[cell] = (flags & Terrain.LOS_BLOCKING) != 0;
		flamable[cell] = (flags & Terrain.FLAMABLE) != 0;
		secret[cell] = (flags & Terrain.SECRET) != 0;
		solid[cell] = (flags & Terrain.SOLID) != 0;
		avoid[cell] = (flags & Terrain.AVOID) != 0;
		pit[cell] = (flags & Terrain.PIT) != 0;
		water[cell] = terrain == Terrain.WATER || (terrain >= Terrain.WATER_TILES && terrain < Terrain.WOOL_RUG);
	}

	public int checkdew() {
		int dewdrops = 0;
		for (int i = 0; i < LENGTH; i++) {
			Heap heap = heaps.get(i);
			if (heap != null)
				dewdrops += heap.dewdrops();
		}
		return dewdrops;
	}


	public Heap drop(Item item, int cell) {

		if ((map[cell] == Terrain.ALCHEMY)
				&& (!(item instanceof Plant.Seed || item instanceof Blandfruit)
				|| item instanceof BlandfruitBush.Seed
				|| (item instanceof Blandfruit && (((Blandfruit) item).potionAttrib != null || heaps
				.get(cell) != null)) || Dungeon.hero
				.buff(AlchemistsToolkit.alchemy.class) != null
				&& Dungeon.hero.buff(AlchemistsToolkit.alchemy.class)
				.isCursed())) {
			int n;
			do {
				n = cell + PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (map[n] != Terrain.EMPTY_SP);
			cell = n;
		}

		Heap heap = heaps.get(cell);
		if (heap == null) {

			heap = new Heap();
			heap.seen = Dungeon.visible[cell];
			heap.pos = cell;
			if (map[cell] == Terrain.CHASM
					|| (Dungeon.level != null && pit[cell])) {
				Dungeon.dropToChasm(item);
				GameScene.discard(heap);
			} else {
				heaps.put(cell, heap);
				GameScene.add(heap);
			}

		} else if (heap.type == Heap.Type.LOCKED_CHEST
				|| heap.type == Heap.Type.CRYSTAL_CHEST
				|| heap.type == Heap.Type.HARD_TOMB){

			int n;
			do {
				n = cell + PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Level.passable[n] && !Level.avoid[n]);
			return drop(item, n);

		}
		heap.drop(item);

		if (Dungeon.level != null) {
			press(cell, null);
		}

		return heap;
	}

	public Plant plant(Plant.Seed seed, int pos) {

		Plant plant = plants.get(pos);
		if (plant != null) {
			plant.wither();
		}

		if (map[pos] == Terrain.HIGH_GRASS || map[pos] == Terrain.EMPTY
				|| map[pos] == Terrain.EMBERS || map[pos] == Terrain.EMPTY_DECO) {
			map[pos] = Terrain.GRASS;
			GameScene.updateMap(pos);
		}

		plant = seed.couch(pos);
		plants.put(pos, plant);

		GameScene.add(plant);

		return plant;
	}

	public void uproot(int pos) {
		plants.delete(pos);
	}

	public int pitCell() {
		return randomRespawnCell();
	}

	public void press(int cell, Char ch) {

		if (pit[cell] && ch == Dungeon.hero) {
			Chasm.heroFall(cell);
			return;
		}

		TimekeepersHourglass.timeFreeze timeFreeze = null;

		if (ch != null)
			timeFreeze = ch.buff(TimekeepersHourglass.timeFreeze.class);

		boolean frozen = timeFreeze != null;

		boolean trap = false;
		boolean fleece = false;
		boolean sheep = false;

		switch (map[cell]) {

			case Terrain.SECRET_TOXIC_TRAP:
				GLog.i(TXT_HIDDEN_PLATE_CLICKS);
			case Terrain.TOXIC_TRAP:
				trap = true;
				if (!frozen)
					ToxicTrap.trigger(cell, ch);
				break;

			case Terrain.SECRET_FIRE_TRAP:
				GLog.i(TXT_HIDDEN_PLATE_CLICKS);
			case Terrain.FIRE_TRAP:
				trap = true;
				if (!frozen)
					FireTrap.trigger(cell, ch);
				break;

			case Terrain.SECRET_PARALYTIC_TRAP:
				GLog.i(TXT_HIDDEN_PLATE_CLICKS);
			case Terrain.PARALYTIC_TRAP:
				trap = true;
				if (!frozen)
					ParalyticTrap.trigger(cell, ch);
				break;

			case Terrain.SECRET_POISON_TRAP:
				GLog.i(TXT_HIDDEN_PLATE_CLICKS);
			case Terrain.POISON_TRAP:
				trap = true;
				if (!frozen)
					PoisonTrap.trigger(cell, ch);
				break;

			case Terrain.SECRET_ALARM_TRAP:
				GLog.i(TXT_HIDDEN_PLATE_CLICKS);
			case Terrain.ALARM_TRAP:
				trap = true;
				if (!frozen)
					AlarmTrap.trigger(cell, ch);
				break;

			case Terrain.SECRET_LIGHTNING_TRAP:
				GLog.i(TXT_HIDDEN_PLATE_CLICKS);
			case Terrain.LIGHTNING_TRAP:
				trap = true;
				if (!frozen)
					LightningTrap.trigger(cell, ch);
				break;

			case Terrain.SECRET_GRIPPING_TRAP:
				GLog.i(TXT_HIDDEN_PLATE_CLICKS);
			case Terrain.GRIPPING_TRAP:
				trap = true;
				if (!frozen)
					GrippingTrap.trigger(cell, ch);
				break;

			case Terrain.SECRET_SUMMONING_TRAP:
				GLog.i(TXT_HIDDEN_PLATE_CLICKS);
			case Terrain.SUMMONING_TRAP:
				trap = true;
				if (!frozen)
					SummoningTrap.trigger(cell, ch);
				break;

			case Terrain.FLEECING_TRAP:
				trap = true;
				if (ch instanceof SheepSokoban || ch instanceof SheepSokobanSwitch || ch instanceof SheepSokobanCorner) {
					fleece = true;
				}
				if (ch != null)
					FleecingTrap.trigger(cell, ch);
				break;

			case Terrain.CHANGE_SHEEP_TRAP:
				trap = true;
				if (ch instanceof SheepSokoban || ch instanceof SheepSokobanSwitch || ch instanceof SheepSokobanCorner) {
					sheep = true;
					ChangeSheepTrap.trigger(cell, ch);
				}
				break;

			case Terrain.HIGH_GRASS:
				HighGrass.trample(this, cell, ch);
				break;

			case Terrain.WELL:
				WellWater.affectCell(cell);
				break;

			case Terrain.ALCHEMY:
				if (ch == null) {
					Alchemy.transmute(cell);
				}
				break;

			case Terrain.DOOR:
				Door.enter(cell);
				break;
		}

		if (trap && !frozen && !fleece) {

			if (Dungeon.visible[cell])
				Sample.INSTANCE.play(Assets.SND_TRAP);

			if (ch == Dungeon.hero)
				Dungeon.hero.interrupt();

			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);

		} else if (trap && frozen && !fleece) {

			Sample.INSTANCE.play(Assets.SND_TRAP);

			Level.set(cell, Terrain.discover(map[cell]));
			GameScene.updateMap(cell);

			timeFreeze.setDelayedPress(cell);

		} else if (trap && frozen && fleece) {

			Sample.INSTANCE.play(Assets.SND_TRAP);

			Level.set(cell, Terrain.discover(map[cell]));
			GameScene.updateMap(cell);

			timeFreeze.setDelayedPress(cell);

		} else if (trap && !frozen && fleece) {

			if (Dungeon.visible[cell])
				Sample.INSTANCE.play(Assets.SND_TRAP);

			if (ch == Dungeon.hero)
				Dungeon.hero.interrupt();

			set(cell, Terrain.WOOL_RUG);
			GameScene.updateMap(cell);

		} else if (trap && sheep) {

			if (Dungeon.visible[cell])
				Sample.INSTANCE.play(Assets.SND_TRAP);

			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);

		}

		Plant plant = plants.get(cell);
		if (plant != null) {
			plant.activate(ch);
		}
	}

	public void mobPress(Mob mob) {

		int cell = mob.pos;

		if (pit[cell] && !mob.flying) {
			Chasm.mobFall(mob);
			return;
		}

		boolean trap = true;
		boolean fleece = false;
		boolean sheep = false;
		switch (map[cell]) {

			case Terrain.TOXIC_TRAP:
				ToxicTrap.trigger(cell, mob);
				break;

			case Terrain.FIRE_TRAP:
				FireTrap.trigger(cell, mob);
				break;

			case Terrain.PARALYTIC_TRAP:
				ParalyticTrap.trigger(cell, mob);
				break;

			case Terrain.FLEECING_TRAP:
				if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner) {
					fleece = true;
				}
				FleecingTrap.trigger(cell, mob);
				break;

			case Terrain.CHANGE_SHEEP_TRAP:
				if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner) {
					sheep = true;
					ChangeSheepTrap.trigger(cell, mob);
				}
				break;

			case Terrain.SOKOBAN_ITEM_REVEAL:
				trap = false;
				if (mob instanceof SheepSokoban || mob instanceof SheepSokobanSwitch || mob instanceof SheepSokobanCorner) {
					HeapGenTrap.trigger(cell, mob);
					sheep = true;
				}
				break;

			case Terrain.POISON_TRAP:
				PoisonTrap.trigger(cell, mob);
				break;

			case Terrain.ALARM_TRAP:
				AlarmTrap.trigger(cell, mob);
				break;

			case Terrain.LIGHTNING_TRAP:
				LightningTrap.trigger(cell, mob);
				break;

			case Terrain.GRIPPING_TRAP:
				GrippingTrap.trigger(cell, mob);
				break;

			case Terrain.SUMMONING_TRAP:
				SummoningTrap.trigger(cell, mob);
				break;

			case Terrain.DOOR:
				Door.enter(cell);

			default:
				trap = false;
		}

		if (trap && !fleece) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);
		}

		if (trap && fleece) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.WOOL_RUG);
			GameScene.updateMap(cell);
		}

		if (trap && sheep) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.INACTIVE_TRAP);
			GameScene.updateMap(cell);
		}

		if (!trap && sheep) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play(Assets.SND_TRAP);
			}
			set(cell, Terrain.EMPTY);
			GameScene.updateMap(cell);
		}

		Plant plant = plants.get(cell);
		if (plant != null) {
			plant.activate(mob);
		}
	}

	public void updateFieldOfView( Char c, boolean[] fieldOfView ) {

		int cx = c.pos % getWidth();
		int cy = c.pos / getWidth();

		boolean sighted = c.buff( Blindness.class ) == null && c.buff( Shadows.class ) == null
				&& c.buff( TimekeepersHourglass.timeStasis.class ) == null && c.isAlive();
		if (sighted) {
			ShadowCaster.castShadow( cx, cy, fieldOfView, c.viewDistance );
		} else {
			BArray.setFalse(fieldOfView);
		}

		int sense = 1;
		//Currently only the hero can get mind vision
		if (c.isAlive() && c == Dungeon.hero) {
			for (Buff b : c.buffs( MindVision.class )) {
				sense = Math.max( ((MindVision)b).distance, sense );
			}
		}

		if ((sighted && sense > 1) || !sighted) {

			int ax = Math.max( 0, cx - sense );
			int bx = Math.min( cx + sense, getWidth() - 1 );
			int ay = Math.max( 0, cy - sense );
			int by = Math.min( cy + sense, getHeight() - 1 );

			int len = bx - ax + 1;
			int pos = ax + ay * getWidth();
			for (int y = ay; y <= by; y++, pos+=getWidth()) {
				System.arraycopy(discoverable, pos, fieldOfView, pos, len);
			}
		}

		//Currently only the hero can get mind vision or awareness
		if (c.isAlive() && c == Dungeon.hero) {
			Dungeon.hero.mindVisionEnemies.clear();
			if (c.buff( MindVision.class ) != null) {
				for (Mob mob : mobs) {
					int p = mob.pos;

					if (!fieldOfView[p]){
						Dungeon.hero.mindVisionEnemies.add(mob);
					}
					for (int i : PathFinder.NEIGHBOURS9)
						fieldOfView[p+i] = true;

				}
			} else if (((Hero)c).heroClass == HeroClass.HUNTRESS) {
				for (Mob mob : mobs) {
					int p = mob.pos;
					if (distance( c.pos, p) == 2) {

						if (!fieldOfView[p]){
							Dungeon.hero.mindVisionEnemies.add(mob);
						}
						for (int i : PathFinder.NEIGHBOURS9)
							fieldOfView[p+i] = true;
					}
				}
			}
			if (c.buff( Awareness.class ) != null) {
				for (Heap heap : heaps.values()) {
					int p = heap.pos;
					for (int i : PathFinder.NEIGHBOURS9)
						fieldOfView[p+i] = true;
				}
			}
		}

		if (c == Dungeon.hero) {
			for (Heap heap : heaps.values())
				if (!heap.seen && fieldOfView[heap.pos])
					heap.seen = true;
		}

	}

	public int distance(int a, int b) {
		int ax = a % getWidth();
		int ay = a / getWidth();
		int bx = b % getWidth();
		int by = b / getWidth();
		return Math.max(Math.abs(ax - bx), Math.abs(ay - by));
	}

	public boolean adjacent( int a, int b ) {
		return distance( a, b ) == 1;
	}

	//returns true if the input is a valid tile within the level
	public boolean insideMap(int tile) {
		//top and bottom row and beyond
		return !((tile < getWidth() || tile >= getLength() - getWidth()) ||
				//left and right column
				(tile % getWidth() == 0 || tile % getWidth() == getWidth() - 1));
	}

	public boolean insideMapPermissive(int tile) {
		//top and bottom row and beyond
		return !(tile < 0 || tile >= LENGTH);
	}

	public String tileName(int tile) {

		if (tile >= Terrain.WATER_TILES) {
			return tileName(Terrain.WATER);
		}
		// && tile<Terrain.WOOL_RUG

		if (tile != Terrain.CHASM && (Terrain.flags[tile] & Terrain.PIT) != 0) {
			return tileName(Terrain.CHASM);
		}

		switch (tile) {
			case Terrain.CHASM:
				return Messages.get(Level.class, "chasm_name");
			case Terrain.EMPTY:
			case Terrain.EMPTY_SP:
			case Terrain.EMPTY_DECO:
			case Terrain.SECRET_TOXIC_TRAP:
			case Terrain.SECRET_FIRE_TRAP:
			case Terrain.SECRET_PARALYTIC_TRAP:
			case Terrain.SECRET_POISON_TRAP:
			case Terrain.SECRET_ALARM_TRAP:
			case Terrain.SECRET_LIGHTNING_TRAP:
				return Messages.get(Level.class, "floor_name");
			case Terrain.GRASS:
				return Messages.get(Level.class, "grass_name");
			case Terrain.WATER:
				return Messages.get(Level.class, "water_name");
			case Terrain.WALL:
			case Terrain.WALL_DECO:
			case Terrain.SECRET_DOOR:
				return Messages.get(Level.class, "wall_name");
			case Terrain.DOOR:
				return Messages.get(Level.class, "closed_door_name");
			case Terrain.OPEN_DOOR:
				return Messages.get(Level.class, "open_door_name");
			case Terrain.ENTRANCE:
				return Messages.get(Level.class, "entrance_name");
			case Terrain.EXIT:
				return Messages.get(Level.class, "exit_name");
			case Terrain.EMBERS:
				return Messages.get(Level.class, "embers_name");
			case Terrain.LOCKED_DOOR:
				return Messages.get(Level.class, "locked_door_name");
			case Terrain.PEDESTAL:
				return Messages.get(Level.class, "pedestal_name");
			case Terrain.BARRICADE:
				return Messages.get(Level.class, "barricade_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(Level.class, "high_grass_name");
			case Terrain.LOCKED_EXIT:
				return Messages.get(Level.class, "locked_exit_name");
			case Terrain.UNLOCKED_EXIT:
				return Messages.get(Level.class, "unlocked_exit_name");
			case Terrain.WELL:
				return Messages.get(Level.class, "well_name");
			case Terrain.EMPTY_WELL:
				return Messages.get(Level.class, "empty_well_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(Level.class, "statue_name");
			case Terrain.TOXIC_TRAP:
				return Messages.get(ToxicTrap.class, "name");
			case Terrain.FIRE_TRAP:
				return Messages.get(FireTrap.class, "name");
			case Terrain.PARALYTIC_TRAP:
				return Messages.get(ParalyticTrap.class, "name");
			case Terrain.POISON_TRAP:
				return Messages.get(PoisonTrap.class, "name");
			case Terrain.ALARM_TRAP:
				return Messages.get(AlarmTrap.class, "name");
			case Terrain.LIGHTNING_TRAP:
				return Messages.get(LightningTrap.class, "name");
			case Terrain.GRIPPING_TRAP:
				return Messages.get(GrippingTrap.class, "name");
			case Terrain.SUMMONING_TRAP:
				return Messages.get(SummoningTrap.class, "name");
			case Terrain.INACTIVE_TRAP:
				return Messages.get(Level.class, "inactive_trap_name");
			case Terrain.BOOKSHELF:
				return Messages.get(Level.class, "bookshelf_name");
			case Terrain.ALCHEMY:
				return Messages.get(Level.class, "alchemy_name");
			case Terrain.SHRUB:
				return Messages.get(Level.class, "shrub_name");
			default:
				return "???";
		}
	}

	public String tileDesc(int tile) {

		switch (tile) {
			case Terrain.CHASM:
				return Messages.get(Level.class, "chasm_desc");
			case Terrain.WATER:
				return Messages.get(Level.class, "water_desc");
			case Terrain.ENTRANCE:
				return Messages.get(Level.class, "entrance_desc");
			case Terrain.EXIT:
			case Terrain.UNLOCKED_EXIT:
				return Messages.get(Level.class, "exit_desc");
			case Terrain.EMBERS:
				return Messages.get(Level.class, "embers_desc");
			case Terrain.HIGH_GRASS:
				return Messages.get(Level.class, "high_grass_desc");
			case Terrain.SHRUB:
				return Messages.get(Level.class, "shrub_desc");
			case Terrain.LOCKED_DOOR:
				return Messages.get(Level.class, "locked_door_desc");
			case Terrain.LOCKED_EXIT:
				return Messages.get(Level.class, "locked_exit_desc");
			case Terrain.BARRICADE:
				return Messages.get(Level.class, "barricade_desc");
			case Terrain.TOXIC_TRAP:
			case Terrain.FIRE_TRAP:
			case Terrain.PARALYTIC_TRAP:
			case Terrain.POISON_TRAP:
			case Terrain.ALARM_TRAP:
			case Terrain.LIGHTNING_TRAP:
			case Terrain.GRIPPING_TRAP:
			case Terrain.SUMMONING_TRAP:
				return Messages.get(Level.class, "actrap_desc");
			case Terrain.INACTIVE_TRAP:
				return Messages.get(Level.class, "inactive_trap_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(Level.class, "statue_desc");
			case Terrain.ALCHEMY:
				return Messages.get(Level.class, "alchemy_desc");
			case Terrain.EMPTY_WELL:
				return Messages.get(Level.class, "empty_well_desc");
			default:
				if (tile >= Terrain.WATER_TILES) {
					return tileDesc(Terrain.WATER);
				}
				if ((Terrain.flags[tile] & Terrain.PIT) != 0) {
					return tileDesc(Terrain.CHASM);
				}
				return "";
		}
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight(){
		return HEIGHT;
	}

	public int getLength() {
		return WIDTH * HEIGHT;
	}

	public Point cellToPoint(int cell ){
		return new Point(cell % getWidth(), cell / getWidth());
	}

	public int pointToCell( Point p ){
		return p.x + p.y*getWidth();
	}

	public Mob findMob( int pos ){
		for (Mob mob : mobs){
			if (mob.pos == pos){
				return mob;
			}
		}
		return null;
	}
}
