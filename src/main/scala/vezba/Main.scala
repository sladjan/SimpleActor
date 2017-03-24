package vezba

import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by Sladjan Kuzmanovic on 24/03/2017.
*/


object Main extends App{

  println("Main starting")

  // instantiate actor system
  val as = ActorSystem("Exercise")
  val supervisor = as.actorOf(Supervisor.props, "supervisor")

  // wait for signal to terminate
  Await.result(as.whenTerminated,  Duration.Inf)
  as.terminate()
}