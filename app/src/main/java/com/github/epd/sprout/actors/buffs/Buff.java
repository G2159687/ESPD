
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.ui.BuffIndicator;

import java.text.DecimalFormat;
import java.util.HashSet;

public class Buff extends Actor {

	{
		actPriority = 3; //low priority, at the end of a turn
	}

	public Char target;

	public HashSet<Class<?>> resistances = new HashSet<Class<?>>();

	public HashSet<Class<?>> immunities = new HashSet<Class<?>>();

	public boolean attachTo(Char target) {

		if (target.immunities().contains(getClass())) {
			return false;
		}

		this.target = target;
		target.add(this);

		return target.buffs().contains(this);
	}

	public void detach() {
		target.remove(this);
	}

	@Override
	public boolean act() {
		diactivate();
		return true;
	}

	public int icon() {
		return BuffIndicator.NONE;
	}

	public String desc() {
		return "";
	}

	//to handle the common case of showing how many turns are remaining in a buff description.
	protected String dispTurns(float input) {
		return input == 1 ? "1" : new DecimalFormat("#.##").format(input);
	}

	public static <T extends Buff> T append(Char target, Class<T> buffClass) {
		try {
			T buff = buffClass.newInstance();
			buff.attachTo(target);
			return buff;
		} catch (Exception e) {
			return null;
		}
	}

	public static <T extends FlavourBuff> T append(Char target,
	                                               Class<T> buffClass, float duration) {
		T buff = append(target, buffClass);
		buff.spend(duration);
		return buff;
	}

	public static <T extends Buff> T affect(Char target, Class<T> buffClass) {
		T buff = target.buff(buffClass);
		if (buff != null) {
			return buff;
		} else {
			return append(target, buffClass);
		}
	}

	public static <T extends FlavourBuff> T affect(Char target,
	                                               Class<T> buffClass, float duration) {
		T buff = affect(target, buffClass);
		buff.spend(duration);
		return buff;
	}

	public static <T extends FlavourBuff> T prolong(Char target,
	                                                Class<T> buffClass, float duration) {
		T buff = affect(target, buffClass);
		buff.postpone(duration);
		return buff;
	}

	public static void detach(Buff buff) {
		if (buff != null) {
			buff.detach();
		}
	}

	public static void detach(Char target, Class<? extends Buff> cl) {
		detach(target.buff(cl));
	}
}
