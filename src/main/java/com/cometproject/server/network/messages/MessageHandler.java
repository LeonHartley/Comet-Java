package com.cometproject.server.network.messages;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.headers.Events;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.incoming.catalog.GetCataIndexMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.GetCataPageMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.PurchaseGiftMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.PurchaseItemMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.data.GetShopData2MessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.data.GetShopDataMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupDialogMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.GroupFurnitureCatalogMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.pets.PetRacesMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.pets.ValidatePetNameMessageEvent;
import com.cometproject.server.network.messages.incoming.group.*;
import com.cometproject.server.network.messages.incoming.group.favourite.SetFavouriteGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.GroupForumPermissionsMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.GroupForumThreadsMessageEvent;
import com.cometproject.server.network.messages.incoming.group.settings.ManageGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.group.settings.ModifyGroupBadgeMessageEvent;
import com.cometproject.server.network.messages.incoming.group.settings.ModifyGroupSettingsMessageEvent;
import com.cometproject.server.network.messages.incoming.group.settings.ModifyGroupTitleMessageEvent;
import com.cometproject.server.network.messages.incoming.handshake.CheckReleaseMessageEvent;
import com.cometproject.server.network.messages.incoming.handshake.GenerateSecretKeyMessageEvent;
import com.cometproject.server.network.messages.incoming.handshake.InitCryptoMessageEvent;
import com.cometproject.server.network.messages.incoming.handshake.SSOTicketMessageEvent;
import com.cometproject.server.network.messages.incoming.help.HelpTicketMessageEvent;
import com.cometproject.server.network.messages.incoming.help.InitHelpToolMessageEvent;
import com.cometproject.server.network.messages.incoming.landing.HotelViewItemMessageEvent;
import com.cometproject.server.network.messages.incoming.landing.RefreshPromoArticlesMessageEvent;
import com.cometproject.server.network.messages.incoming.messenger.*;
import com.cometproject.server.network.messages.incoming.moderation.*;
import com.cometproject.server.network.messages.incoming.navigator.*;
import com.cometproject.server.network.messages.incoming.room.access.AnswerDoorbellMessageEvent;
import com.cometproject.server.network.messages.incoming.room.action.*;
import com.cometproject.server.network.messages.incoming.room.bots.BotConfigMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.ModifyBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.PlaceBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.RemoveBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.AddUserToRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.FollowRoomInfoMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.InitalizeRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.LoadHeightmapMessageEvent;
import com.cometproject.server.network.messages.incoming.room.floor.SaveFloorMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.*;
import com.cometproject.server.network.messages.incoming.room.item.gifts.OpenGiftMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinNameMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.wired.SaveWiredMessageEvent;
import com.cometproject.server.network.messages.incoming.room.moderation.*;
import com.cometproject.server.network.messages.incoming.room.pets.PetInformationMessageEvent;
import com.cometproject.server.network.messages.incoming.room.pets.PlacePetMessageEvent;
import com.cometproject.server.network.messages.incoming.room.pets.RemovePetMessageEvent;
import com.cometproject.server.network.messages.incoming.room.settings.*;
import com.cometproject.server.network.messages.incoming.room.trading.*;
import com.cometproject.server.network.messages.incoming.user.club.ClubStatusMessageEvent;
import com.cometproject.server.network.messages.incoming.user.details.*;
import com.cometproject.server.network.messages.incoming.user.inventory.BadgeInventoryMessageEvent;
import com.cometproject.server.network.messages.incoming.user.inventory.BotInventoryMessageEvent;
import com.cometproject.server.network.messages.incoming.user.inventory.OpenInventoryMessageEvent;
import com.cometproject.server.network.messages.incoming.user.inventory.PetInventoryMessageEvent;
import com.cometproject.server.network.messages.incoming.user.profile.GetProfileMessageEvent;
import com.cometproject.server.network.messages.incoming.user.profile.GetRelationshipsMessageEvent;
import com.cometproject.server.network.messages.incoming.user.profile.SetRelationshipMessageEvent;
import com.cometproject.server.network.messages.incoming.user.profile.WearBadgeMessageEvent;
import com.cometproject.server.network.messages.incoming.user.username.ChangeUsernameCheckMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.ChangeLooksMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.SaveWardrobeMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.WardrobeMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.LoadPlaylistMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.NextVideoMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.PlayVideoMessageEvent;
import com.cometproject.server.network.messages.outgoing.room.access.LoadRoomByDoorBellMessageEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

public final class MessageHandler {
    public static Logger log = Logger.getLogger(MessageHandler.class.getName());
    private final FastMap<Short, IEvent> messages = new FastMap<>();

    public MessageHandler() {
        this.load();
    }

