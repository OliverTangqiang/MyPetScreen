package com.example.mypetscreen;

import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    //Initialize variable
    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;

    //Create constructor
    public MainAdapter(Activity context, List<MainData> dataList){
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Initialize main data
        MainData data = dataList.get(position);
        //Initialize database
        database = RoomDB.getInstance(context);
        //Set text on text view
        holder.textView.setText(data.getText());

        holder.btEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Initialise main data
                MainData d = dataList.get(holder.getAdapterPosition());
                //get id
                int sID = d.getID();
                //get text
                String sText = d.getText();

                //Create dialog
                Dialog dialog = new Dialog(context);
                //Set content view
                dialog.setContentView(R.layout.dialog_update);
                //Initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                //Initialize Height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                //Set layout
                dialog.getWindow().setLayout(width, height);
                //show dialog
                dialog.show();

                //Initialise and assign variables
                EditText editText = dialog.findViewById(R.id.edit_text);
                Button btUpdate = dialog.findViewById(R.id.bt_update);

                //set text on edit text
                editText.setText(sText);

                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Dismiss dialog
                        dialog.dismiss();
                        //Get updated text from edit text
                        String uText = editText.getText().toString().trim();
                        //Update text in database
                        database.mainDao().update(sID,uText);
                        //Notify when data is updated
                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        notifyDataSetChanged();

                    }
                });


            }
        });

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialise main data
                MainData d = dataList.get(holder.getAdapterPosition());
                //Delete text from database
                database.mainDao().delete(d);
                //Notify when the data has been deleted
                int position = holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });

    }

    @Override
    public int getItemCount() {

        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Initialize variable
        TextView textView;
        ImageView btEdit, btDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign variable
            textView = itemView.findViewById(R.id.text_view);
            btEdit = itemView.findViewById(R.id.bt_edit);
            btDelete = itemView.findViewById(R.id.bt_delete);
        }
    }
}
