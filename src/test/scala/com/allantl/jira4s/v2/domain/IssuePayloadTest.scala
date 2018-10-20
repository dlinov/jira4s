package com.allantl.jira4s.v2.domain

import io.circe.Json
import org.specs2.mutable.Specification
import io.circe.syntax._
import io.circe.parser._

class IssuePayloadTest extends Specification {

  "Issue Payload" should {
    "be encoded to json successfully" in {
      val json = IssuePayload(Field("summary", "hello world")).asJson
      val expected = Json.obj(
        "fields" -> Json.obj(("summary", Json.fromString("hello world"))),
        "properties" -> Json.Null,
        "update" -> Json.Null,
      )

      (json.noSpaces must be).equalTo(expected.noSpaces)
    }

    "be encoded with all values successfully" in {
      val json = IssuePayload(
        Seq(Field("summary", "hello world")),
        Some(Seq(EntityProperty("issue", Json.fromString("10000")))),
        Some(Json.fromString("workflow"))
      ).asJson

      val expected = Json.obj(
        "fields" -> Json.obj(("summary", Json.fromString("hello world"))),
        "properties" -> Json.arr(
          Json.obj(
            ("key", Json.fromString("issue")),
            ("value", Json.fromString("10000"))
          )
        ),
        "update" -> Json.fromString("workflow")
      )

      (json.noSpaces must be).equalTo(expected.noSpaces)
    }

    "be decoded from json successfully" in {
      val rawJson =
        """{"update":{"worklog":[{"add":{"started":"2011-07-05T11:05:00.000+0000","timeSpent":"60m"}}]},"fields":{"project":{"id":"10000"},"summary":"something's wrong","issuetype":{"id":"10000"}},"properties":[{"key":"key1","value":"A value"},{"key":"key2","value":{"o":"some object"}}]}""".stripMargin

      val payload = parse(rawJson).map(_.as[IssuePayload])
      payload must beRight
    }
  }

}
