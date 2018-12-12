package models

import java.util.concurrent.atomic.AtomicBoolean

trait BaseElement {
  private val state: AtomicBoolean = new AtomicBoolean(true)

  def prob: Float

  def calculateProb: Float = {
    if (isAlive)  {
      1.toFloat - prob
    } else {
      prob
    }
  }

  def restoreInitial(): Unit = {}

  def isAlive: Boolean = state.get()

  def broke(): Unit = state.set(false)

  def restore(): Unit = state.set(true)

  def and(other: BaseElement): Evaluated = Evaluated().updateState(this.isAlive && other.isAlive)

  def or(other: BaseElement): Evaluated = Evaluated().updateState(this.isAlive || other.isAlive)
}



