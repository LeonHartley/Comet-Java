package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Composers {
    public static final short WiredRewardMessageComposer = 2475; // PRODUCTION-201702211601-24705679
    public static final short HeightMapMessageComposer = 2987; // PRODUCTION-201702211601-24705679
    public static final short CallForHelpPendingCallsMessageComposer = 3937; // PRODUCTION-201702211601-24705679
    public static final short ChatMessageComposer = 2695; // PRODUCTION-201702211601-24705679
    public static final short GroupMembersMessageComposer = 2146; // PRODUCTION-201702211601-24705679
    public static final short OpenBotActionMessageComposer = 441; // PRODUCTION-201702211601-24705679
    public static final short UserObjectMessageComposer = 843; // PRODUCTION-201702211601-24705679
    public static final short ActionMessageComposer = 476; // PRODUCTION-201702211601-24705679
    public static final short ManageGroupMessageComposer = 642; // PRODUCTION-201702211601-24705679
    public static final short FloodControlMessageComposer = 985; // PRODUCTION-201702211601-24705679
    public static final short FlatControllerAddedMessageComposer = 3157; // PRODUCTION-201702211601-24705679
    public static final short TradingClosedMessageComposer = 3249; // PRODUCTION-201702211601-24705679
    public static final short FlatCreatedMessageComposer = 3857; // PRODUCTION-201702211601-24705679
    public static final short ScrSendUserInfoMessageComposer = 1813; // PRODUCTION-201702211601-24705679
    public static final short CheckPetNameMessageComposer = 2955; // PRODUCTION-201702211601-24705679
    public static final short QuestAbortedMessageComposer = 2304; // PRODUCTION-201702211601-24705679
    public static final short RespectPetNotificationMessageComposer = 3186; // PRODUCTION-201702211601-24705679
    public static final short PromotableRoomsMessageComposer = 1416; // PRODUCTION-201702211601-24705679
    public static final short CloseConnectionMessageComposer = 1452; // PRODUCTION-201702211601-24705679
    public static final short CfhTopicsInitMessageComposer = 849; // PRODUCTION-201702211601-24705679
    public static final short WiredEffectConfigMessageComposer = 656; // PRODUCTION-201702211601-24705679
    public static final short FriendListUpdateMessageComposer = 2385; // PRODUCTION-201702211601-24705679
    public static final short ObjectAddMessageComposer = 387; // PRODUCTION-201702211601-24705679
    public static final short NavigatorCollapsedCategoriesMessageComposer = 1238; // PRODUCTION-201702211601-24705679
    public static final short RoomRightsListMessageComposer = 892; // PRODUCTION-201702211601-24705679
    public static final short TradingUpdateMessageComposer = 600; // PRODUCTION-201702211601-24705679
    public static final short CarryObjectMessageComposer = 1303; // PRODUCTION-201702211601-24705679
    public static final short NewGroupInfoMessageComposer = 2892; // PRODUCTION-201702211601-24705679
    public static final short RoomForwardMessageComposer = 1373; // PRODUCTION-201702211601-24705679
    public static final short GroupFurniSettingsMessageComposer = 2917; // PRODUCTION-201702211601-24705679
    public static final short CreditBalanceMessageComposer = 1066; // PRODUCTION-201702211601-24705679
    public static final short CatalogUpdatedMessageComposer = 789; // PRODUCTION-201702211601-24705679
    public static final short UserTypingMessageComposer = 2510; // PRODUCTION-201702211601-24705679
    public static final short ObjectRemoveMessageComposer = 3402; // PRODUCTION-201702211601-24705679
    public static final short RoomEntryInfoMessageComposer = 3360; // PRODUCTION-201702211601-24705679
    public static final short CatalogOfferMessageComposer = 2043; // PRODUCTION-201702211601-24705679
    public static final short CatalogIndexMessageComposer = 2420; // PRODUCTION-201702211601-24705679
    public static final short ThreadsListDataMessageComposer = 2164; // PRODUCTION-201702211601-24705679
    public static final short GroupFurniConfigMessageComposer = 3655; // PRODUCTION-201702211601-24705679
    public static final short HabboUserBadgesMessageComposer = 942; // PRODUCTION-201702211601-24705679
    public static final short FlatAccessibleMessageComposer = 984; // PRODUCTION-201702211601-24705679
    public static final short ModeratorInitMessageComposer = 3867; // PRODUCTION-201702211601-24705679
    public static final short FloorPlanSendDoorMessageComposer = 2531; // PRODUCTION-201702211601-24705679
    public static final short SleepMessageComposer = 3630; // PRODUCTION-201702211601-24705679
    public static final short FlatControllerRemovedMessageComposer = 3174; // PRODUCTION-201702211601-24705679
    public static final short UniqueMachineIDMessageComposer = 1935; // PRODUCTION-201702211601-24705679
    public static final short ItemAddMessageComposer = 926; // PRODUCTION-201702211601-24705679
    public static final short GroupForumDataMessageComposer = 3185; // PRODUCTION-201702211601-24705679
    public static final short UpdateFreezeLivesMessageComposer = 1697; // PRODUCTION-201702211601-24705679
    public static final short NavigatorSettingsMessageComposer = 714; // PRODUCTION-201702211601-24705679
    public static final short ItemUpdateMessageComposer = 2152; // PRODUCTION-201702211601-24705679
    public static final short AchievementsMessageComposer = 3932; // PRODUCTION-201702211601-24705679
    public static final short LatencyResponseMessageComposer = 932; // PRODUCTION-201702211601-24705679
    public static final short RoomReadyMessageComposer = 3343; // PRODUCTION-201702211601-24705679
    public static final short HabboActivityPointNotificationMessageComposer = 2173; // PRODUCTION-201702211601-24705679
    public static final short BuddyListMessageComposer = 3561; // PRODUCTION-201702211601-24705679
    public static final short YoutubeDisplayPlaylistsMessageComposer = 3821; // PRODUCTION-201702211601-24705679
    public static final short TradingCompleteMessageComposer = 2484; // PRODUCTION-201702211601-24705679
    public static final short PetInformationMessageComposer = 2678; // PRODUCTION-201702211601-24705679
    public static final short ModeratorRoomChatlogMessageComposer = 2020; // PRODUCTION-201702211601-24705679
    public static final short MOTDNotificationMessageComposer = 93; // PRODUCTION-201702211601-24705679
    public static final short GroupInfoMessageComposer = 3585; // PRODUCTION-201702211601-24705679
    public static final short SlideObjectBundleMessageComposer = 3912; // PRODUCTION-201702211601-24705679
    public static final short FurniListRemoveMessageComposer = 3764; // PRODUCTION-201702211601-24705679
    public static final short FurniListNotificationMessageComposer = 80; // PRODUCTION-201702211601-24705679
    public static final short RoomInfoUpdatedMessageComposer = 1834; // PRODUCTION-201702211601-24705679
    public static final short AvatarEffectMessageComposer = 2185; // PRODUCTION-201702211601-24705679
    public static final short OpenConnectionMessageComposer = 3151; // PRODUCTION-201702211601-24705679
    public static final short FurniListMessageComposer = 934; // PRODUCTION-201702211601-24705679
    public static final short PostUpdatedMessageComposer = 118; // PRODUCTION-201702211601-24705679
    public static final short UserFlatCatsMessageComposer = 87; // PRODUCTION-201702211601-24705679
    public static final short ObjectUpdateMessageComposer = 3144; // PRODUCTION-201702211601-24705679
    public static final short ThreadUpdatedMessageComposer = 1043; // PRODUCTION-201702211601-24705679
    public static final short HabboSearchResultMessageComposer = 20; // PRODUCTION-201702211601-24705679
    public static final short RespectNotificationMessageComposer = 3083; // PRODUCTION-201702211601-24705679
    public static final short PetHorseFigureInformationMessageComposer = 1369; // PRODUCTION-201702211601-24705679
    public static final short MessengerInitMessageComposer = 3993; // PRODUCTION-201702211601-24705679
    public static final short ModeratorUserInfoMessageComposer = 33; // PRODUCTION-201702211601-24705679
    public static final short YouAreControllerMessageComposer = 3124; // PRODUCTION-201702211601-24705679
    public static final short RoomRatingMessageComposer = 2364; // PRODUCTION-201702211601-24705679
    public static final short RefreshFavouriteGroupMessageComposer = 1799; // PRODUCTION-201702211601-24705679
    public static final short AvailabilityStatusMessageComposer = 1899; // PRODUCTION-201702211601-24705679
    public static final short AchievementUnlockedMessageComposer = 1411; // PRODUCTION-201702211601-24705679
    public static final short FlatAccessDeniedMessageComposer = 3883; // PRODUCTION-201702211601-24705679
    public static final short NavigatorFlatCatsMessageComposer = 1997; // PRODUCTION-201702211601-24705679
    public static final short UsersMessageComposer = 165; // PRODUCTION-201702211601-24705679
    public static final short SecretKeyMessageComposer = 1147; // PRODUCTION-201702211601-24705679
    public static final short TradingStartMessageComposer = 2799; // PRODUCTION-201702211601-24705679
    public static final short RoomSettingsDataMessageComposer = 157; // PRODUCTION-201702211601-24705679
    public static final short NewBuddyRequestMessageComposer = 1185; // PRODUCTION-201702211601-24705679
    public static final short DoorbellMessageComposer = 544; // PRODUCTION-201702211601-24705679
    public static final short OpenGiftMessageComposer = 3146; // PRODUCTION-201702211601-24705679
    public static final short CantConnectMessageComposer = 2250; // PRODUCTION-201702211601-24705679
    public static final short FloorHeightMapMessageComposer = 2458; // PRODUCTION-201702211601-24705679
    public static final short SellablePetBreedsMessageComposer = 3656; // PRODUCTION-201702211601-24705679
    public static final short AchievementScoreMessageComposer = 2993; // PRODUCTION-201702211601-24705679
    public static final short BuildersClubMembershipMessageComposer = 2868; // PRODUCTION-201702211601-24705679
    public static final short PetTrainingPanelMessageComposer = 1933; // PRODUCTION-201702211601-24705679
    public static final short QuestCompletedMessageComposer = 3620; // PRODUCTION-201702211601-24705679
    public static final short UserRightsMessageComposer = 3935; // PRODUCTION-201702211601-24705679
    public static final short ForumsListDataMessageComposer = 460; // PRODUCTION-201702211601-24705679
    public static final short UserChangeMessageComposer = 2567; // PRODUCTION-201702211601-24705679
    public static final short ModeratorUserChatlogMessageComposer = 2357; // PRODUCTION-201702211601-24705679
    public static final short GiftWrappingConfigurationMessageComposer = 546; // PRODUCTION-201702211601-24705679
    public static final short FloorPlanFloorMapMessageComposer = 133; // PRODUCTION-201702211601-24705679
    public static final short ThreadReplyMessageComposer = 1488; // PRODUCTION-201702211601-24705679
    public static final short GroupCreationWindowMessageComposer = 642; // PRODUCTION-201702211601-24705679
    public static final short GetGuestRoomResultMessageComposer = 2991; // PRODUCTION-201702211601-24705679
    public static final short RoomNotificationMessageComposer = 1234; // PRODUCTION-201702211601-24705679
    public static final short InitCryptoMessageComposer = 1247; // PRODUCTION-201702211601-24705679
    public static final short SoundSettingsMessageComposer = 2158; // PRODUCTION-201702211601-24705679
    public static final short WiredTriggerConfigMessageComposer = 2246; // PRODUCTION-201702211601-24705679
    public static final short ItemsMessageComposer = 634; // PRODUCTION-201702211601-24705679
    public static final short PurchaseOKMessageComposer = 3517; // PRODUCTION-201702211601-24705679
    public static final short BadgeEditorPartsMessageComposer = 3420; // PRODUCTION-201702211601-24705679
    public static final short NewConsoleMessageMessageComposer = 3501; // PRODUCTION-201702211601-24705679
    public static final short HideWiredConfigMessageComposer = 2657; // PRODUCTION-201702211601-24705679
    public static final short CatalogPageMessageComposer = 3491; // PRODUCTION-201702211601-24705679
    public static final short AddExperiencePointsMessageComposer = 1900; // PRODUCTION-201702211601-24705679
    public static final short AvatarEffectsMessageComposer = 3820; // PRODUCTION-201702211601-24705679
    public static final short QuestListMessageComposer = 1694; // PRODUCTION-201702211601-24705679
    public static final short UnbanUserFromRoomMessageComposer = 128; // PRODUCTION-201702211601-24705679
    public static final short WiredConditionConfigMessageComposer = 3653; // PRODUCTION-201702211601-24705679
    public static final short StickyNoteMessageComposer = 214; // PRODUCTION-201702211601-24705679
    public static final short SanctionStatusMessageComposer = 1748; // PRODUCTION-201702211601-24705679
    public static final short ObjectsMessageComposer = 1250; // PRODUCTION-201702211601-24705679
    public static final short RoomVisualizationSettingsMessageComposer = 65; // PRODUCTION-201702211601-24705679
    public static final short PromoArticlesMessageComposer = 1345; // PRODUCTION-201702211601-24705679
    public static final short MaintenanceStatusMessageComposer = 3172; // PRODUCTION-201702211601-24705679
    public static final short BuddyRequestsMessageComposer = 2000; // PRODUCTION-201702211601-24705679
    public static final short AuthenticationOKMessageComposer = 1363; // PRODUCTION-201702211601-24705679
    public static final short QuestStartedMessageComposer = 2714; // PRODUCTION-201702211601-24705679
    public static final short BotInventoryMessageComposer = 2143; // PRODUCTION-201702211601-24705679
    public static final short PerkAllowancesMessageComposer = 2318; // PRODUCTION-201702211601-24705679
    public static final short RoomEventMessageComposer = 417; // PRODUCTION-201702211601-24705679
    public static final short RoomMuteSettingsMessageComposer = 559; // PRODUCTION-201702211601-24705679
    public static final short ModeratorSupportTicketResponseMessageComposer = 235; // PRODUCTION-201702211601-24705679
    public static final short YouTubeDisplayVideoMessageComposer = 1901; // PRODUCTION-201702211601-24705679
    public static final short RoomPropertyMessageComposer = 2161; // PRODUCTION-201702211601-24705679
    public static final short ModeratorSupportTicketMessageComposer = 3243; // PRODUCTION-201702211601-24705679
    public static final short RoomInviteMessageComposer = 45; // PRODUCTION-201702211601-24705679
    public static final short FurniListUpdateMessageComposer = 3453; // PRODUCTION-201702211601-24705679
    public static final short BadgesMessageComposer = 2590; // PRODUCTION-201702211601-24705679
    public static final short NavigatorSearchResultSetMessageComposer = 635; // PRODUCTION-201702211601-24705679
    public static final short IgnoreStatusMessageComposer = 979; // PRODUCTION-201702211601-24705679
    public static final short ShoutMessageComposer = 678; // PRODUCTION-201702211601-24705679
    public static final short MoodlightConfigMessageComposer = 2824; // PRODUCTION-201702211601-24705679
    public static final short FurnitureAliasesMessageComposer = 2552; // PRODUCTION-201702211601-24705679
    public static final short LoveLockDialogueCloseMessageComposer = 1702; // PRODUCTION-201702211601-24705679
    public static final short TradingErrorMessageComposer = 1593; // PRODUCTION-201702211601-24705679
    public static final short ProfileInformationMessageComposer = 297; // PRODUCTION-201702211601-24705679
    public static final short ModeratorRoomInfoMessageComposer = 308; // PRODUCTION-201702211601-24705679
    public static final short CampaignMessageComposer = 720; // PRODUCTION-201702211601-24705679
    public static final short LoveLockDialogueMessageComposer = 1217; // PRODUCTION-201702211601-24705679
    public static final short PurchaseErrorMessageComposer = 3645; // PRODUCTION-201702211601-24705679
    public static final short PopularRoomTagsResultMessageComposer = 3480; // PRODUCTION-201702211601-24705679
    public static final short GiftWrappingErrorMessageComposer = 2309; // PRODUCTION-201702211601-24705679
    public static final short WhisperMessageComposer = 3197; // PRODUCTION-201702211601-24705679
    public static final short CatalogItemDiscountMessageComposer = 416; // PRODUCTION-201702211601-24705679
    public static final short HabboGroupBadgesMessageComposer = 1348; // PRODUCTION-201702211601-24705679
    public static final short CanCreateRoomMessageComposer = 215; // PRODUCTION-201702211601-24705679
    public static final short ThreadDataMessageComposer = 1181; // PRODUCTION-201702211601-24705679
    public static final short TradingFinishMessageComposer = 759; // PRODUCTION-201702211601-24705679
    public static final short DanceMessageComposer = 2502; // PRODUCTION-201702211601-24705679
    public static final short GenericErrorMessageComposer = 132; // PRODUCTION-201702211601-24705679
    public static final short NavigatorPreferencesMessageComposer = 510; // PRODUCTION-201702211601-24705679
    public static final short MutedMessageComposer = 614; // PRODUCTION-201702211601-24705679
    public static final short BroadcastMessageAlertMessageComposer = 407; // PRODUCTION-201702211601-24705679
    public static final short YouAreOwnerMessageComposer = 2742; // PRODUCTION-201702211601-24705679
    public static final short ModeratorTicketChatlogMessageComposer = 3759; // PRODUCTION-201702211601-24705679
    public static final short BadgeDefinitionsMessageComposer = 2780; // PRODUCTION-201702211601-24705679
    public static final short UserRemoveMessageComposer = 2783; // PRODUCTION-201702211601-24705679
    public static final short RoomSettingsSavedMessageComposer = 3969; // PRODUCTION-201702211601-24705679
    public static final short ModeratorUserRoomVisitsMessageComposer = 3281; // PRODUCTION-201702211601-24705679
    public static final short RoomErrorNotifMessageComposer = 835; // PRODUCTION-201702211601-24705679
    public static final short NavigatorLiftedRoomsMessageComposer = 1695; // PRODUCTION-201702211601-24705679
    public static final short NavigatorMetaDataParserMessageComposer = 257; // PRODUCTION-201702211601-24705679
    public static final short GetRelationshipsMessageComposer = 174; // PRODUCTION-201702211601-24705679
    public static final short ItemRemoveMessageComposer = 964; // PRODUCTION-201702211601-24705679
    public static final short ThreadCreatedMessageComposer = 2717; // PRODUCTION-201702211601-24705679
    public static final short EnforceCategoryUpdateMessageComposer = 2479; // PRODUCTION-201702211601-24705679
    public static final short AchievementProgressedMessageComposer = 3311; // PRODUCTION-201702211601-24705679
    public static final short ActivityPointsMessageComposer = 262; // PRODUCTION-201702211601-24705679
    public static final short PetInventoryMessageComposer = 3745; // PRODUCTION-201702211601-24705679
    public static final short GetRoomBannedUsersMessageComposer = 3753; // PRODUCTION-201702211601-24705679
    public static final short UserUpdateMessageComposer = 104; // PRODUCTION-201702211601-24705679
    public static final short FavouritesMessageComposer = 2873; // PRODUCTION-201702211601-24705679
    public static final short WardrobeMessageComposer = 3009; // PRODUCTION-201702211601-24705679
    public static final short LoveLockDialogueSetLockedMessageComposer = 1702; // PRODUCTION-201702211601-24705679
    public static final short TradingAcceptMessageComposer = 1808; // PRODUCTION-201702211601-24705679
    public static final short SongInventoryMessageComposer = 3662; // PRODUCTION-201702211601-24705679
    public static final short SongIdMessageComposer = 393; // PRODUCTION-201702211601-24705679
    public static final short SongDataMessageComposer = 2365; // PRODUCTION-201702211601-24705679
    public static final short PlaylistMessageComposer = 1620; // PRODUCTION-201702211601-24705679
    public static final short PlayMusicMessageComposer = 1284; // PRODUCTION-201702211601-24705679
    public static final short QuickPollMessageComposer = 1715; // PRODUCTION-201702211601-24705679
    public static final short QuickPollResultMessageComposer = 454; // PRODUCTION-201702211601-24705679
    public static final short QuickPollResultsMessageComposer = 2167; // PRODUCTION-201702211601-24705679
    public static final short InitializePollMessageComposer = 1931; // PRODUCTION-201702211601-24705679
    public static final short PollMessageComposer = 2833; // PRODUCTION-201702211601-24705679
    public static final short AvatarAspectUpdateMessageComposer = 1351; // PRODUCTION-201702211601-24705679
    public static final short YouAreSpectatorMessageComposer = 1028; // PRODUCTION-201702211601-24705679
    public static final short UpdateStackMapMessageComposer = 1918; // PRODUCTION-201702211601-24705679
    public static final short CameraPhotoPreviewMessageComposer = 1641; // PRODUCTION-201702211601-24705679
    public static final short CameraBuyPhotoMessageComposer = 3247; // PRODUCTION-201702211601-24705679
    public static final short CameraPriceMessageComposer = 512; // PRODUCTION-201702211601-24705679
    public static final short UpdateUsernameMessageComposer = 1498; // PRODUCTION-201702211601-24705679
    public static final short UserNameChangeMessageComposer = 2505; // PRODUCTION-201702211601-24705679
    public static final short NameChangeUpdateMessageComposer = 2208; // PRODUCTION-201702211601-24705679
    public static final short SendHotelViewLooksMessageComposer = 1310; // PRODUCTION-201702211601-24705679

    public static final short GuideSessionAttachedMessageComposer = 3000; // PRODUCTION-201702211601-24705679
    public static final short GuideSessionDetachedMessageComposer = 2768; // PRODUCTION-201702211601-24705679
    public static final short GuideSessionStartedMessageComposer = 2611; // PRODUCTION-201702211601-24705679
    public static final short GuideSessionEndedMessageComposer = 3490; // PRODUCTION-201702211601-24705679
    public static final short GuideSessionErrorMessageComposer = 2088; // PRODUCTION-201702211601-24705679
    public static final short GuideSessionMessageMessageComposer = 3534; // PRODUCTION-201702211601-24705679
    public static final short GuideSessionRequesterRoomMessageComposer = 1135; // PRODUCTION-201702211601-24705679
    public static final short GuideSessionInvitedToGuideRoomMessageComposer = 1421; // PRODUCTION-201702211601-24705679
    public static final short GuideSessionPartnerIsTypingMessageComposer = 1431; // PRODUCTION-201702211601-24705679

    public static final short GuideToolsMessageComposer = 3533; // PRODUCTION-201702211601-24705679
    public static final short GuardianNewReportReceivedMessageComposer = 3625; // PRODUCTION-201702211601-24705679
    public static final short GuardianVotingRequestedMessageComposer = 3939; // PRODUCTION-201702211601-24705679
    public static final short GuardianVotingVotesMessageComposer = 3148; // PRODUCTION-201702211601-24705679
    public static final short GuardianVotingResultMessageComposer = 1560; // PRODUCTION-201702211601-24705679
    public static final short GuardianVotingTimeEndedMessageComposer = 1626; // PRODUCTION-201702211601-24705679

    public static final short ModToolReportReceivedAlertMessageComposer = 3114; // PRODUCTION-201702211601-24705679

    public static final short BullyReportClosedMessageComposer = 2556; // PRODUCTION-201702211601-24705679
    public static final short BullyReportRequestMessageComposer = 2607; // PRODUCTION-201702211601-24705679
    public static final short BullyReportedMessageMessageComposer = 2652; // PRODUCTION-201702211601-24705679

    public static final short HelperRequestDisabledMessageComposer = 1184; // PRODUCTION-201702211601-24705679

    public static final short UserTagsMessageComposer = 2460; // PRODUCTION-201702211601-24705679

    public static final short GetRoomFilterListMessageComposer = 710; // PRODUCTION-201702211601-24705679

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