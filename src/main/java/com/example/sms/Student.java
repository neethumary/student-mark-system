package com.example.sms;

public class Student {
    private final String name;
    // 10 subjects
    private final int s1, s2, s3, s4, s5, s6, s7, s8, s9, s10;

    public Student(String name, int s1, int s2, int s3, int s4, int s5, int s6, int s7, int s8, int s9, int s10) {
        this.name = name;
        this.s1 = s1; this.s2 = s2; this.s3 = s3; this.s4 = s4; this.s5 = s5;
        this.s6 = s6; this.s7 = s7; this.s8 = s8; this.s9 = s9; this.s10 = s10;
    }

    public String getName() { return name; }
    public int getS1() { return s1; }
    public int getS2() { return s2; }
    public int getS3() { return s3; }
    public int getS4() { return s4; }
    public int getS5() { return s5; }
    public int getS6() { return s6; }
    public int getS7() { return s7; }
    public int getS8() { return s8; }
    public int getS9() { return s9; }
    public int getS10() { return s10; }
}
