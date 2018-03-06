
package com.github.epd.sprout.actors.mobs.pets;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.VelociroosterSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class Velocirooster extends PET {

	{
		name = Messages.get(this, "name");
		spriteClass = VelociroosterSprite.class;
		flying = false;
		state = HUNTING;
		level = 1;
		type = 3;
		cooldown = 1000;

	}

	protected int regen = 1;
	protected float regenChance = 0.05f;


	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (2 + level) * 7;
		defenseSkill = 1 + level * 2;
	}

	@Override
	public void flee() {
		((VelociroosterSprite) sprite).hpBar.killAndErase();
		super.flee();
	}

	@Override
	public int dr() {
		return level * 3;
	}

	@Override
	public int attackSkill(Char target) {
		return defenseSkill;
	}

	@Override
	public int damageRoll() {

		int dmg = 0;
		if (cooldown == 0) {
			dmg = Random.NormalIntRange(HT / 2, HT);
			if (level < 50)
				GLog.p(Messages.get(this, "ready"));
			cooldown = 1000;
		} else {
			dmg = Random.NormalIntRange(HT / 5, HT / 2);
		}
		return dmg;

	}

	@Override
	protected boolean act() {

		if (cooldown > 0) {
			cooldown = Math.max(cooldown - (level * level), 0);
			if (level < 50 && cooldown == 0) {
				yell(Messages.get(this, "atk"));
			}
		}

		if (Random.Float() < regenChance && HP < HT) {
			HP += regen;
		}

		return super.act();
	}


	@Override
	public String description() {
		return Messages.get(this, "desc");
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