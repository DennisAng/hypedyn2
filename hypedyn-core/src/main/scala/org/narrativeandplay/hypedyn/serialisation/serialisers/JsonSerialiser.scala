package org.narrativeandplay.hypedyn.serialisation.serialisers

import org.json4s._
import org.json4s.native.JsonMethods._
import org.narrativeandplay.hypedyn.serialisation._

object JsonSerialiser {
  def serialise(data: AstElement) = pretty(render(AstElementToJValue(data)))
  def deserialise(data: String): AstElement = jValueToAstElement(parse(data))

  private def AstElementToJValue(elem: AstElement): JValue = elem match {
    case AstInteger(i) => JInt(i)
    case AstFloat(f) => JDouble(f)
    case AstString(s) => JString(s)
    case AstBoolean(b) => JBool(b)
    case AstDecimal(d) => JDecimal(d)
    case AstNothing => JNothing
    case AstNull => JNull
    case AstList(ls @ _*) => JArray((ls map AstElementToJValue).toList)
    case AstMap(hs @ _*) =>
      JObject((hs map { case (k, v) =>
        k -> AstElementToJValue(v)
      }).toList)
  }

  private def jValueToAstElement(jValue: JValue): AstElement = jValue match {
    case JInt(i) => AstInteger(i)
    case JDouble(f) => AstFloat(f)
    case JString(s) => AstString(s)
    case JBool(b) => AstBoolean(b)
    case JDecimal(d) => AstDecimal(d)
    case JNothing => AstNothing
    case JNull => AstNull
    case JArray(a) => AstList(a map jValueToAstElement: _*)
    case JObject(o) =>
      AstMap(o map { case (k, v) =>
        k -> jValueToAstElement(v)
      }: _*)
  }
}

