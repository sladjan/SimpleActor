package vezba

import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by Sladjan Kuzmanovic on 24/03/2017.
  */


// these are the messages that can be exchanged
case class GiveAProduct(sender: ActorRef)
//case  class OrderActor(order: ActorRef)

case  class ProductDelivering(numberOfProduct: Int)
case  class OrderNewProducts(numberOfProduct:Int)

class ProductProducer extends Actor {

  // internal state
  var productReserved = 4
  var noOfTries = 3
  val orderActor = context.actorOf(Order.props(self), "Order")


  println(self.path.name + ", started. caffeine reserve: " + productReserved)

  // behaviour
  override def receive: Receive = {

    case GiveAProduct(user) =>
      println(self.path.name + ", received GiveAProduct")
      if (productReserved == 0) {
        println(self.path.name + ", out of product!")
        throw OutOfProductException()
      }

      // send nicotine back to our sender
      productReserved -= 1
      println(self.path.name + ", remaining products:: " + productReserved)
      if (productReserved == 0)
      {
        noOfTries -=1
        // every fourth set of cups will fail, first three will succeed ordering new cups
        if(noOfTries > 0)
          orderActor ! OrderNewProducts(5)
        else
          noOfTries = 3
      }
      user ! ProductDelivering(1)
    case OrderNewProducts(cups) =>
      println(self.path.name + ", Ordered new cups")
      this.productReserved +=  cups
      println(self.path.name + ", No of cups:: " + this.productReserved)

  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println(self.path.name + ", about to restart")
    super.preRestart(reason, message)
    message.foreach(self ! _)
  }

  override def postRestart(reason: Throwable) = {
    println(self.path.name + ", ...restart completed, my product reserve is: " + productReserved)
    super.postRestart(reason)
  }

}

object ProductProducer {
  def props = Props[ProductProducer]
}