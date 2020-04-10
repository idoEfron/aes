import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AES3 {



    public AES3() {


    }

    public void encrypt(String keysPath, String messagePath, String outputFile) throws IOException {

        byte[] cipherText = null;

        Path path = Paths.get(messagePath);
        byte[] message = Files.readAllBytes(path);

        path = Paths.get(keysPath);
        byte[] keys = Files.readAllBytes(path);


        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(cipherText);
        }
        catch (Exception e){
            System.out.println("error occured.");
        }

    }

    public void decrypt(String keysPath, String messagePath, String outputFile) throws IOException {

        byte[] plainText = null;

        Path path = Paths.get(messagePath);
        byte[] message = Files.readAllBytes(path);

        path = Paths.get(keysPath);
        byte[] keys = Files.readAllBytes(path);


        path = Paths.get(outputFile);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(plainText);
        }
        catch (Exception e){
            System.out.println("error occured.");
        }
    }

}
