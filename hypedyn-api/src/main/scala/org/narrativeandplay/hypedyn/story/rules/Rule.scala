package org.narrativeandplay.hypedyn.story.rules

trait Rule {
  def id: RuleId
  def conditions: List[Conditional]
  def actions: List[Actionable]
}

case class RuleId(value: Long) extends AnyVal with Ordered[RuleId] {
  override def compare(that: RuleId): Int = value compare that.value

  def increment = new RuleId(value + 1)
  def inc = increment

  def isValid = value >= 0
}
