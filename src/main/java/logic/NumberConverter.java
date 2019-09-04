package main.java.logic;

import java.util.stream.IntStream;

public class NumberConverter {
    public int charToNumber(char letter) {
        char[] letters = {'Y', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

        return IntStream
                .range(0, letters.length)
                .filter(j -> letter == letters[j])
                .findFirst()
                .orElse(0);
    }
}
