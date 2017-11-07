
package com.github.epd.sprout;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.github.epd.sprout.messages.Languages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.scenes.TitleScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

import java.util.Locale;

import javax.microedition.khronos.opengles.GL10;

public class ShatteredPixelDungeon extends Game {

	public ShatteredPixelDungeon() {
		super(TitleScene.class);

		com.watabou.utils.Bundle
				.addAlias(
						com.github.epd.sprout.items.scrolls.ScrollOfMagicalInfusion.class,
						"com.github.epd.sprout.items.scrolls.ScrollOfWeaponUpgrade");

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		updateImmersiveMode();

		DisplayMetrics metrics = new DisplayMetrics();
		instance.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		boolean landscape = metrics.widthPixels > metrics.heightPixels;

		if (Preferences.INSTANCE.getBoolean(Preferences.KEY_LANDSCAPE, false) != landscape) {
			landscape(!landscape);
		}

		Music.INSTANCE.enable(music());
		Sample.INSTANCE.enable(soundFx());

		Sample.INSTANCE.load(Assets.SND_CLICK, Assets.SND_GOLD,

				Assets.SND_STEP, Assets.SND_WATER, Assets.SND_OPEN,
				Assets.SND_UNLOCK, Assets.SND_ITEM, Assets.SND_DEWDROP,
				Assets.SND_HIT, Assets.SND_MISS,

				Assets.SND_DESCEND, Assets.SND_EAT, Assets.SND_READ,
				Assets.SND_LULLABY, Assets.SND_DRINK, Assets.SND_SHATTER,
				Assets.SND_ZAP, Assets.SND_LIGHTNING, Assets.SND_LEVELUP,
				Assets.SND_DEATH, Assets.SND_CHALLENGE, Assets.SND_CURSED,
				Assets.SND_EVOKE, Assets.SND_TRAP, Assets.SND_TOMB,
				Assets.SND_ALERT, Assets.SND_MELD, Assets.SND_BOSS,
				Assets.SND_BLAST, Assets.SND_PLANT, Assets.SND_RAY,
				Assets.SND_BEACON, Assets.SND_TELEPORT, Assets.SND_CHARMS,
				Assets.SND_MASTERY, Assets.SND_PUFF, Assets.SND_ROCKS,
				Assets.SND_BURNING, Assets.SND_FALLING, Assets.SND_GHOST,
				Assets.SND_SECRET, Assets.SND_BONES, Assets.SND_BEE,
				Assets.SND_MIMIC);

		if (classicFont()) {
			RenderedText.setFont("pixelfont.ttf");
		} else {
			RenderedText.setFont(null);
		}
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);

