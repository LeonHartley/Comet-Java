package com.cometsrv.network.messages;

import com.cometsrv.network.messages.headers.Events;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.incoming.catalog.GetCataIndexMessageEvent;
import com.cometsrv.network.messages.incoming.catalog.GetCataPageMessageEvent;
import com.cometsrv.network.messages.incoming.catalog.HabboClubPackagesMessageEvent;
import com.cometsrv.network.messages.incoming.catalog.PurchaseItemMessageEvent;
import com.cometsrv.network.messages.incoming.catalog.data.GetShopData2MessageEvent;
import com.cometsrv.network.messages.incoming.catalog.data.GetShopDataMessageEvent;
import com.cometsrv.network.messages.incoming.catalog.groups.BuyGroupDialogMessageEvent;
import com.cometsrv.network.messages.incoming.catalog.groups.BuyGroupMessageEvent;
import com.cometsrv.network.messages.incoming.catalog.pets.PetRacesMessageEvent;
import com.cometsrv.network.messages.incoming.handshake.CheckReleaseMessageEvent;
import com.cometsrv.network.messages.incoming.handshake.GenerateSecretKeyMessageEvent;
import com.cometsrv.network.messages.incoming.handshake.InitCryptoMessageEvent;
import com.cometsrv.network.messages.incoming.handshake.SSOTicketMessageEvent;
import com.cometsrv.network.messages.incoming.help.HelpTicketMessageEvent;
import com.cometsrv.network.messages.incoming.help.InitHelpToolMessageEvent;
import com.cometsrv.network.messages.incoming.messenger.AcceptFriendshipMessageEvent;
import com.cometsrv.network.messages.incoming.messenger.PrivateChatMessageEvent;
import com.cometsrv.network.messages.incoming.messenger.RequestFriendshipMessageEvent;
import com.cometsrv.network.messages.incoming.moderation.ModToolBanUserMessageEvent;
import com.cometsrv.network.messages.incoming.moderation.ModToolRoomChatlogMessageEvent;
import com.cometsrv.network.messages.incoming.moderation.ModToolUserChatlogMessageEvent;
import com.cometsrv.network.messages.incoming.moderation.ModToolUserInfoMessageEvent;
import com.cometsrv.network.messages.incoming.navigator.*;
import com.cometsrv.network.messages.incoming.room.action.*;
import com.cometsrv.network.messages.incoming.room.bots.PlaceBotMessageEvent;
import com.cometsrv.network.messages.incoming.room.engine.AddUserToRoomMessageEvent;
import com.cometsrv.network.messages.incoming.room.engine.FollowRoomInfoMessageEvent;
import com.cometsrv.network.messages.incoming.room.engine.InitalizeRoomMessageEvent;
import com.cometsrv.network.messages.incoming.room.engine.LoadHeightmapMessageEvent;
import com.cometsrv.network.messages.incoming.room.item.*;
import com.cometsrv.network.messages.incoming.room.item.wired.SaveWiredMessageEvent;
import com.cometsrv.network.messages.incoming.room.moderation.GetBannedUsersMessageEvent;
import com.cometsrv.network.messages.incoming.room.moderation.GiveRightsMessageEvent;
import com.cometsrv.network.messages.incoming.room.moderation.KickUserMessageEvent;
import com.cometsrv.network.messages.incoming.room.moderation.RemoveAllRightsMessageEvent;
import com.cometsrv.network.messages.incoming.room.settings.LoadRoomInfoMessageEvent;
import com.cometsrv.network.messages.incoming.room.settings.SaveRoomDataMessageEvent;
import com.cometsrv.network.messages.incoming.room.settings.UsersWithRightsMessageEvent;
import com.cometsrv.network.messages.incoming.room.trading.*;
import com.cometsrv.network.messages.incoming.user.club.ClubStatusMessageEvent;
import com.cometsrv.network.messages.incoming.user.details.ChangeMottoMessageEvent;
import com.cometsrv.network.messages.incoming.user.details.UserInformationMessageEvent;
import com.cometsrv.network.messages.incoming.user.inventory.BadgeInventoryMessageEvent;
import com.cometsrv.network.messages.incoming.user.inventory.BotInventoryMessageEvent;
import com.cometsrv.network.messages.incoming.user.inventory.OpenInventoryMessageEvent;
import com.cometsrv.network.messages.incoming.user.profile.GetProfileMessageEvent;
import com.cometsrv.network.messages.incoming.user.profile.GetRelationshipsMessageEvent;
import com.cometsrv.network.messages.incoming.user.profile.SetRelationshipMessageEvent;
import com.cometsrv.network.messages.incoming.user.wardrobe.ChangeLooksMessageEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;
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
        this.registerModTool();
        this.registerHelpTool();
        this.registerMessenger();
        this.registerNavigator();
        this.registerUser();
        this.registerBots();
        this.registerRoom();
        this.registerRoomTrade();
        this.registerRoomModeration();
        this.registerItems();
        this.registerCatalog();

        log.info("Loaded " + this.getMessages().size() + " message events");
    }

	public void registerHandshake() {
        this.getMessages().put(Events.CheckReleaseMessageEvent, new CheckReleaseMessageEvent());
        this.getMessages().put(Events.InitCryptoMessageEvent, new InitCryptoMessageEvent());
        this.getMessages().put(Events.GenerateSecretKeyMessageEvent, new GenerateSecretKeyMessageEvent());
        this.getMessages().put(Events.SSOTicketMessageEvent, new SSOTicketMessageEvent());
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
        this.getMessages().put(Events.PopularRoomsMessageEvent, new PopularRoomsMessageEvent());
        this.getMessages().put(Events.LoadSearchRoomMessageEvent, new LoadSearchRoomMessageEvent());
        this.getMessages().put(Events.SearchRoomMessageEvent, new SearchRoomMessageEvent());
        this.getMessages().put(Events.CanCreateRoomMessageEvent, new CanCreateRoomMessageEvent());
        this.getMessages().put(Events.CreateNewRoomMessageEvent, new CreateRoomMessageEvent());
        this.getMessages().put(Events.LoadCategoriesMessageEvent, new LoadCategoriesMessageEvent());
    }

    public void registerUser() {
        this.getMessages().put(Events.GetProfileMessageEvent, new GetProfileMessageEvent());
        this.getMessages().put(Events.ClubStatusMessageEvent, new ClubStatusMessageEvent());
        this.getMessages().put(Events.UserInformationMessageEvent, new UserInformationMessageEvent());
        this.getMessages().put(Events.ChangeLooksMessageEvent, new ChangeLooksMessageEvent());
        this.getMessages().put(Events.OpenInventoryMessageEvent, new OpenInventoryMessageEvent());
        this.getMessages().put(Events.BadgeInventoryMessageEvent, new BadgeInventoryMessageEvent());
        this.getMessages().put(Events.ChangeMottoMessageEvent, new ChangeMottoMessageEvent());
        this.getMessages().put(Events.GetRelationshipsMessageEvent, new GetRelationshipsMessageEvent());
        this.getMessages().put(Events.SetRelationshipMessageEvent, new SetRelationshipMessageEvent());
    }

    public void registerBots() {
        this.getMessages().put(Events.BotInventoryMessageEvent, new BotInventoryMessageEvent());
        this.getMessages().put(Events.PlaceBotMessageEvent, new PlaceBotMessageEvent());
    }

    public void registerRoom() {
        this.getMessages().put(Events.InitalizeRoomMessageEvent, new InitalizeRoomMessageEvent());
        this.getMessages().put(Events.LoadHeightmapMessageEvent, new LoadHeightmapMessageEvent());
        this.getMessages().put(Events.AddUserToRoomMessageEvent, new AddUserToRoomMessageEvent());
        this.getMessages().put(Events.ExitRoomMessageEvent, new ExitRoomMessageEvent());
        this.getMessages().put(Events.TalkMessageEvent, new TalkMessageEvent());
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
    }

    public void registerCatalog() {
        this.getMessages().put(Events.GetCataIndexMessageEvent, new GetCataIndexMessageEvent());
        this.getMessages().put(Events.GetCataPageMessageEvent, new GetCataPageMessageEvent());
        this.getMessages().put(Events.PurchaseItemMessageEvent, new PurchaseItemMessageEvent());
        this.getMessages().put(Events.HabboClubPackagesMessageEvent, new HabboClubPackagesMessageEvent());
        this.getMessages().put(Events.CatalogData1MessageEvent, new GetShopDataMessageEvent());
        this.getMessages().put(Events.CatalogData2MessageEvent, new GetShopData2MessageEvent());
        this.getMessages().put(Events.BuyGroupDialogMessageEvent, new BuyGroupDialogMessageEvent());
        this.getMessages().put(Events.BuyGroupMessageEvent, new BuyGroupMessageEvent());
        this.getMessages().put(Events.PetRacesMessageEvent, new PetRacesMessageEvent());
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
				if(Events.valueOfId(header) == null || Events.valueOfId(header).equals("") && header != 2906) // 2906 = annoying ping header
                    log.debug("Unknown message ID: " + header);
                else if(header != 2906)
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
