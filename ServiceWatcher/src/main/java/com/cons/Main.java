package com.cons;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main(String[] args) {
        //System.out.println( "Hello World!" );
        Configuration conf = new Configuration();
        conf.init();
        if (conf.isValid()){
            System.out.println("Conf File Is Valid Init Started");
        }
    }
}
