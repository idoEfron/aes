import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class AES3 {


    public AES3() {
    }


    private List<byte[]> randomizeKeys(){
        List<byte[]> keys = new ArrayList<>();
        SecureRandom random = new SecureRandom();
        byte[] key1 = new byte[16]; // 128 bits are converted to 16 bytes;
        random.nextBytes(key1);
        byte[] key2 = new byte[16]; // 128 bits are converted to 16 bytes;
        random.nextBytes(key2);

        keys.add(key1);
        keys.add(key2);

        return keys;
    }

    public void attack(String messagePath,String cipherPath, String output) throws IOException {
        Path path = Paths.get(messagePath);
        byte[] message = Files.readAllBytes(path);

        List<byte[]> keys = randomizeKeys();

        byte[][] key1 =  fillmatrix(keys.get(0),0);
        byte[][] key2 =  fillmatrix(keys.get(1),0);

        byte[] mid_cipher = iteration(message,key1);
        mid_cipher = iteration(mid_cipher,key2);

        byte[][] mid_cipherBlock = fillmatrix(mid_cipher,0);

        path = Paths.get(cipherPath);
        byte[] cipher = Files.readAllBytes(path);

        byte[][] cipherBlock = fillmatrix(cipher,0);

        byte[][] key3matrice = new byte[4][4];

        shiftUp(mid_cipherBlock);
        xorAction(key3matrice,mid_cipherBlock,cipherBlock);
        List<Byte> key3List = extractMatrix(key3matrice);
        byte[] key3 = new byte[key3List.size()];
        for(int i=0;i<key3.length;i++){
            key3[i] = key3List.get(i);
        }

        try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(key3);
        }
        catch (Exception e){
            System.out.println("error occured.");
        }

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

    private byte[] DeIteration(byte[] message, byte[][] key1) {

        List<Byte> solution = new ArrayList<>();

        int matrices = (message.length)/16;
        for(int i=0;i<matrices;i++){
            //fill matrix
            byte[][] bytesArray = fillmatrix(message,i*16);

            byte[][] solutionArray = new byte[4][4];

            xorAction(solutionArray,bytesArray,key1);

            bytesArray=shiftDown(solutionArray);

            solution.addAll(extractMatrix(bytesArray));
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

    /**
     *
     * @param keysPath
     * @param messagePath
     * @param outputFile
     * @throws IOException
     */
    public void decrypt(String keysPath, String messagePath, String outputFile) throws IOException {

        Path path = Paths.get(messagePath);
        byte[] message = Files.readAllBytes(path);

        byte[] plainText = new byte[message.length];

        path = Paths.get(keysPath);
        byte[] keys = Files.readAllBytes(path);
        byte[][] key1 = fillmatrix(keys,0);
        byte[][] key2 = fillmatrix(keys,16);
        byte[][] key3 = fillmatrix(keys,32);

        plainText = DeIteration(message,key3);
        plainText = DeIteration(plainText,key2);
        plainText = DeIteration(plainText,key1);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(plainText);
        }
        catch (Exception e){
            System.out.println("error occured.");
        }
        path = Paths.get(messagePath);
        byte[] temppp = Files.readAllBytes(path);
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



    public byte[][] shiftDown(byte[][]array) {

        for (int row = 0; row < array.length; row++) {
            int rowLength = array[row].length;
            // keep shift within bounds of the array
            int shift = row % 4;
            Byte[] tmp = new Byte[shift];
            for (int i = 0; i < shift; i++) {
                tmp[i] = array[3-i][row];
            }
            // shift like normal
            for (int col = 3; col >0; col--) {
                if(col-shift>=0) {
                    array[col][row] = array[col - shift][row];
                }
            }
            // copy back the "falle
            // n off" elements
            int n=tmp.length;
            Byte[] b = new Byte[tmp.length];
            int j = tmp.length;
            for (int i = 0; i < n; i++) {
                b[j - 1] = tmp[i];
                j = j - 1;
            }
            for (int i = 0; i < shift; i++) {
                array[i][row] = b[i];
            }
        }
        return array;
    }
    }
