
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.RedWraith;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Pickaxe extends Item {

	public static final String AC_MINE = Messages.get(Pickaxe.class, "ac_mine");
	public static final float TIME_TO_MINE = 2;
	private static final String TXT_NO_VEIN = Messages.get(Pickaxe.class, "no_vein");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PICKAXE;

		unique = true;

		defaultAction = AC_MINE;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_MINE);
		return actions;
	}

	@Override
	public void execute(final Hero hero, String action) {

		if (action == AC_MINE) {

			if (!(Dungeon.depth > 10 && Dungeon.depth < 16 || Dungeon.depth > 55 && Dungeon.depth < 66 || Dungeon.depth == 27)){
				GLog.w(TXT_NO_VEIN);
				return;
			}

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {

				final int pos = hero.pos + PathFinder.NEIGHBOURS8[i];
				final Heap heap = Dungeon.level.heaps.get(pos);
				if (Dungeon.level.map[pos] == Terrain.WALL_DECO) {

					hero.spend(TIME_TO_MINE);
					hero.busy();

					hero.sprite.attack(pos, new Callback() {

						@Override
						public void call() {

							CellEmitter.center(pos).burst(
									Speck.factory(Speck.STAR), 7);
							Sample.INSTANCE.play(Assets.SND_EVOKE);

							Level.set(pos, Terrain.WALL);
							GameScene.updateMap(pos);

							DarkGold gold = new DarkGold();
							if (Dungeon.depth > 10 && Dungeon.depth < 16 || Dungeon.depth > 55 && Dungeon.depth < 66) {
								if (gold.doPickUp(Dungeon.hero)) {
									GLog.i(Messages.get(Hero.class, "have"), gold.name());
								} else {
									Dungeon.level.drop(gold, hero.pos).sprite
											.drop();
								}
							} else if (Dungeon.depth == 27){
								Item.autocollect(Generator.random(Generator.Category.MUSHROOM), hero.pos);
							}

							Hunger hunger = hero.buff(Hunger.class);
							if (hunger != null && !hunger.isStarving()) {
								hunger.satisfy(-Hunger.STARVING / 50);
								BuffIndicator.refreshHero();
							}

							hero.onOperateComplete();
						}
					});

					return;
				} else if (heap != null && heap.type == Heap.Type.HARD_TOMB){

					hero.spend(TIME_TO_MINE);
					hero.busy();

					hero.sprite.attack(pos, new Callback() {

						@Override
						public void call() {

							heap.type = Heap.Type.HEAP;
							Sample.INSTANCE.play(Assets.SND_TOMB);
							Camera.main.shake(1, 0.5f);
							RedWraith.spawnAround2x(hero.pos);

							heap.sprite.link();
							heap.sprite.drop();

							GameScene.updateMap(pos);

							hero.onOperateComplete();
						}
					});

					return;
				}
			}

			GLog.w(TXT_NO_VEIN);

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
