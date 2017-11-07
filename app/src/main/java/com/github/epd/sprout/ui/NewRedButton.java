
package com.github.epd.sprout.ui;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Chrome;
import com.github.epd.sprout.scenes.PixelScene;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class NewRedButton extends Button {

	protected NinePatch bg;
	protected RenderedText text;
	protected Image icon;

	public NewRedButton(String label) {
		this(label, 9);
	}

	public NewRedButton(String label, int size) {
		super();

		text = PixelScene.renderText(size);
		text.text(label);
		add(text);
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		bg = Chrome.get(Chrome.Type.BUTTON);
		add(bg);
	}

	@Override
	protected void layout() {

		super.layout();

		bg.x = x;
		bg.y = y;
		bg.size(width, height);

		text.x = x + (width - text.width()) / 2;
		text.y = y + (height - text.baseLine()) / 2;
		PixelScene.align(text);

		if (icon != null) {
			icon.x = x + text.x - icon.width() - 2;
			icon.y = y + (height - icon.height()) / 2;
			PixelScene.align(icon);
		}
	}

	@Override
	protected void onTouchDown() {
		bg.brightness(1.2f);
		Sample.INSTANCE.play(Assets.SND_CLICK);
	}

	@Override
	protected void onTouchUp() {
		bg.resetColor();
	}

	public void enable(boolean value) {
		active = value;
		text.alpha(value ? 1.0f : 0.3f);
	}

	public void text(String value) {
		text.text(value);
		layout();
	}

	public void textColor(int value) {
		text.hardlight(value);
	}

	public void icon(Image icon) {
		if (this.icon != null) {
			remove(this.icon);
		}
		this.icon = icon;
		if (this.icon != null) {
			add(this.icon);
			layout();
		}
	}

	public float reqWidth() {
		return text.width() + 2f;
	}

	public float reqHeight() {
		return text.baseLine() + 4;
	}
}
