package android.json;

import android.text.TextUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static android.json.JPath.SEPARATOR_NODE;

public class JNodeArray implements Cloneable {
	private boolean isAbsolute;

	private ArrayList<String> jNodeList;

	public JNodeArray() {
		jNodeList = new ArrayList<String>();
	}

	public JNodeArray(String jPath) throws JSONException {
		this();

		if (!TextUtils.isEmpty(jPath)) {
			isAbsolute = jPath.startsWith(SEPARATOR_NODE);
			jPath = jPath.substring(isAbsolute ? 1 : 0);
			String[] paths = jPath.split(SEPARATOR_NODE);

			if (checkNodes(paths)) {
				jNodeList.addAll(Arrays.asList(paths));
			} else {
				throw new JSONException("JPath is Illegal!");
			}
		}
	}

	public String get(int location) {

		return jNodeList.get(location);
	}

	public boolean add(String node) {
		boolean result = false;
		if (!TextUtils.isEmpty(node)) {
			result = jNodeList.add(node);
		}

		return result;
	}

	public boolean add(int location, String node) {
		boolean result = false;
		if (!TextUtils.isEmpty(node)) {
			try {
				jNodeList.add(location, node);
				result = true;
			} catch (Exception e) {
				// do nothing
			}
		}

		return result;
	}

	public boolean addAll(Collection<String> collection) {

		return checkNodes(collection) && jNodeList.addAll(collection);
	}

	public boolean addAll(int location, Collection<String> collection) throws Exception {
		boolean resule = false;

		try {
			resule = checkNodes(collection) && jNodeList.addAll(location, collection);
		} catch (Exception e) {
			// do nothing
		}

		return resule;
	}

	public String set(int location, String node) {
		String result = null;
		if (!TextUtils.isEmpty(node)) {
			try {
				result = jNodeList.set(location, node);
			} catch (Exception e) {
				// do nothing
			}
		}

		return result;
	}

	public boolean contains(String node) {

		return jNodeList.contains(node);
	}

	public int indexOf(String node) {

		return jNodeList.indexOf(node);
	}

	public String remove(int location) {
		String result = null;

		try {
			result = jNodeList.remove(location);
		} catch (Exception e) {
			// do nothing
		}

		return result;
	}

	public boolean retainAll(Collection<?> collection) throws Exception {
		boolean result = false;

		if (collection != null) {
			result = jNodeList.retainAll(collection);
		}

		return result;
	}

	public boolean isEmpty() {

		return jNodeList.isEmpty();
	}

	public int length() {

		return jNodeList.size();
	}

	public JNodeArray subJNodeArray(int start, int end) throws Exception {
		JNodeArray jNodeArray = new JNodeArray();
		jNodeArray.addAll(jNodeList.subList(start, end));

		return jNodeArray;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JNodeArray clone() {
		JNodeArray newJNodeArray = null;

		try {
			newJNodeArray = (JNodeArray) super.clone();
			newJNodeArray.jNodeList = (ArrayList<String>) jNodeList.clone();
		} catch (Exception e) {
			// do nothing
		}

		return newJNodeArray;
	}

	@Override
	public int hashCode() {
		int result = isAbsolute ? 17 : 1;

		for (String node : jNodeList) {
			result = (31 * result) + (node == null ? 0 : node.hashCode());
		}

		return result;
	}

	@Override
	public boolean equals(Object o) {

		return (o != null && o instanceof JNodeArray) && toString().equals(o.toString());
	}

	@Override
	public String toString() {
		StringBuilder sbJNodeArray = new StringBuilder(isAbsolute ? SEPARATOR_NODE : "");

		int size = jNodeList.size();
		for (int i = 0; i < size; i++) {
			sbJNodeArray.append(jNodeList.get(i));
			if (i < size - 1) {
				sbJNodeArray.append(SEPARATOR_NODE);
			}
		}

		return sbJNodeArray.toString();
	}

	private boolean checkNodes(Collection<String> collection) {
		boolean result = false;
		if (collection != null) {
			for (String node : collection) {
				if (TextUtils.isEmpty(node)) {
					result = false;

					break;
				} else {
					result = true;
				}
			}
		}

		return result;
	}

	private boolean checkNodes(String[] nodes) {
		boolean result = false;
		if (nodes != null) {
			for (String node : nodes) {
				if (TextUtils.isEmpty(node)) {
					result = false;

					break;
				} else {
					result = true;
				}
			}
		}

		return result;
	}
}
