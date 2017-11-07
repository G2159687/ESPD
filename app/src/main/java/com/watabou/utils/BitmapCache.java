
package com.watabou.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class BitmapCache {

	private static final String DEFAULT = "__default";

	private static HashMap<String, Layer> layers = new HashMap<String, BitmapCache.Layer>();

	private static BitmapFactory.Options opts = new BitmapFactory.Options();

	static {
		opts.inDither = false;
	}

	public static Context context;

	public static Bitmap get(String assetName) {
		return get(DEFAULT, assetName);
	}

	public static Bitmap get(String layerName, String assetName) {

		Layer layer;
		if (!layers.containsKey(layerName)) {
			layer = new Layer();
			layers.put(layerName, layer);
		} else {
			layer = layers.get(layerName);
		}

		if (layer.containsKey(assetName)) {
			return layer.get(assetName);
		} else {

			try {
				InputStream stream = context.getResources().getAssets().open(assetName);
				Bitmap bmp = BitmapFactory.decodeStream(stream, null, opts);
				layer.put(assetName, bmp);
				return bmp;
			} catch (IOException e) {
				return null;
			}

		}
	}

	public static Bitmap get(int resID) {
		return get(DEFAULT, resID);
	}

	public static Bitmap get(String layerName, int resID) {

		Layer layer;
		if (!layers.containsKey(layerName)) {
			layer = new Layer();
			layers.put(layerName, layer);
		} else {
			layer = layers.get(layerName);
		}

		if (layer.containsKey(resID)) {
			return layer.get(resID);
		} else {

			Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resID);
			layer.put(resID, bmp);
			return bmp;

		}
	}

	public static void clear(String layerName) {
		if (layers.containsKey(layerName)) {
			layers.get(layerName).clear();
			layers.remove(layerName);
		}
	}

	public static void clear() {
		for (Layer layer : layers.values()) {
			layer.clear();
		}
		layers.clear();
	}

	@SuppressWarnings("serial")
	private static class Layer extends HashMap<Object, Bitmap> {

		@Override
		public void clear() {
			for (Bitmap bmp : values()) {
				bmp.recycle();
			}
			super.clear();
		}
	}
}
