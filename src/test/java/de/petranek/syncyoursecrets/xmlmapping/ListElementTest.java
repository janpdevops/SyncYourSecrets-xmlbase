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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Iterator;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.petranek.syncyoursecrets.util.XmlSerializeTool;
import de.petranek.syncyoursecrets.xmlmapping.ListElement;
import de.petranek.syncyoursecrets.xmlmapping.MappingElement;
import de.petranek.syncyoursecrets.xmlmapping.StringElement;

/**
 * The Class ListElementTest tests creating and parsing an ListElement.
 *
 * @author Jan Petranek
 */
public class ListElementTest {

	/** The Constant SECOND_CONTENT contains the content of the 2nd element. */
	public static final String SECOND_CONTENT = "Another funny element!";

	/** The Constant SECOND_ELEMENT_NAME. */
	public static final String SECOND_ELEMENT_NAME = "otherElement";

	/** The Constant FIRST_CONTENT contains the content of the 1rst element. */
	public static final String FIRST_CONTENT = "Hello, World!";

	/** The Constant FIRST_ELEMENT_NAME. */
	public static final String FIRST_ELEMENT_NAME = "firstElement";

	/** The Constant LIST_ELEMENT_NAME. */
	public static final String LIST_ELEMENT_NAME = "list";

	/** The testfile. */
	private File testfile;

	/**
	 * Sets the testsuite up.
	 *
	 * @throws java.lang.Exception 	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		setTestfile(FileLocationHelper.getFile("tmp/createList.xml"));
	}

	/**
	 * Tear the testsuite down.
	 *
	 * @throws java.lang.Exception 	 *
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
		if (getTestfile().exists()) {
			getTestfile().delete();
		}
	}

	/**
	 * Test a round trip, where a ListElement with two child nodes is written to
	 * file and read back in.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testRoundTrip() throws Exception {
		Document docOut = toXml();
		XmlSerializeTool.writeFile(docOut, getTestfile());

		Document docin = XmlSerializeTool.readFile(getTestfile());
		fillFromXml(docin);
	}

	/**
	 * Creates a List and serialize it into an XML document.
	 *
	 * @return the document containing the ListElement.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public Document toXml() throws Exception {
		Document doc = XmlSerializeTool.createDocument();

		ListElement list = new ListElement(LIST_ELEMENT_NAME, null);

		StringElement stringElement = new StringElement(FIRST_ELEMENT_NAME,
				null);
		stringElement.setContent(FIRST_CONTENT, false);
		list.add(stringElement);

		StringElement otherElement = new StringElement(SECOND_ELEMENT_NAME,
				null);
		otherElement.setContent(SECOND_CONTENT, false);
		list.add(otherElement);

		Element root = list.toXml(doc);

		doc.appendChild(root);

		return doc;

	}

	/**
	 * Populate a ListElement from XML and check its content.
	 *
	 * @param doc
	 *            the doc
	 *
	 * @throws Exception
	 *             the exception
	 */
	public void fillFromXml(Document doc) throws Exception {

		Element root = doc.getDocumentElement();
		ListElement list = new ListElement(root, null);
		assertEquals("Name on list checked", LIST_ELEMENT_NAME, list
				.getElementName());

		Iterator<MappingElement> it = list.iterator();

		int foundcount = 0;
		while (it.hasNext()) {
			StringElement current = (StringElement) it.next();
			if (current.getElementName().equals(FIRST_ELEMENT_NAME)) {
				assertEquals("Check content", FIRST_CONTENT, current
						.getContent());
				foundcount++;
			}
			if (current.getElementName().equals(SECOND_ELEMENT_NAME)) {
				assertEquals("Check content", SECOND_CONTENT, current
						.getContent());
				foundcount++;
			}

		}
		assertEquals("Number of found elements should match", 2, foundcount);
	}

	/**
	 * Sets the testfile.
	 *
	 * @param testfile
	 *            the testfile to set
	 */
	protected void setTestfile(File testfile) {
		this.testfile = testfile;
	}

	/**
	 * Gets the testfile.
	 *
	 * @return the testfile
	 */
	protected File getTestfile() {
		return testfile;
	}

}
