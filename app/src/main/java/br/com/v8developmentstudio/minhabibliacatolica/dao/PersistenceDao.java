package br.com.v8developmentstudio.minhabibliacatolica.dao;

/**
 * Created by cleiton.dantas on 23/10/2015.
 */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import br.com.v8developmentstudio.minhabibliacatolica.vo.Livro;
import br.com.v8developmentstudio.minhabibliacatolica.vo.RelacLivroCap;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Versiculo;


public class PersistenceDao extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "DB_MINHABIBLIACATOLICAV8";
    private static final String COLUMN_ID = "ID";

    private static final String TABLE_LIVROS = "LIVROS";
    private static final String COLUMN_TITULO_LIVRO = "TITULOLIVRO";
    private static final String COLUMN_ABREVIACAO = "ABREVIACAO";
    private static final String COLUMN_QTDCAPITULOS = "QTDCAPITULOS";

    private static final String TABLE_RELAC_LIVRO_CAP = "TB_RELAC_LIVRO_CAP";
    private static final String COLUMN_IDLIVRO = "IDLIVRO";
    private static final String COLUMN_IDNUMCAP = "IDNUMCAP";
    private static final String COLUMN_NOME_TABELA = "NOME_TABELA";

    private static final String COLUMN_VERSICULO = "VER";

    private static final String TABLE_FAVORITOS = "FAVORITOS";
    private static final String COLUMN_IDORACAO = "IDORACAO";


    private Cursor cursor;
    private static PersistenceDao instance;
    private static final int VERSAO =1;
    public static SQLiteDatabase bancoDados = null;
    private static Context contextStatic;
    public PersistenceDao(Context context) {
        super(context, DATABASE_NAME, null, VERSAO);
        contextStatic =context;
    }
    public static PersistenceDao getInstance(Context context) {
        if(instance == null)
            instance = new PersistenceDao(context);
        return instance;
    }
    /**
     * Verifica a existencia do banco de dados
     *
     * @param bancoDados
     * @return
     */
    public boolean verificaBancoExistente(SQLiteDatabase bancoDados){
        cursor = bancoDados.query(TABLE_LIVROS, new String[]{COLUMN_ID}, null,null,null,null,null);
        if(cursor.getCount()>2){
            return true;
        }
        return false;
    }

    /**
     */
    public RelacLivroCap getRelacLivroCap(SQLiteDatabase bancoDados,int idLivro,int idNumCap){
        String query =  COLUMN_IDLIVRO+" = ? AND "+COLUMN_IDNUMCAP +" = ?";
        String[] args = {""+idLivro,""+idNumCap};
        cursor = bancoDados.query(TABLE_RELAC_LIVRO_CAP, new String[]{COLUMN_ID,COLUMN_IDLIVRO,COLUMN_IDNUMCAP,COLUMN_NOME_TABELA},query,args,null,null,null);
        RelacLivroCap relacLivroCap =null;
        while(cursor.moveToNext()){
            relacLivroCap = new RelacLivroCap();
            relacLivroCap.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
            relacLivroCap.setIdLivro(idLivro);
            relacLivroCap.setIdNumCap(idNumCap);
            relacLivroCap.setNome_tabela(cursor.getString(cursor.getColumnIndex(COLUMN_NOME_TABELA)));
            break;
        }
        bancoDados.close();
        return relacLivroCap;
    }


    /**
     * Metodo Busca os titulos e Lista para ler na view
     */
    public List<Livro> getLivros(SQLiteDatabase bancoDados){
        ArrayList<Livro> livroArrayList = new ArrayList<>();
        cursor = bancoDados.query(TABLE_LIVROS, new String[]{COLUMN_ID,COLUMN_TITULO_LIVRO,COLUMN_QTDCAPITULOS,COLUMN_ABREVIACAO}, null,null,null,null,null);
        Livro livro;
        while(cursor.moveToNext()){
            livro = new Livro();
            livro.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
            livro.setTituloLivro(cursor.getString(cursor.getColumnIndex(COLUMN_TITULO_LIVRO)));
            livro.setAbreviacao(cursor.getString(cursor.getColumnIndex(COLUMN_ABREVIACAO)));
            livro.setQtdCapitulos(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QTDCAPITULOS))));
            livroArrayList.add(livro);
        }
        bancoDados.close();
        return livroArrayList;
    }
