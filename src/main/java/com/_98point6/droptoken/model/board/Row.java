package com._98point6.droptoken.model.board;

/**
 * This class keeps track of tokens as they are being added to it.
 * Once it gets full it should be able to determine if a winning play
 * took place or not.
 */
class Row {

    /**
     * Total capacity of a row
     */
    int capacity;

    /**
     * Count of tokens added
     */
    private int count;

    /**
     * Last token added
     */
    private byte lastTokenAdded = -1;

    /**
     * A flag that indicates if all tokens added are the same or not
     */
    private boolean hasSameElements = true;
    
    Row(int capacity) {
        this.capacity = capacity;
        this.count = 0;
    }

    /**
     * This is called by the column when its top element reaches the height of this row
     * @param token
     */
    void tokenAdded(byte token) {
        count++;
        
        if (lastTokenAdded != -1 && token != lastTokenAdded) {
            hasSameElements = false;
        }
        
        lastTokenAdded = token;
    }

    /**
     * Indicates if this row is already full
     * @return
     */
    boolean isFull() {
        return count == capacity;
    }

    /**
     * Returns true if the row is full of tokens with the same value
     * @return
     */
    boolean check() {
        return isFull() && hasSameElements;
    }
    
}
