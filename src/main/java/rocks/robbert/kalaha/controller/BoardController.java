package rocks.robbert.kalaha.controller;

import org.springframework.web.bind.annotation.*;
import rocks.robbert.kalaha.GameManager;
import rocks.robbert.kalaha.model.Board;
import rocks.robbert.kalaha.model.Player;

import java.util.Collections;
import java.util.Map;

@RestController
@SessionAttributes("game")
public class BoardController {
    private GameManager game;

    /**
     * Calls ones per game to initialize the board.
     *
     * @return Board
     */
    @RequestMapping(value = "init", method = RequestMethod.GET)
    public Board board() {
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        game = new GameManager(p1, p2);
        return game.getBoard();
    }

    @RequestMapping(value = "current", method = RequestMethod.GET)
    public Board current() {
        return game.getBoard();
    }

    @RequestMapping(value = "move/{pos}", method = RequestMethod.POST)
    public Map<String, String> move(
            @PathVariable(value = "pos") int pos
    ) {
        return Collections.singletonMap("result", game.move(pos));
    }
}
