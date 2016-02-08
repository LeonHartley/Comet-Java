package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Composers {
    public static final short InitializePollMessageComposer = 522;
    public static final short PollMessageComposer = 1307;
    public static final short AvatarAspectUpdateMessageComposer = 3632;
    public static final short UnknownGroupMessageComposer = 1981;
    public static final short QuestionParserMessageComposer = 1719;
    public static final short CatalogItemDiscountMessageComposer = 3322;//3944
    public static final short HelperToolMessageComposer = 224;//1804
    public static final short RoomErrorNotifMessageComposer = 1864;//2527
    public static final short FollowFriendFailedMessageComposer = 1170;//645
    public static final short PurchaseOKMessageComposer = 2843;//3956
    public static final short GroupMembershipRequestedMessageComposer = 423;//3593
    public static final short ModeratorInitMessageComposer = 2545;//2742
    public static final short CatalogOfferMessageComposer = 3848;//1633
    public static final short FindFriendsProcessResultMessageComposer = 3763;//1
    public static final short ThreadsListDataMessageComposer = 1538;//2435
    public static final short UserChangeMessageComposer = 32;//2418
    public static final short FloorHeightMapMessageComposer = 1112;//1988
    public static final short RoomInfoUpdatedMessageComposer = 3833;//696
    public static final short PresentDeliverErrorMessageComposer = 934;//2745
    public static final short AvatarEffectAddedMessageComposer = 2760;//3002
    public static final short MessengerErrorMessageComposer = 915;//2278
    public static final short MarketplaceCanMakeOfferResultMessageComposer = 1874;//539
    public static final short ScrSendUserInfoMessageComposer = 2811;//3747
    public static final short GameAccountStatusMessageComposer = 139;//603
    public static final short RoomSettingsDataMessageComposer = 633;//2491
    public static final short GuestRoomSearchResultMessageComposer = 43;//1112
    public static final short OpenGiftMessageComposer = 1375;//2473
    public static final short NewUserExperienceGiftOfferMessageComposer = 1904;//63
    public static final short UpdateUsernameMessageComposer = 3801;//2974
    public static final short VoucherRedeemOkMessageComposer = 3432;//3914
    public static final short FigureSetIdsMessageComposer = 3469;//313
    public static final short SlideObjectBundleMessageComposer = 1143;//2286
    public static final short StickyNoteMessageComposer = 2338;//1292
    public static final short UserRemoveMessageComposer = 2841;//1602
    public static final short GetGuestRoomResultMessageComposer = 2224;//1301
    public static final short DoorbellMessageComposer = 162;//1211
    public static final short YouAreNotControllerMessageComposer = 1202;//2746
    public static final short AvailabilityStatusMessageComposer = 2468;//3219
    public static final short GiftWrappingConfigurationMessageComposer = 3348;//934
    public static final short QuestStartedMessageComposer = 1477;//54
    public static final short NavigatorLiftedRoomsMessageComposer = 761;//506
    public static final short NavigatorPreferencesMessageComposer = 1430;//1862
    public static final short TradingFinishMessageComposer = 2369;//1225
    public static final short GetRelationshipsMessageComposer = 1589;//1341
    public static final short FriendNotificationMessageComposer = 1211;//2841
    public static final short WhisperMessageComposer = 2280;//1291
    public static final short BadgeEditorPartsMessageComposer = 2519;//1482
    public static final short TraxSongInfoMessageComposer = 523;//1430
    public static final short GroupFurniConfigMessageComposer = 418;//781
    public static final short PostUpdatedMessageComposer = 1752;//1998
    public static final short UserUpdateMessageComposer = 3153;//269
    public static final short ModeratorUserRoomVisitsMessageComposer = 1101;//2884
    public static final short MutedMessageComposer = 229;//954
    public static final short MarketplaceConfigurationMessageComposer = 3702;//2442
    public static final short ChatMessageComposer = 3821;//3075
    public static final short ShoutMessageComposer = 909;//3598
    public static final short TradingConfirmedMessageComposer = 1367;//3716
    public static final short ThreadCreatedMessageComposer = 3683;//2439
    public static final short CheckGnomeNameMessageComposer = 2491;//130
    public static final short GroupCreationWindowMessageComposer = 1232;//1447
    public static final short SetGroupIdMessageComposer = 3197;//876
    public static final short OpenBotActionMessageComposer = 895;//694
    public static final short ThreadDataMessageComposer = 879;//3836
    public static final short YouAreOwnerMessageComposer = 495;//3101
    public static final short RoomForwardMessageComposer = 1963;//1634
    public static final short FavouritesMessageComposer = 604;//1585
    public static final short TalentLevelUpMessageComposer = 3538;//135
    public static final short RoomSettingsSavedMessageComposer = 3737;//523
    public static final short RoomReadyMessageComposer = 2029;//1745
    public static final short BCBorrowedItemsMessageComposer = 3424;//3946
    public static final short UserTagsMessageComposer = 774;//3711
    public static final short QuestAbortedMessageComposer = 3581;//3001
    public static final short CampaignMessageComposer = 3234;//581
    public static final short CatalogPageMessageComposer = 3477;//3739
    public static final short RoomEventMessageComposer = 2274;//1217
    public static final short ObjectRemoveMessageComposer = 85;//3448
    public static final short AchievementScoreMessageComposer = 3710;//635
    public static final short UpdateFavouriteRoomMessageComposer = 854;//3243
    public static final short ModeratorRoomChatlogMessageComposer = 1362;//2384
    public static final short MarketplaceItemStatsMessageComposer = 2909;//3462
    public static final short WiredConditionConfigMessageComposer = 1456;//608
    public static final short SellablePetBreedsMessageComposer = 1871;//3619
    public static final short BuddyListMessageComposer = 3394;//2945
    public static final short HabboSearchResultMessageComposer = 214;//1085
    public static final short ItemUpdateMessageComposer = 2933;//2330
    public static final short PetHorseFigureInformationMessageComposer = 560;//1289
    public static final short PetInventoryMessageComposer = 3528;//931
    public static final short MoodlightConfigMessageComposer = 1964;//2943
    public static final short PongMessageComposer = 624;//1481
    public static final short GroupMembersMessageComposer = 2297;//877
    public static final short RentableSpaceMessageComposer = 2660;//69
    public static final short GetYouTubePlaylistMessageComposer = 763;//3252
    public static final short RespectNotificationMessageComposer = 474;//3691
    public static final short RecyclerRewardsMessageComposer = 2457;//3472
    public static final short SleepMessageComposer = 3852;//1997
    public static final short GetRoomBannedUsersMessageComposer = 3580;//434
    public static final short ModeratorUserInfoMessageComposer = 289;//1274
    public static final short WiredTriggerConfigMessageComposer = 1618;//439
    public static final short RoomRatingMessageComposer = 3464;//1815
    public static final short ModeratorSupportTicketResponseMessageComposer = 3927;//1461
    public static final short PlayableGamesMessageComposer = 549;//3513
    public static final short TalentTrackLevelMessageComposer = 2382;//2783
    public static final short JoinQueueMessageComposer = 749;//3440
    public static final short UserObjectMessageComposer = 1823;//349
    public static final short MarketPlaceOwnOffersMessageComposer = 2806;//2802
    public static final short PetBreedingMessageComposer = 616;//2901
    public static final short SubmitBullyReportMessageComposer = 453;//1512
    public static final short UserNameChangeMessageComposer = 2587;//666
    public static final short WardrobeMessageComposer = 2760;//3002
    public static final short FlatAccessibleMessageComposer = 1179;//2888
    public static final short FurniListNotificationMessageComposer = 2725;//1415
    public static final short ModeratorUserChatlogMessageComposer = 3308;//3494
    public static final short ThreadUpdatedMessageComposer = 3226;//64
    public static final short LoveLockDialogueMessageComposer = 173;//2765
    public static final short SendBullyReportMessageComposer = 2094;//3422
    public static final short VoucherRedeemErrorMessageComposer = 3670;//3094
    public static final short PurchaseErrorMessageComposer = 3016;//2041
    public static final short ThreadReplyMessageComposer = 1936;//2861
    public static final short UnknownCalendarMessageComposer = 1799;//2979
    public static final short QuestListMessageComposer = 664;//2492
    public static final short AvatarEffectExpiredMessageComposer = 68;//451
    public static final short FriendListUpdateMessageComposer = 1611;//3668
    public static final short NavigatorFlatCatsMessageComposer = 1109;//1564
    public static final short UserFlatCatsMessageComposer = 377;//235
    public static final short HideWiredConfigMessageComposer = 3715;//988
    public static final short UpdateFreezeLivesMessageComposer = 1395;//1754
    public static final short ActivityPointsMessageComposer = 1911;//1405
    public static final short UnbanUserFromRoomMessageComposer = 3472;//3720
    public static final short AvatarEffectMessageComposer = 2662;//1834
    public static final short PetTrainingPanelMessageComposer = 1067;//3541
    public static final short LoveLockDialogueCloseMessageComposer = 1534;//1576
    public static final short FurniListRemoveMessageComposer = 1903;//3421
    public static final short BuildersClubMembershipMessageComposer = 2357;//3010
    public static final short SecretKeyMessageComposer = 3179;//129
    public static final short CloseConnectionMessageComposer = 1898;//1433
    public static final short HabboActivityPointNotificationMessageComposer = 606;//1000
    public static final short NavigatorMetaDataParserMessageComposer = 371;//3717
    public static final short NavigatorCollapsedCategoriesMessageComposer = 1263;//3414
    public static final short UpdateFavouriteGroupMessageComposer = 3685;//2973
    public static final short FlatAccessDeniedMessageComposer = 1582;//1562
    public static final short LatencyResponseMessageComposer = 3014;//53
    public static final short BuddyRequestsMessageComposer = 2757;//1486
    public static final short HabboUserBadgesMessageComposer = 1123;//3228
    public static final short HeightMapMessageComposer = 207;//1016
    public static final short ObjectUpdateMessageComposer = 273;//2125
    public static final short YouAreControllerMessageComposer = 1425;//2652
    public static final short CatalogIndexMessageComposer = 2018;//3030
    public static final short FlatControllerRemovedMessageComposer = 1205;//2743
    public static final short NewBuddyRequestMessageComposer = 2981;//1963
    public static final short CanCreateRoomMessageComposer = 1237;//543
    public static final short ModeratorRoomInfoMessageComposer = 13;//2048
    public static final short GroupMemberUpdatedMessageComposer = 2954;//486
    public static final short FloodControlMessageComposer = 1197;//567
    public static final short RoomRightsListMessageComposer = 2410;//3338
    public static final short AvatarEffectActivatedMessageComposer = 1710;//3768
    public static final short InstantMessageErrorMessageComposer = 2964;//2003
    public static final short GnomeBoxMessageComposer = 1778;//2496
    public static final short CfhTopicsInitMessageComposer = 2067;//904
    public static final short IgnoreStatusMessageComposer = 3882;//3073
    public static final short PetInformationMessageComposer = 3913;//3529
    public static final short NavigatorSearchResultSetMessageComposer = 815;//748
    public static final short GroupInfoMessageComposer = 3160;//1183
    public static final short ConcurrentUsersGoalProgressMessageComposer = 2955;//3700
    public static final short UsersMessageComposer = 2422;//810
    public static final short VideoOffersRewardsMessageComposer = 1896;//2991
    public static final short ItemRemoveMessageComposer = 762;//11
    public static final short SanctionStatusMessageComposer = 193;//2304
    public static final short GetYouTubeVideoMessageComposer = 2374;//13
    public static final short GenericErrorMessageComposer = 169;//68
    public static final short UserRightsMessageComposer = 1862;//326
    public static final short ItemAddMessageComposer = 1841;//710
    public static final short CheckPetNameMessageComposer = 3019;//2134
    public static final short RespectPetNotificationMessageComposer = 3637;//2579
    public static final short EnforceCategoryUpdateMessageComposer = 315;//3772
    public static final short ActionMessageComposer = 179;//1623
    public static final short CommunityGoalHallOfFameMessageComposer = 690;//3375
    public static final short ModeratorSupportTicketMessageComposer = 1275;//641
    public static final short AchievementsMessageComposer = 509;//1072
    public static final short FloorPlanFloorMapMessageComposer = 2337;//3776
    public static final short SendGameInvitationMessageComposer = 1165;//2493
    public static final short AchievementUnlockedMessageComposer = 1887;//440
    public static final short GiftWrappingErrorMessageComposer = 2534;//688
    public static final short OpenConnectionMessageComposer = 1329;//1055
    public static final short TradingClosedMessageComposer = 2068;//2781
    public static final short PromoArticlesMessageComposer = 3565;//134
    public static final short Game1WeeklyLeaderboardMessageComposer = 3124;//3634
    public static final short RentableSpacesErrorMessageComposer = 838;//1608
    public static final short AddExperiencePointsMessageComposer = 3779;//1450
    public static final short OpenHelpToolMessageComposer = 3831;//285
    public static final short CreditBalanceMessageComposer = 3604;//284
    public static final short QuestCompletedMessageComposer = 3692;//811
    public static final short GetRoomFilterListMessageComposer = 2169;//3828
    public static final short GameAchievementListMessageComposer = 1264;//561
    public static final short CarryObjectMessageComposer = 2623;//1400
    public static final short InitCryptoMessageComposer = 675;//3542
    public static final short PromotableRoomsMessageComposer = 2166;//2090
    public static final short TradingCompleteMessageComposer = 1959;//3580
    public static final short FloorPlanSendDoorMessageComposer = 2180;//1825
    public static final short FurniListMessageComposer = 2183;//3740
    public static final short RoomEntryInfoMessageComposer = 3378;//2290
    public static final short CatalogUpdatedMessageComposer = 885;//3710
    public static final short SetUniqueIdMessageComposer = 2935;//2786
    public static final short FurniListUpdateMessageComposer = 506;//3490
    public static final short NewGroupInfoMessageComposer = 1095;//1935
    public static final short RoomNotificationMessageComposer = 2419;//251
    public static final short ClubGiftsMessageComposer = 1549;//324
    public static final short FurniListAddMessageComposer = 176;//3631
    public static final short MOTDNotificationMessageComposer = 1829;//3551
    public static final short HabboGroupBadgesMessageComposer = 2487;//1821
    public static final short PopularRoomTagsResultMessageComposer = 234;//1412
    public static final short NewConsoleMessageMessageComposer = 2121;//1941
    public static final short RoomPropertyMessageComposer = 1328;//1831
    public static final short TradingUpdateMessageComposer = 2277;//2909
    public static final short MarketPlaceOffersMessageComposer = 2985;//970
    public static final short TalentTrackMessageComposer = 3614;//3244
    public static final short GroupFurniSettingsMessageComposer = 613;//3091
    public static final short ProfileInformationMessageComposer = 3872;//3744
    public static final short BadgeDefinitionsMessageComposer = 2066;//2015
    public static final short SoundSettingsMessageComposer = 2921;//3005
    public static final short UserTypingMessageComposer = 2854;//1890
    public static final short Game2WeeklyLeaderboardMessageComposer = 1127;//2556
    public static final short NameChangeUpdateMessageComposer = 2698;//765
    public static final short RoomVisualizationSettingsMessageComposer = 3786;//792
    public static final short MarketplaceMakeOfferResultMessageComposer = 3960;//1037
    public static final short UserPerksMessageComposer = 2807;//2163
    public static final short ForumsListDataMessageComposer = 3596;//898
    public static final short DanceMessageComposer = 845;//1407
    public static final short FlatCreatedMessageComposer = 1621;//3118
    public static final short BotInventoryMessageComposer = 2620;//2641
    public static final short ObjectsMessageComposer = 3521;//2434
    public static final short ItemsMessageComposer = 2335;//2560
    public static final short LoadGameMessageComposer = 1403;//2983
    public static final short AvatarEffectsMessageComposer = 3310;//1987
    public static final short ManageGroupMessageComposer = 2653;//3531
    public static final short UpdateMagicTileMessageComposer = 2641;//469
    public static final short CampaignCalendarDataMessageComposer = 1480;//1764
    public static final short FurnitureAliasesMessageComposer = 817;//1687
    public static final short MaintenanceStatusMessageComposer = 3198;//981
    public static final short Game3WeeklyLeaderboardMessageComposer = 2194;//1473
    public static final short BadgesMessageComposer = 154;//1273
    public static final short WiredEffectConfigMessageComposer = 1469;//2089
    public static final short GameListMessageComposer = 2481;//1050
    public static final short RoomMuteSettingsMessageComposer = 257;//3904
    public static final short RoomInviteMessageComposer = 3942;//3861
    public static final short LoveLockDialogueSetLockedMessageComposer = 1534;//1576
    public static final short BroadcastMessageAlertMessageComposer = 1279;//805
    public static final short MarketplaceCancelOfferResultMessageComposer = 202;//1685
    public static final short ForumDataMessageComposer = 254;//298
    public static final short AchievementProgressedMessageComposer = 305;//1088
    public static final short RefreshFavouriteGroupMessageComposer = 382;//625
    public static final short TradingErrorMessageComposer = 2876;//345
    public static final short ObjectAddMessageComposer = 505;//1161
    public static final short TradingAcceptMessageComposer = 1367;//3716
    public static final short AuthenticationOKMessageComposer = 1442;//3560
    public static final short TradingStartMessageComposer = 2290;//71
    public static final short NavigatorSettingsMessageComposer = 3175;//1001
    public static final short FlatControllerAddedMessageComposer = 1056;//2036
    public static final short ModeratorTicketChatlogMessageComposer = 766;//2246
    public static final short MessengerInitMessageComposer = 391;//3426
    public static final short UpdateStackMapMessageComposer = 3251;

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
