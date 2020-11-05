package android.text;

import android.assist.Assert;
import android.math.MD5;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * Created by handy on 17-3-14.
 */

public class KneadUtils {
    private static final String TAG = "KneadUtils";

    public static String fixed(Map<String, String> metaMap) {
        StringBuilder tmpBuilder = new StringBuilder();

        if (Assert.notEmpty(metaMap)) {
            String timestamp = TextUtilz.nullTo(TextUtilz.toString(metaMap.get("t")));

            if (Assert.notEmpty(metaMap)) {
                Set<Map.Entry<String, String>> entrySet = metaMap.entrySet();
                for (Map.Entry<String, String> meta : entrySet) {
                    tmpBuilder.append(TextUtilz.nullTo(TextUtilz.toString(meta.getKey())));
                }
            }

            tmpBuilder.append(MD5.encrypt(timestamp));
        }

        // Log.d(TAG, "## " + tmpBuilder.toString());

        return TextUtilz.toTrim(tmpBuilder.toString());
    }

    /**
     * 把字典按Key的字母顺序排序, 把所有参数名和参数值串在一起
     *
     * @param metaMap
     *
     * @return
     */
    public static String sort(Map<String, String> metaMap) {
        String sign = "";

        if (Assert.notEmpty(metaMap)) {
            TreeMap<String, String> signMap = null;
            if (metaMap instanceof TreeMap) {
                signMap = (TreeMap<String, String>) metaMap;

            } else {
                signMap = new TreeMap<>();
                signMap.putAll(metaMap);
            }

            StringBuilder query = new StringBuilder();

            final Set<Map.Entry<String, String>> paramSet = signMap.entrySet();
            for (Map.Entry<String, String> param : paramSet) {
                String key = param.getKey(), value = param.getValue();
                if (Assert.notEmpty(key) && Assert.notEmpty(value)) {
                    query.append(key).append(value);
                }
            }

            String kv = query.toString();
            sign = MD5.encrypt(kv);

//            Log.i(TAG, ">> KV: \t%s", kv);
//            Log.i(TAG, ">> KV MD5: %s", sign);
        }

        return sign;
    }
}
