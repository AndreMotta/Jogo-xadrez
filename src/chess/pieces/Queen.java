package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece{

	public Queen(Board board, Color color) {
		super(board, color);}

	@Override
	public String toString() {
		return "Q";}
	
	public boolean[][] possibleMoves() {
		boolean[][]mat= new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);
		
		//acima da peça
		p.setValues(position.getRow()-1, position.getColumn());
		while(getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;
			p.setRow(p.getRow()-1);}
		if(getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;}
		
		//esquerda
		p.setValues(position.getRow(), position.getColumn()-1);
		while(getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;
			p.setColumn(p.getColumn()-1);}
		if(getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;}
		
		//direita
		p.setValues(position.getRow(), position.getColumn()+1);
		while(getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;
			p.setColumn(p.getColumn()+1);}
		if(getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;}
		
		//baixo
		p.setValues(position.getRow()+1, position.getColumn());
		while(getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;
			p.setRow(p.getRow()+1);}
		if(getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;}
		
		//noroeste(nw)
		p.setValues(position.getRow()-1, position.getColumn()-1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;
			p.setValues(p.getRow()-1, p.getColumn()-1);}
		
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;}
		
		//nordeste (ne)
		p.setValues(position.getRow()-1, position.getColumn()+1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;
			p.setValues(p.getRow()-1, p.getColumn()+1);}
		
		if(getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;}
		
		//sudeste (se)
		p.setValues(position.getRow()+1, position.getColumn()+1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;
			p.setValues(p.getRow()+1, p.getColumn()+1);}
		
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;}
		
		//sudoeste(
		p.setValues(position.getRow()+1, position.getColumn()-1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;
			p.setValues(p.getRow()+1, p.getColumn()-1);}
		
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()]=true;}
		
		return mat;}
	
}
