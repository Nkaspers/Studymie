package org.semesterbreak;

import com.google.gson.Gson;
import javafx.scene.control.TreeItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlashcardManager {
    private List<FlashcardStack> stackList;
    private int projectCounter = 0;

    public FlashcardManager() {
        stackList = new ArrayList<>();

        FlashcardStack stack1 = new FlashcardStack("Stapel 1");
        stack1.getFlashcards().add(new Flashcard("Frage 1", stack1));
        stack1.getFlashcards().add(new Flashcard("Frage 2", stack1));
        stack1.getFlashcards().add(new Flashcard("Frage 3", stack1));
        stack1.getFlashcards().add(new Flashcard("Frage 4", stack1));


        FlashcardStack stack2 = new FlashcardStack("Stapel 2");
        stack2.getFlashcards().add(new Flashcard("Frage 1", stack2));
        stack2.getFlashcards().add(new Flashcard("Frage 2", stack2));


        stackList.addAll(Arrays.asList(stack1, stack2));
    }

    public List<FlashcardStack> getStackList(){
        return stackList;
    }

     public void createJsonProject() throws IOException {
        JSONObject project = new JSONObject();
        project.put("Name", "Projekt 1");
        project.put("Id", projectCounter);
        JSONArray stackListJson = new JSONArray();
        for(FlashcardStack stack : stackList) {
            stackListJson.add(stack);
        }
        ObjectMapper mapper = new ObjectMapper();
        String projectNumber = String.valueOf(projectCounter);
        String projectPath = MessageFormat.format("C:\\Development\\project{0}", projectNumber);
        try {
            File file = new File(projectPath);
            mapper.writeValue(file, project);
        } catch(IOException e) {
            e.printStackTrace();
        }
        projectCounter++;
     }
    // to-do: Übergabe des Project Paths und der gesuchten Id/values als Parameter
     public JSONArray getJsonStack() {
         JSONParser parser = new JSONParser();
         JSONArray searchedStackFromProject = new JSONArray();
         try {
            JSONObject selectedProject = (JSONObject) parser.parse(new FileReader("C:\\Development\\project0"));
            JSONArray stackListFromProject = (JSONArray) selectedProject.get("StackList");
            searchedStackFromProject = (JSONArray) stackListFromProject.get(0);
         } catch (ParseException e) {
             e.printStackTrace();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return searchedStackFromProject;
     }

     public void deleteJsonStack() {
        JSONParser parser = new JSONParser();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String path = "C:\\Development\\project0";
            JSONObject selectedProject = (JSONObject) parser.parse(new FileReader(path));
            JSONArray stackListFromProject = (JSONArray) selectedProject.get("StackList");
            stackListFromProject.remove(0);
            selectedProject.remove("StackList");

            File updatedFile = new File(path);
            selectedProject.put("StackList", stackListFromProject);
            mapper.writeValue(updatedFile, selectedProject);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
     }



    public Flashcard addFlashcard(FlashcardStack stack) {
        Flashcard flashcard = new Flashcard("untitled", stack);
        stack.getFlashcards().add(flashcard);
        return flashcard;
    }

    public FlashcardStack addFlashcardStack() {
        FlashcardStack flashcardStack = new FlashcardStack("neuer Stapel");
        stackList.add(flashcardStack);
        return flashcardStack;
    }

    public void removeFromStack(Flashcard flashcard) {
        flashcard.getCurrentStack().getFlashcards().remove(flashcard);
    }

    public void removeStack(FlashcardStack lastSelectedTreeViewItem) {
        getStackList().remove(lastSelectedTreeViewItem);
    }
    public void moveTreeElement(TreeViewElement element, int shift){
        if(element.isFlashcard()){
            Flashcard flashcard = (Flashcard) element;
            int index = flashcard.getCurrentStack().getFlashcards().indexOf(flashcard);
            flashcard.getCurrentStack().getFlashcards().remove(flashcard);
            flashcard.getCurrentStack().getFlashcards().add(index+shift, flashcard);
        }else{
            FlashcardStack stack = (FlashcardStack) element;
            int index = stackList.indexOf(stack);
            stackList.remove(stack);
            stackList.add((index+shift), stack);
        }
    }
    public Flashcard duplicateFlashcard(TreeViewElement element) {
            Flashcard flashcard = (Flashcard) element;
            FlashcardStack stack = flashcard.getCurrentStack();
            int index = stack.getFlashcards().indexOf(flashcard);
            Flashcard flashcardCopy = new Flashcard(flashcard.getQuestion(), stack);
            stack.getFlashcards().add(index+1, flashcardCopy);
            return flashcardCopy;
    }
}
