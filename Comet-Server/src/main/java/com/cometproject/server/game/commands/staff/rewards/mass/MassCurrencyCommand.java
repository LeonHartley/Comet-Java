package com.cometproject.server.game.commands.staff.rewards.mass;

import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang.StringUtils;


public abstract class MassCurrencyCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1 || params[0].isEmpty() || !StringUtils.isNumeric(params[0]))
            return;

        final int amount = Integer.parseInt(params[0]);

        for (ISession session : NetworkManager.getInstance().getSessions().getSessions().values()) {
            try {

                String currencyType = "coins";

                if (this instanceof MassCoinsCommand) {
                    session.getPlayer().getData().increaseCredits(amount);
                } else if (this instanceof MassDucketsCommand) {
                    session.getPlayer().getData().increaseActivityPoints(amount);
                    currencyType = "activity.points";
                    this.logDesc = "El staff %s ha dado a todo el hotel %n duckets"
                            .replace("%n", Integer.toString(amount));

                } else if (this instanceof MassPointsCommand) {
                    session.getPlayer().getData().increaseVipPoints(amount);
                    currencyType = "vip.points";
                    this.logDesc = "El staff %s ha dado a todo el hotel %n diamantes"
                            .replace("%n", Integer.toString(amount));

                } else if (this instanceof MassSeasonalCommand) {
                    session.getPlayer().getData().increaseSeasonalPoints(amount);
                    currencyType = "seasonal";
                    this.logDesc = "El staff %s ha dado a todo el hotel %n boolares"
                            .replace("%n", Integer.toString(amount));
                }

                if(!currencyType.equals("coins")) {
                    session.send(session.getPlayer().composeCurrenciesBalance());
                    this.logDesc = "El staff %s ha dado a todo el hotel %n créditos"
                            .replace("%n", Integer.toString(amount));
                }

                session.send(new AdvancedAlertMessageComposer(
                        Locale.get("command.points.successtitle"),
                        Locale.get("command.points.successmessage").replace("%amount%", String.valueOf(amount))
                                .replace("%type%", Locale.get(currencyType + ".name"))
                ));

                session.getPlayer().getData().save();
                session.getPlayer().sendBalance();
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String getLoggableDescription(){
        return this.logDesc;
    }

    @Override
    public boolean isLoggable(){
        return true;
    }
}