    public void load() {
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
        this.registerRoomAccess();
        this.registerItems();
        this.registerCatalog();
        this.registerPets();
        this.registerLanding();
        this.registerGroups();
        this.registerQuests();

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
        this.getMessages().put(Events.ModToolRoomInfoMessageEvent, new ModToolRoomInfoMessageEvent());
        this.getMessages().put(Events.ModToolRoomVisitsMessageEvent, new ModToolRoomVisitsMessageEvent());
        this.getMessages().put(Events.ModToolUserAlertMessageEvent, new ModToolUserAlertMessageEvent());
        this.getMessages().put(Events.ModToolUserCautionMessageEvent, new ModToolUserCautionMessageEvent());
        this.getMessages().put(Events.ModToolUserKickMessageEvent, new ModToolUserKickMessageEvent());
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
        this.getMessages().put(Events.SearchFriendsMessageEvent, new SearchFriendsMessageEvent());
        this.getMessages().put(Events.FollowFriendMessageEvent, new FollowFriendMessageEvent());
        this.getMessages().put(Events.DeleteFriendsMessageEvent, new DeleteFriendsMessageEvent());
        this.getMessages().put(Events.InviteFriendsMessageEvent, new InviteFriendsMessageEvent());
    }

    public void registerNavigator() {
        this.getMessages().put(Events.OwnRoomsMessageEvent, new OwnRoomsMessageEvent());
        this.getMessages().put(Events.PopularRoomsMessageEvent, new PopularRoomsMessageEvent());
        this.getMessages().put(Events.LoadSearchRoomMessageEvent, new LoadSearchRoomMessageEvent());
        this.getMessages().put(Events.CanCreateRoomMessageEvent, new CanCreateRoomMessageEvent());
        this.getMessages().put(Events.SearchRoomMessageEvent, new SearchRoomMessageEvent());
        this.getMessages().put(Events.CreateNewRoomMessageEvent, new CreateRoomMessageEvent());
        this.getMessages().put(Events.FeaturedRoomsMessageEvent, new FeaturedRoomsMessageEvent());
        this.getMessages().put(Events.AddToStaffPickedRoomsMessageEvent, new AddToStaffPickedRoomsMessageEvent());
        this.getMessages().put(Events.PromotedRoomsMessageEvent, new PromotedRoomsMessageEvent());
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
        this.getMessages().put(Events.WearBadgeMessageEvent, new WearBadgeMessageEvent());
        this.getMessages().put(Events.WardrobeMessageEvent, new WardrobeMessageEvent());
        this.getMessages().put(Events.SaveWardrobeMessageEvent, new SaveWardrobeMessageEvent());
        this.getMessages().put(Events.ChangeHomeRoomMessageEvent, new ChangeHomeRoomMessageEvent());
        this.getMessages().put(Events.ChangeUsernameCheckMessageEvent, new ChangeUsernameCheckMessageEvent());
        this.getMessages().put(Events.LoadPlaylistMessageEvent, new LoadPlaylistMessageEvent());
        this.getMessages().put(Events.PlayVideoMessageEvent, new PlayVideoMessageEvent());
        this.getMessages().put(Events.NextVideoMessageEvent, new NextVideoMessageEvent());
        this.getMessages().put(Events.UpdateAudioSettingsMessageEvent, new UpdateAudioSettingsMessageEvent());
        this.getMessages().put(Events.UpdateChatStyleMessageEvent, new UpdateChatStyleMessageEvent());
    }

    public void registerBots() {
        this.getMessages().put(Events.BotInventoryMessageEvent, new BotInventoryMessageEvent());
        this.getMessages().put(Events.PlaceBotMessageEvent, new PlaceBotMessageEvent());
        this.getMessages().put(Events.ModifyBotMessageEvent, new ModifyBotMessageEvent());
        this.getMessages().put(Events.RemoveBotMessageEvent, new RemoveBotMessageEvent());
        this.getMessages().put(Events.BotConfigMessageEvent, new BotConfigMessageEvent());
    }

    public void registerPets() {
        this.getMessages().put(Events.PetInventoryMessageEvent, new PetInventoryMessageEvent());
        this.getMessages().put(Events.PlacePetMessageEvent, new PlacePetMessageEvent());
        this.getMessages().put(Events.PetInformationMessageEvent, new PetInformationMessageEvent());
        this.getMessages().put(Events.RemovePetMessageEvent, new RemovePetMessageEvent());
    }

