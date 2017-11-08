
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Badges;
import com.github.epd.sprout.Badges.Badge;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Egg;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.consumables.TomeOfMastery;
import com.github.epd.sprout.items.artifacts.LloydsBeacon;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicMapping;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.teleporter.Sokoban2;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.TenguSprite;
import com.github.epd.sprout.ui.BossHealthBar;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Tengu extends Mob {

	private static final int JUMP_DELAY = 5;

	{
		name = Messages.get(this, "name");
		spriteClass = TenguSprite.class;
		baseSpeed = 2f;

		HP = HT = 300;
		EXP = 20;
		defenseSkill = 30;

		properties.add(Property.UNDEAD);
		properties.add(Property.BOSS);
	}

	private int timeToJump = JUMP_DELAY;


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15, 18);
	}

	@Override
	public int attackSkill(Char target) {
		return 20;
	}

	@Override
	public int dr() {
		return 10;
	}

	@Override
	public void die(Object cause) {

		//note
		Badges.Badge badgeToCheck = null;
		switch (Dungeon.hero.heroClass) {
			case WARRIOR:
				badgeToCheck = Badge.MASTERY_WARRIOR;
				break;
			case MAGE:
				badgeToCheck = Badge.MASTERY_MAGE;
				break;
			case ROGUE:
				badgeToCheck = Badge.MASTERY_ROGUE;
				break;
			case HUNTRESS:
				badgeToCheck = Badge.MASTERY_HUNTRESS;
				break;
		}
		if (!Badges.isUnlocked(badgeToCheck)) {
			Item.autocollect(new TomeOfMastery(), pos);
		} else {
			Item.autocollect(new Egg(), pos);
		}

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
			GLog.p(Messages.get(LloydsBeacon.class, "stronger"));
		}

		Item.autocollect(new Sokoban2(), pos);

		GameScene.bossSlain();
		Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();

		super.die(cause);

		Dungeon.tengukilled = true;

		TenguEscape.spawnAt(pos);

	}

	@Override
	protected boolean getCloser(int target) {
		if (Level.fieldOfView[target]) {
			jump();
			return true;
		} else {
			return super.getCloser(target);
		}
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {
		timeToJump--;
		if (timeToJump <= 0 && Dungeon.level.adjacent(pos, enemy.pos)) {
			jump();
			return true;
		} else {
			return super.doAttack(enemy);
		}
	}

	private void jump() {
		timeToJump = JUMP_DELAY;

		for (int i = 0; i < 2; i++) {
			int trapPos;
			do {
				trapPos = Random.Int(Dungeon.level.getLength());
			} while (!Level.fieldOfView[trapPos] || !Level.passable[trapPos]);

			if (Dungeon.level.map[trapPos] == Terrain.INACTIVE_TRAP) {
				Level.set(trapPos, Terrain.POISON_TRAP);
				GameScene.updateMap(trapPos);
				ScrollOfMagicMapping.discover(trapPos);
			}
		}

		int newPos;

		do {
			newPos = Random.Int(Dungeon.level.getLength());
		} while (!Level.fieldOfView[newPos] || !Level.passable[newPos]
				|| Dungeon.level.adjacent(newPos, enemy.pos)
				|| Actor.findChar(newPos) != null);
		sprite.move(pos, newPos);
		move(newPos);

		if (Dungeon.visible[newPos]) {
			CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
			Sample.INSTANCE.play(Assets.SND_PUFF);
		}


		spend(1 / speed());
	}

	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		yell(Messages.get(this, "notice", Dungeon.hero.givenName()));
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Poison.class);
		RESISTANCES.add(Death.class);
		RESISTANCES.add(ScrollOfPsionicBlast.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
