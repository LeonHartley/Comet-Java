package com.cometproject.server.game.rooms.types.misc;

import com.cometproject.server.game.rooms.RoomManager;
import javolution.util.FastMap;

import java.util.Map;


public class ChatEmotionsManager {
    private FastMap<String, ChatEmotion> emotions;

    public ChatEmotionsManager() {
        emotions = new FastMap<String, ChatEmotion>() {{
            put(":)", ChatEmotion.SMILE);
            put(";)", ChatEmotion.SMILE);
            put(":]", ChatEmotion.SMILE);
            put(";]", ChatEmotion.SMILE);
            put("=)", ChatEmotion.SMILE);
            put("=]", ChatEmotion.SMILE);
            put(":-)", ChatEmotion.SMILE);

            put(">:(", ChatEmotion.ANGRY);
            put(">:[", ChatEmotion.ANGRY);
            put(">;[", ChatEmotion.ANGRY);
            put(">;(", ChatEmotion.ANGRY);
            put(">=(", ChatEmotion.ANGRY);

            put(":o", ChatEmotion.SHOCKED);
            put(";o", ChatEmotion.SHOCKED);
            put(">;o", ChatEmotion.SHOCKED);
            put(">:o", ChatEmotion.SHOCKED);
            put(">=o", ChatEmotion.SHOCKED);
            put("=o", ChatEmotion.SHOCKED);

            put(";'(", ChatEmotion.SAD);
            put(";[", ChatEmotion.SAD);
            put(":[", ChatEmotion.SAD);
            put(";(", ChatEmotion.SAD);
            put("=(", ChatEmotion.SAD);
            put("='(", ChatEmotion.SAD);
            put(":(", ChatEmotion.SAD);
            put(":-(", ChatEmotion.SAD);

            put(";D", ChatEmotion.LAUGH);
            put(":D", ChatEmotion.LAUGH);
            put(":L", ChatEmotion.LAUGH);

            //hehe ;-)
            put("leon", ChatEmotion.SMILE);
            put("alex", ChatEmotion.SMILE);//quackfag
            put("comet", ChatEmotion.SMILE);
            put("java", ChatEmotion.SMILE);
            put("meesha", ChatEmotion.SMILE);

            put("phoenix", ChatEmotion.SAD);
            put("butterfly", ChatEmotion.SAD);

            put("matou19", ChatEmotion.ANGRY);
            put("mathis", ChatEmotion.ANGRY);

            put("minette", ChatEmotion.SHOCKED); // mathis' cat
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
