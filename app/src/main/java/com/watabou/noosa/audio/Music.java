
package com.watabou.noosa.audio;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.watabou.noosa.Game;

import java.io.IOException;

public enum Music implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

	INSTANCE;

	private MediaPlayer player;

	private String lastPlayed;
	private boolean lastLooping;

	private boolean enabled = true;

	public void play(String assetName, boolean looping) {

		if (isPlaying() && lastPlayed.equals(assetName)) {
			return;
		}

		stop();

		lastPlayed = assetName;
		lastLooping = looping;

		if (!enabled || assetName == null) {
			return;
		}

		try {

			AssetFileDescriptor afd = Game.instance.getAssets().openFd(assetName);

			player = new MediaPlayer();
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			player.setOnPreparedListener(this);
			player.setOnErrorListener(this);
			player.setLooping(looping);
			player.prepareAsync();

		} catch (IOException e) {

			player.release();
			player = null;

		}
	}

	public void mute() {
		lastPlayed = null;
		stop();
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		player.start();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		if (player != null) {
			player.release();
			player = null;
		}
		return true;
	}

	public void pause() {
		if (player != null) {
			player.pause();
		}
	}

	public void resume() {
		if (player != null) {
			player.start();
		}
	}

	public void stop() {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
	}

	public void volume(float value) {
		if (player != null) {
			player.setVolume(value, value);
		}
	}

	public boolean isPlaying() {
		return player != null && player.isPlaying();
	}

	public void enable(boolean value) {
		enabled = value;
		if (isPlaying() && !value) {
			stop();
		} else if (!isPlaying() && value) {
			play(lastPlayed, lastLooping);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}
}
