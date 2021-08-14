package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

public static void main(String[] args) {
	
	Scanner sc= new Scanner(System.in);
	ChessMatch chessMatch= new ChessMatch();
	List<ChessPiece>captured = new ArrayList<>();
	
	
	while(!chessMatch.getCheckMate()){//enquanto não estiver em Xeque-mate
		try {
			UI.clearScreen();
			UI.printMatch(chessMatch,captured);
			System.out.println();
			System.out.print("Origem: ");
			ChessPosition source = UI.readChessPosition(sc);
			
			boolean[][]possibleMoves= chessMatch.possibleMoves(source);
			UI.clearScreen();
			UI.printBoard(chessMatch.getPieces(),possibleMoves);
			
			System.out.println();
			System.out.print("Destino: ");
			ChessPosition target = UI.readChessPosition(sc);
			
			ChessPiece capturedPiece = chessMatch.performChessMove(source,target);
			
			if(capturedPiece != null) {
				captured.add(capturedPiece);}
			
			if(chessMatch.getPromoted() != null) {
				System.out.println("Diga a peça para a promoção (B/C/R/Q): ");
				String type= sc.nextLine();
				chessMatch.replacePromotedPiece(type);}
		}//fim try
		
		 catch(ChessException e ){
			System.out.println(e.getMessage());
			sc.nextLine();}
		
		catch(InputMismatchException e ){
			System.out.println(e.getMessage());
			sc.nextLine();}
		}//fim while
	
	UI.clearScreen();
	UI.printMatch(chessMatch, captured);}//fim do jogo, após o Xeque-mate, codigo para mostrar o tabuleiro final
}
