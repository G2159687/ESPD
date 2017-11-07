
package com.watabou.glscripts;

import com.watabou.glwrap.Program;
import com.watabou.glwrap.Shader;

import java.util.HashMap;

public class Script extends Program {

	private static final HashMap<Class<? extends Script>, Script> all =
			new HashMap<Class<? extends Script>, Script>();

	private static Script curScript = null;
	private static Class<? extends Script> curScriptClass = null;

	@SuppressWarnings("unchecked")
	public synchronized static <T extends Script> T use(Class<T> c) {

		if (c != curScriptClass) {

			Script script = all.get(c);
			if (script == null) {
				try {
					script = c.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
				all.put(c, script);
			}

			if (curScript != null) {
				curScript.unuse();
			}

			curScript = script;
			curScriptClass = c;
			curScript.use();

		}

		return (T) curScript;
	}

	public static void reset() {
		for (Script script : all.values()) {
			script.delete();
		}
		all.clear();

		curScript = null;
		curScriptClass = null;
	}

	public void compile(String src) {

		String[] srcShaders = src.split("//\n");
		attach(Shader.createCompiled(Shader.VERTEX, srcShaders[0]));
		attach(Shader.createCompiled(Shader.FRAGMENT, srcShaders[1]));
		link();

	}

	public void unuse() {
	}
}
