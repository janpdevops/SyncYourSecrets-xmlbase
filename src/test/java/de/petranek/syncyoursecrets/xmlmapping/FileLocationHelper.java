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

import java.io.File;

/**
 * The Class FileLocationHelper helps in getting properties from the 
 * test run environment. This is required for test from ant.
 * 
 * @author Jan Petranek
 */
public class FileLocationHelper {

	public final static String DEFAULT_BASE = "src/test/resources";

	public static File getFile(String trailingPath) {
		final String fileName = getFileName(trailingPath);
		return new File(fileName);
	}

	/**
	 * @param trailingPath
	 * @return
	 */
	public static String getFileName(String trailingPath) {


		final String fileName = DEFAULT_BASE + File.separator + trailingPath;
		return fileName;
	}

}
