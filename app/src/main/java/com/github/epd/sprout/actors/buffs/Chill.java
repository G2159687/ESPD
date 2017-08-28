package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Thief;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.FrozenCarpaccio;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

import java.text.DecimalFormat;

public class Chill extends FlavourBuff {

    @Override
    public boolean attachTo(Char target) {
        //can't chill what's frozen!
        if (target.buff(Frost.class) != null) return false;

        if (super.attachTo(target)){
            Burning.detach( target, Burning.class );

            //chance of potion breaking is the same as speed factor.
            if (Random.Float(1f) > speedFactor() && target instanceof Hero) {

                Hero hero = (Hero)target;
                Item item = hero.belongings.randomUnequipped();
                if (item instanceof Potion) {

                    item = item.detach( hero.belongings.backpack );
                    GLog.w(Messages.get(this, "freezes", item.toString()));
                    ((Potion) item).shatter(hero.pos);

                } else if (item instanceof MysteryMeat) {

                    item = item.detach( hero.belongings.backpack );
                    FrozenCarpaccio carpaccio = new FrozenCarpaccio();
                    if (!carpaccio.collect( hero.belongings.backpack )) {
                        Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
                    }
                    GLog.w(Messages.get(this, "freezes", item.toString()));

                }
            } else if (target instanceof Thief && ((Thief)target).item instanceof Potion) {

                ((Potion) ((Thief)target).item).shatter( target.pos );
                ((Thief) target).item = null;

            }
            return true;
        } else {
            return false;
        }
    }

    //reduces speed by 10% for every turn remaining, capping at 50%
    public float speedFactor(){
        return Math.max(0.5f, 1 - cooldown()*0.1f);
    }

    @Override
    public int icon() {
        return BuffIndicator.FROST;
    }

    @Override
    public String toString() {
        return Messages.get(Chill.class, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(), new DecimalFormat("#.##").format((1f-speedFactor())*100f));
    }

}
