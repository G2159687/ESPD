
package com.github.epd.sprout.actors.mobs.npcs;


import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.RatKingSprite;

public class RatKingDen extends NPC {

	{
		name = Messages.get(RatKing.class, "name");
		spriteClass = RatKingSprite.class;

		state = SLEEPING;
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public float speed() {
		return 2f;
	}

	@Override
	protected Char chooseEnemy() {
		return null;
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public boolean reset() {
		return true;
	}


	@Override
	public boolean interact() {

		sprite.turnTo(pos, Dungeon.hero.pos);
		if (state == SLEEPING) {
			notice();
			yell(Messages.get(RatKing.class, "yone"));
			state = WANDERING;

		} else {
			yell(Messages.get(RatKing.class, "yell"));
		}
		return true;
	}

	@Override
	public String description() {
		return ((RatKingSprite) sprite).festive ? Messages.get(RatKing.class, "descone")
				: Messages.get(RatKing.class, "desctwo");
	}
}
