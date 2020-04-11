import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String arge[]) throws IOException {



        AES3 aes = new AES3();
        Scanner in = new Scanner(System.in);
        String[] arrManu = new String[]{"-e", "-d", "-b"};
        for (int i = 0; i < arrManu.length; i++)
            System.out.println(arrManu[i]);

        boolean quit = false;
        String menuItem;
        List<String> strArr=null;
        do {
            menuItem = in.nextLine();

            switch (menuItem) {

                case "-e":

                    System.out.println("You've chosen item #1");
                    strArr = new ArrayList<>();
                    strArr =choose();
                    aes.encrypt(strArr.get(0),strArr.get(1),strArr.get(2));
                    quit=true;
                    System.out.println("the end of encrypt");
                    break;

                case "-d":

                    System.out.println("You've chosen item #2");
                    strArr = new ArrayList<>();
                    strArr =choose();
                    aes.decrypt(strArr.get(0) ,strArr.get(1),strArr.get(2));
                    quit=true;
                    System.out.println("the end of decrypt");
                    break;

                case "-b":

                    System.out.println("You've chosen item #3");

                    boolean quitSub = false;
                    String SubmenuItem;
                    String m = null;
                    String c = null;
                    String o = null;
                    do {
                        String[] subMenuE = new String[]{"-m", "-c", "-o"};
                        for (int i = 0; i < subMenuE.length; i++)
                            System.out.println(subMenuE[i]);
                        Scanner inSub = new Scanner(System.in);
                        SubmenuItem = inSub.nextLine();

                        switch (SubmenuItem) {
                            case "-m":
                                m=inSub.nextLine();
                                break;
                            case "-c":
                                c=inSub.nextLine();
                                break;
                            case "-o":
                                o=inSub.nextLine();
                                break;
                            default:
                                System.out.println("Invalid choice.");

                        }
                        if(m!=null&&c!=null&&o!=null){
                            quitSub=true;
                        }
                    } while (!quitSub);

                    System.out.println("the end of break");
                    break;

                default:

                    System.out.println("Invalid choice.");

            }

        } while (!quit);

        System.out.println("Bye-bye!");

    }


    private static List<String> choose() {

        boolean quitSub = false;
        String SubmenuItem;
        String k = null;
        String i = null;
        String o = null;
        do {
            String[] subMenuE = new String[]{"-k", "-i", "-o"};
            for (int j = 0; j < subMenuE.length; j++)
                System.out.println(subMenuE[j]);

            Scanner inSub = new Scanner(System.in);
            SubmenuItem = inSub.nextLine();

            switch (SubmenuItem) {
                case "-k":
                    k=inSub.nextLine();
                    break;
                case "-i":
                    i=inSub.nextLine();
                    break;
                case "-o":
                    o=inSub.nextLine();
                    break;
                default:

                    System.out.println("Invalid choice.");

            }
            if(k!=null&&i!=null&&o!=null){
                quitSub=true;
            }
        } while (!quitSub);
        List<String> listStr = new ArrayList<>();
        listStr.add(k);
        listStr.add(i);
        listStr.add(o);
        return listStr;

    }
}

