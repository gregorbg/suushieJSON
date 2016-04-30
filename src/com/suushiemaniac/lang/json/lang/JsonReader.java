package com.suushiemaniac.lang.json.lang;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.lang.antlr.JSONBaseVisitor;
import com.suushiemaniac.lang.json.lang.antlr.JSONLexer;
import com.suushiemaniac.lang.json.lang.antlr.JSONParser;
import com.suushiemaniac.lang.json.value.JSONType;
import com.suushiemaniac.lang.json.value.JsonElement;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class JsonReader extends JSONBaseVisitor<JSON> {
    private JsonTypeReader typeReader;

    public JsonReader() {
        this.typeReader = new JsonTypeReader();
    }

    public JSON parse(String json) {
        JSONLexer lexer = new JSONLexer(new ANTLRInputStream(json));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JSONParser parser = new JSONParser(tokens);
        ParseTree tree = parser.json();
        return this.visit(tree);
    }

    @Override
    public JSON visitJson(JSONParser.JsonContext ctx) {
        JSONType type = this.typeReader.visit(ctx);
        return new JSON(type instanceof JsonElement ? (JsonElement) type : null);
    }
}
