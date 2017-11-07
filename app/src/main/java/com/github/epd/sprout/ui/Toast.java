
package com.github.epd.sprout.ui;

import com.github.epd.sprout.Chrome;
import com.github.epd.sprout.scenes.PixelScene;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

public class Toast extends Component {

	private static final float MARGIN_HOR = 2;
	private static final float MARGIN_VER = 2;

	protected NinePatch bg;
	protected SimpleButton close;
	protected RenderedTextMultiline text;

	public Toast(String text) {
		super();
		text(text);

		width = this.text.width() + close.width() + bg.marginHor() + MARGIN_HOR
				* 3;
		height = Math.max(this.text.height(), close.height()) + bg.marginVer()
				+ MARGIN_VER * 2;
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		bg = Chrome.get(Chrome.Type.TOAST_TR);
		add(bg);

		close = new SimpleButton(Icons.get(Icons.CLOSE)) {
			@Override
			protected void onClick() {
				onClose();
			}
		};
		add(close);

		text = PixelScene.renderMultiline(8);
		add(text);
	}

	@Override
	protected void layout() {
		super.layout();

		bg.x = x;
		bg.y = y;
		bg.size(width, height);

		close.setPos(
				bg.x + bg.width() - bg.marginHor() / 2f - MARGIN_HOR - close.width(),
				y + (height - close.height()) / 2f);
		PixelScene.align(close);

		text.setPos(close.left() - MARGIN_HOR - text.width(), y + (height - text.height()) / 2);
		PixelScene.align(text);
	}

	@Override
	public synchronized void kill() {
		super.kill();
		text.destroy();
	}

	public void text(String txt) {
		text.text(txt);
	}

	protected void onClose() {
	}
}
