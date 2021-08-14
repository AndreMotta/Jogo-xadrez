package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {//Regras XADREZ

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;//boolean recebe false como inicial
	private boolean checkMate;//esta em ingês pq só transformei em PT-BR as frases que aparecem no console
	private ChessPiece enPasssatVulnerable;
	private ChessPiece promoted;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces   = new ArrayList<>();

	public ChessMatch() {
		board= new Board(8,8);
		turn = 1;
		currentPlayer = Color.White;
		initialSetup();}
	
	public int getTurn() {
		return turn;}
	
	public Color getCurrentPlayer(){
		return currentPlayer;}
	
	public boolean getCheck() {
		return check;}
	
	public boolean getCheckMate() {
		return checkMate;}
	
	public ChessPiece getEnPasssatVulnerable() {
		return enPasssatVulnerable;}
	
	public ChessPiece getPromoted() {
		return promoted;}
	
	public ChessPiece[][]getPieces(){
		ChessPiece[][] mat=new ChessPiece[board.getRows()][board.getColumns()];
		for(int i = 0; i<board.getRows();i++) {
			for(int j=0;j<board.getColumns();j++){
				mat[i][j]= (ChessPiece) board.piece(i,j);}}
	return mat;}
	
	private Piece makeMove(Position source,Position target) {
		ChessPiece p=(ChessPiece) board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece= board.removePiece(target);
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);}
		
		//Roque pequeno
		if(p instanceof King && target.getColumn()== source.getColumn()+2) {
			Position sourceT= new Position(source.getRow(),source.getColumn()+3); 
			Position targetT= new Position(source.getRow(),source.getColumn()+1); 
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();}
		
		//Roque grande
		if(p instanceof King && target.getColumn()== source.getColumn()-2) {
			Position sourceT= new Position(source.getRow(),source.getColumn()-4); 
			Position targetT= new Position(source.getRow(),source.getColumn()-1); 
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();}
		
		//En passant
		if(p instanceof Pawn) {
			if(source.getColumn()!=target.getColumn() && capturedPiece== null){
				Position pawnposition;
				if(p.getColor() == Color.White) {
					pawnposition= new Position (target.getRow()+1,target.getColumn());}
				else {
					pawnposition= new Position (target.getRow()-1,target.getColumn());}
				
				capturedPiece= board.removePiece(pawnposition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);}
		}//fim if En passant
		
		return capturedPiece;}
	
	private void undoMove(Position source, Position target,Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);}
		
		//Roque pequeno
		if(p instanceof King && target.getColumn()== source.getColumn()+2) {
			Position sourceT= new Position(source.getRow(),source.getColumn()+3); 
			Position targetT= new Position(source.getRow(),source.getColumn()+1); 
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();}
		
		//Roque grande
		if(p instanceof King && target.getColumn()== source.getColumn()-2) {
			Position sourceT= new Position(source.getRow(),source.getColumn()-4); 
			Position targetT= new Position(source.getRow(),source.getColumn()-1); 
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();}
		
		//En passant
		if(p instanceof Pawn) {
			if(source.getColumn()!=target.getColumn() && capturedPiece== enPasssatVulnerable){
				ChessPiece pawn= (ChessPiece)board.removePiece(target);
				Position pawnposition;
				if(p.getColor() == Color.White) {
					pawnposition= new Position (3,target.getColumn());}
				else {
					pawnposition= new Position (4,target.getColumn());}
				
				board.placePiece(pawn, pawnposition);}
		}//fim if En passant
		
		
	}
	
	public boolean[][]possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();}
	
	public  ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {//ATENÇÃO NA HORA DO XEQUEMATE
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source,target);
		Piece capturedPiece = makeMove(source,target);
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Você não pode se colocar em Xeque");}
		
		ChessPiece movedPiece= (ChessPiece)board.piece(target);
		
		//Promotion
		promoted= null;
		if(movedPiece instanceof Pawn) {
			if(movedPiece.getColor() == Color.White && target.getRow()== 0 || movedPiece.getColor() == Color.Black && target.getRow()== 7) {
				promoted= (ChessPiece)board.piece(target);
				promoted= replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer)))? true:false;//se o oponente ficar em check ira dar vdd, se n false
		
		if(testCheckMate(opponent(currentPlayer))) {//ATENÇÃO AQUI, PRECISA SER TRUE!!!!
			checkMate = true;}
		else{
			nextTurn();
		}
		
		//En passant
		if(movedPiece instanceof Pawn && (target.getRow()== source.getRow()-2 || target.getRow()== source.getRow()+2)){
			enPasssatVulnerable= movedPiece;}
		else {
			enPasssatVulnerable= null;
		}
		
		return (ChessPiece)capturedPiece;}//Fim ChessPiece
	
	public ChessPiece replacePromotedPiece(String type) {
		if(promoted == null) {
			throw new IllegalStateException("Não existe peça a ser promovida");}
		if(!type.equals("B") && !type.equals("C") && !type.equals("R") && !type.equals("Q")) {
			throw new InvalidParameterException("Tipo invalido para promover");}
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece p= board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece= newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;}//fim ReplacePromotedPiece
	
	private ChessPiece newPiece(String type, Color color) {
		if(type.equals("B"))return new Bishop(board,color);
		if(type.equals("C"))return new Knight(board,color);
		if(type.equals("R"))return new Rook  (board,color);
		return new Queen (board,color);}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)){
			throw new ChessException("A peça escolhida não pode se mover pra o local de destino");}
	}
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("Não existe peça no local!");}
		if(currentPlayer != ((ChessPiece) board.piece(position)).getColor()){
			throw new ChessException("A peça escolhida não é sua");}
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Não existe movimentos possiveis para a peça escolhida");}
		}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.White) ? Color.Black : Color.White;}
	
	private Color opponent(Color color) {
		return (color == Color.White) ? Color.Black : Color.White;}
	
	private ChessPiece king(Color color) {
		List<Piece>list = piecesOnTheBoard.stream().filter(x ->((ChessPiece)x).getColor()== color).collect(Collectors.toList());
			for(Piece p:list) {
				if(p instanceof King) {
					return(ChessPiece)p;}		
			}
		throw new IllegalStateException("Não existe o rei " + color + "desta cor no tabuleiro");}
	
	private boolean testCheck(Color color) {
		 Position kingPosition = king(color).getChessPosition().toPosition();
		 List<Piece>opponentPieces = piecesOnTheBoard.stream().filter(x ->((ChessPiece)x).getColor()== opponent(color)).collect(Collectors.toList());
		 for (Piece p: opponentPieces) {
			 boolean[][]mat=p.possibleMoves();
			 if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				 return true;}
		}//fim for
		 return false;}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;}
		
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;}
					}
				}
			}
		}return true;
	}	
	
	private void placeNewPiece(char column,int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column,row).toPosition());
		piecesOnTheBoard.add(piece);}
	
	private void initialSetup() {
		    placeNewPiece('a', 1, new Rook(board, Color.White));
	        placeNewPiece('b', 1, new Knight(board, Color.White));
	        placeNewPiece('c', 1, new Bishop(board, Color.White));
	        placeNewPiece('d', 1, new Queen(board, Color.White));
	        placeNewPiece('e', 1, new King(board, Color.White, this));
	        placeNewPiece('f', 1, new Bishop(board, Color.White));
	        placeNewPiece('g', 1, new Knight(board, Color.White));
	        placeNewPiece('h', 1, new Rook(board, Color.White));
	        placeNewPiece('a', 2, new Pawn(board, Color.White, this));
	        placeNewPiece('b', 2, new Pawn(board, Color.White, this));
	        placeNewPiece('c', 2, new Pawn(board, Color.White, this));
	        placeNewPiece('d', 2, new Pawn(board, Color.White, this));
	        placeNewPiece('e', 2, new Pawn(board, Color.White, this));
	        placeNewPiece('f', 2, new Pawn(board, Color.White, this));
	        placeNewPiece('g', 2, new Pawn(board, Color.White, this));
	        placeNewPiece('h', 2, new Pawn(board, Color.White, this));

	        placeNewPiece('a', 8, new Rook(board, Color.Black));
	        placeNewPiece('b', 8, new Knight(board, Color.Black));
	        placeNewPiece('c', 8, new Bishop(board, Color.Black));
	        placeNewPiece('d', 8, new Queen(board, Color.Black));
	        placeNewPiece('e', 8, new King(board, Color.Black, this));
	        placeNewPiece('f', 8, new Bishop(board, Color.Black));
	        placeNewPiece('g', 8, new Knight(board, Color.Black));
	        placeNewPiece('h', 8, new Rook(board, Color.Black));
	        placeNewPiece('a', 7, new Pawn(board, Color.Black, this));
	        placeNewPiece('b', 7, new Pawn(board, Color.Black, this));
	        placeNewPiece('c', 7, new Pawn(board, Color.Black, this));
	        placeNewPiece('d', 7, new Pawn(board, Color.Black, this));
	        placeNewPiece('e', 7, new Pawn(board, Color.Black, this));
	        placeNewPiece('f', 7, new Pawn(board, Color.Black, this));
	        placeNewPiece('g', 7, new Pawn(board, Color.Black, this));
	        placeNewPiece('h', 7, new Pawn(board, Color.Black, this));
	}

	
}
