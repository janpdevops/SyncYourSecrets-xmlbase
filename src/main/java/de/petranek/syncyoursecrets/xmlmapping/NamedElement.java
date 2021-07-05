/**
 * SyncYourSecrets-pwmodel ties the generic MappingElements from the
 * xmlbase to a concrete structure, suitable for password entries.
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
package de.petranek.syncyoursecrets.xmlmapping;

import de.petranek.syncyoursecrets.util.SysInvalidArgumentException;
import de.petranek.syncyoursecrets.util.SysParseException;
import de.petranek.syncyoursecrets.util.SysRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The Class NamedElement is an XML Mapping Element, that has a name property.
 * The name is stored as a child node. This allows us to modify the name
 * independently from other properties.
 * 
 * @author Jan Petranek
 */
public class NamedElement extends MappingElement {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(NamedElement.class);

	/** The Constant for the property NAME. */
	public static final String NAME = "name";

	/** The name property, added as a child element. */
	private StringElement nameProperty;

	/**
	 * Creates a NamedElement from an XML node.
	 * 
	 * @param node
	 *            the node
	 * @param parent
	 *            the parent in the object structure, or null if this is the
	 *            root element
	 * 
	 * @throws SysParseException
	 *             the sys parse exception
	 * @throws SysInvalidArgumentException
	 *             the sys invalid argument exception
	 */
	public NamedElement(Element node, MappingElement parent)
			throws SysParseException, SysInvalidArgumentException {
		super(node, parent);

		if (getLegacyName() != null) {

			parseLegacyName();
		} else {
			parseName(node);
		}

	}

	/**
	 * Parse the name as a child Element, this is the default since Version 2.
	 * 
	 * @param node
	 *            the node
	 * 
	 * @throws SysParseException
	 *             the sys parse exception
	 * @throws SysInvalidArgumentException
	 *             the sys invalid argument exception
	 */
	private void parseName(Element node) throws SysParseException,
			SysInvalidArgumentException {
		// find name property among children
		Node current = node.getFirstChild();
		while (current != null) {
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				if (NAME.equals(current.getNodeName())) {
					logger.debug("Found name property");

					Element elem = (Element) current;

					nameProperty = new StringElement(elem, this);
				}

			}
			current = current.getNextSibling();
		}
	}

	/**
	 * Take the legacy name (parsed in the MappingElement) and create a new
	 * nameProperty with useful values, esp. the name value inherits its
	 * timestamps from this node.
	 * 
	 * @throws SysInvalidArgumentException
	 *             the sys invalid argument exception
	 */
	private void parseLegacyName() throws SysInvalidArgumentException {
		// convert legacy name to property
		try {
			nameProperty = new StringElement(NAME, this);

			// don't trigger update on the parent (this).
			nameProperty.setContent(getLegacyName(), true);
			nameProperty.setCreated(this.getCreated());
			nameProperty.setLastAction(this.getLastAction());
			nameProperty.setLastModified(this.getLastModified());
		} catch (ElementDeletedException ex) {
			String msg = "Cannot set legacy name";
			logger.error(msg, ex);
			throw new SysRuntimeException(msg, ex);
		}
	}

	/**
	 * The Constructor.
	 * 
	 * @param elementName
	 *            the element name
	 * @param parent
	 *            the parent
	 * 
	 * @throws SysInvalidArgumentException
	 *             the sys invalid argument exception
	 */
	public NamedElement(String elementName, MappingElement parent)
			throws SysInvalidArgumentException {
		super(elementName, parent);
		nameProperty = new StringElement(NAME, this);

	}

	/**
	 * Serialize this and the name node to XML.
	 * 
	 * @param doc
	 *            the doc
	 * 
	 * @return the element
	 * 
	 * @throws SysInvalidArgumentException
	 *             the sys invalid argument exception
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#toXml(org.w3c.dom.Document)
	 */
	@Override
	public Element toXml(Document doc) throws SysInvalidArgumentException {
		logger.debug("Entering toXml at " + MappingElement.log(this));
		Element elem = super.toXml(doc);

		if (nameProperty != null) {
			elem.appendChild(nameProperty.toXml(doc));
			logger.trace("Serialized child " + MappingElement.log(nameProperty));

		}
		logger.debug("Exiting toXml");

		return elem;
	}

	/**
	 * Get this node's name.
	 * 
	 * @return the name
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#getName()
	 */
	public String getName() {
		if (nameProperty == null) {
			return null;
		}
		return nameProperty.getContent();
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name
	 * @param skipUpdate
	 *            the skip update
	 * 
	 * @throws ElementDeletedException
	 *             the element deleted exception
	 */
	public void setName(String name, boolean skipUpdate)
			throws ElementDeletedException {
		nameProperty.setContent(name, skipUpdate);
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 * 
	 * @throws ElementDeletedException
	 *             the element deleted exception
	 */
	public void setName(String name) throws ElementDeletedException {
		setName(name, false);
	}

	/**
	 * Delete this node.
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#delete()
	 */
	@Override
	public void delete() {
		super.delete();

		this.nameProperty = null;
	}

	/**
	 * Gets the name property.
	 * 
	 * @return the nameProperty
	 */
	protected StringElement getNameProperty() {
		return nameProperty;
	}

	/**
	 * Sets the name property.
	 * 
	 * @param nameProperty
	 *            the nameProperty to set
	 */
	protected void setNameProperty(StringElement nameProperty) {
		this.nameProperty = nameProperty;
	}

	/**
	 * Merge this with other NamedElement. This also merges the name property.
	 * 
	 * @param other
	 *            the other element to merge with
	 * 
	 * @return the merged mapping element
	 * 
	 * @throws SysInvalidArgumentException
	 *             the sys invalid argument exception
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#merge(de.petranek.syncyoursecrets.xmlmapping.MappingElement)
	 */
	public MappingElement merge(MappingElement other)
			throws SysInvalidArgumentException {

		NamedElement otherNamed = (NamedElement) other;

		NamedElement merged = (NamedElement) super.merge(otherNamed);

		StringElement mergedName = null;
		if (this.nameProperty != null && otherNamed.getNameProperty() != null) {
			mergedName = (StringElement) this.nameProperty.merge(otherNamed
					.getNameProperty());
			merged.setNameProperty(mergedName);
		} // no else case needed, the merged node will have it's original child.

		return merged;
	}

	/**
	 * Gives a String representation of this object meaningful to the user.
	 * IMPLEMENTATIONS OF THIS METHOD CONTAIN SENSITIVE DATA. DO NOT LOG THIS!
	 * 
	 * @return A String representation of this object.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

}
