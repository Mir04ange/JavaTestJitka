/*
 * JAVA THREADS – TAHÁK S KOMENTÁŘI
 *
 * Soubor je určený k učení na písemku. Obsahuje základní práci s vlákny,
 * jejich spouštění, čekání, přerušení a bezpečnou práci se sdílenými daty.
 *
 * Spuštění:
 *   javac ThreadTahak.java
 *   java ThreadTahak
 */

public class ThreadMirinka {

    public static void main(String[] args) {
        // Ukázky můžeš postupně odkomentovat a vyzkoušet.
        ukazkaStartAJoin();
        // ukazkaRunnable();
        // ukazkaSleep();
        // ukazkaInterrupt();
        // ukazkaSdilenaData();
    }

    // ------------------------------------------------------------
    // 1. CO JE THREAD?
    // ------------------------------------------------------------
    // Thread = vlákno. Umožňuje vykonávat více částí programu "současně".
    // Každé vlákno má vlastní běh programu, ale může sdílet stejná data.
    //
    // Příklad využití:
    // - hra: jedno vlákno řeší pohyb, jiné zvuky nebo časovač,
    // - stahování souboru bez zamrznutí aplikace,
    // - obsluha více úloh najednou.

    // ------------------------------------------------------------
    // 2. VYTVOŘENÍ VLÁKNA POMOCÍ EXTENDS THREAD
    // ------------------------------------------------------------
    static class MojeVlakno extends Thread {
        @Override
        public void run() {
            // Kód, který se vykoná ve vedlejším vlákně.
            System.out.println("Běží: " + getName());
        }
    }

    // ------------------------------------------------------------
    // 3. VYTVOŘENÍ VLÁKNA POMOCÍ IMPLEMENTS RUNNABLE
    // ------------------------------------------------------------
    // Runnable představuje úkol, který má vlákno vykonat.
    // Často je lepší než extends Thread, protože třída může dědit
    // ještě z jiné třídy.
    static class MujUkol implements Runnable {
        @Override
        public void run() {
            System.out.println("Úkol běží ve vlákně: "
                    + Thread.currentThread().getName());
        }
    }

    static void ukazkaRunnable() {
        Thread vlakno = new Thread(new MujUkol());
        vlakno.start(); // Spustí nový běh vlákna.
    }

    // ------------------------------------------------------------
    // 4. start() VS run()
    // ------------------------------------------------------------
    // start() = vytvoří/spustí nové vlákno a v něm zavolá run().
    // run()   = zavolá metodu normálně jako běžnou metodu.
    //           Nevytvoří nové vlákno!
    //
    // Správně:   vlakno.start();
    // Ne pro souběžný běh: vlakno.run();

