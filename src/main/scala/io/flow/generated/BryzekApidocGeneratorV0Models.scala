/**
 * Generated by apidoc - http://www.apidoc.me
 * Service version: 0.11.48
 * apidoc:0.11.51 http://www.apidoc.me/bryzek/apidoc-generator/0.11.48/play_2_x_standalone_json
 */
package com.bryzek.apidoc.generator.v0.models {

  /**
   * An attribute represents a key/value pair that is optionally used to provide
   * additional instructions / data to the code generator. An example could be an
   * attribute to specify the root import path for a go client..
   */
  case class Attribute(
    name: String,
    value: String
  )

  case class Error(
    code: String,
    message: String
  )

  /**
   * Represents a source file
   */
  case class File(
    name: String,
    dir: _root_.scala.Option[String] = None,
    contents: String
  )

  /**
   * The generator metadata.
   */
  case class Generator(
    key: String,
    name: String,
    language: _root_.scala.Option[String] = None,
    description: _root_.scala.Option[String] = None,
    attributes: Seq[String] = Nil
  )

  case class Healthcheck(
    status: String
  )

  /**
   * The result of invoking a generator.
   */
  case class Invocation(
    @deprecated("Use files instead") source: String,
    files: Seq[com.bryzek.apidoc.generator.v0.models.File]
  )

  /**
   * The invocation form is the payload send to the code generators when requesting
   * generation of client code.
   */
  case class InvocationForm(
    service: com.bryzek.apidoc.spec.v0.models.Service,
    attributes: Seq[com.bryzek.apidoc.generator.v0.models.Attribute] = Nil,
    userAgent: _root_.scala.Option[String] = None
  )

}

