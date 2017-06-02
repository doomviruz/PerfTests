import java.time.Duration
import java.time.LocalDateTime

case class Signal(variables : Array[Int], value : Int)

case class Individual(lowerGenes : Array[Int], upperGenes : Array[Int])

class Test {
  val magic = 100031
  val varCount = 32

  def lowerGenerator(i : Int) : Array[Int] = (for(_ <- 0 to varCount-1) yield  if (i % 2 > 0) 0 else magic / 2).toArray
  def upperGenerator(i : Int) : Array[Int] = (for(_ <- 0 to varCount-1) yield  if (i % 2 > 0) magic else magic / 2).toArray

  def FillPopulation: Array[Individual] = {
    (for(i <- 0 to 49) yield Individual(lowerGenerator(i), upperGenerator(i))).toArray
  }

  def FillSignals: Array[Signal] = {
    (for (i <- 0 to 99999) yield Signal((for(j <- 0 to varCount-1) yield j + i).toArray, if (i % 2 > 0) -1 else 1)).toArray
  }

  def CalculateFF(signals: Array[Signal], population: Array[Individual], valueFactor: Int): Int = {
    var result = 0
    val individualsCount = population.length;
    var i = 0;
    while(i < individualsCount){
      val individual = population(i)
      val lower = individual.lowerGenes
      val upper = individual.upperGenes
      val signalCount = signals.length;
      var j = 0;
      while(j < signalCount) {
        var passed = true
        var t = 0
        val signal = signals(j)
        val variables = signal.variables
        while (passed && t < varCount) {
          val value = variables(t)
          if(value <= lower(t))
            passed = false
          else
            if(value >= upper(t))
              passed = false

          t += 1
        }

        if(passed && valueFactor == 1)
          result += signal.value

        j += 1;
      }

      i += 1;
    }
    result
  }

  def Run : Unit = {
    val signals = FillSignals
    val population = FillPopulation
    var total = 0
    val start = LocalDateTime.now()
    var generation = 0;
    while(generation < 1000) {
      total += CalculateFF(signals, population, generation % 2)
      generation += 1;
    }

    val time = Duration.between(start, LocalDateTime.now())
    System.out.println(s"$total ${time.getSeconds}")
  }
}
