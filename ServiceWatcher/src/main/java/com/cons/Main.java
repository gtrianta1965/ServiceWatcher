package com.cons;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.init();
        if (!conf.isValid()){
            System.out.println("Error reading configuration (" + conf.getError() + ")");
            System.exit(1);
        }
    }
}
