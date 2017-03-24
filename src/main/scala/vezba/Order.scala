package vezba

import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by Sladjan Kuzmanovic on 24/03/2017.
  */
class Order(coffeeMachine: ActorRef) extends Actor {

  // this stuff is executed as the actor comes to life
  println(self.path.name + ", " + "Order actor started")

  // behaviour
  override def receive: Receive = {
    case OrderNewProducts(cups) =>
      println(self.path.name + ", ordered " + cups + " cup(s) for coffee")
      coffeeMachine ! OrderNewProducts(cups)
  }

}

// this is the companion object - it describes how the actor needs to be created
object Order {
  def props(coffeeMachine: ActorRef) = Props(new Order(coffeeMachine))
}


