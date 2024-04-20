package com.gamesmindt.myapplication.bd;

public class Utilidades {
    public static final String TABLE_NAME = "empresa";
    public static final String COLUMN_ID_EMPRESA = "id";
    public static final String COLUMN_EMPRESARIO = "empresario";
    public static final String COLUMN_UBICACION = "ubicacion";
    public static final String COLUMN_TIPO_EMPRESA = "tipo_empresa";
    public static final String COLUMN_SITIO_WEB = "sitio_web";
    public static final String COLUMN_RUC = "ruc";
    public static final String COLUMN_RAZON_SOCIAL = "razon_social";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_AREA = "area";

    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID_EMPRESA + " INTEGER PRIMARY KEY," +
                    COLUMN_EMPRESARIO + " TEXT,"+
                    COLUMN_RUC + " TEXT," +
                    COLUMN_NOMBRE + " TEXT," +
                    COLUMN_TIPO_EMPRESA + " TEXT," +
                    COLUMN_RAZON_SOCIAL + " TEXT," +
                    COLUMN_AREA + " TEXT," +
                    COLUMN_SITIO_WEB + " TEXT" +
                    ")";
    public static final String DELETE_TABLE_QUERY =
            "DROP TABLE " + TABLE_NAME;


}

