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

import de.petranek.syncyoursecrets.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;

import java.time.ZonedDateTime;

/**
 * The Class MappingElement is the basic class for objects in SyncYourSecrets.
 * 
 * Create Upon creation, the create and modification timestamps are set.
 * 
 * Update The modification timestamp is set. The lastOperation now is "UPDATE"
 * 
 * Delete The modification timestamp is set. The lastOperation now is "DELETE".
 * Its contents will be deleted, the node is merely a placeholder for the
 * delete-action.
 * 
 * A MappingElement can be merged with another MappingElement of the same type.
 * The basic algorithm is as follows: Select the MappingElement, that was most
 * recently updated. If the last recently updated MappingElement was not
 * deleted, then merge all child properties recursively.
 * 
 * The merge behaviour makes sure, a deletion will be propagated to the merged
 * file, if the deletion was the latest action. If, however a node has been
 * deleted in file A, and later it has been updated in file B, then the updated
 * node will be the result of the merge. This ensures that the latest action
 * will not be lost.
 * 
 * @author Jan Petranek
 */
public class MappingElement implements Comparable<MappingElement> {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(MappingElement.class);

	/** The Constant LEGACY_VERSION_1. */
	public static final int LEGACY_VERSION_1 = 1;

	/** The Constant CURRENT_VERSION. */
	public static final int CURRENT_VERSION = 2;

	/** The Constant VERSION denotes the corresponding XML-Attribute. */
	private static final String VERSION = "version";

	/** The Constant LAST_ACTION denotes the corresponding XML-Attribute. */
	private static final String LAST_ACTION = "lastAction";

	/** The Constant ID denotes the corresponding XML-Attribute. */
	private static final String ID = "id";

	/** The Constant LAST_MODIFIED denotes the corresponding XML-Attribute. */
	private static final String LAST_MODIFIED = "lastModified";

	/** The Constant CREATED denotes the corresponding XML-Attribute. */
	private static final String CREATED = "created";

	/** The Constant NAME denotes the corresponding XML-Attribute. */
	private static final String NAME = "name";

	/**
	 * The parent node in the object tree. If this is null, we are the root of
	 * the object tree.
	 */
	private MappingElement parent;

	/**
	 * The version of the XML structure that has been read. 0 not initialized 1
	 * has been used up to Version 0.1.3 2 will be the future released version.
	 * */
	private int version = 0;

