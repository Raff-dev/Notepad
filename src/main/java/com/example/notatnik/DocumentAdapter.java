package com.example.notatnik;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {
    private ArrayList<Document> documentArrayList;
    private OnDocumentListener onDocumentListener;
    public DocumentAdapter(ArrayList<Document> documentArrayList,OnDocumentListener onDocumentListener) {
        this.documentArrayList = documentArrayList;
        this.onDocumentListener=onDocumentListener;
    }

    public interface OnDocumentListener{
        void OnDocumentClick(int position);
    }



public static class DocumentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTextView;
        TextView itemsCountTextView;
        ImageView typeImageView;
        MyDBHandler dbHandler;
        OnDocumentListener onDocumentListener;


        public DocumentViewHolder(@NonNull View itemView, OnDocumentListener onDocumentListener) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            itemsCountTextView = itemView.findViewById(R.id.itemsCountTextView);
            typeImageView = itemView.findViewById(R.id.typeImageView);
            dbHandler = new MyDBHandler(itemView.getContext(), null, null, 1);
            this.onDocumentListener = onDocumentListener;
            itemView.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
        onDocumentListener.OnDocumentClick(getAdapterPosition());
    }
}

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_document, parent, false);
        return new DocumentViewHolder(v,onDocumentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        setInfo(holder,documentArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return documentArrayList.size();
    }

    private void setInfo(DocumentViewHolder holder, Document document){
        holder.nameTextView.setText(document.getName());
        if (document.getType().equals("CheckList")) {
            String itemsCountString = String.valueOf(holder.dbHandler.getTaskCount(document.getId()));
            holder.itemsCountTextView.setText(itemsCountString);
            switch (document.getColor()) {
                case "Orange":
                    holder.typeImageView.setImageResource(R.drawable.corange);
                    break;
                case "Green":
                    holder.typeImageView.setImageResource(R.drawable.cgreen);
                    break;
                case "Blue":
                    holder.typeImageView.setImageResource(R.drawable.cblue);
                    break;
                case "Red":
                    holder.typeImageView.setImageResource(R.drawable.cred);
                    break;
                default:
                    holder.typeImageView.setImageResource(R.drawable.cred);
            }
        } else {
            holder.itemsCountTextView.setText("");
            switch (document.getColor()) {
                case "Orange":
                    holder.typeImageView.setImageResource(R.drawable.norange);
                    break;
                case "Green":
                    holder.typeImageView.setImageResource(R.drawable.ngreen);
                    break;
                case "Blue":
                    holder.typeImageView.setImageResource(R.drawable.nblue);
                    break;
                case "Red":
                    holder.typeImageView.setImageResource(R.drawable.nred);
                    break;
                default:
                    holder.typeImageView.setImageResource(R.drawable.nred);
            }
        }

    }
}
