package es.juanito.institutos.config.auth.listeners;

import java.util.concurrent.atomic.AtomicInteger;

public class SecurityStats {

    public static final AtomicInteger totalLogins = new AtomicInteger(0);
    public static final AtomicInteger adminLogins = new AtomicInteger(0);
    public static final AtomicInteger userLogins = new AtomicInteger(0);
    public static final AtomicInteger totalLogouts = new AtomicInteger(0);
}
