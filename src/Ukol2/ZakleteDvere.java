package Ukol2;

public class ZakleteDvere implements Runnable {
    
    String jmeno;
    int dolniMez;
    int horniMez;

    public ZakleteDvere(String jmeno, int dolniMez, int horniMez) {
        this.jmeno = jmeno;
        this.dolniMez = dolniMez;
        this.horniMez = horniMez;
    }

    boolean jePrvocislo(int cislo){
        if (cislo < 2) return false;
        for (int i = 2; i * i <= cislo ; i++) {

            if (cislo % i == 0) {
                return false;
            }
        }
        return true;
    }


    boolean jePalindrom(int cislo){
        String text = String.valueOf(cislo);

        String opacne = new StringBuilder(text).reverse().toString();

        return opacne.equals(text);


    }
    @Override
    public void run(){
        boolean xd= false ;

        for (int i = dolniMez; i < horniMez ; i++) {

            if (jePrvocislo(i) && jePalindrom(i)){

                System.out.println(jmeno + " " + "nasel cislo " + i);
                xd = true;
                break;
            }


        }
        if (!xd){
            System.out.println(jmeno +" nenasel cislo");
        }
    }




}
