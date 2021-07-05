/**
 * SyncYourSecrets-xmlbase provides a basic layer for SyncYourSecrets
 *
 *
 *    Copyright 2008 Jan Petranek
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
package de.petranek.syncyoursecrets.xmlmapping;

import de.petranek.syncyoursecrets.util.XmlSerializeTool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

/**
 * The Class StringElementTest test the StringElement.
 *
 * @author Jan Petranek
 */
public class StringElementTest {

	/** The Constant TEST_ID. */
	private static final int TEST_ID = 4711;

	/** The Constant OLD_TIME_STAMP. */
	private static final ZonedDateTime OLD_TIME_STAMP =  ZonedDateTime.parse(
			"2008-09-21T15:51:30.346+02:00");

	/** The Constant UPDATED_TIME_STAMP. */
	private static final ZonedDateTime UPDATED_TIME_STAMP =  ZonedDateTime.parse(
			"2008-09-21T16:51:30.346+02:00");

	/** The Constant OLD_CONTENT. */
	private static final String OLD_CONTENT = "Goodby, old World!";

	/** The Constant UPDATED_CONTENT. */
	private static final String UPDATED_CONTENT = "Hello, Brave New World!";

	/** The Constant ENTRY_NAME. */
	private static final String ENTRY_NAME = "firstElement";

	/** The Constant TEMPFILE points to a temporary file. */
	private static final File TEMPFILE = FileLocationHelper
			.getFile("/tmp/createString.xml");

	/** Test content. */
	private static final String TEST_CONTENT = "Hello, World!";

	/**
	 * Test the merge function of two StringElements. We expect the merged
	 * StringElement to contain all the properties of the newer StringElement.
	 *
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testMerge() throws Exception {
		// create a newer StringElement
		StringElement newEntry = new StringElement(ENTRY_NAME, null);
		newEntry.setContent(UPDATED_CONTENT, false);
		newEntry.setId(TEST_ID);
		newEntry.setLastModified(UPDATED_TIME_STAMP);
		newEntry.setLastAction(MappingElement.ACTIONS.UPDATE);

		// create an "old" StringElement
		StringElement oldEntry = new StringElement(ENTRY_NAME, null);
		oldEntry.setContent(OLD_CONTENT, false);
		oldEntry.setId(TEST_ID);
		oldEntry.setLastModified(OLD_TIME_STAMP);
		oldEntry.setLastAction(MappingElement.ACTIONS.CREATE);

		// compare the merged Entry with the newest StringElement.
		StringElement mergedEntry = (StringElement) oldEntry.merge(newEntry);
		assertEquals("Comparing element name", ENTRY_NAME, mergedEntry
				.getElementName());
		assertEquals("Comparing content", UPDATED_CONTENT, mergedEntry
				.getContent());
		assertEquals("Comparing last modified timestamp", UPDATED_TIME_STAMP,
				mergedEntry.getLastModified());
		assertEquals("Comparing last modification action",
				MappingElement.ACTIONS.UPDATE, mergedEntry.getLastAction());

	}

	/**
	 * Fill a StringElement from the XML testfile and check its content. This is
	 * part of the roundtrip test.
	 *
	 * @param input the file to read
	 *
	 * @throws Exception the exception
	 */
	public void fillFromXml(File input) throws Exception {
		Document doc = XmlSerializeTool.readFile(input);

		Element root = doc.getDocumentElement();
		StringElement stringElement = new StringElement(root, null);
		assertEquals("Element name check", ENTRY_NAME, stringElement
				.getElementName());

		assertEquals("Check content", TEST_CONTENT, stringElement.getContent());

	}

	/**
	 * Cleanup the testfile.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		if (TEMPFILE.exists()) {
			TEMPFILE.delete();
		}
	}

	/**
	 * Cleanup the testfile.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
		if (TEMPFILE.exists()) {
			TEMPFILE.delete();
		}

	}

	/**
	 * This test case is a round trip: It first generates a StringElement,
	 * writes it to a file and then reads the file back in.
	 *
	 * This is part of the roundtrip test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testRoundTrip() throws Exception {
		toXml(TEMPFILE);

		fillFromXml(TEMPFILE);
	}

	/**
	 * Create a StringElement and write it to the XML testfile.
	 *
	 * @param output the file to write to.
	 *
	 * @throws Exception the exception
	 */
	public void toXml(File output) throws Exception {
		// create new document
		Document doc = XmlSerializeTool.createDocument();

		StringElement stringElement = new StringElement(ENTRY_NAME, null);
		stringElement.setContent(TEST_CONTENT, false);

		Element node = stringElement.toXml(doc);

		doc.appendChild(node);

		XmlSerializeTool.writeFile(doc, output);
	}
}
