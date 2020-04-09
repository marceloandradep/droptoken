package com._98point6.droptoken.model.board;

/**
 * This class is responsible for storing the actual token data
 * and to create the linking between columns, rows and diagonals
 * so that we don't need to iterate over an matrix to check
 * a winning condition
 */
class Column {

    /**
     * References to the rows of the grid
     */
    private Row[] rows;

    /**
     * References to the diagonals of the grid
     */
    private Row diagonal1;
    private Row diagonal2;

    /**
     * Data stored by the column
     */
    private byte[] data;

    /**
     * Index of this column relative to the grid
     */
    private int index;

    /**
     * Count of tokens added to the column
     */
    private int count;

    /**
     * Indicates if the tokens added are the same
     */
    private boolean hasSameToken = true;
    
    Column(int index, Row[] rows, Row diagonal1, Row diagonal2) {
        this.index = index;
        this.rows = rows;
        this.diagonal1 = diagonal1;
        this.diagonal2 = diagonal2;
        
        data = new byte[rows[0].capacity];
        count = 0;
    }

    /**
     * Returns true if the top element of this column is at diagonal1 of the grid
     * @return
     */
    boolean isAtTopDiagonal1() {
        return index == (count - 1);
    }

    /**
     * Returns true if the top element of this column is at diagonal2 of the grid
     * @return
     */
    boolean isAtTopDiagonal2() {
        Row row = topRow();
        return count == (row.capacity - index);
    }

    /**
     * Returns true if this column is full
     * @return
     */
    boolean isFull() {
        return count == data.length;
    }

    /**
     * Gets the top element of this column
     * @return
     */
    byte topElement() {
        if (count == 0) {
            return -1;
        }

        return data[count - 1];
    }

    /**
     * Put a token at the top of this column
     * @param token
     */
    private void putAtTop(byte token) {
        data[count++] = token;
    }

    /**
     * Gets in which row the top of this column is at
     * @return
     */
    private Row topRow() {
        return rows[count - 1];
    }

    /**
     * Drop a token in this column and returns true if it was a winning play
     * 
     * @param token
     * @return
     */
    boolean drop(byte token) {
        byte topElement = topElement();
        
        if (topElement != -1 && topElement != token) {
            hasSameToken = false;
        }

        putAtTop(token);
        
        Row row = topRow();
        row.tokenAdded(token);
        
        if (isAtTopDiagonal1()) {
            diagonal1.tokenAdded(token);
        }
        
        if (isAtTopDiagonal2()) {
            diagonal2.tokenAdded(token);
        }
        
        return row.check() || diagonal1.check() || diagonal2.check() || this.check();
    }

    /**
     * Check if this column is full and all elements are the same
     * @return
     */
    private boolean check() {
        return isFull() && hasSameToken;
    }
    
}
