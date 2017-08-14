package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class ToxicImbue extends Buff {

	public static final float DURATION = 30f;

	protected float left;

	private static final String LEFT = "left";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEFT, left);

	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat(LEFT);
	}

	public void set(float duration) {
		this.left = duration;
	}

	@Override
	public boolean act() {
		GameScene.add(Blob.seed(target.pos, 50, ToxicGas.class));

		spend(TICK);
		left -= TICK;
		if (left <= 0)
			detach();

		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.IMMUNITY;
	}

	@Override
	public String toString() {
		return Messages.get(this,"name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left));
	}

	{
		immunities.add(ToxicGas.class);
		immunities.add(Poison.class);
	}
}
