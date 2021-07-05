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
package de.petranek.syncyoursecrets.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * The Class FileHelper contains basic methods to read and write content to or
 * from a file.
 * 
 * @author Jan Petranek
 */
public final class FileHelper {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(FileHelper.class);

	/** The ENCODING used for all files. */
	static final String ENCODING = "UTF-8";

	/**
	 * Private constructor; This class is a utility class and never needs to be
	 * instantiated.
	 */
	private FileHelper() {
		super();
	}

	/**
	 * Write the given content to a file.
	 * 
	 * @param content
	 *            the content
	 * @param destination
	 *            the destination file
	 * 
	 * @throws FileIOException
	 *             when the write has failed.
	 */
	public static void writeStringToFile(final String content,
			final File destination) throws FileIOException {

		logger.debug("entering writeStringToFile");
		if (destination.exists() && !destination.canWrite()) {
			String msg = "Cannot write to file "
					+ destination.getAbsolutePath()
					+ " File already exists and cannot be overwritten";
			logger.warn(msg);
			throw new FileIOException(msg);
		}

		Writer fWriter = null;
		PrintWriter pWriter = null;
		try {
			logger.debug("Attempting to write to file "
					+ destination.getAbsolutePath());
			fWriter = new OutputStreamWriter(new BufferedOutputStream(
					new FileOutputStream(destination)), ENCODING);

			pWriter = new PrintWriter(fWriter);

			pWriter.print(content);
			pWriter.flush();
			pWriter.close();

			logger.debug("Successful write to file "
					+ destination.getAbsolutePath());
		} catch (IOException e) {

			String msg = "Error occured while writing to file "
					+ destination.getAbsolutePath();

			logger.error(msg, e);
			FileIOException fex = new FileIOException(msg, e);
			throw fex;

		} finally {
			if (fWriter != null) {
				try {
					fWriter.close();
					logger.debug("Successfully closed file "
							+ destination.getAbsolutePath());
				} catch (IOException e) {

					String msg = "Error occured while closing file "
							+ destination.getAbsolutePath();

					logger.error(msg, e);
					FileIOException fex = new FileIOException(msg, e);
					throw fex;
				}
			}
		}
		logger.debug("exiting writeStringToFile");
	}

	/**
	 * Check file for readability. Returns normally, when the file is readable.
	 * If not, a FileIOException is thrown.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @throws FileIOException
	 *             when the file is not readable or an error has occurred when
	 *             accessing the file.
	 */
	public static void checkFileForReadability(final File file)
			throws FileIOException {
		logger.debug("entering checkFileForReadability");
		if (file == null) {
			String msg = ("File may not be null");
			logger.warn(msg);
			throw new FileIOException(msg);
		}

		if (!file.exists()) {
			String msg = ("File " + file.getAbsolutePath() + " does not exist!");
			logger.warn(msg);
			throw new FileIOException(msg);
		}
		if (!file.canRead()) {
			String msg = ("File " + file.getAbsolutePath() + " is not readable");
			logger.warn(msg);
			throw new FileIOException(msg);
		}
		logger.debug("exiting checkFileForReadability");
	}

	/**
	 * Read the contents of a file and return them as a string.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return the string
	 * 
	 * @throws FileIOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String readStringFromFile(final File file)
			throws FileIOException {
		logger.debug("entering readStringFromFile");
		StringBuffer sb = new StringBuffer();
		char[] content = null;
		// file should exist and be readable:
		checkFileForReadability(file);

		Reader reader = null;
		try {
			reader = new InputStreamReader(new BufferedInputStream(
					new FileInputStream(file)), ENCODING);

			int length = (int) file.length();
			content = new char[length];
			int readChars;
			do {
				readChars = reader.read(content);
				if (readChars > 0) {
					sb.append(content, 0, readChars);
				}
			} while (readChars > 0);

			logger.debug("Read: File length : " + length + " Read characters: "
					+ sb.length());
			if (sb.length() < length) {
				String msg = "Failed to read entire file "
						+ file.getAbsolutePath();

				logger.error(msg);

				throw new FileIOException(msg);
			}

		} catch (IOException e) {
			String msg = "I/O-Error while reading file  "
					+ file.getAbsolutePath();

			logger.error(msg, e);

			throw new FileIOException(msg, e);

		} finally {
			// only close, if the reader is open.
			if (reader != null) {
				try {
					reader.close();
					logger.debug("Successfully closed file "
							+ file.getAbsolutePath());
				} catch (IOException e) {

					String msg = "Error occured while closing file "
							+ file.getAbsolutePath();

					logger.error(msg, e);
					FileIOException fex = new FileIOException(msg, e);
					throw fex;
				}
			}
		}

		logger.debug("exiting readStringFromFile");
		return sb.toString();
	}
}
