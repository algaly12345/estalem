package com.samm.estalem.Util;

public  class CheckEmpty {
    public static class isEmpty {
        public static boolean CheckEmpty(String s1) {
            if (s1.equals(""))
                return false;
            else
                return true;
        }

        public static boolean CheckEmpty(String s1, String s2) {
            if (s1.equals("") || s2.equals(""))
            {
                return false;}

            else
                return true;
        }

        public static boolean CheckEmpty(String s1, String s2, String s3) {
            if (s1.equals("") || s2.equals("") || s3.equals(""))
                return false;
            else
                return true;
        }

        public static   boolean CheckEmpty(String s1, String s2, String s3, String s4) {
            if (s1.equals("") || s2.equals("") || s3.equals("") || s4.equals(""))
                return false;
            else
                return true;
        }

        public static boolean CheckEmpty(String s1, String s2, String s3, String s4, String s5) {
            if (s1.isEmpty() || s2.equals("") || s3.equals("") || s4.equals("") || s5.equals(""))
                return false;
            else
                return true;
        }

        public static boolean CheckEmpty(String s1, String s2, String s3, String s4, String s5, String s6) {
            if (s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("") || s5.equals("") || s6.equals(""))
                return false;
            else
                return true;
        }

        public static boolean CheckEmpty(String s1, String s2, String s3, String s4, String s5, String s6, String s7) {
            if (s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("") || s5.equals("") || s6.equals("") || s7.equals(""))
                return false;
            else
                return true;
        }
    }
}