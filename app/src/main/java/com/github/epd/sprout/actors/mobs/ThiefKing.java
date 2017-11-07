
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.consumables.AdamantRing;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ThiefKingSprite;
import com.github.epd.sprout.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class ThiefKing extends Mob implements Callback {


	{
		name = Messages.get(this, "name");
		spriteClass = ThiefKingSprite.class;

		HP = HT = 500;
		defenseSkill = 28;

		EXP = 16;
		maxLvl = 14;
		flying = true;

		properties.add(Property.UNDEAD);
		properties.add(Property.BOSS);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 70);
	}

	@Override
	public int attackSkill(Char target) {
		return 25;
	}

	@Override
	public int dr() {
		return 14;
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	public void die(Object cause) {

		GameScene.bossSlain();
		Item.autocollect(new AdamantRing(), pos);
		Dungeon.level.drop(new Gold(Random.Int(1900, 4000)), pos).sprite.drop();
		super.die(cause);

		if (!Dungeon.limitedDrops.armband.dropped() && Random.Float() < 0.2f){
			Dungeon.limitedDrops.armband.drop();
			Item.autocollect(new MasterThievesArmband(), pos);
		}

		Dungeon.banditkingkilled = true;

		yell(Messages.get(this, "die"));

	}


	@Override
	public void call() {
		next();
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
		RESISTANCES.add(LightningTrap.Electricity.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