    public void registerRoom() {
        this.getMessages().put(Events.InitalizeRoomMessageEvent, new InitalizeRoomMessageEvent());
        this.getMessages().put(Events.LoadHeightmapMessageEvent, new LoadHeightmapMessageEvent());
        this.getMessages().put(Events.AddUserToRoomMessageEvent, new AddUserToRoomMessageEvent());
        this.getMessages().put(Events.AddUserToRoom2MessageEvent, new AddUserToRoomMessageEvent());
        this.getMessages().put(Events.ExitRoomMessageEvent, new ExitRoomMessageEvent());
        this.getMessages().put(Events.TalkMessageEvent, new TalkMessageEvent());
        this.getMessages().put(Events.ShoutMessageEvent, new ShoutMessageEvent());
        this.getMessages().put(Events.WhisperMessageEvent, new WhisperMessageEvent());
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
        this.getMessages().put(Events.UpdatePapersMessageEvent, new UpdatePapersMessageEvent());
        this.getMessages().put(Events.DropHandItemMessageEvent, new DropHandItemMessageEvent());
        this.getMessages().put(Events.DeleteRoomMessageEvent, new DeleteRoomMessageEvent());
        this.getMessages().put(Events.MuteRoomMessageEvent, new MuteRoomMessageEvent());
        this.getMessages().put(Events.SaveFloorMessageEvent, new SaveFloorMessageEvent());
        this.getMessages().put(Events.RateRoomMessageEvent, new RateRoomMessageEvent());
        this.getMessages().put(Events.GiveHandItemMessageEvent, new GiveHandItemMessageEvent());
    }

    public void registerRoomTrade() {
        this.getMessages().put(Events.BeginTradeMessageEvent, new BeginTradeMessageEvent());
        this.getMessages().put(Events.CancelTradeMessageEvent, new CancelTradeMessageEvent());
        this.getMessages().put(Events.CancelTradeButtonMessageEvent, new CancelTradeMessageEvent());
        this.getMessages().put(Events.SendOfferMessageEvent, new SendOfferMessageEvent());
        this.getMessages().put(Events.CancelOfferMessageEvent, new CancelOfferMessageEvent());
        this.getMessages().put(Events.AcceptTradeMessageEvent, new AcceptTradeMessageEvent());
        this.getMessages().put(Events.ConfirmTradeMessageEvent, new ConfirmTradeMessageEvent());
    }

    public void registerRoomModeration() {
        this.getMessages().put(Events.KickUserMessageEvent, new KickUserMessageEvent());
        this.getMessages().put(Events.GiveRightsMessageEvent, new GiveRightsMessageEvent());
        this.getMessages().put(Events.RemoveRightsMessageEvent, new RemoveRightsMessageEvent());
        this.getMessages().put(Events.RemoveAllRightsMessageEvent, new RemoveAllRightsMessageEvent());
        this.getMessages().put(Events.GetBannedUsersMessageEvent, new GetBannedUsersMessageEvent());
    }

    public void registerRoomAccess() {
        this.getMessages().put(Events.AnswerDoorBellMessageEvent, new AnswerDoorbellMessageEvent());
        this.getMessages().put(Events.LoadRoomByDoorBellMessageEvent, new LoadRoomByDoorBellMessageEvent());
    }

    public void registerItems() {
        this.getMessages().put(Events.PlaceItemMessageEvent, new PlaceItemMessageEvent());
        this.getMessages().put(Events.ChangeFloorItemPositionMessageEvent, new ChangeFloorItemPositionMessageEvent());
        this.getMessages().put(Events.ChangeWallItemPositionMessageEvent, new ChangeWallItemPositionMessageEvent());
        this.getMessages().put(Events.PickUpItemMessageEvent, new PickUpItemMessageEvent());
        this.getMessages().put(Events.ChangeFloorItemStateMessageEvent, new ChangeFloorItemStateMessageEvent());
        this.getMessages().put(Events.OneWayGateTriggerMessageEvent, new ChangeFloorItemStateMessageEvent());
        this.getMessages().put(Events.OpenDiceMessageEvent, new OpenDiceMessageEvent());
        this.getMessages().put(Events.RunDiceMessageEvent, new RunDiceMessageEvent());
        this.getMessages().put(Events.SaveWiredTriggerMessageEvent, new SaveWiredMessageEvent());
        this.getMessages().put(Events.SaveWiredEffectMessageEvent, new SaveWiredMessageEvent());
        this.getMessages().put(Events.ExchangeItemMessageEvent, new ExchangeItemMessageEvent());
        this.getMessages().put(Events.UseWallItemMessageEvent, new UseWallItemMessageEvent());
        this.getMessages().put(Events.SaveMannequinMessageEvent, new SaveMannequinMessageEvent());
        this.getMessages().put(Events.SaveMannequinNameMessageEvent, new SaveMannequinNameMessageEvent());
        this.getMessages().put(Events.SaveTonerMessageEvent, new SaveTonerMessageEvent());
        this.getMessages().put(Events.SaveBrandingMessageEvent, new SaveBrandingMessageEvent());
        this.getMessages().put(Events.ChangeWallItemStateMessageEvent, new ChangeWallItemStateMessageEvent());
        this.getMessages().put(Events.OpenGiftMessageEvent, new OpenGiftMessageEvent());
        this.getMessages().put(Events.UseMoodlightMessageEvent, new UseMoodlightMessageEvent());
        this.getMessages().put(Events.ToggleMoodlightMessageEvent, new ToggleMoodlightMessageEvent());
        this.getMessages().put(Events.UpdateMoodlightMessageEvent, new UpdateMoodlightMessageEvent());
        this.getMessages().put(Events.SaveStackToolMessageEvent, new SaveStackToolMessageEvent());
    }

