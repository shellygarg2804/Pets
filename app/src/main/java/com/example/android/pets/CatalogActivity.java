/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import data.petdbhelper;
import data.petsContract.petsEntry;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static petdbhelper mDbHelper;
    private  static PetCursorAdaptor adaptor;
    private static int LOADER_ID= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView listView= (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        //setting up adaptor to the listview
        adaptor= new PetCursorAdaptor(this,null);
        listView.setAdapter(adaptor);

        getLoaderManager().initLoader(LOADER_ID, null,this);

        //this will open when an item is clicked upon.
        //setting onitemclickListner for the same
        //it [passes intent to editors Activity with the unique uri of the respective item
        //opens editors activity with uri passed as data to edit a pet

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Uri uri= ContentUris.withAppendedId(petsEntry.CONTENT_URI,id);
                Intent i= new Intent(CatalogActivity.this,EditorActivity.class);
                //set data field of intent with unique uri appended with id.
                i.setData(uri);
                //launch Activity
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertdata();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                DeleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteAllPets() {
        int rowsDeleted = getContentResolver().delete(petsEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");

    }


    private void insertdata() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values= new ContentValues();
        values.put(petsEntry.COLUMN_PET_NAME,"Toto");
        values.put(petsEntry.COLUMN_PET_BREED,"Terrier");
        values.put(petsEntry.COLUMN_PET_GENDER, petsEntry.GENDER_MALE);
        values.put(petsEntry.COLUMN_PET_WEIGHT, 7);

        Uri newuri = getContentResolver().insert(petsEntry.CONTENT_URI,values);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection ={
                petsEntry._ID,
                petsEntry.COLUMN_PET_NAME,
                petsEntry.COLUMN_PET_BREED
        };

        return new android.content.CursorLoader(this,petsEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        adaptor.swapCursor(data);    //Cursor(data) recieved from on create Loader method will be provided(swaped) in the adaptor

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adaptor.swapCursor(null);

    }

}

