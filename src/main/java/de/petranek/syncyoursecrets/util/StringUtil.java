/**
 * SyncYourSecrets-xmlbase provides a basic layer for SyncYourSecrets
 * 
 * 
 *    Copyright 2009 Jan Petranek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.   
 * You may obtain a copy of the License at   
 *     http://www.apache.org/licenses/LICENSE-2.0   
 *    
 * Unless required by applicable law or agreed to in writing, software   
 * distributed under the License is distributed on an "AS IS" BASIS,   
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
 * See the License for the specific language governing permissions and   
 * limitations under the License.   
 * 
 */
package de.petranek.syncyoursecrets.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class StringUtil is a static helper class for String operations.
 * 
 * @author Jan Petranek
 */
public final class StringUtil {

	/** The Constant WHITESPACE_PATTERN. */
	static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s*");

	/**
	 * Private Constructor, as this is a static helper class.
	 */
	private StringUtil() {
		// util class, no constructor
	}

	/**
	 * Checks if a given String is empty. A String is considered empty, if * it
	 * is null * it consist only of whitespace * its length is 0
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return true, if is empty
	 */
	public static boolean isEmpty(String s) {
		if (null == s) {
			return true;
		}

		Matcher m = WHITESPACE_PATTERN.matcher(s);
		return m.matches();
	}

}
