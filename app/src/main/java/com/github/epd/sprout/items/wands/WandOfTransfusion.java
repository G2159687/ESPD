package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Charm;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.Beam;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.BloodParticle;
import com.github.epd.sprout.effects.particles.LeafParticle;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.rings.Ring;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfTransfusion extends Wand {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.WAND_TRANSFUTION;

		collisionProperties = Ballistica.PROJECTILE;
	}

	@Override
	protected void onZap(Ballistica beam) {

		for (int c : beam.subPath(0, beam.dist))
			CellEmitter.center(c).burst( BloodParticle.BURST, 1 );

		int cell = beam.collisionPos;

		Char ch = Actor.findChar(cell);
		Heap heap = Dungeon.level.heaps.get(cell);

		//this wand does a bunch of different things depending on what it targets.

		//if we find a character..
		if (ch != null && ch instanceof Mob){

			if (((Mob) ch).ally){

				int healing = Dungeon.hero.HP / 10;
				ch.HP = Math.min(ch.HP + healing, ch.HT);
				ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1 + level / 2);
				ch.sprite.showStatus(CharSprite.POSITIVE, "%d", healing);
				damageHero(healing);

				//harms the undead
			} else if (ch.properties().contains(Char.Property.UNDEAD)){

				int damage;
				if (level < 41) {
					damage = (int) Math.ceil(ch.HP*(0.1f+(0.01f*level)));
				} else {
					damage = (int) Math.ceil(ch.HP*0.5);
				}
				ch.damage(damage, this);
				ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + level);
				Sample.INSTANCE.play(Assets.SND_BURNING);
				damageHero(Dungeon.hero.HP / 10);

				//charms an enemy
			} else {

				float duration = 5+level/5;
				Buff.affect(ch, Charm.class, Charm.durationFactor(ch) * duration).object = curUser.id();

				ch.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );

			}

			//if we find an item...
		} else if (heap != null && heap.type == Heap.Type.HEAP){
			Item item = heap.peek();

			//30% + 10%*lvl chance to uncurse the item and reset it to base level if degraded.
			if (item != null && Random.Float() <= 0.3f+level*0.1f){
				if (item.cursed){
					item.cursed = false;
					CellEmitter.get(cell).start( ShadowParticle.UP, 0.05f, 10 );
					Sample.INSTANCE.play(Assets.SND_BURNING);
				}

				int lvldiffFromBase = item.level - (item instanceof Ring ? 1 : 0);
				if (lvldiffFromBase < 0){
					item.upgrade(-lvldiffFromBase);
					CellEmitter.get(cell).start(Speck.factory(Speck.UP), 0.2f, 3);
					Sample.INSTANCE.play(Assets.SND_EVOKE);
				}
			}

			//if we find some trampled grass...
		} else if (Dungeon.level.map[cell] == Terrain.GRASS) {

			//regrow one grass tile, suuuuuper useful...
			Level.set(cell, Terrain.HIGH_GRASS);
			GameScene.updateMap(cell);
			CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 4);

			//If we find embers...
		} else if (Dungeon.level.map[cell] == Terrain.EMBERS) {

			//30% + 3%*lvl chance to grow a random plant, or just regrow grass.
			if (Random.Float() <= 0.3f+level*0.03f) {
				Dungeon.level.plant((Plant.Seed) Generator.random(Generator.Category.SEED), cell);
				CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 8);
				GameScene.updateMap(cell);
			} else{
				Level.set(cell, Terrain.HIGH_GRASS);
				GameScene.updateMap(cell);
				CellEmitter.get( cell ).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}

		}

	}


	private void damageHero(int healing){
		// 10% of hero hp
		Dungeon.hero.damage(healing, this);

		if (!curUser.isAlive()){
			Dungeon.fail( Utils.format(ResultDescriptions.ITEM, name) );
			GLog.n(Messages.get(this,"ondeath"));
		}
	}

	@Override
	protected void fx(Ballistica beam, Callback callback) {
		curUser.sprite.parent.add(
				new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.tileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		// TODO: add effect
		/*if (Random.Int( level() + 10 ) >= 9){
			//grants a free use of the staff
			freeCharge = true;
			GLog.p( Messages.get(this, "charged") );
			attacker.sprite.emitter().burst(BloodParticle.BURST, 20);
		}*/
	}

	@Override
	public String desc() {
		return Messages.get(this,"desc");
	}
}

