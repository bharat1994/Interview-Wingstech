package com.interviewtaskwingstech.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.interviewtaskwingstech.R;

public class CommonFunction {

    public static AlertDialog customProgressDialog(Context context, String title) {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_dialog_layout, null);

        TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        if (!title.equalsIgnoreCase("")) {
            tvMessage.setText(title);
        }

        builder.setView(dialogView);
        builder.setCancelable(false);
        alertDialog = builder.create();
        return alertDialog;
    }

}
