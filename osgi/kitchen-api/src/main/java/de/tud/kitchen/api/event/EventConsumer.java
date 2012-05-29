package de.tud.kitchen.api.event;

import java.lang.reflect.Method;

/**
 * Base class for all EventConsumers. </br>
 * Does reflection based selection of the right handle Method
 * 
 * To extend this class you have to:
 * <ol>
 * 	<li>Override the handle method using the snippet in the JavaDoc for that method</li>
 * 	<li>Add a handle* method for each event type you want to receive where * is the name of the event class</li>
 * </ol>
 * 
 * @author niklas
 */
public abstract class EventConsumer {

	/**
	 * Override with the following snippet:
	 * <pre>
	 *  try {
     * 		getMethod( o.getClass() ).invoke(this, new Object[] { o } );
     * } catch (Exception ex) {
     *  System.out.println( "no appropriate handle() method" );
     * }
	 * </pre>
	 * @param o
	 */
	abstract public void handle(Object o);

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
			String method = newc.getName();
			method = "handle" + method.substring(method.lastIndexOf('.') + 1);
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
				method = "handle"
						+ method.substring(method.lastIndexOf('.') + 1);
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
						new Class[] { Object.class });
			} catch (Exception ex) {
			}
		return m;
	}

}
