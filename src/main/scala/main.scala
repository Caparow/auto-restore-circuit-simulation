import models.{Element, Evaluated, ProcessorElement}
import simulations.SimulationSystem

object main extends App {

  /**
    * List of all elements that was defined in scheme of variant #48
    */

  // P - processors
  val p1 = ProcessorElement("Pr-1", 40, 80)
  val p2 = ProcessorElement("Pr-2", 60, 100)
  val p3 = ProcessorElement("Pr-3", 20, 60)
  val p5 = ProcessorElement("Pr-5", 50, 100)
  val p6 = ProcessorElement("Pr-6", 30, 70)

  // A - elements
  val a1 = Element("A-1")
  val a2 = Element("A-2")

  // B - buses
  val b1 = Element("B-1")
  val b2 = Element("B-2")
  val b4 = Element("B-4")
  val b5 = Element("B-5")

  // C - elements
  val c1 = Element("C-1")
  val c2 = Element("C-2")
  val c4 = Element("C-4")
  val c5 = Element("C-5")
  val c6 = Element("C-6")

  // D - elements
  val d1 = Element("D-1")
  val d2 = Element("D-2")
  val d3 = Element("D-3")
  val d6 = Element("D-6")
  val d8 = Element("D-8")

  // M - bridges
  val m1 = Element("M-1")
  val m2 = Element("M-2")

  val all = List(p1, p2, p3, p5, p6, a1, a2, b1, b2, b4, b5, c1, c2, c4, c5, c6, d1, d2, d3, d6, d8, m1, m2)

  /**
    * Here processors restoring table (just like in methodical sources) that was created randomly with inspiration
    * of sample from lectures
    */
  val pt = Map(
    p1 -> Map(p2 -> 30, p3 -> 40, p6 -> 10), //need 40
    p2 -> Map(p1 -> 50, p3 -> 40, p5 -> 30, p6 -> 30), // need 60
    p3 -> Map(p1 -> 20, p2 -> 30, p5 -> 20), // need 20
    p5 -> Map(p2 -> 30, p3 -> 50, p6 -> 20), // need 50
    p6 -> Map(p1 -> 30, p2 -> 20, p3 -> 10, p5 -> 20) // need 30
  )

  /**
    * Randomly created functions that describes logic of entire circuit (variant #48)
    */
  def f1: Evaluated = (d1 or d2) and c1 and (b1 or b2) and p2 and p3 and a1 and (m1 or m2) and c4 and d6

  def f2: Evaluated = (d2 or d3) and c2 and (b1 or b2) and p1 and a1 and (m1 or m2) and a2 and (b4 or b5) and p6

  def f3: Evaluated = d8 and (c5 or c6) and b4 and p5 and a2 and m1 and d6 and c4 and a1 and (b1 or b2) and p3

  def f4: Evaluated = p1 and (b1 or b2) and (d1 or d2) and c1 and a1 and (m1 or m2) and a2 and b4 and p5 and d8 and (c5 or c6) and d8

  /**
    * Concatenation of four functions above
    */
  def functionToCheck: Evaluated = f1 and f2 and f3 and f4


  /**
    * Base simulation system (blackbox was user)
    */
  val simulation = new SimulationSystem(all, functionToCheck, pt)

  /**
    * Here functions which perform simulations.
    * First parameter - number of elements which would fail in one simulation step (number of permutations calculates based on this value).
    * Second parameter - percent of permutations to take for simulation performing.
    * Now this for calls will perform simulations which required by laboratory work.
    *
    * !!!!!! READ THIS !!!!!!
    *
    * So all you need :
    * 1 - just perform calculations
    * 2 - based on results of simulations decide which elements need to be changed (maybe it could be processors table ?)
    * 3 - change entire circuit (and/or tables).
    * 4 - add/remove elements above if you need
    * 5 - create new 4 functions based on your !!!NEW!!! circuit.
    * 6 - perform calculations again
    * 7 - ???
    * 8 - PROFIT
    * 9 - write results into document, but remember YOU NEW CIRCUIT MUST HAVE LOVER FAILURE LEVEL THEN BASE CIRCUIT !!!
    *     in other case failed not elements - but you :c
    */
  simulation.performSimulation(1, 100)
  simulation.performSimulation(2, 100)
  simulation.performSimulation(3, 50)
  simulation.performSimulation(4, 10)
}
