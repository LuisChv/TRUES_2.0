package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.Tramite;
import sv.ues.fia.eisi.trues.db.entity.Usuario;
import sv.ues.fia.eisi.trues.db.entity.UsuarioTramite;

public class UsuarioTramiteControl {
    private final Context context;
    private DatabaseHelper databaseHelper;
    private TramiteControl tramiteControl;

    public UsuarioTramiteControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
        tramiteControl = new TramiteControl(context);
    }

    //CREAR
    /*idUsuarioTramite INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
     usuario VARCHAR(7) NOT NULL,
     idTramite INTEGER NOT NULL,
      progreso REAL(10) NOT NULL*/
    public void crearUTramite(String usuario, Integer idTramite) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("usuario", usuario);
        values.put("idTramite",idTramite);
        values.put("progreso", (float) 0);

        db.insert("usuarioTramite",null,values);

        String[] args = {String.valueOf(idTramite)};
        Cursor cursor = db.rawQuery("SELECT * FROM paso WHERE idTramite = ?", args);

        while (cursor.moveToNext()){
            values = new ContentValues();
            values.put("usuario", usuario);
            values.put("idPaso", cursor.getInt(0));
            values.put("estado", false);
            db.insert("usuarioPaso", null, values);
        }
        db.close();
    }
    //OBTENER
    public UsuarioTramite obtenerUTramite(Integer idUTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String mensaje="";
        String args[] ={String.valueOf(idUTramite)};
        String[] columnas={"idUsuarioTramite","usuario","idTramite","progreso"};
        Cursor cursor=db.query("usuarioTramite",columnas,"idUsuarioTramite = ?",args,null, null, null);
        if(cursor.moveToFirst()){
            UsuarioTramite uTramite= new UsuarioTramite();
            uTramite.setIdUsuarioT(cursor.getInt(0));
            uTramite.setUsuario(cursor.getString(1));
            uTramite.setIdTramite(cursor.getInt(2));
            uTramite.setProgreso(cursor.getFloat(3));
            return uTramite;
        }else{
            return null;
        }
    }


    public Boolean existeUTramite(String usuario, Integer idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String args[] ={usuario, String.valueOf(idTramite)};
        Cursor cursor=db.query("usuarioTramite",null,"usuario = ? AND idTramite = ?",args,null, null, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }


    /*idUsuarioTramite INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
     usuario VARCHAR(7) NOT NULL,
     idTramite INTEGER NOT NULL,
      progreso REAL(10) NOT NULL*/


    //OBTENER LISTA DE TRAMITES
    public List<UsuarioTramite> obtenerMisTramites(String usuario){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<UsuarioTramite> tramiteList = new ArrayList<>();
        String[]args={usuario};
        UsuarioTramite usuarioTramite;
        Cursor cursor = db.rawQuery("SELECT * FROM usuarioTramite WHERE usuario = ?", args);

        while (cursor.moveToNext()){
            usuarioTramite = new UsuarioTramite();
            usuarioTramite.setIdUsuarioT(cursor.getInt(0));
            usuarioTramite.setUsuario(cursor.getString(1));
            usuarioTramite.setIdTramite(cursor.getInt(2));
            usuarioTramite.setProgreso(cursor.getFloat(3));
            tramiteList.add(usuarioTramite);
        }
        cursor.close();
        return tramiteList;
    }

    //ELIMINAR
    public void eliminarUTramite(Integer idUTramite, Integer idTramite, String usuario){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[]args={String.valueOf(idUTramite)};
        db.delete("usuarioTramite","idUsuarioTramite=?",args);

        String[] args2 = {String.valueOf(idTramite), usuario};
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarioPaso up JOIN paso p " +
                        "WHERE(p.idPaso = up.idPaso AND p.idTramite = ? AND up.usuario = ?)", args2);

        while (cursor.moveToNext()){
            String[] args3 = {String.valueOf(cursor.getInt(0))};
            db.delete("usuarioPaso", "idUsuarioPaso = ?", args3);
        }

        cursor.close();
        Toast.makeText(context.getApplicationContext(), "Se ha eliminado su progreso.", Toast.LENGTH_SHORT).show();
        db.close();
    }
}
