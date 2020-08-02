package com.info.rehberim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder>  {
    private Context mContext;
    private List<Person> personList;
    private Database database;


    public RVAdapter(Context mContext, List<Person> personList, Database database) {
        this.mContext = mContext;
        this.personList = personList;
        this.database = database;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private ImageView profile_image;
        private CardView cardView;
        private ImageView imageViewExtra;


        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            profile_image = itemView.findViewById(R.id.profile_image);
            cardView = itemView.findViewById(R.id.cardView);
            imageViewExtra = itemView.findViewById(R.id.imageViewExtra);

        }
    }
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {
        holder.textViewName.setText(personList.get(position).getPersonName());
        holder.profile_image.setImageBitmap(personList.get(position).getPersonImage());
        holder.imageViewExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(mContext,holder.imageViewExtra);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.actionDelete:
                                Snackbar.make(holder.imageViewExtra,"Do you want to delete?",Snackbar.LENGTH_SHORT)
                                        .setAction("Yes", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                new DbHelper().deletePerson(database,personList.get(position).getPersonId());
                                                personList = new DbHelper().getAllData(database);
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .show();
                                return true;
                            case R.id.actionEdit:
                                Intent intent = new Intent(mContext,EditActivity.class);
                                //Bitmap smallImage = makeSmallerImage(personList.get(position).getPersonImage(),300);
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                if (personList.get(position).getPersonImage() != null) {
                                    personList.get(position).getPersonImage().compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                                } else {
                                    Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.round_add_icon);
                                    personList.get(position).setPersonImage(icon);
                                }
                                byte[] byteArray =  outputStream.toByteArray();
                                intent.putExtra("edit_name",personList.get(position).getPersonName());
                                intent.putExtra("edit_telNumber",personList.get(position).getPersonTelNumber());
                                intent.putExtra("edit_email",personList.get(position).getPersonEmail());
                                intent.putExtra("edit_image",byteArray);
                                intent.putExtra("person_id",personList.get(position).getPersonId());
                                mContext.startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public Bitmap makeSmallerImage(Bitmap image,int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height ;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int)(width/bitmapRatio);
        } else {
            height  = maxSize;
            width = (int)(height / bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);

    }

}



