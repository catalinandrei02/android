var water = 400
var milk = 540
var beans = 120
var cups = 9
var money = 550
fun main() {
    while (true) {
        print("Write action (buy, fill, take, remaining, exit): ")
        val a = readln()
        when {
            a == "buy" -> buy()
            a == "fill" -> fill()
            a == "take" -> take()
            a == "remaining" -> remaining()
            a == "exit" -> break
        }
    }

}
fun remaining() {
    println("")
    println("The coffe machine has:")
    println("${water} ml of water")
    println("${milk} ml of milk")
    println("${beans} g of beans")
    println("${cups} disposable cups")
    println("$${money} of money")
    println("")
}
fun buy() {
    print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino: ")
    val b = readln()
    when {
        b == "back" -> {}
        b.toInt() == 1 -> {
            if (water - 250 >= 0 && beans - 16 >= 0  && cups >= 1) {
                water -= 250
                beans -= 16
                money += 4
                cups--
                println("I have enough resources, making you a coffee!")
            }
            when {
                water - 250 < 0 -> println("Sorry, not enough Water!")
                beans - 16 < 0 -> println("Sorry, not enough coffee Beans!")
                cups < 1 -> println("Sorry, not enough Cups!")
            }
        }
        b.toInt() == 2 -> {
            if (water - 350 >= 0 && milk - 75 >= 0 && beans - 20 >= 0  && cups >= 1) {
                water -= 350
                milk -= 75
                beans -= 20
                money += 7
                cups--
                println("I have enough resources, making you a coffee!")
            }
            when {
                water - 350 < 0 -> println("Sorry, not enough Water!")
                milk - 75 < 0 -> println("Sorry, not enough Milk!")
                beans - 20 < 0 -> println("Sorry, not enough coffee Beans!")
                cups < 1 -> println("Sorry, not enough Cups!")
            }

        }
        b.toInt() == 3 -> {
            if (water - 200 >= 0 && milk - 100 >= 0 && beans - 10 >= 0 && cups >= 1) {
                water -= 200
                milk -= 100
                beans -= 10
                money += 6
                cups--
                println("I have enough resources, making you a coffee!")
            }
            when {
                water - 200 < 0 -> println("Sorry, not enough Water!")
                milk - 100 < 0 -> println("Sorry, not enough Milk!")
                beans - 10 < 0 -> println("Sorry, not enough coffee Beans!")
                cups < 1 -> println("Sorry, not enough Cups!")
            }
        }
    }
}
fun fill() {
    print("Write how many ml of water do you want to add: ")
    water += readln().toInt()
    print("Write how many ml of milk do you want to add: ")
    milk += readln().toInt()
    print("Write how many grams of coffee beans do you want to add: ")
    beans += readln().toInt()
    print("Write how many disposable cups of coffee do you want to add: ")
    cups += readln().toInt()
}
fun take() {
    println("I gave you $${money}")
    money = 0
}