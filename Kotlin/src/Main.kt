import java.time.Duration
import java.time.LocalDateTime

data class Signal(val variables: IntArray, val value: Int)
data class Individual(val lowerGenes: IntArray, val upperGenes: IntArray)

val magic = 100031
val varCount = 32

fun lowerGenerator(i: Int) = IntArray(varCount) { if (i % 2 > 0) 0 else magic / 2 }

fun upperGenerator(i: Int) = IntArray(varCount) { if (i % 2 > 0) magic else magic / 2 }

fun fillPopulation() = Array(50) { Individual(lowerGenerator(it), upperGenerator(it)) }

fun fillSignals() = Array(100000) { Signal(IntArray(varCount, { j -> j + it }), if (it % 2 > 0) -1 else 1) }

fun calculateFF(signals: Array<Signal>, population: Array<Individual>, valueFactor: Int): Int {
    var result = 0
    for ((lower, upper) in population) for ((variables, value1) in signals) {
        var passed = true
        for (t in 0..varCount - 1) {
            val value = variables[t]
            if (value <= lower[t]) {
                passed = false
                break
            }

            if (value >= upper[t]) {
                passed = false
                break
            }
        }

        if (passed && valueFactor == 1)
            result += value1
    }
    return result
}

fun main(args: Array<String>) {
    val trades = fillSignals()
    val population = fillPopulation()
    val start = LocalDateTime.now()
    val total = (0..999).sumBy { calculateFF(trades, population, it % 2) }

    val time = Duration.between(start, LocalDateTime.now())
    System.out.println("$total ${time.seconds}")
}
