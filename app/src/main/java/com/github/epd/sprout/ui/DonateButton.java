
package com.github.epd.sprout.ui;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.windows.WndEasySettings;
import com.github.epd.sprout.windows.WndMessage;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class DonateButton extends Button {

	private Image image;

	public DonateButton() {
		super();

		width = image.width;
		height = image.height;
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		image = Icons.get(Icons.DONATE);
		add(image);
	}

	@Override
	protected void layout() {
		super.layout();

		image.x = x;
		image.y = y;
	}

	@Override
	protected void onTouchDown() {
		image.brightness(1.5f);
		Sample.INSTANCE.play(Assets.SND_CLICK);
	}

	@Override
	protected void onTouchUp() {
		image.resetColor();
	}

	@Override
	protected void onClick() {
		parent.add(new WndMessage("_捐赠_\n\n" + "本游戏的翻译处理过程耗时两个多月，我在将相关代码移植到此版本的时候耗费了大量的精力，同时还花了很多时间来提升游戏体验。\n\n" +
				"因此，如果您认为我的工作有用，并且有多余的零花钱的话，可以考虑适当地对我进行捐赠。捐赠的数额不限，重要的是您的一片心意。\n\n" +
				"支付宝账户：_3529858533@qq.com_\n\n" +
				"请注意，捐赠只是用来肯定我在过去为这个游戏做出的努力，如果不出意外，本游戏并不会更新。感谢您的理解！"));
	}

}
