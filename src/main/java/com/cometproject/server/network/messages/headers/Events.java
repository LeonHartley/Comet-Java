package com.cometproject.server.network.messages.headers;

import java.lang.reflect.Field;

public class Events {
	public static short CheckReleaseMessageEvent = 4000;
	public static short InitCryptoMessageEvent = 1266;//642;//1266;
    public static short GenerateSecretKeyMessageEvent = 3987; //1042;//3987;
	public static short SSOTicketMessageEvent = 1461;
	public static short AcceptFriendshipMessageEvent = 3528;
    public static short AcceptTradeMessageEvent = 3202;
    public static short ApplyActionMessageEvent = 1843;
    public static short ApplyDanceMessageEvent = 866;
    public static short ApplySignMessageEvent = 3482;
    public static short BeginTradeMessageEvent = 1074;
    public static short CancelTradeMessageEvent = 2715;
    public static short CanCreateRoomMessageEvent = 2274;
    public static short CatalogData1MessageEvent = 2469;
    public static short CatalogData2MessageEvent = 907;
    public static short ChangeFloorItemPositionMessageEvent = 1757;
	public static short ChangeFloorItemStateMessageEvent = 3820;
	public static short ChangeLooksMessageEvent = 885;
	public static short ChangeWallItemPositionMessageEvent = 1897;
    public static short UseWallItemMessageEvent = 2882;
	public static short ClubStatusMessageEvent = 2999;
	public static short ConfirmTradeMessageEvent = 1717;
	public static short CreateNewRoomMessageEvent = 9;
	public static short FeaturedRoomsMessageEvent = 3754;
	public static short FollowRoomInfoMessageEvent = 634;
	public static short GetCataIndexMessageEvent = 3071;
	public static short GetCataPageMessageEvent = 1777;
	public static short GetProfileMessageEvent = 3637;
	public static short InitalizeRoomMessageEvent = 2807;
	public static short LoadHeightmapMessageEvent = 2327;
	public static short LoadRoomInfoMessageEvent = 1102;
	public static short LoadSearchRoomMessageEvent = 3733;
	public static short LookToMessageEvent = 1653;
	public static short OpenDiceMessageEvent = 3101;
	public static short OpenInventoryMessageEvent = 2297;
	public static short OwnRoomsMessageEvent = 1907;
	public static short PickUpItemMessageEvent = 1019;
	public static short PlaceItemMessageEvent = 2696;
	public static short PopularRoomsMessageEvent = 512;
	public static short PrivateChatMessageEvent = 1582;
	public static short PurchaseItemMessageEvent = 1416;
	public static short RequestFriendshipMessageEvent = 202;
	public static short RunDiceMessageEvent = 22;
	public static short SearchRoomMessageEvent = 47;
	public static short SendOfferMessageEvent = 940;
	public static short ShoutMessageEvent = 1454;
	public static short StartTypingMessageEvent = 678;
	public static short StopTypingMessageEvent = 1236;
	public static short TalkMessageEvent = 2275;
	public static short UserInformationMessageEvent = 1396;
	public static short WalkMessageEvent = 2600;
	public static short WisperMessageEvent = 3802;
	public static short AddUserToRoomMessageEvent = 383;
	public static short ExitRoomMessageEvent = 549;
	public static short KickUserMessageEvent = 3345;
	public static short GiveRightsMessageEvent = 859;
	public static short RemoveAllRightsMessageEvent = 3020;
	public static short HabboClubPackagesMessageEvent = 947;
    public static short LoadCategoriesMessageEvent = 1272;
    public static short SaveWiredTriggerMessageEvent = 3501;
    public static short SaveWiredEffectMessageEvent = 3930;
    public static short SaveRoomDataMessageEvent = 3036;
    public static short RespectUserMessageEvent = 430;
    public static short ChangeMottoMessageEvent = 2874;
    public static short GetRelationshipsMessageEvent = 2233;
    public static short SetRelationshipMessageEvent = 1590;
    public static short CancelOfferMessageEvent = 2147;
    public static short BotInventoryMessageEvent = 1280;
    public static short PlaceBotMessageEvent = 2343;
    public static short UsersWithRightsMessageEvent = 308;
    public static short GetBannedUsersMessageEvent = 1450;
    public static short ModToolUserInfoMessageEvent = 2676;
    public static short ModToolUserChatlogMessageEvent = 1753;
    public static short ModToolRoomChatlogMessageEvent = 43;
    public static short ModToolBanUserMessageEvent = 3535;
    public static short InitHelpToolMessageEvent = 883;
    public static short HelpTicketMessageEvent = 1774;
    public static short BadgeInventoryMessageEvent = 2818;
    public static short UseMoodlightMessageEvent = 3264;
    public static short UserBadgesMessageEvent = 1256;
    public static short BuyGroupMessageEvent = 660;
    public static short BuyGroupDialogMessageEvent = 3253;
    public static short PetRacesMessageEvent = 3675;
    public static short ExchangeItemMessageEvent = 3582;
    public static short ModifyBotMessageEvent = 616;
    public static short BotConfigMessageEvent = 2841;
    public static short RemoveBotMessageEvent = 2788;
    public static short UpdatePapersMessageEvent = 2421;
    public static short SaveMannequinMessageEvent = 1311;
    public static short SaveMannequinNameMessageEvent = 2451;

    public static short ManageGroupMessageEvent = 454;
    public static short AddUserToRoom2MessageEvent = 3317;
    public static short RemoveRightsMessageEvent = 572;
    public static short DeleteRoomMessageEvent = 2830;
    public static short StartQuizMessageEvent = 1337;//2048;
    public static short UnacceptTradeMessageEvent = 478;
    public static short PurchaseGiftMessageEvent = 553;
    public static short ModRoomChatMessageEvent = 2620;
    public static short ModRoomInfoMessageEvent = 1847;
    public static short ModUserInfoMessageEvent = 3530;
    public static short SearchFriendsMessageEvent = 1903;
    public static short ChangeWallItemStateMessageEvent = 1441;
    public static short CloseDiceMessageEvent = 2557;
    public static short DenyFriendshipMessageEvent = 1752;
    public static short ExchangeCoinMessageEvent = 3266;
    public static short FollowFriendMessageEvent = 1177;
    public static short GroupInfoMessageEvent = 539;
    public static short InviteFriendsMessageEvent = 2849;
    public static short LogInMessageEvent = 1461;
    public static short AnswerQuizMessageEvent = 2048;

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
