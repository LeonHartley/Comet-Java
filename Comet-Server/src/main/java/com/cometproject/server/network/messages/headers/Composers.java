package com.cometproject.server.network.messages.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Composers {
    public static final short RoomRightsLevelMessageComposer = -811;//811
    public static final short AchievementsListMessageComposer = 3160;//2739
    public static final short ModerationToolUserChatlogMessageComposer = 855;//3132
    public static final short CanCreateRoomMessageComposer = 1913;//133
    public static final short UpdateRoomItemMessageComposer = 520;//2593
    public static final short PlayMusicMessageComposer = 1142;//1333
    public static final short AdvancedAlertMessageComposer = 1802;//693
    public static final short ApplyEffectMessageComposer = 3256;//982
    public static final short LogoutMessageComposer = 4000;//4000
    public static final short GroupPurchasePageMessageComposer = -68;//68
    public static final short ModerationToolUserToolMessageComposer = 1472;//3809
    public static final short LoveLockCloseWidgetMessageComposer = 3016;//1221
    public static final short CatalogOfferConfigMessageComposer = 2234;//2347
    public static final short TradeStartMessageComposer = 1459;//3376
    public static final short LoadModerationToolMessageComposer = 434;//3685
    public static final short EnableTradingMessageComposer = 1711;//1561
    public static final short OfficialRoomsMessageComposer = 972;//327
    public static final short UserObjectMessageComposer = 1096;//3693
    public static final short InitCryptoMessageComposer = 2172;//2409
    public static final short BuildersClubMembershipMessageComposer = 2920;//1774
    public static final short ModerationRoomToolMessageComposer = 2985;//1207
    public static final short AddWallItemMessageComposer = 2846;//232
    public static final short PetInfoMessageComposer = 3628;//450
    public static final short RoomForwardMessageComposer = 1000;//376
    public static final short PurchaseOKMessageComposer = 2950;//3405
    public static final short RoomNotificationMessageComposer = 1802;//693
    public static final short WiredConditionMessageComposer = 381;//1134
    public static final short RoomErrorMessageComposer = 1214;//514
    public static final short GroupPurchasePartsMessageComposer = -1009;//1009
    public static final short AchievementProgressMessageComposer = 2106;//393
    public static final short YouTubeLoadPlaylistsMessageComposer = 2298;//589
    public static final short UniqueMachineIDMessageComposer = 3540;//2456
    public static final short GiftUserNotFoundMessageComposer = 1457;//3911
    public static final short RoomConnectionErrorMessageComposer = 869;//621
    public static final short RoomPlayerUnbannedMessageComposer = 3566;//2311
    public static final short CatalogPageMessageComposer = 1018;//3163
    public static final short GroupMembersMessageComposer = -3355;//3355
    public static final short AchievementPointsMessageComposer = 1560;//3485
    public static final short ConsoleSearchFriendMessageComposer = 2431;//2423
    public static final short InitHelpToolMessageComposer = 10;//3470
    public static final short CameraTokenMessageComposer = -3920;//3920
    public static final short RoomBannedListMessageComposer = 3252;//3170
    public static final short SendPerkAllowancesMessageComposer = 1553;//2225
    public static final short RelationshipMessageComposer = 1709;//1822
    public static final short UserProfileMessageComposer = -1538;//1538
    public static final short RoomSpacesMessageComposer = 3139;//3135
    public static final short ChatMessageComposer = 1470;//3467
    public static final short CitizenshipStatusMessageComposer = 265;//1938
    public static final short PingMessageComposer = 3548;//2723
    public static final short HeightMapMessageComposer = 3677;//1517
    public static final short CreditsBalanceMessageComposer = 713;//2549
    public static final short OutOfRoomMessageComposer = 12;//2562
    public static final short WiredRewardMessageComposer = 1650;//1910
    public static final short UpdateFloorItemExtraDataMessageComposer = 3341;//1897
    public static final short WiredTriggerMessageComposer = 3517;//2659
    public static final short RoomEnterErrorMessageComposer = 869;//621
    public static final short GroupRoomMessageComposer = 2973;//1991
    public static final short RespectPetMessageComposer = -0;//0
    public static final short RoomFloorItemsMessageComposer = 2963;//3058
    public static final short LandingPromosMessageComposer = 2682;//1916
    public static final short GetFloorPlanUsedCoordsMessageComposer = 2115;//3125
    public static final short SellablePetBreedsMessageComposer = 1086;//2655
    public static final short UpdateUserDataMessageComposer = 1676;//1796
    public static final short MutedMessageComposer = 3568;//3902
    public static final short TradeUpdateMessageComposer = 364;//449
    public static final short TradeCompletedMessageComposer = 2380;//50
    public static final short ModerationToolTicketChatlogMessageComposer = 3173;//2484
    public static final short ChangeFavouriteGroupMessageComposer = 2300;//2662
    public static final short CatalogPublishMessageComposer = 3484;//3581
    public static final short LoadFriendsMessageComposer = 3114;//2021
    public static final short EnableRoomInfoMessageComposer = 2306;//2921
    public static final short DoorbellMessageComposer = 1246;//1408
    public static final short AddFloorItemMessageComposer = 1706;//3212
    public static final short LoadInventoryMessageComposer = 117;//1227
    public static final short ConsoleChatMessageComposer = 1587;//2349
    public static final short SubscriptionStatusMessageComposer = 2645;//1996
    public static final short EffectsInventoryMessageComposer = 689;//184
    public static final short ModerationToolRoomChatlogMessageComposer = 1486;//1926
    public static final short AchievementRequirementsMessageComposer = 1823;//3883
    public static final short GroupDataEditMessageComposer = 2649;//303
    public static final short FlatCategoriesMessageComposer = 2217;//3629
    public static final short PetRespectNotificationMessageComposer = 2621;//3059
    public static final short DanceStatusMessageComposer = 1701;//3931
    public static final short CatalogOfferMessageComposer = 3126;//1642
    public static final short WhisperMessageComposer = 693;//513
    public static final short SetFloorPlanDoorMessageComposer = 1189;//1930
    public static final short RoomFloorWallLevelsMessageComposer = 3236;//3561
    public static final short UpdateIgnoreStatusMessageComposer = 8;//483
    public static final short CheckPetNameMessageComposer = 498;//3936
    public static final short RoomRatingMessageComposer = 1250;//464
    public static final short AvatarUpdateMessageComposer = 2463;//1579
    public static final short SetRoomUserMessageComposer = 1321;//1541
    public static final short UpdateInventoryMessageComposer = 3757;//912
    public static final short MOTDNotificationMessageComposer = 1551;//2713
    public static final short EnableNotificationsMessageComposer = 820;//495
    public static final short FloodFilterMessageComposer = 1326;//2385
    public static final short ShoutMessageComposer = 727;//1886
    public static final short GroupDataMessageComposer = 293;//1689
    public static final short ItemAnimationMessageComposer = 318;//1366
    public static final short PickUpWallItemMessageComposer = 1796;//333
    public static final short YouAreNotControllerMessageComposer = -3287;//3287
    public static final short UserBadgesMessageComposer = 2848;//3613
    public static final short UserClubRightsMessageComposer = 925;//2660
    public static final short ReceiveBadgeMessageComposer = 3445;//868
    public static final short LoadWardrobeMessageComposer = 2444;//15
    public static final short UpdateRoomWallItemMessageComposer = 3504;//910
    public static final short NavigatorListingsMessageComposer = 135;//477
    public static final short LoadBadgesWidgetMessageComposer = 3294;//1942
    public static final short SongIdMessageComposer = 1931;//2254
    public static final short GiveRespectsMessageComposer = 1169;//26
    public static final short UpdateActivityPointsMessageComposer = 3710;//2623
    public static final short LoadRoomRightsListMessageComposer = 1808;//3463
    public static final short AuthenticationOKMessageComposer = 3151;//2489
    public static final short BotInventoryMessageComposer = 200;//204
    public static final short RoomUserActionMessageComposer = 757;//1402
    public static final short QuestListMessageComposer = 2238;//416
    public static final short BotSpeechListMessageComposer = 1563;//2287
    public static final short TicketSentMessageComposer = 369;//241
    public static final short AchievementUnlockedMessageComposer = 2343;//3412
    public static final short EventCategoriesMessageComposer = 2477;//979
    public static final short ApplyHanditemMessageComposer = 3956;//2394
    public static final short FriendRequestsMessageComposer = 2801;//9
    public static final short GroupFurniturePageMessageComposer = 1574;//1975
    public static final short AlertNotificationMessageComposer = 3598;//531
    public static final short MinimailCountMessageComposer = 2895;//1013
    public static final short RemoveInventoryObjectMessageComposer = 2692;//2197
    public static final short FriendUpdateMessageComposer = 2537;//1820
    public static final short ConsoleSendFriendRequestMessageComposer = 634;//2783
    public static final short CatalogLimitedItemSoldOutMessageComposer = 3373;//3053
    public static final short YouAreControllerMessageComposer = 2599;//742
    public static final short MaintenanceNotificationMessageComposer = 357;//3785
    public static final short TypingStatusMessageComposer = 4;//3495
    public static final short LandingWidgetMessageComposer = 986;//2345
    public static final short TradeErrorMessageComposer = 858;//446
    public static final short RoomUserIdleMessageComposer = 3985;//517
    public static final short BuddyListMessageComposer = 1204;//115
    public static final short ActivityPointsMessageComposer = 74;//379
    public static final short HelpTicketResponseMessageComposer = 991;//1721
    public static final short SecretKeyMessageComposer = 690;//1345
    public static final short PetInventoryMessageComposer = 3675;//2936
    public static final short OpenGiftMessageComposer = -2872;//2872
    public static final short DimmerDataMessageComposer = 995;//3525
    public static final short FloorMapMessageComposer = 2759;//2499
    public static final short LoadPostItMessageComposer = 2654;//966
    public static final short RoomEventMessageComposer = 3781;//2541
    public static final short HomeRoomMessageComposer = 2159;//87
    public static final short TradeConfirmationMessageComposer = 2596;//3557
    public static final short StartQuestMessageComposer = 777;//1083
    public static final short QuestCompletedMessageComposer = 1690;//717
    public static final short LoadVolumeMessageComposer = 426;//2496
    public static final short UserLeftRoomMessageComposer = 184;//2934
    public static final short HelpTicketMessageComposer = 493;//2840
    public static final short PickUpFloorItemMessageComposer = 559;//2983
    public static final short OnCreateRoomInfoMessageComposer = 1037;//737
    public static final short UpdateAvatarAspectMessageComposer = -2831;//2831
    public static final short TradeCloseMessageComposer = 2352;//284
    public static final short CancelQuestMessageComposer = 1645;//2711
    public static final short ConsoleInvitationMessageComposer = 490;//3191
    public static final short RemoveRightsMessageComposer = 3068;//1707
    public static final short FavouriteRoomsMessageComposer = 360;//270
    public static final short SongInventoryMessageComposer = 1416;//1208
    public static final short GiveRoomRightsMessageComposer = 3936;//866
    public static final short GroupFurnitureWidgetMessageComposer = -103;//103
    public static final short DoorbellNoOneMessageComposer = 1095;//2339
    public static final short CatalogPromotionGetRoomsMessageComposer = 2259;//896
    public static final short RoomOwnershipMessageComposer = 1025;//2101
    public static final short RoomUpdateMessageComposer = 2306;//2921
    public static final short SongDataMessageComposer = 1030;//2017
    public static final short NewInventoryObjectMessageComposer = 1773;//1429
    public static final short PopularRoomTagsMessageComposer = 2914;//2084
    public static final short DoorbellOpenedMessageComposer = 2144;//1736
    public static final short PhotoCostMessageComposer = -716;//716
    public static final short TradeAcceptMessageComposer = 3681;//173
    public static final short PlaylistMessageComposer = 2286;//2167
    public static final short UpdateStackMapMessageComposer = 484;//1163
    public static final short LoveLockConfirmedMessageComposer = 3297;//289
    public static final short RoomWallItemsMessageComposer = 2246;//1254
    public static final short RoomGroupMessageComposer = 3472;//823
    public static final short YouTubeLoadVideoMessageComposer = 1790;//1167
    public static final short SaveWiredMessageComposer = 1820;//2529
    public static final short GiftWrappingConfigurationMessageComposer = 3357;//1628
    public static final short CatalogIndexMessageComposer = 36;//2571
    public static final short LoveLockWidgetMessageComposer = 2413;//3979
    public static final short RoomDataMessageComposer = 1563;//1982
    public static final short RoomSettingsDataMessageComposer = 2505;//359
    public static final short WiredEffectMessageComposer = 575;//2946
    public static final short ModerationToolRoomVisitsMessageComposer = 2020;//792
    public static final short NavigatorMetaDataMessageComposer = 3706;

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
