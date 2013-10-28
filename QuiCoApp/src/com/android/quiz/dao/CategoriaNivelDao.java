package com.android.quiz.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.quiz.db.DBHelper;
import com.android.quiz.modelo.CategoriaNivel;

public class CategoriaNivelDao {
	
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public CategoriaNivelDao(Context contexto){
		helper = DBHelper.instance(contexto.getApplicationContext());
	}
	
	private SQLiteDatabase getDb(){
		if (db==null){
			db = helper.getWritableDatabase();
		}
		return db;
	}

	public void close(){
		helper.close();
		db = null;
	}
	
	// Consulta o status do n�vel e da categoria.
		public int consultaStatusNivel(int id_categoria, int id_nivel) {
			CategoriaNivel cat_niv = new CategoriaNivel();
			//int status;
			Cursor cursor = null;
			//dbQuery = this.getReadableDatabase();
			String tabela = "status_nivel";
			String where = "id_categoria=? AND id_nivel=?";
			String[] coluna = new String[] { "status_nivel" };
			String argumentos[] = new String[] { String.valueOf(id_categoria),
					String.valueOf(id_nivel) };

			cursor = getDb().query(tabela, coluna, where, argumentos, null, null,
					null);

			cursor.moveToFirst();

			cat_niv.setStatus_nivel(cursor.getInt(cursor.getColumnIndex("status_nivel")));

			return cat_niv.getStatus_nivel();
		}
		
		/*
		 * Atualiza o status do n�vel (bloqueado ou desbloqueado), recebendo como
		 * par�metros: id da tabela categoria, id do n�vel e o status que ser�
		 * atualizado.
		 */
		public int atualizaStatusNivel(int id_categoria_nivel, int status) {

			
			ContentValues values = new ContentValues();

			values.put("status_nivel", status);

			String tabela = "status_nivel";
			String where = "_id = ?";
			// String[] coluna = new String[] {"status_nivel"};
			String argumentos[] = new String[] {String.valueOf(id_categoria_nivel)};

			return getDb().update(tabela, values, where, argumentos);

		}
		
		//consulta o id da tabela status_nivel para usar na consulta de sele��o da perguntas
		public int consultaIdCategoriaNIvel(int id_categoria, int id_nivel) {
			CategoriaNivel cat_niv = new CategoriaNivel();
			//int status;
			Cursor cursor = null;
			//dbQuery = this.getReadableDatabase();
			String tabela = "status_nivel";
			String where = "id_categoria=? AND id_nivel=?";
			String[] coluna = new String[] { "_id" };
			String argumentos[] = new String[] { String.valueOf(id_categoria),
					String.valueOf(id_nivel) };

			cursor = getDb().query(tabela, coluna, where, argumentos, null, null,
					null);

			cursor.moveToFirst();

			cat_niv.setId_cat_niv(cursor.getInt(cursor.getColumnIndex("_id")));

			return cat_niv.getId_cat_niv();
		}
		

}
