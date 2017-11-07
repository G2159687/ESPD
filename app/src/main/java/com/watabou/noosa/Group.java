
package com.watabou.noosa;

import java.util.ArrayList;

public class Group extends Gizmo {

	protected ArrayList<Gizmo> members;

	// Accessing it is a little faster,
	// than calling memebers.getSize()
	public int length;

	public static boolean freezeEmitters = false;

	public Group() {
		members = new ArrayList<Gizmo>();
		length = 0;
	}

	@Override
	public synchronized void destroy() {
		for (int i = 0; i < length; i++) {
			Gizmo g = members.get(i);
			if (g != null) {
				g.destroy();
			}
		}

		members.clear();
		members = null;
		length = 0;
	}

	@Override
	public synchronized void update() {
		for (int i = 0; i < length; i++) {
			Gizmo g = members.get(i);
			if (g != null && g.exists && g.active) {
				g.update();
			}
		}
	}

	@Override
	public synchronized void draw() {
		for (int i = 0; i < length; i++) {
			Gizmo g = members.get(i);
			if (g != null && g.exists && g.isVisible()) {
				g.draw();
			}
		}
	}

	@Override
	public synchronized void kill() {
		// A killed group keeps all its members,
		// but they get killed too
		for (int i = 0; i < length; i++) {
			Gizmo g = members.get(i);
			if (g != null && g.exists) {
				g.kill();
			}
		}

		super.kill();
	}

	public synchronized int indexOf(Gizmo g) {
		return members.indexOf(g);
	}

	public synchronized Gizmo add(Gizmo g) {

		if (g.parent == this) {
			return g;
		}

		if (g.parent != null) {
			g.parent.remove(g);
		}

		// Trying to find an empty space for a new member
		for (int i = 0; i < length; i++) {
			if (members.get(i) == null) {
				members.set(i, g);
				g.parent = this;
				return g;
			}
		}

		members.add(g);
		g.parent = this;
		length++;
		return g;
	}

	public synchronized Gizmo addToBack(Gizmo g) {

		if (g.parent == this) {
			sendToBack(g);
			return g;
		}

		if (g.parent != null) {
			g.parent.remove(g);
		}

		if (members.get(0) == null) {
			members.set(0, g);
			g.parent = this;
			return g;
		}

		members.add(0, g);
		g.parent = this;
		length++;
		return g;
	}

	public synchronized Gizmo recycle(Class<? extends Gizmo> c) {

		Gizmo g = getFirstAvailable(c);
		if (g != null) {

			return g;

		} else if (c == null) {

			return null;

		} else {

			try {
				return add(c.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	// Fast removal - replacing with null
	public synchronized Gizmo erase(Gizmo g) {
		int index = members.indexOf(g);
		if (index != -1) {
			members.set(index, null);
			g.parent = null;
			return g;
		} else {
			return null;
		}
	}

	// Real removal
	public synchronized Gizmo remove(Gizmo g) {
		if (members.remove(g)) {
			length--;
			g.parent = null;
			return g;
		} else {
			return null;
		}
	}

	public synchronized Gizmo replace(Gizmo oldOne, Gizmo newOne) {
		int index = members.indexOf(oldOne);
		if (index != -1) {
			members.set(index, newOne);
			newOne.parent = this;
			oldOne.parent = null;
			return newOne;
		} else {
			return null;
		}
	}

	public synchronized Gizmo getFirstAvailable(Class<? extends Gizmo> c) {

		for (int i = 0; i < length; i++) {
			Gizmo g = members.get(i);
			if (g != null && !g.exists && ((c == null) || g.getClass() == c)) {
				return g;
			}
		}

		return null;
	}

	public synchronized int countLiving() {

		int count = 0;

		for (int i = 0; i < length; i++) {
			Gizmo g = members.get(i);
			if (g != null && g.exists && g.alive) {
				count++;
			}
		}

		return count;
	}

	public synchronized int countDead() {

		int count = 0;

		for (int i = 0; i < length; i++) {
			Gizmo g = members.get(i);
			if (g != null && !g.alive) {
				count++;
			}
		}

		return count;
	}

	public synchronized Gizmo random() {
		if (length > 0) {
			return members.get((int) (Math.random() * length));
		} else {
			return null;
		}
	}

	public synchronized void clear() {
		for (int i = 0; i < length; i++) {
			Gizmo g = members.get(i);
			if (g != null) {
				g.parent = null;
			}
		}
		members.clear();
		length = 0;
	}

	public synchronized Gizmo bringToFront(Gizmo g) {
		if (members.contains(g)) {
			members.remove(g);
			members.add(g);
			return g;
		} else {
			return null;
		}
	}

	public synchronized Gizmo sendToBack(Gizmo g) {
		if (members.contains(g)) {
			members.remove(g);
			members.add(0, g);
			return g;
		} else {
			return null;
		}
	}
}
