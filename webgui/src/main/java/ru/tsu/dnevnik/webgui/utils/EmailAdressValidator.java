package ru.tsu.dnevnik.webgui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alexey on 03.02.2015.
 */
public final class EmailAdressValidator {
    private static final Pattern pattern = Pattern.compile("^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|mail|[a-z][a-z])$");
    public static boolean  check(String email){
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches())
            return true;
        else
            return false;
    }
}
