package org.narrativeandplay.hypedyn.serialisation

import org.narrativeandplay.hypedyn.story.Narrative.ReaderStyle
import org.narrativeandplay.hypedyn.story.internal.Story.Metadata
import org.narrativeandplay.hypedyn.story.rules.BooleanOperator.{And, Or}
import org.narrativeandplay.hypedyn.story.{Narrative, NodalContent, NodeId}
import org.narrativeandplay.hypedyn.story.internal.NodeContent.Ruleset
import org.narrativeandplay.hypedyn.story.internal.{NodeContent, Story, Node}
import org.narrativeandplay.hypedyn.story.rules._
import org.narrativeandplay.hypedyn.story.rules.internal.{Action, Condition, Rule}

package object serialisers {

  /**
   * Typeclass instance for serialising Nodes
   */
  implicit object NodeSerialiser extends Serialisable[Node] {
    /**
     * Returns the serialised representation of an object
     *
     * @param node The object to serialise
     */
    override def serialise(node: Node): AstElement = AstMap("id" -> AstInteger(node.id.value),
                                                         "name" -> AstString(node.name),
                                                         "content" -> NodeContentSerialiser.serialise(node.content),
                                                         "isStart" -> AstBoolean(node.isStartNode),
                                                         "rules" -> AstList((node.rules map RuleSerialiser.serialise).toSeq: _*))

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): Node = {
      val data = serialised.asInstanceOf[AstMap]
      val id = data("id").asInstanceOf[AstInteger].i
      val name = data("name").asInstanceOf[AstString].s
      val content = NodeContentSerialiser.deserialise(data("content"))
      val isStart = data("isStart").asInstanceOf[AstBoolean].boolean
      val rules = data("rules").asInstanceOf[AstList].toList map RuleSerialiser.deserialise

      new Node(NodeId(id), name, content, isStart, rules)
    }
  }

  /**
   * Implicit class to allow the use of `node.serialise`
   *
   * @param node The node to extend
   */
  implicit class SerialisableNode(node: Node) {
    def serialise = NodeSerialiser.serialise(node)
  }

  /**
   * Typeclass instance for serialising stories
   */
  implicit object StorySerialiser extends Serialisable[Story] {
    /**
     * Returns the serialised representation of an object
     *
     * @param story The object to serialise
     */
    override def serialise(story: Story): AstElement =
      AstMap("title" -> AstString(story.title),
             "author" -> AstString(story.author),
             "description" -> AstString(story.description),
             "metadata" -> StoryMetadataSerialiser.serialise(story.metadata),
             "nodes" -> AstList(story.nodes map NodeSerialiser.serialise: _*),
             "facts" -> AstList(story.facts map FactSerialiser.serialise: _*),
             "rules" -> AstList(story.rules map RuleSerialiser.serialise: _*))

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): Story = {
      val data = serialised.asInstanceOf[AstMap]
      val title = data("title").asInstanceOf[AstString].s
      val author = data("author").asInstanceOf[AstString].s
      val description = data("description").asInstanceOf[AstString].s
      val metadata = StoryMetadataSerialiser.deserialise(data("metadata"))
      val nodes = data("nodes").asInstanceOf[AstList].toList map NodeSerialiser.deserialise
      val facts = data("facts").asInstanceOf[AstList].toList map FactSerialiser.deserialise
      val rules = data("rules").asInstanceOf[AstList].toList map RuleSerialiser.deserialise

      new Story(title, author, description, metadata, nodes, facts, rules)
    }
  }

  /**
   * Implicit class to allow `story.serialise`
   * @param story
   */
  implicit class SerialisableStory(story: Story) {
    def serialise = StorySerialiser.serialise(story)
  }

  /**
   * Typeclass instance for serialising node content
   */
  implicit object NodeContentSerialiser extends Serialisable[NodeContent] {
    /**
     * Returns the serialised representation of an object
     *
     * @param nodeContent The object to serialise
     */
    override def serialise(nodeContent: NodeContent): AstElement =
      AstMap("text" -> AstString(nodeContent.text),
             "rulesets" -> AstList(nodeContent.rulesets map RulesetSerialiser.serialise: _*))

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): NodeContent = {
      val data = serialised.asInstanceOf[AstMap]
      val text = data("text").asInstanceOf[AstString].s
      val rulesets = data("rulesets").asInstanceOf[AstList].toList map RulesetSerialiser.deserialise

      NodeContent(text, rulesets)
    }
  }

  /**
   * Typeclass instance for serialising rulesets
   */
  implicit object RulesetSerialiser extends Serialisable[NodeContent.Ruleset] {
    /**
     * Returns the serialised representation of an object
     *
     * @param ruleset The object to serialise
     */
    override def serialise(ruleset: Ruleset): AstElement =
      AstMap("id" -> AstInteger(ruleset.id.value),
             "name" -> AstString(ruleset.name),
             "start" -> AstInteger(ruleset.indexes.startIndex.index),
             "end" -> AstInteger(ruleset.indexes.endIndex.index),
             "rules" -> AstList(ruleset.rules map RuleSerialiser.serialise: _*))

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): Ruleset = {
      import NodalContent._
      val data = serialised.asInstanceOf[AstMap]
      val id = RulesetId(data("id").asInstanceOf[AstInteger].i)
      val name = data("name").asInstanceOf[AstString].s
      val indexes = RulesetIndexes(TextIndex(data("start").asInstanceOf[AstInteger].i),
                                   TextIndex(data("end").asInstanceOf[AstInteger].i))
      val rules = data("rules").asInstanceOf[AstList].toList map RuleSerialiser.deserialise

      Ruleset(id, name, indexes, rules)
    }
  }

  /**
   * Typeclass instance for serialising rules
   */
  implicit object RuleSerialiser extends Serialisable[Rule] {
    /**
     * Returns the serialised representation of an object
     *
     * @param rule The object to serialise
     */
    override def serialise(rule: Rule): AstElement =
      AstMap("id" -> AstInteger(rule.id.value),
             "name" -> AstString(rule.name),
             "stopIfTrue" -> AstBoolean(rule.stopIfTrue),
             "conditionsOp" -> AstString(rule.conditionsOp match {
                                           case And => "and"
                                           case Or => "or"
                                         }),
             "conditions" -> AstList(rule.conditions map ConditionSerialiser.serialise: _*),
             "actions" -> AstList(rule.actions map ActionSerialiser.serialise: _*))

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): Rule = {
      val data = serialised.asInstanceOf[AstMap]
      val id = RuleId(data("id").asInstanceOf[AstInteger].i)
      val name = data("name").asInstanceOf[AstString].s
      val stopIfTrue = data("stopIfTrue").asInstanceOf[AstBoolean].boolean
      val conditionsOp = data("conditionsOp").asInstanceOf[AstString].s match {
        case "and" => And
        case "or" => Or
        case unknown => throw DeserialisationException(s"Unknown operator for conditons: $unknown")
      }
      val conditions = data("conditions").asInstanceOf[AstList].toList map ConditionSerialiser.deserialise
      val actions = data("actions").asInstanceOf[AstList].toList map ActionSerialiser.deserialise

      Rule(id, name, stopIfTrue, conditionsOp, conditions, actions)
    }
  }

  /**
   * Typeclass instance for serialsing conditions
   */
  implicit object ConditionSerialiser extends Serialisable[Condition] {
    /**
     * Returns the serialised representation of an object
     *
     * @param condition The object to serialise
     */
    override def serialise(condition: Condition): AstElement =
      AstMap("conditionType" -> AstString(condition.conditionType.value),
             "params" -> AstMap((condition.params map { case (k, v) =>
               k.value -> AstString(v.value)
             }).toSeq: _*))

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): Condition = {
      val data = serialised.asInstanceOf[AstMap]
      val conditionType = data("conditionType").asInstanceOf[AstString].s
      val params = data("params").asInstanceOf[AstMap].toMap map { case (k, v) =>
        k -> v.asInstanceOf[AstString].s
      }

      Condition(Conditional.ConditionType(conditionType), params map { case (k, v) =>
        RuleLike.ParamName(k) -> RuleLike.ParamValue(v)
      })
    }
  }

  /**
   * Typeclass instance for serialising actions
   */
  implicit object ActionSerialiser extends Serialisable[Action] {
    /**
     * Returns the serialised representation of an object
     *
     * @param action The object to serialise
     */
    override def serialise(action: Action): AstElement = AstMap("actionType" -> AstString(action.actionType.value),
                                                                "params" -> AstMap((action.params map { case (k, v) =>
                                                                    k.value -> AstString(v.value)
                                                                }).toSeq: _*))

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): Action = {
      val data = serialised.asInstanceOf[AstMap]
      val actionType = data("actionType").asInstanceOf[AstString].s
      val params = data("params").asInstanceOf[AstMap].toMap map { case (k, v) =>
          k -> v.asInstanceOf[AstString].s
      }

      Action(Actionable.ActionType(actionType), params map { case (k, v) =>
        RuleLike.ParamName(k) -> RuleLike.ParamValue(v)
      })
    }
  }

  /**
   * Typeclass instance for serialising facts
   */
  implicit object FactSerialiser extends Serialisable[Fact] {
    /**
     * Returns the serialised representation of an object
     *
     * @param fact The object to serialise
     */
    override def serialise(fact: Fact): AstElement = {
      val value: AstElement = fact match {
        case IntegerFact(_, _, i) => AstInteger(i)
        case StringFact(_, _, s) => AstString(s)
        case BooleanFact(_, _, b) => AstBoolean(b)
        case IntegerFactList(_, _, is) => AstList(is map serialise: _*)
        case StringFactList(_, _, ss) => AstList(ss map serialise: _*)
        case BooleanFactList(_, _, bs) => AstList(bs map serialise: _*)
      }

      val factType = fact match {
        case _: IntegerFact => "int"
        case _: StringFact => "string"
        case _: BooleanFact => "bool"
        case _: IntegerFactList => "int list"
        case _: StringFactList => "string list"
        case _: BooleanFactList => "bool list"
      }

      AstMap("id" -> AstInteger(fact.id.value),
             "name" -> AstString(fact.name),
             "type" -> AstString(factType),
             "initialValue" -> value)
    }

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): Fact = {
      val data = serialised.asInstanceOf[AstMap]
      val id = FactId(data("id").asInstanceOf[AstInteger].i)
      val name = data("name").asInstanceOf[AstString].s

      (data("type").asInstanceOf[AstString].s, data("initialValue")) match {
        case ("int", value) => IntegerFact(id, name, value.asInstanceOf[AstInteger].i)
        case ("string", value) => StringFact(id, name, value.asInstanceOf[AstString].s)
        case ("bool", value) => BooleanFact(id, name, value.asInstanceOf[AstBoolean].boolean)
        case ("int list", value) =>
          IntegerFactList(id, name,
                          value.asInstanceOf[AstList].toList map deserialise map (_.asInstanceOf[IntegerFact]))
        case ("string list", value) =>
          StringFactList(id, name,
                         value.asInstanceOf[AstList].toList map deserialise map (_.asInstanceOf[StringFact]))
        case ("bool list", value) =>
          BooleanFactList(id, name,
                          value.asInstanceOf[AstList].toList map deserialise map (_.asInstanceOf[BooleanFact]))
        case (factType, value) => throw DeserialisationException(s"Unknown fact type: $factType with value: $value")
      }
    }
  }

  /**
   * Typeclass instance for serialising story metadata
   */
  implicit object StoryMetadataSerialiser extends Serialisable[Story.Metadata] {
    /**
     * Returns the serialised representation of an object
     *
     * @param t The object to serialise
     */
    override def serialise(t: Metadata): AstElement = AstMap("comments" -> AstString(t.comments),
                                                             "readerStyle" -> ReaderStyleSerialiser.serialise(t.readerStyle),
                                                             "backDisabled" -> AstBoolean(t.isBackButtonDisabled),
                                                             "restartDisabled" -> AstBoolean(t.isRestartButtonDisabled))

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): Metadata = {
      val data = serialised.asInstanceOf[AstMap]
      val comments = data("comments").asInstanceOf[AstString].s
      val readerStyle = ReaderStyleSerialiser deserialise data("readerStyle")
      val backDisabled = data("backDisabled").asInstanceOf[AstBoolean].boolean
      val restartDisabled = data("restartDisabled").asInstanceOf[AstBoolean].boolean

      Story.Metadata(comments, readerStyle, backDisabled, restartDisabled)
    }
  }

  /**
   * Typeclass instance for serialising the reader style information
   */
  implicit object ReaderStyleSerialiser extends Serialisable[Narrative.ReaderStyle] {
    /**
     * Returns the serialised representation of an object
     *
     * @param t The object to serialise
     */
    override def serialise(t: ReaderStyle): AstElement = t match {
      case Narrative.ReaderStyle.Standard => AstString("standard")
      case Narrative.ReaderStyle.Fancy => AstString("fancy")
      case Narrative.ReaderStyle.Custom(file) => AstString(file)
    }

    /**
     * Returns an object given it's serialised representation
     *
     * @param serialised The serialised form of the object
     */
    override def deserialise(serialised: AstElement): ReaderStyle = serialised.asInstanceOf[AstString].s match {
      case "standard" => Narrative.ReaderStyle.Standard
      case "fancy" => Narrative.ReaderStyle.Fancy
      case file => Narrative.ReaderStyle.Custom(file)
    }
  }
}
