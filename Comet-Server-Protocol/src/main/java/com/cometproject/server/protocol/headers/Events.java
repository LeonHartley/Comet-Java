package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Events {
    public static final short SubmitPollAnswerMessageEvent = 3757;
    public static final short GetPollMessageEvent = 2580;
    public static final short StaffPickRoomMessageEvent = 510;
    public static final short RemoveMyRightsMessageEvent = 879;
    public static final short GiveHandItemMessageEvent = 3315;//514
    public static final short InitTradeMessageEvent = 3313;//2745
    public static final short ChatMessageEvent = 670;//1109
    public static final short GetClubGiftsMessageEvent = 3302;//243
    public static final short GoToHotelViewMessageEvent = 3576;//1698
    public static final short GetCurrentQuestMessageEvent = 90;//3550
    public static final short GetRoomFilterListMessageEvent = 1348;//1246
    public static final short GetBadgeEditorPartsMessageEvent = 1670;//350
    public static final short GetForumStatsMessageEvent = 872;//3224
    public static final short GetPromoArticlesMessageEvent = 3895;//265
    public static final short GetCatalogPageMessageEvent = 39;//1156
    public static final short ModifyWhoCanRideHorseMessageEvent = 1993;//448
    public static final short RemoveBuddyMessageEvent = 698;//1007
    public static final short RefreshCampaignMessageEvent = 3544;//3391
    public static final short AcceptBuddyMessageEvent = 45;//2307
    public static final short YouTubeVideoInformationMessageEvent = 2395;//827
    public static final short FollowFriendMessageEvent = 2280;//1228ye
    public static final short TradingRemoveItemMessageEvent = 1033;//807
    public static final short SaveBotActionMessageEvent = 678;//64
    public static final short GetGroupCreationWindowMessageEvent = 468;//533
    public static final short LetUserInMessageEvent = 2356;//1152
    public static final short InfoRetrieveMessageEvent = 186;//3322
    public static final short CancelQuestMessageEvent = 3879;//1644
    public static final short GetBadgesMessageEvent = 166;//718
    public static final short GenerateSecretKeyMessageEvent = 3847;//67
    public static final short GetMarketplaceItemStatsMessageEvent = 1203;//3643
    public static final short GetSellablePetBreedsMessageEvent = 2505;//3907
    public static final short ForceOpenCalendarBoxMessageEvent = 2879;//869
    public static final short SetFriendBarStateMessageEvent = 716;//2145
    public static final short DeleteRoomMessageEvent = 722;//2548
    public static final short SetSoundSettingsMessageEvent = 3820;//1981
    public static final short InitializeGameCenterMessageEvent = 751;//20
    public static final short RedeemOfferCreditsMessageEvent = 1207;//3597
    public static final short GetGroupFurniSettingsMessageEvent = 41;//984
    public static final short FriendListUpdateMessageEvent = 2664;//2794
    public static final short ModerateRoomMessageEvent = 3458;//3698
    public static final short DeclineGroupMembershipMessageEvent = 403;//2635
    public static final short AvatarEffectActivatedMessageEvent = 129;//2574
    public static final short JoinGroupMessageEvent = 2615;//3255
    public static final short ConfirmLoveLockMessageEvent = 2082;//305
    public static final short UseHabboWheelMessageEvent = 2651;//3470
    public static final short OpenFlatConnectionMessageEvent = 407;//3054
    public static final short TradingOfferItemsMessageEvent = 2996;//3899
    public static final short SaveRoomSettingsMessageEvent = 2074;//437
    public static final short DropHandItemMessageEvent = 1751;//2136
    public static final short ToggleMoodlightMessageEvent = 1826;//589
    public static final short GetDailyQuestMessageEvent = 484;//3642
    public static final short SetMannequinNameMessageEvent = 2406;//1527
    public static final short UseOneWayGateMessageEvent = 2816;//3704
    public static final short EventTrackerMessageEvent = 2386;//926
    public static final short FloorPlanEditorRoomPropertiesMessageEvent = 24;//132
    public static final short TradingConfirmMessageEvent = 2399;//1826
    public static final short PickUpPetMessageEvent = 2342;//2324
    public static final short UpdateGroupColoursMessageEvent = 1443;//182
    public static final short GetPetInventoryMessageEvent = 263;//190
    public static final short AvatarEffectSelectedMessageEvent = 628;//1680
    public static final short InitializeFloorPlanSessionMessageEvent = 2623;//1535
    public static final short GetOwnOffersMessageEvent = 3829;//3626
    public static final short RequestFurniInventoryMessageEvent = 352;//1093
    public static final short GoToFlatMessageEvent = 1601;//811
    public static final short CheckPetNameMessageEvent = 159;//2734
    public static final short SetUserFocusPreferenceEvent = 526;//45
    public static final short SubmitBullyReportMessageEvent = 1803;//3678
    public static final short RemoveRightsMessageEvent = 40;//2967
    public static final short MakeOfferMessageEvent = 255;//2152
    public static final short KickUserMessageEvent = 3929;//2926
    public static final short GetRoomSettingsMessageEvent = 1014;//3139
    public static final short GetThreadsListDataMessageEvent = 1606;//1775
    public static final short GetForumUserProfileMessageEvent = 2639;
    public static final short SaveWiredEffectConfigMessageEvent = 3431;//2554
    public static final short GetAchievementsMessageEvent = 2931;//3919
    public static final short GetRoomEntryDataMessageEvent = 2768;//1880
    public static final short JoinPlayerQueueMessageEvent = 951;//1082
    public static final short GetModeratorUserChatlogMessageEvent = 695;//2041
    public static final short CanCreateRoomMessageEvent = 361;//1832
    public static final short SetTonerMessageEvent = 1061;//3125
    public static final short GetModeratorTicketChatlogsMessageEvent = 3484;//1168
    public static final short SetGroupFavouriteMessageEvent = 2625;//2333
    public static final short ModerationCautionMessageEvent = 505;//3068
    public static final short SaveWiredTriggerConfigMessageEvent = 1897;//776
    public static final short PlaceBotMessageEvent = 2321;//2496
    public static final short GetRelationshipsMessageEvent = 866;//1354
    public static final short ModerationBanMessageEvent = 2595;//3594
    public static final short SetMessengerInviteStatusMessageEvent = 1379;//687
    public static final short UseFurnitureMessageEvent = 3846;//3395
    public static final short GetUserFlatCatsMessageEvent = 3672;//18
    public static final short AssignRightsMessageEvent = 3574;//157
    public static final short GetRoomBannedUsersMessageEvent = 581;//2769
    public static final short ReleaseTicketMessageEvent = 3800;//3372
    public static final short OpenPlayerProfileMessageEvent = 3591;//1883
    public static final short GetSanctionStatusMessageEvent = 2883;//1864
    public static final short CreditFurniRedeemMessageEvent = 1676;//3128
    public static final short DisconnectionMessageEvent = 2391;//1650
    public static final short ActionMessageEvent = 3639;//1542
    public static final short PickupObjectMessageEvent = 636;//2908
    public static final short FindRandomFriendingRoomMessageEvent = 1874;//835
    public static final short UseSellableClothingMessageEvent = 818;//3343
    public static final short TradingCancelMessageEvent = 2967;//3302
    public static final short MoveObjectMessageEvent = 1781;//1107
    public static final short LookToMessageEvent = 3744;//1352
    public static final short InitCryptoMessageEvent = 316;//3136
    public static final short GetFurnitureAliasesMessageEvent = 2125;//2695
    public static final short TakeAdminRightsMessageEvent = 2725;//1588
    public static final short ModifyRoomFilterListMessageEvent = 256;//135
    public static final short MoodlightUpdateMessageEvent = 856;//2643
    public static final short GetPetTrainingPanelMessageEvent = 2088;//2081
    public static final short GetGroupMembersMessageEvent = 205;//1492
    public static final short GetModeratorRoomChatlogMessageEvent = 2312;//2829
    public static final short GetSongInfoMessageEvent = 3418;//2497
    public static final short UseWallItemMessageEvent = 3396;//3977
    public static final short GetTalentTrackMessageEvent = 1284;//1793
    public static final short GiveAdminRightsMessageEvent = 465;//2623
    public static final short PostGroupContentMessageEvent = 477;//2865
    public static final short GetCatalogModeMessageEvent = 2267;//1962
    public static final short SendBullyReportMessageEvent = 2973;//1809
    public static final short CancelOfferMessageEvent = 1862;//257
    public static final short GetModeratorUserInfoMessageEvent = 2984;//3398
    public static final short SaveWiredConditionConfigMessageEvent = 488;//1074
    public static final short GetModeratorRoomInfoMessageEvent = 182;//2244
    public static final short TradingOfferItemMessageEvent = 114;//1854
    public static final short RedeemVoucherMessageEvent = 489;//389
    public static final short ThrowDiceMessageEvent = 1182;//1461
    public static final short ModerationKickMessageEvent = 3589;//1196
    public static final short CraftSecretMessageEvent = 1622;//3353
    public static final short GetGameListingMessageEvent = 2993;//189
    public static final short GetModeratorUserRoomVisitsMessageEvent = 730;//3226
    public static final short SetRelationshipMessageEvent = 2112;//3655
    public static final short RequestBuddyMessageEvent = 3775;//3337
    public static final short MemoryPerformanceMessageEvent = 731;//2721
    public static final short SubmitNewTicketMessageEvent = 963;//2927
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 21;//3248
    public static final short ToggleYouTubeVideoMessageEvent = 890;//1938
    public static final short SetMannequinFigureMessageEvent = 3936;//3877
    public static final short GetEventCategoriesMessageEvent = 1086;//3716
    public static final short DeleteGroupThreadMessageEvent = 3299;//1439
    public static final short ApplySignMessageEvent = 2966;//1183
    public static final short StartQuestMessageEvent = 1282;//639
    public static final short PurchaseGroupMessageEvent = 2546;//2025
    public static final short MessengerInitMessageEvent = 2151;//3551
    public static final short CancelTypingMessageEvent = 1114;//2664
    public static final short GetMoodlightConfigMessageEvent = 3472;//223
    public static final short OpenHelpToolMessageEvent = 1839;//2120
    public static final short GetGroupInfoMessageEvent = 3211;//538
    public static final short CreateFlatMessageEvent = 3077;//3939
    public static final short GetWardrobeMessageEvent = 765;//3906
    public static final short LatencyTestMessageEvent = 1789;//2613
    public static final short ChangeMottoMessageEvent = 3515;//1075
    public static final short GetSelectedBadgesMessageEvent = 2226;//1515
    public static final short AddStickyNoteMessageEvent = 425;//3911
    public static final short ChangeNameMessageEvent = 1067;//750
    public static final short RideHorseMessageEvent = 1440;//1474
    public static final short GetCatalogIndexMessageEvent = 1294;//690
    public static final short InitializeNewNavigatorMessageEvent = 882;//3109
    public static final short SetChatPreferenceMessageEvent = 2006;//408
    public static final short GetForumsListDataMessageEvent = 3912;//3686
    public static final short ToggleMuteToolMessageEvent = 2462;//3155
    public static final short UpdateGroupIdentityMessageEvent = 1062;//3249
    public static final short UpdateStickyNoteMessageEvent = 342;//2706
    public static final short UnbanUserFromRoomMessageEvent = 3060;//3962
    public static final short UnIgnoreUserMessageEvent = 3023;//2158
    public static final short OpenGiftMessageEvent = 1515;//1001
    public static final short ApplyDecorationMessageEvent = 728;//1859
    public static final short GetRecipeConfigMessageEvent = 3654;//1090
    public static final short ScrGetUserInfoMessageEvent = 12;//537
    public static final short RemoveGroupMemberMessageEvent = 649;//2481
    public static final short DiceOffMessageEvent = 191;//1705
    public static final short YouTubeGetNextVideo = 1843;//1695
    public static final short GetQuestListMessageEvent = 2305;//2100
    public static final short DeleteFavouriteRoomMessageEvent = 855;//856
    public static final short RespectUserMessageEvent = 1955;//3237
    public static final short AddFavouriteRoomMessageEvent = 3092;//3623
    public static final short DeclineBuddyMessageEvent = 835;//829
    public static final short StartTypingMessageEvent = 3362;//289
    public static final short GetGroupFurniConfigMessageEvent = 3046;//1522
    public static final short SendRoomInviteMessageEvent = 2694;//779
    public static final short RemoveAllRightsMessageEvent = 1404;//795
    public static final short GetYouTubeTelevisionMessageEvent = 3517;//2609
    public static final short FindNewFriendsMessageEvent = 1264;//60
    public static final short GetPromotableRoomsMessageEvent = 276;//1765
    public static final short GetBotInventoryMessageEvent = 363;//2708
    public static final short TradingModifyMessageEvent = 1153;//1119
    public static final short PurchaseFromCatalogMessageEvent = 2830;//3146
    public static final short GetRentableSpaceMessageEvent = 793;//3763
    public static final short OpenBotActionMessageEvent = 2544;//3985
    public static final short SetUsernameMessageEvent = 2577;//84
    public static final short OpenCalendarBoxMessageEvent = 724;//1898
    public static final short DeleteGroupPostMessageEvent = 317;//2614
    public static final short GetClientVersionMessageEvent = 4000;//4000
    public static final short ModerationMuteMessageEvent = 1940;//2011
    public static final short CheckValidNameMessageEvent = 8;//1642
    public static final short UpdateGroupBadgeMessageEvent = 2959;//2452
    public static final short PlaceObjectMessageEvent = 579;//417
    public static final short GetCreditsInfoMessageEvent = 3697;//3369
    public static final short RemoveGroupFavouriteMessageEvent = 1412;//2173
    public static final short UpdateNavigatorSettingsMessageEvent = 2501;//1466
    public static final short ModerationTradeLockMessageEvent = 1160;//2374
    public static final short UniqueIDMessageEvent = 1471;//3718
    public static final short CheckGnomeNameMessageEvent = 2281;//3333
    public static final short NewNavigatorSearchMessageEvent = 2722;//343
    public static final short GetPetInformationMessageEvent = 2853;//542
    public static final short GetGuestRoomMessageEvent = 1164;//22
    public static final short UpdateThreadMessageEvent = 1522;//546
    public static final short AcceptGroupMembershipMessageEvent = 2259;//3373
    public static final short GetMarketplaceConfigurationMessageEvent = 1604;//618
    public static final short Game2GetWeeklyLeaderboardMessageEvent = 2106;//2268
    public static final short BuyOfferMessageEvent = 3699;//82
    public static final short SitMessageEvent = 1565;//302
    public static final short RemoveSaddleFromHorseMessageEvent = 1892;//1836
    public static final short GiveRoomScoreMessageEvent = 336;//3909
    public static final short GetHabboClubWindowMessageEvent = 715;//523
    public static final short DeleteStickyNoteMessageEvent = 2777;//1870
    public static final short MuteUserMessageEvent = 2997;//121
    public static final short ApplyHorseEffectMessageEvent = 870;//2289
    public static final short SSOTicketMessageEvent = 1778;//1563
    public static final short OnBullyClickMessageEvent = 1932;//2105
    public static final short HabboSearchMessageEvent = 3375;//2053
    public static final short PickTicketMessageEvent = 3973;//2017
    public static final short UpdateFigureDataMessageEvent = 2560;//916
    public static final short GetGiftWrappingConfigurationMessageEvent = 1928;//1631
    public static final short GetCraftingRecipesAvailableMessageEvent = 1653;//2576
    public static final short GetThreadDataMessageEvent = 1559;//433
    public static final short ManageGroupMessageEvent = 2547;//3693
    public static final short PlacePetMessageEvent = 223;//2313
    public static final short EditRoomPromotionMessageEvent = 3707;//480
    public static final short GetCatalogOfferMessageEvent = 2180;//1654
    public static final short SaveFloorPlanModelMessageEvent = 1287;//946
    public static final short WhisperMessageEvent = 878;//2782
    public static final short MoveWallItemMessageEvent = 609;//40
    public static final short GetBuddyRequestsMessageEvent = 2485;//1349
    public static final short ClientVariablesMessageEvent = 1600;//3992
    public static final short ShoutMessageEvent = 2101;//692
    public static final short PingMessageEvent = 2584;//2572
    public static final short DeleteGroupMessageEvent = 747;//1418
    public static final short UpdateGroupSettingsMessageEvent = 3180;//3697
    public static final short GetRecyclerRewardsMessageEvent = 3258;//2508
    public static final short PurchaseRoomPromotionMessageEvent = 3078;//1497
    public static final short PickUpBotMessageEvent = 644;//3721
    public static final short GetOffersMessageEvent = 442;//230
    public static final short DanceMessageEvent = 645;//3176
    public static final short GetHabboGroupBadgesMessageEvent = 301;//452
    public static final short GetUserTagsMessageEvent = 1722;//1869
    public static final short GetPlayableGamesMessageEvent = 482;//3063
    public static final short ModeratorActionMessageEvent = 781;//3700
    public static final short SaveWardrobeOutfitMessageEvent = 55;//1629
    public static final short GetCatalogRoomPromotionMessageEvent = 538;//41
    public static final short SetActivatedBadgesMessageEvent = 2752;//911
    public static final short MoveAvatarMessageEvent = 1737;//1384
    public static final short SaveBrandingItemMessageEvent = 3156;//3753
    public static final short TradingCancelConfirmMessageEvent = 2264;//3787
    public static final short SaveEnforcedCategorySettingsMessageEvent = 3413;//3126
    public static final short TradingAcceptMessageEvent = 3374;//2618
    public static final short RespectPetMessageEvent = 1618;//775
    public static final short GetMarketplaceCanMakeOfferMessageEvent = 1647;//184
    public static final short UpdateMagicTileMessageEvent = 1248;//3178
    public static final short GetStickyNoteMessageEvent = 2796;//143
    public static final short IgnoreUserMessageEvent = 2394;//2135
    public static final short BanUserMessageEvent = 3940;//3069
    public static final short UpdateForumSettingsMessageEvent = 931;//2690
    public static final short ModerationMsgMessageEvent = 2375;//675
    public static final short GetRoomRightsMessageEvent = 2734;//2000
    public static final short SendMsgMessageEvent = 1981;//936
    public static final short CloseTicketMesageEvent = 50;//1165
    public static final short UpdateSnapshotsMessageEvent = 2703;

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
