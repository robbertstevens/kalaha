package rocks.robbert.kalaha.controller;

import org.springframework.web.bind.annotation.*;
import rocks.robbert.kalaha.model.Board;
import rocks.robbert.kalaha.model.Player;

@RestController
@SessionAttributes("board")
public class BoardController {
    private Board board;

    /**
     * Calls ones per game to initialize the board.
     *
     * @return Board
     */
    @RequestMapping(value = "board", method = RequestMethod.GET)
    public Board board() {
        setBoard(new Board(new Player("player-one"), new Player("player-two")));
        return getBoard();
    }

    @RequestMapping(value = "move/{pos}", method = RequestMethod.POST)
    public Board move(
            @PathVariable(value = "pos") int pos
    ) {
        Board board = getBoard();
        board.move(pos);
        return board;
    }

    private Board getBoard() {
        return board;
    }

    private void setBoard(Board board) {
        this.board = board;
    }
}
