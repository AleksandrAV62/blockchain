package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.security.MessageDigest;
import java.util.Scanner;


public class NoobChain {

    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
		


        System.out.print("Enter how many zeros the hash must start with: ");
        Scanner sc = new Scanner(System.in);
        int difficulty = sc.nextInt();
        System.out.print("\n");
		
		

        ArrayList<Long> timeMain = new ArrayList<>();

        final int blockCount = 4;

        for (int i = 0; i <= blockCount; i++) {  
			
            if (blockchain.size() <= blockCount) {

                long start = System.currentTimeMillis(); // начало времени выполнения операции

                if (i == 0) {
                    Block block = new Block("0", new Date().getTime());
                    blockchain.add(block);

                } else {
                    Block block = new Block(blockchain.get(i - 1).getHash(), new Date().getTime());
                    blockchain.add(block);	
                }
                blockchain.get(i).maineBlock(difficulty);
				
                Thread.sleep(1000);
                long finish = System.currentTimeMillis(); // окончание времени выполнения операции
                long elapsed = (finish - start) / 1000; // время в секундах
                timeMain.add(elapsed); // запись данных в лист для дальнейшей печати
            }
        }

        for (int i = 0; i < blockchain.size(); i++) {
            if (blockchain.get(i).getHash().equals(blockchain.get(i).calculateHash())
                    &&
                    i == 0 ? blockchain.get(i).getPreviousHash().equals("0") :
                    blockchain.get(i - 1).getHash().equals(blockchain.get(i).getPreviousHash())) {

                System.out.println("Block:\n" +
                        "Id: " + (i+1) + "\n" +
                        "Timestamp: " + blockchain.get(i).getTimeStamp() + "\n" +
                        "Magic number:" + blockchain.get(i).getMagic() + "\n" +
                        "Hash of the previous block:\n" +
                        blockchain.get(i).getPreviousHash() + "\n" +
                        "Hash of the block:\n" +
                        blockchain.get(i).getHash() + "\n" +
                        "Block was generating for " + timeMain.get(i) + " seconds" + "\n");

            } else {
                System.out.println("bad");
            }
        }
    }
}

class Block implements Serializable {
	
	private static final long serialVersionUID = 1L; // вновь добавлен

    private String hash;            // кеш текущего блока
    private final String previousHash;    // кеш предыдущего блока
    private final long timeStamp;        // метка времени
    private int magic;              // вновь добавлен



    public Block(String previousHash, Long timeStamp) {

        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        timeStamp + // предыдущая была версия Long.toString
                        magic); // вновь добавлен , было Integer.toString
    }
// вновь добавлен
    public void maineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring( 0, difficulty).equals(target)) {
            magic ++;
            hash = calculateHash();
        }
    }



    public String getHash() {         
        return this.hash;
    }
    public String getPreviousHash() {
        return this.previousHash;
    }
    public long getTimeStamp() {
        return this.timeStamp;
    }
    public Integer getMagic() {
        return this.magic;
    }
}

class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
