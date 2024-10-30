package com.example.ac2p1movel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder> {

    private List<Filme> filmes;
    private Context context;

    public FilmeAdapter(Context context) {
        this.context = context;
        this.filmes = new ArrayList<>();
        buscarFilmesDoFirestore(); // Busca os filmes do Firestore ao iniciar o adaptador
    }

    @NonNull
    @Override
    public FilmeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filme, parent, false);
        return new FilmeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmeViewHolder holder, int position) {
        Filme filme = filmes.get(position);
        holder.titulo.setText(filme.getTitulo());
        holder.ano.setText(String.valueOf(filme.getAno()));
        holder.diretor.setText(filme.getDiretor());

        // Usando Glide para carregar imagens de URLs
        Glide.with(context).load(filme.getImagem()).into(holder.imagem);

        // Configurar o clique do botão para mostrar a sinopse em um Toast
        holder.button.setOnClickListener(v ->
                Toast.makeText(context, filme.getSinopse(), Toast.LENGTH_SHORT).show());
    }


    @Override
    public int getItemCount() {
        return filmes.size();
    }

    public void buscarFilmesDoFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("filmes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Filme filme = document.toObject(Filme.class);
                    filmes.add(filme);
                }
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Erro ao buscar filmes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class FilmeViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, ano, diretor;
        ImageView imagem;
        Button button;

        public FilmeViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo);
            ano = itemView.findViewById(R.id.ano);
            diretor = itemView.findViewById(R.id.diretor);
            imagem = itemView.findViewById(R.id.imagem);
            button = itemView.findViewById(R.id.button);
        }
    }
}

