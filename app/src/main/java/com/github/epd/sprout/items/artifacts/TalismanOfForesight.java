package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Awareness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MindVision;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.SpellSprite;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class TalismanOfForesight extends Artifact {

	{
		name = Messages.get(TalismanOfForesight.class,"name");
		image = ItemSpriteSheet.ARTIFACT_TALISMAN;

		level = 0;
		exp = 0;
		levelCap = 10;

		charge = 0;
		partialCharge = 0;
		chargeCap = 100;
		reinforced = true;

		defaultAction = AC_SCRY;
	}

	public static final String AC_SCRY = Messages.get(TalismanOfForesight.class,"ac_scry");

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge == 100 && !cursed)
			actions.add(AC_SCRY);
		return actions;
	}

	@Override
	public boolean isUpgradable() {
		return true;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_SCRY)) {

			if (!isEquipped(hero))
				GLog.i(Messages.get(TalismanOfForesight.class,"equip"));
			else if (charge != chargeCap)
				GLog.i(Messages.get(TalismanOfForesight.class,"no_charge"));
			else if (Dungeon.depth > 50 && level < 50)
				GLog.i(Messages.get(TalismanOfForesight.class,"sokoban"));
			else {
				hero.sprite.operate(hero.pos);
				hero.busy();
				Sample.INSTANCE.play(Assets.SND_BEACON);
				charge = 0;
				for (int i = 0; i < Level.getLength(); i++) {
					int terr = Dungeon.level.map[i];
					if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
						GameScene.updateMap(i);
						if (Dungeon.visible[i]) {
							GameScene.discoverTile(i, terr);
						}
					}
				}
				if (level > 10 && Dungeon.depth <= 50){
					//Magic mapping normal
					int length = Level.LENGTH;
					int[] map = Dungeon.level.map;
					boolean[] mapped = Dungeon.level.mapped;
					boolean[] discoverable = Level.discoverable;
					for (int i = 0; i < length; i++) {
						int terr = map[i];
						if (discoverable[i]) {
							mapped[i] = true;
							if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
								Level.set(i, Terrain.discover(terr));
								GameScene.updateMap(i);
								if (Dungeon.visible[i]) {
									GameScene.discoverTile(i, terr);
								}
							}
						}
					}
					GameScene.updateFog();
					SpellSprite.show(curUser, SpellSprite.MAP);
				}
				if (level > 24){
					Buff.affect(hero, MindVision.class, (float) level);
				}
				if (level > 49 && Dungeon.depth > 50){
					//Magic mapping with sokoban
					int length = Level.LENGTH;
					int[] map = Dungeon.level.map;
					boolean[] mapped = Dungeon.level.mapped;
					boolean[] discoverable = Level.discoverable;
					for (int i = 0; i < length; i++) {
						int terr = map[i];
						if (discoverable[i]) {
							mapped[i] = true;
							if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
								Level.set(i, Terrain.discover(terr));
								GameScene.updateMap(i);
								if (Dungeon.visible[i]) {
									GameScene.discoverTile(i, terr);
								}
							}
						}
					}
					GameScene.updateFog();
					SpellSprite.show(curUser, SpellSprite.MAP);
				}

				GLog.p(Messages.get(TalismanOfForesight.class,"scry"));
				updateQuickslot();

				Buff.affect(hero, Awareness.class, Awareness.DURATION);
				Dungeon.observe();
			}
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Foresight();
	}

	@Override
	public String desc() {
		String desc = Messages.get(this,"desc");
		if (isEquipped(Dungeon.hero)) {
			if (!cursed) {
				desc += "\n\n" + Messages.get(this,"desc_worn");
				if (charge == 100)
					desc += "\n\n" + Messages.get(this,"full");
			} else {
				desc += "\n\n" + Messages.get(this,"desc_cursed");
			}
		}

		return desc;
	}

	public class Foresight extends ArtifactBuff {
		private int warn = 0;

		@Override
		public boolean act() {
			spend(TICK);

			boolean smthFound = false;

			int distance = 3;

			int cx = target.pos % Level.getWidth();
			int cy = target.pos / Level.getWidth();
			int ax = cx - distance;
			if (ax < 0) {
				ax = 0;
			}
			int bx = cx + distance;
			if (bx >= Level.getWidth()) {
				bx = Level.getWidth() - 1;
			}
			int ay = cy - distance;
			if (ay < 0) {
				ay = 0;
			}
			int by = cy + distance;
			if (by >= Level.HEIGHT) {
				by = Level.HEIGHT - 1;
			}

			for (int y = ay; y <= by; y++) {
				for (int x = ax, p = ax + y * Level.getWidth(); x <= bx; x++, p++) {

					if (Dungeon.visible[p] && Level.secret[p]
							&& Dungeon.level.map[p] != Terrain.SECRET_DOOR)
						smthFound = true;
				}
			}

			if (smthFound == true && !cursed) {
				if (warn == 0) {
					GLog.w(Messages.get(this,"uneasy"));
					if (target instanceof Hero) {
						((Hero) target).interrupt();
					}
				}
				warn = 3;
			} else {
				if (warn > 0) {
					warn--;
				}
			}
			BuffIndicator.refreshHero();

			// fully charges in 2500 turns at lvl=0, scaling to 1000 turns at
			// lvl = 10.
			if (charge < 100 && !cursed) {
				partialCharge += 0.04 + (level * 0.006);

				if (partialCharge > 1 && charge < 100) {
					partialCharge--;
					charge++;
					updateQuickslot();
				} else if (charge >= 100) {
					partialCharge = 0;
					GLog.p(Messages.get(this,"full_charge"));
				}
			}

			return true;
		}

		public void charge() {
			charge = Math.min(charge + (2 + (level / 3)), chargeCap);
			exp++;
			if (exp >= 4 && level < levelCap) {
				upgrade();
				GLog.p(Messages.get(this,"levelup"));
				exp -= 4;
			}
		}

		@Override
		public String toString() {
			return Messages.get(this,"name");
		}

		@Override
		public int icon() {
			if (warn == 0)
				return BuffIndicator.NONE;
			else
				return BuffIndicator.FORESIGHT;
		}

		@Override
		public String desc() {	return Messages.get(this,"desc"); }
	}
}
