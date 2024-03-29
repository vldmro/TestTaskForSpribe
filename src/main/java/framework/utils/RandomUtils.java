package framework.utils;

import java.util.Random;

public class RandomUtils {

    public static int randomValidAgeString(){
        return new Random().nextInt(45) + 16;
    }
}
