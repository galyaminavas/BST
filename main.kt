class Node<T: Comparable<T>> (var key: T) {
    var parent: Node<T>? = null
    var leftChild: Node<T>? = null
    var rightChild: Node<T>? = null
}

class BinarySearchTree<T: Comparable<T>>  {
    var root: Node<T>? = null

    fun add (key: T) {
        var current: Node<T>? = root
        val newNode = Node(key)
        if (root == null) {
            root = newNode
            return
        }
        while (current != null) {
            if (key > current.key) {
                if (current.rightChild == null) {
                    current.rightChild = newNode
                    newNode.parent = current
                    return
                }
                current = current.rightChild
            }
            else {
                if (current.leftChild == null) {
                    current.leftChild = newNode
                    newNode.parent = current
                    return
                }
                current = current.leftChild
            }
        }
    }

    fun search (key: T): Node<T>? {
        var current: Node<T>? = root
        if ((current?.key == key) || (current == null)) {
            return current
        }
        while (current != null) {
            if (key == current.key)
                return current
            if (key > current.key) {
                current = current.rightChild
            } else {
                current = current.leftChild
            }
        }
        return null
    }
}

fun main(args: Array<String>) {
    val tree = BinarySearchTree<Int>()
    tree.add(4)
    tree.add(6)
    tree.add(1)
    tree.add(9)
    tree.add(2)
    tree.add(5)

    var key = 1
    println("The tree root is ${tree.root?.key}")
    println()
    for (key in 1..10) {
        println("Result for searching by the key $key is ${tree.search(key)?.key}")
        if (tree.search(key) != null) {
            println("Parent: ${tree.search(key)?.parent?.key}")
            println("Left child: ${tree.search(key)?.leftChild?.key}")
            println("Right child: ${tree.search(key)?.rightChild?.key}")
        }
        println()
    }
}
