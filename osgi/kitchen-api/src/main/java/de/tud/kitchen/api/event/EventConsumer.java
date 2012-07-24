/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <nlochschmidt@gmail.com>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.api.event;

import java.lang.reflect.Method;
import java.util.HashMap;

import de.tud.kitchen.api.Kitchen;

/**
 * Base class for all EventConsumers. </br> The Publish-Subscribe Event
 * architecture relies on reflection based determination of the handle methods
 * inside an EventConsumer subclass.
 * <p>
 * To use this class you have to:
 * <ol>
 * <li>Extend class as a normal or "public static" inner class</li>
 * <li>Add a handle* method for each event type you want to receive where * is
 * the name of the event class and one argument with the event class (e.g.
 * handleEvent(Event event) or handleAccelerometerEvent(AccelerometerEvent
 * accEvent)</li>
 * <li>instantiate and register your EventConsumer with a {@link Kitchen}
 * instance.
 * </ol>
 * </p>
 * 
 * Afterwards you will get the event delivered to the correct methods for each type
 * 
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 */
public abstract class EventConsumer {

	private final HashMap<Class<?>, Method> lookupCache;

	public EventConsumer() {
		lookupCache = new HashMap<Class<?>, Method>();
	}

	/**
	 * generic handle method
	 * 
	 * @param o
	 */
	public final void handle(Object o) {
		try {
			getMethod(o.getClass()).invoke(this, new Object[] { o });
		} catch (IllegalAccessException iae) {
			throw new RuntimeException(
					"Anonymous event consumers are not supported. Declare your event consumer as normal or inner class",
					iae);
		} catch (Exception ex) {
			System.out.println("no appropriate handle() method " + ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Method taken from Reflective Visitor Example at
	 * {@link http://www.javaworld.com/javatips/jw-javatip98.html}
	 * 
	 * Finds a method that takes the event type as an argument.
	 * <p>
	 * Method-search follows the hierarchy
	 * <ol>
	 * <li>Look for handle*() in the current class
	 * <li>Look for handle*() in superclasses</li>
	 * <li>Look for handle*() in interfaces</li>
	 * <li>Look for handleObject() in current class</li>
	 * </ol>
	 * </p>
	 * @param c class for which to a handle method is searched
	 * @return found method
	 */
	@SuppressWarnings("rawtypes")
	protected Method getMethod(Class c) {
		Class newc = c;
		Method m = this.lookupCache.get(c);
		while (m == null && newc != Object.class) {
			String method = newc.getCanonicalName();
			if (method == null) {
				newc = newc.getSuperclass();
				continue;
			}
			method = generateMethodName(method);
			try {
				m = getClass().getMethod(method, new Class[] { newc });
				lookupCache.put(c, m);
			} catch (NoSuchMethodException ex) {
				newc = newc.getSuperclass();
			}
		}
		if (newc == Object.class) {
			// System.out.println( "Searching for interfaces" );
			Class[] interfaces = c.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				String method = interfaces[i].getName();
				method = generateMethodName(method);
				try {
					m = getClass().getMethod(method, new Class[] { interfaces[i] });
					lookupCache.put(c, m);
				} catch (NoSuchMethodException ex) {
				}
			}
		}
		if (m == null)
			try {
				m = getClass().getMethod("handleObject", new Class[] { Event.class });
				lookupCache.put(c, m);
			} catch (Exception ex) {
			}
		return m;
	}

	private static String generateMethodName(String className) {
		className = className.substring(className.lastIndexOf('.') + 1);
		return "handle" + className;
	}

	public void handleObject(Event event) {
		System.out.println("Try avoiding these printouts");
	}

	/**
	 * check if this consumer can handle a specific event type
	 * 
	 * @param eventType 
	 * @return true if it can handle the eventType, false otherwise
	 */
	public boolean handles(Class<?> eventType) {
		try {
			if (getMethod(eventType).equals(getClass().getMethod("handleObject", new Class[] { Event.class })))
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
