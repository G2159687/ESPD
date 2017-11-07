
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.SokobanSheepSwitchSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class SheepSokobanSwitch extends NPC {

	private static final String[] QUOTES = {Messages.get(SheepSokoban.class, "one"), Messages.get(SheepSokoban.class, "two"), Messages.get(SheepSokoban.class, "three"), Messages.get(SheepSokoban.class, "four")};

	{
		name = Messages.get(SheepSokobanSwitch.class, "name");
		spriteClass = SokobanSheepSwitchSprite.class;

		properties.add(Property.IMMOVABLE);
	}


	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public String description() {
		return Messages.get(SheepSokobanSwitch.class, "desc");
	}

	@Override
	public boolean interact() {
		int curPos = pos;

		GLog.n(Messages.get(SheepSokoban.class, "sheepname") + Random.element(QUOTES));
		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();

		return true;

	}

}
