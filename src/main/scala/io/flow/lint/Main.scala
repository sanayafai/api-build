package io.flow.lint

object Main extends App {

  private[this] val linter = Lint()
  private[this] var errors = scala.collection.mutable.Map[String, Seq[String]]()

  private[this] def addError(organization: String, application: String, error: String) {
    val key = s"$organization/$application"
    errors.get(key) match {
      case None => {
        errors.put(key, Seq(error))
      }
      case Some(existing) => {
        errors.put(key, existing ++ Seq(error))
      }
    }
  }

  ApidocConfig.load() match {
    case Left(error) => println(s"** Error loading apidoc config: $error")
    case Right(config) => {
      Downloader.withClient(config) { dl =>

        import scala.concurrent.ExecutionContext.Implicits.global

        args.foreach { name =>
          val (organization, application, version) = name.split("/").map(_.trim).toList match {
            case org :: app :: Nil => (org, app, "latest")
            case org :: app :: version :: Nil => (org, app, version)
            case _ => {
              sys.error(s"Invalid name[$name] - expected organization/application (e.g. flow/user)")
            }
          }

          println("")
          println(s"$name")
          print(s"  Downloading...")
          dl.service(organization, application, version) match {
            case Left(error) => {
              addError(organization, application, error)
              println("\n  ** ERROR: " + error)
            }
            case Right(service) => {
              print("  Done\n  Starting Linter... ")
              linter.validate(service) match {
                case Nil => println("\n  Valid!")
                case errors => {
                  errors.size match {
                    case 1 => println(" 1 error:")
                    case n => println(s" $n errors:")
                  }
                  errors.sorted.foreach { error =>
                    addError(organization, application, error)
                    println(s"    - $error")
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  if (!errors.isEmpty) {
    println("")
    println("==================================================")
    errors.size match {
      case 1 => println(s"SUMMARY: 1 API HAD ERRORS")
      case n => println(s"SUMMARY: $n APIs HAD ERRORS")
    }
    println("==================================================")
    errors.keys.toSeq.sorted foreach { app =>
      println(app)
      errors(app).foreach { err =>
        println(s"  - $err")
      }
      println("")
    }
  }


  System.exit(errors.size)
}