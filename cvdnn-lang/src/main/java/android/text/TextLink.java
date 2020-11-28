package android.text;

import android.assist.Assert;

public class TextLink {
	public static final String SEPARATOR_COMMA = ",";

	private String mDefaultSeparator;
	private final StringBuilder mTextBuffer;

	private TextLink(String separator) {
		mDefaultSeparator = separator;

		mTextBuffer = new StringBuilder();
	}

	public static TextLink create() {
		return new TextLink(SEPARATOR_COMMA);
	}

	public static TextLink create(String separator) {
		return new TextLink(separator);
	}

	public void clear() {
		if (mTextBuffer.length() > 0) {
			mTextBuffer.delete(0, mTextBuffer.length());
		}
	}

	public TextLink append(String... args) {

		return append(args, mDefaultSeparator);
	}

	public TextLink append(String[] args, String separator) {
		if (Assert.notEmpty(args)) {
			for (String a : args) {
				if (mTextBuffer.length() == 0) {
					mTextBuffer.append(a);

				} else {
					mTextBuffer.append(Assert.notEmpty(separator) ? separator : mDefaultSeparator).append(a);
				}
			}
		}

		return this;
	}

	public String[] blockSort() {

		return blockSort(mDefaultSeparator);
	}

	public String[] blockSort(String separator) {

		return TextUtilz.blockSort(mTextBuffer.toString(), Assert.notEmpty(separator) ? separator : mDefaultSeparator);
	}

	@Override
	public String toString() {

		return mTextBuffer.toString();
	}
}
