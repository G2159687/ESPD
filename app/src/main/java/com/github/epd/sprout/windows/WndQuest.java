
package com.github.epd.sprout.windows;

import com.github.epd.sprout.actors.mobs.npcs.NPC;
import com.github.epd.sprout.utils.Utils;

public class WndQuest extends WndTitledMessage {

	public WndQuest(NPC questgiver, String text) {
		super(questgiver.sprite(), Utils.capitalize(questgiver.name), text);
	}
}
