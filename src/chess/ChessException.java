package chess;

import boardgame.BoardException;

public class ChessException extends BoardException{//É uma extensão de RunTime afetando com que seja mais "completa"
private static final long serialVersionUID = 1L;

public ChessException(String msg) {
	super(msg);}

}