    // ------------------------------------------------------------
    // 5. join() – ČEKÁNÍ NA DOKONČENÍ VLÁKNA
    // ------------------------------------------------------------
    // join() způsobí, že aktuální vlákno čeká, dokud dané vlákno neskončí.
    // Hodí se, když potřebujeme výsledek práce jiného vlákna.
    //
    // Příklad: nejdřív se stáhnou data a až potom se zpracují.
    static void ukazkaStartAJoin() {
        Thread vlakno = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("Pracovní vlákno: " + i);
            }
        });

        vlakno.start();

        try {
            vlakno.join(); // Main počká, až pracovní vlákno skončí.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Zachová informaci o přerušení.
        }

        System.out.println("Vlákno skončilo, pokračuji dál.");
    }

    // ------------------------------------------------------------
    // 6. sleep() – PAUZA
    // ------------------------------------------------------------
    // Thread.sleep(1000) pozastaví AKTUÁLNÍ vlákno na přibližně 1 sekundu.
    // Čas se zadává v milisekundách: 1000 ms = 1 sekunda.
    // sleep() neukončí vlákno, jen ho na chvíli uspí.
    static void ukazkaSleep() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    // ------------------------------------------------------------
    // 7. interrupt() – POŽÁDÁNÍ O PŘERUŠENÍ
    // ------------------------------------------------------------
    // interrupt() není násilné zabití vlákna.
    // Nastaví příznak přerušení; pokud vlákno čeká ve sleep()/join(),
    // může dostat InterruptedException.
    // Vlákno by mělo přerušení správně zpracovat a ukončit se.
    static void ukazkaInterrupt() throws InterruptedException {
        Thread vlakno = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Pracuji...");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    // Při výjimce se příznak přerušení obvykle vymaže.
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Vlákno se ukončuje.");
        });

        vlakno.start();
        Thread.sleep(1000);
        vlakno.interrupt(); // Požádá pracovní vlákno o ukončení.
        vlakno.join();
    }

    // ------------------------------------------------------------
    // 8. UŽITEČNÉ METODY THREAD
    // ------------------------------------------------------------
    static void uzitecneMetody() throws InterruptedException {
        Thread t = new Thread(() -> System.out.println("Ahoj"));

        t.setName("MojeVlakno"); // Nastaví jméno vlákna.
        System.out.println(t.getName()); // Vrátí jméno vlákna.
        System.out.println(t.isAlive()); // Je vlákno právě spuštěné a neskončilo?

        t.start();
        t.join();

        System.out.println(t.isAlive()); // Po dokončení bude false.
        System.out.println(Thread.currentThread().getName()); // Název aktuálního vlákna.
    }

    // ------------------------------------------------------------
    // 9. RACE CONDITION – SOUBĚH O PŘÍSTUP KE STEJNÝM DATŮM
    // ------------------------------------------------------------
    // Pokud více vláken mění stejnou proměnnou současně, může vzniknout
    // chyba. Například dvě vlákna navyšují čítač a některé změny se ztratí.
    //
    // synchronized zajistí, že danou část kódu provádí najednou
    // pouze jedno vlákno se stejným zámkem.
    static class BezpecnyCitac {
        private int hodnota = 0;

        public synchronized void zvys() {
            hodnota++;
        }

        public synchronized int getHodnota() {
            return hodnota;
        }
    }

    static void ukazkaSdilenaData() throws InterruptedException {
        BezpecnyCitac citac = new BezpecnyCitac();

        Thread a = new Thread(() -> {
            for (int i = 0; i < 1000; i++) citac.zvys();
        });

        Thread b = new Thread(() -> {
            for (int i = 0; i < 1000; i++) citac.zvys();
        });

        a.start();
        b.start();
        a.join();
        b.join();

        System.out.println("Výsledek: " + citac.getHodnota()); // 2000
    }

    // ------------------------------------------------------------
    // 10. DAEMON THREAD
    // ------------------------------------------------------------
    // Démonické vlákno je pomocné vlákno. JVM může skončit, když už běží
    // pouze démonická vlákna. Nastavuje se PŘED start().
    //
    // Thread t = new Thread(() -> { ... });
    // t.setDaemon(true);
    // t.start();

    // ------------------------------------------------------------
    // 11. PRIORITY – PRIORITA VLÁKNA
    // ------------------------------------------------------------
    // t.setPriority(Thread.MIN_PRIORITY);  // 1
    // t.setPriority(Thread.NORM_PRIORITY); // 5 – běžná priorita
    // t.setPriority(Thread.MAX_PRIORITY);  // 10
    // Priorita je pouze doporučení plánovači OS/JVM.
    // Nezaručuje, že vlákno poběží první nebo rychleji.

    // ------------------------------------------------------------
    // 12. LAMBDA – KRATŠÍ ZÁPIS
    // ------------------------------------------------------------
    // Thread t = new Thread(() -> {
    //     System.out.println("Běžím ve vlákně");
    // });
    // t.start();

    // ------------------------------------------------------------
    // 13. STAVY VLÁKNA (Thread.State)
    // ------------------------------------------------------------
    // NEW        – vytvořené, ale ještě nespouštěné
    // RUNNABLE   – připravené nebo právě běžící
    // BLOCKED    – čeká na zámek (monitor)
    // WAITING    – čeká bez časového limitu (např. join())
    // TIMED_WAITING – čeká omezenou dobu (např. sleep())
    // TERMINATED – dokončené
    //
    // t.getState() vrátí aktuální stav. Stav se může rychle měnit.

    // ------------------------------------------------------------
    // 14. EXECUTOR SERVICE – SPRÁVA VÍCE ÚKOLŮ
    // ------------------------------------------------------------
    // Pro větší programy se často používá pool vláken místo vytváření
    // nového Thread pro každý úkol.
    //
    // import java.util.concurrent.ExecutorService;
    // import java.util.concurrent.Executors;
    //
    // ExecutorService executor = Executors.newFixedThreadPool(3);
    // executor.submit(() -> System.out.println("Úkol běží"));
    // executor.shutdown(); // Nepřijímá nové úkoly, dokončí odeslané.
    //
    // Pozor: po použití executor vždy korektně ukonči.

    // ------------------------------------------------------------
    // 15. WAIT() A NOTIFY() – KOMUNIKACE PŘES ZÁMEK
    // ------------------------------------------------------------
    // wait() uspí vlákno a uvolní zámek objektu.
    // notify() probudí jedno vlákno čekající na stejném objektu.
    // Musí se volat uvnitř synchronized bloku/metody.
    // V praxi se podmínka kontroluje ve while smyčce, ne v if.
    //
    // synchronized (zamek) {
    //     while (!jePripraveno) {
    //         zamek.wait();
    //     }
    // }
    //
    // synchronized (zamek) {
    //     jePripraveno = true;
    //     zamek.notifyAll(); // Probudí všechna čekající vlákna.
    // }

    // ------------------------------------------------------------
    // RYCHLÉ OPAKOVÁNÍ PŘED PÍSEMKOU
    // ------------------------------------------------------------
    // start()     – spustí nové vlákno
    // run()       – obsah práce vlákna; přímé zavolání nové vlákno nespustí
    // sleep(ms)   – uspí aktuální vlákno
    // join()      – počká na dokončení jiného vlákna
    // interrupt() – požádá vlákno o přerušení
    // isAlive()   – zjistí, zda vlákno běží / ještě neskončilo
    // getName()   – vrátí jméno vlákna
    // setName()   – nastaví jméno vlákna
    // synchronized – chrání sdílenou část kódu před souběžným přístupem
    //
    // ČASTÉ CHYBY:
    // 1. Zavolat run() místo start().
    // 2. Zapomenout ošetřit InterruptedException.
    // 3. Myslet si, že interrupt() vlákno okamžitě zabije.
    // 4. Používat sdílené proměnné bez ochrany.
    // 5. Zavolat start() dvakrát na stejném objektu Thread – to nejde.
}
