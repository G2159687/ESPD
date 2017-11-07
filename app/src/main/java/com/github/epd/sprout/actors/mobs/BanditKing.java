
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.CountDown;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.items.weapon.melee.Spork;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.BanditKingSprite;
import com.github.epd.sprout.ui.BossHealthBar;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class BanditKing extends Thief {

	public Item item;

	{
		name = Messages.get(BanditKing.class, "name");
		spriteClass = BanditKingSprite.class;
		HP = HT = 200; //200

		EXP = 10;
		maxLvl = 25;
		flying = true;

		lootChance = 0.05f;
		defenseSkill = 20; //20
		if (Dungeon.depth < 25) {
			Dungeon.sporkAvail = false;
		}

		properties.add(Property.UNDEAD);
		properties.add(Property.BOSS);

	}

	@Override
	public int dr() {
		return 20; //20
	}

	@Override
	public float speed() {
		return 2f;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (enemy.buff(CountDown.class) == null) {
			Buff.affect(enemy, CountDown.class);
			state = FLEEING;
		}

		return damage;
	}

	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
	}

	@Override
	protected boolean steal(Hero hero) {
		if (super.steal(hero)) {


			if (Dungeon.depth < 25) {
				Buff.prolong(hero, Blindness.class, Random.Int(5, 12));
				Buff.affect(hero, Poison.class).set(
						Random.Int(5, 7) * Poison.durationFactor(enemy));
				Buff.prolong(hero, Cripple.class, Cripple.DURATION);
				Dungeon.observe();
			} else if (hero.buff(CountDown.class) == null) {
				Buff.affect(enemy, CountDown.class);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		if (Dungeon.depth < 25) {
			yell(Messages.get(BanditKing.class, "die"));
			GLog.w(Messages.get(BanditKing.class, "dis"));
			if (!Dungeon.limitedDrops.spork.dropped()) {
				Item.autocollect(new Spork(), pos);
				Dungeon.limitedDrops.spork.drop();
				Dungeon.sporkAvail = false;
				yell(Messages.get(BanditKing.class, "spork"));
			}
		}

		if (!Dungeon.limitedDrops.armband.dropped() && Random.Float() < 0.1f){
			Dungeon.limitedDrops.armband.drop();
			Item.autocollect(new MasterThievesArmband().identify(), pos);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}

}
