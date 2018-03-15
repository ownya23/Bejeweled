/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bejeweleed;

/**
 *
 * @author Bruno Pereira, João Letra e Raquel Baptista
 */
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller implements Initializable { //class que inicializa o controlador

    Tabuleiro meuTabuleiro = new Tabuleiro();
    TabuleiroDificil meuTabuleiroDificil = new TabuleiroDificil();
    Ficheiro meuFicheiro = new Ficheiro();

    int px1, py1, px2, py2;

    int selOneOrTwo = 1;

    private final Button[][] btn = new Button[8][8];
    int modo = 0;
    boolean key = false;

    @FXML
    private Label lable1;

    @FXML
    private TextField idJogador;
    @FXML
    private ImageView imageView;
    @FXML
    private VBox vBox2;
    @FXML
    private ListView listView1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        jogo();

    }

    private void jogo() { //esqueleto do jogo
        txtPontos.setText("Introduza o seu nome:");
        btnFacil.setVisible(false);
        btnDificil.setVisible(false);
        lable4.setVisible(false);

        lable3.setVisible(false);
        try {

            File file = new File("ScoreBoard.txt"); // cria o ficheiro scoreboard.txt
            FileWriter fr1 = new FileWriter(file, true);

            fr1.close();

        } catch (Exception e) {
            System.out.println();
            System.out.println(" Ocorreu um erro durante a escrita no ficheiro! [DEBUG: " + e.getMessage() + "]");
            System.out.println();
        }

        if (modo == 0) { //modo de iniciar novo jogo

            for (int i = 0; i < 8; i++) { //loop que cria os botões na grid pane
                for (int j = 0; j < 8; j++) {

                    btn[i][j] = new Button();//cria os botões
                    btn[i][j].setPadding(Insets.EMPTY);
                    btn[i][j].setMinSize(50, 50);
                    btn[i][j].setMaxSize(50, 50);
                    btn[i][j].setVisible(false); //o botão fica invisivel 

                    tableJogo.add(btn[i][j], j, i);//adiciona o botão à grid pane

                }

            }

        }

        switch (modo) {// escolhe o modo facil ou dificil
            case 1:
                meuTabuleiro.jogoFacil();
                lable4.setVisible(true);

                for (int i = 0; i < 8; i++) { // introduz as imagens
                    for (int j = 0; j < 8; j++) {

                        int X = meuTabuleiro.getTabuleiro(i, j);
                        String imagem = String.valueOf(X);
                        btn[i][j].setVisible(true);
                        btn[i][j].setStyle("-fx-background-image: url('/testing/gem" + (imagem) + ".png')");
                        btn[i][j].setOnAction(this::handleBtn);
                    }
                }
                lable2.setVisible(true);
                lable2.setText("Jogadas Possiveis: " + String.valueOf(meuTabuleiro.getJogadas())); // indica as jogadas possiveis
                txtPontos.setText(String.valueOf(meuTabuleiro.getPontos())); //indica a pontuação
                break;

            case 2:
                meuTabuleiroDificil.jogoDificil();
                lable4.setVisible(true);

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {

                        int X = meuTabuleiroDificil.getTabuleiroDificil(i, j);
                        String imagem = String.valueOf(X);
                        btn[i][j].setVisible(true);
                        btn[i][j].setStyle("-fx-background-image: url('/testing/gem" + (imagem) + ".png')");
                        btn[i][j].setOnAction(this::handleBtn);//acção de jogo

                    }

                }
                lable2.setVisible(true);
                lable2.setText("Jogadas Possiveis: " + String.valueOf(meuTabuleiroDificil.getJogadas()));
                txtPontos.setText(String.valueOf(meuTabuleiroDificil.getPontos()));
                break;

        }

        if (meuTabuleiro.getEncerraJogo() == 1) {

            System.out.println("correu");
            for (int i = 0; i < 8; i++) { //coloca os botoes invisiveis
                for (int j = 0; j < 8; j++) {

                    btn[i][j].setVisible(false);
                }

            }

            String nome = idJogador.getText();
            int pontuacao = 0;
            switch (modo) {
                case 1:
                    pontuacao = meuTabuleiro.getPontos();
                    break;
                case 2:
                    pontuacao = meuTabuleiroDificil.getPontos();
                    break;
            }
            
            meuFicheiro.escrever(nome, pontuacao); // adiciona os dados ao ficheiro

            String novaMatriz[][] = meuFicheiro.carregaFicheiro("ScoreBoard.txt"); //cria uma matriz com os dados do ficheiro
            String matrizScores[][] = new String[10][2];
            ArrayList<String> listaScores = new ArrayList<>();

            String nome2 = novaMatriz[0][0];
            String score2 = novaMatriz[0][1];

            for (int i = 1; i < novaMatriz.length - 1; i++) {
                if (Integer.valueOf(novaMatriz[i][1]) > Integer.valueOf(score2)) {

                    nome2 = novaMatriz[i][0];
                    score2 = novaMatriz[i][1];
                }
            }

            for (int i = 0; i < novaMatriz.length - 1; i++) {//adiciona os dados da matriz à lista que vai ser lida
                listaScores.add(novaMatriz[i][0] + "\t\t" + novaMatriz[i][1]);
            }

            // List<String> myList = Arrays.asList(listaScores);
            lable2.setText("Fim do Jogo!");
            lable4.setText(idJogador.getText() + " fez " + String.valueOf(pontuacao) + " Pontos!");
            txtPontos.setText("High Scores:");
            imageView.setVisible(false);
            listView1.setVisible(true);
            listView1.setItems(FXCollections.observableArrayList(listaScores));

            System.out.println("ola");

        }
    }

    @FXML
    private Label lable2;

    @FXML
    private void onExit(ActionEvent event) {
        System.exit(0); // encerra o Jogo
    }

    void handleBtn(ActionEvent event) {// função que movimenta os botões

        int selJogada = 0;
        lable3.setVisible(false);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (event.getSource() == btn[i][j]) {

                    if (selOneOrTwo == 1) {//seleciona o primeiro botão
                        System.out.print("First button clicked: ");
                        px1 = i;
                        py1 = j;
                        System.out.println("px1=" + px1 + " py1=" + py1);
                        selOneOrTwo = 2;
                    } else if (selOneOrTwo == 2) {//seleciona o segundo botão

                        System.out.print("Second button clicked: ");
                        px2 = i;
                        py2 = j;
                        System.out.println("px2=" + px2 + " py2=" + py2);

                        if (i > 0 && i < 7 && j > 0 && j < 7) {//quadrado do meio

                            if (px2 == (px1 + 1) && py2 == py1 || px2 == (px1 - 1) && py2 == py1 || py2 == (py1 + 1) && px2 == px1 || py2 == (py1 - 1) && px2 == px1) {

                                selJogada = 1;
                            } else {
                                lable3.setText("Jogada inválida!");
                                lable3.setVisible(true);
                            }
                        }

                        // linha da cima
                        // Queremos tratar da movimentações da linha i = 0, onde o i é constante e o j variavel, mas não incluimos os vertices
                        if (j > 0 && j < 7 && i == 0) {
                            // Quando selecionamos 1 peça e depois uma a sua direita vamos ter que px2 == px1 pois i mantem valor de 0, 
                            // contudo py2 é igual ao local original (1a peça escolhida py1) mais uma casa. Tambem inclui movimentação de troca com a peça à esquerda e a baixo.
                            if (py2 == (py1 + 1) && px2 == px1 || py2 == (py1 - 1) && px2 == px1 || px2 == (px1 - 1) && py2 == py1) {
                                selJogada = 1;
                            }
                        }
                        // linha da esquerda
                        // Queremos tratar da movimentações da linha j = 0, onde o j é constante e o i variavel, mas não incluimos os vertices
                        if (i > 0 && i < 7 && j == 0) {
                            // Quando selecionamos 1 peça e depois uma abaixo vamos ter que py2 == py1 pois j mantem valor de 0, 
                            // contudo px2 é igual ao local original (1a peça escolhida px1) mais uma casa.Tambem inclui movimentação de troca com a peça à direita e a cima.
                            if (px2 == (px1 + 1) && py2 == py1 || px2 == (px1 - 1) && py2 == py1 || py2 == (py1 - 1) && px2 == px1) {
                                selJogada = 1;
                            }
                        }
                        // linha da direita
                        /////
                        if (j == 7 && i > 0 && i < 7) {
                            if (px2 == (px1 + 1) && py2 == py1 || px2 == (px1 - 1) && py2 == py1 || py2 == (py1 + 1) && px2 == px1) {
                                selJogada = 1;
                            }
                        }
                        // linha de baixo
                        /////
                        if (i == 7 && j > 0 && j < 7) {
                            if (py2 == (py1 + 1) && px2 == px1 || py2 == (py1 - 1) && px2 == px1 || px2 == (px1 + 1) && py2 == py1) {
                                selJogada = 1;
                            }
                        }
                        // canto superior esquerdo
                        if (i == 0 && j == 0) {
                            if (px2 == (px1 - 1) && py2 == py1 || py2 == (py1 - 1) && px2 == px1) {
                                selJogada = 1;
                            }
                        }
                        // canto superior direito
                        if (i == 0 && j == 7) {
                            if (px2 == (px1 - 1) && py2 == py1 || py2 == (py1 + 1) && px2 == px1) {
                                selJogada = 1;
                            }
                        }
                        // canto inferior esquerdo
                        if (i == 7 && j == 0) {
                            if (px2 == (px1 + 1) && py2 == py1 || py2 == (py1 - 1) && px2 == px1) {
                                selJogada = 1;
                            }
                        }
                        // canto inferior direito
                        if (i == 7 && j == 7) {
                            if (px2 == (px1 + 1) && py2 == py1 || py2 == (py1 + 1) && px2 == px1) {
                                selJogada = 1;
                            }
                        }

                        ////////////////////////////////////
                        selOneOrTwo = 1;
                    }

                }
            }
        }
        //switch (modo ){
        //case 1:
        if (selJogada == 1) {//seleciona o local onde vai ser feita a jogada
            switch (modo) {
                case 1:
                    meuTabuleiro.jogada(px1, py1, px2, py2);
                case 2:
                    meuTabuleiroDificil.jogada(px1, py1, px2, py2);
            }
            jogo();
        }
        if (selOneOrTwo == 1 && selJogada == 0) {

        }
        //break;
        //}
    }

    @FXML
    void handleBtnDificil(ActionEvent event) {

        modo = 2;
        btnDificil.setVisible(false);
        btnFacil.setVisible(false);
        lable2.setVisible(false);
        lable3.setVisible(false);
        lable4.setVisible(true);
        lable4.setText("Pontos:");
        jogo();

    }

    @FXML
    void handleBtnFacil(ActionEvent event) {

        modo = 1;
        btnDificil.setVisible(false);
        btnFacil.setVisible(false);
        lable2.setVisible(false);
        lable3.setVisible(false);
        lable4.setVisible(true);
        lable4.setText("Pontos:");

        jogo();

    }

    @FXML
    void handleMove(ActionEvent event) {
        meuTabuleiro.setEncerraJogo(1);

    }

    @FXML
    void newGame(ActionEvent event) {
        try {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {

                    btn[i][j].setVisible(false);
                }
            }
            meuTabuleiro = new Tabuleiro();
            meuTabuleiroDificil = new TabuleiroDificil();

            txtPontos.setText("Introduza o seu nome:");
            idJogador.setVisible(true);
            lable4.setVisible(false);
            lable2.setVisible(false);
            imageView.setVisible(true);
            listView1.setVisible(false);

        } catch (Exception e) {
            System.out.println("erro");
        }
    }
    @FXML
    private Label lable4;

    @FXML
    private HBox hbox2;

    @FXML
    private GridPane tableJogo;

    @FXML
    private Button btnFacil;

    @FXML
    private Button btnDificil;

    @FXML
    private Label txtPontos;

    @FXML
    private Label lable3;

    @FXML
    private void salvaFicheiro(ActionEvent event) {
        /*String nomeFicheiro=idJogador.getText();
        System.out.println("GRAVA!! "+nomeFicheiro);*/
        idJogador.setVisible(false);
        txtPontos.setText("");
        btnFacil.setVisible(true);
        btnDificil.setVisible(true);

        lable3.setVisible(true);

    }

}
