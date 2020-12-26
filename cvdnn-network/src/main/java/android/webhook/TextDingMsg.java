package android.webhook;

import android.assist.Assert;
import android.io.Stream;
import android.json.Json;

import androidx.annotation.RawRes;

import org.json.JSONObject;

import static android.webhook.DingHookMsg.MsgType.TEXT;
import static java.util.Locale.CHINESE;

public final class TextDingMsg extends AtDingMsg {

    public static TextDingMsg make() {
        return new TextDingMsg();
    }

    ////////////////////////////////
    //

    private String text;

    private TextDingMsg() {
    }

    public final TextDingMsg text(String t, Object... args) {
        this.text = String.format(CHINESE, t, args);

        return this;
    }

    public final TextDingMsg text(@RawRes int rawRes, Object... args) {

        return text(Stream.text(rawRes), args);
    }

    @Override
    protected MsgType type() {
        return TEXT;
    }

    @Override
    protected JSONObject content() {
        String dstText = Assert.notEmpty(text) ? text : "";

        if (isAtAll) {
            dstText += "\n\n";

        } else if (Assert.notEmpty(atMobiles)) {
            StringBuilder temp = new StringBuilder();
            temp.append(dstText).append("\n\n");
            atMobiles.forEach(at -> temp.append('@').append(at).append(' '));

            dstText = temp.toString();
        }

        return Json.make()
                .put("content", dstText)
                .build();
    }
}
