package com.example.ac2p1movel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvFilmes;
    private FilmeAdapter filmeAdapter;
    private List<Filme> filmesList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and adapter
        rvFilmes = findViewById(R.id.rvFilmes);
        // Configurando LinearLayoutManager com orientação horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFilmes.setLayoutManager(layoutManager);
        rvFilmes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        filmesList = new ArrayList<>();
        filmeAdapter = new FilmeAdapter(this);
        rvFilmes.setAdapter(filmeAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Apply window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            v.setPadding(0, insets.getSystemWindowInsetTop(), 0, 0);
            return WindowInsetsCompat.CONSUMED;
        });

        // Fetch movies from Firestore
        fetchMoviesFromFirestore();
    }

    private void fetchMoviesFromFirestore() {
        db.collection("filmes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Filme filme = document.toObject(Filme.class);
                            filmesList.add(filme);
                        }
                        // Update adapter after data is fetched
                        new Handler(Looper.getMainLooper()).post(() -> filmeAdapter.notifyDataSetChanged());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors here
                        e.printStackTrace();
                    }
                });
    }
}

