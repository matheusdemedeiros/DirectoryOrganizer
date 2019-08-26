/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directoryorg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Matheus
 */
public class Util {

    //inicializando o array de diretorios que ja foram criados
    private static ArrayList<String> verifiedExtensions = new ArrayList<>();
    //caminho que sera informado pelo usuario
    private static String PATH /* = "c:\\users\\matheus\\downloads\\teste\\"*/;
    //chamada para o cmd
    private static String CMD = "cmd /c";
    //transformando o path em file
    private static File file;
    //adicionando os arquivos existentes no diretorio em um array
    private static File files[];
    //inicializando a variável extension que mais tarde irá 
    //capturar as extensoes de todos os arquivos existentes no diretorio
    private static String extension = null;
    //inicializando a variavel command que sera responsavel por formar os comandos a serem executados pelo cmd
    private static String command;

    private static void makeDirectory(String extension) {
        if (!checkParsedExtensions(extension)) {
            command = CMD + " mkdir " + PATH + extension;
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao criar o diretório:\n" + ex.getMessage());
            }
            verifiedExtensions.add(extension);
        }
    }

    private static boolean checkParsedExtensions(String extension) {
        boolean retorno = false;
        for (int i = 0; i < verifiedExtensions.size(); i++) {
            if (verifiedExtensions.get(i).equalsIgnoreCase(extension)) {
                retorno = true;
            }
        }
        return retorno;
    }

    private static void inputPath() {
        PATH = System.getProperty("user.dir");
        //PATH para testes de implementacao
        //PATH = new String(JOptionPane.showInputDialog("Informe o caminho do diretório que deseja organizar:"))*/;
        PATH += "\\";
        file = new File(PATH);
    }

    private static void listFilesGetExtensions() {
        files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file1 = files[i];
            if (!file1.getName().equals("DirectoryOrganizer.jar")) {
                extension = generateExtension(file1.getName());
                if (!extension.equals("-")) {
                    //maneira anterior(so funciona com extensões que sejam representadas por 3 caracteres)
                    //extension = file1.getName().substring(file1.getName().length() - 3, file1.getName().length());
                    makeDirectory(extension.toUpperCase());
                }
            }
        }
    }

    public static void organize() {
        inputPath();
        listFilesGetExtensions();
        try {
            moveFiles();
        } catch (InterruptedException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void moveFiles() throws InterruptedException {
        int a = 0;
        for (int i = 0; i < verifiedExtensions.size(); i++) {
            //command = CMD + " mkdir " + PATH + extension;
            command = CMD + " move " + PATH + "*." + verifiedExtensions.get(i).toString() + " " + PATH + verifiedExtensions.get(i).toString() + "\\";
            a++;
            try {
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao mover o(s) arquivo(s): " + ex.getMessage());
            }
        }
    }

    private static String generateExtension(String nameFile) {
        String result = "-";
        int a = nameFile.length();
        for (int i = a; i > 0; i--) {
            if (nameFile.substring(i).startsWith(".")) {
                result = nameFile.substring(i + 1);
                //System.out.println("substring ex: " + result);
                break;
            }
        }
        return result;
    }
}
