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

import de.petranek.syncyoursecrets.util.DateTimeUtil;
import de.petranek.syncyoursecrets.util.SysParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// TODO: Auto-generated Javadoc
/**
 * The Class ListElementMergeTest test the merge behaviour of the ListElement.
 * Two ListElements are created, one called left, the other called right. These
 * ListElements are merged and the result is checked.
 *
 * To avoid copying the properties of all elements within each ListElement, we
 * place the properties for each ListElement (right, left, merged) into two
 * maps. One map holds the names of the child nodes, the other holds the
 * timestamp of the latest modification.
 *
 * The properties are indexed with the ID of the property (i.e. the child node),
 * so we can uniquely identify the child nodes across all ListElements.
 *
 * Deletion is done without the support of the Map solution.
 */
public class ListElementMergeTest {

	/** The Child node. */
	private static final String CHILD_NODE = "child";

	/** Maps the ids to the children's names of the left ListElement. */
	private Map<Long, String> leftNames = new HashMap<Long, String>();

	/** Maps the ids to the children's names of the right ListElement. */
	private Map<Long, String> rightNames = new HashMap<Long, String>();

	/** Maps the ids to the children's names of the expected ListElement. */
	private Map<Long, String> expectedNames = new HashMap<Long, String>();

	/** Maps the ids to the children's modification times of the left ListElement. */
	private Map<Long, ZonedDateTime> leftTimes = new HashMap<Long, ZonedDateTime>();

	/** Maps the ids to the children's modification times of the right ListElement. */
	private Map<Long, ZonedDateTime> rightTimes = new HashMap<Long, ZonedDateTime>();

	/** Maps the ids to the children's modification times of the expected ListElement. */
	private Map<Long, ZonedDateTime> expectedTimes = new HashMap<Long, ZonedDateTime>();

	/**
	 * Gets the old time stamp.
	 *
	 * @return the Old time Stamp
	 *
	 * @throws SysParseException the sys parse exception
	 */
	private static ZonedDateTime getOldTimeStamp() throws SysParseException {
		return DateTimeUtil.parseDateTime("2008-09-21T15:51:30.346+02:00");
	}

	/**
	 * Gets the updated time stamp.
	 *
	 * @return a time stamp after old, but before newest
	 *
	 * @throws SysParseException the sys parse exception
	 */
	private static ZonedDateTime getUpdatedTimeStamp() throws SysParseException {
		return DateTimeUtil.parseDateTime("2008-09-21T16:51:30.346+02:00");
	}

	/**
	 * Gets the second update time stamp.
	 *
	 * @return a time stamp after old, but before newest
	 *
	 * @throws SysParseException the sys parse exception
	 */
	private static ZonedDateTime getSecondUpdateTimeStamp() throws SysParseException {
		return DateTimeUtil.parseDateTime("2008-09-21T18:00:00.346+02:00");
	}

	/**
	 * Gets the newest time stamp.
	 *
	 * @return a time stamp after old and updated.
	 *
	 * @throws SysParseException the sys parse exception
	 */
	private static ZonedDateTime getNewestTimeStamp() throws SysParseException {
		return DateTimeUtil.parseDateTime("2008-09-22T00:00:00.000+02:00");

	}

