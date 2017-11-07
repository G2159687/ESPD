
package com.github.epd.sprout.items;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Badges;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Fury;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroSubClass;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.SpellSprite;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndChooseWay;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class TomeOfMastery extends Item {

	private static final String TXT_BLINDED = Messages.get(TomeOfMastery.class, "blinded");

	public static final float TIME_TO_READ = 10;

	public static final String AC_READ = Messages.get(TomeOfMastery.class, "ac_read");

	{
		stackable = false;
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.MASTERY;

		unique = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_READ);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_READ)) {

			if (hero.buff(Blindness.class) != null) {
				GLog.w(TXT_BLINDED);
				return;
			}

			curUser = hero;

			HeroSubClass way1 = null;
			HeroSubClass way2 = null;
			switch (hero.heroClass) {
				case WARRIOR:
					way1 = HeroSubClass.GLADIATOR;
					way2 = HeroSubClass.BERSERKER;
					break;
				case MAGE:
					way1 = HeroSubClass.BATTLEMAGE;
					way2 = HeroSubClass.WARLOCK;
					break;
				case ROGUE:
					way1 = HeroSubClass.FREERUNNER;
					way2 = HeroSubClass.ASSASSIN;
					break;
				case HUNTRESS:
					way1 = HeroSubClass.SNIPER;
					way2 = HeroSubClass.WARDEN;
					break;
			}
			GameScene.show(new WndChooseWay(this, way1, way2));

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public boolean doPickUp(Hero hero) {
		Badges.validateMastery();
		return super.doPickUp(hero);
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}

	public void choose(HeroSubClass way) {

		detach(curUser.belongings.backpack);

		curUser.spend(TomeOfMastery.TIME_TO_READ);
		curUser.busy();

		curUser.subClass = way;

		curUser.sprite.operate(curUser.pos);
		Sample.INSTANCE.play(Assets.SND_MASTERY);

		SpellSprite.show(curUser, SpellSprite.MASTERY);
		curUser.sprite.emitter().burst(Speck.factory(Speck.MASTERY), 12);
		GLog.w(Messages.get(this, "way"),
				Utils.capitalize(way.title()));

		if (way == HeroSubClass.BERSERKER
				&& curUser.HP <= curUser.HT * Fury.LEVEL) {
			Buff.affect(curUser, Fury.class);
		}
	}
}
