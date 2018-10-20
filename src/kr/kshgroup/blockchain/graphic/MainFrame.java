package kr.kshgroup.blockchain.graphic;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public final class MainFrame extends JFrame {
    private Graph userGraph = new SingleGraph("UserGraph");
    private Graph chainGraph = new SingleGraph("ChainGraph");

    private ArrayList<CommandListener> listeners = new ArrayList<>();

    public MainFrame() {
        super("Blockchain simulator");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1800, 100);
        setLayout(new BorderLayout());
//        setLocationRelativeTo(null);

        setComponents();

        setVisible(true);
    }

    private void setComponents() {
        Viewer userViewer = new Viewer(userGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        userViewer.enableAutoLayout();
        ViewPanel userView = userViewer.addDefaultView(true);

        Viewer chainViewer = new Viewer(chainGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        chainViewer.enableAutoLayout();
        ViewPanel chainView = chainViewer.addDefaultView(true);

//        userView.setBounds(0, 0, 900, 800);
//        chainView.setBounds(900, 0, 900, 800);
//
//        JPanel graphPanel = new JPanel();
//        graphPanel.setBounds(0, 0, 1800, 800);
//        graphPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
//
//        graphPanel.add(userView);
//        graphPanel.add(chainView);

        JTextField command = new JTextField();

        command.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String n = command.getText().trim();

                if(n.isEmpty()) {
                    return;
                }

                int index = n.indexOf(' ');
                String label = "";
                String args[];
                if(index != -1) {
                    label = n.substring(0, index);
                    args = n.substring(index + 1, n.length()).split(" ");
                } else {
                    label = n;
                    args = new String[0];
                }
                for(CommandListener cn : listeners) {
                    cn.onCommand(label, args);
                }
                command.setText("");
            }
        });

//        add(graphPanel, BorderLayout.NORTH);
        add(command, BorderLayout.SOUTH);
    }

    public void addCommandListener(CommandListener listener) {
        this.listeners.add(listener);
    }

    public void addUserNode(String user) {
        if (userGraph.getNode(user) != null) {
            return;
        }
        userGraph.addNode(user);

        Node node = userGraph.getNode(user);
        if(!node.hasAttribute("ui.label"))
            node.addAttribute("ui.label", node.getId());


    }

    public void addUserEdge(String name, String user1, String user2) {
        if (userGraph.getEdge(name) != null) {
            return;
        }
        userGraph.addEdge(name, user1, user2);
    }

    public void removeUserNode(String user) {
        if (userGraph.getNode(user) == null) {
            return;
        }
        userGraph.removeNode(user);
    }

    public void removeUserEdge(String edge) {
        if (userGraph.getEdge(edge) == null) {
            return;
        }
        userGraph.removeEdge(edge);
    }

    public void clearChian() {
        chainGraph.clear();
    }

    public void addChainNode(String block) {
        if (chainGraph.getNode(block) != null) {
            return;
        }
        chainGraph.addNode(block);

        Node node = chainGraph.getNode(block);
        if(!node.hasAttribute("ui.label"))
            node.addAttribute("ui.label", node.getId());
    }

    public void addChainEdge(String name, String block1, String block2) {
        if (chainGraph.getEdge(name) != null) {
            return;
        }
        chainGraph.addEdge(name, block1, block2);
    }

    public void removeChainNode(String block) {
        if (chainGraph.getNode(block) == null) {
            return;
        }
        chainGraph.removeNode(block);
    }

    public void removeChainEdge(String edge) {
        if (chainGraph.getEdge(edge) == null) {
            return;
        }
        chainGraph.removeEdge(edge);
    }

    public void setChainEdgeHighlighted(String edge, boolean highlighted) {
        if (chainGraph.getEdge(edge) == null) {
            return;
        }
        Edge e = chainGraph.getEdge(edge);
        e.setAttribute("ui.style", highlighted ? "fill-color: red;" : "fill-color: black;");
    }

    public void setChainRoot(String root) {
        Node e = chainGraph.getNode(root);
        e.setAttribute("ui.style", "fill-color: red;");
    }

    public void setChainVerified(String root) {
        Node e = chainGraph.getNode(root);
        e.setAttribute("ui.style", "fill-color: brown;");
    }
}
