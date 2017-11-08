
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.consumables.ArmorKit;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.RedDewdrop;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.DwarfKingTombSprite;
import com.github.epd.sprout.ui.BossHealthBar;
import com.watabou.utils.Random;

public class DwarfKingTomb extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = DwarfKingTombSprite.class;

		HP = HT = 600;
		defenseSkill = 5;

		EXP = 10;

		hostile = false;
		state = PASSIVE;

		loot = new RedDewdrop();
		lootChance = 0.05f;

		properties.add(Property.UNDEAD);
		properties.add(Property.BOSS);

	}

	@Override
	public void beckon(int cell) {
		// Do nothing
	}

	@Override
	public void add(Buff buff) {
	}


	@Override
	public int damageRoll() {
		return 0;
	}

	@Override
	public int attackSkill(Char target) {
		return 0;
	}

	@Override
	public int dr() {
		return 18;

	}


	private boolean checkKing() {

		int kingAlive = 0;
		if (Dungeon.level.mobs != null) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob instanceof King) {
					kingAlive++;
				}
			}
		}
		return kingAlive > 0;
	}

	@Override
	public void damage(int dmg, Object src) {
		if (checkKing()) {
			yell(Messages.get(this, "im"));
		} else {
			BossHealthBar.assignBoss(this);
			super.damage(dmg, src);
		}
	}


	@Override
	public String description() {
		return Messages.get(this, "desc");
	}


	@SuppressWarnings("unchecked")
	@Override
	public void die(Object cause) {

		for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
			if (mob instanceof DwarfLich || mob instanceof King || mob instanceof King.Undead || mob instanceof Wraith) {
				mob.die(cause);
			}
		}

		GameScene.bossSlain();
		Item.autocollect(new ArmorKit(), pos);
		Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
		Dungeon.level.drop(new Gold(Random.Int(4900, 10000)), pos).sprite.drop();

		super.die(cause);

	}

}
