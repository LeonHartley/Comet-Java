package com.cometproject.network.messages.outgoing.misc;

import com.cometproject.config.CometSettings;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class AdvancedAlertMessageComposer {
    public static Composer compose(String title, String redText, String message, String hotelName, String hotelLink, String illustration) {
        Composer msg = new Composer(Composers.AdvancedAlertMessageComposer);

        msg.writeString(title);
        msg.writeString(redText);
        msg.writeString(message);
        msg.writeString(hotelName);
        msg.writeString(hotelLink);
        msg.writeString(illustration);

        return msg;
    }

    public static Composer compose(String header, String message) {
        return compose(
                CometSettings.hotelName,
                header,
                message,
                CometSettings.hotelName,
                CometSettings.hotelUrl + "?ref=alert_link",
                ""
        );
    }

    public static Composer compose(String header, String message, String image) {
        return compose(
                CometSettings.hotelName,
                header,
                message,
                CometSettings.hotelName,
                CometSettings.hotelUrl + "?ref=alert_link",
                image
        );
    }
}
