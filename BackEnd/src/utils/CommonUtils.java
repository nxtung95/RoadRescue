package utils;

import java.util.Random;

public class CommonUtils {
    public static String generateRandomNum(int quantity) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        while (true) {
            int num = random.nextInt(8) + 1;
            otp.append(num);
            if (otp.length() >= quantity) {
                break;
            }
        }
//        System.out.println(verifyNum.toString());
        return otp.toString();
    }
}
