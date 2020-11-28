package android.math;

import android.assist.Assert;

/**
 * 数据缩短器，注被加密到数据数据差异越大，加密得到到数据越精准
 * 
 * @author handy
 */
public class ShortDigest {

	public static String encrypt(String text) {
		String result = null;

		byte[] byteArray = MD5.encryptToByteArray(text);
		if (Assert.notEmpty(byteArray)) {
			long hashCode = 0, seed = 31; // 31 131 1313 13131 131313 etc..  

			// BKDR Hash
			for (int i = 0; i < byteArray.length; i++) {
				hashCode = (seed * hashCode + byteArray[i]) & Long.MAX_VALUE;
			}

			result = Radix62.fromDecimal(hashCode);
		} else {
			result = new String(Radix62.ELEMENTS, 0, 1);
		}

		return result;
	}
}