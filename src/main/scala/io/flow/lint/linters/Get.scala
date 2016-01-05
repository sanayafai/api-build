package io.flow.lint.linters

import io.flow.lint.Linter
import com.bryzek.apidoc.spec.v0.models.{Method, Operation, Parameter, ParameterLocation, Resource, Response, Service}
import com.bryzek.apidoc.spec.v0.models.{ResponseCodeInt, ResponseCodeOption, ResponseCodeUndefinedType}

/**
  * Enforces that each resource has a top level GET method where:
  * 
  * a. All parameters are optional
  * b. First param named "id" with type "[string]"
  * c. last three parameters named limit, offset, sort
  * d. limit is long w/ default of 25, minimum of 1, maximum of 100
  * d. offset is long w/ default of 0, minimum of 0, no maximum
  * e. sort is a string with a default specified
  */
case object Get extends Linter with Helpers {

  private[this] val RequiredParameters = Seq("id", "limit", "offset", "sort")

  override def validate(service: Service): Seq[String] = {
    nonHealthcheckResources(service).map(validateResource(_)).flatten
  }

  def validateResource(resource: Resource): Seq[String] = {
    resource.operations.
      filter(_.method == Method.Get).
      filter(returnsArray(_)).
      flatMap { validateOperation(resource, _) }
  }

  def queryParameters(operation: Operation): Seq[Parameter] = {
    operation.parameters.filter(_.location == ParameterLocation.Query)
  }

  def validateOperation(resource: Resource, operation: Operation): Seq[String] = {
    val requiredErrors = queryParameters(operation).filter(p => p.required && p.default.isEmpty) match {
      case Nil => Nil
      case params => params.map { p =>
        RequiredParameters.contains(p.name) match {
          case true => error(resource, operation, s"Parameter[${p.name}] must be optional")
          case false => error(resource, operation, s"Parameter[${p.name}] must be optional or must have a default")
        }
      }
    }

    val paramNames = queryParameters(operation).map(_.name).filter(name => RequiredParameters.contains(name))
    val missingRequiredParams = RequiredParameters.filter(n => !paramNames.contains(n)) match {
      case Nil => Nil
      case missing => {
        val noun = missing.size match {
          case 1 => "parameter"
          case _ => "parameters"
        }
        Seq(error(resource, operation, s"Missing $noun: " + missing.mkString(", ")))
      }
    }

    val paramErrors = Seq(
      queryParameters(operation).find(_.name == "id").map( p =>
        validateParameter(resource, operation, p, "[string]", maximum = Some(25))
      ),
      queryParameters(operation).find(_.name == "limit").map( p =>
        validateParameter(resource, operation, p, "long", default = Some("25"), minimum = Some(1), maximum = Some(100))
      ),
      queryParameters(operation).find(_.name == "offset").map( p =>
        validateParameter(resource, operation, p, "long", default = Some("0"), minimum = Some(0), maximum = None)
      ),
      queryParameters(operation).find(_.name == "sort").map( p =>
        validateParameter(resource, operation, p, "string", hasDefault = Some(true))
      )
    ).flatten.flatten

    val positionErrors = missingRequiredParams match {
      case Nil => validateParameterPositions(resource, operation)
      case errors => Nil
    }

    // TODO: Validate responses

    missingRequiredParams ++ requiredErrors ++ paramErrors ++ positionErrors
  }

  // validate id if present is in first position, and the parameter
  // list ends with limit, offset, sort
  private[this] def validateParameterPositions(
    resource: Resource,
    operation: Operation
  ): Seq[String] = {
    val names = queryParameters(operation).map(_.name)
    val lastThree = names.reverse.take(3).reverse

    Seq(
      names.head == "id" match {
        case true => Nil
        case false => Seq(error(resource, operation, s"Parameter[id] must be the first parameter"))
      },
      lastThree match {
        case "limit" :: "offset" :: "sort" :: Nil => Nil
        case _ => {
          Seq(error(resource, operation, s"Last three parameters must be limit, offset, sort and not " + lastThree.mkString(", ")))
        }
      }
    ).flatten
  }

  def validateParameter(
    resource: Resource,
    operation: Operation,
    param: Parameter,
    datatype: String,
    hasDefault: Option[Boolean] = None,
    default: Option[String] = None,
    minimum: Option[Long] = None,
    maximum: Option[Long] = None
  ): Seq[String] = {
    val typeErrors = param.`type` == datatype match {
      case true => Nil
      case false => {
        Seq(error(resource, operation, s"parameter[${param.name}] must be of type ${datatype} and not ${param.`type`}"))
      }
    }

    val defaultErrors = hasDefault match {
      case None => {
        compare(resource, operation, param, "default", param.default, default)
      }
      case Some(value) => {
        value match {
          case true => {
            param.default match {
              case None => Seq(error(resource, operation, s"parameter[${param.name}] must have a default"))
              case Some(_) => Nil
            }
          }
          case false => {
            param.default match {
              case None => Nil
              case Some(v) => Seq(error(resource, operation, s"parameter[${param.name}] must not have a default. Current value is $v"))
            }
          }
        }
      }
    }

    val minimumErrors = compare(resource, operation, param, "minimum", param.minimum, minimum)
    val maximumErrors = compare(resource, operation, param, "maximum", param.maximum, maximum)

    typeErrors ++ defaultErrors ++ minimumErrors ++ maximumErrors
  }

  private[this] def compare(
    resource: Resource,
    operation: Operation,
    param: Parameter,
    desc: String,
    actual: Option[Any],
    expected: Option[Any]
  ): Seq[String] = {
    expected match {
      case None => {
        actual match {
          case None => Nil
          case Some(a) => Seq(error(resource, operation, s"parameter[${param.name}] $desc must not be specified. Current value is $a"))
        }
      }
      case Some(value) => {
        actual match {
          case None => Seq(error(resource, operation, s"parameter[${param.name}] $desc is missing. Expected $value"))
          case Some(a) => {
            a == value match {
              case true => Nil
              case false => Seq(error(resource, operation, s"parameter[${param.name}] $desc must be $value and not $a"))
            }
          }
        }
      }
    }
  }

}
