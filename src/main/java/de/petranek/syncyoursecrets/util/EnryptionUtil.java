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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionInitializationException;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

/**
 * The EncryptionUtil handles encryption and decryption for SyncYourSecrets.
 * 
 * We use a symmetric, password based algorithm here. Thanks to Jasypt, the
 * password will be used with a random grain of salt ;-)
 * 
 * All this is encapsulated in this class to provide a single entry point for
 * cryptographic operations.
 * 
 * This class is designed to delegate all the pesky cryptographic details to
 * Jasypt and Bouncycastle. There are three good reasons for this: - By using
 * existing, scrutinized packages, we can minimize the risk of making some
 * beginner's mistake at this crucial point. We don't want to compromise
 * security, because someone forgot how to add salt to his passwords, do we? -
 * Should weaknesses be found in the underlying algorithms, we can simply use a
 * fixed version of these. - Avoid to re-invent the wheel. (aka lazyness)
 * 
 * @author Jan Petranek
 */
public final class EnryptionUtil {

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(EnryptionUtil.class);

	/**
	 * The symmetric encryption algorithm for SyncYourSecrets. WARNING:
	 * Modifications to this algorithm will break backwards compatibility with
	 * previously encrypted SyncYourSecrets files !
	 */
	private static final String CRYPTO_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC";

	/**
	 * Utility class, shall not be instantiated.
	 */
	private EnryptionUtil() {
		super();
	}

	/**
	 * Decrypt a string with the given password. The password will be treated
	 * with a salt.
	 * 
	 * @param cypher
	 *            the cyphertext, as String
	 * @param password
	 *            the password
	 * 
	 * @return the plaintext as string
	 * 
	 * @throws SysCryptoException
	 *             when the decryption has failed
	 */
	public static String decryptString(String cypher, String password)
			throws SysCryptoException {

		logger.debug("entering decryptString");

		try {
			StandardPBEStringEncryptor encryptor = initCypher();
			encryptor.setPassword(password);
			String output = encryptor.decrypt(cypher);

			logger.debug("exiting decryptString");
			return output;

		} catch (EncryptionOperationNotPossibleException ex) {
			String msg = "Decryption failed";
			logger.warn(msg, ex);
			throw new SysCryptoException(msg, ex);

		} catch (EncryptionInitializationException ux) {
			String msg = "Failed to initialize decryption";
			logger.warn(msg, ux);
			throw new SysCryptoException(msg, ux);
		}

	}

	/**
	 * Encrypt a plaintext with the given password. The password will be treated
	 * with a salt.
	 * 
	 * @param plaintext
	 *            the plaintext
	 * @param password
	 *            the password
	 * 
	 * @return the cyphertext
	 * 
	 * @throws SysCryptoException
	 *             when the encryption has failed
	 */
	public static String encryptString(String plaintext, String password)
			throws SysCryptoException {
		logger.debug("entering encryptString");
		try {
			StandardPBEStringEncryptor encryptor = initCypher();
			encryptor.setPassword(password);
			String output = encryptor.encrypt(plaintext);

			logger.debug("exiting encryptString");
			return output;

		} catch (EncryptionOperationNotPossibleException ex) {
			String msg = "Encryption failed";
			logger.warn(msg, ex);
			throw new SysCryptoException(msg, ex);

		} catch (EncryptionInitializationException ux) {
			String msg = "Failed to initialize encryption";
			logger.warn(msg, ux);
			throw new SysCryptoException(msg, ux);
		}

	}

	/**
	 * Inits the cypher, using the our CRYPTO_ALGORITM.
	 * 
	 * @return a standard password based string encryptor
	 */
	private static StandardPBEStringEncryptor initCypher() {
		logger.debug("entering initCypher");
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setProvider(new BouncyCastleProvider());
		encryptor.setAlgorithm(CRYPTO_ALGORITHM);
		logger.debug("exiting initCypher");
		return encryptor;
	}

}
