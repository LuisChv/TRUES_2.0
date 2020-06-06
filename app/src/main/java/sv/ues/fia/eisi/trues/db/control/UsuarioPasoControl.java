package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.R;
import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.Paso;
import sv.ues.fia.eisi.trues.db.entity.PasoEstado;
import sv.ues.fia.eisi.trues.db.entity.Usuario;
import sv.ues.fia.eisi.trues.db.entity.UsuarioPaso;

public class UsuarioPasoControl {
    private final Context context;
    private DatabaseHelper databaseHelper;

    public UsuarioPasoControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
    }

    //CREAR
/*idUsuarioPaso INTEGER NOT NULL PRIMARY KEY,
 usuario VARCHAR(7) NOT NULL,
 idPaso INTEGER NOT NULL*/
    public String crearUsuarioPaso(String usuario, Integer idPaso, boolean estado){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        ContentValues values = new ContentValues();
        values.put("idPaso", idPaso);
        values.put("usuario",usuario);
        values.put("estado", estado);
        db.insert("usuarioPaso", null, values);
        mensaje="se ha creado con extio el objeto usuarioPaso";
        db.close();
        return mensaje;
    }

    //Obtener
    public UsuarioPaso obtenerUsuarioPaso(String id){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String mensaje;
        String args[] = {id};
        String [] columnas={"idUsuarioPaso","usuario","idPaso","estado"};
        Cursor cursor=db.query("usuarioPaso",columnas, "idUsuarioPaso=?",args,null,null,null);
        UsuarioPaso uPaso=null;
        if(cursor.moveToFirst()){
            uPaso=new UsuarioPaso();
            uPaso.setIdUsuarioPaso(cursor.getInt(0));
            uPaso.setUsuario(cursor.getString(1));
            uPaso.setIdPaso(cursor.getInt(2));
            uPaso.setEstado(cursor.getInt(0)==1);
        }
        return uPaso;
    }

    public List<PasoEstado> obtenerMisPasos(String usuario, int idTramite){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String args[] = {usuario,String.valueOf(idTramite)};
        Cursor cursor = db.rawQuery("SELECT * FROM  usuarioPaso up JOIN paso p  " +
                "WHERE ( up.usuario = ? AND p.idTramite = ? AND p.idPaso = up.idPaso)",args);

        List<PasoEstado> pasoEstadoList = new ArrayList<>();
        PasoEstado pasoEstado = null;

        while (cursor.moveToNext()){
            pasoEstado = new PasoEstado();
            pasoEstado.setIdUsuarioPaso(cursor.getInt(0));
            pasoEstado.setIdPaso(cursor.getInt(2));
            pasoEstado.setEstado((cursor.getInt(3)==1));
            pasoEstado.setDescripcionPaso(cursor.getString(7));
            pasoEstado.setPorcentaje(cursor.getFloat(8));
            pasoEstadoList.add(pasoEstado);
        }
        return pasoEstadoList;
    }

    public List<PasoEstado> obtenerTodosPasos(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT p.descripcionPaso, up.estado " +
                "FROM usuarioPaso AS up INNER JOIN paso AS p ON up.idPaso = p.idPaso ",null);
        List<PasoEstado> pasoEstadoList = new ArrayList<>();
        PasoEstado pasoEstado = null;
        while (cursor.moveToNext()){
            pasoEstado = new PasoEstado();
            pasoEstado.setIdPaso(cursor.getInt(0));
            pasoEstado.setDescripcionPaso(cursor.getString(3));
            pasoEstado.setEstado((cursor.getInt(2)==1?true:false));
            pasoEstadoList.add(pasoEstado);
        }
        return pasoEstadoList;
    }

    //eliminar
    public String eliminarUsuarioPaso(String id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {id};
        UsuarioPaso uPaso= new UsuarioPaso();
        uPaso=obtenerUsuarioPaso(id);
        Cursor cursor=db.query("usuarioPaso", null, "idUsuarioPaso=?", args, null,null,null);
        if(cursor.moveToFirst()){
            db.delete("usuarioPaso","idUsuarioPaso=?",args);
            mensaje="Se ha eliminado con exito el objeto UsuarioPaso: " +uPaso.getIdUsuarioPaso();
        }
        else{
            mensaje="No se ha podido eliminar el objeto, ya que este no existe o no se ha podido encontrar";
        }
        return mensaje;
    }

    public  void setEstado(int id, boolean estado){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put("estado", estado);
        db.update("usuarioPaso", values, "idUsuarioPaso = ?", args);
        db.close();

    }
}
