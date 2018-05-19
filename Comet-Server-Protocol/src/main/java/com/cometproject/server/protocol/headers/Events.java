package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Events {
    public static final short ConfirmUsernameMessageEvent = 1590;//3199
    public static final short GetRoomBannedUsersMessageEvent = 2652;//824
    public static final short GetPetInventoryMessageEvent = 567;//2326
    public static final short DropHandItemMessageEvent = 2846;//1454
    public static final short ReleaseTicketMessageEvent = 3042;//893
    public static final short GetModeratorRoomInfoMessageEvent = 1299;//1701
    public static final short KickUserMessageEvent = 3838;//310
    public static final short SaveWiredEffectConfigMessageEvent = 1843;//3789
    public static final short RespectPetMessageEvent = 31;//856
    public static final short GenerateSecretKeyMessageEvent = 412;//1127
    public static final short GetModeratorTicketChatlogsMessageEvent = 3461;//1660
    public static final short GetAchievementsMessageEvent = 3047;//1039
    public static final short SaveWiredTriggerConfigMessageEvent = 2167;//2954
    public static final short AcceptGroupMembershipMessageEvent = 2891;//3155
    public static final short GetGroupFurniSettingsMessageEvent = 2666;//1123
    public static final short TakeAdminRightsMessageEvent = 2542;//3630
    public static final short RemoveAllRightsMessageEvent = 2398;//2853
    public static final short UpdateThreadMessageEvent = 777;//1911
    public static final short ManageGroupMessageEvent = 2697;//1992
    public static final short ModifyRoomFilterListMessageEvent = 2973;//514
    public static final short SSOTicketMessageEvent = 286;//2190
    public static final short JoinGroupMessageEvent = 2529;//887
    public static final short DeclineGroupMembershipMessageEvent = 2688;//40
    public static final short UniqueIDMessageEvent = 921;//522
    public static final short RemoveMyRightsMessageEvent = 3596;//3765
    public static final short ApplyHorseEffectMessageEvent = 2160;//1869
    public static final short GetPetInformationMessageEvent = 983;//2517
    public static final short GiveHandItemMessageEvent = 2094;//881
    public static final short UpdateFigureDataMessageEvent = 3509;//586
    public static final short RemoveGroupMemberMessageEvent = 3326;//2541
    public static final short EventLogMessageEvent = 658;//1252
    public static final short RefreshCampaignMessageEvent = 2260;//1409
    public static final short GetPromotableRoomsMessageEvent = 2372;//381
    public static final short UseOneWayGateMessageEvent = 3521;//1136
    public static final short AddStickyNoteMessageEvent = 890;//1554
    public static final short GetSelectedBadgesMessageEvent = 904;//3562
    public static final short UpdateStickyNoteMessageEvent = 3742;//1858
    public static final short CloseTicketMesageEvent = 3406;//78
    public static final short RequestBuddyMessageEvent = 3619;//918
    public static final short GetFurnitureAliasesMessageEvent = 3253;//2437
    public static final short GetRoomSettingsMessageEvent = 3700;//501
    public static final short RequestFurniInventoryMessageEvent = 3750;//2905
    public static final short ModerationKickMessageEvent = 794;//933
    public static final short OpenFlatConnectionMessageEvent = 2450;//1718
    public static final short SpectateRoomMessageEvent = 1187;//aa
    public static final short DanceMessageEvent = 1126;//1527
    public static final short RemoveBuddyMessageEvent = 1106;//3022
    public static final short LatencyTestMessageEvent = 2348;//3978
    public static final short InfoRetrieveMessageEvent = 2401;//518
    public static final short YouTubeGetNextVideo = 1294;//1202
    public static final short SetObjectDataMessageEvent = 2194;//1429
    public static final short MessengerInitMessageEvent = 1186;//958
    public static final short PickUpBotMessageEvent = 2856;//2790
    public static final short ActionMessageEvent = 3017;//2234
    public static final short LookToMessageEvent = 3557;//2187
    public static final short ToggleMoodlightMessageEvent = 3146;//3440
    public static final short FollowFriendMessageEvent = 2;//2087
    public static final short PickUpPetMessageEvent = 1207;//814
    public static final short GetSellablePetBreedsMessageEvent = 171;//2463
    public static final short GetForumUserProfileMessageEvent = 2593;//1279
    public static final short GetForumsListDataMessageEvent = 559;//1307
    public static final short IgnoreUserMessageEvent = 2097;//2825
    public static final short DeleteRoomMessageEvent = 638;//1748
    public static final short StartQuestMessageEvent = 2425;//646
    public static final short GetGiftWrappingConfigurationMessageEvent = 3335;//1400
    public static final short UpdateGroupIdentityMessageEvent = 1885;//3296
    public static final short RideHorseMessageEvent = 45;//3097
    public static final short ApplySignMessageEvent = 1195;//619
    public static final short FindRandomFriendingRoomMessageEvent = 1275;//2960
    public static final short GetModeratorUserChatlogMessageEvent = 1925;//1655
    public static final short GetWardrobeMessageEvent = 2699;//3046
    public static final short MuteUserMessageEvent = 3753;//431
    public static final short UpdateForumSettingsMessageEvent = 2339;//663
    public static final short ApplyDecorationMessageEvent = 1388;//368
    public static final short GetBotInventoryMessageEvent = 1199;//1621
    public static final short UseHabboWheelMessageEvent = 2452;//1152
    public static final short EditRoomPromotionMessageEvent = 22;//643
    public static final short GetModeratorUserInfoMessageEvent = 1588;//2821
    public static final short PlaceBotMessageEvent = 3391;//2833
    public static final short GetCatalogPageMessageEvent = 2148;//3269
    public static final short GetThreadsListDataMessageEvent = 276;//3055
    public static final short ShoutMessageEvent = 1795;//592
    public static final short DiceOffMessageEvent = 3670;//2000
    public static final short LetUserInMessageEvent = 754;//3626
    public static final short SetActivatedBadgesMessageEvent = 1429;//2581
    public static final short UpdateGroupSettingsMessageEvent = 164;//2123
    public static final short ApproveNameMessageEvent = 321;//750
    public static final short SubmitNewTicketMessageEvent = 2586;//3736
    public static final short DeleteGroupMessageEvent = 3593;//2962
    public static final short DeleteStickyNoteMessageEvent = 2506;//2682
    public static final short GetGroupInfoMessageEvent = 1415;//1850
    public static final short GetStickyNoteMessageEvent = 3686;//2867
    public static final short DeclineBuddyMessageEvent = 1591;//117
    public static final short OpenGiftMessageEvent = 2066;//1575
    public static final short GiveRoomScoreMessageEvent = 337;//1327
    public static final short SetGroupFavouriteMessageEvent = 538;//486
    public static final short SetMannequinNameMessageEvent = 1301;//2751
    public static final short CallForHelpMessageEvent = 2586;//3736
    public static final short RoomDimmerSavePresetMessageEvent = 1488;//445
    public static final short UpdateGroupBadgeMessageEvent = 1088;//1690
    public static final short PickTicketMessageEvent = 1001;//3975
    public static final short SetTonerMessageEvent = 2186;//866
    public static final short RespectUserMessageEvent = 2377;//2050
    public static final short DeleteGroupThreadMessageEvent = 1549;//128
    public static final short DeleteGroupReplyMessageEvent = 1616;//1198
    public static final short CreditFurniRedeemMessageEvent = 1009;//946
    public static final short ModerationMsgMessageEvent = 975;//2828
    public static final short ToggleYouTubeVideoMessageEvent = 1777;//1293
    public static final short UpdateNavigatorSettingsMessageEvent = 3724;//2178
    public static final short ToggleMuteToolMessageEvent = 36;//3553
    public static final short ChatMessageEvent = 1831;//395
    public static final short SaveRoomSettingsMessageEvent = 1090;//1732
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 2142;//2678
    public static final short GetGroupCreationWindowMessageEvent = 1907;//807
    public static final short GiveAdminRightsMessageEvent = 1587;//3221
    public static final short GetGroupMembersMessageEvent = 1048;//1834
    public static final short ModerateRoomMessageEvent = 2949;//64
    public static final short GetForumStatsMessageEvent = 2932;//952
    public static final short GetPromoArticlesMessageEvent = 1293;//1090
    public static final short SitMessageEvent = 1805;//1250
    public static final short SetSoundSettingsMessageEvent = 1718;//1266
    public static final short ModerationCautionMessageEvent = 2849;//3585
    public static final short InitializeFloorPlanSessionMessageEvent = 698;//228
    public static final short ModeratorActionMessageEvent = 1992;//1282
    public static final short PostGroupContentMessageEvent = 324;//147
    public static final short GetModeratorRoomChatlogMessageEvent = 329;//3136
    public static final short GetUserFlatCatsMessageEvent = 1761;//1832
    public static final short RemoveRightsMessageEvent = 1877;//973
    public static final short ModerationBanMessageEvent = 265;//872
    public static final short CanCreateRoomMessageEvent = 3866;//3014
    public static final short UseWallItemMessageEvent = 3032;//3473
    public static final short PlaceObjectMessageEvent = 1268;//2973
    public static final short OpenBotActionMessageEvent = 643;//901
    public static final short GetEventCategoriesMessageEvent = 1735;//3783
    public static final short GetRoomEntryDataMessageEvent = 2195;//227
    public static final short MoveWallItemMessageEvent = 1038;//3094
    public static final short UpdateGroupColoursMessageEvent = 1131;//3125
    public static final short HabboSearchMessageEvent = 1145;//3584
    public static final short CommandBotMessageEvent = 436;//3554
    public static final short SetCustomStackingHeightMessageEvent = 3794;//3920
    public static final short UnIgnoreUserMessageEvent = 2879;//101
    public static final short GetGuestRoomMessageEvent = 3646;//552
    public static final short SetMannequinFigureMessageEvent = 190;//2975
    public static final short AssignRightsMessageEvent = 2578;//1917
    public static final short GetYouTubeTelevisionMessageEvent = 1183;//1443
    public static final short SetMessengerInviteStatusMessageEvent = 2702;//625
    public static final short UpdateFloorPropertiesMessageEvent = 400;//2141
    public static final short GetMoodlightConfigMessageEvent = 2090;//749
    public static final short PurchaseRoomPromotionMessageEvent = 2937;//1403
    public static final short SendRoomInviteMessageEvent = 282;//1777
    public static final short ModerationMuteMessageEvent = 3861;//3579
    public static final short SetRelationshipMessageEvent = 681;//1205
    public static final short ChangeMottoMessageEvent = 3079;//1076
    public static final short UnbanUserFromRoomMessageEvent = 451;//3047
    public static final short GetRoomRightsMessageEvent = 551;//2683
    public static final short PurchaseGroupMessageEvent = 1934;//3782
    public static final short CreateFlatMessageEvent = 859;//2139
    public static final short OpenHelpToolMessageEvent = 1708;//924
    public static final short ThrowDiceMessageEvent = 348;//1886
    public static final short SaveWiredConditionConfigMessageEvent = 2001;//676
    public static final short GetCatalogOfferMessageEvent = 1313;//1181
    public static final short PurchaseFromCatalogMessageEvent = 3250;//1733
    public static final short PickupObjectMessageEvent = 3064;//1504
    public static final short CancelQuestMessageEvent = 3297;//2672
    public static final short NavigatorSearchMessageEvent = 946;//3706
    public static final short MoveAvatarMessageEvent = 3802;//2175
    public static final short GetClientVersionMessageEvent = 4000;//4000
    public static final short InitializeNavigatorMessageEvent = 3231;//2061
    public static final short GetRoomFilterListMessageEvent = 1973;//354
    public static final short WhisperMessageEvent = 88;//3182
    public static final short InitCryptoMessageEvent = 3347;//1905
    public static final short GetPetTrainingPanelMessageEvent = 2691;//3274
    public static final short MoveObjectMessageEvent = 2955;//2104
    public static final short StartTypingMessageEvent = 1266;//1147
    public static final short GoToHotelViewMessageEvent = 2644;//882
    public static final short GetExtendedProfileMessageEvent = 3455;//3596
    public static final short SendMsgMessageEvent = 1750;//779
    public static final short CancelTypingMessageEvent = 978;//2045
    public static final short GetGroupFurniConfigMessageEvent = 2183;//38
    public static final short RemoveGroupFavouriteMessageEvent = 1332;//73
    public static final short PlacePetMessageEvent = 2174;//3332
    public static final short ModifyWhoCanRideHorseMessageEvent = 579;//921
    public static final short GetRelationshipsMessageEvent = 716;//2220
    public static final short GetCatalogIndexMessageEvent = 2069;//3285
    public static final short ScrGetUserInfoMessageEvent = 857;//2657
    public static final short ConfirmLoveLockMessageEvent = 357;//3256
    public static final short RemoveSaddleFromHorseMessageEvent = 2118;//3853
    public static final short AcceptBuddyMessageEvent = 2363;//661
    public static final short GetQuestListMessageEvent = 3227;//46
    public static final short SaveWardrobeOutfitMessageEvent = 1577;//596
    public static final short BanUserMessageEvent = 1414;//3344
    public static final short GetThreadDataMessageEvent = 1832;//2335
    public static final short GetBadgesMessageEvent = 1723;//916
    public static final short UseFurnitureMessageEvent = 788;//3119
    public static final short GoToFlatMessageEvent = 586;//3021
    public static final short GetModeratorUserRoomVisitsMessageEvent = 2828;//757
    public static final short GetSanctionStatusMessageEvent = 1654;//25
    public static final short SetChatPreferenceMessageEvent = 3378;//3027
    public static final short ResizeNavigatorMessageEvent = 3072;//3831
    public static final short CameraDataMessageEvent = 654;//782
    public static final short RenderRoomMessageEvent = 1184;//2505
    public static final short BuyPhotoMessageEvent = 1554;//2358
    public static final short SongInventoryMessageEvent = 1218;//2675
    public static final short SongIdMessageEvent = 37;//2365
    public static final short SongDataMessageEvent = 2675;//217
    public static final short PlaylistMessageEvent = 1176;//3730
    public static final short PlaylistAddMessageEvent = 52;//2370
    public static final short PlaylistRemoveMessageEvent = 1562;//3670
    public static final short StaffPickRoomMessageEvent = 1920;//570
    public static final short SubmitPollAnswerMessageEvent = 2978;//1762
    public static final short GetPollMessageEvent = 1422;//405
    public static final short UpdateSnapshotsMessageEvent = 290;//2489
    public static final short MarkAsReadMessageEvent = 2073;//3561
    public static final short InitTradeMessageEvent = 3722;//2460
    public static final short TradingOfferItemMessageEvent = 3376;//2849
    public static final short TradingOfferItemsMessageEvent = 3395;//1884
    public static final short TradingRemoveItemMessageEvent = 240;//3227
    public static final short TradingAcceptMessageEvent = 2165;//305
    public static final short TradingCancelMessageEvent = 1926;//3003
    public static final short TradingModifyMessageEvent = 945;//3885
    public static final short TradingConfirmMessageEvent = 561;//620
    public static final short TradingCancelConfirmMessageEvent = 1703;//3698
    public static final short RedeemVoucherMessageEvent = 1191;//3327
    public static final short Hide = 1191;//3327
    public static final short ChangeNameMessageEvent = 3124;//2443
    public static final short CheckValidNameMessageEvent = 3946;//2102
    public static final short RequestGuideToolMessageEvent = 2599;//493
    public static final short RequestGuideAssistanceMessageEvent = 2761;//330
    public static final short GuideUserTypingMessageEvent = 3302;//186
    public static final short GuideReportHelperMessageEvent = 2581;//1887
    public static final short GuideRecommendHelperMessageEvent = 3563;//3350
    public static final short GuideUserMessageMessageEvent = 3062;//479
    public static final short GuideCancelHelpRequestMessageEvent = 903;//2735
    public static final short GuideHandleHelpRequestMessageEvent = 835;//3827
    public static final short GuideVisitUserMessageEvent = 2353;//1781
    public static final short GuideInviteUserMessageEvent = 2156;//149
    public static final short GuideCloseHelpRequestMessageEvent = 175;//1626
    public static final short GuardianNoUpdatesWantedMessageEvent = 2267;//2115
    public static final short GuardianVoteMessageEvent = 3625;//1676
    public static final short GuardianAcceptRequestMessageEvent = 706;//3429
    public static final short RequestReportUserBullyingMessageEvent = 2385;//2388
    public static final short ReportBullyMessageEvent = 318;//3543
    public static final short GetUserTagsMessageEvent = 1468;//702
    public static final short FindNewFriendsMessageEvent = 488;//531
    public static final short SaveNavigatorSearchMessageEvent = 3301;//3718
    public static final short DeleteNavigatorSavedSearchMessageEvent = 2235;//1800
    public static final short AddFavouriteRoomMessageEvent = 3523;//533
    public static final short DeleteFavouriteRoomMessageEvent = 1969;//1160
    public static final short SaveFootballGateMessageEvent = 177;//3832
    public static final short GroupConfirmRemoveMemberMessageEvent = 423;//3478
    public static final short GetGroupPartsMessageEvent = 2047;//2451
    public static final short SetRoomCameraFollowMessageEvent = 3527;//3279
    public static final short BreedPetsMessageEvent = 2162;//2805
    public static final short OpenPetPackageMessageEvent = 455;//2249
    public static final short RedeemClothingMessageEvent = 3162;//2699
    public static final short EquipEffectMessageEvent = 2255;//295
    public static final short ThumbnailMessageEvent = 2046;
    public static final short PurchasePhotoMessageEvent = 1554;
    public static final short PhotoPricingMessageEvent = 654;
    public static final short GetGameListMessageEvent = 1288;
    public static final short GetGameAchievementsMessageEvent = 97;
    public static final short GetGameStatusMessageEvent = 1740;
    public static final short JoinGameQueueMessageEvent = 3654;
    public static final short NavigatorSaveViewModeMessageEvent = 597;

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