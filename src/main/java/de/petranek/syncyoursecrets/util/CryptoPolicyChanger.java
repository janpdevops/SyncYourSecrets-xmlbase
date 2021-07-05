/**
 * 
 */
package de.petranek.syncyoursecrets.util;

import java.lang.reflect.Field;

/**
 * @author janp
 *
 *         Thanks to
 *         http://suhothayan.blogspot.de/2012/05/how-to-install-java-cryptography
 *         .html
 */
public class CryptoPolicyChanger {

	/**
	 * Just call this and you don't have to bother changing your JRE's crypto
	 * policy. Works on JRE 7 and 8.
	 */
	public static void enableStrongEncryption() {
		try {
			Field field = Class.forName("javax.crypto.JceSecurity")
					.getDeclaredField("isRestricted");
			field.setAccessible(true);
			field.set(null, java.lang.Boolean.FALSE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
