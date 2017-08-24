package ahk.pkginterface;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.ActionsData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class ScriptWriter {
    private final ComponentStorage componentStorage;

    public ScriptWriter(ComponentStorage compStorage) {
        componentStorage = compStorage;
    }
    /*
     * Call this method when you are changeing the key in ChangeKey class.
     */
    public void changeKey() {
        String keyToBeChanged = componentStorage.changeKeyInfo.currentKeyDisblayedLabel.getText().replace(" ", "");
        File scriptToChangeIn = componentStorage.changeKeyInfo.currentScriptDisblayedFile;
        HashMap<String, Integer> map = componentStorage.changeKeyInfo.getCurrentScriptKeyInfo();
        int indexWhereToBePlaced = map.get(keyToBeChanged);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(scriptToChangeIn.toPath(), StandardCharsets.UTF_8);
            lines.remove(indexWhereToBePlaced - 1);
            if (componentStorage.toBeChangedKeys.size() == 2) {
                lines.add(indexWhereToBePlaced - 1, componentStorage.toBeChangedKeys.get(0).getKeysynonyminahk() + " & " + componentStorage.toBeChangedKeys.get(1).getKeysynonyminahk() + "::");
            } else {
                lines.add(indexWhereToBePlaced - 1, componentStorage.toBeChangedKeys.get(0).getKeysynonyminahk() + "::");
            }
            Files.write(scriptToChangeIn.toPath(), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (componentStorage.toBeChangedKeys.size() == 2) {
            componentStorage.changeKeyInfo.currentKeyDisblayedLabel.setText(componentStorage.toBeChangedKeys.get(0).getKey() + " & " + componentStorage.toBeChangedKeys.get(1).getKey());
        } else {
            componentStorage.changeKeyInfo.currentKeyDisblayedLabel.setText(componentStorage.toBeChangedKeys.get(0).getKey());
        }
    }
    public void changeAction(){
        File scriptToChangeIn = componentStorage.changeKeyInfo.currentScriptDisblayedFile;
        Integer actionStartsHere = componentStorage.changeKeyInfo.getIndexForSpecificKey(componentStorage.ahkinterface.actionAndKey.get(componentStorage.changeKeyInfo.currentActionDisbalayedLabel.getText())); // -1 because list starts from zero and the code starts from one so
        ArrayList<String> spotifyContent = null;
        if(Objects.nonNull(componentStorage.toBeChangedAction.stream().filter(s -> s.toLowerCase().contains("spotify")))) spotifyContent = getSpotifyScriptContent();
        try {
            removeOldActionContent(scriptToChangeIn,actionStartsHere);
            final List<String> lines = Files.readAllLines(scriptToChangeIn.toPath(), StandardCharsets.UTF_8);
            int placerIndex = actionStartsHere;
            if(Objects.nonNull(spotifyContent)) {
                for (String oneLine : spotifyContent) {
                    if(placerIndex>=lines.size()){
                        lines.add(oneLine);
                    }else if (Objects.nonNull(lines.get(placerIndex)) && lines.get(placerIndex).contains("::")){
                        lines.add(placerIndex, oneLine);
                    }
                    placerIndex++;
                }

                componentStorage.toBeChangedAction.forEach(
                        action -> {
                            for(String oneLineOfCode : new ActionsData().getActionCode(action)){
                                lines.add(3+actionStartsHere,oneLineOfCode);
                                lines.add(11+actionStartsHere,oneLineOfCode);
                            }
                        }
                );
                Files.write(scriptToChangeIn.toPath(), lines, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<String> getSpotifyScriptContent(){
        return new ArrayList<String>(Arrays.asList(new ActionsData().getSpotifyActionCode()));
    }
    private void removeOldActionContent(File script,Integer startsHere){
        List<String> lines;
        try{
            lines = Files.readAllLines(script.toPath(), StandardCharsets.UTF_8);
            Integer endsHere = searchEndingPoint(lines,startsHere);
            List<String> updatedLines = new ArrayList<>();
            for(int i = 0;i<lines.size();i++){ // -1 from line.size could effect how the code is written
                if(startsHere > i || endsHere <= i){ // changed the direction
                    updatedLines.add(lines.get(i));
                }else{
                    System.out.println("line removed at " +i);
                }
            }
            Files.write(script.toPath(), updatedLines,StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Integer searchEndingPoint(List<String> lines, Integer startsHere) {
        Integer endingPoint = 0;
        for(int i = startsHere; i < lines.size();i++){
            if(lines.get(i).contains("::")) {
                return i;
            }
            endingPoint = lines.size();
        }
        return  endingPoint;
    }

    private ArrayList<String> getScriptContent(){
        final ArrayList<String> listOfCode = new ArrayList<>();
        componentStorage.toBeChangedAction.stream().forEach(
                oneActionName ->{
                    for(String oneLine : new ActionsData().getActionCode(oneActionName)){
                        listOfCode.add(oneLine);
                    }
                }
        );
        return listOfCode;
    }
}
