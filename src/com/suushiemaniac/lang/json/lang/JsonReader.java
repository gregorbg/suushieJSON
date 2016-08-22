package com.suushiemaniac.lang.json.lang;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.lang.antlr.JSONBaseVisitor;
import com.suushiemaniac.lang.json.lang.antlr.JSONLexer;
import com.suushiemaniac.lang.json.lang.antlr.JSONParser;
import com.suushiemaniac.lang.json.util.StringUtils;
import com.suushiemaniac.lang.json.value.*;
import com.suushiemaniac.lang.json.value.JsonArray;
import com.suushiemaniac.lang.json.value.JsonObject;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;
import java.util.stream.Collectors;

public class JsonReader extends JSONBaseVisitor<JSON> {
    public JSON parse(String json) {
        JSONLexer lexer = new JSONLexer(new ANTLRInputStream(json));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JSONParser parser = new JSONParser(tokens);
        ParseTree tree = parser.json();
        return this.visit(tree);
    }

    @Override
    public JSON visitStrType(JSONParser.StrTypeContext ctx) {
        return new JsonString(ctx.STRING().getText());
    }

    @Override
    public JSON visitBoolType(JSONParser.BoolTypeContext ctx) {
        return new JsonBoolean(Boolean.parseBoolean(ctx.getText().toLowerCase()));
    }

    @Override
    public JSON visitNumType(JSONParser.NumTypeContext ctx) {
        String numLiteral = ctx.NUMBER().getText();
        return numLiteral.contains(".") ? new JsonFloat(Float.parseFloat(numLiteral)) : new JsonInteger(Integer.parseInt(numLiteral));
    }

    @Override
    public JSON visitNullType(JSONParser.NullTypeContext ctx) {
        return new JsonNull();
    }

    @Override
    public JSON visitArray(JSONParser.ArrayContext ctx) {
        List<JSON> elements = ctx.value().stream().map(this::visit).collect(Collectors.toList());

        return new JsonArray(elements);
    }

    @Override
    public JSON visitObject(JSONParser.ObjectContext ctx) {
        SortedMap<String, JSON> members = new TreeMap<>();

        for (JSONParser.PairContext prCtx : ctx.pair()) {
            JSON valValue = this.visit(prCtx.value());

            members.put(StringUtils.jsonUnwrap(prCtx.STRING().getText()), valValue);
        }

        return new JsonObject(members);
    }
}