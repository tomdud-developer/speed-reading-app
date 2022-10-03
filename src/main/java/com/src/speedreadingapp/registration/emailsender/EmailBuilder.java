package com.src.speedreadingapp.registration.emailsender;

public class EmailBuilder {
    public static String buildEmail(String name, String link) {
        return "Cześć " + name + ". Rejestrujesz się w serwisie do nauki szybkiego czytania. To twój link aktywacyjny: " + link;
    }
}
