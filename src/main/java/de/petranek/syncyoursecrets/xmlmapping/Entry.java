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

import de.petranek.syncyoursecrets.util.SysInvalidArgumentException;
import de.petranek.syncyoursecrets.util.SysParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Class Entry is composed of a set of StringElements. This is suitable for
 * an entry with several data fields. It should be subclassed in a model class.
 * 
 * @author Jan Petranek
 */
public class Entry extends NamedElement {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(Entry.class);

	/** Holds the string elements, identified by a String. */
	private Map<String, StringElement> stringElements = new HashMap<String, StringElement>();

	/**
	 * Instantiates a new entry.
	 * 
	 * @param elementName
	 *            the XML element name
	 * @param parent
	 *            the parent in the object structure, or null if this is the
	 *            root element
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 */
	public Entry(String elementName, MappingElement parent)
			throws SysInvalidArgumentException {
		super(elementName, parent);
		logger.debug("entering constructor from parameters");
		initChildren();
		logger.debug("exiting constructor");

	}

	/**
	 * Inits the children. Override this in a subclass, if you want to define
	 * child properties, e.g. addStringProperty("myproperty");
	 * 
	 */
	protected void initChildren() {

	}

	/**
	 * Adds a string property. A new StringElement, representing this property,
	 * will be created and added to the children.
	 * 
	 * The name of the property is also used as the name of the XML-Element.
	 * When parsing an XML-Node, the constructor looks for nodes with this name
	 * and fills a property.
	 * 
	 * This method must be called from the constructor before the nodes are
	 * parsed. A suitable place is the initChildren()-method.
	 * 
	 * @param property
	 *            the property, also the XML-name of the StringElement
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 */
	protected void addStringProperty(String property)
			throws SysInvalidArgumentException {
		if (property == null) {
			String msg = "Cannot add String property for null";
			logger.fatal(msg);
			throw new SysInvalidArgumentException(msg);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Adding String property " + property);
		}
		StringElement stringElement = new StringElement(property, this);

		stringElements.put(property, stringElement);
	}

