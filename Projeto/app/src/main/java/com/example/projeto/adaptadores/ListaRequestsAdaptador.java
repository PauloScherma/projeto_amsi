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
import com.example.projeto.modelo.Request;
import com.example.projeto.modelo.SingletonGestorRequests;

import java.util.ArrayList;

public class ListaRequestsAdaptador extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Request> requests;

    public ListaRequestsAdaptador(Context context, ArrayList<Request> requests) {
        this.context = context;
        this.requests = requests;
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

        final Request currentRequest = requests.get(i);

        viewHolder.update(currentRequest);
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Criar um diálogo de confirmação para uma melhor experiência do utilizador
                new AlertDialog.Builder(context)
                        .setTitle("Apagar Pedido")
                        .setMessage("Tem a certeza que deseja apagar este pedido?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 4. Chamar o método da API para apagar o pedido específico
                                // O 'currentRequest' tem o ID e outras informações necessárias
                                SingletonGestorRequests.getInstance(context).removerRequestAPI(currentRequest, context);

                                // Feedback para o utilizador
                                Toast.makeText(context, "Pedido a ser apagado...", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Não", null) // Não faz nada se o utilizador clicar em "Não"
                        .show();
            }
        });

        return view;
    }

    private class ViewHolderLista {
        private TextView tvTitle, tvDescription, tvStatus, tvPriority, tvCreated;
        private ImageButton btnDelete;

        public ViewHolderLista(View view) {
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvPriority = view.findViewById(R.id.tvPriority);
            tvCreated = view.findViewById(R.id.tvCreated);
            btnDelete = view.findViewById(R.id.btnDelete);
        }

        public void update(final Request request) {
            tvTitle.setText(request.getTitle());
            tvDescription.setText(request.getDescription());
            tvStatus.setText(request.getStatus().toString());
            tvPriority.setText(request.getPriority().toString());
            tvCreated.setText("Created: " + (request.getCreatedAt() != null ? request.getCreatedAt() : "N/A"));

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