	/**
	 * The version of the XML structure. 1 has been used up to Version 0.1.3
	 * (default) 2 will be the future released version.
	 * 
	 * @return the version
	 */
	public int getVersion() {
		if (this.parent != null) {
			return parent.getVersion();
		}
		if (version < LEGACY_VERSION_1) {
			return LEGACY_VERSION_1;
		}
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * The Enumeration of possible last ACTIONS.
	 */
	public enum ACTIONS {

		/** The last action was a CREATE. */
		CREATE,

		/** The last action was an UPDATE. */
		UPDATE,

		/** The last action was a DELETE. */
		DELETE
	};

	/** The timestamp of the creation. */
	private ZonedDateTime created;

	/** The timestamp of the last modification. */
	private ZonedDateTime lastModified;

	/**
	 * The id of this MappingElement. The Id has to be unique within a document.
	 */
	private long id;

	/** The last action. */
	private ACTIONS lastAction;

	/** The element name in the XML-document. */
	private String elementName;

	/** The attribute name. This attribute is optional and may be null. */
	private String name;

	/**
	 * Instantiates a new, empty mapping element.
	 * 
	 * @param elementName
	 *            the element name, as it will appear in the XML
	 * @param parent
	 *            the parent in the object structure, or null if this is the
	 *            root element
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the input was invalid
	 */
	public MappingElement(String elementName, MappingElement parent)
			throws SysInvalidArgumentException {
		super();

		logger.debug("entering constructor");

		this.setElementName(elementName);
		this.createNow();
		this.parent = parent;
		this.version = CURRENT_VERSION;

		logger.debug("exiting constructor");
	}

	/**
	 * Parses an Element and fills a MappingElement from its contents.
	 * 
	 * @param node
	 *            the node, must have been serialized from the same type.
	 * @param parent
	 *            the parent in the object structure, or null if this is the
	 *            root element
	 * 
	 * @throws SysParseException
	 *             when the XML could not be processed
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 */
	public MappingElement(Element node, MappingElement parent)
			throws SysParseException, SysInvalidArgumentException {
		this(node.getNodeName(), parent);

		version = LEGACY_VERSION_1; // default to this, when no version is
		// found.
		logger.debug("entering Constructor from XML Element");

		try {

			NamedNodeMap attributes = node.getAttributes();

			this.setCreated(DateTimeUtil.parseDateTime(attributes.getNamedItem(
					CREATED).getNodeValue()));
			this.setLastModified(DateTimeUtil.parseDateTime(attributes
					.getNamedItem(LAST_MODIFIED).getNodeValue()));
			this.setId(Long.parseLong(attributes.getNamedItem(ID)
					.getNodeValue()));
			this.setLastAction(ACTIONS.valueOf(attributes.getNamedItem(
					LAST_ACTION).getNodeValue()));

			// check for a version > 1
			Node versionAttribute = attributes.getNamedItem(VERSION);
			if (versionAttribute != null) {
				this.version = Integer
						.parseInt(versionAttribute.getNodeValue());
				logger.debug("Version is " + this.version);
			}

			// we are still version 1
			if (getVersion() == LEGACY_VERSION_1) {
				parseLegacyName(attributes);
			}

		} catch (DOMException domex) {
			String msg = "Failed to traverse XML";
			logger.error(msg, domex);
			throw new SysParseException(msg, domex);
		} catch (NumberFormatException nfe) {
			String msg = "Version cannot be parsed as number";
			logger.error(msg, nfe);
			throw new SysParseException(msg, nfe);
		}
		logger.debug("exiting Constructor from XML Element");

	}

	/**
	 * Parses the legacy name. Search for an attribute called "name" and store
	 * it as our legacy name. In XML-Version 1, this was the way to store a name
	 * property.
	 * 
	 * 
	 * @param attributes
	 *            the XML attributes
	 */
	private void parseLegacyName(NamedNodeMap attributes) {
		Node nameAttribute = attributes.getNamedItem(NAME);
		// name attribute is optional, even in version 1
		if (nameAttribute != null) {
			logger.trace("Version 1 Name attribute found for node "
					+ MappingElement.log(this));
			this.name = nameAttribute.getNodeValue();
		} else {

			this.name = null;
			logger.trace("Version 1 Name attribute empty for node "
					+ MappingElement.log(this));
		}
	}

	/**
	 * Merge this MappingElement with another MappingElement. The other
	 * MappingElement must be of the same type. Both MappingElements should have
	 * the same Id, so we can expect to merge the same MappingElement.
	 * 
	 * @param other
	 *            the other MappingElement
	 * 
	 * @return the merged mapping element
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 */
	public MappingElement merge(MappingElement other)
			throws SysInvalidArgumentException {
		return getNewest(other);
	}

	/**
	 * Gets the most recently updated MappingElement.
	 * 
	 * @param other
	 *            the other MappingElement to compare it with
	 * 
	 * @return the most recently updated Mapping Element
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 */
	protected MappingElement getNewest(MappingElement other)
			throws SysInvalidArgumentException {
		logger.trace("entering getNewest");
		if (other == null) {
			String msg = ("Cannot compare timesamps with null object");
			logger.fatal(msg);
			throw new SysInvalidArgumentException(msg);
		}

		if (this.getLastModified().compareTo(other.getLastModified()) >= 1) {
			logger.trace("This is newer " + this.getLastModified() + " than "
					+ other.getLastModified());
			logger.trace("exiting getNewest");
			return this;
		} else {
			logger.trace("This is older " + this.getLastModified() + " than "
					+ other.getLastModified());
			logger.trace("exiting getNewest");
			return other;
		}
	}

	/**
	 * Call this method upon creation. Sets the corresponding attributes.
	 */
	protected void createNow() {
		logger.trace("entering createNow");

		try {
			this.setLastAction(ACTIONS.CREATE);

			this.setCreated(ZonedDateTime.now());
			this.setLastModified(this.getCreated());
			this.setId(createUid());
		} catch (SysInvalidArgumentException ex) {
			String msg = "Cannot execute createNow, this is a bug";
			logger.fatal(msg, ex);
			throw new SysRuntimeException(msg, ex);
		}
		logger.trace("exiting createNow");
	}

	/**
	 * Creates a pseudo-unique id Note: if necessary, replace this by a real uid
	 * function.
	 * 
	 * @return the long
	 */
	protected long createUid() {
		logger.trace("entering createUid");

		// long now = System.currentTimeMillis();
		double random = Math.random() * Long.MAX_VALUE;
		long uid = Math.round(random);

		logger.trace("exiting createUid");
		return uid;

	}

	/**
	 * Delete this MappingElement. Note: The MappingElement will only be marked
	 * as deleted. However, all content and child nodes will be deleted.
	 */
	public void delete() {
		logger.debug("entering delete for node " + MappingElement.log(this));

		try {
			this.setLastAction(ACTIONS.DELETE);
			this.setLastModified(ZonedDateTime.now());
			this.name = null;

			if (this.parent != null) {
				this.parent.modify();
			}
		} catch (SysInvalidArgumentException ex) {
			String msg = "Cannot execute delete, this is a bug";
			logger.fatal(msg, ex);
			throw new SysRuntimeException(msg, ex);
		} catch (ElementDeletedException ex) {
			String msg = "Parent already deleted, cannot execute delete, this is a bug";
			logger.fatal(msg, ex);
			throw new SysRuntimeException(msg, ex);
		}
		logger.debug("exiting delete");
	}

	/**
	 * Call this method, when the MappingElement has been updated.
	 * 
	 * @throws ElementDeletedException
	 *             when the element was already deleted
	 */
	protected void modify() throws ElementDeletedException {
		logger.trace("entering modify");
		// make sure, deleted entries are not modified post mortem.
		if (this.isDeleted()) {

			String msg = "Node " + MappingElement.log(this)
					+ " already deleted, cannot modify";
			logger.error(msg);
			throw new ElementDeletedException(msg);
		}

		try {

			this.setLastAction(ACTIONS.UPDATE);
			this.setLastModified( ZonedDateTime.now());

			// propagate modification to parent node.
			if (this.parent != null) {
				parent.modify();
			}

		} catch (SysInvalidArgumentException ex) {
			String msg = "Cannot execute modify, this is a bug";
			logger.fatal(msg, ex);
			throw new SysRuntimeException(msg, ex);
		}

		logger.trace("exiting modify");
	}

	/**
	 * Gets the creation timestamp.
	 * 
	 * @return the creation timestamp
	 */
	public ZonedDateTime getCreated() {
		return created;
	}

	/**
	 * Sets the creation timestamp.
	 * 
	 * @param created
	 *            the new creation timestamp.
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 */
	public void setCreated(ZonedDateTime created) throws SysInvalidArgumentException {
		if (created == null) {
			String msg = ("Cannot set creation time to null");
			logger.error(msg);
			throw new SysInvalidArgumentException(msg);
		}
		this.created = created;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the last action.
	 * 
	 * @return the last action
	 */
	public ACTIONS getLastAction() {
		return lastAction;
	}

	/**
	 * Sets the last action.
	 * 
	 * @param lastAction
	 *            the new last action
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 */
	public void setLastAction(ACTIONS lastAction)
			throws SysInvalidArgumentException {
		if (lastAction == null) {
			String msg = ("Cannot set lastAction to null");
			logger.error(msg);
			throw new SysInvalidArgumentException(msg);
		}
		this.lastAction = lastAction;
	}

	/**
	 * Gets the last modification timestamp.
	 * 
	 * @return the last modification timestamp
	 */
	public ZonedDateTime getLastModified() {
		return lastModified;
	}

	/**
	 * Sets the last modification timestamp.
	 * 
	 * @param lastModified
	 *            the new last modification timestamp
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 */
	public void setLastModified(ZonedDateTime lastModified)
			throws SysInvalidArgumentException {
		if (lastModified == null) {
			String msg = ("Cannot set last modification timestamp to null");
			logger.error(msg);
			throw new SysInvalidArgumentException(msg);
		}
		this.lastModified = lastModified;
	}

	/**
	 * Gets the element name.
	 * 
	 * @return the element name
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * Sets the element name.
	 * 
	 * @param name
	 *            the new element name
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the argument was invalid
	 */
	public void setElementName(String name) throws SysInvalidArgumentException {
		if (StringUtil.isEmpty(name)) {
			String msg = ("Cannot set XML element name to empty or null");
			logger.error(msg);
			throw new SysInvalidArgumentException(msg);
		}

		this.elementName = name;
	}

	/**
	 * Serializes this MappingElement to XML. The resulting XML-Element must
	 * still be added to the appropriate place in the DOM-Tree.
	 * 
	 * @param doc
	 *            the XML document
	 * 
	 * @return an XML-Element representing this MappingElement (and its
	 *         children)
	 * 
	 * @throws SysInvalidArgumentException
	 *             when the document could not be used
	 */
	public Element toXml(Document doc) throws SysInvalidArgumentException {
		logger.debug("entering toXml at " + MappingElement.log(this));
		if (doc == null) {
			String msg = ("Cannot serialize to XML, document was null");
			logger.error(msg);
			throw new SysInvalidArgumentException(msg);
		}
		Element node = doc.createElement(this.getElementName());

		String created = DateTimeUtil.dateTime2String(this.getCreated());
		property2Attribute(CREATED, created, node);

		String lastModified = DateTimeUtil.dateTime2String(this
				.getLastModified());
		property2Attribute(LAST_MODIFIED, lastModified, node);

		property2Attribute(ID, Long.toString(this.getId()), node);

		property2Attribute(LAST_ACTION, this.getLastAction().name(), node);

		// only write version at top element
		if (isRootElement()) {
			// we always write the current version, no regards to the parsed
			// version.
			property2Attribute(VERSION, String.valueOf(CURRENT_VERSION), node);
		}

		logger.debug("exiting toXml at " + MappingElement.log(this));
		return node;
	}

	/**
	 * We are the root element in the object hierarchy, if we have no parent.
	 * 
	 * @return true, if this is the root element.
	 */
	public boolean isRootElement() {
		return this.parent == null;
	}

	/**
	 * Set an attribute on the given node.
	 * 
	 * @param attribName
	 *            the name of the attribute
	 * @param value
	 *            the value of the attribute
	 * @param node
	 *            the node
	 */
	protected static void property2Attribute(String attribName, String value,
			Element node) {
		node.setAttribute(attribName, value);
	}

	/**
	 * Gets the name. Override this in a subclass, if desired
	 * 
	 * @return the name
	 */
	public String getName() {
		return null;
	}

	/**
	 * Gets the legacy name.
	 * 
	 * @return the name
	 */
	public String getLegacyName() {
		return name;
	}

	/**
	 * Computes the hash-code.
	 * 
	 * Code generated by eclipse, no need to modify.
	 * 
	 * @return a hash code for this object.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (getId() ^ (getId() >>> 32));
		return result;
	}

	/**
	 * Checks, if two objects are equal. Equality is based on the id.
	 * 
	 * Code generated by eclipse, no need to modify.
	 * 
	 * @param obj
	 *            The object to compare with.
	 * 
	 * @return true, if the ids are equal.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MappingElement other = (MappingElement) obj;
		if (getId() != other.getId()) {
			return false;
		}
		return true;
	}

	/**
	 * Compares this MappingElement to the other MappingElement. The ordering is
	 * based on the id.
	 * 
	 * @param other
	 *            the MappingElement to compare with
	 * 
	 * @return -1, if this &lt; other; 0 if this == other; 1 if this &gt; other
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MappingElement other) {
		return (this.getId() < other.getId() ? -1 : (this.getId() == other
				.getId() ? 0 : 1));
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
		return String.valueOf(this.getId());
	}

	/**
	 * Gives a String representation of this object that can be logged.
	 * IMPLEMENTATIONS OF THIS METHOD MUST NOT PRESENT SENSITIVE DATA. The
	 * result of the toString method is used in the log, so if you don't want
	 * your secret passwords appearing in the logs, only write structural
	 * information.
	 * 
	 * @return A String representation of this object.
	 * 
	 * @see java.lang.Object#toString()
	 */
	private String toLogString() {
		return "[" + this.getElementName() + "]: " + this.getId();
	}

	/**
	 * Checks if this MappingElement is deleted.
	 * 
	 * @return true, if is deleted
	 */
	public boolean isDeleted() {

		return this.getLastAction().equals(MappingElement.ACTIONS.DELETE);
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent or null, if this is the root of the object hierarchy.
	 */
	public MappingElement getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            the parent to set or null, if this is the root element.
	 */
	public void setParent(MappingElement parent) {
		this.parent = parent;
	}

	public static String log(MappingElement element) {
		if (element == null) {
			return "null";
		}
		return element.toLogString();

	}

}
