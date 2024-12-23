package com.gdx.pokemon.screen.renderer;

import com.gdx.pokemon.model.YSortable;

import java.util.Comparator;

/**
 * Used for sorting objects by their Y-coord during rendering.
 *
 */
public class WorldObjectYComparator implements Comparator<YSortable> {

	@Override
	public int compare(YSortable o1, YSortable o2) {
		if (o1.getWorldY() < o2.getWorldY()) {
			return -1;
		} else if (o1.getWorldY() > o2.getWorldY()) {
			return 1;
		}
		return 0;
	}
}
