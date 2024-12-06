package com.example.mi_concesionario;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;



public class InsertarActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private EditText etMarca, etModelo, etNumCV, etPrecio, etColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);

        // Inicializar la base de datos
        db = openOrCreateDatabase("Concesionario", MODE_PRIVATE, null);

        // Inicializar vistas
        etMarca = findViewById(R.id.etMarca);
        etModelo = findViewById(R.id.etModelo);
        etNumCV = findViewById(R.id.etNumCV);
        etPrecio = findViewById(R.id.etPrecio);
        etColor = findViewById(R.id.etColor);
        Button btnInsertar = findViewById(R.id.btnInsertar);

        // Configurar el evento de clic para insertar un coche
        btnInsertar.setOnClickListener(v -> insertarCoche());
    }

    // Método para insertar un coche en la base de datos
    private void insertarCoche() {
        // Obtener los datos de los campos de texto
        String marca = etMarca.getText().toString();
        String modelo = etModelo.getText().toString();
        String numCVStr = etNumCV.getText().toString();
        String precioStr = etPrecio.getText().toString();
        String color = etColor.getText().toString();

        // Validar los datos antes de insertarlos
        if (marca.isEmpty() || modelo.isEmpty() || numCVStr.isEmpty() || precioStr.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir los valores numéricos
        int numCV = Integer.parseInt(numCVStr);
        double precio = Double.parseDouble(precioStr);

        // Crear un objeto ContentValues para insertar los datos
        ContentValues values = new ContentValues();
        values.put("marca", marca);
        values.put("modelo", modelo);
        values.put("numCV", numCV);
        values.put("precio", precio);
        values.put("color", color);

        // Insertar el coche en la base de datos
        db.insert("coches", null, values);

        // Mostrar un mensaje de éxito
        Toast.makeText(this, "Coche insertado correctamente", Toast.LENGTH_SHORT).show();

        // Limpiar los campos de texto
        limpiarCampos();
    }

    // Método para limpiar los campos de texto
    private void limpiarCampos() {
        etMarca.setText("");
        etModelo.setText("");
        etNumCV.setText("");
        etPrecio.setText("");
        etColor.setText("");
    }
}
