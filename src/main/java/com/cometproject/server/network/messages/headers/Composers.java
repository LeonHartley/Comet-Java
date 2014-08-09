package com.cometproject.server.network.messages.headers;

import java.lang.reflect.Field;

public class Composers {
    public static short InitCryptoMessageComposer = 3016;
    public static short SecretKeyMessageComposer = 847;
    public static short HomeRoomMessageComposer = 2174;
    public static short FuserightsMessageComposer = 1723;
    public static short ActionMessageComposer = 2439;
    public static short IdleStatusMessageComposer = 908;
    public static short AdvancedAlertMessageComposer = 2664;
    public static short AllowancesMessageComposer = 3899;
    public static short BoughtItemMessageComposer = 2408;
    public static short CanCreateRoomMessageComposer = 3353;
    public static short CataIndexMessageComposer = 1319;
    public static short CataPageMessageComposer = 1732;
    public static short CurrenciesMessageComposer = 2446;
    public static short DanceMessageComposer = 3030;
    public static short FloorItemsMessageComposer = 3670;
    public static short FollowBuddyMessageComposer = 3168;
    public static short GetPowerListMessageComposer = 2547;
    public static short GetRoomDataMessageComposer = 329;
    public static short HeightmapMessageComposer = 2749;
    public static short HotelViewMessageComposer = 2919;
    public static short InstantChatMessageComposer = 2109;
    public static short InstantInviteMessageComposer = 258;
    public static short InventoryMessageComposer = 1293;
    public static short LeaveRoomMessageComposer = 3666;
    public static short LoadFriendsMessageComposer = 3332;
    public static short LoadProfileMessageComposer = 3563;
    public static short LoadRightsOnRoomMessageComposer = 2423; // ACCESS_LEVEL
    public static short LoginMessageComposer = 531;
    public static short ModelAndIdMessageComposer = 1667;
    public static short ModToolMessageComposer = 2578;
    public static short MotdMessageComposer = 2291;
    public static short NavigatorFlatListMessageComposer = 2694;
    public static short OfferMessageComposer = 2235;
    public static short OnCreateRoomInfoMessageComposer = 3379;
    public static short PapersMessageComposer = 3642;
    public static short RelativeHeightmapMessageComposer = 3768;
    public static short RemoveFloorItemMessageComposer = 3386;
    public static short RemoveObjectFromInventoryMessageComposer = 2759;
    public static short RemovePowersMessageComposer = 3094;
    public static short GivePowersMessageComposer = 3763;
    public static short RemoveWallItemMessageComposer = 2003;
    public static short RequestFriendshipMessageComposer = 1546;
    public static short RoomDataMessageComposer = 495;
    public static short RoomFullMessageComposer = 1819;
    public static short OwnerRightsMessageComposer = 1001;
    public static short RoomPanelMessageComposer = 2318;
    public static short RoomStatusesMessageComposer = 3977;
    public static short RoomUsersMessageComposer = 411;
    public static short SearchFriendsMessageComposer = 171;
    public static short SendCreditsMessageComposer = 2507;
    public static short SendFloorItemMessageComposer = 1013;
    public static short SendWallItemMessageComposer = 1926;
    public static short SerializeClubMessageComposer = 2179;
    public static short ShopData1MessageComposer = 1141;
    public static short ShopData2MessageComposer = 2642;
    public static short ShoutMessageComposer = 1192;
    public static short TalkMessageComposer = 3372;

    public static short TradeAcceptUpdateMessageComposer = 1142;
    public static short TradeCloseMessageComposer = 7;
    public static short TradeCloseCleanMessageComposer = 1681;
    public static short TradeCompleteMessageComposer = 3017;
    public static short TradeStartMessageComposer = 2078;
    public static short TradeUpdateMessageComposer = 918;

