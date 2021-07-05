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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The Class SetIntersection computes the intersection of two sets, as
 * well as the exclusive sets for each set.
 *
 * After the class has been initialized through the constructor,
 * the computed sets can be obtained through the getter methods.
 *
 * The class is not typed, we simply don't need it here, as a Set of Objects
 * is completely adequate for our needs.
 *
 * @author Jan Petranek
 */
public class SetIntersection {

	/** The set of entries exclusively found in A. */
	@SuppressWarnings("unchecked")
	private Set exclusiveInA = new HashSet();

	/** The set of entries exclusively found in B. */
	@SuppressWarnings("unchecked")
	private Set exclusiveInB = new HashSet();

	/**
	 * The intersection of the two sets, i.e. the set of elements,
	 *  where element is in A and element is in B.
	 */
	@SuppressWarnings("unchecked")
	private Set intersection = new HashSet();

	/**
	 * Takes the two input sets A and B and computes the
	 * intersection as well as the exclusive sets for each set.
	 *
	 * @param a the a
	 * @param b the b
	 */
	@SuppressWarnings("unchecked")
	public SetIntersection(Set a, Set b) {
		Set superSet = computeSuperSet(a, b);

		computeSubsets(superSet, a, b);
	}

	/**
	 * Compute super set.
	 *
	 * @param a the a
	 * @param b the b
	 *
	 * @return the sets the
	 */
	@SuppressWarnings("unchecked")
	private Set computeSuperSet(Set a, Set b) {
		Set superset = new HashSet();
		superset.addAll(a);
		superset.addAll(b);
		return superset;
	}

	/**
	 * Compute subsets.
	 *
	 * @param superSet the super set
	 * @param a the a
	 * @param b the b
	 */
	@SuppressWarnings("unchecked")
	private void computeSubsets(Set superSet, Set a, Set b) {
		Iterator superSetIterator = superSet.iterator();

		while (superSetIterator.hasNext()) {
			Object current = superSetIterator.next();

			if (a.contains(current) && b.contains(current)) {
				intersection.add(current);
			} else if (a.contains(current)) {
				exclusiveInA.add(current);
			} else if (b.contains(current)) {
				exclusiveInB.add(current);
			}

		}

	}

	/**
	 * Gets the set of entries exclusively found in A.
	 *
	 * @return the exclusiveInA
	 */
	@SuppressWarnings("unchecked")
	public Set getExclusiveInA() {
		return exclusiveInA;
	}

	/**
	 * Gets the set of entries exclusively found in B.
	 *
	 * @return the exculusiveInB
	 */
	@SuppressWarnings("unchecked")
	public Set getExclusiveInB() {
		return exclusiveInB;
	}

	/**
	 * Gets the intersection of the two sets, i.e. the set of elements,
	 * where element is in A and element is in B..
	 *
	 * @return the intersection
	 */
	@SuppressWarnings("unchecked")
	public Set getIntersection() {
		return intersection;
	}

}
