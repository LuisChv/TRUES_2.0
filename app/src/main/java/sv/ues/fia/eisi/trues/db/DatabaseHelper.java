package sv.ues.fia.eisi.trues.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final String crearTablaUsuario =            "CREATE TABLE usuario (usuario VARCHAR(7) NOT NULL PRIMARY KEY, password VARCHAR(30) NOT NULL, nombre VARCHAR(30), apellido VARCHAR(30), tipo BOOLEAN NOT NULL, idFacultad INTEGER NOT NULL)";
    private final String crearTablaAccesoUsuario =      "CREATE TABLE accesoUsuario (idAccesoUsuario INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , usuario VARCHAR(7) NOT NULL, idOption CHAR(7) NOT NULL)";
    private final String crearTablaCargo =              "CREATE TABLE cargo (idCargo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombreCargo VARCHAR(128) NOT NULL)";
    private final String crearTablaDocumento =          "CREATE TABLE documento (idDocumento INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, url VARCHAR(256) NOT NULL, nombreDocumento VARCHAR(128) NOT NULL)";
    private final String crearTablaDocumentoTramite =   "CREATE TABLE documentoTramite (idDocumentoTramite INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idDocumento INTEGER NOT NULL, idTramite INTEGER NOT NULL)";
    private final String crearTablaFacultad =           "CREATE TABLE facultad (idFacultad INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombreFacultad VARCHAR(64) NOT NULL)";
    private final String crearTablaOpcionCrud =         "CREATE TABLE opcionCrud (idOpcion CHAR(3) NOT NULL PRIMARY KEY, descOpcion VARCHAR(30) NOT NULL)";
    private final String crearTablaPaso =               "CREATE TABLE paso (idPaso INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idPersonal INTEGER NOT NULL, idTramite INTEGER NOT NULL, descripcionPaso VARCHAR(256) NOT NULL, porcentaje REAL(10) NOT NULL)";
    private final String crearTablaPersonal =           "CREATE TABLE personal (idPersonal INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombrePersonal VARCHAR(128) NOT NULL)";
    private final String crearTablaPersonalCargo =      "CREATE TABLE personalCargo(idPersonalCargo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idCargo INTEGER NOT NULL, idPersonal INTEGER NOT NULL)";
    private final String crearTablaPersonalUnidadAdmin= "CREATE TABLE personalUnidadAdmin (idPersonalAdmin INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idPersonal INTEGER NOT NULL, idUnidad INTEGER NOT NULL)";
    private final String crearTablaPersonalUbicacion =  "CREATE TABLE personalUbicacion (idPersonalUbicacion INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idUbicacion INTEGER NOT NULL, idPersonal INTEGER NOT NULL)";
    private final String crearTablaRequisito =          "CREATE TABLE requisito (idRequisito INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, descripcion VARCHAR(256) NOT NULL)";
    private final String crearTablaRequisitoTramite =   "CREATE TABLE requisitoTramite(idRequisitoTramite INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idTramite INTEGER NOT NULL, idRequisito INTEGER NOT NULL)";
    private final String crearTablaTipoUsuario =        "CREATE TABLE tipoUsuario (idTipo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombreTipo VARCHAR(16) NOT NULL)";
    private final String crearTablaTramite =            "CREATE TABLE tramite (idTramite INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idFacultad INTEGER NOT NULL, nombreTramite VARCHAR(64) NOT NULL)";
    private final String crearTablaUbicacion =          "CREATE TABLE ubicacion (idUbicacion INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, longitud REAL NOT NULL, latitud REAL NOT NULL,altitud REAL NOT NULL, componenteTematica VARCHAR(128) NOT NULL)";
    private final String crearTablaUnidadAdmin =        "CREATE TABLE unidadAdmin (idUnidad INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idFacultad INTEGER NOT NULL, nombreUnidadAdmin VARCHAR(64) NOT NULL)";
    private final String crearTablaUsuarioTramite =     "CREATE TABLE usuarioTramite (idUsuarioTramite INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, usuario VARCHAR(7) NOT NULL, idTramite INTEGER NOT NULL, progreso REAL(10) NOT NULL)";
    private final String crearTablaUsuarioPaso =        "CREATE TABLE usuarioPaso (idUsuarioPaso INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, usuario VARCHAR(7) NOT NULL, idPaso INTEGER NOT NULL, estado INTEGER NOT NULL)";
    private final String crearTablaActividad =          "CREATE TABLE actividad (idActividad INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idFacultad INTEGER NOT NULL, nombreActividad VARCHAR(256) NOT NULL, inicio VARCHAR(10) NOT NULL, final VARCHAR(10) NOT NULL)";
    private final String crearTablaActividadTramite =   "CREATE TABLE actividadTramite (idActividadTramite INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, idTramite INTEGER NOT NULL, idActividad INTEGER NOT NULL)";


    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(crearTablaUsuario);
        sqLiteDatabase.execSQL(crearTablaAccesoUsuario);
        sqLiteDatabase.execSQL(crearTablaCargo);
        sqLiteDatabase.execSQL(crearTablaDocumento);
        sqLiteDatabase.execSQL(crearTablaDocumentoTramite);
        sqLiteDatabase.execSQL(crearTablaFacultad);
        sqLiteDatabase.execSQL(crearTablaOpcionCrud);
        sqLiteDatabase.execSQL(crearTablaPaso);
        sqLiteDatabase.execSQL(crearTablaPersonal);
        sqLiteDatabase.execSQL(crearTablaPersonalCargo);
        sqLiteDatabase.execSQL(crearTablaPersonalUnidadAdmin);
        sqLiteDatabase.execSQL(crearTablaPersonalUbicacion);
        sqLiteDatabase.execSQL(crearTablaRequisito);
        sqLiteDatabase.execSQL(crearTablaRequisitoTramite);
        sqLiteDatabase.execSQL(crearTablaTipoUsuario);
        sqLiteDatabase.execSQL(crearTablaTramite);
        sqLiteDatabase.execSQL(crearTablaUbicacion);
        sqLiteDatabase.execSQL(crearTablaUnidadAdmin);
        sqLiteDatabase.execSQL(crearTablaUsuarioTramite);
        sqLiteDatabase.execSQL(crearTablaUsuarioPaso);
        sqLiteDatabase.execSQL(crearTablaActividad);
        sqLiteDatabase.execSQL(crearTablaActividadTramite);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS usuario");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS accesoUsuario");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS calendario" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS cargo" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS documento" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS documentoTramite" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS facultad" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS opcionCrud" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS paso" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS personal" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS personalCargo" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS personalUnidadAdmin" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS personalUbicacion" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS requisito" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS requisitoTramite" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tipoUsuario" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tramite" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ubicacion" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS unidadAdmin" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS usuarioTramite" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS usuarioPaso" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS actividad");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS actividadTramite");
        onCreate(sqLiteDatabase);
    }
}
