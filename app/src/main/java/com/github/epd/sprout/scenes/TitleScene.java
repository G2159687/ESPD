/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.scenes;

import android.opengl.GLES20;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Badges;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.effects.BannerSprites;
import com.github.epd.sprout.effects.Fireball;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.Archs;
import com.github.epd.sprout.ui.DonateButton;
import com.github.epd.sprout.ui.ExitButton;
import com.github.epd.sprout.ui.LanguageButton;
import com.github.epd.sprout.ui.PrefsButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

import javax.microedition.khronos.opengles.GL10;

public class TitleScene extends PixelScene {

	@Override
	public void create() {

		super.create();
		Badges.loadGlobal();

		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );

		float topRegion = Math.max(95f, h*0.45f);

		title.x = (w - title.width()) / 2f;
		if (ShatteredPixelDungeon.landscape())
			title.y = (topRegion - title.height()) / 2f;
		else
			title.y = 16 + (topRegion - title.height() - 16) / 2f;

		align(title);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = (float)Math.sin( -(time += Game.elapsed) );
			}
			@Override
			public void draw() {
				GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
				super.draw();
				GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;

		if (Badges.isUnlocked(Badges.Badge.SUPPORTER) && Badges.isUnlocked(Badges.Badge.SUPPORTER2)) {
			add(title);
			add(signs);
			placeTorch(title.x + 22, title.y + 46);
			placeTorch(title.x + title.width - 22, title.y + 46);
		}

		RenderedTextMultiline hint = renderMultiline("请在查看完关于->游戏须知\n以及设定->帮助之后\n再开始游戏！\n\n感谢你的理解和支持！", 9);
		hint.hardlight(0xFFFF00);
		if (!(Badges.isUnlocked(Badges.Badge.SUPPORTER) && Badges.isUnlocked(Badges.Badge.SUPPORTER2))) add(hint);


		hint.setPos((topRegion - title.height()) / 2f, 16 + (topRegion - title.height() - 16) / 2f);
		align(hint);

		DashboardItem btnBadges = new DashboardItem( Messages.get(this, "badges"), 3 ) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( BadgesScene.class );
			}
		};
		add(btnBadges);

		DashboardItem btnAbout = new DashboardItem( Messages.get(this, "about"), 1 ) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( AboutScene.class );
			}
		};
		add( btnAbout );

		DashboardItem btnPlay = new DashboardItem( Messages.get(this, "play"), 0 ) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( StartScene.class );
			}
		};
		if (Badges.isUnlocked(Badges.Badge.SUPPORTER) && Badges.isUnlocked(Badges.Badge.SUPPORTER2)) add( btnPlay );

		DashboardItem btnRankings = new DashboardItem( Messages.get(this, "rankings"), 2 ) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( RankingsScene.class );
			}
		};
		add( btnRankings );

		if (ShatteredPixelDungeon.landscape()) {
			btnRankings     .setPos( w / 2 - btnRankings.width(), topRegion );
			btnBadges       .setPos( w / 2, topRegion );
			btnPlay         .setPos( btnRankings.left() - btnPlay.width(), topRegion );
			btnAbout        .setPos( btnBadges.right(), topRegion );
		} else {
			btnPlay.setPos( w / 2 - btnPlay.width(), topRegion );
			btnRankings.setPos( w / 2, btnPlay.top() );
			btnBadges.setPos( w / 2 - btnBadges.width(), btnPlay.top() + DashboardItem.SIZE );
			btnAbout.setPos( w / 2, btnBadges.top() );
		}

		BitmapText source = new BitmapText("PD v 1.7.5", font1x);
		source.measure();
		source.hardlight(0x444444);
		source.x = w - source.width();
		source.y = h - source.height();
		add(source);

		TouchArea changes = new TouchArea(source.x, source.y, source.width, source.height) {
			@Override
			protected void onClick (Touchscreen.Touch touch){
				ShatteredPixelDungeon.switchNoFade(ChangesScene.class);
			}
		};
		add (changes);

		BitmapText version = new BitmapText("v " + Game.version + "", font1x);
		version.measure();
		version.hardlight(0xCCCCCC);
		version.x = w - version.width();
		version.y = h - version.height() - source.height();
		add(version);

		TouchArea changes2 = new TouchArea(version.x, version.y, version.width, version.height) {
			@Override
			protected void onClick (Touchscreen.Touch touch){
				ShatteredPixelDungeon.switchNoFade(ChangesScene.class);
			}
		};
		add (changes2);

		PrefsButton btnPrefs = new PrefsButton();
		btnPrefs.setPos(0, 0);
		add(btnPrefs);

		LanguageButton btnLang = new LanguageButton();
		btnLang.setPos(16, 1);
		add( btnLang );

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(w - btnExit.width(), 0);
		add(btnExit);

        DonateButton btnDonate = new DonateButton();
        btnDonate.setPos(w/2 - (btnDonate.width()/2), (5*h)/6);
        add(btnDonate);

		fadeIn();
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private static class DashboardItem extends Button {

		public static final float SIZE	= 48;

		private static final int IMAGE_SIZE	= 32;

		private Image image;
		private RenderedText label;

		DashboardItem( String text, int index ) {
			super();

			image.frame( image.texture.uvRect( index * IMAGE_SIZE, 0, (index + 1) * IMAGE_SIZE, IMAGE_SIZE ) );
			this.label.text( text );

			setSize( SIZE, SIZE );
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			image = new Image( Assets.DASHBOARD );
			add( image );

			label = renderText( 9 );
			add( label );
		}

		@Override
		protected void layout() {
			super.layout();

			image.x = x + (width - image.width()) / 2;
			image.y = y;
			align(image);

			label.x = x + (width - label.width()) / 2;
			label.y = image.y + image.height() +2;
			align(label);
		}

		@Override
		protected void onTouchDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 0.8f );
		}

		@Override
		protected void onTouchUp() {
			image.resetColor();
		}
	}
}
