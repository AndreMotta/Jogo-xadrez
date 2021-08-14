package chess;
import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece{

	private Color color;
	private int moveCount;

	public ChessPiece(Board board, Color color) {
		super(board);//deixar o super pq esta chamando o construtor da Piece
		this.color = color;}

	public Color getColor() {
		return color;}// sem set pq a cor não deve ser modificada
	
	public int getMoveCount() {
		return moveCount;}
	
	public void increaseMoveCount() {//encrementa o moveCount
		moveCount++;}
	
	public void decreaseMoveCount() {
		moveCount--;}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);}
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p != null && p.getColor()!= color;}
}
