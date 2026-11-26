package MainTest;

public class Ukol1 {
    public static void main(String[] args) {
    Thread oli = new Thread(()->{
        System.out.println("Oli rika, ze ma hotovo");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });


    Thread pepa = new Thread(()->{

        System.out.println("Pepik dela jidlo");



        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


    });


    Thread kaja = new Thread(()->{

        System.out.println("Princ kaja privital vsechny hosty");


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    });

    oli.start();
    pepa.start();
    kaja.start();

    try {
        oli.join();
        pepa.join();
        kaja.join();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }


        System.out.println("Královna jita děkuje za pripravy");




    }
}
