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


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.petranek.syncyoursecrets.util.SetIntersectionTest;
import de.petranek.syncyoursecrets.util.StringUtilTest;

/**
 * This class is only an empty dummy class. All testclasses are placed in the
 * annotation.
 *
 * @author Jan Petranek
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { CryptedTest.class, ListElementTest.class,
		ListElementMergeTest.class, StringElementTest.class,
		XmlSerializeToolTest.class, StringUtilTest.class,
		SetIntersectionTest.class })
public class AllXmlBaseTests {
	// dummy class
}
