
package com.github.epd.sprout.items.artifacts;
//If it weren't super obvious, this is going to become an artifact soon.

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.scrolls.ScrollOfTeleportation;
import com.github.epd.sprout.items.wands.WandOfBlink;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.CellSelector;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.QuickSlotButton;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndOtiluke2;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class LloydsBeacon extends Artifact {

	private static final String TXT_CREATURES = Messages.get(LloydsBeacon.class, "creatures");
	private static final String TXT_RETURN = Messages.get(LloydsBeacon.class, "return");
	private static final String TXT_INFO = Messages.get(LloydsBeacon.class, "info");
	private static final String TXT_SET = Messages.get(LloydsBeacon.class, "set");

	public static final float TIME_TO_USE = 1;

	public static final String AC_ZAP = Messages.get(LloydsBeacon.class, "ac_zap");
	public static final String AC_SET = Messages.get(LloydsBeacon.class, "ac_set");
	public static final String AC_RETURN = Messages.get(LloydsBeacon.class, "ac_return");
	public static final String AC_PORTREGULAR = Messages.get(LloydsBeacon.class, "ac_regular");

	private int returnDepth = -1;
	private int returnPos;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ARTIFACT_BEACON;

		levelCap = 3;

		charge = 0;
		chargeCap = 3 + level;

		defaultAction = AC_ZAP;
		usesTargeting = true;
	}

	private static final String DEPTH = "depth";
	private static final String POS = "pos";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEPTH, returnDepth);
		if (returnDepth != -1) {
			bundle.put(POS, returnPos);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		returnDepth = bundle.getInt(DEPTH);
		returnPos = bundle.getInt(POS);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (!Dungeon.bossLevel() && !((Dungeon.depth > 50 && Dungeon.depth < 56) || Dungeon.depth == 35)) {
			actions.add(AC_ZAP);
		}
		if (Dungeon.depth < 25 && !Dungeon.bossLevel()) {
			actions.add(AC_SET);
		}
		if (returnDepth != -1 && Dungeon.depth < 25 && !Dungeon.bossLevel()) {
			actions.add(AC_RETURN);
		}
		if (Dungeon.depth < 26 && !hero.petfollow && !Dungeon.bossLevel()) {
			actions.add(AC_PORTREGULAR);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action == AC_PORTREGULAR) {

			GameScene.show(new WndOtiluke2());

		}

		if (action == AC_SET || action == AC_RETURN) {
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				Char ch = Actor.findChar(hero.pos + PathFinder.NEIGHBOURS8[i]);
				if (ch != null && !(ch instanceof Mob && !((Mob) ch).hostile)) {
					GLog.w(TXT_CREATURES);
					return;
				}
			}
		}

		if (action == AC_ZAP) {

			curUser = hero;
			int chargesToUse = Dungeon.depth > 20 ? 2 : 1;

			if (!isEquipped(hero)) {
				GLog.i(Messages.get(Artifact.class, "need_to_equip"));
				QuickSlotButton.cancel();

			} else if (charge < chargesToUse) {
				GLog.i(Messages.get(this, "no_charge"));
				QuickSlotButton.cancel();

			} else {
				GameScene.selectCell(zapper);
			}

		} else if (action == AC_SET) {

			returnDepth = Dungeon.depth;
			returnPos = hero.pos;

			hero.spend(LloydsBeacon.TIME_TO_USE);
			hero.busy();

			hero.sprite.operate(hero.pos);
			Sample.INSTANCE.play(Assets.SND_BEACON);

			GLog.i(TXT_RETURN);

		} else if (action == AC_RETURN) {

			if (returnDepth == Dungeon.depth) {
				WandOfBlink.appear(hero, returnPos);
				Dungeon.level.press(returnPos, hero);
				Dungeon.observe();
				GameScene.updateFog();
			} else {

				Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null) buff.detach();

				Buff buffinv = Dungeon.hero.buff(Invisibility.class);
				if (buffinv != null)
					buffinv.detach();
				Invisibility.dispel();
				Dungeon.hero.invisible = 0;

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
					if (mob instanceof DriedRose.GhostHero) mob.destroy();

				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				InterlevelScene.returnDepth = returnDepth;
				InterlevelScene.returnPos = returnPos;
				Game.switchScene(InterlevelScene.class);
			}


		} else {

			super.execute(hero, action);

		}
	}

	protected CellSelector.Listener zapper = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer target) {

			if (target == null) return;

			Invisibility.dispel();
			charge -= Dungeon.depth > 20 ? 2 : 1;
			updateQuickslot();

			if (Actor.findChar(target) == curUser) {
				ScrollOfTeleportation.teleportHero(curUser);
				Sample.INSTANCE.play(Assets.SND_TELEPORT);
				curUser.spendAndNext(1f);
			} else {
				final Ballistica bolt = new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT);
				final Char ch = Actor.findChar(bolt.collisionPos);

				if (ch == curUser) {
					ScrollOfTeleportation.teleportHero(curUser);
					Sample.INSTANCE.play(Assets.SND_TELEPORT);
					curUser.spendAndNext(1f);
				} else {
					Sample.INSTANCE.play(Assets.SND_ZAP);
					curUser.sprite.zap(bolt.collisionPos);
					curUser.busy();

					MagicMissile.force(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, new Callback() {
						@Override
						public void call() {
							if (ch != null) {

								int count = 10;
								int pos;
								do {
									pos = Dungeon.level.randomRespawnCell();
									if (count-- <= 0) {
										break;
									}
								} while (pos == -1);

								if (pos == -1) {

									GLog.w(ScrollOfTeleportation.TXT_NO_TELEPORT);

								} else if (ch.properties().contains(Char.Property.IMMOVABLE)) {

									GLog.w(Messages.get(LloydsBeacon.class, "tele_fail"));

								} else {

									ch.pos = pos;
									ch.sprite.place(ch.pos);
									ch.sprite.visible = Dungeon.visible[pos];

								}
							}
							curUser.spendAndNext(1f);
						}
					});

				}


			}

		}

		@Override
		public String prompt() {
			return Messages.get(LloydsBeacon.class, "prompt");
		}
	};

	@Override
	protected ArtifactBuff passiveBuff() {
		return new beaconRecharge();
	}

	@Override
	public Item upgrade() {
		chargeCap++;

		return super.upgrade();
	}

	@Override
	public String desc() {
		return TXT_INFO + (returnDepth == -1 ? "" : Utils.format(TXT_SET, returnDepth));
	}

	public void reset() {
		returnDepth = -1;
	}

	private static final Glowing WHITE = new Glowing(0xFFFFFF);

	@Override
	public Glowing glowing() {
		return returnDepth != -1 ? WHITE : null;
	}

	public class beaconRecharge extends ArtifactBuff {
		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed) {
				partialCharge += 1 / (100f - (chargeCap - charge) * 10f);

				if (partialCharge >= 1) {
					partialCharge--;
					charge++;

					if (charge == chargeCap) {
						partialCharge = 0;
					}
				}
			}

			updateQuickslot();
			spend(TICK);
			return true;
		}
	}
}
