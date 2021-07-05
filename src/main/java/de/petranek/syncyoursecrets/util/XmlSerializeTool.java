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
package de.petranek.syncyoursecrets.util;

import de.petranek.syncyoursecrets.io.FileHelper;
import de.petranek.syncyoursecrets.io.FileIOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * The Class XmlSerializeTool handles serialization and de-serialization between
 * XML-Documents and DOM-Trees.
 *
 * The XML-Documents can be simple Strings, XML-Files or XML-Files encrypted
 * with our EncryptionUtil.
 *
 * @author Jan Petranek
 */
public final class XmlSerializeTool {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(XmlSerializeTool.class);

	/**
	 * Utility class, shall not be instantiated.
	 */
	private XmlSerializeTool() {
		super();
	}

	/**
	 * Read an encrypted file and parse the contained XML document.
	 *
	 * @param password
	 *            the password
	 * @param file
	 *            the file
	 *
	 * @return the decrypted document
	 *
	 * @throws SysXmlBaseException
	 *             when the operation fails
	 */
	public static Document readEncryptedFile(String password, File file)
			throws SysXmlBaseException {

		logger.debug("entering readEncryptedFile");

		String plaintext = readEncryptedFileContent(password, file);

		return readString(plaintext);

	}

	/**
	 * Read an encrypted file and return the content as String.
	 *
	 * @param password the password for decryption
	 * @param file the file to read
	 * @return the decrypted content
	 * @throws FileIOException
	 *             when the file cannot be read
	 * @throws SysCryptoException
	 *             when the operation fails
	 */
	public static String readEncryptedFileContent(String password, File file)
			throws FileIOException, SysCryptoException {
		logger.debug("entering readEncryptedFile");
		String cypher = FileHelper.readStringFromFile(file);
		String plaintext = EnryptionUtil.decryptString(cypher, password);
		logger.debug("exiting readEncryptedFile");
		return plaintext;
	}

	/**
	 * Write encrypted document to a file.
	 *
	 * @param password
	 *            the password
	 * @param doc
	 *            the document
	 * @param file
	 *            the file
	 *
	 * @throws SysXmlBaseException
	 *             when the operation fails
	 */
	public static void writeEncryptedDocument(String password, Document doc,
			File file) throws SysXmlBaseException {
		logger.debug("entering writeEncryptedDocument");

		String plaintext = writeString(doc);
		writeEncryptedString(password, file, plaintext);

		logger.debug("exiting writeEncryptedDocument");
	}

	/**
	 * Encrypt the given plaintext and write it into the given file.
	 *
	 * @param password the password for encryption
	 * @param file the file to write to
	 * @param plaintext the plaintext to encrypt and save
	 * @throws SysCryptoException when the encryption fails
	 * @throws FileIOException when the file access fails
	 */
	public static void writeEncryptedString(String password, File file,
			String plaintext) throws SysCryptoException, FileIOException {
		String cypher = EnryptionUtil.encryptString(plaintext, password);
		FileHelper.writeStringToFile(cypher, file);
	}

	/**
	 * Parse a document from a string.
	 *
	 * @param xmlString
	 *            the s
	 *
	 * @return the document
	 *
	 *         the parser configuration exception
	 * @throws SysXmlBaseException
	 *             when the operation fails
	 */
	public static Document readString(String xmlString)
			throws SysXmlBaseException {
		logger.debug("entering readString");
		InputSource source = new InputSource(new StringReader(xmlString));
		Document doc;
		try {

			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(source);

			logger.debug("exiting readString");
			return doc;

		} catch (SAXException e) {
			String msg = "Failed to parse XML";
			logger.warn(msg, e);
			throw new SysParseException(msg, e);
		} catch (IOException e) {
			String msg = "Failed to parse XML within encrypted file";
			logger.warn(msg, e);
			throw new SysParseException(msg, e);
		} catch (ParserConfigurationException e) {
			String msg = "XML parser is ill-configured, this is a serious issue";
			logger.error(msg, e);
			throw new SysParseException(msg, e);
		} catch (IllegalArgumentException e) {
			String msg = ("Illegal invocation of the xml parser");

			logger.error(msg + " source argument was " + source, e);
			throw new SysParseException(msg, e);
		}

	}

