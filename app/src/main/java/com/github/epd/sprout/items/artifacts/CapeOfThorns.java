package com.github.epd.sprout.items.artifacts;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class CapeOfThorns extends Artifact {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.ARTIFACT_CAPE;

		reinforced = true;

		level = 0;
		levelCap = 10;
        levelCapNew = 200;

		charge = 0;
		chargeCap = 100;
		cooldown = 0;

		defaultAction = "NONE";
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Thorns();
	}

	@Override
    public boolean isUpgradable() {
        return true;
    }

	@Override
	public String desc() {
		String desc = Messages.get(this,"desc");
		if (isEquipped(Dungeon.hero)) {
			desc += "\n\n";
			if (cooldown == 0)
				desc += Messages.get(this,"desc_inactive");
			else
				desc += Messages.get(this,"desc_active");
		}

		return desc;
	}

	public class Thorns extends ArtifactBuff {

		@Override
		public boolean act() {
			if (cooldown > 0) {
				cooldown--;
				if (cooldown == 0) {
					BuffIndicator.refreshHero();
					GLog.w(Messages.get(this,"inert"));
				}
				updateQuickslot();
			}
			spend(TICK);
			return true;
		}

		public int proc(int damage, Char attacker, Char defender){
			if (cooldown == 0) {
				if (attacker != null) charge += damage * (1 + level * 0.07);
				if (charge >= chargeCap) {
					charge = 0;
					cooldown = (int) Math.round(10 + 10 * Math.sqrt(level / 10));
					GLog.p(Messages.get(this,"radiating"));
					BuffIndicator.refreshHero();
				}
			}

			if (cooldown != 0) {
				int deflected = (int)(Random.NormalIntRange(damage / 10, 4 * (damage / 5)) * Math.sqrt(Math.sqrt( (double)((level+1)) / 20 )));
                if (deflected < damage) damage -= deflected;
                else damage -= damage;

				if (attacker != null) attacker.damage( deflected, this);
				
                if (level < 10) {

                    exp += deflected;

                    if (exp >= (level + 1) * 4 && level < levelCap) {
                        exp -= (level + 1) * 4;
                        upgrade();
                        GLog.p(Messages.get(this, "levelup"));
                    }

                }

			}
			updateQuickslot();
			return damage;
		}

		@Override
		public String toString() {
			return Messages.get(this,"name");
		}

		@Override
		public int icon() {
			if (cooldown == 0)
				return BuffIndicator.NONE;
			else
				return BuffIndicator.THORNS;
		}

		@Override
		public void detach() {
			cooldown = 0;
			charge = 0;
			super.detach();
		}

		@Override
		public String desc() {
			return Messages.get(CapeOfThorns.class,"buffdesc", dispTurns(cooldown));
		}

	}

}
