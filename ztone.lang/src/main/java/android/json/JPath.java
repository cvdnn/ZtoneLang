package android.json;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;

public class JPath implements Cloneable {
	private static final String TAG = "JPath";

	public static final String SEPARATOR_NODE = "/";
	public static final String SEPARATOR_VALUE = "$";

	private String mJKey;
	private JNodeArray mJNodeArray;

	private boolean mIsJValuePath;

	private JPath(String path) throws JSONException {
		if (!TextUtils.isEmpty(path)) {
			int i = path.lastIndexOf(SEPARATOR_VALUE);
			if (i >= 0) {
				mJKey = path.substring(i + 1);
				if (TextUtils.isEmpty(mJKey)) {
					throw new JSONException("JPath is Illegal!");
				}

				path = i == 0 ? "" : path.substring(0, i);

				mIsJValuePath = true;
			}

			mJNodeArray = new JNodeArray(path);
		}
	}

	public static JPath createJPath(String strJPath) {
		JPath jPath = null;

		try {
			jPath = new JPath(strJPath);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return jPath;
	}

	public String getKey() {
		return mJKey;
	}

	public JNodeArray getJNodeArray() {
		return mJNodeArray;
	}

	public boolean isJValuePath() {

		return mIsJValuePath;
	}

	@Override
	public JPath clone() {
		JPath newJPath = null;

		try {
			newJPath = (JPath) super.clone();
			newJPath.mJKey = new String(mJKey);
			newJPath.mJNodeArray = mJNodeArray.clone();
		} catch (Exception e) {
			// do nothing
			Log.e("", e.getMessage(), e);
		}

		return newJPath;
	}

	@Override
	public int hashCode() {
		int result = 1;

		int len = mJNodeArray.length();
		for (int i = 0; i < len; i++) {
			String node = mJNodeArray.get(i);
			result = (31 * result) + (node == null ? 0 : node.hashCode());
		}

		result = (31 * result) + (mJKey == null ? 0 : mJKey.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object o) {
		boolean resule = false;

		if (o != null && o instanceof JPath) {
			JPath jPath = (JPath) o;
			resule = (TextUtils.isEmpty(mJKey) && TextUtils.isEmpty(jPath.mJKey) || !TextUtils.isEmpty(mJKey)
					&& mJKey.equals(jPath.mJKey))
					&& mJNodeArray.equals(jPath.mJNodeArray);
		}

		return resule;
	}

	@Override
	public String toString() {
		StringBuilder sbJPath = new StringBuilder(mJNodeArray.toString());

		if (!TextUtils.isEmpty(mJKey)) {
			sbJPath.append(SEPARATOR_VALUE).append(mJKey);
		}

		return sbJPath.toString();
	}
}
