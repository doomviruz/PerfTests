package com.company;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by dv on 15.02.2017.
 */


public class Test{
    static int magic = 100031;

    public Individual[] FillPopulation()
    {
        Individual[] result = new Individual[50];
        for(int i = 0; i < result.length; i++)
        {
            result[i] = new Individual();
            int[] lower = new int[32];
            int[] upper = new int[32];
            for(int j = 0; j < lower.length; j++)
            {
                lower[j] = i % 2 > 0 ? 0 : magic / 2;
                upper[j] = i % 2 > 0 ? magic : magic / 2;
            }

            result[i].lowerGenes = lower;
            result[i].upperGenes = upper;
        }

        return result;
    }

    public Signal[] FillSignals()
    {
        Signal[] result = new Signal[100000];
        for(int i = 0; i < result.length; i++)
        {
            result[i] = new Signal();
            int[] variables = new int[32];
            for (int j = 0; j < variables.length; j++)
                variables[j] = j + i;

            result[i].variables = variables;
            result[i].value = i % 2 > 0 ? -1 : 1;
        }

        return result;
    }

    public int CalculateFF(Signal[] signals, Individual[] population, int valueFactor)
    {
        int result = 0;
        for(int i = 0; i < population.length; i++)
        {
            Individual individuum = population[i];
            int[] lower = individuum.lowerGenes;
            int[] upper = individuum.upperGenes;
            for (int j = 0; j < signals.length; j++)
            {
                boolean passed = true;
                Signal signal = signals[j];
                int[] variables = signal.variables;
                for(int t = 0; t < variables.length; t++)
                {
                    int val = variables[t];
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

    public Test(){
        Signal[] signals = FillSignals();
        Individual[] population = FillPopulation();
        int total = 0;
        LocalDateTime start = LocalDateTime.now();
        for (int generation = 0; generation < 1000; generation++)
            total += CalculateFF(signals, population, generation % 2);

        Duration time = Duration.between(start, LocalDateTime.now());
        String s = String.format("%1$d %2$d", total, time.getSeconds());
        System.out.println(s);
    }
}
