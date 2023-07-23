package ru.recreation.recreationassistant.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class ListUtils {
    public <T> List<T> pickNRandom(List<T> lst, int n) {
        List<T> copy = new ArrayList<T>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

}