	/**
	 * Parses a node and fills an Entry from its contents.
	 * 
	 * This constructor first calls initChildren(), so place any code for your
	 * properties there.
	 * 
	 * @param node
	 *            the node, must have been serialized from the same type.
	 * @param parent
	 *            the parent in the object structure, or null if this is the
	 *            root element
	 * 
	 * @throws SysParseException
	 *             when the XML could not be parsed
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 */
	public Entry(Element node, MappingElement parent) throws SysParseException,
			SysInvalidArgumentException {
		super(node, parent);
		logger.debug("entering constructor from XML");

		if (!this.isDeleted()) {
			initChildren();
		} else { // skip processing children, there should be
			// none.
			logger.debug("exiting constructor from XML, was already deleted");
			return;
		}

		Node current = node.getFirstChild();
		while (current != null) {
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				if (stringElements.containsKey(current.getNodeName())) {
					logger.debug("Adding child element for node "
							+ current.getNodeName());

					Element elem = (Element) current;
					StringElement property = new StringElement(elem, this);
					stringElements.put(current.getNodeName(), property);
				}
			}
			current = current.getNextSibling();
		}
		logger.debug("exiting constructor from XML");
	}

	/**
	 * Gets the child by name.
	 * 
	 * @param childname
	 *            the name of the child, as given in addStringProperty().
	 * 
	 * @return the StringElement
	 * 
	 * @throws ElementDeletedException
	 *             when the element has already been deleted
	 */
	public StringElement getStringChildByName(String childname)
			throws ElementDeletedException {
		if (this.isDeleted()) {
			throw new ElementDeletedException(
					"This element has already been deleted " + childname);
		}
		return stringElements.get(childname);

	}

	/**
	 * Serializes this Entry to XML. The resulting XML-Element must still be
	 * added to the appropriate place in the DOM-Tree.
	 * 
	 * Children are serialized as elements with the name of their property.
	 * 
	 * @param doc
	 *            the XML document
	 * 
	 * @return an XML-Element representing this MappingElement (and its
	 *         children)
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#toXml(org.w3c
	 *      .dom.Document)
	 */
	@Override
	public Element toXml(Document doc) throws SysInvalidArgumentException {
		logger.debug("Entering toXml at " + MappingElement.log(this));
		Element elem = super.toXml(doc);

		if (logger.isDebugEnabled()) {
			logger.debug("Going to serialize " + stringElements.size()
					+ " children");
		}

		for (StringElement child : stringElements.values()) {

			elem.appendChild(child.toXml(doc));
			logger.trace("Serialized child " + MappingElement.log(child));
		}

		logger.debug("Exiting toXml");

		return elem;
	}

	/**
	 * Merge this Entry with another Entry. The other Entry must be of the same
	 * type. Both MappingElements should have the same Id, so we can expect to
	 * merge the same Entry.
	 * 
	 * First, the latest Entry is selected. If the entry is not deleted, the
	 * children are merged individually, i.e. the Entry is not considered
	 * atomic. This makes it possible to change two different properties on the
	 * same Entry and see both independent changes in the merged Entry. E.g. if
	 * you have a property contact e-mail and another one called remark, you can
	 * update each of them in different files and not loose your updates.
	 * 
	 * In contrast, if you update the same property the newest one wins. This
	 * however, is design (aka a "feature")
	 * 
	 * @param element
	 *            the other MappingElement
	 * 
	 * @return the merged mapping element
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#merge(de.petranek.syncyoursecrets.xmlmapping.MappingElement)
	 */
	@Override
	public MappingElement merge(MappingElement element)
			throws SysInvalidArgumentException {
		logger.debug("Entering merge: Merge " + MappingElement.log(this)
				+ " with " + MappingElement.log(element));
		if (element instanceof Entry) {
			Entry other = (Entry) element;

			Entry merged = (Entry) super.merge(other);
			if (!merged.isDeleted()) {
				// really merge

				// iterate over the string elements & set the child nodes
				// get the union of all ids
				Set<String> mergedKeySet = computeMergedKeySet(other);

				// iterate over all ids and merge the children found by the id
				for (String key : mergedKeySet) {
					// try to fetch the content from both sets
					StringElement mine = this.stringElements.get(key);
					StringElement thine = other.stringElements.get(key);

					final StringElement mergedChild = mergeChild(mine, thine);
					mergedChild.setParent(merged);
					merged.stringElements.put(key, mergedChild);
				}

			} else if (logger.isTraceEnabled()) {
				logger.trace("Merged object already deleted, no further merge");
			}

			logger.debug("Exiting merge, resulting in "
					+ MappingElement.log(merged));
			return merged;
		} else {
			throw new SysInvalidArgumentException("Cannot merge "
					+ this.getClass() + " with " + element.getClass());
		}

	}

	/**
	 * Merge the two child nodes.
	 * 
	 * First check, if the nodes are present in both parents. In this case, we
	 * delegate the merge to the two child nodes. Otherwise, if only one node is
	 * present, we select this one.
	 * 
	 * 
	 * @param mine
	 *            the mine
	 * @param thine
	 *            the thine
	 * 
	 * @return the string element
	 * 
	 * @throws SysInvalidArgumentException
	 *             the sys invalid argument exception
	 */
	private StringElement mergeChild(StringElement mine, StringElement thine)
			throws SysInvalidArgumentException {
		logger.trace("Merging child nodes " + MappingElement.log(mine)
				+ " and " + MappingElement.log(thine));
		final StringElement mergedChild;
		if (mine == null) {

			if (logger.isTraceEnabled()) {
				logger.trace("Node only present in other, selecting "
						+ MappingElement.log(thine));
			}
			mergedChild = thine;

		} else if (thine == null) {

			if (logger.isTraceEnabled()) {
				logger.trace("Node only present in this node, selecting "
						+ MappingElement.log(mine));
			}
			mergedChild = mine;

		} else { // neither is null

			mergedChild = (StringElement) mine.merge(thine);
			if (logger.isTraceEnabled()) {
				logger.trace("Node present in both, selecting "
						+ MappingElement.log(mergedChild));
			}
		}
		return mergedChild;
	}

	/**
	 * Compute the set of keys (i.e. Ids) of the merged set.
	 * 
	 * In short, the merged entry will contain the union of both entry sets.
	 * 
	 * @param other
	 *            the other
	 * 
	 * @return the set< string>
	 */
	private Set<String> computeMergedKeySet(Entry other) {
		Set<String> mergedKeySet = new HashSet<String>();
		mergedKeySet.addAll(this.stringElements.keySet());
		mergedKeySet.addAll(other.stringElements.keySet());

		if (logger.isTraceEnabled()) {
			logger.trace("Merged key set contains " + mergedKeySet.size()
					+ " child nodes");
		}
		return mergedKeySet;
	}

	/**
	 * Delete this Entry. Note: The Entry will only be marked as deleted.
	 * However, all content and child nodes will be deleted.
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#delete()
	 */
	@Override
	public void delete() {
		super.delete();
		this.stringElements.clear();
	}

}
