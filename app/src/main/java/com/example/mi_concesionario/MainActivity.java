package com.example.mi_concesionario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private ArrayList<String> cochesList;
    private ArrayList<Integer> cocheIds;  // Para almacenar los IDs de los coches
    private ArrayAdapter<String> cochesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la base de datos
        db = openOrCreateDatabase("Concesionario", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS coches (" +
                "cod_coche INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "marca TEXT, modelo TEXT, numCV INTEGER, precio REAL, color TEXT)");

        // Inicializar vistas
        ListView lvCoches = findViewById(R.id.lvCoches);
        Button btnBorrar = findViewById(R.id.btnBorrar);
        Button btnInsertar = findViewById(R.id.btnInsertar);
        Button btnActualizar = findViewById(R.id.btnActualizar);

        // Lista para los coches
        cochesList = new ArrayList<>();
        cocheIds = new ArrayList<>();  // Lista para almacenar los IDs de los coches
        cochesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cochesList);
        lvCoches.setAdapter(cochesAdapter);

        // Evento para cargar los coches al hacer clic en Consultar
        findViewById(R.id.btnConsultar).setOnClickListener(v -> cargarCoches());

        // Evento para ir a la pantalla de Borrar
        btnBorrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BorrarActivity.class);
            startActivity(intent);
        });

        // Evento para ir a la pantalla de Insertar
        btnInsertar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InsertarActivity.class);
            startActivity(intent);
        });

        // Evento para ir a la pantalla de Actualizar
        btnActualizar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ActualizarActivity.class);
            startActivity(intent);
        });

        // Configuración del listener de la lista para mostrar los detalles del coche
        lvCoches.setOnItemClickListener((parent, view, position, id) -> {
            // Obtener el ID del coche seleccionado
            int cocheId = cocheIds.get(position);

            // Consultar los detalles del coche usando el ID
            Cursor c = db.rawQuery("SELECT marca, modelo, numCV, precio, " +
                            "color FROM coches WHERE cod_coche = ?"
                    , new String[]{String.valueOf(cocheId)});
            if (c.moveToNext()) {
                @SuppressLint("Range")
                String marca = c.getString(c.getColumnIndex("marca"));
                @SuppressLint("Range")
                String modelo = c.getString(c.getColumnIndex("modelo"));
                @SuppressLint("Range")
                int numCV = c.getInt(c.getColumnIndex("numCV"));
                @SuppressLint("Range")
                float precio = c.getFloat(c.getColumnIndex("precio"));
                @SuppressLint("Range")
                String color = c.getString(c.getColumnIndex("color"));

                // Mostrar los detalles del coche en un Toast
                String mensaje =
                        "Marca: " + marca + "\nModelo: " + modelo + "\nCaballos: "
                                + numCV + "\nPrecio: " + precio + "\nColor: " + color;
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
            c.close();
        });

        // Cargar los coches al iniciar la actividad
        cargarCoches();
    }

    // Método para cargar los coches en el ListView
    private void cargarCoches() {
        cochesList.clear();
        cocheIds.clear();  // Limpiar la lista de IDs

        // Consultar los coches de la base de datos
        Cursor c = db.rawQuery("SELECT cod_coche," +
                " marca || ' - ' || modelo FROM coches", null);
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
