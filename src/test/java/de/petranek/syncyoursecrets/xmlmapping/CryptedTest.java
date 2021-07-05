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


import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import de.petranek.syncyoursecrets.util.XmlSerializeTool;

/**
 * The Class CryptedTest basically runs the same testcase as the
 * ListElementTest. In Addition, it adds a layer of encryption to the testcase.
 * 
 * @author Jan Petranek
 */
public class CryptedTest extends ListElementTest {

	/**
	 * The Constant PASSWORD for en/decryption. A password longer than 7
	 * characters can be used only, if the unrestricted cryptography extensions
	 * have been installed in the JVM.
	 */
	public static final String PASSWORD = "secret";

	/**
	 * Sets the testcase up.
	 * 
	 * @throws java.lang.Exception 	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		setTestfile(FileLocationHelper.getFile("cryptedList.xml"));
	}

	/**
	 * Round-Trip testcase with added encryption.
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.ListElementTest#testRoundTrip
	 *      ()
	 * @throws Exception
	 *             , when the test fails.
	 */
	@Test
	public void testRoundTrip() throws Exception {
		Document docOut = toXml();

		XmlSerializeTool
				.writeEncryptedDocument(PASSWORD, docOut, getTestfile());

		Document docin = XmlSerializeTool.readEncryptedFile(PASSWORD,
				getTestfile());

		fillFromXml(docin);
	}

}
