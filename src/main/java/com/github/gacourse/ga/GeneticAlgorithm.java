package com.github.gacourse.ga;


public class GeneticAlgorithm {

    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    /**精英计数**/
    private int elitismCount;
    /**锦标赛规模**/
    protected int tournamentSize;

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {

        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    /**
     * Initialize population
     *  初始化种群
     * @param timetable
     * @return population The initial population generated
     */
    public Population initPopulation(Timetable timetable) {
        // Initialize population
        Population population = new Population(this.populationSize, timetable);
        return population;
    }

    /**
     * Check if population has met termination condition
     * 判断进化代数是否已达到最大进化代数
     * @param generationsCount
     *            Number of generations passed
     * @param maxGenerations
     *            Number of generations to terminate after
     * @return boolean True if termination condition met, otherwise, false
     */
    public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
        return (generationsCount > maxGenerations);
    }

    /**
     * Check if population has met termination condition
     * 判断进化是否满足终止条件
     * @param population
     * @return boolean True if termination condition met, otherwise, false
     */
    public boolean isTerminationConditionMet(Population population) {
        return population.getFittest(0).getFitness() == 1.0;
    }

    /**
     * Calculate individual's fitness value
     *  计算个体的适应度值
     * @param individual
     * @param timetable
     * @return fitness
     */
    public double calcFitness(Individual individual, Timetable timetable) {

        // Create new timetable object to use -- cloned from an existing timetable
        Timetable threadTimetable = new Timetable(timetable);
        //生成一个随机组合的课表单，包括班级，课程，随机时段，随机教室，随机授课教师（同一课程可能有多个教师授课，所以随机选择一个教师）
        threadTimetable.createClazzes(individual);

        //计算当前课表冲突数量
        int clashes = threadTimetable.calcClashes();
        // Calculate fitness 计算适应度
        double fitness = 1 / (double) (clashes + 1);

        //如果没有冲突 适应度为1.0
        individual.setFitness(fitness);

        return fitness;
    }

    /**
     * Evaluate population
     *
     * @param population
     * @param timetable
     */
    public void evalPopulation(Population population, Timetable timetable) {
        double populationFitness = 0;

        // Loop over population evaluating individuals and summing population fitness
        for (Individual individual : population.getIndividuals()) {
            //计算种群适应度，即所有个体在种群中的适应度之和
            populationFitness += this.calcFitness(individual, timetable);
        }
        population.setPopulationFitness(populationFitness);
    }

    /**
     * Selects parent for crossover using tournament selection
     * 控制竞标值
     * Tournament selection works by choosing N random individuals, and then
     * choosing the best of those.
     *
     * @param population
     * @return The individual selected as a parent
     */
    public Individual selectParent(Population population) {
        // Create tournament
        Population tournament = new Population(this.tournamentSize);

        // Add random individuals to the tournament
        // 随机打乱群众次序
        population.shuffle();
        // 选择锦标赛淘汰后的小群体
        for (int i = 0; i < this.tournamentSize; i++) {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i, tournamentIndividual);
        }

        // Return the best
        //获取最终竞争成功的个体
        return tournament.getFittest(0);
    }


    /**
     * Apply mutation to population 种群变异
     *
     * @param population
     * @param timetable
     * @return The mutated population
     */
    public Population mutatePopulation(Population population, Timetable timetable) {
        // Initialize new population
        Population newPopulation = new Population(this.populationSize);

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            //获取适应度最高的个体
            Individual individual = population.getFittest(populationIndex);

            // Create random individual to swap genes with
            Individual randomIndividual = new Individual(timetable);

            // Loop over individual's genes
            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                // Skip mutation if this is an elite individual
                if (populationIndex > this.elitismCount) {
                    // Does this gene need mutation?
                    // 如果发生基因突变
                    if (this.mutationRate > Math.random()) {
                        // Swap for new gene
                        // 替换染色体
                        individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
                    }
                }
            }

            // Add individual to population
            // 更新个体
            newPopulation.setIndividual(populationIndex, individual);
        }

        // Return mutated population
        return newPopulation;
    }

    /**
     * Apply crossover to population 交叉遗传
     *
     * @param population The population to apply crossover to
     * @return The new population
     */
    public Population crossoverPopulation(Population population) {
        // Create new population
        Population newPopulation = new Population(population.size());

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            //获取适应度最高的个体
            Individual parent1 = population.getFittest(populationIndex);

            // Apply crossover to this individual?
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                // Initialize offspring
                Individual offspring = new Individual(parent1.getChromosomeLength());

                // Find second parent
                Individual parent2 = selectParent(population);

                // Loop over genome
                for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                    // Use half of parent1's genes and half of parent2's genes
                    //交叉遗传概率为1/2
                    if (0.5 > Math.random()) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }

                // Add offspring to new population 替换当前个体为交叉后的新个体
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                // Add individual to new population without applying crossover
                // 如果没有发生交叉遗传保持不变
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }
}
