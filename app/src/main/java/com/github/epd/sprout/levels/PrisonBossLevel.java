
package com.github.epd.sprout.levels;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.Scene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class PrisonBossLevel extends Level {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}


	private boolean enteredArena = false;
	private static final String ENTERED = "entered";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ENTERED, enteredArena);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		enteredArena = bundle.getBoolean(ENTERED);
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

	@Override
	protected void createMobs() {
	}

	@Override
	protected boolean build() {

		setSize(8, 13);

		map = Layouts.PRISON_BOSS.clone();

		buildFlagMaps();
		cleanWalls();

		entrance = 3 + getWidth() * 11;
		exit = 3 + getWidth();

		return true;
	}

	@Override
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		drop(new IronKey(Dungeon.depth), entrance + 1).type = Heap.Type.CHEST;
	}

	@Override
	public void press(int cell, Char ch) {

		super.press(cell, ch);

		if (ch == Dungeon.hero && !enteredArena && cell < 8 * getWidth()) {

			locked = true;
			enteredArena = true;

			Mob boss = Bestiary.mob(Dungeon.depth);
			boss.state = boss.WANDERING;
			boss.pos = 1 + getWidth() * 2;
			GameScene.add(boss);
			boss.notice();

			mobPress(boss);

			set(3 + getWidth() * 8, Terrain.LOCKED_DOOR);
			GameScene.updateMap(3 + getWidth() * 8);

			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (mob instanceof PET) {
					if (!((PET) mob).stay) {
						((PET) mob).pos = 3 + getWidth() * 8;
						mob.sprite.move(Dungeon.hero.pos, mob.pos);
					}
				}
				if (mob instanceof DriedRose.GhostHero) {
					((DriedRose.GhostHero) mob).pos = 3 + getWidth() * 8;
					mob.sprite.move(Dungeon.hero.pos, mob.pos);
				}
			}

			Dungeon.observe();

		}
	}

	@Override
	public int randomDestination() {

		int cell = -1;

		do {
			cell = Random.IntRange(getWidth() * 2, getWidth() * 8);
		} while (Level.passable[cell]);
		return cell;

	}

	@Override
	public Heap drop(Item item, int cell) {

		if (item instanceof SkeletonKey) {
			locked = false;

			set(3 + getWidth() * 8, Terrain.DOOR);
			GameScene.updateMap(3 + getWidth() * 8);
			Dungeon.observe();
		}

		return super.drop(item, cell);
	}

	@Override
	public int randomRespawnCell() {
		return -1;
	}

	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(PrisonLevel.class, "water_name");
			default:
				return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(PrisonLevel.class, "empty_deco_desc");
			default:
				return super.tileDesc(tile);
		}
	}

	@Override
	public void addVisuals(Scene scene) {
		PrisonLevel.addVisuals(this, scene);
	}
}
