
package com.github.epd.sprout.actors.mobs.npcs;


import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.weapon.melee.Spork;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.RatKingSprite;

public class RatKing extends NPC {

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

		int checkChests = 0;
		int length = Dungeon.level.getLength();
		for (int i = 0; i < length; i++) {
			Heap chest = Dungeon.level.heaps.get(i);
			if (chest != null && chest.chestCheck()) {
				checkChests++;
			}
		}

		Spork spork = Dungeon.hero.belongings.getItem(Spork.class);

		sprite.turnTo(pos, Dungeon.hero.pos);
		if (state == SLEEPING) {
			notice();
			yell(Messages.get(RatKing.class, "yone"));
			yell(Messages.get(RatKing.class, "ytwo"));
			state = WANDERING;
		} else if (Statistics.deepestFloor > 10 && checkChests >= Dungeon.ratChests && spork == null) {
			yell(Messages.get(RatKing.class, "notsteal"));
			Dungeon.sporkAvail = true;
		} else if (checkChests < Dungeon.ratChests) {
			Dungeon.sporkAvail = false;
			yell(Messages.get(RatKing.class, "steal"));
		} else if (spork != null) {
			yell(Messages.get(RatKing.class, "found"));

		} else {
			yell(Messages.get(RatKing.class, "yell"));
		}
		return true;
	}

	@Override
	public String description() {
		return ((RatKingSprite) sprite).festive ? Messages.get(RatKing.class, "descone") : Messages.get(RatKing.class, "desctwo");
	}
}
