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

import com.github.epd.sprout.Badges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.Amulet;
import com.github.epd.sprout.items.TownReturnBeacon;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.OtilukeNPCSprite;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class OtilukeNPC extends NPC {

    {
        name = Messages.get(this, "name");
        spriteClass = OtilukeNPCSprite.class;
    }

    protected static final float SPAWN_DELAY = 2f;

    private static final String TXT_DUNGEON = Messages.get(OtilukeNPC.class, "one");

    private static final String TXT_DUNGEON2 = Messages.get(OtilukeNPC.class, "two");

    private static final String TXT_DUNGEON4 = Messages.get(OtilukeNPC.class, "four");


    @Override
    protected boolean act() {
        throwItem();
        return super.act();
    }

    private boolean first = true;

    private static final String FIRST = "first";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    @Override
    public int defenseSkill(Char enemy) {
        return 1000;
    }

    @Override
    public String defenseVerb() {
        return Messages.get(OtilukeNPC.class, "def");
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

    public static OtilukeNPC spawnAt(int pos) {
        if (Level.passable[pos] && Actor.findChar(pos) == null) {

            OtilukeNPC w = new OtilukeNPC();
            w.pos = pos;
            GameScene.add(w, SPAWN_DELAY);

            return w;

        } else {
            return null;
        }
    }


    @Override
    public boolean interact() {

        sprite.turnTo(pos, Dungeon.hero.pos);

        TownReturnBeacon beacon = Dungeon.hero.belongings.getItem(TownReturnBeacon.class);

        if (Badges.checkOtilukeRescued()) {
                tell(TXT_DUNGEON4);
                Dungeon.level.drop(new Amulet(), Dungeon.hero.pos).sprite.drop();
        } else if (first && beacon == null) {
            Badges.validateOtilukeRescued();
            first = false;
            tell(TXT_DUNGEON2);
            Dungeon.level.drop(new TownReturnBeacon(), Dungeon.hero.pos).sprite.drop();
            Dungeon.level.drop(new Amulet(), Dungeon.hero.pos).sprite.drop();
        } else {
            Badges.validateOtilukeRescued();
            tell(TXT_DUNGEON);
            Dungeon.level.drop(new Amulet(), Dungeon.hero.pos).sprite.drop();
        }
        return false;
    }

    private void tell(String format, Object... args) {
        GameScene.show(new WndQuest(this, Utils.format(format, args)));
    }

    @Override
    public String description() {
        return Messages.get(OtilukeNPC.class, "desc");
    }

}
