package com.cometproject.server.network.messages.outgoing.misc;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class AdvancedAlertMessageComposer {
    public static Composer compose(String title, String message, String hotelName, String hotelLink, String illustration) {
        Composer msg = new Composer(Composers.AdvancedAlertMessageComposer);

        msg.writeString(illustration);
        msg.writeInt(hotelLink.isEmpty() ? 2 : 4);
        msg.writeString("title");
        msg.writeString(title);
        msg.writeString("message");
        msg.writeString(message);

        if(!hotelLink.isEmpty()) {
            msg.writeString("linkUrl");
            msg.writeString(hotelLink);
            msg.writeString("linkTitle");
            msg.writeString(hotelName);
        }

        return msg;
    }

    public static Composer compose(String header, String message) {
        return compose(
                header,
                message,
                CometSettings.hotelName,
                CometSettings.hotelUrl + "?ref=alert_link",
                ""
        );
    }

    public static Composer compose(String message) {
        return compose(
                "Message from Hotel Management",
                message,
                "",
                "",
                ""
        );
    }

    public static Composer compose(String header, String message, String image) {
        return compose(
                header,
                message,
                CometSettings.hotelName,
                CometSettings.hotelUrl + "?ref=alert_link",
                image
        );
    }
}
