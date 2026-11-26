package Ukol2;

public class Ukol2 {


    public static void main(String[] args) throws InterruptedException {


        Thread kornel = new Thread(new ZakleteDvere("Korny ", 10, 99));
        Thread hermi = new Thread(new ZakleteDvere("Hermi ", 100, 200));
        Thread martas = new Thread(new ZakleteDvere("Martas ", 200, 400));
        Thread sary = new Thread(new ZakleteDvere("Sary ", 700, 800));
        Thread ali = new Thread(new ZakleteDvere("Ali ", 300, 500));

        kornel.start();
        hermi.start();
        martas.start();
        sary.start();
        ali.start();

        kornel.join();
        hermi.join();
        martas.join();
        sary.join();
        ali.join();





    }

}
