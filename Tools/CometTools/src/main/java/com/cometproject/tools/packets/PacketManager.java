package com.cometproject.tools.packets;

import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.tools.packets.instances.MessageComposer;
import com.cometproject.tools.packets.instances.MessageEvent;
import com.cometproject.tools.packets.revisions.HabboRevision;
import com.cometproject.tools.utils.ScriptsLoader;
import com.google.common.base.Stopwatch;
import javolution.util.FastList;


import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class PacketManager {
    private final String oldScriptsFilename = "Habbo_old.txt";
    private final String newScriptsFilename = "Habbo_new.txt";

    private HabboRevision oldRevision;
    private HabboRevision newRevision;

    public PacketManager() {
        String oldScripts = ScriptsLoader.load("./scripts/" + oldScriptsFilename);
        String newScripts = ScriptsLoader.load("./scripts/" + newScriptsFilename);

        if(oldScripts == null) {
            // TODO: make UI error handling.
            System.out.println("Old scripts are non-existent. Please check the file locations!");
            return;
        } else if(newScripts == null) {
            System.out.println("New scripts are non-existent. Please check the file locations!");
            return;
        }

        this.oldRevision = parseScripts(oldScripts.split("\n"));
        this.newRevision = parseScripts(newScripts.split("\n"));
    }

    private HabboRevision parseScripts(String[] scriptLines) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String releaseString = "";

        Map<String, String> types = new FastMap<>();
        Map<String, MessageComposer> composers = new FastMap<>();
        Map<String, MessageEvent> events = new FastMap<>();

        for(int i = 0; i < scriptLines.length; i++) {
            String line = scriptLines[i];

            if(line.contains("RELEASE63")) {
                releaseString = line.split("\"")[1].split("\"")[0];
                continue;
            }

            if(line.contains("function flush():Boolean;")) {
                String starter = scriptLines[i - 2].split("interface ")[1].split(" ")[0];

                for(int j = 0; j < scriptLines.length; j++) {
                    String s = scriptLines[j];

                    if(!s.contains("implements " + starter)) {
                        continue;
                    }

                    String packetParserName = s.split(" class ")[1].split(" ")[0];

                    List<String> structure = new FastList<>();

                    for(int k = j; k < scriptLines.length; k++) {
                        if(scriptLines[k].contains("function parse(")) {
                            String messageReaderClassName = scriptLines[k].split("_arg1:")[1].split("\\)")[0];

                            String loopIndent = "";

                            for(int l = k; l < scriptLines.length; l++) {
                                if(scriptLines[l].contains("while")) {
                                    loopIndent = scriptLines[l].split("while")[0];
                                    structure.add("loop {");
                                }

                                if(scriptLines[l].startsWith(loopIndent + "};")) {
                                    structure.add("}");
                                    loopIndent = "";
                                }

                                if(scriptLines[l].contains("_arg1") && k != l) {
                                    if(scriptLines[l].contains("_arg1.")) {
                                        String readFunctionName = scriptLines[l].split("_arg1.")[1].split("\\(\\)")[0];

                                        if(!types.containsKey(readFunctionName)) {
                                            boolean classFound = false;
                                            String type = "Unknown";

                                            for (int m = 0; m < scriptLines.length; m++) {

                                                if (scriptLines[m].contains(" implements " + messageReaderClassName)) {
                                                    classFound = true;
                                                }

                                                if (classFound && scriptLines[m].contains(readFunctionName)) {
                                                    try {
                                                        type = scriptLines[m].split(":")[1].split("\\{")[0];
                                                    } catch(Exception e) {
                                                        continue;
                                                    }

                                                    break;
                                                }

                                                if (classFound && scriptLines[m].startsWith("    }")) {
                                                    break;
                                                }
                                            }

                                            types.put(readFunctionName, type);
                                        }

                                        structure.add(types.get(readFunctionName));
                                    } else {
                                            structure.add("ExternalParser");
                                            // TODO: this
                                        }
                                    }

                                if(scriptLines[l].startsWith("        }")) {
                                    break;
                                }
                            }
                        }

                        if(scriptLines[k].startsWith("    }")) {
                            break;
                        }
                    }

                    short id = 0;
                    String packetHeaderName = null;

                    for(int k = 0; k < scriptLines.length; k++) {
                        if(id != 0)
                            break;

                        if(scriptLines[k].contains(packetParserName) && scriptLines[k].contains("super")) {
                            packetHeaderName = scriptLines[k - 1].split("function")[1].split("\\(")[0];
                            for(int l = 0; l < scriptLines.length; l++) {
                                if(scriptLines[l].contains(packetHeaderName) && scriptLines[l].contains("] = ") && !scriptLines[l].contains("\"")) {
                                    try {
                                        id = Short.parseShort(scriptLines[l].split("\\[")[1].split("]")[0]);
                                    } catch(Exception e) {
                                        continue;
                                    }
                                }
                            }
                        }
                    }

                    try {
                        MessageComposer cmp = new MessageComposer(id, packetHeaderName, packetParserName);
                        cmp.setStructure(structure);

                        composers.put(packetHeaderName, cmp);
                    } catch(Exception e) {
                        continue;
                    }
                }
            }
        }

        HabboRevision revision = new HabboRevision(releaseString, composers, events);
        System.out.println("Parse scripts process took: " + (((double) stopwatch.elapsed(TimeUnit.MILLISECONDS)) / 1000));
        return revision;
    }

    public HabboRevision getOldRevision() {
        return oldRevision;
    }

    public HabboRevision getNewRevision() {
        return newRevision;
    }
}
