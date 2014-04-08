package com.cometproject.tools.ui;

import com.cometproject.tools.CometTools;
import com.cometproject.tools.packets.instances.MessageComposer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.StringWriter;

public class CometWindow extends JFrame {
    public static final String WINDOW_TITLE = "Comet Tools";
    public static final Dimension WINDOW_DIMENSION = new Dimension(1024, 700);

    private CometTools tools;

    private JPanel panel;
    private JTabbedPane tabbedPane1;
    private JList list1;
    private JTextArea packetInformation;

    public CometWindow(CometTools tools) {
        this.tools = tools;

        this.panel.setSize(WINDOW_DIMENSION);
        this.panel.setPreferredSize(WINDOW_DIMENSION);

        this.setSize(WINDOW_DIMENSION);
        this.setPreferredSize(WINDOW_DIMENSION);

        this.setContentPane(panel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setEnabled(true);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle(WINDOW_TITLE);

        this.pack();

        initPacketList();
    }

    private void initPacketList() {
        DefaultListModel model = new DefaultListModel();

        for(MessageComposer msg : tools.getPacketManager().getNewRevision().getComposers().values()) {
            model.addElement(msg.getId() + " (" + msg.getClassName() + ")");
        }

        this.list1.setModel(model);
        this.list1.addListSelectionListener(new CometPacketSelectionHandler());
    }

    private class CometPacketSelectionHandler implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            try {
                StringBuilder info = new StringBuilder();
                info.append("Packet Structure\n==================================\n");

                String key = ((String) list1.getSelectedValue()).split("\\(")[1].split("\\)")[0];

                MessageComposer msg = tools.getPacketManager().getNewRevision().getComposers().get(key);

                if(msg == null) {
                    System.out.println("Message is null!");
                    return;
                }

                info.append("Composer msg = new Composer(" + msg.getId() + ");\n");

                for(String structure : msg.getStructure()) {
                    if(structure.equals("int")) {
                        info.append("msg.writeInt(0);\n");
                    } else if(structure.equals("String")) {
                        info.append("msg.writeString(\"\");\n");
                    } else if(structure.equals("Boolean")) {
                        info.append("msg.writeBoolean(false);\n");
                    } else if(structure.contains("loop")) {
                        info.append("while(bool) {");
                    } else if(structure.contains("}")) {
                        info.append("}");
                    } else if(structure.equals("ExternalParser")) {
                        info.append("// TODO: ExternalParser...");
                    }
                }

                packetInformation.setText(info.toString());
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
