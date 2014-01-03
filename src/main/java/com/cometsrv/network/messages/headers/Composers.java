package com.cometsrv.network.messages.headers;

import java.lang.reflect.Field;

public class Composers {
    public static short InitCryptoMessageComposer = 889;//1099; //889;
    public static short SecretKeyMessageComposer = 3489;//1474; //3489;
    public static short HomeRoomMessageComposer = 854;
    public static short FuserightsMessageComposer = 325;
    public static short ActionMessageComposer = 3984;
    public static short IdleStatusMessageComposer = 3689;
    public static short AlertMessageComposer = 193;
    public static short AdvancedAlertMessageComposer = 2372;
    public static short AllowancesMessageComposer = 709;
    public static short BoughtItemMessageComposer = 2434;
    public static short BuyGroupDialogMessageComposer = 1061;
    public static short CanCreateRoomMessageComposer = 3710;
    public static short CataIndexMessageComposer = 3845;
    public static short CataPageMessageComposer = 3379;
    public static short CitizenshipPanelMessageComposer = 3372;
    public static short CurrenciesMessageComposer = 1271;
    public static short DanceMessageComposer = 732;
    public static short EndTalentPractiseMessageComposer = 840;
    public static short FloorItemsMessageComposer = 2087;
    public static short FollowBuddyMessageComposer = 1959;
    public static short GetPowerListMessageComposer = 2532;
    public static short GetRoomDataMessageComposer = 3367;
    public static short GetUserBadgesMessageComposer = 833;
    public static short GivePowersMessageComposer = 3158;
    public static short HeightmapMessageComposer = 3942;
    public static short HotelViewMessageComposer = 436;
    public static short InstantChatMessageComposer = 2747;
    public static short InstantInviteMessageComposer = 2925;
    public static short InventoryMessageComposer = 3572;
    public static short ItemInHandMessageComposer = 1773;
    public static short LeaveRoomMessageComposer = 3359;
    public static short LoadFriendsMessageComposer = 1800;
    public static short LoadProfileMessageComposer = 3330;
    public static short LoadQuizMessageComposer = 1652;
    public static short LoadRightsOnRoomMessageComposer = 8;
    public static short LoginMessageComposer = 2008;
    public static short ModelAndIdMessageComposer = 206;
    public static short ModToolMessageComposer = 3563;
    public static short MotdMessageComposer = 367;
    public static short NavigatorFlatListMessageComposer = 1019;
    public static short OfferMessageComposer = 366;
    public static short OnCreateRoomInfoMessageComposer = 2897;
    public static short PapersMessageComposer = 2993;
    public static short PendingFriendsMessageComposer = 3942;
    public static short QuitRightsMessageComposer = 3185;
    public static short RelativeHeightmapMessageComposer = 2443;
    public static short RemoveFloorItemMessageComposer = 1891;
    public static short RemoveObjectFromInventoryMessageComposer = 238;
    public static short RemovePowersMessageComposer = 3702;
    public static short RemoveWallItemMessageComposer = 1317;
    public static short RequestFriendshipMessageComposer = 1964;
    public static short RoomChatlogMessageComposer = 3800;
    public static short RoomDataMessageComposer = 2456;
    public static short RoomEventsMessageComposer = 381;
    public static short RoomFullMessageComposer = 1819;
    public static short OwnerRightsMessageComposer = 1812;
    public static short RoomPanelMessageComposer = 1873;
    public static short RoomStatusesMessageComposer = 1387;
    public static short RoomToolMessageComposer = 1180;
    public static short RoomUsersMessageComposer = 610;
    public static short SearchFriendsMessageComposer = 1788;
    public static short SendCreditsMessageComposer = 3412;
    public static short SendFloorItemMessageComposer = 3076;
    public static short SendWallItemMessageComposer = 1382;
    public static short SerializeClubMessageComposer = 319;
    public static short ShopData1MessageComposer = 747;
    public static short ShopData2MessageComposer = 2642;
    public static short ShoutMessageComposer = 2408;
    public static short TalkMessageComposer = 1510;
    public static short TradeAcceptUpdateMessageComposer = 299;
    public static short TradeCloseMessageComposer = 2024;
    public static short TradeCloseCleanMessageComposer = 387;
    public static short TradeCompleteMessageComposer = 2557;
    public static short TradeStartMessageComposer = 2185;
    public static short TradeUpdateMessageComposer = 1386;
    public static short TypingStatusMessageComposer = 3218;
    public static short UpdateFloorExtraDataMessageComposer = 1194;
    public static short UpdateFloorItemMessageComposer = 3510;
    public static short UpdateFriendStateMessageComposer = 1710;
    public static short UpdateInfoMessageComposer = 2859;
    public static short UpdateInventoryMessageComposer = 1507;
    public static short UpdateWallExtraDataMessageComposer = 893;
    public static short UpdateWallItemMessageComposer = 893;
    public static short UserInfoMessageComposer = 178;
    public static short UserToolMessageComposer = 1231;
    public static short WallItemsMessageComposer = 1348;
    public static short WelcomeUserMessageComposer = 2933;
    public static short WisperMessageComposer = 2732;
    public static short UnseenItemsMessageComposer = 469;
    public static short ClubMessageComposer = 2677;
    public static short HTMLColoursMessageComposer = 2049;
    public static short SendAdvGroupInitMessageComposer = 3225;
    public static short GroupModifyInfoMessageComposer = 858;
    public static short FeaturedRoomsMessageComposer = 1969;

    public static short WiredTriggerMessageComposer = 639;
    public static short WiredEffectMessageComposer = 1843;
    public static short SaveWiredMessageComposer = 2992;
    public static short RoomCategoriesMessageComposer = 224;
    public static short ConfigureWallAndFloorMessageComposer = 287;
    public static short GiveRespectMessageComposer = 123;
    public static short RelationshipsMessageComposer = 3570;
    public static short SendPurchaseAlertMessageComposer = 469;
    public static short BotInventoryMessageComposer = 1294;
    public static short PlaceBotMessageComposer = 610;

    public static short ModToolUserInfoMessageComposer = 3282;
    public static short ModToolUserChatlogMessageComposer = 2981;

    public static short InitHelpToolMessageComposer = 3466;
    public static short TicketSentMessageComposer = 3027;
    public static short HelpTicketMessageComposer = 2740;

    public static short BadgeInventoryMessageComposer = 2356;
    public static short FloodFilterMessageComposer = 3410;

    public static short ApplyEffectMessageComposer = 3194;
    public static short UserBadgesMessageComposer = 833;

    public static short GroupPartsMessageComposer = 1061;
    public static short GroupElementsMessageComposer = 29;

    public static String valueOfId(int i) {
        Composers c = new Composers();
        Field[] fields;
        try {
            fields = c.getClass().getDeclaredFields();

            for (Field field : fields) {
                if (field.getInt(field.getName()) == i) {
                    return field.getName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
