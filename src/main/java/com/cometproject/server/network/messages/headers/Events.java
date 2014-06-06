package com.cometproject.server.network.messages.headers;

import java.lang.reflect.Field;

public class Events {
    public static short CheckReleaseMessageEvent = 4000;
    public static short InitCryptoMessageEvent = 1182;
    public static short GenerateSecretKeyMessageEvent = 20;
    public static short SSOTicketMessageEvent = 496;
    public static short AcceptFriendshipMessageEvent = 3011;
    public static short AcceptTradeMessageEvent = 2011;
    public static short ApplyActionMessageEvent = 548;
    public static short ApplyDanceMessageEvent = 2488;
    public static short ApplySignMessageEvent = 2156;
    public static short BeginTradeMessageEvent = 585;
    public static short CancelTradeMessageEvent = 428;
    public static short CanCreateRoomMessageEvent = 2569;
    public static short CatalogData1MessageEvent = 3219;
    public static short CatalogData2MessageEvent = 3774;
    public static short ChangeFloorItemPositionMessageEvent = 680;
    public static short ChangeFloorItemStateMessageEvent = 945;
    public static short ChangeLooksMessageEvent = 3767;
    public static short ChangeWallItemPositionMessageEvent = 3588;
    public static short UseWallItemMessageEvent = 40;
    public static short ClubStatusMessageEvent = 733;
    public static short ConfirmTradeMessageEvent = 2049;
    public static short CreateNewRoomMessageEvent = 481;
    public static short FeaturedRoomsMessageEvent = 2791;
    public static short FollowRoomInfoMessageEvent = 2547;
    public static short GetCataIndexMessageEvent = 3324;
    public static short GetCataPageMessageEvent = 711;
    public static short GetProfileMessageEvent = 3369;
    public static short InitalizeRoomMessageEvent = 3010;
    public static short LoadHeightmapMessageEvent = 3791;
    public static short LoadRoomInfoMessageEvent = 3802;
    public static short LoadSearchRoomMessageEvent = 3663;
    public static short LookToMessageEvent = 1562;
    public static short OpenDiceMessageEvent = 2690;
    public static short OpenInventoryMessageEvent = 1404;
    public static short OwnRoomsMessageEvent = 3428;
    public static short PickUpItemMessageEvent = 3594;
    public static short PlaceItemMessageEvent = 1205;
    public static short PopularRoomsMessageEvent = 1995;
    public static short PrivateChatMessageEvent = 143;
    public static short PurchaseItemMessageEvent = 2770;
    public static short RequestFriendshipMessageEvent = 2701;
    public static short RunDiceMessageEvent = 2314;
    public static short SearchRoomMessageEvent = 3366;
    public static short SendOfferMessageEvent = 405;
    public static short ShoutMessageEvent = 2507;
    public static short StartTypingMessageEvent = 3411;
    public static short StopTypingMessageEvent = 274;
    public static short TalkMessageEvent = 816;
    public static short UserInformationMessageEvent = 881;
    public static short WalkMessageEvent = 1015;
    public static short WisperMessageEvent = 678;
    public static short AddUserToRoomMessageEvent = 2552;
    public static short ExitRoomMessageEvent = 1547;
    public static short KickUserMessageEvent = 3118;
    public static short GiveRightsMessageEvent = 2729;
    public static short RemoveRightsMessageEvent = 3432;
    public static short RemoveAllRightsMessageEvent = 330;
    public static short SaveWiredTriggerMessageEvent = 795;
    public static short SaveWiredEffectMessageEvent = 81;
    public static short SaveRoomDataMessageEvent = 2618;
    public static short RespectUserMessageEvent = 1001;
    public static short ChangeMottoMessageEvent = 467;
    public static short GetRelationshipsMessageEvent = 3230;
    public static short SetRelationshipMessageEvent = 1574;
    public static short CancelOfferMessageEvent = 1640;
    public static short BotInventoryMessageEvent = 2195;
    public static short PlaceBotMessageEvent = 2107;
    public static short UsersWithRightsMessageEvent = 0; // TODO
    public static short GetBannedUsersMessageEvent = 0;
    public static short ModToolUserInfoMessageEvent = 3083;
    public static short ModToolUserChatlogMessageEvent = 0; // TODO
    public static short ModToolRoomChatlogMessageEvent = 1987; // TODO
    public static short ModToolBanUserMessageEvent = 0; // TODO
    public static short InitHelpToolMessageEvent = 3198;
    public static short HelpTicketMessageEvent = 400;
    public static short BadgeInventoryMessageEvent = 889;
    public static short UserBadgesMessageEvent = 1956;
    public static short BuyGroupMessageEvent = 3291;
    public static short BuyGroupDialogMessageEvent = 2706;
    public static short PetRacesMessageEvent = 276;
    public static short ExchangeItemMessageEvent = 3440;
    public static short ModifyBotMessageEvent = 2742;
    public static short BotConfigMessageEvent = 868;
    public static short RemoveBotMessageEvent = 3624;
    public static short UpdatePapersMessageEvent = 32;
    public static short SaveMannequinMessageEvent = 328; // TODO: check this
    public static short SaveMannequinNameMessageEvent = 740;
    public static short SaveTonerMessageEvent = 2303; // TODO: check this
    public static short ValidatePetNameMessageEvent = 3114;
    public static short PetInventoryMessageEvent = 2830;
    public static short PlacePetMessageEvent = 202;
    public static short DropHandItemMessageEvent = 3542;
    public static short SaveBrandingMessageEvent = 2952;
    public static short AddUserToRoom2MessageEvent = 3474; // TODO: check this
    public static short WearBadgeMessageEvent = 3204;
    public static short AddToStaffPickedRoomsMessageEvent = 2341;
    public static short PurchaseGiftMessageEvent = 2265;
    public static short DeleteFriendsMessageEvent = 997;
    public static short OneWayGateTriggerMessageEvent = 0; // TODO: this
    public static short OpenGiftMessageEvent = 446;
    public static short ModToolRoomInfoMessageEvent = 3425; // TODO: this
    public static short PetInformationMessageEvent = 2604;
    public static short RemovePetMessageEvent = 3961;
    public static short SearchFriendsMessageEvent = 658;
    public static short ChangeWallItemStateMessageEvent = 579;
    public static short InviteFriendsMessageEvent = 436;
    public static short FollowFriendMessageEvent = 3250;
    public static short MuteRoomMessageEvent = 2835;
    public static short WardrobeMessageEvent = 3608;
    public static short SaveWardrobeMessageEvent = 1596;
    public static short DeleteRoomMessageEvent = 1691;
    public static short ChangeHomeRoomMessageEvent = 2922;
    public static short HotelViewItemMessageEvent = 2773;
    public static short RefreshPromoArticlesMessageEvent = 2142;
    public static short AnswerDoorBellMessageEvent = 1093;
    public static short LoadRoomByDoorBellMessageEvent = 1127;
    public static short ChangeUsernameCheckMessageEvent = 3061;
    public static short SaveFloorMessageEvent = 2200;
    public static short LoadPlaylistMessageEvent = 3835;
    public static short PlayVideoMessageEvent = 517;
    public static short RateRoomMessageEvent = 1662;
    public static short UseMoodlightMessageEvent = 3841;
    public static short ToggleMoodlightMessageEvent = 1609;

    public static String valueOfId(int i) {
        Events e = new Events();
        Field[] fields;
        try {
            fields = e.getClass().getDeclaredFields();

            for (Field field : fields) {
                if (field.getInt(field.getName()) == i) {
                    return field.getName();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }
}
