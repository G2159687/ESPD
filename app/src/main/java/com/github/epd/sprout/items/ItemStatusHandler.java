
package com.github.epd.sprout.items;

import com.github.epd.sprout.Dungeon;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class ItemStatusHandler<T extends Item> {

	private Class<? extends T>[] items;

	private HashMap<Class<? extends T>, Integer> images;
	private HashMap<Class<? extends T>, String> labels;
	private HashSet<Class<? extends T>> known;

	public ItemStatusHandler(Class<? extends T>[] items, String[] allLabels,
	                         Integer[] allImages) {

		this.items = items;

		this.images = new HashMap<Class<? extends T>, Integer>();
		this.labels = new HashMap<Class<? extends T>, String>();
		known = new HashSet<Class<? extends T>>();

		ArrayList<String> labelsLeft = new ArrayList<String>(
				Arrays.asList(allLabels));
		ArrayList<Integer> imagesLeft = new ArrayList<Integer>(
				Arrays.asList(allImages));

		for (int i = 0; i < items.length; i++) {

			Class<? extends T> item = (items[i]);

			int index = Random.Int(labelsLeft.size());

			labels.put(item, labelsLeft.get(index));
			labelsLeft.remove(index);

			images.put(item, imagesLeft.get(index));
			imagesLeft.remove(index);
		}
	}

	public ItemStatusHandler(Class<? extends T>[] items, String[] labels,
	                         Integer[] images, Bundle bundle) {

		this.items = items;

		this.images = new HashMap<Class<? extends T>, Integer>();
		this.labels = new HashMap<Class<? extends T>, String>();
		known = new HashSet<Class<? extends T>>();

		restore(bundle, labels, images);
	}

	private static final String PFX_IMAGE = "_image";
	private static final String PFX_LABEL = "_label";
	private static final String PFX_KNOWN = "_known";

	public void save(Bundle bundle) {
		for (int i = 0; i < items.length; i++) {
			String itemName = items[i].toString();
			bundle.put(itemName + PFX_IMAGE, images.get(items[i]));
			bundle.put(itemName + PFX_LABEL, labels.get(items[i]));
			bundle.put(itemName + PFX_KNOWN, known.contains(items[i]));
		}
	}

	private void restore(Bundle bundle, String[] allLabels, Integer[] allImages) {

		ArrayList<String> labelsLeft = new ArrayList<String>(
				Arrays.asList(allLabels));
		ArrayList<Integer> imagesLeft = new ArrayList<Integer>(
				Arrays.asList(allImages));

		for (int i = 0; i < items.length; i++) {

			Class<? extends T> item = (items[i]);
			String itemName = item.toString();

			if (bundle.contains(itemName + PFX_LABEL) && Dungeon.version > 4) {

				String label = bundle.getString(itemName + PFX_LABEL);
				labels.put(item, label);
				labelsLeft.remove(label);

				Integer image = bundle.getInt(itemName + PFX_IMAGE);
				images.put(item, image);
				imagesLeft.remove(image);

				if (bundle.getBoolean(itemName + PFX_KNOWN)) {
					known.add(item);
				}

				// if there's a new item, give it a random image
				// or.. if we're loading from an untrusted version, randomize
				// the image to be safe.
			} else {

				int index = Random.Int(labelsLeft.size());

				labels.put(item, labelsLeft.get(index));
				labelsLeft.remove(index);

				images.put(item, imagesLeft.get(index));
				imagesLeft.remove(index);

				if (bundle.contains(itemName + PFX_KNOWN)
						&& bundle.getBoolean(itemName + PFX_KNOWN)) {
					known.add(item);
				}
			}
		}
	}

	public int image(T item) {
		return images.get(item.getClass());
	}

	public String label(T item) {
		return labels.get(item.getClass());
	}

	public boolean isKnown(T item) {
		return known.contains(item.getClass());
	}

	@SuppressWarnings("unchecked")
	public void know(T item) {
		known.add((Class<? extends T>) item.getClass());

		if (known.size() == items.length - 1) {
			for (int i = 0; i < items.length; i++) {
				if (!known.contains(items[i])) {
					known.add(items[i]);
					break;
				}
			}
		}
	}

	public HashSet<Class<? extends T>> known() {
		return known;
	}

	public HashSet<Class<? extends T>> unknown() {
		HashSet<Class<? extends T>> result = new HashSet<Class<? extends T>>();
		for (Class<? extends T> i : items) {
			if (!known.contains(i)) {
				result.add(i);
			}
		}
		return result;
	}
}
