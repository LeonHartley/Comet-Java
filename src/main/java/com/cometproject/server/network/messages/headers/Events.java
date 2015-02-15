package com.cometproject.server.network.messages.headers;

import javolution.util.FastMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;


public class Events {
    public static final short ModerationToolUserToolMessageEvent = 1578;//3161
    public static final short PromoteRoomMessageEvent = 2315;//1487
    public static final short LoadBadgeInventoryMessageEvent = 3697;//267
    public static final short InitCryptoMessageEvent = 1299;//3200
    public static final short UnignoreUserMessageEvent = 2741;//1766
    public static final short OpenQuestsMessageEvent = 615;//2153
    public static final short GetGroupInfoMessageEvent = 2672;//3697
    public static final short MannequinUpdateDataMessageEvent = 1643;//1710
    public static final short RoomBanUserMessageEvent = 1159;//1730
    public static final short TradeStartMessageEvent = 1645;//2242
    public static final short EnterOneWayDoorMessageEvent = 1341;//1603
    public static final short ChatMessageEvent = 1102;//1169
    public static final short ConsoleInstantChatMessageEvent = 663;//913
    public static final short SubmitHelpTicketMessageEvent = 2547;//2299
    public static final short SaveRoomBackgroundTonerMessageEvent = 1966;//3807
    public static final short LoadItemsInventoryMessageEvent = 387;//3828
    public static final short LandingRefreshPromosMessageEvent = 2368;//2461
    public static final short GetCatalogPageMessageEvent = 519;//1913
    public static final short GroupManageMessageEvent = 1071;//576
    public static final short GiveHanditemMessageEvent = 2641;//3348 -> could be wrong.
    public static final short EnterPrivateRoomMessageEvent = 2041;//2435
    public static final short TradeConfirmMessageEvent = 2445;//1420
    public static final short SaveFloorPlanEditorMessageEvent = 1236;//2989
    public static final short DeleteFriendMessageEvent = 3917;//514
    public static final short TradeUnacceptMessageEvent = 2453;//3493
    public static final short GetSubscriptionDataMessageEvent = 1848;//304
    public static final short ModerationToolUserChatlogMessageEvent = 546;//3884
    public static final short TriggerWallItemMessageEvent = 2379;//1516
    public static final short CatalogPromotionGetRoomsMessageEvent = 1503;//2795
    public static final short InfoRetrieveMessageEvent = 2781;//235
    public static final short RoomSettingsMuteUserMessageEvent = 3211;//3067
    public static final short PickUpPetMessageEvent = 269;//2002
    public static final short PickUpBotMessageEvent = 3284;//1261
    public static final short GiveRespectMessageEvent = 1870;//838
    public static final short SavePostItMessageEvent = 1338;//1842
    public static final short UpdateMoodlightMessageEvent = 2386;//2866
    public static final short GroupUpdateBadgeMessageEvent = 1030;//2999
    public static final short UserSignMessageEvent = 2869;//1040
    public static final short RoomEventUpdateMessageEvent = 3170;//2328
    public static final short GetGroupMembersMessageEvent = 3872;//1373
    public static final short FollowFriendMessageEvent = 3135;//166
    public static final short AcceptGroupRequestMessageEvent = 1610;//3437
    public static final short ModerationToolGetRoomVisitsMessageEvent = 2950;//2384
    public static final short RateRoomMessageEvent = 1130;//1041
    public static final short ReedemExchangeItemMessageEvent = 983;//1143
    public static final short UniqueIDMessageEvent = 830;//558
    public static final short ModerationBanUserMessageEvent = 3196;//2286
    public static final short UserUpdateLookMessageEvent = 1588;//1797
    public static final short CreateRoomMessageEvent = 471;//2746
    public static final short ModerationToolRoomChatlogMessageEvent = 3076;//1963
    public static final short CameraTokenMessageEvent = 3666;//2600
    public static final short ClientVersionMessageEvent = 4000;//4000
    public static final short YouTubeGetPlayerMessageEvent = 1252;//2870
    public static final short DeclineFriendMessageEvent = 333;//549
    public static final short RequestFriendMessageEvent = 1298;//3162
    public static final short RoomGetSettingsInfoMessageEvent = 1398;//227
    public static final short RelationshipsGetMessageEvent = 3398;//3563
    public static final short CanCreateRoomMessageEvent = 649;//746
    public static final short RoomAddFloorItemMessageEvent = 1954;//900
    public static final short OpenGiftMessageEvent = 1140;//1779
    public static final short RoomRemoveAllRightsMessageEvent = 3453;//1180
    public static final short NavigatorGetPopularRoomsMessageEvent = 602;//3375
    public static final short GetFloorPlanDoorMessageEvent = 2714;//2364
    public static final short LoadPetInventoryMessageEvent = 3773;//474
    public static final short AcceptFriendMessageEvent = 1833;//882
    public static final short WiredSaveMatchingMessageEvent = 3552;//376
    public static final short ModerationKickUserMessageEvent = 1320;//2688
    public static final short TriggerDiceRollMessageEvent = 47;//3119
    public static final short SetChatPreferenceMessageEvent = 1777;//1997
    public static final short WardrobeUpdateMessageEvent = 89;//1862
    public static final short RoomUserActionMessageEvent = 2056;//1146
    public static final short RetrieveCitizenshipStatus = 2743;//574
    public static final short PetGetInformationMessageEvent = 2147;//2829
    public static final short LandingLoadWidgetMessageEvent = 3375;//3606
    public static final short OpenPostItMessageEvent = 1133;//2341
    public static final short UserWhisperMessageEvent = 1612;//2944
    public static final short NavigatorGetHighRatedRoomsMessageEvent = 2240;//2906
    public static final short PlaceBotMessageEvent = 3782;//2171
    public static final short GetGroupFurnitureMessageEvent = 1560;//1472
    public static final short ChangeUsernameMessageEvent = 35;//1139
    public static final short SetHomeRoomMessageEvent = 3182;//645
    public static final short GenerateSecretKeyMessageEvent = 2096;//2205
    public static final short IgnoreInvitationsMessageEvent = 930;//3705
    public static final short TradeAddItemOfferMessageEvent = 1271;//2949
    public static final short CreateGuildMessageEvent = 3233;//2856
    public static final short GoToHotelViewMessageEvent = 2382;//1593
    public static final short WiredSaveTriggerMessageEvent = 1942;//9
    public static final short GetRoomRightsListMessageEvent = 3850;//3062
    public static final short ModerationToolRoomToolMessageEvent = 1505;//84
    public static final short StartTypingMessageEvent = 801;//2470
    public static final short RoomGetInfoMessageEvent = 3753;//809
    public static final short RoomSettingsMuteAllMessageEvent = 2686;//626
    public static final short ModerationToolSendUserAlertMessageEvent = 3298;//1496
    public static final short RoomRemoveUserRightsMessageEvent = 2988;//1681
    public static final short PickUpItemMessageEvent = 1198;//1057
    public static final short GetGroupPurchaseBoxMessageEvent = 2834;//2354
    public static final short LoadBotInventoryMessageEvent = 3774;//1802
    public static final short UseHabboWheelMessageEvent = 3114;//2219
    public static final short GroupMakeAdministratorMessageEvent = 3716;//1142
    public static final short LoadProfileByUsernameMessageEvent = 2507;//3590
    public static final short IgnoreUserMessageEvent = 2713;//2528
    public static final short DropHanditemMessageEvent = 3891;//3358
    public static final short NavigatorGetPopularTagsMessageEvent = 3091;//2658
    public static final short FloorItemMoveMessageEvent = 2753;//835
    public static final short SetRelationshipMessageEvent = 1579;//241
    public static final short GetGiftWrappingConfigurationMessageEvent = 665;//1622
    public static final short WiredSaveEffectMessageEvent = 1300;//2124
    public static final short LookAtUserMessageEvent = 1785;//3856
    public static final short ModerationToolSendUserCautionMessageEvent = 1583;//98
    public static final short TradeCancelMessageEvent = 2233;//2367
    public static final short TriggerDiceCloseMessageEvent = 894;//2355
    public static final short DoorbellAnswerMessageEvent = 773;//694
    public static final short ModerationToolPerformRoomActionMessageEvent = 3063;//1105
    public static final short SSOTicketMessageEvent = 516;//493
    public static final short ConsoleSearchFriendsMessageEvent = 1168;//573
    public static final short SaveRoomBrandingMessageEvent = 111;//1016
    public static final short UserWalkMessageEvent = 2115;//1953
    public static final short NavigatorGetFlatCategoriesMessageEvent = 2195;//2954 -> could be wrong, as another composer now sends w/ this, I chose the bottom one
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 2483;//3079
    public static final short OpenHelpToolMessageEvent = 1995;//2871
    public static final short UserUpdateMottoMessageEvent = 2261;//2664
    public static final short TradeAcceptMessageEvent = 2225;//873
    public static final short RoomUnbanUserMessageEvent = 3469;//1673
    public static final short WallItemMoveMessageEvent = 1663;//3712
    public static final short CatalogOfferConfigMessageEvent = 342;//238
    public static final short TradeDiscardMessageEvent = 1016;//1380
    public static final short DeletePostItMessageEvent = 558;//2547
    public static final short GetFloorPlanFurnitureMessageEvent = 1834;//1785
    public static final short GetCatalogIndexMessageEvent = 2072;//3982
    public static final short WardrobeMessageEvent = 1489;//2676
    public static final short YouTubeChoosePlaylistVideoMessageEvent = 598;//2469
    public static final short ActivateMoodlightMessageEvent = 3209;//3934
    public static final short RoomDeleteMessageEvent = 113;//3992
    public static final short DeclineMembershipMessageEvent = 2330;//1728
    public static final short GetCatalogOfferMessageEvent = 1436;//3743
    public static final short TileStackMagicSetHeightMessageEvent = 1479;//3459
    public static final short RoomLoadByDoorbellMessageEvent = 945;//3503
    public static final short NavigatorGetEventsMessageEvent = 1760;//1502
    public static final short NavigatorGetFeaturedRoomsMessageEvent = 3371;//192
    public static final short TriggerMoodlightMessageEvent = 169;//830
    public static final short GiveRightsMessageEvent = 3438;//2657
    public static final short GetRoomBannedUsersMessageEvent = 2141;//648
    public static final short SaveFootballGateOutfitMessageEvent = 906;//1860
    public static final short NavigatorGetMyRoomsMessageEvent = 2996;//3993
    public static final short RoomAddPostItMessageEvent = 1931;//2822
    public static final short RoomSaveSettingsMessageEvent = 1601;//2988
    public static final short GroupUserJoinMessageEvent = 902;//992
    public static final short RoomApplySpaceMessageEvent = 358;//2736
    public static final short RequestLatencyTestMessageEvent = 1873;//3843
    public static final short CheckPetNameMessageEvent = 1545;//1195
    public static final short RoomKickUserMessageEvent = 1518;//738
    public static final short MannequinSaveDataMessageEvent = 1079;//3652
    public static final short LoadUserProfileMessageEvent = 672;//2232
    public static final short BotActionsMessageEvent = 2905;//857
    public static final short GroupUpdateColoursMessageEvent = 1149;//2336
    public static final short GetUserBadgesMessageEvent = 3293;//2180
    public static final short AddToStaffPickedRoomsMessageEvent = 2241;//537
    public static final short ShoutMessageEvent = 1895;//2867
    public static final short RequestLeaveGroupMessageEvent = 3421;//3139
    public static final short PlacePetMessageEvent = 1119;//850
    public static final short StopTypingMessageEvent = 883;//1634
    public static final short GroupUpdateNameMessageEvent = 520;//383
    public static final short GroupFurnitureWidgetMessageEvent = 1525;//1711
    public static final short YouTubeGetPlaylistGetMessageEvent = 545;//289
    public static final short NavigatorSearchRoomByNameMessageEvent = 1455;//161
    public static final short GetSellablePetBreedsMessageEvent = 504;//1812
    public static final short UserDanceMessageEvent = 1956;//334
    public static final short PurchaseFromCatalogMessageEvent = 197;//563
    public static final short HorseMountOnMessageEvent = 2378;//3384
    public static final short TriggerItemMessageEvent = 1630;//163
    public static final short ModerationToolSendRoomAlertMessageEvent = 1644;//2842
    public static final short GroupUpdateSettingsMessageEvent = 199;//2090
    public static final short WiredSaveConditionMessageEvent = 78;//1936
    public static final short BotSpeechListMessageEvent = 377;//1192
    public static final short RemoveGroupAdminMessageEvent = 69;//1458
    public static final short ConsoleInviteFriendsMessageEvent = 1953;//2072
    public static final short SetFavoriteGroupMessageEvent = 3183;//2698
    public static final short RespectPetMessageEvent = 1380;//3143
    public static final short RemoveOwnRightsMessageEvent = 1021;//260
    public static final short SetActivatedBadgesMessageEvent = 836;//2701
    public static final short SaveClientSettingsMessageEvent = 414;//3624
    public static final short SitMessageEvent = 514;
    public static final short KickCommandMessageEvent = 676;

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
