package com.cometproject.server.network.messages;

import com.cometproject.server.network.messages.headers.Events;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.incoming.catalog.GetCataIndexMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.GetCataPageMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.HabboClubPackagesMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.PurchaseItemMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.data.GetShopData2MessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.data.GetShopDataMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupDialogMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.marketplace.MarketplaceConfigurationMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.pets.PetRacesMessageEvent;
import com.cometproject.server.network.messages.incoming.handshake.*;
import com.cometproject.server.network.messages.incoming.help.HelpTicketMessageEvent;
import com.cometproject.server.network.messages.incoming.help.InitHelpToolMessageEvent;
import com.cometproject.server.network.messages.incoming.messenger.AcceptFriendshipMessageEvent;
import com.cometproject.server.network.messages.incoming.messenger.PrivateChatMessageEvent;
import com.cometproject.server.network.messages.incoming.messenger.RequestFriendshipMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.ModToolBanUserMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.ModToolRoomChatlogMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.ModToolUserChatlogMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.ModToolUserInfoMessageEvent;
import com.cometproject.server.network.messages.incoming.navigator.*;
import com.cometproject.server.network.messages.incoming.room.action.*;
import com.cometproject.server.network.messages.incoming.room.bots.BotConfigMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.ModifyBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.PlaceBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.RemoveBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.AddUserToRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.FollowRoomInfoMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.InitalizeRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.LoadHeightmapMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.*;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinNameMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.wired.SaveWiredMessageEvent;
import com.cometproject.server.network.messages.incoming.room.moderation.GetBannedUsersMessageEvent;
import com.cometproject.server.network.messages.incoming.room.moderation.GiveRightsMessageEvent;
import com.cometproject.server.network.messages.incoming.room.moderation.KickUserMessageEvent;
import com.cometproject.server.network.messages.incoming.room.moderation.RemoveAllRightsMessageEvent;
import com.cometproject.server.network.messages.incoming.room.settings.LoadRoomInfoMessageEvent;
import com.cometproject.server.network.messages.incoming.room.settings.SaveRoomDataMessageEvent;
import com.cometproject.server.network.messages.incoming.room.settings.UpdatePapersMessageEvent;
import com.cometproject.server.network.messages.incoming.room.settings.UsersWithRightsMessageEvent;
import com.cometproject.server.network.messages.incoming.room.trading.*;
import com.cometproject.server.network.messages.incoming.user.club.ClubStatusMessageEvent;
import com.cometproject.server.network.messages.incoming.user.details.ChangeMottoMessageEvent;
import com.cometproject.server.network.messages.incoming.user.details.UserInformationMessageEvent;
import com.cometproject.server.network.messages.incoming.user.inventory.BadgeInventoryMessageEvent;
import com.cometproject.server.network.messages.incoming.user.inventory.BotInventoryMessageEvent;
import com.cometproject.server.network.messages.incoming.user.inventory.OpenInventoryMessageEvent;
import com.cometproject.server.network.messages.incoming.user.profile.GetProfileMessageEvent;
import com.cometproject.server.network.messages.incoming.user.profile.GetRelationshipsMessageEvent;
import com.cometproject.server.network.messages.incoming.user.profile.SetRelationshipMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.ChangeLooksMessageEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

public class MessageHandler {
	private FastMap<Short, IEvent> messages;

    public static Logger log = Logger.getLogger(MessageHandler.class.getName());
	
	public MessageHandler() {
	    this.load();
	}

    public void load() {
        if(this.messages == null) {
            this.messages = new FastMap<>();
        } else {
            this.messages.clear();
        }

        this.registerHandshake();
        //this.registerModTool();
        //this.registerHelpTool();
        //this.registerMessenger();
        this.registerNavigator();
        this.registerUser();
        this.registerBots();
        this.registerRoom();
        //this.registerRoomTrade();
        //this.registerRoomModeration();
        //this.registerItems();
        this.registerCatalog();

        log.info("Loaded " + this.getMessages().size() + " message events");
    }

	public void registerHandshake() {
        this.getMessages().put(Events.CheckReleaseMessageEvent, new CheckReleaseMessageEvent());
        this.getMessages().put(Events.InitCryptoMessageEvent, new InitCryptoMessageEvent());
        this.getMessages().put(Events.GenerateSecretKeyMessageEvent, new GenerateSecretKeyMessageEvent());
        this.getMessages().put(Events.SSOTicketMessageEvent, new SSOTicketMessageEvent());
        this.getMessages().put(Events.UniqueIdMessageEvent, new UniqueIdMessageEvent());
	}

    public void registerModTool() {
        this.getMessages().put(Events.ModToolUserInfoMessageEvent, new ModToolUserInfoMessageEvent());
        this.getMessages().put(Events.ModToolUserChatlogMessageEvent, new ModToolUserChatlogMessageEvent());
        this.getMessages().put(Events.ModToolRoomChatlogMessageEvent, new ModToolRoomChatlogMessageEvent());
        this.getMessages().put(Events.ModToolBanUserMessageEvent, new ModToolBanUserMessageEvent());
    }

