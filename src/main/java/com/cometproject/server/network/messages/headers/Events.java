package com.cometproject.server.network.messages.headers;

import javolution.util.FastMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;


public class Events {
    public static final short ClientVersionMessageEvent = 4000;
    public static final short InitCryptoMessageEvent = 3200;
    public static final short GenerateSecretKeyMessageEvent = 2205;
    public static final short UniqueIDMessageEvent = 558;
    public static final short SSOTicketMessageEvent = 493;
    public static final short InfoRetrieveMessageEvent = 235;
    public static final short GetSubscriptionDataMessageEvent = 304;
    public static final short LandingLoadWidgetMessageEvent = 3606;
    public static final short LandingRefreshPromosMessageEvent = 2461;
    public static final short RequestLatencyTestMessageEvent = 3843;
    public static final short SaveClientSettingsMessageEvent = 3624;
    public static final short RetrieveCitizenshipStatus = 574;
    public static final short NavigatorGetEventsMessageEvent = 1502;
    public static final short NavigatorGetFeaturedRoomsMessageEvent = 192;
    public static final short NavigatorGetHighRatedRoomsMessageEvent = 2906;
    public static final short NavigatorGetMyRoomsMessageEvent = 3993;
    public static final short NavigatorGetPopularRoomsMessageEvent = 3375;
    public static final short NavigatorGetPopularTagsMessageEvent = 2658;
    public static final short NavigatorSearchRoomByNameMessageEvent = 161;
    public static final short NavigatorGetFlatCategoriesMessageEvent = 2954;
    public static final short CanCreateRoomMessageEvent = 746;
    public static final short CreateRoomMessageEvent = 2746;
    public static final short SetHomeRoomMessageEvent = 645;
    public static final short GoToHotelViewMessageEvent = 1593;
    public static final short GetCatalogIndexMessageEvent = 3982;
    public static final short GetCatalogPageMessageEvent = 1913;
    public static final short GetCatalogOfferMessageEvent = 3743;
    public static final short CatalogOfferConfigMessageEvent = 238;
    public static final short PurchaseFromCatalogMessageEvent = 563;
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 3079;
    public static final short GetSellablePetBreedsMessageEvent = 1812;
    public static final short GetGiftWrappingConfigurationMessageEvent = 1622;
    public static final short CatalogPromotionGetRoomsMessageEvent = 2795;
    public static final short PromoteRoomMessageEvent = 1487;
    public static final short CheckPetNameMessageEvent = 1195;
    public static final short EnterPrivateRoomMessageEvent = 2435;
    public static final short RoomGetInfoMessageEvent = 809;
    public static final short RoomUserActionMessageEvent = 1146;
    public static final short ChatMessageEvent = 1169;
    public static final short ShoutMessageEvent = 2867;
    public static final short UserWhisperMessageEvent = 2944;
    public static final short UserWalkMessageEvent = 1953;
    public static final short UserDanceMessageEvent = 334;
    public static final short UserSignMessageEvent = 1040;
    public static final short RoomBanUserMessageEvent = 1730;
    public static final short RoomDeleteMessageEvent = 3992;
    public static final short RoomEventUpdateMessageEvent = 2328;
    public static final short RoomGetSettingsInfoMessageEvent = 227;
    public static final short RoomKickUserMessageEvent = 738;
    public static final short RateRoomMessageEvent = 1041;
    public static final short RoomLoadByDoorbellMessageEvent = 3503;
    public static final short DoorbellAnswerMessageEvent = 694;
    public static final short DropHanditemMessageEvent = 3358;
    public static final short GiveHanditemMessageEvent = 3348;
    public static final short GiveRespectMessageEvent = 838;
    public static final short GiveRightsMessageEvent = 2657;
    public static final short RoomRemoveAllRightsMessageEvent = 1180;
    public static final short RoomRemoveUserRightsMessageEvent = 1681;
    public static final short RoomSaveSettingsMessageEvent = 2988;
    public static final short RoomSettingsMuteAllMessageEvent = 626;
    public static final short RoomSettingsMuteUserMessageEvent = 3067;
    public static final short RoomUnbanUserMessageEvent = 1673;
    public static final short StartTypingMessageEvent = 2470;
    public static final short StopTypingMessageEvent = 1634;
    public static final short IgnoreUserMessageEvent = 2528;
    public static final short UnignoreUserMessageEvent = 1766;
    public static final short GetRoomBannedUsersMessageEvent = 648;
    public static final short GetRoomRightsListMessageEvent = 3062;
    public static final short LookAtUserMessageEvent = 3856;
    public static final short BotActionsMessageEvent = 857;
    public static final short BotSpeechListMessageEvent = 1192;
    public static final short HorseMountOnMessageEvent = 3384;
    public static final short PetGetInformationMessageEvent = 2829;
    public static final short RespectPetMessageEvent = 3143;
    public static final short SaveFloorPlanEditorMessageEvent = 2989;
    public static final short GetFloorPlanFurnitureMessageEvent = 1785;
    public static final short GetFloorPlanDoorMessageEvent = 2364;
    public static final short LoadUserProfileMessageEvent = 2232;
    public static final short GetUserBadgesMessageEvent = 2180;
    public static final short RelationshipsGetMessageEvent = 3563;
    public static final short SetRelationshipMessageEvent = 241;
    public static final short UserUpdateLookMessageEvent = 1797;
    public static final short UserUpdateMottoMessageEvent = 2664;
    public static final short ChangeUsernameMessageEvent = 1139;
    public static final short WardrobeMessageEvent = 2676;
    public static final short WardrobeUpdateMessageEvent = 1862;
    public static final short RoomAddFloorItemMessageEvent = 900;
    public static final short FloorItemMoveMessageEvent = 835;
    public static final short TriggerDiceCloseMessageEvent = 2355;
    public static final short TriggerDiceRollMessageEvent = 3119;
    public static final short TriggerItemMessageEvent = 163;
    public static final short TriggerMoodlightMessageEvent = 830;
    public static final short TriggerWallItemMessageEvent = 1516;
    public static final short EnterOneWayDoorMessageEvent = 1603;
    public static final short UpdateMoodlightMessageEvent = 2866;
    public static final short OpenPostItMessageEvent = 2341;
    public static final short UseHabboWheelMessageEvent = 2219;
    public static final short ActivateMoodlightMessageEvent = 3934;
    public static final short RoomAddPostItMessageEvent = 2822;
    public static final short RoomApplySpaceMessageEvent = 2736;
    public static final short SaveFootballGateOutfitMessageEvent = 1860;
    public static final short LoadPetInventoryMessageEvent = 474;
    public static final short LoadBotInventoryMessageEvent = 1802;
    public static final short LoadBadgeInventoryMessageEvent = 267;
    public static final short LoadItemsInventoryMessageEvent = 3828;
    public static final short PlaceBotMessageEvent = 2171;
    public static final short PlacePetMessageEvent = 850;
    public static final short PickUpBotMessageEvent = 1261;
    public static final short PickUpItemMessageEvent = 1057;
    public static final short PickUpPetMessageEvent = 2002;
    public static final short SetActivatedBadgesMessageEvent = 2701;
    public static final short WiredSaveConditionMessageEvent = 1936;
    public static final short WiredSaveEffectMessageEvent = 2124;
    public static final short WiredSaveMatchingMessageEvent = 376;
    public static final short WiredSaveTriggerMessageEvent = 9;
    public static final short YouTubeChoosePlaylistVideoMessageEvent = 2469;
    public static final short YouTubeGetPlayerMessageEvent = 2870;
    public static final short YouTubeGetPlaylistGetMessageEvent = 289;
    public static final short TileStackMagicSetHeightMessageEvent = 3459;
    public static final short WallItemMoveMessageEvent = 3712;
    public static final short SavePostItMessageEvent = 1842;
    public static final short SaveRoomBackgroundTonerMessageEvent = 3807;
    public static final short SaveRoomBrandingMessageEvent = 1016;
    public static final short MannequinSaveDataMessageEvent = 3652;
    public static final short MannequinUpdateDataMessageEvent = 1710;
    public static final short OpenGiftMessageEvent = 1779;
    public static final short ReedemExchangeItemMessageEvent = 1143;
    public static final short OpenQuestsMessageEvent = 2153;
    public static final short ModerationToolUserToolMessageEvent = 3161;
    public static final short ModerationToolRoomChatlogMessageEvent = 1963;
    public static final short ModerationToolRoomToolMessageEvent = 84;
    public static final short ModerationBanUserMessageEvent = 2286;
    public static final short ModerationKickUserMessageEvent = 2688;
    public static final short ModerationToolGetRoomVisitsMessageEvent = 2384;
    public static final short ModerationToolPerformRoomActionMessageEvent = 1105;
    public static final short ModerationToolSendRoomAlertMessageEvent = 2842;
    public static final short ModerationToolSendUserAlertMessageEvent = 1496;
    public static final short ModerationToolSendUserCautionMessageEvent = 98;
    public static final short ModerationToolUserChatlogMessageEvent = 3884;
    public static final short SubmitHelpTicketMessageEvent = 2299;
    public static final short OpenHelpToolMessageEvent = 2871;
    public static final short AcceptFriendMessageEvent = 882;
    public static final short ConsoleInstantChatMessageEvent = 913;
    public static final short ConsoleInviteFriendsMessageEvent = 2072;
    public static final short ConsoleSearchFriendsMessageEvent = 573;
    public static final short FollowFriendMessageEvent = 166;
    public static final short DeclineFriendMessageEvent = 549;
    public static final short DeleteFriendMessageEvent = 0x0202;
    public static final short RequestFriendMessageEvent = 3162;
    public static final short TradeAcceptMessageEvent = 873;
    public static final short TradeAddItemOfferMessageEvent = 2949;
    public static final short TradeCancelMessageEvent = 2367;
    public static final short TradeConfirmMessageEvent = 1420;
    public static final short TradeDiscardMessageEvent = 1380;
    public static final short TradeStartMessageEvent = 2242;
    public static final short TradeUnacceptMessageEvent = 3493;
    public static final short RequestLeaveGroupMessageEvent = 3139;
    public static final short AcceptGroupRequestMessageEvent = 3437;
    public static final short CreateGuildMessageEvent = 2856;
    public static final short GetGroupFurnitureMessageEvent = 1472;
    public static final short GetGroupInfoMessageEvent = 3697;
    public static final short GetGroupMembersMessageEvent = 1373;
    public static final short GetGroupPurchaseBoxMessageEvent = 2354;
    public static final short DeclineMembershipMessageEvent = 1728;
    public static final short GroupMakeAdministratorMessageEvent = 1142;
    public static final short GroupManageMessageEvent = 576;
    public static final short GroupUpdateBadgeMessageEvent = 2999;
    public static final short GroupUpdateColoursMessageEvent = 2336;
    public static final short GroupUpdateNameMessageEvent = 383;
    public static final short GroupUpdateSettingsMessageEvent = 2090;
    public static final short GroupUserJoinMessageEvent = 992;
    public static final short SetFavoriteGroupMessageEvent = 2698;
    public static final short RemoveGroupAdminMessageEvent = 1458;
    public static final short SetChatPreferenceMessageEvent = 1997;
    public static final short IgnoreInvitationsMessageEvent = 3705;
    public static final short GroupFurnitureWidgetMessageEvent = 1711;
    public static final short CameraTokenMessageEvent = 2600;
    public static final short AddToStaffPickedRoomsMessageEvent = 537;
    public static final short DeletePostItMessageEvent = 2547;
    public static final short LoadProfileByUsernameMessageEvent = 3590;
    public static final short RemoveOwnRightsMessageEvent = 260;

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
