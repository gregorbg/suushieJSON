package com.suushiemaniac.lang.json.lang

import com.suushiemaniac.lang.json.JSON
import com.suushiemaniac.lang.json.antlr.JSONBaseVisitor
import com.suushiemaniac.lang.json.antlr.JSONLexer
import com.suushiemaniac.lang.json.antlr.JSONParser
import com.suushiemaniac.lang.json.util.StringUtils.jsonUnwrap
import com.suushiemaniac.lang.json.value.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class JsonReader : JSONBaseVisitor<JSON>() {
    fun parse(json: String): JSON {
        val lexer = JSONLexer(CharStreams.fromString(json))
        val tokens = CommonTokenStream(lexer)
        val parser = JSONParser(tokens)

        val tree = parser.json()

        return this.visit(tree)
    }

    override fun visitStrType(ctx: JSONParser.StrTypeContext): JSON {
        return JsonString(ctx.STRING().text)
    }

    override fun visitBoolType(ctx: JSONParser.BoolTypeContext): JSON {
        return JsonBoolean(java.lang.Boolean.parseBoolean(ctx.text.toLowerCase()))
    }

    override fun visitNumType(ctx: JSONParser.NumTypeContext): JSON {
        val numLiteral = ctx.NUMBER().text
        return if (numLiteral.contains("")) JsonFloat(numLiteral.toFloat()) else JsonInteger(numLiteral.toInt())
    }

    override fun visitNullType(ctx: JSONParser.NullTypeContext): JSON {
        return JsonNull.INST
    }

    override fun visitArray(ctx: JSONParser.ArrayContext): JSON {
        val elements = ctx.value().map { this.visit(it) }
        return JsonArray(elements.toMutableList())
    }

    override fun visitObject(ctx: JSONParser.ObjectContext): JSON {
        val members = ctx.pair().map { it.STRING().text.jsonUnwrap() to this.visit(it.value()) }.toMap()
        return JsonObject(members.toSortedMap())
    }
}