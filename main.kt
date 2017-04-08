import tree.bstree.*



fun main(args: Array<String>) {
    val tree = BSTree<Int, String>()
    tree.add(50, "50")
    tree.add(30, "30")
    tree.add(20, "20")
    tree.add(40, "40")
    tree.add(70, "70")
    tree.add(60, "60")
    tree.add(80, "80")
    tree.printLevelOrderTraversal()
    tree.delete(40)
    tree.printLevelOrderTraversal()
    println(tree.search(30)?.rightChild)
    println(tree.search(40))

}

/*import java.util.Scanner

import tree.rbtree.*

fun main(args: Array<String>) {
    val tree = RBTree<Int, String>()

    val input = Scanner(System.`in`)
    var switcher: String? = ""

    menu@
    while (switcher != "q") {
        println("What do you want from me?")
        println("a - to add new node")
        println("b - to search node by key")
        println("p - to print tree")
        println("i - information about the tree")
        println("q - to exit")
        switcher = readLine()

        when (switcher) {
            "a" -> {
                println("\nYou now can add new nodes in the tree. \nRemember to type data in format: \n'key' 'value' \n(keys must be Integer type)")
                println("If you want to go back to the menu, enter 'm'")

                while (input.hasNext()) {
                    if (input.hasNextInt()) {
                        var keybuff: Int = input.nextInt()
                        var valuebuff: String = input.next()
                        tree.add(keybuff, valuebuff)
                    }
                    else {
                        var buff: String = input.next()
                        if (buff == "m") {
                            continue@menu
                        }
                        else {
                            println("Sorry, I don't understand you\n")
                            continue@menu
                        }
                    }
                }
            }
            "b" -> {
                print("\nEnter the key you want to find: ") //error if not Integer
                var keybuff: Int = input.nextInt()
                if (tree.search(keybuff) != null) {
                    println("Value for the key: ${tree.search(keybuff)?.value}")
                    println("Colour: ${tree.search(keybuff)?.colour}")
                    println("Parent: ${tree.search(keybuff)?.parent?.value}")
                    println("Left child: ${tree.search(keybuff)?.leftChild?.value}")
                    println("Right child: ${tree.search(keybuff)?.rightChild?.value}\n")
                }
                else {
                    println("There is no node with this key\n")
                }
                continue@menu
            }
            "p" -> {
                //[x] means that x is read
                if (tree.root != null) {
                    println("\nThe tree looks like:")
                    tree.printLevelOrderTraversal()
                    println()
                }
                else {
                    println("\nThe tree is empty\n")
                }
                continue@menu
            }
            "i" -> {
                println("\nThe tree root is ${tree.root?.value}")
                println("There are ${tree.nodesCount} nodes in the tree")
                println("The tree height is ${tree.height(tree.root)}\n")
                continue@menu
            }
            "q" -> {
                println("Our work here is done.")
            }
            else -> println("Sorry, I don't understand you\n")
        }
    }
}*/