	/**
	 * Parse an XML file and return the document.
	 *
	 * @param file
	 *            the file
	 *
	 * @return the document
	 *
	 * @throws SysXmlBaseException
	 *             when the operation fails
	 */
	public static Document readFile(File file) throws SysXmlBaseException {
		logger.debug("entering readFile");

		try {
			Document doc;
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(file);
			logger.debug("exiting readFile");
			return doc;

		} catch (SAXException e) {
			String msg = "Failed to parse XML within encrypted file";
			logger.warn(msg, e);
			throw new SysParseException(msg, e);
		} catch (IOException e) {
			String msg = "Failed to parse XML within encrypted file";
			logger.warn(msg, e);
			throw new SysParseException(msg, e);
		} catch (ParserConfigurationException e) {
			String msg = "XML parser is ill-configured, this is a serious issue";
			logger.error(msg, e);
			throw new SysParseException(msg, e);
		} catch (IllegalArgumentException e) {
			String msg = ("Illegal invocation of the xml parser");

			logger.error(msg + " source argument was " + file, e);
			throw new SysParseException(msg, e);
		}

	}

	/**
	 * Write a document to a file.
	 *
	 * @param doc
	 *            the document
	 * @param file
	 *            the file to write to
	 *
	 * @throws SysXmlBaseException
	 *             when the operation fails
	 */
	public static void writeFile(Document doc, File file)
			throws SysXmlBaseException {
		logger.debug("entering writeFile");

		if (file == null) {
			String msg = ("File may not be null");
			throw new SysXmlBaseException(msg);
		}

		StreamResult result = new StreamResult(file);

		writeDocument(doc, result);
		logger.debug("exiting writeFile");

	}

	/**
	 * Write the document to the StreamResult.
	 *
	 * @param doc
	 *            the doc
	 * @param result
	 *            the result
	 *
	 * @throws SysXmlBaseException
	 *             when the operation fails
	 */
	private static void writeDocument(Document doc, StreamResult result)
			throws SysXmlBaseException {
		logger.debug("entering readEncryptedFile");

		try {
			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();

			DOMSource source = new DOMSource(doc);

			transformer.transform(source, result);

			logger.debug("exiting ");

		} catch (TransformerConfigurationException e) {
			String msg = "Writing XML failed";
			logger.warn(msg, e);
			throw new SysParseException(msg, e);

		} catch (TransformerFactoryConfigurationError e) {
			String msg = "Writing XML failed";
			logger.warn(msg, e);
			throw new SysParseException(msg, e);

		} catch (TransformerException e) {
			String msg = "Writing XML failed";
			logger.warn(msg, e);
			throw new SysParseException(msg, e);

		}

	}

	/**
	 * Serialize the document to a String.
	 *
	 * @param doc
	 *            the doc
	 *
	 * @return the string
	 *
	 * @throws SysXmlBaseException
	 *             when the operation fails
	 */
	public static String writeString(Document doc) throws SysXmlBaseException {
		logger.debug("entering writeString");
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);

		writeDocument(doc, result);
		logger.debug("exiting writeString");
		return writer.toString();

	}

	/**
	 * Creates a new, empty document.
	 *
	 * @return the document
	 *
	 * @throws SysXmlBaseException
	 *             when the operation fails
	 */
	public static Document createDocument() throws SysXmlBaseException {
		logger.debug("entering createDocument");
		try {
			DocumentBuilderFactory docBFac;
			DocumentBuilder docBuild;
			docBFac = DocumentBuilderFactory.newInstance();
			docBuild = docBFac.newDocumentBuilder();
			Document doc = docBuild.newDocument();
			logger.debug("exiting createDocument");
			return doc;

		} catch (ParserConfigurationException e) {
			String msg = "XML parser is ill-configured, this is a serious issue";
			logger.error(msg, e);
			throw new SysParseException(msg, e);
		}
	}
}