    public void registerCatalog() {
        this.getMessages().put(Events.GetCataIndexMessageEvent, new GetCataIndexMessageEvent());
        this.getMessages().put(Events.GetCataPageMessageEvent, new GetCataPageMessageEvent());
        this.getMessages().put(Events.PurchaseItemMessageEvent, new PurchaseItemMessageEvent());
        this.getMessages().put(Events.CatalogData1MessageEvent, new GetShopDataMessageEvent());
        this.getMessages().put(Events.CatalogData2MessageEvent, new GetShopData2MessageEvent());
        this.getMessages().put(Events.BuyGroupDialogMessageEvent, new BuyGroupDialogMessageEvent());
        this.getMessages().put(Events.BuyGroupMessageEvent, new BuyGroupMessageEvent());
        this.getMessages().put(Events.PetRacesMessageEvent, new PetRacesMessageEvent());
        this.getMessages().put(Events.ValidatePetNameMessageEvent, new ValidatePetNameMessageEvent());
        this.getMessages().put(Events.PurchaseGiftMessageEvent, new PurchaseGiftMessageEvent());
        this.getMessages().put(Events.GroupFurnitureCatalogMessageEvent, new GroupFurnitureCatalogMessageEvent());
    }

    public void registerLanding() {
        this.getMessages().put(Events.RefreshPromoArticlesMessageEvent, new RefreshPromoArticlesMessageEvent());
        this.getMessages().put(Events.HotelViewItemMessageEvent, new HotelViewItemMessageEvent());
    }

    public void registerGroups() {
        this.getMessages().put(Events.GroupInformationMessageEvent, new GroupInformationMessageEvent());
        this.getMessages().put(Events.GroupMembersMessageEvent, new GroupMembersMessageEvent());
        this.getMessages().put(Events.ManageGroupMessageEvent, new ManageGroupMessageEvent());
        this.getMessages().put(Events.RevokeMembershipMessageEvent, new RevokeMembershipMessageEvent());
        this.getMessages().put(Events.JoinGroupMessageEvent, new JoinGroupMessageEvent());
        this.getMessages().put(Events.ModifyGroupTitleMessageEvent, new ModifyGroupTitleMessageEvent());
        this.getMessages().put(Events.RevokeGroupAdminMessageEvent, new RevokeAdminMessageEvent());
        this.getMessages().put(Events.GiveGroupAdminMessageEvent, new GiveGroupAdminMessageEvent());
        this.getMessages().put(Events.ModifyGroupSettingsMessageEvent, new ModifyGroupSettingsMessageEvent());
        this.getMessages().put(Events.AcceptMembershipMessageEvent, new AcceptMembershipMessageEvent());
        this.getMessages().put(Events.ModifyGroupBadgeMessageEvent, new ModifyGroupBadgeMessageEvent());
        this.getMessages().put(Events.SetFavouriteGroupMessageEvent, new SetFavouriteGroupMessageEvent());

//        this.getMessages().put(Events.GroupForumPermissionsMessageEvent, new GroupForumPermissionsMessageEvent());
//        this.getMessages().put(Events.GroupForumThreadsMessageEvent, new GroupForumThreadsMessageEvent());
    }

    public void registerQuests() {
        //this.getMessages().put(Events.OpenQuestsMessageEvent, new OpenQuestsMessageEvent());
    }

    public void handle(Event message, Session client) {
        final Short header = message.getId();

        if (this.getMessages().containsKey(header)) {
            final long start = System.currentTimeMillis();

            log.debug("Started packet process for packet: [" + Events.valueOfId(header) + "][" + header + "]");

            try {
                this.getMessages().get(header).handle(client, message);

                log.debug("Finished packet process for packet: [" + Events.valueOfId(header) + "][" + header + "] in " + ((System.currentTimeMillis() - start)) + "ms");
            } catch (Exception e) {
                log.error("Error while handling event: " + this.getMessages().get(header).getClass().getName(), e);
            }
        } else if(Comet.isDebugging) {
            log.debug("Unhandled message: " + Events.valueOfId(header) + " / " + header);
        }
    }

    public FastMap<Short, IEvent> getMessages() {
        return this.messages;
    }
}