//
    /**
     * Metodo Busca os titulos e Lista para ler na view
     */
    public List<Versiculo> getCapitulos(SQLiteDatabase bancoDados,String TABLE_CAPITULO){
        ArrayList<Versiculo> versiculoArrayList = new ArrayList<>();
        cursor = bancoDados.query(TABLE_CAPITULO, new String[]{COLUMN_ID,COLUMN_VERSICULO}, null,null,null,null,null);
        Versiculo versiculo =null;
        while(cursor.moveToNext()){
            versiculo = new Versiculo();
            versiculo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
            versiculo.setTexto(cursor.getString(cursor.getColumnIndex(COLUMN_VERSICULO)));
            versiculoArrayList.add(versiculo);
        }
        bancoDados.close();
        return versiculoArrayList;
    }


    /**
     * Método que persiste o numero da oracao que deseja que seja favoritado
     * @param bancoDados
     * @param numero
     */
    public void salvarFavorito(SQLiteDatabase bancoDados,int numero){
        String query = "INSERT or replace INTO "+TABLE_FAVORITOS+" ("+COLUMN_IDORACAO+") VALUES ("+numero+");";
        bancoDados.execSQL(query);
    }
    /**
     * Busca o favorito por id de oracao retornando se existe eu nao
     * @param numero
     * @return
     */
    public boolean buscaFavoritoPorIdOracao(SQLiteDatabase bancoDados,int numero){
        cursor = bancoDados.query(TABLE_FAVORITOS, new String[]{COLUMN_IDORACAO},"IDORACAO = "+numero  ,null,null,null,null);
        return cursor.getCount()>0;
    }

    /**
     * Busca o favorito por id de oracao retornando se existe eu nao
     * @return
     */
    public List<Integer> buscaTodosFavoritos(SQLiteDatabase bancoDados){
        List<Integer> registrosFavoritos = new ArrayList<Integer>();
        cursor = bancoDados.query(TABLE_FAVORITOS, new String[]{COLUMN_IDORACAO},null,null,null,null,null);
        while(cursor.moveToNext()){
            registrosFavoritos.add(cursor.getInt(cursor.getColumnIndex(COLUMN_IDORACAO)));
        }
        return registrosFavoritos;
    }



    /**
     * Metodo Cria o conteudo do banco de dados apartir das linas do arquivo de SQL com InputStrem

     */
    public void criaConteudo(InputStream input,final SQLiteDatabase openDB) {
        try {
            byFile(input,openDB);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void byFile(int fileID, SQLiteDatabase bd) throws IOException {
        StringBuilder sql = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(contextStatic.getResources().openRawResource(fileID)));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0) {
                sql.append(line);
                if (line.endsWith(";")) {
                    bd.execSQL(sql.toString());
                    sql.delete(0, sql.length());
                }
            }
        }
    }
    protected void byFile(InputStream input, SQLiteDatabase bd) throws IOException {
        StringBuilder sql = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0) {
                sql.append(line);
                if (line.endsWith(";")) {
                    bd.execSQL(sql.toString());
                    sql.delete(0, sql.length());
                }
            }
        }
    }

    /**
     * Cria o banco de dados se não existe
     * @param bancoDados
     */
    @Override
    public void onCreate(SQLiteDatabase bancoDados) {

        bancoDados.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase bancoDados, int oldVersion, int newVersion) {

        onCreate(bancoDados);
    }

    public SQLiteDatabase openDB(){
        try{
            bancoDados = contextStatic.openOrCreateDatabase(PersistenceDao.DATABASE_NAME, Context.MODE_WORLD_READABLE, null);
        }catch (Exception e){
        }
        return bancoDados;
    }

    public SQLiteDatabase openDB(Context context){
        try{
            bancoDados = context.openOrCreateDatabase(PersistenceDao.DATABASE_NAME, Context.MODE_WORLD_READABLE, null);
        }catch (Exception e){
        }
        return bancoDados;
    }



    public void copiaBanco(String dataBaseName) {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            // Abre o arquivo que deve estar na pasta assets
            is = contextStatic.getAssets().open(dataBaseName);

            // Abre o arquivo do banco vazio ele fica em:
            // /data/data/nome.do.pacote.da.app/databases
            fos = new FileOutputStream(contextStatic.getDatabasePath(dataBaseName));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getExiteBase(String dataBaseName){
        return contextStatic.getDatabasePath(dataBaseName).exists();
    }
    public boolean getDeleteBase(String dataBaseName){
        return contextStatic.getDatabasePath(dataBaseName).delete();
    }

    // Método para criacao de tabelas
    public void criatabelas(SQLiteDatabase bancoDados){

        cursor = bancoDados.query(TABLE_RELAC_LIVRO_CAP, null,null,null,null,null,null);
        List<RelacLivroCap> relacLivroCapList = new ArrayList<>();
        RelacLivroCap relacLivroCap =null;
        while(cursor.moveToNext()){
            relacLivroCap = new RelacLivroCap();
            relacLivroCap.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
            relacLivroCap.setNome_tabela(cursor.getString(cursor.getColumnIndex(COLUMN_NOME_TABELA)));
            relacLivroCapList.add(relacLivroCap);
        }
        for (RelacLivroCap cp:relacLivroCapList){
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS "+cp.getNome_tabela() + " ( ID  INTEGER PRIMARY KEY,VER TEXT );");
        }

        bancoDados.close();

    }

    public void salvarEstadoPreferences(int idLivro,int idCapitulo){
        SharedPreferences settings = contextStatic.getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("idlivro", idLivro);
        editor.putInt("idcapitulo", idCapitulo);
        editor.commit();
    }

    public int getEstadoLivroPreferences(){
        SharedPreferences settings = contextStatic.getSharedPreferences("Preferences", 0);
        return settings.getInt("idlivro", 1);
    }
    public int getEstadoCapituloPreferences(){
        SharedPreferences settings = contextStatic.getSharedPreferences("Preferences", 0);
        return settings.getInt("idcapitulo", 1);
    }

}