    public void registerHelpTool() {
        this.getMessages().put(Events.InitHelpToolMessageEvent, new InitHelpToolMessageEvent());
        this.getMessages().put(Events.HelpTicketMessageEvent, new HelpTicketMessageEvent());
    }

    public void registerMessenger() {
        this.getMessages().put(Events.FollowRoomInfoMessageEvent, new FollowRoomInfoMessageEvent());
        this.getMessages().put(Events.PrivateChatMessageEvent, new PrivateChatMessageEvent());
        this.getMessages().put(Events.RequestFriendshipMessageEvent, new RequestFriendshipMessageEvent());
        this.getMessages().put(Events.AcceptFriendshipMessageEvent, new AcceptFriendshipMessageEvent());
    }

    public void registerNavigator() {
        this.getMessages().put(Events.OwnRoomsMessageEvent, new OwnRoomsMessageEvent());
        /*this.getMessages().put(Events.PopularRoomsMessageEvent, new PopularRoomsMessageEvent());
        this.getMessages().put(Events.LoadSearchRoomMessageEvent, new LoadSearchRoomMessageEvent());
        this.getMessages().put(Events.SearchRoomMessageEvent, new SearchRoomMessageEvent());
        this.getMessages().put(Events.CanCreateRoomMessageEvent, new CanCreateRoomMessageEvent());
        this.getMessages().put(Events.CreateNewRoomMessageEvent, new CreateRoomMessageEvent());
        this.getMessages().put(Events.LoadCategoriesMessageEvent, new LoadCategoriesMessageEvent());
        this.getMessages().put(Events.FeaturedRoomsMessageEvent, new FeaturedRoomsMessageEvent());*/
    }

    public void registerUser() {
       // this.getMessages().put(Events.GetProfileMessageEvent, new GetProfileMessageEvent());
       // this.getMessages().put(Events.ClubStatusMessageEvent, new ClubStatusMessageEvent());
        this.getMessages().put(Events.UserInformationMessageEvent, new UserInformationMessageEvent());
       // this.getMessages().put(Events.ChangeLooksMessageEvent, new ChangeLooksMessageEvent());
        this.getMessages().put(Events.OpenInventoryMessageEvent, new OpenInventoryMessageEvent());
        this.getMessages().put(Events.BadgeInventoryMessageEvent, new BadgeInventoryMessageEvent());
       // this.getMessages().put(Events.ChangeMottoMessageEvent, new ChangeMottoMessageEvent());
       // this.getMessages().put(Events.GetRelationshipsMessageEvent, new GetRelationshipsMessageEvent());
       // this.getMessages().put(Events.SetRelationshipMessageEvent, new SetRelationshipMessageEvent());
    }

    public void registerBots() {
        this.getMessages().put(Events.BotInventoryMessageEvent, new BotInventoryMessageEvent());
        //this.getMessages().put(Events.PlaceBotMessageEvent, new PlaceBotMessageEvent());
        //this.getMessages().put(Events.ModifyBotMessageEvent, new ModifyBotMessageEvent());
        //this.getMessages().put(Events.RemoveBotMessageEvent, new RemoveBotMessageEvent());
        //this.getMessages().put(Events.BotConfigMessageEvent, new BotConfigMessageEvent());
    }

    public void registerRoom() {
        this.getMessages().put(Events.InitalizeRoomMessageEvent, new InitalizeRoomMessageEvent());
        this.getMessages().put(Events.LoadHeightmapMessageEvent, new LoadHeightmapMessageEvent());
        this.getMessages().put(Events.TalkMessageEvent, new TalkMessageEvent());
        /*this.getMessages().put(Events.AddUserToRoomMessageEvent, new AddUserToRoomMessageEvent());
        this.getMessages().put(Events.ExitRoomMessageEvent, new ExitRoomMessageEvent());

        this.getMessages().put(Events.ShoutMessageEvent, new ShoutMessageEvent());
        this.getMessages().put(Events.WisperMessageEvent, new WisperMessageEvent());
        this.getMessages().put(Events.WalkMessageEvent, new WalkMessageEvent());
        this.getMessages().put(Events.ApplyActionMessageEvent, new ApplyActionMessageEvent());
        this.getMessages().put(Events.ApplySignMessageEvent, new ApplySignMessageEvent());
        this.getMessages().put(Events.ApplyDanceMessageEvent, new ApplyDanceMessageEvent());
        this.getMessages().put(Events.LoadRoomInfoMessageEvent, new LoadRoomInfoMessageEvent());
        this.getMessages().put(Events.UsersWithRightsMessageEvent, new UsersWithRightsMessageEvent());
        this.getMessages().put(Events.SaveRoomDataMessageEvent, new SaveRoomDataMessageEvent());
        this.getMessages().put(Events.RespectUserMessageEvent, new RespectUserMessageEvent());
        this.getMessages().put(Events.StartTypingMessageEvent, new StartTypingMessageEvent());
        this.getMessages().put(Events.StopTypingMessageEvent, new StopTypingMessageEvent());
        this.getMessages().put(Events.LookToMessageEvent, new LookToMessageEvent());
        this.getMessages().put(Events.UserBadgesMessageEvent, new UserBadgesMessageEvent());
        this.getMessages().put(Events.UpdatePapersMessageEvent, new UpdatePapersMessageEvent());*/
    }

