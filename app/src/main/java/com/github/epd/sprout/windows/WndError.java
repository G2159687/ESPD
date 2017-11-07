
package com.github.epd.sprout.windows;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.Icons;

public class WndError extends WndTitledMessage {

	private static final String TXT_TITLE = Messages.get(WndError.class, "title");

	public WndError(String message) {
		super(Icons.WARNING.get(), TXT_TITLE, message);
	}

}
