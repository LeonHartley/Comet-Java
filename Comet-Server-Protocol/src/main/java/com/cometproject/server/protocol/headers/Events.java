package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Events {
    public static final short ConfirmUsernameMessageEvent = 3800; // PRODUCTION-201702211601-24705679
    public static final short GetRoomBannedUsersMessageEvent = 2428; // PRODUCTION-201702211601-24705679
    public static final short GetPetInventoryMessageEvent = 2829; // PRODUCTION-201702211601-24705679
    public static final short DropHandItemMessageEvent = 2965; // PRODUCTION-201702211601-24705679
    public static final short ReleaseTicketMessageEvent = 1371; // PRODUCTION-201702211601-24705679
    public static final short GetModeratorRoomInfoMessageEvent = 2976; // PRODUCTION-201702211601-24705679
    public static final short KickUserMessageEvent = 279; // PRODUCTION-201702211601-24705679
    public static final short SaveWiredEffectConfigMessageEvent = 2582; // PRODUCTION-201702211601-24705679
    public static final short RespectPetMessageEvent = 1153; // PRODUCTION-201702211601-24705679
    public static final short GenerateSecretKeyMessageEvent = 2325; // PRODUCTION-201702211601-24705679
    public static final short GetModeratorTicketChatlogsMessageEvent = 3970; // PRODUCTION-201702211601-24705679
    public static final short GetAchievementsMessageEvent = 1269; // PRODUCTION-201702211601-24705679
    public static final short SaveWiredTriggerConfigMessageEvent = 2765; // PRODUCTION-201702211601-24705679
    public static final short AcceptGroupMembershipMessageEvent = 3634; // PRODUCTION-201702211601-24705679
    public static final short GetGroupFurniSettingsMessageEvent = 3783; // PRODUCTION-201702211601-24705679
    public static final short TakeAdminRightsMessageEvent = 1650; // PRODUCTION-201702211601-24705679
    public static final short RemoveAllRightsMessageEvent = 331; // PRODUCTION-201702211601-24705679
    public static final short UpdateThreadMessageEvent = 67; // PRODUCTION-201702211601-24705679
    public static final short ManageGroupMessageEvent = 3739; // PRODUCTION-201702211601-24705679
    public static final short ModifyRoomFilterListMessageEvent = 508; // PRODUCTION-201702211601-24705679
    public static final short SSOTicketMessageEvent = 1326; // PRODUCTION-201702211601-24705679
    public static final short JoinGroupMessageEvent = 3577; // PRODUCTION-201702211601-24705679
    public static final short DeclineGroupMembershipMessageEvent = 3362; // PRODUCTION-201702211601-24705679
    public static final short UniqueIDMessageEvent = 3045; // PRODUCTION-201702211601-24705679
    public static final short RemoveMyRightsMessageEvent = 3719; // PRODUCTION-201702211601-24705679
    public static final short ApplyHorseEffectMessageEvent = 1341; // PRODUCTION-201702211601-24705679
    public static final short GetPetInformationMessageEvent = 466; // PRODUCTION-201702211601-24705679
    public static final short GiveHandItemMessageEvent = 504; // PRODUCTION-201702211601-24705679
    public static final short UpdateFigureDataMessageEvent = 1617; // PRODUCTION-201702211601-24705679
    public static final short RemoveGroupMemberMessageEvent = 3493; // PRODUCTION-201702211601-24705679
    public static final short EventLogMessageEvent = 1718; // PRODUCTION-201702211601-24705679
    public static final short RefreshCampaignMessageEvent = 3693; // PRODUCTION-201702211601-24705679
    public static final short GetPromotableRoomsMessageEvent = 1232; // PRODUCTION-201702211601-24705679
    public static final short UseOneWayGateMessageEvent = 2422; // PRODUCTION-201702211601-24705679
    public static final short AddStickyNoteMessageEvent = 1075; // PRODUCTION-201702211601-24705679
    public static final short GetSelectedBadgesMessageEvent = 3839; // PRODUCTION-201702211601-24705679
    public static final short UpdateStickyNoteMessageEvent = 3327; // PRODUCTION-201702211601-24705679
    public static final short CloseTicketMesageEvent = 571; // PRODUCTION-201702211601-24705679
    public static final short RequestBuddyMessageEvent = 285; // PRODUCTION-201702211601-24705679
    public static final short GetFurnitureAliasesMessageEvent = 1733; // PRODUCTION-201702211601-24705679
    public static final short GetRoomSettingsMessageEvent = 2075; // PRODUCTION-201702211601-24705679
    public static final short RequestFurniInventoryMessageEvent = 3786; // PRODUCTION-201702211601-24705679
    public static final short ModerationKickMessageEvent = 1460; // PRODUCTION-201702211601-24705679
    public static final short OpenFlatConnectionMessageEvent = 181; // PRODUCTION-201702211601-24705679
    public static final short DanceMessageEvent = 214; // PRODUCTION-201702211601-24705679
    public static final short RemoveBuddyMessageEvent = 842; // PRODUCTION-201702211601-24705679
    public static final short LatencyTestMessageEvent = 2581; // PRODUCTION-201702211601-24705679
    public static final short InfoRetrieveMessageEvent = 2176; // PRODUCTION-201702211601-24705679
    public static final short YouTubeGetNextVideo = 3268; // PRODUCTION-201702211601-24705679
    public static final short SetObjectDataMessageEvent = 3591; // PRODUCTION-201702211601-24705679
    public static final short MessengerInitMessageEvent = 1837; // PRODUCTION-201702211601-24705679
    public static final short PickUpBotMessageEvent = 2639; // PRODUCTION-201702211601-24705679
    public static final short ActionMessageEvent = 207; // PRODUCTION-201702211601-24705679
    public static final short LookToMessageEvent = 1463; // PRODUCTION-201702211601-24705679
    public static final short ToggleMoodlightMessageEvent = 2197; // PRODUCTION-201702211601-24705679
    public static final short FollowFriendMessageEvent = 226; // PRODUCTION-201702211601-24705679
    public static final short PickUpPetMessageEvent = 1061; // PRODUCTION-201702211601-24705679
    public static final short GetSellablePetBreedsMessageEvent = 1847; // PRODUCTION-201702211601-24705679
    public static final short GetForumUserProfileMessageEvent = 1982; // PRODUCTION-201702211601-24705679
    public static final short GetForumsListDataMessageEvent = 1900; // PRODUCTION-201702211601-24705679
    public static final short IgnoreUserMessageEvent = 2397; // PRODUCTION-201702211601-24705679
    public static final short DeleteRoomMessageEvent = 870; // PRODUCTION-201702211601-24705679
    public static final short StartQuestMessageEvent = 1614; // PRODUCTION-201702211601-24705679
    public static final short GetGiftWrappingConfigurationMessageEvent = 1169; // PRODUCTION-201702211601-24705679
    public static final short UpdateGroupIdentityMessageEvent = 2738; // PRODUCTION-201702211601-24705679
    public static final short RideHorseMessageEvent = 1171; // PRODUCTION-201702211601-24705679
    public static final short ApplySignMessageEvent = 828; // PRODUCTION-201702211601-24705679
    public static final short FindRandomFriendingRoomMessageEvent = 2962; // PRODUCTION-201702211601-24705679
    public static final short GetModeratorUserChatlogMessageEvent = 457; // PRODUCTION-201702211601-24705679
    public static final short GetWardrobeMessageEvent = 371; // PRODUCTION-201702211601-24705679
    public static final short MuteUserMessageEvent = 3069; // PRODUCTION-201702211601-24705679
    public static final short UpdateForumSettingsMessageEvent = 3468; // PRODUCTION-201702211601-24705679
    public static final short ApplyDecorationMessageEvent = 2190; // PRODUCTION-201702211601-24705679
    public static final short GetBotInventoryMessageEvent = 3029; // PRODUCTION-201702211601-24705679
    public static final short UseHabboWheelMessageEvent = 952; // PRODUCTION-201702211601-24705679
    public static final short EditRoomPromotionMessageEvent = 1710; // PRODUCTION-201702211601-24705679
    public static final short GetModeratorUserInfoMessageEvent = 2925; // PRODUCTION-201702211601-24705679
    public static final short PlaceBotMessageEvent = 3061; // PRODUCTION-201702211601-24705679
    public static final short GetCatalogPageMessageEvent = 3782; // PRODUCTION-201702211601-24705679
    public static final short GetThreadsListDataMessageEvent = 458; // PRODUCTION-201702211601-24705679
    public static final short ShoutMessageEvent = 3088; // PRODUCTION-201702211601-24705679
    public static final short DiceOffMessageEvent = 3144; // PRODUCTION-201702211601-24705679
    public static final short LetUserInMessageEvent = 2363; // PRODUCTION-201702211601-24705679
    public static final short SetActivatedBadgesMessageEvent = 3775; // PRODUCTION-201702211601-24705679
    public static final short UpdateGroupSettingsMessageEvent = 3514; // PRODUCTION-201702211601-24705679
    public static final short ApproveNameMessageEvent = 852; // PRODUCTION-201702211601-24705679
    public static final short SubmitNewTicketMessageEvent = 1957; // PRODUCTION-201702211601-24705679
    public static final short DeleteGroupMessageEvent = 1760; // PRODUCTION-201702211601-24705679
    public static final short DeleteStickyNoteMessageEvent = 3195; // PRODUCTION-201702211601-24705679
    public static final short GetGroupInfoMessageEvent = 390; // PRODUCTION-201702211601-24705679
    public static final short GetStickyNoteMessageEvent = 2117; // PRODUCTION-201702211601-24705679
    public static final short DeclineBuddyMessageEvent = 1357; // PRODUCTION-201702211601-24705679
    public static final short OpenGiftMessageEvent = 235; // PRODUCTION-201702211601-24705679
    public static final short GiveRoomScoreMessageEvent = 2005; // PRODUCTION-201702211601-24705679
    public static final short SetGroupFavouriteMessageEvent = 3417; // PRODUCTION-201702211601-24705679
    public static final short SetMannequinNameMessageEvent = 3125; // PRODUCTION-201702211601-24705679
    public static final short CallForHelpMessageEvent = 1957; // PRODUCTION-201702211601-24705679
    public static final short RoomDimmerSavePresetMessageEvent = 1818; // PRODUCTION-201702211601-24705679
    public static final short UpdateGroupBadgeMessageEvent = 3448; // PRODUCTION-201702211601-24705679
    public static final short PickTicketMessageEvent = 703; // PRODUCTION-201702211601-24705679
    public static final short SetTonerMessageEvent = 543; // PRODUCTION-201702211601-24705679
    public static final short RespectUserMessageEvent = 1487; // PRODUCTION-201702211601-24705679
    public static final short DeleteGroupThreadMessageEvent = 884; // PRODUCTION-201702211601-24705679
    public static final short DeleteGroupReplyMessageEvent = 485; // PRODUCTION-201702211601-24705679

    public static final short CreditFurniRedeemMessageEvent = 3311; // PRODUCTION-201702211601-24705679
    public static final short ModerationMsgMessageEvent = 32; // PRODUCTION-201702211601-24705679
    public static final short ToggleYouTubeVideoMessageEvent = 3908; // PRODUCTION-201702211601-24705679
    public static final short UpdateNavigatorSettingsMessageEvent = 3332; // PRODUCTION-201702211601-24705679
    public static final short ToggleMuteToolMessageEvent = 2130; // PRODUCTION-201702211601-24705679
    public static final short ChatMessageEvent = 3794; // PRODUCTION-201702211601-24705679
    public static final short SaveRoomSettingsMessageEvent = 1109; // PRODUCTION-201702211601-24705679
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 2991; // PRODUCTION-201702211601-24705679
    public static final short GetGroupCreationWindowMessageEvent = 3373; // PRODUCTION-201702211601-24705679
    public static final short GiveAdminRightsMessageEvent = 2502; // PRODUCTION-201702211601-24705679
    public static final short GetGroupMembersMessageEvent = 2415; // PRODUCTION-201702211601-24705679
    public static final short ModerateRoomMessageEvent = 342; // PRODUCTION-201702211601-24705679
    public static final short GetForumStatsMessageEvent = 3827; // PRODUCTION-201702211601-24705679
    public static final short GetPromoArticlesMessageEvent = 2003; // PRODUCTION-201702211601-24705679
    public static final short SitMessageEvent = 157; // PRODUCTION-201702211601-24705679
    public static final short SetSoundSettingsMessageEvent = 3416; // PRODUCTION-201702211601-24705679
    public static final short ModerationCautionMessageEvent = 1023; // PRODUCTION-201702211601-24705679
    public static final short InitializeFloorPlanSessionMessageEvent = 3438; // PRODUCTION-201702211601-24705679
    public static final short ModeratorActionMessageEvent = 3169; // PRODUCTION-201702211601-24705679
    public static final short PostGroupContentMessageEvent = 3828; // PRODUCTION-201702211601-24705679
    public static final short GetModeratorRoomChatlogMessageEvent = 2782; // PRODUCTION-201702211601-24705679
    public static final short GetUserFlatCatsMessageEvent = 815; // PRODUCTION-201702211601-24705679
    public static final short RemoveRightsMessageEvent = 139; // PRODUCTION-201702211601-24705679
    public static final short ModerationBanMessageEvent = 460; // PRODUCTION-201702211601-24705679
    public static final short CanCreateRoomMessageEvent = 2875; // PRODUCTION-201702211601-24705679
    public static final short UseWallItemMessageEvent = 2285; // PRODUCTION-201702211601-24705679
    public static final short PlaceObjectMessageEvent = 3278; // PRODUCTION-201702211601-24705679
    public static final short OpenBotActionMessageEvent = 124; // PRODUCTION-201702211601-24705679
    public static final short GetEventCategoriesMessageEvent = 2841; // PRODUCTION-201702211601-24705679
    public static final short GetRoomEntryDataMessageEvent = 2452; // PRODUCTION-201702211601-24705679
    public static final short MoveWallItemMessageEvent = 463; // PRODUCTION-201702211601-24705679
    public static final short UpdateGroupColoursMessageEvent = 2388; // PRODUCTION-201702211601-24705679
    public static final short HabboSearchMessageEvent = 411; // PRODUCTION-201702211601-24705679
    public static final short CommandBotMessageEvent = 973; // PRODUCTION-201702211601-24705679
    public static final short SetCustomStackingHeightMessageEvent = 2681; // PRODUCTION-201702211601-24705679
    public static final short UnIgnoreUserMessageEvent = 3129; // PRODUCTION-201702211601-24705679
    public static final short GetGuestRoomMessageEvent = 2092; // PRODUCTION-201702211601-24705679
    public static final short SetMannequinFigureMessageEvent = 1180; // PRODUCTION-201702211601-24705679
    public static final short AssignRightsMessageEvent = 152; // PRODUCTION-201702211601-24705679
    public static final short GetYouTubeTelevisionMessageEvent = 2509; // PRODUCTION-201702211601-24705679
    public static final short SetMessengerInviteStatusMessageEvent = 472; // PRODUCTION-201702211601-24705679
    public static final short UpdateFloorPropertiesMessageEvent = 2255; // PRODUCTION-201702211601-24705679
    public static final short GetMoodlightConfigMessageEvent = 2644; // PRODUCTION-201702211601-24705679
    public static final short PurchaseRoomPromotionMessageEvent = 3424; // PRODUCTION-201702211601-24705679
    public static final short SendRoomInviteMessageEvent = 2426; // PRODUCTION-201702211601-24705679
    public static final short ModerationMuteMessageEvent = 2191; // PRODUCTION-201702211601-24705679
    public static final short SetRelationshipMessageEvent = 3121; // PRODUCTION-201702211601-24705679
    public static final short ChangeMottoMessageEvent = 2430; // PRODUCTION-201702211601-24705679
    public static final short UnbanUserFromRoomMessageEvent = 1910; // PRODUCTION-201702211601-24705679
    public static final short GetRoomRightsMessageEvent = 3968; // PRODUCTION-201702211601-24705679
    public static final short PurchaseGroupMessageEvent = 355; // PRODUCTION-201702211601-24705679
    public static final short CreateFlatMessageEvent = 2178; // PRODUCTION-201702211601-24705679
    public static final short OpenHelpToolMessageEvent = 347; // PRODUCTION-201702211601-24705679
    public static final short ThrowDiceMessageEvent = 2020; // PRODUCTION-201702211601-24705679
    public static final short SaveWiredConditionConfigMessageEvent = 3322; // PRODUCTION-201702211601-24705679
    public static final short GetCatalogOfferMessageEvent = 845; // PRODUCTION-201702211601-24705679
    public static final short PurchaseFromCatalogMessageEvent = 1813; // PRODUCTION-201702211601-24705679
    public static final short PickupObjectMessageEvent = 3549; // PRODUCTION-201702211601-24705679
    public static final short CancelQuestMessageEvent = 3131; // PRODUCTION-201702211601-24705679
    public static final short NavigatorSearchMessageEvent = 1340; // PRODUCTION-201702211601-24705679
    public static final short MoveAvatarMessageEvent = 1611; // PRODUCTION-201702211601-24705679
    public static final short GetClientVersionMessageEvent = 4000; // PRODUCTION-201702211601-24705679
    public static final short InitializeNavigatorMessageEvent = 2367; // PRODUCTION-201702211601-24705679
    public static final short GetRoomFilterListMessageEvent = 1094; // PRODUCTION-201702211601-24705679
    public static final short WhisperMessageEvent = 3399; // PRODUCTION-201702211601-24705679
    public static final short InitCryptoMessageEvent = 2701; // PRODUCTION-201702211601-24705679
    public static final short GetPetTrainingPanelMessageEvent = 559; // PRODUCTION-201702211601-24705679
    public static final short MoveObjectMessageEvent = 1051; // PRODUCTION-201702211601-24705679
    public static final short StartTypingMessageEvent = 1223; // PRODUCTION-201702211601-24705679
    public static final short GoToHotelViewMessageEvent = 616; // PRODUCTION-201702211601-24705679
    public static final short GetExtendedProfileMessageEvent = 1194; // PRODUCTION-201702211601-24705679
    public static final short SendMsgMessageEvent = 606; // PRODUCTION-201702211601-24705679
    public static final short CancelTypingMessageEvent = 3; // PRODUCTION-201702211601-24705679
    public static final short GetGroupFurniConfigMessageEvent = 2864; // PRODUCTION-201702211601-24705679


    public static final short RemoveGroupFavouriteMessageEvent = 2911; // PRODUCTION-201702211601-24705679
    public static final short PlacePetMessageEvent = 2025; // PRODUCTION-201702211601-24705679
    public static final short ModifyWhoCanRideHorseMessageEvent = 2685; // PRODUCTION-201702211601-24705679
    public static final short GetRelationshipsMessageEvent = 3668; // PRODUCTION-201702211601-24705679
    public static final short GetCatalogIndexMessageEvent = 3336; // PRODUCTION-201702211601-24705679
    public static final short ScrGetUserInfoMessageEvent = 3905; // PRODUCTION-201702211601-24705679
    public static final short ConfirmLoveLockMessageEvent = 2199; // PRODUCTION-201702211601-24705679
    public static final short RemoveSaddleFromHorseMessageEvent = 1482; // PRODUCTION-201702211601-24705679
    public static final short AcceptBuddyMessageEvent = 1785; // PRODUCTION-201702211601-24705679
    public static final short GetQuestListMessageEvent = 3073; // PRODUCTION-201702211601-24705679
    public static final short SaveWardrobeOutfitMessageEvent = 2626; // PRODUCTION-201702211601-24705679
    public static final short BanUserMessageEvent = 928; // PRODUCTION-201702211601-24705679
    public static final short GetThreadDataMessageEvent = 3228; // PRODUCTION-201702211601-24705679
    public static final short GetBadgesMessageEvent = 2336; // PRODUCTION-201702211601-24705679
    public static final short UseFurnitureMessageEvent = 1908; // PRODUCTION-201702211601-24705679
    public static final short GoToFlatMessageEvent = 1812; // PRODUCTION-201702211601-24705679
    public static final short GetModeratorUserRoomVisitsMessageEvent = 3816; // PRODUCTION-201702211601-24705679
    public static final short GetSanctionStatusMessageEvent = 2210; // PRODUCTION-201702211601-24705679
    public static final short SetChatPreferenceMessageEvent = 2193; // PRODUCTION-201702211601-24705679
    public static final short ResizeNavigatorMessageEvent = 1763; // PRODUCTION-201702211601-24705679
    public static final short CameraDataMessageEvent = 880; // PRODUCTION-201702211601-24705679
    public static final short RenderRoomMessageEvent = 3606; // PRODUCTION-201702211601-24705679
    public static final short BuyPhotoMessageEvent = 2260; // PRODUCTION-201702211601-24705679
    public static final short SongInventoryMessageEvent = 1987; // PRODUCTION-201702211601-24705679
    public static final short SongIdMessageEvent = 101; // PRODUCTION-201702211601-24705679
    public static final short SongDataMessageEvent = 2145; // PRODUCTION-201702211601-24705679
    public static final short PlaylistMessageEvent = 1046; // PRODUCTION-201702211601-24705679
    public static final short PlaylistAddMessageEvent = 1627; // PRODUCTION-201702211601-24705679
    public static final short PlaylistRemoveMessageEvent = 3990; // PRODUCTION-201702211601-24705679
    public static final short StaffPickRoomMessageEvent = 2669; // PRODUCTION-201702211601-24705679
    public static final short SubmitPollAnswerMessageEvent = 1782; // PRODUCTION-201702211601-24705679
    public static final short GetPollMessageEvent = 801; // PRODUCTION-201702211601-24705679
    public static final short UpdateSnapshotsMessageEvent = 3202; // PRODUCTION-201702211601-24705679
    public static final short MarkAsReadMessageEvent = 2198; // PRODUCTION-201702211601-24705679
    public static final short InitTradeMessageEvent = 2566; // PRODUCTION-201702211601-24705679
    public static final short TradingOfferItemMessageEvent = 2250; // PRODUCTION-201702211601-24705679
    public static final short TradingOfferItemsMessageEvent = 2981; // PRODUCTION-201702211601-24705679
    public static final short TradingRemoveItemMessageEvent = 3325; // PRODUCTION-201702211601-24705679
    public static final short TradingAcceptMessageEvent = 2133; // PRODUCTION-201702211601-24705679
    public static final short TradingCancelMessageEvent = 506; // PRODUCTION-201702211601-24705679
    public static final short TradingModifyMessageEvent = 3973; // PRODUCTION-201702211601-24705679
    public static final short TradingConfirmMessageEvent = 265; // PRODUCTION-201702211601-24705679
    public static final short TradingCancelConfirmMessageEvent = 2528; // PRODUCTION-201702211601-24705679
    public static final short RedeemVoucherMessageEvent = 3175; // PRODUCTION-201702211601-24705679
    public static final short Hide = 3175; // PRODUCTION-201702211601-24705679
    public static final short ChangeNameMessageEvent = 562; // PRODUCTION-201702211601-24705679
    public static final short CheckValidNameMessageEvent = 1954; // PRODUCTION-201702211601-24705679

    public static final short RequestGuideToolMessageEvent = 2165; // PRODUCTION-201702211601-24705679
    public static final short RequestGuideAssistanceMessageEvent = 158; // PRODUCTION-201702211601-24705679
    public static final short GuideUserTypingMessageEvent = 487; // PRODUCTION-201702211601-24705679
    public static final short GuideReportHelperMessageEvent = 579; // PRODUCTION-201702211601-24705679
    public static final short GuideRecommendHelperMessageEvent = 802; // PRODUCTION-201702211601-24705679
    public static final short GuideUserMessageMessageEvent = 2826; // PRODUCTION-201702211601-24705679
    public static final short GuideCancelHelpRequestMessageEvent = 2488; // PRODUCTION-201702211601-24705679
    public static final short GuideHandleHelpRequestMessageEvent = 116; // PRODUCTION-201702211601-24705679
    public static final short GuideVisitUserMessageEvent = 916; // PRODUCTION-201702211601-24705679
    public static final short GuideInviteUserMessageEvent = 2131; // PRODUCTION-201702211601-24705679
    public static final short GuideCloseHelpRequestMessageEvent = 3467; // PRODUCTION-201702211601-24705679
    public static final short GuardianNoUpdatesWantedMessageEvent = 314; // PRODUCTION-201702211601-24705679
    public static final short GuardianVoteMessageEvent = 1768; // PRODUCTION-201702211601-24705679
    public static final short GuardianAcceptRequestMessageEvent = 2760; // PRODUCTION-201702211601-24705679

    public static final short RequestReportUserBullyingMessageEvent = 2062; // PRODUCTION-201702211601-24705679
    public static final short ReportBullyMessageEvent = 3358; // PRODUCTION-201702211601-24705679

    public static final short GetUserTagsMessageEvent = 3007; // PRODUCTION-201702211601-24705679
    public static final short SaveNavigatorSearchMessageEvent = 1979; // PRODUCTION-201702211601-24705679
    public static final short DeleteNavigatorSavedSearchMessageEvent = 3655; // PRODUCTION-201702211601-24705679
    public static final short SaveFootballGateMessageEvent = 3974; // PRODUCTION-201702211601-24705679


    private static Map<Short, String> eventPacketNames = new HashMap<>();

    static {
        try {
            for (Field field : Events.class.getDeclaredFields()) {
                if (!Modifier.isPrivate(field.getModifiers()))
                    eventPacketNames.put(field.getShort(field.getName()), field.getName());
            }
        } catch (Exception ignored) {

        }
    }

    public static String valueOfId(short packetId) {
        if (eventPacketNames.containsKey(packetId)) {
            return eventPacketNames.get(packetId);
        }

        return "UnknownMessageEvent";
    }
}