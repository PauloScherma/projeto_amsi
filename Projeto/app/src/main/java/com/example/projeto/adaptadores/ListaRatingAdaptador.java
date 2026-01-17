package com.example.projeto.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.projeto.R;
import com.example.projeto.modelo.Rating;
import com.example.projeto.modelo.SingletonGestorRequests;

import java.util.ArrayList;

public class ListaRatingAdaptador extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Rating> ratings;

    public ListaRatingAdaptador(Context context, ArrayList<Rating> ratings) {
        this.context = context;
        this.ratings = ratings;
    }

    @Override
    public int getCount() {
        return ratings.size();
    }

    @Override
    public Object getItem(int i) {
        return ratings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return ratings.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.fragment_item_lista_rating, viewGroup, false);

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        final Rating currentRating = ratings.get(i);

        viewHolder.update(currentRating);
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Apagar Pedido")
                        .setMessage("Tem a certeza que deseja apagar este pedido?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SingletonGestorRequests.getInstance(context).removerRatingAPI(currentRating, context);
                                Toast.makeText(context, "Pedido a ser apagado...", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Não", null) // Não faz nada se o utilizador clicar em "Não"
                        .show();
            }
        });


        return view;
    }

    private class ViewHolder {
        private TextView tvTitle, tvDescription, tvScore;
        private ImageButton btnDelete;

        public ViewHolder(View view) {
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvScore = view.findViewById(R.id.tvScore);
            btnDelete = view.findViewById(R.id.btnDelete);
        }

        public void update(final Rating rating) {
            tvTitle.setText(rating.getTitle());
            tvDescription.setText(rating.getDescription());
            tvScore.setText(String.valueOf(rating.getScore()));

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     SingletonGestorRequests.getInstance(context).removerRating(rating.getId());
                     notifyDataSetChanged();
                }
            });
        }
    }
}
