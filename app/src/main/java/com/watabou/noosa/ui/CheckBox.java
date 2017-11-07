
package com.watabou.noosa.ui;

public class CheckBox extends Button {

	protected boolean checked;

	public boolean checked() {
		return checked;
	}

	public void checked(boolean value) {
		if (checked != value) {
			checked = value;
			updateState();
		}
	}

	protected void updateState() {

	}

	@Override
	protected void onClick() {
		checked(!checked);
		onChange();
	}

	protected void onChange() {
	}
}
