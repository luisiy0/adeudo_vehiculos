package finanzas.col.gob.mx.adeudo.DB;

import android.provider.BaseColumns;

/**
 * Created by lnunez on 16/12/2015.
 */
public class DBContract {

    public static final String CONTENT_AUTHORITY = "finanzasyadministracion.col.gob.mx.adeudovehicular";

    public static final class Vehiculo implements BaseColumns {
        public static final String TABLE_NAME = "vehiculo";

        public static final String COLUMN_PLACA = "placa";
        public static final String COLUMN_SERIE = "serie";
    }

    public static final class Vehiculo_Detalle implements BaseColumns {
        public static final String TABLE_NAME = "vehiculo_detalle";

        public static final String COLUMN_FK_Vehiculo_Id = "vehiculo_Id";

        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_MODELO = "modelo";
        public static final String COLUMN_SERIE = "serie";
        public static final String COLUMN_ID_VEHICULO = "id_vehiculo";
        public static final String COLUMN_MARCA = "marca";
        public static final String COLUMN_LINEA = "linea";
        public static final String COLUMN_VERSION = "version";
        public static final String COLUMN_SUBSIDIO = "subsidio";

    }

    public static final class Vehiculo_Concepto implements BaseColumns {
        public static final String TABLE_NAME = "vehiculo_concepto";

        public static final String COLUMN_FK_Vehiculo_Id = "vehiculo_Id";

        public static final String COLUMN_EJERCICIO = "ejercicio";
        public static final String COLUMN_CONCEPTO = "concepto";
        public static final String COLUMN_IMPORTE = "importe";
    }

    public static final class Vehiculo_Linea implements BaseColumns {
        public static final String TABLE_NAME = "vehiculo_linea";

        public static final String COLUMN_FK_Vehiculo_Id = "vehiculo_Id";

        public static final String COLUMN_LINEA = "linea";
        public static final String COLUMN_FECHA_VENCIMIENTO = "fecha_vencimiento";
        public static final String COLUMN_TOTAL = "total";
    }


    public static final class Vehiculo_Recibo implements BaseColumns {
        public static final String TABLE_NAME = "vehiculo_recibo";

        public static final String COLUMN_FK_Vehiculo_Id = "vehiculo_Id";

        public static final String COLUMN_RECIBO = "recibo";
        public static final String COLUMN_LINEA = "linea";
    }

    public static final class Bancos implements BaseColumns {
        public static final String TABLE_NAME = "bancos";

        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_DIRECCION = "direccion";
        public static final String COLUMN_LATITUD = "latitud";
        public static final String COLUMN_LONGITUD = "longitud";
    }

    public static final class Receptoria implements BaseColumns {
        public static final String TABLE_NAME = "receptoria";

        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_DIRECCION = "direccion";
        public static final String COLUMN_TELEFONO = "telefono";
        public static final String COLUMN_LATITUD = "latitud";
        public static final String COLUMN_LONGITUD = "longitud";
    }

}
