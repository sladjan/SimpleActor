package vezba

import akka.actor.{Actor, ActorRef, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by Sladjan Kuzmanovic on 24/03/2017.
  */

class Customer(productProducer: ActorRef) extends Actor {

  // this stuff is executed as the actor comes to life
  println(self.path.name + ", " + "Customer actor started")
  productProducer ! GiveAProduct(self)

  // behaviour
  override def receive: Receive = {
    case ProductDelivering(products) =>
      println(self.path.name + ", received " + products + " products")
      context.system.scheduler.scheduleOnce(2000.millisecond, productProducer, GiveAProduct(self))
  }

}

// this is the companion object - it describes how the actor needs to be created
object Customer {
  def props(productProducer: ActorRef) = Props(new Customer(productProducer))
}


