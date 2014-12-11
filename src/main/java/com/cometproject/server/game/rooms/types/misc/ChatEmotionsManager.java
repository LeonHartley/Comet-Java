package com.cometproject.server.game.rooms.types.misc;

import com.cometproject.server.game.rooms.RoomManager;
import javolution.util.FastMap;

import java.util.Map;


public class ChatEmotionsManager {
    private FastMap<String, ChatEmotion> emotions;

    public ChatEmotionsManager() {
        emotions = new FastMap<String, ChatEmotion>() {{
            put(":)", ChatEmotion.Smile);
            put(";)", ChatEmotion.Smile);
            put(":]", ChatEmotion.Smile);
            put(";]", ChatEmotion.Smile);
            put("=)", ChatEmotion.Smile);
            put("=]", ChatEmotion.Smile);
            put(":-)", ChatEmotion.Smile);

            put(">:(", ChatEmotion.Angry);
            put(">:[", ChatEmotion.Angry);
            put(">;[", ChatEmotion.Angry);
            put(">;(", ChatEmotion.Angry);
            put(">=(", ChatEmotion.Angry);

            put(":o", ChatEmotion.Shocked);
            put(";o", ChatEmotion.Shocked);
            put(">;o", ChatEmotion.Shocked);
            put(">:o", ChatEmotion.Shocked);
            put(">=o", ChatEmotion.Shocked);
            put("=o", ChatEmotion.Shocked);

            put(";'(", ChatEmotion.Sad);
            put(";[", ChatEmotion.Sad);
            put(":[", ChatEmotion.Sad);
            put(";(", ChatEmotion.Sad);
            put("=(", ChatEmotion.Sad);
            put("='(", ChatEmotion.Sad);
            put(":(", ChatEmotion.Sad);
            put(":-(", ChatEmotion.Sad);

            put(";D", ChatEmotion.Laugh);
            put(":D", ChatEmotion.Laugh);
            put(":L", ChatEmotion.Laugh);

            //hehe ;-)
            put("leon", ChatEmotion.Smile);
            put("comet", ChatEmotion.Smile);
            put("java", ChatEmotion.Smile);
            put("meesha", ChatEmotion.Smile);

            put("phoenix", ChatEmotion.Sad);
            put("butterfly", ChatEmotion.Sad);

            put("matou19", ChatEmotion.Angry);
            put("mathis", ChatEmotion.Angry);

            put("minette", ChatEmotion.Shocked); // mathis' cat
        }};

        RoomManager.log.info("Loaded " + this.emotions.size() + " chat emotions");
    }

    public int getEmotion(String message) {
        for (Map.Entry<String, ChatEmotion> emotion : emotions.entrySet()) {
            if (message.toLowerCase().contains(emotion.getKey().toLowerCase())) {
                return emotion.getValue().getEmotionId();
            }
        }
        return 0;
    }
}
