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
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ShopkeeperSprite;
import com.github.epd.sprout.windows.WndBag;
import com.github.epd.sprout.windows.WndTradeItem;

public class Shopkeeper extends NPC {

    public static final String TXT_THIEF = Messages.get(Shopkeeper.class, "thief");

    {
        name = Messages.get(Shopkeeper.class, "name");
        spriteClass = ShopkeeperSprite.class;
    }

    @Override
    protected boolean act() {

        throwItem();

        sprite.turnTo(pos, Dungeon.hero.pos);
        spend(TICK);
        return true;
    }

    @Override
    public void damage(int dmg, Object src) {
        //flee();
    }

    @Override
    public void add(Buff buff) {
        //flee();
    }

    public void flee() {
        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type == Heap.Type.FOR_SALE) {
                CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
                heap.destroy();
            }
        }

        destroy();

        sprite.killAndErase();
        CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public String description() {
        return Messages.get(Shopkeeper.class, "desc");
    }

    public static WndBag sell() {
        return GameScene.selectItem(itemSelector, WndBag.Mode.FOR_SALE,
                Messages.get(Shopkeeper.class, "sell"));
    }

    private static WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null) {
                WndBag parentWnd = sell();
                GameScene.show(new WndTradeItem(item, parentWnd));
            }
        }
    };

    @Override
    public boolean interact() {
        sell();
        return false;
    }
}
