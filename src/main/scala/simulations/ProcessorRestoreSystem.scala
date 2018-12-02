package simulations

import models.ProcessorElement
import simulations.ProcessorRestoreSystem.RestoreOp

import scala.collection.mutable

class ProcessorRestoreSystem(table: Map[ProcessorElement, Map[ProcessorElement, Int]], procList: List[ProcessorElement]) {
  private val mostRestored = new mutable.HashMap[RestoreOp, Int]

  def performRestoring(): Unit = {
    for (_ <- 1 to table.size) {
      procList.foreach { proc =>
        if (!proc.isAlive) {
          val availableToRestore = table.getOrElse(proc, Map.empty)
          val (canRestore, setToRestore) = findBestProcList(availableToRestore, proc.power)

          setToRestore.foreach { case (p, v) =>
            p.currentPowerLevel += v
            val key = RestoreOp(proc, p, canRestore)
            val cur = mostRestored.getOrElse(key, 0)
            mostRestored.put(key, cur + 1)
          }

          if (canRestore) {
            proc.restore()
          }

        }
      }
    }
  }

  def printMostFailedRestores(): Unit = {
    println("\n\tFailed processors restores:")
    println(Tabulator.format( Seq(Seq("Patient", "Donor", "Failed times")) ++
      mostRestored.toSeq.filter(!_._1.res).sortBy(- _._2)
        .map{ case (ro, v) => Seq(ro.patient, ro.donor, v).map(_.toString) })
    )
  }

  def dropCounter(): Unit = {
    mostRestored.clear()
  }

  private def findBestProcList(procList: Map[ProcessorElement, Int], powerToRestore: Int): (Boolean, Set[(ProcessorElement, Int)]) = {
    val filtered = procList.filter { case (p, _) => p.isAlive }
    val allPossible = filtered.toSet.subsets().toList
    val goodCombinations = allPossible.filter(_.map(_._2).sum >= powerToRestore)
    val (toGood, toBad) = goodCombinations.partition(_.forall { case (p, v) => p.max - p.currentPowerLevel >= v })

    if (toGood.nonEmpty) {
      true -> toGood.minBy(_.size)
    } else if (toBad.nonEmpty) {
      false -> toBad.minBy(_.size)
    } else {
      false -> Set.empty
    }
  }
}

object ProcessorRestoreSystem {

  case class RestoreOp(patient: ProcessorElement, donor: ProcessorElement, res: Boolean){
    override def toString: String = s"$patient <- $donor"
  }

}