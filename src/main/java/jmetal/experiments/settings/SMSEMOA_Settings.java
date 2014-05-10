//  SMSEMA_Settings.java 
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

package jmetal.experiments.settings;

import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.smsemoa.SMSEMOA;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ProblemFactory;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Settings class of algorithm SMSEMOA
 */
public class SMSEMOA_Settings extends Settings {
  public int populationSize_                ;
  public int maxEvaluations_                ;
  public double mutationProbability_        ;
  public double crossoverProbability_       ;
  public double crossoverDistributionIndex_ ;
  public double mutationDistributionIndex_  ;
  public double offset_                     ;
  
  /**
   * Constructor
   */
  public SMSEMOA_Settings(String problem) {
    super(problem) ;
    
    Object [] problemParams = {"Real"};
    try {
	    problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
    } catch (JMException e) {
      Configuration.logger_.log(Level.SEVERE, "Unable to get problem", e);
    }      
    populationSize_             = 100   ; 
    maxEvaluations_             = 25000 ;
    mutationProbability_        = 1.0/problem_.getNumberOfVariables() ;
    crossoverProbability_       = 0.9   ;
    crossoverDistributionIndex_ = 20.0  ;
    mutationDistributionIndex_  = 20.0  ;
    offset_                     = 100.0 ;

  } // SMSEMOA_Settings

  
  /**
   * Configure SMSEMOA with user-defined parameter experiments.settings
   * @return A SMSEMOA algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm ;
    Selection  selection ;
    Crossover  crossover ;
    Mutation   mutation  ;

    // Creating the algorithm. 
    algorithm = new SMSEMOA(problem_) ;
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize",populationSize_);
    algorithm.setInputParameter("maxEvaluations",maxEvaluations_);
    algorithm.setInputParameter("offset", offset_);

    // Mutation and Crossover for Real codification 
    HashMap<String, Object> parameters = new HashMap<String, Object>() ;
    parameters.put("probability", crossoverProbability_) ;
    parameters.put("distributionIndex", crossoverDistributionIndex_) ;
    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);                   

    parameters = new HashMap<String, Object>() ;
    parameters.put("probability", mutationProbability_) ;
    parameters.put("distributionIndex", mutationDistributionIndex_) ;
    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);                    

		// Selection Operator
    parameters = null ;
	selection = SelectionFactory.getSelectionOperator("RandomSelection", parameters);

    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);
   
    return algorithm ;
  } // configure

  /**
   * Configure SMSEMOA with user-defined parameter experiments.settings
   * @return A SMSEMOA algorithm object
   */
  @Override
  public Algorithm configure(Properties configuration) throws JMException {
    Algorithm algorithm ;
    Selection  selection ;
    Crossover  crossover ;
    Mutation   mutation  ;

    // Creating the algorithm.
    algorithm = new SMSEMOA(problem_) ;

    // Algorithm parameters
    populationSize_ = Integer.parseInt(configuration.getProperty("populationSize",String.valueOf(populationSize_)));
    maxEvaluations_  = Integer.parseInt(configuration.getProperty("maxEvaluations",String.valueOf(maxEvaluations_)));
    offset_ = Double.parseDouble(configuration.getProperty("offset",String.valueOf(offset_)));
    algorithm.setInputParameter("populationSize",populationSize_);
    algorithm.setInputParameter("maxEvaluations",maxEvaluations_);
    algorithm.setInputParameter("offset", offset_);

    // Mutation and Crossover for Real codification
    crossoverProbability_ = Double.parseDouble(configuration.getProperty("crossoverProbability",String.valueOf(crossoverProbability_)));
    crossoverDistributionIndex_ = Double.parseDouble(configuration.getProperty("crossoverDistributionIndex",String.valueOf(crossoverDistributionIndex_)));

    HashMap<String, Object> parameters = new HashMap<String, Object>() ;
    parameters.put("probability", crossoverProbability_) ;
    parameters.put("distributionIndex", crossoverDistributionIndex_) ;
    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);

    mutationProbability_ = Double.parseDouble(configuration.getProperty("mutationProbability",String.valueOf(mutationProbability_)));
    mutationDistributionIndex_ = Double.parseDouble(configuration.getProperty("mutationDistributionIndex",String.valueOf(mutationDistributionIndex_)));
    parameters = new HashMap<String, Object>() ;

    parameters.put("probability", mutationProbability_) ;
    parameters.put("distributionIndex", mutationDistributionIndex_) ;
    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);

    // Selection Operator
    parameters = null ;
    selection = SelectionFactory.getSelectionOperator("RandomSelection", parameters) ;

    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);

    return algorithm ;
  }
} // SMSEMOA_Settings
