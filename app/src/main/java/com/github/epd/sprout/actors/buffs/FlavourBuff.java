
package com.github.epd.sprout.actors.buffs;

import java.text.DecimalFormat;

//buff whose only logic is to wait and detach after a time.
public class FlavourBuff extends Buff {

	@Override
	public boolean act() {
		detach();
		return true;
	}

	//flavour buffs can all just rely on cooldown()
	public String dispTurns() {
		//add one turn as buffs act last, we want them to end at 1 visually, even if they end at 0 internally.
		float visualTurnsLeft = cooldown() + 1f;
		return visualTurnsLeft == 1 ? "1" : new DecimalFormat("#.##").format(visualTurnsLeft);
	}

	public int dispTurnsInt() {
		//add one turn as buffs act last, we want them to end at 1 visually, even if they end at 0 internally.
		float visualTurnsLeft = cooldown() + 1f;
		return (int) visualTurnsLeft;
	}
}
