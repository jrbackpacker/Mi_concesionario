package com.example.mi_concesionario;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BorrarActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private ArrayList<String> cochesList;
    private ArrayList<Integer> cocheIds;
    private ArrayAdapter<String> cochesAdapter;
    private int cocheIdSeleccionado;  // Para almacenar el ID del coche seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar);

        // Inicializar la base de datos
        db = openOrCreateDatabase("Concesionario", MODE_PRIVATE, null);

        // Inicializar vistas
        ListView lvCoches = findViewById(R.id.lvCochesBorrar);
        Button btnBorrarCoche = findViewById(R.id.btnBorrarCoche);

        // Lista para los coches
        cochesList = new ArrayList<>();
        cocheIds = new ArrayList<>();  // Lista para almacenar los IDs de los coches
        cochesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cochesList);
        lvCoches.setAdapter(cochesAdapter);

        // Evento para seleccionar un coche
        lvCoches.setOnItemClickListener((parent, view, position, id) -> {
            cocheIdSeleccionado = cocheIds.get(position);  // Obtener el ID del coche seleccionado
        });

        // Evento para borrar el coche
        btnBorrarCoche.setOnClickListener(v -> {
            if (cocheIdSeleccionado != -1) {  // Verificar que un coche ha sido seleccionado
                // Borrar el coche de la base de datos
                db.delete("coches", "cod_coche = ?", new String[]{String.valueOf(cocheIdSeleccionado)});

                // Mostrar un mensaje de éxito
                Toast.makeText(BorrarActivity.this, "Coche borrado correctamente", Toast.LENGTH_SHORT).show();

                // Recargar la lista de coches
                cargarCoches();
            } else {
                // Si no se ha seleccionado un coche, mostrar un mensaje
                Toast.makeText(BorrarActivity.this, "Por favor, selecciona un coche para borrar",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar los coches al iniciar la actividad
        cargarCoches();
    }

    // Método para cargar los coches en el ListView
    private void cargarCoches() {
        cochesList.clear();
        cocheIds.clear();  // Limpiar la lista de IDs

        // Consultar los coches de la base de datos
        Cursor c = db.rawQuery("SELECT cod_coche, marca || ' - ' || modelo FROM coches", null);
        while (c.moveToNext()) {
            // Agregar el ID del coche y el texto al ListView
            int id = c.getInt(0);  // Obtener el ID del coche
            String coche = c.getString(1);  // Obtener la marca y el modelo
            cochesList.add(coche);
            cocheIds.add(id);  // Guardar el ID en la lista
        }
        c.close();

        cochesAdapter.notifyDataSetChanged();
    }
}
