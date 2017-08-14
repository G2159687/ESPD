package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Earthroot;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ChaliceOfBlood extends Artifact {

	private static final String TXT_CHALICE = Messages.get(ChaliceOfBlood.class,"name");
	private static final String TXT_YES = Messages.get(ChaliceOfBlood.class,"yes");
	private static final String TXT_NO = Messages.get(ChaliceOfBlood.class,"no");
	private static final String TXT_PRICK = Messages.get(ChaliceOfBlood.class,"prick_warn");

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.ARTIFACT_CHALICE1;

		level = 0;
		levelCap = 10;
		reinforced = true;
	}

	public static final String AC_PRICK = Messages.get(ChaliceOfBlood.class,"ac_prick");

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && level < levelCap && !cursed)
			actions.add(AC_PRICK);
		return actions;
	}

	@Override
	public boolean isUpgradable() {
		return true;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_PRICK)) {

			int damage = 3 * (level * level);

			if (damage > hero.HP * 0.75) {

				GameScene.show(new WndOptions(TXT_CHALICE, TXT_PRICK, TXT_YES,
						TXT_NO) {
					@Override
					protected void onSelect(int index) {
						if (index == 0)
							prick(Dungeon.hero);
					}
				});

			} else {
				prick(hero);
			}
		}
	}

	private void prick(Hero hero) {
		int damage = 3 * (level * level);

		Earthroot.Armor armor = hero.buff(Earthroot.Armor.class);
		if (armor != null) {
			damage = armor.absorb(damage);
		}

		damage -= Random.IntRange(0, hero.dr());

		hero.sprite.operate(hero.pos);
		hero.busy();
		hero.spend(3f);
		if (damage <= 0) {
			GLog.i(Messages.get(this,"onprick2"));
		} else if (damage < 25) {
			GLog.w(Messages.get(this,"onprick3"));
			Sample.INSTANCE.play(Assets.SND_CURSED);
			hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
		} else if (damage < 100) {
			GLog.w(Messages.get(this,"onprick"));
			Sample.INSTANCE.play(Assets.SND_CURSED);
			hero.sprite.emitter().burst(ShadowParticle.CURSE, 12);
		} else {
			GLog.w(Messages.get(this,"onprick4"));
			Sample.INSTANCE.play(Assets.SND_CURSED);
			hero.sprite.emitter().burst(ShadowParticle.CURSE, 18);
		}

		if (damage > 0)
			hero.damage(damage, this);

		if (!hero.isAlive()) {
			Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
			GLog.n(Messages.get(this,"ondeath"));
		} else {
			upgrade();
		}
	}

	@Override
	public Item upgrade() {
		if (level >= 6)
			image = ItemSpriteSheet.ARTIFACT_CHALICE3;
		else if (level >= 2)
			image = ItemSpriteSheet.ARTIFACT_CHALICE2;
		return super.upgrade();
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new chaliceRegen();
	}

	@Override
	public String desc() {
		String desc = Messages.get(this,"desc");
		if (level < levelCap)
			desc += Messages.get(this,"desc_5");
		else
			desc += Messages.get(this,"desc_4");

		if (isEquipped(Dungeon.hero)) {
			desc += "\n\n";
			if (cursed)
				desc += Messages.get(this,"desc_cursed");
			else if (level == 0)
				desc += Messages.get(this,"desc_1");
			else if (level < levelCap)
				desc += Messages.get(this,"desc_2");
			else
				desc += Messages.get(this,"desc_3");
		}

		return desc;
	}

	public class chaliceRegen extends ArtifactBuff {

	}

}
