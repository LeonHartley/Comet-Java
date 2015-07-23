package com.cometproject.stresstest.commands;

import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.stresstest.CometStressTest;
import com.cometproject.stresstest.connections.CometClientConnection;
import com.cometproject.stresstest.connections.messages.ChangeFigureMessageComposer;
import com.cometproject.stresstest.connections.messages.ExitRoomMessageComposer;
import com.cometproject.stresstest.connections.messages.LoadRoomMessageComposer;
import com.cometproject.stresstest.connections.messages.TalkMessageComposer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandHandler {
    public static void init(CometStressTest stressTest) {
        Thread cmdThread = new Thread(() -> {
            while (CometStressTest.isRunning) {
                if(!CometStressTest.isRunning) {
                    break;
                }

                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String input = br.readLine();

                    if (input != null && input.startsWith("/")) {
                        if(input.equals("/walk")) {
                            for(CometClientConnection cometClientConnection : stressTest.getConnections()) {
                                if(cometClientConnection.isWalk()) {
                                    cometClientConnection.setIsWalk(false);
                                } else {
                                    cometClientConnection.setIsWalk(true);
                                }
                            }
                        } else if(input.equals("/leaverooms")) {
                            for(CometClientConnection cometClientConnection : stressTest.getConnections()) {
                                cometClientConnection.send(new ExitRoomMessageComposer());
                            }

                            stressTest.getRooms().clear();
                        } else if(input.equals("/joinrooms")) {
                            for(CometClientConnection cometClientConnection : stressTest.getConnections()) {
                                if(cometClientConnection.isOnline()) {
                                    for(Map.Entry<Integer, AtomicInteger> room : stressTest.getRooms().entrySet()) {
                                        if(room.getValue().get() < 300) {
                                            room.getValue().incrementAndGet();

                                            cometClientConnection.send(new LoadRoomMessageComposer(room.getKey(), ""));
                                            cometClientConnection.setIsInRoom(true);
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if(input.startsWith("/say")) {
                            for(CometClientConnection cometClientConnection : stressTest.getConnections()) {
                                cometClientConnection.send(new TalkMessageComposer(input.replace("/say ", "")));
                            }
                        } else if(input.startsWith("/figure")) {
                            for(CometClientConnection cometClientConnection : stressTest.getConnections()) {
                                cometClientConnection.send(new ChangeFigureMessageComposer(input.replace("/figure ", "")));
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cmdThread.start();
    }
}
