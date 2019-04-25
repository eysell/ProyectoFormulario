package net.unadeca.proyectoformulario.database.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.unadeca.proyectoformulario.database.ProyectoFormularioDB;

import java.util.Locale;

@Table(database = ProyectoFormularioDB.class)
public class Formulario extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String nombre;

    @Column
    public int edad;

    @Column
    public String pais_origen;

    @Column
    public String profesion;

    @Column
    public String genero;

    public String toString(){
        return String.format(Locale.getDefault(), "Nombre: %s\nEdad: " + "%s\nPaisOrigen: %s\nProfesion", this.nombre, this.edad, this.pais_origen, this.profesion);
    }



}
