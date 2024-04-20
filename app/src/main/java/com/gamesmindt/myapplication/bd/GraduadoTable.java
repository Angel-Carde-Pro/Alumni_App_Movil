package com.gamesmindt.myapplication.bd;

public class GraduadoTable {
    public static final String TABLE_NAME = "Graduado";
    public static final String COLUMN_ID_GRADUADO = "id";
    public static final String COLUMN_ID_USER_GRADUADO = "idUser";

    public static final String COLUMN_PRIMER_NOMBRE = "primerNombre";
    public static final String COLUMN_MAIL_PERSONAL = "mailPersonal";
    public static final String COLUMN_OFERTA_ID="ofertaId";

    public static final String COLUMN_USUARIO_GRADUADO="graduadoUser";
    public static final String COLUMN_SEGUNDO_NOMBRE_GRADUADO="segundoNombre";
    public static final String COLUMN_PRIMER_APELLIDO_GRADUADO="primerApellido";
    public static final String COLUMN_TELEFONO_GRADUADO="telefono";
    public static final String COLUMN_ESTADO_GRADUADO="estadoGraduado";


    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID_GRADUADO + " INTEGER PRIMARY KEY," +
                    COLUMN_ID_USER_GRADUADO + " TEXT,"+
                    COLUMN_USUARIO_GRADUADO + " TEXT,"+
                    COLUMN_PRIMER_NOMBRE + " TEXT,"+
                    COLUMN_SEGUNDO_NOMBRE_GRADUADO + " TEXT,"+
                    COLUMN_PRIMER_APELLIDO_GRADUADO + " TEXT,"+
                    COLUMN_TELEFONO_GRADUADO + " TEXT,"+
                    COLUMN_ESTADO_GRADUADO + " TEXT,"+
                    COLUMN_MAIL_PERSONAL + " TEXT," +
                    COLUMN_OFERTA_ID + " TEXT" +
                    ")";
    public static final String DELETE_TABLE_QUERY =
            "DROP TABLE " + TABLE_NAME;




}
