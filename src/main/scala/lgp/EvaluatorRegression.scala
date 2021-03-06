package lgp

import lgp.Model.{Individuo, Problem}

import scala.util.Random

class EvaluatorRegression(implicit problem: Problem, random: Random) extends Evaluator[SampleRegression] {
  override def evaluate(individuo: Model.Individuo, samples: List[SampleRegression]): Double = {
    val (resulvedIndeviduo, variables) = prepareVariables(individuo.efectiveActions, problem)

    val costs = for {
      SampleRegression(parameters, result) <- samples
    } yield {
      val registers = parameters ++ variables
      resulvedIndeviduo.evaluate(registers)

      val error = result - registers(problem.inputSize)

      Math.pow(error, 2)
    }

    costs.sum * (1 + 0.001 * individuo.efectiveActions.size.toFloat / problem.maxCandidateSize.toFloat)
  }

  override def baseline(samples: List[SampleRegression]): Double = {
    val mean = samples.map(_.expected).sum / samples.size
    samples.map(sample => Math.pow(sample.expected-mean, 2)).sum / samples.size
  }
}
