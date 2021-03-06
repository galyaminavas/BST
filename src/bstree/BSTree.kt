package bstree

import tree.*

class BSTree<Key : Comparable<Key>, Value>: Tree<Key, Value> {

    var root: BSNode<Key, Value>? = null

    override fun add(key: Key, value: Value) {
        //if node with this key already exists - do not add new one
        if (this.search(key) != null)
            return
        var current: BSNode<Key, Value>? = root
        val newNode = BSNode(key, value)
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
    
    override fun search(key: Key): BSNode<Key, Value>? {
        var current: BSNode<Key, Value>? = root
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

    private fun min(x: BSNode<Key, Value>): BSNode<Key, Value> {
        if (x.leftChild == null)
            return x
        return min(x.leftChild!!)
    }

    private fun recursiveRemove(root: BSNode<Key, Value>?, key: Key): BSNode<Key, Value>? {
        if (root == null)
            return null
        if (root.key > key)
            root.leftChild = recursiveRemove(root.leftChild, key)
        else if (root.key < key)
            root.rightChild = recursiveRemove(root.rightChild, key)
        else {
            if (root.leftChild == null)
                return root.rightChild
            else if (root.rightChild == null)
                return root.leftChild
            //if node has both children
            var temp: BSNode<Key, Value> = min(root.rightChild!!)
            root.key = temp.key
            root.value = temp.value
            root.rightChild = recursiveRemove(root.rightChild, root.key)
            }
            return root
    }

    override fun delete(key: Key) {
        //if there is no such key
        if (search(key) == null)
            return
        this.root = recursiveRemove(this.root, key)
    }

    internal fun height(node: BSNode<Key, Value>?): Int {
        if (node == null)
            return 0
        else {
            var leftHeight = height(node.leftChild)
            var rightHeight = height(node.rightChild)
            if (leftHeight > rightHeight)
                return leftHeight + 1
            else
                return rightHeight + 1
        }
    }

    internal fun printLevel(root: BSNode<Key, Value>?, level: Int) {
        if (root == null)
            return
        if (level == 1) {
            print("${root.value} ")
        }
        else {
            printLevel(root.leftChild, level - 1)
            printLevel(root.rightChild, level - 1)
        }
    }

    internal fun printLevelOrderTraversal() {
        val h = height(root)
        for (i in 1..h) {
            printLevel(root, i)
            println()
        }
    }

    private fun first(): BSNode<Key, Value>? {
        var currNode = this.root
        while (currNode?.leftChild != null)
            currNode = currNode.leftChild
        return currNode
    }

    private fun nextElement(node: BSNode<Key, Value>?): BSNode<Key, Value>? {
        var currNode: BSNode<Key, Value>? = node ?: return null
        if (currNode?.rightChild != null) {
            currNode = currNode.rightChild
            while (currNode?.leftChild != null)
                currNode = currNode.leftChild
        }
        else {
            while (currNode != null && currNode.key <= node.key)
                currNode = currNode.parent
        }
        return currNode
    }

    override fun iterator(): Iterator<BSNode<Key, Value>> = object: Iterator<BSNode<Key, Value>> {
        var node = first()

        override fun next(): BSNode<Key, Value> {
            val result = node
            node = nextElement(node)
            return result!!
        }

        override fun hasNext(): Boolean {
            return node != null
        }
    }

    fun printElements() {
        for (it in this)
            print("${it.value} ")
    }
}