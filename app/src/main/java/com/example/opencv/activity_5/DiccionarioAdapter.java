package com.example.opencv.activity_5;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opencv.R;

import java.util.List;

public class DiccionarioAdapter extends RecyclerView.Adapter<DiccionarioAdapter.ViewHolder> {

    private List<DiccionarioItem> lista;
    private Context context;

    public DiccionarioAdapter(List<DiccionarioItem> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diccionario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiccionarioItem item = lista.get(position);
        holder.textViewNombre.setText(item.getNombre());
        holder.imageViewSeña.setImageResource(item.getImagenResId());

        // Hacer clickeable cada elemento y abrir Diccionario_5_find
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Diccionario_5_find.class);
            intent.putExtra("nombre", item.getNombre());  // Enviar el nombre de la seña
            intent.putExtra("imagen", item.getImagenResId());  // Enviar la imagen de la seña
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<DiccionarioItem> nuevaLista) {
        lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        ImageView imageViewSeña;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            imageViewSeña = itemView.findViewById(R.id.imageViewSeña);
        }
    }
}
