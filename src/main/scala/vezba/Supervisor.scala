package vezba

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, Props}

import scala.concurrent.duration._

/**
  * Created by Sladjan Kuzmanovic on 24/03/2017.
  */


class Supervisor extends Actor {

  val productProducer = context.actorOf(ProductProducer.props, "ProductProducer")
  val customer = context.actorOf(Customer.props(productProducer), "Customer")

  // behaviour for failing children
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5.seconds) {

    case _: OutOfProductException =>
      println(self.path.name + ", " + sender.path.name + " failed!, restarting it.")
      Restart
  }

  def receive = {
    case _ => println(self.path.name + ", " + "received something unexpected")
  }
}

object Supervisor {
  val props = Props[Supervisor]
}