package kr.kshgroup.blockchain;

import kr.kshgroup.blockchain.blockchain.BlockChain;
import kr.kshgroup.blockchain.blockchain.UserDatas;
import kr.kshgroup.blockchain.graphic.GraphicMain;

import java.util.LinkedList;

public final class EntryMain extends Thread {
    public static void main(String[] args) {
        new EntryMain().start();
    }

    private GraphicMain graphic;
    private BlockChain blockchain;
    private UserDatas users;

    private LinkedList<Runnable> runOnMainThread = new LinkedList<>();

    private boolean isExit = false;

    public EntryMain() {
        this.graphic = new GraphicMain();
        this.blockchain = new BlockChain(this);
        this.users = new UserDatas(this);
    }

    public void run() {
        this.graphic.initialize();
        this.blockchain.initialize();
        this.users.initialize();

        this.graphic.getMainFrame().addCommandListener(new MainCommandListener(this));

        while (!isExit) {
            while (!runOnMainThread.isEmpty()) {
                runOnMainThread.pop().run();
            }
        }

        this.users.onExit();
        this.blockchain.onExit();
        this.graphic.onExit();

        System.exit(0);
    }

    public void exit() {
        isExit = true;
    }

    public BlockChain getBlockChain() {
        return blockchain;
    }

    public GraphicMain getGraphic() {
        return graphic;
    }

    public UserDatas getUserDatas() {
        return users;
    }

    public void runOnMainThread(Runnable r) {
        runOnMainThread.add(r);
    }
}
