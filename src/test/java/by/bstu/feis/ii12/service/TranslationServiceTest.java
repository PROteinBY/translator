package by.bstu.feis.ii12.service;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationServiceTest {

    private TranslationService translationService = new TranslationService();

    @Test
    public void translateTest() {
        Pattern p = Pattern.compile("(?<word>[а-яА-Я]+)");
        Matcher m = p.matcher("fucking сука beach бляцкая давай поговорим о ебле с конями на велосипеде а еще давай поговорим о твоих выебонах");
        while (m.find()) {
            System.out.println("KEK!");
            System.out.println(m.group("word"));
        }
    }

}
