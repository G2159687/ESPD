
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroSubClass;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

public class WandOfBlink extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_BLINK;
		collisionProperties = Ballistica.PROJECTILE;
	}

	private static final String TXT_PREVENTING = Messages.get(WandOfBlink.class, "prevent");

	@Override
	protected void onZap(Ballistica bolt) {

		if (Dungeon.sokobanLevel(Dungeon.depth)) {
			GLog.w(TXT_PREVENTING);
			Invisibility.dispel();
			return;
		}

		curUser.sprite.visible = true;
		if (Actor.findChar(bolt.collisionPos) == null) {
			appear(Dungeon.hero, bolt.collisionPos);
		} else if (bolt.dist > 1) {
			appear(Dungeon.hero, bolt.path.get(bolt.dist - 1));
		} else appear(Dungeon.hero, Dungeon.hero.pos);

		Dungeon.observe();
		GameScene.updateFog();

	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.whiteLight(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
		if (!Dungeon.sokobanLevel(Dungeon.depth)) {
			curUser.sprite.visible = false;
		}
	}

	public static void appear(Char ch, int pos) {

		ch.sprite.interruptMotion();

		ch.move(pos);
		ch.sprite.place(pos);

		if (ch.invisible == 0) {
			ch.sprite.alpha(0);
			ch.sprite.parent.add(new AlphaTweener(ch.sprite, 1, 0.4f));
		}

		ch.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		Sample.INSTANCE.play(Assets.SND_TELEPORT);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int reachFactor(Hero hero) {
		int reach = super.reachFactor(hero);
		if (hero.subClass == HeroSubClass.BATTLEMAGE) {
			reach++;
		}
		return reach;
	}
}
