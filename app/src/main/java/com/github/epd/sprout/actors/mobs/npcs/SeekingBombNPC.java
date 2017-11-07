
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.SeekingBombSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class SeekingBombNPC extends NPC {

	{
		name = Messages.get(SeekingBombNPC.class, "name");
		spriteClass = SeekingBombSprite.class;

		state = HUNTING;

	}

	private static final float SPAWN_DELAY = 0.1f;

	@Override
	public int attackSkill(Char target) {
		return 99;
	}


	@Override
	public int attackProc(Char enemy, int damage) {
		int dmg = super.attackProc(enemy, damage);

		Bomb bomb = new Bomb();
		bomb.explode(pos);
		yell(Messages.get(SeekingBombNPC.class, "yell"));


		destroy();
		sprite.die();

		return dmg;
	}

	@Override
	protected Char chooseEnemy() {

		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.hostile && Level.fieldOfView[mob.pos]) {
					enemies.add(mob);
				}
			}

			enemy = enemies.size() > 0 ? Random.element(enemies) : null;
		}

		return enemy;
	}

	@Override
	public String description() {
		return Messages.get(SeekingBombNPC.class, "desc");
	}


	@Override
	public boolean interact() {

		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();

		return true;
	}

	public static SeekingBombNPC spawnAt(int pos) {

		SeekingBombNPC b = new SeekingBombNPC();

		b.pos = pos;
		b.state = b.HUNTING;
		GameScene.add(b, SPAWN_DELAY);

		return b;

	}

	@Override
	public void die(Object cause) {
		Bomb bomb = new Bomb();
		bomb.explode(pos);

		super.die(cause);
	}

}