		if (hasFocus) {
			updateImmersiveMode();
		}
	}

	public static void switchNoFade(Class<? extends PixelScene> c) {
		PixelScene.noFade = true;
		switchScene(c);
	}

	/*
	 * ---> Prefernces
	 */

	public static void landscape(boolean value) {
		Game.instance
				.setRequestedOrientation(value ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
						: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Preferences.INSTANCE.put(Preferences.KEY_LANDSCAPE, value);
	}

	public static boolean landscape() {
		return width > height;
	}

	public static void scale(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_SCALE, value);
		switchScene(TitleScene.class);
	}

	// *** IMMERSIVE MODE ****

	private static boolean immersiveModeChanged = false;

	@SuppressLint("NewApi")
	public static void immerse(boolean value) {
		Preferences.INSTANCE.put(Preferences.KEY_IMMERSIVE, value);

		instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updateImmersiveMode();
				immersiveModeChanged = true;
			}
		});
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);

		if (immersiveModeChanged) {
			requestedReset = true;
			immersiveModeChanged = false;
		}
	}

	@SuppressLint("NewApi")
	public static void updateImmersiveMode() {
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			try {
				// Sometime NullPointerException happens here
				instance.getWindow()
						.getDecorView()
						.setSystemUiVisibility(
								immersed() ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE
										| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
										| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
										| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
										| View.SYSTEM_UI_FLAG_FULLSCREEN
										| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
										: 0);
			} catch (Exception e) {
				reportException(e);
			}
		}
	}

	public static boolean immersed() {
		return Preferences.INSTANCE
				.getBoolean(Preferences.KEY_IMMERSIVE, false);
	}

	// *****************************

	public static int scale() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_SCALE, 0);
	}

	public static void zoom(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_ZOOM, value);
	}

	public static int zoom() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_ZOOM, 0);
	}

	public static void music(boolean value) {
		Music.INSTANCE.enable(value);
		Preferences.INSTANCE.put(Preferences.KEY_MUSIC, value);
	}

	public static boolean music() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_MUSIC, false);
	}

	public static void autocollect(boolean value) {
		Preferences.INSTANCE.put(Preferences.KEY_AUTOCOLLECT, value);
	}

	public static boolean autocollect() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_AUTOCOLLECT, false);
	}

	public static void soundFx(boolean value) {
		Sample.INSTANCE.enable(value);
		Preferences.INSTANCE.put(Preferences.KEY_SOUND_FX, value);
	}

	public static boolean soundFx() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_SOUND_FX, false);
	}

	public static void brightness(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_BRIGHTNESS, value);
		GameScene.updateFog();
	}

	public static int brightness() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_BRIGHTNESS, 2, -2, 2);
	}

	public static void dewmode(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_DEWMODE, value);
	}

	public static int dewmode() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_DEWMODE, 0);
	}

	public static void language(Languages lang) {
		Preferences.INSTANCE.put(Preferences.KEY_LANG, lang.code());
	}

	public static Languages language() {
		String code = Preferences.INSTANCE.getString(Preferences.KEY_LANG, null);
		if (code == null){
			return Languages.matchLocale(Locale.getDefault());
		} else {
			return Languages.matchCode(code);
		}
	}

	public static void classicFont(boolean classic) {
		Preferences.INSTANCE.put(Preferences.KEY_CLASSICFONT, classic);
		if (classic) {
			RenderedText.setFont("pixelfont.ttf");
		} else {
			RenderedText.setFont(null);
		}
	}

	public static boolean classicFont() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_CLASSICFONT,
				(language() != Languages.CHINESE));
	}

	public static void lastClass(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_LAST_CLASS, value);
	}

	public static int lastClass() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_LAST_CLASS, 0, 0, 3);
	}

	public static void challenges(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_CHALLENGES, value);
	}

	public static int challenges() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_CHALLENGES, 0, 0, Challenges.MAX_VALUE);
	}

	public static void quickSlots(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_QUICKSLOTS, value);
	}

	public static int quickSlots() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_QUICKSLOTS, 3, 0, 4);
	}

	public static void flipToolbar(boolean value) {
		Preferences.INSTANCE.put(Preferences.KEY_FLIPTOOLBAR, value);
	}

	public static boolean flipToolbar() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_FLIPTOOLBAR, false);
	}

	public static void flipTags(boolean value) {
		Preferences.INSTANCE.put(Preferences.KEY_FLIPTAGS, value);
	}

	public static boolean flipTags() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_FLIPTAGS, false);
	}

	public static void toolbarMode(String value) {
		Preferences.INSTANCE.put(Preferences.KEY_BARMODE, value);
	}

	public static String toolbarMode() {
		return Preferences.INSTANCE.getString(Preferences.KEY_BARMODE, !landscape() ? "SPLIT" : "GROUP");
	}

	public static void intro(boolean value) {
		Preferences.INSTANCE.put(Preferences.KEY_INTRO, value);
	}

	public static boolean intro() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_INTRO, true);
	}

	public static void version(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_VERSION, value);
	}

	public static int version() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_VERSION, 0);
	}

	/*
	 * <--- Preferences
	 */

	public static void reportException(Throwable tr) {
		Log.e("PD", Log.getStackTraceString(tr));
	}
}