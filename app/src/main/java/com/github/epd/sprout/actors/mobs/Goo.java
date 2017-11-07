
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.GooWarn;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Ooze;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.LloydsBeacon;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.teleporter.Sokoban1;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.items.weapon.melee.Chainsaw;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.SewerBossLevel;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.GooSprite;
import com.github.epd.sprout.ui.BossHealthBar;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Goo extends Mob {
	{
		name = Messages.get(this, "name");
		HP = HT = 200; //200
		EXP = 10;
		defenseSkill = 12;
		spriteClass = GooSprite.class;

		loot = new LloydsBeacon().identify();
		lootChance = 0.5f;

		lootOther = new Chainsaw().enchantBuzz().identify();
		lootChanceOther = 1f;

		properties.add(Property.UNDEAD);
		properties.add(Property.BOSS);
	}

	private int pumpedUp = 0;
	private int goosAlive = 0;

	@Override
	public int damageRoll() {
		if (pumpedUp > 0) {
			pumpedUp = 0;
			for (int i = 0; i < PathFinder.NEIGHBOURS9DIST2.length; i++) {
				int j = pos + PathFinder.NEIGHBOURS9DIST2[i];
				if (Dungeon.level.insideMap(j) && Level.passable[j])
					CellEmitter.get(j).burst(ElmoParticle.FACTORY, 10);
			}
			Sample.INSTANCE.play(Assets.SND_BURNING);
			return Random.NormalIntRange(5, 30);
		} else {
			return Random.NormalIntRange(2, 12);
		}
	}

	@Override
	public int attackSkill(Char target) {
		return (pumpedUp > 0) ? 30 : 15;
	}

	@Override
	public int dr() {
		return 2;
	}

	@Override
	public boolean act() {

		if (Level.water[pos] && HP < HT) {
			sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			HP++;
		}

		return super.act();
	}


	@Override
	protected boolean canAttack(Char enemy) {
		return (pumpedUp > 0) ? distance(enemy) <= 2 : super.canAttack(enemy);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, Ooze.class);
			enemy.sprite.burst(0x000000, 5);
		}

		if (pumpedUp > 0) {
			Camera.main.shake(3, 0.2f);
		}

		return damage;
	}

	@Override
	protected boolean doAttack(Char enemy) {
		if (pumpedUp == 1) {
			((GooSprite) sprite).pumpUp();
			for (int i = 0; i < PathFinder.NEIGHBOURS9DIST2.length; i++) {
				int j = pos + PathFinder.NEIGHBOURS9DIST2[i];
				if (Dungeon.level.insideMap(j) && Level.passable[j])
					GameScene.add(Blob.seed(j, 2, GooWarn.class));
			}
			pumpedUp++;

			spend(attackDelay());

			return true;
		} else if (pumpedUp >= 2 || Random.Int(3) > 0) {

			boolean visible = Dungeon.visible[pos];

			if (visible) {
				if (pumpedUp >= 2) {
					((GooSprite) sprite).pumpAttack();
				} else
					sprite.attack(enemy.pos);
			} else {
				attack(enemy);
			}

			spend(attackDelay());

			return !visible;

		} else {

			pumpedUp++;

			((GooSprite) sprite).pumpUp();

			for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
				int j = pos + PathFinder.NEIGHBOURS9[i];
				GameScene.add(Blob.seed(j, 2, GooWarn.class));

			}

			if (Dungeon.visible[pos]) {
				sprite.showStatus(CharSprite.NEGATIVE, "!!!");
				GLog.n(Messages.get(this, "atk"));
			}

			spend(attackDelay());

			return true;
		}
	}

	@Override
	public boolean attack(Char enemy) {
		boolean result = super.attack(enemy);
		pumpedUp = 0;
		return result;
	}

	@Override
	protected boolean getCloser(int target) {
		pumpedUp = 0;
		return super.getCloser(target);
	}

	@Override
	public void die(Object cause) {

		super.die(cause);

		for (Mob mob : Dungeon.level.mobs) {

			if (mob instanceof Goo || mob instanceof PoisonGoo) {
				goosAlive++;
			}

		}

		if (goosAlive == 0) {

			((SewerBossLevel) Dungeon.level).unseal();

			GameScene.bossSlain();
			Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();

		}
		Item.autocollect(new Sokoban1(), pos);
		yell(Messages.get(this, "die"));
	}

	protected boolean spawnedMini = false;

	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		yell(Messages.get(this, "notice"));
		if (!spawnedMini) {
			PoisonGoo.spawnAround(pos);
			spawnedMini = true;
		}
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private final String PUMPEDUP = "pumpedup";

	@Override
	public void storeInBundle(Bundle bundle) {

		super.storeInBundle(bundle);

		bundle.put(PUMPEDUP, pumpedUp);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {

		super.restoreFromBundle(bundle);
		if (state != SLEEPING) BossHealthBar.assignBoss(this);
		pumpedUp = bundle.getInt(PUMPEDUP);
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Death.class);
		RESISTANCES.add(ScrollOfPsionicBlast.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Roots.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

}
