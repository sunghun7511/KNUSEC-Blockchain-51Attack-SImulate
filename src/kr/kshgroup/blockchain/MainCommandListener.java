package kr.kshgroup.blockchain;

import kr.kshgroup.blockchain.blockchain.UserThread;
import kr.kshgroup.blockchain.graphic.CommandListener;

import javax.swing.*;

public class MainCommandListener implements CommandListener {
    private EntryMain main;
    public MainCommandListener(EntryMain main) {
        this.main = main;
    }

    @Override
    public void onCommand(String label, String[] args) {
        if (label.equalsIgnoreCase("user")) {
            if(args[0].equalsIgnoreCase("add")) {
                String username = args[1];
                main.getUserDatas().addUser(username);
            } else if(args[0].equalsIgnoreCase("mine")) {
                String username = args[1];
                int parent = Integer.parseInt(args[2]);
                String data = args[3];
                for(int i = 4 ; i < args.length ; i ++){
                    data += " " + args[i];
                }
                main.getUserDatas().getUser(username).mine(data, parent, true);
            } else if(args[0].equalsIgnoreCase("power")) {
                String username = args[1];
                int power = Integer.parseInt(args[2]);
                main.getUserDatas().getUser(username).setPower(power);
            } else if(args[0].equalsIgnoreCase("pause")) {
                String username = args[1];
                UserThread ut = main.getUserDatas().getUser(username);
                ut.setPause(!ut.getPause());
            }
        } else if(label.equalsIgnoreCase("block")) {

        } else if(label.equalsIgnoreCase("chain")) {
            if (args[0].equalsIgnoreCase("difficulty")) {
                int diff = Integer.parseInt(args[1]);
                main.getBlockChain().setDifficulty(diff);
            } else if(args[0].equalsIgnoreCase("verify")) {
                int count = Integer.parseInt(args[1]);
                main.getBlockChain().setVerifyChainCount(count);
            }
        } else {
            alert("Input commands!\nuser, block, chain");
        }
    }

    private void alert(String message) {
        JOptionPane.showMessageDialog( null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
