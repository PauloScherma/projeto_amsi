package com.example.projeto.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projeto.R;
import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;

import java.util.ArrayList;

public class ListaRequestsAdaptador extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Request> requests;

    public ListaRequestsAdaptador(Context context, ArrayList<Request> livros) {
        this.context = context;
        this.requests = livros;
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public Object getItem(int i) {
        return requests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return requests.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.fragment_item_lista_request, viewGroup, false);

        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }
        viewHolder.update(requests.get(i));
        return view;
    }

    private class ViewHolderLista {
        private TextView tvCapa, tvTitulo, tvSerie, tvAutor, tvAno;
        private ImageButton btnDelete;

        public ViewHolderLista(View view) {
            tvTitulo = view.findViewById(R.id.tvTitulo);
            tvSerie = view.findViewById(R.id.tvSerie);
            tvAutor = view.findViewById(R.id.tvAutor);
            tvAno = view.findViewById(R.id.tvAno);
            btnDelete = view.findViewById(R.id.btnDelete);
        }

        public void update(Request request) {
            tvCapa.setText(request.getCapa());
            tvTitulo.setText(request.getTitulo());
            tvSerie.setText(request.getSerie());
            tvAutor.setText(request.getAutor());
            tvAno.setText(String.valueOf(request.getAno()));

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingletonGestorRequests.getInstance(context).removerRequest(request.getId());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
