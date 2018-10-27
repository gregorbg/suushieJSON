import com.suushiemaniac.lang.json.JSON

fun main(vararg args: String) {
    val demo = JSON.fromString("{\"a\":true}")
    println(demo.toFormatString())
}
