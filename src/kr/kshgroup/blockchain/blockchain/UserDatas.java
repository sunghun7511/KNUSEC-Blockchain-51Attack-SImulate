package kr.kshgroup.blockchain.blockchain;

import kr.kshgroup.blockchain.EntryMain;

import java.util.ArrayList;

public class UserDatas {
    private ArrayList<UserThread> threads = new ArrayList<>();
    private EntryMain main;

    public UserDatas(EntryMain main) {
        this.main = main;
    }

    public void initialize() {

    }

    public void onExit() {

    }

    public UserThread getUser(String username) {
        for(UserThread ut : threads) {
            if(ut.getUsername().equalsIgnoreCase(username)) {
                return ut;
            }
        }
        return null;
    }

    public void addUser(String username) {
        if(getUser(username) != null) {
            return;
        }
        UserThread ut = new UserThread(main, username);
        threads.add(ut);

        main.getGraphic().getMainFrame().addUserNode(username);
        for(UserThread t : threads) {
            main.getGraphic().getMainFrame().addUserEdge(username + "/" + t.getUsername(), username, t.getUsername());
        }

        ut.start();
    }
}
