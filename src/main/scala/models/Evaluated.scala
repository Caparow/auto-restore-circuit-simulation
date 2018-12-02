package models

case class Evaluated() extends BaseElement {
  def updateState(t: Boolean): Evaluated = {
    if (t) restore() else broke()
    this
  }
}
