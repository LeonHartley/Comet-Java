package com.cometproject.server.network.messages;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.incoming.catalog.*;
import com.cometproject.server.network.messages.incoming.catalog.ads.CatalogPromotionGetRoomsMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.ads.PromoteRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.ads.PromotionUpdateMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.data.CatalogOfferConfigMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.data.GetGiftWrappingConfigurationMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupDialogMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.GroupFurnitureCatalogMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.pets.PetRacesMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.pets.ValidatePetNameMessageEvent;
import com.cometproject.server.network.messages.incoming.group.*;
import com.cometproject.server.network.messages.incoming.group.favourite.ClearFavouriteGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.group.favourite.SetFavouriteGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.data.ForumDataMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.data.GetForumsMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.threads.*;
import com.cometproject.server.network.messages.incoming.group.forum.settings.SaveForumSettingsMessageEvent;
import com.cometproject.server.network.messages.incoming.group.settings.*;
import com.cometproject.server.network.messages.incoming.handshake.*;
import com.cometproject.server.network.messages.incoming.help.DeletePendingTicketMessageEvent;
import com.cometproject.server.network.messages.incoming.help.HelpTicketMessageEvent;
import com.cometproject.server.network.messages.incoming.help.InitHelpToolMessageEvent;
import com.cometproject.server.network.messages.incoming.help.tool.OpenGuideToolMessageEvent;
import com.cometproject.server.network.messages.incoming.landing.LandingLoadWidgetMessageEvent;
import com.cometproject.server.network.messages.incoming.landing.RefreshPromoArticlesMessageEvent;
import com.cometproject.server.network.messages.incoming.latency.RequestLatencyTestMessageEvent;
import com.cometproject.server.network.messages.incoming.messenger.*;
import com.cometproject.server.network.messages.incoming.moderation.*;
import com.cometproject.server.network.messages.incoming.moderation.tickets.ModToolCloseIssueMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.tickets.ModToolPickTicketMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.tickets.ModToolReleaseIssueMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.tickets.ModToolTicketChatlogMessageEvent;
import com.cometproject.server.network.messages.incoming.music.SongDataMessageEvent;
import com.cometproject.server.network.messages.incoming.music.SongIdMessageEvent;
import com.cometproject.server.network.messages.incoming.music.playlist.PlaylistAddMessageEvent;
import com.cometproject.server.network.messages.incoming.music.playlist.PlaylistMessageEvent;
import com.cometproject.server.network.messages.incoming.music.playlist.PlaylistRemoveMessageEvent;
import com.cometproject.server.network.messages.incoming.navigator.*;
import com.cometproject.server.network.messages.incoming.quests.CancelQuestMessageEvent;
import com.cometproject.server.network.messages.incoming.quests.NextQuestMessageEvent;
import com.cometproject.server.network.messages.incoming.quests.OpenQuestsMessageEvent;
import com.cometproject.server.network.messages.incoming.quests.StartQuestMessageEvent;
import com.cometproject.server.network.messages.incoming.room.access.AnswerDoorbellMessageEvent;
import com.cometproject.server.network.messages.incoming.room.access.LoadRoomByDoorBellMessageEvent;
import com.cometproject.server.network.messages.incoming.room.action.*;
import com.cometproject.server.network.messages.incoming.room.bots.BotConfigMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.ModifyBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.PlaceBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.RemoveBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.FollowRoomInfoMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.InitializeRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.room.filter.EditWordFilterMessageEvent;
import com.cometproject.server.network.messages.incoming.room.filter.WordFilterListMessageEvent;
import com.cometproject.server.network.messages.incoming.room.floor.GetFloorPlanDoorMessageEvent;
import com.cometproject.server.network.messages.incoming.room.floor.GetTilesInUseMessageEvent;
import com.cometproject.server.network.messages.incoming.room.floor.SaveFloorMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.*;
import com.cometproject.server.network.messages.incoming.room.item.gifts.OpenGiftMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.lovelock.ConfirmLoveLockMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinFigureMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.DeletePostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.OpenPostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.PlacePostitMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.SavePostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.wired.SaveWiredDataMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.wired.UpdateSnapshotsMessageEvent;
import com.cometproject.server.network.messages.incoming.room.moderation.*;
import com.cometproject.server.network.messages.incoming.room.pets.*;
import com.cometproject.server.network.messages.incoming.room.settings.*;
import com.cometproject.server.network.messages.incoming.room.trading.*;
import com.cometproject.server.network.messages.incoming.user.achievements.AchievementsListMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.CameraTokenMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.RenderRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.TakePhotoMessageEvent;
import com.cometproject.server.network.messages.incoming.user.citizenship.CitizenshipStatusMessageEvent;
import com.cometproject.server.network.messages.incoming.user.club.ClubStatusMessageEvent;
import com.cometproject.server.network.messages.incoming.user.details.*;
import com.cometproject.server.network.messages.incoming.user.inventory.*;
import com.cometproject.server.network.messages.incoming.user.profile.*;
import com.cometproject.server.network.messages.incoming.user.wardrobe.ChangeLooksMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.SaveWardrobeMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.WardrobeMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.LoadPlaylistMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.NextVideoMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.PlayVideoMessageEvent;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.messages.types.tasks.MessageEventTask;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.headers.Events;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.*;

