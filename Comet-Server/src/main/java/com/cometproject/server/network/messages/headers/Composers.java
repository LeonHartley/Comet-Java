package com.cometproject.server.network.messages.headers;



import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Composers {
    public static final short InitCryptoMessageComposer = 2409;//3084
    public static final short SecretKeyMessageComposer = 1345;//1297
    public static final short UniqueMachineIDMessageComposer = 2456;//550
    public static final short RoomRightsLevelMessageComposer = 811;//512
    public static final short CanCreateRoomMessageComposer = 133;//3877
    public static final short ModerationToolUserChatlogMessageComposer = 3132;//1054
    public static final short UpdateRoomItemMessageComposer = 2593;//13
    public static final short AdvancedAlertMessageComposer = 693;//351
    public static final short ApplyEffectMessageComposer = 982;//1083
    public static final short GroupPurchasePageMessageComposer = 68;//3194
    public static final short LogoutMessageComposer = 4000;//4000
    public static final short AvatarUpdateMessageComposer = 1579;//167
    public static final short ModerationToolUserToolMessageComposer = 3809;//2070
    public static final short EnableTradingMessageComposer = 1561;//2654
    public static final short UserObjectMessageComposer = 3693;//426
    public static final short OfficialRoomsMessageComposer = 327;//2056
    public static final short TradeStartMessageComposer = 3376;//3541
    public static final short LoadModerationToolMessageComposer = 3685;//835
    public static final short BuildersClubMembershipMessageComposer = 1774;//3590
    public static final short AddWallItemMessageComposer = 232;//2970
    public static final short ModerationRoomToolMessageComposer = 1207;//2076
    public static final short PetInfoMessageComposer = 450;//1219
    public static final short RoomForwardMessageComposer = 376;//1345
    public static final short PurchaseOKMessageComposer = 3405;//1810
    public static final short RoomErrorMessageComposer = 514;//205
    public static final short WiredConditionMessageComposer = 1134;//239
    public static final short RoomNotificationMessageComposer = 693;//351
    public static final short GroupPurchasePartsMessageComposer = 1009;//3024
    public static final short YouTubeLoadPlaylistsMessageComposer = 589;//3555
    public static final short GiftUserNotFoundMessageComposer = 3911;//418
    public static final short RoomConnectionErrorMessageComposer = 621;//3958
    public static final short RoomPlayerUnbannedMessageComposer = 2311;//1781
    public static final short CatalogPageMessageComposer = 3163;//3563
    public static final short GroupMembersMessageComposer = 3355;//2933
    public static final short ConsoleSearchFriendMessageComposer = 2423;//251
    public static final short AchievementPointsMessageComposer = 3485;//1104
    public static final short SendPerkAllowancesMessageComposer = 2225;//93
    public static final short RoomBannedListMessageComposer = 3170;//1844
    public static final short CameraTokenMessageComposer = 3920;//2917
    public static final short InitHelpToolMessageComposer = 3470;//302
    public static final short CatalogOfferConfigMessageComposer = 2347;//1811
    public static final short UserProfileMessageComposer = 1538;//691
    public static final short RelationshipMessageComposer = 1822;//1217
    public static final short RoomSpacesMessageComposer = 3135;//744
    public static final short ChatMessageComposer = 3467;//336
    public static final short CitizenshipStatusMessageComposer = 1938;//833
    public static final short PingMessageComposer = 2723;//164
    public static final short HeightMapMessageComposer = 1517;//817
    public static final short CreditsBalanceMessageComposer = 2549;//2050
    public static final short OutOfRoomMessageComposer = 2562;//3448
    public static final short WiredRewardMessageComposer = 1910;//3404
    public static final short UpdateFloorItemExtraDataMessageComposer = 1897;//1650
    public static final short RoomEnterErrorMessageComposer = 621;//3958
    public static final short WiredTriggerMessageComposer = 2659;//493
    public static final short GroupRoomMessageComposer = 1991;//2334
    public static final short RespectPetMessageComposer = -0;//0
    public static final short RoomFloorItemsMessageComposer = 3058;//3148
    public static final short LandingPromosMessageComposer = 1916;//3928
    public static final short SellablePetBreedsMessageComposer = 2655;//3252
    public static final short GetFloorPlanUsedCoordsMessageComposer = 3125;//1978
    public static final short UpdateUserDataMessageComposer = 1796;//1321
    public static final short MutedMessageComposer = 3902;//1229
    public static final short TradeCompletedMessageComposer = 50;//1362
    public static final short TradeUpdateMessageComposer = 449;//1507
    public static final short ChangeFavouriteGroupMessageComposer = 2662;//2978
    public static final short LoadFriendsMessageComposer = 2021;//3382
    public static final short EnableRoomInfoMessageComposer = 2921;//2157
    public static final short DoorbellMessageComposer = 1408;//472
    public static final short AddFloorItemMessageComposer = 3212;//1328
    public static final short LoadInventoryMessageComposer = 1227;//1517
    public static final short ConsoleChatMessageComposer = 2349;//3249
    public static final short EffectsInventoryMessageComposer = 184;//3648
    public static final short SubscriptionStatusMessageComposer = 1996;//2807
    public static final short ModerationToolRoomChatlogMessageComposer = 1926;//849
    public static final short GroupDataEditMessageComposer = 303;//2317
    public static final short FlatCategoriesMessageComposer = 3629;//120
    public static final short DanceStatusMessageComposer = 3931;//671
    public static final short PetRespectNotificationMessageComposer = 3059;//3632
    public static final short CatalogOfferMessageComposer = 1642;//994
    public static final short WhisperMessageComposer = 513;//2702
    public static final short RoomFloorWallLevelsMessageComposer = 3561;//2033
    public static final short SetFloorPlanDoorMessageComposer = 1930;//3911
    public static final short UpdateIgnoreStatusMessageComposer = 483;//3916
    public static final short CheckPetNameMessageComposer = 3936;//1252
    public static final short RoomRatingMessageComposer = 464;//3998
    public static final short SetRoomUserMessageComposer = 1541;//3761
    public static final short UpdateInventoryMessageComposer = 912;//2316
    public static final short MOTDNotificationMessageComposer = 2713;//2855
    public static final short EnableNotificationsMessageComposer = 495;//2077
    public static final short FloodFilterMessageComposer = 2385;//25
    public static final short ShoutMessageComposer = 1886;//1526
    public static final short GroupDataMessageComposer = 1689;//3372
    public static final short ItemAnimationMessageComposer = 1366;//3165
    public static final short PickUpWallItemMessageComposer = 333;//2428
    public static final short UserBadgesMessageComposer = 3613;//3144
    public static final short UserClubRightsMessageComposer = 2660;//639
    public static final short UpdateRoomWallItemMessageComposer = 910;//2370
    public static final short LoadWardrobeMessageComposer = 15;//591
    public static final short NavigatorListingsMessageComposer = 477;//3127
    public static final short LoadBadgesWidgetMessageComposer = 1942;//153
    public static final short GiveRespectsMessageComposer = 26;//2106
    public static final short LoadRoomRightsListMessageComposer = 3463;//1182
    public static final short AuthenticationOKMessageComposer = 2489;//1988
    public static final short RoomUserActionMessageComposer = 1402;//3529
    public static final short BotInventoryMessageComposer = 204;//428
    public static final short BotSpeechListMessageComposer = 2287;//705
    public static final short QuestListMessageComposer = 416;//1961
    public static final short TicketSentMessageComposer = 241;//1205
    public static final short EventCategoriesMessageComposer = 979;//1686
    public static final short ApplyHanditemMessageComposer = 2394;//1596
    public static final short FriendRequestsMessageComposer = 9;//1011
    public static final short GroupFurniturePageMessageComposer = 1975;//3713
    public static final short AlertNotificationMessageComposer = 531;//2000;//2503
    public static final short MinimailCountMessageComposer = 1013;//2030
    public static final short RemoveInventoryObjectMessageComposer = 2197;//1737
    public static final short CatalogLimitedItemSoldOutMessageComposer = 3053;//1645
    public static final short ConsoleSendFriendRequestMessageComposer = 2783;//332
    public static final short FriendUpdateMessageComposer = 1820;//397
    public static final short TypingStatusMessageComposer = 3495;//1059
    public static final short MaintenanceNotificationMessageComposer = 3785;//2704
    public static final short LandingWidgetMessageComposer = 2345;//3048
    public static final short TradeErrorMessageComposer = 446;//3558
    public static final short RoomUserIdleMessageComposer = 517;//800
    public static final short BuddyListMessageComposer = 115;//2472
    public static final short ActivityPointsMessageComposer = 379;//1566
    public static final short PetInventoryMessageComposer = 2936;//1091
    public static final short OpenGiftMessageComposer = 2872;//1224
    public static final short FloorMapMessageComposer = 2499;//3872
    public static final short DimmerDataMessageComposer = 3525;//3967
    public static final short RoomEventMessageComposer = 2541;//4
    public static final short LoadPostItMessageComposer = 966;//1060
    public static final short HomeRoomMessageComposer = 87;//1367
    public static final short TradeConfirmationMessageComposer = 3557;//3991
    public static final short LoadVolumeMessageComposer = 2496;//2551
    public static final short UserLeftRoomMessageComposer = 2934;//2441
    public static final short PickUpFloorItemMessageComposer = 2983;//1492
    public static final short OnCreateRoomInfoMessageComposer = 737;//3217
    public static final short UpdateAvatarAspectMessageComposer = 2831;//1150
    public static final short TradeCloseMessageComposer = 284;//3987
    public static final short ConsoleInvitationMessageComposer = 3191;//677
    public static final short RemoveRightsMessageComposer = 1707;//3134
    public static final short FavouriteRoomsMessageComposer = 270;//3905
    public static final short GiveRoomRightsMessageComposer = 866;//1781
    public static final short GroupFurnitureWidgetMessageComposer = 103;//1789
    public static final short CatalogPromotionGetRoomsMessageComposer = 896;//2425
    public static final short DoorbellNoOneMessageComposer = 2339;//1042
    public static final short RoomUpdateMessageComposer = 2921;//2157
    public static final short RoomOwnershipMessageComposer = 2101;//1951
    public static final short NewInventoryObjectMessageComposer = 1429;//787
    public static final short CatalogPublishMessageComposer = 3581;//1843
    public static final short PopularRoomTagsMessageComposer = 2084;//2438
    public static final short DoorbellOpenedMessageComposer = 1736;//1251
    public static final short TradeAcceptMessageComposer = 173;//1442
    public static final short RoomWallItemsMessageComposer = 1254;//3642
    public static final short RoomGroupMessageComposer = 823;//2341
    public static final short YouTubeLoadVideoMessageComposer = 1167;//3823
    public static final short SaveWiredMessageComposer = 2529;//2339
    public static final short YouAreControllerMessageComposer = 742;//342
    public static final short CatalogIndexMessageComposer = 2571;//1534
    public static final short GiftWrappingConfigurationMessageComposer = 1628;//3554
    public static final short RoomDataMessageComposer = 1982;//1287
    public static final short RoomSettingsDataMessageComposer = 359;//1999
    public static final short ModerationToolRoomVisitsMessageComposer = 792;//408
    public static final short WiredEffectMessageComposer = 2946;//3893
    public static final short YouAreNotControllerMessageComposer = 3287;//new
    public static final short PhotoCostMessageComposer = 716;//new
    public static final short LoveLockWidgetMessageComposer = 3979;//new
    public static final short LoveLockConfirmedMessageComposer = 289;//new
    public static final short LoveLockCloseWidgetMessageComposer = 1221;//new
    public static final short HelpTicketMessageComposer = 2840;
    public static final short ModerationToolTicketChatlogMessageComposer = 2484;
    public static final short HelpTicketResponseMessageComposer = 1721;
    public static final short UpdateStackMapMessageComposer = 1163;
    public static final short SongInventoryMessageComposer = 1208;
    public static final short SongIdMessageComposer = 2254;
    public static final short SongDataMessageComposer = 2017;
    public static final short PlaylistMessageComposer = 2167;
    public static final short PlayMusicMessageComposer = 1333;
    public static final short StartQuestMessageComposer = 1083;
    public static final short CancelQuestMessageComposer = 2711;
    public static final short QuestCompletedMessageComposer = 717;
    public static final short UpdateActivityPointsMessageComposer = 2623;

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
