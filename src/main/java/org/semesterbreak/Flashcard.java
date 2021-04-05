package org.semesterbreak;

import java.text.SimpleDateFormat;

public class Flashcard implements TreeViewElement{
    public String question;
    private SimpleDateFormat dueDate;
    private Flashcard next;

    public Flashcard(String question) {
        this.question = question;
    }


    @Override
    public boolean isFlashcard() {
        return true;
    }
}
