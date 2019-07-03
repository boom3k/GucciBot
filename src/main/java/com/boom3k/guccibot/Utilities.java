package com.boom3k.guccibot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utilities {

    static Logger logger = Logger.getLogger(Utilities.class);

    /**
     * @param title       title of the window
     * @param buttonTitle title of the button that selects the file
     * @return String of the file directory path
     */
    public static File getFileFromJFC(String startPath, String title, String buttonTitle) {

        JFileChooser jfc = new JFileChooser(startPath);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Zip File", "zip");
        jfc.setFileFilter(filter);
        jfc.setDialogTitle(title);

        int returnValue = jfc.showDialog(null, buttonTitle);
        if (returnValue != JFileChooser.CANCEL_OPTION) {
            return jfc.getSelectedFile();
        } else {
            System.out.println("No file selected, please rerun application and select a file.");
            System.exit(0);
            return null;
        }
    }

    /**
     * @return String of the file directory path
     */
    public static String getDirectoryPathFromJFC() {
        String result;

        JFileChooser jfc = new JFileChooser("C:");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setControlButtonsAreShown(true);
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Please select a directory";
            }
        });
        jfc.showOpenDialog(null);

        result = jfc.getSelectedFile().getAbsolutePath();

        return result;
    }

    /**
     * @param jsonFile the jsonFile that will be turned into a JsonObject
     * @return a JsonObject
     */
    public static JsonObject getJsonObject(File jsonFile) {
        try {
            return getJsonObject(new BufferedReader(new FileReader(jsonFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param reader BufferedReader of data from a fileInputStream
     * @return a JsonObject
     */
    public static JsonObject getJsonObject(BufferedReader reader) {
        return (JsonObject) new JsonParser().parse(reader);
    }

    public static String getJsonElementStringFromFile(File jsonFile, String jsonElement) throws FileNotFoundException {
        return getJsonObject(new java.io.File(jsonFile.getPath())).get(jsonElement).toString().replace("\"", "");
    }



    public static String encodeString(String trueString) {
        return Base64.getEncoder().encodeToString(trueString.getBytes());
    }

    public static String decodeString(String encodedString) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(encodedString);
        String decodedString = new String(bytes, UTF_8);
        return decodedString;
    }

    public static boolean invertBoolean(boolean bool) {
        if (bool == true) {
            return false;
        } else
            return true;
    }

    public static void executeCallables(ArrayList<Callable<Void>> callables, int poolSize) {
        try {
            logger.info("Executing " + callables.size() + " threads....");
            Executors.newFixedThreadPool(poolSize).invokeAll(callables);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e);
            try {
                logger.info("Waiting for 3 seconds");
                Thread.sleep(3000);
                Executors.newSingleThreadExecutor().execute(Thread.currentThread());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    static public void kill() {
        logger.info("***Application terminated***");
        System.exit(0);
    }


}
