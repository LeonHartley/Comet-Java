package com.cometproject.server.network.messages;

import com.cometproject.api.config.Configuration;
import com.cometproject.api.networking.messages.IMessageEventHandler;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.composers.gamecenter.GameAccountStatusMessageComposer;
import com.cometproject.server.composers.gamecenter.GameStatusMessageComposer;
import com.cometproject.server.composers.gamecenter.LoadGameMessageComposer;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.incoming.catalog.*;
import com.cometproject.server.network.messages.incoming.catalog.ads.CatalogPromotionGetRoomsMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.ads.PromoteRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.ads.PromotionUpdateMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.data.GetGiftWrappingConfigurationMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupDialogMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.GroupFurnitureCatalogMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.pets.PetRacesMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.pets.ValidatePetNameMessageEvent;
import com.cometproject.server.network.messages.incoming.gamecenter.GetGameAchievementsMessageEvent;
import com.cometproject.server.network.messages.incoming.gamecenter.GetGameListMessageEvent;
import com.cometproject.server.network.messages.incoming.group.*;
import com.cometproject.server.network.messages.incoming.group.favourite.ClearFavouriteGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.group.favourite.SetFavouriteGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.data.ForumDataMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.data.GetForumsMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.settings.SaveForumSettingsMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.threads.*;
import com.cometproject.server.network.messages.incoming.group.settings.*;
import com.cometproject.server.network.messages.incoming.handshake.*;
import com.cometproject.server.network.messages.incoming.help.GetSanctionStatusEvent;
import com.cometproject.server.network.messages.incoming.help.HelpTicketMessageEvent;
import com.cometproject.server.network.messages.incoming.help.InitHelpToolMessageEvent;
import com.cometproject.server.network.messages.incoming.help.guides.GuideHandleHelpRequestMessageEvent;
import com.cometproject.server.network.messages.incoming.help.guides.OpenGuideToolMessageEvent;
import com.cometproject.server.network.messages.incoming.help.guides.RequestGuideAssistanceMessageEvent;
import com.cometproject.server.network.messages.incoming.landing.LandingLoadWidgetMessageEvent;
import com.cometproject.server.network.messages.incoming.landing.RefreshPromoArticlesMessageEvent;
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
import com.cometproject.server.network.messages.incoming.navigator.updated.*;
import com.cometproject.server.network.messages.incoming.performance.EventLogMessageEvent;
import com.cometproject.server.network.messages.incoming.performance.RequestLatencyTestMessageEvent;
import com.cometproject.server.network.messages.incoming.polls.GetPollMessageEvent;
import com.cometproject.server.network.messages.incoming.polls.SubmitPollAnswerMessageEvent;
import com.cometproject.server.network.messages.incoming.quests.CancelQuestMessageEvent;
import com.cometproject.server.network.messages.incoming.quests.OpenQuestsMessageEvent;
import com.cometproject.server.network.messages.incoming.quests.StartQuestMessageEvent;
import com.cometproject.server.network.messages.incoming.room.access.AnswerDoorbellMessageEvent;
import com.cometproject.server.network.messages.incoming.room.access.LoadRoomByDoorBellMessageEvent;
import com.cometproject.server.network.messages.incoming.room.access.SpectateRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.room.action.*;
import com.cometproject.server.network.messages.incoming.room.bots.BotConfigMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.ModifyBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.PlaceBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.RemoveBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.AddUserToRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.FollowRoomInfoMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.GetFurnitureAliasesMessageEvent;
import com.cometproject.server.network.messages.incoming.room.engine.InitializeRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.room.filter.EditWordFilterMessageEvent;
import com.cometproject.server.network.messages.incoming.room.filter.WordFilterListMessageEvent;
import com.cometproject.server.network.messages.incoming.room.floor.GetTilesInUseMessageEvent;
import com.cometproject.server.network.messages.incoming.room.floor.SaveFloorMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.*;
import com.cometproject.server.network.messages.incoming.room.item.gifts.OpenGiftMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.lovelock.ConfirmLoveLockMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinFigureMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.DeletePostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.OpenPostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.PlacePostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.SavePostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.wired.SaveWiredDataMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.wired.UpdateSnapshotsMessageEvent;
import com.cometproject.server.network.messages.incoming.room.moderation.*;
import com.cometproject.server.network.messages.incoming.room.pets.*;
import com.cometproject.server.network.messages.incoming.room.pets.horse.ApplyHorseEffectMessageEvent;
import com.cometproject.server.network.messages.incoming.room.pets.horse.ModifyWhoCanRideHorseMessageEvent;
import com.cometproject.server.network.messages.incoming.room.pets.horse.RemoveHorseSaddleMessageEvent;
import com.cometproject.server.network.messages.incoming.room.pets.horse.RideHorseMessageEvent;
import com.cometproject.server.network.messages.incoming.room.settings.*;
import com.cometproject.server.network.messages.incoming.room.trading.*;
import com.cometproject.server.network.messages.incoming.user.achievements.AchievementsListMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.PhotoPricingMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.PurchasePhotoMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.RenderRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.ThumbnailMessageEvent;
import com.cometproject.server.network.messages.incoming.user.club.ClubStatusMessageEvent;
import com.cometproject.server.network.messages.incoming.user.details.*;
import com.cometproject.server.network.messages.incoming.user.inventory.*;
import com.cometproject.server.network.messages.incoming.user.profile.*;
import com.cometproject.server.network.messages.incoming.user.wardrobe.ChangeLooksMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.RedeemClothingMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.SaveWardrobeMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.WardrobeMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.LoadPlaylistMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.NextVideoMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.PlayVideoMessageEvent;
import com.cometproject.server.network.messages.outgoing.handshake.ConfirmUsernameMessageEvent;
import com.cometproject.server.network.messages.types.tasks.MessageEventTask;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.headers.Events;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public final class MessageHandler {
    public static Logger log = Logger.getLogger(MessageHandler.class.getName());

    private final Map<Short, Event> messages = Maps.newConcurrentMap();
    private final Map<Short, IMessageEventHandler> eventHandlers = Maps.newConcurrentMap();

    private final ExecutorService eventExecutor;
    private final boolean asyncEventExecution;

    public MessageHandler() {
        this.asyncEventExecution = Boolean.parseBoolean((String) Configuration.currentConfig().getOrDefault("comet.network.alternativePacketHandling.enabled", "false"));
//        this.eventExecutor = asyncEventExecution ? Executors.newFixedThreadPool(Integer.parseInt((String) Configuration.currentConfig().getOrDefault("comet.network.alternativePacketHandling.threads", "8"))) : null;

        if (this.asyncEventExecution) {
            switch ((String) Configuration.currentConfig().getOrDefault("comet.network.alternativePacketHandling.type", "threadpool")) {
                default:
                    log.info("Using fixed thread-pool event executor");
                    this.eventExecutor = Executors.newFixedThreadPool(8);
                    break;

                case "forkjoin":
                    log.info("Using fork-join event executor");
                    this.eventExecutor = new ForkJoinPool(Integer.parseInt((String) Configuration.currentConfig().getOrDefault("comet.network.alternativePacketHandling.coreSize", 16)), ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
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
        this.registerPromotions();
        this.registerAchievements();
        this.registerPolls();
        this.registerMisc();
        this.registerMusic();
        this.registerCamera();
        this.registerGuideTool();
        this.registerGameCenter();

        log.info("Loaded " + this.getMessages().size() + " message events");
    }

    private void registerGameCenter() {
        this.getMessages().put(Events.GetGameListMessageEvent, new GetGameListMessageEvent());
        this.getMessages().put(Events.GetGameAchievementsMessageEvent, new GetGameAchievementsMessageEvent());

        this.getMessages().put(Events.GetGameStatusMessageEvent, (session, event) -> {
            final int gameId = event.readInt();

            session.send(new GameAccountStatusMessageComposer(gameId));
            session.send(new GameStatusMessageComposer(gameId, 0));
        });

        this.getMessages().put(Events.JoinGameQueueMessageEvent, (session, event) -> {
            final int gameId = event.readInt();

            final UUID sessionId = UUID.randomUUID();

            PlayerManager.getInstance().getSsoTicketToPlayerId().put(session.getPlayer().getId() + sessionId.toString(), session.getPlayer().getId());

            session.send(new LoadGameMessageComposer(gameId, "http://localhost/comet/swf/games/gamecenter_basejump/BaseJump.swf", session.getPlayer().getId() + sessionId.toString(), "localhost", "30010", "30010", "http://localhost/comet/swf/games/gamecenter_basejump/BasicAssets.swf"));
        });
    }

    private void registerMisc() {
        this.getMessages().put(Events.LatencyTestMessageEvent, new RequestLatencyTestMessageEvent());
        this.getMessages().put(Events.EventLogMessageEvent, new EventLogMessageEvent());
    }

    public void registerHandshake() {
        this.getMessages().put(Events.GetClientVersionMessageEvent, new CheckReleaseMessageEvent());
        this.getMessages().put(Events.InitCryptoMessageEvent, new InitCryptoMessageEvent());
        this.getMessages().put(Events.GenerateSecretKeyMessageEvent, new GenerateSecretKeyMessageEvent());
        this.getMessages().put(Events.SSOTicketMessageEvent, new SSOTicketMessageEvent());
        this.getMessages().put(Events.UniqueIDMessageEvent, new UniqueIdMessageEvent());
        this.getMessages().put(Events.ConfirmUsernameMessageEvent, new ConfirmUsernameMessageEvent());
    }

    public void registerModTool() {
        this.getMessages().put(Events.GetModeratorUserInfoMessageEvent, new ModToolUserInfoMessageEvent());
        this.getMessages().put(Events.GetModeratorUserChatlogMessageEvent, new ModToolUserChatlogMessageEvent());
        this.getMessages().put(Events.GetModeratorRoomChatlogMessageEvent, new ModToolRoomChatlogMessageEvent());
        this.getMessages().put(Events.ModerationBanMessageEvent, new ModToolBanUserMessageEvent());
        this.getMessages().put(Events.GetModeratorRoomInfoMessageEvent, new ModToolRoomInfoMessageEvent());
        this.getMessages().put(Events.GetModeratorUserRoomVisitsMessageEvent, new ModToolRoomVisitsMessageEvent());
        this.getMessages().put(Events.ModerationMsgMessageEvent, new ModToolUserAlertMessageEvent());
        this.getMessages().put(Events.ModerationCautionMessageEvent, new ModToolUserCautionMessageEvent());
        this.getMessages().put(Events.ModerationKickMessageEvent, new ModToolUserKickMessageEvent());
        this.getMessages().put(Events.ModeratorActionMessageEvent, new ModToolRoomAlertMessageEvent());
        this.getMessages().put(Events.ModerateRoomMessageEvent, new ModToolRoomActionMessageEvent());
        this.getMessages().put(Events.PickTicketMessageEvent, new ModToolPickTicketMessageEvent());
        this.getMessages().put(Events.GetModeratorTicketChatlogsMessageEvent, new ModToolTicketChatlogMessageEvent());
        this.getMessages().put(Events.CloseTicketMesageEvent, new ModToolCloseIssueMessageEvent());
        this.getMessages().put(Events.ReleaseTicketMessageEvent, new ModToolReleaseIssueMessageEvent());
        this.getMessages().put(Events.ModerationMuteMessageEvent, new ModerationMuteUserMessageEvent());
    }

    public void registerHelpTool() {
        this.getMessages().put(Events.OpenHelpToolMessageEvent, new InitHelpToolMessageEvent());
        this.getMessages().put(Events.SubmitNewTicketMessageEvent, new HelpTicketMessageEvent());
        this.getMessages().put(Events.GetSanctionStatusMessageEvent, new GetSanctionStatusEvent());
    }

    public void registerMessenger() {
        this.getMessages().put(Events.MessengerInitMessageEvent, new InitializeFriendListMessageEvent());
        this.getMessages().put(Events.SendMsgMessageEvent, new PrivateChatMessageEvent());
        this.getMessages().put(Events.RequestBuddyMessageEvent, new RequestFriendshipMessageEvent());
        this.getMessages().put(Events.AcceptBuddyMessageEvent, new AcceptFriendshipMessageEvent());
        this.getMessages().put(Events.HabboSearchMessageEvent, new SearchFriendsMessageEvent());
        this.getMessages().put(Events.FollowFriendMessageEvent, new FollowFriendMessageEvent());
        this.getMessages().put(Events.RemoveBuddyMessageEvent, new DeleteFriendsMessageEvent());
        this.getMessages().put(Events.SendRoomInviteMessageEvent, new InviteFriendsMessageEvent());
        this.getMessages().put(Events.DeclineBuddyMessageEvent, new DeclineFriendshipMessageEvent());
    }

    public void registerNavigator() {
        this.getMessages().put(Events.GetUserFlatCatsMessageEvent, new LoadCategoriesMessageEvent());
        this.getMessages().put(Events.InitializeNavigatorMessageEvent, new InitializeNewNavigatorMessageEvent());
        this.getMessages().put(Events.NavigatorSearchMessageEvent, new NewNavigatorSearchMessageEvent());
        this.getMessages().put(Events.CanCreateRoomMessageEvent, new CanCreateRoomMessageEvent());
        this.getMessages().put(Events.CreateFlatMessageEvent, new CreateRoomMessageEvent());
        this.getMessages().put(Events.GetEventCategoriesMessageEvent, new EventCategoriesMessageEvent());
        this.getMessages().put(Events.GetPromotableRoomsMessageEvent, new CatalogPromotionGetRoomsMessageEvent());
        this.getMessages().put(Events.StaffPickRoomMessageEvent, new AddToStaffPickedRoomsMessageEvent());
        this.getMessages().put(Events.ResizeNavigatorMessageEvent, new ResizeNavigatorMessageEvent());
        this.getMessages().put(Events.SaveNavigatorSearchMessageEvent, new SaveNavigatorSearchMessageEvent());
        this.getMessages().put(Events.DeleteNavigatorSavedSearchMessageEvent, new DeleteNavigatorSavedSearchMessageEvent());
        this.getMessages().put(Events.FindRandomFriendingRoomMessageEvent, new FindRandomFriendingRoomMessageEvent());
        this.getMessages().put(Events.AddFavouriteRoomMessageEvent, new ToggleFavouriteRoomMessageEvent());
        this.getMessages().put(Events.DeleteFavouriteRoomMessageEvent, new ToggleFavouriteRoomMessageEvent());
        this.getMessages().put(Events.NavigatorSaveViewModeMessageEvent, new NavigatorSaveViewModeMessageEvent());
    }

    public void registerUser() {
        this.getMessages().put(Events.GetExtendedProfileMessageEvent, new GetProfileMessageEvent());
        this.getMessages().put(Events.GetForumUserProfileMessageEvent, new GetProfileByUsernameMessageEvent());
        this.getMessages().put(Events.ScrGetUserInfoMessageEvent, new ClubStatusMessageEvent());
        this.getMessages().put(Events.InfoRetrieveMessageEvent, new InfoRetrieveMessageEvent());
        this.getMessages().put(Events.UpdateFigureDataMessageEvent, new ChangeLooksMessageEvent());
        this.getMessages().put(Events.RequestFurniInventoryMessageEvent, new OpenInventoryMessageEvent());
        this.getMessages().put(Events.GetBadgesMessageEvent, new BadgeInventoryMessageEvent());
        this.getMessages().put(Events.ChangeMottoMessageEvent, new ChangeMottoMessageEvent());
        this.getMessages().put(Events.GetRelationshipsMessageEvent, new GetRelationshipsMessageEvent());
        this.getMessages().put(Events.SetRelationshipMessageEvent, new SetRelationshipMessageEvent());
        this.getMessages().put(Events.SetActivatedBadgesMessageEvent, new WearBadgeMessageEvent());
        this.getMessages().put(Events.GetWardrobeMessageEvent, new WardrobeMessageEvent());
        this.getMessages().put(Events.SaveWardrobeOutfitMessageEvent, new SaveWardrobeMessageEvent());
        this.getMessages().put(Events.UpdateNavigatorSettingsMessageEvent, new ChangeHomeRoomMessageEvent());
        this.getMessages().put(Events.SetSoundSettingsMessageEvent, new UpdateAudioSettingsMessageEvent());
        this.getMessages().put(Events.SetChatPreferenceMessageEvent, new UpdateChatStyleMessageEvent());
        this.getMessages().put(Events.SetMessengerInviteStatusMessageEvent, new IgnoreInvitationsMessageEvent());
        this.getMessages().put(Events.ChangeNameMessageEvent, new ChangeNameMessageEvent());
        this.getMessages().put(Events.CheckValidNameMessageEvent, new CheckValidNameMessageEvent());
        this.getMessages().put(Events.RedeemClothingMessageEvent, new RedeemClothingMessageEvent());
        this.getMessages().put(Events.EquipEffectMessageEvent, new EquipEffectMessageEvent());
    }

    public void registerBots() {
        this.getMessages().put(Events.GetBotInventoryMessageEvent, new BotInventoryMessageEvent());
        this.getMessages().put(Events.PlaceBotMessageEvent, new PlaceBotMessageEvent());
        this.getMessages().put(Events.CommandBotMessageEvent, new ModifyBotMessageEvent());
        this.getMessages().put(Events.PickUpBotMessageEvent, new RemoveBotMessageEvent());
        this.getMessages().put(Events.OpenBotActionMessageEvent, new BotConfigMessageEvent());
    }

    public void registerPets() {
        this.getMessages().put(Events.GetPetInventoryMessageEvent, new PetInventoryMessageEvent());
        this.getMessages().put(Events.PlacePetMessageEvent, new PlacePetMessageEvent());
        this.getMessages().put(Events.GetPetInformationMessageEvent, new PetInformationMessageEvent());
        this.getMessages().put(Events.PickUpPetMessageEvent, new RemovePetMessageEvent());
        this.getMessages().put(Events.RideHorseMessageEvent, new RideHorseMessageEvent());
        this.getMessages().put(Events.RespectPetMessageEvent, new ScratchPetMessageEvent());
        this.getMessages().put(Events.GetPetTrainingPanelMessageEvent, new GetPetTrainingPanelMessageEvent());
        this.getMessages().put(Events.ApplyHorseEffectMessageEvent, new ApplyHorseEffectMessageEvent());
        this.getMessages().put(Events.RemoveSaddleFromHorseMessageEvent, new RemoveHorseSaddleMessageEvent());
        this.getMessages().put(Events.ModifyWhoCanRideHorseMessageEvent, new ModifyWhoCanRideHorseMessageEvent());
        this.getMessages().put(Events.BreedPetsMessageEvent, new BreedPetsMessageEvent());
        this.getMessages().put(Events.OpenPetPackageMessageEvent, new OpenPetPackageMessageEvent());
    }

    public void registerRoom() {
        this.getMessages().put(Events.OpenFlatConnectionMessageEvent, new InitializeRoomMessageEvent());
        this.getMessages().put(Events.SpectateRoomMessageEvent, new SpectateRoomMessageEvent());
        this.getMessages().put(Events.GetGuestRoomMessageEvent, new FollowRoomInfoMessageEvent());
        this.getMessages().put(Events.GetRoomEntryDataMessageEvent, new AddUserToRoomMessageEvent());
        this.getMessages().put(Events.GoToHotelViewMessageEvent, new ExitRoomMessageEvent());
        this.getMessages().put(Events.ChatMessageEvent, new TalkMessageEvent());
        this.getMessages().put(Events.ShoutMessageEvent, new ShoutMessageEvent());
        this.getMessages().put(Events.WhisperMessageEvent, new WhisperMessageEvent());
        this.getMessages().put(Events.MoveAvatarMessageEvent, new WalkMessageEvent());
        this.getMessages().put(Events.ActionMessageEvent, new ApplyActionMessageEvent());
        this.getMessages().put(Events.ApplySignMessageEvent, new ApplySignMessageEvent());
        this.getMessages().put(Events.DanceMessageEvent, new ApplyDanceMessageEvent());
        this.getMessages().put(Events.GetRoomSettingsMessageEvent, new GetRoomSettingsDataMessageEvent());
        this.getMessages().put(Events.SaveRoomSettingsMessageEvent, new SaveRoomDataMessageEvent());
        this.getMessages().put(Events.RespectUserMessageEvent, new RespectUserMessageEvent());
        this.getMessages().put(Events.StartTypingMessageEvent, new StartTypingMessageEvent());
        this.getMessages().put(Events.CancelTypingMessageEvent, new StopTypingMessageEvent());
        this.getMessages().put(Events.LookToMessageEvent, new LookToMessageEvent());
        this.getMessages().put(Events.GetSelectedBadgesMessageEvent, new UserBadgesMessageEvent());
        this.getMessages().put(Events.ApplyDecorationMessageEvent, new ApplyDecorationMessageEvent());
        this.getMessages().put(Events.DropHandItemMessageEvent, new DropHandItemMessageEvent());
        this.getMessages().put(Events.DeleteRoomMessageEvent, new DeleteRoomMessageEvent());
        this.getMessages().put(Events.ToggleMuteToolMessageEvent, new MuteRoomMessageEvent());
        this.getMessages().put(Events.GiveRoomScoreMessageEvent, new RateRoomMessageEvent());
        this.getMessages().put(Events.GiveHandItemMessageEvent, new GiveHandItemMessageEvent());
        this.getMessages().put(Events.UpdateFloorPropertiesMessageEvent, new SaveFloorMessageEvent());
        this.getMessages().put(Events.InitializeFloorPlanSessionMessageEvent, new GetTilesInUseMessageEvent());
        this.getMessages().put(Events.IgnoreUserMessageEvent, new IgnoreUserMessageEvent());
        this.getMessages().put(Events.UnIgnoreUserMessageEvent, new UnignoreUserMessageEvent());
        this.getMessages().put(Events.RemoveMyRightsMessageEvent, new RemoveOwnRightsMessageEvent());
        this.getMessages().put(Events.SitMessageEvent, new SitMessageEvent());
        this.getMessages().put(Events.GetFurnitureAliasesMessageEvent, new GetFurnitureAliasesMessageEvent());
    }

    public void registerRoomTrade() {
        this.getMessages().put(Events.InitTradeMessageEvent, new BeginTradeMessageEvent());
        this.getMessages().put(Events.TradingOfferItemMessageEvent, new SendOfferMessageEvent());
        this.getMessages().put(Events.TradingOfferItemsMessageEvent, new TradingOfferItemsMessageEvent());
        this.getMessages().put(Events.TradingRemoveItemMessageEvent, new CancelOfferMessageEvent());
        this.getMessages().put(Events.TradingAcceptMessageEvent, new AcceptTradeMessageEvent());
        this.getMessages().put(Events.TradingModifyMessageEvent, new UnacceptTradeMessageEvent());
        this.getMessages().put(Events.TradingCancelMessageEvent, new CancelTradeMessageEvent());
        this.getMessages().put(Events.TradingConfirmMessageEvent, new ConfirmTradeMessageEvent());
        this.getMessages().put(Events.TradingCancelConfirmMessageEvent, new CancelConfirmTradeMessageEvent());
    }

    public void registerRoomModeration() {
        this.getMessages().put(Events.KickUserMessageEvent, new KickUserMessageEvent());
        this.getMessages().put(Events.BanUserMessageEvent, new BanUserMessageEvent());
        this.getMessages().put(Events.AssignRightsMessageEvent, new GiveRightsMessageEvent());
        this.getMessages().put(Events.RemoveRightsMessageEvent, new RemoveRightsMessageEvent());
        this.getMessages().put(Events.RemoveAllRightsMessageEvent, new RemoveAllRightsMessageEvent());
        this.getMessages().put(Events.GetRoomBannedUsersMessageEvent, new GetBannedUsersMessageEvent());
        this.getMessages().put(Events.UnbanUserFromRoomMessageEvent, new RoomUnbanUserMessageEvent());
        this.getMessages().put(Events.MuteUserMessageEvent, new MutePlayerMessageEvent());
        this.getMessages().put(Events.GetRoomRightsMessageEvent, new UsersWithRightsMessageEvent());
        this.getMessages().put(Events.GetRoomFilterListMessageEvent, new WordFilterListMessageEvent());
        this.getMessages().put(Events.ModifyRoomFilterListMessageEvent, new EditWordFilterMessageEvent());
    }

    public void registerRoomAccess() {
        this.getMessages().put(Events.LetUserInMessageEvent, new AnswerDoorbellMessageEvent());
        this.getMessages().put(Events.GoToFlatMessageEvent, new LoadRoomByDoorBellMessageEvent());
    }

    public void registerItems() {
        this.getMessages().put(Events.PlaceObjectMessageEvent, new PlaceItemMessageEvent());
        this.getMessages().put(Events.MoveObjectMessageEvent, new MoveFloorItemMessageEvent());
        this.getMessages().put(Events.MoveWallItemMessageEvent, new ChangeWallItemPositionMessageEvent());
        this.getMessages().put(Events.PickupObjectMessageEvent, new PickUpItemMessageEvent());
        this.getMessages().put(Events.UseFurnitureMessageEvent, new ChangeFloorItemStateMessageEvent());
        this.getMessages().put(Events.UseOneWayGateMessageEvent, new ChangeFloorItemStateMessageEvent());
        this.getMessages().put(Events.DiceOffMessageEvent, new OpenDiceMessageEvent());
        this.getMessages().put(Events.ThrowDiceMessageEvent, new RunDiceMessageEvent());

        this.getMessages().put(Events.SaveWiredEffectConfigMessageEvent, new SaveWiredDataMessageEvent());
        this.getMessages().put(Events.SaveWiredConditionConfigMessageEvent, new SaveWiredDataMessageEvent());
        this.getMessages().put(Events.SaveWiredTriggerConfigMessageEvent, new SaveWiredDataMessageEvent());
        this.getMessages().put(Events.UpdateSnapshotsMessageEvent, new UpdateSnapshotsMessageEvent());

        this.getMessages().put(Events.CreditFurniRedeemMessageEvent, new ExchangeItemMessageEvent());
        this.getMessages().put(Events.UseWallItemMessageEvent, new UseWallItemMessageEvent());
        this.getMessages().put(Events.UseHabboWheelMessageEvent, new UseWallItemMessageEvent());
        this.getMessages().put(Events.SetMannequinNameMessageEvent, new SaveMannequinMessageEvent());
        this.getMessages().put(Events.SetMannequinFigureMessageEvent, new SaveMannequinFigureMessageEvent());
        this.getMessages().put(Events.SetTonerMessageEvent, new SaveTonerMessageEvent());
        this.getMessages().put(Events.SetObjectDataMessageEvent, new SaveBrandingMessageEvent());
        this.getMessages().put(Events.OpenGiftMessageEvent, new OpenGiftMessageEvent());
        this.getMessages().put(Events.GetMoodlightConfigMessageEvent, new UseMoodlightMessageEvent());
        this.getMessages().put(Events.ToggleMoodlightMessageEvent, new ToggleMoodlightMessageEvent());
        this.getMessages().put(Events.RoomDimmerSavePresetMessageEvent, new UpdateMoodlightMessageEvent());
        this.getMessages().put(Events.SetCustomStackingHeightMessageEvent, new SaveStackToolMessageEvent());
        this.getMessages().put(Events.AddStickyNoteMessageEvent, new PlacePostItMessageEvent());
        this.getMessages().put(Events.GetStickyNoteMessageEvent, new OpenPostItMessageEvent());
        this.getMessages().put(Events.UpdateStickyNoteMessageEvent, new SavePostItMessageEvent());
        this.getMessages().put(Events.DeleteStickyNoteMessageEvent, new DeletePostItMessageEvent());
        this.getMessages().put(Events.GetYouTubeTelevisionMessageEvent, new LoadPlaylistMessageEvent());
        this.getMessages().put(Events.ToggleYouTubeVideoMessageEvent, new PlayVideoMessageEvent());
        this.getMessages().put(Events.YouTubeGetNextVideo, new NextVideoMessageEvent());
        this.getMessages().put(Events.ConfirmLoveLockMessageEvent, new ConfirmLoveLockMessageEvent());
        this.getMessages().put(Events.SaveFootballGateMessageEvent, new SaveFootballGateMessageEvent());
    }

    public void registerPromotions() {
        this.getMessages().put(Events.PurchaseRoomPromotionMessageEvent, new PromoteRoomMessageEvent());
        this.getMessages().put(Events.EditRoomPromotionMessageEvent, new PromotionUpdateMessageEvent());
    }

    //
    public void registerCatalog() {
        this.getMessages().put(Events.GetCatalogIndexMessageEvent, new GetCataIndexMessageEvent());
        this.getMessages().put(Events.GetCatalogPageMessageEvent, new GetCataPageMessageEvent());
        this.getMessages().put(Events.PurchaseFromCatalogMessageEvent, new PurchaseItemMessageEvent());
        this.getMessages().put(Events.GetGiftWrappingConfigurationMessageEvent, new GetGiftWrappingConfigurationMessageEvent());
        this.getMessages().put(Events.GetGroupCreationWindowMessageEvent, new BuyGroupDialogMessageEvent());
        this.getMessages().put(Events.PurchaseGroupMessageEvent, new BuyGroupMessageEvent());
        this.getMessages().put(Events.GetSellablePetBreedsMessageEvent, new PetRacesMessageEvent());
        this.getMessages().put(Events.ApproveNameMessageEvent, new ValidatePetNameMessageEvent());
        this.getMessages().put(Events.PurchaseFromCatalogAsGiftMessageEvent, new PurchaseGiftMessageEvent());
        this.getMessages().put(Events.GetGroupFurniConfigMessageEvent, new GroupFurnitureCatalogMessageEvent());
        this.getMessages().put(Events.GetCatalogOfferMessageEvent, new GetCatalogOfferMessageEvent());
        this.getMessages().put(Events.RedeemVoucherMessageEvent, new RedeemVoucherMessageEvent());
    }

    public void registerLanding() {
        this.getMessages().put(Events.GetPromoArticlesMessageEvent, new RefreshPromoArticlesMessageEvent());
        this.getMessages().put(Events.RefreshCampaignMessageEvent, new LandingLoadWidgetMessageEvent());
    }

    //
    public void registerGroups() {
        this.getMessages().put(Events.GetGroupInfoMessageEvent, new GroupInformationMessageEvent());
        this.getMessages().put(Events.GetGroupMembersMessageEvent, new GroupMembersMessageEvent());
        this.getMessages().put(Events.ManageGroupMessageEvent, new ManageGroupMessageEvent());
        this.getMessages().put(Events.RemoveGroupMemberMessageEvent, new RevokeMembershipMessageEvent());
        this.getMessages().put(Events.JoinGroupMessageEvent, new JoinGroupMessageEvent());
        this.getMessages().put(Events.UpdateGroupIdentityMessageEvent, new ModifyGroupTitleMessageEvent());
        this.getMessages().put(Events.TakeAdminRightsMessageEvent, new RevokeAdminMessageEvent());
        this.getMessages().put(Events.GiveAdminRightsMessageEvent, new GiveGroupAdminMessageEvent());
        this.getMessages().put(Events.UpdateGroupSettingsMessageEvent, new ModifyGroupSettingsMessageEvent());
        this.getMessages().put(Events.AcceptGroupMembershipMessageEvent, new AcceptMembershipMessageEvent());
        this.getMessages().put(Events.UpdateGroupBadgeMessageEvent, new ModifyGroupBadgeMessageEvent());
        this.getMessages().put(Events.SetGroupFavouriteMessageEvent, new SetFavouriteGroupMessageEvent());
        this.getMessages().put(Events.GetGroupFurniSettingsMessageEvent, new GroupFurnitureWidgetMessageEvent());
        this.getMessages().put(Events.UpdateGroupColoursMessageEvent, new GroupUpdateColoursMessageEvent());
        this.getMessages().put(Events.DeclineGroupMembershipMessageEvent, new DeclineMembershipMessageEvent());
        this.getMessages().put(Events.RemoveGroupFavouriteMessageEvent, new ClearFavouriteGroupMessageEvent());
        this.getMessages().put(Events.DeleteGroupMessageEvent, new DeleteGroupMessageEvent());
        this.getMessages().put(Events.GroupConfirmRemoveMemberMessageEvent, new GroupConfirmRemoveMemberMessageEvent());
    }

    //
    public void registerGroupForums() {
        this.getMessages().put(Events.GetForumStatsMessageEvent, new ForumDataMessageEvent());
        this.getMessages().put(Events.UpdateForumSettingsMessageEvent, new SaveForumSettingsMessageEvent());
        this.getMessages().put(Events.GetThreadsListDataMessageEvent, new ForumThreadsMessageEvent());
        this.getMessages().put(Events.PostGroupContentMessageEvent, new PostMessageMessageEvent());
        this.getMessages().put(Events.GetThreadDataMessageEvent, new ViewThreadMessageEvent());
        this.getMessages().put(Events.UpdateThreadMessageEvent, new UpdateThreadMessageEvent());
        this.getMessages().put(Events.GetForumsListDataMessageEvent, new GetForumsMessageEvent());
        this.getMessages().put(Events.DeleteGroupThreadMessageEvent, new HideGroupForumPostMessageEvent());
        this.getMessages().put(Events.DeleteGroupReplyMessageEvent, new HideGroupForumPostMessageEvent());
    }

    public void registerQuests() {
        this.getMessages().put(Events.GetQuestListMessageEvent, new OpenQuestsMessageEvent());
        this.getMessages().put(Events.StartQuestMessageEvent, new StartQuestMessageEvent());
        this.getMessages().put(Events.CancelQuestMessageEvent, new CancelQuestMessageEvent());
    }


    public void registerMusic() {
        this.getMessages().put(Events.SongInventoryMessageEvent, new SongInventoryMessageEvent());
        this.getMessages().put(Events.SongIdMessageEvent, new SongIdMessageEvent());
        this.getMessages().put(Events.SongDataMessageEvent, new SongDataMessageEvent());
        this.getMessages().put(Events.PlaylistAddMessageEvent, new PlaylistAddMessageEvent());
        this.getMessages().put(Events.PlaylistRemoveMessageEvent, new PlaylistRemoveMessageEvent());
        this.getMessages().put(Events.PlaylistMessageEvent, new PlaylistMessageEvent());
    }

    public void registerPolls() {
        this.getMessages().put(Events.GetPollMessageEvent, new GetPollMessageEvent());
        this.getMessages().put(Events.SubmitPollAnswerMessageEvent, new SubmitPollAnswerMessageEvent());
    }

    public void registerAchievements() {
        this.getMessages().put(Events.GetAchievementsMessageEvent, new AchievementsListMessageEvent());
    }

    private void registerCamera() {
        this.getMessages().put(Events.ThumbnailMessageEvent, new ThumbnailMessageEvent());
        this.getMessages().put(Events.RenderRoomMessageEvent, new RenderRoomMessageEvent());
        this.getMessages().put(Events.PurchasePhotoMessageEvent, new PurchasePhotoMessageEvent());
        this.getMessages().put(Events.PhotoPricingMessageEvent, new PhotoPricingMessageEvent());
    }

    public void registerGuideTool() {
        this.getMessages().put(Events.RequestGuideToolMessageEvent, new OpenGuideToolMessageEvent());
        this.getMessages().put(Events.RequestGuideAssistanceMessageEvent, new RequestGuideAssistanceMessageEvent());
        this.getMessages().put(Events.GuideHandleHelpRequestMessageEvent, new GuideHandleHelpRequestMessageEvent());
    }

    public void handle(MessageEvent message, Session client) {
        final Short header = message.getId();

        if (Comet.isDebugging) {
            log.debug(message.toString());
        }

        if (!Comet.isRunning)
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
                                log.trace("[" + event.getClass().getSimpleName() + "][" + message.getId() + "][" + client.getPlayer().getId() + "][" + client.getPlayer().getData().getUsername() + "] Packet took " + timeTakenSinceCreation + "ms to execute");
                            else
                                log.trace("[" + event.getClass().getSimpleName() + "][" + message.getId() + "] Packet took " + timeTakenSinceCreation + "ms to execute");
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
