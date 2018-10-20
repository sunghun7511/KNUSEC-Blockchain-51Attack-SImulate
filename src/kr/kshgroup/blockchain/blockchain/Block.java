package kr.kshgroup.blockchain.blockchain;

import kr.kshgroup.blockchain.utils.HashUtils;

public final class Block {

    private final String prev_hash;
    private final String data;
    private final long time;

    private final long uid;

    private long nonce;
    private String hash;

    public Block(long uid, String data, String prev_hash) {
        this.data = data;
        this.prev_hash = prev_hash;
        this.time = System.currentTimeMillis();

        this.uid = uid;

        this.hash = calculateHash();
    }

    private String calculateHash() {
        return HashUtils.SHA256(prev_hash + Long.toString(time) + data + Long.toString(nonce));
    }

    public void recalculateHash() {
        this.hash = calculateHash();
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return prev_hash;
    }

    public String getData() {
        return data;
    }

    public long getTime() {
        return time;
    }

    public long getNonce() {
        return nonce;
    }

    public long getUID() {
        return uid;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public void increaseNonce() {
        this.nonce ++;
    }
}
