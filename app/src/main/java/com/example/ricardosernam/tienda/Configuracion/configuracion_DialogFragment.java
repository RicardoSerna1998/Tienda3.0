package com.example.ricardosernam.tienda.Configuracion;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricardosernam.tienda.DatabaseHelper;
import com.example.ricardosernam.tienda.Empleados.Empleados;
import com.example.ricardosernam.tienda.R;
import com.example.ricardosernam.tienda.provider.ContractParaProductos;
import com.example.ricardosernam.tienda.ventas.Ventas;

@SuppressLint("ValidFragment")
public class configuracion_DialogFragment extends android.support.v4.app.DialogFragment {
    private LinearLayout botones;
    private Cursor configuracion;
    private SQLiteDatabase db;
    private Button aceptar, cancelar;
    private EditText codigo;
    private CheckBox escaner;
    private static ContentValues values;
    private Spinner columnas;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.dialog_fragment_configuracion, container);
        this.getDialog().setTitle("Modificar");///cambiamos titulo del DialogFragment

        codigo = rootView.findViewById(R.id.ETcodigoImpresora);
        escaner = rootView.findViewById(R.id.CBescaner);
        columnas = rootView.findViewById(R.id.SPNcolumnas);

        aceptar = rootView.findViewById(R.id.BtnAceptarPago);
        cancelar = rootView.findViewById(R.id.BtnCancelarPago);


        values = new ContentValues();


        //cantidad.setSelection(cantidad.getText().length());
        DatabaseHelper admin = new DatabaseHelper(getContext(), ContractParaProductos.DATABASE_NAME, null, ContractParaProductos.DATABASE_VERSION);
        db = admin.getWritableDatabase();

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //cantidad.setShowSoftInputOnFocus(false);
        configuracion= db.rawQuery("select impresora, escaner, columnas from configuracion", null);
        if(configuracion.moveToFirst()){
            codigo.setText(configuracion.getString(0));
            if(configuracion.getInt(1)==1){
                escaner.setChecked(true);
            }else{
                escaner.setChecked(false);
            }
            columnas.setSelection(configuracion.getInt(2)-2);
        }



        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        values.put("impresora", codigo.getText().toString());
                    if(escaner.isChecked()){
                        values.put("escaner", 1);
                    }else{
                        values.put("escaner", 0);
                    }

                    values.put("columnas", columnas.getSelectedItem().toString());

                   /* if (columnas.getSelectedItemId() == 0) {
                            values.put("tipo_empleado", "Admin.");
                        }
                        else if(tipoEmpleado.getSelectedItemId()==1){
                            values.put("tipo_empleado", "Cajero");
                        }
                        else{
                            values.put("tipo_empleado", "Otro");
                        }*/

                        db.update("configuracion", values, "_id='" + 1 + "'", null);
                        dismiss();
                        Toast.makeText(getContext(), "Configuración guardada", Toast.LENGTH_LONG).show();

            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;
    }
    /*public static void repetido(String nombre){
        for(int i=0; i<ContractParaProductos.itemsProductosVenta.size(); i++) {
            if(nombre.equals(ContractParaProductos.itemsProductosVenta.get(i).getNombre())){  ///si se repite
                ContractParaProductos.itemsProductosVenta.remove(i);   ////eliminamos el previamente agregado
            }
        }
    }*/
}
