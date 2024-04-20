package com.gamesmindt.myapplication.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.Nullable;

import com.gamesmindt.myapplication.fragments.OfertasFragment;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "alumni.db";

    final String CREARPERSONA = "CREATE TABLE IF NOT EXISTS \"persona \" (\n" +
            "\t\"cod_perso\"\tINTEGER,\n" +
            "\t\"cedula\"\tTEXT NOT NULL,\n" +
            "\t\"primer_nombre\"\tTEXT,\n" +
            "\t\"segundo_nombre\"\tTEXT,\n" +
            "\t\"apellido_paterno\"\tTEXT,\n" +
            "\t\"apellido_materno\"\tTEXT,\n" +
            "\t\"fecha_nacimiento\"\tTEXT,\n" +
            "\t\"telefono\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"cod_perso\")\n" +
            ");"
            ;
    final String CREARROL_EMPRESARIO= "CREATE TABLE IF NOT EXISTS \"rol\" (\n" +
            "\t\"id_rol\"\tINTEGER,\n" +
            "\t\"nombre\"\tTEXT,\n" +
            "\t\"descripcion\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"id_rol\")\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"empresario\" (\n" +
            "\t\"id_empre\"\tINTEGER,\n" +
            "\t\"usuario_id_usuario\"\tNUMERIC COLLATE UTF16,\n" +
            "\t\"puesto\"\tTEXT,\n" +
            "\t\"estado\"\tTEXT,\n" +
            "\t\"email\"\tTEXT,\n" +
            "\t\"descripcion\"\tTEXT,\n" +
            "\t\"anios\"\tINTEGER,\n" +
            "\tFOREIGN KEY(\"usuario_id_usuario\") REFERENCES \"usuario\"(\"id_usuario\"),\n" +
            "\tPRIMARY KEY(\"id_empre\")\n" +
            ");";
    final String CREARPROVINCIAS_CIUDAD_SECTOR="CREATE TABLE IF NOT EXISTS \"provincia\" (\n" +
            "\t\"id_provincia\"\tINTEGER,\n" +
            "\t\"nombre\"\tTEXT,\n" +
            "\t\"pais\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"id_provincia\")\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"ciudad\" (\n" +
            "\t\"id_ciudad\"\tINTEGER,\n" +
            "\t\"id_provincia\"\tINTEGER,\n" +
            "\t\"nombre\"\tTEXT,\n" +
            "\tFOREIGN KEY(\"id_provincia\") REFERENCES \"provincia\"(\"id_provincia\"),\n" +
            "\tPRIMARY KEY(\"id_ciudad\")\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"sector_empresarial\" (\n" +
            "\t\"sec_emp_id\"\tINTEGER,\n" +
            "\t\"nombre\"\tTEXT,\n" +
            "\t\"descripcion\"\tTEXT,\n" +
            "\tPRIMARY KEY(\"sec_emp_id\")\n" +
            ");";
    final String DEMAS_TABLAS = "CREATE TABLE IF NOT EXISTS \"ofertaslaborales\" (\n" +
            "\t\"oferta_id\"\tINTEGER,\n" +
            "\t\"id_empresa\"\tINTEGER,\n" +
            "\t\"fecha_publicacion\"\tTEXT,\n" +
            "\t\"fecha_apertura\"\tTEXT,\n" +
            "\t\"fecha_cierre\"\tTEXT,\n" +
            "\t\"salario\"\tREAL,\n" +
            "\t\"experiencia\"\tTEXT,\n" +
            "\t\"estado\"\tINTEGER,\n" +
            "\t\"cargo\"\tTEXT,\n" +
            "\t\"area_conocimiento\"\tINTEGER,\n" +
            "\tFOREIGN KEY(\"id_empresa\") REFERENCES \"empresa\"(\"id_empresa\"),\n" +
            "\tPRIMARY KEY(\"oferta_id\"),\n" +
            "\tUNIQUE(\"id_empresa\")\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"empresa\" (\n" +
            "\t\"id_empresa\"\tINTEGER,\n" +
            "\t\"sec_emp_id\"\tINTEGER,\n" +
            "\t\"id_empre\"\tINTEGER,\n" +
            "\t\"id_ciudad\"\tINTEGER,\n" +
            "\t\"ubicacion\"\tTEXT,\n" +
            "\t\"tipo_empresa\"\tTEXT,\n" +
            "\t\"sitio_web\"\tTEXT,\n" +
            "\t\"ruc\"\tTEXT,\n" +
            "\t\"razon_social\"\tTEXT,\n" +
            "\t\"nombre\"\tTEXT,\n" +
            "\t\"area\"\tTEXT,\n" +
            "\tFOREIGN KEY(\"sec_emp_id\") REFERENCES \"sector_empresarial\"(\"sec_emp_id\"),\n" +
            "\tFOREIGN KEY(\"id_empre\") REFERENCES \"empresario\"(\"id_empre\"),\n" +
            "\tFOREIGN KEY(\"id_ciudad\") REFERENCES \"ciudad\"(\"id_ciudad\"),\n" +
            "\tPRIMARY KEY(\"id_empresa\")\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"graduado\" (\n" +
            "\t\"graduado_id\"\tINTEGER,\n" +
            "\t\"usuario_id_usuario\"\tINTEGER,\n" +
            "\t\"id_ciudad\"\tINTEGER,\n" +
            "\t\"ruta_pdf\"\tTEXT,\n" +
            "\t\"estado_civil\"\tTEXT,\n" +
            "\t\"email_personal\"\tTEXT,\n" +
            "\t\"año_graduacion\"\tTEXT,\n" +
            "\tFOREIGN KEY(\"usuario_id_usuario\") REFERENCES \"usuario\"(\"id_usuario\"),\n" +
            "\tFOREIGN KEY(\"id_ciudad\") REFERENCES \"ciudad\"(\"id_ciudad\"),\n" +
            "\tPRIMARY KEY(\"graduado_id\")\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"usuario\" (\n" +
            "\t\"id_usuario\"\tINTEGER,\n" +
            "\t\"persona_cod_perso\"\tINTEGER,\n" +
            "\t\"id_rol\"\tINTEGER,\n" +
            "\t\"nombre_usuario\"\tTEXT,\n" +
            "\t\"clave\"\tTEXT,\n" +
            "\t\"ruta_imagen\"\tTEXT,\n" +
            "\t\"estado\"\tTEXT,\n" +
            "\tFOREIGN KEY(\"persona_cod_perso\") REFERENCES \"persona \"(\"cod_perso\"),\n" +
            "\tFOREIGN KEY(\"id_rol\") REFERENCES \"rol\"(\"id_rol\"),\n" +
            "\tPRIMARY KEY(\"id_usuario\")\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS \"postulados\" (\n" +
            "\t\"graduado_id\"\tINTEGER,\n" +
            "\t\"oferta_id\"\tINTEGER,\n" +
            "\tFOREIGN KEY(\"graduado_id\") REFERENCES \"graduado\"(\"graduado_id\"),\n" +
            "\tFOREIGN KEY(\"oferta_id\") REFERENCES \"ofertaslaborales\"(\"oferta_id\")\n" +
            ");";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //sqLiteDatabase.execSQL(CREARPERSONA+CREARROL_EMPRESARIO+CREARPROVINCIAS_CIUDAD_SECTOR+DEMAS_TABLAS);
        //System.out.println("CREAR " + Utilidades.CREATE_TABLE_QUERY);
        sqLiteDatabase.execSQL(Utilidades.CREATE_TABLE_QUERY);
        sqLiteDatabase.execSQL(JobTable.CREATE_TABLE_QUERY);
        sqLiteDatabase.execSQL(GraduadoTable.CREATE_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS empresa");
        onCreate(sqLiteDatabase);
    }

    public static boolean hayConexionInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            // Comprueba si hay una conexión de red activa y si el dispositivo está conectado
            return networkInfo != null && networkInfo.isConnected();
        }
        // Devuelve false si no hay ningún servicio de red disponible
        return false;
    }

}
