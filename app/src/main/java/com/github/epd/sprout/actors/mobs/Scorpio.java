
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Light;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.items.weapon.enchantments.Leech;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ScorpioSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Scorpio extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = ScorpioSprite.class;

		HP = HT = 100 + (adj(0) * Random.NormalIntRange(1, 3));
		defenseSkill = 24 + adj(1);
		viewDistance = Light.DISTANCE;

		EXP = 14;
		maxLvl = 25;

		loot = new PotionOfHealing();
		lootChance = 0.2f;

		lootOther = new MysteryMeat();
		lootChanceOther = 0.333f; // by default, see die()

		properties.add(Property.UNDEAD);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 52 + adj(0));
	}

	@Override
	public int attackSkill(Char target) {
		return 36 + adj(1);
	}

	@Override
	public int dr() {
		return 16 + adj(1);
	}

	@Override
	protected boolean canAttack(Char enemy) {
		Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT);
		return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(2) == 0) {
			Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
		}

		return damage;
	}

	@Override
	protected boolean getCloser(int target) {
		if (state == HUNTING) {
			return enemySeen && getFurther(target);
		} else {
			return super.getCloser(target);
		}
	}

	@Override
	protected Item createLoot() {
		// 5/count+5 total chance of getting healing, failing the 2nd roll drops
		// mystery meat instead.
		if (Random.Int(5) <= 4) {
			return (Item) loot;
		} else {
			return new MysteryMeat();
		}
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(Leech.class);
		RESISTANCES.add(Poison.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
