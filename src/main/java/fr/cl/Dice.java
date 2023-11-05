package fr.cl;

import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public record Dice(int number, int dice, int mod) {
    public static final Pattern pattern = Pattern.compile("^(\\d*)?[d|D](4|6|12|20|100)\\+?(\\d*)?");

    public int throwDice() {
        return IntStream.range(0, number).map(i -> ThreadLocalRandom.current().nextInt(dice)).sum() + mod;
    }

    @Override
    public String toString() {
        return "Lancer de " + number + " dés " + dice + (mod == 0 ? "" : " avec un modificateur de +" + mod) + " (" + number + "d" + dice + "+" + mod +")";
    }

    public static Dice createFromMatcher(Matcher matcher) {

        var number = 1;
        var nbr = matcher.group(1);
        if (!nbr.isEmpty()) {
            number = Integer.parseInt(matcher.group(1));
        }

        var dice = Integer.parseInt(matcher.group(2));

        var mod = 0;
        var strMod = matcher.group(3);
        if (!strMod.isEmpty()) {
            mod = Integer.parseInt(strMod);
        }

        return new Dice(number, dice, mod);
    }
}
