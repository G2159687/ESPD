package com.github.epd.sprout.items.help.mechanics;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;


public class BugSolve extends Item{

    {
        name = Messages.get(this,"name");
        image = ItemSpriteSheet.REMAINS;
    }

    @Override
    public String info() {
        return Messages.get(this,"desc");
    }
}
