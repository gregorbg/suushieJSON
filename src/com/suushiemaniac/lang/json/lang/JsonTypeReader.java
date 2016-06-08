package com.suushiemaniac.lang.json.lang;

import com.suushiemaniac.lang.json.lang.antlr.JSONBaseVisitor;
import com.suushiemaniac.lang.json.lang.antlr.JSONParser;
import com.suushiemaniac.lang.json.util.StringUtils;
import com.suushiemaniac.lang.json.value.*;
import com.suushiemaniac.lang.json.value.JsonArray;
import com.suushiemaniac.lang.json.value.JsonObject;

import java.util.*;
import java.util.stream.Collectors;

public class JsonTypeReader extends JSONBaseVisitor<JSONType> {
    @Override
    public JSONType visitStrType(JSONParser.StrTypeContext ctx) {
        return new JsonString(ctx.STRING().getText());
    }

    @Override
    public JSONType visitBoolType(JSONParser.BoolTypeContext ctx) {
        return new JsonBoolean(Boolean.parseBoolean(ctx.getText().toLowerCase()));
    }

    @Override
    public JSONType visitNumType(JSONParser.NumTypeContext ctx) {
        String numLiteral = ctx.NUMBER().getText();
        return numLiteral.contains(".") ? new JsonFloat(Float.parseFloat(numLiteral)) : new JsonInteger(Integer.parseInt(numLiteral));
    }

    @Override
    public JSONType visitNullType(JSONParser.NullTypeContext ctx) {
        return new JsonNull();
    }

    @Override
    public JSONType visitArray(JSONParser.ArrayContext ctx) {
        List<JSONType> elements = ctx.value().stream().map(this::visit).collect(Collectors.toList());

        return new JsonArray(elements);
    }

    @Override
    public JSONType visitObject(JSONParser.ObjectContext ctx) {
        SortedMap<String, JSONType> members = new TreeMap<>();

        for (JSONParser.PairContext prCtx : ctx.pair()) {
            JSONType valValue = this.visit(prCtx.value());

            members.put(StringUtils.jsonUnwrap(prCtx.STRING().getText()), valValue);
        }

        return new JsonObject(members);
    }
}