    public void registerRoomTrade() {
        this.getMessages().put(Events.BeginTradeMessageEvent, new BeginTradeMessageEvent());
        this.getMessages().put(Events.CancelTradeMessageEvent, new CancelTradeMessageEvent());
        this.getMessages().put(Events.SendOfferMessageEvent, new SendOfferMessageEvent());
        this.getMessages().put(Events.CancelOfferMessageEvent, new CancelOfferMessageEvent());
        this.getMessages().put(Events.AcceptTradeMessageEvent, new AcceptTradeMessageEvent());
        this.getMessages().put(Events.ConfirmTradeMessageEvent, new ConfirmTradeMessageEvent());
    }

    public void registerRoomModeration() {
        this.getMessages().put(Events.KickUserMessageEvent, new KickUserMessageEvent());
        this.getMessages().put(Events.GiveRightsMessageEvent, new GiveRightsMessageEvent());
        this.getMessages().put(Events.RemoveAllRightsMessageEvent, new RemoveAllRightsMessageEvent());
        this.getMessages().put(Events.GetBannedUsersMessageEvent, new GetBannedUsersMessageEvent());
    }

    public void registerItems() {
        this.getMessages().put(Events.PlaceItemMessageEvent, new PlaceItemMessageEvent());
        this.getMessages().put(Events.ChangeFloorItemPositionMessageEvent, new ChangeFloorItemPositionMessageEvent());
        this.getMessages().put(Events.ChangeWallItemPositionMessageEvent, new ChangeWallItemPositionMessageEvent());
        this.getMessages().put(Events.PickUpItemMessageEvent, new PickUpItemMessageEvent());
        this.getMessages().put(Events.ChangeFloorItemStateMessageEvent, new ChangeFloorItemStateMessageEvent());
        this.getMessages().put(Events.OpenDiceMessageEvent, new OpenDiceMessageEvent());
        this.getMessages().put(Events.RunDiceMessageEvent, new RunDiceMessageEvent());
        this.getMessages().put(Events.SaveWiredTriggerMessageEvent, new SaveWiredMessageEvent());
        this.getMessages().put(Events.SaveWiredEffectMessageEvent, new SaveWiredMessageEvent());
        this.getMessages().put(Events.ExchangeItemMessageEvent, new ExchangeItemMessageEvent());
        this.getMessages().put(Events.UseWallItemMessageEvent, new UseWallItemMessageEvent());
        this.getMessages().put(Events.SaveMannequinMessageEvent, new SaveMannequinMessageEvent());
        this.getMessages().put(Events.SaveMannequinNameMessageEvent, new SaveMannequinNameMessageEvent());
        this.getMessages().put(Events.SaveTonerMessageEvent, new SaveTonerMessageEvent());
    }

    public void registerCatalog() {
        this.getMessages().put(Events.MarketplaceConfigurationMessageEvent, new MarketplaceConfigurationMessageEvent());
        /*this.getMessages().put(Events.GetCataIndexMessageEvent, new GetCataIndexMessageEvent());
        this.getMessages().put(Events.GetCataPageMessageEvent, new GetCataPageMessageEvent());
        this.getMessages().put(Events.PurchaseItemMessageEvent, new PurchaseItemMessageEvent());
        this.getMessages().put(Events.HabboClubPackagesMessageEvent, new HabboClubPackagesMessageEvent());
        this.getMessages().put(Events.CatalogData1MessageEvent, new GetShopDataMessageEvent());
        this.getMessages().put(Events.CatalogData2MessageEvent, new GetShopData2MessageEvent());
        this.getMessages().put(Events.BuyGroupDialogMessageEvent, new BuyGroupDialogMessageEvent());
        this.getMessages().put(Events.BuyGroupMessageEvent, new BuyGroupMessageEvent());
        this.getMessages().put(Events.PetRacesMessageEvent, new PetRacesMessageEvent());*/
    }

	public void handle(Event message, Session client) {
		try {
			Short header = message.getId();
			
			if(this.getMessages().containsKey(header)) {
                long start = System.currentTimeMillis();

                log.debug("Started packet process for packet: [" + Events.valueOfId(header) + "][" + header + "]");
                log.debug(message.toString());

				this.getMessages().get(header).handle(client, message);
                log.debug("Finished packet process for packet: [" + Events.valueOfId(header) + "][" + header + "] in " + ((System.currentTimeMillis() - start)) + "ms");
			} else {
				if(Events.valueOfId(header) == null || Events.valueOfId(header).equals("") && header != 2783) // 2783 = annoying ping header
                    log.debug("Unknown message ID: " + header);
                else if(header != 2783)
                    log.debug("Unhandled message: " + Events.valueOfId(header) + " / " + header);
			}
		} catch(Exception e) {
			log.error("Error while handling incoming message", e);
		}
	}
	
	public FastMap<Short, IEvent> getMessages() {
		return this.messages;
	}
}
