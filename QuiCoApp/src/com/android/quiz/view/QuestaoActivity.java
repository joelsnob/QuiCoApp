package com.android.quiz.view;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.quiz.R;
import com.android.quiz.R.id;
import com.android.quiz.R.layout;
import com.android.quiz.dao.CategoriaNivelDao;
import com.android.quiz.dao.QuestaoDao;
import com.android.quiz.modelo.Questao;

public class QuestaoActivity extends Activity implements OnClickListener {

	List<Questao> qLista;
	
	int id_cat_niv = 0;
	int nivel_atual = 0;
	Questao qAtual;
	QuestaoDao qt_dao;
	CategoriaNivelDao cat_niv_dao;
	int qid = 0;
	int contador = 1;
	TextView txQuestao, txtContador;
	Button btnOpcao1, btnOpcao2, btnOpcao3, btnOpcao4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.questao);

		// recebe os par�metros da Activity N�vel
		Intent categoria = getIntent();
		Bundle params = categoria.getExtras();
		if (params != null) {
			id_cat_niv = params.getInt("categoria_nivel");
			nivel_atual = params.getInt("nivel");
		}

		// instancia a classe QuestaoDao e CategoriaNivelDao
		qt_dao = new QuestaoDao();
		cat_niv_dao = new CategoriaNivelDao(getApplicationContext());

		// consulta as questoes no banco de dados
		consultaQuestoesBD();

		// seta o primeiro registro no objeto da classe questao
		qAtual = qLista.get(qid);

		setQuestions();

		// inicializa os botoes
		Button btnOpcao1 = (Button) findViewById(R.id.btnOpcao1);
		Button btnOpcao2 = (Button) findViewById(R.id.btnOpcao2);
		Button btnOpcao3 = (Button) findViewById(R.id.btnOpcao3);
		Button btnOpcao4 = (Button) findViewById(R.id.btnOpcao4);

		// m�todo para evento click do botao
		btnOpcao1.setOnClickListener(this);
		btnOpcao2.setOnClickListener(this);
		btnOpcao3.setOnClickListener(this);
		btnOpcao4.setOnClickListener(this);

		if (isXLargeScreen(getApplicationContext()))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

	public static boolean isXLargeScreen(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	private void setQuestions() {

		// seta a questao no textView
		TextView txQuestao = (TextView) findViewById(R.id.txtQuestao);
		txQuestao.setText(qAtual.getQuestao());

		// seta as op��es nos botoes
		TextView btnOpcao1 = (TextView) findViewById(R.id.btnOpcao1);
		btnOpcao1.setText(qAtual.getOpcao1());

		TextView btnOpcao2 = (TextView) findViewById(R.id.btnOpcao2);
		btnOpcao2.setText(qAtual.getOpcao2());

		TextView btnOpcao3 = (TextView) findViewById(R.id.btnOpcao3);
		btnOpcao3.setText(qAtual.getOpcao3());

		TextView btnOpcao4 = (TextView) findViewById(R.id.btnOpcao4);
		btnOpcao4.setText(qAtual.getOpcao4());

		txtContador = (TextView) findViewById(R.id.txtContador);
		txtContador.setText(String.valueOf(contador) + "/5");
		contador++;

		// incrementa o �ndice da lista
		qid++;
	}

	// m�todo para saber qual bot�o foi clicado
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOpcao1:
			// pega o texto do bot�o
			Button b1 = (Button) v;
			String respostab1 = b1.getText().toString();
			// verifica se a resposta clicada � igual a cadastrada no banco
			verificaResposta(respostab1);

			break;
		case R.id.btnOpcao2:
			Button b2 = (Button) v;
			String respostab2 = b2.getText().toString();
			verificaResposta(respostab2);
			break;
		case R.id.btnOpcao3:
			Button b3 = (Button) v;
			String respostab3 = b3.getText().toString();
			verificaResposta(respostab3);
			break;
		case R.id.btnOpcao4:
			Button b4 = (Button) v;
			String respostab4 = b4.getText().toString();
			verificaResposta(respostab4);
			break;

		}
	}

	/*
	 * m�todo para verificar resposta certa e passar para a pr�xima e mostrar
	 * msg se perder ou responder todas as perguntas corretamente
	 */
	public void verificaResposta(String resposta) {
		if (qAtual.getResposta().equals(resposta)) {
			if (qid < 5) {
				qAtual = qLista.get(qid);
				setQuestions();

			} else {
				contador = 1;
				// incrementa o nivel atual
				nivel_atual = nivel_atual + 1;

				// verifica se o nivel ainda � valido para atualizar no banco, o
				// status do n�vel
				if (nivel_atual < 6) {

					// incrementa o id do proximo n�vel, para desbloquear o
					// botao
					id_cat_niv = id_cat_niv + 1;
					// atualiza o status do nivel para desbloqueado
					cat_niv_dao.atualizaStatusNivel(id_cat_niv, 1);

				}
				mostrarMsgGanhou();

			}
		} else {
			contador = 1;
			mostrarMsgPerdeu();
		}

	}

	// m�todo para mostrar a mensagem se ganhar
	private void mostrarMsgGanhou() {

		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.resposta_certa);

		dialog.setTitle("Ganhou");
		dialog.setCancelable(false);

		final Button btnProxNivel = (Button) dialog
				.findViewById(R.id.btnProximoNivel);
		final Button btnCategoria = (Button) dialog
				.findViewById(R.id.btnTelaCategoria);
		final Button btnSair = (Button) dialog.findViewById(R.id.btnSair);

		// clicar no bot�o categoria
		btnCategoria.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent cat = new Intent(QuestaoActivity.this,
						CategoriaActivity.class);
				startActivity(cat);
				finish();
				dialog.dismiss();

			}
		});

		// clicar no bot�o pr�ximo n�vel
		btnProxNivel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// verifica se o n�vel ainda � v�lido para passar para o pr�ximo
				if (nivel_atual < 6) {
					qid = 0;
					consultaQuestoesBD();
					qAtual = qLista.get(qid);
					setQuestions();
					dialog.dismiss();

				} else {
					// sen�o chama a tela de categoria
					Intent proxCategoria = new Intent(QuestaoActivity.this,
							CategoriaActivity.class);
					startActivity(proxCategoria);
					finish();
					dialog.dismiss();
				}

			}
		});

		// clicar no bot�o sair
		btnSair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent inicio = new Intent(QuestaoActivity.this,
						IniciarActivity.class);
				startActivity(inicio);
				finish();
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	// m�todo para mostrar mensagem se perder
	private void mostrarMsgPerdeu() {

		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.resposta_errada);
		dialog.setTitle("Perdeu");
		dialog.setCancelable(false);

		final Button btnSair = (Button) dialog.findViewById(R.id.btnSair);
		final Button btnCategoria = (Button) dialog
				.findViewById(R.id.btnTelaCategoria2);
		final Button btnNovoJogo = (Button) dialog
				.findViewById(R.id.btnJogarNovamente);

		// clicar no bot�o sair
		btnSair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent inicio = new Intent(QuestaoActivity.this,
						IniciarActivity.class);
				startActivity(inicio);
				finish();
				dialog.dismiss();

			}
		});

		// clicar no bot�o categoria
		btnCategoria.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent cat = new Intent(QuestaoActivity.this,
						CategoriaActivity.class);
				startActivity(cat);
				finish();
				dialog.dismiss();

			}
		});

		// clicar no bot�o jogar novamente
		btnNovoJogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qid = 0;
				consultaQuestoesBD();
				qAtual = qLista.get(qid);
				setQuestions();
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	// consulta quest�es no banco de dados
	public void consultaQuestoesBD() {
		try {
			qLista = qt_dao.getQuestionSet(id_cat_niv, 5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// fecha conexao com banco de dados
	/*
	 * @Override protected void onDestroy(){ qt_dao.close();
	 * cat_niv_dao.close(); super.onDestroy(); }
	 */

	@Override
	public void onBackPressed() {
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.dialog_questao);
		dialog.setTitle("Sair");
		dialog.setCancelable(false);

		final Button btnContinuar = (Button) dialog
				.findViewById(R.id.btnContinuar);
		final Button btnTerminar = (Button) dialog
				.findViewById(R.id.btnTerminar);

		btnContinuar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				dialog.dismiss();

			}
		});

		btnTerminar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(QuestaoActivity.this,
						CategoriaActivity.class);
				startActivity(i);
				finish();
				dialog.dismiss();
			}
		});

		dialog.show();

	}

}
