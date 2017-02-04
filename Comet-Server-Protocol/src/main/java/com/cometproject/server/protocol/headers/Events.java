package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Events {
    public static final short RequestGuideAssistanceMessageEvent = 585; // PRODUCTION-201610052203-260805057
    public static final short GuideUserTypingMessageEvent = 1588; // PRODUCTION-201610052203-260805057
    public static final short GuideReportHelperMessageEvent = 776; // PRODUCTION-201610052203-260805057
    public static final short GuideRecommendHelperMessageEvent = 1109; // PRODUCTION-201610052203-260805057
    public static final short GuideUserMessageMessageEvent = 2258; // PRODUCTION-201610052203-260805057
    public static final short GuideCancelHelpRequestMessageEvent = 2653; // PRODUCTION-201610052203-260805057
    public static final short GuideHandleHelpRequestMessageEvent = 3702; // PRODUCTION-201610052203-260805057
    public static final short GuideVisitUserMessageEvent = 2479; // PRODUCTION-201610052203-260805057
    public static final short GuideInviteUserMessageEvent = 2500; // PRODUCTION-201610052203-260805057
    public static final short GuideCloseHelpRequestMessageEvent = 3183; // PRODUCTION-201610052203-260805057
    public static final short GuardianNoUpdatesWantedMessageEvent = 1223; // PRODUCTION-201610052203-260805057
    public static final short GuardianVoteMessageEvent = 2714; // PRODUCTION-201610052203-260805057
    public static final short GuardianAcceptRequestMessageEvent = 1030; // PRODUCTION-201610052203-260805057

    public static final short RequestReportUserBullyingMessageEvent = 1174; // PRODUCTION-201610052203-260805057
    public static final short ReportBullyMessageEvent = 3350; // PRODUCTION-201610052203-260805057
    public static final short OpenGuideToolMessageEvent = 3320; // PRODUCTION-201610052203-260805057
    public static final short ConfirmUsernameMessageEvent = 1709; // PRODUCTION-201610052203-260805057
    public static final short GetRoomBannedUsersMessageEvent = 15; // PRODUCTION-201610052203-260805057
    public static final short GetPetInventoryMessageEvent = 2135; // PRODUCTION-201610052203-260805057
    public static final short DropHandItemMessageEvent = 3887; // PRODUCTION-201610052203-260805057
    public static final short ReleaseTicketMessageEvent = 1510; // PRODUCTION-201610052203-260805057
    public static final short GetModeratorRoomInfoMessageEvent = 1831; // PRODUCTION-201610052203-260805057
    public static final short KickUserMessageEvent = 3731; // PRODUCTION-201610052203-260805057
    public static final short SaveWiredEffectConfigMessageEvent = 1842; // PRODUCTION-201610052203-260805057
    public static final short RespectPetMessageEvent = 2516; // PRODUCTION-201610052203-260805057
    public static final short GenerateSecretKeyMessageEvent = 239; // PRODUCTION-201610052203-260805057
    public static final short GetModeratorTicketChatlogsMessageEvent = 942; // PRODUCTION-201610052203-260805057
    public static final short GetAchievementsMessageEvent = 710; // PRODUCTION-201610052203-260805057
    public static final short SaveWiredTriggerConfigMessageEvent = 208; // PRODUCTION-201610052203-260805057
    public static final short AcceptGroupMembershipMessageEvent = 3655; // PRODUCTION-201610052203-260805057
    public static final short GetGroupFurniSettingsMessageEvent = 571; // PRODUCTION-201610052203-260805057
    public static final short TakeAdminRightsMessageEvent = 3765; // PRODUCTION-201610052203-260805057
    public static final short RemoveAllRightsMessageEvent = 2413; // PRODUCTION-201610052203-260805057
    public static final short UpdateThreadMessageEvent = 2158; // PRODUCTION-201610052203-260805057
    public static final short ManageGroupMessageEvent = 91; // PRODUCTION-201610052203-260805057
    public static final short ModifyRoomFilterListMessageEvent = 2130; // PRODUCTION-201610052203-260805057
    public static final short SSOTicketMessageEvent = 2176; // PRODUCTION-201610052203-260805057
    public static final short JoinGroupMessageEvent = 1357; // PRODUCTION-201610052203-260805057
    public static final short DeclineGroupMembershipMessageEvent = 2742; // PRODUCTION-201610052203-260805057
    public static final short UniqueIDMessageEvent = 1666; // PRODUCTION-201610052203-260805057
    public static final short RemoveMyRightsMessageEvent = 1000; // PRODUCTION-201610052203-260805057
    public static final short ApplyHorseEffectMessageEvent = 2322; // PRODUCTION-201610052203-260805057
    public static final short GetPetInformationMessageEvent = 1986; // PRODUCTION-201610052203-260805057
    public static final short GiveHandItemMessageEvent = 3261; // PRODUCTION-201610052203-260805057
    public static final short UpdateFigureDataMessageEvent = 1475; // PRODUCTION-201610052203-260805057
    public static final short RemoveGroupMemberMessageEvent = 2861; // PRODUCTION-201610052203-260805057
    public static final short EventLogMessageEvent = 1751; // PRODUCTION-201610052203-260805057
    public static final short RefreshCampaignMessageEvent = 244; // PRODUCTION-201610052203-260805057
    public static final short GetPromotableRoomsMessageEvent = 624; // PRODUCTION-201610052203-260805057
    public static final short UseOneWayGateMessageEvent = 1749; // PRODUCTION-201610052203-260805057
    public static final short AddStickyNoteMessageEvent = 2851; // PRODUCTION-201610052203-260805057
    public static final short GetSelectedBadgesMessageEvent = 2981; // PRODUCTION-201610052203-260805057
    public static final short UpdateStickyNoteMessageEvent = 500; // PRODUCTION-201610052203-260805057
    public static final short CloseTicketMesageEvent = 1692; // PRODUCTION-201610052203-260805057
    public static final short RequestBuddyMessageEvent = 498; // PRODUCTION-201610052203-260805057
    public static final short GetFurnitureAliasesMessageEvent = 3975; // PRODUCTION-201610052203-260805057
    public static final short GetRoomSettingsMessageEvent = 719; // PRODUCTION-201610052203-260805057
    public static final short RequestFurniInventoryMessageEvent = 2591; // PRODUCTION-201610052203-260805057
    public static final short ModerationKickMessageEvent = 2178; // PRODUCTION-201610052203-260805057
    public static final short OpenFlatConnectionMessageEvent = 3451; // PRODUCTION-201610052203-260805057
    public static final short DanceMessageEvent = 3421; // PRODUCTION-201610052203-260805057
    public static final short RemoveBuddyMessageEvent = 3372; // PRODUCTION-201610052203-260805057
    public static final short LatencyTestMessageEvent = 2194; // PRODUCTION-201610052203-260805057
    public static final short InfoRetrieveMessageEvent = 3980; // PRODUCTION-201610052203-260805057
    public static final short YouTubeGetNextVideo = 1912; // PRODUCTION-201610052203-260805057
    public static final short SetObjectDataMessageEvent = 3708; // PRODUCTION-201610052203-260805057
    public static final short MessengerInitMessageEvent = 1552; // PRODUCTION-201610052203-260805057
    public static final short PickUpBotMessageEvent = 3027; // PRODUCTION-201610052203-260805057
    public static final short ActionMessageEvent = 2924; // PRODUCTION-201610052203-260805057
    public static final short LookToMessageEvent = 2168; // PRODUCTION-201610052203-260805057
    public static final short ToggleMoodlightMessageEvent = 3928; // PRODUCTION-201610052203-260805057
    public static final short FollowFriendMessageEvent = 524; // PRODUCTION-201610052203-260805057
    public static final short PickUpPetMessageEvent = 3933; // PRODUCTION-201610052203-260805057
    public static final short GetSellablePetBreedsMessageEvent = 1060; // PRODUCTION-201610052203-260805057
    public static final short GetForumUserProfileMessageEvent = 2201; // PRODUCTION-201610052203-260805057
    public static final short GetForumsListDataMessageEvent = 2671; // PRODUCTION-201610052203-260805057
    public static final short IgnoreUserMessageEvent = 2926; // PRODUCTION-201610052203-260805057
    public static final short DeleteRoomMessageEvent = 1242; // PRODUCTION-201610052203-260805057
    public static final short StartQuestMessageEvent = 56; // PRODUCTION-201610052203-260805057
    public static final short GetGiftWrappingConfigurationMessageEvent = 661; // PRODUCTION-201610052203-260805057
    public static final short UpdateGroupIdentityMessageEvent = 1041; // PRODUCTION-201610052203-260805057
    public static final short RideHorseMessageEvent = 1410; // PRODUCTION-201610052203-260805057
    public static final short ApplySignMessageEvent = 3646; // PRODUCTION-201610052203-260805057
    public static final short FindRandomFriendingRoomMessageEvent = 3610; // PRODUCTION-201610052203-260805057
    public static final short GetModeratorUserChatlogMessageEvent = 217; // PRODUCTION-201610052203-260805057
    public static final short GetWardrobeMessageEvent = 2870; // PRODUCTION-201610052203-260805057
    public static final short MuteUserMessageEvent = 1565; // PRODUCTION-201610052203-260805057
    public static final short UpdateForumSettingsMessageEvent = 2260; // PRODUCTION-201610052203-260805057
    public static final short ApplyDecorationMessageEvent = 2298; // PRODUCTION-201610052203-260805057
    public static final short GetBotInventoryMessageEvent = 3222; // PRODUCTION-201610052203-260805057
    public static final short UseHabboWheelMessageEvent = 2971; // PRODUCTION-201610052203-260805057
    public static final short EditRoomPromotionMessageEvent = 63; // PRODUCTION-201610052203-260805057
    public static final short GetModeratorUserInfoMessageEvent = 2828; // PRODUCTION-201610052203-260805057
    public static final short PlaceBotMessageEvent = 1296; // PRODUCTION-201610052203-260805057
    public static final short GetCatalogPageMessageEvent = 2065; // PRODUCTION-201610052203-260805057
    public static final short GetThreadsListDataMessageEvent = 3240; // PRODUCTION-201610052203-260805057
    public static final short ShoutMessageEvent = 2236; // PRODUCTION-201610052203-260805057
    public static final short DiceOffMessageEvent = 2854; // PRODUCTION-201610052203-260805057
    public static final short LetUserInMessageEvent = 3278; // PRODUCTION-201610052203-260805057
    public static final short SetActivatedBadgesMessageEvent = 3345; // PRODUCTION-201610052203-260805057
    public static final short UpdateGroupSettingsMessageEvent = 1806; // PRODUCTION-201610052203-260805057
    public static final short ApproveNameMessageEvent = 2098; // PRODUCTION-201610052203-260805057
    public static final short SubmitNewTicketMessageEvent = 2207; // PRODUCTION-201610052203-260805057
    public static final short DeleteGroupMessageEvent = 2045; // PRODUCTION-201610052203-260805057
    public static final short DeleteStickyNoteMessageEvent = 1202; // PRODUCTION-201610052203-260805057
    public static final short GetGroupInfoMessageEvent = 2748; // PRODUCTION-201610052203-260805057
    public static final short GetStickyNoteMessageEvent = 2921; // PRODUCTION-201610052203-260805057
    public static final short DeclineBuddyMessageEvent = 2285; // PRODUCTION-201610052203-260805057
    public static final short OpenGiftMessageEvent = 1771; // PRODUCTION-201610052203-260805057
    public static final short GiveRoomScoreMessageEvent = 812; // PRODUCTION-201610052203-260805057
    public static final short SetGroupFavouriteMessageEvent = 3081; // PRODUCTION-201610052203-260805057
    public static final short SetMannequinNameMessageEvent = 1755; // PRODUCTION-201610052203-260805057
    public static final short CallForHelpMessageEvent = 2207; // PRODUCTION-201610052203-260805057
    public static final short RoomDimmerSavePresetMessageEvent = 3143; // PRODUCTION-201610052203-260805057
    public static final short UpdateGroupBadgeMessageEvent = 53; // PRODUCTION-201610052203-260805057
    public static final short PickTicketMessageEvent = 2788; // PRODUCTION-201610052203-260805057
    public static final short SetTonerMessageEvent = 2452; // PRODUCTION-201610052203-260805057
    public static final short RespectUserMessageEvent = 3840; // PRODUCTION-201610052203-260805057
    public static final short DeleteGroupThreadMessageEvent = 3450; // PRODUCTION-201610052203-260805057
    public static final short DeleteGroupReplyMessageEvent = 1419; // PRODUCTION-201610052203-260805057

    public static final short CreditFurniRedeemMessageEvent = 1667; // PRODUCTION-201610052203-260805057
    public static final short ModerationMsgMessageEvent = 3683; // PRODUCTION-201610052203-260805057
    public static final short ToggleYouTubeVideoMessageEvent = 3139; // PRODUCTION-201610052203-260805057
    public static final short UpdateNavigatorSettingsMessageEvent = 3307; // PRODUCTION-201610052203-260805057
    public static final short ToggleMuteToolMessageEvent = 862; // PRODUCTION-201610052203-260805057
    public static final short ChatMessageEvent = 2141; // PRODUCTION-201610052203-260805057
    public static final short SaveRoomSettingsMessageEvent = 3522; // PRODUCTION-201610052203-260805057
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 298; // PRODUCTION-201610052203-260805057
    public static final short GetGroupCreationWindowMessageEvent = 2052; // PRODUCTION-201610052203-260805057
    public static final short GiveAdminRightsMessageEvent = 2636; // PRODUCTION-201610052203-260805057
    public static final short GetGroupMembersMessageEvent = 439; // PRODUCTION-201610052203-260805057
    public static final short ModerateRoomMessageEvent = 2372; // PRODUCTION-201610052203-260805057
    public static final short GetForumStatsMessageEvent = 3200; // PRODUCTION-201610052203-260805057
    public static final short GetPromoArticlesMessageEvent = 1392; // PRODUCTION-201610052203-260805057
    public static final short SitMessageEvent = 344; // PRODUCTION-201610052203-260805057
    public static final short SetSoundSettingsMessageEvent = 420; // PRODUCTION-201610052203-260805057
    public static final short ModerationCautionMessageEvent = 612; // PRODUCTION-201610052203-260805057
    public static final short InitializeFloorPlanSessionMessageEvent = 332; // PRODUCTION-201610052203-260805057
    public static final short ModeratorActionMessageEvent = 3487; // PRODUCTION-201610052203-260805057
    public static final short PostGroupContentMessageEvent = 3907; // PRODUCTION-201610052203-260805057
    public static final short GetModeratorRoomChatlogMessageEvent = 992; // PRODUCTION-201610052203-260805057
    public static final short GetUserFlatCatsMessageEvent = 2612; // PRODUCTION-201610052203-260805057
    public static final short RemoveRightsMessageEvent = 2190; // PRODUCTION-201610052203-260805057
    public static final short ModerationBanMessageEvent = 1324; // PRODUCTION-201610052203-260805057
    public static final short CanCreateRoomMessageEvent = 875; // PRODUCTION-201610052203-260805057
    public static final short UseWallItemMessageEvent = 2480; // PRODUCTION-201610052203-260805057
    public static final short PlaceObjectMessageEvent = 1852; // PRODUCTION-201610052203-260805057
    public static final short OpenBotActionMessageEvent = 3205; // PRODUCTION-201610052203-260805057
    public static final short GetEventCategoriesMessageEvent = 671; // PRODUCTION-201610052203-260805057
    public static final short GetRoomEntryDataMessageEvent = 3994; // PRODUCTION-201610052203-260805057
    public static final short MoveWallItemMessageEvent = 1293; // PRODUCTION-201610052203-260805057
    public static final short UpdateGroupColoursMessageEvent = 2522; // PRODUCTION-201610052203-260805057
    public static final short HabboSearchMessageEvent = 609; // PRODUCTION-201610052203-260805057
    public static final short CommandBotMessageEvent = 3092; // PRODUCTION-201610052203-260805057
    public static final short SetCustomStackingHeightMessageEvent = 2892; // PRODUCTION-201610052203-260805057
    public static final short UnIgnoreUserMessageEvent = 1683; // PRODUCTION-201610052203-260805057
    public static final short GetGuestRoomMessageEvent = 1327; // PRODUCTION-201610052203-260805057
    public static final short SetMannequinFigureMessageEvent = 3241; // PRODUCTION-201610052203-260805057
    public static final short AssignRightsMessageEvent = 335; // PRODUCTION-201610052203-260805057
    public static final short GetYouTubeTelevisionMessageEvent = 869; // PRODUCTION-201610052203-260805057
    public static final short SetMessengerInviteStatusMessageEvent = 1930; // PRODUCTION-201610052203-260805057
    public static final short UpdateFloorPropertiesMessageEvent = 3121; // PRODUCTION-201610052203-260805057
    public static final short GetMoodlightConfigMessageEvent = 368; // PRODUCTION-201610052203-260805057
    public static final short PurchaseRoomPromotionMessageEvent = 2205; // PRODUCTION-201610052203-260805057
    public static final short SendRoomInviteMessageEvent = 1446; // PRODUCTION-201610052203-260805057
    public static final short ModerationMuteMessageEvent = 1273; // PRODUCTION-201610052203-260805057
    public static final short SetRelationshipMessageEvent = 882; // PRODUCTION-201610052203-260805057
    public static final short ChangeMottoMessageEvent = 3273; // PRODUCTION-201610052203-260805057
    public static final short UnbanUserFromRoomMessageEvent = 3538; // PRODUCTION-201610052203-260805057
    public static final short GetRoomRightsMessageEvent = 59; // PRODUCTION-201610052203-260805057
    public static final short PurchaseGroupMessageEvent = 1822; // PRODUCTION-201610052203-260805057
    public static final short CreateFlatMessageEvent = 3560; // PRODUCTION-201610052203-260805057
    public static final short OpenHelpToolMessageEvent = 2826; // PRODUCTION-201610052203-260805057
    public static final short ThrowDiceMessageEvent = 2023; // PRODUCTION-201610052203-260805057
    public static final short SaveWiredConditionConfigMessageEvent = 85; // PRODUCTION-201610052203-260805057
    public static final short GetCatalogOfferMessageEvent = 422; // PRODUCTION-201610052203-260805057
    public static final short PurchaseFromCatalogMessageEvent = 1943; // PRODUCTION-201610052203-260805057
    public static final short PickupObjectMessageEvent = 2593; // PRODUCTION-201610052203-260805057
    public static final short CancelQuestMessageEvent = 1996; // PRODUCTION-201610052203-260805057
    public static final short NavigatorSearchMessageEvent = 462; // PRODUCTION-201610052203-260805057
    public static final short MoveAvatarMessageEvent = 1660; // PRODUCTION-201610052203-260805057
    public static final short GetClientVersionMessageEvent = 4000; // PRODUCTION-201610052203-260805057
    public static final short InitializeNavigatorMessageEvent = 3956; // PRODUCTION-201610052203-260805057
    public static final short GetRoomFilterListMessageEvent = 2751; // PRODUCTION-201610052203-260805057
    public static final short WhisperMessageEvent = 2677; // PRODUCTION-201610052203-260805057
    public static final short InitCryptoMessageEvent = 3091; // PRODUCTION-201610052203-260805057
    public static final short GetPetTrainingPanelMessageEvent = 1819; // PRODUCTION-201610052203-260805057
    public static final short MoveObjectMessageEvent = 1322; // PRODUCTION-201610052203-260805057
    public static final short StartTypingMessageEvent = 2735; // PRODUCTION-201610052203-260805057
    public static final short GoToHotelViewMessageEvent = 2988; // PRODUCTION-201610052203-260805057
    public static final short GetExtendedProfileMessageEvent = 1289; // PRODUCTION-201610052203-260805057
    public static final short SendMsgMessageEvent = 2893; // PRODUCTION-201610052203-260805057
    public static final short CancelTypingMessageEvent = 2778; // PRODUCTION-201610052203-260805057
    public static final short GetGroupFurniConfigMessageEvent = 2634; // PRODUCTION-201610052203-260805057


    public static final short RemoveGroupFavouriteMessageEvent = 337; // PRODUCTION-201610052203-260805057
    public static final short PlacePetMessageEvent = 698; // PRODUCTION-201610052203-260805057
    public static final short ModifyWhoCanRideHorseMessageEvent = 3319; // PRODUCTION-201610052203-260805057
    public static final short GetRelationshipsMessageEvent = 1228; // PRODUCTION-201610052203-260805057
    public static final short GetCatalogIndexMessageEvent = 3058; // PRODUCTION-201610052203-260805057
    public static final short ScrGetUserInfoMessageEvent = 444; // PRODUCTION-201610052203-260805057
    public static final short ConfirmLoveLockMessageEvent = 3813; // PRODUCTION-201610052203-260805057
    public static final short RemoveSaddleFromHorseMessageEvent = 1999; // PRODUCTION-201610052203-260805057
    public static final short AcceptBuddyMessageEvent = 3349; // PRODUCTION-201610052203-260805057
    public static final short GetQuestListMessageEvent = 3370; // PRODUCTION-201610052203-260805057
    public static final short SaveWardrobeOutfitMessageEvent = 3605; // PRODUCTION-201610052203-260805057
    public static final short BanUserMessageEvent = 1057; // PRODUCTION-201610052203-260805057
    public static final short GetThreadDataMessageEvent = 911; // PRODUCTION-201610052203-260805057
    public static final short GetBadgesMessageEvent = 1214; // PRODUCTION-201610052203-260805057
    public static final short UseFurnitureMessageEvent = 2984; // PRODUCTION-201610052203-260805057
    public static final short GoToFlatMessageEvent = 3938; // PRODUCTION-201610052203-260805057
    public static final short GetModeratorUserRoomVisitsMessageEvent = 483; // PRODUCTION-201610052203-260805057
    public static final short GetSanctionStatusMessageEvent = 1804; // PRODUCTION-201610052203-260805057
    public static final short SetChatPreferenceMessageEvent = 2805; // PRODUCTION-201610052203-260805057
    public static final short ResizeNavigatorMessageEvent = 2806; // PRODUCTION-201610052203-260805057
    public static final short CameraDataMessageEvent = 2604; // PRODUCTION-201610052203-260805057
    public static final short RenderRoomMessageEvent = 1245; // PRODUCTION-201610052203-260805057
    public static final short BuyPhotoMessageEvent = 2156; // PRODUCTION-201610052203-260805057
    public static final short SongInventoryMessageEvent = 1200; // PRODUCTION-201610052203-260805057
    public static final short SongIdMessageEvent = 536; // PRODUCTION-201610052203-260805057
    public static final short SongDataMessageEvent = 3206; // PRODUCTION-201610052203-260805057
    public static final short PlaylistMessageEvent = 2939; // PRODUCTION-201610052203-260805057
    public static final short PlaylistAddMessageEvent = 929; // PRODUCTION-201610052203-260805057
    public static final short PlaylistRemoveMessageEvent = 2109; // PRODUCTION-201610052203-260805057
    public static final short StaffPickRoomMessageEvent = 2512; // PRODUCTION-201610052203-260805057
    public static final short SubmitPollAnswerMessageEvent = 442; // PRODUCTION-201610052203-260805057
    public static final short GetPollMessageEvent = 237; // PRODUCTION-201610052203-260805057
    public static final short UpdateSnapshotsMessageEvent = 1773; // PRODUCTION-201610052203-260805057
    public static final short MarkAsReadMessageEvent = 387; // PRODUCTION-201610052203-260805057
    public static final short InitTradeMessageEvent = 2376; // PRODUCTION-201610052203-260805057
    public static final short TradingOfferItemMessageEvent = 970; // PRODUCTION-201610052203-260805057
    public static final short TradingOfferItemsMessageEvent = 781; // PRODUCTION-201610052203-260805057
    public static final short TradingRemoveItemMessageEvent = 226; // PRODUCTION-201610052203-260805057
    public static final short TradingAcceptMessageEvent = 2526; // PRODUCTION-201610052203-260805057
    public static final short TradingCancelMessageEvent = 1208; // PRODUCTION-201610052203-260805057
    public static final short TradingModifyMessageEvent = 555; // PRODUCTION-201610052203-260805057
    public static final short TradingConfirmMessageEvent = 1987; // PRODUCTION-201610052203-260805057
    public static final short TradingCancelConfirmMessageEvent = 1054; // PRODUCTION-201610052203-260805057
    public static final short RedeemVoucherMessageEvent = 2664; // PRODUCTION-201610052203-260805057
    public static final short Hide= 2664; // PRODUCTION-201610052203-260805057
    public static final short ChangeNameMessageEvent = 412; // PRODUCTION-201610052203-260805057
    public static final short CheckValidNameMessageEvent = 2902; // PRODUCTION-201610052203-260805057


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
