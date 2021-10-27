package com.github.klyser8.mobmadness;

public class JavaTests {

    public static void main(String[] args) {
        String s = "|||||||||||||";
        s = s.replaceAll(".", "$0\\$");
        s = "$" + s;
        s = s.substring(0, s.length() - 1);
        System.out.println(s);
    }

}
