package com.cometproject.server.network.messages.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Events {
    public static final short ModerationToolUserToolMessageEvent = 594;//2300
    public static final short PromoteRoomMessageEvent = 1794;//1324
    public static final short LoadBadgeInventoryMessageEvent = 2208;//307
    public static final short SongDataMessageEvent = 1747;//2520
    public static final short InitCryptoMessageEvent = 0;//1582
    public static final short UnignoreUserMessageEvent = 2644;//1437
    public static final short PlaylistRemoveMessageEvent = 3862;//3621
    public static final short OpenQuestsMessageEvent = 1521;//3555
    public static final short GetGroupInfoMessageEvent = 15;//3654
    public static final short MannequinUpdateDataMessageEvent = 3262;//1675
    public static final short RoomBanUserMessageEvent = 1933;//1079
    public static final short SitMessageEvent = 137;//2881
    public static final short TradeStartMessageEvent = 2307;//951
    public static final short EnterOneWayDoorMessageEvent = 1606;//1306
    public static final short ChatMessageEvent = 17;//2414
    public static final short ConsoleInstantChatMessageEvent = 534;//333
    public static final short SubmitHelpTicketMessageEvent = 2326;//914
    public static final short SaveRoomBackgroundTonerMessageEvent = 1410;//2468
    public static final short LandingRefreshPromosMessageEvent = 3975;//503
    public static final short GetCatalogPageMessageEvent = 2086;//873
    public static final short WordFilterListMessageEvent = 3090;//649
    public static final short LoadItemsInventoryMessageEvent = 726;//442
    public static final short GroupManageMessageEvent = 1159;//3033
    public static final short GiveHanditemMessageEvent = 569;//646
    public static final short EnterPrivateRoomMessageEvent = 53;//2377
    public static final short TradeConfirmMessageEvent = 2340;//2530
    public static final short SaveFloorPlanEditorMessageEvent = 3053;//29
    public static final short DeleteFriendMessageEvent = 2443;//2125
    public static final short TradeUnacceptMessageEvent = 176;//1330
    public static final short GetSubscriptionDataMessageEvent = 3785;//3298
    public static final short PlaylistAddMessageEvent = 3011;//2991
    public static final short ModerationToolUserChatlogMessageEvent = 1504;//314
    public static final short TriggerWallItemMessageEvent = 1125;//399
    public static final short SongIdMessageEvent = 3595;//2737
    public static final short CatalogPromotionGetRoomsMessageEvent = 913;//3979
    public static final short InfoRetrieveMessageEvent = 2173;//2650
    public static final short NextQuestMessageEvent = 3574;//1257
    public static final short RoomSettingsMuteUserMessageEvent = 713;//202
    public static final short PickUpPetMessageEvent = 3859;//3190
    public static final short InitializeFriendListMessageEvent = 2072;//996
    public static final short PickUpBotMessageEvent = 179;//1722
    public static final short GiveRespectMessageEvent = -2939;//2939
    public static final short DeletePendingTicketMessageEvent = 1976;//3504
    public static final short SavePostItMessageEvent = 2408;//3296
    public static final short UpdateMoodlightMessageEvent = 2382;//2187
    public static final short GroupUpdateBadgeMessageEvent = 2115;//436
    public static final short UserSignMessageEvent = 3780;//2185
    public static final short RoomEventUpdateMessageEvent = 2792;//350
    public static final short GetGroupMembersMessageEvent = 1;//1905
    public static final short FollowFriendMessageEvent = 2511;//55
    public static final short AcceptGroupRequestMessageEvent = 1798;//1596
    public static final short ModerationToolGetRoomVisitsMessageEvent = -2942;//2942
    public static final short RateRoomMessageEvent = 3;//1390
    public static final short CancelQuestMessageEvent = 1003;//2615
    public static final short ReedemExchangeItemMessageEvent = 2711;//2625
    public static final short UniqueIDMessageEvent = 135;//1724
    public static final short ModerationBanUserMessageEvent = 1336;//2037
    public static final short UserUpdateLookMessageEvent = 1093;//3061
    public static final short CreateRoomMessageEvent = 1240;//2738
    public static final short ModerationToolRoomChatlogMessageEvent = 1356;//2198
    public static final short CameraTokenMessageEvent = -1732;//1732
    public static final short ClientVersionMessageEvent = 4000;//4000
    public static final short YouTubeGetPlayerMessageEvent = 1857;//277
    public static final short DeclineFriendMessageEvent = 2394;//1884
    public static final short EditWordFilterMessageEvent = 480;//1340
    public static final short RequestFriendMessageEvent = 1824;//1562
    public static final short RoomGetSettingsInfoMessageEvent = 388;//2109
    public static final short RelationshipsGetMessageEvent = 3895;//1256
    public static final short CanCreateRoomMessageEvent = 3497;//3073
    public static final short AchievementsListMessageEvent = -3288;//3288
    public static final short RoomAddFloorItemMessageEvent = 2801;//1517
    public static final short RenderRoomMessageEvent = 963;//1810
    public static final short OpenGiftMessageEvent = 56;//1190
    public static final short RoomRemoveAllRightsMessageEvent = 20;//2159
    public static final short NavigatorGetPopularRoomsMessageEvent = 2615;//544
    public static final short GetFloorPlanDoorMessageEvent = 2103;//1880
    public static final short LoadPetInventoryMessageEvent = 2747;//1684
    public static final short AcceptFriendMessageEvent = 1269;//2815
    public static final short ModerationToolPickTicketMessageEvent = 3546;//303
    public static final short WiredSaveMatchingMessageEvent = 1155;//3375
    public static final short ModerationKickUserMessageEvent = 1834;//698
    public static final short TriggerDiceRollMessageEvent = 967;//2964
    public static final short SetChatPreferenceMessageEvent = 3084;//2076
    public static final short WardrobeUpdateMessageEvent = 3927;//2367
    public static final short RoomUserActionMessageEvent = 3346;//49
    public static final short RetrieveCitizenshipStatus = 644;//2445
    public static final short PetGetInformationMessageEvent = 2953;//2602
    public static final short LandingLoadWidgetMessageEvent = 2269;//3986
    public static final short OpenPostItMessageEvent = 1557;//1144
    public static final short UserWhisperMessageEvent = 1094;//1015
    public static final short NavigatorGetHighRatedRoomsMessageEvent = 795;//115
    public static final short PlaceBotMessageEvent = 11;//1925
    public static final short GetGroupFurnitureMessageEvent = -2936;//2936
    public static final short ChangeUsernameMessageEvent = 2932;//2095
    public static final short SetHomeRoomMessageEvent = 1647;//3301
    public static final short GenerateSecretKeyMessageEvent = 3713;//221
    public static final short IgnoreInvitationsMessageEvent = 3263;//2430
    public static final short ConfirmLoveLockMessageEvent = 3493;//2482
    public static final short TradeAddItemOfferMessageEvent = 2919;//3837
    public static final short CreateGuildMessageEvent = 2975;//2873
    public static final short GoToHotelViewMessageEvent = 151;//1368
    public static final short WiredSaveTriggerMessageEvent = 1191;//2486
    public static final short GetRoomRightsListMessageEvent = 730;//3482
    public static final short ModerationToolRoomToolMessageEvent = -3230;//3230
    public static final short StartTypingMessageEvent = 168;//1069
    public static final short RoomGetInfoMessageEvent = 3662;//2700
    public static final short RoomSettingsMuteAllMessageEvent = 3250;//3091
    public static final short ModerationToolSendUserAlertMessageEvent = 46;//3518
    public static final short RoomRemoveUserRightsMessageEvent = 2297;//2526
    public static final short StartQuestMessageEvent = 3943;//2015
    public static final short PickUpItemMessageEvent = 3509;//3029
    public static final short OpenGuideToolMessageEvent = 3444;//1124
    public static final short GetGroupPurchaseBoxMessageEvent = 2256;//3082
    public static final short LoadBotInventoryMessageEvent = 1297;//2519
    public static final short UseHabboWheelMessageEvent = 1942;//2006
    public static final short GroupMakeAdministratorMessageEvent = 3107;//1662
    public static final short LoadProfileByUsernameMessageEvent = 2172;//2454
    public static final short IgnoreUserMessageEvent = 431;//1700
    public static final short ModerationToolCloseIssueMessageEvent = 680;//2388
    public static final short TakePhotoMessageEvent = 1684;//1339
    public static final short GetGiftWrappingConfigurationMessageEvent = 648;//3329
    public static final short WiredSaveEffectMessageEvent = 3658;//1259
    public static final short LookAtUserMessageEvent = 1180;//2213
    public static final short TradeCancelMessageEvent = 931;//319
    public static final short TriggerDiceCloseMessageEvent = 579;//3774
    public static final short DropHanditemMessageEvent = 2863;//1029
    public static final short NavigatorGetPopularTagsMessageEvent = 123;//2809
    public static final short SaveForumSettingsMessageEvent = 2977;//3152
    public static final short FloorItemMoveMessageEvent = 2465;//923
    public static final short SetRelationshipMessageEvent = 835;//851
    public static final short PlaylistMessageEvent = 1391;//3899
    public static final short ModerationToolSendUserCautionMessageEvent = 448;//1377
    public static final short DoorbellAnswerMessageEvent = 416;//461
    public static final short ModerationToolPerformRoomActionMessageEvent = 939;//2265
    public static final short SSOTicketMessageEvent = 2649;//1278
    public static final short SongInventoryMessageEvent = 2745;//2753
    public static final short ConsoleSearchFriendsMessageEvent = 3315;//3471
    public static final short SaveRoomBrandingMessageEvent = 2901;//176
    public static final short UserWalkMessageEvent = 2839;//1231
    public static final short NavigatorGetFlatCategoriesMessageEvent = 3513;//3594
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 2121;//2534
    public static final short OpenHelpToolMessageEvent = 1964;//298
    public static final short UserUpdateMottoMessageEvent = 1056;//312
    public static final short TradeAcceptMessageEvent = 1251;//1113
    public static final short RoomUnbanUserMessageEvent = 1375;//3130
    public static final short WallItemMoveMessageEvent = 1249;//655
    public static final short CatalogOfferConfigMessageEvent = 3389;//864
    public static final short TradeDiscardMessageEvent = 3161;//1218
    public static final short DeletePostItMessageEvent = 254;//1351
    public static final short GetFloorPlanFurnitureMessageEvent = 1622;//498
    public static final short GetCatalogIndexMessageEvent = 2869;//1083
    public static final short WardrobeMessageEvent = 1246;//2024
    public static final short YouTubeChoosePlaylistVideoMessageEvent = -1648;//1648
    public static final short ActivateMoodlightMessageEvent = 362;//2680
    public static final short RoomDeleteMessageEvent = 2856;//490
    public static final short DeclineMembershipMessageEvent = 3819;//716
    public static final short GetCatalogOfferMessageEvent = 1016;//1708
    public static final short TileStackMagicSetHeightMessageEvent = 2198;//2776
    public static final short RoomLoadByDoorbellMessageEvent = 2107;//2705
    public static final short NavigatorGetEventsMessageEvent = 300;//1357
    public static final short NavigatorGetFeaturedRoomsMessageEvent = 2095;//2719
    public static final short TriggerMoodlightMessageEvent = 2785;//469
    public static final short GiveRightsMessageEvent = 762;//1542
    public static final short GetRoomBannedUsersMessageEvent = 927;//602
    public static final short SaveFootballGateOutfitMessageEvent = 1922;//3245
    public static final short ModerationToolTicketChatlogMessageEvent = 159;//2032
    public static final short NavigatorGetMyRoomsMessageEvent = 129;//183
    public static final short RoomAddPostItMessageEvent = 982;//260
    public static final short RoomSaveSettingsMessageEvent = 643;//3180
    public static final short GroupUserJoinMessageEvent = 3106;//2775
    public static final short RoomApplySpaceMessageEvent = 2279;//3427
    public static final short RequestLatencyTestMessageEvent = 590;//1968
    public static final short CheckPetNameMessageEvent = 3057;//3824
    public static final short RoomKickUserMessageEvent = 1814;//3454
    public static final short MannequinSaveDataMessageEvent = 3880;//3031
    public static final short LoadUserProfileMessageEvent = 2662;//3719
    public static final short BotActionsMessageEvent = 3884;//1243
    public static final short GroupUpdateColoursMessageEvent = 2703;//1721
    public static final short GetUserBadgesMessageEvent = 1961;//496
    public static final short AddToStaffPickedRoomsMessageEvent = -859;//859
    public static final short ShoutMessageEvent = 42;//1647
    public static final short PlacePetMessageEvent = 215;//3821
    public static final short StopTypingMessageEvent = 1409;//1912
    public static final short RequestLeaveGroupMessageEvent = 1389;//2760
    public static final short GroupUpdateNameMessageEvent = 991;//2495
    public static final short GroupFurnitureWidgetMessageEvent = 1233;//451
    public static final short YouTubeGetPlaylistGetMessageEvent = -1450;//1450
    public static final short NavigatorSearchRoomByNameMessageEvent = 1340;//846
    public static final short GetSellablePetBreedsMessageEvent = 3283;//3077
    public static final short UserDanceMessageEvent = 2156;//1743
    public static final short PurchaseFromCatalogMessageEvent = 2881;//2013
    public static final short HorseMountOnMessageEvent = 3808;//1415
    public static final short TriggerItemMessageEvent = 2090;//1777
    public static final short ModerationToolReleaseIssueMessageEvent = 2231;//2730
    public static final short ModerationToolSendRoomAlertMessageEvent = 427;//426
    public static final short GroupUpdateSettingsMessageEvent = 624;//1553
    public static final short WiredSaveConditionMessageEvent = 2520;//904
    public static final short BotSpeechListMessageEvent = -2833;//2833
    public static final short ClearFavouriteGroupMessageEvent = 3654;//360
    public static final short RemoveGroupAdminMessageEvent = 2746;//1376
    public static final short ConsoleInviteFriendsMessageEvent = 2791;//2798
    public static final short SetFavoriteGroupMessageEvent = 2108;//2272
    public static final short RespectPetMessageEvent = 3683;//2175
    public static final short RemoveOwnRightsMessageEvent = 1683;//2529
    public static final short SetActivatedBadgesMessageEvent = 2228;//1333
    public static final short SaveClientSettingsMessageEvent = 1901;//620

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
