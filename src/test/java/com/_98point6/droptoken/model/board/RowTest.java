package com._98point6.droptoken.model.board;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RowTest {

    @Test
    public void when_row_is_filled_should_return_is_full() {
        Row row = new Row(4);

        row.tokenAdded((byte)1);
        row.tokenAdded((byte)1);
        row.tokenAdded((byte)1);
        row.tokenAdded((byte)1);

        assertTrue(row.isFull());
    }

    @Test
    public void when_row_is_partially_filled_should_return_not_is_full() {
        Row row = new Row(4);

        row.tokenAdded((byte)1);
        row.tokenAdded((byte)1);
        
        assertFalse(row.isFull());
    }

    @Test
    public void when_drop_same_elements_should_check_as_true() {
        Row row = new Row(4);

        row.tokenAdded((byte)1);
        row.tokenAdded((byte)1);
        row.tokenAdded((byte)1);
        row.tokenAdded((byte)1);

        assertTrue(row.check());
    }

    @Test
    public void when_drop_different_elements_should_check_as_false() {
        Row row = new Row(4);

        row.tokenAdded((byte)1);
        row.tokenAdded((byte)1);
        row.tokenAdded((byte)2);
        row.tokenAdded((byte)1);

        assertFalse(row.check());
    }

    @Test
    public void when_partially_filled_should_check_as_false() {
        Row row = new Row(4);

        row.tokenAdded((byte)1);
        row.tokenAdded((byte)1);

        assertFalse(row.check());
    }
    
}
