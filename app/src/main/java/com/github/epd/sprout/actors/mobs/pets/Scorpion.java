
package com.github.epd.sprout.actors.mobs.pets;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ScorpionSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class Scorpion extends PET {

	{
		name = Messages.get(Scorpion.class, "name");
		spriteClass = ScorpionSprite.class;
		flying = false;
		state = HUNTING;
		level = 1;
		type = 8;
		cooldown = 1000;

	}

	protected int regen = 1;
	protected float regenChance = 0.1f;


	@Override
	public int dr() {
		return level * 4;
	}

	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (3 + level) * 10;
		defenseSkill = 1 + level * 2;
	}


	@Override
	public int attackSkill(Char target) {
		return defenseSkill;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(HT / 4, HT);
	}

	@Override
	public void flee() {
		((ScorpionSprite) sprite).hpBar.killAndErase();
		super.flee();
	}

	@Override
	protected boolean act() {

		if (cooldown > 0) {
			cooldown = Math.max(cooldown - (level * level), 0);
			if (level < 50 && cooldown == 0) {
				GLog.p(Messages.get(Scorpion.class, "ready"));
			}
		}

		if (Random.Float() < regenChance && HP < HT) {
			HP += regen;
		}

		return super.act();
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (cooldown > 0 && Random.Int(10) == 0) {
			Buff.prolong(enemy, Paralysis.class, Random.Float(1, 1.5f + level));
		}
		if (cooldown == 0) {
			Buff.prolong(enemy, Paralysis.class, Random.Float(1, 1.5f + level));

			sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 1);
			sprite.showStatus(CharSprite.POSITIVE, Integer.toString(damage));

			HP = Math.min(HT, HP + damage);

			if (Dungeon.hero.HP < Dungeon.hero.HT) {
				Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 1);
				Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(damage));
				Dungeon.hero.HP = Math.min(Dungeon.hero.HT, Dungeon.hero.HP + damage);
			}

			damage += damage;

			if (level < 50)
				yell(Messages.get(Scorpion.class, "atk"));
			cooldown = 1000;
		}

		return damage;
	}

	@Override
	public String description() {
		return Messages.get(Scorpion.class, "desc");
	}


	@Override
	public boolean interact() {

		if (this.buff(MagicalSleep.class) != null) {
			Buff.detach(this, MagicalSleep.class);
		}

		if (state == SLEEPING) {
			state = HUNTING;
		}
		if (buff(Paralysis.class) != null) {
			Buff.detach(this, Paralysis.class);
			GLog.i(Messages.get(bee.class, "shake"), name);
		}

		int curPos = pos;

		if (Level.passable[pos] && Dungeon.level.map[pos] != Terrain.CHASM) {

			moveSprite(pos, Dungeon.hero.pos);
			move(Dungeon.hero.pos);

			Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
			Dungeon.hero.move(curPos);

			Dungeon.hero.spend(1 / Dungeon.hero.speed());
			Dungeon.hero.busy();
		}

		return true;
	}


}