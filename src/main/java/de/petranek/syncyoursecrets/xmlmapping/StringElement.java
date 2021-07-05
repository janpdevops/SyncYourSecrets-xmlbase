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

/**
 * The Class StringElement is a simple MappingElement. Its content is a String
 * (or a textnode in XML-terms).
 * 
 * @author Jan Petranek
 */
public class StringElement extends MappingElement {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(StringElement.class);

	/** The content. */
	private String content = "";

	/**
	 * Instantiates a new (empty) string element.
	 * 
	 * @param elementName
	 *            the name of the XML-element
	 * @param parent
	 *            the parent in the object structure, or null if this is the
	 *            root element
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 */
	public StringElement(String elementName, MappingElement parent)
			throws SysInvalidArgumentException {
		super(elementName, parent);
	}

	/**
	 * Parses a node and creates a new string element. The new StringElement
	 * will be filled with the values from the node.
	 * 
	 * @param node
	 *            the node
	 * @param parent
	 *            the parent in the object structure, or null if this is the
	 *            root element
	 * @throws SysParseException
	 *             when the XML could not be parsed
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 */
	public StringElement(final Element node, MappingElement parent)
			throws SysParseException, SysInvalidArgumentException {
		super(node, parent);

		logger.debug("entering constructor from Xml");
		this.content = (node.getTextContent());
		if (this.content == null) {

			logger.debug("No content set");
			this.content = "";
		}
		logger.debug("exiting constructor from Xml");
	}

	/**
	 * Merge with another StringElement. StringElements have only one property
	 * (the content) and are thus considered atomic. The merge will simply
	 * return the newest of the two StringElements.
	 * 
	 * @param other
	 *            the StringElement to merge with.
	 * @return the most recently updated StringElement
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#merge(de.petranek.syncyoursecrets.xmlmapping.MappingElement)
	 */
	@Override
	public MappingElement merge(MappingElement other)
			throws SysInvalidArgumentException {
		// simply, the newest one wins.
		logger.debug("Merging " + MappingElement.log(this) + " with "
				+ MappingElement.log(other));
		return getNewest(other);
	}

	/**
	 * Gets the content.
	 * 
	 * @return the content or an empty String
	 */
	public String getContent() {
		return (content != null ? content : "");
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            the new content
	 * @throws ElementDeletedException
	 *             when the argument was invalid
	 */
	public void setContent(String content) throws ElementDeletedException {
		setContent(content, false);
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            the new content
	 * @param skipUpdate
	 *            TODO
	 * @throws ElementDeletedException
	 *             when the argument was invalid
	 */
	public void setContent(String content, boolean skipUpdate)
			throws ElementDeletedException {
		// make sure, deleted entries are not modified post mortem.
		if (this.isDeleted()) {
			String msg = "Already deleted, cannot set content on "
					+ MappingElement.log(this);
			logger.error(msg);
			throw new ElementDeletedException(msg);
		}

		// suppress modify of unchanged content
		if (!this.content.equals(content)) {
			logger.trace("Content modified");
			this.content = content;
			if (this.content == null) {
				this.content = "";
			}

			if (skipUpdate) {
				logger.trace("Skip firing modify event");
			} else {
				this.modify();
			}
		} else if (logger.isTraceEnabled()) {
			logger.trace("Content remained unchanged in setContent");
		}
	}

	/**
	 * Serializes this to an XML-Element.
	 * 
	 * The content will be inserted as a child textnode.
	 * 
	 * @param doc
	 *            document to insert into
	 * @return Element representing this StringElement.
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#toXml(org.w3c
	 *      .dom.Document)
	 */
	@Override
	public Element toXml(Document doc) throws SysInvalidArgumentException {
		logger.debug("Entering serialize to XML for "
				+ MappingElement.log(this));
		Element node = super.toXml(doc);
		Node textNode = doc.createTextNode(this.getContent());
		node.appendChild(textNode);

		logger.debug("Exiting serialize to XML");
		return node;
	}

	/**
	 * Mark this as deleted. The content will be deleted.
	 * 
	 * @see de.petranek.syncyoursecrets.xmlmapping.MappingElement#delete()
	 */
	@Override
	public void delete() {
		this.content = "";

		// must go last, otherwise the update action will be triggered.
		super.delete();

	}

}
