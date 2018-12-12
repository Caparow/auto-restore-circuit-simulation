package models

import java.util.concurrent.atomic.AtomicReference

case class ProcessorElement(name: String, power: Int, max: Int, prob: Float) extends BaseElement {
  var currentPowerLevel: Int = power

  private val restoredBy: AtomicReference[ProcessorElement] = new AtomicReference[ProcessorElement](null)

  override def toString: String = s"Proces: $name"

  def setRestored(other: ProcessorElement): Unit = restoredBy.set(other)

  override def isAlive: Boolean =
    super.isAlive && Option(restoredBy.get()).forall(_.isAlive) && currentPowerLevel <= max

  override def restoreInitial(): Unit = {
    restoredBy.set(null)
    restore()
    currentPowerLevel = power
  }
}
