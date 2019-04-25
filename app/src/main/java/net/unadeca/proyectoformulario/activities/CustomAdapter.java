package net.unadeca.proyectoformulario.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.unadeca.proyectoformulario.R;
import net.unadeca.proyectoformulario.database.models.Formulario;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Formulario> {
    private List<Formulario> dataSet;
    Context mContext;
    CoordinatorLayout view;

    private static class ViewHolder {
        TextView txtFormulario;
        ImageView delete;
        ImageView update;

    }

    public CustomAdapter(List<Formulario> data, Context context, CoordinatorLayout l) {
        super(context, R.layout.registro, data);
        this.dataSet = data;
        this.mContext = context;
        this.view = l;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Formulario dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.registro, parent, false);
            viewHolder.txtFormulario = convertView.findViewById(R.id.text);
            viewHolder.delete = convertView.findViewById(R.id.delete);
            viewHolder.update = convertView.findViewById(R.id.imagen);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtFormulario.setText(dataModel.toString());
        viewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialog(dataModel);

            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataModel.delete();
                dataSet.remove(dataModel);
                notifyDataSetChanged();
                Toast.makeText(getContext(), "Se eliminó el registro", Toast.LENGTH_LONG).show();


            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    public void mostrarDialog(final Formulario F){
        //Para personlizar un dialogo
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        //Hacemos una vista
        View formulario = layoutInflater.inflate(R.layout.formulario, null);
        //Hacemos que el diálogo se conecte con el diseño


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //Establecemos la vista como parte del diálogo
        builder.setView(formulario);
        final TextInputLayout nombre = formulario.findViewById(R.id.txtNombre);
        nombre.getEditText().setText(F.nombre);
        final TextInputLayout edad = formulario.findViewById(R.id.txtEdad);
        edad.getEditText().setText(F.edad);
        final TextInputLayout pais_origen = formulario.findViewById(R.id.txtPaisOrigen);
        pais_origen.getEditText().setText(F.pais_origen);
        final TextInputLayout profesion = formulario.findViewById(R.id.txtProfesion);
        profesion.getEditText().setText(F.profesion);
        final TextInputLayout genero = formulario.findViewById(R.id.txtGenero);
        genero.getEditText().setText(F.genero);

        builder.setMessage("Rellene toda la información solicitada")
                .setTitle("Crear nuevo registro")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            validate(nombre, edad, pais_origen, profesion, genero);
                            guardarABD(nombre, edad, pais_origen, profesion, genero, F);
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

    //Métodos para validar la información
    private void validate(TextInputLayout nombre, TextInputLayout edad, TextInputLayout pais_origen, TextInputLayout profesion, TextInputLayout genero) throws Exception{
        if (nombre.getEditText().getText().toString().isEmpty())
            throw new Exception("Necesita escribir su nombre");

        if (edad.getEditText().getText().toString().isEmpty())
            throw new Exception("Necesita escribir su edad");

        if (pais_origen.getEditText().getText().toString().isEmpty())
            throw new Exception("Necesita escribir su país de origen");

        if (profesion.getEditText().getText().toString().isEmpty())
            throw new Exception("Necesita escribir su profesión");

        if (genero.getEditText().getText().toString().isEmpty())
            throw new Exception("Necesita escribir su género");

        }

    private void guardarABD(TextInputLayout nombre, TextInputLayout edad, TextInputLayout pais_origen, TextInputLayout profesion, TextInputLayout genero,
                            Formulario formulario){

        formulario.nombre = nombre.getEditText().getText().toString();
        formulario.edad = Integer.parseInt(edad.getEditText().getText().toString());
        formulario.pais_origen = pais_origen.getEditText().getText().toString();
        formulario.profesion = profesion.getEditText().getText().toString();
        formulario.genero = genero.getEditText().getText().toString();
        formulario.save();

        Snackbar.make(view, "Se ha modificado el formulario", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        notifyDataSetChanged();

    }

}