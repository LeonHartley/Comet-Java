package com.cometproject.api.events;


import com.cometproject.api.commands.CommandInfo;
import com.cometproject.api.networking.sessions.ISession;

import java.util.Map;
import java.util.function.BiConsumer;

public interface EventHandler  {
   void initialize();

   <T extends EventArgs> boolean handleEvent(Class<? extends Event> eventClass, T args);

   void registerEvent(Event consumer);

   void registerChatCommand(String commandExecutor, BiConsumer<ISession, String[]> consumer);

   void registerCommandInfo(String commandName, CommandInfo info);

   Map<String, CommandInfo> getCommands();

   boolean handleCommand(ISession session, String commandExectutor, String[] arguments);
}
