package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Events {
    public static final short GetRoomBannedUsersMessageEvent = 3251; // PRODUCTION-201608171204-891546786
    public static final short GetPetInventoryMessageEvent = 765; // PRODUCTION-201608171204-891546786
    public static final short DropHandItemMessageEvent = 469; // PRODUCTION-201608171204-891546786
    public static final short ReleaseTicketMessageEvent = 175; // PRODUCTION-201608171204-891546786
    public static final short GetModeratorRoomInfoMessageEvent = 1695; // PRODUCTION-201608171204-891546786
    public static final short KickUserMessageEvent = 1240; // PRODUCTION-201608171204-891546786
    public static final short SaveWiredEffectConfigMessageEvent = 1394; // PRODUCTION-201608171204-891546786
    public static final short RespectPetMessageEvent = 608; // PRODUCTION-201608171204-891546786
    public static final short GenerateSecretKeyMessageEvent = 682; // PRODUCTION-201608171204-891546786
    public static final short GetModeratorTicketChatlogsMessageEvent = 2490; // PRODUCTION-201608171204-891546786
    public static final short GetAchievementsMessageEvent = 2093; // PRODUCTION-201608171204-891546786
    public static final short SaveWiredTriggerConfigMessageEvent = 3964; // PRODUCTION-201608171204-891546786
    public static final short AcceptGroupMembershipMessageEvent = 3659; // PRODUCTION-201608171204-891546786
    public static final short GetGroupFurniSettingsMessageEvent = 1519; // PRODUCTION-201608171204-891546786
    public static final short TakeAdminRightsMessageEvent = 1892; // PRODUCTION-201608171204-891546786
    public static final short RemoveAllRightsMessageEvent = 1593; // PRODUCTION-201608171204-891546786
    public static final short UpdateThreadMessageEvent = 988; // PRODUCTION-201608171204-891546786
    public static final short ManageGroupMessageEvent = 432; // PRODUCTION-201608171204-891546786
    public static final short ModifyRoomFilterListMessageEvent = 1164; // PRODUCTION-201608171204-891546786
    public static final short SSOTicketMessageEvent = 435; // PRODUCTION-201608171204-891546786
    public static final short JoinGroupMessageEvent = 143; // PRODUCTION-201608171204-891546786
    public static final short DeclineGroupMembershipMessageEvent = 718; // PRODUCTION-201608171204-891546786
    public static final short UniqueIDMessageEvent = 1936; // PRODUCTION-201608171204-891546786
    public static final short RemoveMyRightsMessageEvent = 704; // PRODUCTION-201608171204-891546786
    public static final short ApplyHorseEffectMessageEvent = 626; // PRODUCTION-201608171204-891546786
    public static final short GetPetInformationMessageEvent = 3171; // PRODUCTION-201608171204-891546786
    public static final short GiveHandItemMessageEvent = 150; // PRODUCTION-201608171204-891546786
    public static final short UpdateFigureDataMessageEvent = 3919; // PRODUCTION-201608171204-891546786
    public static final short RemoveGroupMemberMessageEvent = 1922; // PRODUCTION-201608171204-891546786
    public static final short EventLogMessageEvent = 1480; // PRODUCTION-201608171204-891546786
    public static final short RefreshCampaignMessageEvent = 1731; // PRODUCTION-201608171204-891546786
    public static final short GetPromotableRoomsMessageEvent = 965; // PRODUCTION-201608171204-891546786
    public static final short UseOneWayGateMessageEvent = 3560; // PRODUCTION-201608171204-891546786
    public static final short AddStickyNoteMessageEvent = 294; // PRODUCTION-201608171204-891546786
    public static final short GetSelectedBadgesMessageEvent = 3087; // PRODUCTION-201608171204-891546786
    public static final short UpdateStickyNoteMessageEvent = 3076; // PRODUCTION-201608171204-891546786
    public static final short CloseTicketMesageEvent = 1284; // PRODUCTION-201608171204-891546786
    public static final short RequestBuddyMessageEvent = 2640; // PRODUCTION-201608171204-891546786
    public static final short GetFurnitureAliasesMessageEvent = 2326; // PRODUCTION-201608171204-891546786
    public static final short GetRoomSettingsMessageEvent = 1378; // PRODUCTION-201608171204-891546786
    public static final short RequestFurniInventoryMessageEvent = 3379; // PRODUCTION-201608171204-891546786
    public static final short ModerationKickMessageEvent = 2607; // PRODUCTION-201608171204-891546786
    public static final short OpenFlatConnectionMessageEvent = 3326; // PRODUCTION-201608171204-891546786
    public static final short DanceMessageEvent = 3139; // PRODUCTION-201608171204-891546786
    public static final short RemoveBuddyMessageEvent = 283; // PRODUCTION-201608171204-891546786
    public static final short LatencyTestMessageEvent = 2274; // PRODUCTION-201608171204-891546786
    public static final short InfoRetrieveMessageEvent = 295; // PRODUCTION-201608171204-891546786
    public static final short YouTubeGetNextVideo = 2110; // PRODUCTION-201608171204-891546786
    public static final short SetObjectDataMessageEvent = 1333; // PRODUCTION-201608171204-891546786
    public static final short MessengerInitMessageEvent = 1873; // PRODUCTION-201608171204-891546786
    public static final short PickUpBotMessageEvent = 680; // PRODUCTION-201608171204-891546786
    public static final short ActionMessageEvent = 3227; // PRODUCTION-201608171204-891546786
    public static final short LookToMessageEvent = 3930; // PRODUCTION-201608171204-891546786
    public static final short ToggleMoodlightMessageEvent = 3200; // PRODUCTION-201608171204-891546786
    public static final short FollowFriendMessageEvent = 2025; // PRODUCTION-201608171204-891546786
    public static final short PickUpPetMessageEvent = 714; // PRODUCTION-201608171204-891546786
    public static final short GetSellablePetBreedsMessageEvent = 1602; // PRODUCTION-201608171204-891546786
    public static final short GetForumUserProfileMessageEvent = 1794; // PRODUCTION-201608171204-891546786
    public static final short GetForumsListDataMessageEvent = 1035; // PRODUCTION-201608171204-891546786
    public static final short IgnoreUserMessageEvent = 521; // PRODUCTION-201608171204-891546786
    public static final short DeleteRoomMessageEvent = 1328; // PRODUCTION-201608171204-891546786
    public static final short StartQuestMessageEvent = 2931; // PRODUCTION-201608171204-891546786
    public static final short GetGiftWrappingConfigurationMessageEvent = 3283; // PRODUCTION-201608171204-891546786
    public static final short TradingAcceptMessageEvent = 2320; // PRODUCTION-201608171204-891546786
    public static final short UpdateGroupIdentityMessageEvent = 1407; // PRODUCTION-201608171204-891546786
    public static final short RideHorseMessageEvent = 2408; // PRODUCTION-201608171204-891546786
    public static final short ApplySignMessageEvent = 3935; // PRODUCTION-201608171204-891546786
    public static final short FindRandomFriendingRoomMessageEvent = 2534; // PRODUCTION-201608171204-891546786
    public static final short GetModeratorUserChatlogMessageEvent = 2637; // PRODUCTION-201608171204-891546786
    public static final short TradingOfferItemMessageEvent = 1293; // PRODUCTION-201608171204-891546786
    public static final short GetWardrobeMessageEvent = 3102; // PRODUCTION-201608171204-891546786
    public static final short MuteUserMessageEvent = 1841; // PRODUCTION-201608171204-891546786
    public static final short UpdateForumSettingsMessageEvent = 1493; // PRODUCTION-201608171204-891546786
    public static final short ApplyDecorationMessageEvent = 1406; // PRODUCTION-201608171204-891546786
    public static final short GetBotInventoryMessageEvent = 1348; // PRODUCTION-201608171204-891546786
    public static final short UseHabboWheelMessageEvent = 3931; // PRODUCTION-201608171204-891546786
    public static final short EditRoomPromotionMessageEvent = 2809; // PRODUCTION-201608171204-891546786
    public static final short GetModeratorUserInfoMessageEvent = 200; // PRODUCTION-201608171204-891546786
    public static final short PlaceBotMessageEvent = 2036; // PRODUCTION-201608171204-891546786
    public static final short GetCatalogPageMessageEvent = 1560; // PRODUCTION-201608171204-891546786
    public static final short GetThreadsListDataMessageEvent = 2709; // PRODUCTION-201608171204-891546786
    public static final short ShoutMessageEvent = 529; // PRODUCTION-201608171204-891546786
    public static final short DiceOffMessageEvent = 2933; // PRODUCTION-201608171204-891546786
    public static final short LetUserInMessageEvent = 479; // PRODUCTION-201608171204-891546786
    public static final short SetActivatedBadgesMessageEvent = 2859; // PRODUCTION-201608171204-891546786
    public static final short UpdateGroupSettingsMessageEvent = 2459; // PRODUCTION-201608171204-891546786
    public static final short ApproveNameMessageEvent = 1060; // PRODUCTION-201608171204-891546786
    public static final short CancelOfferMessageEvent = 3832; // PRODUCTION-201608171204-891546786
    public static final short TradingCancelMessageEvent = 1942; // PRODUCTION-201608171204-891546786
    public static final short DeleteGroupMessageEvent = 2123; // PRODUCTION-201608171204-891546786
    public static final short DeleteStickyNoteMessageEvent = 1104; // PRODUCTION-201608171204-891546786
    public static final short GetGroupInfoMessageEvent = 2443; // PRODUCTION-201608171204-891546786
    public static final short GetStickyNoteMessageEvent = 3586; // PRODUCTION-201608171204-891546786
    public static final short DeclineBuddyMessageEvent = 1102; // PRODUCTION-201608171204-891546786
    public static final short OpenGiftMessageEvent = 1138; // PRODUCTION-201608171204-891546786
    public static final short GiveRoomScoreMessageEvent = 2254; // PRODUCTION-201608171204-891546786
    public static final short SetGroupFavouriteMessageEvent = 2377; // PRODUCTION-201608171204-891546786
    public static final short SetMannequinNameMessageEvent = 2247; // PRODUCTION-201608171204-891546786
    public static final short CallForHelpMessageEvent = 2642; // PRODUCTION-201608171204-891546786
    public static final short RoomDimmerSavePresetMessageEvent = 2798; // PRODUCTION-201608171204-891546786
    public static final short UpdateGroupBadgeMessageEvent = 3270; // PRODUCTION-201608171204-891546786
    public static final short PickTicketMessageEvent = 3105; // PRODUCTION-201608171204-891546786
    public static final short SetTonerMessageEvent = 1655; // PRODUCTION-201608171204-891546786
    public static final short RespectUserMessageEvent = 3913; // PRODUCTION-201608171204-891546786
    public static final short DeleteGroupThreadMessageEvent = 2877; // PRODUCTION-201608171204-891546786
    public static final short CreditFurniRedeemMessageEvent = 171; // PRODUCTION-201608171204-891546786
    public static final short ModerationMsgMessageEvent = 2435; // PRODUCTION-201608171204-891546786
    public static final short ToggleYouTubeVideoMessageEvent = 3330; // PRODUCTION-201608171204-891546786
    public static final short UpdateNavigatorSettingsMessageEvent = 2597; // PRODUCTION-201608171204-891546786
    public static final short ToggleMuteToolMessageEvent = 3112; // PRODUCTION-201608171204-891546786
    public static final short InitTradeMessageEvent = 1235; // PRODUCTION-201608171204-891546786
    public static final short ChatMessageEvent = 2677; // PRODUCTION-201608171204-891546786
    public static final short SaveRoomSettingsMessageEvent = 2244; // PRODUCTION-201608171204-891546786
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 2479; // PRODUCTION-201608171204-891546786
    public static final short GetGroupCreationWindowMessageEvent = 3111; // PRODUCTION-201608171204-891546786
    public static final short GiveAdminRightsMessageEvent = 2904; // PRODUCTION-201608171204-891546786
    public static final short GetGroupMembersMessageEvent = 247; // PRODUCTION-201608171204-891546786
    public static final short ModerateRoomMessageEvent = 3312; // PRODUCTION-201608171204-891546786
    public static final short GetForumStatsMessageEvent = 959; // PRODUCTION-201608171204-891546786
    public static final short GetPromoArticlesMessageEvent = 800; // PRODUCTION-201608171204-891546786
    public static final short SitMessageEvent = 1905; // PRODUCTION-201608171204-891546786
    public static final short SetSoundSettingsMessageEvent = 908; // PRODUCTION-201608171204-891546786
    public static final short ModerationCautionMessageEvent = 3434; // PRODUCTION-201608171204-891546786
    public static final short InitializeFloorPlanSessionMessageEvent = 1556; // PRODUCTION-201608171204-891546786
    public static final short ModeratorActionMessageEvent = 3810; // PRODUCTION-201608171204-891546786
    public static final short PostGroupContentMessageEvent = 2237; // PRODUCTION-201608171204-891546786
    public static final short GetModeratorRoomChatlogMessageEvent = 2493; // PRODUCTION-201608171204-891546786
    public static final short GetUserFlatCatsMessageEvent = 3673; // PRODUCTION-201608171204-891546786
    public static final short RemoveRightsMessageEvent = 2559; // PRODUCTION-201608171204-891546786
    public static final short ModerationBanMessageEvent = 3564; // PRODUCTION-201608171204-891546786
    public static final short CanCreateRoomMessageEvent = 999; // PRODUCTION-201608171204-891546786
    public static final short UseWallItemMessageEvent = 2773; // PRODUCTION-201608171204-891546786
    public static final short PlaceObjectMessageEvent = 3914; // PRODUCTION-201608171204-891546786
    public static final short OpenBotActionMessageEvent = 3565; // PRODUCTION-201608171204-891546786
    public static final short GetEventCategoriesMessageEvent = 1919; // PRODUCTION-201608171204-891546786
    public static final short GetRoomEntryDataMessageEvent = 2518; // PRODUCTION-201608171204-891546786
    public static final short MoveWallItemMessageEvent = 3865; // PRODUCTION-201608171204-891546786
    public static final short UpdateGroupColoursMessageEvent = 1513; // PRODUCTION-201608171204-891546786
    public static final short HabboSearchMessageEvent = 2307; // PRODUCTION-201608171204-891546786
    public static final short CommandBotMessageEvent = 1613; // PRODUCTION-201608171204-891546786
    public static final short SetCustomStackingHeightMessageEvent = 2912; // PRODUCTION-201608171204-891546786
    public static final short UnIgnoreUserMessageEvent = 1881; // PRODUCTION-201608171204-891546786
    public static final short GetGuestRoomMessageEvent = 127; // PRODUCTION-201608171204-891546786
    public static final short SetMannequinFigureMessageEvent = 1351; // PRODUCTION-201608171204-891546786
    public static final short AssignRightsMessageEvent = 351; // PRODUCTION-201608171204-891546786
    public static final short GetYouTubeTelevisionMessageEvent = 1044; // PRODUCTION-201608171204-891546786
    public static final short SetMessengerInviteStatusMessageEvent = 3148; // PRODUCTION-201608171204-891546786
    public static final short UpdateFloorPropertiesMessageEvent = 2115; // PRODUCTION-201608171204-891546786
    public static final short GetMoodlightConfigMessageEvent = 380; // PRODUCTION-201608171204-891546786
    public static final short PurchaseRoomPromotionMessageEvent = 562; // PRODUCTION-201608171204-891546786
    public static final short SendRoomInviteMessageEvent = 3737; // PRODUCTION-201608171204-891546786
    public static final short ModerationMuteMessageEvent = 1840; // PRODUCTION-201608171204-891546786
    public static final short SetRelationshipMessageEvent = 605; // PRODUCTION-201608171204-891546786
    public static final short ChangeMottoMessageEvent = 321; // PRODUCTION-201608171204-891546786
    public static final short UnbanUserFromRoomMessageEvent = 2245; // PRODUCTION-201608171204-891546786
    public static final short GetRoomRightsMessageEvent = 645; // PRODUCTION-201608171204-891546786
    public static final short PurchaseGroupMessageEvent = 2249; // PRODUCTION-201608171204-891546786
    public static final short CreateFlatMessageEvent = 1802; // PRODUCTION-201608171204-891546786
    public static final short OpenHelpToolMessageEvent = 1586; // PRODUCTION-201608171204-891546786
    public static final short ThrowDiceMessageEvent = 1636; // PRODUCTION-201608171204-891546786
    public static final short SaveWiredConditionConfigMessageEvent = 2776; // PRODUCTION-201608171204-891546786
    public static final short GetCatalogOfferMessageEvent = 1114; // PRODUCTION-201608171204-891546786
    public static final short PurchaseFromCatalogMessageEvent = 616; // PRODUCTION-201608171204-891546786
    public static final short PickupObjectMessageEvent = 3353; // PRODUCTION-201608171204-891546786
    public static final short CancelQuestMessageEvent = 3406; // PRODUCTION-201608171204-891546786
    public static final short NavigatorSearchMessageEvent = 3708; // PRODUCTION-201608171204-891546786
    public static final short MoveAvatarMessageEvent = 1887; // PRODUCTION-201608171204-891546786
    public static final short GetClientVersionMessageEvent = 4000; // PRODUCTION-201608171204-891546786
    public static final short InitializeNavigatorMessageEvent = 410; // PRODUCTION-201608171204-891546786
    public static final short TradingOfferItemsMessageEvent = 3623; // PRODUCTION-201608171204-891546786
    public static final short GetRoomFilterListMessageEvent = 2291; // PRODUCTION-201608171204-891546786
    public static final short WhisperMessageEvent = 2617; // PRODUCTION-201608171204-891546786
    public static final short InitCryptoMessageEvent = 260; // PRODUCTION-201608171204-891546786
    public static final short GetPetTrainingPanelMessageEvent = 3474; // PRODUCTION-201608171204-891546786
    public static final short MoveObjectMessageEvent = 2034; // PRODUCTION-201608171204-891546786
    public static final short StartTypingMessageEvent = 567; // PRODUCTION-201608171204-891546786
    public static final short GoToHotelViewMessageEvent = 3183; // PRODUCTION-201608171204-891546786
    public static final short GetExtendedProfileMessageEvent = 1456; // PRODUCTION-201608171204-891546786
    public static final short SendMsgMessageEvent = 1224; // PRODUCTION-201608171204-891546786
    public static final short CancelTypingMessageEvent = 3678; // PRODUCTION-201608171204-891546786
    public static final short GetGroupFurniConfigMessageEvent = 3513; // PRODUCTION-201608171204-891546786
    public static final short TradingConfirmMessageEvent = 3308; // PRODUCTION-201608171204-891546786
    public static final short RemoveGroupFavouriteMessageEvent = 3108; // PRODUCTION-201608171204-891546786
    public static final short PlacePetMessageEvent = 1740; // PRODUCTION-201608171204-891546786
    public static final short ModifyWhoCanRideHorseMessageEvent = 1917; // PRODUCTION-201608171204-891546786
    public static final short GetRelationshipsMessageEvent = 744; // PRODUCTION-201608171204-891546786
    public static final short GetCatalogIndexMessageEvent = 1778; // PRODUCTION-201608171204-891546786
    public static final short ScrGetUserInfoMessageEvent = 983; // PRODUCTION-201608171204-891546786
    public static final short ConfirmLoveLockMessageEvent = 1278; // PRODUCTION-201608171204-891546786
    public static final short RemoveSaddleFromHorseMessageEvent = 2615; // PRODUCTION-201608171204-891546786
    public static final short AcceptBuddyMessageEvent = 3254; // PRODUCTION-201608171204-891546786
    public static final short GetQuestListMessageEvent = 3137; // PRODUCTION-201608171204-891546786
    public static final short SaveWardrobeOutfitMessageEvent = 2289; // PRODUCTION-201608171204-891546786
    public static final short BanUserMessageEvent = 618; // PRODUCTION-201608171204-891546786
    public static final short GetThreadDataMessageEvent = 2855; // PRODUCTION-201608171204-891546786
    public static final short GetBadgesMessageEvent = 3265; // PRODUCTION-201608171204-891546786
    public static final short UseFurnitureMessageEvent = 3023; // PRODUCTION-201608171204-891546786
    public static final short GoToFlatMessageEvent = 2324; // PRODUCTION-201608171204-891546786
    public static final short GetModeratorUserRoomVisitsMessageEvent = 2050; // PRODUCTION-201608171204-891546786
    public static final short SetChatPreferenceMessageEvent = 1938; // PRODUCTION-201608171204-891546786
    public static final short ResizeNavigatorMessageEvent = 2744; // PRODUCTION-201608171204-891546786
    public static final short CameraDataMessageEvent = 2690; // PRODUCTION-201608171204-891546786
    public static final short RenderRoomMessageEvent = 664; // PRODUCTION-201608171204-891546786
    public static final short BuyPhotoMessageEvent = 2333; // PRODUCTION-201608171204-891546786
    public static final short SongInventoryMessageEvent = 3151; // PRODUCTION-201608171204-891546786
    public static final short SongIdMessageEvent = 3520; // PRODUCTION-201608171204-891546786
    public static final short SongDataMessageEvent = 303; // PRODUCTION-201608171204-891546786
    public static final short PlaylistMessageEvent = 530; // PRODUCTION-201608171204-891546786
    public static final short PlaylistAddMessageEvent = 2122; // PRODUCTION-201608171204-891546786
    public static final short PlaylistRemoveMessageEvent = 647; // PRODUCTION-201608171204-891546786
    public static final short StaffPickRoomMessageEvent = 1265; // PRODUCTION-201608171204-891546786
    public static final short SubmitPollAnswerMessageEvent = 783; // PRODUCTION-201608171204-891546786
    public static final short GetPollMessageEvent = 3182; // PRODUCTION-201608171204-891546786
    public static final short UpdateSnapshotsMessageEvent = 660; // PRODUCTION-201608171204-891546786
    public static final short MarkAsReadMessageEvent = 523; // PRODUCTION-201608171204-891546786

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
