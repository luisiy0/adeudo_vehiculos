package finanzas.col.gob.mx.adeudo;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import finanzas.col.gob.mx.adeudo.Utilities.DrawerItem;
import finanzas.col.gob.mx.adeudo.Utilities.DrawerListAdapter;
import finanzas.col.gob.mx.adeudo.Utilities.Utils;

public class Mapa extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence activityTitle;
    private CharSequence itemTitle;

    private ArrayList<String> Bancos;
    TextView Titulo;
    String tab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        //regresar el tab seleccionado
        if (getIntent().hasExtra("tab")) {
            tab = getIntent().getStringExtra("tab").toString();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("tab", tab);
            setResult(Activity.RESULT_OK, returnIntent);
        }

        Titulo = (TextView) findViewById(R.id.titulo);

        itemTitle = activityTitle = getTitle();



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Setear una sombra sobre el contenido principal cuando el drawer se despliegue
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);


        ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();


        Bancos = new ArrayList<>();
        Bancos.add(Utils.Afirme);
        Bancos.add(Utils.BANBAJIO);
        Bancos.add(Utils.BANAMEX);
        Bancos.add(Utils.BANORTE);
        Bancos.add(Utils.HSBC);
        Bancos.add(Utils.SANTANDER);
        Bancos.add(Utils.SCOTIABANK);
        Bancos.add(Utils.TELECOMM);

        //Crear elementos de la lista
        items.add(new DrawerItem(Utils.Afirme, R.drawable.afirme));
        items.add(new DrawerItem(Utils.BANBAJIO, R.drawable.bajio));
        items.add(new DrawerItem(Utils.BANAMEX, R.drawable.banamex));
        items.add(new DrawerItem(Utils.BANORTE, R.drawable.banorte));
        items.add(new DrawerItem(Utils.HSBC, R.drawable.hsbc));
        items.add(new DrawerItem(Utils.SANTANDER, R.drawable.santander));
        items.add(new DrawerItem(Utils.SCOTIABANK, R.drawable.scotia));
        items.add(new DrawerItem(Utils.TELECOMM, R.drawable.telecomm));

        // Relacionar el adaptador y la escucha de la lista del drawer
        drawerList.setAdapter(new DrawerListAdapter(this, items));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Habilitar el icono de la app por si hay algún estilo que lo deshabilitó
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        // Crear ActionBarDrawerToggle para la apertura y cierre
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(itemTitle);

                /*Usa este método si vas a modificar la action bar
                con cada fragmento
                 */
                //invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(activityTitle);

                /*Usa este método si vas a modificar la action bar
                con cada fragmento
                 */
                //invalidateOptionsMenu();
            }
        };
        //Seteamos la escucha
        drawerLayout.setDrawerListener(drawerToggle);

            if (savedInstanceState == null) {
                selectItem(0);
            }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mapa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            // Toma los eventos de selección del toggle aquí
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* La escucha del ListView en el Drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Reemplazar el contenido del layout principal por un fragmento
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(Utils.Banco, Bancos.get(position));

        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Se actualiza el item seleccionado y el título, después de cerrar el drawer
        drawerList.setItemChecked(position, true);
        setTitle(Bancos.get(position));
        Titulo.setText(Bancos.get(position));
        drawerLayout.closeDrawer(drawerList);
    }

    /* Método auxiliar para setear el titulo de la action bar */
    @Override
    public void setTitle(CharSequence title) {
        itemTitle = title;
        getSupportActionBar().setTitle(itemTitle);
        Titulo.setText(itemTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
            // Sincronizar el estado del drawer
            drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Cambiar las configuraciones del drawer si hubo modificaciones
        drawerToggle.onConfigurationChanged(newConfig);
    }



}
