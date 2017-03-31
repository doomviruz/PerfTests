package main

import ("fmt"
	"time")

const magic  = 100031
const varCount = 32

type Signal struct{
	variables [varCount]int
	value int
}

type Individual struct {
	lowerGenes [varCount]int
	upperGenes [varCount]int
}

func FillPopulation() []Individual{
	var result []Individual = make([]Individual, 50)
	for i := 0; i < 50; i++{
		for j := 0; j < varCount; j++ {
			if i % 2 > 0 {
				result[i].lowerGenes[j] = 0
				result[i].upperGenes[j] = magic
			} else {
				result[i].lowerGenes[j] = magic / 2
				result[i].upperGenes[j] = magic / 2
			}
		}
	}
	return result
}

func FillSignals() []Signal{
	var result []Signal = make([]Signal, 100000)
	for i := 0; i < 100000; i++ {
		for j := 0; j < varCount; j++{
			result[i].variables[j] = j + i;
		}
		if i % 2 > 0{
			result[i].value = -1
		} else {
			result[i].value = 1
		}

	}
	return  result
}

func CalculateFF(signals []Signal, population []Individual, valueFactor int) int{
	var result = 0
	for i := 0; i < len(population); i++{
		var individuum = population[i]
		var lower = individuum.lowerGenes
		var upper = individuum.upperGenes
		for j := 0; j < 100000; j++{
			var passed bool = true
			var signal = &signals[j]
			var variables = &signal.variables
			for t := 0; t < varCount; t++{
				var val = variables[t]
				if val <= lower[t] {
					passed = false
					break;
				}

				if val >= upper[t] {
					passed = false
					break;
				}
			}

			if passed {
				if valueFactor == 1 {
					result += signal.value;
				}
			}
		}
	}
	return  result
}

func Run(){
	var population = FillPopulation()
	var signals = FillSignals()
	var total = 0;
	var start = time.Now()

	for generation := 0; generation < 1000; generation++ {
		total += CalculateFF(signals, population, generation % 2)
	}

	var timeSpan = time.Since(start)
	fmt.Println(total, timeSpan.Seconds())
}

func main(){
	Run()
}
