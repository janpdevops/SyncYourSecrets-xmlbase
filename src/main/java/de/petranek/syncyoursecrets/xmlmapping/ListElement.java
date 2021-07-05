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

import de.petranek.syncyoursecrets.util.SetIntersection;
import de.petranek.syncyoursecrets.util.SysInvalidArgumentException;
import de.petranek.syncyoursecrets.util.SysParseException;
import de.petranek.syncyoursecrets.util.SysRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * The Class ListElement represents a list of MappingElements. Each
 * MappingElement is uniquely identified by its id. When merging two
 * ListElements, the children of both lists are recursively merged.
 * 
 * @author Jan Petranek
 */
public class ListElement extends NamedElement {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(ListElement.class);

	/** The elements, identified by their id. */
	private TreeMap<Long, MappingElement> elements = new TreeMap<Long, MappingElement>();

	/**
	 * Gets the elements.
	 * 
	 * NOTE: This method is currently called in test methods; it should not be
	 * used in production code, as this circumvents the encapsulation (esp.
	 * deleted entries can be seen).
	 * 
	 * @return the elements
	 */
	protected TreeMap<Long, MappingElement> getElements() {
		logger.warn("Call to getElements detected; this shall not occur in"
				+ "normal use!");

		return elements;
	}

	/**
	 * Instantiates a new list element.
	 * 
	 * @param elementName
	 *            the XML-elements name
	 * @param parent
	 *            the parent in the object structure, or null if this is the
	 *            root element
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 */
	public ListElement(String elementName, MappingElement parent)
			throws SysInvalidArgumentException {
		super(elementName, parent);

	}

	/**
	 * Recursively parses a XML-node representing a ListElement and fills a new
	 * list element with its values.
	 * 
	 * Note: The processing of the particular child elements is delegated to the
	 * method loadElement. Overwrite this method in a subclass, where you know
	 * what type of elements to expect.
	 * 
	 * @param node
	 *            the node to process
	 * @param parent
	 *            the parent in the object structure, or null if this is the
	 *            root element
	 * 
	 * @throws SysParseException
	 *             when the XML could not be parsed.
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 */
	public ListElement(Element node, MappingElement parent)
			throws SysParseException, SysInvalidArgumentException {
		super(node, parent);

		logger.debug("entering constructor from Xml");
		if (!this.isDeleted()) {
			parseChildNodes(node);
		} else {
			logger.debug("List already deleted, not parsing children");
		}
		logger.debug("exiting constructor from Xml");
	}

