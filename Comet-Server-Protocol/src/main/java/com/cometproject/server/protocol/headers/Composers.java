package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Composers {
    public static final short HeightMapMessageComposer = 2830; // PRODUCTION-201608171204-891546786
    public static final short CallForHelpPendingCallsMessageComposer = 2166; // PRODUCTION-201608171204-891546786
    public static final short ChatMessageComposer = 3956; // PRODUCTION-201608171204-891546786
    public static final short GroupMembersMessageComposer = 2477; // PRODUCTION-201608171204-891546786
    public static final short OpenBotActionMessageComposer = 1687; // PRODUCTION-201608171204-891546786
    public static final short UserObjectMessageComposer = 3966; // PRODUCTION-201608171204-891546786
    public static final short ActionMessageComposer = 41; // PRODUCTION-201608171204-891546786
    public static final short ManageGroupMessageComposer = 744; // PRODUCTION-201608171204-891546786
    public static final short FloodControlMessageComposer = 2140; // PRODUCTION-201608171204-891546786
    public static final short FlatControllerAddedMessageComposer = 1586; // PRODUCTION-201608171204-891546786
    public static final short TradingClosedMessageComposer = 3774; // PRODUCTION-201608171204-891546786
    public static final short FlatCreatedMessageComposer = 2678; // PRODUCTION-201608171204-891546786
    public static final short ScrSendUserInfoMessageComposer = 1210; // PRODUCTION-201608171204-891546786
    public static final short CheckPetNameMessageComposer = 740; // PRODUCTION-201608171204-891546786
    public static final short QuestAbortedMessageComposer = 31; // PRODUCTION-201608171204-891546786
    public static final short RespectPetNotificationMessageComposer = 298; // PRODUCTION-201608171204-891546786
    public static final short PromotableRoomsMessageComposer = 1597; // PRODUCTION-201608171204-891546786
    public static final short CloseConnectionMessageComposer = 1976; // PRODUCTION-201608171204-891546786
    public static final short CfhTopicsInitMessageComposer = 2605; // PRODUCTION-201608171204-891546786
    public static final short WiredEffectConfigMessageComposer = 455; // PRODUCTION-201608171204-891546786
    public static final short FriendListUpdateMessageComposer = 1691; // PRODUCTION-201608171204-891546786
    public static final short ObjectAddMessageComposer = 1276; // PRODUCTION-201608171204-891546786
    public static final short NavigatorCollapsedCategoriesMessageComposer = 3802; // PRODUCTION-201608171204-891546786
    public static final short RoomRightsListMessageComposer = 2936; // PRODUCTION-201608171204-891546786
    public static final short TradingUpdateMessageComposer = 739; // PRODUCTION-201608171204-891546786
    public static final short CarryObjectMessageComposer = 1551; // PRODUCTION-201608171204-891546786
    public static final short NewGroupInfoMessageComposer = 2407; // PRODUCTION-201608171204-891546786
    public static final short RoomForwardMessageComposer = 1079; // PRODUCTION-201608171204-891546786
    public static final short GroupFurniSettingsMessageComposer = 2837; // PRODUCTION-201608171204-891546786
    public static final short CreditBalanceMessageComposer = 2509; // PRODUCTION-201608171204-891546786
    public static final short CatalogUpdatedMessageComposer = 1038; // PRODUCTION-201608171204-891546786
    public static final short UserTypingMessageComposer = 3202; // PRODUCTION-201608171204-891546786
    public static final short ObjectRemoveMessageComposer = 1265; // PRODUCTION-201608171204-891546786
    public static final short RoomEntryInfoMessageComposer = 1094; // PRODUCTION-201608171204-891546786
    public static final short CatalogOfferMessageComposer = 2632; // PRODUCTION-201608171204-891546786
    public static final short CatalogIndexMessageComposer = 1609; // PRODUCTION-201608171204-891546786
    public static final short ThreadsListDataMessageComposer = 2020; // PRODUCTION-201608171204-891546786
    public static final short GroupFurniConfigMessageComposer = 1552; // PRODUCTION-201608171204-891546786
    public static final short HabboUserBadgesMessageComposer = 3808; // PRODUCTION-201608171204-891546786
    public static final short FlatAccessibleMessageComposer = 3992; // PRODUCTION-201608171204-891546786
    public static final short ModeratorInitMessageComposer = 2464; // PRODUCTION-201608171204-891546786
    public static final short FloorPlanSendDoorMessageComposer = 1605; // PRODUCTION-201608171204-891546786
    public static final short SleepMessageComposer = 352; // PRODUCTION-201608171204-891546786
    public static final short FlatControllerRemovedMessageComposer = 0; // PRODUCTION-201608171204-891546786
    public static final short UniqueMachineIDMessageComposer = 2747; // PRODUCTION-201608171204-891546786
    public static final short ItemAddMessageComposer = 2255; // PRODUCTION-201608171204-891546786
    public static final short GroupForumDataMessageComposer = 2857; // PRODUCTION-201608171204-891546786
    public static final short UpdateFreezeLivesMessageComposer = 477; // PRODUCTION-201608171204-891546786
    public static final short NavigatorSettingsMessageComposer = 482; // PRODUCTION-201608171204-891546786
    public static final short ItemUpdateMessageComposer = 1332; // PRODUCTION-201608171204-891546786
    public static final short AchievementsMessageComposer = 1171; // PRODUCTION-201608171204-891546786
    public static final short LatencyResponseMessageComposer = 594; // PRODUCTION-201608171204-891546786
    public static final short RoomReadyMessageComposer = 3411; // PRODUCTION-201608171204-891546786
    public static final short HabboActivityPointNotificationMessageComposer = 146; // PRODUCTION-201608171204-891546786
    public static final short BuddyListMessageComposer = 1353; // PRODUCTION-201608171204-891546786
    public static final short YoutubeDisplayPlaylistsMessageComposer = 2638; // PRODUCTION-201608171204-891546786
    public static final short TradingCompleteMessageComposer = 1084; // PRODUCTION-201608171204-891546786
    public static final short PetInformationMessageComposer = 2780; // PRODUCTION-201608171204-891546786
    public static final short ModeratorRoomChatlogMessageComposer = 2132; // PRODUCTION-201608171204-891546786
    public static final short MOTDNotificationMessageComposer = 1244; // PRODUCTION-201608171204-891546786
    public static final short GroupInfoMessageComposer = 951; // PRODUCTION-201608171204-891546786
    public static final short SlideObjectBundleMessageComposer = 644; // PRODUCTION-201608171204-891546786
    public static final short FurniListRemoveMessageComposer = 1579; // PRODUCTION-201608171204-891546786
    public static final short FurniListNotificationMessageComposer = 2277; // PRODUCTION-201608171204-891546786
    public static final short RoomInfoUpdatedMessageComposer = 658; // PRODUCTION-201608171204-891546786
    public static final short AvatarEffectMessageComposer = 3604; // PRODUCTION-201608171204-891546786
    public static final short OpenConnectionMessageComposer = 3500; // PRODUCTION-201608171204-891546786
    public static final short FurniListMessageComposer = 2264; // PRODUCTION-201608171204-891546786
    public static final short PostUpdatedMessageComposer = 1295; // PRODUCTION-201608171204-891546786
    public static final short UserFlatCatsMessageComposer = 2833; // PRODUCTION-201608171204-891546786
    public static final short ObjectUpdateMessageComposer = 2017; // PRODUCTION-201608171204-891546786
    public static final short ThreadUpdatedMessageComposer = 2339; // PRODUCTION-201608171204-891546786
    public static final short HabboSearchResultMessageComposer = 1248; // PRODUCTION-201608171204-891546786
    public static final short RespectNotificationMessageComposer = 1509; // PRODUCTION-201608171204-891546786
    public static final short PetHorseFigureInformationMessageComposer = 3953; // PRODUCTION-201608171204-891546786
    public static final short MessengerInitMessageComposer = 1914; // PRODUCTION-201608171204-891546786
    public static final short ModeratorUserInfoMessageComposer = 2711; // PRODUCTION-201608171204-891546786
    public static final short YouAreControllerMessageComposer = 3888; // PRODUCTION-201608171204-891546786
    public static final short RoomRatingMessageComposer = 99; // PRODUCTION-201608171204-891546786
    public static final short RefreshFavouriteGroupMessageComposer = 3250; // PRODUCTION-201608171204-891546786
    public static final short AvailabilityStatusMessageComposer = 562; // PRODUCTION-201608171204-891546786
    public static final short AchievementUnlockedMessageComposer = 880; // PRODUCTION-201608171204-891546786
    public static final short FlatAccessDeniedMessageComposer = 3251; // PRODUCTION-201608171204-891546786
    public static final short NavigatorFlatCatsMessageComposer = 1219; // PRODUCTION-201608171204-891546786
    public static final short UsersMessageComposer = 1154; // PRODUCTION-201608171204-891546786
    public static final short SecretKeyMessageComposer = 259; // PRODUCTION-201608171204-891546786
    public static final short TradingStartMessageComposer = 1959; // PRODUCTION-201608171204-891546786
    public static final short RoomSettingsDataMessageComposer = 790; // PRODUCTION-201608171204-891546786
    public static final short NewBuddyRequestMessageComposer = 3140; // PRODUCTION-201608171204-891546786
    public static final short DoorbellMessageComposer = 2610; // PRODUCTION-201608171204-891546786
    public static final short OpenGiftMessageComposer = 2280; // PRODUCTION-201608171204-891546786
    public static final short FloorHeightMapMessageComposer = 2389; // PRODUCTION-201608171204-891546786
    public static final short SellablePetBreedsMessageComposer = 164; // PRODUCTION-201608171204-891546786
    public static final short AchievementScoreMessageComposer = 2350; // PRODUCTION-201608171204-891546786
    public static final short BuildersClubMembershipMessageComposer = 2778; // PRODUCTION-201608171204-891546786
    public static final short PetTrainingPanelMessageComposer = 3121; // PRODUCTION-201608171204-891546786
    public static final short QuestCompletedMessageComposer = 3996; // PRODUCTION-201608171204-891546786
    public static final short UserRightsMessageComposer = 3419; // PRODUCTION-201608171204-891546786
    public static final short ForumsListDataMessageComposer = 757; // PRODUCTION-201608171204-891546786
    public static final short UserChangeMessageComposer = 2534; // PRODUCTION-201608171204-891546786
    public static final short ModeratorUserChatlogMessageComposer = 3179; // PRODUCTION-201608171204-891546786
    public static final short GiftWrappingConfigurationMessageComposer = 3444; // PRODUCTION-201608171204-891546786
    public static final short FloorPlanFloorMapMessageComposer = 2282; // PRODUCTION-201608171204-891546786
    public static final short ThreadReplyMessageComposer = 255; // PRODUCTION-201608171204-891546786
    public static final short GroupCreationWindowMessageComposer = 509; // PRODUCTION-201608171204-891546786
    public static final short GetGuestRoomResultMessageComposer = 925; // PRODUCTION-201608171204-891546786
    public static final short RoomNotificationMessageComposer = 3007; // PRODUCTION-201608171204-891546786
    public static final short InitCryptoMessageComposer = 3891; // PRODUCTION-201608171204-891546786
    public static final short SoundSettingsMessageComposer = 907; // PRODUCTION-201608171204-891546786
    public static final short WiredTriggerConfigMessageComposer = 3579; // PRODUCTION-201608171204-891546786
    public static final short ItemsMessageComposer = 2713; // PRODUCTION-201608171204-891546786
    public static final short PurchaseOKMessageComposer = 1086; // PRODUCTION-201608171204-891546786
    public static final short BadgeEditorPartsMessageComposer = 931; // PRODUCTION-201608171204-891546786
    public static final short NewConsoleMessageMessageComposer = 1415; // PRODUCTION-201608171204-891546786
    public static final short HideWiredConfigMessageComposer = 3343; // PRODUCTION-201608171204-891546786
    public static final short CatalogPageMessageComposer = 1013; // PRODUCTION-201608171204-891546786
    public static final short AddExperiencePointsMessageComposer = 2301; // PRODUCTION-201608171204-891546786
    public static final short AvatarEffectsMessageComposer = 1186; // PRODUCTION-201608171204-891546786
    public static final short QuestListMessageComposer = 1285; // PRODUCTION-201608171204-891546786
    public static final short UnbanUserFromRoomMessageComposer = 2807; // PRODUCTION-201608171204-891546786
    public static final short WiredConditionConfigMessageComposer = 58; // PRODUCTION-201608171204-891546786
    public static final short StickyNoteMessageComposer = 3917; // PRODUCTION-201608171204-891546786
    public static final short ObjectsMessageComposer = 530; // PRODUCTION-201608171204-891546786
    public static final short RoomVisualizationSettingsMessageComposer = 1198; // PRODUCTION-201608171204-891546786
    public static final short PromoArticlesMessageComposer = 1106; // PRODUCTION-201608171204-891546786
    public static final short MaintenanceStatusMessageComposer = 1246; // PRODUCTION-201608171204-891546786
    public static final short BuddyRequestsMessageComposer = 1713; // PRODUCTION-201608171204-891546786
    public static final short AuthenticationOKMessageComposer = 742; // PRODUCTION-201608171204-891546786
    public static final short QuestStartedMessageComposer = 343; // PRODUCTION-201608171204-891546786
    public static final short BotInventoryMessageComposer = 2253; // PRODUCTION-201608171204-891546786
    public static final short PerkAllowancesMessageComposer = 441; // PRODUCTION-201608171204-891546786
    public static final short RoomEventMessageComposer = 1466; // PRODUCTION-201608171204-891546786
    public static final short MuteAllInRoomMessageComposer = 1224; // PRODUCTION-201608171204-891546786
    public static final short ModeratorSupportTicketResponseMessageComposer = 642; // PRODUCTION-201608171204-891546786
    public static final short YouTubeDisplayVideoMessageComposer = 2013; // PRODUCTION-201608171204-891546786
    public static final short RoomPropertyMessageComposer = 908; // PRODUCTION-201608171204-891546786
    public static final short ModeratorSupportTicketMessageComposer = 2667; // PRODUCTION-201608171204-891546786
    public static final short RoomInviteMessageComposer = 2733; // PRODUCTION-201608171204-891546786
    public static final short FurniListUpdateMessageComposer = 1623; // PRODUCTION-201608171204-891546786
    public static final short BadgesMessageComposer = 998; // PRODUCTION-201608171204-891546786
    public static final short NavigatorSearchResultSetMessageComposer = 659; // PRODUCTION-201608171204-891546786
    public static final short IgnoreStatusMessageComposer = 226; // PRODUCTION-201608171204-891546786
    public static final short ShoutMessageComposer = 3080; // PRODUCTION-201608171204-891546786
    public static final short MoodlightConfigMessageComposer = 2574; // PRODUCTION-201608171204-891546786
    public static final short FurnitureAliasesMessageComposer = 3998; // PRODUCTION-201608171204-891546786
    public static final short LoveLockDialogueCloseMessageComposer = 271; // PRODUCTION-201608171204-891546786
    public static final short TradingErrorMessageComposer = 602; // PRODUCTION-201608171204-891546786
    public static final short ProfileInformationMessageComposer = 1912; // PRODUCTION-201608171204-891546786
    public static final short ModeratorRoomInfoMessageComposer = 3380; // PRODUCTION-201608171204-891546786
    public static final short CampaignMessageComposer = 3241; // PRODUCTION-201608171204-891546786
    public static final short LoveLockDialogueMessageComposer = 3789; // PRODUCTION-201608171204-891546786
    public static final short PopularRoomTagsResultMessageComposer = 1103; // PRODUCTION-201608171204-891546786
    public static final short GiftWrappingErrorMessageComposer = 1124; // PRODUCTION-201608171204-891546786
    public static final short WhisperMessageComposer = 1984; // PRODUCTION-201608171204-891546786
    public static final short CatalogItemDiscountMessageComposer = 1047; // PRODUCTION-201608171204-891546786
    public static final short HabboGroupBadgesMessageComposer = 1101; // PRODUCTION-201608171204-891546786
    public static final short CanCreateRoomMessageComposer = 584; // PRODUCTION-201608171204-891546786
    public static final short ThreadDataMessageComposer = 3887; // PRODUCTION-201608171204-891546786
    public static final short TradingFinishMessageComposer = 1084; // PRODUCTION-201608171204-891546786
    public static final short DanceMessageComposer = 1226; // PRODUCTION-201608171204-891546786
    public static final short GenericErrorMessageComposer = 1298; // PRODUCTION-201608171204-891546786
    public static final short NavigatorPreferencesMessageComposer = 272; // PRODUCTION-201608171204-891546786
    public static final short MutedMessageComposer = 3510; // PRODUCTION-201608171204-891546786
    public static final short BroadcastMessageAlertMessageComposer = 3848; // PRODUCTION-201608171204-891546786
    public static final short YouAreOwnerMessageComposer = 3441; // PRODUCTION-201608171204-891546786
    public static final short ModeratorTicketChatlogMessageComposer = 3900; // PRODUCTION-201608171204-891546786
    public static final short BadgeDefinitionsMessageComposer = 1856; // PRODUCTION-201608171204-891546786
    public static final short UserRemoveMessageComposer = 1745; // PRODUCTION-201608171204-891546786
    public static final short RoomSettingsSavedMessageComposer = 1794; // PRODUCTION-201608171204-891546786
    public static final short ModeratorUserRoomVisitsMessageComposer = 768; // PRODUCTION-201608171204-891546786
    public static final short RoomErrorNotifMessageComposer = 3313; // PRODUCTION-201608171204-891546786
    public static final short NavigatorLiftedRoomsMessageComposer = 357; // PRODUCTION-201608171204-891546786
    public static final short NavigatorMetaDataParserMessageComposer = 753; // PRODUCTION-201608171204-891546786
    public static final short GetRelationshipsMessageComposer = 1577; // PRODUCTION-201608171204-891546786
    public static final short ItemRemoveMessageComposer = 1570; // PRODUCTION-201608171204-891546786
    public static final short ThreadCreatedMessageComposer = 1002; // PRODUCTION-201608171204-891546786
    public static final short EnforceCategoryUpdateMessageComposer = 3851; // PRODUCTION-201608171204-891546786
    public static final short AchievementProgressedMessageComposer = 2776; // PRODUCTION-201608171204-891546786
    public static final short ActivityPointsMessageComposer = 1337; // PRODUCTION-201608171204-891546786
    public static final short PetInventoryMessageComposer = 2565; // PRODUCTION-201608171204-891546786
    public static final short GetRoomBannedUsersMessageComposer = 3327; // PRODUCTION-201608171204-891546786
    public static final short UserUpdateMessageComposer = 2404; // PRODUCTION-201608171204-891546786
    public static final short FavouritesMessageComposer = 1044; // PRODUCTION-201608171204-891546786
    public static final short WardrobeMessageComposer = 1208; // PRODUCTION-201608171204-891546786
    public static final short LoveLockDialogueSetLockedMessageComposer = 271; // PRODUCTION-201608171204-891546786
    public static final short TradingAcceptMessageComposer = 1243; // PRODUCTION-201608171204-891546786
    public static final short SongInventoryMessageComposer = 2585; // PRODUCTION-201608171204-891546786
    public static final short SongIdMessageComposer = 1174; // PRODUCTION-201608171204-891546786
    public static final short SongDataMessageComposer = 449; // PRODUCTION-201608171204-891546786
    public static final short PlaylistMessageComposer = 3339; // PRODUCTION-201608171204-891546786
    public static final short PlayMusicMessageComposer = 305; // PRODUCTION-201608171204-891546786
    public static final short QuickPollMessageComposer = 3893; // PRODUCTION-201608171204-891546786
    public static final short QuickPollResultMessageComposer = 783; // PRODUCTION-201608171204-891546786
    public static final short QuickPollResultsMessageComposer = 3232; // PRODUCTION-201608171204-891546786
    public static final short InitializePollMessageComposer = 1456; // PRODUCTION-201608171204-891546786
    public static final short PollMessageComposer = 1178; // PRODUCTION-201608171204-891546786
    public static final short AvatarAspectUpdateMessageComposer = 2534; // PRODUCTION-201608171204-891546786
    public static final short YouAreSpectatorMessageComposer = 1130; // PRODUCTION-201608171204-891546786
    public static final short UpdateStackMapMessageComposer = 3024; // PRODUCTION-201608171204-891546786
    public static final short CameraPhotoPreviewMessageComposer = 3695; // PRODUCTION-201608171204-891546786
    public static final short CameraBuyPhotoMessageComposer = 2742; // PRODUCTION-201608171204-891546786
    public static final short CameraPriceMessageComposer = 786; // PRODUCTION-201608171204-891546786

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
