package com.cometproject.server.network.messages.headers;

import javolution.util.FastMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;


public class Events {
    public static final short ModerationToolUserToolMessageEvent = 2300;//1578
    public static final short PromoteRoomMessageEvent = 1324;//2315
    public static final short LoadBadgeInventoryMessageEvent = 307;//3697
    public static final short InitCryptoMessageEvent = 1582;//1299
    public static final short UnignoreUserMessageEvent = 1437;//2741
    public static final short OpenQuestsMessageEvent = 3555;//615
    public static final short SitMessageEvent = 2881;//514
    public static final short RoomBanUserMessageEvent = 1079;//1159
    public static final short MannequinUpdateDataMessageEvent = 1675;//1643
    public static final short GetGroupInfoMessageEvent = 3654;//2672
    public static final short TradeStartMessageEvent = 951;//1645
    public static final short EnterOneWayDoorMessageEvent = 1306;//1341
    public static final short ChatMessageEvent = 2414;//1102
    public static final short SubmitHelpTicketMessageEvent = 914;//2547
    public static final short ConsoleInstantChatMessageEvent = 333;//663
    public static final short SaveRoomBackgroundTonerMessageEvent = 2468;//1966
    public static final short LandingRefreshPromosMessageEvent = 503;//2368
    public static final short LoadItemsInventoryMessageEvent = 442;//387
    public static final short GetCatalogPageMessageEvent = 873;//519
    public static final short GiveHanditemMessageEvent = 646;//2641
    public static final short GroupManageMessageEvent = 3033;//1071
    public static final short EnterPrivateRoomMessageEvent = 2377;//2041
    public static final short TradeConfirmMessageEvent = 2530;//2445
    public static final short SaveFloorPlanEditorMessageEvent = 29;//1236
    public static final short DeleteFriendMessageEvent = 2125;//3917
    public static final short GetSubscriptionDataMessageEvent = 3298;//1848
    public static final short TradeUnacceptMessageEvent = 1330;//2453
    public static final short ModerationToolUserChatlogMessageEvent = 314;//546
    public static final short TriggerWallItemMessageEvent = 399;//2379
    public static final short InfoRetrieveMessageEvent = 2650;//2781
    public static final short CatalogPromotionGetRoomsMessageEvent = 3979;//1503
    public static final short RoomSettingsMuteUserMessageEvent = 202;//3211
    public static final short PickUpPetMessageEvent = 3190;//269
    public static final short PickUpBotMessageEvent = 1722;//3284
    public static final short GiveRespectMessageEvent = 2939;//1870
    public static final short SavePostItMessageEvent = 3296;//1338
    public static final short UpdateMoodlightMessageEvent = 2187;//2386
    public static final short GroupUpdateBadgeMessageEvent = 436;//1030
    public static final short UserSignMessageEvent = 2185;//2869
    public static final short RoomEventUpdateMessageEvent = 350;//3170
    public static final short FollowFriendMessageEvent = 55;//3135
    public static final short GetGroupMembersMessageEvent = 1905;//3872
    public static final short ModerationToolGetRoomVisitsMessageEvent = 2942;//2950
    public static final short AcceptGroupRequestMessageEvent = 1596;//1610
    public static final short RateRoomMessageEvent = 1390;//1130
    public static final short ReedemExchangeItemMessageEvent = 2625;//983
    public static final short UniqueIDMessageEvent = 1724;//830
    public static final short ModerationBanUserMessageEvent = 2037;//3196
    public static final short UserUpdateLookMessageEvent = 3061;//1588
    public static final short CreateRoomMessageEvent = 2738;//471
    public static final short ModerationToolRoomChatlogMessageEvent = 2198;//3076
    public static final short CameraTokenMessageEvent = 1732;//3666
    public static final short ClientVersionMessageEvent = 4000;//4000
    public static final short YouTubeGetPlayerMessageEvent = 277;//1252
    public static final short DeclineFriendMessageEvent = 1884;//333
    public static final short RoomGetSettingsInfoMessageEvent = 2109;//1398
    public static final short RequestFriendMessageEvent = 1562;//1298
    public static final short RelationshipsGetMessageEvent = 1256;//3398
    public static final short CanCreateRoomMessageEvent = 3073;//649
    public static final short RoomAddFloorItemMessageEvent = 1517;//1954
    public static final short OpenGiftMessageEvent = 1190;//1140
    public static final short RoomRemoveAllRightsMessageEvent = 2159;//3453
    public static final short NavigatorGetPopularRoomsMessageEvent = 544;//602
    public static final short GetFloorPlanDoorMessageEvent = 1880;//2714
    public static final short LoadPetInventoryMessageEvent = 1684;//3773
    public static final short AcceptFriendMessageEvent = 2815;//1833
    public static final short WiredSaveMatchingMessageEvent = 3375;//3552
    public static final short ModerationKickUserMessageEvent = 698;//1320
    public static final short TriggerDiceRollMessageEvent = 2964;//47
    public static final short SetChatPreferenceMessageEvent = 2076;//1777
    public static final short WardrobeUpdateMessageEvent = 2367;//89
    public static final short RetrieveCitizenshipStatus = 2445;//2743
    public static final short RoomUserActionMessageEvent = 49;//2056
    public static final short PetGetInformationMessageEvent = 2602;//2147
    public static final short LandingLoadWidgetMessageEvent = 3986;//3375
    public static final short OpenPostItMessageEvent = 1144;//1133
    public static final short UserWhisperMessageEvent = 1015;//1612
    public static final short NavigatorGetHighRatedRoomsMessageEvent = 115;//2240
    public static final short PlaceBotMessageEvent = 1925;//3782
    public static final short GetGroupFurnitureMessageEvent = 2936;//1560
    public static final short GenerateSecretKeyMessageEvent = 221;//2096
    public static final short SetHomeRoomMessageEvent = 3301;//3182
    public static final short ChangeUsernameMessageEvent = 2095;//35
    public static final short IgnoreInvitationsMessageEvent = 2430;//930
    public static final short TradeAddItemOfferMessageEvent = 3837;//1271
    public static final short CreateGuildMessageEvent = 2873;//3233
    public static final short GoToHotelViewMessageEvent = 1368;//2382
    public static final short WiredSaveTriggerMessageEvent = 2486;//1942
    public static final short GetRoomRightsListMessageEvent = 3482;//3850
    public static final short ModerationToolRoomToolMessageEvent = 3230;//1505
    public static final short StartTypingMessageEvent = 1069;//801
    public static final short RoomGetInfoMessageEvent = 2700;//3753
    public static final short RoomSettingsMuteAllMessageEvent = 3091;//2686
    public static final short RoomRemoveUserRightsMessageEvent = 2526;//2988
    public static final short ModerationToolSendUserAlertMessageEvent = 3518;//3298
    public static final short PickUpItemMessageEvent = 3029;//1198
    public static final short LoadBotInventoryMessageEvent = 2519;//3774
    public static final short GetGroupPurchaseBoxMessageEvent = 3082;//2834
    public static final short GroupMakeAdministratorMessageEvent = 1662;//3716
    public static final short UseHabboWheelMessageEvent = 2006;//3114
    public static final short LoadProfileByUsernameMessageEvent = 2454;//2507
    public static final short IgnoreUserMessageEvent = 1700;//2713
    public static final short DropHanditemMessageEvent = 1029;//3891
    public static final short NavigatorGetPopularTagsMessageEvent = 2809;//3091
    public static final short SetRelationshipMessageEvent = 851;//1579
    public static final short FloorItemMoveMessageEvent = 923;//2753
    public static final short GetGiftWrappingConfigurationMessageEvent = 3329;//665
    public static final short WiredSaveEffectMessageEvent = 1259;//1300
    public static final short LookAtUserMessageEvent = 2213;//1785
    public static final short ModerationToolSendUserCautionMessageEvent = 1377;//1583
    public static final short TradeCancelMessageEvent = 319;//2233
    public static final short TriggerDiceCloseMessageEvent = 3774;//894
    public static final short DoorbellAnswerMessageEvent = 461;//773
    public static final short SSOTicketMessageEvent = 1278;//516
    public static final short ModerationToolPerformRoomActionMessageEvent = 2265;//3063
    public static final short ConsoleSearchFriendsMessageEvent = 3471;//1168
    public static final short NavigatorGetFlatCategoriesMessageEvent = 3594;//2195
    public static final short UserWalkMessageEvent = 1231;//2115
    public static final short SaveRoomBrandingMessageEvent = 176;//111
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 2534;//2483
    public static final short OpenHelpToolMessageEvent = 298;//1995
    public static final short UserUpdateMottoMessageEvent = 312;//2261
    public static final short TradeAcceptMessageEvent = 1113;//2225
    public static final short RoomUnbanUserMessageEvent = 3130;//3469
    public static final short WallItemMoveMessageEvent = 655;//1663
    public static final short CatalogOfferConfigMessageEvent = 864;//342
    public static final short TradeDiscardMessageEvent = 1218;//1016
    public static final short GetCatalogIndexMessageEvent = 1083;//2072
    public static final short GetFloorPlanFurnitureMessageEvent = 498;//1834
    public static final short DeletePostItMessageEvent = 1351;//558
    public static final short WardrobeMessageEvent = 2024;//1489
    public static final short YouTubeChoosePlaylistVideoMessageEvent = 1648;//598
    public static final short ActivateMoodlightMessageEvent = 2680;//3209
    public static final short RoomDeleteMessageEvent = 490;//113
    public static final short DeclineMembershipMessageEvent = 716;//2330
    public static final short GetCatalogOfferMessageEvent = 1708;//1436
    public static final short RoomLoadByDoorbellMessageEvent = 2705;//945
    public static final short TileStackMagicSetHeightMessageEvent = 2776;//1479
    public static final short NavigatorGetEventsMessageEvent = 1357;//1760
    public static final short NavigatorGetFeaturedRoomsMessageEvent = 2719;//3371
    public static final short GiveRightsMessageEvent = 1542;//3438
    public static final short TriggerMoodlightMessageEvent = 469;//169
    public static final short GetRoomBannedUsersMessageEvent = 602;//2141
    public static final short SaveFootballGateOutfitMessageEvent = 3245;//906
    public static final short NavigatorGetMyRoomsMessageEvent = 183;//2996
    public static final short RoomSaveSettingsMessageEvent = 3180;//1601
    public static final short RoomAddPostItMessageEvent = 260;//1931
    public static final short GroupUserJoinMessageEvent = 2775;//902
    public static final short RoomApplySpaceMessageEvent = 3427;//358
    public static final short RequestLatencyTestMessageEvent = 1968;//1873
    public static final short CheckPetNameMessageEvent = 3824;//1545
    public static final short RoomKickUserMessageEvent = 3454;//1518
    public static final short MannequinSaveDataMessageEvent = 3031;//1079
    public static final short LoadUserProfileMessageEvent = 3719;//672
    public static final short BotActionsMessageEvent = 1243;//2905
    public static final short GetUserBadgesMessageEvent = 496;//3293
    public static final short GroupUpdateColoursMessageEvent = 1721;//1149
    public static final short AddToStaffPickedRoomsMessageEvent = 859;//2241
    public static final short ShoutMessageEvent = 1647;//1895
    public static final short StopTypingMessageEvent = 1912;//883
    public static final short RequestLeaveGroupMessageEvent = 2760;//3421
    public static final short PlacePetMessageEvent = 3821;//1119
    public static final short GroupUpdateNameMessageEvent = 2495;//520
    public static final short GroupFurnitureWidgetMessageEvent = 451;//1525
    public static final short YouTubeGetPlaylistGetMessageEvent = 1450;//545
    public static final short NavigatorSearchRoomByNameMessageEvent = 846;//1455
    public static final short GetSellablePetBreedsMessageEvent = 3077;//504
    public static final short PurchaseFromCatalogMessageEvent = 2013;//197
    public static final short UserDanceMessageEvent = 1743;//1956
    public static final short HorseMountOnMessageEvent = 3476;//2378
    public static final short TriggerItemMessageEvent = 1777;//1630
    public static final short ModerationToolSendRoomAlertMessageEvent = 426;//1644
    public static final short GroupUpdateSettingsMessageEvent = 1553;//199
    public static final short WiredSaveConditionMessageEvent = 904;//78
    public static final short BotSpeechListMessageEvent = 2833;//377
    public static final short RemoveGroupAdminMessageEvent = 1376;//69
    public static final short ConsoleInviteFriendsMessageEvent = 2798;//1953
    public static final short RespectPetMessageEvent = 2175;//1380
    public static final short SetFavoriteGroupMessageEvent = 2272;//3183
    public static final short SaveClientSettingsMessageEvent = 620;//414
    public static final short SetActivatedBadgesMessageEvent = 1333;//836
    public static final short RemoveOwnRightsMessageEvent = 2529;//1021
    public static final short RenderRoomMessageEvent = 1810;
    public static final short ConfirmLoveLockMessageEvent = 2482;
    public static final short ClearFavouriteGroupMessageEvent = 360;
    public static final short WordFilterListMessageEvent = 649;
    public static final short EditWordFilterMessageEvent = 1340;
    public static final short DeletePendingTicketMessageEvent = 3504;
    public static final short ModerationToolPickTicketMessageEvent = 303;
    public static final short ModerationToolTicketChatlogMessageEvent = 2032;
    public static final short ModerationToolCloseIssueMessageEvent = 2388;
    public static final short ModerationToolReleaseIssueMessageEvent = 2730;
    public static final short OpenGuideToolMessageEvent = 1124;
    public static final short SaveForumSettingsMessageEvent = 3152;

    private static Map<Short, String> eventPacketNames = new FastMap<>();

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
