package kr.kshgroup.blockchain.blockchain;

import kr.kshgroup.blockchain.EntryMain;
import kr.kshgroup.blockchain.graphic.MainFrame;

import java.util.ArrayList;
import java.util.List;

public final class BlockChain {
    public final static int DEFAULT_VERIFY_CHAIN_COUNT = 3;

    private final ArrayList<Block> pool = new ArrayList<>();
    private final ArrayList<Integer> verified = new ArrayList<>();
    private int verified_depth = 0;
    private int difficulty = 2;
    private int verify_chain_count = DEFAULT_VERIFY_CHAIN_COUNT;

    private EntryMain main;

    public BlockChain(EntryMain main) {
        this.main = main;

        pool.add(new Block(-1, "Genesis Block", "0"));

        verified.add(0);
        verified_depth++;
    }

    public void initialize() {
        updateBlockGUI();
    }

    public void onExit() {

    }

    public boolean isValidBlock(int b) {
        return isValidBlock(pool.get(b), difficulty);
    }

    public boolean isValidBlock(Block b) {
        return isValidBlock(b, difficulty);
    }

    public static boolean isValidBlock(Block b, int difficulty) {
        return b.getHash().substring(0, difficulty).equals(new String(new char[difficulty]).replace('\0', '0'));
    }

    public int getDepth(int i) {
        if(i == getGenesisBlock()) {
            return 1;
        }
        Block b = pool.get(i);
        int tmp = i;
        int depth = 1;

        while(tmp != getGenesisBlock()) {
            tmp = getBlockFromHash(b.getPreviousHash());
            b = pool.get(tmp);
            depth ++;
        }

        return depth;
    }

    public int getGenesisBlock() {
        return 0;
    }

    public List<Integer> getChildrens(int parent) {
        List<Integer> blocks = new ArrayList<>();

        if (parent <= -1) {
            blocks.add(0);
        } else if (parent >= pool.size()) {
            throw new RuntimeException("Invalid index");
        } else {
            System.out.println(pool.size());
            for (int i = parent + 1; i < pool.size(); i++) {
                Block b = pool.get(i);
                if (pool.get(parent).getHash().equals(b.getPreviousHash())) {
                    blocks.add(i);
                }
            }
        }
        return blocks;
    }

    public int getChildrensCount(int parent) {
        int chils = 0;

        if (parent <= -1) {
            return 0;
        } else if (parent >= pool.size()) {
            throw new RuntimeException("Invalid index");
        } else {
            ArrayList<String> tmps = new ArrayList<>();
            tmps.add(getBlock(parent).getPreviousHash());
            for (int i = parent + 1; i < pool.size(); i++) {
                Block b = pool.get(i);
                if (tmps.contains(pool.get(parent).getPreviousHash())) {
                    tmps.add(b.getHash());
                }
            }
            chils += tmps.size() - 1;
        }
        return chils;
    }

    public int getLastVerifiedBlock() {
        return verified.get(verified_depth - 1);
    }

    public boolean putBlock(int i, Block b) {
        if (!isValidBlock(b)) {
            return false;
        }
        if(getDepth(i) < verified_depth) {
            return false;
        }
        pool.add(b);
        int depth = getDepth(pool.size() - 1);
        System.out.println("ADD BLOCK - " + (pool.size() - 1));

        if(depth - verified_depth >= verify_chain_count) {
            int delta = depth - verified_depth;
            verified_depth += delta;
            for(int j = 0 ; j < delta ; j ++) {
                verified.add(0);
            }
            verified.set(verified.size() - 1, pool.size() - 1);
            Block btmp = b;
            int tmp = i;
            int j = verified.size() - 2;

            while(tmp != getGenesisBlock()) {
                tmp = getBlockFromHash(btmp.getPreviousHash());
                btmp = pool.get(tmp);
                verified.set(j--, tmp);
            }
        }
        updateBlockGUI();
        return true;
    }

    private void updateBlockGUI() {
        MainFrame mf = main.getGraphic().getMainFrame();

        mf.clearChian();
        mf.addChainNode(Integer.toString(getGenesisBlock()));
        mf.setChainRoot(Integer.toString(getGenesisBlock()));

        ArrayList<TwoInt> all = new ArrayList<>();

        for(int i : getChildrens(getGenesisBlock())) {
            all.add(new TwoInt(getGenesisBlock(), i));
        }
        while(!all.isEmpty()) {
            TwoInt ind = all.remove(0);
            mf.addChainNode(Integer.toString(ind.i2));
            mf.addChainEdge(Integer.toString(ind.i1) + "." + Integer.toString(ind.i2), Integer.toString(ind.i1), Integer.toString(ind.i2));
            if(verified.contains(ind.i2)) {
                mf.setChainVerified(Integer.toString(ind.i2));
                mf.setChainEdgeHighlighted(Integer.toString(ind.i1) + "." + Integer.toString(ind.i2), true);
            }
            for(int i : getChildrens(ind.i2)) {
                all.add(new TwoInt(ind.i2, i));
            }
        }
    }

    public int getBlockFromHash(String hash) {
        if (hash == null)
            return -1;
        for (int i = 0; i < pool.size(); i++) {
            if (pool.get(i).getHash().equals(hash)) {
                return i;
            }
        }
        return -1;
    }

    public Block getBlock(int index) {
        return pool.get(index);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int diff) {
        this.difficulty = diff;
    }

    public int getVerifyChainCount() {
        return verify_chain_count;
    }

    public void setVerifyChainCount(int count) {
        this.verify_chain_count = count;
    }

    public boolean miningBlock(Block b, int power, boolean cancelMined) {
        int before_depth = 0;
        if (cancelMined) {
            before_depth = verified_depth;
        }
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!b.getHash().substring(0, difficulty).equals(target)) {
            if (before_depth != verified_depth) {
                return false;
            }
            try {
                Thread.sleep(power * 1);
            }catch(Exception ex) {
            }
            b.increaseNonce();
            b.recalculateHash();
        }
        return true;
    }

    private class TwoInt {
        int i1;
        int i2;
        public TwoInt(int i1, int i2) {
            this.i1 = i1;
            this.i2 = i2;
        }
    }
}
