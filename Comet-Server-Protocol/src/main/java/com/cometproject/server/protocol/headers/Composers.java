package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


public class Composers {
    public static final short OpenLinkMessageComposer = 2669;
    public static final short WiredRewardMessageComposer = 2960;//753
    public static final short HeightMapMessageComposer = 1010;//3546
    public static final short CallForHelpPendingCallsMessageComposer = 2804;//771
    public static final short ChatMessageComposer = 2174;//82
    public static final short GroupMembersMessageComposer = 610;//3762
    public static final short OpenBotActionMessageComposer = 1469;//2208
    public static final short UserObjectMessageComposer = 1513;//1500
    public static final short ActionMessageComposer = 1536;//449
    public static final short ManageGroupMessageComposer = 891;//2739
    public static final short FloodControlMessageComposer = 3603;//2438
    public static final short FlatControllerAddedMessageComposer = 3622;//1764
    public static final short TradingClosedMessageComposer = 3671;//733
    public static final short FlatCreatedMessageComposer = 3230;//3273
    public static final short ScrSendUserInfoMessageComposer = 3459;//827
    public static final short CheckPetNameMessageComposer = 3920;//632
    public static final short QuestAbortedMessageComposer = 2671;//1918
    public static final short RespectPetNotificationMessageComposer = 3856;//310
    public static final short PromotableRoomsMessageComposer = 2283;//187
    public static final short CloseConnectionMessageComposer = 1297;//3326
    public static final short CfhTopicsInitMessageComposer = 2333;//2761
    public static final short WiredEffectConfigMessageComposer = 2726;//2617
    public static final short FriendListUpdateMessageComposer = 3872;//275
    public static final short ObjectAddMessageComposer = 3340;//2362
    public static final short NavigatorCollapsedCategoriesMessageComposer = 1146;//3045
    public static final short RoomRightsListMessageComposer = 3321;//3260
    public static final short TradingUpdateMessageComposer = 2560;//1958
    public static final short CarryObjectMessageComposer = 3112;//3618
    public static final short NewGroupInfoMessageComposer = 2279;//1893
    public static final short RoomForwardMessageComposer = 319;//2465
    public static final short GroupFurniSettingsMessageComposer = 1777;//363
    public static final short CreditBalanceMessageComposer = 1556;//1502
    public static final short CatalogUpdatedMessageComposer = 489;//1733
    public static final short UserTypingMessageComposer = 3293;//3773
    public static final short ObjectRemoveMessageComposer = 2039;//2278
    public static final short RoomEntryInfoMessageComposer = 3383;//1034
    public static final short CatalogOfferMessageComposer = 2600;//3782
    public static final short CatalogIndexMessageComposer = 808;//1113
    public static final short ThreadsListDataMessageComposer = 2597;//2053
    public static final short GroupFurniConfigMessageComposer = 1460;//1255
    public static final short HabboUserBadgesMessageComposer = 54;//1132
    public static final short FlatAccessibleMessageComposer = 3140;//1989
    public static final short ModeratorInitMessageComposer = 2772;//1335
    public static final short FloorPlanSendDoorMessageComposer = 2201;//2541
    public static final short SleepMessageComposer = 157;//272
    public static final short FlatControllerRemovedMessageComposer = 257;//3036
    public static final short UniqueMachineIDMessageComposer = 602;//1303
    public static final short ItemAddMessageComposer = 366;//3525
    public static final short GroupForumDataMessageComposer = 2023;//1703
    public static final short UpdateFreezeLivesMessageComposer = 2535;//3543
    public static final short NavigatorSettingsMessageComposer = 1776;//2722
    public static final short ItemUpdateMessageComposer = 1481;//3635
    public static final short AchievementsMessageComposer = 2028;//2419
    public static final short LatencyResponseMessageComposer = 2757;//1991
    public static final short RoomReadyMessageComposer = 3334;//525
    public static final short HabboActivityPointNotificationMessageComposer = 2474;//3295
    public static final short BuddyListMessageComposer = 2891;//2391
    public static final short YoutubeDisplayPlaylistsMessageComposer = 1882;//2096
    public static final short TradingCompleteMessageComposer = 511;//629
    public static final short PetInformationMessageComposer = 3345;//1888
    public static final short ModeratorRoomChatlogMessageComposer = 1708;//1469
    public static final short MOTDNotificationMessageComposer = 408;//2442
    public static final short GroupInfoMessageComposer = 3190;//3336
    public static final short SlideObjectBundleMessageComposer = 352;//342
    public static final short FurniListRemoveMessageComposer = 1748;//3735
    public static final short FurniListNotificationMessageComposer = 1310;//3879
    public static final short RoomInfoUpdatedMessageComposer = 3966;//1380
    public static final short AvatarEffectMessageComposer = 2398;//3856
    public static final short OpenConnectionMessageComposer = 3450;//3374
    public static final short FurniListMessageComposer = 1669;//372
    public static final short PostUpdatedMessageComposer = 2551;//1998
    public static final short UserFlatCatsMessageComposer = 2986;//2372
    public static final short ObjectUpdateMessageComposer = 1125;//2155
    public static final short ThreadUpdatedMessageComposer = 517;//3749
    public static final short HabboSearchResultMessageComposer = 3766;//3659
    public static final short RespectNotificationMessageComposer = 3489;//2214
    public static final short PetHorseFigureInformationMessageComposer = 3937;//2798
    public static final short MessengerInitMessageComposer = 913;//2195
    public static final short ModeratorUserInfoMessageComposer = 134;//3261
    public static final short YouAreControllerMessageComposer = 3668;//2345
    public static final short RoomRatingMessageComposer = 3267;//1758
    public static final short RefreshFavouriteGroupMessageComposer = 1579;//3125
    public static final short AvailabilityStatusMessageComposer = 1769;//1694
    public static final short AchievementUnlockedMessageComposer = 684;//3032
    public static final short FlatAccessDeniedMessageComposer = 448;//1370
    public static final short NavigatorFlatCatsMessageComposer = 3851;//501
    public static final short UsersMessageComposer = 2775;//2206
    public static final short SecretKeyMessageComposer = 3465;//1092
    public static final short TradingStartMessageComposer = 2825;//1539
    public static final short RoomSettingsDataMessageComposer = 3075;//598
    public static final short NewBuddyRequestMessageComposer = 2311;//128
    public static final short DoorbellMessageComposer = 1099;//1478
    public static final short OpenGiftMessageComposer = 3980;//3424
    public static final short CantConnectMessageComposer = 1945;//3
    public static final short FloorHeightMapMessageComposer = 3841;//2000
    public static final short SellablePetBreedsMessageComposer = 569;//3471
    public static final short AchievementScoreMessageComposer = 896;//562
    public static final short BuildersClubMembershipMessageComposer = 2286;//2443
    public static final short PetTrainingPanelMessageComposer = 720;//3729
    public static final short QuestCompletedMessageComposer = 2999;//715
    public static final short UserRightsMessageComposer = 1081;//2494
    public static final short ForumsListDataMessageComposer = 2103;//835
    public static final short UserChangeMessageComposer = 2098;//901
    public static final short ModeratorUserChatlogMessageComposer = 2334;//1660
    public static final short GiftWrappingConfigurationMessageComposer = 1035;//2289
    public static final short FloorPlanFloorMapMessageComposer = 3542;//3600
    public static final short ThreadReplyMessageComposer = 2413;//423
    public static final short GroupCreationWindowMessageComposer = 870;//1224
    public static final short GetGuestRoomResultMessageComposer = 1826;//2475
    public static final short RoomNotificationMessageComposer = 3531;//2591
    public static final short InitCryptoMessageComposer = 2904;//3115
    public static final short SoundSettingsMessageComposer = 1001;//1797
    public static final short WiredTriggerConfigMessageComposer = 3478;//3654
    public static final short ItemsMessageComposer = 2649;//3549
    public static final short PurchaseOKMessageComposer = 479;//2185
    public static final short BadgeEditorPartsMessageComposer = 2579;//2652
    public static final short NewConsoleMessageMessageComposer = 2606;//1672
    public static final short HideWiredConfigMessageComposer = 1991;//1646
    public static final short CatalogPageMessageComposer = 2412;//3128
    public static final short AddExperiencePointsMessageComposer = 1117;//2894
    public static final short AvatarEffectsMessageComposer = 899;//2790
    public static final short QuestListMessageComposer = 2566;//1401
    public static final short UnbanUserFromRoomMessageComposer = 1784;//280
    public static final short WiredConditionConfigMessageComposer = 1810;//1045
    public static final short StickyNoteMessageComposer = 2208;//1488
    public static final short SanctionStatusMessageComposer = 1053;//3894
    public static final short ObjectsMessageComposer = 1264;//974
    public static final short RoomVisualizationSettingsMessageComposer = 2988;//2884
    public static final short PromoArticlesMessageComposer = 253;//3113
    public static final short MaintenanceStatusMessageComposer = 2724;//3148
    public static final short BuddyRequestsMessageComposer = 1151;//3951
    public static final short AuthenticationOKMessageComposer = 3054;//223
    public static final short QuestStartedMessageComposer = 325;//1375
    public static final short BotInventoryMessageComposer = 3095;//3035
    public static final short PerkAllowancesMessageComposer = 3189;//1699
    public static final short RoomEventMessageComposer = 1488;//3073
    public static final short RoomMuteSettingsMessageComposer = 3071;//3597
    public static final short ModeratorSupportTicketResponseMessageComposer = 1825;//1403
    public static final short YouTubeDisplayVideoMessageComposer = 29;//3280
    public static final short RoomPropertyMessageComposer = 424;//3357
    public static final short ModeratorSupportTicketMessageComposer = 283;//2871
    public static final short RoomInviteMessageComposer = 1378;//869
    public static final short FurniListUpdateMessageComposer = 1604;//909
    public static final short BadgesMessageComposer = 2969;//2084
    public static final short NavigatorSearchResultSetMessageComposer = 3984;//3094
    public static final short IgnoreStatusMessageComposer = 3919;//2996
    public static final short ShoutMessageComposer = 3944;//2945
    public static final short MoodlightConfigMessageComposer = 2266;//1605
    public static final short FurnitureAliasesMessageComposer = 316;//3274
    public static final short LoveLockDialogueCloseMessageComposer = 2027;//1664
    public static final short TradingErrorMessageComposer = 1386;//3904
    public static final short ProfileInformationMessageComposer = 1897;//698
    public static final short ModeratorRoomInfoMessageComposer = 154;//465
    public static final short CampaignMessageComposer = 2081;//582
    public static final short LoveLockDialogueMessageComposer = 318;//1391
    public static final short PurchaseErrorMessageComposer = 1408;//3149
    public static final short PopularRoomTagsResultMessageComposer = 2547;//3446
    public static final short GiftWrappingErrorMessageComposer = 1434;//3538
    public static final short WhisperMessageComposer = 2810;//385
    public static final short CatalogItemDiscountMessageComposer = 3575;//1182
    public static final short HabboGroupBadgesMessageComposer = 1333;//1139
    public static final short CanCreateRoomMessageComposer = 277;//670
    public static final short ThreadDataMessageComposer = 3052;//2375
    public static final short TradingFinishMessageComposer = 1940;//2174
    public static final short DanceMessageComposer = 1276;//3755
    public static final short GenericErrorMessageComposer = 2856;//2501
    public static final short NavigatorPreferencesMessageComposer = 2123;//1075
    public static final short MutedMessageComposer = 1671;//1983
    public static final short BroadcastMessageAlertMessageComposer = 82;//1069
    public static final short YouAreOwnerMessageComposer = 2539;//3596
    public static final short ModeratorTicketChatlogMessageComposer = 3492;//898
    public static final short BadgeDefinitionsMessageComposer = 1924;//461
    public static final short UserRemoveMessageComposer = 2848;//3096
    public static final short RoomSettingsSavedMessageComposer = 539;//368
    public static final short ModeratorUserRoomVisitsMessageComposer = 2415;//3082
    public static final short NavigatorLiftedRoomsMessageComposer = 3709;//3345
    public static final short NavigatorMetaDataParserMessageComposer = 2631;//1096
    public static final short GetRelationshipsMessageComposer = 3068;//321
    public static final short ItemRemoveMessageComposer = 2091;//3957
    public static final short ThreadCreatedMessageComposer = 871;//2650
    public static final short EnforceCategoryUpdateMessageComposer = 2621;//1120
    public static final short AchievementProgressedMessageComposer = 2167;//1970
    public static final short ActivityPointsMessageComposer = 3304;//873
    public static final short PetInventoryMessageComposer = 3808;//3513
    public static final short GetRoomBannedUsersMessageComposer = 2712;//292
    public static final short UserUpdateMessageComposer = 2694;//1491
    public static final short FavouritesMessageComposer = 2753;//3973
    public static final short WardrobeMessageComposer = 1178;//344
    public static final short LoveLockDialogueSetLockedMessageComposer = 2027;//1664
    public static final short TradingAcceptMessageComposer = 1464;//2229
    public static final short SongInventoryMessageComposer = 484;//527
    public static final short SongIdMessageComposer = 3538;//1414
    public static final short SongDataMessageComposer = 1822;//2341
    public static final short PlaylistMessageComposer = 1080;//1211
    public static final short PlayMusicMessageComposer = 1401;//1366
    public static final short QuickPollMessageComposer = 1232;//814
    public static final short QuickPollResultMessageComposer = 2178;//2293
    public static final short QuickPollResultsMessageComposer = 3139;//3242
    public static final short InitializePollMessageComposer = 3726;//1816
    public static final short PollMessageComposer = 430;//2401
    public static final short AvatarAspectUpdateMessageComposer = 2786;//2407
    public static final short YouAreSpectatorMessageComposer = 2666;//3365
    public static final short UpdateStackMapMessageComposer = 2730;//2555
    public static final short CameraPhotoPreviewMessageComposer = 3115;//555
    public static final short CameraBuyPhotoMessageComposer = 3859;//1492
    public static final short CameraPriceMessageComposer = 953;//3328
    public static final short UpdateUsernameMessageComposer = 393;//112
    public static final short UserNameChangeMessageComposer = 898;//607
    public static final short NameChangeUpdateMessageComposer = 3436;//928
    public static final short SendHotelViewLooksMessageComposer = 791;//3275
    public static final short GuideSessionAttachedMessageComposer = 2485;//2686
    public static final short GuideSessionDetachedMessageComposer = 1443;//2130
    public static final short GuideSessionStartedMessageComposer = 1219;//1818
    public static final short GuideSessionEndedMessageComposer = 91;//3133
    public static final short GuideSessionErrorMessageComposer = 3550;//2404
    public static final short GuideSessionMessageMessageComposer = 3845;//2402
    public static final short GuideSessionRequesterRoomMessageComposer = 2905;//3906
    public static final short GuideSessionInvitedToGuideRoomMessageComposer = 3149;//24
    public static final short GuideSessionPartnerIsTypingMessageComposer = 3101;//2781
    public static final short GuideToolsMessageComposer = 2857;//422
    public static final short GuardianNewReportReceivedMessageComposer = 2350;//1001
    public static final short GuardianVotingRequestedMessageComposer = 2094;//3075
    public static final short GuardianVotingVotesMessageComposer = 2692;//2449
    public static final short GuardianVotingResultMessageComposer = 1103;//847
    public static final short GuardianVotingTimeEndedMessageComposer = 3015;//238
    public static final short ModToolReportReceivedAlertMessageComposer = 3124;//2604
    public static final short BullyReportClosedMessageComposer = 1998;//1259
    public static final short BullyReportRequestMessageComposer = 2586;//3711
    public static final short BullyReportedMessageMessageComposer = 3004;//1416
    public static final short HelperRequestDisabledMessageComposer = 2817;//2975
    public static final short UserTagsMessageComposer = 2212;//397
    public static final short GetRoomFilterListMessageComposer = 1798;//2297
    public static final short NavigatorSavedSearchesMessageComposer = 2853;//2017
    public static final short FindFriendsProcessResultMessageComposer = 932;//3682
    public static final short NavigatorFavoritedRoomMessageComposer = 3846;//1723
    public static final short FollowErrorMessageComposer = 344;//2054
    public static final short FriendRequestErrorMessageComposer = 2711;//75
    public static final short BotErrorMessageComposer = 2605;//2469
    public static final short PetErrorMessageComposer = 3987;//1819
    public static final short GroupAcceptMemberErrorMessageComposer = 2983;//2398
    public static final short RemoveGroupFromRoomMessageComposer = 740;//274
    public static final short RefreshGroupMembersListMessageComposer = 3186;//1775
    public static final short GroupMemberUpdateMessageComposer = 3263;//1740
    public static final short GroupConfirmRemoveMemberMessageComposer = 2328;//3992
    public static final short RemoveBotMessageComposer = 1551;//499
    public static final short ReceivedHandItemMessageComposer = 935;//3496
    public static final short LimitedEditionSoldOutMessageComposer = 2979;//3847
    public static final short LoveLockDialogueFinishedMessageComposer = 2355;//3449
    public static final short RoomChatSettingsMessageComposer = 2114;//2784
    public static final short AddBotMessageComposer = 1357;//1485
    public static final short PurchaseUnavailableErrorMessageComposer = 2866;//3463
    public static final short GroupFavoritePlayerUpdateMessageComposer = 3770;//3669
    public static final short JoinGroupErrorMessageComposer = 141;//1186
    public static final short RoomActionMessageComposer = 1550;//2283
    public static final short PetBreedingMessageComposer = 3099;//1561
    public static final short PetBreedingCompleteMessageComposer = 3034;//208
    public static final short PetBreedingStartedMessageComposer = 1692;//713
    public static final short PetPackageMessageComposer = 3781;//3418
    public static final short PetPackageOpenedMessageComposer = 1278;//251
    public static final short FigureSetIdsMessageComposer = 745;//1982
    public static final short ThumbnailSavedMessageComposer = 1101;
    public static final short PhotoPreviewMessageComposer = 3115;
    public static final short PhotoPriceMessageComposer = 953;
    public static final short PurchasedPhotoMessageComposer = 3859;
    public static final short YouArePlayingGameMessageComposer = 613;
    public static final short GameListMessageComposer = 1187;
    public static final short GameAchievementsMessageComposer = 2719;
    public static final short GameStatusMessageComposer = 10;
    public static final short GameAccountStatusMessageComposer = 1821;
    public static final short LoadGameMessageComposer = 2610;


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
