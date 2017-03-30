using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Threading.Tasks;

namespace CS
{
    class Program
    {
        struct Signal
        {
            public int[] variables;

            public int value;
        }

        struct Individual
        {
            public int[] lowerGenes;

            public int[] upperGenes;
        }

        static void Main(string[] args)
        {
            var signals = FillSignals();
            var population = FillPopulation();
            var total = 0;
            var start = DateTime.Now;
            for (var generation = 0; generation < 1000; generation++)
                total += CalculateFF(signals, population, generation % 2);

            var time = DateTime.Now - start;
            Console.WriteLine($"{total} {time.TotalSeconds}");
        }

        private static int CalculateFF(Signal[] signals, Individual[] population, int valueFactor)
        {
            var result = 0;
            for(var i = 0; i < population.Length; i++)
            {
                var individuum = population[i];
                var lower = individuum.lowerGenes;
                var upper = individuum.upperGenes;
                for (var j = 0; j < signals.Length; j++)
                {
                    bool passed = true;
                    var signal = signals[j];
                    var variables = signal.variables;
                    for(var t = 0; t < variables.Length; t++)
                    {
                        var val = variables[t];
                        if (val <= lower[t])
                        {
                            passed = false;
                            break;
                        }

                        if (val >= upper[t])
                        {
                            passed = false;
                            break;
                        }
                    }

                    if(passed)
                        result += valueFactor == 1 ? signal.value : 0;
                }
            }

            return result;
        }

        static int magic = 100031;

        private static Individual[] FillPopulation()
        {
            var result = new Individual[50];
            for(var i = 0; i < result.Length; i++)
            {
                var lower = new int[32];
                var upper = new int[32];
                for(var j = 0; j < lower.Length; j++)
                {
                    lower[j] = i % 2 > 0 ? 0 : magic / 2;
                    upper[j] = i % 2 > 0 ? magic : magic / 2;
                }

                result[i].lowerGenes = lower;
                result[i].upperGenes = upper;
            }

            return result;
        }

        private static Signal[] FillSignals()
        {
            var result = new Signal[100000];
            for(var i = 0; i < result.Length; i++)
            {
                var variables = new int[32];
                for (var j = 0; j < variables.Length; j++)
                    variables[j] = j + i;

                result[i].variables = variables;
                result[i].value = i % 2 > 0 ? -1 : 1;
            }

            return result;
        }
    }
}
