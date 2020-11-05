package android.slog.aliyun;

import android.Android;
import android.assist.Assert;
import android.collection.Pairing;
import android.content.Context;
import android.log.service.AysLog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aliyun.sls.android.sdk.ClientConfiguration;
import com.aliyun.sls.android.sdk.LOGClient;
import com.aliyun.sls.android.sdk.SLSLog;
import com.aliyun.sls.android.sdk.core.auth.PlainTextAKSKCredentialProvider;
import com.aliyun.sls.android.sdk.model.Log;
import com.aliyun.sls.android.sdk.model.LogGroup;
import com.aliyun.sls.android.sdk.request.PostLogRequest;

import java.util.Map;

import static com.aliyun.sls.android.sdk.ClientConfiguration.NetworkPolicy.WWAN_OR_WIFI;

public class AysClient implements AysLog.Client {
    private static final String TAG = "AysClient";

    private static final String endpoint = "http://cn-hangzhou.sls.aliyuncs.com";
    private static final String project = "green";
    private static final String logStore = "green_log";

    private LOGClient mLogClient;

    @Override
    public void async(@Nullable String topic, @Nullable String tag, Pairing pair) {
        /* 创建logGroup */
        LogGroup logGroup = new LogGroup(topic, Android.Build.cpuSerial());

        /* 存入一条log */
        LogMap log = new LogMap();
        log.put("__tag__", tag);

        if (Assert.notEmpty(pair)) {
            for (Map.Entry<String, Object> entry : pair.entrySet()) {
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());

                log.put(Assert.notEmpty(key) ? key : "__msg__", value);
            }
        }

        logGroup.PutLog(log);

        try {
            mLogClient.asyncPostLog(new PostLogRequest(project, logStore, logGroup), null);
        } catch (Exception e) {
            android.log.Log.e(TAG, e);
        }
    }

    @Override
    public void async(@Nullable String topic, @Nullable String tag, String text, Object... args) {
        /* 创建logGroup */
        LogGroup logGroup = new LogGroup(topic, Android.Build.cpuSerial());

        /* 存入一条log */
        Log log = new Log();
        log.PutContent("TAG", tag);
        log.PutContent("__msg__", String.format(text, args));

        logGroup.PutLog(log);

        try {
            mLogClient.asyncPostLog(new PostLogRequest(project, logStore, logGroup), null);
        } catch (Exception e) {
            android.log.Log.e(TAG, e);
        }
    }

    @Override
    public final void onSetup(@NonNull Context context) {
//        移动端是不安全环境，不建议直接使用阿里云主账号ak，sk的方式。建议使用STS方式。具体参见
//        https://help.aliyun.com/document_detail/62681.html
//        注意：SDK 提供的 PlainTextAKSKCredentialProvider 只建议在测试环境或者用户可以保证阿里云主账号AK，SK安全的前提下使用。

//        FIXME 主账户使用方式
        String AK = "LTAI4GAtfRuGbPc1e3o";
        String SK = "pD1Fd3U5XaWXHlrANRRIsK1S";
        PlainTextAKSKCredentialProvider credentialProvider = new PlainTextAKSKCredentialProvider(AK, SK);

//        STS使用方式
//        String STS_AK = "LTApV3BWmeJYVX8";
//        String STS_SK = "7UMCsFZgLfjQI7nDfFOYt";
//        String STS_TOKEN = "******";
//        StsTokenCredentialProvider credentialProvider = new StsTokenCredentialProvider(STS_AK, STS_SK, STS_TOKEN);

        ClientConfiguration conf = new ClientConfiguration();
        // 连接超时，默认15秒
        conf.setConnectionTimeout(15 * 1000);
        // socket超时，默认15秒
        conf.setSocketTimeout(15 * 1000);
        // 最大并发请求书，默认5个
        conf.setMaxConcurrentRequest(5);
        // 失败后最大重试次数，默认2次
        conf.setMaxErrorRetry(2);
        conf.setCachable(false);
        conf.setConnectType(WWAN_OR_WIFI);
        // log打印在控制台
        SLSLog.disableLog();

        mLogClient = new LOGClient(context, endpoint, credentialProvider, conf);
    }
}
