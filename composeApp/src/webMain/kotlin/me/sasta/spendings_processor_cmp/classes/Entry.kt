package me.sasta.spendings_processor_cmp.classes

import me.sasta.spendings_processor_cmp.classes.UtilFuncs.checkIfUselessDecimal

data class Entry(
    val name: String,
    val cost: Double
    )
{
    override fun toString(): String {
        return "$name = ${checkIfUselessDecimal(cost)}"
    }
}
