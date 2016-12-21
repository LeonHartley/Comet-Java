package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Composers {
    public static final short WiredRewardMessageComposer = 2509; // PRODUCTION-201610052203-260805057
    public static final short HeightMapMessageComposer = 2222; // PRODUCTION-201610052203-260805057
    public static final short CallForHelpPendingCallsMessageComposer = 2912; // PRODUCTION-201610052203-260805057
    public static final short ChatMessageComposer = 619; // PRODUCTION-201610052203-260805057
    public static final short GroupMembersMessageComposer = 2860; // PRODUCTION-201610052203-260805057
    public static final short OpenBotActionMessageComposer = 1987; // PRODUCTION-201610052203-260805057
    public static final short UserObjectMessageComposer = 3716; // PRODUCTION-201610052203-260805057
    public static final short ActionMessageComposer = 1333; // PRODUCTION-201610052203-260805057
    public static final short ManageGroupMessageComposer = 155; // PRODUCTION-201610052203-260805057
    public static final short FloodControlMessageComposer = 2332; // PRODUCTION-201610052203-260805057
    public static final short FlatControllerAddedMessageComposer = 3107; // PRODUCTION-201610052203-260805057
    public static final short TradingClosedMessageComposer = 1979; // PRODUCTION-201610052203-260805057
    public static final short FlatCreatedMessageComposer = 673; // PRODUCTION-201610052203-260805057
    public static final short ScrSendUserInfoMessageComposer = 269; // PRODUCTION-201610052203-260805057
    public static final short CheckPetNameMessageComposer = 1340; // PRODUCTION-201610052203-260805057
    public static final short QuestAbortedMessageComposer = 2820; // PRODUCTION-201610052203-260805057
    public static final short RespectPetNotificationMessageComposer = 3198; // PRODUCTION-201610052203-260805057
    public static final short PromotableRoomsMessageComposer = 382; // PRODUCTION-201610052203-260805057
    public static final short CloseConnectionMessageComposer = 2299; // PRODUCTION-201610052203-260805057
    public static final short CfhTopicsInitMessageComposer = 718; // PRODUCTION-201610052203-260805057
    public static final short WiredEffectConfigMessageComposer = 525; // PRODUCTION-201610052203-260805057
    public static final short FriendListUpdateMessageComposer = 3155; // PRODUCTION-201610052203-260805057
    public static final short ObjectAddMessageComposer = 1216; // PRODUCTION-201610052203-260805057
    public static final short NavigatorCollapsedCategoriesMessageComposer = 2333; // PRODUCTION-201610052203-260805057
    public static final short RoomRightsListMessageComposer = 1483; // PRODUCTION-201610052203-260805057
    public static final short TradingUpdateMessageComposer = 2331; // PRODUCTION-201610052203-260805057
    public static final short CarryObjectMessageComposer = 653; // PRODUCTION-201610052203-260805057
    public static final short NewGroupInfoMessageComposer = 2638; // PRODUCTION-201610052203-260805057
    public static final short RoomForwardMessageComposer = 3529; // PRODUCTION-201610052203-260805057
    public static final short GroupFurniSettingsMessageComposer = 142; // PRODUCTION-201610052203-260805057
    public static final short CreditBalanceMessageComposer = 2203; // PRODUCTION-201610052203-260805057
    public static final short CatalogUpdatedMessageComposer = 786; // PRODUCTION-201610052203-260805057
    public static final short UserTypingMessageComposer = 3532; // PRODUCTION-201610052203-260805057
    public static final short ObjectRemoveMessageComposer = 1497; // PRODUCTION-201610052203-260805057
    public static final short RoomEntryInfoMessageComposer = 889; // PRODUCTION-201610052203-260805057
    public static final short CatalogOfferMessageComposer = 2553; // PRODUCTION-201610052203-260805057
    public static final short CatalogIndexMessageComposer = 1834; // PRODUCTION-201610052203-260805057
    public static final short ThreadsListDataMessageComposer = 266; // PRODUCTION-201610052203-260805057
    public static final short GroupFurniConfigMessageComposer = 2025; // PRODUCTION-201610052203-260805057
    public static final short HabboUserBadgesMessageComposer = 682; // PRODUCTION-201610052203-260805057
    public static final short FlatAccessibleMessageComposer = 931; // PRODUCTION-201610052203-260805057
    public static final short ModeratorInitMessageComposer = 3933; // PRODUCTION-201610052203-260805057
    public static final short FloorPlanSendDoorMessageComposer = 633; // PRODUCTION-201610052203-260805057
    public static final short SleepMessageComposer = 742; // PRODUCTION-201610052203-260805057
    public static final short FlatControllerRemovedMessageComposer = 293; // PRODUCTION-201610052203-260805057
    public static final short UniqueMachineIDMessageComposer = 2371; // PRODUCTION-201610052203-260805057
    public static final short ItemAddMessageComposer = 584; // PRODUCTION-201610052203-260805057
    public static final short GroupForumDataMessageComposer = 3879; // PRODUCTION-201610052203-260805057
    public static final short UpdateFreezeLivesMessageComposer = 625; // PRODUCTION-201610052203-260805057
    public static final short NavigatorSettingsMessageComposer = 2341; // PRODUCTION-201610052203-260805057
    public static final short ItemUpdateMessageComposer = 3949; // PRODUCTION-201610052203-260805057
    public static final short AchievementsMessageComposer = 560; // PRODUCTION-201610052203-260805057
    public static final short LatencyResponseMessageComposer = 1928; // PRODUCTION-201610052203-260805057
    public static final short RoomReadyMessageComposer = 3291; // PRODUCTION-201610052203-260805057
    public static final short HabboActivityPointNotificationMessageComposer = 1758; // PRODUCTION-201610052203-260805057
    public static final short BuddyListMessageComposer = 3265; // PRODUCTION-201610052203-260805057
    public static final short YoutubeDisplayPlaylistsMessageComposer = 1157; // PRODUCTION-201610052203-260805057
    public static final short TradingCompleteMessageComposer = 648; // PRODUCTION-201610052203-260805057
    public static final short PetInformationMessageComposer = 2668; // PRODUCTION-201610052203-260805057
    public static final short ModeratorRoomChatlogMessageComposer = 150; // PRODUCTION-201610052203-260805057
    public static final short MOTDNotificationMessageComposer = 1141; // PRODUCTION-201610052203-260805057
    public static final short GroupInfoMessageComposer = 3487; // PRODUCTION-201610052203-260805057
    public static final short SlideObjectBundleMessageComposer = 135; // PRODUCTION-201610052203-260805057
    public static final short FurniListRemoveMessageComposer = 489; // PRODUCTION-201610052203-260805057
    public static final short FurniListNotificationMessageComposer = 3353; // PRODUCTION-201610052203-260805057
    public static final short RoomInfoUpdatedMessageComposer = 2344; // PRODUCTION-201610052203-260805057
    public static final short AvatarEffectMessageComposer = 2136; // PRODUCTION-201610052203-260805057
    public static final short OpenConnectionMessageComposer = 3388; // PRODUCTION-201610052203-260805057
    public static final short FurniListMessageComposer = 1793; // PRODUCTION-201610052203-260805057
    public static final short PostUpdatedMessageComposer = 1565; // PRODUCTION-201610052203-260805057
    public static final short UserFlatCatsMessageComposer = 943; // PRODUCTION-201610052203-260805057
    public static final short ObjectUpdateMessageComposer = 1001; // PRODUCTION-201610052203-260805057
    public static final short ThreadUpdatedMessageComposer = 3837; // PRODUCTION-201610052203-260805057
    public static final short HabboSearchResultMessageComposer = 1095; // PRODUCTION-201610052203-260805057
    public static final short RespectNotificationMessageComposer = 3081; // PRODUCTION-201610052203-260805057
    public static final short PetHorseFigureInformationMessageComposer = 843; // PRODUCTION-201610052203-260805057
    public static final short MessengerInitMessageComposer = 944; // PRODUCTION-201610052203-260805057
    public static final short ModeratorUserInfoMessageComposer = 1826; // PRODUCTION-201610052203-260805057
    public static final short YouAreControllerMessageComposer = 757; // PRODUCTION-201610052203-260805057
    public static final short RoomRatingMessageComposer = 2862; // PRODUCTION-201610052203-260805057
    public static final short RefreshFavouriteGroupMessageComposer = 1507; // PRODUCTION-201610052203-260805057
    public static final short AvailabilityStatusMessageComposer = 919; // PRODUCTION-201610052203-260805057
    public static final short AchievementUnlockedMessageComposer = 499; // PRODUCTION-201610052203-260805057
    public static final short FlatAccessDeniedMessageComposer = 256; // PRODUCTION-201610052203-260805057
    public static final short NavigatorFlatCatsMessageComposer = 462; // PRODUCTION-201610052203-260805057
    public static final short UsersMessageComposer = 3315; // PRODUCTION-201610052203-260805057
    public static final short SecretKeyMessageComposer = 1236; // PRODUCTION-201610052203-260805057
    public static final short TradingStartMessageComposer = 846; // PRODUCTION-201610052203-260805057
    public static final short RoomSettingsDataMessageComposer = 3963; // PRODUCTION-201610052203-260805057
    public static final short NewBuddyRequestMessageComposer = 3034; // PRODUCTION-201610052203-260805057
    public static final short DoorbellMessageComposer = 384; // PRODUCTION-201610052203-260805057
    public static final short OpenGiftMessageComposer = 3191; // PRODUCTION-201610052203-260805057
    public static final short CantConnectMessageComposer = 2083; // PRODUCTION-201610052203-260805057
    public static final short FloorHeightMapMessageComposer = 1833; // PRODUCTION-201610052203-260805057
    public static final short SellablePetBreedsMessageComposer = 1356; // PRODUCTION-201610052203-260805057
    public static final short AchievementScoreMessageComposer = 2435; // PRODUCTION-201610052203-260805057
    public static final short BuildersClubMembershipMessageComposer = 257; // PRODUCTION-201610052203-260805057
    public static final short PetTrainingPanelMessageComposer = 620; // PRODUCTION-201610052203-260805057
    public static final short QuestCompletedMessageComposer = 2078; // PRODUCTION-201610052203-260805057
    public static final short UserRightsMessageComposer = 2466; // PRODUCTION-201610052203-260805057
    public static final short ForumsListDataMessageComposer = 359; // PRODUCTION-201610052203-260805057
    public static final short UserChangeMessageComposer = 1149; // PRODUCTION-201610052203-260805057
    public static final short ModeratorUserChatlogMessageComposer = 1212; // PRODUCTION-201610052203-260805057
    public static final short GiftWrappingConfigurationMessageComposer = 2504; // PRODUCTION-201610052203-260805057
    public static final short FloorPlanFloorMapMessageComposer = 2956; // PRODUCTION-201610052203-260805057
    public static final short ThreadReplyMessageComposer = 2954; // PRODUCTION-201610052203-260805057
    public static final short GroupCreationWindowMessageComposer = 2920; // PRODUCTION-201610052203-260805057
    public static final short GetGuestRoomResultMessageComposer = 2940; // PRODUCTION-201610052203-260805057
    public static final short RoomNotificationMessageComposer = 413; // PRODUCTION-201610052203-260805057
    public static final short InitCryptoMessageComposer = 3578; // PRODUCTION-201610052203-260805057
    public static final short SoundSettingsMessageComposer = 654; // PRODUCTION-201610052203-260805057
    public static final short WiredTriggerConfigMessageComposer = 3185; // PRODUCTION-201610052203-260805057
    public static final short ItemsMessageComposer = 747; // PRODUCTION-201610052203-260805057
    public static final short PurchaseOKMessageComposer = 3576; // PRODUCTION-201610052203-260805057
    public static final short BadgeEditorPartsMessageComposer = 50; // PRODUCTION-201610052203-260805057
    public static final short NewConsoleMessageMessageComposer = 731; // PRODUCTION-201610052203-260805057
    public static final short HideWiredConfigMessageComposer = 1597; // PRODUCTION-201610052203-260805057
    public static final short CatalogPageMessageComposer = 2554; // PRODUCTION-201610052203-260805057
    public static final short AddExperiencePointsMessageComposer = 479; // PRODUCTION-201610052203-260805057
    public static final short AvatarEffectsMessageComposer = 628; // PRODUCTION-201610052203-260805057
    public static final short QuestListMessageComposer = 537; // PRODUCTION-201610052203-260805057
    public static final short UnbanUserFromRoomMessageComposer = 2569; // PRODUCTION-201610052203-260805057
    public static final short WiredConditionConfigMessageComposer = 1286; // PRODUCTION-201610052203-260805057
    public static final short StickyNoteMessageComposer = 1393; // PRODUCTION-201610052203-260805057
    public static final short SanctionStatusMessageComposer = 175; // PRODUCTION-201610052203-260805057
    public static final short ObjectsMessageComposer = 945; // PRODUCTION-201610052203-260805057
    public static final short RoomVisualizationSettingsMessageComposer = 1509; // PRODUCTION-201610052203-260805057
    public static final short PromoArticlesMessageComposer = 1484; // PRODUCTION-201610052203-260805057
    public static final short MaintenanceStatusMessageComposer = 1015; // PRODUCTION-201610052203-260805057
    public static final short BuddyRequestsMessageComposer = 1666; // PRODUCTION-201610052203-260805057
    public static final short AuthenticationOKMessageComposer = 3536; // PRODUCTION-201610052203-260805057
    public static final short QuestStartedMessageComposer = 1162; // PRODUCTION-201610052203-260805057
    public static final short BotInventoryMessageComposer = 1024; // PRODUCTION-201610052203-260805057
    public static final short PerkAllowancesMessageComposer = 3893; // PRODUCTION-201610052203-260805057
    public static final short RoomEventMessageComposer = 3287; // PRODUCTION-201610052203-260805057
    public static final short RoomMuteSettingsMessageComposer = 1819; // PRODUCTION-201610052203-260805057
    public static final short ModeratorSupportTicketResponseMessageComposer = 3173; // PRODUCTION-201610052203-260805057
    public static final short YouTubeDisplayVideoMessageComposer = 2508; // PRODUCTION-201610052203-260805057
    public static final short RoomPropertyMessageComposer = 1603; // PRODUCTION-201610052203-260805057
    public static final short ModeratorSupportTicketMessageComposer = 1555; // PRODUCTION-201610052203-260805057
    public static final short RoomInviteMessageComposer = 3720; // PRODUCTION-201610052203-260805057
    public static final short FurniListUpdateMessageComposer = 3514; // PRODUCTION-201610052203-260805057
    public static final short BadgesMessageComposer = 3608; // PRODUCTION-201610052203-260805057
    public static final short NavigatorSearchResultSetMessageComposer = 2149; // PRODUCTION-201610052203-260805057
    public static final short IgnoreStatusMessageComposer = 3675; // PRODUCTION-201610052203-260805057
    public static final short ShoutMessageComposer = 1047; // PRODUCTION-201610052203-260805057
    public static final short MoodlightConfigMessageComposer = 1345; // PRODUCTION-201610052203-260805057
    public static final short FurnitureAliasesMessageComposer = 839; // PRODUCTION-201610052203-260805057
    public static final short LoveLockDialogueCloseMessageComposer = 1364; // PRODUCTION-201610052203-260805057
    public static final short TradingErrorMessageComposer = 3129; // PRODUCTION-201610052203-260805057
    public static final short ProfileInformationMessageComposer = 434; // PRODUCTION-201610052203-260805057
    public static final short ModeratorRoomInfoMessageComposer = 1904; // PRODUCTION-201610052203-260805057
    public static final short CampaignMessageComposer = 3799; // PRODUCTION-201610052203-260805057
    public static final short LoveLockDialogueMessageComposer = 2001; // PRODUCTION-201610052203-260805057
    public static final short PurchaseErrorMessageComposer = 451; // PRODUCTION-201610052203-260805057
    public static final short PopularRoomTagsResultMessageComposer = 1578; // PRODUCTION-201610052203-260805057
    public static final short GiftWrappingErrorMessageComposer = 3984; // PRODUCTION-201610052203-260805057
    public static final short WhisperMessageComposer = 2864; // PRODUCTION-201610052203-260805057
    public static final short CatalogItemDiscountMessageComposer = 2551; // PRODUCTION-201610052203-260805057
    public static final short HabboGroupBadgesMessageComposer = 3410; // PRODUCTION-201610052203-260805057
    public static final short CanCreateRoomMessageComposer = 2679; // PRODUCTION-201610052203-260805057
    public static final short ThreadDataMessageComposer = 1618; // PRODUCTION-201610052203-260805057
    public static final short TradingFinishMessageComposer = 3056; // PRODUCTION-201610052203-260805057
    public static final short DanceMessageComposer = 3080; // PRODUCTION-201610052203-260805057
    public static final short GenericErrorMessageComposer = 1530; // PRODUCTION-201610052203-260805057
    public static final short NavigatorPreferencesMessageComposer = 2445; // PRODUCTION-201610052203-260805057
    public static final short MutedMessageComposer = 1048; // PRODUCTION-201610052203-260805057
    public static final short BroadcastMessageAlertMessageComposer = 591; // PRODUCTION-201610052203-260805057
    public static final short YouAreOwnerMessageComposer = 2394; // PRODUCTION-201610052203-260805057
    public static final short ModeratorTicketChatlogMessageComposer = 833; // PRODUCTION-201610052203-260805057
    public static final short BadgeDefinitionsMessageComposer = 3174; // PRODUCTION-201610052203-260805057
    public static final short UserRemoveMessageComposer = 2477; // PRODUCTION-201610052203-260805057
    public static final short RoomSettingsSavedMessageComposer = 1574; // PRODUCTION-201610052203-260805057
    public static final short ModeratorUserRoomVisitsMessageComposer = 558; // PRODUCTION-201610052203-260805057
    public static final short RoomErrorNotifMessageComposer = 1802; // PRODUCTION-201610052203-260805057
    public static final short NavigatorLiftedRoomsMessageComposer = 1063; // PRODUCTION-201610052203-260805057
    public static final short NavigatorMetaDataParserMessageComposer = 1713; // PRODUCTION-201610052203-260805057
    public static final short GetRelationshipsMessageComposer = 1277; // PRODUCTION-201610052203-260805057
    public static final short ItemRemoveMessageComposer = 3727; // PRODUCTION-201610052203-260805057
    public static final short ThreadCreatedMessageComposer = 520; // PRODUCTION-201610052203-260805057
    public static final short EnforceCategoryUpdateMessageComposer = 2704; // PRODUCTION-201610052203-260805057
    public static final short AchievementProgressedMessageComposer = 2267; // PRODUCTION-201610052203-260805057
    public static final short ActivityPointsMessageComposer = 188; // PRODUCTION-201610052203-260805057
    public static final short PetInventoryMessageComposer = 2939; // PRODUCTION-201610052203-260805057
    public static final short GetRoomBannedUsersMessageComposer = 3580; // PRODUCTION-201610052203-260805057
    public static final short UserUpdateMessageComposer = 1083; // PRODUCTION-201610052203-260805057
    public static final short FavouritesMessageComposer = 3505; // PRODUCTION-201610052203-260805057
    public static final short WardrobeMessageComposer = 980; // PRODUCTION-201610052203-260805057
    public static final short LoveLockDialogueSetLockedMessageComposer = 1364; // PRODUCTION-201610052203-260805057
    public static final short TradingAcceptMessageComposer = 2457; // PRODUCTION-201610052203-260805057
    public static final short SongInventoryMessageComposer = 2020; // PRODUCTION-201610052203-260805057
    public static final short SongIdMessageComposer = 2330; // PRODUCTION-201610052203-260805057
    public static final short SongDataMessageComposer = 6; // PRODUCTION-201610052203-260805057
    public static final short PlaylistMessageComposer = 2195; // PRODUCTION-201610052203-260805057
    public static final short PlayMusicMessageComposer = 2461; // PRODUCTION-201610052203-260805057
    public static final short QuickPollMessageComposer = 2775; // PRODUCTION-201610052203-260805057
    public static final short QuickPollResultMessageComposer = 3661; // PRODUCTION-201610052203-260805057
    public static final short QuickPollResultsMessageComposer = 1585; // PRODUCTION-201610052203-260805057
    public static final short InitializePollMessageComposer = 3582; // PRODUCTION-201610052203-260805057
    public static final short PollMessageComposer = 3428; // PRODUCTION-201610052203-260805057
    public static final short AvatarAspectUpdateMessageComposer = 1149; // PRODUCTION-201610052203-260805057
    public static final short YouAreSpectatorMessageComposer = 1281; // PRODUCTION-201610052203-260805057
    public static final short UpdateStackMapMessageComposer = 3698; // PRODUCTION-201610052203-260805057
    public static final short CameraPhotoPreviewMessageComposer = 1446; // PRODUCTION-201610052203-260805057
    public static final short CameraBuyPhotoMessageComposer = 2544; // PRODUCTION-201610052203-260805057
    public static final short CameraPriceMessageComposer = 3672; // PRODUCTION-201610052203-260805057

    private static Map<Short, String> composerPacketNames = new HashMap<>();

    static {
        try {
            for (Field field : Composers.class.getDeclaredFields()) {
                if (!Modifier.isPrivate(field.getModifiers()))
                    composerPacketNames.put(field.getShort(field.getName()), field.getName());
            }
        } catch (Exception ignored) {

        }
    }

    public static String valueOfId(short packetId) {
        if (composerPacketNames.containsKey(packetId)) {
            return composerPacketNames.get(packetId);
        }

        return "UnknownMessageComposer";
    }
}