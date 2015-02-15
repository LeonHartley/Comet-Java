package com.cometproject.server.network.messages.headers;

import javolution.util.FastMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;


public class Composers {
    public static final short RoomRightsLevelMessageComposer = 512;//2004
    public static final short ModerationToolUserChatlogMessageComposer = 1054;//3582
    public static final short CanCreateRoomMessageComposer = 3877;//2737
    public static final short UpdateRoomItemMessageComposer = 13;//1459
    public static final short AdvancedAlertMessageComposer = 351;//2273
    public static final short ApplyEffectMessageComposer = 1083;//3074
    public static final short LogoutMessageComposer = 4000;//4000
    public static final short GroupPurchasePageMessageComposer = 3194;//2768
    public static final short UpdateUserStatusMessageComposer = 167;//3128
    public static final short ModerationToolUserToolMessageComposer = 2070;//433
    public static final short EnableTradingMessageComposer = 2654;//1086
    public static final short TradeStartMessageComposer = 3541;//2538
    public static final short OfficialRoomsMessageComposer = 2056;//1122
    public static final short UserObjectMessageComposer = 426;//532
    public static final short InitCryptoMessageComposer = 3084;//3523
    public static final short LoadModerationToolMessageComposer = 835;//2851
    public static final short BuildersClubMembershipMessageComposer = 3590;//296
    public static final short ModerationRoomToolMessageComposer = 2076;//2067
    public static final short AddWallItemMessageComposer = 2970;//2400
    public static final short PetInfoMessageComposer = 1219;//830
    public static final short RoomForwardMessageComposer = 1345;//3581
    public static final short PurchaseOKMessageComposer = 1810;//1475
    public static final short RoomNotificationMessageComposer = 351;//2273
    public static final short WiredConditionMessageComposer = 239;//1612
    public static final short RoomErrorMessageComposer = 205;//487
    public static final short GroupPurchasePartsMessageComposer = 3024;//3161
    public static final short YouTubeLoadPlaylistsMessageComposer = 3555;//1225
    public static final short UniqueMachineIDMessageComposer = 550;//1790
    public static final short GiftUserNotFoundMessageComposer = 418;//2517
    public static final short RoomConnectionErrorMessageComposer = 3958;//3820
    public static final short RoomPlayerUnbannedMessageComposer = 1781;//2215
    public static final short CatalogPageMessageComposer = 3563;//415
    public static final short GroupMembersMessageComposer = 2933;//3717
    public static final short AchievementPointsMessageComposer = 1104;//3110
    public static final short ConsoleSearchFriendMessageComposer = 251;//909
    public static final short InitHelpToolMessageComposer = 302;//1140
    public static final short CameraTokenMessageComposer = 2917;//2137 -> could be wrong, 2 new composers there, so I chose the first one.
    public static final short RoomBannedListMessageComposer = 1182;//3997
    public static final short SendPerkAllowancesMessageComposer = 93;//2903
    public static final short RelationshipMessageComposer = 1217;//1485
    public static final short UserProfileMessageComposer = 691;//3031
    public static final short CatalogueOfferConfigMessageComposer = 1811;//1732
    public static final short RoomSpacesMessageComposer = 744;//2251
    public static final short ChatMessageComposer = 336;//803
    public static final short CitizenshipStatusMessageComposer = 833;//1246
    public static final short PingMessageComposer = 164;//3972
    public static final short HeightMapMessageComposer = 817;//1454
    public static final short CreditsBalanceMessageComposer = 2050;//2835
    public static final short OutOfRoomMessageComposer = 3448;//913
    public static final short WiredRewardMessageComposer = 3404;//2640
    public static final short UpdateFloorItemExtraDataMessageComposer = 1650;//931
    public static final short WiredTriggerMessageComposer = 493;//3016
    public static final short RoomEnterErrorMessageComposer = 3958;//3820
    public static final short GroupRoomMessageComposer = 2334;//3646
    public static final short RespectPetMessageComposer = -0;//0 -> I assume this is intentionally 0?
    public static final short RoomFloorItemsMessageComposer = 3148;//529
    public static final short LandingPromosMessageComposer = 3928;//3695
    public static final short GetFloorPlanUsedCoordsMessageComposer = 1978;//3845
    public static final short SellablePetBreedsMessageComposer = 3252;//3770
    public static final short UpdateUserDataMessageComposer = 1321;//3464
    public static final short MutedMessageComposer = 1229;//3871
    public static final short TradeUpdateMessageComposer = 1507;//1165
    public static final short TradeCompletedMessageComposer = 1362;//3757
    public static final short ChangeFavouriteGroupMessageComposer = 2978;//492
    public static final short LoadFriendsMessageComposer = 3382;//168
    public static final short EnableRoomInfoMessageComposer = 2157;//1919
    public static final short DoorbellMessageComposer = 472;//3104
    public static final short AddFloorItemMessageComposer = 1328;//3183
    public static final short LoadInventoryMessageComposer = 1517;//37
    public static final short ConsoleChatMessageComposer = 3249;//793
    public static final short SubscriptionStatusMessageComposer = 2807;//518
    public static final short EffectsInventoryMessageComposer = 3648;//2687
    public static final short ModerationToolRoomChatlogMessageComposer = 849;//3827
    public static final short GroupDataEditMessageComposer = 2317;//1572
    public static final short FlatCategoriesMessageComposer = 120;//1531
    public static final short PetRespectNotificationMessageComposer = 3632;//443
    public static final short DanceStatusMessageComposer = 671;//1362
    public static final short CatalogOfferMessageComposer = 994;//3381
    public static final short WhisperMessageComposer = 2702;//1430
    public static final short SetFloorPlanDoorMessageComposer = 3911;//1381
    public static final short RoomFloorWallLevelsMessageComposer = 2033;//103
    public static final short UpdateIgnoreStatusMessageComposer = 3916;//1996
    public static final short CheckPetNameMessageComposer = 1252;//1073
    public static final short RoomRatingMessageComposer = 3998;//1118
    public static final short SetRoomUserMessageComposer = 3761;//3753
    public static final short UpdateInventoryMessageComposer = 2316;//3351
    public static final short MOTDNotificationMessageComposer = 2855;//2534
    public static final short PollQuestionsMessageComposer = -2525;//2525 -> Couldn't find it in the SWF, even w/ hex.
    public static final short EnableNotificationsMessageComposer = 2077;//114
    public static final short FloodFilterMessageComposer = 25;//3180
    public static final short ShoutMessageComposer = 1526;//1419
    public static final short GroupDataMessageComposer = 3372;//2527
    public static final short ItemAnimationMessageComposer = 3165;//215
    public static final short PickUpWallItemMessageComposer = 2428;//1596
    public static final short UserBadgesMessageComposer = 3144;//2711
    public static final short UserClubRightsMessageComposer = 639;//757
    public static final short LoadWardrobeMessageComposer = 591;//1618
    public static final short UpdateRoomWallItemMessageComposer = 2370;//853
    public static final short NavigatorListingsMessageComposer = 3127;//2825
    public static final short LoadBadgesWidgetMessageComposer = 153;//2569
    public static final short GiveRespectsMessageComposer = 2106;//1447
    public static final short LoadRoomRightsListMessageComposer = 1182;//798
    public static final short AuthenticationOKMessageComposer = 1988;//97
    public static final short BotInventoryMessageComposer = 428;//1346
    public static final short RoomUserActionMessageComposer = 3529;//3685
    public static final short QuestListMessageComposer = 1961;//3562
    public static final short BotSpeechListMessageComposer = 705;//3323
    public static final short TicketSentMessageComposer = 1205;//1082
    public static final short ApplyHanditemMessageComposer = 1596;//314
    public static final short FriendRequestsMessageComposer = 1011;//3389
    public static final short GroupFurniturePageMessageComposer = 3713;//1960
    public static final short AlertNotificationMessageComposer = 2503;//1688
    public static final short LandingWidgetMessageComposer = 3048;//2684
    public static final short TradeErrorMessageComposer = 3558;//2483
    public static final short MinimailCountMessageComposer = 2030;//1714
    public static final short RoomUserIdleMessageComposer = 800;//2858 -> Guessed this from my headers.
    public static final short RemoveInventoryObjectMessageComposer = 1737;//2369
    public static final short ActivityPointsMessageComposer = 1566;//343
    public static final short FriendUpdateMessageComposer = 397;//3335
    public static final short ConsoleSendFriendRequestMessageComposer = 332;//1932
    public static final short CatalogLimitedItemSoldOutMessageComposer = 1645;//2668
    public static final short MaintenanceNotificationMessageComposer = 2704;//431
    public static final short TypingStatusMessageComposer = 1059;//989 -> Guessed this from my headers.
    public static final short SecretKeyMessageComposer = 1297;//893
    public static final short PetInventoryMessageComposer = 1091;//649
    public static final short OpenGiftMessageComposer = 1224;//2272
    public static final short DimmerDataMessageComposer = 3967;//3457
    public static final short FloorMapMessageComposer = 3872;//1768
    public static final short LoadPostItMessageComposer = 1060;//3524
    public static final short RoomEventMessageComposer = 4;//241
    public static final short HomeRoomMessageComposer = 1367;//315
    public static final short TradeConfirmationMessageComposer = 3991;//3063
    public static final short LoadVolumeMessageComposer = 2551;//2967
    public static final short UserLeftRoomMessageComposer = 2441;//658
    public static final short PickUpFloorItemMessageComposer = 1492;//1899
    public static final short OnCreateRoomInfoMessageComposer = 3217;//1361
    public static final short UpdateAvatarAspectMessageComposer = 1150;//2551
    public static final short TradeCloseMessageComposer = 3987;//3670
    public static final short ConsoleInvitationMessageComposer = 677;//1551
    public static final short RemoveRightsMessageComposer = 3134;//867
    public static final short FavouriteRoomsMessageComposer = 3905;//1974
    public static final short GiveRoomRightsMessageComposer = 1781;//6
    public static final short GroupFurnitureWidgetMessageComposer = 1789;//1674
    public static final short DoorbellNoOneMessageComposer = 1042;//2149
    public static final short CatalogPromotionGetRoomsMessageComposer = 2425;//2555
    public static final short RoomOwnershipMessageComposer = 1951;//2101
    public static final short RoomUpdateMessageComposer = 2157;//1919
    public static final short NewInventoryObjectMessageComposer = 787;//1006
    public static final short PublishShopMessageComposer = 1843;//1639
    public static final short PopularRoomTagsMessageComposer = 2438;//3274
    public static final short DoorbellOpenedMessageComposer = 1251;//2590
    public static final short TradeAcceptMessageComposer = 1442;//1827
    public static final short RoomWallItemsMessageComposer = 3642;//1276
    public static final short RoomGroupMessageComposer = 2341;//1907
    public static final short YouTubeLoadVideoMessageComposer = 3823;//3247
    public static final short SaveWiredMessageComposer = 2339;//3283
    public static final short HasOwnerRightsMessageComposer = 342;//1204
    public static final short GiftWrappingConfigurationMessageComposer = 3554;//1966
    public static final short CatalogIndexMessageComposer = 1534;//151
    public static final short RoomDataMessageComposer = 1287;//1231
    public static final short RoomSettingsDataMessageComposer = 1999;//1113
    public static final short WiredEffectMessageComposer = 3893;//178
    public static final short ModerationToolRoomVisitsMessageComposer = 408;//1587
    public static final short BuddyListMessageComposer = 2472; // new
    public static final short EventCategoriesMessageComposer = 1686;


    private static Map<Short, String> composerPacketNames = new FastMap<>();

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
