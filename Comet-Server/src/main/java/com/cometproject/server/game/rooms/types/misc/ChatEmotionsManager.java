package com.cometproject.server.game.rooms.types.misc;

import com.cometproject.server.game.rooms.RoomManager;

import java.util.HashMap;
import java.util.Map;


public class ChatEmotionsManager {
    private Map<String, ChatEmotion> emotions;

    public ChatEmotionsManager() {
        emotions = new HashMap<String, ChatEmotion>() {{
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
            put("luna", ChatEmotion.SMILE);
            put("luck", ChatEmotion.SMILE);

            put("phoenix", ChatEmotion.SAD);
            put("butterfly", ChatEmotion.SAD);

            put("matou19", ChatEmotion.ANGRY);
            put("mathis", ChatEmotion.ANGRY);
            put("helpi", ChatEmotion.ANGRY);

            put("minette", ChatEmotion.SHOCKED); // mathis' cat
        }};

        RoomManager.log.info("Loaded " + this.emotions.size() + " chat emotions");
    }

    public ChatEmotion getEmotion(String message) {
        for (Map.Entry<String, ChatEmotion> emotion : emotions.entrySet()) {
            if (message.toLowerCase().contains(emotion.getKey().toLowerCase())) {
                return emotion.getValue();
            }
        }
        return ChatEmotion.NONE;
    }
}
