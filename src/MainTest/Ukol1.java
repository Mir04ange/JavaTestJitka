package MainTest;

public class Ukol1 {
    public static void main(String[] args) {
    Thread Oli = new Thread(()->{
        System.out.println("Oli rika, ze ma hotovo");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });













    }
}
