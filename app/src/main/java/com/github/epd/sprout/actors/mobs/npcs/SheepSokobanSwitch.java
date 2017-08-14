/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
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
