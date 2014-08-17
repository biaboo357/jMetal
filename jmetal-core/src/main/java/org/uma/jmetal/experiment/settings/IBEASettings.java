//  IBEA_Settings.java 
//
//  Authors:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.experiment.settings;

import org.uma.jmetal.core.Algorithm;
import org.uma.jmetal.experiment.Settings;
import org.uma.jmetal.metaheuristic.multiobjective.ibea.IBEA;
import org.uma.jmetal.operator.crossover.Crossover ;
import org.uma.jmetal.operator.crossover.SBXCrossover;
import org.uma.jmetal.operator.mutation.Mutation;
import org.uma.jmetal.operator.mutation.PolynomialMutation;
import org.uma.jmetal.operator.selection.BinaryTournament;
import org.uma.jmetal.operator.selection.Selection;
import org.uma.jmetal.problem.ProblemFactory;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.FitnessComparator;

import java.util.Properties;

/** Settings class of algorithm IBEA */
public class IBEASettings extends Settings {

  private int populationSize;
  private int maxEvaluations;
  private int archiveSize;

  private double mutationProbability;
  private double crossoverProbability;

  private double crossoverDistributionIndex;
  private double mutationDistributionIndex;

  /** Constructor */
  public IBEASettings(String problemName) throws JMetalException {
    super(problemName);

    Object[] problemParams = {"Real"};
    problem = (new ProblemFactory()).getProblem(this.problemName, problemParams);

    // Default experiment.settings
    populationSize = 100;
    maxEvaluations = 25000;
    archiveSize = 100;

    mutationProbability = 1.0 / problem.getNumberOfVariables();
    crossoverProbability = 0.9;

    crossoverDistributionIndex = 20.0;
    mutationDistributionIndex = 20.0;
  }

  /** Configure() method */
  @Override
  public Algorithm configure() throws JMetalException {
    Algorithm algorithm;
    Selection selection;
    Crossover crossover;
    Mutation mutation;

    crossover = new SBXCrossover.Builder()
            .probability(crossoverProbability)
            .distributionIndex(crossoverDistributionIndex)
            .build() ;

    mutation = new PolynomialMutation.Builder()
            .probability(mutationProbability)
            .distributionIndex(mutationDistributionIndex)
            .build() ;

    selection = new BinaryTournament.Builder()
            .comparator(new FitnessComparator())
            .build() ;

    algorithm = new IBEA.Builder(problem)
            .setArchiveSize(archiveSize)
            .setPopulationSize(populationSize)
            .setMaxEvaluations(maxEvaluations)
            .setCrossover(crossover)
            .setMutation(mutation)
            .setSelection(selection)
            .build() ;

    return algorithm;
  }

  /** Configure() method */
  @Override
  public Algorithm configure(Properties configuration) throws JMetalException {
    populationSize = Integer
      .parseInt(configuration.getProperty("populationSize", String.valueOf(populationSize)));
    maxEvaluations = Integer
      .parseInt(configuration.getProperty("maxEvaluations", String.valueOf(maxEvaluations)));
    archiveSize =
      Integer.parseInt(configuration.getProperty("archiveSize", String.valueOf(archiveSize)));

    crossoverProbability = Double.parseDouble(
      configuration.getProperty("crossoverProbability", String.valueOf(crossoverProbability)));
    crossoverDistributionIndex = Double.parseDouble(configuration
      .getProperty("crossoverDistributionIndex", String.valueOf(crossoverDistributionIndex)));

    mutationProbability = Double.parseDouble(
      configuration.getProperty("mutationProbability", String.valueOf(mutationProbability)));
    mutationDistributionIndex = Double.parseDouble(configuration
      .getProperty("mutationDistributionIndex", String.valueOf(mutationDistributionIndex)));

    return configure();
  }
} 
