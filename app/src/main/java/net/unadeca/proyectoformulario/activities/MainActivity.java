package net.unadeca.proyectoformulario.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.unadeca.proyectoformulario.R;
import net.unadeca.proyectoformulario.database.models.Formulario;
import net.unadeca.proyectoformulario.database.models.Formulario_Table;

import java.util.ArrayList;
import java.util.List;

import static net.unadeca.proyectoformulario.R.layout.formulario;

public class MainActivity extends AppCompatActivity {

    private ListView lista;
    private CoordinatorLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lista = findViewById(R.id.lista);
        view = findViewById(R.id.content);
        setAdapter();

        borrarFormulario();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mostrarDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String[] getFormularios() {
        List<Formulario> formularios = SQLite.select().from(Formulario.class).queryList();
        String[] array = new String[formularios.size()];
        for (int c = 0; c < formularios.size(); c++) {
            array[c] = formularios.get(c).toString();
        }
        return array;
    }
    private List<Formulario> getListFormularios(){
        return  SQLite.select().from(Formulario.class).queryList();

}
    private void setAdapter(){
        lista.setAdapter( new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getFormularios()));

        lista.setAdapter(new CustomAdapter(getListFormularios(),getApplicationContext(), view));

    }
    //Aquí es donde va aempezar la alista del dialogo
    //Dialogo reutilizable pues se puede llamar más de una vez
    //void no retorna ninguna información
    public void mostrarDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(this); //Para personlizar un dialogo
        View formulario = layoutInflater.inflate(R.layout.formulario, null); //Hacemos una vista


        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Hacemos que el diálogo se conecte con el diseño
        //Establecemos la vista como parte del diálogo
        builder.setView(formulario);
        final TextInputLayout nombre = formulario.findViewById(R.id.txtNombre);
        final TextInputLayout edad = formulario.findViewById(R.id.txtEdad);
        final TextInputLayout pais_origen = formulario.findViewById(R.id.txtPaisOrigen);
        final TextInputLayout profesion = formulario.findViewById(R.id.txtProfesion);
        final TextInputLayout genero = formulario.findViewById(R.id.txtGenero);

        builder.setMessage(" ")
                .setTitle("Crear nuevo formulario")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            validate(nombre, edad, pais_origen, profesion, genero);
                            guardarABD(nombre, edad, pais_origen, profesion, genero);
                        }catch (Exception e){
                            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialogo = builder.create();
        dialogo.show();
    }
    //metodo para validar la información
    private void validate(TextInputLayout nombre, TextInputLayout edad, TextInputLayout pais_origen, TextInputLayout profesion, TextInputLayout genero) throws Exception{
        if (nombre.getEditText().getText().toString().isEmpty())
            throw new Exception("Es necesario que ingrese su nombre");

        if (edad.getEditText().getText().toString().isEmpty())
            throw new Exception("Debe ingresar su edad");

        if (pais_origen.getEditText().getText().toString().isEmpty())
            throw new Exception("Por favor debe ingresar su país de origen");

        if (profesion.getEditText().getText().toString().isEmpty())
            throw new Exception("Es necesario que rellene todos los campos solicitados");

        if (genero.getEditText().getText().toString().isEmpty())
            throw new Exception("Ingrese su género por favor");

    }
    //Método para guardar la información
    private void guardarABD(TextInputLayout nombre, TextInputLayout edad, TextInputLayout pais_origen, TextInputLayout profesion, TextInputLayout genero){
        Formulario formulario = new Formulario();
        formulario.edad = Integer.parseInt(edad.getEditText().getText().toString());
        formulario.nombre = nombre.getEditText().getText().toString();
        formulario.pais_origen = pais_origen.getEditText().getText().toString();
        formulario.profesion = profesion.getEditText().getText().toString();
        formulario.genero = genero.getEditText().getText().toString();
        formulario.save();

//Notificaión al usuario
        Snackbar.make(view, "Su información se ha guardado en el registro", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        setAdapter();

    }
    //Método para eliminar
    private void borrarFormulario(){
        //Este se usa si queremos borrar toda la tabla
        //Este borra dependiendo de una consulta
       SQLite.delete().from(Formulario.class).where(Formulario_Table.edad.between(1).and(20
        )).execute();
        setAdapter();

        Snackbar.make(view, "Su registro se ha eliminado corretamente", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
