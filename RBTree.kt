package tree.rbtree

import tree.*

class RBTree<Key : Comparable<Key>, Value>: Tree<Key, Value> {
    var root: RBNode<Key, Value>? = null
    internal var nodesCount: Int = 0

    fun rotateLeft(node: RBNode<Key, Value>) {
        var rightson: RBNode<Key, Value> = node.rightChild!! //right son
        node.rightChild = rightson.leftChild
        rightson.leftChild?.parent = node //son's left subtree

        if (node.parent == null) //if node is a root
            root = rightson
        rightson.parent = node.parent

        rightson.leftChild = node //son's new left subtree
        node.parent = rightson

        if (rightson.parent?.leftChild == node)
            rightson.parent?.leftChild = rightson
        else
            rightson.parent?.rightChild = rightson
    }

    fun rotateRight(node: RBNode<Key, Value>) {
        var leftson: RBNode<Key, Value> = node.leftChild!!
        node.leftChild = leftson.rightChild
        leftson.rightChild?.parent = node

        if (node.parent == null)
            root = leftson
        leftson.parent = node.parent

        leftson.rightChild = node
        node.parent = leftson

        if (leftson.parent?.leftChild == node)
            leftson.parent?.leftChild = leftson
        else
            leftson.parent?.rightChild = leftson
    }

    fun balance(node: RBNode<Key, Value>) {
        if (node == root) {
            node.colour = RBNode.Colour.Black
            return
        }

        if (node.parent?.colour == RBNode.Colour.Black)
            return

        if (node.uncle()?.colour == RBNode.Colour.Red) {
            //parent & uncle are red -> change colours
            node.parent!!.colour = RBNode.Colour.Black
            node.uncle()?.colour = RBNode.Colour.Black
            node.grandparent()?.colour = RBNode.Colour.Red
            balance(node.grandparent()!!)
        }
        else {
            //father is red, uncle is black
            if (node.grandparent()?.leftChild == node.parent) {
                //if parent of new node - left child
                if (node.parent!!.leftChild == node) {
                    //if new node is a left child
                    node.parent!!.colour = RBNode.Colour.Black
                    node.parent!!.parent?.colour = RBNode.Colour.Red
                    rotateRight(node.parent!!.parent!!)
                }
                else {
                    //if new node is a right child
                    rotateLeft(node.parent!!)
                    node.colour = RBNode.Colour.Black
                    node.parent?.colour = RBNode.Colour.Red
                    rotateRight(node.parent!!)
                }
            }
            else {
                //if parent of new node - right child
                if (node.parent!!.rightChild == node) {
                    //if new node is a right child
                    node.parent!!.colour = RBNode.Colour.Black
                    node.parent!!.parent?.colour = RBNode.Colour.Red
                    rotateLeft(node.parent!!.parent!!)
                }
                else {
                    //if new node is a left child
                    rotateRight(node.parent!!)
                    node.colour = RBNode.Colour.Black
                    node.parent?.colour = RBNode.Colour.Red
                    rotateLeft(node.parent!!)
                }
            }
        }
    }

    override fun search(key: Key): RBNode<Key, Value>? {
        var current: RBNode<Key, Value>? = root
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

    override fun add(key: Key, value: Value) {
        //if node with this key already exists - do not add new one
        if (search(key) != null)
            return
        var current: RBNode<Key, Value>? = root
        val newRBNode = RBNode(key, value)
        newRBNode.colour = RBNode.Colour.Red
        if (root == null) {
            root = newRBNode
            nodesCount++
            return balance(newRBNode)
        }
        while (current != null) {
            if (key > current.key) {
                if (current.rightChild == null) {
                    current.rightChild = newRBNode
                    newRBNode.parent = current
                    nodesCount++
                    return balance(newRBNode)
                }
                current = current.rightChild
            } else {
                if (current.leftChild == null) {
                    current.leftChild = newRBNode
                    newRBNode.parent = current
                    nodesCount++
                    return balance(newRBNode)
                }
                current = current.leftChild
            }
        }
        //return balance(newRBNode)
    }

    override fun delete(key: Key) {
        //
    }

    fun height(node: RBNode<Key, Value>?): Int {
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

    fun printLevel(root: RBNode<Key, Value>?, level: Int) {
        if (root == null)
            return
        if (level == 1) {
            if (root.colour == RBNode.Colour.Black)
                print("${root.value} ")
            else
                print("[${root.value}] ")
        }
        else {
                printLevel(root.leftChild, level - 1)
                printLevel(root.rightChild, level - 1)
            }
    }

    fun printLevelOrderTraversal() {
        val h = height(root)
        for (i in 1..h) {
            printLevel(root, i)
            println()
        }
    }
}