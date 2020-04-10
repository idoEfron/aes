import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AES3 {


    public AES3() {
    }

    public void encrypt(String keysPath, String messagePath, String outputFile) throws IOException {

        Path path = Paths.get(messagePath);
        byte[] message = Files.readAllBytes(path);

        byte[] cipherText = new byte[message.length];

        path = Paths.get(keysPath);
        byte[] keys = Files.readAllBytes(path);
        byte[][] key1 = fillmatrix(keys,0);
        byte[][] key2 = fillmatrix(keys,16);
        byte[][] key3 = fillmatrix(keys,32);

        cipherText = iteration(message,key1);
        cipherText = iteration(cipherText,key2);
        cipherText = iteration(cipherText,key3);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(cipherText);
        }
        catch (Exception e){
            System.out.println("error occured.");
        }

    }

    private byte[] iteration(byte[] message, byte[][] key1) {

        List<Byte> solution = new ArrayList<>();

        int matrices = (message.length)/16;
        for(int i=0;i<matrices;i++){
            //fill matrix
            byte[][] bytesArray = fillmatrix(message,i*16);

            byte[][] solutionArray = new byte[4][4];

            bytesArray=shiftUp(bytesArray);

            xorAction(solutionArray,bytesArray,key1);

            solution.addAll(extractMatrix(solutionArray));
        }

        //copy final array
        byte[] solutionFinal = new byte[message.length];
        for(int i=0; i<solution.size();i++){
            solutionFinal[i]= solution.get(i);
        }

        return  solutionFinal;

    }

    private void xorAction(byte[][] solutionArray, byte[][] bytesArray, byte[][] key){
        for (int i=0;i<4;i++){
            for (int j = 0; j < 4; j++) {
                solutionArray[j][i] = (byte) (bytesArray[j][i]^key[j][i]);
            }
        }
    }

    public void printArray(byte[][] array){
        for (byte[] row : array) {
            for (byte col : row) {
                System.out.print(" " + (int) col);
            }
            System.out.println();
        }
    }

    private byte[][] fillmatrix(byte[] message,int start) {
        byte[][] bytesArray;
        bytesArray = new byte[4][4];
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                bytesArray[j][i] = message[start];
                start++;
            }
        }
        return bytesArray;
    }

    private List<Byte> extractMatrix(byte[][] matrix) {
        List<Byte> bytesArray;
        bytesArray = new ArrayList<>();
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                bytesArray.add(matrix[j][i]);
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
    public byte[][] shiftUp(byte[][]array){
        for (int row = 0; row < array.length; row++) {
            int rowLength = array[row].length;

            // keep shift within bounds of the array
            int  shift= row % 4;

            byte[] tmp = new byte[shift];
            for (int i = 0; i < shift; i++) {
                tmp[i] = array[i][row];
            }
            // shift like normal
            for (int col = 0; col < rowLength - shift; col++) {
                array[col][row] = array[col+shift][row];
            }

            // copy back the "fallen off" elements
            for (int i = 0; i < shift; i++) {
                array[i + (rowLength - shift)][row] = tmp[i];
            }
        }
        return array;
    }
}
