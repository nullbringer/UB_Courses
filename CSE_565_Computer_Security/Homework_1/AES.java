import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static SecretKeySpec secretKey;


    private static final byte[] plainText = { (byte) 0x32, (byte) 0x43, (byte) 0xf6, (byte) 0xa8, (byte) 0x88,
            (byte) 0x5a, (byte) 0x30, (byte) 0x8d, (byte) 0x31, (byte) 0x31, (byte) 0x98, (byte) 0xa2, (byte) 0xe0,
            (byte) 0x37, (byte) 0x07, (byte) 0x34 };

    private static final byte[] encKey = { (byte) 0x2b, (byte) 0x7e, (byte) 0x15, (byte) 0x16, (byte) 0x28,
            (byte) 0xae, (byte) 0xd2, (byte) 0xa6, (byte) 0xab, (byte) 0xf7, (byte) 0x15, (byte) 0x88, (byte) 0x09,
            (byte) 0xcf, (byte) 0x4f, (byte) 0x3c };



    public static void printOutput(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (byte b : bytes) {
            sb.append(String.format("0x%02X ", b));
        }
        sb.append("]");
        System.out.println(sb.toString());
    }


    public static void main(String[] args)
    {

        byte[] computedCipher = null;

        try
        {
            secretKey = new SecretKeySpec(encKey, 0,16,"AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/noPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            long startTime = System.currentTimeMillis();

            for (int i=0;i<10000;i++){
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                computedCipher = cipher.doFinal(plainText);


            }

            System.out.println("Execution Time: " + Long.toString(System.currentTimeMillis()-startTime) + " ms");


        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.toString());
        }

        //printOutput(plainText);
        //printOutput(computedCipher);

    }
}