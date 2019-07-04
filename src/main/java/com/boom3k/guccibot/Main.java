package com.boom3k.guccibot;

import com.boom3k.guccibot.Bot.BotClient;
import org.apache.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    Logger logger = Logger.getLogger(Main.class);

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        System.setProperty("appname", Main.class.getName());
        System.setProperty("current.date.time", dateFormat.format(new Date()));
    }

    public static void main(String[] args) {
        BotClient.initializeClient();
    }

    static void explainReact() {
        System.out.println("**********Flux and Mono**********");
        System.out.println("Mono.just(\"Hello World\")");
        System.out.println("Flux.just(\"Hello\",\"World\")");
        System.out.println("**********Flux and Mono End**********\n");

        System.out.println("**********MAP**********");
        System.out.println("Mono-> map(String::length).subscribe(System.out::println) - returns:");
        Mono.just("Hello World")
                .map(String::length)
                .subscribe(System.out::println);
        System.out.println();
        System.out.println("Flux-> map(String::length).subscribe(System.out::println) - returns:");
        Flux.just("Hello", "World")
                .map(String::length)
                .subscribe(System.out::println);
        System.out.println("*******END MAP*******\n");

        System.out.println("************FLAT MAP************");
        System.out.println("Mono-> flatMap(aString -> Mono.just(aString.length())).subscribe(System.out::println) - returns:");
        Mono.just("Hello World")
                .flatMap(aString -> Mono.just(aString.length()))
                .subscribe(System.out::println); // prints 11
        System.out.println();

        System.out.println("Flux-> flatMap(aString -> Flux.just(aString.length(), 42)).subscribe(System.out::println) - returns:");
        Flux.just("Hello", "World")
                .flatMap(aString -> Flux.just(aString.length(), 42))
                .subscribe(System.out::println);
        System.out.println("**************END FLAT MAP**************\n");

        System.out.println("************Filter************");
        System.out.println("Mono-> filter(aString -> aString.equals(\"Hello\")).subscribe(System.out::println) - returns:");
        Mono.just("Hello World")
                .filter(aString -> aString.equals("Hello"))
                .subscribe(System.out::println); // prints nothing
        System.out.println();

        System.out.println("Flux-> filter(aString -> aString.equals(\"Hello\")).subscribe(System.out::println) - returns:");
        Flux.just("Hello", "World")
                .filter(aString -> aString.equals("Hello"))
                .subscribe(System.out::println); // prints "Hello"
        System.out.println("**************END Filter**************\n");

        System.out.println("************FilterWhen************");
        System.out.println("Mono-> filterWhen(aString -> Mono.just(aString.equals(\"Hello\"))).subscribe(System.out::println) - returns:");
        Mono.just("Hello World")
                .filterWhen(aString -> Mono.just(aString.equals("Hello")))
                .subscribe(System.out::println); // prints nothing
        System.out.println();

        System.out.println("Flux-> filterWhen(aString -> Mono.just(aString.equals(\"Hello\"))).subscribe(System.out::println) - returns:");
        Flux.just("Hello", "World")
                .filterWhen(aString -> Mono.just(aString.equals("Hello")))
                .subscribe(System.out::println); // prints "Hello"
        System.out.println("**************END FilterWhen**************\n");
    }

    static void java12StringMethods() {
        String rawString = "   !Twitch Boom3k   ";
        String string2 = rawString;
        String string3 = rawString;
        String string4 = rawString;

        System.out.println("rawString == \"" + rawString + "\"");

        System.out.println("rawString.strip() == \"" + rawString.strip() + "\"");

        System.out.println("rawString.startsWith(\"!Twitch\") == \"" + string2.startsWith("!Twitch") + "\"");
        System.out.println("rawString.startsWith(\"   \") == \"" + string2.startsWith("   ") + "\"");
        System.out.println("rawString.startsWith(\"Boom3k\") == \"" + string2.startsWith("Boom") + "\"");
        System.out.println();


        System.out.println("rawString.endsWith(\"Boom3k\") == \"" + string2.endsWith("Boom3k") + "\"");
        System.out.println("rawString.endsWith(\"   \") == \"" + string2.endsWith("   ") + "\"");


        System.out.println();

        System.out.println("rawString.stripLeading() == \"" + string3.stripLeading() + "\"");
        System.out.println("rawString.stripTrailing() == \"" + string4.stripTrailing() + "\"");
    }

}
