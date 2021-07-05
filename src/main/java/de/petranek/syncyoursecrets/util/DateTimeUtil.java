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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The class DataTimeUtil encapsulates an ISO Date format. It converts String to
 * a DateTime and back.
 * 
 * Unfortunately, the JodaTime library throws runtime exceptions - we attempt to
 * catch them and fail gracefully.
 * 
 * @author Jan Petranek
 */
public final class DateTimeUtil {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(DateTimeUtil.class);



	/**
	 * Utility class, shall not be instantiated.
	 */
	private DateTimeUtil() {
		super();
	}

	/**
	 * Parses the date time.
	 * 
	 * @param dateString
	 *            the date string
	 * 
	 * @return the date time
	 * 
	 * @throws SysParseException
	 *             when the operation fails
	 */
	public static ZonedDateTime parseDateTime(String dateString)
			throws SysParseException {
		try {
			return ZonedDateTime.from( DateTimeFormatter.ISO_DATE_TIME.parse(dateString));
		} catch (UnsupportedOperationException ux) {
			String msg = ("Date parses does not support parsing, your system is rotten.");
			logger.error(msg, ux);
			throw new SysParseException(msg, ux);
		} catch (IllegalArgumentException ix) {
			String msg = ("Cannot parse Date String " + dateString);
			logger.error(msg, ix);
			throw new SysParseException(msg, ix);
		}

	}

	/**
	 * Date time2 string.
	 * 
	 * @param dateTime
	 *            the date time
	 * 
	 * @return the string
	 */
	public static String dateTime2String(ZonedDateTime dateTime) {
		return DateTimeFormatter.ISO_DATE_TIME.format(dateTime);
	}

}
