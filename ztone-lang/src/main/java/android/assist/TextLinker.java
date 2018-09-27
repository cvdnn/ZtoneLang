package android.assist;

public class TextLinker {
	public static final String SEPARATOR_COMMA = ",";

	private String mDefaultSeparator;
	private final StringBuilder mTextBuffer;

	private TextLinker(String separator) {
		mDefaultSeparator = separator;

		mTextBuffer = new StringBuilder();
	}

	public static TextLinker create() {
		return new TextLinker(SEPARATOR_COMMA);
	}

	public static TextLinker create(String separator) {
		return new TextLinker(separator);
	}

	public void clear() {
		if (mTextBuffer.length() > 0) {
			mTextBuffer.delete(0, mTextBuffer.length());
		}
	}

	public TextLinker append(String... args) {

		return append(args, mDefaultSeparator);
	}

	public TextLinker append(String[] args, String separator) {
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
