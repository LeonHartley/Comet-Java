package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class AdvancedAlertMessageComposer extends MessageComposer {
    private final String title;
    private final String message;
    private final String hotelName;
    private final String hotelLink;
    private final String illustration;

    public AdvancedAlertMessageComposer(final String title, final String message, final String hotelName, final String hotelLink, final String illustration) {
        this.title = title;
        this.message = message;
        this.hotelName = hotelName;
        this.hotelLink = hotelLink;
        this.illustration = illustration;
    }

    public AdvancedAlertMessageComposer(final String header, final String message) {
        this(header, message, "", "", "");
    }

    public AdvancedAlertMessageComposer(final String header, final String message, final String image) {
        this(header, message, "", "", image);
    }

    public AdvancedAlertMessageComposer(String message) {
        this("Alert", message, "", "", "");
    }

    @Override
    public short getId() {
        return Composers.AdvancedAlertMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeString(illustration);
        msg.writeInt(hotelLink.isEmpty() ? 2 : 4);
        msg.writeString("title");
        msg.writeString(title);
        msg.writeString("message");
        msg.writeString(message);

        if (!hotelLink.isEmpty()) {
            msg.writeString("linkUrl");
            msg.writeString(hotelLink);
            msg.writeString("linkTitle");
            msg.writeString(hotelName);
        }

    }
}
