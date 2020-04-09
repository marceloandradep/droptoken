package com._98point6.droptoken.model.board;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ColumnTest {
    
    private Column column0;
    private Column column1;
    private Column column2;
    private Column column3;
    
    @Before
    public void setup() {
        Row[] rows = 
                Stream
                        .generate(() -> new Row(4))
                        .limit(4)
                        .toArray(Row[]::new);
        
        Row diagonal1 = new Row(4);
        Row diagonal2 = new Row(4);
        
        column0 = new Column(0, rows, diagonal1, diagonal2);
        column1 = new Column(1, rows, diagonal1, diagonal2);
        column2 = new Column(2, rows, diagonal1, diagonal2);
        column3 = new Column(3, rows, diagonal1, diagonal2);
    }
    
    @Test
    public void when_column_is_filled_should_return_is_full() {
        column0.drop((byte)1);
        column0.drop((byte)1);
        column0.drop((byte)1);
        column0.drop((byte)1);

        assertTrue(column0.isFull());
    }

    @Test
    public void when_column_is_partially_filled_should_return_not_is_full() {
        column0.drop((byte)1);
        column0.drop((byte)1);

        assertFalse(column0.isFull());
    }
    
    @Test
    public void should_check_if_top_is_at_one_of_the_diagonals() {
        column0.drop((byte)1);
        
        column1.drop((byte)1);
        column1.drop((byte)1);

        column2.drop((byte)1);
        column2.drop((byte)1);
        
        column3.drop((byte)1);
        
        assertTrue(column0.isAtTopDiagonal1());
        assertTrue(column1.isAtTopDiagonal1());
        assertTrue(column2.isAtTopDiagonal2());
        assertTrue(column3.isAtTopDiagonal2());

        assertFalse(column0.isAtTopDiagonal2());
        assertFalse(column1.isAtTopDiagonal2());
        assertFalse(column2.isAtTopDiagonal1());
        assertFalse(column3.isAtTopDiagonal1());
    }

    @Test
    public void should_return_top_element() {
        column0.drop((byte)1);
        column0.drop((byte)2);

        assertEquals(2, column0.topElement());
    }
    
    @Test
    public void when_drop_same_elements_in_a_column_should_check_as_true() {
        assertFalse(column0.drop((byte)1));
        assertFalse(column0.drop((byte)1));
        assertFalse(column0.drop((byte)1));
        assertTrue(column0.drop((byte)1));
    }

    @Test
    public void when_drop_different_elements_in_a_column_should_check_as_false() {
        assertFalse(column0.drop((byte)1));
        assertFalse(column0.drop((byte)1));
        assertFalse(column0.drop((byte)1));
        assertFalse(column0.drop((byte)2));
    }

    @Test
    public void when_drop_same_elements_in_a_row_should_check_as_true() {
        assertFalse(column0.drop((byte)1));
        assertFalse(column0.drop((byte)5));

        assertFalse(column1.drop((byte)2));
        assertFalse(column1.drop((byte)5));

        assertFalse(column2.drop((byte)3));
        assertFalse(column2.drop((byte)5));

        assertFalse(column3.drop((byte)4));
        assertTrue(column3.drop((byte)5));
    }

    @Test
    public void when_drop_different_elements_in_a_row_should_check_as_false() {
        assertFalse(column0.drop((byte)1));
        assertFalse(column0.drop((byte)5));

        assertFalse(column1.drop((byte)2));
        assertFalse(column1.drop((byte)5));

        assertFalse(column2.drop((byte)3));
        assertFalse(column2.drop((byte)5));

        assertFalse(column3.drop((byte)4));
        assertFalse(column3.drop((byte)6));
    }

    @Test
    public void when_drop_same_elements_in_diagonal1_should_check_as_true() {
        assertFalse(column0.drop((byte)8));
        
        assertFalse(column1.drop((byte)2));
        assertFalse(column1.drop((byte)8));

        assertFalse(column2.drop((byte)3));
        assertFalse(column2.drop((byte)4));
        assertFalse(column2.drop((byte)8));

        assertFalse(column3.drop((byte)5));
        assertFalse(column3.drop((byte)6));
        assertFalse(column3.drop((byte)7));
        assertTrue(column3.drop((byte)8));
    }

    @Test
    public void when_drop_different_elements_in_diagonal1_should_check_as_false() {
        assertFalse(column0.drop((byte)1));

        assertFalse(column1.drop((byte)2));
        assertFalse(column1.drop((byte)8));

        assertFalse(column2.drop((byte)3));
        assertFalse(column2.drop((byte)4));
        assertFalse(column2.drop((byte)8));

        assertFalse(column3.drop((byte)5));
        assertFalse(column3.drop((byte)6));
        assertFalse(column3.drop((byte)7));
        assertFalse(column3.drop((byte)8));
    }

    @Test
    public void when_drop_same_elements_in_diagonal2_should_check_as_true() {
        assertFalse(column0.drop((byte)5));
        assertFalse(column0.drop((byte)6));
        assertFalse(column0.drop((byte)7));
        assertFalse(column0.drop((byte)8));

        assertFalse(column1.drop((byte)3));
        assertFalse(column1.drop((byte)4));
        assertFalse(column1.drop((byte)8));

        assertFalse(column2.drop((byte)2));
        assertFalse(column2.drop((byte)8));
        
        assertTrue(column3.drop((byte)8));
    }

    @Test
    public void when_drop_different_elements_in_diagonal2_should_check_as_false() {
        assertFalse(column0.drop((byte)5));
        assertFalse(column0.drop((byte)6));
        assertFalse(column0.drop((byte)7));
        assertFalse(column0.drop((byte)8));

        assertFalse(column1.drop((byte)3));
        assertFalse(column1.drop((byte)4));
        assertFalse(column1.drop((byte)8));

        assertFalse(column2.drop((byte)2));
        assertFalse(column2.drop((byte)8));

        assertFalse(column3.drop((byte)1));
    }
    
}