	/**
	 * Sets up the testdata.
	 *
	 * @throws java.lang.Exception 	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		leftNames.put(1L, "Same");
		leftTimes.put(1L, getOldTimeStamp());
		leftNames.put(2L, "old");
		leftTimes.put(2L, getOldTimeStamp());
		leftNames.put(3L, "new");
		leftTimes.put(3L, getUpdatedTimeStamp());
		leftNames.put(4L, "Only in the left list");
		leftTimes.put(4L, getOldTimeStamp());
		// entry 5 not in left list
		leftNames.put(6L, "Will be deleted in right");
		leftTimes.put(6L, getOldTimeStamp());
		leftNames.put(7L, "Will be deleted in left");
		leftTimes.put(7L, getUpdatedTimeStamp());

		rightNames.put(1L, "Same");
		rightTimes.put(1L, getOldTimeStamp());
		rightNames.put(2L, "new");
		rightTimes.put(2L, getUpdatedTimeStamp());
		rightNames.put(3L, "old");
		rightTimes.put(3L, getOldTimeStamp());
		// entry 5 not in right list
		rightNames.put(5L, "Only in the right list");
		rightTimes.put(5L, getOldTimeStamp());
		rightNames.put(6L, "Will be deleted in right");
		rightTimes.put(6L, getUpdatedTimeStamp());
		rightNames.put(7L, "Will be deleted in left");
		rightTimes.put(7L, getUpdatedTimeStamp());

		// expected:
		expectedNames.put(1L, "Same");
		expectedTimes.put(1L, getOldTimeStamp());
		expectedNames.put(3L, "new");
		expectedTimes.put(3L, getUpdatedTimeStamp());
		expectedNames.put(4L, "Only in the left list");
		expectedTimes.put(4L, getOldTimeStamp());

		expectedNames.put(2L, "new");
		expectedTimes.put(2L, getUpdatedTimeStamp());
		// entry 5 not in right list
		expectedNames.put(5L, "Only in the right list");
		expectedTimes.put(5L, getOldTimeStamp());

		// both entries have been deleted.
		expectedNames.put(6L, "");
		expectedTimes.put(6L, getSecondUpdateTimeStamp());
		expectedNames.put(7L, "");
		expectedTimes.put(7L, getNewestTimeStamp());

	}

	/**
	 * Tear down.
	 *
	 * @throws java.lang.Exception 	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {

	}

	/**
	 * Merge two ListElements and check the result.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testMerge() throws Exception {
		ListElement left = listFromProperties(leftNames, leftTimes);
		ListElement right = listFromProperties(rightNames, rightTimes);

		// deletion is handled programatically here:
		MappingElement rightDelete = right.getElements().get(6L);
		right.remove(rightDelete);
		rightDelete.setLastModified(getSecondUpdateTimeStamp());
		right.setLastModified(getSecondUpdateTimeStamp());

		MappingElement leftDelete = left.getElements().get(7L);
		left.remove(leftDelete);
		leftDelete.setLastModified(getNewestTimeStamp());
		left.setLastModified(getNewestTimeStamp());

		// actually do the merge
		ListElement merged = (ListElement) left.merge(right);

		// check deletion
		assertTrue("Entry 6 should be deleted", merged.getElements().get(6L)
				.isDeleted());
		assertTrue("Entry 7 should be deleted", merged.getElements().get(7L)
				.isDeleted());

		// check all properties
		checkProperties(expectedNames, expectedTimes, merged);

	}

	/**
	 * Create a ListElement from the property maps.
	 *
	 * @param names the Map containing the names
	 * @param times the Map containing the modification timestamps
	 *
	 * @return the list element
	 *
	 * @throws Exception the exception
	 */
	public ListElement listFromProperties(Map<Long, String> names,
			Map<Long, ZonedDateTime> times) throws Exception {

		ListElement list = new ListElement("list", null);

		for (Long key : names.keySet()) {
			StringElement entry = new StringElement(CHILD_NODE, list);
			entry.setContent(names.get(key), true);
			entry.setLastModified(times.get(key));
			entry.setId(key);
			list.add(entry);
		}
		return list;

	}

	/**
	 * Check the properties on the resulting ListElement.
	 *
	 * @param names the names
	 * @param times the times
	 * @param list the list
	 *
	 * @throws Exception the exception
	 */
	public void checkProperties(Map<Long, String> names,
								Map<Long, ZonedDateTime> times, ListElement list) throws Exception {

		assertEquals("List expected to contain all elements", 7L, list
				.getElements().keySet().size());

		for (Long key : names.keySet()) {
			assertTrue(list.getElements().containsKey(key));
			StringElement entry = (StringElement) list.getElements().get(key);

			assertEquals("Checking content of element " + key, expectedNames
					.get(key), entry.getContent());
			assertEquals("Checking last modification time on element " + key,
					expectedTimes.get(key), entry.getLastModified());

		}

	}

}
