package com.github.thedeathlycow.calyx.kt;

import java.util.List;
import java.util.Optional;

class Example {
    public static void main(String[] args) {
        Grammar grammar = new Grammar(Options.defaultStrict, g -> {
            g.start(List.of("{greeting}", "{world_phrase}."))
                    .rule("greeting", List.of("Hello", "Hi", "Hey", "Yo"))
                    .rule("world_phrase", List.of("{happy_adj} world", "{sad_adj} world", "world"))
                    .rule("happy_adj", List.of("amazing", "bright", "beautiful"))
                    .rule("sad_adj", List.of("cruel", "miserable"));
            return null;
        });
    }
}