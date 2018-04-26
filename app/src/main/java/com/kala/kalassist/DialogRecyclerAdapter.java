package com.kala.kalassist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kalaman on 20.10.17.
 */
public class DialogRecyclerAdapter extends RecyclerView.Adapter<DialogRecyclerAdapter.DialogViewHolder>{

    ArrayList<SpeechDialog> arrayListSpeechDialog;

    public DialogRecyclerAdapter(ArrayList <SpeechDialog> arrayList) {
        arrayListSpeechDialog = arrayList;
    }

    @Override
    public DialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_speechdialog, parent, false);

        return new DialogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DialogViewHolder holder, int position) {
        SpeechDialog currentSpeechDialog = arrayListSpeechDialog.get(position);
        int pictureResource = currentSpeechDialog.getSpeechCommand().getCommandPictureResourceID();

        holder.textViewQuestion.setText(currentSpeechDialog.getQuestion());
        holder.textViewAnswer.setText(currentSpeechDialog.getAnswer());

        if (pictureResource > -1)
            holder.imageViewCommandType.setImageResource(pictureResource);
        else
            holder.imageViewCommandType.setImageResource(android.R.color.transparent);
    }

    @Override
    public int getItemCount() {
        return arrayListSpeechDialog.size();
    }

    public void addSpeechDialog (SpeechDialog speechDialog) {
        arrayListSpeechDialog.add(0,speechDialog);
        notifyItemInserted(0);
        //notifyDataSetChanged();
    }

    public void deleteSpeechDialog (int index){
        arrayListSpeechDialog.remove(index);
        notifyItemRemoved(index);
    }

    public ArrayList<SpeechDialog> getArrayListSpeechDialog() {
        return arrayListSpeechDialog;
    }

    class DialogViewHolder extends RecyclerView.ViewHolder{
        TextView textViewQuestion;
        TextView textViewAnswer;
        ImageView imageViewCommandType;
        View view;

        public DialogViewHolder(final View itemView) {
            super(itemView);
            textViewAnswer = (TextView)itemView.findViewById(R.id.textView_answer);
            textViewQuestion = (TextView)itemView.findViewById(R.id.textView_question);
            imageViewCommandType = (ImageView)itemView.findViewById(R.id.imageViewCommandType);

            view = itemView;
        }
    }
}
