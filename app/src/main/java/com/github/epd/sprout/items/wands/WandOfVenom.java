package com.github.epd.sprout.items.wands;

import android.view.View;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.VenomGas;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfVenom extends Wand {

    {
        name = Messages.get(this,"name");
        //TODO: final sprite
        image = ItemSpriteSheet.WAND_ADAMANT;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;
    }

    @Override
    protected void onZap(Ballistica bolt) {
        GameScene.add(Blob.seed(bolt.collisionPos, 40+20*level, VenomGas.class));
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.poison(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    @Override
    public String desc() {
        return Messages.get(this,"desc");
    }
}
