package io.flow.lint

import com.bryzek.apidoc.spec.v0.models._
import org.scalatest.{FunSpec, Matchers}

class VersionModelsSpec extends FunSpec with Matchers {

  val linter = Lint(Seq(linters.VersionModels))

  def buildService(fields: Seq[Field]): Service = {
    Services.Base.copy(
      models = Seq(
        Services.buildModel("user_version", fields)
      )
    )
  }

  val idField = Services.buildField("id", "string")
  val timestampField = Services.buildField("timestamp", "date-time-iso8601")
  val typeField = Services.buildField("type", "io.flow.common.v0.enums.change_type")
  val userField = Services.buildField("user", "user")

  it("no-op w/ correct fields") {
    linter.validate(buildService(Seq(idField, timestampField, typeField, userField))) should be(Nil)
  }

  it("error w/ extra field") {
    linter.validate(buildService(
      Seq(
        idField, timestampField, typeField, userField,
        Services.buildField("other")
      )
    )) should be(Seq("Model user_version: Must have exactly 4 fields: id, timestamp, type, user"))
  }

  it("error w/ missing field") {
    linter.validate(buildService(
      Seq(
        idField, timestampField, typeField, Services.buildField("other")
      )
    )) should be(Seq("Model user_version: Must have exactly 4 fields: id, timestamp, type, user"))
  }

  it("error w/ fields in incorrect order") {
    linter.validate(buildService(
      Seq(
        timestampField, typeField, userField, idField
      )
    )) should be(Seq("Model user_version: Must have exactly 4 fields: id, timestamp, type, user"))
  }

  it("error w/ invalid id type") {
    linter.validate(buildService(
      Seq(
        idField.copy(`type` = "long"), timestampField, typeField, userField
      )
    )) should be(Seq("Model user_version Field[id]: Must have type string and not long"))
  }
  
  it("error w/ invalid timestamp type") {
    linter.validate(buildService(
      Seq(
        idField, timestampField.copy(`type`="long"), typeField, userField
      )
    )) should be(Seq("Model user_version Field[timestamp]: Must have type date-time-iso8601 and not long"))
  }

  it("error w/ invalid type type") {
    linter.validate(buildService(
      Seq(
        idField, timestampField, typeField.copy(`type` = "long"), userField
      )
    )) should be(Seq("Model user_version Field[type]: Must have type io.flow.common.v0.enums.change_type and not long"))
  }

  it("error w/ invalid user type") {
    linter.validate(buildService(
      Seq(
        idField, timestampField, typeField, userField.copy(`type` = "long")
      )
    )) should be(Seq("Model user_version Field[user]: Must have type user and not long"))
  }
  
  it("error if field is not required") {
    linter.validate(buildService(Seq(idField.copy(required = false), timestampField, typeField, userField))) should be(
      Seq("Model user_version Field[id]: Must be required")
    )
  }

}