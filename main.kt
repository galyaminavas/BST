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

    fun height (root: Node<T>?): Int {
        if (root == null)
            return 0
        else {
            var leftHeight = height(root.leftChild)
            var rightHeight = height(root.rightChild)
            if (leftHeight > rightHeight)
                return leftHeight + 1
            else
                return rightHeight + 1
        }
    }

    fun printLevel (root: Node<T>?, level: Int) {
        if (root == null)
            return
        if (level == 1)
            print("${root.key} ")
        else {
            printLevel(root.leftChild, level - 1)
            printLevel(root.rightChild, level - 1)
        }
    }

    fun printLevelOrderTraversal () {
        val h = height(root)
        
        for (i in 1..h) {
            printLevel(root, i)
            println()
        }
    }
}

fun main(args: Array<String>) {
    val tree = BinarySearchTree<Int>()
    tree.add(7)
    tree.add(3)
    tree.add(4)
    tree.add(5)
    tree.add(9)
    tree.add(10)

    println("The tree root is ${tree.root?.key}")
    println()
    println("The tree height is ${tree.height(tree.root)}")
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

    println("The tree looks like:")
    tree.printLevelOrderTraversal()

}
