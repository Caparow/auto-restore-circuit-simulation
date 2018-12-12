package models

case class Element(name: String, prob: Float) extends BaseElement {
  override def toString: String = s"Element: $name"

  override def restoreInitial(): Unit = super.restore()
}