package com.gamesmindt.myapplication.bd;

public class JobTable {
    public static final String TABLE_NAME = "OfertaLaboral";
    public static final String COLUMN_ID_JOB = "id";
    public static final String COLUMN_NOMBRE_EMPRESA = "nombreEmpresa";
    public static final String COLUMN_CARGO_EMPRESA = "cargo";

    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID_JOB + " INTEGER PRIMARY KEY," +
                    COLUMN_NOMBRE_EMPRESA + " TEXT,"+
                    COLUMN_CARGO_EMPRESA + " TEXT" +
                    ")";
    public static final String DELETE_TABLE_QUERY =
            "DROP TABLE " + TABLE_NAME;




}
