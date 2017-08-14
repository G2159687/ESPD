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
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Gullin;
import com.github.epd.sprout.actors.mobs.Kupua;
import com.github.epd.sprout.actors.mobs.MineSentinel;
import com.github.epd.sprout.actors.mobs.Otiluke;
import com.github.epd.sprout.actors.mobs.Zot;
import com.github.epd.sprout.actors.mobs.ZotPhase;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Chainsaw extends MeleeWeapon {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.CHAINSAW;
		reinforced = true;
	}
	
	public Boolean turnedOn = false;
	public static final String AC_ON = Messages.get(Chainsaw.class,"ac_on");
	public static final String AC_OFF = Messages.get(Chainsaw.class,"ac_off");
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if(turnedOn){actions.add(AC_OFF);}else{actions.add(AC_ON);}
		return actions;
	}

	@Override
	public void execute(final Hero hero, String action) {
		if (action.equals(AC_ON)) {
			turnedOn=true;
			GLog.i(Messages.get(this,"on"));
			hero.next();

		} else if (action.equals(AC_OFF)) {
			turnedOn=false;		
			GLog.i(Messages.get(this,"off"));
			hero.next();
		} else {
			super.execute(hero, action);
		}
	}
	
	@Override
	public void proc(Char attacker, Char defender, int damage) {
		
		if (defender instanceof Gullin 
        		|| defender instanceof Kupua
        		|| defender instanceof MineSentinel
        		|| defender instanceof Otiluke
        		|| defender instanceof Zot
        		|| defender instanceof ZotPhase){
        	
        	//damage*=2;
			
			defender.damage(Random.Int(damage,damage*4), this);
		}
        
		
		if (enchantment != null) {
			enchantment.proc(this, attacker, defender, damage);		
		}
	}

	@Override
	public Item upgrade() {
		return upgrade(false);
	}

	@Override
	public Item upgrade(boolean enchant) {
		
		return super.upgrade(false);		
	}

	public Item safeUpgrade() {
		return upgrade(enchantment != null);
	}
	
	public Chainsaw() {
		super(1, 1.2f, .75f);
	}

	@Override
	public Item uncurse(){
		cursed=false;
		return this;
	}
	
	@Override
	public String desc() {
		return Messages.get(this,"desc");
	}
	
	private static final String TURNEDON = "turnedOn";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TURNEDON, turnedOn);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		turnedOn = bundle.getBoolean(TURNEDON);
	}

	@Override
	public String info() {

		String name = name();
		final String p = "\n\n";

		StringBuilder info = new StringBuilder(desc());

		String quality = levelKnown && level != 0 ? (level > 0 ? Messages.get(MeleeWeapon.class,"upgraded")
				: Messages.get(this,"degraded")) : "";
		info.append(p);
		info.append(Messages.get(MeleeWeapon.class,"this", name, quality, 1));

		if (levelKnown) {
			info.append(Messages.get(RelicMeleeWeapon.class,"avgdmg",
					Math.round((MIN) * (imbue == Imbue.LIGHT ? 0.75f : (imbue == Imbue.HEAVY ? 1.5f : 1))), Math.round((MAX) * (imbue == Imbue.LIGHT ? 0.75f : (imbue == Imbue.HEAVY ? 1.5f : 1)))));
		}

		if (DLY != 1f) {
			info.append(Messages.get(RelicMeleeWeapon.class,"rather")+ (DLY < 1f ? Messages.get(RelicMeleeWeapon.class,"fast") : Messages.get(RelicMeleeWeapon.class,"slow")));
			if (ACU != 1f) {
				if ((ACU > 1f) == (DLY < 1f)) {
					info.append(Messages.get(RelicMeleeWeapon.class,"and"));
				} else {
					info.append(Messages.get(RelicMeleeWeapon.class,"but"));
				}
				info.append(ACU > 1f ? Messages.get(RelicMeleeWeapon.class,"ac"): Messages.get(RelicMeleeWeapon.class,"inac"));
			}
			info.append(Messages.get(RelicMeleeWeapon.class,"weapon"));

		} else if (ACU != 1f) {
			info.append(Messages.get(RelicMeleeWeapon.class,"rather")
					+ (ACU > 1f ? Messages.get(RelicMeleeWeapon.class,"ac") : Messages.get(RelicMeleeWeapon.class,"inac")) + Messages.get(RelicMeleeWeapon.class,"weapon"));
		}

		switch (imbue) {
			case LIGHT:
				info.append(Messages.get(RelicMeleeWeapon.class,"lighter"));
				break;
			case HEAVY:
				info.append(Messages.get(RelicMeleeWeapon.class,"heavier"));
				break;
			case NONE:
		}

		if (reinforced) {
			info.append(Messages.get(RelicMeleeWeapon.class,"reinforced"));
		}

		if (isEquipped(Dungeon.hero)) {
			info.append(p);
			info.append(Messages.get(RelicMeleeWeapon.class,"atready", name)
					+ (cursed ? Messages.get(RelicMeleeWeapon.class,"equipcursed") : ""));
		} else {
			if (cursedKnown && cursed) {
				info.append(p);
				info.append(Messages.get(RelicMeleeWeapon.class,"knowncursed", name));
			}
		}

		return info.toString();
	}
	
}
