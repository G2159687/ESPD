
package com.github.epd.sprout.ui;

import android.opengl.GLES20;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.Game;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.ui.Component;

public class Archs extends Component {

	private static final float SCROLL_SPEED = 20f;

	private SkinnedBlock arcsBg;
	private SkinnedBlock arcsFg;

	private static float offsB = 0;
	private static float offsF = 0;

	public boolean reversed = false;

	@Override
	protected void createChildren() {
		arcsBg = new SkinnedBlock(1, 1, Assets.ARCS_BG) {
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				//arch bg has no alpha component, this improves performance
				GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ZERO);
				super.draw();
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			}
		};
		arcsBg.autoAdjust = true;
		arcsBg.offsetTo(0, offsB);
		add(arcsBg);

		arcsFg = new SkinnedBlock(1, 1, Assets.ARCS_FG) {
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}
		};
		arcsFg.autoAdjust = true;
		arcsFg.offsetTo(0, offsF);
		add(arcsFg);
	}

	@Override
	protected void layout() {
		arcsBg.size(width, height);
		arcsBg.offset(arcsBg.texture.width / 4 - (width % arcsBg.texture.width)
				/ 2, 0);

		arcsFg.size(width, height);
		arcsFg.offset(arcsFg.texture.width / 4 - (width % arcsFg.texture.width)
				/ 2, 0);
	}

	@Override
	public void update() {

		super.update();

		float shift = Game.elapsed * SCROLL_SPEED;
		if (reversed) {
			shift = -shift;
		}

		arcsBg.offset(0, shift);
		arcsFg.offset(0, shift * 2);

		offsB = arcsBg.offsetY();
		offsF = arcsFg.offsetY();
	}
}