    public static short TypingStatusMessageComposer = 1860;
    public static short UpdateFloorExtraDataMessageComposer = 750;
    public static short UpdateFloorItemMessageComposer = 3169;
    public static short UpdateFriendStateMessageComposer = 161;
    public static short UpdateInfoMessageComposer = 1859;
    public static short UpdateWallItemMessageComposer = 1640;
    public static short UpdateInventoryMessageComposer = 345;
    public static short UserInfoMessageComposer = 2895;
    public static short WallItemsMessageComposer = 1563;
    public static short WelcomeUserMessageComposer = 2933;
    public static short WisperMessageComposer = 1123;
    public static short UnseenItemsMessageComposer = 469;
    public static short ClubMessageComposer = 2677;
    public static short FeaturedRoomsMessageComposer = 2540;//d

    public static short WiredTriggerMessageComposer = 2550;
    public static short WiredEffectMessageComposer = 1800;
    public static short SaveWiredMessageComposer = 1115;
    public static short RoomCategoriesMessageComposer = 1535;
    public static short ConfigureWallAndFloorMessageComposer = 3323;
    public static short GiveRespectMessageComposer = 3836;
    public static short RelationshipsMessageComposer = 3754;
    public static short SendPurchaseAlertMessageComposer = 774;
    public static short BotInventoryMessageComposer = 823;

    public static short ModToolUserInfoMessageComposer = 3872;
    public static short ModToolUserChatlogMessageComposer = 3012;

    public static short InitHelpToolMessageComposer = 3466;
    public static short TicketSentMessageComposer = 3027;
    public static short HelpTicketMessageComposer = 2740;

    public static short BadgeInventoryMessageComposer = 3866;
    public static short FloodFilterMessageComposer = 3728;

    public static short ApplyEffectMessageComposer = 445;
    public static short UserBadgesMessageComposer = 2059;

    public static short GroupPartsMessageComposer = 3696;
    public static short GroupElementsMessageComposer = 2081;

    public static short WardrobeMessageComposer = 3709;

    public static short SlideObjectBundleMessageComposer = 2473;
    public static short BotConfigMessageComposer = 3945;
    public static short PetRacesMessageComposer = 1261;
    public static short ValidatePetNameMessageComposer = 1674;
    public static short PetInventoryMessageComposer = 1074;
    public static short HandItemMessageComposer = 2258;
    public static short PopularTagsMessageComposer = 2484;
    public static short FriendRequestsMessageComposer = 1547;
    public static short ModToolRoomInfoMessageComposer = 2954;
    public static short PetInformationMessageComposer = 2307;

    public static short PingMessageComposer = 884; // Make sure this is right or users may get disconnected lots..
    public static short CatalogPublishedMessageComposer = 1652;

    public static short AchievementPointsMessageComposer = 1942;
    public static short HotelViewItemMessageComposer = 360;
    public static short PromoArticlesMessageComposer = 3897;

    public static short RoomErrorMessageComposer = 1273;
    public static short DoorBellNoAnswerComposer = 371;
    public static short DoorBellRequestComposer = 1738;
    public static short DoorBellAcceptedComposer = 106;

    public static short ChangeUsernameCheckMessageComposer = 2853;

    public static short PlaylistMessageComposer = 2928;
    public static short PlayVideoMessageComposer = 3598;
    public static short RoomRatingMessageComposer = 3533;

    public static short MoodlightMessageComposer = 3496;

    public static short ShowRoomPollMessageComposer = 3762;

    public static short AlertMessageComposer = 656;
    public static short ModToolRoomVisitsMessageComposer = 1586;

    public static short LoadVolumeSettingsMessageComposer = 2440;

    public static short ForwardRoomMessageComposer = 2674;

    public static short NewGroupMessageComposer = 2684;
    public static short GroupDataMessageComposer = 3113;
    public static short GroupInformationMessageComposer = 3435;
    public static short GroupMembersMessageComposer = 129;
    public static short ManageGroupMessageComposer = 3351;

    public static short HotelMaintenanceMessageComposer = 3020;
    public static short GroupBadgesMessageComposer = 1077;


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
