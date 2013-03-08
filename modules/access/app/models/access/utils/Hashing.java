package models.access.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

import play.Logger;
import play.Play;
import play.libs.Codec;
import play.libs.Crypto.HashType;

public class Hashing {
	
	private static final String PASSWORD_PAD =	"^z)*Ql:w.tp>GjNMd)XOb*Ag'vjMc^>d" +
												"!ld++?%{FTWnc:P_ B;pi3z+9]G5D6}D" +
												"4zk'8UZ1vz.*PXo5T#eK]<1UP&u3$m0e" +
												"k=1':]o7p6]&]|#3zC]MP~pRN+rwy$D_";
	
	public static String salt(int bits) {
		SecureRandom sr = new SecureRandom();
		sr.setSeed(System.currentTimeMillis());
		byte[] salt = new byte[bits / 8];
		sr.nextBytes(salt);
		
		return Codec.byteToHexString(salt);
	}
	
	public static String hash(String password, String salt, String secret) {
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance(HashType.SHA256.toString());
		} catch (NoSuchAlgorithmException e) {
			Logger.error(e, "Algorithm not available");
			throw new RuntimeException("Algorithm not available");
		}
		
		md.reset();
		md.update(pad(password).getBytes());
		md.update(secret.getBytes());
		byte[] hash = md.digest(salt.getBytes());
		for (int i = 0; i < 2500; i++) {
			md.reset();
			hash = md.digest(hash);
		}
		
		return Codec.byteToHexString(hash);
	}
	
	private static String pad(String password) {
		String padded = password;
		if (password.length() < PASSWORD_PAD.length()) {
			padded = password + PASSWORD_PAD.substring(password.length());
		}
		
		return padded;
	}
	
}
