package com.cometsrv.network.messages.outgoing.catalog;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.catalog.types.CatalogClubOffer;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;
import org.joda.time.DateTime;

import java.util.List;

public class ClubMessageComposer {
    public static Composer compose(List<CatalogClubOffer>offers) {
        Composer msg = new Composer(Composers.ClubMessageComposer);

        msg.writeInt(offers.size());
        for(CatalogClubOffer offer : offers) {
            long total = (Comet.getTime() * 1000L) + (offer.lengthSeconds() * 1000L);
            DateTime time = new DateTime(total);

            msg.writeInt(offer.getId());
            msg.writeString(offer.getName());
            msg.writeInt(offer.getCost());
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeBoolean(offer.getType() == 2);
            msg.writeInt(offer.lengthMonths());
            msg.writeInt(offer.lengthDays());
            msg.writeInt(0);
            msg.writeInt(time.getYear()); // expire year
            msg.writeInt(time.getDayOfMonth()); // expire day
            msg.writeInt(time.getMonthOfYear()); // expire month
        }

        msg.writeInt(1);

        return msg;
    }
}
