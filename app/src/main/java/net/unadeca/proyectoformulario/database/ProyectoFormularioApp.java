package net.unadeca.proyectoformulario.database;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

public class ProyectoFormularioApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
