package de.hdm_stuttgart.mi.gameoflife.core.presets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PresetLoader {
    private HashMap<String, PresetEncoding> availableFilePaths;
    private String workingDirectory;
    private File file;

    public static Map<PresetEncoding, IPresetDecoder> encoders = Map.of(PresetEncoding.RLE, new RLEDecoder(), PresetEncoding.PlainText, new PlainTextDecoder());

    public PresetLoader(){
        Reload();
    }

    public void Reload(){
        workingDirectory = System.getProperty("user.dir") + "/presets/"; //Absolute Path for current working directory

        availableFilePaths = new HashMap<String, PresetEncoding>();

        file = new File(workingDirectory);


        String[] files = file.list();
        if(files != null){
            Arrays.stream(files).forEach(s -> {
                if(s.endsWith(".rle")){
                    availableFilePaths.put(s, PresetEncoding.RLE);
                } else if(s.endsWith(".txt")){
                    availableFilePaths.put(s, PresetEncoding.PlainText);
                }
            });
        }
    }

    public String[] getAvailableFiles(){
        return availableFilePaths.keySet().toArray(new String[0]);
    }

    public StandardPreset loadPreset(String fileName) throws IOException, InvalidPresetFileException {

        if(!availableFilePaths.containsKey(fileName)){
            throw new FileNotFoundException("Preset file " + workingDirectory + fileName + " is unknown.");
        }

        //Load file.
        File presetFile = new File(workingDirectory + fileName);

        if(!presetFile.exists()){
            throw new FileNotFoundException("Preset file " + workingDirectory + fileName + " doesn't exist.");
        }

        if(presetFile.isDirectory()){
            throw new FileNotFoundException(workingDirectory + fileName + " is a directory not a file.");
        }

        return encoders.get(
                availableFilePaths.get(fileName)
        ).convertToPreset(readFile(workingDirectory + fileName, StandardCharsets.UTF_8));

    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    public enum PresetEncoding
    {
        RLE,
        PlainText
    }

}
