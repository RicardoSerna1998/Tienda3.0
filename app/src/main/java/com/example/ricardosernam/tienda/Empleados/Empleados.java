package com.example.ricardosernam.tienda.Empleados;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

;import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.provider.ContractParaProductos;
import com.example.ricardosernam.tienda.R;

import java.util.ArrayList;

public class Empleados extends Fragment {     /////Fragment de categoria ventas
    public static Cursor empleados, informacion, empleadoElegido;
    public static RecyclerView recycler;
    public static RecyclerView.Adapter adapter;
    public static RecyclerView.LayoutManager lManager;
    public static android.support.v4.app.FragmentManager fm;
    public static SQLiteDatabase db;
    public static TextView nombre;
    public static Button imprimir;
    public static Button  nuevo, editarInfo, configuracion;
    public static ContentValues values=new ContentValues();
    private static ArrayList<Empleados_class> itemsEmpleados = new ArrayList<>();  ///Arraylist que contiene los cardviews seleccionados de productos

    TextView myLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empleados, container, false);
        onViewCreated(view, savedInstanceState);


        fm = getFragmentManager();
        nombre= view.findViewById(R.id.TVnombreNegocio);

        nuevo = view.findViewById(R.id.BtnAgregarEmpleado);
        editarInfo = view.findViewById(R.id.BtnmodificarInfo);
        configuracion = view.findViewById(R.id.BtnConfiguracion);

        recycler = view.findViewById(R.id.RVempleados); ///declaramos el recycler

        DatabaseHelper admin = new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db = admin.getWritableDatabase();

        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new usuariosDialogFragment("Administrador", "Agregar").show(fm, "Empleados");

            }
        });
        editarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new usuariosDialogFragment("Administrador", "Negocio").show(fm, "Empleados");

            }
        });
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new usuariosDialogFragment("Administrador", "Configuracion").show(fm, "Empleados");
            }
        });

        relleno(getContext());
        return view;
    }
    public static void actualizar_disponibles(Context context, String empleado) {
        empleadoElegido= db.rawQuery("select _id from empleados where nombre_empleado='"+empleado+"'", null);
        values.put("disponible", 0);
        if(empleadoElegido.moveToFirst()){
            db.update("empleados", values, "_id='" + empleadoElegido.getString(0) + "'", null);
        }
        Empleados.relleno(context);;
    }
    public static void relleno(Context context) {    ///llamamos el adapter del recycler
        itemsEmpleados.clear();
        empleados = db.rawQuery("select nombre_empleado, tipo_empleado, activo, codigo from empleados where disponible=1 ORDER by tipo_empleado, activo desc", null);
        informacion= db.rawQuery("select nombre_negocio, direccion, telefono from informacion", null);

        if(informacion.moveToFirst()){ ////datos del negocio
            //if(!informacion.getString(0).isEmpty()){
            nombre.setVisibility(View.VISIBLE);
            nombre.setText(informacion.getString(0)+" "+informacion.getString(1)+" "+informacion.getString(2));
            //}
        }
        else{
            nombre.setVisibility(View.GONE);
            nombre.setText("");
        }

        if (empleados.moveToFirst()) {///si hay un elemento

            itemsEmpleados.add(new Empleados_class(empleados.getString(0), empleados.getString(1), empleados.getInt(2), empleados.getString(3)));
            while (empleados.moveToNext()) {
                itemsEmpleados.add(new Empleados_class(empleados.getString(0), empleados.getString(1), empleados.getInt(2), empleados.getString(3)));
            }
        }
        adapter = new Empleados_ventasAdapter(itemsEmpleados, fm, context);///llamamos al adaptador y le enviamos el array como parametro
        lManager = new LinearLayoutManager(context);  //declaramos el layoutmanager
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
    }


}


