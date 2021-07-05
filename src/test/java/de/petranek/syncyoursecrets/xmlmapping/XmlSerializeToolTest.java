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

import de.petranek.syncyoursecrets.util.SysXmlBaseException;
import de.petranek.syncyoursecrets.util.XmlSerializeTool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * The Class XmlSerializeToolTest performs a simple read/write test with an xml
 * document.
 *
 * @author Jan Petranek
 */
public class XmlSerializeToolTest {

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Read write test.
	 *
	 * @throws SysXmlBaseException the sys xml base exception
	 */
	@Test
	public void readWriteTest() throws SysXmlBaseException {
		File input = FileLocationHelper.getFile("input.xml");
		File output = FileLocationHelper.getFile("tmp/output.xml");

		// read input file
		Document doc = XmlSerializeTool.readFile(input);
		assertEquals("Checking root childnodes", "root", doc
				.getDocumentElement().getNodeName());

		// export to output file
		XmlSerializeTool.writeFile(doc, output);

		// read the exported file back in
		doc = XmlSerializeTool.readFile(output);
		assertEquals("Checking root childnodes", "root", doc
				.getDocumentElement().getNodeName());

	}

}