	/**
	 * Parses the child nodes.
	 * 
	 * @param node
	 *            the node
	 * 
	 * @throws SysParseException
	 *             the sys parse exception
	 * @throws SysInvalidArgumentException
	 *             the sys invalid argument exception
	 */
	private void parseChildNodes(Element node) throws SysParseException,
			SysInvalidArgumentException {
		Node current = node.getFirstChild();
		while (current != null) { // can happen, when table has been deleted

			if (current.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) current;
				if (NamedElement.NAME.equals(elem.getNodeName())) {
					// already handled in the NamedElement constructor
					logger.debug("Skipping name node, already handled in super constructor");
				} else {
					resolveChild(current, elem);
				}
			}

			current = current.getNextSibling();
		}
	}

	/**
	 * @param current
	 * @param elem
	 * @throws SysParseException
	 * @throws SysInvalidArgumentException
	 */
	private void resolveChild(Node current, Element elem)
			throws SysParseException, SysInvalidArgumentException {
		MappingElement me = loadElement(elem.getNodeName(), elem);
		if (me != null) {

			this.elements.put(me.getId(), me);

			if (logger.isTraceEnabled()) {
				logger.trace("Adding child element " + MappingElement.log(me)
						+ " to " + MappingElement.log(this));
			}

		} else {
			logger.warn("Child element: " + current.getNodeName()
					+ " could not be converted to child");
		}
	}

	/**
	 * Serializes this ListElement and all its children to XML. The resulting
	 * XML-Element must still be added to the appropriate place in the DOM-Tree.
	 * 
	 * @param doc
	 *            the XML document
	 * 
	 * @return an XML-Element representing this MappingElement (and its
	 *         children)
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#toXml(org.w3c
	 *      .dom.Document)
	 */
	@Override
	public Element toXml(Document doc) throws SysInvalidArgumentException {
		logger.debug("entering serialize to XML for " + this);
		Element node = super.toXml(doc);

		if (logger.isTraceEnabled()) {
			logger.trace("Serializing " + elements.size() + " children");
		}

		for (MappingElement element : elements.values()) {
			Element elem = element.toXml(doc);
			node.appendChild(elem);
		}
		logger.debug("exiting serialize to XML");
		return node;
	}

	/**
	 * Merge this ListElement with another ListElement. After the most recent
	 * ListElement has been selected, the child elements are recursively merged.
	 * 
	 * Each child is uniquely identified by its id.
	 * 
	 * When an entry exists only in one of the two ListElements, it will be
	 * simply inserted into the resulting List.
	 * 
	 * If an entry with the same Id is found in both ListElements, the merged
	 * entry will be inserted into the resulting list.
	 * 
	 * When the entry exists in no list, nothing has to be done ;-)
	 * 
	 * @param other
	 *            the other ListElement
	 * 
	 * @return the merged ListElement
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 * 
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#merge(de.petranek.syncyoursecrets.xmlmapping.MappingElement)
	 */
	@Override
	public MappingElement merge(MappingElement other)
			throws SysInvalidArgumentException {

		logger.debug("entering merge, will merge " + MappingElement.log(this)
				+ " with " + MappingElement.log(other));

		if (other instanceof ListElement) {

			ListElement otherList = (ListElement) other;
			ListElement target = (ListElement) super.merge(other);
			if (!target.isDeleted()) {

				SetIntersection setIntersection = new SetIntersection(
						this.elements.keySet(), otherList.elements.keySet());

				mergeCommonChildren(setIntersection.getIntersection(), this,
						otherList, target);

				addUniqueChildren(setIntersection.getExclusiveInA(), this,
						target);

				addUniqueChildren(setIntersection.getExclusiveInB(), otherList,
						target);

			} else {
				logger.trace("Merged Listelement ist deleted, skip merging children.");
			}

			logger.debug("exiting merge, resulted in "
					+ MappingElement.log(target));
			return target;
		} else {
			String msg = "Cannot merge " + MappingElement.log(this) + " with "
					+ MappingElement.log(other);
			logger.error(msg);
			throw new SysInvalidArgumentException(msg);
		}

	}

	/**
	 * Adds the children that are unique in the sourceElement.
	 * 
	 * @param exclusiveSet
	 *            the set of keys exclusive in the sourceElement
	 * @param sourceElement
	 *            the source element, where the elements can be found by key
	 * @param target
	 *            the element, where the children are added to.
	 */
	private void addUniqueChildren(Set exclusiveSet, ListElement sourceElement,
			ListElement target) {

		for (Object key : exclusiveSet) {
			MappingElement element = sourceElement.elements.get(key);
			target.add(element, true);
			logger.trace(MappingElement.log(element) + " only found in "
					+ MappingElement.log(sourceElement)
					+ " adding to merge result");

		}

	}

	/**
	 * Merge common children. Both ListElements contain a common set of entries,
	 * this should be already computed. For each entry in the key set, we get
	 * the child nodes from both sets, merge them and put them into the target
	 * ListElement.
	 * 
	 * @param intersection
	 *            the set of keys found in both lists.
	 * @param ourList
	 *            first source for children to merge
	 * @param theirList
	 *            first source for children to merge
	 * @param target
	 *            the element, where the merged children are added to.
	 * 
	 * @throws SysInvalidArgumentException
	 *             the sys invalid argument exception
	 */
	private void mergeCommonChildren(Set intersection, ListElement ourList,
			ListElement theirList, ListElement target)
			throws SysInvalidArgumentException {

		for (Object key : intersection) {

			MappingElement ourElement = ourList.elements.get(key);
			MappingElement theirElement = theirList.elements.get(key);

			logger.debug("Element " + MappingElement.log(ourElement)
					+ " found in both lists, merging");
			MappingElement merged = ourElement.merge(theirElement);
			target.add(merged, true);

		}

	}

	/**
	 * Mark this ListElement as deleted and delete all child nodes.
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#delete()
	 */
	@Override
	public void delete() {
		super.delete();
		logger.debug("Deleting " + this);
		// remove all elements
		this.elements.clear();

	}

	/**
	 * Load a child element from XML.
	 * 
	 * By default, we assume all child nodes to be StringElements. Override this
	 * in your own model class, where you can identify the type of node. I
	 * recommend to identify the child node types by the XML element name of the
	 * nodes, e.g. if (name.equals("simple") { return new StringElement(node); }
	 * else (name.equals("custom") { return new CustomElement(node); } else { //
	 * do Exception handling }
	 * 
	 * 
	 * @param name
	 *            the name of the XML-node
	 * @param node
	 *            the XML-node to be parsed
	 * 
	 * @return a mapping element
	 * @throws SysParseException
	 *             when the xML could not be parsed
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 */
	protected MappingElement loadElement(String name, Element node)
			throws SysParseException, SysInvalidArgumentException {
		StringElement sElement = new StringElement(node, this);

		return sElement;
	}

	/**
	 * Adds a child element.
	 * 
	 * @param element
	 *            the child element to add
	 */
	public void add(MappingElement element) {
		add(element, false);
	}

	/**
	 * Adds a child element. If suppressUpdate is set to true, the modification
	 * event is not triggered; useful, when parsing the content.
	 * 
	 * @param element
	 *            the element
	 * @param suppressUpdate
	 *            the suppress update flag
	 */
	protected void add(MappingElement element, boolean suppressUpdate) {
		logger.debug("Adding " + MappingElement.log(element));
		this.elements.put(element.getId(), element);
		element.setParent(this);

		if (!suppressUpdate) {
			try {
				this.modify();
			} catch (ElementDeletedException e) {
				String msg = "Add performed on deleted ListElement";
				logger.error(msg, e);
				throw new SysRuntimeException(msg, e);
			}
		}
	}

	/**
	 * Removes the child element.
	 * 
	 * Note: The element will remain in this List and its delete method will be
	 * called. This is necessary for the merge functionality.
	 * 
	 * @param element
	 *            the child element to remove
	 */
	public void remove(MappingElement element) {
		logger.debug("Deleting " + MappingElement.log(element));

		MappingElement toDelete = this.elements.get(element.getId());
		toDelete.delete();

	}

	/**
	 * Create an Iterator of the (non-deleted) elements. This method should be
	 * used, when accessing child elements from outside.
	 * 
	 * @return the iterator< mapping element>
	 */
	public Iterator<MappingElement> iterator() {
		return createVisibleList().iterator();
	}

	/**
	 * Create an Array of the (non-deleted) elements. This method should be
	 * used, when accessing child elements from outside.
	 * 
	 * @return the mapping element[]
	 */
	public MappingElement[] toArray() {
		ArrayList<MappingElement> visibleList = createVisibleList();

		return visibleList.toArray(new MappingElement[0]);
	}

	/**
	 * Creates a list of "visible" entries, i.e. entries, that have not been
	 * deleted.
	 * 
	 * @return the array list< mapping element>
	 */
	private ArrayList<MappingElement> createVisibleList() {
		// create a cloned list:
		ArrayList<MappingElement> visibleList = new ArrayList<MappingElement>();

		for (MappingElement current : this.elements.values()) {
			if (!current.isDeleted()) {
				if (logger.isTraceEnabled()) {
					logger.trace("createVisibleList: exposing "
							+ MappingElement.log(current));
				}
				visibleList.add(current);
			} else if (logger.isTraceEnabled()) {
				logger.trace("createVisibleList: suppressing "
						+ MappingElement.log(current));
			}
		}
		return visibleList;
	}

}
