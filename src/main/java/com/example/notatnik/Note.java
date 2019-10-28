package com.example.notatnik;

public class Note {
    int documentId;
    String text;

    public Note() {
        this.documentId = 999;
        this.text = "Empty";
    }

    public Note(Note note) {
        this.documentId = note.documentId;
        this.text = note.text;
    }

    public Note(int documentId) {
        this.documentId = documentId;
        this.text = " ";
    }

    public Note(int documentId, String text) {
        this.documentId = documentId;
        this.text = text;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
