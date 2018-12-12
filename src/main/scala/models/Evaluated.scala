package models

case class Evaluated() extends BaseElement {
  override def prob: Float = 0

  def updateState(t: Boolean): Evaluated = {
    if (t) restore() else broke()
    this
  }
}
