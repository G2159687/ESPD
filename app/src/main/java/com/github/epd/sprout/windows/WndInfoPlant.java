
package com.github.epd.sprout.windows;

import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.sprites.PlantSprite;

public class WndInfoPlant extends WndTitledMessage {

	public WndInfoPlant(Plant plant) {

		super(new PlantSprite(plant.image), plant.plantName, plant.desc());

	}
}
