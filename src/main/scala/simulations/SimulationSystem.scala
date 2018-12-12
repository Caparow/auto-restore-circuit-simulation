package simulations

import models.{BaseElement, Evaluated, ProcessorElement}

import scala.collection.mutable

class SimulationSystem(
                        elements: List[BaseElement],
                        funct: => Evaluated,
                        procRestoreTable: Map[ProcessorElement, Map[ProcessorElement, Int]]
                      ) {
  private val processors = elements.collect { case p: ProcessorElement => p }
  private val prosRestoreSystem = new ProcessorRestoreSystem(procRestoreTable, processors)
  private val number = elements.size
  private val counter = new mutable.HashMap[BaseElement, Int]
  private var fails = 0
  private var all = 0
  private var prob: List[Float] = List.empty

  def getProb(n: Int): Float = {
    prob.sum * (100 / n)
  }

  def performSimulation(n: Int, p: Int): Float = {
    dropCounters()
    val generated = generateFailures(n, p)
    generated.foreach(performOneStep(n, _))

    println("\n_____________________________________")
    println("\tSimulation configuration:")
    println(s"Elements to fail: $n")
    println(s"Number of variations: ${(factorial(number) / (factorial(number - n) * factorial(n))).toInt}")
    println(s"Percent of permutations to simulate: $p")
    println(s"Elements: ${elements.size}")
    println("_____________________________________")
    println("\tResults:")
    println(s"Simulations: $all")
    println(s"Failed times: $fails")
    println(s"Failure rate: ${fails.toFloat / all * 100}")
    println(s"Prob: ${getProb(p)}")
    println(s"\nAll fails:")
    println(Tabulator.format(Seq(Seq("Element", "Failed times")) ++ counter.toMap.toSeq.sortBy(-_._2).map { case (k, v) => Seq(k.toString, v.toString) }))
    prosRestoreSystem.printMostFailedRestores()

    getProb(p)
  }

  private def factorial(n: Int): Double = n match {
    case 0 => 1
    case _ => n * factorial(n - 1)
  }

  private def generateFailures(n: Int, p: Int): Iterator[List[Boolean]] = {
    val init = List.fill(n)(false) ++ List.fill(number - n)(true)
    val total = (factorial(number) / (factorial(number - n) * factorial(n))).toInt
    val permutations = init.permutations
    permutations.take(((total.toFloat / 100) * p).toInt)
  }

  private def performOneStep(n: Int, initState: List[Boolean]): Unit = {
    all += 1
    elements.foreach(_.restoreInitial())
    elements.zip(initState).foreach { case (ell, v) => if (!v) ell.broke() }
    val probs = List(elements.map(_.calculateProb).foldLeft(1.toFloat)(_ * _))
    val failed = elements.filter(!_.isAlive)

    prosRestoreSystem.performRestoring()
    if (!funct.isAlive) {
      fails += 1

      failed.foreach { f =>
        val prev = counter.getOrElse(f, 0)
        counter.put(f, prev + 1)
      }
    } else {
      prob = prob ++ probs
    }
  }

  private def dropCounters(): Unit = {
    prosRestoreSystem.dropCounter()
    counter.clear()
    fails = 0
    all = 0
    prob = List.empty
  }
}
