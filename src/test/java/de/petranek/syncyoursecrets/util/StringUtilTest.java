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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.petranek.syncyoursecrets.util.StringUtil;

/**
 * The Class StringUtilTest tests the StringUtil class. What a surprise.
 * @author Jan Petranek
 */
public class StringUtilTest {

	/**
	 * Tests, if an empty line is recognized as empty (yes).
	 */
	@Test
	public void testEmptyLine() {
		String s = "  \n";
		assertTrue("Empty string with line ending", StringUtil.isEmpty(s));
	}

	/**
	 * Tests, if an empty string is recognized as empty (yes).
	 */
	@Test
	public void testEmptyString() {
		String s = "  \t   ";
		assertTrue("Empty string with tab", StringUtil.isEmpty(s));
	}

	/**
	 * Tests, if null is recognized as empty (yes).
	 */
	@Test
	public void testNullString() {
		String s = null;
		assertTrue("Null string", StringUtil.isEmpty(s));
	}

	/**
	 * Tests, if a non-empty String is recognized as empty (no).
	 */
	@Test
	public void nonEmptyString() {
		String s = "   H    ";
		assertFalse("Non empty string", StringUtil.isEmpty(s));
	}

	/**
	 * Tests, if a non-empty multi-line String is recognized as empty (no).
	 */
	@Test
	public void nonEmptyMultiLine() {
		String s = "       \n" + "Nothing";
		assertFalse("Non empty string", StringUtil.isEmpty(s));
	}

	/**
	 * Tests, if a zero-length String is recognized as empty (yes).
	 */
	@Test
	public void zeroLength() {
		String s = "";
		assertTrue("Zero length string", StringUtil.isEmpty(s));
	}
}
