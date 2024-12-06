package com.example.mi_concesionario;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActualizarActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private ArrayList<String> cochesList;
    private ArrayList<Integer> cocheIds;
    private ArrayAdapter<String> cochesAdapter;
    private int cocheIdSeleccionado;  // Para almacenar el ID del coche seleccionado

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        // Inicializar la base de datos
        db = openOrCreateDatabase("Concesionario", MODE_PRIVATE, null);

        // Inicializar vistas
        ListView lvCoches = findViewById(R.id.lvCochesActualizar);
        EditText etMarca = findViewById(R.id.etMarcaActualizar);
        EditText etModelo = findViewById(R.id.etModeloActualizar);
        EditText etNumCV = findViewById(R.id.etNumCVActualizar);
        EditText etPrecio = findViewById(R.id.etPrecioActualizar);
        EditText etColor = findViewById(R.id.etColorActualizar);
        Button btnActualizarCoche = findViewById(R.id.btnActualizarCoche);

        // Lista para los coches
        cochesList = new ArrayList<>();
        cocheIds = new ArrayList<>();  // Lista para almacenar los IDs de los coches
        cochesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cochesList);
        lvCoches.setAdapter(cochesAdapter);

        // Evento para cargar los coches al hacer clic en Consultar
        lvCoches.setOnItemClickListener((parent, view, position, id) -> {
            // Obtener el ID del coche seleccionado
            cocheIdSeleccionado = cocheIds.get(position);

            // Consultar los detalles del coche usando el ID
            Cursor c = db.rawQuery("SELECT marca, modelo, numCV, precio, color FROM coches WHERE cod_coche = ?", new String[]{String.valueOf(cocheIdSeleccionado)});
            if (c.moveToNext()) {
                // Cargar los datos del coche en los campos editables
                etMarca.setText(c.getString(c.getColumnIndex("marca")));
                etModelo.setText(c.getString(c.getColumnIndex("modelo")));
                etNumCV.setText(String.valueOf(c.getInt(c.getColumnIndex("numCV"))));
                etPrecio.setText(String.valueOf(c.getFloat(c.getColumnIndex("precio"))));
                etColor.setText(c.getString(c.getColumnIndex("color")));
            }
            c.close();
        });

        // Evento para actualizar el coche
        btnActualizarCoche.setOnClickListener(v -> {
            String marca = etMarca.getText().toString();
            String modelo = etModelo.getText().toString();
            int numCV = Integer.parseInt(etNumCV.getText().toString());
            float precio = Float.parseFloat(etPrecio.getText().toString());
            String color = etColor.getText().toString();

            // Actualizar los datos en la base de datos
            ContentValues values = new ContentValues();
            values.put("marca", marca);
            values.put("modelo", modelo);
            values.put("numCV", numCV);
            values.put("precio", precio);
            values.put("color", color);

            // Actualizar el coche en la base de datos
            db.update("coches", values, "cod_coche = ?", new String[]{String.valueOf(cocheIdSeleccionado)});

            // Mostrar un mensaje de éxito
            Toast.makeText(ActualizarActivity.this, "Coche actualizado correctamente", Toast.LENGTH_SHORT).show();

            // Volver a la MainActivity
            Intent intent = new Intent(ActualizarActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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
