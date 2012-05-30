package de.tud.kitchen.api.event;

import java.lang.reflect.Method;

/**
 * Base class for all EventConsumers. </br>
 * Does reflection based selection of the right handle Method
 * 
 * To extend this class you have to:
 * <ol>
 * 	<li>Add a handle* method for each event type you want to receive where * is the name of the event class</li>
 * </ol>
 * 
 * @author niklas
 */
public abstract class EventConsumer {

	/**
	 * generic handle method
	 * @param o
	 */
	public final void handle(Object o) {
		try {
			getMethod(o.getClass()).invoke(this, new Object[] { o });
		} catch (IllegalAccessException iae) {
			throw new RuntimeException("Anonymous event consumers are not supported. Declare your event consumer as normal or inner class",iae);
		} catch (Exception ex) {
			System.out.println("no appropriate handle() method");
		}
	}

	/**
	 * Method taken from Reflective Visitor Example at
	 * 
	 * {@link } 
	 * <ol>
	 * <li>Look for handleElementClassName() in the current class<li> 
	 * <li>Look for handleElementClassName() in superclasses</li>
	 * <li>Look for handleElementClassName() in interfaces</li>
	 * <li>Look for handleObject() in current class</li>
	 * 
	 * @param c
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Method getMethod(Class c) {
		Class newc = c;
		Method m = null;
		while (m == null && newc != Object.class) {
			String method = newc.getCanonicalName();
			if (method == null) {
				newc = newc.getSuperclass();
				continue;
			}
			method = generateMethodName(method);
			try {
				m = getClass().getMethod(method, new Class[] { newc });
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
					m = getClass().getMethod(method,
							new Class[] { interfaces[i] });
				} catch (NoSuchMethodException ex) {
				}
			}
		}
		if (m == null)
			try {
				m = getClass().getMethod("handleObject",
						new Class[] { Event.class });
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

}
