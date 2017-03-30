import java.time.*

data class Signal(val variables : IntArray, val value : Int)

data class Individual(val lowerGenes : IntArray, val upperGenes : IntArray)

class Test{
    val magic = 100031
    val varCount = 32

    fun lowerGenerator(i : Int) : IntArray {
        return IntArray(varCount, {v -> if (i % 2 > 0) 0 else magic / 2})
    }

    fun upperGenerator(i : Int) : IntArray {
        return IntArray(varCount, {v -> if (i % 2 > 0) magic else magic / 2})
    }

    fun FillPopulation(): Array<Individual> {
        return Array(50, {i -> Individual(lowerGenerator(i), upperGenerator(i))})
    }

    fun FillSignals(): Array<Signal> {
        return Array(100000, { i -> Signal(IntArray(varCount, {j -> j + i}) , if(i % 2 > 0) -1 else 1) })
    }

    fun CalculateFF(signals: Array<Signal>, population: Array<Individual>, valueFactor: Int): Int {
        var result = 0
        for(i in population.indices) {
            val individual = population[i]
            val lower = individual.lowerGenes
            val upper = individual.upperGenes

            for (j in signals.indices) {
                var passed = true
                val signal = signals[j]
                val variables = signal.variables
                for(t in 0..varCount-1) {
                    val value = variables[t]
                    if(value <= lower[t])
                    {
                        passed = false
                        break
                    }

                    if(value >= upper[t])
                    {
                        passed = false
                        break
                    }
                }

                if(passed && valueFactor == 1)
                    result += signal.value
            }
        }
        return result
    }

    fun Run() : Unit {
        val trades = FillSignals()
        val population = FillPopulation()
        val start = LocalDateTime.now()
        val total = (0..999).sumBy { CalculateFF(trades, population,  it % 2) }

        val time = Duration.between(start, LocalDateTime.now())
        System.out.println("$total ${time.getSeconds()}")
    }
}

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val test = Test()
        test.Run()
    }
}
