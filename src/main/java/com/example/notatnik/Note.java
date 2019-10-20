package com.example.notatnik;

public class Note {
    int documentId;
    String text;

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
