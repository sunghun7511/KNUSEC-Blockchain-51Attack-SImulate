package kr.kshgroup.blockchain.graphic;

public final class GraphicMain {
    private MainFrame frame;

    public GraphicMain() {
    }

    public void initialize() {
        if (frame == null) {
            frame = new MainFrame();

//            addTestCase();
        }
    }

    public void onExit() {

    }

    public MainFrame getMainFrame() {
        return frame;
    }

    private void addTestCase() {
        frame.addUserNode("1");
        frame.addUserNode("2");
        frame.addUserNode("3");

        frame.addUserEdge("12", "1", "2");
        frame.addUserEdge("23", "2", "3");
        frame.addUserEdge("13", "1", "3");

        for (int i = 0; i < 5; i++) {
            frame.addChainNode(Integer.toString(i + 1));
            if (i != 0) {
                String en = Integer.toString(i) + Integer.toString(i + 1);
                frame.addChainEdge(en, Integer.toString(i), Integer.toString(i + 1));
                frame.setChainEdgeHighlighted(en, true);
            }
        }

        frame.addChainNode("5-1");
        frame.addChainEdge("45-1", "4", "5-1");
    }
}
