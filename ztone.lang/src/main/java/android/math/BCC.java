package android.math;

import android.assist.Assert;

public class BCC {

	public static byte encrypt(byte[] barry) {
		byte bcc = 0x00;

		if (Assert.notEmpty(barry)) {
			for (byte b : barry) {
				bcc ^= b;
			}
		}

		return bcc;
	}

	public static byte encrypt(String hexString) {
		byte bcc = 0x00;

		if (Assert.notEmpty(hexString)) {
			int len = hexString.length();
			for (int i = 0; i < len; i += 2) {
				bcc ^= Maths.valueOf(hexString.substring(i, i + 2), 0, 16);
			}
		}

		return bcc;
	}
}
