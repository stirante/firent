package com.stirante.firent.utils;

import java.util.ArrayList;
import java.util.Collections;

public class StringUtils {

    public static ArrayList<String> asList(String... strings) {
        ArrayList<String> r = new ArrayList<>();
        Collections.addAll(r, strings);
        return r;
    }

}
