package com._98point6.droptoken.vertx.http.utils;

import com._98point6.droptoken.model.Game;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class CustomPropertyFilter extends SimpleBeanPropertyFilter {

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        if (pojo instanceof Game) {
            Game game = (Game)pojo;
            
            if (!writer.getName().equals("winner") || (writer.getName().equals("winner") && game.getWinner() != null) || game.isDraw()) {
                super.serializeAsField(pojo, jgen, provider, writer);
            }
        } else {
            super.serializeAsField(pojo, jgen, provider, writer);
        }
    }
}
