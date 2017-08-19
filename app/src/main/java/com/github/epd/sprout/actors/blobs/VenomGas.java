package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Venom;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.messages.Messages;

public class VenomGas extends Blob {

    @Override
    protected void evolve() {
        super.evolve();

        Char ch;
        for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0 && (ch = Actor.findChar(i)) != null) {
                if (!ch.immunities().contains(this.getClass()))
                    Buff.affect(ch, Venom.class).set(2f);
            }
        }
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.VENOM), 0.6f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this,"desc");
    }
}
