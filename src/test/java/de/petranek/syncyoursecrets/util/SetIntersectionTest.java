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

//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.petranek.syncyoursecrets.util.SetIntersection;

/**
 * The Class SetIntersectionTest.
 *
 * @author Jan Petranek
 */
public class SetIntersectionTest {

	/**
	 * Ordinary set intersection, both sets overlap in some part, but
	 * still have their exclusive parts.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void ordinarySetIntersection() {

		HashSet a = new HashSet();
		HashSet b = new HashSet();

		fillSetWithNumbers(a, 1, 5);

		fillSetWithNumbers(b, 3, 9);

		SetIntersection intersection = new SetIntersection(a, b);
		checkSetForRange(intersection.getExclusiveInA(), 1, 2);

		checkSetForRange(intersection.getIntersection(), 3, 5);

		checkSetForRange(intersection.getExclusiveInB(), 6, 9);
	}

	/**
	 * Fill set with a consecutive range of numbers.
	 *
	 * @param set the set
	 * @param lower the first number to include
	 * @param upper the last number to include
	 */
	@SuppressWarnings("unchecked")
	private void fillSetWithNumbers(Set set, int lower, int upper) {
		for (int i = lower; i <= upper; i++) {
			set.add(i);
		}
	}

	/**
	 * Check, if the set contains the given range of numbers..
	 *
	 * @param set the set
	 * @param lower the lowest number to check for
	 * @param upper the upper number to check for
	 */
	@SuppressWarnings("unchecked")
	private void checkSetForRange(Set set, int lower, int upper) {
		for (int i = lower; i <= upper; i++) {
			assertTrue("Set should contain " + i, set.contains(i));

		}
	}

	/**
	 * Test an Intersection with an empty set.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void intersectionWithEmptySet() {
		HashSet a = new HashSet();
		HashSet b = new HashSet();

		fillSetWithNumbers(a, 1, 9);

		SetIntersection intersection = new SetIntersection(a, b);
		checkSetForRange(intersection.getExclusiveInA(), 1, 9);

		assertEquals("Intersection with empty set should be empty", 0,
				intersection.getIntersection().size());
		assertEquals("Other set was empty, should be empty", 0, intersection
				.getExclusiveInB().size());
	}

	/**
	 * Intersection with full set, i.e. both sets overlap compeletely.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void intersectionWithFullSet() {
		HashSet a = new HashSet();
		HashSet b = new HashSet();

		fillSetWithNumbers(a, 1, 9);
		fillSetWithNumbers(b, 1, 9);

		SetIntersection intersection = new SetIntersection(a, b);
		checkSetForRange(intersection.getIntersection(), 1, 9);

		assertEquals("Full intersection, exclusive set A should be empty", 0,
				intersection.getExclusiveInA().size());
		assertEquals("Full intersection, exclusive set B should be empty", 0,
				intersection.getExclusiveInB().size());

	}
}
