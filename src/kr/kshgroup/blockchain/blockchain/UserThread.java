package kr.kshgroup.blockchain.blockchain;

import kr.kshgroup.blockchain.EntryMain;

import java.util.List;
import java.util.Random;

public final class UserThread extends Thread{
    private static long auto_increase = 1;
    private EntryMain main;

    private final long uid;
    private String username;
    private int power = 100;

    private boolean pause = false;

    public UserThread(EntryMain main, String username) {
        this.main = main;
        this.uid = auto_increase ++;
        this.username = username;
    }

    public long getUID() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public void mine(String data, boolean cancelMined) {
        BlockChain bc = main.getBlockChain();

        int last_verified = bc.getLastVerifiedBlock(), last = -1;
        List<Integer> chils;

        System.out.println("ORIGIN : " + last_verified);
        while(!(chils = bc.getChildrens(last_verified)).isEmpty()) {
            last = -1;
            for(int i : chils){
                if (last == -1) {
                    last = i;
                } else {
                    if(bc.getChildrensCount(last) < bc.getChildrensCount(i)) {
                        last = i;
                    } else if(bc.getBlock(last).getTime() > bc.getBlock(i).getTime()) {
                        last = i;
                    }
                }
            }
            last_verified = last;
        }
        System.out.println("AFTER : " + last_verified);

        mine(data, last_verified, cancelMined);
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setPause(boolean paused) {
        this.pause = paused;
    }

    public boolean getPause() {
        return pause;
    }

    public void mine(String data, int parent, boolean cancelMined) {
        BlockChain bc = main.getBlockChain();

        System.out.println("MINE STARTED - " + getUsername());
        Block b = new Block(getUID(), data, bc.getBlock(parent).getHash());
        b.setNonce(new Random().nextLong());
        boolean mined = bc.miningBlock(b, power, cancelMined);
        System.out.println("MINED - " + mined + " / " + getUsername());

        if(mined) {
            bc.putBlock(parent, b);
        }
    }

    @Override
    public void run() {
        while(true) {
            while(pause) {
            }
            mine("This is auto mined data! I am " + getUsername(), true);
        }
    }
}
