package sv.ues.fia.eisi.trues.db.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.trues.db.DatabaseHelper;
import sv.ues.fia.eisi.trues.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.trues.db.entity.Usuario;

public class UsuarioControl {

    private final Context context;
    private DatabaseHelper databaseHelper;
    private AccesoUsuarioControl accesoUsuarioControl;

    public UsuarioControl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, "TRUES", null, 1);
        accesoUsuarioControl = new AccesoUsuarioControl(context);
    }


    //CREAR

    public void crearUsuario(Usuario usuario){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String mensaje;
        String args[] = {usuario.getUsuario()};
        Cursor cursor = db.query("usuario", null, "usuario = ?", args, null,null, null);
        ContentValues values =  new ContentValues();
        values.put("usuario", usuario.getUsuario());
        values.put("password", usuario.getContraseña());
        values.put("nombre", usuario.getNombre());
        values.put("apellido", usuario.getApellido());
        values.put("tipo", usuario.getTipo());
        values.put("idFacultad", usuario.getIdFacultad());

        if (cursor.moveToFirst()){
            mensaje = "Error: este usuario ya fue registrado";
        }
        else {
            db.insert("usuario", null, values);

            if (usuario.getTipo()){
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "00");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "01");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "02");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "03");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "20");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "21");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "22");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "23");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "40");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "41");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "42");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "43");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "50");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "51");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "52");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "53");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "60");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "61");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "62");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "63");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "70");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "71");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "72");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "73");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "80");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "81");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "82");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "83");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "90");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "91");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "92");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "93");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "100");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "101");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "102");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "103");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "110");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "111");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "112");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "113");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "121");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "120");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "123");

            }
            else {
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "01");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "10");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "11");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "12");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "13");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "21");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "31");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "32");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "41");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "52");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "61");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "71");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "81");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "91");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "101");
                accesoUsuarioControl.CrearAccesoUsuario(usuario.getUsuario(), "111");
            }

            mensaje = "Usuario " + usuario.getUsuario() + " registrado con éxito.";
        }

        db.close();

        Toast.makeText(context.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }


    //INICIAR SESIÓN

    public Usuario iniciarSesion(String usuarioId, String contraseña){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Usuario usuario = null;
        String args[] = {usuarioId, contraseña};
        String columnas[] = {"usuario","password", "nombre", "apellido", "tipo", "idFacultad"};
        Cursor cursor = db.query("usuario", columnas, "usuario = ? AND password = ?", args, null, null, null);

        if (cursor.moveToFirst()){
            usuario = new Usuario();
            usuario.setUsuario(cursor.getString(0));
            usuario.setContraseña(cursor.getString(1));
            usuario.setNombre(cursor.getString(2));
            usuario.setApellido(cursor.getString(3));
            usuario.setTipo(cursor.getInt(4) == 1);
            usuario.setIdFacultad(cursor.getInt(5));
        }

        return usuario;
    }


    //CONSULTAR ESTADO DE BD

    public boolean consultarBD(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        boolean estado = false;
        Cursor cursor = db.rawQuery("SELECT * FROM usuario", null);
        if (cursor.moveToFirst()){
            estado = true;
        }

        return estado;
    }

    public List<Usuario> obtenerUsuarios(Integer idFacultad){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Usuario> usuarioList = new ArrayList<>();
        Usuario usuario;
        String[] args = {String.valueOf(idFacultad)};
        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE idFacultad = ?", args);

        while (cursor.moveToNext()){
            usuario = new Usuario();
            usuario.setUsuario(cursor.getString(0));
            usuarioList.add(usuario);
        }
        return usuarioList;
    }

}
