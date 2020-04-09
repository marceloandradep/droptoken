package com._98point6.droptoken.model.factories;

import com._98point6.droptoken.model.DefaultMoveList;
import com._98point6.droptoken.model.interfaces.MoveList;
import org.springframework.stereotype.Component;

@Component
public class MoveListFactory {
    
    public MoveList create() {
        return new DefaultMoveList();
    }
    
}
