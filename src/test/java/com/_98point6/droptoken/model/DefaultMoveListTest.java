package com._98point6.droptoken.model;

import com._98point6.droptoken.model.constants.MoveType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultMoveListTest {
    
    @Test
    public void adding_moves_in_move_list() {
        DefaultMoveList list = new DefaultMoveList();
        
        list.addDropToken("player1", 1);
        list.addQuit("player2");

        List<Move> moves = list.getMoves();

        assertEquals(2, moves.size());
        
        assertEquals(MoveType.MOVE, moves.get(0).getType());
        assertEquals("player1", moves.get(0).getPlayer());
        assertEquals((Integer)1, moves.get(0).getColumn());

        assertEquals(MoveType.QUIT, moves.get(1).getType());
        assertEquals("player2", moves.get(1).getPlayer());
        assertNull(moves.get(1).getColumn());
    }
}
