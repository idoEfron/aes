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

        Path path = Paths.get(messagePath);
        byte[] message = Files.readAllBytes(path);

        byte[] cipherText = new byte[message.length];

        path = Paths.get(keysPath);
        byte[] keys = Files.readAllBytes(path);
        byte[][] key1 = fillmatrix(keys);

        firstIteration(message,key1);



        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(cipherText);
        }
        catch (Exception e){
            System.out.println("error occured.");
        }

    }

    private byte[] firstIteration(byte[] message, byte[][] key1) {
        byte[] solution = null;

        //fill matrix
        byte[][] bytesArray = fillmatrix(message);

        byte[][] solutionArray = new byte[4][4];

        int cell = 0;
        for (int i=0;i<4;i++){
            for (int j = 0; j < 4; j++) {
                solutionArray[j][i] = (byte) (bytesArray[j][i]^key1[j][i]);
            }
        }
        solution = extractMatrix(solutionArray);

        return solution;


    }

    private byte[][] fillmatrix(byte[] message) {
        byte[][] bytesArray;
        bytesArray = new byte[4][4];
        int messageCell =0;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                bytesArray[j][i] = message[messageCell];
                messageCell++;
            }
        }
        return bytesArray;
    }

    private byte[] extractMatrix(byte[][] matrix) {
        byte[] bytesArray;
        bytesArray = new byte[16];
        int messageCell =0;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                bytesArray[messageCell] = matrix[j][i];
                messageCell++;
            }
        }
        return bytesArray;
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