package com.bryzek.apidoc.generator.v0.models {

  package object json {
    import play.api.libs.json.__
    import play.api.libs.json.JsString
    import play.api.libs.json.Writes
    import play.api.libs.functional.syntax._
    import com.bryzek.apidoc.common.v0.models.json._
    import com.bryzek.apidoc.generator.v0.models.json._
    import com.bryzek.apidoc.spec.v0.models.json._

    private[v0] implicit val jsonReadsUUID = __.read[String].map(java.util.UUID.fromString)

    private[v0] implicit val jsonWritesUUID = new Writes[java.util.UUID] {
      def writes(x: java.util.UUID) = JsString(x.toString)
    }

    private[v0] implicit val jsonReadsJodaDateTime = __.read[String].map { str =>
      import org.joda.time.format.ISODateTimeFormat.dateTimeParser
      dateTimeParser.parseDateTime(str)
    }

    private[v0] implicit val jsonWritesJodaDateTime = new Writes[org.joda.time.DateTime] {
      def writes(x: org.joda.time.DateTime) = {
        import org.joda.time.format.ISODateTimeFormat.dateTime
        val str = dateTime.print(x)
        JsString(str)
      }
    }

    implicit def jsonReadsApidocgeneratorAttribute: play.api.libs.json.Reads[Attribute] = {
      (
        (__ \ "name").read[String] and
        (__ \ "value").read[String]
      )(Attribute.apply _)
    }

    def jsObjectAttribute(obj: com.bryzek.apidoc.generator.v0.models.Attribute) = {
      play.api.libs.json.Json.obj(
        "name" -> play.api.libs.json.JsString(obj.name),
        "value" -> play.api.libs.json.JsString(obj.value)
      )
    }

    implicit def jsonWritesApidocgeneratorAttribute: play.api.libs.json.Writes[Attribute] = {
      new play.api.libs.json.Writes[com.bryzek.apidoc.generator.v0.models.Attribute] {
        def writes(obj: com.bryzek.apidoc.generator.v0.models.Attribute) = {
          jsObjectAttribute(obj)
        }
      }
    }

    implicit def jsonReadsApidocgeneratorError: play.api.libs.json.Reads[Error] = {
      (
        (__ \ "code").read[String] and
        (__ \ "message").read[String]
      )(Error.apply _)
    }

    def jsObjectError(obj: com.bryzek.apidoc.generator.v0.models.Error) = {
      play.api.libs.json.Json.obj(
        "code" -> play.api.libs.json.JsString(obj.code),
        "message" -> play.api.libs.json.JsString(obj.message)
      )
    }

    implicit def jsonWritesApidocgeneratorError: play.api.libs.json.Writes[Error] = {
      new play.api.libs.json.Writes[com.bryzek.apidoc.generator.v0.models.Error] {
        def writes(obj: com.bryzek.apidoc.generator.v0.models.Error) = {
          jsObjectError(obj)
        }
      }
    }

    implicit def jsonReadsApidocgeneratorFile: play.api.libs.json.Reads[File] = {
      (
        (__ \ "name").read[String] and
        (__ \ "dir").readNullable[String] and
        (__ \ "contents").read[String]
      )(File.apply _)
    }

    def jsObjectFile(obj: com.bryzek.apidoc.generator.v0.models.File) = {
      play.api.libs.json.Json.obj(
        "name" -> play.api.libs.json.JsString(obj.name),
        "contents" -> play.api.libs.json.JsString(obj.contents)
      ) ++ (obj.dir match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("dir" -> play.api.libs.json.JsString(x))
      })
    }

    implicit def jsonWritesApidocgeneratorFile: play.api.libs.json.Writes[File] = {
      new play.api.libs.json.Writes[com.bryzek.apidoc.generator.v0.models.File] {
        def writes(obj: com.bryzek.apidoc.generator.v0.models.File) = {
          jsObjectFile(obj)
        }
      }
    }

    implicit def jsonReadsApidocgeneratorGenerator: play.api.libs.json.Reads[Generator] = {
      (
        (__ \ "key").read[String] and
        (__ \ "name").read[String] and
        (__ \ "language").readNullable[String] and
        (__ \ "description").readNullable[String] and
        (__ \ "attributes").read[Seq[String]]
      )(Generator.apply _)
    }

    def jsObjectGenerator(obj: com.bryzek.apidoc.generator.v0.models.Generator) = {
      play.api.libs.json.Json.obj(
        "key" -> play.api.libs.json.JsString(obj.key),
        "name" -> play.api.libs.json.JsString(obj.name),
        "attributes" -> play.api.libs.json.Json.toJson(obj.attributes)
      ) ++ (obj.language match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("language" -> play.api.libs.json.JsString(x))
      }) ++
      (obj.description match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("description" -> play.api.libs.json.JsString(x))
      })
    }

    implicit def jsonWritesApidocgeneratorGenerator: play.api.libs.json.Writes[Generator] = {
      new play.api.libs.json.Writes[com.bryzek.apidoc.generator.v0.models.Generator] {
        def writes(obj: com.bryzek.apidoc.generator.v0.models.Generator) = {
          jsObjectGenerator(obj)
        }
      }
    }

    implicit def jsonReadsApidocgeneratorHealthcheck: play.api.libs.json.Reads[Healthcheck] = {
      (__ \ "status").read[String].map { x => new Healthcheck(status = x) }
    }

    def jsObjectHealthcheck(obj: com.bryzek.apidoc.generator.v0.models.Healthcheck) = {
      play.api.libs.json.Json.obj(
        "status" -> play.api.libs.json.JsString(obj.status)
      )
    }

    implicit def jsonWritesApidocgeneratorHealthcheck: play.api.libs.json.Writes[Healthcheck] = {
      new play.api.libs.json.Writes[com.bryzek.apidoc.generator.v0.models.Healthcheck] {
        def writes(obj: com.bryzek.apidoc.generator.v0.models.Healthcheck) = {
          jsObjectHealthcheck(obj)
        }
      }
    }

    implicit def jsonReadsApidocgeneratorInvocation: play.api.libs.json.Reads[Invocation] = {
      (
        (__ \ "source").read[String] and
        (__ \ "files").read[Seq[com.bryzek.apidoc.generator.v0.models.File]]
      )(Invocation.apply _)
    }

    def jsObjectInvocation(obj: com.bryzek.apidoc.generator.v0.models.Invocation) = {
      play.api.libs.json.Json.obj(
        "source" -> play.api.libs.json.JsString(obj.source),
        "files" -> play.api.libs.json.Json.toJson(obj.files)
      )
    }

    implicit def jsonWritesApidocgeneratorInvocation: play.api.libs.json.Writes[Invocation] = {
      new play.api.libs.json.Writes[com.bryzek.apidoc.generator.v0.models.Invocation] {
        def writes(obj: com.bryzek.apidoc.generator.v0.models.Invocation) = {
          jsObjectInvocation(obj)
        }
      }
    }

    implicit def jsonReadsApidocgeneratorInvocationForm: play.api.libs.json.Reads[InvocationForm] = {
      (
        (__ \ "service").read[com.bryzek.apidoc.spec.v0.models.Service] and
        (__ \ "attributes").read[Seq[com.bryzek.apidoc.generator.v0.models.Attribute]] and
        (__ \ "user_agent").readNullable[String]
      )(InvocationForm.apply _)
    }

    def jsObjectInvocationForm(obj: com.bryzek.apidoc.generator.v0.models.InvocationForm) = {
      play.api.libs.json.Json.obj(
        "service" -> com.bryzek.apidoc.spec.v0.models.json.jsObjectService(obj.service),
        "attributes" -> play.api.libs.json.Json.toJson(obj.attributes)
      ) ++ (obj.userAgent match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("user_agent" -> play.api.libs.json.JsString(x))
      })
    }

    implicit def jsonWritesApidocgeneratorInvocationForm: play.api.libs.json.Writes[InvocationForm] = {
      new play.api.libs.json.Writes[com.bryzek.apidoc.generator.v0.models.InvocationForm] {
        def writes(obj: com.bryzek.apidoc.generator.v0.models.InvocationForm) = {
          jsObjectInvocationForm(obj)
        }
      }
    }
  }
}

