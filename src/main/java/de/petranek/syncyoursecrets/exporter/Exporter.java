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
package de.petranek.syncyoursecrets.exporter;

import de.petranek.syncyoursecrets.io.FileHelper;
import de.petranek.syncyoursecrets.io.FileIOException;
import de.petranek.syncyoursecrets.util.SysCryptoException;
import de.petranek.syncyoursecrets.util.XmlSerializeTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * The Class Exporter uses the same encryption/decryption algorithms as the
 * SyncYourSecrets application.
 * 
 * In decryption mode, it reads in an encrypted file and writes the plaintext to
 * a file. This might come in handy as a rescue method, should the XML or the
 * data within become corrupted.
 * 
 * Encryption mode encrypts a file with the password.
 * 
 * Note that I don't advise manipulating a SyncYourSecrets file manually.
 * 
 * @author Jan Petranek
 */
public final class Exporter {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(Exporter.class);

	/**
	 * Private constructor, utility class.
	 */
	private Exporter() {
		super();
		// nothing to do
	}

	/**
	 * Encrypt or decrypt a file and copy the results to the destination file.
	 * 
	 * @param args
	 *            the arguments, see printHelp()
	 */
	public static void main(String[] args) {

		StringBuilder sb = new StringBuilder();
		for (String s : args) {
			sb.append(" [");
			sb.append(s);
			sb.append("]");
		}

		logger.debug("Starting with parameters " + sb.toString());

		if (args.length != 4) {
			printHelp();
			return;
		}

		// first parameter is operation.
		String op = args[0];
		if ("-d".equals(op)) {
			decrypt(args[1], args[2], args[3]);
		} else if ("-e".equals(op)) {
			encrypt(args[1], args[2], args[3]);
		} else {
			printHelp();
		}

	}

	/**
	 * Prints the help message.
	 */
	private static void printHelp() {
		System.out.println("Usage");
		System.out
				.println("\t encrypt: Exporter -e <password> <plaintextfile> <destination>");
		System.out
				.println("\t decrypt: Exporter -d <password> <encryptedfile> <destination>");

	}

	/**
	 * Encrypt the plaintext file with the given password and store the result
	 * in the destinationFile.
	 * 
	 * @param password
	 *            the password
	 * @param plaintextFile
	 *            the plaintext file
	 * @param destinationFile
	 *            the destination file
	 */
	private static void encrypt(String password, String plaintextFile,
			String destinationFile) {
		logger.debug("entering encrypt");
		String plaintext;
		File file = new File(plaintextFile);
		File destFile = new File(destinationFile);
		try {

			plaintext = FileHelper.readStringFromFile(file);
		} catch (FileIOException e) {
			logger.warn("Cannot read the file" + file.getAbsolutePath(), e);
			return;
		}

		try {

			XmlSerializeTool
					.writeEncryptedString(password, destFile, plaintext);
		} catch (SysCryptoException e) {
			logger.warn("Cannot encrypt to  file" + destFile.getAbsolutePath(),
					e);
			return;
		} catch (FileIOException e) {
			logger.warn("Cannot write to  file" + destFile.getAbsolutePath(), e);
			return;
		}
		logger.debug("exiting encrypt");

	}

	/**
	 * Decrypt the enrryptedFile with the given password and store the result in
	 * the destinationFile.
	 * 
	 * @param password
	 *            the password
	 * @param encryptedFile
	 *            the encrypted file
	 * @param destinationFile
	 *            the destination file
	 */
	private static void decrypt(String password, String encryptedFile,
			String destinationFile) {
		logger.debug("entering decrypt");
		String plaintext;
		File inFile = new File(encryptedFile);
		File destFile = new File(destinationFile);
		try {
			plaintext = XmlSerializeTool.readEncryptedFileContent(password,
					inFile);

		} catch (FileIOException e) {
			logger.warn("Cannot decrypt the file" + inFile.getAbsolutePath(), e);
			return;
		} catch (SysCryptoException e) {
			logger.warn("Cannot decrypt the file" + inFile.getAbsolutePath(), e);
			return;
		}

		try {
			FileHelper.writeStringToFile(plaintext, destFile);

		} catch (FileIOException e) {
			logger.warn("Cannot write to  file" + destFile.getAbsolutePath(), e);
			return;
		}
		logger.debug("exiting decrypt");

	}

}
