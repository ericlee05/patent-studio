package com.ericlee.pstudio.alpha.global.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringUtil {
    public static <T> String joinStringWithComma(List<T> list) {
        return list.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    public static String summerizeString(String original, int max) {
        return original.length() <= max ? original : String.format(String.format("%%%ds...", max - 3), original);
    }
}