public final class MessageHandler {
    public static Logger log = Logger.getLogger(MessageHandler.class.getName());
    private final Map<Short, Event> messages = new ConcurrentHashMap<>();

    private final AbstractExecutorService eventExecutor;
    private final boolean asyncEventExecution;

    public MessageHandler() {
        this.asyncEventExecution = Boolean.parseBoolean((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.enabled", "false"));
//        this.eventExecutor = asyncEventExecution ? Executors.newFixedThreadPool(Integer.parseInt((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.threads", "8"))) : null;

        if (this.asyncEventExecution) {
            switch ((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.type", "threadpool")) {
                default:
                    log.info("Using thread-pool event executor");
                    this.eventExecutor = new ThreadPoolExecutor(Integer.parseInt((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.coreSize", "8")), // core size
                            Integer.parseInt((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.maxSize", "32")), // max size
                            10 * 60, // idle timeout
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>());
                    break;

                case "forkjoin":
                    log.info("Using fork-join event executor");
                    this.eventExecutor = new ForkJoinPool(Integer.parseInt((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.coreSize", 16)), ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
                    break;
            }
        } else {
            this.eventExecutor = null;
        }

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
        this.registerGroupForums();
        this.registerQuests();
        this.registerCamera();
        this.registerPromotions();
        this.registerMusic();
        this.registerAchievements();
        this.registerMisc();

        log.info("Loaded " + this.getMessages().size() + " message events");
    }

    private void registerMisc() {
        this.getMessages().put(Events.RequestLatencyTestMessageEvent, new RequestLatencyTestMessageEvent());
    }

    public void registerHandshake() {
        this.getMessages().put(Events.ClientVersionMessageEvent, new CheckReleaseMessageEvent());
        this.getMessages().put(Events.InitCryptoMessageEvent, new InitCryptoMessageEvent());
        this.getMessages().put(Events.GenerateSecretKeyMessageEvent, new GenerateSecretKeyMessageEvent());
        this.getMessages().put(Events.SSOTicketMessageEvent, new SSOTicketMessageEvent());
        this.getMessages().put(Events.UniqueIDMessageEvent, new UniqueIdMessageEvent());
    }

    public void registerModTool() {
        this.getMessages().put(Events.ModerationToolUserToolMessageEvent, new ModToolUserInfoMessageEvent());
        this.getMessages().put(Events.ModerationToolUserChatlogMessageEvent, new ModToolUserChatlogMessageEvent());
        this.getMessages().put(Events.ModerationToolRoomChatlogMessageEvent, new ModToolRoomChatlogMessageEvent());
        this.getMessages().put(Events.ModerationBanUserMessageEvent, new ModToolBanUserMessageEvent());
        this.getMessages().put(Events.ModerationToolRoomToolMessageEvent, new ModToolRoomInfoMessageEvent());
        this.getMessages().put(Events.ModerationToolGetRoomVisitsMessageEvent, new ModToolRoomVisitsMessageEvent());
        this.getMessages().put(Events.ModerationToolSendUserAlertMessageEvent, new ModToolUserAlertMessageEvent());
        this.getMessages().put(Events.ModerationToolSendUserCautionMessageEvent, new ModToolUserCautionMessageEvent());
        this.getMessages().put(Events.ModerationKickUserMessageEvent, new ModToolUserKickMessageEvent());
        this.getMessages().put(Events.ModerationToolSendRoomAlertMessageEvent, new ModToolRoomAlertMessageEvent());
        this.getMessages().put(Events.ModerationToolPerformRoomActionMessageEvent, new ModToolRoomActionMessageEvent());
        this.getMessages().put(Events.ModerationToolPickTicketMessageEvent, new ModToolPickTicketMessageEvent());
        this.getMessages().put(Events.ModerationToolTicketChatlogMessageEvent, new ModToolTicketChatlogMessageEvent());
        this.getMessages().put(Events.ModerationToolCloseIssueMessageEvent, new ModToolCloseIssueMessageEvent());
        this.getMessages().put(Events.ModerationToolReleaseIssueMessageEvent, new ModToolReleaseIssueMessageEvent());
    }

    public void registerHelpTool() {
        this.getMessages().put(Events.OpenHelpToolMessageEvent, new InitHelpToolMessageEvent());
        this.getMessages().put(Events.SubmitHelpTicketMessageEvent, new HelpTicketMessageEvent());
        this.getMessages().put(Events.DeletePendingTicketMessageEvent, new DeletePendingTicketMessageEvent());

        this.getMessages().put(Events.OpenGuideToolMessageEvent, new OpenGuideToolMessageEvent());
    }

    public void registerMessenger() {
        this.getMessages().put(Events.InitializeFriendListMessageEvent, new InitializeFriendListMessageEvent());
        this.getMessages().put(Events.ConsoleInstantChatMessageEvent, new PrivateChatMessageEvent());
        this.getMessages().put(Events.RequestFriendMessageEvent, new RequestFriendshipMessageEvent());
        this.getMessages().put(Events.AcceptFriendMessageEvent, new AcceptFriendshipMessageEvent());
        this.getMessages().put(Events.ConsoleSearchFriendsMessageEvent, new SearchFriendsMessageEvent());
        this.getMessages().put(Events.FollowFriendMessageEvent, new FollowFriendMessageEvent());
        this.getMessages().put(Events.DeleteFriendMessageEvent, new DeleteFriendsMessageEvent());
        this.getMessages().put(Events.ConsoleInviteFriendsMessageEvent, new InviteFriendsMessageEvent());
        this.getMessages().put(Events.DeclineFriendMessageEvent, new DeclineFriendshipMessageEvent());
    }

    public void registerNavigator() {
        this.getMessages().put(Events.NavigatorGetMyRoomsMessageEvent, new OwnRoomsMessageEvent());
        this.getMessages().put(Events.NavigatorGetPopularRoomsMessageEvent, new PopularRoomsMessageEvent());
        this.getMessages().put(Events.NavigatorGetPopularTagsMessageEvent, new LoadSearchRoomMessageEvent());
        this.getMessages().put(Events.CanCreateRoomMessageEvent, new CanCreateRoomMessageEvent());
        this.getMessages().put(Events.NavigatorSearchRoomByNameMessageEvent, new SearchRoomMessageEvent());
        this.getMessages().put(Events.CreateRoomMessageEvent, new CreateRoomMessageEvent());
        this.getMessages().put(Events.NavigatorGetFeaturedRoomsMessageEvent, new FeaturedRoomsMessageEvent());
        this.getMessages().put(Events.AddToStaffPickedRoomsMessageEvent, new AddToStaffPickedRoomsMessageEvent());
        this.getMessages().put(Events.NavigatorGetEventsMessageEvent, new PromotedRoomsMessageEvent());
        this.getMessages().put(Events.NavigatorGetFlatCategoriesMessageEvent, new LoadCategoriesMessageEvent());
        this.getMessages().put(Events.NavigatorGetHighRatedRoomsMessageEvent, new NavigatorGetHighRatedRoomsMessageEvent());
    }

    public void registerUser() {
        this.getMessages().put(Events.RetrieveCitizenshipStatus, new CitizenshipStatusMessageEvent());
        this.getMessages().put(Events.LoadUserProfileMessageEvent, new GetProfileMessageEvent());
        this.getMessages().put(Events.LoadProfileByUsernameMessageEvent, new GetProfileByUsernameMessageEvent());
        this.getMessages().put(Events.GetSubscriptionDataMessageEvent, new ClubStatusMessageEvent());
        this.getMessages().put(Events.InfoRetrieveMessageEvent, new InfoRetrieveMessageEvent());
        this.getMessages().put(Events.UserUpdateLookMessageEvent, new ChangeLooksMessageEvent());
        this.getMessages().put(Events.LoadItemsInventoryMessageEvent, new OpenInventoryMessageEvent());
        this.getMessages().put(Events.LoadBadgeInventoryMessageEvent, new BadgeInventoryMessageEvent());
        this.getMessages().put(Events.UserUpdateMottoMessageEvent, new ChangeMottoMessageEvent());
        this.getMessages().put(Events.RelationshipsGetMessageEvent, new GetRelationshipsMessageEvent());
        this.getMessages().put(Events.SetRelationshipMessageEvent, new SetRelationshipMessageEvent());
        this.getMessages().put(Events.SetActivatedBadgesMessageEvent, new WearBadgeMessageEvent());
        this.getMessages().put(Events.WardrobeMessageEvent, new WardrobeMessageEvent());
        this.getMessages().put(Events.WardrobeUpdateMessageEvent, new SaveWardrobeMessageEvent());
        this.getMessages().put(Events.SetHomeRoomMessageEvent, new ChangeHomeRoomMessageEvent());
        this.getMessages().put(Events.SaveClientSettingsMessageEvent, new UpdateAudioSettingsMessageEvent());
        this.getMessages().put(Events.SetChatPreferenceMessageEvent, new UpdateChatStyleMessageEvent());
        this.getMessages().put(Events.IgnoreInvitationsMessageEvent, new IgnoreInvitationsMessageEvent());
    }

    public void registerBots() {
        this.getMessages().put(Events.LoadBotInventoryMessageEvent, new BotInventoryMessageEvent());
        this.getMessages().put(Events.PlaceBotMessageEvent, new PlaceBotMessageEvent());
        this.getMessages().put(Events.BotActionsMessageEvent, new ModifyBotMessageEvent());
        this.getMessages().put(Events.PickUpBotMessageEvent, new RemoveBotMessageEvent());
        this.getMessages().put(Events.BotSpeechListMessageEvent, new BotConfigMessageEvent());
    }

    public void registerPets() {
        this.getMessages().put(Events.LoadPetInventoryMessageEvent, new PetInventoryMessageEvent());
        this.getMessages().put(Events.PlacePetMessageEvent, new PlacePetMessageEvent());
        this.getMessages().put(Events.PetGetInformationMessageEvent, new PetInformationMessageEvent());
        this.getMessages().put(Events.PickUpPetMessageEvent, new RemovePetMessageEvent());
        this.getMessages().put(Events.HorseMountOnMessageEvent, new HorseMountOnMessageEvent());
        this.getMessages().put(Events.RespectPetMessageEvent, new RespectPetMessageEvent());
    }

    public void registerRoom() {
        this.getMessages().put(Events.EnterPrivateRoomMessageEvent, new InitializeRoomMessageEvent());
        this.getMessages().put(Events.RoomGetInfoMessageEvent, new FollowRoomInfoMessageEvent());
        this.getMessages().put(Events.GoToHotelViewMessageEvent, new ExitRoomMessageEvent());
        this.getMessages().put(Events.ChatMessageEvent, new TalkMessageEvent());
        this.getMessages().put(Events.ShoutMessageEvent, new ShoutMessageEvent());
        this.getMessages().put(Events.UserWhisperMessageEvent, new WhisperMessageEvent());
        this.getMessages().put(Events.UserWalkMessageEvent, new WalkMessageEvent());
        this.getMessages().put(Events.RoomUserActionMessageEvent, new ApplyActionMessageEvent());
        this.getMessages().put(Events.UserSignMessageEvent, new ApplySignMessageEvent());
        this.getMessages().put(Events.UserDanceMessageEvent, new ApplyDanceMessageEvent());
        this.getMessages().put(Events.RoomGetSettingsInfoMessageEvent, new LoadRoomInfoMessageEvent());
        this.getMessages().put(Events.GetRoomRightsListMessageEvent, new UsersWithRightsMessageEvent());
        this.getMessages().put(Events.RoomSaveSettingsMessageEvent, new SaveRoomDataMessageEvent());
        this.getMessages().put(Events.GiveRespectMessageEvent, new RespectUserMessageEvent());
        this.getMessages().put(Events.StartTypingMessageEvent, new StartTypingMessageEvent());
        this.getMessages().put(Events.StopTypingMessageEvent, new StopTypingMessageEvent());
        this.getMessages().put(Events.LookAtUserMessageEvent, new LookToMessageEvent());
        this.getMessages().put(Events.GetUserBadgesMessageEvent, new UserBadgesMessageEvent());
        this.getMessages().put(Events.RoomApplySpaceMessageEvent, new UpdatePapersMessageEvent());
        this.getMessages().put(Events.DropHanditemMessageEvent, new DropHandItemMessageEvent());
        this.getMessages().put(Events.RoomDeleteMessageEvent, new DeleteRoomMessageEvent());
        this.getMessages().put(Events.RoomSettingsMuteAllMessageEvent, new MuteRoomMessageEvent());
        this.getMessages().put(Events.RateRoomMessageEvent, new RateRoomMessageEvent());
        this.getMessages().put(Events.GiveHanditemMessageEvent, new GiveHandItemMessageEvent());
        this.getMessages().put(Events.SaveFloorPlanEditorMessageEvent, new SaveFloorMessageEvent());
        this.getMessages().put(Events.GetFloorPlanFurnitureMessageEvent, new GetTilesInUseMessageEvent());
        this.getMessages().put(Events.GetFloorPlanDoorMessageEvent, new GetFloorPlanDoorMessageEvent());
        this.getMessages().put(Events.IgnoreUserMessageEvent, new IgnoreUserMessageEvent());
        this.getMessages().put(Events.UnignoreUserMessageEvent, new UnignoreUserMessageEvent());
        this.getMessages().put(Events.RemoveOwnRightsMessageEvent, new RemoveOwnRightsMessageEvent());
        this.getMessages().put(Events.SitMessageEvent, new SitMessageEvent());
    }

    public void registerRoomTrade() {
        this.getMessages().put(Events.TradeStartMessageEvent, new BeginTradeMessageEvent());
        this.getMessages().put(Events.TradeCancelMessageEvent, new CancelTradeMessageEvent());
        this.getMessages().put(Events.TradeUnacceptMessageEvent, new UnacceptTradeMessageEvent());
        this.getMessages().put(Events.TradeAddItemOfferMessageEvent, new SendOfferMessageEvent());
        this.getMessages().put(Events.TradeDiscardMessageEvent, new CancelOfferMessageEvent());
        this.getMessages().put(Events.TradeAcceptMessageEvent, new AcceptTradeMessageEvent());
        this.getMessages().put(Events.TradeConfirmMessageEvent, new ConfirmTradeMessageEvent());
    }

    public void registerRoomModeration() {
        this.getMessages().put(Events.RoomKickUserMessageEvent, new KickUserMessageEvent());
        this.getMessages().put(Events.RoomBanUserMessageEvent, new BanUserMessageEvent());
        this.getMessages().put(Events.GiveRightsMessageEvent, new GiveRightsMessageEvent());
        this.getMessages().put(Events.RoomRemoveUserRightsMessageEvent, new RemoveRightsMessageEvent());
        this.getMessages().put(Events.RoomRemoveAllRightsMessageEvent, new RemoveAllRightsMessageEvent());
        this.getMessages().put(Events.GetRoomBannedUsersMessageEvent, new GetBannedUsersMessageEvent());
        this.getMessages().put(Events.RoomUnbanUserMessageEvent, new RoomUnbanUserMessageEvent());
        this.getMessages().put(Events.RoomSettingsMuteUserMessageEvent, new MutePlayerMessageEvent());
        this.getMessages().put(Events.WordFilterListMessageEvent, new WordFilterListMessageEvent());
        this.getMessages().put(Events.EditWordFilterMessageEvent, new EditWordFilterMessageEvent());
    }

    public void registerRoomAccess() {
        this.getMessages().put(Events.DoorbellAnswerMessageEvent, new AnswerDoorbellMessageEvent());
        this.getMessages().put(Events.RoomLoadByDoorbellMessageEvent, new LoadRoomByDoorBellMessageEvent());
    }

    public void registerItems() {
        this.getMessages().put(Events.RoomAddFloorItemMessageEvent, new PlaceItemMessageEvent());
        this.getMessages().put(Events.FloorItemMoveMessageEvent, new ChangeFloorItemPositionMessageEvent());
        this.getMessages().put(Events.WallItemMoveMessageEvent, new ChangeWallItemPositionMessageEvent());
        this.getMessages().put(Events.PickUpItemMessageEvent, new PickUpItemMessageEvent());
        this.getMessages().put(Events.TriggerItemMessageEvent, new ChangeFloorItemStateMessageEvent());
        this.getMessages().put(Events.EnterOneWayDoorMessageEvent, new ChangeFloorItemStateMessageEvent());
        this.getMessages().put(Events.TriggerDiceCloseMessageEvent, new OpenDiceMessageEvent());
        this.getMessages().put(Events.TriggerDiceRollMessageEvent, new RunDiceMessageEvent());
        this.getMessages().put(Events.WiredSaveEffectMessageEvent, new SaveWiredDataMessageEvent());
        this.getMessages().put(Events.WiredSaveTriggerMessageEvent, new SaveWiredDataMessageEvent());
        this.getMessages().put(Events.WiredSaveConditionMessageEvent, new SaveWiredDataMessageEvent());
        this.getMessages().put(Events.ReedemExchangeItemMessageEvent, new ExchangeItemMessageEvent());
        this.getMessages().put(Events.TriggerWallItemMessageEvent, new UseWallItemMessageEvent());
        this.getMessages().put(Events.UseHabboWheelMessageEvent, new UseWallItemMessageEvent());
        this.getMessages().put(Events.MannequinSaveDataMessageEvent, new SaveMannequinMessageEvent());
        this.getMessages().put(Events.MannequinUpdateDataMessageEvent, new SaveMannequinFigureMessageEvent());
        this.getMessages().put(Events.SaveRoomBackgroundTonerMessageEvent, new SaveTonerMessageEvent());
        this.getMessages().put(Events.SaveRoomBrandingMessageEvent, new SaveBrandingMessageEvent());
        this.getMessages().put(Events.OpenGiftMessageEvent, new OpenGiftMessageEvent());
        this.getMessages().put(Events.ActivateMoodlightMessageEvent, new UseMoodlightMessageEvent());
        this.getMessages().put(Events.TriggerMoodlightMessageEvent, new ToggleMoodlightMessageEvent());
        this.getMessages().put(Events.UpdateMoodlightMessageEvent, new UpdateMoodlightMessageEvent());
        this.getMessages().put(Events.TileStackMagicSetHeightMessageEvent, new SaveStackToolMessageEvent());
        this.getMessages().put(Events.SaveFootballGateOutfitMessageEvent, new SaveFootballGateMessageEvent());
        this.getMessages().put(Events.WiredSaveMatchingMessageEvent, new UpdateSnapshotsMessageEvent());
        this.getMessages().put(Events.RoomAddPostItMessageEvent, new PlacePostitMessageEvent());
        this.getMessages().put(Events.OpenPostItMessageEvent, new OpenPostItMessageEvent());
        this.getMessages().put(Events.SavePostItMessageEvent, new SavePostItMessageEvent());
        this.getMessages().put(Events.DeletePostItMessageEvent, new DeletePostItMessageEvent());
        this.getMessages().put(Events.YouTubeGetPlayerMessageEvent, new LoadPlaylistMessageEvent());
        this.getMessages().put(Events.YouTubeGetPlaylistGetMessageEvent, new PlayVideoMessageEvent());
        this.getMessages().put(Events.YouTubeChoosePlaylistVideoMessageEvent, new NextVideoMessageEvent());
        this.getMessages().put(Events.ConfirmLoveLockMessageEvent, new ConfirmLoveLockMessageEvent());
    }

    public void registerPromotions() {
        this.getMessages().put(Events.CatalogPromotionGetRoomsMessageEvent, new CatalogPromotionGetRoomsMessageEvent());
        this.getMessages().put(Events.PromoteRoomMessageEvent, new PromoteRoomMessageEvent());
        this.getMessages().put(Events.RoomEventUpdateMessageEvent, new PromotionUpdateMessageEvent());
    }

    public void registerCatalog() {
        this.getMessages().put(Events.GetCatalogIndexMessageEvent, new GetCataIndexMessageEvent());
        this.getMessages().put(Events.GetCatalogPageMessageEvent, new GetCataPageMessageEvent());
        this.getMessages().put(Events.PurchaseFromCatalogMessageEvent, new PurchaseItemMessageEvent());
        this.getMessages().put(Events.GetGiftWrappingConfigurationMessageEvent, new GetGiftWrappingConfigurationMessageEvent());
        this.getMessages().put(Events.CatalogOfferConfigMessageEvent, new CatalogOfferConfigMessageEvent());
        this.getMessages().put(Events.GetGroupPurchaseBoxMessageEvent, new BuyGroupDialogMessageEvent());
        this.getMessages().put(Events.CreateGuildMessageEvent, new BuyGroupMessageEvent());
        this.getMessages().put(Events.GetSellablePetBreedsMessageEvent, new PetRacesMessageEvent());
        this.getMessages().put(Events.CheckPetNameMessageEvent, new ValidatePetNameMessageEvent());
        this.getMessages().put(Events.PurchaseFromCatalogAsGiftMessageEvent, new PurchaseGiftMessageEvent());
        this.getMessages().put(Events.GetGroupFurnitureMessageEvent, new GroupFurnitureCatalogMessageEvent());
        this.getMessages().put(Events.GetCatalogOfferMessageEvent, new GetCatalogOfferMessageEvent());
    }

    public void registerLanding() {
        this.getMessages().put(Events.LandingRefreshPromosMessageEvent, new RefreshPromoArticlesMessageEvent());
        this.getMessages().put(Events.LandingLoadWidgetMessageEvent, new LandingLoadWidgetMessageEvent());
    }

    public void registerGroups() {
        this.getMessages().put(Events.GetGroupInfoMessageEvent, new GroupInformationMessageEvent());
        this.getMessages().put(Events.GetGroupMembersMessageEvent, new GroupMembersMessageEvent());
        this.getMessages().put(Events.GroupManageMessageEvent, new ManageGroupMessageEvent());
        this.getMessages().put(Events.RequestLeaveGroupMessageEvent, new RevokeMembershipMessageEvent());
        this.getMessages().put(Events.GroupUserJoinMessageEvent, new JoinGroupMessageEvent());
        this.getMessages().put(Events.GroupUpdateNameMessageEvent, new ModifyGroupTitleMessageEvent());
        this.getMessages().put(Events.RemoveGroupAdminMessageEvent, new RevokeAdminMessageEvent());
        this.getMessages().put(Events.GroupMakeAdministratorMessageEvent, new GiveGroupAdminMessageEvent());
        this.getMessages().put(Events.GroupUpdateSettingsMessageEvent, new ModifyGroupSettingsMessageEvent());
        this.getMessages().put(Events.AcceptGroupRequestMessageEvent, new AcceptMembershipMessageEvent());
        this.getMessages().put(Events.GroupUpdateBadgeMessageEvent, new ModifyGroupBadgeMessageEvent());
        this.getMessages().put(Events.SetFavoriteGroupMessageEvent, new SetFavouriteGroupMessageEvent());
        this.getMessages().put(Events.GroupFurnitureWidgetMessageEvent, new GroupFurnitureWidgetMessageEvent());
        this.getMessages().put(Events.GroupUpdateColoursMessageEvent, new GroupUpdateColoursMessageEvent());
        this.getMessages().put(Events.DeclineMembershipMessageEvent, new DeclineMembershipMessageEvent());
        this.getMessages().put(Events.ClearFavouriteGroupMessageEvent, new ClearFavouriteGroupMessageEvent());
    }

    public void registerGroupForums() {
        this.getMessages().put(Events.ForumDataMessageEvent, new ForumDataMessageEvent());
        this.getMessages().put(Events.SaveForumSettingsMessageEvent, new SaveForumSettingsMessageEvent());
        this.getMessages().put(Events.ForumThreadsMessageEvent, new ForumThreadsMessageEvent());
        this.getMessages().put(Events.CreateThreadMessageEvent, new PostMessageMessageEvent());
        this.getMessages().put(Events.ViewThreadMessageEvent, new ViewThreadMessageEvent());
        this.getMessages().put(Events.ModerateThreadMessageEvent, new ModerateThreadMessageEvent());
        this.getMessages().put(Events.UpdateThreadMessageEvent, new UpdateThreadMessageEvent());
        this.getMessages().put(Events.GetForumsMessageEvent, new GetForumsMessageEvent());
    }

    public void registerQuests() {
        this.getMessages().put(Events.OpenQuestsMessageEvent, new OpenQuestsMessageEvent());
        this.getMessages().put(Events.StartQuestMessageEvent, new StartQuestMessageEvent());
        this.getMessages().put(Events.CancelQuestMessageEvent, new CancelQuestMessageEvent());
        this.getMessages().put(Events.NextQuestMessageEvent, new NextQuestMessageEvent());
    }

    public void registerCamera() {
        this.getMessages().put(Events.CameraTokenMessageEvent, new CameraTokenMessageEvent());
        this.getMessages().put(Events.RenderRoomMessageEvent, new RenderRoomMessageEvent());

        this.getMessages().put(Events.TakePhotoMessageEvent, new TakePhotoMessageEvent());
    }

    public void registerMusic() {
        this.getMessages().put(Events.SongInventoryMessageEvent, new SongInventoryMessageEvent());
        this.getMessages().put(Events.SongIdMessageEvent, new SongIdMessageEvent());
        this.getMessages().put(Events.SongDataMessageEvent, new SongDataMessageEvent());
        this.getMessages().put(Events.PlaylistAddMessageEvent, new PlaylistAddMessageEvent());
        this.getMessages().put(Events.PlaylistRemoveMessageEvent, new PlaylistRemoveMessageEvent());
        this.getMessages().put(Events.PlaylistMessageEvent, new PlaylistMessageEvent());
    }

    public void registerAchievements() {
        this.getMessages().put(Events.AchievementsListMessageEvent, new AchievementsListMessageEvent());
    }

    public void handle(MessageEvent message, Session client) {
        final Short header = message.getId();

        if (Comet.isDebugging) {
            log.debug(message.toString());
        }

        if(!Comet.isRunning)
            return;

        if (this.getMessages().containsKey(header)) {
            try {
                final Event event = this.getMessages().get(header);

                if (event != null) {
                    if (this.asyncEventExecution) {
                        this.eventExecutor.submit(new MessageEventTask(event, client, message));
                    } else {
                        final long start = System.currentTimeMillis();
                        log.debug("Started packet process for packet: [" + event.getClass().getSimpleName() + "][" + header + "]");

                        event.handle(client, message);

                        long timeTakenSinceCreation = ((System.currentTimeMillis() - start));

                        // If the packet took more than 100ms to be handled, red flag!
                        if (timeTakenSinceCreation >= 100) {
                            if (client.getPlayer() != null && client.getPlayer().getData() != null)
                                log.warn("[" + event.getClass().getSimpleName() + "][" + message.getId() + "][" + client.getPlayer().getId() + "][" + client.getPlayer().getData().getUsername() + "] Packet took " + timeTakenSinceCreation + "ms to execute");
                            else
                                log.warn("[" + event.getClass().getSimpleName() + "][" + message.getId() + "] Packet took " + timeTakenSinceCreation + "ms to execute");
                        }

                        log.debug("Finished packet process for packet: [" + event.getClass().getSimpleName() + "][" + header + "] in " + ((System.currentTimeMillis() - start)) + "ms");
                    }
                }
            } catch (Exception e) {
                if (client.getLogger() != null)
                    client.getLogger().error("Error while handling event: " + this.getMessages().get(header).getClass().getSimpleName(), e);
                else
                    log.error("Error while handling event: " + this.getMessages().get(header).getClass().getSimpleName(), e);
            }
        } else if (Comet.isDebugging) {
            log.debug("Unhandled message: " + Events.valueOfId(header) + " / " + header);
        }
    }

    public Map<Short, Event> getMessages() {
        return this.messages;
    }
}
