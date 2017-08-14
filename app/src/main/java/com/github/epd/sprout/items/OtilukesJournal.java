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
package com.github.epd.sprout.items;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.items.journalpages.JournalPage;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.items.misc.Spectacles.MagicSight;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.github.epd.sprout.windows.WndOtiluke;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class OtilukesJournal extends Item {

	private static final String TXT_PREVENTING = Messages.get(OtilukesJournal.class,"prevent");

	
	public final float TIME_TO_USE = 1;
	public final int fullCharge = 1000;
	
	
	public static final String AC_RETURN = Messages.get(OtilukesJournal.class,"ac_return");
	public static final String AC_ADD = Messages.get(OtilukesJournal.class,"ac_add");
	public static final String AC_PORT = Messages.get(OtilukesJournal.class,"ac_read");
	
	protected String inventoryTitle = Messages.get(OtilukesJournal.class,"title");
	protected WndBag.Mode mode = WndBag.Mode.JOURNALPAGES;

	
	public int returnDepth = -1;
	public int returnPos;
	
	public int charge = 0;
	public int level = 1;
	
	public int checkReading(){
		int lvl=1;			
		if (Dungeon.hero.buff(MagicSight.class) != null){
			lvl+=1;
		}
		return lvl;
	}
	
	public int reqCharges(){
			
		int calcCharges = Math.round(fullCharge/(level*checkReading()));
		return calcCharges;
		
	}
	

	public boolean[] rooms = new boolean[10];	
	public boolean[] firsts = new boolean[10];	
		
	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.OTILUKES_JOURNAL;

		unique = true;
		
		//rooms[0] = true;
		//firsts[0] = true;
	}
		
	private static final String DEPTH = "depth";
	private static final String POS = "pos";
	private static final String ROOMS = "rooms";
	private static final String FIRSTS = "firsts";
	private static final String CHARGE = "charge";
	private static final String LEVEL = "level";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEPTH, returnDepth);
		bundle.put(ROOMS, rooms);
		bundle.put(CHARGE, charge);
		bundle.put(FIRSTS, firsts);
		bundle.put(LEVEL, level);
		if (returnDepth != -1) {
			bundle.put(POS, returnPos);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		returnDepth = bundle.getInt(DEPTH);
		returnPos = bundle.getInt(POS);
		charge = bundle.getInt(CHARGE);
		level = bundle.getInt(LEVEL);
		rooms = bundle.getBooleanArray(ROOMS);
		firsts = bundle.getBooleanArray(FIRSTS);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		
		actions.add(AC_ADD);
		
		if (returnDepth > 0 && (Dungeon.depth<56 || Dungeon.depth==68 || Dungeon.depth==69) && Dungeon.depth>49 && !hero.petfollow){
		actions.add(AC_RETURN);
		}
		//charge >= reqCharges() &&		
		if (charge >= reqCharges() && Dungeon.depth<26 && !hero.petfollow && (level>1 || rooms[0])){
		actions.add(AC_PORT);
		}
				
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action == AC_PORT) {

			if (Dungeon.bossLevel()) {
				hero.spend(TIME_TO_USE);
				GLog.w(TXT_PREVENTING);
				return;
			}
						
			
		}

		if (action == AC_PORT) {
				
			GameScene.show(new WndOtiluke(rooms, this));
			
		}
              
       if (action == AC_RETURN) {
    	   
    	   hero.spend(TIME_TO_USE);
    	   
    	       IronKey key = hero.belongings.getKey(IronKey.class, Dungeon.depth);
			   if (key!=null){key.detachAll(Dungeon.hero.belongings.backpack);}
			   updateQuickslot();
			   
			   checkPetPort();
			   
				InterlevelScene.mode = InterlevelScene.Mode.RETURN;	
				InterlevelScene.returnDepth = returnDepth;
				InterlevelScene.returnPos = returnPos;
				Game.switchScene(InterlevelScene.class);
				returnDepth=-1;
			}
               
       if (action == AC_ADD) {

    	   GameScene.selectItem(itemSelector, mode, inventoryTitle);
			
		}		
					
		 else {

			super.execute(hero, action);

		}
	}

	@Override
	public int price() {
		return 300*quantity;
	}
	
	public void reset() {
		returnDepth = -1;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private PET checkpet(){
		for (Mob mob : Dungeon.level.mobs) {
			if(mob instanceof PET) {
				return (PET) mob;
			}
		}	
		return null;
	}
	
	private boolean checkpetNear(){
		for (int n : PathFinder.NEIGHBOURS8) {
			int c =  Dungeon.hero.pos + n;
			if (Actor.findChar(c) instanceof PET) {
				return true;
			}
		}
		return false;
	}
	
	private void checkPetPort(){
		PET pet = checkpet();
		if(pet!=null){
		  Dungeon.hero.petType=pet.type;
		  Dungeon.hero.petLevel=pet.level;
		  Dungeon.hero.petKills=pet.kills;	
		  Dungeon.hero.petHP=pet.HP;
		  Dungeon.hero.petExperience=pet.experience;
		  Dungeon.hero.petCooldown=pet.cooldown;
		  pet.destroy();
		  Dungeon.hero.petfollow=true;
		} else Dungeon.hero.petfollow = Dungeon.hero.haspet && Dungeon.hero.petfollow;
		
	}

		
	@Override
	public String info() {
		
		String strdesc = Messages.get(OtilukesJournal.class,"desc1");
		
		if(level>1){
		  if (charge<reqCharges()){
			
			strdesc = strdesc +  Messages.get(OtilukesJournal.class,"desc2", ((100*charge)/reqCharges()));
		  } else {			
			strdesc = strdesc +  Messages.get(OtilukesJournal.class,"desc3");
		  }
		}
		
		return strdesc;
	}
	
	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null && item instanceof JournalPage) {
				Hero hero = Dungeon.hero;
				int room = ((JournalPage) item).room;
			
				hero.sprite.operate(hero.pos);
				hero.busy();
				hero.spend(2f);
				Sample.INSTANCE.play(Assets.SND_BURNING);
				hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);

				item.detach(hero.belongings.backpack);
				GLog.h(Messages.get(OtilukesJournal.class,"add"));
				level++;
				
				if(charge<(fullCharge-500)){
					charge=fullCharge;
				}  else {
					charge+=500; 
				}
				
				rooms[room] = true;
				firsts[room] = true;
				
		}
	 }
	};
	

}
