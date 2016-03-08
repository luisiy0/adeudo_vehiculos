package finanzas.col.gob.mx.adeudo.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import finanzas.col.gob.mx.adeudo.Utilities.Utils;
import finanzas.col.gob.mx.adeudo.VehiculoFragment;


import  finanzas.col.gob.mx.adeudo.DB.DBContract.Vehiculo;
import  finanzas.col.gob.mx.adeudo.DB.DBContract.Vehiculo_Detalle;
import  finanzas.col.gob.mx.adeudo.DB.DBContract.Vehiculo_Concepto;
import  finanzas.col.gob.mx.adeudo.DB.DBContract.Vehiculo_Linea;
import  finanzas.col.gob.mx.adeudo.DB.DBContract.Vehiculo_Recibo;
import  finanzas.col.gob.mx.adeudo.DB.DBContract.Bancos;
import  finanzas.col.gob.mx.adeudo.DB.DBContract.Receptoria;

/**
 * Created by lnunez on 07/12/2015.
 */
public class DBHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "DBVehiculos.db";
    public static final int VERSION = 26;


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_Vehiculo_TABLE = "create table " + Vehiculo.TABLE_NAME + " ( " +
                Vehiculo._ID + " integer primary key, " +
                Vehiculo.COLUMN_PLACA + " text NOT NULL , " +
                Vehiculo.COLUMN_SERIE + " text NOT NULL ); "
                ;

        final String SQL_CREATE_Vehiculo_Recibo_TABLE = "create table " + Vehiculo_Recibo.TABLE_NAME + " ( " +
                Vehiculo_Recibo._ID + " integer primary key, " +
                Vehiculo_Recibo.COLUMN_RECIBO + " text NOT NULL , " +
                Vehiculo_Recibo.COLUMN_LINEA + " text NOT NULL , " +
                Vehiculo_Recibo.COLUMN_FK_Vehiculo_Id + " integer not null , " +
                " FOREIGN KEY (" + Vehiculo_Recibo.COLUMN_FK_Vehiculo_Id + ") REFERENCES " +
                Vehiculo.TABLE_NAME + " (" + Vehiculo._ID + ") ); "
                ;

        final String SQL_CREATE_Vehiculo_Linea_TABLE = "create table " + Vehiculo_Linea.TABLE_NAME + " ( " +
                Vehiculo_Linea._ID + " integer primary key, " +
                Vehiculo_Linea.COLUMN_LINEA + " text NOT NULL , " +
                Vehiculo_Linea.COLUMN_FECHA_VENCIMIENTO + " text NOT NULL , " +
                Vehiculo_Linea.COLUMN_TOTAL + " real not null , " +
                Vehiculo_Linea.COLUMN_FK_Vehiculo_Id + " integer not null , " +
                " FOREIGN KEY (" + Vehiculo_Linea.COLUMN_FK_Vehiculo_Id + ") REFERENCES " +
                Vehiculo.TABLE_NAME + " (" + Vehiculo._ID + ") ); "
                ;

        final String SQL_CREATE_Vehiculo_Concepto_TABLE = "create table " + Vehiculo_Concepto.TABLE_NAME + " ( " +
                Vehiculo_Concepto._ID + " integer primary key, " +
                Vehiculo_Concepto.COLUMN_EJERCICIO + " integer NOT NULL , " +
                Vehiculo_Concepto.COLUMN_CONCEPTO + " text NOT NULL , " +
                Vehiculo_Concepto.COLUMN_IMPORTE + " real NOT NULL , " +
                Vehiculo_Concepto.COLUMN_FK_Vehiculo_Id + " integer not null , " +
                " FOREIGN KEY (" + Vehiculo_Concepto.COLUMN_FK_Vehiculo_Id + ") REFERENCES " +
                Vehiculo.TABLE_NAME + " (" + Vehiculo._ID + ") ); "
                ;

        final String SQL_CREATE_Vehiculo_Detalle_TABLE = "create table " + Vehiculo_Detalle.TABLE_NAME + " ( " +
                Vehiculo_Detalle._ID + " integer primary key, " +
                Vehiculo_Detalle.COLUMN_NOMBRE + " text NOT NULL , " +
                Vehiculo_Detalle.COLUMN_MODELO + " text NOT NULL , " +
                Vehiculo_Detalle.COLUMN_SERIE + " text NOT NULL , " +
                Vehiculo_Detalle.COLUMN_ID_VEHICULO + " text NOT NULL , " +
                Vehiculo_Detalle.COLUMN_MARCA + " text NOT NULL , " +
                Vehiculo_Detalle.COLUMN_LINEA + " text NOT NULL , " +
                Vehiculo_Detalle.COLUMN_VERSION + " text NOT NULL , " +
                Vehiculo_Detalle.COLUMN_SUBSIDIO + " text NULL , " +
                Vehiculo_Detalle.COLUMN_FK_Vehiculo_Id + " integer not null , " +
                " FOREIGN KEY (" + Vehiculo_Detalle.COLUMN_FK_Vehiculo_Id + ") REFERENCES " +
                Vehiculo.TABLE_NAME + " (" + Vehiculo._ID + ") ); "
                ;

        final String SQL_CREATE_Bancos_TABLE = "create table " + Bancos.TABLE_NAME + " ( " +
                Bancos._ID + " integer primary key, " +
                Bancos.COLUMN_NOMBRE + " text NOT NULL , " +
                Bancos.COLUMN_DIRECCION + " text NOT NULL , " +
                Bancos.COLUMN_LATITUD + " text NOT NULL , " +
                Bancos.COLUMN_LONGITUD + " text NOT NULL  " + " ); ";


        final String SQL_CREATE_Receptoria_TABLE = "create table " + Receptoria.TABLE_NAME + " ( " +
                Receptoria._ID + " integer primary key, " +
                Receptoria.COLUMN_NOMBRE + " text NOT NULL , " +
                Receptoria.COLUMN_DIRECCION + " text NOT NULL , " +
                Receptoria.COLUMN_TELEFONO + " text , " +
                Receptoria.COLUMN_LATITUD + " text NOT NULL , " +
                Receptoria.COLUMN_LONGITUD + " text NOT NULL  " + " ); ";


        // TODO Auto-generated method stub
        db.execSQL(SQL_CREATE_Vehiculo_TABLE);
        db.execSQL(SQL_CREATE_Vehiculo_Recibo_TABLE);
        db.execSQL(SQL_CREATE_Vehiculo_Linea_TABLE);
        db.execSQL(SQL_CREATE_Vehiculo_Concepto_TABLE);
        db.execSQL(SQL_CREATE_Vehiculo_Detalle_TABLE);
        db.execSQL(SQL_CREATE_Bancos_TABLE);
        db.execSQL(SQL_CREATE_Receptoria_TABLE);
        DatosBancos(db);
        DatosReceptoria(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS vehiculos");
        db.execSQL("DROP TABLE IF EXISTS " + Vehiculo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Vehiculo_Detalle.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Vehiculo_Concepto.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Vehiculo_Linea.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Vehiculo_Recibo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Bancos.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Receptoria.TABLE_NAME);
        onCreate(db);
    }

    private void DatosBancos(SQLiteDatabase db){

        ContentValues contentValues = new ContentValues();
        //afirme
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.Afirme);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv Sevilla Del Rio #298\nLomas Vista Hermosa");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2516821");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7129837");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.Afirme);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nTercer Anillo Periferico S/N\nEl Diezmo");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2558591");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.6903317");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.Afirme);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nBulevar Miguel de la Madrid #1847\nSalagua");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.1063651");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.335468");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.Afirme);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nCalle Mexico #112\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0535229");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3177062");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.Afirme);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Minatitlan\nCalle Portal Zaragoza S/N\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.3859155");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.0509681");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.Afirme);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Tecoman\nCalle Medellin #116, Centro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.8986822");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.8772402");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        //Banco del bajio
        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANBAJIO);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nFelipe Sevilla del Río # 290\nLomas de Circunvalación");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2516825");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7129842");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANBAJIO);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Tecoman\nHeroico Colegio Militar #215\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.9161592");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.8830702");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANBAJIO);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Villa de Álvarez\nAv. Benito Juárez #106\nAlta Villa");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2631349");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.742292");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        // banamex
        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nMiguel Hidalgo #90\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2428394");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7289894");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,  Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nPedro A Galvan #215\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2399604");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7182735");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,  Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv. Constitución #555\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2521358");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7224422");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,  Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv. Felipe Sevilla Del Rio #199\nJardines Vista Hermosa");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2548488");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7170129");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,  Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Villa de Álvarez\nMa. Ahumada De Gómez #371\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2721845");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7394628");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,  Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Tecoman\nAv. López Mateos #224\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.9134912");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.8774877");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,  Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nMéxico #136\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0529594");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3194526");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,  Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nCalle 1 Norte #13\nFondeport");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.079651");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.290953");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,  Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nBlvd. Miguel De La Madrid KM 11.5\nSantiago");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.1057779");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3371834");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,  Utils.BANAMEX);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nAv. Elías Zamora #9\nNuevo Salahua");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0885438");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3003204");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        // banorte

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANORTE);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nFrancisco I. Madero #381\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2392125");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7221095");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANORTE);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv. Felipe Sevilla del Rio #201\nJardines Vista Hermosa");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2547009");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7162376");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANORTE);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\n20 De Noviembre #470\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2314283");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.727196");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANORTE);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Villa de Alvarez\nAv. Benito Juarez #240\nAlta Villa");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2561466");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7462391");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, "BANORTE");
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nBlvd. Miguel De La Madrid KM. 7.5\nPlaya Azul");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0924316");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3187205");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANORTE);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nAv. Mexico Esq. Ingenieros\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0492243");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.319908");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.BANORTE);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Tecoman\nAv. Lopez Mateos #339\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.9148628");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.8785142");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        // santander
        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nRey Coliman #305\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2347391");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.725954");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv. Mader #164\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2413367");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7275968");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nMaclovio Herrera 96\nJardines De La Corregidora");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2493034");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7295892");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE,Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv. San Fernando S/N\nJardines De Las Lomas");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2460532");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7161435");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv. Felipe Sevilla Del Rio #152\nLomas De Vista Hermosa");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2542187");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7156243");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Villa De Alvarez\nAv. Benito Juarez #20\nAlta Villa");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2645915");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7401407");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nMorelos #140\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0535011");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3172809");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nBlvd. Miguel De La Madrid #426\nTapeixtles");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.080297");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.2897924");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nBlvd. Miguel De La Madrid #1337\nSalahua");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.1066189");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3355859");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nJuarez y Morelos #13\nSantiago");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.1192457");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3531855");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SANTANDER);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Tecoman\nMedellin #33\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.9122753");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.8754707");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        //scotiabank
        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SCOTIABANK);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nFelipe Sevilla Del Rio #499\nLomas De Circunvalación");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2515273");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7119797");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SCOTIABANK);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv. Juarez #30\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2406549");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.725046");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SCOTIABANK);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Villa de Alvarez\nAv. Maria Ahumada de Gomez #372\nCampestre");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2724439");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.737543");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.SCOTIABANK);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nBlvd. Miguel de la Madrid #5750\nLas Brisas");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0857914");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3084552");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        //TELECOMM
        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nFrancisco I. Madero #243\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2406753");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7239874");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nInsurgentes #680\nCamino Real");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2432191");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7110834");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Los Tepames\nFrancisco I. Madero # 38-A\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0963919");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.6241424");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Armeria\nChihuahua #3\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.9374789");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.9659398");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Cofradía de Juarez\nAllende #30\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.959915");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.9571284");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Cuyutlan\nLa Paz #12\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.9169606");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.0656036");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Coquimatlan\nBenito Juárez #349\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2085411");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.8095967");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nVenustiano Carranza #4\nSantiago");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.1186982");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.356011");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nBrisas #310\nLas Brisas");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0672725");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3024412");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nPrimavera Mz. #53\nValle de las Garzas");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.1332901");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3405397");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "El Colomo\nVenustiano Carranza S/N\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0605285");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.2577247");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nCarrillo Puerto #135\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0531023");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3180748");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Villa de Alvarez\nAv. Niños Heroes #353\nJuan José Rios");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2558811");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7537297");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Villa de Alvarez\nMerced Cabrera #84\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.264978");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7383204");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Cuauhtémoc\nMiguel Hidalgo\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.3282061");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.6029769");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Queseria\nMoctezuma #1\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.3865095");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.5737537");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Minatitlan\nIndependencia #27\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.3864937");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.052172");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Comala\nGuillermo Prieto #100\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.32257");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7628634");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Ixtlahuacan\nInterior Presidencia Municipal\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0022263");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7383342");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Cerro de Ortega\nAv. Revolución s/n\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.7482052");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7165448");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Madrid\nArgentina #109\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0841606");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.8763786");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.TELECOMM);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Tecoman\nAldama #230\nCentro");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.9095971");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.875409");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        //HSBC
        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.HSBC);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nMadero #183");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2413355");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7268784");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.HSBC);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv. De Los Maestros #377");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2550315");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7312784");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.HSBC);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv Felipe Sevilla Del Rio 112");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2551598");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7178191");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.HSBC);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Colima\nAv Benito Juárez #106");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.2714633");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.7380955");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.HSBC);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Tecoman\nLibertad #2 y 4");
        contentValues.put(Bancos.COLUMN_LATITUD, "18.911066");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-103.8739099");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.HSBC);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nAv. Mexico #99");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.0531502");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.317009");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Bancos.COLUMN_NOMBRE, Utils.HSBC);
        contentValues.put(Bancos.COLUMN_DIRECCION, "Manzanillo\nArturo Meillon #13");
        contentValues.put(Bancos.COLUMN_LATITUD, "19.1154156");
        contentValues.put(Bancos.COLUMN_LONGITUD, "-104.3519926");
        db.insert(Bancos.TABLE_NAME, null, contentValues);

    }

    private void DatosReceptoria(SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Receptoria.COLUMN_NOMBRE, Utils.Armeria);
        contentValues.put(Receptoria.COLUMN_DIRECCION, "Centro\nSinaloa #4");
        //contentValues.put(Receptoria.COLUMN_TELEFONO, Utils.Afirme);
        contentValues.put(Receptoria.COLUMN_LATITUD, "18.9368903");
        contentValues.put(Receptoria.COLUMN_LONGITUD, "-103.9672044");
        db.insert(Receptoria.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Receptoria.COLUMN_NOMBRE, Utils.Colima);
        contentValues.put(Receptoria.COLUMN_DIRECCION, "Centro\nReforma #81");
        contentValues.put(Receptoria.COLUMN_TELEFONO, "3140270");
        contentValues.put(Receptoria.COLUMN_LATITUD, "19.2428809");
        contentValues.put(Receptoria.COLUMN_LONGITUD, "-103.7371715");
        db.insert(Receptoria.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Receptoria.COLUMN_NOMBRE, Utils.Manzanillo);
        contentValues.put(Receptoria.COLUMN_DIRECCION, "Centro\nVicente Guerrero #26");
        contentValues.put(Receptoria.COLUMN_TELEFONO, "3327022");
        contentValues.put(Receptoria.COLUMN_LATITUD, "19.0505596");
        contentValues.put(Receptoria.COLUMN_LONGITUD, "-104.3182979");
        db.insert(Receptoria.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Receptoria.COLUMN_NOMBRE, Utils.Tecoman);
        contentValues.put(Receptoria.COLUMN_DIRECCION, "La Floresta\nLerdo de Tejada #525");
        contentValues.put(Receptoria.COLUMN_TELEFONO, "3253016");
        contentValues.put(Receptoria.COLUMN_LATITUD, "18.911044");
        contentValues.put(Receptoria.COLUMN_LONGITUD, "-103.8644479");
        db.insert(Receptoria.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Receptoria.COLUMN_NOMBRE, Utils.TransporteColima);
        contentValues.put(Receptoria.COLUMN_DIRECCION, "De Los Trabajadores\nNicolás Lenin #1175");
        //contentValues.put(Receptoria.COLUMN_TELEFONO, "3253016");
        contentValues.put(Receptoria.COLUMN_LATITUD, "19.2338419");
        contentValues.put(Receptoria.COLUMN_LONGITUD, "-103.7063276");
        db.insert(Receptoria.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Receptoria.COLUMN_NOMBRE, Utils.TransporteManzanillo);
        contentValues.put(Receptoria.COLUMN_DIRECCION, "Barrio 5\nValle de las Garzas\nElias Zamora #2213");
        //contentValues.put(Receptoria.COLUMN_TELEFONO, "3253016");
        contentValues.put(Receptoria.COLUMN_LATITUD, "19.1134295");
        contentValues.put(Receptoria.COLUMN_LONGITUD, "-104.3246514");
        db.insert(Receptoria.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Receptoria.COLUMN_NOMBRE, Utils.OficinaCentral);
        contentValues.put(Receptoria.COLUMN_DIRECCION, "El Diezmo\nEjercito Mexicano S/N");
        //contentValues.put(Receptoria.COLUMN_TELEFONO, "3253016");
        contentValues.put(Receptoria.COLUMN_LATITUD, "19.2554782");
        contentValues.put(Receptoria.COLUMN_LONGITUD, "-103.6896838");
        db.insert(Receptoria.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Receptoria.COLUMN_NOMBRE, Utils.ModuloVehicularVilla);
        contentValues.put(Receptoria.COLUMN_DIRECCION, "Plaza Colima Soriana\nMaría Ahumada de Gómez #39,Local 49");
        //contentValues.put(Receptoria.COLUMN_TELEFONO, "3253016");
        contentValues.put(Receptoria.COLUMN_LATITUD, "19.2716163");
        contentValues.put(Receptoria.COLUMN_LONGITUD, "-103.7363063");
        db.insert(Receptoria.TABLE_NAME, null, contentValues);


    }


    public ArrayList<HashMap<String, String>> getRecibos(){
        ArrayList<HashMap<String, String>> Vehiculos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + Vehiculo_Recibo.TABLE_NAME + " INNER JOIN " + Vehiculo.TABLE_NAME +
                " on " + Vehiculo_Recibo.TABLE_NAME + "." + Vehiculo_Recibo.COLUMN_FK_Vehiculo_Id + " = " + Vehiculo.TABLE_NAME +"." + Vehiculo._ID +" ", null);

        res.moveToFirst();
        while(res.isAfterLast() == false){
            HashMap<String, String> vehi = new HashMap<String, String>();
            vehi.put(Vehiculo.COLUMN_PLACA,res.getString(res.getColumnIndex(Vehiculo.COLUMN_PLACA)));
            vehi.put(Vehiculo_Recibo.COLUMN_RECIBO, res.getString(res.getColumnIndex(Vehiculo_Recibo.COLUMN_RECIBO)));
            Vehiculos.add(vehi);
            res.moveToNext();
        }
        return Vehiculos;
    }

    public ArrayList<String> getPlacas()
    {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select DISTINCT  " + Vehiculo.COLUMN_PLACA + " from " + Vehiculo.TABLE_NAME , null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(Vehiculo.COLUMN_PLACA)));
            res.moveToNext();
        }

        return array_list;
    }

    public boolean existePlaca(String placa,String serie)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select count(*) as cantidad from " + Vehiculo_Linea.TABLE_NAME + " INNER JOIN " + Vehiculo.TABLE_NAME +
                " on " + Vehiculo_Linea.TABLE_NAME + "." + Vehiculo_Linea.COLUMN_FK_Vehiculo_Id + " = " + Vehiculo.TABLE_NAME + "." + Vehiculo._ID + " where  "
                + Vehiculo.TABLE_NAME + "." + Vehiculo.COLUMN_PLACA + " = '" + placa + "' and "
                + Vehiculo.TABLE_NAME + "." + Vehiculo.COLUMN_SERIE + " = '" + serie + "' ", null);
        if(res.getCount() > 0)
        {
            res.moveToFirst();
            if( Integer.parseInt(res.getString(res.getColumnIndex("cantidad"))) > 0)
            {
                return true;

            }else{
                return false;
            }
        }else {
            return false;
        }

    }

    public void GuardarPlaca(String placa, String serie){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select count(*) as total from " + Vehiculo.TABLE_NAME + " where  "+
                Vehiculo.COLUMN_PLACA + " = '" + placa + "'", null);
        if( res.getCount() > 0){
            res.moveToFirst();
            if(Integer.parseInt(res.getString(res.getColumnIndex("total"))) < 1){ // si no hay datos se guarda la placa
                db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(Vehiculo.COLUMN_PLACA, placa);
                contentValues.put(Vehiculo.COLUMN_SERIE, serie);
                db.insert(Vehiculo.TABLE_NAME, null, contentValues);
            }
        }
    }

    public void InsertVehiculoLinea( Integer Vehiculo_Id, String Linea, String Fecha_Vencimiento, Double Total ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Vehiculo_Linea.COLUMN_FK_Vehiculo_Id, Vehiculo_Id);
        contentValues.put(Vehiculo_Linea.COLUMN_LINEA, Linea);
        contentValues.put(Vehiculo_Linea.COLUMN_FECHA_VENCIMIENTO, Fecha_Vencimiento);
        contentValues.put(Vehiculo_Linea.COLUMN_TOTAL, Total);
        db.insert(Vehiculo_Linea.TABLE_NAME, null, contentValues);
    }

    public void UpdateVehiculoLinea( Integer Vehiculo_Id, String Linea, String Fecha_Vencimiento, Double Total ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Vehiculo_Linea.COLUMN_LINEA, Linea);
        contentValues.put(Vehiculo_Linea.COLUMN_FECHA_VENCIMIENTO, Fecha_Vencimiento);
        contentValues.put(Vehiculo_Linea.COLUMN_TOTAL, Total);
        db.update(Vehiculo_Linea.TABLE_NAME, contentValues, Vehiculo_Linea.COLUMN_FK_Vehiculo_Id + " = ? ", new String[]{String.valueOf(Vehiculo_Id)});
    }

    public Integer GetVehiculoId(String placa, String serie){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select " + Vehiculo._ID + " from " + Vehiculo.TABLE_NAME + " where  " +
                 Vehiculo.COLUMN_PLACA + " = '" + placa + "' and " +
                 Vehiculo.COLUMN_SERIE + " = '" + serie + "' ", null);
            res.moveToFirst();
        return Integer.parseInt(res.getString(res.getColumnIndex(Vehiculo._ID)));
    }

    public Integer GetVehiculoId(String Linea){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select " + Vehiculo_Linea.COLUMN_FK_Vehiculo_Id + " from " + Vehiculo_Linea.TABLE_NAME + " where  " +
                Vehiculo_Linea.COLUMN_LINEA + " = '" + Linea + "'", null);
        res.moveToFirst();
        return Integer.parseInt(res.getString(res.getColumnIndex(Vehiculo_Linea.COLUMN_FK_Vehiculo_Id )));
    }

    public void InsertVehiculoDetalle(Integer Vehiculo_Id, String Nombre, String Modelo, String Serie, String Id_Vehiculo, String Marca, String Linea, String Version,String Subsidio){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Vehiculo_Detalle.COLUMN_FK_Vehiculo_Id, Vehiculo_Id);
        contentValues.put(Vehiculo_Detalle.COLUMN_NOMBRE, Nombre);
        contentValues.put(Vehiculo_Detalle.COLUMN_MODELO, Modelo);
        contentValues.put(Vehiculo_Detalle.COLUMN_SERIE, Serie);
        contentValues.put(Vehiculo_Detalle.COLUMN_ID_VEHICULO, Id_Vehiculo);
        contentValues.put(Vehiculo_Detalle.COLUMN_MARCA, Marca);
        contentValues.put(Vehiculo_Detalle.COLUMN_LINEA, Linea);
        contentValues.put(Vehiculo_Detalle.COLUMN_VERSION, Version);
        contentValues.put(Vehiculo_Detalle.COLUMN_SUBSIDIO, Subsidio);
        db.insert(Vehiculo_Detalle.TABLE_NAME, null, contentValues);
    }

    public void UpdateVehiculoDetalle(Integer Vehiculo_Id, String Nombre, String Modelo, String Serie, String Id_Vehiculo, String Marca, String Linea, String Version,String Subsidio){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Vehiculo_Detalle.COLUMN_NOMBRE, Nombre);
        contentValues.put(Vehiculo_Detalle.COLUMN_MODELO, Modelo);
        contentValues.put(Vehiculo_Detalle.COLUMN_SERIE, Serie);
        contentValues.put(Vehiculo_Detalle.COLUMN_ID_VEHICULO, Id_Vehiculo);
        contentValues.put(Vehiculo_Detalle.COLUMN_MARCA, Marca);
        contentValues.put(Vehiculo_Detalle.COLUMN_LINEA, Linea);
        contentValues.put(Vehiculo_Detalle.COLUMN_VERSION, Version);
        contentValues.put(Vehiculo_Detalle.COLUMN_SUBSIDIO, Subsidio);
        db.update(Vehiculo_Detalle.TABLE_NAME, contentValues, Vehiculo_Detalle.COLUMN_FK_Vehiculo_Id + " = ? ", new String[]{String.valueOf(Vehiculo_Id)});
    }

    public void DeleteVehiculoConcepto(Integer Vehiculo_Id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Vehiculo_Concepto.TABLE_NAME, Vehiculo_Concepto.COLUMN_FK_Vehiculo_Id + " = ? ", new String[]{String.valueOf(Vehiculo_Id)});
    }

    public void InsertVehiculoConcepto(Integer Vehiculo_Id, ArrayList <HashMap<String, String>> lista){
        SQLiteDatabase db = this.getWritableDatabase();
        for(int i = 0; i < lista.size(); i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put(Vehiculo_Concepto.COLUMN_FK_Vehiculo_Id, Vehiculo_Id);
            contentValues.put(Vehiculo_Concepto.COLUMN_EJERCICIO, lista.get(i).get(VehiculoFragment.KEY_Ejercicio));
            contentValues.put(Vehiculo_Concepto.COLUMN_CONCEPTO, lista.get(i).get(VehiculoFragment.KEY_Concepto));
            contentValues.put(Vehiculo_Concepto.COLUMN_IMPORTE, lista.get(i).get(VehiculoFragment.KEY_Importe));
            db.insert(Vehiculo_Concepto.TABLE_NAME, null, contentValues);
        }

    }

    public  ArrayList<String>  GetVehiculosLinea(Integer Vehiculo_Id){
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select " + Vehiculo_Linea.COLUMN_LINEA + " , " +
                                    Vehiculo_Linea.COLUMN_FECHA_VENCIMIENTO +" , " +
                                    Vehiculo_Linea.COLUMN_TOTAL    +" from " + Vehiculo_Linea.TABLE_NAME +
                                  " where " + Vehiculo_Linea.COLUMN_FK_Vehiculo_Id + " = " +  String.valueOf(Vehiculo_Id), null);
        res.moveToFirst();

        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Linea.COLUMN_LINEA)));
        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Linea.COLUMN_FECHA_VENCIMIENTO)));
        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Linea.COLUMN_TOTAL)));

        return array_list;
    }

    public  ArrayList<String>  GetVehiculosDetalle(Integer Vehiculo_Id){
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select " + Vehiculo_Detalle.COLUMN_NOMBRE + " , " +
                Vehiculo_Detalle.COLUMN_MODELO +" , " +
                Vehiculo_Detalle.COLUMN_SERIE    +" , " +
                Vehiculo_Detalle.COLUMN_ID_VEHICULO+ " , " +
                Vehiculo_Detalle.COLUMN_MARCA + " , " +
                Vehiculo_Detalle.COLUMN_LINEA + " , " +
                Vehiculo_Detalle.COLUMN_VERSION +  " , " +
                Vehiculo_Detalle.COLUMN_SUBSIDIO +
                " from " + Vehiculo_Detalle.TABLE_NAME +
                " where " + Vehiculo_Detalle.COLUMN_FK_Vehiculo_Id + " = " +  String.valueOf(Vehiculo_Id), null);
        res.moveToFirst();

        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Detalle.COLUMN_NOMBRE)));
        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Detalle.COLUMN_MODELO)));
        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Detalle.COLUMN_SERIE)));
        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Detalle.COLUMN_ID_VEHICULO)));
        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Detalle.COLUMN_MARCA)));
        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Detalle.COLUMN_LINEA)));
        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Detalle.COLUMN_VERSION)));
        array_list.add(res.getString(res.getColumnIndex(Vehiculo_Detalle.COLUMN_SUBSIDIO)));

        return array_list;
    }

    public ArrayList<HashMap<String, String>> GetVehiculosConcepto(Integer Vehiculo_Id){
        ArrayList<HashMap<String, String>> Vehiculos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select " + Vehiculo_Concepto.COLUMN_EJERCICIO + " , " +
                Vehiculo_Concepto.COLUMN_CONCEPTO +" , " +
                Vehiculo_Concepto.COLUMN_IMPORTE + " from " + Vehiculo_Concepto.TABLE_NAME +
                " where " + Vehiculo_Concepto.COLUMN_FK_Vehiculo_Id + " = " +  String.valueOf(Vehiculo_Id), null);

        res.moveToFirst();
        while(res.isAfterLast() == false){
            HashMap<String, String> vehi = new HashMap<String, String>();
            vehi.put(Vehiculo_Concepto.COLUMN_EJERCICIO,res.getString(res.getColumnIndex(Vehiculo_Concepto.COLUMN_EJERCICIO)));
            vehi.put(Vehiculo_Concepto.COLUMN_CONCEPTO, res.getString(res.getColumnIndex(Vehiculo_Concepto.COLUMN_CONCEPTO)));
            vehi.put(Vehiculo_Concepto.COLUMN_IMPORTE, res.getString(res.getColumnIndex(Vehiculo_Concepto.COLUMN_IMPORTE)));
            Vehiculos.add(vehi);
            res.moveToNext();
        }
        return Vehiculos;
    }

   public ArrayList<HashMap<String, String>> GetBanco(String Banco){
       ArrayList<HashMap<String, String>> BancosA = new ArrayList<>();

       SQLiteDatabase db = this.getReadableDatabase();
       Cursor res =  db.rawQuery("select * from " + Bancos.TABLE_NAME  +
             " where " + Bancos.COLUMN_NOMBRE + " = '" +  Banco + "'",null);

       res.moveToFirst();
       while(res.isAfterLast() == false){
           HashMap<String, String> ban = new HashMap<String, String>();

           ban.put(Bancos.COLUMN_NOMBRE,res.getString(res.getColumnIndex(Bancos.COLUMN_NOMBRE)));
           ban.put(Bancos.COLUMN_DIRECCION, res.getString(res.getColumnIndex(Bancos.COLUMN_DIRECCION)));
           ban.put(Bancos.COLUMN_LATITUD, res.getString(res.getColumnIndex(Bancos.COLUMN_LATITUD)));
           ban.put(Bancos.COLUMN_LONGITUD, res.getString(res.getColumnIndex(Bancos.COLUMN_LONGITUD)));
           BancosA.add(ban);
           res.moveToNext();
       }

       return BancosA;
   }

    public ArrayList<HashMap<String, String>> GetReceptoria(){
        ArrayList<HashMap<String, String>> ReceptoriaA = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + Receptoria.TABLE_NAME ,null);

        res.moveToFirst();
        while(res.isAfterLast() == false){
            HashMap<String, String> ban = new HashMap<String, String>();

            ban.put(Receptoria.COLUMN_NOMBRE,res.getString(res.getColumnIndex(Receptoria.COLUMN_NOMBRE)));
            ban.put(Receptoria.COLUMN_DIRECCION, res.getString(res.getColumnIndex(Receptoria.COLUMN_DIRECCION)));
            ban.put(Receptoria.COLUMN_LATITUD, res.getString(res.getColumnIndex(Receptoria.COLUMN_LATITUD)));
            ban.put(Receptoria.COLUMN_LONGITUD, res.getString(res.getColumnIndex(Receptoria.COLUMN_LONGITUD)));
            ban.put(Receptoria.COLUMN_TELEFONO, res.getString(res.getColumnIndex(Receptoria.COLUMN_TELEFONO)));
            ReceptoriaA.add(ban);
            res.moveToNext();
        }

        return ReceptoriaA;
    }

    public void InsertRecibo(String Recibo, String Linea){

        SQLiteDatabase db = this.getReadableDatabase();
        int VehiculoId = GetVehiculoId(Linea);


        Cursor res =  db.rawQuery("select count(*) as cantidad from " + Vehiculo_Recibo.TABLE_NAME +
                " where " + Vehiculo_Recibo.COLUMN_RECIBO + " = '" + Recibo + "' and " +
                Vehiculo_Recibo.COLUMN_LINEA + " = '" + Linea + "' and " +
                Vehiculo_Recibo.COLUMN_FK_Vehiculo_Id + " = " + String.valueOf(VehiculoId) , null);
        res.moveToFirst();

        if (res.getInt(res.getColumnIndex("cantidad")) == 0)
        {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Vehiculo_Recibo.COLUMN_RECIBO , Recibo);
            contentValues.put(Vehiculo_Recibo.COLUMN_LINEA , Linea);
            contentValues.put(Vehiculo_Recibo.COLUMN_FK_Vehiculo_Id , VehiculoId);
            db.insert(Vehiculo_Recibo.TABLE_NAME, null, contentValues);
        }

    }

    public ArrayList<HashMap<String, String>> GetPlacaRecibo(){
        ArrayList<HashMap<String, String>> Placas = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery("select " + Vehiculo.COLUMN_PLACA + "," + Vehiculo_Recibo.COLUMN_RECIBO +
                " from " + Vehiculo_Recibo.TABLE_NAME +
                " inner join " + Vehiculo.TABLE_NAME + " on " +
                        Vehiculo_Recibo.TABLE_NAME + "." + Vehiculo_Recibo.COLUMN_FK_Vehiculo_Id + " = " +
                        Vehiculo.TABLE_NAME + "." + Vehiculo._ID  , null);

        res.moveToFirst();
        while(res.isAfterLast() == false){
            HashMap<String, String> vehi = new HashMap<String, String>();
            vehi.put(Vehiculo.COLUMN_PLACA ,res.getString(res.getColumnIndex(Vehiculo.COLUMN_PLACA )));
            vehi.put(Vehiculo_Recibo.COLUMN_RECIBO, res.getString(res.getColumnIndex(Vehiculo_Recibo.COLUMN_RECIBO)));
            Placas.add(vehi);
            res.moveToNext();
        }
        return Placas;
    }

    public ArrayList<String> GetReciboDetalle(String Recibo){
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery("select " + Vehiculo.COLUMN_PLACA + "," + Vehiculo_Detalle.TABLE_NAME + "." + Vehiculo_Detalle.COLUMN_SERIE + " as serie " +
                " from " + Vehiculo_Recibo.TABLE_NAME +
                " inner join " + Vehiculo.TABLE_NAME + " on " +
                Vehiculo_Recibo.TABLE_NAME + "." + Vehiculo_Recibo.COLUMN_FK_Vehiculo_Id + " = " +
                Vehiculo.TABLE_NAME + "." + Vehiculo._ID  +
                " inner join " + Vehiculo_Detalle.TABLE_NAME + " on " +
                Vehiculo_Detalle.TABLE_NAME + "." + Vehiculo_Detalle.COLUMN_FK_Vehiculo_Id + " = " +
                Vehiculo.TABLE_NAME + "." + Vehiculo._ID +
                " where " +  Vehiculo_Recibo.COLUMN_RECIBO + " = '" + Recibo + "'"
                , null);

        res.moveToFirst();

        array_list.add(res.getString(res.getColumnIndex(Vehiculo.COLUMN_PLACA)));
        array_list.add(res.getString(res.getColumnIndex( "serie")));

        return array_list;
    }


}
